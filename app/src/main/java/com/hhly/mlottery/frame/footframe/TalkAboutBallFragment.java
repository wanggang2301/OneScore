package com.hhly.mlottery.frame.footframe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.InputActivity;
import com.hhly.mlottery.activity.LoginActivity;
import com.hhly.mlottery.adapter.ChatballAdapter;
import com.hhly.mlottery.bean.footballDetails.MatchLike;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.entity.Comment;
import com.sohu.cyan.android.sdk.exception.CyanException;
import com.sohu.cyan.android.sdk.http.CyanRequestListener;
import com.sohu.cyan.android.sdk.http.response.TopicCommentsResp;
import com.sohu.cyan.android.sdk.http.response.TopicLoadResp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hhly204
 * @ClassName: TalkAboutBallFragment
 * @Description: 聊球页面
 * @date 2016-6-2
 */
public class TalkAboutBallFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private View mView;
    private ProgressBar talkballpro;//客队Vs主队点赞的比例
    private ImageView ivHomeLike;//点赞主队
    private ImageView ivGuestLike;//点赞客队
    private TextView tvGuestLikeCount;//点赞客队数
    private TextView tvHomeLikeCount;//点赞主队数
    private LinearLayout mLinearLayout;
    private NestedScrollView nestedscrollview;
    /**
     * 主队点赞
     */
    private ImageView mHomeLike;
    private ImageView mGuestLike;
    /**
     * 点赞动画
     */
    private AnimationSet mRiseHomeAnim;
    private AnimationSet mRiseGuestAnim;
    /**
     * 赛事id
     */
    public String mThirdId;
    /**
     * 评论相关
     */
    private TextView mCommentCount;//评论数
    private TextView mTextView;//输入评论
    //    private TextView mNoData;//暂无评论
    private RecyclerView mRecyclerView;
    private TextView mLoadMore;//加载更多
    private ProgressBar mProgressBar;//上拉加载的进度条
    private ArrayList<Comment> mCommentArrayList;//最新评论数据
    public int cmt_sum;//评论总数
    private long topicid;//畅言分配的文章ID，通过loadTopic接口获取
    private int mCurrentPager = 1;//页数
    private boolean isRequestFinish = true;//是否加载评论结束
    private ChatballAdapter mChatballAdapter;
    private CyanSdk sdk;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ADDKEYHOME = "homeAdd";
    private static final String ADDKEYGUEST = "guestAdd";
    private int type;//1 籃球/0 足球
    private String state = "-1";
    private View view;
    private View emptyView;
    private ProgressBar mProgressBarRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        L.d(TAG, "______________onCreate");
        if (getArguments() != null) {
            mThirdId = getArguments().getString(ARG_PARAM1);
            type = getArguments().getInt("type", -1);
            state = getArguments().getString("state");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_talkaboutball, container, false);
        initView(container);
        requestLikeData(ADDKEYHOME, "0", type);
        initAnim();
        initRecyclerView();
        pullUpLoad(container);//上拉加载更多
        return mView;
    }

    /**
     * bn
     * 初始化动画
     */
    private void initAnim() {
        mRiseHomeAnim = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, 0f, -50f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        mRiseHomeAnim.addAnimation(translateAnimation);
        mRiseHomeAnim.addAnimation(alphaAnimation);
        mRiseHomeAnim.setDuration(300);
        mRiseHomeAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivHomeLike.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mRiseGuestAnim = new AnimationSet(true);
        mRiseGuestAnim.addAnimation(translateAnimation);
        mRiseGuestAnim.addAnimation(alphaAnimation);
        mRiseGuestAnim.setDuration(300);
        mRiseGuestAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivGuestLike.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    //请求点赞数据
    public void requestLikeData(String addkey, String addcount, int type) {
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", mThirdId);
        params.put(addkey, addcount);
        String url = "";
        if (type == 0) {//足球点赞
            url = BaseURLs.URL_FOOTBALL_DETAIL_LIKE_INFO;
        } else if (type == 1) {//篮球点赞
            url = BaseURLs.URL_BASKETBALLBALL_DETAIL_LIKE_INFO;
        }
        VolleyContentFast.requestJsonByPost(url, params, new VolleyContentFast.ResponseSuccessListener<MatchLike>() {
            @Override
            public void onResponse(MatchLike matchLike) {
                tvHomeLikeCount.setText(matchLike.getHomeLike());
                tvGuestLikeCount.setText(matchLike.getGuestLike());
                System.out.println("lzfdianzan" + matchLike.getGuestLike() + matchLike.getHomeLike());
                if (matchLike.getHomeLike() != null && matchLike.getGuestLike() != null) {
                    int homeLikeCount = Integer.parseInt(matchLike.getHomeLike());
                    int guestLikeCount = Integer.parseInt(matchLike.getGuestLike());
                    talkballpro.setMax(homeLikeCount + guestLikeCount);
                    talkballpro.setProgress(homeLikeCount);
                } else {
                    tvHomeLikeCount.setText("0");
                    tvGuestLikeCount.setText("0");
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, MatchLike.class);
    }

    public static TalkAboutBallFragment newInstance(String param1, String param2, int param3) {
        TalkAboutBallFragment fragment = new TalkAboutBallFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString("state", param2);
        args.putInt("type", param3);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView(ViewGroup container) {
        mProgressBarRefresh = (ProgressBar) mView.findViewById(R.id.pull_to_refresh_progress);
        mProgressBarRefresh.setVisibility(View.GONE);
        talkballpro = (ProgressBar) mView.findViewById(R.id.talkball_pro);
        ivHomeLike = (ImageView) mView.findViewById(R.id.talkball_like_anim_img);
        ivGuestLike = (ImageView) mView.findViewById(R.id.talkbail_guest_like_anim_img);
        tvHomeLikeCount = (TextView) mView.findViewById(R.id.talkball_like_count);
        tvGuestLikeCount = (TextView) mView.findViewById(R.id.talkbail_guest_like_count);
        mHomeLike = (ImageView) mView.findViewById(R.id.talkball_home_like);
        mGuestLike = (ImageView) mView.findViewById(R.id.talkball_guest_like);
        mHomeLike.setOnClickListener(this);
        mGuestLike.setOnClickListener(this);
        ivHomeLike.setVisibility(View.INVISIBLE);
        ivGuestLike.setVisibility(View.INVISIBLE);
        setClickableLikeBtn(true);
        //评论相关
        sdk = CyanSdk.getInstance(getActivity());
        if (CommonUtils.isLogin()) {
            CyUtils.loginSso(AppConstants.register.getData().getUser().getUserId(), AppConstants.register.getData().getUser().getNickName(), sdk);
        }
        emptyView = getActivity().getLayoutInflater().inflate(R.layout.layout_nodata, container, false);
//        mNoData = (TextView) mView.findViewById(R.id.nodata);
        mCommentCount = (TextView) mView.findViewById(R.id.tv_commentcount);
        mCommentCount.setOnClickListener(this);
        mTextView = (TextView) mView.findViewById(R.id.et_comment);
        mLinearLayout = (LinearLayout) mView.findViewById(R.id.comment_inputcontainer);
//        nestedscrollview = (NestedScrollView) mView.findViewById(R.id.nestedscrollview);
        mTextView.setOnClickListener(this);
        mCommentCount.setVisibility(View.VISIBLE);
        //获取评论的一切信息
        if (!TextUtils.isEmpty(mThirdId)) {
            loadTopic(mThirdId, mThirdId, CyUtils.SINGLE_PAGE_COMMENT);
        }

    }

    public void setClickableLikeBtn(boolean clickable) {
        mHomeLike.setClickable(clickable);
        mGuestLike.setClickable(clickable);
        if (clickable) {
            mHomeLike.setImageResource(R.mipmap.like_red);
            mGuestLike.setImageResource(R.mipmap.like_blue);
        } else {
            mHomeLike.setImageResource(R.mipmap.like_anim_left);
            mGuestLike.setImageResource(R.mipmap.like_anim_right);
        }
    }

    /*
      以下是评论相关代码  上面是点赞相关代码
     */
    //获取评论的一切消息  无需登录  并刷新listview
    public void loadTopic(String url, String title, int pagenum) {
        mProgressBarRefresh.setVisibility(View.VISIBLE);
        sdk.loadTopic("", url, title, null, pagenum, pagenum, "", null, 1, 10, new CyanRequestListener<TopicLoadResp>() {
            @Override
            public void onRequestSucceeded(TopicLoadResp topicLoadResp) {
                mProgressBarRefresh.setVisibility(View.GONE);
                mCurrentPager = 1;//这里也要归1，不然在上拉加载到没有数据  再发送评论的时候  就无法再上拉加载了
                mLoadMore.setText(R.string.foot_loadmore);
                topicid = topicLoadResp.topic_id;//文章id
                cmt_sum = topicLoadResp.cmt_sum;//评论总数inf
                mCommentCount.setText(cmt_sum + "");
                mCommentArrayList = topicLoadResp.comments;//最新评论列表  这样写既每次调用该方法时，都会是最新的数据，不用再清除数据  可适应下拉刷新
                if (mCommentArrayList != null && mCommentArrayList.size() > 4) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                if (mCommentArrayList.size() == 0) {//，没请求到数据 mNoData显示
//                    mRecyclerView.setVisibility(View.VISIBLE);
//                    mNoData.setVisibility(View.VISIBLE);
                    mChatballAdapter.setEmptyView(emptyView);
                } else {

//                    mAdapter.setInfosList(mCommentArrayList);
//                    mAdapter.notifyDataSetChanged();
                    mChatballAdapter.getData().clear();
                    mChatballAdapter.addData(mCommentArrayList);
                    mChatballAdapter.notifyDataSetChanged();
//                    mRecyclerView.setVisibility(View.VISIBLE);
//                    mNoData.setVisibility(View.GONE);
                    mRecyclerView.smoothScrollToPosition(0);
                    L.i("lzfnotifyDataSetChanged==");
                }
                L.i("lzf最新列表=" + mCommentArrayList.size());
                L.i("lzf最热列表=" + topicLoadResp.hots.size());
            }

            @Override
            public void onRequestFailed(CyanException e) {
                mProgressBarRefresh.setVisibility(View.GONE);

                L.i("lzftopicid=" + e.toString());
                L.i("lzfcmt_sum=" + e.toString());
                if (mCommentArrayList != null && mCommentArrayList.size() != 0) {//已经有数据  说明不是第一次操作  既是下拉刷新的操作
//                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {//没有数据  说明是第一次操作
//                    mRecyclerView.setVisibility(View.VISIBLE);
//                    mNoData.setVisibility(View.VISIBLE);
                    mChatballAdapter.setEmptyView(emptyView);
                }
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.comment_lv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //mRecyclerView.setNestedScrollingEnabled(true);
        //设置Adapter
        if (getActivity() != null) {
            mChatballAdapter = new ChatballAdapter(R.layout.item_counsel_comment, null, getActivity());
            mRecyclerView.setAdapter(mChatballAdapter);
            mChatballAdapter.setPullUpLoading(new ChatballAdapter.PullUpLoading() {
                @Override
                public void onPullUpLoading() {
                    pullUpLoadMore();
                }
            });
        }
        //设置分隔线
        // recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //设置增加或删除条目的动画
        // recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    //上拉加载
    public void pullUpLoad(ViewGroup container) {
        //listview上拉加载
        view = getActivity().getLayoutInflater().inflate(R.layout.listfooter_more, container, false);
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
        mChatballAdapter.addFooterView(view);
    }

    public void pullUpLoadMore() {
        if (isRequestFinish) {//上一个请求完成才执行这个 不然一直往上拉，会连续发多个请求
            mCurrentPager++;
            //请求下一页数据
            if (!mLoadMore.getText().equals(getResources().getString(R.string.foot_nomoredata))) {//没有更多数据的时候，上拉不再发起请求
                getTopicComments(mCurrentPager);
            }
        }
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
                        mChatballAdapter.getData().clear();
                        mChatballAdapter.addData(mCommentArrayList);
                        mChatballAdapter.notifyDataSetChanged();
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

    //下拉刷新回调
    @Override
    public void onRefresh() {
        //下拉刷新后当前页数重新为1，不然先上拉加载到没有数据  再回去下拉刷新  然后再上拉就没有数据了，其实是有的
        mCurrentPager = 1;
        mLoadMore.setText(R.string.foot_loadmore);
        loadTopic(mThirdId, mThirdId, CyUtils.SINGLE_PAGE_COMMENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //接收全部评论页面返回的评论总数
//        if (requestCode == CyUtils.JUMP_QUESTCODE) {
//            if (resultCode == CyUtils.JUMP_RESULTCODE) {
//                int defaultnum = 0;
//                if (!TextUtils.isEmpty(mCommentCount.getText())) {
//                    defaultnum = Integer.parseInt(mCommentCount.getText().toString());
//                }
//                String commentcount = data.getIntExtra("cmt_sum", defaultnum) + "";
//                mCommentCount.setText(commentcount);
//            }
//        }
//
        if (requestCode == CyUtils.JUMP_COMMENT_QUESTCODE) {
            switch (resultCode) {
                case CyUtils.RESULT_OK:
                    if (CommonUtils.isLogin()) { //接收登录华海成功返回
                        CyUtils.loginSso(AppConstants.register.getData().getUser().getUserId(), AppConstants.register.getData().getUser().getNickName(), sdk);
                    }
                    break;
                case CyUtils.RESULT_CODE://接收评论输入页面返回
                    loadTopic(mThirdId, mThirdId, CyUtils.SINGLE_PAGE_COMMENT);
                    mLinearLayout.setVisibility(View.VISIBLE);
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mRecyclerView.getLayoutParams();
                    lp.setMargins(0, 0, 0, 0);
                    mRecyclerView.requestLayout();
                    break;
            }
        }
    }

    //fg切换时回调该方法
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //此处还没关联activity  所以getacitivty为空
        if (!getUserVisibleHint()) {
            MyApp.getContext().sendBroadcast(new Intent("closeself"));
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commentcount:
                break;
            case R.id.et_comment:
                if (!CommonUtils.isLogin()) {
                    //跳转登录界面
                    Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent1, CyUtils.JUMP_COMMENT_QUESTCODE);
                } else {//跳转输入评论页面
                    Intent intent2 = new Intent(getActivity(), InputActivity.class);
                    intent2.putExtra(CyUtils.INTENT_PARAMS_SID, topicid);
                    startActivityForResult(intent2, CyUtils.JUMP_COMMENT_QUESTCODE);
                    mLinearLayout.setVisibility(View.GONE);
                    System.out.println("lzftalk跳" + topicid);
                    //解决在评论输入窗口的时候  上拉加载按钮被盖住的问题
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mRecyclerView.getLayoutParams();
                    lp.setMargins(0, 0, 0, DisplayUtil.dip2px(getActivity(), 60));
                    mRecyclerView.requestLayout();

                }
                break;
            case R.id.talkball_home_like:
                ivHomeLike.setVisibility(View.VISIBLE);
                ivHomeLike.startAnimation(mRiseHomeAnim);
//                //String url = "http://192.168.10.242:8181/mlottery/core/footBallMatch.updLike.do";
                requestLikeData(ADDKEYHOME, "1", type);
                break;
            case R.id.talkball_guest_like:
                ivGuestLike.setVisibility(View.VISIBLE);
                ivGuestLike.startAnimation(mRiseGuestAnim);
                //String url = "http://192.168.10.242:8181/mlottery/core/footBallMatch.updLike.do";
                requestLikeData(ADDKEYGUEST, "1", type);
                break;
        }
    }
}
