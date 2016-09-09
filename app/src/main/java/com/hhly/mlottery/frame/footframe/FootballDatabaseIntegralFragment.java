package com.hhly.mlottery.frame.footframe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketballDatabaseRankingAdapter;
import com.hhly.mlottery.bean.basket.basketdatabase.IntegralResult;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchStage;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingGroup;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingResult;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingTeam;
import com.hhly.mlottery.bean.footballDetails.database.DataBaseBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.basketballframe.CupMatchStageChooseDialogFragment;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * description:
 * author: yixq
 * Created by A on 2016/9/5.
 * 足球资料库积分
 */
public class FootballDatabaseIntegralFragment extends Fragment{

    private static final String LEAGUE = "league";
    private static final String PARAM_ID = "leagueId";
    private static final String PARAM_SEASON = "season";
    private static final String PARAM_MATCH_TYPE = "matchType";
    private static final String PARAM_FIRST_STAGE_ID = "firstStageId";

    private static final int STATUS_LOADING = 1;
    private static final int STATUS_ERROR = 2;
    private static final int STATUS_NO_DATA = 3;
    private static final int STATUS_LOAD_SUCCESS = 4;

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
    private String season;

    private IntegralResult mResult;

    private List<BasketballDatabaseRankingAdapter.Section> mSections;
    private BasketballDatabaseRankingAdapter mAdapter;
    private RadioButton mAllRadio;
    private RadioButton mHomeRadio;
    private RadioButton mGuestRadio;
    private RadioGroup mRadioGroup;
    private int mTypeSelect = 0;// 主客场选中type（默认全部）【All: 0 、 主场：1 、 客场：2】

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
//        mRadioGroup.setVisibility(View.VISIBLE);
        mAllRadio = (RadioButton)view.findViewById(R.id.football_database_details_all);
        mAllRadio.setChecked(true);//默认选中
        mHomeRadio = (RadioButton)view.findViewById(R.id.football_database_details_home);
        mGuestRadio = (RadioButton)view.findViewById(R.id.football_database_details_guest);
        RadioGroupOnClick();

        initEmptyView();

        initListener();

        initRecycler();

        load(null);
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
            handleData(mResult.getRankingObj().getAll());
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "all==", Toast.LENGTH_SHORT).show();
        } else if (type == 1) {
            mSections.clear();
            handleData(mResult.getRankingObj().getHome());
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "home==", Toast.LENGTH_SHORT).show();
        } else if(type == 2){
            mSections.clear();
            handleData(mResult.getRankingObj().getGuest());
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "guest==", Toast.LENGTH_SHORT).show();
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
                load(null);
            }
        });
    }

    /**
     * 更新
     */
    public void update() {
        load(null);
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

    private void initListener() {

        RxView.clicks(mTitleTextView)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mResult != null) {
                            CupMatchStageChooseDialogFragment dialog =
                                    CupMatchStageChooseDialogFragment.newInstance(mResult.getStageResult());
                            dialog.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, int i) {
                                    load(mResult.getSearchCondition().get(i).getStageId());
                                }
                            });
                            dialog.show(getChildFragmentManager(), "stageChoose");
                        } else {
                            ToastTools.showQuick(getContext(), "稍候，获取数据中");
                        }
                    }
                });

        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mResult == null) return;
                if (mResult.getFirstStageIndex() == null) return;
                List<MatchStage> matchStages = mResult.getSearchCondition();
                if (!CollectionUtils.notEmpty(matchStages)) return;
                if (mResult.getFirstStageIndex() > 0) {
                    load(matchStages.get(mResult.getFirstStageIndex() - 1).getStageId());
                }
            }
        });
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mResult == null) return;
                if (mResult.getFirstStageIndex() == null) return;
                List<MatchStage> matchStages = mResult.getSearchCondition();
                if (!CollectionUtils.notEmpty(matchStages)) return;
                if (mResult.getFirstStageIndex() < matchStages.size() - 1) {
                    load(matchStages.get(mResult.getFirstStageIndex() + 1).getStageId());
                }
            }
        });
    }

    private void initRecycler() {
        mSections = new ArrayList<>();
        mAdapter = new BasketballDatabaseRankingAdapter(mSections);
        mAdapter.setEmptyView(mEmptyView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(manager);
    }

    private void load(String firstStageId) {
        mSections.clear();
        mAdapter.notifyDataSetChanged();
        setStatus(STATUS_LOADING);

//        Map<String, String> params = new HashMap<>();
//        params.put(PARAM_ID, league.getLeagueId());
//        putIfNotNull(params, PARAM_SEASON, season);
//        putIfNotNull(params, PARAM_FIRST_STAGE_ID, firstStageId);
//        params.put(PARAM_MATCH_TYPE, league.getMatchType().toString());
        // http://192.168.31.72:3000/basketball/ranking
        // http://192.168.31.115:8888/mlottery/core/basketballData.findRanking.do?lang=zh&leagueId=7&season=2014-2015&matchType=2
//        VolleyContentFast.requestJsonByGet(BaseURLs.URL_BASKET_DATABASE_RANKING, params,
        String url = "http://192.168.10.242:8181/mlottery/core/basketballData.findRanking117.do";
        Map<String, String> map = new HashMap<>();
        map.put("leagueId" , "1");
        map.put("matchType" , "1");
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DATABASE_INTEGRAL, map,
                new VolleyContentFast.ResponseSuccessListener<IntegralResult>() {
                    @Override
                    public void onResponse(IntegralResult result) {
                        if (result == null || result.getRankingObj() == null) {
                            setStatus(STATUS_NO_DATA);
                            return;
                        }
                        mResult = result;
                        Integer firstStageIndex = result.getFirstStageIndex();
                        mAdapter.setType(result.getRankingType());
                        handleHeadView(result.getSearchCondition(), firstStageIndex);
                        updataAdapter(mTypeSelect);
                        setStatus(STATUS_LOAD_SUCCESS);
//                        handleData(result.getRankingObj().getAll());
//                        mAdapter.notifyDataSetChanged();
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        VolleyError error = exception.getVolleyError();
                        if (error != null) error.printStackTrace();
                        setStatus(STATUS_ERROR);
                    }
                }, IntegralResult.class);
    }

    private void putIfNotNull(Map<String, String> map, String key, String val) {
        if (val != null) {
            map.put(key, val);
        }
    }

    /**
     * 处理数据
     *
     * @param groupList
     */
    private void handleData(List<RankingGroup> groupList) {
        if (CollectionUtils.notEmpty(groupList)) {
            for (RankingGroup group : groupList) {
                if (mResult.getRankingType() != RankingResult.SINGLE_LEAGUE) {
                    mSections.add(new BasketballDatabaseRankingAdapter.Section(
                            true, group.getGroupName()));
                }
                mSections.add(new BasketballDatabaseRankingAdapter.Section(null));
                if (CollectionUtils.notEmpty(group.getDatas())) {
                    for (RankingTeam rankingTeam : group.getDatas()) {
                        mSections.add(
                                new BasketballDatabaseRankingAdapter.Section(rankingTeam));
                    }
                }
            }
        } else {
            setStatus(STATUS_NO_DATA);
        }
    }

    /**
     * 处理 HeadView 的显示
     *
     * @param matchStages
     * @param firstStageIndex
     */
    private void handleHeadView(List<MatchStage> matchStages, Integer firstStageIndex) {
        if (CollectionUtils.notEmpty(matchStages)) {
            if (firstStageIndex != null) {
                mTitleTextView.setText(matchStages.get(
                        firstStageIndex).getStageName());
            }
            // 杯赛且大于1才显示
            if (RankingResult.CUP == mResult.getRankingType()
                    && matchStages.size() > 1 && mAdapter.getHeaderViewsCount() == 0) {
                mAdapter.addHeaderView(mButtonFrame);
                mAdapter.setEmptyView(true, mEmptyView);
            }

            if (firstStageIndex != null) {
                mLeftButton.setVisibility(firstStageIndex == 0 ? View.GONE : View.VISIBLE);
                mRightButton.setVisibility(
                        firstStageIndex + 1 == mResult.getSearchCondition().size() ?
                                View.GONE : View.VISIBLE);
            }
        }
    }

    public static FootballDatabaseIntegralFragment newInstance(DataBaseBean league, String season) {

        Bundle args = new Bundle();
        args.putParcelable(LEAGUE, league);
        args.putString(PARAM_SEASON, season);
        FootballDatabaseIntegralFragment fragment = new FootballDatabaseIntegralFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setSeason(String season) {
        this.season = season;
        Bundle args = getArguments();
        if (args != null) args.putString(PARAM_SEASON, season);
    }
}
