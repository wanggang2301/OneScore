package com.hhly.mlottery.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.ToastTools;

import pl.droidsonroids.gif.GifImageView;

/**
 * desc:足球事件动画直播
 * Created by 107_tangrr on 2017/5/29 0029.
 */

public class FootballEventAnimationFragment extends Activity implements View.OnClickListener {

    private RelativeLayout fl_animation_content;

    // 比赛状态显示控件
    private RelativeLayout rl_match_txt_content;
    private GifImageView gif_match_start;
    private TextView tv_start;
    private TextView tv_half_txt;
    private ImageView iv_match_txt_bg;
    // 信号中断
    private ImageView iv_signal_off;
    // 喝水
    private ImageView iv_drink_water;
    // 受伤
    private ImageView iv_injured;
    // 伤停补时
    private ImageView iv_injured_addtime;
    // 主队后场控球
    private GifImageView gif_home_control;
    // 客队后场控球
    private GifImageView gif_guest_control;
    // 主队进攻
    private GifImageView gif_home_attack;
    // 客队进攻
    private GifImageView gif_guest_attack;
    // 主队危险进攻
    private GifImageView gif_home_attack_danger;
    // 客队危险进攻
    private GifImageView gif_guest_attack_danger;
    // 主队射偏1
    private RelativeLayout rl_home_deviate1;
    private GifImageView gif_home_ball1;
    private GifImageView gif_home_position1;
    private GifImageView gif_guest_ball1;
    private GifImageView gif_guest_position1;
    private GifImageView gif_home_ball2;
    private GifImageView gif_home_position2;
    private GifImageView gif_guest_ball2;
    private GifImageView gif_guest_position2;
    private GifImageView gif_guest_hit1;
    private GifImageView gif_guest_hit2;
    private GifImageView gif_home_hit1;
    private GifImageView gif_home_hit2;
    private GifImageView gif_home_goal_door_position;
    private GifImageView gfi_home_goal_door;
    private GifImageView gif_guest_goal_door_position;
    private GifImageView gfi_guest_goal_door;
    private GifImageView gif_home_goal_out_position;
    private GifImageView gif_guest_goal_out_position;
    private GifImageView gif_home_goal_out;
    private GifImageView gif_guest_goal_out;

    private GifImageView gif_guest_corner_r_position;
    private GifImageView gif_guest_corner_l_position;
    private GifImageView gif_home_corner_r_position;
    private GifImageView gif_home_corner_l_position;
    private GifImageView gif_guest_corner_r;
    private GifImageView gif_guest_corner_l;
    private GifImageView gif_home_corner_r;
    private GifImageView gif_home_corner_l;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.football_event_animation_fragment);

        initView();
        initEvent();


//        gif_home_ball1.setImageResource(R.mipmap.football_home_ball_gif);
        gif_home_position1.setImageResource(R.mipmap.football_home_position_gif);
        gif_guest_ball1.setImageResource(R.mipmap.football_guest_ball_gif);
        gif_guest_position1.setImageResource(R.mipmap.football_guest_position_gif);

        gif_home_ball2.setImageResource(R.mipmap.football_home_ball2_gif);
        gif_home_position2.setImageResource(R.mipmap.football_home_position_gif);
        gif_guest_ball2.setImageResource(R.mipmap.football_guest_ball2_gif);
        gif_guest_position2.setImageResource(R.mipmap.football_guest_position_gif);

        gif_guest_hit1.setImageResource(R.mipmap.football_guest_hit1);
        gif_guest_hit2.setImageResource(R.mipmap.football_guest_hit2);
        gif_home_hit1.setImageResource(R.mipmap.football_home_hit1);
        gif_home_hit2.setImageResource(R.mipmap.football_home_hit2);

        gif_home_goal_door_position.setImageResource(R.mipmap.football_home_position_gif);
        gfi_home_goal_door.setImageResource(R.mipmap.football_home_goal_door);
        gif_guest_goal_door_position.setImageResource(R.mipmap.football_guest_position_gif);
        gfi_guest_goal_door.setImageResource(R.mipmap.football_guest_goal_door);

        gif_home_goal_out_position.setImageResource(R.mipmap.football_home_position_gif);
        gif_guest_goal_out_position.setImageResource(R.mipmap.football_guest_position_gif);
        gif_home_goal_out.setImageResource(R.mipmap.football_home_goal_out);
        gif_guest_goal_out.setImageResource(R.mipmap.football_guest_goal_out);

        gif_guest_corner_r_position.setImageResource(R.mipmap.football_guest_position_gif);
        gif_guest_corner_l_position.setImageResource(R.mipmap.football_guest_position_gif);
        gif_guest_corner_r.setImageResource(R.mipmap.football_guest_corner_right);
        gif_guest_corner_l.setImageResource(R.mipmap.football_guest_corner_left);

        gif_home_corner_r_position.setImageResource(R.mipmap.football_home_position_gif);
        gif_home_corner_l_position.setImageResource(R.mipmap.football_home_position_gif);
        gif_home_corner_r.setImageResource(R.mipmap.football_home_corner_right);
        gif_home_corner_l.setImageResource(R.mipmap.football_home_corner_left);
    }

    private void initView() {
        fl_animation_content = (RelativeLayout) findViewById(R.id.fl_animation_content);

        // 比赛开始
        rl_match_txt_content = (RelativeLayout) findViewById(R.id.rl_match_txt_content);
        gif_match_start = (GifImageView) findViewById(R.id.gif_match_start);
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_half_txt = (TextView) findViewById(R.id.tv_half_txt);
        iv_match_txt_bg = (ImageView) findViewById(R.id.iv_match_txt_bg);
        // 信号中断
        iv_signal_off = (ImageView) findViewById(R.id.iv_signal_off);
        // 喝水
        iv_drink_water = (ImageView) findViewById(R.id.iv_drink_water);
        // 受伤
        iv_injured = (ImageView) findViewById(R.id.iv_injured);
        // 伤停补时
        iv_injured_addtime = (ImageView) findViewById(R.id.iv_injured_addtime);
        // 主队后场控球
//        gif_home_control = (GifImageView) findViewById(R.id.gif_home_control);
//        // 客队后场控球
//        gif_guest_control = (GifImageView) findViewById(R.id.gif_guest_control);
//        // 主队进攻
//        gif_home_attack = (GifImageView) findViewById(R.id.gif_home_attack);
//        // 客队进攻
//        gif_guest_attack = (GifImageView) findViewById(R.id.gif_guest_attack);
//        // 主队危险进攻
//        gif_home_attack_danger = (GifImageView) findViewById(R.id.gif_home_attack_danger);
//        // 客队危险进攻
//        gif_guest_attack_danger = (GifImageView) findViewById(R.id.gif_guest_attack_danger);
//        // 主队射偏
//        rl_home_deviate1 = (RelativeLayout) findViewById(R.id.rl_home_deviate1);
//        gif_home_ball1 = (GifImageView) findViewById(R.id.gif_home_ball1);
        gif_home_position1 = (GifImageView) findViewById(R.id.gif_home_position1);
        gif_guest_ball1 = (GifImageView) findViewById(R.id.gif_guest_ball1);
        gif_guest_position1 = (GifImageView) findViewById(R.id.gif_guest_position1);
        gif_home_ball2 = (GifImageView) findViewById(R.id.gif_home_ball2);
        gif_home_position2 = (GifImageView) findViewById(R.id.gif_home_position2);
        gif_guest_ball2 = (GifImageView) findViewById(R.id.gif_guest_ball2);
        gif_guest_position2 = (GifImageView) findViewById(R.id.gif_guest_position2);

        gif_guest_hit1 = (GifImageView) findViewById(R.id.gif_guest_hit1);
        gif_guest_hit2 = (GifImageView) findViewById(R.id.gif_guest_hit2);
        gif_home_hit1 = (GifImageView) findViewById(R.id.gif_home_hit1);
        gif_home_hit2 = (GifImageView) findViewById(R.id.gif_home_hit2);

        gif_home_goal_door_position = (GifImageView) findViewById(R.id.gif_home_goal_door_position);
        gfi_home_goal_door = (GifImageView) findViewById(R.id.gfi_home_goal_door);
        gif_guest_goal_door_position = (GifImageView) findViewById(R.id.gif_guest_goal_door_position);
        gfi_guest_goal_door = (GifImageView) findViewById(R.id.gfi_guest_goal_door);
        gif_home_goal_out_position = (GifImageView) findViewById(R.id.gif_home_goal_out_position);
        gif_guest_goal_out_position = (GifImageView) findViewById(R.id.gif_guest_goal_out_position);
        gif_home_goal_out = (GifImageView) findViewById(R.id.gif_home_goal_out);
        gif_guest_goal_out = (GifImageView) findViewById(R.id.gif_guest_goal_out);

        gif_guest_corner_r_position = (GifImageView) findViewById(R.id.gif_guest_corner_r_position);
        gif_guest_corner_l_position = (GifImageView) findViewById(R.id.gif_guest_corner_l_position);
        gif_guest_corner_r = (GifImageView) findViewById(R.id.gif_guest_corner_r);
        gif_guest_corner_l = (GifImageView) findViewById(R.id.gif_guest_corner_l);

        gif_home_corner_r_position = (GifImageView) findViewById(R.id.gif_home_corner_r_position);
        gif_home_corner_l_position = (GifImageView) findViewById(R.id.gif_home_corner_l_position);
        gif_home_corner_r = (GifImageView) findViewById(R.id.gif_home_corner_r);
        gif_home_corner_l = (GifImageView) findViewById(R.id.gif_home_corner_l);


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
        findViewById(R.id.bt_cheer).setOnClickListener(this);
    }

    private void initEvent() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_match_start://比赛开始
                ToastTools.showQuick(this, "比赛开始");
                gif_match_start.setImageResource(R.mipmap.football_match_start_gif);
                tv_start.setBackgroundResource(R.mipmap.football_match_start_txt);
                gif_match_start.setVisibility(View.VISIBLE);
                tv_half_txt.setVisibility(View.VISIBLE);
                showGifAnimation("1");
                break;
            case R.id.bt_halftime://中场休息
                ToastTools.showQuick(this, "中场休息");
                tv_start.setBackgroundResource(R.mipmap.football_halftime_bg);
                gif_match_start.setVisibility(View.GONE);
                tv_half_txt.setVisibility(View.GONE);
                showGifAnimation("1");
                break;
            case R.id.bt_match_stop://比赛暂停
                ToastTools.showQuick(this, "比赛暂停");
                tv_start.setBackgroundResource(R.mipmap.football_match_stop_txt);
                gif_match_start.setVisibility(View.GONE);
                tv_half_txt.setVisibility(View.GONE);
                showGifAnimation("1");
                break;
            case R.id.bt_match_recovery://比赛恢复
                ToastTools.showQuick(this, "比赛恢复");
                tv_start.setBackgroundResource(R.mipmap.football_match_recovery_txt);
                gif_match_start.setVisibility(View.GONE);
                tv_half_txt.setVisibility(View.GONE);
                showGifAnimation("1");
                break;
            case R.id.bt_match_over://比赛结束
                ToastTools.showQuick(this, "比赛结束");
                tv_start.setBackgroundResource(R.mipmap.football_match_over_txt);
                gif_match_start.setVisibility(View.GONE);
                tv_half_txt.setVisibility(View.GONE);
                showGifAnimation("1");
                break;
            case R.id.bt_signal_off://比赛直播信号中断
                ToastTools.showQuick(this, "比赛直播信号中断");
                showGifAnimation("515");
                break;
            case R.id.bt_drink_water://喝水
                ToastTools.showQuick(this, "喝水");
                showGifAnimation("311");
                break;
            case R.id.bt_injured://受伤
                ToastTools.showQuick(this, "受伤");
                showGifAnimation("132");
                break;
            case R.id.bt_injured_addtime://伤停补时
                ToastTools.showQuick(this, "伤停补时");
                showGifAnimation("260");
                break;
            case R.id.bt_home_control://主队后场控球
                ToastTools.showQuick(this, "主队后场控球");
                showGifAnimation("1051");
                gif_home_control.setImageResource(R.mipmap.football_home_control);
                break;
            case R.id.bt_guest_control://客队后场控球
                ToastTools.showQuick(this, "客队后场控球");
                showGifAnimation("2075");
                gif_guest_control.setImageResource(R.mipmap.football_guest_control);
                break;
            case R.id.bt_home_attack://主队进攻
                ToastTools.showQuick(this, "主队进攻");
                showGifAnimation("1024");
                gif_home_attack.setImageResource(R.mipmap.football_home_attack_gif);
                break;
            case R.id.bt_guest_attack://客队进攻
                ToastTools.showQuick(this, "客队进攻");
                showGifAnimation("2048");
                gif_guest_attack.setImageResource(R.mipmap.football_guest_attack_gif);
                break;
            case R.id.bt_home_attack_danger://主队危险进攻
                ToastTools.showQuick(this, "主队危险进攻");
                showGifAnimation("1026");
                gif_home_attack_danger.setImageResource(R.mipmap.football_home_attack_danger_gif);
                break;
            case R.id.bt_guest_attack_danger://客队危险进攻
                ToastTools.showQuick(this, "客队危险进攻");
                showGifAnimation("2050");
                gif_guest_attack_danger.setImageResource(R.mipmap.football_guest_attack_danger_gif);
                break;
            case R.id.bt_home_deviate1://主队射偏1
                ToastTools.showQuick(this, "主队射偏1");
                showGifAnimation("1040");
                gif_home_ball1.setImageResource(R.mipmap.football_home_ball_gif);
                gif_home_position1.setImageResource(R.mipmap.football_home_position_gif);
                break;
            case R.id.bt_guest_deviate1://客队射偏1
                ToastTools.showQuick(this, "客队射偏1");
                showGifAnimation("2064");
                break;
            case R.id.bt_home_deviate2://主队射偏2
                ToastTools.showQuick(this, "主队射偏2");
                showGifAnimation("1041");
                break;
            case R.id.bt_guest_deviate2://客队射偏2
                ToastTools.showQuick(this, "客队射偏2");
                showGifAnimation("2065");
                break;
            case R.id.bt_home_hit1://主队射正1
                ToastTools.showQuick(this, "主队射正1");
                break;
            case R.id.bt_guest_hit1://客队射正1
                ToastTools.showQuick(this, "客队射正1");
                break;
            case R.id.bt_home_hit2://主队射正2
                ToastTools.showQuick(this, "主队射正2");
                break;
            case R.id.bt_guest_hit2://客队射正2
                ToastTools.showQuick(this, "客队射正2");
                break;
            case R.id.bt_home_goal_door://主队球门球
                ToastTools.showQuick(this, "主队球门球");
                break;
            case R.id.bt_guest_goal_door://客队球门球
                ToastTools.showQuick(this, "客队球门球");
                break;
            case R.id.bt_home_goal_out://主队界外球
                ToastTools.showQuick(this, "主队界外球");
                break;
            case R.id.bt_guest_goal_out://客队界外球
                ToastTools.showQuick(this, "客队界外球");
                break;
            case R.id.bt_home_corner_left://主队角球左侧
                ToastTools.showQuick(this, "主队角球左侧");
                break;
            case R.id.bt_guest_corner_left://客队角球左侧
                ToastTools.showQuick(this, "客队角球左侧");
                break;
            case R.id.bt_home_corner_right://主队角球右侧
                ToastTools.showQuick(this, "主队角球右侧");
                break;
            case R.id.bt_guest_corner_right://客队角球右侧
                ToastTools.showQuick(this, "客队角球右侧");
                break;
            case R.id.bt_home_offside://主队越位
                ToastTools.showQuick(this, "主队越位");
                break;
            case R.id.bt_guest_offside://客队越位
                ToastTools.showQuick(this, "客队越位");
                break;
            case R.id.bt_home_penalty://主队点球
                ToastTools.showQuick(this, "主队点球");
                break;
            case R.id.bt_guest_penalty://客队点球
                ToastTools.showQuick(this, "客队点球");
                break;
            case R.id.bt_home_penalty_lose://主队点球罚失
                ToastTools.showQuick(this, "主队点球罚失");
                break;
            case R.id.bt_guest_penalty_lose://客队点球罚失 
                ToastTools.showQuick(this, "客队点球罚失");
                break;
            case R.id.bt_home_y://主队黄牌
                ToastTools.showQuick(this, "主队黄牌");
                break;
            case R.id.bt_guest_y://客队黄牌
                ToastTools.showQuick(this, "客队黄牌");
                break;
            case R.id.bt_home_r://主队红牌
                ToastTools.showQuick(this, "主队红牌");
                break;
            case R.id.bt_guest_r://客队红牌
                ToastTools.showQuick(this, "客队红牌");
                break;
            case R.id.bt_home_whistle://主队哨子
                ToastTools.showQuick(this, "主队哨子");
                break;
            case R.id.bt_guest_whistle://客队哨子
                ToastTools.showQuick(this, "客队哨子");
                break;
            case R.id.bt_home_substitution://主队换人
                ToastTools.showQuick(this, "主队换人");
                break;
            case R.id.bt_guest_substitution://客队换人
                ToastTools.showQuick(this, "客队换人");
                break;
            case R.id.bt_home_free_kick://主队任意球
                ToastTools.showQuick(this, "主队任意球");
                break;
            case R.id.bt_guest_free_kick://客队任意球
                ToastTools.showQuick(this, "客队任意球");
                break;
            case R.id.bt_home_free_kick_danger_fk1://主队任危险任意球FK1
                ToastTools.showQuick(this, "主队任危险任意球FK1");
                break;
            case R.id.bt_guest_free_kick_danger_fk1://客队危险任意球FK1
                ToastTools.showQuick(this, "客队危险任意球FK1");
                break;
            case R.id.bt_home_free_kick_danger_fk2://主队任危险任意球FK2
                ToastTools.showQuick(this, "主队任危险任意球FK2");
                break;
            case R.id.bt_guest_free_kick_danger_fk2://客队危险任意球FK2
                ToastTools.showQuick(this, "客队危险任意球FK2");
                break;
            case R.id.bt_home_free_kick_danger_fk3l://主队任危险任意球FK3L
                ToastTools.showQuick(this, "主队任危险任意球FK3L");
                break;
            case R.id.bt_guest_free_kick_danger_fk3l://客队危险任意球FK3L
                ToastTools.showQuick(this, "客队危险任意球FK3L");
                break;
            case R.id.bt_home_free_kick_danger_fk3r://主队任危险任意球FK3R
                ToastTools.showQuick(this, "主队任危险任意球FK3R");
                break;
            case R.id.bt_guest_free_kick_danger_fk3r://客队危险任意球FK3R
                ToastTools.showQuick(this, "客队危险任意球FK3R");
                break;
            case R.id.bt_home_free_kick_danger_fk4://主队任危险任意球FK4
                ToastTools.showQuick(this, "主队任危险任意球FK4");
                break;
            case R.id.bt_guest_free_kick_danger_fk4://客队危险任意球FK4
                ToastTools.showQuick(this, "客队危险任意球FK4");
                break;
            case R.id.bt_home_goal://主队进球
                ToastTools.showQuick(this, "主队进球");
                break;
            case R.id.bt_guest_goal://客队进球
                ToastTools.showQuick(this, "客队进球");
                break;
            case R.id.bt_cheer://欢呼
                ToastTools.showQuick(this, "欢呼");
                break;
            default:
                break;
        }
    }

    private void showGifAnimation(String type) {
        rl_match_txt_content.setVisibility("1".equals(type) ? View.VISIBLE : View.GONE);
        iv_signal_off.setVisibility(("515".equals(type) || "516".equals(type)) ? View.VISIBLE : View.GONE);
        iv_drink_water.setVisibility("311".equals(type) ? View.VISIBLE : View.GONE);
        iv_injured.setVisibility("132".equals(type) ? View.VISIBLE : View.GONE);
        iv_injured_addtime.setVisibility("260".equals(type) ? View.VISIBLE : View.GONE);
        gif_home_control.setVisibility("1051".equals(type) ? View.VISIBLE : View.GONE);
        gif_guest_control.setVisibility("2075".equals(type) ? View.VISIBLE : View.GONE);
        gif_home_attack.setVisibility("1024".equals(type) ? View.VISIBLE : View.GONE);
        gif_guest_attack.setVisibility("2048".equals(type) ? View.VISIBLE : View.GONE);
        gif_home_attack_danger.setVisibility("1026".equals(type) ? View.VISIBLE : View.GONE);
        gif_guest_attack_danger.setVisibility("2050".equals(type) ? View.VISIBLE : View.GONE);
        rl_home_deviate1.setVisibility(("1040".equals(type) || "1041".equals(type)) ? View.VISIBLE : View.GONE);
//        rl_guest_deviate1.setVisibility(("2064".equals(type) || "2065".equals(type)) ? View.VISIBLE : View.GONE);

    }
}
