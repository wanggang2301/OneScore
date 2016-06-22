package com.hhly.mlottery.frame.footframe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.InputActivity;
import com.hhly.mlottery.activity.LoginActivity;
import com.hhly.mlottery.adapter.CounselComentLvAdapter;
import com.hhly.mlottery.bean.footballDetails.MatchLike;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.PullUpRefreshListView;
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
    private TextView mNoData;//暂无评论
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PullUpRefreshListView mListView;
    private TextView mLoadMore;//加载更多
    private ProgressBar mProgressBar;//上拉加载的进度条
    private ArrayList<Comment> mCommentArrayList;//最新评论数据
    public int cmt_sum;//评论总数
    private long topicid;//畅言分配的文章ID，通过loadTopic接口获取
    private int mCurrentPager = 1;//页数
    private boolean isRequestFinish = true;//是否加载评论结束
    private CounselComentLvAdapter mAdapter;
    private CyanSdk sdk;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ADDKEYHOME = "homeAdd";
    private static final String ADDKEYGUEST = "guestAdd";
    private Context mContext;
    private int type;//1 籃球/0 足球
    private String state = "-1";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

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
        mView = inflater.inflate(R.layout.fragment_talkaboutball, null);
        initView();
        requestLikeData(ADDKEYHOME, "0", type);
        initAnim();
        pullUpLoad();//上拉加载更多
        registerBroadCast();
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
        if (type == 0) {
            url = BaseURLs.URL_FOOTBALL_DETAIL_LIKE_INFO;
//            ToastTools.ShowQuickCenter(getActivity(),"足球");
        } else if (type == 1) {
//            url = BaseURLs.URL_BASKETBALLBALL_DETAIL_LIKE_INFO;
//            ToastTools.ShowQuickCenter(getActivity(),"籃球");
        }
        VolleyContentFast.requestJsonByPost(url, params, new VolleyContentFast.ResponseSuccessListener<MatchLike>() {
            @Override
            public void onResponse(MatchLike matchLike) {
                tvHomeLikeCount.setText(matchLike.getHomeLike());
                tvGuestLikeCount.setText(matchLike.getGuestLike());
                int homeLikeCount = Integer.parseInt(matchLike.getHomeLike());
                int guestLikeCount = Integer.parseInt(matchLike.getGuestLike());
                talkballpro.setMax(homeLikeCount + guestLikeCount);
                talkballpro.setProgress(homeLikeCount);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, MatchLike.class);
    }
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadTopic(mThirdId, mThirdId, CyUtils.SINGLE_PAGE_COMMENT);
            ToastTools.ShowQuickCenter(getActivity(),"shoudaoguangbo");
        }
    };
    public void registerBroadCast() {
        IntentFilter intentFilter=new IntentFilter("loadingdata");
        getActivity().registerReceiver(broadcastReceiver, intentFilter);

    }

    private void initView() {
        sdk = CyanSdk.getInstance(mContext);
        if (CommonUtils.isLogin()) {
            CyUtils.loginSso(AppConstants.register.getData().getUser().getUserId(), AppConstants.register.getData().getUser().getNickName(), sdk);
        }
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
        //评论相关
        mNoData = (TextView) mView.findViewById(R.id.nodata);
        mListView = (PullUpRefreshListView) mView.findViewById(R.id.comment_lv);
        mListView.setItemsCanFocus(true);
        mAdapter = new CounselComentLvAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.comment_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));
        mCommentCount = (TextView) mView.findViewById(R.id.tv_commentcount);
        mCommentCount.setOnClickListener(this);
        mTextView = (TextView) mView.findViewById(R.id.et_comment);
        mLinearLayout = (LinearLayout) mView.findViewById(R.id.comment_inputcontainer);
        mTextView.setOnClickListener(this);
        mCommentCount.setVisibility(View.VISIBLE);
        //获取评论的一切信息
        if (!TextUtils.isEmpty(mThirdId)) {
            loadTopic(mThirdId, mThirdId, CyUtils.SINGLE_PAGE_COMMENT);
        }
        //以前的规则是-1不可点  现在延续
        if (state != null && state.equals("-1")) {
            setClickableLikeBtn(false);
        } else {
            setClickableLikeBtn(true);
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
      以下是评论相关代码  上面是分享相关代码
     */
    //获取评论的一切消息  无需登录  并刷新listview
    public void loadTopic(String url, String title, int pagenum) {
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        sdk.loadTopic("", url, title, null, pagenum, pagenum, "", null, 1, 10, new CyanRequestListener<TopicLoadResp>() {
            @Override
            public void onRequestSucceeded(TopicLoadResp topicLoadResp) {
                mCurrentPager = 1;//这里也要归1，不然在上拉加载到没有数据  再发送评论的时候  就无法再上拉加载了
                mLoadMore.setText(R.string.foot_loadmore);
                topicid = topicLoadResp.topic_id;//文章id
                cmt_sum = topicLoadResp.cmt_sum;//评论总数
                mCommentCount.setText(cmt_sum + "");
                mCommentArrayList = topicLoadResp.comments;//最新评论列表  这样写既每次调用该方法时，都会是最新的数据，不用再清除数据  可适应下拉刷新
                if (mCommentArrayList.size() == 0) {//，没请求到数据 mNoData显示
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mNoData.setVisibility(View.VISIBLE);
                } else {
                    mAdapter.setInfosList(mCommentArrayList);
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mNoData.setVisibility(View.GONE);
                    mListView.setSelection(0);
                    L.i("lzfnotifyDataSetChanged==");
                }
                mSwipeRefreshLayout.setRefreshing(false);
                L.i("lzf最新列表=" + mCommentArrayList.size());
                L.i("lzf最热列表=" + topicLoadResp.hots.size());
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
//                case CyUtils.RESULT_CODE://接收评论输入页面返回
//                    loadTopic(mThirdId, mThirdId, CyUtils.SINGLE_PAGE_COMMENT);
//                    mLinearLayout.setVisibility(View.VISIBLE);
//                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mSwipeRefreshLayout.getLayoutParams();
//                    lp.setMargins(0, 0, 0, 0);
//                    mSwipeRefreshLayout.requestLayout();
//                    break;
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
        } else {
            Intent intent2 = new Intent(mContext, InputActivity.class);
            intent2.putExtra(CyUtils.INTENT_PARAMS_SID, topicid);
            startActivityForResult(intent2, CyUtils.JUMP_COMMENT_QUESTCODE);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
                    Intent intent1 = new Intent(mContext, LoginActivity.class);
                    startActivityForResult(intent1, CyUtils.JUMP_COMMENT_QUESTCODE);
                } else {//跳转输入评论页面
                    Intent intent2 = new Intent(mContext, InputActivity.class);
                    intent2.putExtra(CyUtils.INTENT_PARAMS_SID, topicid);
                    startActivityForResult(intent2, CyUtils.JUMP_COMMENT_QUESTCODE);
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    mLinearLayout.setVisibility(View.GONE);
                    //解决在评论输入窗口的时候  上拉加载按钮被盖住的问题
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mSwipeRefreshLayout.getLayoutParams();
                    lp.setMargins(0, 0, 0, DisplayUtil.dip2px(mContext, 60));
                    mSwipeRefreshLayout.requestLayout();

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(broadcastReceiver);
        try {
            sdk.logOut();
        } catch (CyanException e) {
            e.printStackTrace();
        }
    }
}
