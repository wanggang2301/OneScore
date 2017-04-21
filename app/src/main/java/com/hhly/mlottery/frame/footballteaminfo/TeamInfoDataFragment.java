package com.hhly.mlottery.frame.footballteaminfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballTeamInfoActivity;
import com.hhly.mlottery.adapter.football.teaminfoadapter.FootTeamInfoDataAdapter;
import com.hhly.mlottery.bean.footballteaminfo.FootTeamInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:足球球队数据
 * Created by 107_tangrr on 2017/4/21 0021.
 */

public class TeamInfoDataFragment extends Fragment implements View.OnClickListener {
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;
    private static final int NOTO_DATA = 3;

    private Activity mContext;
    private List<FootTeamInfoBean> teamInfoList = new ArrayList<>();
    private View mView;
    private RecyclerView recyclerView;
    private FootTeamInfoDataAdapter mAdapter;
    private LinearLayout networkExceptionLayout;
    private TextView networkExceptionReloadBtn;
    private TextView tvNotData;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_football_team_info_list, container, false);

        initView();
        return mView;
    }

    private void initView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.team_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new FootTeamInfoDataAdapter(R.layout.football_team_info_data_item, teamInfoList);
        recyclerView.setAdapter(mAdapter);

        networkExceptionLayout = (LinearLayout) mView.findViewById(R.id.network_exception_layout);
        networkExceptionReloadBtn = (TextView) mView.findViewById(R.id.network_exception_reload_btn);
        networkExceptionReloadBtn.setOnClickListener(this);

        tvNotData = (TextView) mView.findViewById(R.id.tv_not_data);
    }

    public void updataList(List<FootTeamInfoBean> list) {
        if (list == null) {
            setStatus(ERROR);
        } else {
            if (list.size() == 0) {
                setStatus(NOTO_DATA);
            } else {
                setStatus(SUCCESS);
                teamInfoList.clear();
                teamInfoList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    // 设置页面显示状态
    private void setStatus(int status) {
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
                ((FootballTeamInfoActivity)mContext).initData();
                break;
        }
    }
}
