package com.hhly.mlottery.frame.footballframe;

import android.app.AlertDialog;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballTeamInfoActivity;
import com.hhly.mlottery.adapter.basketball.SportsDialogAdapter;
import com.hhly.mlottery.adapter.football.FootballDatabaseIntegralAdapter;
import com.hhly.mlottery.bean.footballDetails.FootballIntegralResult;
import com.hhly.mlottery.bean.footballDetails.FootballRankingData;
import com.hhly.mlottery.bean.footballDetails.FootballRankingList;
import com.hhly.mlottery.bean.footballDetails.database.DataBaseBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ScrollTouchListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 * author: yixq
 * Created by A on 2016/9/5.
 * 足球资料库积分
 */
public class FootballDatabaseIntegralFragment extends Fragment implements View.OnClickListener {

    private static final String LEAGUE = "league";
    private static final String PARAM_ID = "leagueId";
    private static final String PARAM_TYPE = "type";

    private static final String PARAM_DATE = "leagueDate";
    private static final String PARAM_MATCH_ROUND = "condition";

    private static final int STATUS_LOADING = 1;
    private static final int STATUS_ERROR = 2;
    private static final int STATUS_NO_DATA = 3;
    private static final int STATUS_LOAD_SUCCESS = 4;

    private static final int CUP_TYPE = 2;

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

    private DataBaseBean league;

    private FootballIntegralResult mResult;

    private List<FootballDatabaseIntegralAdapter.Section> mSections;
    private FootballDatabaseIntegralAdapter mAdapter;
    private RadioButton mAllRadio;
    private RadioButton mHomeRadio;
    private RadioButton mGuestRadio;
    private RadioGroup mRadioGroup;
    private int mTypeSelect = 0;// 主客场选中type（默认全部）【All: 0 、 主场：1 、 客场：2】

    private boolean isLoad = false;//是否可选择
    private int currentPosition = 0; // 当前选中 （默认选中第一项）
    private String[] mRoundString;
    private String mLeagueRound = "";
    private String mLeagueDate = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            league = args.getParcelable(LEAGUE);
            mLeagueDate = args.getString(PARAM_DATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mEmptyView = inflater.inflate(R.layout.basket_database_empty_layout, container, false);
        return inflater.inflate(R.layout.fragment_basket_database_ranking, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButtonFrame = LayoutInflater.from(view.getContext())
                .inflate(R.layout.layout_basket_database_choose, (ViewGroup) view, false);

        mTitleTextView = (TextView) mButtonFrame.findViewById(R.id.title_button);
        mLeftButton = (ImageView) mButtonFrame.findViewById(R.id.left_button);
        mRightButton = (ImageView) mButtonFrame.findViewById(R.id.right_button);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mRadioGroup = (RadioGroup)view.findViewById(R.id.gendergroup);
        mAllRadio = (RadioButton)view.findViewById(R.id.football_database_details_all);
        mAllRadio.setChecked(true);//默认选中
        mHomeRadio = (RadioButton)view.findViewById(R.id.football_database_details_home);
        mGuestRadio = (RadioButton)view.findViewById(R.id.football_database_details_guest);
        RadioGroupOnClick();

        initEmptyView();

        setIntegralDetailsOnClick();
        initRecycler();

        mTitleTextView.setOnClickListener(this);
        mLeftButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);

        load();
    }
    /**
     * RadioGroup的点击（全部、主、客切换）
     */
    private void RadioGroupOnClick(){
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == mAllRadio.getId()) {
                    mTypeSelect = 0;
                }else if(checkedId == mHomeRadio.getId()){
                    mTypeSelect = 1;
                }else if(checkedId == mGuestRadio.getId()){
                    mTypeSelect = 2;
                }
                updataAdapter(mTypeSelect);
            }
        });
    }
    public void updataAdapter(int type){

        if (type == 0) {
            mSections.clear();
            if (mResult.getRankingObj().getAll() == null || mResult.getRankingObj().getAll().equals("")){
                setStatus(STATUS_NO_DATA);
            }else{
                handleData(mResult.getRankingObj().getAll());
                mAdapter.notifyDataSetChanged();
            }
        } else if (type == 1) {
            mSections.clear();
            if (mResult.getRankingObj().getHome() == null || mResult.getRankingObj().getHome().equals("")){
                setStatus(STATUS_NO_DATA);
            }else{
                handleData(mResult.getRankingObj().getHome());
                mAdapter.notifyDataSetChanged();
            }
        } else if(type == 2){
            mSections.clear();
            if (mResult.getRankingObj().getGuest() == null || mResult.getRankingObj().getGuest().equals("")){
                setStatus(STATUS_NO_DATA);
            }else{
                handleData(mResult.getRankingObj().getGuest());
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initEmptyView() {
        mProgressBar = (ProgressBar) mEmptyView.findViewById(R.id.progress);
        mErrorLayout = mEmptyView.findViewById(R.id.error_layout);
        mRefreshTextView = (TextView) mEmptyView.findViewById(R.id.reloading_txt);
        mNoDataTextView = (TextView) mEmptyView.findViewById(R.id.no_data_txt);

        mRefreshTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });
    }

    /**
     * 更新
     */
    public void update() {
        load();
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
        mRadioGroup.setVisibility(status == STATUS_LOAD_SUCCESS ? View.VISIBLE : View.GONE);
    }

    private void initRecycler() {
        mSections = new ArrayList<>();
        mAdapter = new FootballDatabaseIntegralAdapter(mSections);
        mAdapter.setFootballTeamIntegralDetailsClickListener(footballTeamIntegralDetailsClickListener);
        mAdapter.setEmptyView(mEmptyView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(manager);
    }

    private void load() {
        mSections.clear();
        mAdapter.notifyDataSetChanged();
        setStatus(STATUS_LOADING);

//        url = "http://192.168.31.8:8080/mlottery/core/androidLeagueData.findAndroidLeagueScore.do";
        // http://192.168.31.8:8080/mlottery/core/androidLeagueData.findAndroidLeagueScore.do?lang=zh&type=2&leagueId=67&leagueDate=2014-2016&condition=外围赛
        Map<String, String> map = new HashMap<>();
        map.put(PARAM_ID , league.getLeagueId());
        map.put(PARAM_TYPE , league.getKind());

        if (mLeagueDate != null) map.put(PARAM_DATE , mLeagueDate);
        if (mLeagueRound != null && !mLeagueRound.equals("")) map.put(PARAM_MATCH_ROUND , mLeagueRound);
        String url = BaseURLs.URL_FOOTBALL_DATABASE_INTEGRAL_FIRST;
        /**
         * 这里只能用post（ps：用get请求是参数带中文，4.4手机请求不到数据...）
         */
        VolleyContentFast.requestJsonByPost(url, map,
                new VolleyContentFast.ResponseSuccessListener<FootballIntegralResult>() {
                    @Override
                    public void onResponse(FootballIntegralResult result) {
                        if (result == null || result.getRankingObj() == null) {
                            setStatus(STATUS_NO_DATA);
                            return;
                        }
                        mResult = result;
                        mAdapter.setType(result.getRankingType());

                        mRoundString = result.getSearchCondition();
                        if (mRoundString != null && mRoundString.length != 0) {
                            isLoad = true;
                        }
                        handleHeadViewNew(mRoundString, currentPosition);
                        updataAdapter(mTypeSelect);
                        if (!league.getKind().equals("2")) {
                            setStatus(STATUS_LOAD_SUCCESS);
                        }
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        isLoad = false;
                        VolleyError error = exception.getVolleyError();
                        if (error != null) error.printStackTrace();
                        setStatus(STATUS_ERROR);
                    }
                }, FootballIntegralResult.class);
    }

    /**
     * 处理数据
     *
     * @param groupList
     */
    private void handleData(List<FootballRankingList> groupList) {
        if (CollectionUtils.notEmpty(groupList)) {
            for (FootballRankingList group : groupList) {
                if (mResult.getRankingType() != FootballIntegralResult.SINGLE_LEAGUE) {
                    mSections.add(new FootballDatabaseIntegralAdapter.Section(true, group.getGroup()));
                }
                mSections.add(new FootballDatabaseIntegralAdapter.Section(null));
                if (CollectionUtils.notEmpty(group.getList())) {
                    for (FootballRankingData rankingTeam : group.getList()) {
                        mSections.add(new FootballDatabaseIntegralAdapter.Section(rankingTeam));
                    }
                }
            }
        } else {
            setStatus(STATUS_NO_DATA);
        }
    }
    /**
     *   选择按钮
     * @param data  阶段选择内容
     * @param currnIndex 当前选择的position
     */
    private void handleHeadViewNew(String[] data , Integer currnIndex){
        if (data != null) {
            if (data.length != 0) {
                mTitleTextView.setText(data[currnIndex]);
            }
            // 杯赛且大于1才显示
            if (data.length > 1 && mAdapter.getHeaderViewsCount() == 0 && mResult.getRankingType() == CUP_TYPE) {
                mAdapter.addHeaderView(mButtonFrame);
                mAdapter.setEmptyView(true, mEmptyView);
            }
                mLeftButton.setVisibility(currnIndex <= 0 ? View.GONE : View.VISIBLE);
                mRightButton.setVisibility(currnIndex >= data.length-1 ? View.GONE : View.VISIBLE);
        }
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
        load();
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
        load();
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
                    handleHeadViewNew(mRoundString ,currentPosition);
                    load();
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
                    handleHeadViewNew(mRoundString ,currentPosition);
                    load();
                }
            });
            scrollview.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
        }
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(view);
    }

    public static FootballDatabaseIntegralFragment newInstance(DataBaseBean league, String season) {

        Bundle args = new Bundle();
        args.putParcelable(LEAGUE, league);
        args.putString(PARAM_DATE, season);
        FootballDatabaseIntegralFragment fragment = new FootballDatabaseIntegralFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setSeason(String type) {
        this.mLeagueDate = type;
        Bundle args = getArguments();
        if (args != null) args.putString(PARAM_DATE, type);
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

    private FootballTeamIntegralDetailsClickListener footballTeamIntegralDetailsClickListener;
    public interface FootballTeamIntegralDetailsClickListener {
        void IntegralDetailsOnClick(View view, FootballRankingData teamData);
    }
    private void setIntegralDetailsOnClick(){
        footballTeamIntegralDetailsClickListener = new FootballTeamIntegralDetailsClickListener() {
            @Override
            public void IntegralDetailsOnClick(View view, FootballRankingData teamData) {
                        Intent homeIntent = new Intent(getContext(), FootballTeamInfoActivity.class);
                        homeIntent.putExtra("TEAM_ID", teamData.getTid() + "");
                        homeIntent.putExtra("TITLE_TEAM_NAME", teamData.getName());
                        startActivity(homeIntent);
            }
        };
    }
}
