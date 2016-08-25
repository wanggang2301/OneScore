package com.hhly.mlottery.impl;

import com.hhly.mlottery.bean.account.SendSmsCode;
import com.hhly.mlottery.util.net.VolleyContentFast;

/**
 * Created by lyx on 2016/6/3.
 * 获取验证码回调
 */
public interface GetVerifyCodeCallBack {
    /**获取验证码之前*/
    void beforGet();
    /**获取验证码返回, 默认已处理正常情况下的 返回值*/
    void onGetResponce(SendSmsCode code);
    /**获取验证码错误  ， 默认已弹出 "网速不给力"*/
    void onGetError(VolleyContentFast.VolleyException exception);

}
