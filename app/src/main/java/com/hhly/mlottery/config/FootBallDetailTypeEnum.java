package com.hhly.mlottery.config;

/**
 * @author: Wangg
 * @name： 跳转到足球内页标记
 * @description: xxx
 * @created on:2017/5/5  10:47.
 */

public interface FootBallDetailTypeEnum {

    //key
    String CURRENT_TAB_KEY = "current_ab";


    //默认
    int FOOT_DETAIL_DEFAULT = -1;

    int FOOT_DETAIL_RECOMMEND = 0;

    // 推介
    int FOOT_DATAIL_BETTING = 0;

    //滚球
    int FOOT_DETAIL_ROLL = 1;

    //直播
    int FOOT_DETAIL_LIVE = 2;


    //指数
    int FOOT_DETAIL_INDEX = 3;


    //分析(由分析和情报组成)
    int FOOT_ANALYSE_MERGE = 4;

    //聊球
    int FOOT_DETAIL_CHARTBALL = 5;


    //只为标记是由哪里跳转到足球内页的
    //情报
    int FOOT_DETAIL_INFOCENTER = 6;
    //分析
    int FOOT_DETAIL_ANALYSIS = 7;


}
