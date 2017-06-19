package com.hhly.mlottery.activity;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.ToastTools;

import pl.droidsonroids.gif.GifImageView;

import static com.hhly.mlottery.R.id.ll_guest_free_kick_fk3_title_2_time;

/**
 * desc:足球事件动画直播
 * Created by 107_tangrr on 2017/5/29 0029.
 */

public class FootballEventAnimationFragment extends Activity implements View.OnClickListener {

    // 比赛开始、信号中断、喝水、受伤、伤停补时 LinearLayout
    private LinearLayout ll_match_start_content;
    // 比赛状态显示控件
    private RelativeLayout rl_match_txt_content;
    private GifImageView gif_match_start;
    private TextView tv_start, tv_half_txt;
    // 信号中断、喝水、受伤、伤停补时
    private ImageView iv_signal_off, iv_drink_water, iv_injured, iv_injured_addtime;
    // 后场控球、进攻、危险进攻
    private LinearLayout ll_control_content;
    // 后场控球
    private LinearLayout ll_control;
    private GifImageView gif_home_control, gif_guest_control;
    // 进攻
    private RelativeLayout rl_attack;
    private GifImageView gif_home_attack, gif_guest_attack;
    // 危险进攻
    private RelativeLayout rl_attack_danger;
    private GifImageView gif_home_attack_danger, gif_guest_attack_danger;
    // 射正、射偏1
    private LinearLayout ll_offside_content1;
    private RelativeLayout rl_home_offside1, rl_guest_offside1,rl_home_offside_title1,rl_guest_offside_title1;
    private TextView tv_home_title1, tv_guest_title1,tv_home_title1_time,tv_guest_title1_time;
    private GifImageView gif_home_position1, gif_guest_position1;
    private GifImageView gif_home_ball1, gif_guest_ball1, gif_home_hit1, gif_guest_hit1;
    // 射正、射偏2
    private LinearLayout ll_offside_content2;
    private RelativeLayout rl_home_offside2, rl_guest_offside2,rl_home_offside_title2,rl_guest_offside_title2;
    private TextView tv_home_title2, tv_guest_title2,tv_home_title2_time,tv_guest_title2_time;
    private GifImageView gif_home_position2, gif_guest_position2;
    private GifImageView gif_home_ball2, gif_guest_ball2, gif_guest_hit2, gif_home_hit2;
    // 球门球
    private LinearLayout ll_goal_door_content;
    private RelativeLayout rl_home_goal_door, rl_guest_goal_door;
    private GifImageView gif_home_goal_door_position, gif_guest_goal_door_position;
    private GifImageView gfi_home_goal_door, gfi_guest_goal_door;
    private TextView tv_home_goal_door_title_time,tv_guest_goal_door_title_time;
    // 界外球
    private RelativeLayout rl_goal_out_content;
    private RelativeLayout rl_home_goal_out, rl_guest_goal_out;
    private GifImageView gif_home_goal_out_position, gif_guest_goal_out_position;
    private GifImageView gif_home_goal_out, gif_guest_goal_out;
    private TextView tv_home_goal_out_title_time,tv_guest_goal_out_title_time;
    // 角球
    private RelativeLayout rl_corner_content;
    private RelativeLayout rl_home_corner, rl_guest_corner;
    private LinearLayout ll_home_title_l, ll_home_title_r, ll_guest_title_l, ll_guest_title_r;
    private GifImageView gif_guest_corner_r_position, gif_guest_corner_l_position, gif_home_corner_r_position, gif_home_corner_l_position;
    private GifImageView gif_guest_corner_r, gif_guest_corner_l, gif_home_corner_r, gif_home_corner_l;
    private TextView tv_home_corner_title_l_time,tv_guest_corner_title_l_time,tv_home_corner_title_r_time,tv_guest_corner_title_r_time;
    // 越位
    private RelativeLayout rl_offside_content;
    private LinearLayout ll_home_offside_title, ll_guest_offside_title;
    private RelativeLayout rl_home_offside_bg, rl_guest_offside_bg;
    private GifImageView gif_home_offside, gif_guest_offside;
    private GifImageView gif_home_offside_position, gif_guest_offside_position;
    // 点球
    private LinearLayout ll_penalty_content;
    private GifImageView gif_home_penalty, gif_guest_penalty;
    // 点球罚失
    private RelativeLayout rl_penalty_lose_content;
    private LinearLayout ll_home_penalty_lose_title, ll_guest_penalty_lose_title;
    private GifImageView gif_home_penalty_lose, gif_guest_penalty_lose;
    private GifImageView gif_home_penalty_lose_position, gif_guest_penalty_lose_position;
    // 红黄牌
    private RelativeLayout rl_r_or_y_content;
    private LinearLayout ll_home_r_or_y_title, ll_guest_r_or_y_title;
    private GifImageView gif_home_r_or_y_position, gif_guest_r_or_y_position;
    private TextView tv_home_desc, tv_guest_desc;
    private ImageView iv_home_icon, iv_guest_icon, iv_home_r_or_y, iv_guest_r_or_y;
    // 任意球fk2、fk4
    private RelativeLayout fl_free_kick_fk2_fk4_content;
    private LinearLayout ll_home_free_kick_bg, ll_guest_free_kick_bg, ll_home_free_kick_fk4_bg, ll_guest_free_kick_fk4_bg, ll_home_free_kick_fk2_bg, ll_guest_free_kick_fk2_bg;
    private LinearLayout ll_guest_free_kick_fk2_title,ll_home_free_kick_fk2_title,ll_home_free_kick_fk4_title,ll_guest_free_kick_fk4_title;
    private TextView tv_home_free_kick_fk4_title, tv_guest_free_kick_fk4_title, tv_home_free_kick_fk2_title, tv_guest_free_kick_fk2_title;
    private TextView tv_guest_free_kick_fk2_title_time,tv_home_free_kick_fk2_title_time,tv_home_free_kick_fk4_title_time,tv_guest_free_kick_fk4_title_time;
    private GifImageView gif_home_free_kick_fk2_position, gif_guest_free_kick_fk2_position, gif_home_free_kick_fk4_position, gif_guest_free_kick_fk4_position;
    // 任意球fk3 1
    private LinearLayout fl_free_kick_fk3_content1;
    private RelativeLayout rl_home_free_kick_fk3_1, rl_guest_free_kick_fk3_1;
    private LinearLayout ll_home_free_kick_fk3_title,ll_guest_free_kick_fk3_title;
    private GifImageView gif_home_free_kick_fk3_position, gif_guest_free_kick_fk3_position;
    // 任意球fk3 2
    private RelativeLayout fl_free_kick_fk3_content2;
    private LinearLayout ll_home_free_kick_fk3_bg_2, ll_guest_free_kick_fk3_bg_2;
    private LinearLayout ll_home_free_kick_fk3_title_2, ll_guest_free_kick_fk3_title_2;
    private GifImageView gif_home_free_kick_fk3_position_2, gif_guest_free_kick_fk3_position_2;
    private TextView ll_home_free_kick_fk3_title_2_time,ll_guest_free_kick_fk3_title_time,ll_home_free_kick_fk3_title_time,ll_guest_free_kick_fk3_title_2_time;
    // 任意球fk1
    private RelativeLayout fl_free_kick_fk1_content;
    private LinearLayout ll_home_free_kick_fk1_bg, ll_guest_free_kick_fk1_bg;
    private LinearLayout ll_home_free_kick_fk1_title, ll_guest_free_kick_fk1_title;
    private TextView ll_home_free_kick_fk1_title_time, ll_guest_free_kick_fk1_title_time;
    private GifImageView gif_home_free_kick_fk1_position, gif_guest_free_kick_fk1_position;

    // 进球
    private LinearLayout ll_goal_content;
    private ImageView iv_home_goal,iv_guest_goal;
    private GifImageView gif_home_goal_cancel,gif_guest_goal_cancel;
    private TextView tv_home_goal_title,tv_guest_goal_title;

    // 欢呼
    private LinearLayout ll_cheer_content;
    private GifImageView gif_cheer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.football_event_animation_fragment);

        initView();
        initEvent();
    }

    private void initView() {
        // 比赛开始、信号中断、喝水、受伤、伤停补时 LinearLayout
        ll_match_start_content = (LinearLayout) findViewById(R.id.ll_match_start_content);
        rl_match_txt_content = (RelativeLayout) findViewById(R.id.rl_match_txt_content);
        gif_match_start = (GifImageView) findViewById(R.id.gif_match_start);
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_half_txt = (TextView) findViewById(R.id.tv_half_txt);
        iv_signal_off = (ImageView) findViewById(R.id.iv_signal_off);
        iv_drink_water = (ImageView) findViewById(R.id.iv_drink_water);
        iv_injured = (ImageView) findViewById(R.id.iv_injured);
        iv_injured_addtime = (ImageView) findViewById(R.id.iv_injured_addtime);

        // 后场控球、进攻、危险进攻
        ll_control_content = (LinearLayout) findViewById(R.id.ll_control_content);
        ll_control = (LinearLayout) findViewById(R.id.ll_control);
        gif_home_control = (GifImageView) findViewById(R.id.gif_home_control);
        gif_guest_control = (GifImageView) findViewById(R.id.gif_guest_control);
        rl_attack = (RelativeLayout) findViewById(R.id.rl_attack);
        gif_home_attack = (GifImageView) findViewById(R.id.gif_home_attack);
        gif_guest_attack = (GifImageView) findViewById(R.id.gif_guest_attack);
        rl_attack_danger = (RelativeLayout) findViewById(R.id.rl_attack_danger);
        gif_home_attack_danger = (GifImageView) findViewById(R.id.gif_home_attack_danger);
        gif_guest_attack_danger = (GifImageView) findViewById(R.id.gif_guest_attack_danger);

        // 射正、射偏1
        ll_offside_content1 = (LinearLayout) findViewById(R.id.ll_offside_content1);
        rl_home_offside1 = (RelativeLayout) findViewById(R.id.rl_home_offside1);
        rl_guest_offside1 = (RelativeLayout) findViewById(R.id.rl_guest_offside1);
        rl_home_offside_title1 = (RelativeLayout) findViewById(R.id.rl_home_offside_title1);
        rl_guest_offside_title1 = (RelativeLayout) findViewById(R.id.rl_guest_offside_title1);
        tv_home_title1 = (TextView) findViewById(R.id.tv_home_title1);
        tv_guest_title1 = (TextView) findViewById(R.id.tv_guest_title1);
        gif_home_position1 = (GifImageView) findViewById(R.id.gif_home_position1);
        gif_guest_position1 = (GifImageView) findViewById(R.id.gif_guest_position1);
        gif_home_ball1 = (GifImageView) findViewById(R.id.gif_home_ball1);
        gif_guest_ball1 = (GifImageView) findViewById(R.id.gif_guest_ball1);
        gif_home_hit1 = (GifImageView) findViewById(R.id.gif_home_hit1);
        gif_guest_hit1 = (GifImageView) findViewById(R.id.gif_guest_hit1);
        tv_home_title1_time = (TextView) findViewById(R.id.tv_home_title1_time);
        tv_guest_title1_time = (TextView) findViewById(R.id.tv_guest_title1_time);

        // 射正、射偏2
        ll_offside_content2 = (LinearLayout) findViewById(R.id.ll_offside_content2);
        rl_home_offside2 = (RelativeLayout) findViewById(R.id.rl_home_offside2);
        rl_guest_offside2 = (RelativeLayout) findViewById(R.id.rl_guest_offside2);
        rl_home_offside_title2 = (RelativeLayout) findViewById(R.id.rl_home_offside_title2);
        rl_guest_offside_title2 = (RelativeLayout) findViewById(R.id.rl_guest_offside_title2);
        tv_home_title2 = (TextView) findViewById(R.id.tv_home_title2);
        tv_guest_title2 = (TextView) findViewById(R.id.tv_guest_title2);
        tv_home_title2_time = (TextView) findViewById(R.id.tv_home_title2_time);
        tv_guest_title2_time = (TextView) findViewById(R.id.tv_guest_title2_time);
        gif_home_position2 = (GifImageView) findViewById(R.id.gif_home_position2);
        gif_guest_position2 = (GifImageView) findViewById(R.id.gif_guest_position2);
        gif_home_ball2 = (GifImageView) findViewById(R.id.gif_home_ball2);
        gif_guest_ball2 = (GifImageView) findViewById(R.id.gif_guest_ball2);
        gif_home_hit2 = (GifImageView) findViewById(R.id.gif_home_hit2);
        gif_guest_hit2 = (GifImageView) findViewById(R.id.gif_guest_hit2);

        // 球门球
        ll_goal_door_content = (LinearLayout) findViewById(R.id.ll_goal_door_content);
        rl_home_goal_door = (RelativeLayout) findViewById(R.id.rl_home_goal_door);
        rl_guest_goal_door = (RelativeLayout) findViewById(R.id.rl_guest_goal_door);
        gif_home_goal_door_position = (GifImageView) findViewById(R.id.gif_home_goal_door_position);
        gif_guest_goal_door_position = (GifImageView) findViewById(R.id.gif_guest_goal_door_position);
        gfi_home_goal_door = (GifImageView) findViewById(R.id.gfi_home_goal_door);
        gfi_guest_goal_door = (GifImageView) findViewById(R.id.gfi_guest_goal_door);
        tv_home_goal_door_title_time = (TextView) findViewById(R.id.tv_home_goal_door_title_time);
        tv_guest_goal_door_title_time = (TextView) findViewById(R.id.tv_guest_goal_door_title_time);

        // 界外球
        rl_goal_out_content = (RelativeLayout) findViewById(R.id.rl_goal_out_content);
        rl_home_goal_out = (RelativeLayout) findViewById(R.id.rl_home_goal_out);
        rl_guest_goal_out = (RelativeLayout) findViewById(R.id.rl_guest_goal_out);
        gif_home_goal_out_position = (GifImageView) findViewById(R.id.gif_home_goal_out_position);
        gif_guest_goal_out_position = (GifImageView) findViewById(R.id.gif_guest_goal_out_position);
        gif_home_goal_out = (GifImageView) findViewById(R.id.gif_home_goal_out);
        gif_guest_goal_out = (GifImageView) findViewById(R.id.gif_guest_goal_out);
        tv_home_goal_out_title_time = (TextView) findViewById(R.id.tv_home_goal_out_title_time);
        tv_guest_goal_out_title_time = (TextView) findViewById(R.id.tv_guest_goal_out_title_time);

        // 角球
        rl_corner_content = (RelativeLayout) findViewById(R.id.rl_corner_content);
        rl_home_corner = (RelativeLayout) findViewById(R.id.rl_home_corner);
        rl_guest_corner = (RelativeLayout) findViewById(R.id.rl_guest_corner);
        ll_home_title_l = (LinearLayout) findViewById(R.id.ll_home_title_l);
        ll_guest_title_l = (LinearLayout) findViewById(R.id.ll_guest_title_l);
        ll_home_title_r = (LinearLayout) findViewById(R.id.ll_home_title_r);
        ll_guest_title_r = (LinearLayout) findViewById(R.id.ll_guest_title_r);
        gif_guest_corner_r_position = (GifImageView) findViewById(R.id.gif_guest_corner_r_position);
        gif_home_corner_r_position = (GifImageView) findViewById(R.id.gif_home_corner_r_position);
        gif_guest_corner_l_position = (GifImageView) findViewById(R.id.gif_guest_corner_l_position);
        gif_home_corner_l_position = (GifImageView) findViewById(R.id.gif_home_corner_l_position);
        gif_guest_corner_r = (GifImageView) findViewById(R.id.gif_guest_corner_r);
        gif_home_corner_r = (GifImageView) findViewById(R.id.gif_home_corner_r);
        gif_guest_corner_l = (GifImageView) findViewById(R.id.gif_guest_corner_l);
        gif_home_corner_l = (GifImageView) findViewById(R.id.gif_home_corner_l);
        tv_home_corner_title_l_time = (TextView) findViewById(R.id.tv_home_corner_title_l_time);
        tv_guest_corner_title_l_time = (TextView) findViewById(R.id.tv_guest_corner_title_l_time);
        tv_home_corner_title_r_time = (TextView) findViewById(R.id.tv_home_corner_title_r_time);
        tv_guest_corner_title_r_time = (TextView) findViewById(R.id.tv_guest_corner_title_r_time);

        // 越位
        rl_offside_content = (RelativeLayout) findViewById(R.id.rl_offside_content);
        rl_home_offside_bg = (RelativeLayout) findViewById(R.id.rl_home_offside_bg);
        rl_guest_offside_bg = (RelativeLayout) findViewById(R.id.rl_guest_offside_bg);
        ll_home_offside_title = (LinearLayout) findViewById(R.id.ll_home_offside_title);
        ll_guest_offside_title = (LinearLayout) findViewById(R.id.ll_guest_offside_title);
        gif_home_offside = (GifImageView) findViewById(R.id.gif_home_offside);
        gif_guest_offside = (GifImageView) findViewById(R.id.gif_guest_offside);
        gif_home_offside_position = (GifImageView) findViewById(R.id.gif_home_offside_position);
        gif_guest_offside_position = (GifImageView) findViewById(R.id.gif_guest_offside_position);

        // 点球
        ll_penalty_content = (LinearLayout) findViewById(R.id.ll_penalty_content);
        gif_home_penalty = (GifImageView) findViewById(R.id.gif_home_penalty);
        gif_guest_penalty = (GifImageView) findViewById(R.id.gif_guest_penalty);

        // 点球罚失
        rl_penalty_lose_content = (RelativeLayout) findViewById(R.id.rl_penalty_lose_content);
        ll_home_penalty_lose_title = (LinearLayout) findViewById(R.id.ll_home_penalty_lose_title);
        ll_guest_penalty_lose_title = (LinearLayout) findViewById(R.id.ll_guest_penalty_lose_title);
        gif_home_penalty_lose = (GifImageView) findViewById(R.id.gif_home_penalty_lose);
        gif_guest_penalty_lose = (GifImageView) findViewById(R.id.gif_guest_penalty_lose);
        gif_home_penalty_lose_position = (GifImageView) findViewById(R.id.gif_home_penalty_lose_position);
        gif_guest_penalty_lose_position = (GifImageView) findViewById(R.id.gif_guest_penalty_lose_position);

        // 红黄牌
        rl_r_or_y_content = (RelativeLayout) findViewById(R.id.rl_r_or_y_content);
        ll_home_r_or_y_title = (LinearLayout) findViewById(R.id.ll_home_r_or_y_title);
        ll_guest_r_or_y_title = (LinearLayout) findViewById(R.id.ll_guest_r_or_y_title);
        gif_home_r_or_y_position = (GifImageView) findViewById(R.id.gif_home_r_or_y_position);
        gif_guest_r_or_y_position = (GifImageView) findViewById(R.id.gif_guest_r_or_y_position);
        tv_home_desc = (TextView) findViewById(R.id.tv_home_desc);
        tv_guest_desc = (TextView) findViewById(R.id.tv_guest_desc);
        iv_home_icon = (ImageView) findViewById(R.id.iv_home_icon);
        iv_guest_icon = (ImageView) findViewById(R.id.iv_guest_icon);
        iv_home_r_or_y = (ImageView) findViewById(R.id.iv_home_r_or_y);
        iv_guest_r_or_y = (ImageView) findViewById(R.id.iv_guest_r_or_y);

        // 任意球fk2、fk4
        fl_free_kick_fk2_fk4_content = (RelativeLayout) findViewById(R.id.fl_free_kick_fk2_fk4_content);
        ll_home_free_kick_bg = (LinearLayout) findViewById(R.id.ll_home_free_kick_bg);
        ll_guest_free_kick_bg = (LinearLayout) findViewById(R.id.ll_guest_free_kick_bg);
        ll_home_free_kick_fk4_bg = (LinearLayout) findViewById(R.id.ll_home_free_kick_fk4_bg);
        ll_guest_free_kick_fk4_bg = (LinearLayout) findViewById(R.id.ll_guest_free_kick_fk4_bg);
        ll_home_free_kick_fk2_bg = (LinearLayout) findViewById(R.id.ll_home_free_kick_fk2_bg);
        ll_guest_free_kick_fk2_bg = (LinearLayout) findViewById(R.id.ll_guest_free_kick_fk2_bg);
        ll_guest_free_kick_fk2_title = (LinearLayout) findViewById(R.id.ll_guest_free_kick_fk2_title);
        ll_home_free_kick_fk2_title = (LinearLayout) findViewById(R.id.ll_home_free_kick_fk2_title);
        ll_home_free_kick_fk4_title = (LinearLayout) findViewById(R.id.ll_home_free_kick_fk4_title);
        ll_guest_free_kick_fk4_title = (LinearLayout) findViewById(R.id.ll_guest_free_kick_fk4_title);
        tv_home_free_kick_fk4_title = (TextView) findViewById(R.id.tv_home_free_kick_fk4_title);
        tv_guest_free_kick_fk4_title = (TextView) findViewById(R.id.tv_guest_free_kick_fk4_title);
        tv_home_free_kick_fk2_title = (TextView) findViewById(R.id.tv_home_free_kick_fk2_title);
        tv_guest_free_kick_fk2_title = (TextView) findViewById(R.id.tv_guest_free_kick_fk2_title);
        tv_guest_free_kick_fk2_title_time = (TextView) findViewById(R.id.tv_guest_free_kick_fk2_title_time);
        tv_home_free_kick_fk2_title_time = (TextView) findViewById(R.id.tv_home_free_kick_fk2_title_time);
        tv_home_free_kick_fk4_title_time = (TextView) findViewById(R.id.tv_home_free_kick_fk4_title_time);
        tv_guest_free_kick_fk4_title_time = (TextView) findViewById(R.id.tv_guest_free_kick_fk4_title_time);
        gif_home_free_kick_fk2_position = (GifImageView) findViewById(R.id.gif_home_free_kick_fk2_position);
        gif_guest_free_kick_fk2_position = (GifImageView) findViewById(R.id.gif_guest_free_kick_fk2_position);
        gif_home_free_kick_fk4_position = (GifImageView) findViewById(R.id.gif_home_free_kick_fk4_position);
        gif_guest_free_kick_fk4_position = (GifImageView) findViewById(R.id.gif_guest_free_kick_fk4_position);

        // 任意球fk3 1
        fl_free_kick_fk3_content1 = (LinearLayout) findViewById(R.id.fl_free_kick_fk3_content1);
        ll_home_free_kick_fk3_title = (LinearLayout) findViewById(R.id.ll_home_free_kick_fk3_title);
        ll_guest_free_kick_fk3_title = (LinearLayout) findViewById(R.id.ll_guest_free_kick_fk3_title);
        rl_home_free_kick_fk3_1 = (RelativeLayout) findViewById(R.id.rl_home_free_kick_fk3_1);
        rl_guest_free_kick_fk3_1 = (RelativeLayout) findViewById(R.id.rl_guest_free_kick_fk3_1);
        gif_home_free_kick_fk3_position = (GifImageView) findViewById(R.id.gif_home_free_kick_fk3_position);
        gif_guest_free_kick_fk3_position = (GifImageView) findViewById(R.id.gif_guest_free_kick_fk3_position);

        // 任意球fk3 2
        fl_free_kick_fk3_content2 = (RelativeLayout) findViewById(R.id.fl_free_kick_fk3_content2);
        ll_home_free_kick_fk3_bg_2 = (LinearLayout) findViewById(R.id.ll_home_free_kick_fk3_bg_2);
        ll_guest_free_kick_fk3_bg_2 = (LinearLayout) findViewById(R.id.ll_guest_free_kick_fk3_bg_2);
        ll_home_free_kick_fk3_title_2 = (LinearLayout) findViewById(R.id.ll_home_free_kick_fk3_title_2);
        ll_guest_free_kick_fk3_title_2 = (LinearLayout) findViewById(R.id.ll_guest_free_kick_fk3_title_2);
        gif_home_free_kick_fk3_position_2 = (GifImageView) findViewById(R.id.gif_home_free_kick_fk3_position_2);
        gif_guest_free_kick_fk3_position_2 = (GifImageView) findViewById(R.id.gif_guest_free_kick_fk3_position_2);
        ll_home_free_kick_fk3_title_2_time = (TextView) findViewById(R.id.ll_home_free_kick_fk3_title_2_time);
        ll_guest_free_kick_fk3_title_time = (TextView) findViewById(R.id.ll_guest_free_kick_fk3_title_time);
        ll_home_free_kick_fk3_title_time = (TextView) findViewById(R.id.ll_home_free_kick_fk3_title_time);
        ll_guest_free_kick_fk3_title_2_time = (TextView) findViewById(R.id.ll_guest_free_kick_fk3_title_2_time);

        // 任意球fk1
        fl_free_kick_fk1_content = (RelativeLayout) findViewById(R.id.fl_free_kick_fk1_content);
        ll_home_free_kick_fk1_bg = (LinearLayout) findViewById(R.id.ll_home_free_kick_fk1_bg);
        ll_guest_free_kick_fk1_bg = (LinearLayout) findViewById(R.id.ll_guest_free_kick_fk1_bg);
        ll_home_free_kick_fk1_title = (LinearLayout) findViewById(R.id.ll_home_free_kick_fk1_title);
        ll_guest_free_kick_fk1_title = (LinearLayout) findViewById(R.id.ll_guest_free_kick_fk1_title);
        gif_home_free_kick_fk1_position = (GifImageView) findViewById(R.id.gif_home_free_kick_fk1_position);
        gif_guest_free_kick_fk1_position = (GifImageView) findViewById(R.id.gif_guest_free_kick_fk1_position);
        ll_home_free_kick_fk1_title_time = (TextView) findViewById(R.id.ll_home_free_kick_fk1_title_time);
        ll_guest_free_kick_fk1_title_time = (TextView) findViewById(R.id.ll_guest_free_kick_fk1_title_time);

        // 进球
        ll_goal_content = (LinearLayout) findViewById(R.id.ll_goal_content);
        iv_home_goal = (ImageView) findViewById(R.id.iv_home_goal);
        iv_guest_goal = (ImageView) findViewById(R.id.iv_guest_goal);
        gif_home_goal_cancel = (GifImageView) findViewById(R.id.gif_home_goal_cancel);
        gif_guest_goal_cancel = (GifImageView) findViewById(R.id.gif_guest_goal_cancel);
        tv_home_goal_title = (TextView) findViewById(R.id.tv_home_goal_title);
        tv_guest_goal_title = (TextView) findViewById(R.id.tv_guest_goal_title);

        // 欢呼
        ll_cheer_content = (LinearLayout) findViewById(R.id.ll_cheer_content);
        gif_cheer = (GifImageView) findViewById(R.id.gif_cheer);

        gif_cheer.setImageResource(R.mipmap.football_cheer);// 欢呼动画
        iv_home_goal.setImageResource(R.drawable.football_goal_animation);// 主队进球动画
        homeAnima = (AnimationDrawable) iv_home_goal.getDrawable();


        iv_guest_goal.setImageResource(R.drawable.football_goal_animation);// 客队进球动画
        guestAnima = (AnimationDrawable) iv_guest_goal.getDrawable();
        gif_home_goal_cancel.setImageResource(R.mipmap.football_cancel_goal);// 取消进球
        gif_guest_goal_cancel.setImageResource(R.mipmap.football_cancel_goal);// 取消进球



        findViewById(R.id.bt_match_start).setOnClickListener(this);
        findViewById(R.id.bt_halftime).setOnClickListener(this);
        findViewById(R.id.bt_match_stop).setOnClickListener(this);
        findViewById(R.id.bt_match_recovery).setOnClickListener(this);
        findViewById(R.id.bt_match_over).setOnClickListener(this);
        findViewById(R.id.bt_signal_off).setOnClickListener(this);
        findViewById(R.id.bt_drink_water).setOnClickListener(this);
        findViewById(R.id.bt_injured).setOnClickListener(this);
        findViewById(R.id.bt_injured_addtime).setOnClickListener(this);
        findViewById(R.id.bt_home_control).setOnClickListener(this);
        findViewById(R.id.bt_guest_control).setOnClickListener(this);
        findViewById(R.id.bt_home_attack).setOnClickListener(this);
        findViewById(R.id.bt_guest_attack).setOnClickListener(this);
        findViewById(R.id.bt_home_attack_danger).setOnClickListener(this);
        findViewById(R.id.bt_guest_attack_danger).setOnClickListener(this);
        findViewById(R.id.bt_home_deviate1).setOnClickListener(this);
        findViewById(R.id.bt_guest_deviate1).setOnClickListener(this);
        findViewById(R.id.bt_home_deviate2).setOnClickListener(this);
        findViewById(R.id.bt_guest_deviate2).setOnClickListener(this);
        findViewById(R.id.bt_home_hit1).setOnClickListener(this);
        findViewById(R.id.bt_guest_hit1).setOnClickListener(this);
        findViewById(R.id.bt_home_hit2).setOnClickListener(this);
        findViewById(R.id.bt_guest_hit2).setOnClickListener(this);
        findViewById(R.id.bt_home_goal_door).setOnClickListener(this);
        findViewById(R.id.bt_guest_goal_door).setOnClickListener(this);
        findViewById(R.id.bt_home_goal_out).setOnClickListener(this);
        findViewById(R.id.bt_guest_goal_out).setOnClickListener(this);
        findViewById(R.id.bt_home_corner_left).setOnClickListener(this);
        findViewById(R.id.bt_guest_corner_left).setOnClickListener(this);
        findViewById(R.id.bt_home_corner_right).setOnClickListener(this);
        findViewById(R.id.bt_guest_corner_right).setOnClickListener(this);
        findViewById(R.id.bt_home_offside).setOnClickListener(this);
        findViewById(R.id.bt_guest_offside).setOnClickListener(this);
        findViewById(R.id.bt_home_penalty).setOnClickListener(this);
        findViewById(R.id.bt_guest_penalty).setOnClickListener(this);
        findViewById(R.id.bt_home_penalty_lose).setOnClickListener(this);
        findViewById(R.id.bt_guest_penalty_lose).setOnClickListener(this);
        findViewById(R.id.bt_home_y).setOnClickListener(this);
        findViewById(R.id.bt_guest_y).setOnClickListener(this);
        findViewById(R.id.bt_home_r).setOnClickListener(this);
        findViewById(R.id.bt_guest_r).setOnClickListener(this);
        findViewById(R.id.bt_home_whistle).setOnClickListener(this);
        findViewById(R.id.bt_guest_whistle).setOnClickListener(this);
        findViewById(R.id.bt_home_substitution).setOnClickListener(this);
        findViewById(R.id.bt_guest_substitution).setOnClickListener(this);
        findViewById(R.id.bt_home_free_kick).setOnClickListener(this);
        findViewById(R.id.bt_guest_free_kick).setOnClickListener(this);
        findViewById(R.id.bt_home_free_kick_danger_fk1).setOnClickListener(this);
        findViewById(R.id.bt_guest_free_kick_danger_fk1).setOnClickListener(this);
        findViewById(R.id.bt_home_free_kick_danger_fk2).setOnClickListener(this);
        findViewById(R.id.bt_guest_free_kick_danger_fk2).setOnClickListener(this);
        findViewById(R.id.bt_home_free_kick_danger_fk3l).setOnClickListener(this);
        findViewById(R.id.bt_guest_free_kick_danger_fk3l).setOnClickListener(this);
        findViewById(R.id.bt_home_free_kick_danger_fk3r).setOnClickListener(this);
        findViewById(R.id.bt_guest_free_kick_danger_fk3r).setOnClickListener(this);
        findViewById(R.id.bt_home_free_kick_danger_fk4).setOnClickListener(this);
        findViewById(R.id.bt_guest_free_kick_danger_fk4).setOnClickListener(this);
        findViewById(R.id.bt_home_goal).setOnClickListener(this);
        findViewById(R.id.bt_guest_goal).setOnClickListener(this);
        findViewById(R.id.bt_home_goal_cancel).setOnClickListener(this);
        findViewById(R.id.bt_guest_goal_cancel).setOnClickListener(this);
    }

    private void initEvent() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_match_start://比赛开始  "12" || "13"
                ToastTools.showQuick(this, "比赛开始");
                gif_match_start.setImageResource(R.mipmap.football_match_start_gif);
                tv_start.setBackgroundResource(R.mipmap.football_match_start_txt);
                rl_match_txt_content.setVisibility(View.VISIBLE);
                gif_match_start.setVisibility(View.VISIBLE);
                tv_half_txt.setVisibility(View.VISIBLE);
                iv_signal_off.setVisibility(View.GONE);
                iv_drink_water.setVisibility(View.GONE);
                iv_injured.setVisibility(View.GONE);
                iv_injured_addtime.setVisibility(View.GONE);
                showGifAnimation(12);
                break;
            case R.id.bt_halftime://中场休息
                ToastTools.showQuick(this, "中场休息");
                tv_start.setBackgroundResource(R.mipmap.football_halftime_bg);
                rl_match_txt_content.setVisibility(View.VISIBLE);
                gif_match_start.setVisibility(View.GONE);
                tv_half_txt.setVisibility(View.GONE);
                iv_signal_off.setVisibility(View.GONE);
                iv_drink_water.setVisibility(View.GONE);
                iv_injured.setVisibility(View.GONE);
                iv_injured_addtime.setVisibility(View.GONE);
                showGifAnimation(1);
                break;
            case R.id.bt_match_stop://比赛暂停  (code >= 302 && code<=317) ||  code == 532 || code == 518 || code == 149
                ToastTools.showQuick(this, "比赛暂停");
                tv_start.setBackgroundResource(R.mipmap.football_match_stop_txt);
                rl_match_txt_content.setVisibility(View.VISIBLE);
                gif_match_start.setVisibility(View.GONE);
                tv_half_txt.setVisibility(View.GONE);
                iv_signal_off.setVisibility(View.GONE);
                iv_drink_water.setVisibility(View.GONE);
                iv_injured.setVisibility(View.GONE);
                iv_injured_addtime.setVisibility(View.GONE);
                showGifAnimation(532);
                break;
            case R.id.bt_match_recovery://比赛恢复 code == 517 || code == 148
                ToastTools.showQuick(this, "比赛恢复");
                tv_start.setBackgroundResource(R.mipmap.football_match_recovery_txt);
                rl_match_txt_content.setVisibility(View.VISIBLE);
                gif_match_start.setVisibility(View.GONE);
                tv_half_txt.setVisibility(View.GONE);
                iv_signal_off.setVisibility(View.GONE);
                iv_drink_water.setVisibility(View.GONE);
                iv_injured.setVisibility(View.GONE);
                iv_injured_addtime.setVisibility(View.GONE);
                showGifAnimation(517);
                break;
            case R.id.bt_match_over://比赛结束
                ToastTools.showQuick(this, "比赛结束");
                tv_start.setBackgroundResource(R.mipmap.football_match_over_txt);
                rl_match_txt_content.setVisibility(View.VISIBLE);
                gif_match_start.setVisibility(View.GONE);
                tv_half_txt.setVisibility(View.GONE);
                iv_signal_off.setVisibility(View.GONE);
                iv_drink_water.setVisibility(View.GONE);
                iv_injured.setVisibility(View.GONE);
                iv_injured_addtime.setVisibility(View.GONE);
                showGifAnimation(-1);
                break;
            case R.id.bt_signal_off://比赛直播信号中断 code == 515 || code == 516
                ToastTools.showQuick(this, "比赛直播信号中断");
                rl_match_txt_content.setVisibility(View.GONE);
                iv_signal_off.setVisibility(View.VISIBLE);
                iv_drink_water.setVisibility(View.GONE);
                iv_injured.setVisibility(View.GONE);
                iv_injured_addtime.setVisibility(View.GONE);
                showGifAnimation(515);
                break;
            case R.id.bt_drink_water://喝水
                ToastTools.showQuick(this, "喝水");
                rl_match_txt_content.setVisibility(View.GONE);
                iv_signal_off.setVisibility(View.GONE);
                iv_drink_water.setVisibility(View.VISIBLE);
                iv_injured.setVisibility(View.GONE);
                iv_injured_addtime.setVisibility(View.GONE);
                showGifAnimation(311);
                break;
            case R.id.bt_injured://受伤
                ToastTools.showQuick(this, "受伤");
                rl_match_txt_content.setVisibility(View.GONE);
                iv_signal_off.setVisibility(View.GONE);
                iv_drink_water.setVisibility(View.GONE);
                iv_injured.setVisibility(View.VISIBLE);
                iv_injured_addtime.setVisibility(View.GONE);
                showGifAnimation(132);
                break;
            case R.id.bt_injured_addtime://伤停补时
                ToastTools.showQuick(this, "伤停补时");
                rl_match_txt_content.setVisibility(View.GONE);
                iv_signal_off.setVisibility(View.GONE);
                iv_drink_water.setVisibility(View.GONE);
                iv_injured.setVisibility(View.GONE);
                iv_injured_addtime.setVisibility(View.VISIBLE);
                showGifAnimation(260);
                break;
            case R.id.bt_home_control://主队后场控球
                ToastTools.showQuick(this, "主队后场控球");
                gif_home_control.setImageResource(R.mipmap.football_home_control);
                ll_control.setVisibility(View.VISIBLE);
                rl_attack.setVisibility(View.GONE);
                rl_attack_danger.setVisibility(View.GONE);
                gif_home_control.setVisibility(View.VISIBLE);
                gif_guest_control.setVisibility(View.INVISIBLE);
                showGifAnimation(1051);
                break;
            case R.id.bt_guest_control://客队后场控球
                ToastTools.showQuick(this, "客队后场控球");
                gif_guest_control.setImageResource(R.mipmap.football_guest_control);
                ll_control.setVisibility(View.VISIBLE);
                rl_attack.setVisibility(View.GONE);
                rl_attack_danger.setVisibility(View.GONE);
                gif_home_control.setVisibility(View.INVISIBLE);
                gif_guest_control.setVisibility(View.VISIBLE);
                showGifAnimation(2075);
                break;
            case R.id.bt_home_attack://主队进攻
                ToastTools.showQuick(this, "主队进攻");
                gif_home_attack.setImageResource(R.mipmap.football_home_attack_gif);
                ll_control.setVisibility(View.GONE);
                rl_attack.setVisibility(View.VISIBLE);
                rl_attack_danger.setVisibility(View.GONE);
                gif_home_attack.setVisibility(View.VISIBLE);
                gif_guest_attack.setVisibility(View.INVISIBLE);
                showGifAnimation(1024);
                break;
            case R.id.bt_guest_attack://客队进攻
                ToastTools.showQuick(this, "客队进攻");
                gif_guest_attack.setImageResource(R.mipmap.football_guest_attack_gif);
                ll_control.setVisibility(View.GONE);
                rl_attack.setVisibility(View.VISIBLE);
                rl_attack_danger.setVisibility(View.GONE);
                gif_home_attack.setVisibility(View.INVISIBLE);
                gif_guest_attack.setVisibility(View.VISIBLE);
                showGifAnimation(2048);
                break;
            case R.id.bt_home_attack_danger://主队危险进攻
                ToastTools.showQuick(this, "主队危险进攻");
                gif_home_attack_danger.setImageResource(R.mipmap.football_home_attack_danger_gif);
                ll_control.setVisibility(View.GONE);
                rl_attack.setVisibility(View.GONE);
                rl_attack_danger.setVisibility(View.VISIBLE);
                gif_home_attack_danger.setVisibility(View.VISIBLE);
                gif_guest_attack_danger.setVisibility(View.INVISIBLE);
                showGifAnimation(1026);
                break;
            case R.id.bt_guest_attack_danger://客队危险进攻
                ToastTools.showQuick(this, "客队危险进攻");
                gif_guest_attack_danger.setImageResource(R.mipmap.football_guest_attack_danger_gif);
                ll_control.setVisibility(View.GONE);
                rl_attack.setVisibility(View.GONE);
                rl_attack_danger.setVisibility(View.VISIBLE);
                gif_home_attack_danger.setVisibility(View.INVISIBLE);
                gif_guest_attack_danger.setVisibility(View.VISIBLE);
                showGifAnimation(2050);
                break;
            case R.id.bt_home_deviate1://主队射偏1  1040
                ToastTools.showQuick(this, "主队射偏1");
                rl_home_offside1.setVisibility(View.VISIBLE);
                rl_guest_offside1.setVisibility(View.INVISIBLE);
                gif_home_position1.setVisibility(View.VISIBLE);
                gif_guest_position1.setVisibility(View.INVISIBLE);
                rl_home_offside_title1.setVisibility(View.VISIBLE);
                rl_guest_offside_title1.setVisibility(View.INVISIBLE);
                gif_home_ball1.setVisibility(View.VISIBLE);
                gif_guest_ball1.setVisibility(View.INVISIBLE);
                gif_home_hit1.setVisibility(View.INVISIBLE);
                gif_guest_hit1.setVisibility(View.INVISIBLE);
                tv_home_title1.setText(getString(R.string.football_play_deviate));
                gif_home_ball1.setImageResource(R.mipmap.football_home_ball_gif);
                gif_home_position1.setImageResource(R.mipmap.football_home_position_gif);
                tv_home_title1_time.setText("10'");
                showGifAnimation(1111);
                break;
            case R.id.bt_guest_deviate1://客队射偏1  2064
                ToastTools.showQuick(this, "客队射偏1");
//                rl_home_offside1.setVisibility(View.INVISIBLE);
//                rl_guest_offside1.setVisibility(View.VISIBLE);
//                gif_home_position1.setVisibility(View.INVISIBLE);
//                gif_guest_position1.setVisibility(View.VISIBLE);
//                rl_home_offside_title1.setVisibility(View.INVISIBLE);
//                rl_guest_offside_title1.setVisibility(View.VISIBLE);
//                gif_home_ball1.setVisibility(View.INVISIBLE);
//                gif_guest_ball1.setVisibility(View.VISIBLE);
//                gif_home_hit1.setVisibility(View.INVISIBLE);
//                gif_guest_hit1.setVisibility(View.INVISIBLE);
//                tv_guest_title1.setText(getString(R.string.football_play_deviate));
//                tv_guest_title1_time.setText("10'");
//                gif_guest_ball1.setImageResource(R.mipmap.football_guest_ball_gif);
//                gif_guest_position1.setImageResource(R.mipmap.football_guest_position_gif);
                showGifAnimation(1111);
                break;
            case R.id.bt_home_deviate2://主队射偏2  1041
                ToastTools.showQuick(this, "主队射偏2");
                rl_home_offside2.setVisibility(View.VISIBLE);
                rl_guest_offside2.setVisibility(View.INVISIBLE);
                gif_home_position2.setVisibility(View.VISIBLE);
                gif_guest_position2.setVisibility(View.INVISIBLE);
                rl_home_offside_title2.setVisibility(View.VISIBLE);
                rl_guest_offside_title2.setVisibility(View.INVISIBLE);
                gif_home_ball2.setVisibility(View.VISIBLE);
                gif_guest_ball2.setVisibility(View.INVISIBLE);
                gif_home_hit2.setVisibility(View.INVISIBLE);
                gif_guest_hit2.setVisibility(View.INVISIBLE);
                tv_home_title2.setText(getString(R.string.football_play_deviate));
                tv_home_title2_time.setText("10'");
                gif_home_ball2.setImageResource(R.mipmap.football_home_ball2_gif);
                gif_home_position2.setImageResource(R.mipmap.football_home_position_gif);
                showGifAnimation(2222);
                break;
            case R.id.bt_guest_deviate2://客队射偏2  2065
                ToastTools.showQuick(this, "客队射偏2");
                rl_home_offside2.setVisibility(View.INVISIBLE);
                rl_guest_offside2.setVisibility(View.VISIBLE);
                gif_home_position2.setVisibility(View.INVISIBLE);
                gif_guest_position2.setVisibility(View.VISIBLE);
                rl_home_offside_title2.setVisibility(View.INVISIBLE);
                rl_guest_offside_title2.setVisibility(View.VISIBLE);
                gif_home_ball2.setVisibility(View.INVISIBLE);
                gif_guest_ball2.setVisibility(View.VISIBLE);
                gif_home_hit2.setVisibility(View.INVISIBLE);
                gif_guest_hit2.setVisibility(View.INVISIBLE);
                tv_guest_title2.setText(getString(R.string.football_play_deviate));
                tv_guest_title2_time.setText("10'");
                gif_guest_ball2.setImageResource(R.mipmap.football_guest_ball2_gif);
                gif_guest_position2.setImageResource(R.mipmap.football_guest_position_gif);
                showGifAnimation(2222);
                break;
            case R.id.bt_home_hit1://主队射正1  1039
                ToastTools.showQuick(this, "主队射正1");
                rl_home_offside1.setVisibility(View.VISIBLE);
                rl_guest_offside1.setVisibility(View.INVISIBLE);
                gif_home_position1.setVisibility(View.VISIBLE);
                gif_guest_position1.setVisibility(View.INVISIBLE);
                rl_home_offside_title1.setVisibility(View.VISIBLE);
                rl_guest_offside_title1.setVisibility(View.INVISIBLE);
                gif_home_ball1.setVisibility(View.INVISIBLE);
                gif_guest_ball1.setVisibility(View.INVISIBLE);
                gif_home_hit1.setVisibility(View.VISIBLE);
                gif_guest_hit1.setVisibility(View.INVISIBLE);
                tv_home_title1.setText(getString(R.string.football_play_hit));
                tv_home_title1_time.setText("10'");
                gif_home_hit1.setImageResource(R.mipmap.football_home_hit1);
                gif_home_position1.setImageResource(R.mipmap.football_home_position_gif);
                showGifAnimation(1111);
                break;
            case R.id.bt_guest_hit1://客队射正1  2063
                ToastTools.showQuick(this, "客队射正1");
                rl_home_offside1.setVisibility(View.INVISIBLE);
                rl_guest_offside1.setVisibility(View.VISIBLE);
                gif_home_position1.setVisibility(View.INVISIBLE);
                gif_guest_position1.setVisibility(View.VISIBLE);
                rl_home_offside_title1.setVisibility(View.INVISIBLE);
                rl_guest_offside_title1.setVisibility(View.VISIBLE);
                gif_home_ball1.setVisibility(View.INVISIBLE);
                gif_guest_ball1.setVisibility(View.INVISIBLE);
                gif_home_hit1.setVisibility(View.INVISIBLE);
                gif_guest_hit1.setVisibility(View.VISIBLE);
                tv_guest_title1.setText(getString(R.string.football_play_hit));
                tv_guest_title1_time.setText("10'");
                gif_guest_hit1.setImageResource(R.mipmap.football_guest_hit1);
                gif_guest_position1.setImageResource(R.mipmap.football_guest_position_gif);
                showGifAnimation(1111);
                break;
            case R.id.bt_home_hit2://主队射正2  1039
                ToastTools.showQuick(this, "主队射正2");
                rl_home_offside2.setVisibility(View.VISIBLE);
                rl_guest_offside2.setVisibility(View.INVISIBLE);
                gif_home_position2.setVisibility(View.VISIBLE);
                gif_guest_position2.setVisibility(View.INVISIBLE);
                rl_home_offside_title2.setVisibility(View.VISIBLE);
                rl_guest_offside_title2.setVisibility(View.INVISIBLE);
                gif_home_ball2.setVisibility(View.INVISIBLE);
                gif_guest_ball2.setVisibility(View.INVISIBLE);
                gif_home_hit2.setVisibility(View.VISIBLE);
                gif_guest_hit2.setVisibility(View.INVISIBLE);
                tv_home_title2.setText(getString(R.string.football_play_hit));
                tv_home_title2_time.setText("10'");
                gif_home_hit2.setImageResource(R.mipmap.football_home_hit2);
                gif_home_position2.setImageResource(R.mipmap.football_home_position_gif);
                showGifAnimation(2222);
                break;
            case R.id.bt_guest_hit2://客队射正2  2063
                ToastTools.showQuick(this, "客队射正2");
                rl_home_offside2.setVisibility(View.INVISIBLE);
                rl_guest_offside2.setVisibility(View.VISIBLE);
                gif_home_position2.setVisibility(View.INVISIBLE);
                gif_guest_position2.setVisibility(View.VISIBLE);
                rl_home_offside_title2.setVisibility(View.INVISIBLE);
                rl_guest_offside_title2.setVisibility(View.VISIBLE);
                gif_home_ball2.setVisibility(View.INVISIBLE);
                gif_guest_ball2.setVisibility(View.INVISIBLE);
                gif_home_hit2.setVisibility(View.INVISIBLE);
                gif_guest_hit2.setVisibility(View.VISIBLE);
                tv_guest_title2.setText(getString(R.string.football_play_hit));
                tv_guest_title2_time.setText("10'");
                gif_guest_hit2.setImageResource(R.mipmap.football_guest_hit2);
                gif_guest_position2.setImageResource(R.mipmap.football_guest_position_gif);
                showGifAnimation(2222);
                break;
            case R.id.bt_home_goal_door://主队球门球
                ToastTools.showQuick(this, "主队球门球");
                rl_home_goal_door.setVisibility(View.VISIBLE);
                tv_home_goal_door_title_time.setVisibility(View.VISIBLE);
                rl_guest_goal_door.setVisibility(View.INVISIBLE);
                tv_guest_goal_door_title_time.setVisibility(View.INVISIBLE);
                gif_home_goal_door_position.setImageResource(R.mipmap.football_home_position_gif);
                gfi_home_goal_door.setImageResource(R.mipmap.football_home_goal_door);
                tv_home_goal_door_title_time.setText("10'");
                showGifAnimation(1053);
                break;
            case R.id.bt_guest_goal_door://客队球门球
                ToastTools.showQuick(this, "客队球门球");
                rl_home_goal_door.setVisibility(View.INVISIBLE);
                tv_home_goal_door_title_time.setVisibility(View.INVISIBLE);
                rl_guest_goal_door.setVisibility(View.VISIBLE);
                tv_guest_goal_door_title_time.setVisibility(View.VISIBLE);
                gif_guest_goal_door_position.setImageResource(R.mipmap.football_guest_position_gif);
                gfi_guest_goal_door.setImageResource(R.mipmap.football_guest_goal_door);
                tv_guest_goal_door_title_time.setText("10'");
                showGifAnimation(2077);
                break;
            case R.id.bt_home_goal_out://主队界外球
                ToastTools.showQuick(this, "主队界外球");
                rl_home_goal_out.setVisibility(View.VISIBLE);
                tv_home_goal_out_title_time.setVisibility(View.VISIBLE);
                rl_guest_goal_out.setVisibility(View.INVISIBLE);
                tv_guest_goal_out_title_time.setVisibility(View.INVISIBLE);
                gif_guest_goal_out.setVisibility(View.INVISIBLE);
                gif_home_goal_out_position.setImageResource(R.mipmap.football_home_position_gif);
                gif_home_goal_out.setImageResource(R.mipmap.football_home_goal_out);
                tv_home_goal_out_title_time.setText("10'");
                showGifAnimation(1054);
                break;
            case R.id.bt_guest_goal_out://客队界外球
                ToastTools.showQuick(this, "客队界外球");
                rl_home_goal_out.setVisibility(View.INVISIBLE);
                tv_home_goal_out_title_time.setVisibility(View.INVISIBLE);
                rl_guest_goal_out.setVisibility(View.VISIBLE);
                tv_guest_goal_out_title_time.setVisibility(View.VISIBLE);
                gif_guest_goal_out.setVisibility(View.VISIBLE);
                gif_guest_goal_out_position.setImageResource(R.mipmap.football_guest_position_gif);
                gif_guest_goal_out.setImageResource(R.mipmap.football_guest_goal_out);
                tv_guest_goal_out_title_time.setText("10'");
                showGifAnimation(2078);
                break;
            case R.id.bt_home_corner_left://主队角球左侧  1025
                ToastTools.showQuick(this, "主队角球左侧");
                rl_home_corner.setVisibility(View.VISIBLE);
                rl_guest_corner.setVisibility(View.INVISIBLE);
                ll_home_title_l.setVisibility(View.VISIBLE);
                ll_home_title_r.setVisibility(View.GONE);
                ll_guest_title_l.setVisibility(View.GONE);
                ll_guest_title_r.setVisibility(View.GONE);
                gif_guest_corner_r_position.setVisibility(View.GONE);
                gif_guest_corner_l_position.setVisibility(View.GONE);
                gif_home_corner_r_position.setVisibility(View.GONE);
                gif_home_corner_l_position.setVisibility(View.VISIBLE);
                gif_guest_corner_r.setVisibility(View.GONE);
                gif_guest_corner_l.setVisibility(View.GONE);
                gif_home_corner_r.setVisibility(View.GONE);
                gif_home_corner_l.setVisibility(View.VISIBLE);
                gif_home_corner_l_position.setImageResource(R.mipmap.football_home_position_gif);
                gif_home_corner_l.setImageResource(R.mipmap.football_home_corner_left);
                tv_home_corner_title_l_time.setText("10'");
                showGifAnimation(3333);
                break;
            case R.id.bt_guest_corner_left://客队角球左侧  2049
                ToastTools.showQuick(this, "客队角球左侧");
                rl_home_corner.setVisibility(View.INVISIBLE);
                rl_guest_corner.setVisibility(View.VISIBLE);
                ll_home_title_l.setVisibility(View.GONE);
                ll_home_title_r.setVisibility(View.GONE);
                ll_guest_title_l.setVisibility(View.VISIBLE);
                ll_guest_title_r.setVisibility(View.GONE);
                gif_guest_corner_r_position.setVisibility(View.GONE);
                gif_guest_corner_l_position.setVisibility(View.VISIBLE);
                gif_home_corner_r_position.setVisibility(View.GONE);
                gif_home_corner_l_position.setVisibility(View.GONE);
                gif_guest_corner_r.setVisibility(View.GONE);
                gif_guest_corner_l.setVisibility(View.VISIBLE);
                gif_home_corner_r.setVisibility(View.GONE);
                gif_home_corner_l.setVisibility(View.GONE);
                gif_guest_corner_l_position.setImageResource(R.mipmap.football_guest_position_gif);
                gif_guest_corner_l.setImageResource(R.mipmap.football_guest_corner_left);
                tv_guest_corner_title_l_time.setText("10'");
                showGifAnimation(4444);
                break;
            case R.id.bt_home_corner_right://主队角球右侧  1025
                ToastTools.showQuick(this, "主队角球右侧");
                rl_home_corner.setVisibility(View.VISIBLE);
                rl_guest_corner.setVisibility(View.INVISIBLE);
                ll_home_title_l.setVisibility(View.GONE);
                ll_home_title_r.setVisibility(View.VISIBLE);
                ll_guest_title_l.setVisibility(View.GONE);
                ll_guest_title_r.setVisibility(View.GONE);
                gif_guest_corner_r_position.setVisibility(View.GONE);
                gif_guest_corner_l_position.setVisibility(View.GONE);
                gif_home_corner_r_position.setVisibility(View.VISIBLE);
                gif_home_corner_l_position.setVisibility(View.GONE);
                gif_guest_corner_r.setVisibility(View.GONE);
                gif_guest_corner_l.setVisibility(View.GONE);
                gif_home_corner_r.setVisibility(View.VISIBLE);
                gif_home_corner_l.setVisibility(View.GONE);
                gif_home_corner_r_position.setImageResource(R.mipmap.football_home_position_gif);
                gif_home_corner_r.setImageResource(R.mipmap.football_home_corner_right);
                tv_home_corner_title_r_time.setText("10'");
                showGifAnimation(3333);
                break;
            case R.id.bt_guest_corner_right://客队角球右侧  2049
                ToastTools.showQuick(this, "客队角球右侧");
                rl_home_corner.setVisibility(View.INVISIBLE);
                rl_guest_corner.setVisibility(View.VISIBLE);
                ll_home_title_l.setVisibility(View.GONE);
                ll_home_title_r.setVisibility(View.GONE);
                ll_guest_title_l.setVisibility(View.GONE);
                ll_guest_title_r.setVisibility(View.VISIBLE);
                gif_guest_corner_r_position.setVisibility(View.VISIBLE);
                gif_guest_corner_l_position.setVisibility(View.GONE);
                gif_home_corner_r_position.setVisibility(View.GONE);
                gif_home_corner_l_position.setVisibility(View.GONE);
                gif_guest_corner_r.setVisibility(View.VISIBLE);
                gif_guest_corner_l.setVisibility(View.GONE);
                gif_home_corner_r.setVisibility(View.GONE);
                gif_home_corner_l.setVisibility(View.GONE);
                gif_guest_corner_r_position.setImageResource(R.mipmap.football_guest_position_gif);
                gif_guest_corner_r.setImageResource(R.mipmap.football_guest_corner_right);
                tv_guest_corner_title_r_time.setText("10'");
                showGifAnimation(4444);
                break;
            case R.id.bt_home_offside://主队越位
                ToastTools.showQuick(this, "主队越位");
                rl_home_offside_bg.setVisibility(View.VISIBLE);
                rl_guest_offside_bg.setVisibility(View.GONE);
                ll_home_offside_title.setVisibility(View.VISIBLE);
                ll_guest_offside_title.setVisibility(View.GONE);
                gif_home_offside_position.setVisibility(View.VISIBLE);
                gif_guest_offside_position.setVisibility(View.INVISIBLE);
                gif_home_offside.setImageResource(R.mipmap.football_home_offside_gif);
                gif_home_offside_position.setImageResource(R.mipmap.football_home_position_gif);
                showGifAnimation(1043);
                break;
            case R.id.bt_guest_offside://客队越位
                ToastTools.showQuick(this, "客队越位");
                rl_home_offside_bg.setVisibility(View.GONE);
                rl_guest_offside_bg.setVisibility(View.VISIBLE);
                ll_home_offside_title.setVisibility(View.GONE);
                ll_guest_offside_title.setVisibility(View.VISIBLE);
                gif_home_offside_position.setVisibility(View.INVISIBLE);
                gif_guest_offside_position.setVisibility(View.VISIBLE);
                gif_guest_offside.setImageResource(R.mipmap.football_guest_offside_gif);
                gif_guest_offside_position.setImageResource(R.mipmap.football_guest_position_gif);
                showGifAnimation(2067);
                break;
            case R.id.bt_home_penalty://主队点球
                ToastTools.showQuick(this, "主队点球");
                gif_home_penalty.setVisibility(View.VISIBLE);
                gif_guest_penalty.setVisibility(View.GONE);
                gif_home_penalty.setImageResource(R.mipmap.football_home_penalty_gif);
                showGifAnimation(1031);
                break;
            case R.id.bt_guest_penalty://客队点球
                ToastTools.showQuick(this, "客队点球");
                gif_home_penalty.setVisibility(View.GONE);
                gif_guest_penalty.setVisibility(View.VISIBLE);
                gif_guest_penalty.setImageResource(R.mipmap.football_guest_penalty_gif);
                showGifAnimation(2055);
                break;
            case R.id.bt_home_penalty_lose://主队点球罚失
                ToastTools.showQuick(this, "主队点球罚失");
                ll_home_penalty_lose_title.setVisibility(View.VISIBLE);
                ll_guest_penalty_lose_title.setVisibility(View.GONE);
                gif_home_penalty_lose.setVisibility(View.VISIBLE);
                gif_guest_penalty_lose.setVisibility(View.GONE);
                gif_home_penalty_lose_position.setVisibility(View.VISIBLE);
                gif_guest_penalty_lose_position.setVisibility(View.INVISIBLE);
                gif_home_penalty_lose.setImageResource(R.mipmap.football_home_penalty_lose_gif);
                gif_home_penalty_lose_position.setImageResource(R.mipmap.football_home_position_gif);
                showGifAnimation(1060);
                break;
            case R.id.bt_guest_penalty_lose://客队点球罚失 
                ToastTools.showQuick(this, "客队点球罚失");
                ll_home_penalty_lose_title.setVisibility(View.GONE);
                ll_guest_penalty_lose_title.setVisibility(View.VISIBLE);
                gif_home_penalty_lose.setVisibility(View.GONE);
                gif_guest_penalty_lose.setVisibility(View.VISIBLE);
                gif_home_penalty_lose_position.setVisibility(View.INVISIBLE);
                gif_guest_penalty_lose_position.setVisibility(View.VISIBLE);
                gif_guest_penalty_lose.setImageResource(R.mipmap.football_guest_penalty_lose_gif);
                gif_guest_penalty_lose_position.setImageResource(R.mipmap.football_guest_position_gif);
                showGifAnimation(2084);
                break;
            case R.id.bt_home_y://主队黄牌
                ToastTools.showQuick(this, "主队黄牌");
                ll_home_r_or_y_title.setVisibility(View.VISIBLE);
                ll_guest_r_or_y_title.setVisibility(View.GONE);
                gif_home_r_or_y_position.setVisibility(View.VISIBLE);
                gif_guest_r_or_y_position.setVisibility(View.INVISIBLE);
                iv_home_icon.setVisibility(View.INVISIBLE);
                iv_home_r_or_y.setVisibility(View.VISIBLE);
                iv_guest_r_or_y.setVisibility(View.GONE);
                gif_home_r_or_y_position.setImageResource(R.mipmap.football_home_position_gif);
                tv_home_desc.setText(getString(R.string.foot_event_yc));
                iv_home_r_or_y.setImageResource(R.mipmap.football_home_y);
                showGifAnimation(1034);
                break;
            case R.id.bt_guest_y://客队黄牌
                ToastTools.showQuick(this, "客队黄牌");
                ll_home_r_or_y_title.setVisibility(View.GONE);
                ll_guest_r_or_y_title.setVisibility(View.VISIBLE);
                gif_home_r_or_y_position.setVisibility(View.INVISIBLE);
                gif_guest_r_or_y_position.setVisibility(View.VISIBLE);
                iv_guest_icon.setVisibility(View.INVISIBLE);
                iv_home_r_or_y.setVisibility(View.GONE);
                iv_guest_r_or_y.setVisibility(View.VISIBLE);
                gif_guest_r_or_y_position.setImageResource(R.mipmap.football_guest_position_gif);
                tv_guest_desc.setText(getString(R.string.foot_event_yc));
                iv_guest_r_or_y.setImageResource(R.mipmap.football_guest_y);
                showGifAnimation(2058);
                break;
            case R.id.bt_home_r://主队红牌
                ToastTools.showQuick(this, "主队红牌");
                ll_home_r_or_y_title.setVisibility(View.VISIBLE);
                ll_guest_r_or_y_title.setVisibility(View.GONE);
                gif_home_r_or_y_position.setVisibility(View.VISIBLE);
                gif_guest_r_or_y_position.setVisibility(View.INVISIBLE);
                iv_home_icon.setVisibility(View.INVISIBLE);
                iv_home_r_or_y.setVisibility(View.VISIBLE);
                iv_guest_r_or_y.setVisibility(View.GONE);
                gif_home_r_or_y_position.setImageResource(R.mipmap.football_home_position_gif);
                tv_home_desc.setText(getString(R.string.foot_event_rc));
                iv_home_r_or_y.setImageResource(R.mipmap.football_home_r);
                showGifAnimation(1032);
                break;
            case R.id.bt_guest_r://客队红牌
                ToastTools.showQuick(this, "客队红牌");
                ll_home_r_or_y_title.setVisibility(View.GONE);
                ll_guest_r_or_y_title.setVisibility(View.VISIBLE);
                gif_home_r_or_y_position.setVisibility(View.INVISIBLE);
                gif_guest_r_or_y_position.setVisibility(View.VISIBLE);
                iv_guest_icon.setVisibility(View.INVISIBLE);
                iv_home_r_or_y.setVisibility(View.GONE);
                iv_guest_r_or_y.setVisibility(View.VISIBLE);
                gif_guest_r_or_y_position.setImageResource(R.mipmap.football_guest_position_gif);
                tv_guest_desc.setText(getString(R.string.foot_event_rc));
                iv_guest_r_or_y.setImageResource(R.mipmap.football_guest_r);
                showGifAnimation(2056);
                break;
            case R.id.bt_home_whistle://主队哨子
                ToastTools.showQuick(this, "主队哨子");
                ll_home_r_or_y_title.setVisibility(View.VISIBLE);
                ll_guest_r_or_y_title.setVisibility(View.GONE);
                gif_home_r_or_y_position.setVisibility(View.VISIBLE);
                gif_guest_r_or_y_position.setVisibility(View.INVISIBLE);
                iv_home_icon.setVisibility(View.VISIBLE);
                iv_home_r_or_y.setVisibility(View.GONE);
                iv_guest_r_or_y.setVisibility(View.GONE);
                gif_home_r_or_y_position.setImageResource(R.mipmap.football_home_position_gif);
                tv_home_desc.setText(getString(R.string.football_play_whistle));
                iv_home_icon.setImageResource(R.mipmap.football_foul_whistle_left);
                showGifAnimation(1042);
                break;
            case R.id.bt_guest_whistle://客队哨子
                ToastTools.showQuick(this, "客队哨子");
                ll_home_r_or_y_title.setVisibility(View.GONE);
                ll_guest_r_or_y_title.setVisibility(View.VISIBLE);
                gif_home_r_or_y_position.setVisibility(View.INVISIBLE);
                gif_guest_r_or_y_position.setVisibility(View.VISIBLE);
                iv_guest_icon.setVisibility(View.VISIBLE);
                iv_home_r_or_y.setVisibility(View.GONE);
                iv_guest_r_or_y.setVisibility(View.GONE);
                gif_guest_r_or_y_position.setImageResource(R.mipmap.football_guest_position_gif);
                tv_guest_desc.setText(getString(R.string.football_play_whistle));
                iv_guest_icon.setImageResource(R.mipmap.football_foul_whistle_right);
                showGifAnimation(2066);
                break;
            case R.id.bt_home_substitution://主队换人
                ToastTools.showQuick(this, "主队换人");
                ll_home_r_or_y_title.setVisibility(View.VISIBLE);
                ll_guest_r_or_y_title.setVisibility(View.GONE);
                gif_home_r_or_y_position.setVisibility(View.VISIBLE);
                gif_guest_r_or_y_position.setVisibility(View.INVISIBLE);
                iv_home_icon.setVisibility(View.VISIBLE);
                iv_home_r_or_y.setVisibility(View.GONE);
                iv_guest_r_or_y.setVisibility(View.GONE);
                gif_home_r_or_y_position.setImageResource(R.mipmap.football_home_position_gif);
                tv_home_desc.setText(getString(R.string.foot_event_player));
                iv_home_icon.setImageResource(R.mipmap.football_home_substitution);
                showGifAnimation(1055);
                break;
            case R.id.bt_guest_substitution://客队换人
                ToastTools.showQuick(this, "客队换人");
                ll_home_r_or_y_title.setVisibility(View.GONE);
                ll_guest_r_or_y_title.setVisibility(View.VISIBLE);
                gif_home_r_or_y_position.setVisibility(View.INVISIBLE);
                gif_guest_r_or_y_position.setVisibility(View.VISIBLE);
                iv_guest_icon.setVisibility(View.VISIBLE);
                iv_home_r_or_y.setVisibility(View.GONE);
                iv_guest_r_or_y.setVisibility(View.GONE);
                gif_guest_r_or_y_position.setImageResource(R.mipmap.football_guest_position_gif);
                tv_guest_desc.setText(getString(R.string.foot_event_player));
                iv_guest_icon.setImageResource(R.mipmap.football_guest_substitution);
                showGifAnimation(2079);
                break;
            case R.id.bt_home_free_kick://主队任意球  1028
                ToastTools.showQuick(this, "主队任意球");
                ll_home_free_kick_bg.setVisibility(View.VISIBLE);
                ll_guest_free_kick_bg.setVisibility(View.GONE);
                ll_home_free_kick_fk4_bg.setVisibility(View.GONE);
                ll_guest_free_kick_fk4_bg.setVisibility(View.GONE);
                ll_home_free_kick_fk2_bg.setVisibility(View.GONE);
                ll_guest_free_kick_fk2_bg.setVisibility(View.GONE);
                gif_home_free_kick_fk2_position.setVisibility(View.GONE);
                gif_home_free_kick_fk4_position.setVisibility(View.GONE);
                gif_guest_free_kick_fk4_position.setVisibility(View.GONE);
                gif_guest_free_kick_fk2_position.setVisibility(View.VISIBLE);
                gif_guest_free_kick_fk2_position.setImageResource(R.mipmap.football_home_position_gif);
                ll_guest_free_kick_fk2_title.setVisibility(View.VISIBLE);
                ll_home_free_kick_fk2_title.setVisibility(View.INVISIBLE);
                ll_home_free_kick_fk4_title.setVisibility(View.INVISIBLE);
                ll_guest_free_kick_fk4_title.setVisibility(View.INVISIBLE);
                tv_guest_free_kick_fk2_title.setText(getString(R.string.football_play_free_kick));
                tv_guest_free_kick_fk2_title_time.setText("10'");
                showGifAnimation(5555);
                break;
            case R.id.bt_guest_free_kick://客队任意球  2052
                ToastTools.showQuick(this, "客队任意球");
                ll_home_free_kick_bg.setVisibility(View.GONE);
                ll_guest_free_kick_bg.setVisibility(View.VISIBLE);
                ll_home_free_kick_fk4_bg.setVisibility(View.GONE);
                ll_guest_free_kick_fk4_bg.setVisibility(View.GONE);
                ll_home_free_kick_fk2_bg.setVisibility(View.GONE);
                ll_guest_free_kick_fk2_bg.setVisibility(View.GONE);
                gif_home_free_kick_fk2_position.setVisibility(View.VISIBLE);
                gif_home_free_kick_fk4_position.setVisibility(View.GONE);
                gif_guest_free_kick_fk4_position.setVisibility(View.GONE);
                gif_guest_free_kick_fk2_position.setVisibility(View.GONE);
                gif_home_free_kick_fk2_position.setImageResource(R.mipmap.football_guest_position_gif);
                ll_guest_free_kick_fk2_title.setVisibility(View.INVISIBLE);
                ll_home_free_kick_fk2_title.setVisibility(View.VISIBLE);
                ll_home_free_kick_fk4_title.setVisibility(View.INVISIBLE);
                ll_guest_free_kick_fk4_title.setVisibility(View.INVISIBLE);
                tv_home_free_kick_fk2_title.setText(getString(R.string.football_play_free_kick));
                tv_home_free_kick_fk2_title_time.setText("10'");
                showGifAnimation(5555);
                break;
            case R.id.bt_home_free_kick_danger_fk1://主队任危险任意球FK1  1027
                ToastTools.showQuick(this, "主队任危险任意球FK1");
                ll_home_free_kick_fk1_bg.setVisibility(View.VISIBLE);
                ll_guest_free_kick_fk1_bg.setVisibility(View.INVISIBLE);
                ll_home_free_kick_fk1_title.setVisibility(View.VISIBLE);
                ll_home_free_kick_fk1_title_time.setVisibility(View.VISIBLE);
                ll_guest_free_kick_fk1_title.setVisibility(View.GONE);
                ll_guest_free_kick_fk1_title_time.setVisibility(View.GONE);
                gif_home_free_kick_fk1_position.setVisibility(View.VISIBLE);
                gif_guest_free_kick_fk1_position.setVisibility(View.GONE);
                gif_home_free_kick_fk1_position.setImageResource(R.mipmap.football_home_position_gif);
                ll_home_free_kick_fk1_title_time.setText("10'");
                showGifAnimation(6666);
                break;
            case R.id.bt_guest_free_kick_danger_fk1://客队危险任意球FK1  2051
                ToastTools.showQuick(this, "客队危险任意球FK1");
                ll_home_free_kick_fk1_bg.setVisibility(View.INVISIBLE);
                ll_guest_free_kick_fk1_bg.setVisibility(View.VISIBLE);
                ll_home_free_kick_fk1_title.setVisibility(View.GONE);
                ll_home_free_kick_fk1_title_time.setVisibility(View.GONE);
                ll_guest_free_kick_fk1_title.setVisibility(View.VISIBLE);
                ll_guest_free_kick_fk1_title_time.setVisibility(View.VISIBLE);
                gif_home_free_kick_fk1_position.setVisibility(View.GONE);
                gif_guest_free_kick_fk1_position.setVisibility(View.VISIBLE);
                gif_guest_free_kick_fk1_position.setImageResource(R.mipmap.football_guest_position_gif);
                ll_guest_free_kick_fk1_title_time.setText("10'");
                showGifAnimation(6666);
                break;
            case R.id.bt_home_free_kick_danger_fk2://主队任危险任意球FK2
                ToastTools.showQuick(this, "主队任危险任意球FK2");
                ll_home_free_kick_bg.setVisibility(View.GONE);
                ll_guest_free_kick_bg.setVisibility(View.GONE);
                ll_home_free_kick_fk4_bg.setVisibility(View.GONE);
                ll_guest_free_kick_fk4_bg.setVisibility(View.GONE);
                ll_home_free_kick_fk2_bg.setVisibility(View.VISIBLE);
                ll_guest_free_kick_fk2_bg.setVisibility(View.GONE);
                gif_home_free_kick_fk2_position.setVisibility(View.GONE);
                gif_home_free_kick_fk4_position.setVisibility(View.VISIBLE);
                gif_guest_free_kick_fk4_position.setVisibility(View.GONE);
                gif_guest_free_kick_fk2_position.setVisibility(View.GONE);
                gif_home_free_kick_fk4_position.setImageResource(R.mipmap.football_home_position_gif);
                ll_guest_free_kick_fk2_title.setVisibility(View.INVISIBLE);
                ll_home_free_kick_fk2_title.setVisibility(View.INVISIBLE);
                ll_home_free_kick_fk4_title.setVisibility(View.VISIBLE);
                ll_guest_free_kick_fk4_title.setVisibility(View.INVISIBLE);
                tv_home_free_kick_fk4_title_time.setText("10'");
                tv_home_free_kick_fk4_title.setText(getString(R.string.football_play_free_kick_danger));
                showGifAnimation(7777);
                break;
            case R.id.bt_guest_free_kick_danger_fk2://客队危险任意球FK2
                ToastTools.showQuick(this, "客队危险任意球FK2");
                ll_home_free_kick_bg.setVisibility(View.GONE);
                ll_guest_free_kick_bg.setVisibility(View.GONE);
                ll_home_free_kick_fk4_bg.setVisibility(View.GONE);
                ll_guest_free_kick_fk4_bg.setVisibility(View.GONE);
                ll_home_free_kick_fk2_bg.setVisibility(View.GONE);
                ll_guest_free_kick_fk2_bg.setVisibility(View.VISIBLE);
                gif_home_free_kick_fk2_position.setVisibility(View.GONE);
                gif_home_free_kick_fk4_position.setVisibility(View.GONE);
                gif_guest_free_kick_fk4_position.setVisibility(View.VISIBLE);
                gif_guest_free_kick_fk2_position.setVisibility(View.GONE);
                gif_guest_free_kick_fk4_position.setImageResource(R.mipmap.football_guest_position_gif);
                ll_guest_free_kick_fk2_title.setVisibility(View.INVISIBLE);
                ll_home_free_kick_fk2_title.setVisibility(View.INVISIBLE);
                ll_home_free_kick_fk4_title.setVisibility(View.INVISIBLE);
                ll_guest_free_kick_fk4_title.setVisibility(View.VISIBLE);
                tv_guest_free_kick_fk4_title_time.setText("10'");
                tv_guest_free_kick_fk4_title.setText(getString(R.string.football_play_free_kick_danger));
                showGifAnimation(7777);
                break;
            case R.id.bt_home_free_kick_danger_fk3l://主队任危险任意球FK3L
                ToastTools.showQuick(this, "主队任危险任意球FK3L");
                ll_home_free_kick_fk3_bg_2.setVisibility(View.VISIBLE);
                ll_guest_free_kick_fk3_bg_2.setVisibility(View.INVISIBLE);
                ll_home_free_kick_fk3_title_2.setVisibility(View.VISIBLE);
                ll_guest_free_kick_fk3_title_2.setVisibility(View.INVISIBLE);
                gif_home_free_kick_fk3_position_2.setVisibility(View.VISIBLE);
                gif_guest_free_kick_fk3_position_2.setVisibility(View.INVISIBLE);
                gif_home_free_kick_fk3_position_2.setImageResource(R.mipmap.football_home_position_gif);
                ll_home_free_kick_fk3_title_2_time.setText("10'");
                showGifAnimation(8888);
                break;
            case R.id.bt_guest_free_kick_danger_fk3l://客队危险任意球FK3L
                ToastTools.showQuick(this, "客队危险任意球FK3L");
                rl_home_free_kick_fk3_1.setVisibility(View.INVISIBLE);
                ll_home_free_kick_fk3_title.setVisibility(View.INVISIBLE);
                rl_guest_free_kick_fk3_1.setVisibility(View.VISIBLE);
                ll_guest_free_kick_fk3_title.setVisibility(View.VISIBLE);
                gif_guest_free_kick_fk3_position.setImageResource(R.mipmap.football_guest_position_gif);
                ll_guest_free_kick_fk3_title_time.setText("10'");
                showGifAnimation(9999);
                break;
            case R.id.bt_home_free_kick_danger_fk3r://主队任危险任意球FK3R
                ToastTools.showQuick(this, "主队任危险任意球FK3R");
                rl_home_free_kick_fk3_1.setVisibility(View.VISIBLE);
                ll_home_free_kick_fk3_title.setVisibility(View.VISIBLE);
                rl_guest_free_kick_fk3_1.setVisibility(View.INVISIBLE);
                ll_guest_free_kick_fk3_title.setVisibility(View.INVISIBLE);
                gif_home_free_kick_fk3_position.setImageResource(R.mipmap.football_home_position_gif);
                ll_home_free_kick_fk3_title_time.setText("10'");
                showGifAnimation(9999);
                break;
            case R.id.bt_guest_free_kick_danger_fk3r://客队危险任意球FK3R
                ToastTools.showQuick(this, "客队危险任意球FK3R");
                ll_home_free_kick_fk3_bg_2.setVisibility(View.INVISIBLE);
                ll_guest_free_kick_fk3_bg_2.setVisibility(View.VISIBLE);
                ll_home_free_kick_fk3_title_2.setVisibility(View.INVISIBLE);
                ll_guest_free_kick_fk3_title_2.setVisibility(View.VISIBLE);
                gif_home_free_kick_fk3_position_2.setVisibility(View.INVISIBLE);
                gif_guest_free_kick_fk3_position_2.setVisibility(View.VISIBLE);
                gif_guest_free_kick_fk3_position_2.setImageResource(R.mipmap.football_guest_position_gif);
                ll_guest_free_kick_fk3_title_2_time.setText("10'");
                showGifAnimation(8888);
                break;
            case R.id.bt_home_free_kick_danger_fk4://主队任危险任意球FK4
                ToastTools.showQuick(this, "主队任危险任意球FK4");
                ll_home_free_kick_bg.setVisibility(View.GONE);
                ll_guest_free_kick_bg.setVisibility(View.GONE);
                ll_home_free_kick_fk4_bg.setVisibility(View.VISIBLE);
                ll_guest_free_kick_fk4_bg.setVisibility(View.GONE);
                ll_home_free_kick_fk2_bg.setVisibility(View.GONE);
                ll_guest_free_kick_fk2_bg.setVisibility(View.GONE);
                gif_home_free_kick_fk2_position.setVisibility(View.VISIBLE);
                gif_home_free_kick_fk4_position.setVisibility(View.GONE);
                gif_guest_free_kick_fk4_position.setVisibility(View.GONE);
                gif_guest_free_kick_fk2_position.setVisibility(View.GONE);
                gif_home_free_kick_fk2_position.setImageResource(R.mipmap.football_home_position_gif);
                ll_guest_free_kick_fk2_title.setVisibility(View.INVISIBLE);
                ll_home_free_kick_fk2_title.setVisibility(View.VISIBLE);
                ll_home_free_kick_fk4_title.setVisibility(View.INVISIBLE);
                ll_guest_free_kick_fk4_title.setVisibility(View.INVISIBLE);
                tv_home_free_kick_fk2_title.setText(getString(R.string.football_play_free_kick_danger));
                tv_home_free_kick_fk2_title_time.setText("10'");
                showGifAnimation(-9999);
                break;
            case R.id.bt_guest_free_kick_danger_fk4://客队危险任意球FK4
                ToastTools.showQuick(this, "客队危险任意球FK4");
                ll_home_free_kick_bg.setVisibility(View.GONE);
                ll_guest_free_kick_bg.setVisibility(View.GONE);
                ll_home_free_kick_fk4_bg.setVisibility(View.GONE);
                ll_guest_free_kick_fk4_bg.setVisibility(View.VISIBLE);
                ll_home_free_kick_fk2_bg.setVisibility(View.GONE);
                ll_guest_free_kick_fk2_bg.setVisibility(View.GONE);
                gif_home_free_kick_fk2_position.setVisibility(View.GONE);
                gif_home_free_kick_fk4_position.setVisibility(View.GONE);
                gif_guest_free_kick_fk4_position.setVisibility(View.GONE);
                gif_guest_free_kick_fk2_position.setVisibility(View.VISIBLE);
                gif_guest_free_kick_fk2_position.setImageResource(R.mipmap.football_guest_position_gif);
                ll_guest_free_kick_fk2_title.setVisibility(View.VISIBLE);
                ll_home_free_kick_fk2_title.setVisibility(View.INVISIBLE);
                ll_home_free_kick_fk4_title.setVisibility(View.INVISIBLE);
                ll_guest_free_kick_fk4_title.setVisibility(View.INVISIBLE);
                tv_guest_free_kick_fk2_title.setText(getString(R.string.football_play_free_kick_danger));
                tv_guest_free_kick_fk2_title_time.setText("10'");
                showGifAnimation(-9999);
                break;
            case R.id.bt_home_goal://主队进球  1029
                ToastTools.showQuick(this, "主队进球");
                iv_home_goal.setVisibility(View.VISIBLE);
                iv_guest_goal.setVisibility(View.GONE);
                gif_home_goal_cancel.setVisibility(View.GONE);
                gif_guest_goal_cancel.setVisibility(View.GONE);
                tv_home_goal_title.setVisibility(View.VISIBLE);
                tv_guest_goal_title.setVisibility(View.GONE);
                tv_home_goal_title.setText("进球");
                homeAnima.stop();
                homeAnima.start();
                guestAnima.stop();
                showGifAnimation(1029);
                break;
            case R.id.bt_guest_goal://客队进球  2053
                ToastTools.showQuick(this, "客队进球");
                iv_home_goal.setVisibility(View.GONE);
                iv_guest_goal.setVisibility(View.VISIBLE);
                gif_home_goal_cancel.setVisibility(View.GONE);
                gif_guest_goal_cancel.setVisibility(View.GONE);
                tv_home_goal_title.setVisibility(View.GONE);
                tv_guest_goal_title.setVisibility(View.VISIBLE);
                tv_guest_goal_title.setText("进球");
                guestAnima.stop();
                guestAnima.start();
                homeAnima.stop();
                showGifAnimation(2053);
                break;
            case R.id.bt_home_goal_cancel:// 主队取消进球  1030
                iv_home_goal.setVisibility(View.GONE);
                iv_guest_goal.setVisibility(View.GONE);
                gif_home_goal_cancel.setVisibility(View.VISIBLE);
                gif_guest_goal_cancel.setVisibility(View.GONE);
                tv_home_goal_title.setVisibility(View.VISIBLE);
                tv_guest_goal_title.setVisibility(View.GONE);
                tv_home_goal_title.setText("取消进球");
                showGifAnimation(1030);
                break;
            case R.id.bt_guest_goal_cancel:// 客队取消进球
                iv_home_goal.setVisibility(View.GONE);
                iv_guest_goal.setVisibility(View.GONE);
                gif_home_goal_cancel.setVisibility(View.GONE);
                gif_guest_goal_cancel.setVisibility(View.VISIBLE);
                tv_home_goal_title.setVisibility(View.GONE);
                tv_guest_goal_title.setVisibility(View.VISIBLE);
                tv_guest_goal_title.setText("取消进球");
                showGifAnimation(2054);
                break;
            default:
                break;
        }
    }

    AnimationDrawable homeAnima;
    AnimationDrawable guestAnima;

    private void showGifAnimation(int type) {

        // 比赛开始、信号中断、喝水、受伤、伤停补时
        ll_match_start_content.setVisibility((12 == type || 13 == type || // 比赛开始
                1 == type || //中场休息
                (type >= 302 && type <= 317) || type == 532 || type == 518 || type == 149 || //比赛暂停
                517 == type || 148 == type || // 比赛恢复
                -1 == type ||// 比赛结束
                515 == type || 516 == type || // 信号中断
                311 == type || // 喝水
                132 == type || // 受伤
                260 == type // 伤停补时
        ) ? View.VISIBLE : View.GONE);

        // 后场控球、进攻、危险进攻
        ll_control_content.setVisibility((1051 == type || // 主队后场控球
                2075 == type || // 客队后场控球
                1024 == type || //主队进攻
                2048 == type || //客队进攻
                1026 == type || //主队危险进攻
                2050 == type //客队危险进攻
        ) ? View.VISIBLE : View.GONE);

        // 射正、射偏1
        ll_offside_content1.setVisibility(1111 == type ? View.VISIBLE : View.GONE);

        // 射正、射偏2
        ll_offside_content2.setVisibility(2222 == type ? View.VISIBLE : View.GONE);

        // 球门球
        ll_goal_door_content.setVisibility((1053 == type || 2077 == type) ? View.VISIBLE : View.GONE);

        // 界外球
        rl_goal_out_content.setVisibility((1054 == type || 2078 == type) ? View.VISIBLE : View.GONE);

        // 角球
        rl_corner_content.setVisibility((3333 == type || 4444 == type) ? View.VISIBLE : View.GONE);

        // 越位
        rl_offside_content.setVisibility((1043 == type || 2067 == type) ? View.VISIBLE : View.GONE);

        // 点球
        ll_penalty_content.setVisibility((1031 == type || 2055 == type) ? View.VISIBLE : View.GONE);

        // 点球罚失
        rl_penalty_lose_content.setVisibility((1060 == type || 2084 == type) ? View.VISIBLE : View.GONE);

        // 红黄牌
        rl_r_or_y_content.setVisibility((1042 == type || 2066 == type || // 犯规
                1032 == type || 2056 == type || // 红牌
                1034 == type || 2058 == type || // 黄牌
                1055 == type || 2079 == type // 换人
        ) ? View.VISIBLE : View.GONE);

        // 任意球fk2、fk4
        fl_free_kick_fk2_fk4_content.setVisibility((5555 == type || 7777 == type || -9999 == type) ? View.VISIBLE : View.GONE);

        // 任意球fk3 1
        fl_free_kick_fk3_content1.setVisibility(9999 == type ? View.VISIBLE : View.GONE);

        // 任意球fk3 2
        fl_free_kick_fk3_content2.setVisibility(8888 == type ? View.VISIBLE : View.GONE);

        // 任意球fk1
        fl_free_kick_fk1_content.setVisibility(6666 == type ? View.VISIBLE : View.GONE);

        // 进球
        ll_goal_content.setVisibility((1029 == type || 2053 == type || 1030 == type || 2054 == type) ? View.VISIBLE : View.GONE);

        // 欢呼
        ll_cheer_content.setVisibility((1029 == type || 2053 == type) ? View.VISIBLE : View.GONE);
    }
}
