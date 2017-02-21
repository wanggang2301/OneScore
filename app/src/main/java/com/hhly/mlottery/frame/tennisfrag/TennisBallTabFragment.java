package com.hhly.mlottery.frame.tennisfrag;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.tennisball.TennisBallScoreAdapter;
import com.hhly.mlottery.bean.tennisball.TennisBallBean;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

/**
 * desc:网球比分列表Tab页面
 * Created by 107_tangrr on 2017/2/20 0020.
 */

public class TennisBallTabFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String TENNIS_TYPE = "tennis_type";
    private int type;

    private Context mContext;
    private View mView;
    private LinearLayout tennis_date_content;
    private RecyclerView tennis_recycler;
    private TextView tv_date;
    private TennisBallBean tennisBallBean;
    private ExactSwipeRefreshLayout swipeRefreshLayout;
    private TennisBallScoreAdapter mAdapter;
    private TennisDateChooseDialogFragment tennisDateChooseDialogFragment;

    public static TennisBallTabFragment newInstance(int type) {
        TennisBallTabFragment fragment = new TennisBallTabFragment();
        Bundle args = new Bundle();
        args.putInt(TENNIS_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(TENNIS_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mContext = getActivity();
        mView = View.inflate(mContext, R.layout.fragment_tennls_ball_tab, null);

        initView();
        initData();

        return mView;
    }

    private void initData() {
        getData();

        mAdapter = new TennisBallScoreAdapter(R.layout.item_tennis_score, tennisBallBean.getData().getMatchData());
        tennis_recycler.setAdapter(mAdapter);
    }

    private void initView() {
        tennis_date_content = (LinearLayout) mView.findViewById(R.id.tennis_date_content);
        tennis_recycler = (RecyclerView) mView.findViewById(R.id.tennis_recycler);
        tv_date = (TextView) mView.findViewById(R.id.tv_date);
        tv_date.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        tennis_recycler.setLayoutManager(layoutManager);
        swipeRefreshLayout = (ExactSwipeRefreshLayout) mView.findViewById(R.id.tennis_swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));

        switch (type) {
            case 0:// 即时
                tennis_date_content.setVisibility(View.GONE);
                break;
            case 1:// 赛果
                tennis_date_content.setVisibility(View.VISIBLE);
                break;
            case 2:// 赛程
                tennis_date_content.setVisibility(View.VISIBLE);
                break;
            case 3:// 关注
                tennis_date_content.setVisibility(View.GONE);
                break;
        }
    }


    private void getData() {
        tennisBallBean = JSON.parseObject(TestData, TennisBallBean.class);
    }

    private String TestData = "{\"result\":200,\"data\":{\"dates\":[\"2017-02-20\",\"2017-02-19\",\"2017-02-15\"],\"matchData\":[{\"matchId\":\"786811\",\"leagueId\":\"323417\",\"leagueName\":\"卡塔尔公开赛 \",\"roundName\":\"第一轮\",\"time\":\"08:00:00\",\"date\":\"2017-02-20\",\"homePlayerId\":\"319473\",\"homePlayerName\":\"西格蒙德\",\"homePlayerId2\":\"\",\"homePlayerName2\":\"\",\"awayPlayerId\":\"319494\",\"awayPlayerName\":\"普格\",\"awayPlayerId2\":\"\",\"awayPlayerName2\":\"\",\"matchStatus\":-1,\"server\":0,\"set\":0,\"matchScore\":{\"homeSetScore1\":0,\"homeSetScore2\":0,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":0,\"awaySetScore2\":0,\"awaySetScore3\":0,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeDeciderScore1\":0,\"homeDeciderScore2\":0,\"homeDeciderScore3\":0,\"homeDeciderScore4\":0,\"homeDeciderScore5\":0,\"awayDeciderScore1\":0,\"awayDeciderScore2\":0,\"awayDeciderScore3\":0,\"awayDeciderScore4\":0,\"awayDeciderScore5\":0,\"homeTotalScore\":0,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null}},{\"matchId\":\"786563\",\"leagueId\":\"323417\",\"leagueName\":\"卡塔1111尔公开赛 \",\"roundName\":\"第一轮\",\"time\":\"16:00:00\",\"date\":\"2017-02-20\",\"homePlayerId\":\"319426\",\"homePlayerName\":\"张啊啊啊帅\",\"homePlayerId2\":\"\",\"homePlayerName2\":\"\",\"awayPlayerId\":\"319578\",\"awayPlayerName\":\"巴波丝\",\"awayPlayerId2\":\"\",\"awayPlayerName2\":\"\",\"matchStatus\":0,\"server\":0,\"set\":0,\"matchScore\":{\"homeSetScore1\":0,\"homeSetScore2\":10,\"homeSetScore3\":0,\"homeSetScore4\":0,\"homeSetScore5\":0,\"awaySetScore1\":0,\"awaySetScore2\":0,\"awaySetScore3\":10,\"awaySetScore4\":0,\"awaySetScore5\":0,\"homeDeciderScore1\":0,\"homeDeciderScore2\":0,\"homeDeciderScore3\":0,\"homeDeciderScore4\":0,\"homeDeciderScore5\":0,\"awayDeciderScore1\":0,\"awayDeciderScore2\":0,\"awayDeciderScore3\":0,\"awayDeciderScore4\":0,\"awayDeciderScore5\":0,\"homeTotalScore\":30,\"awayTotalScore\":0,\"homeCurrentScore\":null,\"awayCurrentScore\":null}}]}}";

    @Override
    public void onRefresh() {
        ToastTools.showQuick(mContext, "下拉刷新!");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date:// 日期选择
                dateChooseShow();

                break;
        }
    }

    private void dateChooseShow() {
        if (tennisDateChooseDialogFragment == null) {
            tennisDateChooseDialogFragment = TennisDateChooseDialogFragment.newInstance(tennisBallBean.getData().getDates(), "2017-02-20", new TennisDateChooseDialogFragment.OnDateChooseListener() {
                @Override
                public void onDateChoose(String date) {
                    //  TODO 选择后，设置显示的日期及对应的数据

                    ToastTools.showQuick(mContext, "选择了："+date);
                }
            });
        }
        if (!tennisDateChooseDialogFragment.isVisible()) {
            tennisDateChooseDialogFragment.show(getChildFragmentManager(), "tennisDateChooseDialogFragment");
        }
    }
}
