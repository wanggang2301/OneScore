package com.hhly.mlottery.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.CounselComentLvAdapter;
import com.hhly.mlottery.config.StaticValues;
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
 * lzf
 * 全部评论展示页面
 */
public class CounselCommentActivity extends BaseActivity implements OnClickListener, SwipeRefreshLayout.OnRefreshListener, CyanRequestListener<SubmitResp> {


    private ImageView mPublic_img_back;// 返回
    private TextView mPublic_txt_title;// 标题
    private TextView mCommentCount;//评论数
    private EditText mEditText;//输入评论
    private TextView mSend;//发送评论
    private TextView mNoData;//暂无评论
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PullUpRefreshListView mListView;
    private CounselComentLvAdapter mAdapter;
    private TextView mLoadMore;//加载更多
    private ProgressBar mProgressBar;//上拉加载的进度条
    private ArrayList<Comment> mCommentArrayList;//最新评论数据
    private CyanSdk sdk;
    private String url;
    private String title;
    private String model;
    private static final String INTENT_PARAMS_URL = "url";
    private static final String INTENT_PARAMS_TITLE = "title";
    private static final int SINGLE_PAGE_COMMENT = 30;
    private long topicid;//畅言分配的文章ID，通过loadTopic接口获取
    private int cmt_sum;//评论总数
    private int mCurrentPager = 1;
    private boolean isRequestFinish = true;
    private   int def = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselcomment);
        Intent intent = getIntent();
        url = intent.getStringExtra(INTENT_PARAMS_URL);
        title = intent.getStringExtra(INTENT_PARAMS_TITLE);
        sdk = CyanSdk.getInstance(this);
        initView();

        initScrollView();//解决adjustresize和透明状态栏的冲突

        initListView();
        //获取评论的一切信息
        if (!TextUtils.isEmpty(url)) {
            loadTopic(url, title);
        }
        pullUpLoad();//上拉加载更多
        model=DeviceInfo.getModel().replace(" ", "");
    }

    private void initListView() {
        mAdapter = new CounselComentLvAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    private void initScrollView() {
        //解决adjustresize和透明状态栏的冲突
        final ScrollView scrollview = (ScrollView) findViewById(R.id.scrollview);
        final View decview = getWindow().getDecorView();
        decview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decview.getWindowVisibleDisplayFrame(r);
                int screenheight = decview.getRootView().getHeight();
                int h = screenheight - r.bottom;

                if (h > 300) {//软键盘显示
//                    ToastTools.ShowQuickCenter(WebActivity.this, "软件盘显示");
//                    Log.e("lzf", "软件盘显示");
                    mEditText.setHint("");
                    if (model.equals("m2note")){
                        h-=145;
                    }

                } else if (h < 300) {//软键盘隐藏
                    if (h != 0) {
                        def = h;//因为有的手机在键盘隐藏时   int h = screenheight - r.bottom;这两个的
                        // 差值h不是0，有一个差值，所以把这个差值保存起来，重新layou的时候，减去这个差值
                    }
//                    ToastTools.ShowQuickCenter(WebActivity.this, "软件盘隐藏");
//                    Log.e("lzf", "软件盘隐藏");
                    mEditText.setHint(R.string.hint_content);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//api大于19透明状态栏才有效果
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) scrollview.getLayoutParams();
                    lp.setMargins(0, 0, 0, h - def);
                    scrollview.requestLayout();

                }
            }
        });
    }

    private void initView() {
        mPublic_img_back = (ImageView) findViewById(R.id.public_img_back);
        mPublic_txt_title = (TextView) findViewById(R.id.public_txt_title);
        mNoData = (TextView) findViewById(R.id.nodata);
        mPublic_txt_title.setText(R.string.comment_title);
        mListView = (PullUpRefreshListView) findViewById(R.id.comment_lv);
        mListView.setItemsCanFocus(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastTools.ShowQuickCenter(CounselCommentActivity.this,position+"");
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.comment_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(this, StaticValues.REFRASH_OFFSET_END));


        mCommentCount = (TextView) findViewById(R.id.tv_commentcount);
        mEditText = (EditText) findViewById(R.id.et_comment);
        mSend = (TextView) findViewById(R.id.iv_send);
        mEditText.setOnClickListener(this);
        mSend.setOnClickListener(this);
        mPublic_img_back.setOnClickListener(this);
        mSend.setVisibility(View.VISIBLE);
        mCommentCount.setVisibility(View.GONE);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(mEditText.getText())) {
                    mSend.setSelected(false);
                } else {
                    mSend.setSelected(true);
                }

            }
        });
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEditText.setSelected(hasFocus);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back://返回
                CyUtils.hideKeyBoard(this);
                setResult(2, new Intent().putExtra("cmt_sum", cmt_sum));
                MobclickAgent.onEvent(mContext, "Football_CounselCommentActivity_Exit");
                finish();
                break;
            case R.id.iv_send://发送评论
                MobclickAgent.onEvent(mContext, "Football_CounselCommentActivity_Send");
                mCurrentPager = 1;//这里也要归1，不然在上拉加载到没有数据  再发送评论的时候  就无法再上拉加载了
                mLoadMore.setText(R.string.foot_loadmore);
                if (TextUtils.isEmpty(mEditText.getText())) {//没有输入内容
                    ToastTools.ShowQuickCenter(this, getResources().getString(R.string.warn_nullcontent));
                } else {//有输入内容
                    if (CyUtils.isLogin) {//已登录
                        CyUtils.submitComment(topicid, mEditText.getText() + "", sdk, this);
                    } else {//未登录
                        ToastTools.ShowQuickCenter(this, getResources().getString(R.string.warn_submitfail));
                        CyUtils.loginSso(DeviceInfo.getDeviceId(this), DeviceInfo.getDeviceId(this), sdk);
                    }
                    CyUtils.hideKeyBoard(this);
                    mEditText.clearFocus();
                }


                break;
        }
    }

    //下拉刷新回调
    @Override
    public void onRefresh() {
        //下拉刷新后当前页数重新为1，不然先上拉加载到没有数据  再回去下拉刷新  然后再上拉就没有数据了，其实是有的
        mCurrentPager = 1;
        mLoadMore.setText(R.string.foot_loadmore);
        loadTopic(url, title);
    }

    //获取评论的一切消息  无需登录  并刷新listview
    public void loadTopic(String url, String title) {
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        sdk.loadTopic("", url, title, null, SINGLE_PAGE_COMMENT, SINGLE_PAGE_COMMENT, "", null, 1, 10, new CyanRequestListener<TopicLoadResp>() {
            @Override
            public void onRequestSucceeded(TopicLoadResp topicLoadResp) {
                topicid = topicLoadResp.topic_id;//文章id
                cmt_sum = topicLoadResp.cmt_sum;//评论总数
                mCommentArrayList = topicLoadResp.comments;//最新评论列表  这样写既每次调用该方法时，都会是最新的数据，不用再清除数据  可适应下拉刷新
                if (mCommentArrayList.size() == 0) {
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mNoData.setVisibility(View.VISIBLE);
                } else {
                    mAdapter.setInfosList(mCommentArrayList);
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mNoData.setVisibility(View.GONE);
                    mListView.setSelection(0);
                }
                mSwipeRefreshLayout.setRefreshing(false);
                L.i("lzf最新列表=" + mCommentArrayList.size());
                L.i("lzf最热列表=" + topicLoadResp.hots.size());
                setResult(2, new Intent().putExtra("cmt_sum", cmt_sum));
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

    //上拉加载
    public void pullUpLoad() {
        //listview上拉加载
        View view = getLayoutInflater().inflate(R.layout.listfooter_more, null);
        mLoadMore = (TextView) view.findViewById(R.id.load_more);
        TextView bottomline = (TextView) view.findViewById(R.id.bottomline);
        bottomline.setVisibility(View.GONE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRequestFinish) {//上一个请求完成才执行这个 不然一直往上拉，会连续发多个请求
                    mCurrentPager++;
                    //请求下一页数据
                    getTopicComments(mCurrentPager);
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
                    getTopicComments(mCurrentPager);
                }
            }
        });

    }

    //分页查询文章怕评论数据 无需登录
    public void getTopicComments(int page) {
        mProgressBar.setVisibility(View.VISIBLE);
        isRequestFinish = false;
        mLoadMore.setText(R.string.foot_loadingmore);
        sdk.getTopicComments(topicid, SINGLE_PAGE_COMMENT, page, null, "", 1, 5, new CyanRequestListener<TopicCommentsResp>() {
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

    //评论提交成功回调接口
    @Override
    public void onRequestSucceeded(SubmitResp submitResp) {
        mEditText.setText("");

        //刷新界面
        loadTopic(url, title);
    }

    //评论提交失败回调接口
    @Override
    public void onRequestFailed(CyanException e) {
        ToastTools.ShowQuickCenter(this, getResources().getString(R.string.warn_submitfail));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("Football_CounselCommentActivity");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("Football_CounselCommentActivity");
    }
}