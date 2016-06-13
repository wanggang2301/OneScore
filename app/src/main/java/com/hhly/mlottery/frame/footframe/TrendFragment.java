package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.bean.footballDetails.TrendAllBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.MyLineChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 攻防
 * Created by asus1 on 2015/12/29.
 */
public class TrendFragment extends Fragment {

    private View mView;
    private Context mContext;

    private final int ERROR = -1;//访问失败
    private final int SUCCESS = 0;// 访问成功
    private final int STARTLOADING = 1;// 正在加载中

    private List<Integer> mHomeDangers = new ArrayList<>();// 主队攻防数据
    private List<Integer> mGuestDangers = new ArrayList<>();// 客队攻防数据
    private List<Integer> mHomeCorners = new ArrayList<>();// 主队角球数据
    private List<Integer> mGuestCorners = new ArrayList<>();// 客队角球数据

    private LinearLayout ll_trend_main;// 走势图容器
    private FrameLayout ff;// 攻防折线图显示容器
    private FrameLayout ff_corner;// 角球折线图显示容器
    private FrameLayout fl_attackTrend_loading;// 正在加载中
    private FrameLayout fl_attackTrend_networkError;// 加载失败
    private TextView reLoading;// 刷新
    private ScrollView sv_attack;
    private MyLineChart myLineChartAttack;// 攻防图表对象
    private MyLineChart myLineChartCorner;// 角球图表对象

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.attack_trend, container, false);
        mContext = getActivity();
        initView();
        initEvent();
        return mView;
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        // 访问失败，点击刷新
        reLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 请求数据
                getVolleyData();
            }
        });
      /*  // 走势图滚动监听
        sv_attack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        if (sv_attack.getScrollY() != 0) {// 处于顶部
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
        });*/
    }

    /**
     * 初始化界面
     */
    private void initView() {
        ll_trend_main = (LinearLayout) mView.findViewById(R.id.ll_trend_main);
        ff = (FrameLayout) mView.findViewById(R.id.fl_main);
        ff_corner = (FrameLayout) mView.findViewById(R.id.fl_main_corner);
        fl_attackTrend_loading = (FrameLayout) mView.findViewById(R.id.fl_attackTrend_loading);
        fl_attackTrend_networkError = (FrameLayout) mView.findViewById(R.id.fl_attackTrend_networkError);
      //  sv_attack = (ScrollView) mView.findViewById(R.id.sv_attack);

        reLoading = (TextView) mView.findViewById(R.id.reLoading);// 刷新
        // 攻防走势图控件
        myLineChartAttack = new MyLineChart(mContext);
        myLineChartAttack.setXlabel(new String[]{"0", "", "", "45'", "", "", "90'"});// 设置X轴刻度值
        myLineChartAttack.setmLineXYColor(mContext.getResources().getColor(R.color.res_pl_color));// 设置XY主轴的颜色
        myLineChartAttack.setmXYTextColor(mContext.getResources().getColor(R.color.res_time_color));// 设置XY轴文字颜色
        myLineChartAttack.setmGridColor(mContext.getResources().getColor(R.color.linecolor));// 设置网格颜色
        myLineChartAttack.setmOneLineColor(mContext.getResources().getColor(R.color.firstPlayers_homeTeam_bg));// 设置第一条线颜色
        myLineChartAttack.setmTwoLineColor(mContext.getResources().getColor(R.color.firstPlayers_visitingTeam_bg));// 设置第二条线颜色
        myLineChartAttack.setMargin(DisplayUtil.dip2px(mContext, 16));// 设置边距
        myLineChartAttack.setXscale(DisplayUtil.px2dip(mContext, 6));// 设置X轴长度
        myLineChartAttack.setYscale(DisplayUtil.px2dip(mContext, 6));// 设置Y轴长度
        myLineChartAttack.setmTextSize(DisplayUtil.dip2px(mContext, 10));// XY轴字体大小
        myLineChartAttack.setmLineWidth(DisplayUtil.dip2px(mContext, 1));// 线条宽度
        myLineChartAttack.setmCircleSize(DisplayUtil.dip2px(mContext, 3));// 圆点大小
        // 角球走势图控件
        myLineChartCorner = new MyLineChart(mContext);
        myLineChartCorner.setXlabel(new String[]{"0", "", "", "45'", "", "", "90'"});// 设置X轴刻度值
        myLineChartCorner.setmLineXYColor(mContext.getResources().getColor(R.color.res_pl_color));// 设置XY主轴的颜色
        myLineChartCorner.setmXYTextColor(mContext.getResources().getColor(R.color.res_time_color));// 设置XY轴文字颜色
        myLineChartCorner.setmGridColor(mContext.getResources().getColor(R.color.linecolor));// 设置网格颜色
        myLineChartCorner.setmOneLineColor(mContext.getResources().getColor(R.color.firstPlayers_homeTeam_bg));// 设置第一条线颜色
        myLineChartCorner.setmTwoLineColor(mContext.getResources().getColor(R.color.firstPlayers_visitingTeam_bg));// 设置第二条线颜色
        myLineChartCorner.setMargin(DisplayUtil.dip2px(mContext, 16));// 设置边距
        myLineChartCorner.setXscale(DisplayUtil.px2dip(mContext, 6));// 设置X轴长度
        myLineChartCorner.setYscale(DisplayUtil.px2dip(mContext, 6));// 设置Y轴长度
        myLineChartCorner.setmTextSize(DisplayUtil.dip2px(mContext, 10));// XY轴字体大小
        myLineChartCorner.setmLineWidth(DisplayUtil.dip2px(mContext, 1));// 线条宽度
        myLineChartCorner.setmCircleSize(DisplayUtil.dip2px(mContext, 3));// 圆点大小
    }

    private boolean isHttpData = false;// 是否有请求到后台数据

    /**
     * 初始化数据
     */
    public void initData() {
        boolean mStart = StadiumFragment.isStart;// 判断是否完场
        if (!mStart) {
            // 显示走势图
            fl_attackTrend_loading.setVisibility(View.GONE);
            fl_attackTrend_networkError.setVisibility(View.GONE);
            ll_trend_main.setVisibility(View.VISIBLE);
            showData(StadiumFragment.homeCorners, StadiumFragment.guestCorners, myLineChartCorner, ff_corner);// 显示角球数据
            showData(StadiumFragment.homeDangers, StadiumFragment.guestDangers, myLineChartAttack, ff);// 显示攻防数据
        } else {
            if (!isHttpData) {
                // 请求后台数据
                getVolleyData();
            }
        }
    }

    /**
     * 显示数据走势图
     */
    private void showData(List<Integer> mHDList, List<Integer> mGDList, MyLineChart myLineChart, FrameLayout mff) {
        // 动态判断Y轴刻度
        if (mHDList.size() != 0 && mGDList.size() != 0) {
            // 获取Y轴需要显示的最大值
            int hc = 0;
            int gc = 0;
            for (Integer homeMax : mHDList) {
                if (homeMax > hc) {
                    hc = homeMax;
                }
            }
            for (Integer guestMax : mGDList) {
                if (guestMax > gc) {
                    gc = guestMax;
                }
            }
            // 动态设置Y轴刻度
            if (mHDList.size() > 1 && mGDList.size() > 1) {
                if (hc >= gc) {
                    int hcCount = (int) Math.ceil((hc / 4D)) * 4;
                    if (hcCount < 4) {
                        hcCount = 4;// 设置最小刻度
                    }
                    myLineChart.setYlabel(new String[]{"0", hcCount / 4 + "", (hcCount / 4) * 2 + "", (hcCount / 4) * 3 + "", (hcCount / 4) * 4 + ""});// 设置Y轴刻度值
                } else {
                    int gcCount = (int) Math.ceil((gc / 4D)) * 4;
                    if (gcCount < 4) {
                        gcCount = 4;
                    }
                    myLineChart.setYlabel(new String[]{"0", gcCount / 4 + "", (gcCount / 4) * 2 + "", (gcCount / 4) * 3 + "", (gcCount / 4) * 4 + ""});// 设置Y轴刻度值
                }
            } else {
                myLineChart.setYlabel(new String[]{"0", "2", "4", "6", "8"});// 设置Y轴刻度值
            }
        }

        myLineChart.setData(mHDList);// 设置第一条线数据
        myLineChart.setData2(mGDList);// 设置第二条线数据
        mff.removeAllViews();
        mff.addView(myLineChart);
    }

    /**
     * 请求后台数据
     *
     * @return
     */
    public void getVolleyData() {
        if (getActivity() == null) {
            return;
        }
        mHandler.sendEmptyMessage(STARTLOADING);// 正在加载数据中
        // 获取对象ID
        String mThirdId = ((FootballMatchDetailActivity) getActivity()).mThirdId;
        // 设置参数
        Map<String, String> myPostParams = new HashMap<>();
        myPostParams.put("thirdId", mThirdId);
        L.d("xxxx", "mThirdId   " + mThirdId);
        // 请求数据
        VolleyContentFast.requestJsonByPost(BaseURLs.URL_FOOTBALL_DETAIL_FINDCORNERANDDANGER_INFO, myPostParams, new VolleyContentFast.ResponseSuccessListener<TrendAllBean>() {
            @Override
            public void onResponse(TrendAllBean jsonObject) {
                if (jsonObject != null) {
                    mHomeCorners.clear();
                    mGuestCorners.clear();
                    mHomeDangers.clear();
                    mGuestDangers.clear();
                    mHomeCorners = jsonObject.getHomeCorner();
                    mGuestCorners = jsonObject.getGuestCorner();
                    mHomeDangers = jsonObject.getHomeDanger();
                    mGuestDangers = jsonObject.getGuestDanger();
                    if (mHomeCorners != null && mGuestCorners != null && mHomeDangers != null && mGuestDangers != null) {
                        mHomeCorners.add(0, 0);
                        mGuestCorners.add(0, 0);
                        mHomeDangers.add(0, 0);
                        mGuestDangers.add(0, 0);
                        mHandler.sendEmptyMessage(SUCCESS);// 访问成功
                    } else {
                        mHandler.sendEmptyMessage(ERROR);
                    }
                } else {
                    // 后台没请求到数据
                    mHandler.sendEmptyMessage(ERROR);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERROR);// 访问失败
            }
        }, TrendAllBean.class);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case STARTLOADING:// 正在加载中
                    fl_attackTrend_loading.setVisibility(View.VISIBLE);
                    fl_attackTrend_networkError.setVisibility(View.GONE);
                    ll_trend_main.setVisibility(View.GONE);
                    break;
                case SUCCESS:// 加载成功
                    fl_attackTrend_loading.setVisibility(View.GONE);
                    fl_attackTrend_networkError.setVisibility(View.GONE);
                    ll_trend_main.setVisibility(View.VISIBLE);
                    isHttpData = true;
                    showData(mHomeCorners, mGuestCorners, myLineChartCorner, ff_corner);// 显示角球数据
                    showData(mHomeDangers, mGuestDangers, myLineChartAttack, ff);// 显示攻防数据
                    break;
                case ERROR:// 加载失败
                    fl_attackTrend_loading.setVisibility(View.GONE);
                    fl_attackTrend_networkError.setVisibility(View.VISIBLE);
                    ll_trend_main.setVisibility(View.GONE);
                    isHttpData = false;
                    break;
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {// 显示
            initData();
        }
        super.onHiddenChanged(hidden);


    }
}
