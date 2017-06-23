package com.hhly.mlottery.frame;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.TechnicalStatisticBean;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuely198 on 2017/6/20.
 * 技术统计
 */

public class TechnicalStatisticsFragment extends Fragment  implements  SwipeRefreshLayout.OnRefreshListener{


    private View view;
    private RadioGroup radioGroup;
    private RadioButton preseason_games;
    private RadioButton regular_season;
    private RadioButton playoff;
    private TextView routine_win_1;
    private TextView routine_win_2;
    private TextView routine_win_3;
    private TextView routine_lose_1;
    private TextView routine_lose_2;
    private TextView routine_lose_3;
    private TextView overtime_win_1;
    private TextView overtime_win_2;
    private TextView overtime_win_3;
    private TextView overtime_lose_1;
    private TextView overtime_lose_2;
    private TextView overtime_lose_3;
    private TextView shoot_zong_1;
    private TextView shoot_zong_2;
    private TextView shoot_zong_3;
    private TextView shoot_in_1;
    private TextView shoot_in_2;
    private TextView shoot_in_3;
    private TextView shoot_hit_1;
    private TextView shoot_hit_2;
    private TextView shoot_hit_3;
    private TextView three_points_zong_1;
    private TextView three_points_zong_2;
    private TextView three_points_zong_3;
    private TextView three_points_in_1;
    private TextView three_points_in_2;
    private TextView three_points_in_3;
    private TextView three_points_hit_1;
    private TextView three_points_hit_2;
    private TextView three_points_hit_3;
    private TextView free_throw_zong_1;
    private TextView free_throw_zong_2;
    private TextView free_throw_zong_3;
    private TextView free_throw_in_2;
    private TextView free_throw_in_1;
    private TextView free_throw_in_3;
    private TextView free_throw_hit_1;
    private TextView free_throw_hit_2;
    private TextView free_throw_hit_3;
    private TextView rebound_zong_1;
    private TextView rebound_zong_2;
    private TextView rebound_zong_3;
    private TextView rebound_in_1;
    private TextView rebound_in_2;
    private TextView rebound_in_3;
    private TextView rebound_hit_1;
    private TextView rebound_hit_2;
    private TextView rebound_hit_3;
    private TextView assists_1;
    private TextView assists_2;
    private TextView assists_3;
    private TextView steals_1;
    private TextView steals_2;
    private TextView steals_3;
    private TextView shot_1;
    private TextView shot_2;
    private TextView shot_3;
    private TextView foul_1;
    private TextView foul_2;
    private TextView foul_3;
    private TextView error_1;
    private TextView error_3;
    private TextView error_2;
    private List<TechnicalStatisticBean.DataBean> playoffList;
    private List<TechnicalStatisticBean.DataBean> regularList;
    private List<TechnicalStatisticBean.DataBean> preseasonList;
    private TextView season_1;
    private TextView season_2;
    private TextView season_3;
    private ScrollView technical_scrollview;
    private TextView match_no_data_txt;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Activity mActivity;
    private Context mContext;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.technical_statistics_fragment, container, false);

        mContext = mActivity;
        initView();
        initData();
        initEvent();
        return view;
    }

    private void initData() {

        String url = "http://m.1332255.com:81/mlottery/core/basketballData.teamTechStatData.do";
        Map<String, String> param = new HashMap<>();
        param.put("season", "2016-2017");
        param.put("leagueId", "1");
        param.put("teamId", "1");

        VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<TechnicalStatisticBean>() {
            @Override
            public void onResponse(TechnicalStatisticBean handicapStatisticsBean) {


                if (handicapStatisticsBean.getResult() == 200) {

                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);

                    //季后赛
                    playoffList = handicapStatisticsBean.getPlayoffList();
                    //常规赛
                    regularList = handicapStatisticsBean.getRegularList();
                    //季前赛
                    preseasonList = handicapStatisticsBean.getPreseasonList();

                    inithandicapStatisticDatas(preseasonList);
                    initEvent();

                }
            }

        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {


            }

        }, TechnicalStatisticBean.class);


    }

    /*更新数据*/
    private void inithandicapStatisticDatas(List<TechnicalStatisticBean.DataBean> dataBean) {

        if (dataBean.size()==0) {
            swipeRefreshLayout.setVisibility(View.GONE);
            match_no_data_txt.setVisibility(View.VISIBLE);
            return;
        } else {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            match_no_data_txt.setVisibility(View.GONE);
        }


        //第一赛季
        TechnicalStatisticBean.DataBean dataBean1 = dataBean.get(0);
        TechnicalStatisticBean.DataBean dataBean2 = dataBean.get(1);
        TechnicalStatisticBean.DataBean dataBean3 = dataBean.get(2);

        //赛季时间
        season_1.setText(dataBean1.getSeason());
        season_2.setText(dataBean2.getSeason());
        season_3.setText(dataBean3.getSeason());
        //常规
        routine_win_1.setText(dataBean1.getWinRoutine() + "");
        routine_win_2.setText(dataBean2.getWinRoutine() + "");
        routine_win_3.setText(dataBean3.getWinRoutine() + "");

        routine_lose_1.setText(dataBean1.getLoseRoutine() + "");
        routine_lose_2.setText(dataBean2.getLoseRoutine() + "");
        routine_lose_3.setText(dataBean3.getLoseRoutine() + "");
        //加时
        overtime_win_1.setText(dataBean1.getWinOvertime() + "");
        overtime_win_2.setText(dataBean2.getWinOvertime() + "");
        overtime_win_3.setText(dataBean3.getWinOvertime() + "");
        overtime_lose_1.setText(dataBean1.getLoseOvertime() + "");
        overtime_lose_2.setText(dataBean2.getLoseOvertime() + "");
        overtime_lose_3.setText(dataBean3.getLoseOvertime() + "");
        //投篮
        shoot_zong_1.setText(dataBean1.getTotalShoot() + "(" + dataBean1.getAverageShoot() + ")" + "");
        shoot_zong_2.setText(dataBean2.getTotalShoot() + "(" + dataBean1.getAverageShoot() + ")" + "");
        shoot_zong_3.setText(dataBean3.getTotalShoot() + "(" + dataBean1.getAverageShoot() + ")" + "");

        shoot_in_1.setText(dataBean1.getTotalShootHit() + "(" + dataBean1.getAverageShootHit() + ")" + "");
        shoot_in_2.setText(dataBean2.getTotalShootHit() + "(" + dataBean1.getAverageShootHit() + ")" + "");
        shoot_in_3.setText(dataBean3.getTotalShootHit() + "(" + dataBean1.getAverageShootHit() + ")" + "");

        shoot_hit_1.setText(dataBean1.getPercentShot() + "");
        shoot_hit_2.setText(dataBean2.getPercentShot() + "");
        shoot_hit_3.setText(dataBean3.getPercentShot() + "");
        //三分
        three_points_zong_1.setText(dataBean1.getTotalThreeMin() + "(" + dataBean1.getAverageThreeMin() + ")" + "");
        three_points_zong_2.setText(dataBean2.getTotalThreeMin() + "(" + dataBean1.getAverageThreeMin() + ")" + "");
        three_points_zong_3.setText(dataBean3.getTotalThreeMin() + "(" + dataBean1.getAverageThreeMin() + ")" + "");

        three_points_in_1.setText(dataBean1.getTotalThreeMinHit() + "(" + dataBean1.getAverageThreeMinHit() + ")" + "");
        three_points_in_2.setText(dataBean2.getTotalThreeMinHit() + "(" + dataBean1.getAverageThreeMinHit() + ")" + "");
        three_points_in_3.setText(dataBean3.getTotalThreeMinHit() + "(" + dataBean1.getAverageThreeMinHit() + ")" + "");

        three_points_hit_1.setText(dataBean1.getPercentThree());
        three_points_hit_2.setText(dataBean2.getPercentThree());
        three_points_hit_3.setText(dataBean3.getPercentThree());
        //罚球
        free_throw_zong_1.setText(dataBean1.getTotalPunishBall() + "(" + dataBean1.getAveragePunishBall() + ")" + "");
        free_throw_zong_2.setText(dataBean2.getTotalPunishBall() + "(" + dataBean1.getAveragePunishBall() + ")" + "");
        free_throw_zong_3.setText(dataBean3.getTotalPunishBall() + "(" + dataBean1.getAveragePunishBall() + ")" + "");


        free_throw_in_1.setText(dataBean1.getTotalPunishBallHit() + "(" + dataBean1.getAveragePunishBallHit() + ")" + "");
        free_throw_in_2.setText(dataBean2.getTotalPunishBallHit() + "(" + dataBean1.getAveragePunishBallHit() + ")" + "");
        free_throw_in_3.setText(dataBean3.getTotalPunishBallHit() + "(" + dataBean1.getAveragePunishBallHit() + ")" + "");


        free_throw_hit_1.setText(dataBean1.getPercentThrow() + "");
        free_throw_hit_2.setText(dataBean2.getPercentThrow() + "");
        free_throw_hit_3.setText(dataBean3.getPercentThrow() + "");
        //篮板
        rebound_zong_1.setText(dataBean1.getAttack() + dataBean1.getDefend() + "");
        rebound_zong_2.setText(dataBean2.getAttack() + dataBean1.getDefend() + "");
        rebound_zong_3.setText(dataBean3.getAttack() + dataBean1.getDefend() + "");

        rebound_in_1.setText(dataBean1.getAttack() + "");
        rebound_in_2.setText(dataBean2.getAttack() + "");
        rebound_in_3.setText(dataBean3.getAttack() + "");

        rebound_hit_1.setText(dataBean1.getDefend() + "");
        rebound_hit_2.setText(dataBean2.getDefend() + "");
        rebound_hit_3.setText(dataBean3.getDefend() + "");

        //助攻
        assists_1.setText(dataBean1.getHelpattack() + "");
        assists_2.setText(dataBean2.getHelpattack() + "");
        assists_3.setText(dataBean3.getHelpattack() + "");
        //抢断
        steals_1.setText(dataBean1.getRob() + "");
        steals_2.setText(dataBean2.getRob() + "");
        steals_3.setText(dataBean3.getRob() + "");
        //盖帽
        shot_1.setText(dataBean1.getCover() + "");
        shot_2.setText(dataBean2.getCover() + "");
        shot_3.setText(dataBean3.getCover() + "");
        //犯规
        foul_1.setText(dataBean1.getFoul() + "");
        foul_2.setText(dataBean2.getFoul() + "");
        foul_3.setText(dataBean3.getFoul() + "");
        //失误
        error_1.setText(dataBean1.getMisplay() + "");
        error_2.setText(dataBean2.getMisplay() + "");
        error_3.setText(dataBean3.getMisplay() + "");


    }

    private void initEvent() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                int radioButtonId = radioGroup.getCheckedRadioButtonId();


                switch (radioButtonId) {
                    case R.id.odd_plate_btn://季前赛
                        inithandicapStatisticDatas(preseasonList);
                        break;
                    case R.id.odd_big_btn://常规赛
                        inithandicapStatisticDatas(regularList);

                        break;
                    case R.id.odd_op_btn://季后赛
                        inithandicapStatisticDatas(playoffList);

                        break;
                    default:
                        break;
                }


            }
        });


    }

    private void initView() {


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));




        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        preseason_games = (RadioButton) view.findViewById(R.id.odd_plate_btn);
        regular_season = (RadioButton) view.findViewById(R.id.odd_big_btn);
        playoff = (RadioButton) view.findViewById(R.id.odd_op_btn);


        technical_scrollview = (ScrollView) view.findViewById(R.id.technical_scrollview);
        //暂无数据
        match_no_data_txt = (TextView) view.findViewById(R.id.match_no_data_txt);
        //赛季时间
        season_1 = (TextView) view.findViewById(R.id.season_1);
        season_2 = (TextView) view.findViewById(R.id.season_2);
        season_3 = (TextView) view.findViewById(R.id.season_3);


        //常规
        routine_win_1 = (TextView) view.findViewById(R.id.routine_win_11);
        routine_win_2 = (TextView) view.findViewById(R.id.routine_win_22);
        routine_win_3 = (TextView) view.findViewById(R.id.routine_win_33);


        routine_lose_1 = (TextView) view.findViewById(R.id.routine_lose_1);
        routine_lose_2 = (TextView) view.findViewById(R.id.routine_lose_2);
        routine_lose_3 = (TextView) view.findViewById(R.id.routine_lose_3);
        //加时
        overtime_win_1 = (TextView) view.findViewById(R.id.overtime_win_1);
        overtime_win_2 = (TextView) view.findViewById(R.id.overtime_win_2);
        overtime_win_3 = (TextView) view.findViewById(R.id.overtime_win_3);
        overtime_lose_1 = (TextView) view.findViewById(R.id.overtime_lose_1);
        overtime_lose_2 = (TextView) view.findViewById(R.id.overtime_lose_2);
        overtime_lose_3 = (TextView) view.findViewById(R.id.overtime_lose_3);
        //投篮
        shoot_zong_1 = (TextView) view.findViewById(R.id.shoot_zong_1);
        shoot_zong_2 = (TextView) view.findViewById(R.id.shoot_zong_2);
        shoot_zong_3 = (TextView) view.findViewById(R.id.shoot_zong_3);
        shoot_in_1 = (TextView) view.findViewById(R.id.shoot_in_1);
        shoot_in_2 = (TextView) view.findViewById(R.id.shoot_in_2);
        shoot_in_3 = (TextView) view.findViewById(R.id.shoot_in_3);
        shoot_hit_1 = (TextView) view.findViewById(R.id.shoot_hit_1);
        shoot_hit_2 = (TextView) view.findViewById(R.id.shoot_hit_2);
        shoot_hit_3 = (TextView) view.findViewById(R.id.shoot_hit_3);
        //三分
        three_points_zong_1 = (TextView) view.findViewById(R.id.three_points_zong_1);
        three_points_zong_2 = (TextView) view.findViewById(R.id.three_points_zong_2);
        three_points_zong_3 = (TextView) view.findViewById(R.id.three_points_zong_3);
        three_points_in_1 = (TextView) view.findViewById(R.id.three_points_in_1);
        three_points_in_2 = (TextView) view.findViewById(R.id.three_points_in_2);
        three_points_in_3 = (TextView) view.findViewById(R.id.three_points_in_3);
        three_points_hit_1 = (TextView) view.findViewById(R.id.three_points_hit_1);
        three_points_hit_2 = (TextView) view.findViewById(R.id.three_points_hit_2);
        three_points_hit_3 = (TextView) view.findViewById(R.id.three_points_hit_3);
        //罚球
        free_throw_zong_1 = (TextView) view.findViewById(R.id.free_throw_zong_1);
        free_throw_zong_2 = (TextView) view.findViewById(R.id.free_throw_zong_2);
        free_throw_zong_3 = (TextView) view.findViewById(R.id.free_throw_zong_3);
        free_throw_in_1 = (TextView) view.findViewById(R.id.free_throw_in_1);
        free_throw_in_2 = (TextView) view.findViewById(R.id.free_throw_in_2);
        free_throw_in_3 = (TextView) view.findViewById(R.id.free_throw_in_3);
        free_throw_hit_1 = (TextView) view.findViewById(R.id.free_throw_hit_1);
        free_throw_hit_2 = (TextView) view.findViewById(R.id.free_throw_hit_2);
        free_throw_hit_3 = (TextView) view.findViewById(R.id.free_throw_hit_3);
        //篮板
        rebound_zong_1 = (TextView) view.findViewById(R.id.rebound_zong_1);
        rebound_zong_2 = (TextView) view.findViewById(R.id.rebound_zong_2);
        rebound_zong_3 = (TextView) view.findViewById(R.id.rebound_zong_3);
        rebound_in_1 = (TextView) view.findViewById(R.id.rebound_in_1);
        rebound_in_2 = (TextView) view.findViewById(R.id.rebound_in_2);
        rebound_in_3 = (TextView) view.findViewById(R.id.rebound_in_3);
        rebound_hit_1 = (TextView) view.findViewById(R.id.rebound_hit_1);
        rebound_hit_2 = (TextView) view.findViewById(R.id.rebound_hit_2);
        rebound_hit_3 = (TextView) view.findViewById(R.id.rebound_hit_3);

        //助攻
        assists_1 = (TextView) view.findViewById(R.id.assists_1);
        assists_2 = (TextView) view.findViewById(R.id.assists_2);
        assists_3 = (TextView) view.findViewById(R.id.assists_3);
        //抢断
        steals_1 = (TextView) view.findViewById(R.id.steals_1);
        steals_2 = (TextView) view.findViewById(R.id.steals_2);
        steals_3 = (TextView) view.findViewById(R.id.steals_3);
        //盖帽
        shot_1 = (TextView) view.findViewById(R.id.shot_1);
        shot_2 = (TextView) view.findViewById(R.id.shot_2);
        shot_3 = (TextView) view.findViewById(R.id.shot_3);
        //犯规
        foul_1 = (TextView) view.findViewById(R.id.foul_1);
        foul_2 = (TextView) view.findViewById(R.id.foul_2);
        foul_3 = (TextView) view.findViewById(R.id.foul_3);
        //失误
        error_1 = (TextView) view.findViewById(R.id.error_1);
        error_2 = (TextView) view.findViewById(R.id.error_2);
        error_3 = (TextView) view.findViewById(R.id.error_3);


    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (Activity) context;
    }

    @Override
    public void onRefresh() {
        preseason_games.setChecked(true);
        regular_season.setChecked(false);
        playoff.setChecked(false);
        initData();
    }
}