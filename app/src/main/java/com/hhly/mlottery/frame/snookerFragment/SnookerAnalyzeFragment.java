package com.hhly.mlottery.frame.snookerFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.AnalyzeMatchAdapter;
import com.hhly.mlottery.adapter.AnalyzeRankAdapter;
import com.hhly.mlottery.bean.footballDetails.AnalyzeBean;
import com.hhly.mlottery.bean.snookerbean.snookerDetail.SnookerAnalyzeBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.AnalyzeTitle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by mdy155
 * date 2017.2/16
 * 斯诺克分析页面
 */
public class SnookerAnalyzeFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final java.lang.String TAG = "SnookerAnalyzeFragment";

    private final static int VIEW_STATUS_LOADING = 1;
    private final static int VIEW_STATUS_SUCCESS = 3;
    private final static int VIEW_STATUS_NET_ERROR = 4;

    private String mThirdId;
    private String mHomeName;
    private String mGustName;


    /**
     * 异常界面
     */
    private LinearLayout mExceptionLayout;
    /**
     * 刷新
     */
    private RelativeLayout mProgressBarLayout;
    /**
     * 无历史对战
     */
    private LinearLayout mNoHistoryBattle;
    /**
     * 正常的历史对战布局
     */
    private LinearLayout mHistoryBattle;

    /**
     * 无主队近况
     */
    private LinearLayout mNoHomeRecentBattle;

    /**
     * 正常的主队近况
     */
    private LinearLayout mHomeRecentBattle;
    /**
     * 无客队近况
     */
    private LinearLayout mNoGuestRecentBattle;

    /**
     * 正常的客队近况
     */
    private LinearLayout mGuestRecentBattle;

    private View view;
//    /**
//     * 积分排行的listView
//     */
//    private ListView mRankListView;

    private LinearLayout mAnalyze_layout; //展示数据的layout
    /**
     * 职业对比
     */
    private ListView mGoalAndLossListView;

    private LinearLayout mRankNodata;
    /**
     * 历史对战的listview
     */
    private ListView mHistoryListView;
    /**
     * 主队近况的listview
     */
    private ListView mHomeRecentListView;
    /**
     * 客队近况的listview
     */
    private ListView mGuestRecentListView;



    /**
     * 积分排行adapter
     */

    private AnalyzeRankAdapter mGoalAdapter;//职业排行

    private AnalyzeMatchAdapter mHistoryAdapter;//历史对战

    private AnalyzeMatchAdapter mHomeAdapter;//主队近况

    private AnalyzeMatchAdapter mGuestAdapter;//客队近况


    /**
     * 职业对比
     */
    private List<SnookerAnalyzeBean.ProfessionDataEntity> mGoalList;
    /**
     * 历史对战数据
     */
    private List<SnookerAnalyzeBean.SnookerEntity> mHistoryBattlesList;
    /**
     * 主队近况数据
     */
    private List<SnookerAnalyzeBean.SnookerEntity> mHomeRecentList;
    /**
     * 客队近况数据
     */
    private List<SnookerAnalyzeBean.SnookerEntity> mGuestRecentList;
    //4个模块
    private AnalyzeTitle mTitleGoal;
    private AnalyzeTitle mTitleHistory;
    private AnalyzeTitle mTitleHomeRecently;
    private AnalyzeTitle mTitleGuestRecently;
    /**
     * 历史对战的文字概括
     */
    private TextView mHistoryText;
    /**
     * 主队近况的文字概括
     */
    private TextView mHomeText;
    /**
     * 客队近况的文字概括
     */
    private TextView mGuestText;

    private SnookerAnalyzeBean mSnookerAnalyzeBean;



    public SnookerAnalyzeFragment() {

    }


    public static SnookerAnalyzeFragment newInstance(String param1, String param2) {
        SnookerAnalyzeFragment fragment = new SnookerAnalyzeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThirdId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_snooker_analyze, container, false);
        initItem();
        initData();
        return view;
    }

    private Handler mViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case VIEW_STATUS_LOADING:

                    mExceptionLayout.setVisibility(View.GONE);
                    mAnalyze_layout.setVisibility(View.GONE);
                    mProgressBarLayout.setVisibility(View.VISIBLE);

                    break;
                case VIEW_STATUS_SUCCESS:

                    mProgressBarLayout.setVisibility(View.GONE);
                    mExceptionLayout.setVisibility(View.GONE);
                    mAnalyze_layout.setVisibility(View.VISIBLE);
                    break;
                case VIEW_STATUS_NET_ERROR:

                    mExceptionLayout.setVisibility(View.VISIBLE);
                    mAnalyze_layout.setVisibility(View.GONE);
                    mProgressBarLayout.setVisibility(View.GONE);
                    break;
            }
        }
    };


    /**
     * 初始化界面的4大模块
     */
    private void initItem() {
        mAnalyze_layout= (LinearLayout) view.findViewById(R.id.snooker_analyze_view);

        mTitleGoal = (AnalyzeTitle) view.findViewById(R.id.analyze_title_goal);
        mTitleHistory = (AnalyzeTitle) view.findViewById(R.id.analyze_title_history);
        mTitleHomeRecently = (AnalyzeTitle) view.findViewById(R.id.analyze_title_home_recently);
        mTitleGuestRecently = (AnalyzeTitle) view.findViewById(R.id.analyze_title_guest_recently);

        mTitleGoal.setTitle(getResources().getString(R.string.snooker_player_compare));
        mTitleHistory.setTitle(getResources().getString(R.string.snooker_history_bettle));
        mTitleHomeRecently.setTitle(getResources().getString(R.string.snooker_player1));
        mTitleGuestRecently.setTitle(getResources().getString(R.string.snooker_player2));

        //网络异常及正在加载的控件，
        mProgressBarLayout = (RelativeLayout) view.findViewById(R.id.snooker_analyze_progressbar);
        mExceptionLayout = (LinearLayout) view.findViewById(R.id.network_exception_layout);
        TextView reloadBtn = (TextView) view.findViewById(R.id.network_exception_reload_btn);
        reloadBtn.setOnClickListener(this);

    }

    /**
     * 初始化视图
     */
    private void initView() {
//        mRankListView = (ListView) view.findViewById(R.id.analyze_rank_listview);
        mRankNodata= (LinearLayout) view.findViewById(R.id.rank_nodata);
        mGoalAndLossListView = (ListView) view.findViewById(R.id.analyze_goal_listview);
        mHistoryListView = (ListView) view.findViewById(R.id.analyze_history_listview);
        mHomeRecentListView = (ListView) view.findViewById(R.id.analyze_homerecently_listview);
        mGuestRecentListView = (ListView) view.findViewById(R.id.analyze_guestrecently_listview);

//        mRankListView.setDivider(MyApp.getContext().getResources().getDrawable(R.color.transparency));
        mGoalAndLossListView.setDivider(MyApp.getContext().getResources().getDrawable(R.color.transparency));
        mHistoryListView.setDivider(MyApp.getContext().getResources().getDrawable(R.color.transparency));
        mHomeRecentListView.setDivider(MyApp.getContext().getResources().getDrawable(R.color.transparency));
        mGuestRecentListView.setDivider(MyApp.getContext().getResources().getDrawable(R.color.transparency));
        mGoalAndLossListView.setFocusable(false);
        mHistoryListView.setFocusable(false);
        mHomeRecentListView.setFocusable(false);
        mGuestRecentListView.setFocusable(false);

        mHistoryText = (TextView) view.findViewById(R.id.analyze_history_text);
        mHomeText = (TextView) view.findViewById(R.id.analyze_homerecently_text);
        mGuestText = (TextView) view.findViewById(R.id.analyze_guestrecently_text);

        //有无历史对战
        mNoHistoryBattle = (LinearLayout) view.findViewById(R.id.history_nodata);
        mHistoryBattle = (LinearLayout) view.findViewById(R.id.layout_history);

        //有无主队近况
        mNoHomeRecentBattle = (LinearLayout) view.findViewById(R.id.homeRecent_nodata);
        mHomeRecentBattle = (LinearLayout) view.findViewById(R.id.layout_home_recent);

        //有无客队近况
        mNoGuestRecentBattle = (LinearLayout) view.findViewById(R.id.guest_recent_nodata);
        mGuestRecentBattle = (LinearLayout) view.findViewById(R.id.layout_guest_recent);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        Map<String, String> params = new HashMap<>();
        params.put("matchId", mThirdId);
        //  http://192.168.10.242:8181/mlottery/core/footBallMatch.matchAnalysis.do?thirdId=278222&lang=zh
        String url="http://m.13322.com:81/mlottery/core/snookerAnalysis.findAnalysisInfo.do";
        VolleyContentFast.requestJsonByGet(BaseURLs.SNOOKER_ANALYZE_URL, params,
                new VolleyContentFast.ResponseSuccessListener<SnookerAnalyzeBean>() {
                    @Override
                    public void onResponse(SnookerAnalyzeBean analyzeBean) {

                        if (!analyzeBean.getResult().equals("200")) {

                            mTitleGoal.addLayout(R.layout.analyze_nodata);
                            mTitleHistory.addLayout(R.layout.analyze_nodata);
                            mTitleHomeRecently.addLayout(R.layout.analyze_nodata);
                            mTitleGuestRecently.addLayout(R.layout.analyze_nodata);

                            return;
                        }
                        mSnookerAnalyzeBean=analyzeBean;
                        initItemData(analyzeBean);
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                    }
                }, SnookerAnalyzeBean.class
        );

    }

    /**
     * 请求到数据之后的操作
     */
    private void initItemData(SnookerAnalyzeBean analyzeBean) {

        mTitleGoal.addLayout(R.layout.fragment_analyze_goal);
        mTitleHistory.addLayout(R.layout.fragment_analyze_history);
        mTitleHomeRecently.addLayout(R.layout.fragment_analyze_homerecently);
        mTitleGuestRecently.addLayout(R.layout.fragment_analyze_guestrecently);

        initView();//初始化加载的布局的listview等

        if (analyzeBean.getBattleHistory() == null||analyzeBean.getBattleHistory().size()==0) { //历史对比
            mNoHistoryBattle.setVisibility(View.VISIBLE);
            mHistoryBattle.setVisibility(View.GONE);
        } else {    ////历史对战
            mHistoryBattlesList = analyzeBean.getBattleHistory();
            if(getActivity()==null){
                return;
            }
            mHistoryAdapter = new AnalyzeMatchAdapter(getActivity(), mHistoryBattlesList);
            mHistoryListView.setAdapter(mHistoryAdapter);
            mNoHistoryBattle.setVisibility(View.GONE);
            mHistoryBattle.setVisibility(View.VISIBLE);

            SnookerAnalyzeBean.SnookerStatisics historyBattle = analyzeBean.getBattleHistoryStatistics();

            if(historyBattle!=null){
                mHistoryText.setText(Html.fromHtml(setHistoryText(historyBattle)));//不同字不同颜色
            }

        }

        if(analyzeBean.getPlayerRecent().getHome()==null||analyzeBean.getPlayerRecent().getHome().size()==0){ //无主队近况
            mNoHomeRecentBattle.setVisibility(View.VISIBLE);
            mHomeRecentBattle.setVisibility(View.GONE);
        }else{

            mHomeRecentList = analyzeBean.getPlayerRecent().getHome();
            if(getActivity()==null){
                return;
            }
            mHomeAdapter = new AnalyzeMatchAdapter(getActivity(), mHomeRecentList);
            mHomeRecentListView.setAdapter(mHomeAdapter);
            mNoHomeRecentBattle.setVisibility(View.GONE);
            mHomeRecentBattle.setVisibility(View.VISIBLE);

            SnookerAnalyzeBean.SnookerStatisics homeRecent = analyzeBean.getPlayerRecent().getHomeStatisics();
                //设置最近比赛信息1
            if(homeRecent!=null){
                mHomeText.setText(Html.fromHtml(setHomeRecentText(homeRecent)));
            }

        }

        //客队近况无比赛

        if(analyzeBean.getPlayerRecent().getGuest()==null||analyzeBean.getPlayerRecent().getGuest().size()==0){
            mGuestRecentBattle.setVisibility(View.GONE);
            mNoGuestRecentBattle.setVisibility(View.VISIBLE);
        }else{
            //客队近况
            mGuestRecentList = analyzeBean.getPlayerRecent().getGuest();
            if(getActivity()==null){
                return;
            }
            mGuestAdapter = new AnalyzeMatchAdapter(getActivity(), mGuestRecentList);
            mGuestRecentListView.setAdapter(mGuestAdapter);

            mGuestRecentBattle.setVisibility(View.VISIBLE);
            mNoGuestRecentBattle.setVisibility(View.GONE);

            SnookerAnalyzeBean.SnookerStatisics guestRecent = analyzeBean.getPlayerRecent().getGuestStatisics();
            //设置最近比赛信息2
            if(guestRecent!=null){
                mGuestText.setText(Html.fromHtml(setGuestRecentText(guestRecent)));
            }

        }


//        //积分排名
//        mRankList = analyzeBean.getScoreRank();
//        mRankAdapter = new AnalyzeRankAdapter(getActivity(), mRankList);
////        mRankListView.setAdapter(mRankAdapter);

        //得失球
        if(analyzeBean.getProfessionData()==null||analyzeBean.getProfessionData().size()==0){
            mRankNodata.setVisibility(View.VISIBLE);
            mGuestRecentListView.setVisibility(View.GONE);
        }else {
            mGoalList = analyzeBean.getProfessionData();
            mRankNodata.setVisibility(View.GONE);
            mGuestRecentListView.setVisibility(View.VISIBLE);
            if(getActivity()!=null){
                mGoalAdapter = new AnalyzeRankAdapter(getActivity(), mGoalList);
                mGoalAndLossListView.setAdapter(mGoalAdapter);
            }
        }

        mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);

    }



    /**
     * 历史对战的文字概括设置
     *
     * @param battle
     * @return
     */

    public String setHistoryText(SnookerAnalyzeBean.SnookerStatisics battle) {

        return getActivity().getString(R.string.shuangfangjin) + battle.getVsCount() + getActivity().getString(R.string.cijiaofeng) + mSnookerAnalyzeBean.getMatchInfo().getHomePlayer() + "<font color='#ff0000'><b>" + battle.getWin() + getActivity().getString(R.string.analyze_win) + "、 " + "</b></font> "
                + "<font color='#0eae7e'><b>" + battle.getDraw() + getActivity().getString(R.string.analyze_equ) + "、 " + "</b></font> " + "<font color='#1756e5'><b>" + battle.getLose() + getActivity().getString(R.string.analyze_defeat) + "。 " + "</b></font> " + getActivity().getString(R.string.winpro)
                + "<font color='#ff0000'><b>" + battle.getWinPercent() + "</b></font> ";
    }

    /**
     * 主队近况的文字概括
     *
     * @param battle
     * @return
     */
    public String setHomeRecentText(SnookerAnalyzeBean.SnookerStatisics battle) {

        return getActivity().getString(R.string.zhuduijin) + battle.getVsCount() + getActivity().getString(R.string.cibisai)+mSnookerAnalyzeBean.getMatchInfo().getHomePlayer() + "<font color='#ff0000'><b>" + battle.getWin() + getActivity().getString(R.string.analyze_win) + "、 " + "</b></font> "
                + "<font color='#0eae7e'><b>" + battle.getDraw() + getActivity().getString(R.string.analyze_equ) + "、 " + "</b></font> " + "<font color='#1756e5'><b>" + battle.getLose() + getActivity().getString(R.string.analyze_defeat) + "。 " + "</b></font> " + getActivity().getString(R.string.winpro)
                + "<font color='#ff0000'><b>" + battle.getWinPercent() + "</b></font> " ;
    }

    /**
     * 客队近况的文字概括
     *
     * @param battle
     * @return
     */
    public String setGuestRecentText(SnookerAnalyzeBean.SnookerStatisics battle) {


        return getActivity().getString(R.string.zhuduijin) + battle.getVsCount() + getActivity().getString(R.string.cibisai) +mSnookerAnalyzeBean.getMatchInfo().getGuestPlayer() + "<font color='#ff0000'><b>" + battle.getWin() + getActivity().getString(R.string.analyze_win) + "、 " + "</b></font> "
                + "<font color='#0eae7e'><b>" + battle.getDraw() + getActivity().getString(R.string.analyze_equ) + "、 " + "</b></font> " + "<font color='#1756e5'><b>" + battle.getLose() + getActivity().getString(R.string.analyze_defeat) + "。 " + "</b></font> " + getActivity().getString(R.string.winpro)
                + "<font color='#ff0000'><b>" + battle.getWinPercent() + "</b></font> " ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network_exception_reload_btn:
                mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                initData();
        }

    }

}
