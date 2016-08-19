package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivityTest;
import com.hhly.mlottery.adapter.football.EventAdapter;
import com.hhly.mlottery.bean.footballDetails.DataStatisInfo;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.bean.footballDetails.MatchTimeLiveBean;
import com.hhly.mlottery.bean.footballDetails.MathchStatisInfo;
import com.hhly.mlottery.bean.footballDetails.trend.Bean;
import com.hhly.mlottery.bean.footballDetails.trend.FootballTrendBean;
import com.hhly.mlottery.bean.footballDetails.trend.TrendFormBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.StadiumUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.MyLineChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author wang gang
 * @date 2016/6/12 11:21
 * @des 足球内页改版直播(足球事件直播, 走勢統計)
 */
public class StatisticsFragment extends Fragment {


    private static String STA_PARM = "STA_PARM";

    private static final String HOME = "1"; //主队
    private static final String GUEST = "0"; //客队


    //主队事件
    private static final String SCORE = "1029";//主队进球
    private static final String RED_CARD = "1032";
    private static final String YELLOW_CARD = "1034";
    private static final String CORNER = "1025";
    //客队事件
    private static final String SCORE1 = "2053";//客队进球
    private static final String RED_CARD1 = "2056";
    private static final String YELLOW_CARD1 = "2058";
    private static final String CORNER1 = "2049";

    /**
     * 上半场
     */
    private static final String FIRSTHALF = "1";
    /**
     * 中场
     */
    private static final String HALFTIME = "2";


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

    private String eventType;


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


    private RadioGroup radioGroup;

    private LinearLayout rl_event;
    private RelativeLayout ll_statistics;

    private NestedScrollView mNestedScrollView_trend;
    private NestedScrollView mNestedScrollView_event;
    private NestedScrollView mNestedScrollView_nodata;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private EventAdapter eventAdapter;

    private List<MatchTimeLiveBean> eventMatchLive = new ArrayList<>();


    /***
     * 走势图
     */

    private static final String TREND_DEFAULT = "0"; //维度
    private static final String TREND_CORNER = "1"; //角球
    private static final String TREND_GOAL = "2";  //进球


    private LineChart chart_shoot;  //射正  //射正+进球
    private LineChart chart_shootAside; //射偏   //射偏
    private LineChart chart_dangerousAttack; //危险进攻  //危险进攻
    private LineChart chart_attack;  //进攻  //进攻


    private List<Entry> shootHomeValues;
    private List<Entry> shootGuestValues;
    private List<Entry> shootAsideHomeValues;
    private List<Entry> shootAsideGuestValues;
    private List<Entry> dangerousAttackHomeValues;
    private List<Entry> dangerousAttackGuestValues;
    private List<Entry> attackHomeValues;
    private List<Entry> attackGuestValues;

    private List<Integer> shootHomeColors;
    private List<Integer> shootGuestColors;
    private List<Integer> shootAsideHomeColors;
    private List<Integer> shootAsideGuestColors;
    private List<Integer> dangerousAttackHomeColors;
    private List<Integer> dangerousAttackGuestColors;
    private List<Integer> attackHomeColors;
    private List<Integer> attackGuestColors;


    private TrendFormBean trendFormBean;


    private XAxis shotXAxis;
    private YAxis shotYAxis;

    private XAxis shotAsideXAxis;
    private YAxis shotAsideYAxis;

    private XAxis dangerousAttackXAxis;
    private YAxis dangerousAttackYAxis;

    private XAxis attackXAxis;
    private YAxis attackYAxis;

    private static final float HALFMATCH = 5f;
    private static final float FINISHMATCH = 10f;


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

         /*   shootHomeValues = new ArrayList<>();
            shootHomeValues.add(new Entry(0f, 0f));
            shootHomeValues.add(new Entry(2f, 3f));
            shootHomeValues.add(new Entry(3f, 4f));
            shootHomeValues.add(new Entry(6f, 5f));
            shootHomeValues.add(new Entry(10f, 7f));
            shootHomeValues.add(new Entry(17f, 9f));

            shootGuestValues = new ArrayList<>();
            shootGuestValues.add(new Entry(0f, 0f));

            shootGuestValues.add(new Entry(1f, 1f));
            shootGuestValues.add(new Entry(3f, 1f));
            shootGuestValues.add(new Entry(5f, 3f));
            shootGuestValues.add(new Entry(10f, 7f));
            shootGuestValues.add(new Entry(15f, 7f));
            shootGuestValues.add(new Entry(21f, 10f));

            shotXAxis.setAxisMaxValue(getXMaxValue(21f, HALFMATCH));
            shotXAxis.setLabelCount(getXLabelCount(21f, HALFMATCH));


            shotYAxis.setAxisMaxValue(getYMaxValue(10f));
            shotYAxis.setLabelCount(getLabelCount(10f));


            showTrendData(chart_shoot, shootHomeValues, shootGuestValues);*/


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
        radioGroup = (RadioGroup) mView.findViewById(R.id.radio_group);
        mNestedScrollView_event = (NestedScrollView) mView.findViewById(R.id.nested_scroll_view_event);
        mNestedScrollView_trend = (NestedScrollView) mView.findViewById(R.id.nested_scroll_view_trend);
        mNestedScrollView_nodata = (NestedScrollView) mView.findViewById(R.id.nested_scroll_view_nodata);

        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        mNestedScrollView_event.setFillViewport(true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.live_event:
                        if (!eventType.equals("0")) {
                            mNestedScrollView_event.setVisibility(View.VISIBLE);
                            mNestedScrollView_trend.setVisibility(View.GONE);
                        }

                        break;
                    case R.id.live_statistics:
                        if (!eventType.equals("0")) {
                            mNestedScrollView_event.setVisibility(View.GONE);
                            mNestedScrollView_trend.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
            }
        });


        ll_trend_main = (LinearLayout) mView.findViewById(R.id.ll_trend_main);
        ff = (FrameLayout) mView.findViewById(R.id.fl_main);
        ff_corner = (FrameLayout) mView.findViewById(R.id.fl_main_corner);
        fl_attackTrend_loading = (FrameLayout) mView.findViewById(R.id.fl_attackTrend_loading);
        fl_attackTrend_networkError = (FrameLayout) mView.findViewById(R.id.fl_attackTrend_networkError);
        //  sv_attack = (ScrollView) mView.findViewById(R.id.sv_attack);

        reLoading = (TextView) mView.findViewById(R.id.reLoading);// 刷新走势图
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


        /**
         * 走势图
         */

        chart_shoot = (LineChart) mView.findViewById(R.id.chart_shoot);
        chart_shootAside = (LineChart) mView.findViewById(R.id.chart_shootAside);
        chart_dangerousAttack = (LineChart) mView.findViewById(R.id.chart_dangerousAttack);
        chart_attack = (LineChart) mView.findViewById(R.id.chart_attack);


        initChartView(chart_shoot, "暂无射正球门数据.");
        initChartView(chart_shootAside, "暂无射偏球门数据.");
        initChartView(chart_dangerousAttack, "暂无危险进攻数据.");
        initChartView(chart_attack, "暂无进攻数据.");


        shotYAxis = chart_shoot.getAxisLeft();
        shotYAxis.setAxisMinValue(0f);
        shotYAxis.enableGridDashedLine(10f, 10f, 0f);  //Y轴横向虚线
        shotYAxis.setDrawZeroLine(false);
        shotYAxis.setAxisLineColor(Color.BLACK);
        shotXAxis = chart_shoot.getXAxis();   //x轴
        shotXAxis.setAxisLineColor(Color.BLACK);
        shotXAxis.enableGridDashedLine(10f, 10f, 0f);
        shotXAxis.setAxisMinValue(0f);


        shotAsideYAxis = chart_shootAside.getAxisLeft();
        shotAsideYAxis.setAxisMinValue(0f);
        shotAsideYAxis.enableGridDashedLine(10f, 10f, 0f);  //Y轴横向虚线
        shotAsideYAxis.setDrawZeroLine(false);
        shotAsideXAxis = chart_shootAside.getXAxis();   //x轴
        shotAsideXAxis.enableGridDashedLine(10f, 10f, 0f);
        shotAsideXAxis.setAxisMinValue(0f);

        dangerousAttackYAxis = chart_dangerousAttack.getAxisLeft();
        dangerousAttackYAxis.setAxisMinValue(0f);
        dangerousAttackYAxis.enableGridDashedLine(10f, 10f, 0f);  //Y轴横向虚线
        dangerousAttackYAxis.setDrawZeroLine(false);
        dangerousAttackXAxis = chart_dangerousAttack.getXAxis();   //x轴
        dangerousAttackXAxis.enableGridDashedLine(10f, 10f, 0f);
        dangerousAttackXAxis.setAxisMinValue(0f);

        attackYAxis = chart_attack.getAxisLeft();
        attackYAxis.setAxisMinValue(0f);
        attackYAxis.enableGridDashedLine(10f, 10f, 0f);  //Y轴横向虚线
        attackYAxis.setDrawZeroLine(false);
        attackXAxis = chart_attack.getXAxis();   //x轴
        attackXAxis.enableGridDashedLine(10f, 10f, 0f);
        attackXAxis.setAxisMinValue(0f);

    }


    private void initChartView(LineChart mChart, String msg) {
        mChart.setDrawGridBackground(false);
        mChart.setDescription("时间(T)");
        mChart.setNoDataTextDescription(msg);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getAxisLeft().setAxisLineWidth(1f);
        mChart.getXAxis().setAxisLineWidth(1f);
        Legend l_shoot = mChart.getLegend();
        l_shoot.setForm(Legend.LegendForm.LINE);

    }


    /**
     * 足球事件直播
     *
     * @param livestatus
     * @param matchTimeLiveBeanMs
     */
    public void setEventMatchLive(String livestatus, List<MatchTimeLiveBean> matchTimeLiveBeanMs) {
        //统计事件个数
        this.eventMatchLive = matchTimeLiveBeanMs;
        eventType = livestatus;

        if ("0".equals(livestatus)) {
            mNestedScrollView_nodata.setVisibility(View.VISIBLE);
            mNestedScrollView_event.setVisibility(View.GONE);
            mNestedScrollView_trend.setVisibility(View.GONE);
        } else if ("1".equals(livestatus) || "-1".equals(livestatus)) {   //-1代表完场  1代表直播中
            mNestedScrollView_nodata.setVisibility(View.GONE);
            mNestedScrollView_event.setVisibility(View.VISIBLE);
            mNestedScrollView_trend.setVisibility(View.VISIBLE);
            computeEventNum(livestatus);
            eventAdapter = new EventAdapter(mContext, eventMatchLive);
            recyclerView.setAdapter(eventAdapter);
        }
    }

    public void updateRecycleView(String status) {
        computeEventNum(status);
        eventAdapter.notifyDataSetChanged();
    }


    /***
     * code=261时,取playinfo信息
     */

    public void setPlayInfo(MatchTextLiveBean matchTextLiveBean) {
        Iterator<MatchTimeLiveBean> iterator = eventMatchLive.iterator();
        while (iterator.hasNext()) {
            MatchTimeLiveBean bean = iterator.next();
            if (bean.getEnNum().equals(matchTextLiveBean.getCancelEnNum())) {//取消进球等事件的判断
                iterator.remove();//用xMatchLive.remove会有异常
            }
        }
    }

    /**
     * 增加对应的事件直播
     * <p/>
     * eventMatchTimeLiveList.add(new MatchTimeLiveBean(matchTextLiveBean.getTime(), matchTextLiveBean.getCode(), matchTextLiveBean.getHomeScore() + " : " + matchTextLiveBean.getGuestScore(), matchTextLiveBean.getMsgId(), HALFTIME, matchTextLiveBean.getPlayInfo(), 0));
     */
    public void addFootBallEvent(MatchTextLiveBean matchTextLiveBean) {
        String place = matchTextLiveBean.getMsgPlace();
        if (matchTextLiveBean.getMsgPlace().equals("2")) {//客队
            place = "0";  //客队
        }
        if ("1".equals(matchTextLiveBean.getCode())) {   //code=1时,上班场结束status=2
            eventMatchLive.add(new MatchTimeLiveBean(matchTextLiveBean.getTime(), matchTextLiveBean.getCode(), matchTextLiveBean.getHomeScore() + " : " + matchTextLiveBean.getGuestScore(), matchTextLiveBean.getMsgId(), HALFTIME, matchTextLiveBean.getPlayInfo(), matchTextLiveBean.getEnNum(), 0));
        } else if ("3".equals(matchTextLiveBean.getCode())) { //code=3时，下半场结束  status=-1
            eventMatchLive.add(new MatchTimeLiveBean(matchTextLiveBean.getTime(), matchTextLiveBean.getCode(), matchTextLiveBean.getHomeScore() + " : " + matchTextLiveBean.getGuestScore(), matchTextLiveBean.getMsgId(), "-1", matchTextLiveBean.getPlayInfo(), matchTextLiveBean.getEnNum(), 0));
        } else {
            eventMatchLive.add(new MatchTimeLiveBean(matchTextLiveBean.getTime(), matchTextLiveBean.getCode(), place, matchTextLiveBean.getMsgId(), matchTextLiveBean.getState(), matchTextLiveBean.getPlayInfo(), matchTextLiveBean.getEnNum(), 0));
        }
    }


    /**
     * 取消对应的事件直播
     */
    public void cancelFootBallEvent(MatchTextLiveBean matchTextLiveBean) {
        Iterator<MatchTimeLiveBean> iterator = eventMatchLive.iterator();
        while (iterator.hasNext()) {
            MatchTimeLiveBean bean = iterator.next();
            if (bean.getEnNum().equals(matchTextLiveBean.getCancelEnNum())) {//取消进球等事件的判断
                iterator.remove();//用xMatchLive.remove会有异常
            }
        }
    }

    private void computeEventNum(String status) {
        int homeGoal = 0;
        int homeRc = 0;
        int homeYc = 0;
        int homeCorner = 0;
        int guestGoal = 0;
        int guestRc = 0;
        int guestYc = 0;
        int guestCorner = 0;
        int statusEqual2 = 0; //计算List里面出现的state=2 code=1多次出现，只需要一个

        Iterator<MatchTimeLiveBean> iterator = eventMatchLive.iterator();
        while (iterator.hasNext()) {
            MatchTimeLiveBean m = iterator.next();

            if (HALFTIME.equals(m.getState()) && "1".equals(m.getCode())) {
                if (statusEqual2 == 1) {
                    iterator.remove();
                } else {
                    statusEqual2++;
                }

            } else {
                if (HOME.equals(m.getIsHome())) {
                    if (SCORE.equals(m.getCode())) {
                        homeGoal++;
                        m.setEventnum(homeGoal);
                    } else if (YELLOW_CARD.equals(m.getCode())) {
                        homeYc++;
                        m.setEventnum(homeYc);
                    } else if (RED_CARD.equals(m.getCode())) {
                        homeRc++;
                        m.setEventnum(homeRc);
                    } else if (CORNER.equals(m.getCode())) {
                        homeCorner++;
                        m.setEventnum(homeCorner);
                    }
                } else {
                    if (SCORE1.equals(m.getCode())) {
                        guestGoal++;
                        m.setEventnum(guestGoal);
                    } else if (YELLOW_CARD1.equals(m.getCode())) {
                        guestYc++;
                        m.setEventnum(guestYc);
                    } else if (RED_CARD1.equals(m.getCode())) {
                        guestRc++;
                        m.setEventnum(guestRc);
                    } else if (CORNER1.equals(m.getCode())) {
                        guestCorner++;
                        m.setEventnum(guestCorner);
                    }
                }
            }
        }

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
     * 改版走势图
     */


    private int getLabelCount(float yMax) {
        return (int) yMax / (int) Math.ceil(yMax / 8f) + 1;
    }

    private float getYMaxValue(float yMax) {
        return ((int) Math.ceil(yMax / 8f)) * getLabelCount(yMax);
    }

    private int getXLabelCount(float xMax, float range) {
        return xMax % range == 0 ? (int) Math.ceil(xMax / range) + 1 : (int) Math.ceil(xMax / range);
    }

    private float getXMaxValue(float xMax, float range) {
        return getXLabelCount(xMax, range) * range;
    }


    /**
     * 走势图计算数据
     */


    private void computeTrendData() {

        /**
         * x轴为分钟  y轴为个数
         */

        //射正
        Iterator<Bean> shotHomeIterator = trendFormBean.getShot().getHome().iterator();
        int shotHome = 0;
        shootHomeValues = new ArrayList<>();
        shootHomeColors = new ArrayList<>();

        shootHomeValues.add(new Entry(0f, 0f));
        shootHomeColors.add(Color.TRANSPARENT);

        while (shotHomeIterator.hasNext()) {
            Bean bean = shotHomeIterator.next();
            if (TREND_CORNER.equals(bean.getFlag())) {
                shootHomeColors.add(Color.GREEN);
            } else if (TREND_GOAL.equals(bean.getFlag())) {
                shotHome++;
                shootHomeColors.add(Color.BLACK);
            } else {
                shotHome++;
                shootHomeColors.add(Color.TRANSPARENT);
            }

            shootHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), shotHome));
        }


        Iterator<Bean> shotGuestIterator = trendFormBean.getShot().getGuest().iterator();
        int shotGuest = 0;
        shootGuestValues = new ArrayList<>();
        shootGuestColors = new ArrayList<>();

        shootGuestValues.add(new Entry(0f, 0f));
        shootGuestColors.add(Color.TRANSPARENT);

        while (shotGuestIterator.hasNext()) {
            Bean bean = shotGuestIterator.next();
            if (TREND_CORNER.equals(bean.getFlag())) {
                shootGuestColors.add(Color.GREEN);
            } else if (TREND_GOAL.equals(bean.getFlag())) {
                shotGuest++;
                shootGuestColors.add(Color.BLACK);
            } else {
                shotGuest++;
                shootGuestColors.add(Color.TRANSPARENT);
            }
            shootGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), shotGuest));
        }


        shotXAxis.setAxisMaxValue(getXMaxValue(90f, FINISHMATCH));
        shotXAxis.setLabelCount(getXLabelCount(90f, FINISHMATCH));


        if (shotHome > shotGuest) {
            shotYAxis.setAxisMaxValue(getYMaxValue(shotHome));
            shotYAxis.setLabelCount(getLabelCount(shotHome));
        } else {
            shotYAxis.setAxisMaxValue(getYMaxValue(shotGuest));
            shotYAxis.setLabelCount(getLabelCount(shotGuest));
        }

        showTrendData(chart_shoot, shootHomeValues, shootGuestValues, shootHomeColors, shootGuestColors);


        //射偏
        Iterator<Bean> shotAsideHomeIterator = trendFormBean.getShepian().getHome().iterator();
        int shotAsideHome = 0;
        shootAsideHomeValues = new ArrayList<>();
        while (shotAsideHomeIterator.hasNext()) {
            Bean bean = shotAsideHomeIterator.next();
            if (TREND_DEFAULT.equals(bean.getFlag())) {
                shotAsideHome++;
            }
            shootAsideHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), shotAsideHome));
        }


        Iterator<Bean> shotAsideGuestIterator = trendFormBean.getShepian().getGuest().iterator();
        int shotAsideGuest = 0;
        shootAsideGuestValues = new ArrayList<>();
        while (shotAsideGuestIterator.hasNext()) {
            Bean bean = shotAsideGuestIterator.next();
            if (TREND_DEFAULT.equals(bean.getFlag())) {
                shotAsideGuest++;
            }
            shootAsideGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), shotAsideGuest));
        }

        //危险进攻
        Iterator<Bean> dangerAttackHomeIterator = trendFormBean.getDangerousAttack().getHome().iterator();
        int dangerAttackHome = 0;
        dangerousAttackHomeValues = new ArrayList<>();
        while (dangerAttackHomeIterator.hasNext()) {
            Bean bean = dangerAttackHomeIterator.next();
            if (TREND_DEFAULT.equals(bean.getFlag())) {
                dangerAttackHome++;
            }
            dangerousAttackHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), dangerAttackHome));
        }

        Iterator<Bean> dangerAttackGusetIterator = trendFormBean.getDangerousAttack().getGuest().iterator();
        int dangerAttackGuest = 0;
        dangerousAttackGuestValues = new ArrayList<>();
        while (dangerAttackGusetIterator.hasNext()) {
            Bean bean = dangerAttackGusetIterator.next();
            if (TREND_DEFAULT.equals(bean.getFlag())) {
                dangerAttackGuest++;
            }
            dangerousAttackGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), dangerAttackGuest));
        }

        //进攻
        Iterator<Bean> attackHomeIterator = trendFormBean.getAttack().getHome().iterator();
        int attackHome = 0;
        attackHomeValues = new ArrayList<>();
        while (attackHomeIterator.hasNext()) {
            Bean bean = attackHomeIterator.next();
            if (TREND_DEFAULT.equals(bean.getFlag())) {
                attackHome++;
            }
            attackHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), attackHome));
        }

        Iterator<Bean> attackGusetIterator = trendFormBean.getAttack().getGuest().iterator();
        int attackGuest = 0;
        attackGuestValues = new ArrayList<>();
        while (attackGusetIterator.hasNext()) {
            Bean bean = attackGusetIterator.next();
            if (TREND_DEFAULT.equals(bean.getFlag())) {
                attackGuest++;
            }
            attackGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), attackGuest));
        }
    }


    private void showTrendData(LineChart mChart, List<Entry> homeEntry, List<Entry> guestEntry, List<Integer> homeColors, List<Integer> guestColors) {
        LineDataSet mHomeLineDataSet;
        LineDataSet mGuestLineDataSet;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
           /* mHomeLineDataSet = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            mHomeLineDataSet.setValues(homeEntry);


            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();*/
        } else {


            mHomeLineDataSet = new LineDataSet(homeEntry, "DataSet1");
            mHomeLineDataSet.enableDashedHighlightLine(10f, 5f, 0f);
            mHomeLineDataSet.setColor(Color.RED);
            mHomeLineDataSet.setCircleColor(Color.BLACK);
            mHomeLineDataSet.setLineWidth(0.7f);
            mHomeLineDataSet.setCircleRadius(3f);
            mHomeLineDataSet.setDrawCircleHole(false);
            mHomeLineDataSet.setValueTextSize(9f);
            mHomeLineDataSet.setDrawValues(false);
            mHomeLineDataSet.setCircleColors(homeColors);


            mGuestLineDataSet = new LineDataSet(guestEntry, "DataSet2");
            mGuestLineDataSet.enableDashedHighlightLine(10f, 5f, 0f);
            mGuestLineDataSet.setColor(Color.BLUE);
            mGuestLineDataSet.setCircleColor(Color.BLACK);
            mGuestLineDataSet.setLineWidth(0.7f);
            mGuestLineDataSet.setCircleRadius(3f);
            mGuestLineDataSet.setDrawCircleHole(false);
            mGuestLineDataSet.setValueTextSize(9f);
            mGuestLineDataSet.setDrawValues(false);
            mGuestLineDataSet.setCircleColors(guestColors);


            ArrayList<ILineDataSet> dataSetsList = new ArrayList<ILineDataSet>();
            dataSetsList.add(mHomeLineDataSet);
            dataSetsList.add(mGuestLineDataSet);

            LineData lineData = new LineData(dataSetsList);
            mChart.setData(lineData);
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

        L.d("112233", mThirdId);
        // 设置参数
        Map<String, String> myPostParams = new HashMap<>();
        myPostParams.put("matchId", mThirdId);

        //BaseURLs.URL_FOOTBALL_DETAIL_FINDCORNERANDDANGER_INFO

        String url = "http://192.168.10.242:8181/mlottery/core/trendForm.findTrendForm.do";

        VolleyContentFast.requestJsonByPost(url, myPostParams, new VolleyContentFast.ResponseSuccessListener<FootballTrendBean>() {
            @Override
            public void onResponse(FootballTrendBean jsonObject) {
                if (jsonObject != null) {
                    if (!"200".equals(jsonObject.getResult())) {
                        return;
                    }
                    trendFormBean = jsonObject.getTrendForm();
                    mHandler.sendEmptyMessage(SUCCESS);

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
        }, FootballTrendBean.class);
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

                    L.d("112233", "请求成功");


                    computeTrendData();

                    // showData(mHomeCorners, mGuestCorners, myLineChartCorner, ff_corner);// 显示角球数据
                    // showData(mHomeDangers, mGuestDangers, myLineChartAttack, ff);// 显示攻防数据
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


    private float convertStringToFloat(String time) {
        return (float) Double.parseDouble(time) / 60000;
    }

    public static class Axis {

        private float x;
        private float y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

}
