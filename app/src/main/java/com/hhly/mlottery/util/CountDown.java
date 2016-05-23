package com.hhly.mlottery.util;

import android.os.CountDownTimer;

public class CountDown extends CountDownTimer {
    private static final String TAG = CountDown.class.getSimpleName();

    private CountDownCallback callback;


    /**
     *
     * @param millisInFuture  总时长
     * @param countDownInterval 间隔时间
     * @param callback
     */
    public CountDown(long millisInFuture, long countDownInterval, CountDownCallback callback) {
        super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        this.callback = callback;
    }

    @Override
    public void onFinish() {// 计时完毕时触发
        L.d(TAG , " onfinish");
        if (callback != null){
            callback.onFinish();
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程显示
        L.d(TAG , " onTick : "+ millisUntilFinished);
        if (callback != null){
            callback.onTick(millisUntilFinished);
        }
    }

    public interface CountDownCallback{
        void onFinish();
        void onTick(long millisUntilFinished);
    }
}