package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.bean.footballDetails.FirstPlayersBean;
import com.hhly.mlottery.bean.footballDetails.PlayerInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首发阵容
 */
public class FirstPlayersFragment extends Fragment {

    private View mView;// 根布局
    private Context mContext;// 上下文对象
    private FrameLayout fl_firsPlayers_not;// 暂无首发容器
    private FrameLayout fl_firsPlayers_content;// 首发内容容器
    private FrameLayout fl_firsPlayers_loading;// 正在加载中
    private FrameLayout fl_firsPlayers_networkError;// 加载失败

    //private ImageView iv_loading_img;// 加载动画图片
    //private Animation mAnimation;// 加载动画
    private TextView reLoading;// 点击刷新

    private LinearLayout ll_rosters_homeTeam;// 主队名单容器
    private LinearLayout ll_rosters_visitingTeam;// 客队名单容器

    private List<PlayerInfo> homeLineUpList = new ArrayList<>();// 主队首发阵容列表单名
    private List<PlayerInfo> guestLineUpList = new ArrayList<>();// 客队首发阵容列表单名

    private static final int ERROR = -1;//访问失败
    private static final int SUCCESS = 0;// 访问成功
    private static final int STARTLOADING = 1;// 数据加载中
    private ScrollView sv_first;
    private TextView tv_homeTeam_title;
    private TextView tv_visitingTeam_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.first_players, container, false);
        mContext = getActivity();

        initView();//  初始化菜单
        //initData();// 初始化数据
        initEvent();// 初始化事件

        return mView;
    }

    private boolean isHttpData = false;

    /**
     * 初始化事件
     */
    private void initEvent() {
        reLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 请求后台数据
                getVolleyData();
            }
        });
        // 走势图滚动监听
        sv_first.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        if (sv_first.getScrollY() != 0) {// 处于顶部
                            if (getActivity() != null) {
                                ((FootballMatchDetailActivity) getActivity()).mRefreshLayout.setEnabled(false);
                            }
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (getActivity() != null) {
                            ((FootballMatchDetailActivity) getActivity()).mRefreshLayout.setEnabled(true);
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 请求后台数据
     */
    private void getVolleyData() {
        // 显示加载数据中
        mHandler.sendEmptyMessage(STARTLOADING);// 数据正在加载中
        // 获取对象ID
        if (getActivity() == null) {
            return;
        }
        String mThirdId = ((FootballMatchDetailActivity) getActivity()).mThirdId;
        // 设置URL
        //String url = "http://192.168.10.152:8080/mlottery/core/footBallMatch.findLineUp.do";
        // 设置参数
        Map<String, String> myPostParams = new HashMap<>();
        myPostParams.put("thirdId", mThirdId);

        VolleyContentFast.requestJsonByPost(BaseURLs.URL_FOOTBALL_DETAIL_FINDLINEUP_INFO, myPostParams, new VolleyContentFast.ResponseSuccessListener<FirstPlayersBean>() {
            @Override
            public void onResponse(FirstPlayersBean jsonObject) {
                // 访问成功
                homeLineUpList.clear();
                guestLineUpList.clear();
                if (jsonObject != null) {
                    homeLineUpList = jsonObject.getHomeLineUp();// 获取主队名单列表
                    guestLineUpList = jsonObject.getGuestLineUp();// 获取客队名单列表
                }
                mHandler.sendEmptyMessage(SUCCESS);// 访问成功
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERROR);// 访问失败
            }
        }, FirstPlayersBean.class);
    }

    /**
     * 显示数据内容
     */
    private void showDate() {
        // 判断是否有首发内容
        if (homeLineUpList != null && guestLineUpList != null) {
            if (homeLineUpList.size() > 0) {
                // 显示首发内容
                fl_firsPlayers_not.setVisibility(View.GONE);
                fl_firsPlayers_content.setVisibility(View.VISIBLE);

                int dip5 = DisplayUtil.dip2px(mContext, 5);
                int dip10 = DisplayUtil.dip2px(mContext, 10);

                ll_rosters_homeTeam.removeAllViews();
                ll_rosters_homeTeam.addView(tv_homeTeam_title);
                // 添加主队名单
                for (int i = 0, len = homeLineUpList.size(); i < len; i++) {
                    TextView tv_homeTeams = new TextView(mContext);
                    tv_homeTeams.setText(homeLineUpList.get(i).getName());
                    if (i == 0) {
                        tv_homeTeams.setPadding(dip5, dip10, 0, dip10);
                    } else {
                        tv_homeTeams.setPadding(dip5, 0, 0, dip10);
                    }
                    tv_homeTeams.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
                    ll_rosters_homeTeam.addView(tv_homeTeams);
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.RIGHT;// 设置靠右对齐

                ll_rosters_visitingTeam.removeAllViews();
                ll_rosters_visitingTeam.addView(tv_visitingTeam_title);
                // 添加客队名单
                for (int i = 0, len = guestLineUpList.size(); i < len; i++) {
                    TextView tv_visitingTeams = new TextView(mContext);
                    tv_visitingTeams.setText(guestLineUpList.get(i).getName());
                    tv_visitingTeams.setLayoutParams(params);
                    if (i == 0) {
                        tv_visitingTeams.setPadding(0, dip10, dip5, dip10);
                    } else {
                        tv_visitingTeams.setPadding(0, 0, dip5, dip10);
                    }
                    tv_visitingTeams.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
                    ll_rosters_visitingTeam.addView(tv_visitingTeams);
                }
                return;
            }
        }
        // 显示暂无首发提示
        fl_firsPlayers_not.setVisibility(View.VISIBLE);
        fl_firsPlayers_content.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 请求后台数据
        if (!isHttpData) {
            getVolleyData();
        }
    }

    /**
     * 初始化菜单
     */
    private void initView() {
        fl_firsPlayers_not = (FrameLayout) mView.findViewById(R.id.fl_firsPlayers_not);
        fl_firsPlayers_content = (FrameLayout) mView.findViewById(R.id.fl_firsPlayers_content);
        fl_firsPlayers_loading = (FrameLayout) mView.findViewById(R.id.fl_firsPlayers_loading);
        fl_firsPlayers_networkError = (FrameLayout) mView.findViewById(R.id.fl_firsPlayers_networkError);

        ll_rosters_homeTeam = (LinearLayout) mView.findViewById(R.id.ll_rosters_homeTeam);
        ll_rosters_visitingTeam = (LinearLayout) mView.findViewById(R.id.ll_rosters_visitingTeam);
        reLoading = (TextView) mView.findViewById(R.id.reLoading);
        sv_first = (ScrollView) mView.findViewById(R.id.sv_first);

        tv_homeTeam_title = (TextView) mView.findViewById(R.id.tv_homeTeam_title);
        tv_visitingTeam_title = (TextView) mView.findViewById(R.id.tv_visitingTeam_title);

        /*iv_loading_img = (ImageView) mView.findViewById(R.id.iv_loading_img);
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.cirle);
        mAnimation.setInterpolator(new LinearInterpolator());
        iv_loading_img.startAnimation(mAnimation);// 添加加载动画*/
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case STARTLOADING:// 加载数据中
                    fl_firsPlayers_loading.setVisibility(View.VISIBLE);// 显示正在加载中
                    fl_firsPlayers_networkError.setVisibility(View.GONE);
                    fl_firsPlayers_not.setVisibility(View.GONE);
                    fl_firsPlayers_content.setVisibility(View.GONE);
                    break;
                case SUCCESS:// 数据加载成功
                    fl_firsPlayers_loading.setVisibility(View.GONE);// 显示正在加载中
                    fl_firsPlayers_networkError.setVisibility(View.GONE);
                    isHttpData = true;
                    showDate();
                    break;
                case ERROR:// 数据加载失败
                    fl_firsPlayers_loading.setVisibility(View.GONE);
                    fl_firsPlayers_networkError.setVisibility(View.VISIBLE);// 显示网络访问失败
                    fl_firsPlayers_not.setVisibility(View.GONE);
                    fl_firsPlayers_content.setVisibility(View.GONE);
                    isHttpData = false;
                    break;
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            initData();
        }
        super.onHiddenChanged(hidden);
    }

    public void refershData() {
        isHttpData = false;
        initData();
    }
}
