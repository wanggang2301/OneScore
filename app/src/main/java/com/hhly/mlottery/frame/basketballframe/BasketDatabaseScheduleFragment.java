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
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketballDatabaseScheduleSectionAdapter;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchDay;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchStage;
import com.hhly.mlottery.bean.basket.basketdatabase.ScheduleResult;
import com.hhly.mlottery.bean.basket.basketdatabase.ScheduledMatch;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 描    述：篮球资料库赛程
 * 作    者：longs@13322.com
 * 时    间：2016/8/3
 */
public class BasketDatabaseScheduleFragment extends Fragment {

    private static final int MATCH_TYPE_LEAGUE = 1; // 联赛
    private static final int MATCH_TYPE_CUP = 2; // 杯赛

    View mButtonFrame;
    TextView mTitleTextView;
    ImageView mLeftButton;
    ImageView mRightButton;

    RecyclerView mRecyclerView;

    private ScheduleResult mResult;
    private List<BasketballDatabaseScheduleSectionAdapter.Section> mSections;
    private List<MatchStage> mStageList;
    private BasketballDatabaseScheduleSectionAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket_database_schedule, container, false);
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

        mStageList = new ArrayList<>();

        initRecycler();

        initListener();

        loadData();
    }

    private void initListener() {
        mTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mResult != null) {
                    DialogFragment dialog = null;
                    if (mResult.getMatchType() == MATCH_TYPE_CUP) {
                        dialog = CupMatchStageChooseDialogFragment.newInstance(mResult.getStageResult());
                    } else if (mResult.getMatchType() == MATCH_TYPE_LEAGUE) {
                        dialog = LeagueMatchStageChooseDialogFragment.newInstance(mResult);
                    }
                    if (dialog != null) {
                        dialog.show(getChildFragmentManager(), "stageChoose");
                    }
                } else {
                    ToastTools.showQuick(getContext(), "稍候，获取数据中");
                }
            }
        });
    }

    /**
     * 刷新数据
     */
    public void update() {
        loadData();
    }

    private void loadData() {
        // http://192.168.31.115:8888/mlottery/core/basketballData.findSchedule.do?lang=zh&leagueId=1&season=2015-2016
        VolleyContentFast.requestJsonByGet("http://192.168.31.115:8888/mlottery/core/basketballData.findSchedule.do?lang=zh&leagueId=2&matchType=1&firstStageId=1&secondStageId=2015-04",
                new VolleyContentFast.ResponseSuccessListener<ScheduleResult>() {
                    @Override
                    public void onResponse(ScheduleResult result) {
                        mResult = result;
                        List<MatchDay> matchData = result.getMatchData();
                        if (CollectionUtils.notEmpty(matchData)) {
                            mSections.clear();
                            for (MatchDay matchDay : matchData) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd E", Locale.CHINA);
                                mSections.add(new BasketballDatabaseScheduleSectionAdapter
                                        .Section(true, dateFormat.format(matchDay.getDay())));
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
        mAdapter.addHeaderView(mButtonFrame);
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
