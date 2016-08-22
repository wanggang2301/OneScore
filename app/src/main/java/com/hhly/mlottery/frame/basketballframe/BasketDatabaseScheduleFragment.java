package com.hhly.mlottery.frame.basketballframe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketballDatabaseScheduleSectionAdapter;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchDay;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchStage;
import com.hhly.mlottery.bean.basket.basketdatabase.ScheduleResult;
import com.hhly.mlottery.bean.basket.basketdatabase.ScheduledMatch;
import com.hhly.mlottery.bean.basket.infomation.LeagueBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.LocaleFactory;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描    述：篮球资料库赛程
 * 作    者：longs@13322.com
 * 时    间：2016/8/3
 */
public class BasketDatabaseScheduleFragment extends Fragment {

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
    private String season;

    private ScheduleResult mResult;
    private List<BasketballDatabaseScheduleSectionAdapter.Section> mSections;
    private List<MatchStage> mStageList;
    private BasketballDatabaseScheduleSectionAdapter mAdapter;

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

        initListener();

        loadData(null, null);
    }

    private void initEmptyView() {
        mProgressBar = (ProgressBar) mEmptyView.findViewById(R.id.progress);
        mErrorLayout = mEmptyView.findViewById(R.id.error_layout);
        mRefreshTextView = (TextView) mEmptyView.findViewById(R.id.reloading_txt);
        mNoDataTextView = (TextView) mEmptyView.findViewById(R.id.no_data_txt);

        mRefreshTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(null, null);
            }
        });
    }

    private void initListener() {
        mTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseQuickAdapter.OnRecyclerViewItemClickListener itemClickListener =
                        new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int i) {
                                loadData(mResult.getSearchCondition().get(i).getStageId(), null);
                            }
                        };
                LeagueMatchStageChooseDialogFragment.OnChooseOkListener chooseOkListener =
                        new LeagueMatchStageChooseDialogFragment.OnChooseOkListener() {
                            @Override
                            public void onChooseOk(String firstStageId, String secondStageId) {
                                loadData(firstStageId, secondStageId);
                            }
                        };

                if (mResult != null) {
                    DialogFragment dialog = null;
                    if (mResult.getMatchType() == MATCH_TYPE_CUP) {
                        CupMatchStageChooseDialogFragment cupDialog =
                                CupMatchStageChooseDialogFragment.newInstance(mResult.getStageResult());
                        cupDialog.setOnRecyclerViewItemClickListener(itemClickListener);
                        dialog = cupDialog;
                    } else if (mResult.getMatchType() == MATCH_TYPE_LEAGUE) {
                        LeagueMatchStageChooseDialogFragment leagueDialog =
                                LeagueMatchStageChooseDialogFragment.newInstance(mResult);
                        leagueDialog.setOnChooseOkListener(chooseOkListener);
                        dialog = leagueDialog;
                    }
                    if (dialog != null) {
                        dialog.show(getChildFragmentManager(), "stageChoose");
                    }
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
                    loadData(matchStages.get(mResult.getFirstStageIndex() - 1).getStageId(), null);
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
                    loadData(matchStages.get(mResult.getFirstStageIndex() + 1).getStageId(), null);
                }
            }
        });
    }

    /**
     * 刷新数据
     */
    public void update() {
        loadData(null, null);
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

    private void loadData(String firstStageId, String secondStageId) {
        mSections.clear();
        mAdapter.notifyDataSetChanged();
        setStatus(STATUS_LOADING);
        Map<String, String> params = produceParams(firstStageId, secondStageId);

        // http://192.168.31.115:8888/mlottery/core/basketballData.findSchedule.do?lang=zh&leagueId=1&season=2015-2016
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_BASKET_DATABASE_SCHEDULE, params,
                new VolleyContentFast.ResponseSuccessListener<ScheduleResult>() {
                    @Override
                    public void onResponse(ScheduleResult result) {
                        if (result == null) {
                            setStatus(STATUS_NO_DATA);
                            mButtonFrame.setVisibility(View.GONE);
                            return;
                        }
                        mButtonFrame.setVisibility(View.VISIBLE);
                        mResult = result;
                        handleData(result.getMatchData());
                        handleHeadView(result.getSearchCondition(), result.getFirstStageIndex());
                        mAdapter.notifyDataSetChanged();
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException e) {
                        VolleyError error = e.getVolleyError();
                        if (error != null) error.printStackTrace();
                        setStatus(STATUS_ERROR);
                    }
                }, ScheduleResult.class);
    }

    private Map<String, String> produceParams(String firstStageId, String secondStageId) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_ID, league.getLeagueId());
        putIfNotNull(params, PARAM_SEASON, season);
        putIfNotNull(params, PARAM_FIRST_STAGE_ID, firstStageId);
        putIfNotNull(params, PARAM_SECOND_STAGE_ID, secondStageId);
        params.put(PARAM_MATCH_TYPE, league.getMatchType().toString());
        return params;
    }

    private void putIfNotNull(Map<String, String> map, String key, String val) {
        if (val != null) {
            map.put(key, val);
        }
    }

    private void handleHeadView(List<MatchStage> searchCondition, Integer firstStageIndex) {
        if (CollectionUtils.notEmpty(searchCondition)) {
            mStageList.clear();
            mStageList.addAll(searchCondition);
            MatchStage matchStage = mStageList.get(mResult.getFirstStageIndex());
            mTitleTextView.setText(matchStage.getStageName());
        }

        if (firstStageIndex != null) {
            mLeftButton.setVisibility(firstStageIndex == 0 ? View.GONE : View.VISIBLE);
            mRightButton.setVisibility(
                    firstStageIndex + 1 == mResult.getSearchCondition().size() ?
                            View.GONE : View.VISIBLE);
        }
    }

    private void handleData(List<MatchDay> matchData) {
        if (CollectionUtils.notEmpty(matchData)) {
            for (MatchDay matchDay : matchData) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd E", LocaleFactory.get());
                mSections.add(new BasketballDatabaseScheduleSectionAdapter
                        .Section(true, dateFormat.format(matchDay.getDay())));
                for (ScheduledMatch match : matchDay.getDatas()) {
                    mSections.add(new BasketballDatabaseScheduleSectionAdapter
                            .Section(match));
                }
            }
        } else {
            setStatus(STATUS_NO_DATA);
            mButtonFrame.setVisibility(View.GONE);
        }
    }

    private void initRecycler() {
        mSections = new ArrayList<>();
        mAdapter = new BasketballDatabaseScheduleSectionAdapter(mSections);
        mAdapter.addHeaderView(mButtonFrame);
        mAdapter.setEmptyView(true, mEmptyView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(manager);
    }

    public static BasketDatabaseScheduleFragment newInstance(LeagueBean leagueBean, String season) {

        Bundle args = new Bundle();
        args.putParcelable(LEAGUE, leagueBean);
        args.putString(PARAM_SEASON, season);
        BasketDatabaseScheduleFragment fragment = new BasketDatabaseScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setSeason(String season) {
        this.season = season;
        Bundle args = getArguments();
        if (args != null) args.putString(PARAM_SEASON, season);
    }
}