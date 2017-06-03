package com.hhly.mlottery.frame.footballframe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.football.EventAdapter;
import com.hhly.mlottery.bean.footballDetails.DataStatisInfo;
import com.hhly.mlottery.bean.footballDetails.MatchDetail;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.bean.footballDetails.MatchTimeLiveBean;
import com.hhly.mlottery.bean.footballDetails.MathchStatisInfo;
import com.hhly.mlottery.bean.footballDetails.PlayerInfo;
import com.hhly.mlottery.bean.footballDetails.trend.FootballTrendBean;
import com.hhly.mlottery.bean.footballDetails.trend.TrendBean;
import com.hhly.mlottery.bean.footballDetails.trend.TrendFormBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.FootBallDetailTypeEnum;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.FootballEventComparator;
import com.hhly.mlottery.util.FootballTrendChartComparator;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.StadiumUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.FootballEventView;
import com.hhly.mlottery.widget.TimeView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.hhly.mlottery.config.FootBallTypeEnum.*;

/**
 * @author wang gang
 * @date 2016/6/12 11:21
 * @des 足球内页改版直播(足球事件直播, 走勢統計)
 */
public class LiveFragment extends Fragment implements View.OnClickListener {

    private static final String HOME = "1"; //主队
    private static final String GUEST = "0"; //客队

    /***
     * 走势图
     */
    private static final String TREND_DEFAULT = "0"; //维度
    private static final String TREND_CORNER = "1"; //角球
    private static final String TREND_GOAL = "2";  //进球

    private static final String LIVEENDED = "-1";//直播结束


    private String type = "";
    private View mView;
    private Context mContext;

    private final int ERROR = -1;//访问失败
    private final int SUCCESS = 0;// 访问成功
    private final int STARTLOADING = 1;// 正在加载中

    private LinearLayout ll_trend_main;// 走势图容器
    private FrameLayout fl_attackTrend_loading;// 正在加载中
    private FrameLayout fl_attackTrend_networkError;// 加载失败
    private TextView reLoading;// 刷新

    private String eventType;

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

    private NestedScrollView mNestedScrollView_lineup;
    private NestedScrollView mNestedScrollView_statistics;
    private FrameLayout fl_live_text;
    private NestedScrollView mNestedScrollView_trend;
    private NestedScrollView mNestedScrollView_event;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private EventAdapter eventAdapter;

    private List<MatchTimeLiveBean> eventMatchLive = new ArrayList<>();

    private LinearLayout ll_nodata;
    private LinearLayout ll_event;

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

    private int shotHome;
    private int shotGuest;
    private int shotAsideHome;
    private int shotAsideGuest;
    private int dangerAttackHome;
    private int dangerAttackGuest;
    private int attackHome;
    private int attackGuest;

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

    private TextView tv_home_corner, tv_home_danger, tv_home_shoot_correct, tv_home_shoot_miss;
    private TextView tv_guest_corner, tv_guest_danger, tv_guest_shoot_correct, tv_guest_shoot_miss;
    private ProgressBar pb_corner, pb_danger, pb_shoot_correct, pb_shoot;

    private FrameLayout fl_firsPlayers_not;
    private LinearLayout fl_firsPlayers_content;
    private LinearLayout ll_rosters_homeTeam;
    private LinearLayout ll_rosters_visitingTeam;

    private TextView tv_shot;
    private TextView tv_shotAside;
    private TextView tv_dangerAttack;
    private TextView tv_attack;

    private TextView goChart;

    private Activity mActivity;

    private List<MatchTextLiveBean> matchLive;
    private List<Integer> allMatchLiveMsgId;

    private MatchDetail matchDetail;
    private List<MatchTextLiveBean> trendChartList;
    private List<MatchTimeLiveBean> eventMatchTimeLiveList;
    private String keepTime;
    private final static String BEFOURLIVE = "0";//直播前
    private final static String ONLIVE = "1";//直播中
    private String mPreStatus;

    /**
     * 时间轴的集合
     */
    private List<MatchTimeLiveBean> xMatchLive = new ArrayList<>();

    //时间轴的view
    private TimeView timeView;
    private FootballEventView timeLayoutTop;
    private FootballEventView timeLayoutBottom;

    private NoLiveTextFragment mNoLiveTextFragment;
    private LiveTextFragment mliveTextFragment;
    private FinishMatchLiveTextFragment finishMatchLiveTextFragment;//完场
    private String mThirdId;

    public static LiveFragment newInstance(String thirdId, MatchDetail matchDetail, MathchStatisInfo mathchStatisInfo, List<MatchTimeLiveBean> eventMatchTimeLiveList, List<MatchTextLiveBean> trendChartList, String keepTime) {
        LiveFragment fragment = new LiveFragment();
        Bundle bundle = new Bundle();
        bundle.putString("thirdId", thirdId);
        bundle.putParcelable("matchDetail", matchDetail);
        bundle.putParcelable("mathchStatisInfo", mathchStatisInfo);
        bundle.putParcelableArrayList("trendChartList", (ArrayList<? extends Parcelable>) trendChartList);
        bundle.putParcelableArrayList("eventMatchTimeLiveList", (ArrayList<? extends Parcelable>) eventMatchTimeLiveList);
        bundle.putString("keepTime", keepTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mThirdId = getArguments().getString("thirdId");
            this.matchDetail = getArguments().getParcelable("matchDetail");
            this.mMathchStatisInfo = getArguments().getParcelable("mathchStatisInfo");
            this.trendChartList = getArguments().getParcelableArrayList("trendChartList");
            this.eventMatchTimeLiveList = getArguments().getParcelableArrayList("eventMatchTimeLiveList");
            this.keepTime = getArguments().getString("keepTime");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_lives, container, false);
        mContext = mActivity;
        initView();
        initData();
        return mView;
    }

    private void initData() {
        mPreStatus = matchDetail.getLiveStatus();
        showLineUp();

        if (BEFOURLIVE.equals(matchDetail.getLiveStatus())) { //赛前
            setEventMatchLive(matchDetail, null);
        } else {
            setEventMatchLive(matchDetail, eventMatchTimeLiveList);

            if (LIVEENDED.equals(matchDetail.getLiveStatus())) {//完场
                finishMatchRequest();
            } else if (ONLIVE.equals(matchDetail.getLiveStatus())) {//未完场
                initLiveStatics(matchDetail.getLiveStatus());
                initChartData(matchDetail.getLiveStatus());
                showTimeView(keepTime);
            }
        }
    }

    private void finishMatchRequest() {
        getLiveStatic();
        getTrenFormInfo();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goChart:
                ((FootballMatchDetailActivity) mActivity).mViewPager.setCurrentItem(FootBallDetailTypeEnum.FOOT_DETAIL_CHARTBALL);
                break;

            case R.id.reLoading:
                getTrenFormInfo();
                break;

            case R.id.reLoadin:
                getLiveStatic();
                break;
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {
        radioGroup = (RadioGroup) mView.findViewById(R.id.radio_group);
        fl_live_text = (FrameLayout) mView.findViewById(R.id.fl_live_text);
        mNestedScrollView_lineup = (NestedScrollView) mView.findViewById(R.id.nested_scroll_view_lineup);
        mNestedScrollView_statistics = (NestedScrollView) mView.findViewById(R.id.nested_scroll_view_statistics);
        mNestedScrollView_event = (NestedScrollView) mView.findViewById(R.id.nested_scroll_view_event);
        mNestedScrollView_trend = (NestedScrollView) mView.findViewById(R.id.nested_scroll_view_trend);
        //  mNestedScrollView_nodata = (NestedScrollView) mView.findViewById(R.id.nested_scroll_view_nodata);

        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        ll_nodata = (LinearLayout) mView.findViewById(R.id.nodata);
        ll_event = (LinearLayout) mView.findViewById(R.id.ll_event);

        mNestedScrollView_event.setFillViewport(true);

        //x时间轴
        timeView = (TimeView) mView.findViewById(R.id.time_football);
        timeLayoutTop = (FootballEventView) mView.findViewById(R.id.time_layout_top);
        timeLayoutBottom = (FootballEventView) mView.findViewById(R.id.time_layout_bottom);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {

                    case R.id.live_lineup:
                        mNestedScrollView_lineup.setVisibility(View.VISIBLE);
                        mNestedScrollView_statistics.setVisibility(View.GONE);
                        fl_live_text.setVisibility(View.VISIBLE);
                        mNestedScrollView_event.setVisibility(View.GONE);
                        mNestedScrollView_trend.setVisibility(View.GONE);

                        break;

                    case R.id.live_statistics:
                        mNestedScrollView_lineup.setVisibility(View.GONE);
                        mNestedScrollView_statistics.setVisibility(View.VISIBLE);
                        fl_live_text.setVisibility(View.VISIBLE);
                        mNestedScrollView_event.setVisibility(View.GONE);
                        mNestedScrollView_trend.setVisibility(View.GONE);
                        break;

                    case R.id.live_text:
                        mNestedScrollView_lineup.setVisibility(View.GONE);
                        mNestedScrollView_statistics.setVisibility(View.GONE);
                        fl_live_text.setVisibility(View.VISIBLE);
                        mNestedScrollView_event.setVisibility(View.GONE);
                        mNestedScrollView_trend.setVisibility(View.GONE);

                        break;
                    case R.id.live_event:
                        mNestedScrollView_lineup.setVisibility(View.GONE);
                        mNestedScrollView_statistics.setVisibility(View.GONE);
                        fl_live_text.setVisibility(View.GONE);
                        mNestedScrollView_event.setVisibility(View.VISIBLE);
                        mNestedScrollView_trend.setVisibility(View.GONE);
                        break;
                    case R.id.live_trend:
                        mNestedScrollView_lineup.setVisibility(View.GONE);
                        mNestedScrollView_statistics.setVisibility(View.GONE);
                        fl_live_text.setVisibility(View.GONE);
                        mNestedScrollView_event.setVisibility(View.GONE);
                        mNestedScrollView_trend.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });


        ll_trend_main = (LinearLayout) mView.findViewById(R.id.ll_trend_main);

        fl_attackTrend_loading = (FrameLayout) mView.findViewById(R.id.fl_attackTrend_loading);
        fl_attackTrend_networkError = (FrameLayout) mView.findViewById(R.id.fl_attackTrend_networkError);
        //  sv_attack = (ScrollView) mView.findViewById(R.id.sv_attack);

        reLoading = (TextView) mView.findViewById(R.id.reLoading);// 刷新走势图


        //首發陣容
        fl_firsPlayers_not = (FrameLayout) mView.findViewById(R.id.fl_firsPlayers_not);
        fl_firsPlayers_content = (LinearLayout) mView.findViewById(R.id.fl_firsPlayers_content);
        ll_rosters_homeTeam = (LinearLayout) mView.findViewById(R.id.ll_rosters_homeTeam);
        ll_rosters_visitingTeam = (LinearLayout) mView.findViewById(R.id.ll_rosters_visitingTeam);

        /***
         * 统计图
         */

        fl_cornerTrend_loading = (FrameLayout) mView.findViewById(R.id.fl_cornerTrend_loading);
        fl_cornerTrend_networkError = (FrameLayout) mView.findViewById(R.id.fl_cornerTrend_networkError);
        layout_match_bottom = (RelativeLayout) mView.findViewById(R.id.layout_match_bottom);
        reLoadin = (TextView) mView.findViewById(R.id.reLoadin);// 刷新统计图


        tv_home_corner = (TextView) mView.findViewById(R.id.home_corner);
        tv_home_danger = (TextView) mView.findViewById(R.id.home_danger_attack);
        tv_home_shoot_correct = (TextView) mView.findViewById(R.id.home_shoot_correct);
        tv_home_shoot_miss = (TextView) mView.findViewById(R.id.home_shoot_miss);

        tv_guest_corner = (TextView) mView.findViewById(R.id.guest_corner);
        tv_guest_danger = (TextView) mView.findViewById(R.id.guest_danger_attack);
        tv_guest_shoot_correct = (TextView) mView.findViewById(R.id.guest_shoot_correct);
        tv_guest_shoot_miss = (TextView) mView.findViewById(R.id.guest_shoot_miss);

        pb_corner = (ProgressBar) mView.findViewById(R.id.pb_corner);
        pb_danger = (ProgressBar) mView.findViewById(R.id.pb_danger_attack);
        pb_shoot_correct = (ProgressBar) mView.findViewById(R.id.pb_shoot_correct);
        pb_shoot = (ProgressBar) mView.findViewById(R.id.pb_shoot);


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


        tv_shot = (TextView) mView.findViewById(R.id.tv_shoot);
//        tv_shot_home = (TextView) mView.findViewById(R.id.tv_shot_home);
//        tv_shot_guest = (TextView) mView.findViewById(R.id.tv_shot_guest);
        tv_shotAside = (TextView) mView.findViewById(R.id.tv_shotAside);
//        tv_shotAside_home = (TextView) mView.findViewById(R.id.tv_shotAside_home);
//        tv_shotAside_guest = (TextView) mView.findViewById(R.id.tv_shotAside_guest);
        tv_dangerAttack = (TextView) mView.findViewById(R.id.tv_dangerAttack);
//        tv_dangerAttack_home = (TextView) mView.findViewById(R.id.tv_dangerAttack_home);
//        tv_dangerAttack_guest = (TextView) mView.findViewById(R.id.tv_dangerAttack_guest);
        tv_attack = (TextView) mView.findViewById(R.id.tv_attack);
//        tv_attack_home = (TextView) mView.findViewById(R.id.tv_attack_home);
//        tv_attack_guest = (TextView) mView.findViewById(R.id.tv_attack_guest);


        goChart = (TextView) mView.findViewById(R.id.goChart);
        goChart.setOnClickListener(this);

        reLoadin.setOnClickListener(this);
        reLoading.setOnClickListener(this);
        /**
         * 走势图
         */

        chart_shoot = (LineChart) mView.findViewById(R.id.chart_shoot);
        chart_shootAside = (LineChart) mView.findViewById(R.id.chart_shootAside);
        chart_dangerousAttack = (LineChart) mView.findViewById(R.id.chart_dangerousAttack);
        chart_attack = (LineChart) mView.findViewById(R.id.chart_attack);


        initChartView(chart_shoot, mContext.getResources().getString(R.string.shot_nodata));
        initChartView(chart_shootAside, mContext.getResources().getString(R.string.shotAside_nodata));
        initChartView(chart_dangerousAttack, mContext.getResources().getString(R.string.dangerAttackk_nodata));
        initChartView(chart_attack, mContext.getResources().getString(R.string.attack_nodata));


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
        shotAsideYAxis.setAxisLineColor(Color.BLACK);
        shotAsideXAxis = chart_shootAside.getXAxis();   //x轴
        shotAsideXAxis.setAxisLineColor(Color.BLACK);
        shotAsideXAxis.enableGridDashedLine(10f, 10f, 0f);
        shotAsideXAxis.setAxisMinValue(0f);

        dangerousAttackYAxis = chart_dangerousAttack.getAxisLeft();
        dangerousAttackYAxis.setAxisMinValue(0f);
        dangerousAttackYAxis.enableGridDashedLine(10f, 10f, 0f);  //Y轴横向虚线
        dangerousAttackYAxis.setDrawZeroLine(false);
        dangerousAttackYAxis.setAxisLineColor(Color.BLACK);
        dangerousAttackXAxis = chart_dangerousAttack.getXAxis();   //x轴
        dangerousAttackXAxis.setAxisLineColor(Color.BLACK);
        dangerousAttackXAxis.enableGridDashedLine(10f, 10f, 0f);
        dangerousAttackXAxis.setAxisMinValue(0f);

        attackYAxis = chart_attack.getAxisLeft();
        attackYAxis.setAxisMinValue(0f);
        attackYAxis.enableGridDashedLine(10f, 10f, 0f);  //Y轴横向虚线
        attackYAxis.setDrawZeroLine(false);
        attackYAxis.setAxisLineColor(Color.BLACK);
        attackXAxis = chart_attack.getXAxis();   //x轴
        attackXAxis.enableGridDashedLine(10f, 10f, 0f);
        attackXAxis.setAxisLineColor(Color.BLACK);
        attackXAxis.setAxisMinValue(0f);
    }


    private void initChartView(LineChart mChart, String msg) {
        mChart.setDrawGridBackground(false);
        mChart.setDescription(mContext.getResources().getString(R.string.time));
        mChart.setNoDataTextDescription("");
        mChart.setNoDataTextColor(mContext.getResources().getColor(R.color.res_pl_color));
        mChart.setNoDataText(msg);
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
        l_shoot.setEnabled(false);
    }


    /**
     * 足球事件直播
     */
    private void setEventMatchLive(MatchDetail matchDetail, List<MatchTimeLiveBean> matchTimeLiveBeanMs) {
        //统计事件个数
        this.eventMatchLive = matchTimeLiveBeanMs;
        this.eventType = matchDetail.getLiveStatus();

        if ("0".equals(eventType)) {

            mNoLiveTextFragment = NoLiveTextFragment.newInstance();
            if (getActivity() != null) {
                addFragmentToActivity(getChildFragmentManager(), mNoLiveTextFragment, R.id.fl_live_text);

            }
            ll_nodata.setVisibility(View.VISIBLE);
            ll_event.setVisibility(View.GONE);
            mNestedScrollView_trend.setVisibility(View.GONE);
        } else if ("1".equals(eventType) || "-1".equals(eventType)) {   //-1代表完场  1代表直播中


            ll_nodata.setVisibility(View.GONE);
            ll_event.setVisibility(View.VISIBLE);
            mNestedScrollView_trend.setVisibility(View.VISIBLE);

            //文字直播
            initLiveText(matchDetail);

            //时间轴和事件
            initEvent(matchDetail);

        }
    }


    /**
     * 比赛赛中、赛后初始化数据文字直播
     */
    private void initLiveText(MatchDetail mMatchDetail) {
        matchLive = new ArrayList<>();
        matchLive = mMatchDetail.getMatchInfo().getMatchLive();
        if (matchLive == null) {
            return;
        }

        allMatchLiveMsgId = new ArrayList<>();
        for (MatchTextLiveBean ml : matchLive) {
            if (ml.getMsgId() != null && !"".equals(ml.getMsgId())) {
                allMatchLiveMsgId.add(Integer.parseInt(ml.getMsgId()));
            }
        }

        //赛果
        if (LIVEENDED.equals(mMatchDetail.getLiveStatus())) {
            finishMatchLiveTextFragment = FinishMatchLiveTextFragment.newInstance((ArrayList<MatchTextLiveBean>) matchLive, mMatchDetail.getLiveStatus());
            if (getActivity() != null) {
                addFragmentToActivity(getChildFragmentManager(), finishMatchLiveTextFragment, R.id.fl_live_text);
            }

        } else { //赛中
            L.d("xxccvv", "赛中" + matchLive.size());
            mliveTextFragment = LiveTextFragment.newInstance((ArrayList<MatchTextLiveBean>) matchLive, mMatchDetail.getLiveStatus());
            if (getActivity() != null) {
                addFragmentToActivity(getChildFragmentManager(), mliveTextFragment, R.id.fl_live_text);
            }

        }
    }


    private void initEvent(MatchDetail mMatchDetail) {
        if (LIVEENDED.equals(mMatchDetail.getLiveStatus())) {
            if (mMatchDetail.getMatchInfo().getMatchTimeLive() != null) {
                xMatchLive = mMatchDetail.getMatchInfo().getMatchTimeLive();//完场直接有数据
                secondToMinute(xMatchLive);
                String time = "5400000";//90分钟的毫秒数
                showFootballEventByState();
                showTimeView(time);//画一下时间轴
            }

        } else { //赛中
            initFootBallEventData(mMatchDetail);
        }


        computeEventNum(eventType);

        //事件轴Y
        eventAdapter = new EventAdapter(mContext, eventMatchLive);
        recyclerView.setAdapter(eventAdapter);
    }

    /**
     * 赛中的时间轴数据，从文字直播数据中筛选
     *
     * @param mMatchDetail
     */
    public void initFootBallEventData(MatchDetail mMatchDetail) {
        getTimeLive();
        showFootballEventByState();
    }


    public void updateRecycleView(String status) {
        computeEventNum(status);
        eventAdapter.notifyDataSetChanged();
    }


    //文字直播推送刷新
    public void setLiveTextDetails(List<MatchTextLiveBean> matchLive) {
        if (mliveTextFragment != null) {
            mliveTextFragment.updataLiveTextAdapter(matchLive);
        }
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
    public void addVerticalFootBallEvent(MatchTextLiveBean matchTextLiveBean) {
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


    private void computeEventNum(String status) {
        int homeGoal = 0;
        int homeRc = 0;
        int homeYc = 0;
        int homeCorner = 0;
        int homeDianQiu = 0;
        int guestGoal = 0;
        int guestRc = 0;
        int guestYc = 0;
        int guestCorner = 0;
        int guestDianQiu = 0;

        int statusEqual2 = 0; //计算List里面出现的state=2 code=1多次出现，只需要一个


        int penalty = 0; //点球

        int extraTime = 0; //加时


        Iterator<MatchTimeLiveBean> iterator = eventMatchLive.iterator();
        while (iterator.hasNext()) {
            MatchTimeLiveBean m = iterator.next();
            if (HALFTIME.equals(m.getState()) && "1".equals(m.getCode())) {
                if (statusEqual2 == 1) {
                    iterator.remove();
                } else {
                    statusEqual2++;
                }

            } else if ("18".equals(m.getCode()) || "19".equals(m.getCode())) {  //处理开始点球重复事件
                if (penalty == 1) {
                    iterator.remove();
                } else {
                    penalty++;
                }

            } else if ("14".equals(m.getCode()) || "15".equals(m.getCode())) {  //处理开始加时赛重复事件
                if (extraTime == 1) {
                    iterator.remove();
                } else {
                    extraTime++;
                }

                /*5	Stop 1st half of overtime	结束上半场加时赛
                7	Stop 2nd half of overtime	结束下半场加时赛
                14	Start 1st half of overtime, kickoff:	开始上半场加时赛，开球：
                15	Start 1st half of overtime, kickoff:	开始上半场加时赛，开球：
                16	Start 2nd half of overtime, kickoff:	开始下半场加时赛，开球：
                 list.add("9");
                17	Start 2nd half of overtime, kickoff:	开始下半场加时赛，开球：*/

            } else if ("5".equals(m.getCode()) || "7".equals(m.getCode()) || "9".equals(m.getCode()) || "16".equals(m.getCode()) || "17".equals(m.getCode()) || "605".equals(m.getCode())) {
                //无用重复事件直接remove
                iterator.remove();
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
                    } else if (DIANQIU.equals(m.getCode())) {
                        homeDianQiu++;
                        m.setEventnum(homeDianQiu);
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
                    } else if (DIANQIU1.equals(m.getCode())) {
                        guestDianQiu++;
                        m.setEventnum(guestDianQiu);
                    }
                }
            }
        }
    }

    /**
     * 初始化数据走势图数据
     */
    public void initChartData(String id) {
        if ("0".equals(id)) {
            trendChartList = new ArrayList<>();
            firstInitChartValues();
            liveMatchTrendData();

        } else if ("1".equals(id)) {
            firstInitChartValues();
            liveMatchTrendData();
        }
    }

    /**
     * 改版走势图
     */


    private int getYLabelCount(float yMax) {
        if (yMax == 0) {
            return 1;
        }
        return (int) yMax / (int) Math.ceil(yMax / 8f) + 1;
    }

    private float getYMaxValue(float yMax) {
        return ((int) Math.ceil(yMax / 8f)) * getYLabelCount(yMax);
    }

    private int getXLabelCount(float xMax, float range) {
        return xMax % range == 0 ? (int) Math.ceil(xMax / range) + 1 : (int) Math.ceil(xMax / range);
    }

    private float getXMaxValue(float xMax, float range) {

        return getXLabelCount(xMax, range) * range;
    }


    private void updateChartView() {
        shotHome = 0;
        shotGuest = 0;
        shotAsideHome = 0;
        shotAsideGuest = 0;
        dangerAttackHome = 0;
        dangerAttackGuest = 0;
        attackHome = 0;
        attackGuest = 0;


        shootHomeValues.clear();
        shootGuestValues.clear();
        shootAsideHomeValues.clear();
        shootAsideGuestValues.clear();
        dangerousAttackHomeValues.clear();
        dangerousAttackGuestValues.clear();
        attackHomeValues.clear();
        attackGuestValues.clear();

        shootHomeColors.clear();
        shootGuestColors.clear();
        shootAsideHomeColors.clear();
        shootAsideGuestColors.clear();
        dangerousAttackHomeColors.clear();
        dangerousAttackGuestColors.clear();
        attackHomeColors.clear();
        attackGuestColors.clear();

        shootHomeValues.add(new Entry(0f, 0f));
        shootGuestValues.add(new Entry(0f, 0f));
        shootAsideHomeValues.add(new Entry(0f, 0f));
        shootAsideGuestValues.add(new Entry(0f, 0f));
        dangerousAttackHomeValues.add(new Entry(0f, 0f));
        dangerousAttackGuestValues.add(new Entry(0f, 0f));
        attackHomeValues.add(new Entry(0f, 0f));
        attackGuestValues.add(new Entry(0f, 0f));

        shootHomeColors.add(Color.TRANSPARENT);
        shootGuestColors.add(Color.TRANSPARENT);
        shootAsideHomeColors.add(Color.TRANSPARENT);
        shootAsideGuestColors.add(Color.TRANSPARENT);
        dangerousAttackHomeColors.add(Color.TRANSPARENT);
        dangerousAttackGuestColors.add(Color.TRANSPARENT);
        attackHomeColors.add(Color.TRANSPARENT);
        attackGuestColors.add(Color.TRANSPARENT);

    }

    private void firstInitChartValues() {

        shotHome = 0;
        shotGuest = 0;
        shotAsideHome = 0;
        shotAsideGuest = 0;
        dangerAttackHome = 0;
        dangerAttackGuest = 0;
        attackHome = 0;
        attackGuest = 0;

        shootHomeValues = new ArrayList<>();
        shootGuestValues = new ArrayList<>();
        shootAsideHomeValues = new ArrayList<>();
        shootAsideGuestValues = new ArrayList<>();
        dangerousAttackHomeValues = new ArrayList<>();
        dangerousAttackGuestValues = new ArrayList<>();
        attackHomeValues = new ArrayList<>();
        attackGuestValues = new ArrayList<>();

        shootHomeColors = new ArrayList<>();
        shootGuestColors = new ArrayList<>();
        shootAsideHomeColors = new ArrayList<>();
        shootAsideGuestColors = new ArrayList<>();
        dangerousAttackHomeColors = new ArrayList<>();
        dangerousAttackGuestColors = new ArrayList<>();
        attackHomeColors = new ArrayList<>();
        attackGuestColors = new ArrayList<>();

        shootHomeValues.add(new Entry(0f, 0f));
        shootGuestValues.add(new Entry(0f, 0f));
        shootAsideHomeValues.add(new Entry(0f, 0f));
        shootAsideGuestValues.add(new Entry(0f, 0f));
        dangerousAttackHomeValues.add(new Entry(0f, 0f));
        dangerousAttackGuestValues.add(new Entry(0f, 0f));
        attackHomeValues.add(new Entry(0f, 0f));
        attackGuestValues.add(new Entry(0f, 0f));

        shootHomeColors.add(Color.TRANSPARENT);
        shootGuestColors.add(Color.TRANSPARENT);
        shootAsideHomeColors.add(Color.TRANSPARENT);
        shootAsideGuestColors.add(Color.TRANSPARENT);
        dangerousAttackHomeColors.add(Color.TRANSPARENT);
        dangerousAttackGuestColors.add(Color.TRANSPARENT);
        attackHomeColors.add(Color.TRANSPARENT);
        attackGuestColors.add(Color.TRANSPARENT);

    }

    /**
     * 走势图完场计算数据
     */
    private void finishMatchTrendData() {
        /**
         * x轴为分钟  y轴为个数
         */
        firstInitChartValues();
        //射正
        Iterator<TrendBean> shotHomeIterator = trendFormBean.getShot().getHome().iterator();
        int shotHome = 0;
        while (shotHomeIterator.hasNext()) {
            TrendBean bean = shotHomeIterator.next();
            if (TREND_CORNER.equals(bean.getFlag())) {
                shootHomeColors.add(Color.parseColor("#19A67A"));
            } else if (TREND_GOAL.equals(bean.getFlag())) {
                shotHome++;
                shootHomeColors.add(Color.parseColor("#FF0000"));
            } else {
                shotHome++;
                shootHomeColors.add(Color.TRANSPARENT);
            }

            shootHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), shotHome));
        }


        Iterator<TrendBean> shotGuestIterator = trendFormBean.getShot().getGuest().iterator();
        int shotGuest = 0;

        while (shotGuestIterator.hasNext()) {
            TrendBean bean = shotGuestIterator.next();
            if (TREND_CORNER.equals(bean.getFlag())) {
                shootGuestColors.add(Color.parseColor("#19A67A"));
            } else if (TREND_GOAL.equals(bean.getFlag())) {
                shotGuest++;
                shootGuestColors.add(Color.parseColor("#FF0000"));
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
            shotYAxis.setLabelCount(getYLabelCount(shotHome));
        } else {
            shotYAxis.setAxisMaxValue(getYMaxValue(shotGuest));
            shotYAxis.setLabelCount(getYLabelCount(shotGuest));
        }

        showTrendChartView(chart_shoot, shootHomeValues, shootGuestValues, shootHomeColors, shootGuestColors);


        //射偏
        Iterator<TrendBean> shotAsideHomeIterator = trendFormBean.getShepian().getHome().iterator();
        int shotAsideHome = 0;
        while (shotAsideHomeIterator.hasNext()) {
            TrendBean bean = shotAsideHomeIterator.next();
            if (TREND_CORNER.equals(bean.getFlag())) {
                shootAsideHomeColors.add(Color.parseColor("#19A67A"));

            } else if (TREND_GOAL.equals(bean.getFlag())) {
                shootAsideHomeColors.add(Color.parseColor("#FF0000"));

            } else {
                shotAsideHome++;
                shootAsideHomeColors.add(Color.TRANSPARENT);

            }


            shootAsideHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), shotAsideHome));
        }


        Iterator<TrendBean> shotAsideGuestIterator = trendFormBean.getShepian().getGuest().iterator();
        int shotAsideGuest = 0;
        while (shotAsideGuestIterator.hasNext()) {
            TrendBean bean = shotAsideGuestIterator.next();
            if (TREND_CORNER.equals(bean.getFlag())) {
                shootAsideGuestColors.add(Color.parseColor("#19A67A"));
            } else if (TREND_GOAL.equals(bean.getFlag())) {
                shootAsideGuestColors.add(Color.parseColor("#FF0000"));
            } else {
                shotAsideGuest++;
                shootAsideGuestColors.add(Color.TRANSPARENT);
            }
            shootAsideGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), shotAsideGuest));
        }


        shotAsideXAxis.setAxisMaxValue(getXMaxValue(90f, FINISHMATCH));
        shotAsideXAxis.setLabelCount(getXLabelCount(90f, FINISHMATCH));

        if (shotAsideHome > shotAsideGuest) {
            shotAsideYAxis.setAxisMaxValue(getYMaxValue(shotAsideHome));
            shotAsideYAxis.setLabelCount(getYLabelCount(shotAsideHome));
        } else {
            shotAsideYAxis.setAxisMaxValue(getYMaxValue(shotAsideGuest));
            shotAsideYAxis.setLabelCount(getYLabelCount(shotAsideGuest));
        }


        showTrendChartView(chart_shootAside, shootAsideHomeValues, shootAsideGuestValues, shootAsideHomeColors, shootAsideGuestColors);


        //危险进攻
        Iterator<TrendBean> dangerAttackHomeIterator = trendFormBean.getDangerousAttack().getHome().iterator();
        int dangerAttackHome = 0;
        while (dangerAttackHomeIterator.hasNext()) {
            TrendBean bean = dangerAttackHomeIterator.next();
            if (TREND_CORNER.equals(bean.getFlag())) {
                dangerousAttackHomeColors.add(Color.parseColor("#19A67A"));
            } else if (TREND_GOAL.equals(bean.getFlag())) {
                dangerousAttackHomeColors.add(Color.parseColor("#FF0000"));
            } else {
                dangerAttackHome++;
                dangerousAttackHomeColors.add(Color.TRANSPARENT);
            }
            dangerousAttackHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), dangerAttackHome));
        }

        Iterator<TrendBean> dangerAttackGusetIterator = trendFormBean.getDangerousAttack().getGuest().iterator();
        int dangerAttackGuest = 0;
        while (dangerAttackGusetIterator.hasNext()) {
            TrendBean bean = dangerAttackGusetIterator.next();
            if (TREND_CORNER.equals(bean.getFlag())) {
                dangerousAttackGuestColors.add(Color.parseColor("#19A67A"));
            } else if (TREND_GOAL.equals(bean.getFlag())) {
                dangerousAttackGuestColors.add(Color.parseColor("#FF0000"));
            } else {
                dangerAttackGuest++;
                dangerousAttackGuestColors.add(Color.TRANSPARENT);
            }
            dangerousAttackGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), dangerAttackGuest));
        }


        dangerousAttackXAxis.setAxisMaxValue(getXMaxValue(90f, FINISHMATCH));
        dangerousAttackXAxis.setLabelCount(getXLabelCount(90f, FINISHMATCH));


        if (dangerAttackHome > dangerAttackGuest) {
            dangerousAttackYAxis.setAxisMaxValue(getYMaxValue(dangerAttackHome));
            dangerousAttackYAxis.setLabelCount(getYLabelCount(dangerAttackHome));
        } else {
            dangerousAttackYAxis.setAxisMaxValue(getYMaxValue(dangerAttackGuest));
            dangerousAttackYAxis.setLabelCount(getYLabelCount(dangerAttackGuest));
        }

        showTrendChartView(chart_dangerousAttack, dangerousAttackHomeValues, dangerousAttackGuestValues, dangerousAttackHomeColors, dangerousAttackGuestColors);


        //进攻
        Iterator<TrendBean> attackHomeIterator = trendFormBean.getAttack().getHome().iterator();
        int attackHome = 0;
        while (attackHomeIterator.hasNext()) {
            TrendBean bean = attackHomeIterator.next();
            if (TREND_CORNER.equals(bean.getFlag())) {
                attackHomeColors.add(Color.parseColor("#19A67A"));
            } else if (TREND_GOAL.equals(bean.getFlag())) {
                attackHomeColors.add(Color.parseColor("#FF0000"));
            } else {
                attackHome++;
                attackHomeColors.add(Color.TRANSPARENT);
            }
            attackHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), attackHome));
        }

        Iterator<TrendBean> attackGusetIterator = trendFormBean.getAttack().getGuest().iterator();
        int attackGuest = 0;
        while (attackGusetIterator.hasNext()) {
            TrendBean bean = attackGusetIterator.next();
            if (TREND_CORNER.equals(bean.getFlag())) {
                attackGuestColors.add(Color.parseColor("#19A67A"));
            } else if (TREND_GOAL.equals(bean.getFlag())) {
                attackGuestColors.add(Color.parseColor("#FF0000"));
            } else {
                attackGuest++;
                attackGuestColors.add(Color.TRANSPARENT);
            }
            attackGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), attackGuest));
        }

        attackXAxis.setAxisMaxValue(getXMaxValue(90f, FINISHMATCH));
        attackXAxis.setLabelCount(getXLabelCount(90f, FINISHMATCH));


        if (attackHome > attackGuest) {
            attackYAxis.setAxisMaxValue(getYMaxValue(attackHome));
            attackYAxis.setLabelCount(getYLabelCount(attackHome));
        } else {
            attackYAxis.setAxisMaxValue(getYMaxValue(attackGuest));
            attackYAxis.setLabelCount(getYLabelCount(attackGuest));
        }

        showTrendChartView(chart_attack, attackHomeValues, attackGuestValues, attackHomeColors, attackGuestColors);
        setChartScore();
    }

    private void showTrendChartView(LineChart mChart, List<Entry> homeEntry, List<Entry> guestEntry, List<Integer> homeColors, List<Integer> guestColors) {
        LineDataSet mHomeLineDataSet;
        LineDataSet mGuestLineDataSet;

        mHomeLineDataSet = new LineDataSet(homeEntry, "");
        mHomeLineDataSet.setDrawHighlightIndicators(false);


        mHomeLineDataSet.setLabel("");
        mHomeLineDataSet.enableDashedHighlightLine(10f, 5f, 0f);
        mHomeLineDataSet.setColor(Color.parseColor("#C23531"));
        mHomeLineDataSet.setCircleColor(Color.BLACK);
        mHomeLineDataSet.setLineWidth(0.7f);
        mHomeLineDataSet.setCircleRadius(3f);
        mHomeLineDataSet.setDrawCircleHole(false);
        mHomeLineDataSet.setDrawValues(false);
        mHomeLineDataSet.setCircleColors(homeColors);

        mGuestLineDataSet = new LineDataSet(guestEntry, "");
        mGuestLineDataSet.enableDashedHighlightLine(10f, 5f, 0f);
        mGuestLineDataSet.setColor(Color.BLUE);
        mGuestLineDataSet.setCircleColor(Color.BLACK);
        mGuestLineDataSet.setLineWidth(0.7f);
        mGuestLineDataSet.setCircleRadius(3f);
        mGuestLineDataSet.setDrawCircleHole(false);
        mGuestLineDataSet.setValueTextSize(9f);
        mGuestLineDataSet.setDrawValues(false);
        mGuestLineDataSet.setCircleColors(guestColors);


        ArrayList<ILineDataSet> dataSetsList = new ArrayList<>();
        dataSetsList.add(mHomeLineDataSet);
        dataSetsList.add(mGuestLineDataSet);

        LineData lineData = new LineData(dataSetsList);
        mChart.setData(lineData);

        mChart.invalidate();
    }


    /**
     * 显示数据走势图
     */

    private void liveMatchTrendData() {
        //trendChartList排序由小到大

        if (trendChartList.size() > 1) {
            Collections.sort(trendChartList, new FootballTrendChartComparator());
        }

        float maxXais = 0;

        if (trendChartList.size() == 0) {
            return;
        }

        maxXais = convertStringToFloat(trendChartList.get(trendChartList.size() - 1).getTime());


        Iterator<MatchTextLiveBean> iterator = trendChartList.iterator();
        while (iterator.hasNext()) {

            MatchTextLiveBean bean = iterator.next();

            switch (bean.getCode()) {
                case SCORE:
                    shotHome++;
                    shootHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), shotHome));
                    shootAsideHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), shotAsideHome));
                    dangerousAttackHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), dangerAttackHome));
                    attackHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), attackHome));
                    shootHomeColors.add(Color.parseColor("#FF0000"));
                    shootAsideHomeColors.add(Color.parseColor("#FF0000"));
                    dangerousAttackHomeColors.add(Color.parseColor("#FF0000"));
                    attackHomeColors.add(Color.parseColor("#FF0000"));

                    break;

                case SCORE1:
                    shotGuest++;
                    shootGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), shotGuest));
                    shootAsideGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), shotAsideGuest));
                    dangerousAttackGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), dangerAttackGuest));
                    attackGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), attackGuest));
                    shootGuestColors.add(Color.parseColor("#FF0000"));
                    shootAsideGuestColors.add(Color.parseColor("#FF0000"));
                    dangerousAttackGuestColors.add(Color.parseColor("#FF0000"));
                    attackGuestColors.add(Color.parseColor("#FF0000"));
                    break;

                case CORNER:
                    shootHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), shotHome));
                    shootAsideHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), shotAsideHome));
                    dangerousAttackHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), dangerAttackHome));
                    attackHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), attackHome));
                    shootHomeColors.add(Color.parseColor("#19A67A"));
                    shootAsideHomeColors.add(Color.parseColor("#19A67A"));
                    dangerousAttackHomeColors.add(Color.parseColor("#19A67A"));
                    attackHomeColors.add(Color.parseColor("#19A67A"));
                    break;

                case CORNER1:
                    shootGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), shotGuest));
                    shootAsideGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), shotAsideGuest));
                    dangerousAttackGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), dangerAttackGuest));
                    attackGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), attackGuest));
                    shootGuestColors.add(Color.parseColor("#19A67A"));
                    shootAsideGuestColors.add(Color.parseColor("#19A67A"));
                    dangerousAttackGuestColors.add(Color.parseColor("#19A67A"));
                    attackGuestColors.add(Color.parseColor("#19A67A"));

                    break;

                case SHOOT:
                    shotHome++;
                    shootHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), shotHome));
                    shootHomeColors.add(Color.TRANSPARENT);
                    break;

                case SHOOT1:
                    shotGuest++;
                    shootGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), shotGuest));
                    shootGuestColors.add(Color.TRANSPARENT);
                    break;

                case SHOOTASIDE:
                    shotAsideHome++;
                    shootAsideHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), shotAsideHome));
                    shootAsideHomeColors.add(Color.TRANSPARENT);
                    break;
                case SHOOTASIDE2:
                    shotAsideHome++;
                    shootAsideHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), shotAsideHome));
                    shootAsideHomeColors.add(Color.TRANSPARENT);
                    break;
                case SHOOTASIDE1:
                    shotAsideGuest++;
                    shootAsideGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), shotAsideGuest));
                    shootAsideGuestColors.add(Color.TRANSPARENT);
                    break;
                case SHOOTASIDE12:
                    shotAsideGuest++;
                    shootAsideGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), shotAsideGuest));
                    shootAsideGuestColors.add(Color.TRANSPARENT);
                    break;
                case DANGERATTACK:
                    dangerAttackHome++;
                    dangerousAttackHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), dangerAttackHome));
                    dangerousAttackHomeColors.add(Color.TRANSPARENT);
                    break;
                case DANGERATTACK1:
                    dangerAttackGuest++;
                    dangerousAttackGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), dangerAttackGuest));
                    dangerousAttackGuestColors.add(Color.TRANSPARENT);
                    break;
                case ATTACK:
                    attackHome++;
                    attackHomeValues.add(new Entry(convertStringToFloat(bean.getTime()), attackHome));
                    attackHomeColors.add(Color.TRANSPARENT);
                    break;
                case ATTACK1:
                    attackGuest++;
                    attackGuestValues.add(new Entry(convertStringToFloat(bean.getTime()), attackGuest));
                    attackGuestColors.add(Color.TRANSPARENT);
                    break;
            }
        }


        //比较四个坐标轴时间，取最大的time值

        if (maxXais >= 45f) {
            shotXAxis.setAxisMaxValue(getXMaxValue(maxXais, FINISHMATCH));
            shotXAxis.setLabelCount(getXLabelCount(maxXais, FINISHMATCH));
            shotAsideXAxis.setAxisMaxValue(getXMaxValue(maxXais, FINISHMATCH));
            shotAsideXAxis.setLabelCount(getXLabelCount(maxXais, FINISHMATCH));
            dangerousAttackXAxis.setAxisMaxValue(getXMaxValue(maxXais, FINISHMATCH));
            dangerousAttackXAxis.setLabelCount(getXLabelCount(maxXais, FINISHMATCH));
            attackXAxis.setAxisMaxValue(getXMaxValue(maxXais, FINISHMATCH));
            attackXAxis.setLabelCount(getXLabelCount(maxXais, FINISHMATCH));
        } else {
            shotXAxis.setAxisMaxValue(getXMaxValue(maxXais, HALFMATCH));
            shotXAxis.setLabelCount(getXLabelCount(maxXais, HALFMATCH));

            shotAsideXAxis.setAxisMaxValue(getXMaxValue(maxXais, HALFMATCH));
            shotAsideXAxis.setLabelCount(getXLabelCount(maxXais, HALFMATCH));

            dangerousAttackXAxis.setAxisMaxValue(getXMaxValue(maxXais, HALFMATCH));
            dangerousAttackXAxis.setLabelCount(getXLabelCount(maxXais, HALFMATCH));

            attackXAxis.setAxisMaxValue(getXMaxValue(maxXais, HALFMATCH));
            attackXAxis.setLabelCount(getXLabelCount(maxXais, HALFMATCH));
        }

        //射正
        if (shotHome > shotGuest) {
            shotYAxis.setAxisMaxValue(getYMaxValue(shotHome));
            shotYAxis.setLabelCount(getYLabelCount(shotHome));
        } else {
            shotYAxis.setAxisMaxValue(getYMaxValue(shotGuest));
            shotYAxis.setLabelCount(getYLabelCount(shotGuest));
        }


        showTrendChartView(chart_shoot, shootHomeValues, shootGuestValues, shootHomeColors, shootGuestColors);


        //射偏
        if (shotAsideHome > shotAsideGuest) {
            shotAsideYAxis.setAxisMaxValue(getYMaxValue(shotAsideHome));
            shotAsideYAxis.setLabelCount(getYLabelCount(shotAsideHome));
        } else {
            shotAsideYAxis.setAxisMaxValue(getYMaxValue(shotAsideGuest));
            shotAsideYAxis.setLabelCount(getYLabelCount(shotAsideGuest));
        }

        showTrendChartView(chart_shootAside, shootAsideHomeValues, shootAsideGuestValues, shootAsideHomeColors, shootAsideGuestColors);

        //危险进攻
        if (dangerAttackHome > dangerAttackGuest) {
            dangerousAttackYAxis.setAxisMaxValue(getYMaxValue(dangerAttackHome));
            dangerousAttackYAxis.setLabelCount(getYLabelCount(dangerAttackHome));
        } else {
            dangerousAttackYAxis.setAxisMaxValue(getYMaxValue(dangerAttackGuest));
            dangerousAttackYAxis.setLabelCount(getYLabelCount(dangerAttackGuest));
        }

        showTrendChartView(chart_dangerousAttack, dangerousAttackHomeValues, dangerousAttackGuestValues, dangerousAttackHomeColors, dangerousAttackGuestColors);


        //进攻
        if (attackHome > attackGuest) {
            attackYAxis.setAxisMaxValue(getYMaxValue(attackHome));
            attackYAxis.setLabelCount(getYLabelCount(attackHome));
        } else {
            attackYAxis.setAxisMaxValue(getYMaxValue(attackGuest));
            attackYAxis.setLabelCount(getYLabelCount(attackGuest));
        }

        showTrendChartView(chart_attack, attackHomeValues, attackGuestValues, attackHomeColors, attackGuestColors);

        setChartScore();
    }


    private void setChartScore() {
        tv_shot.setText((int) shootHomeValues.get(shootHomeValues.size() - 1).getY() + ":" + (int) shootGuestValues.get(shootGuestValues.size() - 1).getY());
        tv_shotAside.setText((int) shootAsideHomeValues.get(shootAsideHomeValues.size() - 1).getY() + ":" + (int) shootAsideGuestValues.get(shootAsideGuestValues.size() - 1).getY());
        tv_dangerAttack.setText((int) dangerousAttackHomeValues.get(dangerousAttackHomeValues.size() - 1).getY() + ":" + (int) dangerousAttackGuestValues.get(dangerousAttackGuestValues.size() - 1).getY());
        tv_attack.setText((int) attackHomeValues.get(attackHomeValues.size() - 1).getY() + ":" + (int) attackGuestValues.get(attackGuestValues.size() - 1).getY());

    }


    public void addTrendChartEvent(MatchTextLiveBean matchTextLiveBean) {
        trendChartList.add(matchTextLiveBean);
        updateChartView();
        liveMatchTrendData();
    }

    public void cancelTrendChartEvent(MatchTextLiveBean matchTextLiveBean) {
        Iterator<MatchTextLiveBean> iterator = trendChartList.iterator();
        while (iterator.hasNext()) {
            MatchTextLiveBean bean = iterator.next();
            if (bean.getEnNum().equals(matchTextLiveBean.getCancelEnNum())) {//取消进球等事件的判断
                iterator.remove();//用xMatchLive.remove会有异常
            }
        }
        updateChartView();

        liveMatchTrendData();
    }

    /**
     * 请求走势图后台数据
     *
     * @return
     */
    private void getTrenFormInfo() {

        mHandler.sendEmptyMessage(STARTLOADING);// 正在加载数据中

        // 设置参数
        Map<String, String> myPostParams = new HashMap<>();
        myPostParams.put("matchId", mThirdId);

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_FINDTRENDFORM_INFO, myPostParams, new VolleyContentFast.ResponseSuccessListener<FootballTrendBean>() {
            @Override
            public void onResponse(FootballTrendBean jsonObject) {
                if (jsonObject != null) {
                    if (!"200".equals(jsonObject.getResult())) {
                        return;
                    }

                    if (jsonObject.getTrendForm() != null) {
                        trendFormBean = null;
                        trendFormBean = jsonObject.getTrendForm();
                        mHandler.sendEmptyMessage(SUCCESS);
                    }

                } else {
                    // 后台没请求到数据
                    L.d("112233", "走势请求失败");

                    mHandler.sendEmptyMessage(ERROR);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                L.d("112233", "走势请求失败" + exception);

                mHandler.sendEmptyMessage(ERROR);// 访问失败
            }
        }, FootballTrendBean.class);
    }


    /**
     * 请求统计图后台数据
     *
     * @return
     */
    public void getLiveStatic() {

        mHandlerStatics.sendEmptyMessage(STARTLOADING);// 正在加载中
        Map<String, String> map = new HashMap<>();
        if (getActivity() == null) {
            return;
        } else {
            map.put("thirdId", mThirdId);
            VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_STATISTICAL_DATA_INFO, map, new VolleyContentFast.ResponseSuccessListener<DataStatisInfo>() {
                @Override
                public void onResponse(DataStatisInfo json) {
                    if (json != null && "200".equals(json.getResult())) {
                        mHomeStatisEntity = json.getHomeStatis();
                        mGuestStatisEntity = json.getGuestStatis();
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
    public void initLiveStatics(String id) {
        //未开赛
        if ("0".equals(id)) {

            //  private TextView tv_home_corner, tv_home_danger, tv_home_shoot_correct, tv_home_shoot;
            // private TextView tv_guest_corner, tv_guest_danger, tv_guest_shoot_correct, tv_guest_shoot;
            //private ProgressBar pb_corner, pb_danger, pb_shoot_correct, pb_shoot;

            tv_home_corner.setText("");
            tv_home_danger.setText("");
            tv_home_shoot_correct.setText("");
            tv_home_shoot_miss.setText("");
            tv_guest_corner.setText("");
            tv_guest_danger.setText("");
            tv_guest_shoot_correct.setText("");
            tv_guest_shoot_miss.setText("");

            pb_corner.setProgress(50);
            pb_danger.setProgress(50);
            pb_shoot_correct.setProgress(50);
            pb_shoot.setProgress(50);


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

            pb_corner.setProgress((int) StadiumUtils.computeProgressbarPercent(mMathchStatisInfo.getHome_corner(), mMathchStatisInfo.getHome_corner()));
            pb_danger.setProgress((int) StadiumUtils.computeProgressbarPercent(mMathchStatisInfo.getHome_danger(), mMathchStatisInfo.getGuest_danger()));
            pb_shoot_correct.setProgress((int) StadiumUtils.computeProgressbarPercent(mMathchStatisInfo.getHome_shoot_correct(), mMathchStatisInfo.getHome_shoot_correct()));
            pb_shoot.setProgress((int) StadiumUtils.computeProgressbarPercent(mMathchStatisInfo.getHome_shoot_door(), mMathchStatisInfo.getGuest_shoot_door()));


            tv_home_corner.setText(mMathchStatisInfo.getHome_corner() + "");
            tv_home_danger.setText(mMathchStatisInfo.getHome_danger() + "");
            tv_home_shoot_correct.setText(mMathchStatisInfo.getHome_shoot_correct() + "");
            tv_home_shoot_miss.setText(mMathchStatisInfo.getHome_shoot_miss() + "");
            tv_guest_corner.setText(mMathchStatisInfo.getGuest_corner() + "");
            tv_guest_danger.setText(mMathchStatisInfo.getGuest_danger() + "");
            tv_guest_shoot_correct.setText(mMathchStatisInfo.getHome_shoot_correct() + "");
            tv_guest_shoot_miss.setText(mMathchStatisInfo.getGuest_shoot_miss() + "");


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

                    finishMatchTrendData();

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


    private void showLineUp() {
        List<PlayerInfo> homeLineUpList = matchDetail.getHomeTeamInfo().getLineup();
        List<PlayerInfo> guestLineUpList = matchDetail.getGuestTeamInfo().getLineup();

        if (!homeLineUpList.isEmpty() && !guestLineUpList.isEmpty()) {
            if (homeLineUpList.size() > 0) {
                // 显示首发内容
                fl_firsPlayers_not.setVisibility(View.GONE); ///sd
                fl_firsPlayers_content.setVisibility(View.VISIBLE);

                int dip5 = DisplayUtil.dip2px(mContext, 5);
                int dip10 = DisplayUtil.dip2px(mContext, 10);

                if (ll_rosters_homeTeam.getChildCount() != 0) {  //刷新时清空上次add的textview
                    ll_rosters_homeTeam.removeAllViews();
                }
                if (ll_rosters_visitingTeam.getChildCount() != 0) {
                    ll_rosters_visitingTeam.removeAllViews();
                }
                // 添加主队名单
                for (int i = 0, len = homeLineUpList.size(); i < len; i++) {
                    TextView tv_homeTeams = new TextView(mContext);
                    tv_homeTeams.setText(homeLineUpList.get(i).getName());
                    if (i == 0) {
                        tv_homeTeams.setPadding(dip5, 0, 0, dip10);
                    } else {
                        tv_homeTeams.setPadding(dip5, 0, 0, dip10);
                    }
                    tv_homeTeams.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
                    ll_rosters_homeTeam.addView(tv_homeTeams);
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.RIGHT;// 设置靠右对齐
                // 添加客队名单
                for (int i = 0, len = guestLineUpList.size(); i < len; i++) {
                    TextView tv_visitingTeams = new TextView(mContext);
                    tv_visitingTeams.setText(guestLineUpList.get(i).getName());
                    tv_visitingTeams.setLayoutParams(params);
                    if (i == 0) {
                        tv_visitingTeams.setPadding(0, 0, dip5, dip10);
                    } else {
                        tv_visitingTeams.setPadding(0, 0, dip5, dip10);
                    }
                    tv_visitingTeams.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
                    ll_rosters_visitingTeam.addView(tv_visitingTeams);
                }

            } else {
                // 显示暂无首发提示
                fl_firsPlayers_not.setVisibility(View.VISIBLE);
                fl_firsPlayers_content.setVisibility(View.GONE);
            }

        } else {
            // 显示暂无首发提示
            fl_firsPlayers_not.setVisibility(View.VISIBLE);
            fl_firsPlayers_content.setVisibility(View.GONE);
        }

    }

    private void loadStatics() {

        pb_corner.setProgress((int) StadiumUtils.computeProgressbarPercent(mHomeStatisEntity.getCorner(), mGuestStatisEntity.getCorner()));
        pb_danger.setProgress((int) StadiumUtils.computeProgressbarPercent(mHomeStatisEntity.getDanger(), mGuestStatisEntity.getDanger()));
        pb_shoot_correct.setProgress((int) StadiumUtils.computeProgressbarPercent(mHomeStatisEntity.getShot(), mGuestStatisEntity.getShot()));
        pb_shoot.setProgress((int) StadiumUtils.computeProgressbarPercent(mHomeStatisEntity.getAllShots(), mGuestStatisEntity.getAllShots()));


        tv_home_corner.setText(mHomeStatisEntity.getCorner() + "");
        tv_home_danger.setText(mHomeStatisEntity.getDanger() + "");
        tv_home_shoot_correct.setText(mHomeStatisEntity.getShot() + "");
        tv_home_shoot_miss.setText((mHomeStatisEntity.getAllShots() - mHomeStatisEntity.getShot()) + "");
        tv_guest_corner.setText(mGuestStatisEntity.getCorner() + "");
        tv_guest_danger.setText(mGuestStatisEntity.getDanger() + "");
        tv_guest_shoot_correct.setText(mGuestStatisEntity.getShot() + "");
        tv_guest_shoot_miss.setText((mGuestStatisEntity.getAllShots() - mGuestStatisEntity.getShot()) + "");

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
        float f = (float) Double.parseDouble(time) / 60000;

        return (float) (Math.round(f * 100)) / 100;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int frameId) {
        if (fragmentManager != null && fragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(frameId, fragment)
                    .disallowAddToBackStack()
                    .commit();
            //transaction.add(frameId, fragment);

            //transaction.commitAllowingStateLoss();
        }
    }


    /**
     * 完场将时间改为分钟
     *
     * @param xMatchLive
     */
    private void secondToMinute(List<MatchTimeLiveBean> xMatchLive) {
        for (MatchTimeLiveBean timeLiveBean : xMatchLive) {
            int time = StadiumUtils.convertStringToInt(timeLiveBean.getTime());
            if (time > 45 && timeLiveBean.getState().equals(FIRSTHALF)) {//上半场
                time = 45;
            }
            if (time > 90) {
                time = 90;
            }
            if (timeLiveBean.getState().equals(HALFTIME)) {
                time = 46;
            }
            timeLiveBean.setTime(time + "");
        }
    }

    /**
     * 展示足球事件。根据比赛状态
     */
    public void showFootballEventByState() {
        Collections.sort(xMatchLive, new FootballEventComparator());

        Map<String, List<MatchTimeLiveBean>> timeEventMap1 = new LinkedHashMap<String, List<MatchTimeLiveBean>>();//LinkedHashMap保证map有序
        Map<String, List<MatchTimeLiveBean>> timeEventMap2 = new LinkedHashMap<String, List<MatchTimeLiveBean>>();

        for (MatchTimeLiveBean data : xMatchLive) {
            List<MatchTimeLiveBean> temp1 = timeEventMap1.get(data.getTime());
            List<MatchTimeLiveBean> temp2 = timeEventMap2.get(data.getTime());
            if (temp1 == null) {
                temp1 = new ArrayList<>();
            }
            if (temp2 == null) {
                temp2 = new ArrayList<>();
            }
            if (data.getIsHome().equals("1")) {//主队
                temp1.add(data);
                timeEventMap1.put(data.getTime(), temp1);
            }
            if (data.getIsHome().equals("0")) {//客队
                temp2.add(data);
                timeEventMap2.put(data.getTime(), temp2);
            }

        }

        timeLayoutTop.setEventImageByState(timeEventMap1);
        timeLayoutBottom.setEventImageByState(timeEventMap2);


    }

    /**
     * 展示时间轴时间
     */
    public void showTimeView(String mKeepTime) {
        if (mKeepTime == null) {
            mKeepTime = "0";
        }
        timeView.updateTime(Integer.parseInt(mKeepTime));
    }

    /**
     * 赛中时获取时间轴数据
     */
    private void getTimeLive() {
        xMatchLive = new ArrayList<>();
        if (matchLive == null) {
            return;
        }
        Iterator<MatchTextLiveBean> iterator1 = matchLive.iterator();
        while (iterator1.hasNext()) {
            MatchTextLiveBean bean1 = iterator1.next();
            if (bean1.getCode().equals(SCORE) || bean1.getCode().equals(SCORE1) ||
                    bean1.getCode().equals(RED_CARD) || bean1.getCode().equals(RED_CARD1) ||
                    bean1.getCode().equals(YTORED) || bean1.getCode().equals(YTORED1) ||
                    bean1.getCode().equals(YELLOW_CARD) || bean1.getCode().equals(YELLOW_CARD1) ||
                    bean1.getCode().equals(CORNER) || bean1.getCode().equals(CORNER1) ||
                    bean1.getCode().equals(SUBSTITUTION) || bean1.getCode().equals(SUBSTITUTION1)
                //  bean1.getCode().equals(FIRSTOVER)//把上半场结束的状态也加进来
                    ) {
                String place = bean1.getMsgPlace();
                if (place.equals("2")) {//客队
                    place = "0";//isHome=0客队
                }

                MatchTimeLiveBean timeLiveBean = new MatchTimeLiveBean(secondToMInute(bean1) + "", bean1.getCode(),//这里时间直接转换为分钟，就会一样了
                        place, bean1.getEnNum(), bean1.getState(), "", bean1.getEnNum(), 0);
                xMatchLive.add(timeLiveBean);

            }

        }

        for (MatchTextLiveBean bean : matchLive) {
            Iterator<MatchTimeLiveBean> iterator2 = xMatchLive.iterator();
            while (iterator2.hasNext()) {
                MatchTimeLiveBean bean2 = iterator2.next();
                if (bean2.getMsgId().equals(bean.getCancelEnNum())) {
                    iterator2.remove();//去掉已经取消的
                }
            }
        }


    }

    /**
     * 赛中将时间改为分钟
     *
     * @param bean
     * @return
     */
    private int secondToMInute(MatchTextLiveBean bean) {
        int time = StadiumUtils.convertStringToInt(bean.getTime());
        if (time > 45 && bean.getState().equals(FIRSTHALF)) {//上半场
            time = 45;
        }
        if (time > 90) {
            time = 90;
        }
        if (bean.getState().equals(HALFTIME)) {
            time = 46;
        }
        return time;

    }

    /**
     * 把事件从文字直播集合中取出加到时间轴集合中
     *
     * @param matchTextLiveBean
     */
    public void addFootballEvent(MatchTextLiveBean matchTextLiveBean) {
        String place = matchTextLiveBean.getMsgPlace();
        if (matchTextLiveBean.getMsgPlace().equals("2")) {//客队
            place = "0";
        }

        xMatchLive.add(new MatchTimeLiveBean(secondToMInute(matchTextLiveBean) + "",
                matchTextLiveBean.getCode(), place, matchTextLiveBean.getEnNum(), matchTextLiveBean.getState(), "", matchTextLiveBean.getEnNum(), 0));
    }

    /**
     * 取消对应的足球事件
     */

    public void cancelFootBallEvent(MatchTextLiveBean matchTextLiveBean) {
        Iterator<MatchTimeLiveBean> iterator = xMatchLive.iterator();
        while (iterator.hasNext()) {
            MatchTimeLiveBean bean = iterator.next();
            if (bean.getMsgId().equals(matchTextLiveBean.getCancelEnNum())) {//取消进球等事件的判断
                iterator.remove();//用xMatchLive.remove会有异常
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
