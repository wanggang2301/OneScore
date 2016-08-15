package com.hhly.mlottery.frame.basketballframe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketballDatabaseRankingAdapter;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchStage;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingGroup;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingResult;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingTeam;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描    述：篮球资料库 - 排行
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */
public class BasketDatabaseRankingFragment extends Fragment {

    private static final String PARAM_ID = "leagueId";
    private static final String PARAM_SEASON = "season";
    private static final String PARAM_MATCH_TYPE = "matchType";
    private static final String PARAM_FIRST_STAGE_ID = "firstStageId";

    View mButtonFrame;
    TextView mTitleTextView;
    ImageView mLeftButton;
    ImageView mRightButton;

    RecyclerView mRecyclerView;

    private String leagueId;
    private String season;
    private String matchType;

    private RankingResult mResult;

    private List<BasketballDatabaseRankingAdapter.Section> mSections;
    private BasketballDatabaseRankingAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            leagueId = args.getString(PARAM_ID);
            season = args.getString(PARAM_SEASON);
            matchType = args.getString(PARAM_MATCH_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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

        initListener();

        initRecycler();

        load(null);
    }

    public void update() {
        load(null);
    }

    private void initListener() {
        mTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(manager);
    }

    private void load(String firstStageId) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_ID, leagueId);
        if (season != null) {
            params.put(PARAM_SEASON, season);
        }
        if (firstStageId != null) {
            params.put(PARAM_FIRST_STAGE_ID, firstStageId);
        }
//        params.put(PARAM_MATCH_TYPE, matchType);
        params.put(PARAM_MATCH_TYPE, "2");
        // http://192.168.31.72:3000/basketball/ranking
        // http://192.168.31.115:8888/mlottery/core/basketballData.findRanking.do?lang=zh&leagueId=7&season=2014-2015&matchType=2
        VolleyContentFast.requestJsonByGet("http://192.168.31.115:8888/mlottery/core/basketballData.findRanking.do",
                params,
                new VolleyContentFast.ResponseSuccessListener<RankingResult>() {
                    @Override
                    public void onResponse(RankingResult result) {
                        mResult = result;
                        Integer firstStageIndex = result.getFirstStageIndex();
                        mAdapter.setType(result.getRankingType());
                        handleHeadView(result.getSearchCondition(), firstStageIndex);
                        handleData(result.getRankingObj());
                        mAdapter.notifyDataSetChanged();
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        VolleyError error = exception.getVolleyError();
                        if (error != null) error.printStackTrace();
                    }
                }, RankingResult.class);
    }

    private void handleData(List<RankingGroup> groupList) {
        mSections.clear();
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
            }

            if (firstStageIndex != null) {
                mLeftButton.setVisibility(firstStageIndex == 0 ? View.GONE : View.VISIBLE);
                mRightButton.setVisibility(
                        firstStageIndex + 1 == mResult.getSearchCondition().size() ?
                                View.GONE : View.VISIBLE);
            }
        }
    }

    public static BasketDatabaseRankingFragment newInstance(String leagueId, String season) {

        Bundle args = new Bundle();
        args.putString(PARAM_ID, leagueId);
        args.putString(PARAM_SEASON, season);
        BasketDatabaseRankingFragment fragment = new BasketDatabaseRankingFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
