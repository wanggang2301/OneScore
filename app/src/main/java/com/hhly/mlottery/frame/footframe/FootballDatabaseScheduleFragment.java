package com.hhly.mlottery.frame.footframe;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketballDatabaseScheduleSectionAdapter;
import com.hhly.mlottery.adapter.basketball.SportsDialogAdapter;
import com.hhly.mlottery.adapter.football.FootballDatabaseScheduleSectionAdapter;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchStage;
import com.hhly.mlottery.bean.basket.basketdatabase.ScheduleResult;
import com.hhly.mlottery.bean.basket.infomation.LeagueBean;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.FootballDatabaseScheduleBean;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.ScheduleDataBean;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.ScheduleDatasBean;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.ScheduleRaceBean;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.LocaleFactory;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ScrollTouchListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 * author: yixq
 * Created by A on 2016/9/2.
 * 足球资料库赛程
 */
public class FootballDatabaseScheduleFragment extends Fragment implements View.OnClickListener {

    private static final int MATCH_TYPE_LEAGUE = 1; // 联赛
    private static final int MATCH_TYPE_CUP = 2; // 杯赛

    private static final String LEAGUE = "league";
    private static final String PARAM_ID = "leagueId";
    private static final String PARAM_SEASON = "season";
    private static final String PARAM_MATCH_TYPE = "matchType";
    private static final String PARAM_FIRST_STAGE_ID = "firstStageId";
    private static final String PARAM_SECOND_STAGE_ID = "secondStageId";

    private static final int STATUS_LOADING = 1;
    private static final int STATUS_ERROR = 2;
    private static final int STATUS_NO_DATA = 3;

    View mButtonFrame;
    TextView mTitleTextView;
    ImageView mLeftButton;
    ImageView mRightButton;

    View mEmptyView;
    ProgressBar mProgressBar;
    View mErrorLayout;
    TextView mRefreshTextView;
    TextView mNoDataTextView;

    RecyclerView mRecyclerView;

    private LeagueBean league;
    private String season = null;

    private ScheduleResult mResult;
    private FootballDatabaseScheduleBean mResultNew;
    private List<BasketballDatabaseScheduleSectionAdapter.Section> mSections;
    private List<FootballDatabaseScheduleSectionAdapter.Section> mSectionsNew;
    private List<MatchStage> mStageList;
    private BasketballDatabaseScheduleSectionAdapter mAdapter;
    private FootballDatabaseScheduleSectionAdapter mAdapterNew;
    private String[] mRoundString;
    private String mLeagueRound = "";
    private boolean isLoad = false;//是否可选择
    private String url ;
    private List<ScheduleDataBean> mDataBean;//选择内容
    private int currentPosition = 0; // 当前选中 （默认选中第一项）

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            league = args.getParcelable(LEAGUE);
            season = args.getString(PARAM_SEASON);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mEmptyView = inflater.inflate(R.layout.basket_database_empty_layout, container, false);
        return inflater.inflate(R.layout.fragment_basket_database_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButtonFrame = LayoutInflater.from(view.getContext())
                .inflate(R.layout.layout_basket_database_choose, (ViewGroup) view, false);
        ViewGroup.LayoutParams layoutParams = mEmptyView.getLayoutParams();
        layoutParams.height = DisplayUtil.dip2px(getContext(), 178);
        mEmptyView.setLayoutParams(layoutParams);

        mTitleTextView = (TextView) mButtonFrame.findViewById(R.id.title_button);
        mLeftButton = (ImageView) mButtonFrame.findViewById(R.id.left_button);
        mRightButton = (ImageView) mButtonFrame.findViewById(R.id.right_button);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mStageList = new ArrayList<>();

        initEmptyView();

        initRecycler();

        mTitleTextView.setOnClickListener(this);
        mLeftButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);

        initData(null , null);
    }

    private void initEmptyView() {
        mProgressBar = (ProgressBar) mEmptyView.findViewById(R.id.progress);
        mErrorLayout = mEmptyView.findViewById(R.id.error_layout);
        mRefreshTextView = (TextView) mEmptyView.findViewById(R.id.reloading_txt);
        mNoDataTextView = (TextView) mEmptyView.findViewById(R.id.no_data_txt);

        mRefreshTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(null, null);
            }
        });
    }

    /**
     * 加载前一项（左点击）
     * @param currPosition
     */
    private void loadLeft(int currPosition){
        currentPosition = currPosition - 1;
        if (currentPosition <= 0) {
            mLeagueRound = mRoundString[0];
        }else{
            mLeagueRound = mRoundString[currentPosition];
        }
        handleHeadViewNew(mRoundString ,currentPosition);
        initData(null , null);
    }
    /**
     * 加载后一项（右点击）
     * @param currPosition
     */
    private void loadRight(int currPosition){
        currentPosition = currPosition + 1;
        if (currentPosition >= mRoundString.length-1) {
            mLeagueRound = mRoundString[mRoundString.length-1];
        }else{
            mLeagueRound = mRoundString[currentPosition];
        }
        handleHeadViewNew(mRoundString ,currentPosition);
        initData(null , null);
    }

    /**
     * 刷新数据
     */
    public void update() {
        initData(null , null);
    }

    /**
     * 设置状态
     *
     * @param status
     */
    public void setStatus(int status) {
        mNoDataTextView.setVisibility(status == STATUS_NO_DATA ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(status == STATUS_LOADING ? View.VISIBLE : View.GONE);
        mErrorLayout.setVisibility(status == STATUS_ERROR ? View.VISIBLE : View.GONE);
    }

    private void initData(String firstStageId, String secondStageId){
        mSectionsNew.clear();
        mAdapterNew.notifyDataSetChanged();
        setStatus(STATUS_LOADING);

        // http://192.168.31.115:8888/mlottery/core/basketballData.findSchedule.do?lang=zh&leagueId=1&season=2015-2016
        //http://192.168.31.8:8080/mlottery/core/androidLeagueData.findAndroidLeagueRound.do?lang=zh&leagueId=36&type=0&leagueDate=2016-2017&leagueRound=4
        Map<String , String> map = new HashMap();
        map.put("leagueId" , "36");
        if (season != null) {
            map.put("leagueDate" , season);
        }else{
        map.put("leagueDate" , "2016-2017");
        }
        if (mLeagueRound != null && !mLeagueRound.equals("")) {
            map.put("leagueRound" , mLeagueRound);
        }else{
            map.put("leagueRound" , "4");
        }

        if (url == null || url == "") {
            url = "http://192.168.31.8:8080/mlottery/core/androidLeagueData.findAndroidLeagueRound.do"; //第一次进入的url
        }
//        VolleyContentFast.requestJsonByGet(BaseURLs.URL_BASKET_DATABASE_SCHEDULE, map,
        VolleyContentFast.requestJsonByGet(url, map,
                new VolleyContentFast.ResponseSuccessListener<FootballDatabaseScheduleBean>() {
                    @Override
                    public void onResponse(FootballDatabaseScheduleBean result) {
                        if (result == null || result.getRace() == null) {
                            setStatus(STATUS_NO_DATA);
                            mButtonFrame.setVisibility(View.GONE);
                            return;
                        }
                        mButtonFrame.setVisibility(View.VISIBLE);
                        mResultNew = result;

                        if (result.getData() != null) {
                            mRoundString = result.getData();
                        }

                        if (mRoundString != null && mRoundString.length != 0) {
                            isLoad = true;
                        }
                        handleHeadViewNew(mRoundString ,currentPosition);
                        handleDataNew(result.getRace());
                        mAdapterNew.notifyDataSetChanged();
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException e) {
                        isLoad = false;
                        VolleyError error = e.getVolleyError();
                        if (error != null) error.printStackTrace();
                        setStatus(STATUS_ERROR);
                    }
                }, FootballDatabaseScheduleBean.class);
    }

    private void putIfNotNull(Map<String, String> map, String key, String val) {
        if (val != null) {
            map.put(key, val);
        }
    }

    /**
     *   选择按钮
     * @param data  阶段选择内容
     * @param currnIndex 当前选择的position
     */
    private void handleHeadViewNew(String[] data , Integer currnIndex){

            if (data == null || data.length == 0) {
                mTitleTextView.setText("");
            }else{
                mTitleTextView.setText(data[currnIndex]);
            }

            mLeftButton.setVisibility(currnIndex <= 0 ? View.GONE : View.VISIBLE);
            mRightButton.setVisibility(currnIndex >= data.length-1 ? View.GONE : View.VISIBLE);

    }

    /**
     * 列表数据
     * @param matchData
     */
    private void handleDataNew(List<ScheduleRaceBean> matchData) {
        if (CollectionUtils.notEmpty(matchData)) {
            for (ScheduleRaceBean matchDay : matchData) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd E", LocaleFactory.get());
                mSectionsNew.add(new FootballDatabaseScheduleSectionAdapter
                        .Section(true, dateFormat.format(matchDay.getDay())));
                for (ScheduleDatasBean match : matchDay.getDatas()) {
                    mSectionsNew.add(new FootballDatabaseScheduleSectionAdapter
                            .Section(match));
                }
            }
        } else {
            setStatus(STATUS_NO_DATA);
            mButtonFrame.setVisibility(View.GONE);
        }
    }

    /**
     * 选中器弹框
     */
    public void setDialog(){
        // Dialog 设置
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.football_scheduledialog, null);
        TextView titleView = (TextView) view.findViewById(R.id.headerView);
        titleView.setText(getString(R.string.football_database_details_stage));

        final List<String> data = new ArrayList<>();
        Collections.addAll(data, mRoundString);

        final SportsDialogAdapter mDialogAdapter = new SportsDialogAdapter(data, getContext(), currentPosition);

        final AlertDialog mAlertDialog = mBuilder.create();
        mAlertDialog.setCanceledOnTouchOutside(true);//设置空白处点击 dialog消失

        /**
         * 根据List数据条数加载不同的ListView （数据多加载可滑动 ScrollTouchListview）
         */
        ScrollView scrollview = (ScrollView) view.findViewById(R.id.basket_sports_scroll);//数据多时显示
        ScrollTouchListView scrollListview = (ScrollTouchListView) view.findViewById(R.id.sport_date_scroll);
        ListView listview = (ListView) view.findViewById(R.id.sport_date);//数据少时显示
        if (data.size() > 5) {
            scrollListview.setAdapter(mDialogAdapter);
            scrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    currentPosition = position;
                    mDialogAdapter.updateDatas(position);
                    mDialogAdapter.notifyDataSetChanged();

                    mAlertDialog.dismiss();
                    String newData = mRoundString[currentPosition];
                    mLeagueRound = newData;
                    url = "http://192.168.31.8:8080/mlottery/core/androidLeagueData.findAndroidLeagueRace.do"; //选择后的URL
                    handleHeadViewNew(mRoundString ,currentPosition);
                    initData(null , null);
                }
            });
            scrollview.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        } else {
            listview.setAdapter(mDialogAdapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    currentPosition = position;
                    mDialogAdapter.updateDatas(position);
                    mDialogAdapter.notifyDataSetChanged();

                    mAlertDialog.dismiss();
                    String newData = mRoundString[currentPosition];
                    mLeagueRound = newData;
                    url = "http://192.168.31.8:8080/mlottery/core/androidLeagueData.findAndroidLeagueRace.do"; //选择后的URL
                    handleHeadViewNew(mRoundString ,currentPosition);
                    initData(null , null);
                }
            });
            scrollview.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
        }
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(view);
    }

    private void initRecycler() {
        mSections = new ArrayList<>();
        mAdapter = new BasketballDatabaseScheduleSectionAdapter(mSections);
        mAdapter.addHeaderView(mButtonFrame);
        mAdapter.setEmptyView(true, mEmptyView);

        mSectionsNew = new ArrayList<>();
        mAdapterNew = new FootballDatabaseScheduleSectionAdapter(mSectionsNew);
        mAdapterNew.addHeaderView(mButtonFrame);
        mAdapterNew.setEmptyView(true , mEmptyView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mAdapterNew);
        mRecyclerView.setLayoutManager(manager);
    }

    public static FootballDatabaseScheduleFragment newInstance(LeagueBean leagueBean, String season) {

        Bundle args = new Bundle();
        args.putParcelable(LEAGUE, leagueBean);
        args.putString(PARAM_SEASON, season);
        FootballDatabaseScheduleFragment fragment = new FootballDatabaseScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public void setSeason(String season) {
        this.season = season;
        Bundle args = getArguments();
        if (args != null) args.putString(PARAM_SEASON, season);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_button:
                if (isLoad) {
                    setDialog();
                }
                break;
            case R.id.left_button:
                if (isLoad) {
                    loadLeft(currentPosition);
                }
                break;
            case R.id.right_button:
                if (isLoad) {
                    loadRight(currentPosition);
                }
                break;
        }

    }
}
