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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivityTest;
import com.hhly.mlottery.bean.footballDetails.DataStatisInfo;
import com.hhly.mlottery.bean.footballDetails.MathchStatisInfo;
import com.hhly.mlottery.bean.footballDetails.TrendAllBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.StadiumUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.MyLineChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang gang
 * @date 2016/6/12 11:21
 * @des 足球内页改版统计
 */
public class StatisticsFragment extends Fragment {


    private static String STA_PARM = "STA_PARM";

    private String type = "";
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


    /***
     * 统计
     */


    private FrameLayout fl_cornerTrend_loading;// 正在加载中
    private FrameLayout fl_cornerTrend_networkError;// 访问失败

    private TextView reLoadin;// 刷新
    private DataStatisInfo.HomeStatisEntity mHomeStatisEntity;//主队统计数据
    private DataStatisInfo.GuestStatisEntity mGuestStatisEntity;//客队统计数据
    private ProgressBar prohome_team, prohome_trapping, prohome_foul, prohome_offside, prohome_freehit, prohome_lineout;// 主队
    private TextView home_team_txt, home_trapping_txt, home_foul_txt, home_offside_txt, home_freehit_txt, home_lineout_txt;
    private TextView guest_team_txt, guest_trapping_txt, guest_foul_txt, guest_offside_txt, guest_freehit_txt, guest_lineout_txt;//客队射门数
    private int dataSize;
    private RelativeLayout layout_match_bottom;//统计数据layout

    private MathchStatisInfo mMathchStatisInfo;


    public static StatisticsFragment newInstance() {


        StatisticsFragment fragment = new StatisticsFragment();

        return fragment;
    }


    public static StatisticsFragment newInstance(String type) {

        Bundle args = new Bundle();
        args.putString(STA_PARM, type);
        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_statistics, container, false);
        mContext = getActivity();
       /* Bundle args = getArguments();
        if (args != null) {
            type = args.getString(STA_PARM);
        }*/
        initView();
        //  loadData();
        return mView;
    }


    public void setType(String type) {
        this.type = type;
        loadData();
        initEvent();

    }


    private void loadData() {
        if ("-1".equals(type)) {
            getVolleyData();
            getVolleyDataStatic();

        } else { //未完场
            initData(type);
            initJson(type);
        }

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


        reLoadin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 请求数据
                getVolleyDataStatic();
            }
        });
        // 走势图滚动监听
     /*   sv_attack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        if (sv_attack.getScrollY() != 0) {// 处于顶部
                            if (getActivity() != null) {
                              //  ((FootballMatchDetailActivityTest) getActivity()).mRefreshLayout.setEnabled(false);
                            }
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (getActivity() != null) {
                          //  ((FootballMatchDetailActivityTest) getActivity()).mRefreshLayout.setEnabled(true);
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

        reLoading = (TextView) mView.findViewById(R.id.reLoading);// 刷新走势图
        // 攻防走势图控件
        myLineChartAttack = new MyLineChart(mContext);
      //  myLineChartAttack.setXlabel(new String[]{"0", "", "", "", "","", "","", "","45'", "", "","", "","", "","", "", "90'"});// 设置X轴刻度值
        myLineChartAttack.setXlabel(new String[]{"0", "5", "10",  "40", "45'", "", "", "90'"});// 设置X轴刻度值

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
        //myLineChartCorner.setXlabel(new String[]{"0", "", "", "", "","", "","", "","45'", "", "","", "","", "","", "", "90'"});// 设置X轴刻度值
        myLineChartCorner.setXlabel(new String[]{"0","5","10","40","45'", "", "", "90'"});// 设置X轴刻度值

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


        /***
         * 统计图
         */


        fl_cornerTrend_loading = (FrameLayout) mView.findViewById(R.id.fl_cornerTrend_loading);
        fl_cornerTrend_networkError = (FrameLayout) mView.findViewById(R.id.fl_cornerTrend_networkError);
        layout_match_bottom = (RelativeLayout) mView.findViewById(R.id.layout_match_bottom);
        reLoadin = (TextView) mView.findViewById(R.id.reLoadin);// 刷新统计图


        //主（客）队进度条
        prohome_team = (ProgressBar) mView.findViewById(R.id.prohome_team);

        prohome_trapping = (ProgressBar) mView.findViewById(R.id.prohome_trapping);

        prohome_foul = (ProgressBar) mView.findViewById(R.id.prohome_foul);

        prohome_offside = (ProgressBar) mView.findViewById(R.id.prohome_offside);

        prohome_freehit = (ProgressBar) mView.findViewById(R.id.prohome_freeHit);

        prohome_lineout = (ProgressBar) mView.findViewById(R.id.prohome_lineout);


        //主（客）队进度数
        home_team_txt = (TextView) mView.findViewById(R.id.home_team_txt);
        guest_team_txt = (TextView) mView.findViewById(R.id.guest_team_txt);

        home_trapping_txt = (TextView) mView.findViewById(R.id.home_trapping_txt);
        guest_trapping_txt = (TextView) mView.findViewById(R.id.guest_trapping_txt);

        home_foul_txt = (TextView) mView.findViewById(R.id.home_foul_txt);
        guest_foul_txt = (TextView) mView.findViewById(R.id.guest_foul_txt);

        home_offside_txt = (TextView) mView.findViewById(R.id.home_offside_txt);
        guest_offside_txt = (TextView) mView.findViewById(R.id.guest_offside_txt);

        home_freehit_txt = (TextView) mView.findViewById(R.id.home_freeHit_txt);
        guest_freehit_txt = (TextView) mView.findViewById(R.id.guest_freeHit_txt);

        home_lineout_txt = (TextView) mView.findViewById(R.id.home_lineout_txt);
        guest_lineout_txt = (TextView) mView.findViewById(R.id.guest_lineout_txt);

    }


    public void setList(List<Integer> hCorners, List<Integer> gCorners, List<Integer> hDangers, List<Integer> gDangers) {
        mHomeCorners = hCorners;
        mGuestCorners = gCorners;
        mHomeDangers = hDangers;
        mGuestDangers = gDangers;
    }

    /**
     * 初始化数据走势图数据
     */
    public void initData(String id) {

        if ("0".equals(id)) {
            ArrayList<Integer> arrayList = new ArrayList<>();

            showData(arrayList, arrayList, myLineChartCorner, ff_corner);// 显示角球数据
            showData(arrayList, arrayList, myLineChartAttack, ff);// 显示攻防数据

        } else if ("1".equals(id)) {

            showData(mHomeCorners, mGuestCorners, myLineChartCorner, ff_corner);// 显示角球数据
            showData(mHomeDangers, mGuestDangers, myLineChartAttack, ff);// 显示攻防数据

        } else if ("-1".equals(id)) {
            // getVolleyData();
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
     * 请求走势图后台数据
     *
     * @return
     */
    public void getVolleyData() {
        if (getActivity() == null) {
            return;
        }
        mHandler.sendEmptyMessage(STARTLOADING);// 正在加载数据中
        // 获取对象ID
        String mThirdId = ((FootballMatchDetailActivityTest) getActivity()).mThirdId;
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


    /**
     * 请求统计图后台数据
     *
     * @return
     */
    public void getVolleyDataStatic() {

        L.d("456", "总计");
        mHandlerStatics.sendEmptyMessage(STARTLOADING);// 正在加载中
        Map<String, String> map = new HashMap<>();
        if (getActivity() == null) {
            return;
        } else {
            map.put("thirdId", ((FootballMatchDetailActivityTest) getActivity()).mThirdId);

            L.i("dddffdfd");

            VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_STATISTICAL_DATA_INFO, map, new VolleyContentFast.ResponseSuccessListener<DataStatisInfo>() {
                @Override
                public void onResponse(DataStatisInfo json) {
                    if (json != null && "200".equals(json.getResult())) {
                        mHomeStatisEntity = json.getHomeStatis();
                        mGuestStatisEntity = json.getGuestStatis();
                        //initJson("-1");//初始化json数据，绑定txt
                        mHandlerStatics.sendEmptyMessage(SUCCESS);// 访问成功
                    }
                }
            }, new VolleyContentFast.ResponseErrorListener() {
                @Override
                public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                    L.i("init", "====initFailed===" + exception.getVolleyError().getMessage());
                    mHandlerStatics.sendEmptyMessage(ERROR);
                }
            }, DataStatisInfo.class);
        }

    }

    public void setMathchStatisInfo(MathchStatisInfo m) {
        mMathchStatisInfo = m;
    }


    /***
     * 初始化统计图数据
     *
     * @param id
     */
    public void initJson(String id) {

        //未开赛
        if ("0".equals(id)) {
            prohome_team.setProgress(50);
            prohome_trapping.setProgress(50);
            prohome_foul.setProgress(50);
            prohome_offside.setProgress(50);
            prohome_freehit.setProgress(50);
            prohome_lineout.setProgress(50);


            guest_team_txt.setText("");
            home_team_txt.setText("");

            guest_trapping_txt.setText("");
            home_trapping_txt.setText("");

            guest_foul_txt.setText("");
            home_foul_txt.setText("");

            guest_offside_txt.setText("");
            home_offside_txt.setText("");

            guest_freehit_txt.setText("");
            home_freehit_txt.setText("");

            guest_lineout_txt.setText("");
            home_lineout_txt.setText("");

        } else if ("1".equals(id)) {//正在比赛

            if (mMathchStatisInfo == null) {
                return;
            }


            prohome_team.setProgress((int) StadiumUtils.computeProgressbarPercent(mMathchStatisInfo.getHome_shoot_door(), mMathchStatisInfo.getGuest_shoot_door()));
            prohome_trapping.setProgress((int) StadiumUtils.computeProgressbarPercent(mMathchStatisInfo.getHome_rescue(), mMathchStatisInfo.getGuest_rescue()));
            prohome_foul.setProgress((int) StadiumUtils.computeProgressbarPercent(mMathchStatisInfo.getHome_foul(), mMathchStatisInfo.getGuest_foul()));
            prohome_offside.setProgress((int) StadiumUtils.computeProgressbarPercent(mMathchStatisInfo.getHome_away(), mMathchStatisInfo.getGuest_away()));
            prohome_freehit.setProgress((int) StadiumUtils.computeProgressbarPercent(mMathchStatisInfo.getHome_free_kick(), mMathchStatisInfo.getGuest_free_kick()));
            prohome_lineout.setProgress((int) StadiumUtils.computeProgressbarPercent(mMathchStatisInfo.getHome_lineOut(), mMathchStatisInfo.getGuest_lineOut()));


            guest_team_txt.setText(mMathchStatisInfo.getGuest_shoot_door() + "");
            home_team_txt.setText(mMathchStatisInfo.getHome_shoot_door() + "");

            guest_trapping_txt.setText(mMathchStatisInfo.getGuest_rescue() + "");
            home_trapping_txt.setText(mMathchStatisInfo.getHome_rescue() + "");

            guest_foul_txt.setText(mMathchStatisInfo.getGuest_foul() + "");
            home_foul_txt.setText(mMathchStatisInfo.getHome_foul() + "");

            guest_offside_txt.setText(mMathchStatisInfo.getGuest_away() + "");
            home_offside_txt.setText(mMathchStatisInfo.getHome_away() + "");

            guest_freehit_txt.setText(mMathchStatisInfo.getGuest_free_kick() + "");
            home_freehit_txt.setText(mMathchStatisInfo.getHome_free_kick() + "");

            guest_lineout_txt.setText(mMathchStatisInfo.getGuest_lineOut() + "");
            home_lineout_txt.setText(mMathchStatisInfo.getHome_lineOut() + "");

        } else if ("-1".equals(id)) {//完场
            // getVolleyDataStatic();
        }
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
                    showData(mHomeCorners, mGuestCorners, myLineChartCorner, ff_corner);// 显示角球数据
                    showData(mHomeDangers, mGuestDangers, myLineChartAttack, ff);// 显示攻防数据
                    break;
                case ERROR:// 加载失败
                    fl_attackTrend_loading.setVisibility(View.GONE);
                    fl_attackTrend_networkError.setVisibility(View.VISIBLE);
                    ll_trend_main.setVisibility(View.GONE);
                    break;
            }
        }
    };


    private Handler mHandlerStatics = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case STARTLOADING:// 正在加载中
                    fl_cornerTrend_loading.setVisibility(View.VISIBLE);
                    fl_cornerTrend_networkError.setVisibility(View.GONE);
                    layout_match_bottom.setVisibility(View.GONE);
                    break;
                case SUCCESS:// 访问成功
                    fl_cornerTrend_loading.setVisibility(View.GONE);
                    fl_cornerTrend_networkError.setVisibility(View.GONE);
                    layout_match_bottom.setVisibility(View.VISIBLE);
                    loadStatics();
                    break;
                case ERROR:// 访问失败
                    fl_cornerTrend_loading.setVisibility(View.GONE);
                    fl_cornerTrend_networkError.setVisibility(View.VISIBLE);
                    layout_match_bottom.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
        }
    };

    private void loadStatics() {
        prohome_team.setProgress((int) StadiumUtils.computeProgressbarPercent(mHomeStatisEntity.getAllShots(), mGuestStatisEntity.getAllShots()));
        prohome_trapping.setProgress((int) StadiumUtils.computeProgressbarPercent(mHomeStatisEntity.getTrapping(), mGuestStatisEntity.getTrapping()));
        prohome_foul.setProgress((int) StadiumUtils.computeProgressbarPercent(mHomeStatisEntity.getFoul(), mGuestStatisEntity.getFoul()));
        prohome_offside.setProgress((int) StadiumUtils.computeProgressbarPercent(mHomeStatisEntity.getOffside(), mGuestStatisEntity.getOffside()));
        prohome_freehit.setProgress((int) StadiumUtils.computeProgressbarPercent(mHomeStatisEntity.getFreeHit(), mGuestStatisEntity.getFreeHit()));
        prohome_lineout.setProgress((int) StadiumUtils.computeProgressbarPercent(mHomeStatisEntity.getLineOut(), mGuestStatisEntity.getLineOut()));


        home_team_txt.setText(mHomeStatisEntity.getAllShots() + "");
        guest_team_txt.setText(mGuestStatisEntity.getAllShots() + "");

        home_trapping_txt.setText(mHomeStatisEntity.getTrapping() + "");
        guest_trapping_txt.setText(mGuestStatisEntity.getTrapping() + "");

        home_foul_txt.setText(mHomeStatisEntity.getFoul() + "");
        guest_foul_txt.setText(mGuestStatisEntity.getFoul() + "");

        guest_offside_txt.setText(mGuestStatisEntity.getOffside() + "");
        home_offside_txt.setText(mHomeStatisEntity.getOffside() + "");

        guest_freehit_txt.setText(mGuestStatisEntity.getFreeHit() + "");
        home_freehit_txt.setText(mHomeStatisEntity.getFreeHit() + "");


        guest_lineout_txt.setText(mGuestStatisEntity.getLineOut() + "");
        home_lineout_txt.setText(mHomeStatisEntity.getLineOut() + "");

    }

}
