package com.hhly.mlottery.frame.basketballframe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketballDatabaseRankingAdapter;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchStage;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingGroup;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingResult;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：篮球资料库 - 排行
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */
public class BasketDatabaseRankingFragment extends Fragment {

    TextView mTitleTextView;
    RecyclerView mRecyclerView;

    private List<BasketballDatabaseRankingAdapter.Section> mSections;
    private BasketballDatabaseRankingAdapter mAdapter;

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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        initRecycler();

        load();
    }

    private void initRecycler() {
        mSections = new ArrayList<>();
        mAdapter = new BasketballDatabaseRankingAdapter(RankingResult.CUP, mSections);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(manager);
    }

    private void load() {
        // http://192.168.31.72:3000/basketball/ranking
        VolleyContentFast.requestJsonByGet("http://192.168.31.72:3000/basketball/ranking",
                new VolleyContentFast.ResponseSuccessListener<RankingResult>() {
                    @Override
                    public void onResponse(RankingResult result) {
                        List<RankingGroup> groupList = result.getRankingObj();
                        List<MatchStage> matchStages = result.getSearchCondition();
                        if (CollectionUtils.notEmpty(matchStages)) {
                            mTitleTextView.setText(matchStages.get(result.getFirstStageId()).getStageName());
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

    public static BasketDatabaseRankingFragment newInstance() {

        Bundle args = new Bundle();

        BasketDatabaseRankingFragment fragment = new BasketDatabaseRankingFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
