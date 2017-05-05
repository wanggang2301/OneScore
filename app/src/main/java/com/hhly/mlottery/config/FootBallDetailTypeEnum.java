package com.hhly.mlottery.config;

/**
 * @author: Wangg
 * @name： 跳转到足球内页标记
 * @description: xxx
 * @created on:2017/5/5  10:47.
 */

public interface FootBallDetailTypeEnum {


    String CURRENT_TAB_KEY = "current_ab";

    //默认
    int FOOT_DETAIL_DEFAULT = -1;

    //滚球
    int FOOT_DETAIL_ROLL = 0;

    //直播
    int FOOT_DETAIL_LIVE = 1;

    //分析
    int FOOT_DETAIL_ANALYSIS = 2;

    //情报
    int FOOT_DETAIL_INFOCENTER = 3;

    //指数
    int FOOT_DETAIL_INDEX = 4;

    //聊球
    int FOOT_DETAIL_CHARTBALL = 5;
}
