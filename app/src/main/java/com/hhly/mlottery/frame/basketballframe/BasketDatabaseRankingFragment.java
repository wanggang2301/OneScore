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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketballDatabaseRankingAdapter;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchStage;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingGroup;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingResult;
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

    TextView mTitleTextView;
    ImageView mLeftButton;
    ImageView mRightButton;
    RecyclerView mRecyclerView;
    FrameLayout mButtonFrame;

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
        mTitleTextView = (TextView) view.findViewById(R.id.title_button);
        mLeftButton = (ImageView) view.findViewById(R.id.left_button);
        mRightButton = (ImageView) view.findViewById(R.id.right_button);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mButtonFrame = (FrameLayout) view.findViewById(R.id.button_frame);

        mTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mResult != null) {
                    DialogFragment dialog =
                            CupMatchStageChooseDialogFragment.newInstance(mResult.getStageResult());
                    if (dialog != null) {
                        dialog.show(getChildFragmentManager(), "stageChoose");
                    }
                } else {
                    ToastTools.showQuick(getContext(), "稍候，获取数据中");
                }
            }
        });

        initRecycler();

        load();
    }

    private void initRecycler() {
        mSections = new ArrayList<>();
        mAdapter = new BasketballDatabaseRankingAdapter(mSections);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(manager);
    }

    private void load() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_ID, leagueId);
        if (season != null) {
            params.put(PARAM_SEASON, season);
        }
        params.put(PARAM_MATCH_TYPE, matchType);
        // http://192.168.31.72:3000/basketball/ranking
        // http://192.168.31.115:8888/mlottery/core/basketballData.findRanking.do?lang=zh&leagueId=7&season=2014-2015&matchType=2
        VolleyContentFast.requestJsonByGet("http://192.168.31.115:8888/mlottery/core/basketballData.findRanking.do",
                params,
                new VolleyContentFast.ResponseSuccessListener<RankingResult>() {
                    @Override
                    public void onResponse(RankingResult result) {
                        mResult = result;
                        mAdapter.setType(result.getRankingType());
                        List<RankingGroup> groupList = result.getRankingObj();
                        List<MatchStage> matchStages = result.getSearchCondition();
                        if (CollectionUtils.notEmpty(matchStages)) {
                            mTitleTextView.setText(matchStages.get(result.getFirstStageIndex()).getStageName());
                            mButtonFrame.setVisibility(matchStages.size() > 1 ? View.VISIBLE : View.GONE);
                        }
                        mSections.clear();
                        if (CollectionUtils.notEmpty(groupList)) {
                            for (RankingGroup group : groupList) {
                                mSections.add(new BasketballDatabaseRankingAdapter.Section(
                                        true, group.getGroupName()));
                                mSections.add(new BasketballDatabaseRankingAdapter.Section(group));
                            }
                        }
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

    public static BasketDatabaseRankingFragment newInstance(String leagueId, String season) {

        Bundle args = new Bundle();
        args.putString(PARAM_ID, leagueId);
        args.putString(PARAM_SEASON, season);
        BasketDatabaseRankingFragment fragment = new BasketDatabaseRankingFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
