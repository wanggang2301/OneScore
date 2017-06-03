package com.hhly.mlottery.frame.footballteaminfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.football.teaminfoadapter.FootTeamInfoScoreAdapter;
import com.hhly.mlottery.bean.footballteaminfo.FootTeamHistoryMatchBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.FootBallDetailTypeEnum;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.HandMatchId;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * desc:足球球队赛程赛果比分
 * Created by 107_tangrr on 2017/4/24 0021.
 */

public class TeamInfoScoreFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TEAM_ID = "teamId";
    private static final String LEAGUE_DATE = "leagueDate";
    private final int LOADING = 0;
    private final int SUCCESS = 1;
    private final int ERROR = 2;
    private final int NOTO_DATA = 3;

    private Activity mContext;
    private List<FootTeamHistoryMatchBean.MatchListBean> matchList = new ArrayList<>();
    private View mView;
    private RecyclerView recyclerView;
    private FootTeamInfoScoreAdapter mAdapter;
    private LinearLayout networkExceptionLayout;
    private TextView networkExceptionReloadBtn;
    private TextView tvNotData;
    private ExactSwipeRefreshLayout refreshLayout;

    private String mTeamId;
    private String leagueDate;

    public static TeamInfoScoreFragment newInstance(String thirdId) {
        TeamInfoScoreFragment fragment = new TeamInfoScoreFragment();
        Bundle args = new Bundle();
        args.putString(TEAM_ID, thirdId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeamId = getArguments().getString(TEAM_ID);
            leagueDate = getArguments().getString(LEAGUE_DATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_football_team_info_list, container, false);

        initView();
        return mView;
    }

    // 加载数据
    private void initData() {
        if (leagueDate == null) {
            setStatus(ERROR);
            return;
        }
        setStatus(LOADING);

        final Map<String, String> map = new HashMap<>();
        map.put("teamId", mTeamId);// 球队ID
        map.put("leagueDate", leagueDate);// 日期

        VolleyContentFast.requestJsonByGet(BaseURLs.FOOT_TEAM_HISTORY_URL, map, new VolleyContentFast.ResponseSuccessListener<FootTeamHistoryMatchBean>() {
            @Override
            public void onResponse(FootTeamHistoryMatchBean json) {
                if (json != null && json.getCode() == 200) {
                    matchList.clear();
                    if (json.getMatchList() != null && json.getMatchList().size() != 0) {
                        setStatus(SUCCESS);
                        matchList.addAll(json.getMatchList());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        setStatus(NOTO_DATA);
                    }
                } else {
                    setStatus(NOTO_DATA);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                setStatus(ERROR);
            }
        }, FootTeamHistoryMatchBean.class);
    }

    private void initView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.team_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new FootTeamInfoScoreAdapter(R.layout.football_team_info_score_item, matchList);
        recyclerView.setAdapter(mAdapter);

        networkExceptionLayout = (LinearLayout) mView.findViewById(R.id.network_exception_layout);
        networkExceptionReloadBtn = (TextView) mView.findViewById(R.id.network_exception_reload_btn);
        networkExceptionReloadBtn.setOnClickListener(this);

        tvNotData = (TextView) mView.findViewById(R.id.tv_not_data);

        refreshLayout = (ExactSwipeRefreshLayout) mView.findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.bg_header);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));

        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {

                if (HandMatchId.handId(mContext, String.valueOf(matchList.get(i).getMatchId()))) {
                    Intent intent = new Intent(mContext, FootballMatchDetailActivity.class);
                    intent.putExtra("thirdId", String.valueOf(matchList.get(i).getMatchId()));
                    intent.putExtra("currentFragmentId", 0);
                    intent.putExtra(FootBallDetailTypeEnum.CURRENT_TAB_KEY, FootBallDetailTypeEnum.FOOT_DETAIL_LIVE);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public void updataList(String data) {
        leagueDate = data;
        initData();
    }

    // 设置页面显示状态
    private void setStatus(int status) {
        refreshLayout.setRefreshing(status == LOADING);
        recyclerView.setVisibility(status == SUCCESS ? View.VISIBLE : View.GONE);
        networkExceptionLayout.setVisibility(status == ERROR ? View.VISIBLE : View.GONE);
        tvNotData.setVisibility(status == NOTO_DATA ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.network_exception_reload_btn:
                recyclerView.setVisibility(View.VISIBLE);
                networkExceptionLayout.setVisibility(View.GONE);
                tvNotData.setVisibility(View.GONE);
                initData();
                break;
        }
    }

    @Override
    public void onRefresh() {
        initData();
    }
}
