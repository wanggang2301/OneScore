package com.hhly.mlottery.frame;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.HandicapStatisticsBean;
import com.hhly.mlottery.bean.footballDetails.AnalyzeBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuely198 on 2017/6/20.
 * 篮球盘口统计
 */

public class HandicapStatisticsFragment extends Fragment implements View.OnClickListener {


    private View view;
    private RadioGroup radioGroup;
    private RadioButton let_split;
    private RadioButton size_disk;
    private LinearLayout clean_rl;
    private TextView hanging_z, hanging_h;
    private TextView hanging_g;
    private TextView footwall_z;
    private TextView footwall_h;
    private TextView footwall_g;
    private TextView bigball_z;
    private TextView bigball_h;
    private TextView bigball_g;
    private TextView water_z;
    private TextView water_h;
    private TextView water_g;
    private TextView ball_z;
    private TextView ball_h;
    private TextView ball_g;
    private TextView clean_z;
    private TextView clean_h;
    private TextView clean_g;
    private TextView big_ball_z;
    private TextView big_ball_h;
    private TextView big_ball_g;
    private TextView go_z;
    private TextView go_h;
    private TextView go_g;
    private TextView smallball_z;
    private TextView smallball_h;
    private TextView smallball_g;
    private TextView a_hanging_z;
    private TextView a_hanging_w;
    private TextView a_hanging_g;
    private TextView a_hanging_l;
    private TextView a_wall_s;
    private TextView a_wall_w;
    private TextView a_wall_g;
    private TextView a_wall_l;
    private TextView b_hanging_w;
    private TextView b_hanging_s;
    private TextView b_hanging_g;
    private TextView b_hanging_l;
    private TextView b_wall_s;
    private TextView b_wall_w;
    private TextView b_wall_g;
    private TextView b_wall_l;
    private TextView c_hanging_s;
    private TextView c_hanging_w;
    private TextView c_hanging_g;
    private TextView c_hanging_l;
    private TextView c_wall_s;
    private TextView c_wall_w;
    private TextView c_wall_g;
    private TextView c_wall_l;
    private TextView big_ball_z_tv;
    private TextView go_z_tv;
    private TextView smallball_z_tv;
    private TextView ball_z_tv;
    private TextView bigball_z_tv;
    private HandicapStatisticsBean.TrendPlateBean trendPlate;
    private HandicapStatisticsBean.SizePlateBean sizePlate;
    private HandicapStatisticsBean.LetPlateBean letPlate;
    private TextView screenings_z;
    private TextView screenings_h;
    private TextView screenings_g;
    private LinearLayout footwall_z_rl;
    private LinearLayout handicap_statisticss_rl;
    private LinearLayout footwall_road_rl;
    private Activity mActivity;
    private Context mContext;
    private TextView match_no_data_txt;
    private NestedScrollView scrollView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";


    private String mSeason;
    private String mLeagueId;
    private String mTeamId;
    private LinearLayout match_error_btn;
    private boolean isCheckeed = true;


    public static HandicapStatisticsFragment newInstance(String season, String leagueId, String teamId) {
        HandicapStatisticsFragment fragment = new HandicapStatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, season);
        args.putString(ARG_PARAM2, leagueId);
        args.putString(ARG_PARAM3, teamId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSeason = getArguments().getString(ARG_PARAM1);
            mLeagueId = getArguments().getString(ARG_PARAM2);
            mTeamId = getArguments().getString(ARG_PARAM3);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.handicap_statistics_fragment, container, false);
        mContext = mActivity;
        initView();
        initData();
        return view;
    }

    private void initData() {

        Map<String, String> param = new HashMap<>();
        param.put("season", mSeason);
        param.put("leagueId", mLeagueId);
        param.put("teamId", mTeamId);

        VolleyContentFast.requestJsonByGet(BaseURLs.TEAMPLATEDATA, param, new VolleyContentFast.ResponseSuccessListener<HandicapStatisticsBean>() {
            @Override
            public void onResponse(HandicapStatisticsBean handicapStatisticsBean) {


                if (handicapStatisticsBean.getResult() == 200) {

                    //大小盘 上下盘盘路
                    trendPlate = handicapStatisticsBean.getTrendPlate();
                    //大小盘主客场盘路
                    sizePlate = handicapStatisticsBean.getSizePlate();
                    //让分盘盘路
                    letPlate = handicapStatisticsBean.getLetPlate();

                    if (isCheckeed) {

                        if (trendPlate == null && letPlate == null) {
                            radioGroup.setVisibility(View.VISIBLE);
                            match_no_data_txt.setVisibility(View.VISIBLE);
                            scrollView.setVisibility(View.GONE);
                            match_error_btn.setVisibility(View.GONE);
                        } else {
                            match_no_data_txt.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            match_error_btn.setVisibility(View.GONE);
                            radioGroup.setVisibility(View.VISIBLE);
                            initLetSplitDatas();
                        }

                    } else {
                        if (sizePlate == null) {
                            radioGroup.setVisibility(View.VISIBLE);
                            match_no_data_txt.setVisibility(View.VISIBLE);
                            scrollView.setVisibility(View.GONE);
                            match_error_btn.setVisibility(View.GONE);
                        } else {
                            match_no_data_txt.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            match_error_btn.setVisibility(View.GONE);
                            radioGroup.setVisibility(View.VISIBLE);
                            initSizeDiskDatas();
                        }

                    }


                    initEvent();
                } else {
                    radioGroup.setVisibility(View.GONE);
                    match_no_data_txt.setVisibility(View.GONE);
                    scrollView.setVisibility(View.GONE);
                    match_error_btn.setVisibility(View.VISIBLE);
                }


            }

        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                radioGroup.setVisibility(View.GONE);
                match_no_data_txt.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                match_error_btn.setVisibility(View.VISIBLE);
            }

        }, HandicapStatisticsBean.class);
    }


    private void initEvent() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                int radioButtonId = radioGroup.getCheckedRadioButtonId();


                switch (radioButtonId) {
                    case R.id.let_split:
                        isCheckeed = true;

                        if (trendPlate == null && letPlate == null) {
                            radioGroup.setVisibility(View.VISIBLE);
                            match_no_data_txt.setVisibility(View.VISIBLE);
                            scrollView.setVisibility(View.GONE);
                            match_error_btn.setVisibility(View.GONE);
                        } else {
                            match_no_data_txt.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            match_error_btn.setVisibility(View.GONE);
                            radioGroup.setVisibility(View.VISIBLE);
                            initLetSplitDatas();
                        }
                        break;
                    case R.id.size_disk:
                        isCheckeed = false;
                        if (sizePlate == null) {
                            match_no_data_txt.setVisibility(View.VISIBLE);
                            scrollView.setVisibility(View.GONE);
                            match_error_btn.setVisibility(View.GONE);
                        } else {
                            match_no_data_txt.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            match_error_btn.setVisibility(View.GONE);
                            initSizeDiskDatas();
                        }
                        break;
                    default:
                        break;
                }


            }
        });


    }

    /*更新大小盘数据*/
    private void initSizeDiskDatas() {


        clean_rl.setVisibility(View.GONE);
        handicap_statisticss_rl.setVisibility(View.GONE);
        footwall_road_rl.setVisibility(View.GONE);
        footwall_z_rl.setVisibility(View.GONE);
        bigball_z_tv.setText(getResources().getString(R.string.foot_team_odds_over));
        ball_z_tv.setText(getResources().getString(R.string.foot_team_odds_under));
        big_ball_z_tv.setText(getResources().getString(R.string.handicap_big_ball));
        go_z_tv.setText(getResources().getString(R.string.handicap_go));
        smallball_z_tv.setText(getResources().getString(R.string.handicap_small_ball));


        screenings_z.setText(sizePlate.getTotalSizePlate().getCount());
        screenings_h.setText(sizePlate.getHomeSizePlate().getCount());
        screenings_g.setText(sizePlate.getGuestSizePlate().getCount());

        bigball_z.setText(sizePlate.getTotalSizePlate().getHigh());
        bigball_h.setText(sizePlate.getHomeSizePlate().getHigh());
        bigball_g.setText(sizePlate.getGuestSizePlate().getHigh());

        water_z.setText(sizePlate.getTotalSizePlate().getDraw());
        water_h.setText(sizePlate.getHomeSizePlate().getDraw());
        water_g.setText(sizePlate.getGuestSizePlate().getDraw());

        ball_z.setText(sizePlate.getTotalSizePlate().getLow());
        ball_h.setText(sizePlate.getHomeSizePlate().getLow());
        ball_g.setText(sizePlate.getGuestSizePlate().getLow());


        big_ball_z.setText(sizePlate.getTotalSizePlate().getHighPer());
        big_ball_h.setText(sizePlate.getHomeSizePlate().getHighPer());
        big_ball_g.setText(sizePlate.getGuestSizePlate().getHighPer());


        go_z.setText(sizePlate.getTotalSizePlate().getDrawPer());
        go_h.setText(sizePlate.getHomeSizePlate().getDrawPer());
        go_g.setText(sizePlate.getGuestSizePlate().getDrawPer());

        smallball_z.setText(sizePlate.getTotalSizePlate().getLowPer());
        smallball_h.setText(sizePlate.getHomeSizePlate().getLowPer());
        smallball_g.setText(sizePlate.getGuestSizePlate().getLowPer());

    }

    /*更新让分盘数据*/
    private void initLetSplitDatas() {

        clean_rl.setVisibility(View.VISIBLE);
        footwall_road_rl.setVisibility(View.VISIBLE);
        bigball_z_tv.setText(getResources().getString(R.string.foot_team_odds_hdpw));
        ball_z_tv.setText(getResources().getString(R.string.foot_team_odds_hdpl));
        big_ball_z_tv.setText(getResources().getString(R.string.foot_team_odds_winrate));
        go_z_tv.setText(getResources().getString(R.string.handcaip_goo));
        smallball_z_tv.setText(getResources().getString(R.string.foot_team_odds_loserate));


        screenings_z.setText(letPlate.getTotalLetPlate().getCount());
        screenings_h.setText(letPlate.getHomeLetPlate().getCount());
        screenings_g.setText(letPlate.getGuestLetPlate().getCount());

        hanging_z.setText(letPlate.getTotalLetPlate().getOver());
        hanging_h.setText(letPlate.getHomeLetPlate().getOver());
        hanging_g.setText(letPlate.getGuestLetPlate().getOver());

        footwall_z.setText(letPlate.getTotalLetPlate().getUnder());
        footwall_h.setText(letPlate.getHomeLetPlate().getUnder());
        footwall_g.setText(letPlate.getGuestLetPlate().getUnder());

        bigball_z.setText(letPlate.getTotalLetPlate().getWin());
        bigball_h.setText(letPlate.getHomeLetPlate().getWin());
        bigball_g.setText(letPlate.getGuestLetPlate().getWin());

        water_z.setText(letPlate.getTotalLetPlate().getDraw());
        water_h.setText(letPlate.getHomeLetPlate().getDraw());
        water_g.setText(letPlate.getGuestLetPlate().getDraw());


        ball_z.setText(letPlate.getTotalLetPlate().getLose());
        ball_h.setText(letPlate.getHomeLetPlate().getLose());
        ball_g.setText(letPlate.getGuestLetPlate().getLose());


        clean_z.setText(letPlate.getTotalLetPlate().getNet());
        clean_h.setText(letPlate.getHomeLetPlate().getNet());
        clean_g.setText(letPlate.getGuestLetPlate().getNet());


        big_ball_z.setText(letPlate.getTotalLetPlate().getWinPer());
        big_ball_h.setText(letPlate.getHomeLetPlate().getWinPer());
        big_ball_g.setText(letPlate.getGuestLetPlate().getWinPer());


        go_z.setText(letPlate.getTotalLetPlate().getDrawPer());
        go_h.setText(letPlate.getHomeLetPlate().getDrawPer());
        go_g.setText(letPlate.getGuestLetPlate().getDrawPer());

        smallball_z.setText(letPlate.getTotalLetPlate().getLosePer());
        smallball_h.setText(letPlate.getHomeLetPlate().getLosePer());
        smallball_g.setText(letPlate.getGuestLetPlate().getLosePer());

        a_hanging_z.setText(trendPlate.getTotalPlate().getUpCount());
        a_hanging_w.setText(trendPlate.getTotalPlate().getUpWin());
        a_hanging_g.setText(trendPlate.getTotalPlate().getUpDraw());
        a_hanging_l.setText(trendPlate.getTotalPlate().getUpLose());


        a_wall_s.setText(trendPlate.getTotalPlate().getDownCount());
        a_wall_w.setText(trendPlate.getTotalPlate().getDownWin());
        a_wall_g.setText(trendPlate.getTotalPlate().getDownDraw());
        a_wall_l.setText(trendPlate.getTotalPlate().getDownLose());


        b_hanging_s.setText(trendPlate.getHomePlate().getUpCount());
        b_hanging_w.setText(trendPlate.getHomePlate().getUpWin());
        b_hanging_g.setText(trendPlate.getHomePlate().getUpDraw());
        b_hanging_l.setText(trendPlate.getHomePlate().getUpLose());


        b_wall_s.setText(trendPlate.getHomePlate().getDownCount());
        b_wall_w.setText(trendPlate.getHomePlate().getDownWin());
        b_wall_g.setText(trendPlate.getHomePlate().getDownDraw());
        b_wall_l.setText(trendPlate.getHomePlate().getDownLose());


        c_hanging_s.setText(trendPlate.getGuestPlate().getUpCount());
        c_hanging_w.setText(trendPlate.getGuestPlate().getUpWin());
        c_hanging_g.setText(trendPlate.getGuestPlate().getUpDraw());
        c_hanging_l.setText(trendPlate.getGuestPlate().getUpLose());


        c_wall_s.setText(trendPlate.getGuestPlate().getDownCount());
        c_wall_w.setText(trendPlate.getGuestPlate().getDownWin());
        c_wall_g.setText(trendPlate.getGuestPlate().getDownDraw());
        c_wall_l.setText(trendPlate.getGuestPlate().getDownLose());


    }


    private void initView() {


        scrollView = (NestedScrollView) view.findViewById(R.id.handicap_scrollview);

        //暂无数据
        match_no_data_txt = (TextView) view.findViewById(R.id.match_no_data_txt);
        //网络异常
        match_error_btn = (LinearLayout) view.findViewById(R.id.match_error_ll);
        view.findViewById(R.id.match_error_btn).setOnClickListener(this);


        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        let_split = (RadioButton) view.findViewById(R.id.let_split);
        size_disk = (RadioButton) view.findViewById(R.id.size_disk);

        //场次
        screenings_z = (TextView) view.findViewById(R.id.screenings_z);
        screenings_h = (TextView) view.findViewById(R.id.screenings_h);
        screenings_g = (TextView) view.findViewById(R.id.screenings_g);

        //上盘
        handicap_statisticss_rl = (LinearLayout) view.findViewById(R.id.handicap_statisticss_rl);
        hanging_z = (TextView) view.findViewById(R.id.hanging_z);
        hanging_h = (TextView) view.findViewById(R.id.hanging_h);
        hanging_g = (TextView) view.findViewById(R.id.hanging_g);
        //下盘
        footwall_z_rl = (LinearLayout) view.findViewById(R.id.footwall_z_rl);
        footwall_z = (TextView) view.findViewById(R.id.footwall_z);
        footwall_h = (TextView) view.findViewById(R.id.footwall_h);
        footwall_g = (TextView) view.findViewById(R.id.footwall_g);
        //赢盘
        bigball_z_tv = (TextView) view.findViewById(R.id.bigball_z_tv);
        bigball_z = (TextView) view.findViewById(R.id.bigball_z);
        bigball_h = (TextView) view.findViewById(R.id.bigball_h);
        bigball_g = (TextView) view.findViewById(R.id.bigball_g);
        //走水
        water_z = (TextView) view.findViewById(R.id.water_z);
        water_h = (TextView) view.findViewById(R.id.water_h);
        water_g = (TextView) view.findViewById(R.id.water_g);

        //输盘
        ball_z_tv = (TextView) view.findViewById(R.id.ball_z_tv);
        ball_z = (TextView) view.findViewById(R.id.ball_z);
        ball_h = (TextView) view.findViewById(R.id.ball_h);
        ball_g = (TextView) view.findViewById(R.id.ball_g);

        clean_rl = (LinearLayout) view.findViewById(R.id.clean_rl);
        //净
        clean_z = (TextView) view.findViewById(R.id.clean_z);
        clean_h = (TextView) view.findViewById(R.id.clean_h);
        clean_g = (TextView) view.findViewById(R.id.clean_g);

        //胜
        big_ball_z_tv = (TextView) view.findViewById(R.id.big_ball_z_tv);
        big_ball_z = (TextView) view.findViewById(R.id.big_ball_z);
        big_ball_h = (TextView) view.findViewById(R.id.big_ball_h);
        big_ball_g = (TextView) view.findViewById(R.id.big_ball_g);
        //走
        go_z_tv = (TextView) view.findViewById(R.id.go_z_tv);
        go_z = (TextView) view.findViewById(R.id.go_z);
        go_h = (TextView) view.findViewById(R.id.go_h);
        go_g = (TextView) view.findViewById(R.id.go_g);
        //负
        smallball_z_tv = (TextView) view.findViewById(R.id.smallball_z_tv);
        smallball_z = (TextView) view.findViewById(R.id.smallball_z);
        smallball_h = (TextView) view.findViewById(R.id.smallball_h);
        smallball_g = (TextView) view.findViewById(R.id.smallball_g);

        //上下盘盘路
        footwall_road_rl = (LinearLayout) view.findViewById(R.id.footwall_road_rl);
        // 总上盘
        a_hanging_z = (TextView) view.findViewById(R.id.a_hanging_s);
        a_hanging_w = (TextView) view.findViewById(R.id.a_hanging_w);
        a_hanging_g = (TextView) view.findViewById(R.id.a_hanging_g);
        a_hanging_l = (TextView) view.findViewById(R.id.a_hanging_l);
        //总下盘
        a_wall_s = (TextView) view.findViewById(R.id.a_wall_s);
        a_wall_w = (TextView) view.findViewById(R.id.a_wall_w);
        a_wall_g = (TextView) view.findViewById(R.id.a_wall_g);
        a_wall_l = (TextView) view.findViewById(R.id.a_wall_l);
        //主
        // 总上盘
        b_hanging_s = (TextView) view.findViewById(R.id.b_hanging_s);
        b_hanging_w = (TextView) view.findViewById(R.id.b_hanging_w);
        b_hanging_g = (TextView) view.findViewById(R.id.b_hanging_g);
        b_hanging_l = (TextView) view.findViewById(R.id.b_hanging_l);
        //总下盘
        b_wall_s = (TextView) view.findViewById(R.id.b_wall_s);
        b_wall_w = (TextView) view.findViewById(R.id.b_wall_w);
        b_wall_g = (TextView) view.findViewById(R.id.b_wall_g);
        b_wall_l = (TextView) view.findViewById(R.id.b_wall_l);


        //客
        // 总上盘
        c_hanging_s = (TextView) view.findViewById(R.id.c_hanging_s);
        c_hanging_w = (TextView) view.findViewById(R.id.c_hanging_w);
        c_hanging_g = (TextView) view.findViewById(R.id.c_hanging_g);
        c_hanging_l = (TextView) view.findViewById(R.id.c_hanging_l);
        //总下盘
        c_wall_s = (TextView) view.findViewById(R.id.c_wall_s);
        c_wall_w = (TextView) view.findViewById(R.id.c_wall_w);
        c_wall_g = (TextView) view.findViewById(R.id.c_wall_g);
        c_wall_l = (TextView) view.findViewById(R.id.c_wall_l);


    }

    /**
     * 父类调用下拉刷新
     */
    public void refreshFragment(String season) {
        mSeason = season;
        initData();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.match_error_btn:

                initData();
                break;

            default:
                break;

        }
    }
}
