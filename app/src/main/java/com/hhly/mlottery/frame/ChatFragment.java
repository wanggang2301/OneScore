package com.hhly.mlottery.frame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.CounselCommentActivity;
import com.hhly.mlottery.activity.LoginActivity;
import com.hhly.mlottery.adapter.CounselComentLvAdapter;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.view.PullUpRefreshListView;
import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.entity.Comment;
import com.sohu.cyan.android.sdk.exception.CyanException;
import com.sohu.cyan.android.sdk.http.CyanRequestListener;
import com.sohu.cyan.android.sdk.http.response.SubmitResp;
import com.sohu.cyan.android.sdk.http.response.TopicCommentsResp;
import com.sohu.cyan.android.sdk.http.response.TopicLoadResp;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * @author hhly204
 * @ClassName: ChatFragment    注意  装此碎片的容器，只有评论框时必须是scrollview 有评论框和评论列表时  必须是RelativeLayout
 * @Description: 全部评论页面
 * @date 2016-6-3
 */
public class ChatFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, CyanRequestListener<SubmitResp> {
    private View mView;
    private TextView mCommentCount;//评论数
    private EditText mEditText;//输入评论
    private TextView mSend;//发送评论
    private TextView mNoData;//暂无评论
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PullUpRefreshListView mListView;
    private TextView mLoadMore;//加载更多
    private ProgressBar mProgressBar;//上拉加载的进度条
    private ArrayList<Comment> mCommentArrayList;//最新评论数据
    private CyanSdk sdk;
    private String souceid;//被评论的资源的唯一标识
    private String title;//文章的标题
    private long topicid;//畅言分配的文章ID，通过loadTopic接口获取
    public static int cmt_sum;//评论总数
    private int mCurrentPager = 1;//页数
    private boolean isRequestFinish = true;//是否加载评论结束
    private boolean issubmitFinish = true;//是否提交评论结束
    private boolean isShowComment = false;//是否需要显示评论
    private CounselComentLvAdapter mAdapter;
    private String model;
    private int def = 0;
    private boolean isHiddenCommentCount = false;//是否显示评论数  true  永不显示  false 打开键盘隐藏  隐藏键盘显示
    private Context mContext;
    private onKeyBoardHiddenLisener mKeyBoardHiddenLisener;
    private onPullDownRefreshLisener mOnPullDownRefreshLisener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chat, null);
        initData();
        initView();
        initScrollView();//解决adjustresize和透明状态栏的冲突
        initListView();
        pullUpLoad();//上拉加载更多
        return mView;
    }

    private void initData() {
        mContext = getActivity() == null ? MyApp.getContext() : getActivity();
        sdk = CyanSdk.getInstance(mContext);
        Bundle bundle = getArguments();
        if (bundle != null) {
            souceid = bundle.getString(CyUtils.INTENT_PARAMS_SID);
            L.i("lzfsouceidfg" + souceid);
            title = bundle.getString(CyUtils.INTENT_PARAMS_TITLE);
            isHiddenCommentCount = bundle.getBoolean(CyUtils.ISHIDDENCOMMENTCOUNT);
            isShowComment = bundle.getBoolean(CyUtils.ISSHOWCOMMENT);
        }
//        单点登录   nickname可以相同  用户id不能相同
        if (DeviceInfo.isLogin()) {
            CyUtils.loginSso(AppConstants.register.getUser().getUserId(), AppConstants.register.getUser().getNickName(), sdk);
        }
        model = DeviceInfo.getModel().replace(" ", "");
    }

    private void initListView() {
        mAdapter = new CounselComentLvAdapter(getActivity());
        mListView.setAdapter(mAdapter);

    }

    private void initView() {

        mNoData = (TextView) mView.findViewById(R.id.nodata);
        mListView = (PullUpRefreshListView) mView.findViewById(R.id.comment_lv);
        mListView.setItemsCanFocus(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastTools.showQuickCenter(CounselCommentActivity.this,position+"");
            }
        });


        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.comment_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));


        mCommentCount = (TextView) mView.findViewById(R.id.tv_commentcount);
        mCommentCount.setOnClickListener(this);
        mEditText = (EditText) mView.findViewById(R.id.et_comment);
        mEditText.requestFocus();
        mEditText.setSelected(true);
        mSend = (TextView) mView.findViewById(R.id.iv_send);
        mEditText.setOnClickListener(this);
        mSend.setOnClickListener(this);
        if (isHiddenCommentCount) {
            mSend.setVisibility(View.VISIBLE);
            mCommentCount.setVisibility(View.GONE);
        } else {
            mSend.setVisibility(View.GONE);
            mCommentCount.setVisibility(View.VISIBLE);
        }

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mSend.setVisibility(View.VISIBLE);
                mCommentCount.setVisibility(View.GONE);
                if (TextUtils.isEmpty(mEditText.getText())) {
                    mSend.setSelected(false);
                } else {
                    mSend.setSelected(true);
                }
            }
        });
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DeviceInfo.isLogin()) {
                    //跳转登录界面
                    Intent intent1 = new Intent(mContext, LoginActivity.class);
                    startActivityForResult(intent1, CyUtils.JUMP_COMMENT_QUESTCODE);
                }
            }
        });
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                mEditText.setSelected(hasFocus);


            }


        });
        //获取评论的一切信息
        if (!TextUtils.isEmpty(souceid)) {
            if (isShowComment) {
                loadTopic(souceid, title, CyUtils.SINGLE_PAGE_COMMENT);
            } else {
                loadTopic(souceid, title, 0);
            }

        }
    }

    //上拉加载
    public void pullUpLoad() {
        //listview上拉加载
        View view = getActivity().getLayoutInflater().inflate(R.layout.listfooter_more, null);
        mLoadMore = (TextView) view.findViewById(R.id.load_more);
        TextView bottomline = (TextView) view.findViewById(R.id.bottomline);
        bottomline.setVisibility(View.GONE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRequestFinish) {//上一个请求完成才执行这个 不然一直往上拉，会连续发多个请求
                    mCurrentPager++;
                    //请求下一页数据
                    if (!mLoadMore.getText().equals(getResources().getString(R.string.foot_nomoredata))) {//没有更多数据的时候，上拉不再发起请求
                        getTopicComments(mCurrentPager);
                    }
                }
            }
        });
        mProgressBar = (ProgressBar) view.findViewById(R.id.pull_to_refresh_progress);
        mLoadMore.setText(R.string.foot_loadmore);
        mListView.initBottomView(view);
        mListView.setMyPullUpListViewCallBack(new PullUpRefreshListView.MyPullUpListViewCallBack() {
            @Override
            public void scrollBottomState() {
                //上拉加载的逻辑
                //index代表当前的页面
                if (isRequestFinish) {//上一个请求完成才执行这个，不然一直往上拉，会连续发多个请求
                    mCurrentPager++;
                    //请求下一页数据
                    if (!mLoadMore.getText().equals(getResources().getString(R.string.foot_nomoredata))) {//没有更多数据的时候，上拉不再发起请求
                        getTopicComments(mCurrentPager);
                    }

                }
            }
        });

    }


    //分页查询文章怕评论数据 无需登录
    public void getTopicComments(int page) {
        mProgressBar.setVisibility(View.VISIBLE);
        isRequestFinish = false;
        mLoadMore.setText(R.string.foot_loadingmore);
        sdk.getTopicComments(topicid, CyUtils.SINGLE_PAGE_COMMENT, page, null, "", 1, 5, new CyanRequestListener<TopicCommentsResp>() {
            @Override
            public void onRequestSucceeded(TopicCommentsResp topicCommentsResp) {
                isRequestFinish = true;
                mProgressBar.setVisibility(View.GONE);
                mLoadMore.setText(R.string.foot_loadmore);
                if (mCommentArrayList != null) {
                    if (topicCommentsResp.comments.size() == 0) {
                        mLoadMore.setText(R.string.foot_nomoredata);
                    } else {
                        mCommentArrayList.addAll(topicCommentsResp.comments);
                        mAdapter.setInfosList(mCommentArrayList);
                        mAdapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onRequestFailed(CyanException e) {
                isRequestFinish = true;
                mProgressBar.setVisibility(View.GONE);
                mLoadMore.setText(R.string.foot_neterror);
            }
        });
    }

    private void initScrollView() {
        //解决adjustresize和透明状态栏的冲突
        final View decview = getActivity().getWindow().getDecorView();
        decview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                reLayout(decview, mView);
            }
        });
    }

    //在某个view里重新布局某个view
    public void reLayout(View decview, View scrollview) {
        Rect r = new Rect();
        decview.getWindowVisibleDisplayFrame(r);
        int screenheight = decview.getRootView().getHeight();
        int h = screenheight - r.bottom;
        if (h > 300) {//软键盘显示
            mSend.setVisibility(View.VISIBLE);
            mCommentCount.setVisibility(View.GONE);
            mEditText.setHint("");
            if (model.equals("m2note")) {
                h -= 145;
            }

            if (mKeyBoardHiddenLisener != null) {
                mKeyBoardHiddenLisener.keyBoardShow();
            }


        } else if (h < 300) {//软键盘隐藏
            if (h != 0) {
                def = h;//因为有的手机在键盘隐藏时   int h = screenheight - r.bottom;这两个的
                // 差值h不是0，有一个差值，所以把这个差值保存起来，重新layout的时候，减去这个差值
            }
            if (TextUtils.isEmpty(mEditText.getText())) {

                if (isHiddenCommentCount) {
                    mCommentCount.setVisibility(View.GONE);
                    mSend.setVisibility(View.VISIBLE);
                } else {
                    mCommentCount.setVisibility(View.VISIBLE);
                    mSend.setVisibility(View.GONE);
                }

            } else {
                mSend.setVisibility(View.VISIBLE);
                mCommentCount.setVisibility(View.GONE);
            }
            mEditText.setHint(R.string.hint_content);
            if (mKeyBoardHiddenLisener != null) {
                mKeyBoardHiddenLisener.keyBoardHidden();
            }

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//api大于19透明状态栏才有效果，这时候才重新布局
//            if (isShowComment) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) scrollview.getLayoutParams();
                lp.setMargins(0, 0, 0, h - def);
//            } else {
//                ScrollView.LayoutParams lp = (ScrollView.LayoutParams) scrollview.getLayoutParams();
//                lp.setMargins(0, 0, 0, h - def);
//            }
            scrollview.requestLayout();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_send://发送评论
                MobclickAgent.onEvent(mContext, "Football_CounselCommentActivity_Send");
                mCurrentPager = 1;//这里也要归1，不然在上拉加载到没有数据  再发送评论的时候  就无法再上拉加载了
                mLoadMore.setText(R.string.foot_loadmore);
                if (TextUtils.isEmpty(mEditText.getText())) {//没有输入内容
                    ToastTools.showQuickCenter(mContext, getResources().getString(R.string.warn_nullcontent));
                } else {//有输入内容
                    if (DeviceInfo.isLogin()) {//已登录华海
                        if (CyUtils.isLogin) {//已登录畅言
                            L.i("lzf提交topicid=" + topicid);
                            if (issubmitFinish) {//是否提交完成，若提交未完成，则不再重复提交
                                issubmitFinish = false;
                                CyUtils.submitComment(topicid, mEditText.getText() + "", sdk, this);
                            }
                        } else {//未登录
                            ToastTools.showQuickCenter(mContext, getResources().getString(R.string.warn_submitfail));
                            CyUtils.loginSso(AppConstants.register.getUser().getUserId(), AppConstants.register.getUser().getNickName(), sdk);
                        }
                        CyUtils.hideKeyBoard(getActivity());
                        mEditText.clearFocus();
                    } else {
                        //跳转登录界面
                        Intent intent1 = new Intent(mContext, LoginActivity.class);
                        startActivityForResult(intent1, CyUtils.JUMP_COMMENT_QUESTCODE);
                    }

                }
                break;
            case R.id.tv_commentcount://评论数

                L.d("wwweee","单机小");
                MobclickAgent.onEvent(mContext, "Football_DataInfo_CommentCount");
                Intent intent = new Intent(mContext, CounselCommentActivity.class);
                intent.putExtra(CyUtils.INTENT_PARAMS_SID, souceid);
                intent.putExtra(CyUtils.INTENT_PARAMS_TITLE, title);
                startActivityForResult(intent, CyUtils.JUMP_QUESTCODE);
                break;
        }

    }

    //获取评论的一切消息  无需登录  并刷新listview
    public void loadTopic(String url, String title, int pagenum) {
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);

        L.d("chart", "url=" + url);
        L.d("chart", "title=" + title);
        L.d("chart", "pagenum=" + pagenum);


        sdk.loadTopic("", url, title, null, pagenum, pagenum, "", null, 1, 10, new CyanRequestListener<TopicLoadResp>() {
            @Override
            public void onRequestSucceeded(TopicLoadResp topicLoadResp) {
                topicid = topicLoadResp.topic_id;//文章id
                cmt_sum = topicLoadResp.cmt_sum;//评论总数
                L.d("chart", "topicid=" + topicid);
                L.d("chart", "cmt_sum=" + cmt_sum);

                mCommentCount.setText(cmt_sum + "");
                mCommentArrayList = topicLoadResp.comments;//最新评论列表  这样写既每次调用该方法时，都会是最新的数据，不用再清除数据  可适应下拉刷新
                if (mCommentArrayList == null || mCommentArrayList.size() == 0) {//，没请求到数据 mNoData显示
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    if (isShowComment) {//显示评论的时候 mNoData显示
                        mNoData.setVisibility(View.VISIBLE);
                    } else {
                        mNoData.setVisibility(View.GONE);//不显示评论的时候 mNoData不显示
                    }
                } else {

                    mAdapter.setInfosList(mCommentArrayList);
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mNoData.setVisibility(View.GONE);
                    mListView.setSelection(0);
                    L.i("lzfnotifyDataSetChanged==");
                }
                mSwipeRefreshLayout.setRefreshing(false);
//                L.i("lzf最新列表=" + mCommentArrayList.size());
//                L.i("lzf最热列表=" + topicLoadResp.hots.size());
                if (getActivity() != null) {
                    getActivity().setResult(2, new Intent().putExtra("cmt_sum", cmt_sum));
                }
            }

            @Override
            public void onRequestFailed(CyanException e) {
                L.i("lzftopicid=" + e.toString());
                L.i("lzfcmt_sum=" + e.toString());
                if (mCommentArrayList != null && mCommentArrayList.size() != 0) {//已经有数据  说明不是第一次操作  既是下拉刷新的操作
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                } else {//没有数据  说明是第一次操作
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mNoData.setVisibility(View.VISIBLE);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //下拉刷新回调
    @Override
    public void onRefresh() {
        //下拉刷新后当前页数重新为1，不然先上拉加载到没有数据  再回去下拉刷新  然后再上拉就没有数据了，其实是有的
        mCurrentPager = 1;
        mLoadMore.setText(R.string.foot_loadmore);
        loadTopic(souceid, title, CyUtils.SINGLE_PAGE_COMMENT);
        if (mOnPullDownRefreshLisener != null) {
            mOnPullDownRefreshLisener.onPullDownRefresh();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //接收全部评论页面返回的评论总数
        if (!isShowComment) {
            if (requestCode == CyUtils.JUMP_QUESTCODE) {
                if (resultCode == CyUtils.JUMP_RESULTCODE) {
                    int defaultnum = 0;
                    if (!TextUtils.isEmpty(mCommentCount.getText())) {
                        defaultnum = Integer.parseInt(mCommentCount.getText().toString());
                    }
                    String commentcount = data.getIntExtra("cmt_sum", defaultnum) + "";
                    mCommentCount.setText(commentcount);
                }
            }
        }
        //接收登录华海成功返回
        if (requestCode == 3) {
            if (resultCode == CyUtils.RESULT_OK) {
                if (DeviceInfo.isLogin()) {
                    CyUtils.loginSso(AppConstants.register.getUser().getUserId(), AppConstants.register.getUser().getNickName(), sdk);
                }
            }
        }
    }

    //评论提交成功回调接口
    @Override
    public void onRequestSucceeded(SubmitResp submitResp) {
        issubmitFinish = true;
        mEditText.setText("");
        ToastTools.showQuickCenter(mContext, getResources().getString(R.string.succed_send));
        //刷新界面
        loadTopic(souceid, title, CyUtils.SINGLE_PAGE_COMMENT);
    }

    //评论提交失败回调接口
    @Override
    public void onRequestFailed(CyanException e) {
        issubmitFinish = true;
        ToastTools.showQuickCenter(mContext, getResources().getString(R.string.warn_submitfail));
    }

    //提供键盘显示隐藏的接口供外部调用
    public void setKeyBoardHiddenLisener(onKeyBoardHiddenLisener keyBoardHiddenLisener) {
        this.mKeyBoardHiddenLisener = keyBoardHiddenLisener;
    }

    //提供下拉刷新的接口供外部调用
    public void setOnPullDownRefreshLisener(onPullDownRefreshLisener mOnPullDownRefreshLisener) {
        this.mOnPullDownRefreshLisener = mOnPullDownRefreshLisener;
    }

    public interface onKeyBoardHiddenLisener {
        void keyBoardHidden();

        void keyBoardShow();
    }

    public interface onPullDownRefreshLisener {
        void onPullDownRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            sdk.logOut();
        } catch (CyanException e) {
            e.printStackTrace();
        }
    }
}
