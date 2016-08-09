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
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketballDatabaseScheduleSectionAdapter;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchDay;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchStage;
import com.hhly.mlottery.bean.basket.basketdatabase.ScheduleResult;
import com.hhly.mlottery.bean.basket.basketdatabase.ScheduledMatch;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：篮球资料库赛程
 * 作    者：longs@13322.com
 * 时    间：2016/8/3
 */

public class BasketDatabaseScheduleFragment extends Fragment {

    private static final int MATCH_TYPE_LEAGUE = 1; // 联赛
    private static final int MATCH_TYPE_CUP = 2; // 杯赛

    TextView mTitleTextView;
    ImageView mLeftButton;
    ImageView mRightButton;
    RecyclerView mRecyclerView;

    private List<BasketballDatabaseScheduleSectionAdapter.Section> mSections;
    private List<MatchStage> mStageList;
    private BasketballDatabaseScheduleSectionAdapter mAdapter;

    private CupMatchStageChooseDialogFragment mCupDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket_database_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleTextView = (TextView) view.findViewById(R.id.title_button);
        mLeftButton = (ImageView) view.findViewById(R.id.left_button);
        mRightButton = (ImageView) view.findViewById(R.id.right_button);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        initRecycler();

        mStageList = new ArrayList<>();

        mTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CupMatchStageChooseDialogFragment dialog =
                        CupMatchStageChooseDialogFragment.newInstance(new ArrayList<>(mStageList));
                dialog.show(getChildFragmentManager(), "stageChoose");
            }
        });

        loadData();
    }

    /**
     * 刷新数据
     */
    public void update() {
        loadData();
    }

    private void loadData() {
        // http://192.168.31.115:8888/mlottery/core/basketballData.findSchedule.do?lang=zh&leagueId=1&season=2015-2016
        VolleyContentFast.requestJsonByGet("http://192.168.31.115:8888/mlottery/core/basketballData.findSchedule.do?lang=zh&leagueId=1&season=2015-2016",
                new VolleyContentFast.ResponseSuccessListener<ScheduleResult>() {
                    @Override
                    public void onResponse(ScheduleResult result) {
                        int type = result.getMatchType();
                        List<MatchDay> matchData = result.getMatchData();
                        if (CollectionUtils.notEmpty(matchData)) {
                            mSections.clear();
                            for (MatchDay matchDay : matchData) {
                                mSections.add(new BasketballDatabaseScheduleSectionAdapter
                                        .Section(true, matchDay.getDay()));
                                for (ScheduledMatch match : matchDay.getDatas()) {
                                    mSections.add(new BasketballDatabaseScheduleSectionAdapter
                                            .Section(match));
                                }
                            }
                        }
                        List<MatchStage> searchCondition = result.getSearchCondition();
                        if (CollectionUtils.notEmpty(searchCondition)) {
                            mStageList.clear();
                            mStageList.addAll(searchCondition);
                            MatchStage matchStage = mStageList.get(result.getFirstStageIndex());
                            mTitleTextView.setText(matchStage.getStageName());
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException e) {
                        VolleyError error = e.getVolleyError();
                        if (error != null) error.printStackTrace();
                    }
                }, ScheduleResult.class);
    }

    private void initRecycler() {
        mSections = new ArrayList<>();
        mAdapter = new BasketballDatabaseScheduleSectionAdapter(mSections);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(manager);
    }

    public static BasketDatabaseScheduleFragment newInstance() {

        Bundle args = new Bundle();

        BasketDatabaseScheduleFragment fragment = new BasketDatabaseScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
