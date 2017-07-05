package com.hhly.mlottery.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

/**
 * Created by asus1 on 2016/1/7.
 */
public class StadiumUtils {
    //半场比分是否显示
    public static boolean isHalfScoreVisible(String status) {
        //2中场    3  下半场   -1  完场
        return "2".equals(status) || "3".equals(status) || "-1".equals(status);

    }

    public static void keepTimeAnimation(final TextView tv_frequency) {
        final AlphaAnimation anim1 = new AlphaAnimation(1, 1);
        anim1.setDuration(500);
        final AlphaAnimation anim2 = new AlphaAnimation(0, 0);
        anim2.setDuration(500);
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv_frequency.startAnimation(anim2);
            }
        });

        anim2.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv_frequency.startAnimation(anim1);
            }
        });
        tv_frequency.startAnimation(anim1);
    }


    public static double computeProgressbarPercent(String h, String g) {
        if ("0".equals(h) && "0".equals(g)) {
            return 50;
        }

        if (h == null && g == null) {
            return 50;
        }

        if ("".equals(h) && "".equals(g)) {
            return 50;
        }
        double home = Double.valueOf(h);
        double guest = Double.valueOf(g);
        return home / (home + guest) * 100;
    }

    public static double computeProgressbarPercent(int h, int g) {
        if ("0".equals(h) && "0".equals(g)) {
            return 50;
        }
        double home = Double.valueOf(h);
        double guest = Double.valueOf(g);
        return home / (home + guest) * 100;
    }


    public static int convertStringToInt(String time) {
        if (time == null || time.equals("")) {
            return 0;
        }else{
            return (int) Math.ceil(Double.parseDouble(time) / 60000);
        }
    }
}
