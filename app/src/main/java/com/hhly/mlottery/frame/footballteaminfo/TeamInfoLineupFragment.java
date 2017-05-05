package com.hhly.mlottery.frame.footballteaminfo;

import android.app.Activity;
import android.content.Context;
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

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.teaminfoadapter.FootTeamInfoLineupAdapter;
import com.hhly.mlottery.bean.footballteaminfo.FootTeamInfoListBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * desc:足球球队阵容
 * Created by 107_tangrr on 2017/4/24 0021.
 */

public class TeamInfoLineupFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TEAM_ID = "teamId";
    private static final String LEAGUE_DATE = "leagueDate";
    private final int LOADING = 0;
    private final int SUCCESS = 1;
    private final int ERROR = 2;
    private final int NOTO_DATA = 3;

    private Activity mContext;
    private View mView;
    private RecyclerView recyclerView;
    private FootTeamInfoLineupAdapter mAdapter;
    private LinearLayout networkExceptionLayout;
    private TextView networkExceptionReloadBtn;
    private TextView tvNotData;
    private LinearLayout ll_content;
    private ExactSwipeRefreshLayout refreshLayout;

    private String mTeamId;
    private String leagueDate;
    private List<FootTeamInfoListBean.PlayerBean> playerList = new ArrayList<>();

    public static TeamInfoLineupFragment newInstance(String thirdId) {
        TeamInfoLineupFragment fragment = new TeamInfoLineupFragment();
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
        mView = inflater.inflate(R.layout.fragment_football_team_info_lineup, container, false);

        initView();
        return mView;
    }

    private void initData() {
        if (leagueDate == null) {
            setStatus(ERROR);
            return;
        }
        setStatus(LOADING);

        final Map<String, String> map = new HashMap<>();
        map.put("teamId", mTeamId);// 球队ID
        map.put("leagueDate", leagueDate);// 日期

        VolleyContentFast.requestJsonByGet(BaseURLs.FOOT_TEAM_LIST_URL, map, new VolleyContentFast.ResponseSuccessListener<FootTeamInfoListBean>() {
            @Override
            public void onResponse(FootTeamInfoListBean json) {
                if (json != null && json.getCode() == 200) {
                    playerList.clear();
                    if (json.getPlayer() != null && json.getPlayer().size() != 0) {
                        setStatus(SUCCESS);
                        playerList.addAll(json.getPlayer());
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
        }, FootTeamInfoListBean.class);
    }

    private void initView() {
        ll_content = (LinearLayout) mView.findViewById(R.id.ll_content);
        recyclerView = (RecyclerView) mView.findViewById(R.id.team_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new FootTeamInfoLineupAdapter(R.layout.football_team_info_lineup_item, playerList);
        recyclerView.setAdapter(mAdapter);

        networkExceptionLayout = (LinearLayout) mView.findViewById(R.id.network_exception_layout);
        networkExceptionReloadBtn = (TextView) mView.findViewById(R.id.network_exception_reload_btn);
        networkExceptionReloadBtn.setOnClickListener(this);

        tvNotData = (TextView) mView.findViewById(R.id.tv_not_data);

        refreshLayout = (ExactSwipeRefreshLayout) mView.findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.bg_header);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));
    }

    public void updataList(String data) {
        leagueDate = data;
        initData();
    }

    // 设置页面显示状态
    private void setStatus(int status) {
        refreshLayout.setRefreshing(status == LOADING);
        ll_content.setVisibility(status == SUCCESS ? View.VISIBLE : View.GONE);
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
