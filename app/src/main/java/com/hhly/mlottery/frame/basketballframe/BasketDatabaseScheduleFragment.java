package com.hhly.mlottery.frame.basketballframe;

import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.adapter.basketball.BasketballDatabaseScheduleSectionAdapter;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchDay;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchStage;
import com.hhly.mlottery.bean.basket.basketdatabase.ScheduleResult;
import com.hhly.mlottery.bean.basket.basketdatabase.ScheduledMatch;
import com.hhly.mlottery.bean.basket.infomation.LeagueBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.LocaleFactory;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.jakewharton.rxbinding.view.RxView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

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

        setDetailsOnClick();
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

        final BaseQuickAdapter.OnRecyclerViewItemClickListener itemClickListener =
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        loadData(mResult.getSearchCondition().get(i).getStageId(), null);
                    }
                };
        final LeagueMatchStageChooseDialogFragment.OnChooseOkListener chooseOkListener =
                new LeagueMatchStageChooseDialogFragment.OnChooseOkListener() {
                    @Override
                    public void onChooseOk(String firstStageId, String secondStageId) {
                        loadData(firstStageId, secondStageId);
                    }
                };

        // 防止重复点击
        RxView.clicks(mTitleTextView)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
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
                            ToastTools.showQuick(getContext(), getResources().getString(R.string.basket_database_details_toast));
                        }
                    }
                });

        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPre();
            }
        });

        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNext();
            }
        });
    }

    /**
     * 加载下一页
     */
    private void loadNext() {
        if (mResult == null) return;
        Integer firstStageIndex = mResult.getFirstStageIndex();
        if (mResult.getFirstStageIndex() == null) return;
        List<MatchStage> matchStages = mResult.getSearchCondition();
        if (!CollectionUtils.notEmpty(matchStages)) return;
        Integer secondStageIndex = mResult.getSecondStageIndex();
        if (firstStageIndex < matchStages.size()) {
            MatchStage firstStage = matchStages.get(firstStageIndex);
            if (firstStage.isHasSecondStage() && secondStageIndex != null) {
                int nextIndex = secondStageIndex + 1;
                MatchStage secondStage = matchStages.get(firstStageIndex);
                if (nextIndex == secondStage.getStages().size()) {
                    MatchStage nextFirstStage = matchStages.get(firstStageIndex + 1);
                    List<MatchStage> nextSecondStages = nextFirstStage.getStages();
                    if (!nextFirstStage.isHasSecondStage() ||
                            nextSecondStages.get(0) == null) {
                        loadData(nextFirstStage.getStageId(), null);
                    } else {
                        loadData(nextFirstStage.getStageId(),
                                nextSecondStages.get(0).getStageId());
                    }
                } else {
                    loadData(firstStage.getStageId(),
                            firstStage.getStages().get(nextIndex).getStageId());
                }
            } else {
                MatchStage nextFirstStage = matchStages.get(firstStageIndex + 1);
                List<MatchStage> nextSecondStages = nextFirstStage.getStages();
                if (CollectionUtils.notEmpty(nextSecondStages)) {
                    loadData(nextFirstStage.getStageId(), nextSecondStages.get(0).getStageId());
                } else {
                    loadData(nextFirstStage.getStageId(), null);
                }
            }
        }
    }

    /**
     * 加载上一页
     */
    private void loadPre() {
        if (mResult == null) return;
        Integer firstStageIndex = mResult.getFirstStageIndex();
        if (firstStageIndex == null) return;
        List<MatchStage> matchStages = mResult.getSearchCondition();
        if (!CollectionUtils.notEmpty(matchStages)) return;
        if (firstStageIndex >= 0) {
            MatchStage firstStage = matchStages.get(firstStageIndex);
            Integer secondStageIndex = mResult.getSecondStageIndex();
            if (firstStage.isHasSecondStage() && secondStageIndex != null) {
                int nextIndex = secondStageIndex - 1;
                if (nextIndex == -1) {
                    MatchStage preFirstStage = matchStages.get(firstStageIndex - 1);
                    List<MatchStage> preSecondStages = preFirstStage.getStages();
                    if (!preFirstStage.isHasSecondStage() ||
                            preSecondStages.get(preSecondStages.size() - 1) == null) {
                        loadData(preFirstStage.getStageId(), null);
                    } else {
                        loadData(preFirstStage.getStageId(),
                                preSecondStages.get(preSecondStages.size() - 1).getStageId());
                    }
                } else {
                    loadData(firstStage.getStageId(),
                            firstStage.getStages().get(nextIndex).getStageId());
                }
            } else {
                MatchStage preFirstStage = matchStages.get(firstStageIndex - 1);
                List<MatchStage> preSecondStages = preFirstStage.getStages();
                if (CollectionUtils.notEmpty(preSecondStages)) {
                    loadData(preFirstStage.getStageId(),
                            preSecondStages.get(preSecondStages.size() - 1).getStageId());
                } else {
                    loadData(preFirstStage.getStageId(), null);
                }
            }
        }
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
        if (2 == (league.getMatchType())) secondStageId = null;
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
                        handleHeadView(result.getSearchCondition(), result.getFirstStageIndex(),
                                result.getSecondStageIndex());
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

    private void handleHeadView(List<MatchStage> searchCondition, Integer firstStageIndex,
                                Integer secondStageIndex) {
        if (CollectionUtils.notEmpty(searchCondition)) {
            mStageList.clear();
            mStageList.addAll(searchCondition);
            MatchStage matchStage = mStageList.get(mResult.getFirstStageIndex());
            if (matchStage.isHasSecondStage() && mResult.getSecondStageIndex() != null &&
                    mResult.getSecondStageIndex() != -1) {
                MatchStage secondStage = matchStage.getStages().get(mResult.getSecondStageIndex());
                if (DateUtil.isValidDateYMD(secondStage.getStageName()) || DateUtil.isValidDateYM(secondStage.getStageName())) {
                    mTitleTextView.setText(DateUtil.convertDateToNationYD(secondStage.getStageName()));
                }else{
                    mTitleTextView.setText(secondStage.getStageName());
                }
            } else {
                if (DateUtil.isValidDateYMD(matchStage.getStageName()) || DateUtil.isValidDateYM(matchStage.getStageName())) {
                    mTitleTextView.setText(DateUtil.convertDateToNationYD(matchStage.getStageName()));
                }else{
                    mTitleTextView.setText(matchStage.getStageName());
                }
            }
        }

        if (firstStageIndex != null) {
            boolean secondIsStart = secondStageIndex == null || secondStageIndex == -1
                    || secondStageIndex == 0;
            mLeftButton.setVisibility(firstStageIndex == 0 && secondIsStart ?
                    View.GONE : View.VISIBLE);
            boolean firstIsEnd = firstStageIndex + 1 == mResult.getSearchCondition().size();

            MatchStage firstStage = searchCondition.get(firstStageIndex);
            boolean secondIsEnd = secondStageIndex == null || firstStage.getStages() == null ||
                    secondStageIndex == firstStage.getStages().size() - 1;
            mRightButton.setVisibility(firstIsEnd && secondIsEnd ?
                    View.GONE : View.VISIBLE);
        }
    }

    private void handleData(List<MatchDay> matchData) {
        if (CollectionUtils.notEmpty(matchData)) {
            for (MatchDay matchDay : matchData) {
                SimpleDateFormat dateFormat;
                if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) { //国内
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd E", LocaleFactory.get());
                } else {
                    dateFormat = new SimpleDateFormat("MM-dd-yyyy E", LocaleFactory.get());
                }
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
        mAdapter.setBasketballDetailsClickListener(basketballDetailsClickListener);
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

    //点击跳转到内页
    private BasketballDetailsClickListener basketballDetailsClickListener;
    public interface BasketballDetailsClickListener {
        void DetailsOnClick(View view, String id);
    }
    private void setDetailsOnClick(){
        basketballDetailsClickListener = new BasketballDetailsClickListener() {
            @Override
            public void DetailsOnClick(View view, String thirdId) {
//                Intent intent = new Intent(getActivity(), BasketDetailsActivityTest.class);
//                intent.putExtra(BasketDetailsActivityTest.BASKET_THIRD_ID, thirdId);//跳转到详情 4830987
//                intent.putExtra(BasketDetailsActivityTest.BASKET_MATCH_LEAGUEID, league.getLeagueId());
//                intent.putExtra(BasketDetailsActivityTest.BASKET_MATCH_MATCHTYPE, league.getMatchType().toString());
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
//                Toast.makeText(getContext(), "aaa " + thirdId, Toast.LENGTH_SHORT).show();
            }
        };
    }
}
