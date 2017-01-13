package com.hhly.mlottery.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.Match;
import com.hhly.mlottery.bean.MatchOdd;
import com.hhly.mlottery.callback.FocusClickListener;
import com.hhly.mlottery.frame.footframe.FocusFragment;
import com.hhly.mlottery.util.adapter.ViewHolder;

public class ImmediateUtils {

    private final static String TAG = "ImmediateUtils";

    private static AnimationListener repeatAnim = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animation.start();
        }
    };




    public static void convert(final ViewHolder holder, final Match match, final Context context, int handicap, final FocusClickListener focusClickListener, int itemPaddingRight) {



        if (match.getItemBackGroundColorId() == R.color.item_football_event_yellow) {
            holder.setBackgroundRes(R.id.item_football_content_ll, match.getItemBackGroundColorId());
        } else {
            holder.setBackgroundRes(R.id.item_football_content_ll, R.color.white);
        }


        if (match.getHomeHalfScore() == null) {
            match.setHomeHalfScore("0");
        }

        if (match.getGuestHalfScore() == null) {
            match.setGuestHalfScore("0");
        }

        holder.setTag(R.id.item_football_hometeam, match.getThirdId());// 设置id
        holder.setText(R.id.item_football_hometeam, match.getHometeam());
        holder.setText(R.id.item_football_guestteam, match.getGuestteam());
        holder.setText(R.id.item_football_racename, match.getRacename());
        holder.setTextColor(R.id.item_football_racename, Color.parseColor(match.getRaceColor()));
        holder.setText(R.id.item_football_time, match.getTime());

        if ("0".equals(match.getStatusOrigin())) {// 未开
            holder.setText(R.id.item_football_full_score, "VS");
            holder.setTextColorRes(R.id.item_football_full_score, R.color.content_txt_grad);
            holder.setFakeBoldText(R.id.item_football_full_score, false);
            holder.setVisible(R.id.item_football_half_score, false);

        } else if ("1".equals(match.getStatusOrigin())) {// 上半场
            holder.setText(R.id.item_football_full_score, Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));// 设置比分
            // holder.setFakeBoldText(R.id.item_football_full_score, true);// 加粗
            holder.setTextColor(R.id.item_football_full_score, context.getResources().getColor(R.color.bg_header));
            try {
                int keeptime = Integer.parseInt(match.getKeepTime());// 设置时间
                if (keeptime > 45) {
                    holder.setText(R.id.item_football_half_score, "45+");
                    holder.setTextColor(R.id.item_football_half_score, context.getResources().getColor(R.color.bg_header));
                } else {
                    holder.setText(R.id.item_football_half_score, keeptime + "");
                    holder.setTextColor(R.id.item_football_half_score, context.getResources().getColor(R.color.bg_header));
                }
            } catch (Exception e) {
                holder.setText(R.id.item_football_half_score, "E");
                holder.setTextColor(R.id.item_football_half_score, context.getResources().getColor(R.color.bg_header));
                Log.e(TAG, "时间出错！keeptime = " + match.getKeepTime());
            }
            holder.setVisible(R.id.item_football_half_score, true);// 显示时间
        } else if ("3".equals(match.getStatusOrigin())) {// 下半场
            // holder.setText(R.id.item_football_full_score,
            // match.getHomeScore() +
            // "-" + match.getGuestScore());

            holder.setText(R.id.item_football_full_score, Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            // holder.setFakeBoldText(R.id.item_football_full_score, true);
            holder.setTextColorRes(R.id.item_football_full_score, R.color.bg_header);
            try {
                int keeptime = Integer.parseInt(match.getKeepTime());
                if (keeptime > 90) {
                    holder.setText(R.id.item_football_half_score, "90+");
                    holder.setTextColorRes(R.id.item_football_half_score, R.color.bg_header);
                } else {
                    holder.setText(R.id.item_football_half_score, keeptime + "");
                    holder.setTextColorRes(R.id.item_football_half_score, R.color.bg_header);
                }
            } catch (Exception e) {
                holder.setText(R.id.item_football_half_score, "E");
                holder.setTextColorRes(R.id.item_football_half_score, R.color.bg_header);
            }
            holder.setVisible(R.id.item_football_half_score, true);// 显示时间
        } else if ("4".equals(match.getStatusOrigin())) {// 加时
            holder.setText(R.id.item_football_full_score, Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            // holder.setFakeBoldText(R.id.item_football_full_score, true);
            holder.setTextColorRes(R.id.item_football_full_score, R.color.bg_header);
            holder.setText(R.id.item_football_half_score, R.string.immediate_status_overtime);
            holder.setTextColorRes(R.id.item_football_half_score, R.color.bg_header);
            holder.setVisible(R.id.item_football_half_score, true);// 显示加
        } else if ("5".equals(match.getStatusOrigin())) {// 点球
            holder.setText(R.id.item_football_full_score, Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            // holder.setFakeBoldText(R.id.item_football_full_score, true);
            holder.setTextColorRes(R.id.item_football_full_score, R.color.bg_header);
            holder.setText(R.id.item_football_half_score, R.string.immediate_status_point);
            holder.setTextColorRes(R.id.item_football_half_score, R.color.bg_header);

            holder.setVisible(R.id.item_football_half_score, true);
        } else if ("-1".equals(match.getStatusOrigin())) {// 完场
            holder.setText(R.id.item_football_full_score, Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            // holder.setFakeBoldText(R.id.item_football_full_score, true);
            holder.setTextColor(R.id.item_football_full_score, context.getResources().getColor(R.color.red));
            holder.setText(R.id.item_football_half_score, "(" + match.getHomeHalfScore() + "-" + match.getGuestHalfScore() + ")");
            holder.setTextColor(R.id.item_football_half_score, context.getResources().getColor(R.color.red));
            holder.setVisible(R.id.item_football_half_score, true);

        } else if ("2".equals(match.getStatusOrigin())) {// 中场

            // holder.setFakeBoldText(R.id.item_football_full_score, true);
            holder.setText(R.id.item_football_full_score, Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.setTextColor(R.id.item_football_full_score, context.getResources().getColor(R.color.bg_header));
            holder.setText(R.id.item_football_half_score, R.string.immediate_status_midfield);
            holder.setTextColor(R.id.item_football_half_score, context.getResources().getColor(R.color.bg_header));
            holder.setVisible(R.id.item_football_half_score, true);
        } else if ("-10".equals(match.getStatusOrigin())) {// 取消
            holder.setTextColor(R.id.item_football_full_score, context.getResources().getColor(R.color.bg_header));
            holder.setFakeBoldText(R.id.item_football_full_score, false);
            holder.setText(R.id.item_football_full_score, "VS");
            holder.setTextColorRes(R.id.item_football_full_score, R.color.content_txt_grad);
            holder.setVisible(R.id.item_football_half_score, true);
            holder.setTextColor(R.id.item_football_half_score, context.getResources().getColor(R.color.red));
            holder.setText(R.id.item_football_half_score, R.string.immediate_status_cancel);
        } else if ("-11".equals(match.getStatusOrigin())) {// 待定
            holder.setTextColor(R.id.item_football_full_score, context.getResources().getColor(R.color.bg_header));
            holder.setFakeBoldText(R.id.item_football_full_score, false);
            holder.setText(R.id.item_football_half_score, R.string.immediate_status_hold);
            holder.setTextColor(R.id.item_football_half_score, context.getResources().getColor(R.color.red));
            holder.setVisible(R.id.item_football_half_score, true);
            holder.setText(R.id.item_football_full_score, "VS");
            holder.setTextColorRes(R.id.item_football_full_score, R.color.content_txt_grad);
        } else if ("-12".equals(match.getStatusOrigin())) {// 腰斩
            holder.setText(R.id.item_football_half_score, R.string.immediate_status_cut);
            holder.setTextColor(R.id.item_football_half_score, context.getResources().getColor(R.color.red));
            holder.setText(R.id.item_football_full_score, Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.setTextColor(R.id.item_football_full_score, context.getResources().getColor(R.color.bg_header));
            holder.setVisible(R.id.item_football_half_score, true);
            // holder.setFakeBoldText(R.id.item_football_full_score, true);
        } else if ("-13".equals(match.getStatusOrigin())) {// 中断
            holder.setText(R.id.item_football_half_score, R.string.immediate_status_mesomere);
            holder.setTextColor(R.id.item_football_half_score, context.getResources().getColor(R.color.red));
            holder.setText(R.id.item_football_full_score, Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.setTextColor(R.id.item_football_full_score, context.getResources().getColor(R.color.bg_header));
            holder.setVisible(R.id.item_football_half_score, true);
            // holder.setFakeBoldText(R.id.item_football_full_score, true);

        } else if ("-14".equals(match.getStatusOrigin())) {// 推迟
            holder.setText(R.id.item_football_half_score, R.string.immediate_status_postpone);
            holder.setTextColor(R.id.item_football_half_score, context.getResources().getColor(R.color.red));
            holder.setText(R.id.item_football_full_score, match.getHomeScore() + "-" + match.getGuestScore());
            holder.setTextColor(R.id.item_football_full_score, context.getResources().getColor(R.color.bg_header));
            holder.setVisible(R.id.item_football_half_score, true);

            holder.setText(R.id.item_football_full_score, "VS");
            holder.setTextColorRes(R.id.item_football_full_score, R.color.content_txt_grad);
            holder.setFakeBoldText(R.id.item_football_full_score, false);

        }

        if (match.getHomeTeamTextColorId() == R.color.red || match.getHomeTeamTextColorId() == R.color.content_txt_black) {
            holder.setTextColorRes(R.id.item_football_hometeam, match.getHomeTeamTextColorId());
        } else {
            holder.setTextColorRes(R.id.item_football_hometeam, R.color.content_txt_black);
        }
        if (match.getGuestTeamTextColorId() == R.color.red || match.getGuestTeamTextColorId() == R.color.content_txt_black) {
            holder.setTextColorRes(R.id.item_football_guestteam, match.getGuestTeamTextColorId());
        } else {
            holder.setTextColorRes(R.id.item_football_guestteam, R.color.content_txt_black);
        }

        if ("-10".equals(match.getStatusOrigin()) && "-11".equals(match.getStatusOrigin()) && "-14".equals(match.getStatusOrigin()) && !"0".equals(match.getStatusOrigin())
                && (match.getHomeScore() == null || match.getGuestScore() == null)) {
            holder.setText(R.id.item_football_full_score, Html.fromHtml("<span><b>0</b></span><span>-</span><span><b>0</b></span>"));

        }

        // match.getStatusOrigin());

        if ("1".equals(match.getStatusOrigin()) || "3".equals(match.getStatusOrigin())) {// 显示秒的闪烁
            holder.setText(R.id.item_football_frequency, "\'");
            holder.setVisible(R.id.item_football_frequency, true);
            // Animation anim = AnimationUtils.loadAnimation(context,
            // R.anim.sencond_frequency);
            // holder.setAnimation(R.id.item_football_frequency, anim);

            // if(holder.getAnimation(R.id.item_football_frequency)!=null){
            final AlphaAnimation anim1 = new AlphaAnimation(1, 1);
            anim1.setDuration(500);
            final AlphaAnimation anim2 = new AlphaAnimation(0, 0);
            anim2.setDuration(500);
            anim1.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    holder.startAnimation(R.id.item_football_frequency, anim2);
                }
            });

            anim2.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    holder.startAnimation(R.id.item_football_frequency, anim1);
                }
            });
            holder.startAnimation(R.id.item_football_frequency, anim1);
            // }

        } else {
            holder.setText(R.id.item_football_frequency, "");
            holder.setVisible(R.id.item_football_frequency, View.GONE);
            holder.cancelAnimation(R.id.item_football_frequency);
        }

        if ("3".equals(match.getStatusOrigin()) || "2".equals(match.getStatusOrigin())) {// 显示右上角上半场比分
            holder.setText(R.id.item_football_right_mid_score, "(" + match.getHomeHalfScore() + "-" + match.getGuestHalfScore() + ")");
            holder.setVisible(R.id.item_football_right_mid_score, true);

        } else if ("-12".equals(match.getStatusOrigin()) || "-13".equals(match.getStatusOrigin())) {// 中断和腰斩
            // 有中场比分显示中场比分
            if (match.getHomeHalfScore() != null && match.getGuestHalfScore() != null) {
                holder.setText(R.id.item_football_right_mid_score, "(" + match.getHomeHalfScore() + "-" + match.getGuestHalfScore() + ")");
                holder.setVisible(R.id.item_football_right_mid_score, true);
            } else {
                holder.setVisible(R.id.item_football_right_mid_score, false);
            }
        } else {
            holder.setVisible(R.id.item_football_right_mid_score, false);
        }

        setNullViewGone(holder, R.id.item_football_home_rc, match.getHome_rc());
        setNullViewGone(holder, R.id.item_football_home_yc, match.getHome_yc());
        setNullViewGone(holder, R.id.item_football_guest_rc, match.getGuest_rc());
        setNullViewGone(holder, R.id.item_football_guest_yc, match.getGuest_yc());

        resetOddsView(holder);

        // 盘口计算
        if (handicap == 1) {// 亚盘
            MatchOdd odd = match.getMatchOdds().get("asiaLet");
            if (odd == null) {
                // holder.setVisible(R.id.item_football_odds_layout,
                // View.INVISIBLE);
                holder.setVisible(R.id.item_football_left_odds, View.GONE);
                holder.setVisible(R.id.item_football_handicap_value, View.GONE);
                holder.setVisible(R.id.item_football_right_odds, View.GONE);
            } else {
                holder.setVisible(R.id.item_football_odds_layout, true);
                String handicapValue = odd.getHandicapValue();

                int keeptime = 0;
                if (match.getKeepTime() != null) {
                    keeptime = Integer.parseInt(match.getKeepTime());
                }

                if ((!"-1".equals(match.getStatusOrigin()) && keeptime >= 89) || "-".equals(handicapValue) || "|".equals(handicapValue)) {// 封盘,完场不会有封盘的情况
                    holder.setVisible(R.id.item_football_left_odds, View.GONE);
                    holder.setVisible(R.id.item_football_handicap_value, View.GONE);
                    holder.setText(R.id.item_football_right_odds, R.string.immediate_status_entertained);
                } else {
                    holder.setVisible(R.id.item_football_left_odds, true);
                    holder.setVisible(R.id.item_football_handicap_value, true);
                    holder.setVisible(R.id.item_football_right_odds, true);


                    holder.setText(R.id.item_football_handicap_value, HandicapUtils.changeHandicap(handicapValue));
                    holder.setText(R.id.item_football_left_odds, odd.getLeftOdds());
                    holder.setText(R.id.item_football_right_odds, odd.getRightOdds());
                    if (match.getLeftOddTextColorId() != 0) {
                        holder.setTextColorRes(R.id.item_football_left_odds, match.getLeftOddTextColorId());
                    }
                    if (match.getRightOddTextColorId() != 0) {
                        holder.setTextColorRes(R.id.item_football_right_odds, match.getRightOddTextColorId());
                    }
                    if (match.getMidOddTextColorId() != 0) {
                        holder.setTextColorRes(R.id.item_football_handicap_value, match.getMidOddTextColorId());
                    }
                }

                if ("-1".equals(match.getStatusOrigin())) {// 完场不会有封盘的情况
                    try {
                        float homeScore = Float.parseFloat(match.getHomeScore());
                        float guestScore = Float.parseFloat(match.getGuestScore());
                        float handicapValueF = Float.parseFloat(handicapValue);
                        float re = homeScore - guestScore - handicapValueF;
                        if (re > 0) {// 注意这是亚盘的
                            holder.setTextColorRes(R.id.item_football_left_odds, R.color.white);
                            holder.setBackgroundColorRes(R.id.item_football_left_odds, R.color.resultcol);
                            holder.setTextColorRes(R.id.item_football_handicap_value, R.color.content_txt_black);
                            holder.setBackgroundColorRes(R.id.item_football_handicap_value, R.color.transparent);// 不能是白色
                            holder.setTextColorRes(R.id.item_football_right_odds, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_right_odds, R.color.transparent);




                        } else if (re < 0) {
                            holder.setTextColorRes(R.id.item_football_left_odds, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_left_odds, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_handicap_value, R.color.content_txt_black);
                            holder.setBackgroundColorRes(R.id.item_football_handicap_value, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_right_odds, R.color.white);
                            holder.setBackgroundColorRes(R.id.item_football_right_odds, R.color.resultcol);




                        } else {
                            holder.setTextColorRes(R.id.item_football_left_odds, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_left_odds, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_handicap_value, R.color.content_txt_black);
                            holder.setBackgroundColorRes(R.id.item_football_handicap_value, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_right_odds, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_right_odds, R.color.transparent);


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    if (match.getLeftOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_left_odds, R.color.content_txt_light_grad);
                    } else {
                        holder.setTextColorRes(R.id.item_football_left_odds, match.getLeftOddTextColorId());
                    }

                    if (match.getRightOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_handicap_value, R.color.content_txt_black);
                    } else {
                        holder.setTextColorRes(R.id.item_football_handicap_value, match.getMidOddTextColorId());
                    }

                    if (match.getMidOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_right_odds, R.color.content_txt_light_grad);
                    } else {
                        holder.setTextColorRes(R.id.item_football_right_odds, match.getRightOddTextColorId());
                    }

                    holder.setBackgroundColorRes(R.id.item_football_left_odds, R.color.transparent);
                    holder.setBackgroundColorRes(R.id.item_football_handicap_value, R.color.transparent);
                    holder.setBackgroundColorRes(R.id.item_football_right_odds, R.color.transparent);


                }

            }
        } else if (handicap == 2) {// 大小球
            MatchOdd odd = match.getMatchOdds().get("asiaSize");
            if (odd == null) {
                holder.setVisible(R.id.item_football_left_odds, View.GONE);
                holder.setVisible(R.id.item_football_handicap_value, View.GONE);
                holder.setVisible(R.id.item_football_right_odds, View.GONE);
            } else {
                holder.setVisible(R.id.item_football_odds_layout, true);
                String handicapValue = odd.getHandicapValue();
                int keeptime = 0;
                if (match.getKeepTime() != null) {
                    keeptime = Integer.parseInt(match.getKeepTime());
                }
                if ((!"-1".equals(match.getStatusOrigin()) && keeptime >= 89) || "-".equals(handicapValue) || "|".equals(handicapValue)) {// 封盘
                    holder.setVisible(R.id.item_football_left_odds, View.GONE);
                    holder.setVisible(R.id.item_football_handicap_value, View.GONE);
                    holder.setText(R.id.item_football_right_odds, R.string.immediate_status_entertained);
                } else {
                    holder.setVisible(R.id.item_football_left_odds, true);
                    holder.setVisible(R.id.item_football_handicap_value, true);
                    holder.setVisible(R.id.item_football_right_odds, true);
                    holder.setText(R.id.item_football_handicap_value, HandicapUtils.changeHandicapByBigLittleBall(handicapValue));
                    holder.setText(R.id.item_football_left_odds, odd.getLeftOdds());
                    holder.setText(R.id.item_football_right_odds, odd.getRightOdds());

                    if (match.getLeftOddTextColorId() != 0) {
                        holder.setTextColorRes(R.id.item_football_left_odds, match.getLeftOddTextColorId());
                    }
                    if (match.getRightOddTextColorId() != 0) {
                        holder.setTextColorRes(R.id.item_football_right_odds, match.getRightOddTextColorId());
                    }

                    if (match.getMidOddTextColorId() != 0) {
                        holder.setTextColorRes(R.id.item_football_handicap_value, match.getMidOddTextColorId());
                    }



                }

                if ("-1".equals(match.getStatusOrigin())) {// 完场不会有封盘的情况
                    try {
                        float homeScore = Float.parseFloat(match.getHomeScore());
                        float guestScore = Float.parseFloat(match.getGuestScore());
                        float handicapValueF = Float.parseFloat(handicapValue);
                        float re = homeScore + guestScore - handicapValueF;
                        if (re > 0) {
                            holder.setTextColorRes(R.id.item_football_left_odds, R.color.white);
                            holder.setBackgroundColorRes(R.id.item_football_left_odds, R.color.resultcol);
                            holder.setTextColorRes(R.id.item_football_handicap_value, R.color.content_txt_black);
                            holder.setBackgroundColorRes(R.id.item_football_handicap_value, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_right_odds, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_right_odds, R.color.transparent);




                        } else if (re < 0) {
                            holder.setTextColorRes(R.id.item_football_left_odds, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_left_odds, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_handicap_value, R.color.content_txt_black);
                            holder.setBackgroundColorRes(R.id.item_football_handicap_value, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_right_odds, R.color.white);
                            holder.setBackgroundColorRes(R.id.item_football_right_odds, R.color.resultcol);



                        } else {
                            holder.setTextColorRes(R.id.item_football_left_odds, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_left_odds, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_handicap_value, R.color.black);
                            holder.setBackgroundColorRes(R.id.item_football_handicap_value, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_right_odds, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_right_odds, R.color.transparent);


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // holder.setTextColorRes(R.id.item_football_left_odds,
                    // R.color.content_txt_light_grad);
                    // holder.setTextColorRes(R.id.item_football_handicap_value,
                    // R.color.black);
                    // holder.setTextColorRes(R.id.item_football_right_odds,
                    // R.color.content_txt_light_grad);
                    // holder.setBackgroundColorRes(R.id.item_football_left_odds,
                    // R.color.transparent);
                    // holder.setBackgroundColorRes(R.id.item_football_handicap_value,
                    // R.color.transparent);
                    // holder.setBackgroundColorRes(R.id.item_football_right_odds,
                    // R.color.transparent);

                    if (match.getLeftOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_left_odds, R.color.content_txt_light_grad);
                    } else {
                        holder.setTextColorRes(R.id.item_football_left_odds, match.getLeftOddTextColorId());
                    }

                    if (match.getRightOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_handicap_value, R.color.content_txt_black);
                    } else {
                        holder.setTextColorRes(R.id.item_football_handicap_value, match.getMidOddTextColorId());
                    }

                    if (match.getMidOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_right_odds, R.color.content_txt_light_grad);
                    } else {
                        holder.setTextColorRes(R.id.item_football_right_odds, match.getRightOddTextColorId());
                    }

                    holder.setBackgroundColorRes(R.id.item_football_left_odds, R.color.transparent);
                    holder.setBackgroundColorRes(R.id.item_football_handicap_value, R.color.transparent);
                    holder.setBackgroundColorRes(R.id.item_football_right_odds, R.color.transparent);



                }

            }
        } else if (handicap == 3) {// 欧赔
            MatchOdd odd = match.getMatchOdds().get("euro");
            if (odd == null) {
                holder.setVisible(R.id.item_football_left_odds, View.GONE);
                holder.setVisible(R.id.item_football_handicap_value, View.GONE);
                holder.setVisible(R.id.item_football_right_odds, View.GONE);
            } else {
                String leftOdds = odd.getLeftOdds();
                int keeptime = 0;
                if (match.getKeepTime() != null) {
                    keeptime = Integer.parseInt(match.getKeepTime());
                }
                if ((!"-1".equals(match.getStatusOrigin()) && keeptime >= 89) || "-".equals(leftOdds) || "|".equals(leftOdds)) {// 封盘
                    holder.setVisible(R.id.item_football_left_odds, View.GONE);
                    holder.setVisible(R.id.item_football_handicap_value, View.GONE);
                    holder.setText(R.id.item_football_right_odds, R.string.immediate_status_entertained);
                } else {

                    holder.setText(R.id.item_football_handicap_value, odd.getMediumOdds());
                    holder.setText(R.id.item_football_left_odds, odd.getLeftOdds());
                    holder.setText(R.id.item_football_right_odds, odd.getRightOdds());
                    if (match.getLeftOddTextColorId() != 0) {
                        holder.setTextColor(R.id.item_football_left_odds, match.getLeftOddTextColorId());
                    }
                    if (match.getRightOddTextColorId() != 0) {
                        holder.setTextColor(R.id.item_football_right_odds, match.getRightOddTextColorId());
                    }
                    if (match.getMidOddTextColorId() != 0) {
                        holder.setTextColor(R.id.item_football_handicap_value, match.getMidOddTextColorId());
                    }


                }
                // 完场赔率颜色

                if ("-1".equals(match.getStatusOrigin())) {
                    try {
                        int homeScore = Integer.parseInt(match.getHomeScore());
                        int guestScore = Integer.parseInt(match.getGuestScore());

                        if (homeScore > guestScore) {
                            holder.setTextColorRes(R.id.item_football_left_odds, R.color.white);
                            holder.setBackgroundColorRes(R.id.item_football_left_odds, R.color.resultcol);
                            holder.setTextColorRes(R.id.item_football_handicap_value, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_handicap_value, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_right_odds, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_right_odds, R.color.transparent);
                        } else if (homeScore < guestScore) {
                            holder.setTextColorRes(R.id.item_football_left_odds, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_left_odds, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_handicap_value, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_handicap_value, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_right_odds, R.color.white);
                            holder.setBackgroundColorRes(R.id.item_football_right_odds, R.color.resultcol);
                        } else {
                            holder.setTextColorRes(R.id.item_football_left_odds, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_left_odds, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_handicap_value, R.color.white);
                            holder.setBackgroundColorRes(R.id.item_football_handicap_value, R.color.resultcol);
                            holder.setTextColorRes(R.id.item_football_right_odds, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_right_odds, R.color.transparent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (match.getLeftOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_left_odds, R.color.content_txt_light_grad);
                    } else {
                        holder.setTextColorRes(R.id.item_football_left_odds, match.getLeftOddTextColorId());
                    }

                    if (match.getRightOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_handicap_value, R.color.content_txt_light_grad);
                    } else {
                        holder.setTextColorRes(R.id.item_football_handicap_value, match.getMidOddTextColorId());
                    }

                    if (match.getMidOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_right_odds, R.color.content_txt_light_grad);
                    } else {
                        holder.setTextColorRes(R.id.item_football_right_odds, match.getRightOddTextColorId());
                    }

                    holder.setBackgroundColorRes(R.id.item_football_left_odds, R.color.transparent);
                    holder.setBackgroundColorRes(R.id.item_football_handicap_value, R.color.transparent);
                    holder.setBackgroundColorRes(R.id.item_football_right_odds, R.color.transparent);
                }
            }
        } else {
            holder.setVisible(R.id.item_football_left_odds, View.GONE);
            holder.setVisible(R.id.item_football_handicap_value, View.GONE);
            holder.setVisible(R.id.item_football_right_odds, View.GONE);
        }



        holder.setOnClickListener(R.id.Iv_guangzhu, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (focusClickListener != null) {
                    focusClickListener.onClick(v, match);
                }
            }
        });

        String focusIds = PreferenceUtil.getString(FocusFragment.FOCUS_ISD, "");
        String[] idArray = focusIds.split("[,]");

        for (String id : idArray) {
            if (id.equals(match.getThirdId())) {

                holder.setImageResource(R.id.Iv_guangzhu, R.mipmap.iconfont_guanzhu_hover);
                holder.setTag(R.id.Iv_guangzhu,true);


//                holder.setText(R.id.item_football_focus_btn, R.string.cancel_favourite);
//                holder.setTag(R.id.item_football_focus_btn, true);
                break;
            } else {
                holder.setImageResource(R.id.Iv_guangzhu,R.mipmap.iconfont_guanzhu);
                holder.setTag(R.id.Iv_guangzhu,false);

//                holder.setText(R.id.item_football_focus_btn, R.string.favourite);
//                holder.setTag(R.id.item_football_focus_btn, false);
            }
        }
    }

    public static void interConvert(final ViewHolder holder, final Match match, final Context context, int handicap, final FocusClickListener focusClickListener, int itemPaddingRight) {

        if (Build.VERSION.SDK_INT <= 16 && itemPaddingRight != -1) {// 4.1不支持负的padding值，需要在代码强制设置
            holder.setPadding(R.id.item_football_international_content_ll, 0, 0, itemPaddingRight, 0);
        }

        if (Build.VERSION.SDK_INT >= 16 && itemPaddingRight == -1) {// 如果不使用侧滑，请设置为-1，隐藏
            holder.setVisible(R.id.item_football_international_layout_right, false);
            holder.setPadding(R.id.item_football_international_content_ll, 0, 0, 0, 0);
        }

        holder.setText(R.id.item_football_international_homename, match.getHometeam());
//		holder.setText(R.id.item_football_international_homename, "Uniao Recreativa dos Trabalhadores MG/URT");
        holder.setText(R.id.item_football_international_guestname, match.getGuestteam());
        holder.setTag(R.id.item_football_international_homename, match.getThirdId());// 设置id
        holder.setText(R.id.item_football_international_racename, match.getRacename());
        holder.setTextColor(R.id.item_football_international_racename, Color.parseColor(match.getRaceColor()));
        holder.setTextColorRes(R.id.item_football_international_time, R.color.content_txt_light_grad);

        if (match.getItemBackGroundColorId() == R.color.item_football_event_yellow) {
            holder.setBackgroundRes(R.id.item_football_international_content_ll, match.getItemBackGroundColorId());
        } else {
            holder.setBackgroundRes(R.id.item_football_international_content_ll, R.color.white);
        }

        holder.setVisible(R.id.item_football_international_vs_img, false);
        if ("0".equals(match.getStatusOrigin())) {// 未开
            holder.setVisible(R.id.item_football_international_homescore, View.INVISIBLE);
            holder.setVisible(R.id.item_football_international_guestscore, View.INVISIBLE);
            holder.setVisible(R.id.item_football_international_halfscore, View.INVISIBLE);
            holder.setVisible(R.id.item_football_international_vs_img, true);
            holder.setText(R.id.item_football_international_time, match.getTime());

        } else if ("1".equals(match.getStatusOrigin())) {// 上半场
            setInterScore(holder, match, R.color.bg_header);
            holder.setVisible(R.id.item_football_international_halfscore, View.INVISIBLE);
            try {
                int keeptime = Integer.parseInt(match.getKeepTime());// 设置时间
                if (keeptime > 45) {
                    holder.setText(R.id.item_football_international_time, "45+");
                } else {
                    holder.setText(R.id.item_football_international_time, keeptime + "");
                }
            } catch (Exception e) {
                holder.setText(R.id.item_football_international_time, "E");
                Log.e(TAG, "时间出错！keeptime = " + match.getKeepTime());
            }
            holder.setTextColorRes(R.id.item_football_international_time, R.color.bg_header);
            // holder.setVisible(R.id.item_football_international_time, true);//
            // 显示时间
        } else if ("3".equals(match.getStatusOrigin())) {// 下半场
            setInterScore(holder, match, R.color.bg_header);
            holder.setVisible(R.id.item_football_international_halfscore, true);
            try {
                int keeptime = Integer.parseInt(match.getKeepTime());
                if (keeptime > 90) {
                    holder.setText(R.id.item_football_international_time, "90+");
                } else {
                    holder.setText(R.id.item_football_international_time, keeptime + "");
                }
            } catch (Exception e) {
                holder.setText(R.id.item_football_international_time, "E");
                // Log.e(TAG, "时间出错！keeptime = " + match.getKeepTime());
            }
            holder.setTextColorRes(R.id.item_football_international_time, R.color.bg_header);
            // holder.setVisible(R.id.item_football_international_time, true);//
            // 显示时间
        } else if ("4".equals(match.getStatusOrigin())) {// 加时
            setInterScore(holder, match, R.color.bg_header);
            holder.setText(R.id.item_football_international_time, R.string.immediate_status_overtime);
            holder.setTextColorRes(R.id.item_football_international_time, R.color.bg_header);// 显示加
            holder.setVisible(R.id.item_football_international_halfscore, true);
        } else if ("5".equals(match.getStatusOrigin())) {// 点球

            setInterScore(holder, match, R.color.bg_header);
            holder.setText(R.id.item_football_international_time, R.string.immediate_status_point);
            holder.setTextColorRes(R.id.item_football_international_time, R.color.bg_header);
            holder.setVisible(R.id.item_football_international_halfscore, true);
            // holder.setVisible(R.id.item_football_half_score, true);
        } else if ("-1".equals(match.getStatusOrigin())) {// 完场
            setInterScore(holder, match, R.color.red);
            holder.setText(R.id.item_football_international_time, match.getTime());
            holder.setText(R.id.item_football_international_halfscore, "HT(" + match.getHomeHalfScore() + "-" + match.getGuestHalfScore() + ")");
            holder.setTextColorRes(R.id.item_football_international_halfscore, R.color.score_red);
            holder.setVisible(R.id.item_football_international_halfscore, true);
        } else if ("2".equals(match.getStatusOrigin())) {// 中场

            setInterScore(holder, match, R.color.bg_header);
            holder.setText(R.id.item_football_international_time, R.string.immediate_status_midfield);
            holder.setTextColorRes(R.id.item_football_international_time, R.color.bg_header);
            holder.setVisible(R.id.item_football_international_halfscore, true);
        } else if ("-10".equals(match.getStatusOrigin())) {// 取消
            holder.setVisible(R.id.item_football_international_homescore, View.INVISIBLE);
            holder.setVisible(R.id.item_football_international_guestscore, View.INVISIBLE);
            holder.setVisible(R.id.item_football_international_halfscore, View.INVISIBLE);
            holder.setVisible(R.id.item_football_international_vs_img, true);
            holder.setTextColorRes(R.id.item_football_international_time, R.color.score_red);
            holder.setText(R.id.item_football_international_time, R.string.immediate_status_cancel);
            holder.setVisible(R.id.item_football_international_halfscore, false);

        } else if ("-11".equals(match.getStatusOrigin())) {// 待定
            holder.setVisible(R.id.item_football_international_homescore, View.INVISIBLE);
            holder.setVisible(R.id.item_football_international_guestscore, View.INVISIBLE);
            holder.setVisible(R.id.item_football_international_vs_img, true);

            holder.setText(R.id.item_football_international_time, R.string.immediate_status_hold);
            holder.setTextColorRes(R.id.item_football_international_time, R.color.score_red);
            holder.setVisible(R.id.item_football_international_time, true);
            holder.setVisible(R.id.item_football_international_halfscore, false);
        } else if ("-12".equals(match.getStatusOrigin())) {// 腰斩
            holder.setText(R.id.item_football_international_time, R.string.immediate_status_cut);
            holder.setTextColorRes(R.id.item_football_international_time, R.color.score_red);
            setInterScore(holder, match, R.color.bg_header);
            holder.setVisible(R.id.item_football_international_halfscore, false);

        } else if ("-13".equals(match.getStatusOrigin())) {// 中断
            holder.setText(R.id.item_football_international_time, R.string.immediate_status_mesomere);
            holder.setTextColorRes(R.id.item_football_international_time, R.color.score_red);
            setInterScore(holder, match, R.color.bg_header);
            holder.setVisible(R.id.item_football_international_halfscore, false);

        } else if ("-14".equals(match.getStatusOrigin())) {// 推迟
            holder.setVisible(R.id.item_football_international_homescore, View.INVISIBLE);
            holder.setVisible(R.id.item_football_international_guestscore, View.INVISIBLE);
            holder.setVisible(R.id.item_football_international_halfscore, View.INVISIBLE);
            holder.setVisible(R.id.item_football_international_vs_img, true);
            holder.setText(R.id.item_football_international_time, R.string.immediate_status_postpone);
            holder.setTextColorRes(R.id.item_football_international_time, R.color.score_red);
            // setInterScore(holder, match, R.color.bg_header);
            //holder.setVisible(R.id.item_football_international_halfscore, false);
        }

        if (match.getHomeTeamTextColorId() == R.color.red || match.getHomeTeamTextColorId() == R.color.content_txt_black) {
            holder.setTextColorRes(R.id.item_football_international_homename, match.getHomeTeamTextColorId());
        } else {
            holder.setTextColorRes(R.id.item_football_international_homename, R.color.content_txt_black);
        }
        if (match.getGuestTeamTextColorId() == R.color.red || match.getGuestTeamTextColorId() == R.color.content_txt_black) {
            holder.setTextColorRes(R.id.item_football_international_guestname, match.getGuestTeamTextColorId());
        } else {
            holder.setTextColorRes(R.id.item_football_international_guestname, R.color.content_txt_black);
        }

        if ("1".equals(match.getStatusOrigin()) || "3".equals(match.getStatusOrigin())) {// 显示秒的闪烁
            holder.setText(R.id.item_football_international_frequency, "\'");
            holder.setVisible(R.id.item_football_international_frequency, true);
            final AlphaAnimation anim1 = new AlphaAnimation(1, 1);
            anim1.setDuration(500);
            final AlphaAnimation anim2 = new AlphaAnimation(0, 0);
            anim2.setDuration(500);
            anim1.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    holder.startAnimation(R.id.item_football_international_frequency, anim2);
                }
            });

            anim2.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    holder.startAnimation(R.id.item_football_international_frequency, anim1);
                }
            });
            holder.startAnimation(R.id.item_football_international_frequency, anim1);
            // }

        } else {
            holder.setText(R.id.item_football_international_frequency, "");
            holder.setVisible(R.id.item_football_international_frequency, View.GONE);
            holder.cancelAnimation(R.id.item_football_international_frequency);
        }


        setNullViewGone(holder, R.id.item_football_international_home_rc, match.getHome_rc());
        setNullViewGone(holder, R.id.item_football_international_home_yc, match.getHome_yc());
        setNullViewGone(holder, R.id.item_football_international_guest_rc, match.getGuest_rc());
        setNullViewGone(holder, R.id.item_football_international_guest_yc, match.getGuest_yc());

        resetInterOddsView(holder);

        // 盘口计算
        if (handicap == 1) {// 亚盘
            MatchOdd odd = match.getMatchOdds().get("asiaLet");
            if (odd == null) {
                // holder.setVisible(R.id.item_football_odds_layout,
                // View.INVISIBLE);
                hideInterOddsView(holder);
            } else {
                // holder.setVisible(R.id.item_football_odds_layout, true);
                String handicapValue = odd.getHandicapValue();

                int keeptime = 0;
                if (match.getKeepTime() != null) {
                    keeptime = Integer.parseInt(match.getKeepTime());
                }

                if ((!"-1".equals(match.getStatusOrigin()) && keeptime >= 89) || "-".equals(handicapValue) || "|".equals(handicapValue)) {// 封盘,完场不会有封盘的情况
                    holder.setVisible(R.id.item_football_international_handicap1, true);
                    holder.setVisible(R.id.item_football_international_handicap2, View.INVISIBLE);
                    holder.setVisible(R.id.item_football_international_handicap3, true);
                    holder.setVisible(R.id.item_football_international_odd_top, true);
                    holder.setVisible(R.id.item_football_international_odd_middle, false);
                    holder.setVisible(R.id.item_football_international_odd_bottom, true);
                    holder.setText(R.id.item_football_international_handicap1, "-");
                    holder.setText(R.id.item_football_international_handicap3, "-");
                    holder.setText(R.id.item_football_international_odd_top, "-");
                    holder.setText(R.id.item_football_international_odd_bottom, "-");
                } else {
                    holder.setVisible(R.id.item_football_international_handicap1, true);
                    holder.setVisible(R.id.item_football_international_handicap2, View.INVISIBLE);
                    holder.setVisible(R.id.item_football_international_handicap3, true);
                    holder.setVisible(R.id.item_football_international_odd_top, true);
                    holder.setVisible(R.id.item_football_international_odd_middle, false);
                    holder.setVisible(R.id.item_football_international_odd_bottom, true);

                    // holder.setText(R.id.item_football_handicap_value,
                    // HandicapUtils.changeHandicap(handicapValue));
                    // holder.setText(viewId, textId)

                    String[] handicaps = HandicapUtils.interChangeHandicap(odd.getHandicapValue());

                    holder.setText(R.id.item_football_international_handicap1, handicaps[0]);
                    holder.setText(R.id.item_football_international_handicap3, handicaps[1]);

                    holder.setText(R.id.item_football_international_odd_top, odd.getLeftOdds());
                    holder.setText(R.id.item_football_international_odd_bottom, odd.getRightOdds());
                    if (match.getLeftOddTextColorId() != 0) {
                        holder.setTextColorRes(R.id.item_football_international_odd_top, match.getLeftOddTextColorId());
                    }
                    if (match.getRightOddTextColorId() != 0) {
                        holder.setTextColorRes(R.id.item_football_international_odd_bottom, match.getRightOddTextColorId());
                    }
                    if (match.getMidOddTextColorId() != 0) {
                        holder.setTextColorRes(R.id.item_football_international_handicap1, match.getMidOddTextColorId());
                        holder.setTextColorRes(R.id.item_football_international_handicap3, match.getMidOddTextColorId());
                    }
                }

                if ("-1".equals(match.getStatusOrigin())) {// 完场不会有封盘的情况
                    try {
                        float homeScore = Float.parseFloat(match.getHomeScore());
                        float guestScore = Float.parseFloat(match.getGuestScore());
                        float handicapValueF = Float.parseFloat(handicapValue);
                        float re = homeScore - guestScore - handicapValueF;
                        if (re > 0) {// 注意这是亚盘的
                            holder.setTextColorRes(R.id.item_football_international_odd_top, R.color.white);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_top, R.color.resultcol);
                            holder.setTextColorRes(R.id.item_football_international_handicap1, R.color.content_txt_black);
                            holder.setBackgroundColorRes(R.id.item_football_international_handicap1, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_handicap3, R.color.content_txt_black);
                            holder.setBackgroundColorRes(R.id.item_football_international_handicap3, R.color.transparent);// 不能是白色
                            holder.setTextColorRes(R.id.item_football_international_odd_bottom, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_bottom, R.color.transparent);
                        } else if (re < 0) {
                            holder.setTextColorRes(R.id.item_football_international_odd_top, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_top, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_handicap1, R.color.content_txt_black);
                            holder.setBackgroundColorRes(R.id.item_football_international_handicap1, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_handicap3, R.color.content_txt_black);
                            holder.setBackgroundColorRes(R.id.item_football_international_handicap3, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_odd_bottom, R.color.white);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_bottom, R.color.resultcol);
                        } else {
                            holder.setTextColorRes(R.id.item_football_international_odd_top, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_top, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_handicap1, R.color.content_txt_black);
                            holder.setBackgroundColorRes(R.id.item_football_international_handicap1, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_handicap3, R.color.content_txt_black);
                            holder.setBackgroundColorRes(R.id.item_football_international_handicap3, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_odd_bottom, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_bottom, R.color.transparent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    if (match.getLeftOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_international_odd_top, R.color.content_txt_light_grad);
                    } else {
                        holder.setTextColorRes(R.id.item_football_international_odd_top, match.getLeftOddTextColorId());
                    }

                    if (match.getMidOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_international_handicap1, R.color.content_txt_black);
                        holder.setTextColorRes(R.id.item_football_international_handicap3, R.color.content_txt_black);
                    } else {
                        if (!((!"-1".equals(match.getStatusOrigin()) && keeptime >= 89) || "-".equals(handicapValue) || "|".equals(handicapValue))) {
                            String[] handicaps = HandicapUtils.interChangeHandicap(odd.getHandicapValue());
                            SpannableStringBuilder builder = new SpannableStringBuilder(handicaps[0]);
                            ForegroundColorSpan redSpan0 = new ForegroundColorSpan(context.getResources().getColor(match.getMidOddTextColorId()));
                            builder.setSpan(redSpan0, 1, handicaps[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            ForegroundColorSpan blackSpan0 = new ForegroundColorSpan(context.getResources().getColor(R.color.content_txt_black));
                            builder.setSpan(blackSpan0, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            holder.setText(R.id.item_football_international_handicap1, builder);

                            SpannableStringBuilder builder1 = new SpannableStringBuilder(handicaps[1]);
                            ForegroundColorSpan redSpan1 = new ForegroundColorSpan(context.getResources().getColor(match.getMidOddTextColorId()));
                            builder1.setSpan(redSpan1, 1, handicaps[1].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            ForegroundColorSpan blackSpan1 = new ForegroundColorSpan(context.getResources().getColor(R.color.content_txt_black));
                            builder1.setSpan(blackSpan1, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            holder.setText(R.id.item_football_international_handicap3, builder1);
                            // holder.setTextColorRes(R.id.item_football_international_handicap1,
                            // match.getMidOddTextColorId());
                            // holder.setTextColorRes(R.id.item_football_international_handicap2,
                            // match.getMidOddTextColorId());
                        } else {
                            holder.setTextColorRes(R.id.item_football_international_handicap1, R.color.content_txt_black);
                            holder.setTextColorRes(R.id.item_football_international_handicap3, R.color.content_txt_black);
                        }
                    }

                    if (match.getRightOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_international_odd_bottom, R.color.content_txt_light_grad);
                    } else {
                        holder.setTextColorRes(R.id.item_football_international_odd_bottom, match.getRightOddTextColorId());
                    }

                    holder.setBackgroundColorRes(R.id.item_football_international_odd_top, R.color.transparent);
                    holder.setBackgroundColorRes(R.id.item_football_international_handicap1, R.color.transparent);
                    holder.setBackgroundColorRes(R.id.item_football_international_handicap3, R.color.transparent);
                    holder.setBackgroundColorRes(R.id.item_football_international_odd_bottom, R.color.transparent);
                }

            }
        } else if (handicap == 2) {// 大小球
            MatchOdd odd = match.getMatchOdds().get("asiaSize");
            if (odd == null) {
                hideInterOddsView(holder);
            } else {
                // holder.setVisible(R.id.item_football_odds_layout, true);
                String handicapValue = odd.getHandicapValue();
                int keeptime = 0;
                if (match.getKeepTime() != null) {
                    keeptime = Integer.parseInt(match.getKeepTime());
                }
                if ((!"-1".equals(match.getStatusOrigin()) && keeptime >= 89) || "-".equals(handicapValue) || "|".equals(handicapValue)) {// 封盘
                    holder.setVisible(R.id.item_football_international_handicap1, true);
                    holder.setVisible(R.id.item_football_international_handicap2, View.INVISIBLE);
                    holder.setVisible(R.id.item_football_international_handicap3, true);
                    holder.setVisible(R.id.item_football_international_odd_top, true);
                    holder.setVisible(R.id.item_football_international_odd_middle, true);
                    holder.setVisible(R.id.item_football_international_odd_bottom, true);
                    holder.setText(R.id.item_football_international_odd_top, "-");
                    holder.setText(R.id.item_football_international_odd_middle, "-");
                    holder.setText(R.id.item_football_international_odd_bottom, "-");
                    holder.setText(R.id.item_football_international_handicap1, "O");
                    holder.setText(R.id.item_football_international_handicap3, "U");
                } else {
                    holder.setVisible(R.id.item_football_international_handicap1, true);
                    holder.setVisible(R.id.item_football_international_handicap2, View.INVISIBLE);
                    holder.setVisible(R.id.item_football_international_handicap3, true);
                    holder.setVisible(R.id.item_football_international_odd_top, true);
                    holder.setVisible(R.id.item_football_international_odd_middle, true);
                    holder.setVisible(R.id.item_football_international_odd_bottom, true);
                    holder.setText(R.id.item_football_international_odd_top, odd.getLeftOdds());
                    holder.setText(R.id.item_football_international_odd_middle, odd.getHandicapValue());
                    holder.setText(R.id.item_football_international_odd_bottom, odd.getRightOdds());
                    holder.setText(R.id.item_football_international_handicap1, "O");
                    holder.setText(R.id.item_football_international_handicap3, "U");

                    if (match.getLeftOddTextColorId() != 0) {
                        holder.setTextColorRes(R.id.item_football_international_odd_top, match.getLeftOddTextColorId());
                    }
                    if (match.getRightOddTextColorId() != 0) {
                        holder.setTextColorRes(R.id.item_football_international_odd_middle, match.getRightOddTextColorId());
                    }

                    if (match.getMidOddTextColorId() != 0) {
                        holder.setTextColorRes(R.id.item_football_international_odd_bottom, match.getMidOddTextColorId());
                    }
                }

                if ("-1".equals(match.getStatusOrigin())) {// 完场不会有封盘的情况
                    try {
                        float homeScore = Float.parseFloat(match.getHomeScore());
                        float guestScore = Float.parseFloat(match.getGuestScore());
                        float handicapValueF = Float.parseFloat(handicapValue);
                        float re = homeScore + guestScore - handicapValueF;
                        if (re > 0) {
                            holder.setTextColorRes(R.id.item_football_international_odd_top, R.color.white);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_top, R.color.resultcol);
                            holder.setTextColorRes(R.id.item_football_international_odd_middle, R.color.content_txt_black);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_middle, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_odd_bottom, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_bottom, R.color.transparent);
                        } else if (re < 0) {
                            holder.setTextColorRes(R.id.item_football_international_odd_top, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_top, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_odd_middle, R.color.content_txt_black);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_middle, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_odd_bottom, R.color.white);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_bottom, R.color.resultcol);
                        } else {
                            holder.setTextColorRes(R.id.item_football_international_odd_top, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_top, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_odd_middle, R.color.black);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_middle, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_odd_bottom, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_bottom, R.color.transparent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    if (match.getLeftOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_international_odd_top, R.color.content_txt_light_grad);
                    } else {
                        holder.setTextColorRes(R.id.item_football_international_odd_top, match.getLeftOddTextColorId());
                    }

                    if (match.getRightOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_international_odd_middle, R.color.content_txt_black);
                    } else {
                        holder.setTextColorRes(R.id.item_football_international_odd_middle, match.getMidOddTextColorId());
                    }

                    if (match.getMidOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_international_odd_bottom, R.color.content_txt_light_grad);
                    } else {
                        holder.setTextColorRes(R.id.item_football_international_odd_bottom, match.getRightOddTextColorId());
                    }

                    holder.setBackgroundColorRes(R.id.item_football_international_odd_top, R.color.transparent);
                    holder.setBackgroundColorRes(R.id.item_football_international_odd_middle, R.color.transparent);
                    holder.setBackgroundColorRes(R.id.item_football_international_odd_bottom, R.color.transparent);
                }

            }
        } else if (handicap == 3) {// 欧赔
            MatchOdd odd = match.getMatchOdds().get("euro");
            if (odd == null) {
                hideInterOddsView(holder);
            } else {
                String leftOdds = odd.getLeftOdds();
                int keeptime = 0;
                if (match.getKeepTime() != null) {
                    keeptime = Integer.parseInt(match.getKeepTime());
                }
                if ((!"-1".equals(match.getStatusOrigin()) && keeptime >= 89) || "-".equals(leftOdds) || "|".equals(leftOdds)) {// 封盘
                    holder.setVisible(R.id.item_football_international_handicap1, true);
                    holder.setVisible(R.id.item_football_international_handicap2, true);
                    holder.setVisible(R.id.item_football_international_handicap3, true);
                    holder.setVisible(R.id.item_football_international_odd_top, true);
                    holder.setVisible(R.id.item_football_international_odd_middle, true);
                    holder.setVisible(R.id.item_football_international_odd_bottom, true);
                    holder.setText(R.id.item_football_international_odd_top, "-");
                    holder.setText(R.id.item_football_international_odd_middle, "-");
                    holder.setText(R.id.item_football_international_odd_bottom, "-");
                    holder.setText(R.id.item_football_international_handicap1, "W");
                    holder.setText(R.id.item_football_international_handicap2, "D");
                    holder.setText(R.id.item_football_international_handicap3, "L");
                } else {
                    holder.setVisible(R.id.item_football_international_handicap1, true);
                    holder.setVisible(R.id.item_football_international_handicap2, true);
                    holder.setVisible(R.id.item_football_international_handicap3, true);
                    holder.setVisible(R.id.item_football_international_odd_top, true);
                    holder.setVisible(R.id.item_football_international_odd_middle, true);
                    holder.setVisible(R.id.item_football_international_odd_bottom, true);

                    holder.setText(R.id.item_football_international_handicap1, "W");
                    holder.setText(R.id.item_football_international_handicap2, "D");
                    holder.setText(R.id.item_football_international_handicap3, "L");

                    holder.setText(R.id.item_football_international_odd_top, odd.getLeftOdds());
                    holder.setText(R.id.item_football_international_odd_middle, odd.getMediumOdds());
                    holder.setText(R.id.item_football_international_odd_bottom, odd.getRightOdds());

                    if (match.getLeftOddTextColorId() != 0) {
                        holder.setTextColor(R.id.item_football_international_odd_top, match.getLeftOddTextColorId());
                    }
                    if (match.getRightOddTextColorId() != 0) {
                        holder.setTextColor(R.id.item_football_international_odd_bottom, match.getRightOddTextColorId());
                    }
                    if (match.getMidOddTextColorId() != 0) {
                        holder.setTextColor(R.id.item_football_international_odd_middle, match.getMidOddTextColorId());
                    }
                }
                // 完场赔率颜色

                if ("-1".equals(match.getStatusOrigin())) {
                    try {
                        int homeScore = Integer.parseInt(match.getHomeScore());
                        int guestScore = Integer.parseInt(match.getGuestScore());

                        if (homeScore > guestScore) {
                            holder.setTextColorRes(R.id.item_football_international_odd_top, R.color.white);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_top, R.color.resultcol);
                            holder.setTextColorRes(R.id.item_football_international_odd_middle, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_middle, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_odd_bottom, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_bottom, R.color.transparent);
                        } else if (homeScore < guestScore) {
                            holder.setTextColorRes(R.id.item_football_international_odd_top, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_top, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_odd_middle, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_middle, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_odd_bottom, R.color.white);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_bottom, R.color.resultcol);
                        } else {
                            holder.setTextColorRes(R.id.item_football_international_odd_top, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_top, R.color.transparent);
                            holder.setTextColorRes(R.id.item_football_international_odd_middle, R.color.white);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_middle, R.color.resultcol);
                            holder.setTextColorRes(R.id.item_football_international_odd_bottom, R.color.content_txt_light_grad);
                            holder.setBackgroundColorRes(R.id.item_football_international_odd_bottom, R.color.transparent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (match.getLeftOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_international_odd_top, R.color.content_txt_light_grad);
                    } else {
                        holder.setTextColorRes(R.id.item_football_international_odd_top, match.getLeftOddTextColorId());
                    }

                    if (match.getRightOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_international_odd_middle, R.color.content_txt_light_grad);
                    } else {
                        holder.setTextColorRes(R.id.item_football_international_odd_middle, match.getMidOddTextColorId());
                    }

                    if (match.getMidOddTextColorId() == 0) {
                        holder.setTextColorRes(R.id.item_football_international_odd_bottom, R.color.content_txt_light_grad);
                    } else {
                        holder.setTextColorRes(R.id.item_football_international_odd_bottom, match.getRightOddTextColorId());
                    }

                    holder.setBackgroundColorRes(R.id.item_football_international_odd_top, R.color.transparent);
                    holder.setBackgroundColorRes(R.id.item_football_international_odd_middle, R.color.transparent);
                    holder.setBackgroundColorRes(R.id.item_football_international_odd_bottom, R.color.transparent);
                }
            }
        } else {
            hideInterOddsView(holder);
        }

        holder.setOnClickListener(R.id.Iv_guangzhu, new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, "focus..", 1000).show();
                L.i("110","及时关注");
                if (focusClickListener != null) {
                    focusClickListener.onClick(v, match);
                }
            }
        });




        String focusIds = PreferenceUtil.getString(FocusFragment.FOCUS_ISD, "");
        String[] idArray = focusIds.split("[,]");

        for (String id : idArray) {
            if (id.equals(match.getThirdId())) {

//                holder.setText(R.id.item_football_internationl_focus_btn, R.string.cancel_favourite);
//                holder.setTag(R.id.item_football_internationl_focus_btn, true);

                holder.setImageResource(R.id.Iv_guangzhu, R.mipmap.iconfont_guanzhu_hover);
                holder.setTag(R.id.Iv_guangzhu,true);

                break;
            } else {
                holder.setImageResource(R.id.Iv_guangzhu,R.mipmap.iconfont_guanzhu);
                holder.setTag(R.id.Iv_guangzhu,false);

//                holder.setText(R.id.item_football_internationl_focus_btn, R.string.favourite);
//                holder.setTag(R.id.item_football_internationl_focus_btn, false);
            }
        }

    }

    private static void setInterScore(ViewHolder holder, Match match, int color) {
        holder.setText(R.id.item_football_international_homescore, match.getHomeScore());
        holder.setText(R.id.item_football_international_guestscore, match.getGuestScore());
        holder.setTextColorRes(R.id.item_football_international_homescore, color);
        holder.setTextColorRes(R.id.item_football_international_guestscore, color);
        holder.setVisible(R.id.item_football_international_homescore, true);
        holder.setVisible(R.id.item_football_international_guestscore, true);
    }


    /**
     * 设置红黄牌
     *
     * @param holder
     * @param viewId
     * @param textValue
     */
    public static void setNullViewGone(ViewHolder holder, int viewId, String textValue) {
        if ("".equals(textValue) || "0".equals(textValue) || textValue == null) {
            holder.setVisible(viewId, false);
        } else {
            holder.setText(viewId, textValue);
            holder.setVisible(viewId, true);
        }
    }

    /**
     * 还原赔率部分view
     *
     * @param holder
     */
    private static void resetOddsView(ViewHolder holder) {
        holder.setVisible(R.id.item_football_odds_layout, true);
        holder.setVisible(R.id.item_football_left_odds, true);
        holder.setVisible(R.id.item_football_handicap_value, true);
        holder.setVisible(R.id.item_football_right_odds, true);
        // holder.setTextColor(R.id.item_football_handicap_value, Color.BLACK);
    }

    /**
     * 还原国际版赔率view
     *
     * @param holder
     */
    private static void resetInterOddsView(ViewHolder holder) {
        holder.setVisible(R.id.item_football_international_handicap1, true);
        holder.setVisible(R.id.item_football_international_handicap2, true);
        holder.setVisible(R.id.item_football_international_handicap3, true);
        holder.setVisible(R.id.item_football_international_odd_top, true);
        holder.setVisible(R.id.item_football_international_odd_middle, true);
        holder.setVisible(R.id.item_football_international_odd_bottom, true);
        // holder.setTextColor(R.id.item_football_handicap_value, Color.BLACK);
    }

    private static void hideInterOddsView(ViewHolder holder) {
        holder.setVisible(R.id.item_football_international_handicap1, false);
        holder.setVisible(R.id.item_football_international_handicap2, View.INVISIBLE);
        holder.setVisible(R.id.item_football_international_handicap3, false);
        holder.setVisible(R.id.item_football_international_odd_top, false);
        holder.setVisible(R.id.item_football_international_odd_middle, false);
        holder.setVisible(R.id.item_football_international_odd_bottom, false);
        // holder.setTextColor(R.id.item_football_handicap_value, Color.BLACK);
    }

}
