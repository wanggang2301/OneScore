//package com.hhly.mlottery.frame.footframe;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.hhly.mlottery.MyApp;
//import com.hhly.mlottery.R;
//import com.hhly.mlottery.adapter.AnalyzeMatchAdapter;
//import com.hhly.mlottery.adapter.AnalyzeRankAdapter;
//import com.hhly.mlottery.bean.footballDetails.AnalyzeBean;
//import com.hhly.mlottery.config.BaseURLs;
//import com.hhly.mlottery.util.L;
//import com.hhly.mlottery.util.net.VolleyContentFast;
//import com.hhly.mlottery.widget.AnalyzeTitle;
//import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 分析Fragment
// * 旧的分析界面
// */
//public class OldAnalyzeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private static final String ARG_PARAM3 = "param3";
//    private static final java.lang.String TAG = "OldAnalyzeFragment";
//
//    private final static int VIEW_STATUS_LOADING = 1;
//    private final static int VIEW_STATUS_SUCCESS = 3;
//    private final static int VIEW_STATUS_NET_ERROR = 4;
//
//    private String mThirdId;
//    private String mHomeName;
//    private String mGustName;
//
//    private OnFragmentInteractionListener mListener;
//
//    /**
//     * 异常界面
//     */
//    private LinearLayout mExceptionLayout;
//    /**
//     * 刷新
//     */
//    private RelativeLayout mProgressBarLayout;
//    /**
//     * 无历史对战
//     */
//    private LinearLayout mNoHistoryBattel;
//    /**
//     * 正常的历史对战布局
//     */
//    private LinearLayout mHistoryBattel;
//
//    /**
//     * 无主队近况
//     */
//    private LinearLayout mNoHomeRecentBattel;
//
//    /**
//     * 正常的主队近况
//     */
//    private LinearLayout mHomeRecentBattel;
//    /**
//     * 无客队近况
//     */
//    private LinearLayout mNoGuestRecentBattel;
//
//    /**
//     * 正常的客队近况
//     */
//    private LinearLayout mGuestRecentBattel;
//
//    private View view;
//    /**
//     * 积分排行的listView
//     */
//    private ListView mRankListview;
//    /**
//     * 得失球的listView
//     */
//    private ListView mGoalAndLossListview;
//    /**
//     * 历史对战的listview
//     */
//    private ListView mHistoryListview;
//    /**
//     * 主队近况的listview
//     */
//    private ListView mHomeRecentListview;
//    /**
//     * 客队近况的listview
//     */
//    private ListView mGuestRecentListview;
//
//
//    /**
//     * 积分排行adapter
//     */
//    private AnalyzeRankAdapter mRankAdapter;
//
//    private AnalyzeRankAdapter mGoalAdapter;//得失球
//
//    private AnalyzeMatchAdapter mHistoryAdapter;//历史对战
//
//    private AnalyzeMatchAdapter mHomeAdapter;//主队近况
//
//    private AnalyzeMatchAdapter mGuestAdapter;//客队近况
//
//
//    /**
//     * 积分排行数据
//     */
//    private List<AnalyzeBean.ScoreRankEntity> mRankList;
//    /**
//     * 得失球
//     */
//    private List<AnalyzeBean.GoalAndLossEntity> mGoalList;
//    /**
//     * 历史对战数据
//     */
//    private List<AnalyzeBean.BattleHistoryEntity.BattlesEntity> mHistoryBattlesList;
//    /**
//     * 主队近况数据
//     */
//    private List<AnalyzeBean.TeamRecentEntity.HomeEntity.BattlesEntity> mHomeRecentList;
//    /**
//     * 客队近况数据
//     */
//    private List<AnalyzeBean.TeamRecentEntity.GuestEntity.BattlesEntity> mGuestRecentList;
//    //五个模块
//    private AnalyzeTitle mTitleRank;
//    private AnalyzeTitle mTitleGoal;
//    private AnalyzeTitle mTitleHistory;
//    private AnalyzeTitle mTitleHomeRecently;
//    private AnalyzeTitle mTitleGuestRecently;
//    /**
//     * 历史对战的文字概括
//     */
//    private TextView mHistoryText;
//    /**
//     * 主队近况的文字概括
//     */
//    private TextView mHomeText;
//    /**
//     * 客队近况的文字概括
//     */
//    private TextView mGuestText;
//
//    private ExactSwipeRefreshLayout mRefreshLayout;//下拉刷新页面
//
//    public OldAnalyzeFragment() {
//    }
//
//    public static OldAnalyzeFragment newInstance(String param1, String param2, String param3) {
//        OldAnalyzeFragment fragment = new OldAnalyzeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        args.putString(ARG_PARAM3, param3);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        L.d(TAG, "______________onCreate");
//        if (getArguments() != null) {
//            mThirdId = getArguments().getString(ARG_PARAM1);
//            mHomeName = getArguments().getString(ARG_PARAM2);
//            mGustName = getArguments().getString(ARG_PARAM3);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_analyze, container, false);
//        initItem();
//        initData();
//        return view;
//    }
//
//    private Handler mViewHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case VIEW_STATUS_LOADING:
//
//                    mExceptionLayout.setVisibility(View.GONE);
//
//                    mRefreshLayout.setVisibility(View.GONE);
//                    mProgressBarLayout.setVisibility(View.VISIBLE);
//
//                    break;
//                case VIEW_STATUS_SUCCESS:
//
//                    mProgressBarLayout.setVisibility(View.GONE);
//                    mExceptionLayout.setVisibility(View.GONE);
//                    mRefreshLayout.setVisibility(View.VISIBLE);
//                    break;
//                case VIEW_STATUS_NET_ERROR:
//
//                    mExceptionLayout.setVisibility(View.VISIBLE);
//
//                    mProgressBarLayout.setVisibility(View.GONE);
//                    mRefreshLayout.setVisibility(View.GONE);
//                    break;
//            }
//        }
//    };
//
//
//    /**
//     * 初始化界面的6大模块
//     */
//    private void initItem() {
//        mTitleRank = (AnalyzeTitle) view.findViewById(R.id.analyze_title_rank);
//        mTitleGoal = (AnalyzeTitle) view.findViewById(R.id.analyze_title_goal);
//        mTitleHistory = (AnalyzeTitle) view.findViewById(R.id.analyze_title_history);
//        mTitleHomeRecently = (AnalyzeTitle) view.findViewById(R.id.analyze_title_home_recently);
//        mTitleGuestRecently = (AnalyzeTitle) view.findViewById(R.id.analyze_title_guest_recently);
//
//        mTitleRank.setTitle(getActivity().getString(R.string.ranking));
//        mTitleGoal.setTitle(getActivity().getString(R.string.goal_and_loss));
//        mTitleHistory.setTitle(getActivity().getString(R.string.history_battle));
//        mTitleHomeRecently.setTitle(getActivity().getString(R.string.home_recent));
//        mTitleGuestRecently.setTitle(getActivity().getString(R.string.guest_recent));
//
//        //网络异常及正在加载的控件，
//        mProgressBarLayout = (RelativeLayout) view.findViewById(R.id.analyze_progressbar);
//        mExceptionLayout = (LinearLayout) view.findViewById(R.id.network_exception_layout);
//        TextView reloadBtn = (TextView) view.findViewById(R.id.network_exception_reload_btn);
//        reloadBtn.setOnClickListener(this);
//
//        mRefreshLayout = (ExactSwipeRefreshLayout) view.findViewById(R.id.analyze_rereshlayout);
//        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
//        mRefreshLayout.setOnRefreshListener(this);
//
//
//    }
//
//    /**
//     * 初始化视图
//     */
//    private void initView() {
//        mRankListview = (ListView) view.findViewById(R.id.analyze_rank_listview);
//        mGoalAndLossListview = (ListView) view.findViewById(R.id.analyze_goal_listview);
//        mHistoryListview = (ListView) view.findViewById(R.id.analyze_history_listview);
//        mHomeRecentListview = (ListView) view.findViewById(R.id.analyze_homerecently_listview);
//        mGuestRecentListview = (ListView) view.findViewById(R.id.analyze_guestrecently_listview);
//
//        mRankListview.setDivider(MyApp.getContext().getResources().getDrawable(R.color.transparency));
//        mGoalAndLossListview.setDivider(MyApp.getContext().getResources().getDrawable(R.color.transparency));
//        mHistoryListview.setDivider(MyApp.getContext().getResources().getDrawable(R.color.transparency));
//        mHomeRecentListview.setDivider(MyApp.getContext().getResources().getDrawable(R.color.transparency));
//        mGuestRecentListview.setDivider(MyApp.getContext().getResources().getDrawable(R.color.transparency));
//
//        mHistoryText = (TextView) view.findViewById(R.id.analyze_history_text);
//        mHomeText = (TextView) view.findViewById(R.id.analyze_homerecently_text);
//        mGuestText = (TextView) view.findViewById(R.id.analyze_guestrecently_text);
//
//        //有无历史对战
//        mNoHistoryBattel = (LinearLayout) view.findViewById(R.id.history_nodata);
//        mHistoryBattel = (LinearLayout) view.findViewById(R.id.layout_history);
//
//        //有无主队近况
//        mNoHomeRecentBattel= (LinearLayout) view.findViewById(R.id.homeRecent_nodata);
//        mHomeRecentBattel= (LinearLayout) view.findViewById(R.id.layout_home_recent);
//
//        //有无客队近况
//        mNoGuestRecentBattel= (LinearLayout) view.findViewById(R.id.guest_recent_nodata);
//        mGuestRecentBattel= (LinearLayout) view.findViewById(R.id.layout_guest_recent);
//    }
//
//    /**
//     * 初始化数据
//     */
//    public void initData() {
//        Map<String, String> params = new HashMap<>();
//        params.put("thirdId", mThirdId);
//        //  http://192.168.10.242:8181/mlottery/core/footBallMatch.matchAnalysis.do?thirdId=278222&lang=zh
//        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_ANALYSIS_INFO, params,
//                new VolleyContentFast.ResponseSuccessListener<AnalyzeBean>() {
//                    @Override
//                    public void onResponse(AnalyzeBean analyzeBean) {
//
//                        if (!analyzeBean.getResult().equals("200")) {
//
//                                mTitleRank.addLayout(R.layout.analyze_nodata);
//                                mTitleGoal.addLayout(R.layout.analyze_nodata);
//                                mTitleHistory.addLayout(R.layout.analyze_nodata);
//                                mTitleHomeRecently.addLayout(R.layout.analyze_nodata);
//                                mTitleGuestRecently.addLayout(R.layout.analyze_nodata);
//
//                            return;
//                        }
//                        initItemData(analyzeBean);
//                    }
//                }, new VolleyContentFast.ResponseErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
//                        mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
//                    }
//                }, AnalyzeBean.class
//        );
//
//    }
//
//    /**
//     * 请求到数据之后的操作
//     */
//    private void initItemData(AnalyzeBean analyzeBean) {
//
//        mTitleRank.addLayout(R.layout.fragment_analyze_rank);
//        mTitleGoal.addLayout(R.layout.fragment_analyze_goal);
//        mTitleHistory.addLayout(R.layout.fragment_analyze_history);
//        mTitleHomeRecently.addLayout(R.layout.fragment_analyze_homerecently);
//        mTitleGuestRecently.addLayout(R.layout.fragment_analyze_guestrecently);
//
//        initView();//初始化加载的布局的listview等
//
//        if (analyzeBean.getBattleHistory() == null) {
//            mNoHistoryBattel.setVisibility(View.VISIBLE);
//            mHistoryBattel.setVisibility(View.GONE);
//        } else {    ////历史对战
//            mHistoryBattlesList = analyzeBean.getBattleHistory().getBattles();
//            if(getActivity()==null){
//                return;
//            }
//            mHistoryAdapter = new AnalyzeMatchAdapter(getActivity(), mHistoryBattlesList);
//            mHistoryListview.setAdapter(mHistoryAdapter);
//            mNoHistoryBattel.setVisibility(View.GONE);
//            mHistoryBattel.setVisibility(View.VISIBLE);
//
//            AnalyzeBean.BattleHistoryEntity historyBattle = analyzeBean.getBattleHistory();
//
//            mHistoryText.setText(Html.fromHtml(setHistoryText(historyBattle)));//不同字不同颜色
//        }
//
//        if(analyzeBean.getTeamRecent().getHome()==null){ //无主队近况
//            mNoHomeRecentBattel.setVisibility(View.VISIBLE);
//            mHomeRecentBattel.setVisibility(View.GONE);
//        }else{
//
//            mHomeRecentList = analyzeBean.getTeamRecent().getHome().getBattles();
//            if(getActivity()==null){
//                return;
//            }
//            mHomeAdapter = new AnalyzeMatchAdapter(getActivity(), mHomeRecentList);
//            mHomeRecentListview.setAdapter(mHomeAdapter);
//            mNoHomeRecentBattel.setVisibility(View.GONE);
//            mHomeRecentBattel.setVisibility(View.VISIBLE);
//
//            AnalyzeBean.TeamRecentEntity.HomeEntity homeRecent = analyzeBean.getTeamRecent().getHome();
//
//            mHomeText.setText(Html.fromHtml(setHomeRecentText(homeRecent)));
//        }
//
//        //客队近况无比赛
//
//        if(analyzeBean.getTeamRecent().getGuest()==null){
//            mGuestRecentBattel.setVisibility(View.GONE);
//            mNoGuestRecentBattel.setVisibility(View.VISIBLE);
//        }else{
//            //客队近况
//            mGuestRecentList = analyzeBean.getTeamRecent().getGuest().getBattles();
//            if(getActivity()==null){
//                return;
//            }
//            mGuestAdapter = new AnalyzeMatchAdapter(getActivity(), mGuestRecentList);
//            mGuestRecentListview.setAdapter(mGuestAdapter);
//
//            mGuestRecentBattel.setVisibility(View.VISIBLE);
//            mNoGuestRecentBattel.setVisibility(View.GONE);
//
//            AnalyzeBean.TeamRecentEntity.GuestEntity guestRecent = analyzeBean.getTeamRecent().getGuest();
//
//            mGuestText.setText(Html.fromHtml(setGuestRecentText(guestRecent)));
//        }
//
//
//        //积分排名
//        mRankList = analyzeBean.getScoreRank();
//        mRankAdapter = new AnalyzeRankAdapter(getActivity(), mRankList);
//        mRankListview.setAdapter(mRankAdapter);
//
//        //得失球
//        mGoalList = analyzeBean.getGoalAndLoss();
//        mGoalAdapter = new AnalyzeRankAdapter(getActivity(), mGoalList);
//        mGoalAndLossListview.setAdapter(mGoalAdapter);
//        mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
//
//    }
//
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * 历史对战的文字概括设置
//     *
//     * @param battle
//     * @return
//     */
//
//    public String setHistoryText(AnalyzeBean.BattleHistoryEntity battle) {
//            String[] array;
//            array = battle.getBattleResult().split(";");
//            String win = array[0];
//            String equ = array[1];
//            String def = array[2];
//
//            return getActivity().getString(R.string.shuangfangjin) + battle.getBattleCount() + getActivity().getString(R.string.cijiaofeng) + mHomeName + "<font color='#ff0000'><b>" + win + getActivity().getString(R.string.analyze_win) + "、 " + "</b></font> "
//                    + "<font color='#0eae7e'><b>" + equ + getActivity().getString(R.string.analyze_equ) + "、 " + "</b></font> " + "<font color='#1756e5'><b>" + def + getActivity().getString(R.string.analyze_defeat) + "。 " + "</b></font> " + getActivity().getString(R.string.winpro)
//                    + "<font color='#ff0000'><b>" + battle.getWinPro() + "</b></font> " + ", "
//                    + getActivity().getString(R.string.jin) + "<font color='#ff0000'><b>" + battle.getGoal() + "</b></font>" + getActivity().getString(R.string.qiushi) + "<font color='#1756e5'><b>" + battle.getLoss() + "</b></font>" + getActivity().getString(R.string.ball);
//
//    }
//
//    /**
//     * 主队近况的文字概括
//     *
//     * @param battle
//     * @return
//     */
//    public String setHomeRecentText(AnalyzeBean.TeamRecentEntity.HomeEntity battle) {
//        String[] array = new String[3];
//        array = battle.getBattleResult().split(";");
//        String win = array[0];
//        String equ = array[1];
//        String def = array[2];
//
//        return getActivity().getString(R.string.zhuduijin) + battle.getBattleCount() + getActivity().getString(R.string.cijiaofeng) + "<font color='#ff0000'><b>" + win + getActivity().getString(R.string.analyze_win) + "、 " + "</b></font> "
//                + "<font color='#0eae7e'><b>" + equ + getActivity().getString(R.string.analyze_equ) + "、 " + "</b></font> " + "<font color='#1756e5'><b>" + def + getActivity().getString(R.string.analyze_defeat) + "。 " + "</b></font> " + getActivity().getString(R.string.winpro)
//                + "<font color='#ff0000'><b>" + battle.getWinPro() + "</b></font> " + ", "
//                + getActivity().getString(R.string.jin) + "<font color='#ff0000'><b>" + battle.getGoal() + "</b></font>" + getActivity().getString(R.string.qiushi) + "<font color='#1756e5'><b>" + battle.getLoss() + "</b></font>" + getActivity().getString(R.string.ball);
//    }
//
//    /**
//     * 客队近况的文字概括
//     *
//     * @param battle
//     * @return
//     */
//    public String setGuestRecentText(AnalyzeBean.TeamRecentEntity.GuestEntity battle) {
//        String[] array;
//        array = battle.getBattleResult().split(";");
//        String win = array[0];
//        String equ = array[1];
//        String def = array[2];
//
//        return getActivity().getString(R.string.keduijin) + battle.getBattleCount() + getActivity().getString(R.string.cijiaofeng)  + "<font color='#ff0000'><b>" + win + getActivity().getString(R.string.analyze_win) + "、 " + "</b></font> "
//                + "<font color='#0eae7e'><b>" + equ + getActivity().getString(R.string.analyze_equ) + "、 " + "</b></font> " + "<font color='#1756e5'><b>" + def + getActivity().getString(R.string.analyze_defeat) + "。 " + "</b></font> " + getActivity().getString(R.string.winpro)
//                + "<font color='#ff0000'><b>" + battle.getWinPro() + "</b></font> " + ", "
//                + getActivity().getString(R.string.jin) + "<font color='#ff0000'><b>" + battle.getGoal() + "</b></font>" + getActivity().getString(R.string.qiushi) + "<font color='#1756e5'><b>" + battle.getLoss() + "</b></font>" + getActivity().getString(R.string.ball);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.network_exception_reload_btn:
//                mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
//                initData();
//        }
//
//    }
//
//    @Override
//    public void onRefresh() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mRefreshLayout.setRefreshing(false);
//                initData();
//            }
//        }, 1000);
//
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//}
