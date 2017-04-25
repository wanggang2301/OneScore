package com.hhly.mlottery.frame.footballteaminfo;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballTeamInfoActivity;
import com.hhly.mlottery.adapter.football.teaminfoadapter.FootTeamArrayAdapter;
import com.hhly.mlottery.bean.footballteaminfo.FootTeamInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:足球球队数据
 * Created by 107_tangrr on 2017/4/21 0021.
 */

public class TeamInfoDataFragment extends Fragment implements View.OnClickListener {
    private final int SUCCESS = 1;
    private final int ERROR = 2;
    private final int NOTO_DATA = 3;

    private Activity mContext;
    private List<FootTeamInfoBean> teamInfoList = new ArrayList<>();
    private View mView;
    private LinearLayout networkExceptionLayout;
    private TextView networkExceptionReloadBtn;
    private TextView tvNotData;
    private NestedScrollView scrollView;
    private TextView dataContent, rankTotal, rankZu, rankKe, scoreTotal, scoreZu, scoreKe, countTotal, countZu, countKe, winTotal, winZu, winKe, equTotal, equZu, equKe, loseTotal, loseZu, loseKe, goalTotal, goalZu, goalKe, loseGoalTotal, loseGoalZu, loseGoalKe, goalAvgTotal, goalAvgZu, goalAvgKe, loseAvgTotal, loseAvgZu, loseAvgKe;
    private int currentNameIndex = 0;
    private RelativeLayout ll_data_select;

    private List<String> mItems = new ArrayList<>();
    private FrameLayout fl_mask_view;
    private ImageView tv_team_select;

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
        mView = inflater.inflate(R.layout.fragment_football_team_info_data, container, false);

        initView();
        initData(0);
        return mView;
    }

    private void initData(int type) {
        if (teamInfoList.size() != 0) {
            if (type == 1) {
                mItems.clear();
                for (FootTeamInfoBean footTeamInfoBean : teamInfoList) {
                    mItems.add(footTeamInfoBean.getLgName());
                }
            }
            FootTeamInfoBean bean = teamInfoList.get(currentNameIndex);
            dataContent.setText(bean.getLgName());
            if (bean.getData() != null) {
                // 排名
                if (bean.getData().getRank() != null) {
                    String[] split = bean.getData().getRank().split(",");
                    if (split.length >= 3) {
                        rankTotal.setText(split[0]);
                        rankZu.setText(split[1]);
                        rankKe.setText(split[2]);
                    } else {
                        rankTotal.setText("-");
                        rankZu.setText("-");
                        rankKe.setText("-");
                    }
                }
                // 积分
                if (bean.getData().getScore() != null) {
                    String[] split = bean.getData().getScore().split(",");
                    if (split.length >= 3) {
                        scoreTotal.setText(split[0]);
                        scoreZu.setText(split[1]);
                        scoreKe.setText(split[2]);
                    } else {
                        scoreTotal.setText("-");
                        scoreZu.setText("-");
                        scoreKe.setText("-");
                    }
                }
                // 比赛场次
                if (bean.getData().getMatchCount() != null) {
                    String[] split = bean.getData().getMatchCount().split(",");
                    if (split.length >= 3) {
                        countTotal.setText(split[0]);
                        countZu.setText(split[1]);
                        countKe.setText(split[2]);
                    } else {
                        countTotal.setText("-");
                        countZu.setText("-");
                        countKe.setText("-");
                    }
                }
                // 胜
                if (bean.getData().getWin() != null) {
                    String[] split = bean.getData().getWin().split(",");
                    if (split.length >= 3) {
                        winTotal.setText(split[0]);
                        winZu.setText(split[1]);
                        winKe.setText(split[2]);
                    } else {
                        winTotal.setText("-");
                        winZu.setText("-");
                        winKe.setText("-");
                    }
                }
                // 平
                if (bean.getData().getEqu() != null) {
                    String[] split = bean.getData().getEqu().split(",");
                    if (split.length >= 3) {
                        equTotal.setText(split[0]);
                        equZu.setText(split[1]);
                        equKe.setText(split[2]);
                    } else {
                        equTotal.setText("-");
                        equZu.setText("-");
                        equKe.setText("-");
                    }
                }
                // 负
                if (bean.getData().getLose() != null) {
                    String[] split = bean.getData().getLose().split(",");
                    if (split.length >= 3) {
                        loseTotal.setText(split[0]);
                        loseZu.setText(split[1]);
                        loseKe.setText(split[2]);
                    } else {
                        loseTotal.setText("-");
                        loseZu.setText("-");
                        loseKe.setText("-");
                    }
                }
                // 进球
                if (bean.getData().getGoal() != null) {
                    String[] split = bean.getData().getGoal().split(",");
                    if (split.length >= 3) {
                        goalTotal.setText(split[0]);
                        goalZu.setText(split[1]);
                        goalKe.setText(split[2]);
                    } else {
                        goalTotal.setText("-");
                        goalZu.setText("-");
                        goalKe.setText("-");
                    }
                }
                // 失球
                if (bean.getData().getLoseGoal() != null) {
                    String[] split = bean.getData().getLoseGoal().split(",");
                    if (split.length >= 3) {
                        loseGoalTotal.setText(split[0]);
                        loseGoalZu.setText(split[1]);
                        loseGoalKe.setText(split[2]);
                    } else {
                        loseGoalTotal.setText("-");
                        loseGoalZu.setText("-");
                        loseGoalKe.setText("-");
                    }
                }
                // 均场进球
                if (bean.getData().getGoalAvg() != null) {
                    String[] split = bean.getData().getGoalAvg().split(",");
                    if (split.length >= 3) {
                        goalAvgTotal.setText(split[0]);
                        goalAvgZu.setText(split[1]);
                        goalAvgKe.setText(split[2]);
                    } else {
                        goalAvgTotal.setText("-");
                        goalAvgZu.setText("-");
                        goalAvgKe.setText("-");
                    }
                }
                // 均场失球
                if (bean.getData().getLoseAvg() != null) {
                    String[] split = bean.getData().getLoseAvg().split(",");
                    if (split.length >= 3) {
                        loseAvgTotal.setText(split[0]);
                        loseAvgZu.setText(split[1]);
                        loseAvgKe.setText(split[2]);
                    } else {
                        loseAvgTotal.setText("-");
                        loseAvgZu.setText("-");
                        loseAvgKe.setText("-");
                    }
                }
            }
        }
    }

    private void initView() {
        fl_mask_view = (FrameLayout) mView.findViewById(R.id.fl_mask_view);
        ll_data_select = (RelativeLayout) mView.findViewById(R.id.ll_data_select);
        scrollView = (NestedScrollView) mView.findViewById(R.id.scroll_view);
        mView.findViewById(R.id.ll_data_select).setOnClickListener(this);
        dataContent = (TextView) mView.findViewById(R.id.tv_data_content);
        tv_team_select = (ImageView) mView.findViewById(R.id.tv_team_select);
        rankTotal = (TextView) mView.findViewById(R.id.tv_data_rank_total);
        rankZu = (TextView) mView.findViewById(R.id.tv_data_rank_zu);
        rankKe = (TextView) mView.findViewById(R.id.tv_data_rank_ke);
        scoreTotal = (TextView) mView.findViewById(R.id.tv_data_score_total);
        scoreZu = (TextView) mView.findViewById(R.id.tv_data_score_zu);
        scoreKe = (TextView) mView.findViewById(R.id.tv_data_score_ke);
        countTotal = (TextView) mView.findViewById(R.id.tv_data_count_total);
        countZu = (TextView) mView.findViewById(R.id.tv_data_count_zu);
        countKe = (TextView) mView.findViewById(R.id.tv_data_count_ke);
        winTotal = (TextView) mView.findViewById(R.id.tv_data_win_total);
        winZu = (TextView) mView.findViewById(R.id.tv_data_win_zu);
        winKe = (TextView) mView.findViewById(R.id.tv_data_win_ke);
        equTotal = (TextView) mView.findViewById(R.id.tv_data_equ_total);
        equZu = (TextView) mView.findViewById(R.id.tv_data_equ_zu);
        equKe = (TextView) mView.findViewById(R.id.tv_data_equ_ke);
        loseTotal = (TextView) mView.findViewById(R.id.tv_data_lose_total);
        loseZu = (TextView) mView.findViewById(R.id.tv_data_lose_zu);
        loseKe = (TextView) mView.findViewById(R.id.tv_data_lose_ke);
        goalTotal = (TextView) mView.findViewById(R.id.tv_data_goal_total);
        goalZu = (TextView) mView.findViewById(R.id.tv_data_goal_zu);
        goalKe = (TextView) mView.findViewById(R.id.tv_data_goal_ke);
        loseGoalTotal = (TextView) mView.findViewById(R.id.tv_data_lose_goal_total);
        loseGoalZu = (TextView) mView.findViewById(R.id.tv_data_lose_goal_zu);
        loseGoalKe = (TextView) mView.findViewById(R.id.tv_data_lose_goal_ke);
        goalAvgTotal = (TextView) mView.findViewById(R.id.tv_data_goal_avg_total);
        goalAvgZu = (TextView) mView.findViewById(R.id.tv_data_goal_avg_zu);
        goalAvgKe = (TextView) mView.findViewById(R.id.tv_data_goal_avg_ke);
        loseAvgTotal = (TextView) mView.findViewById(R.id.tv_data_lose_avg_total);
        loseAvgZu = (TextView) mView.findViewById(R.id.tv_data_lose_avg_zu);
        loseAvgKe = (TextView) mView.findViewById(R.id.tv_data_lose_avg_ke);

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
                initData(1);
            }
        }
    }

    // 设置页面显示状态
    private void setStatus(int status) {
        scrollView.setVisibility(status == SUCCESS ? View.VISIBLE : View.GONE);
        networkExceptionLayout.setVisibility(status == ERROR ? View.VISIBLE : View.GONE);
        tvNotData.setVisibility(status == NOTO_DATA ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.network_exception_reload_btn:
                scrollView.setVisibility(View.VISIBLE);
                networkExceptionLayout.setVisibility(View.GONE);
                tvNotData.setVisibility(View.GONE);
                ((FootballTeamInfoActivity) mContext).initData();
                break;
            case R.id.ll_data_select:// 联赛名选择
                if (mItems.size() > 0) {
                    popWindow();
                    fl_mask_view.setAlpha(0.3f);
                    tv_team_select.setBackgroundResource(R.mipmap.foot_team_info_select_not);
                }
                break;
        }
    }

    private void popWindow() {
        final View mView = View.inflate(mContext, R.layout.popup_foot_team_info, null);
        // 创建ArrayAdapter对象
        FootTeamArrayAdapter mAdapter = new FootTeamArrayAdapter(mContext, mItems, currentNameIndex);

        ListView listview = (ListView) mView.findViewById(R.id.match_type);
        listview.setAdapter(mAdapter);

        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(ll_data_select.getWidth());
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(ll_data_select);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentNameIndex = position;
                dataContent.setText(mItems.get(position));
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                initData(0);
                fl_mask_view.setAlpha(0f);
                tv_team_select.setBackgroundResource(R.mipmap.foot_team_info_select_ok);
            }
        });
    }
}
