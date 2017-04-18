package com.hhly.mlottery.config;

/**
 * @author: Wangg
 * @name：xxx
 * @description: 足球常量
 * @created on:2017/4/18  15:40.
 */

public interface FootBallTypeEnum {

    //事件直播
    //主队事件
    String SCORE = "1029";//主队进球
    String RED_CARD = "1032";
    String YELLOW_CARD = "1034";
    String SUBSTITUTION = "1055";
    String CORNER = "1025";
    String YTORED = "1045";//两黄变一红
    String DIANQIU = "1031";

    //客队事件
    String SCORE1 = "2053";//客队进球
    String RED_CARD1 = "2056";
    String YELLOW_CARD1 = "2058";
    String SUBSTITUTION1 = "2079";
    String CORNER1 = "2049";
    String YTORED1 = "2069";//两黄变一红
    String DIANQIU1 = "2055";

    //走势图
    String SHOOT = "1039";
    String SHOOTASIDE = "1040";
    String SHOOTASIDE2 = "1041";
    String DANGERATTACK = "1026";
    String ATTACK = "1024";

    String SHOOT1 = "2063";
    String SHOOTASIDE1 = "2064";
    String SHOOTASIDE12 = "2065";
    String DANGERATTACK1 = "2050";
    String ATTACK1 = "2048";


    /**
     * 未开
     */
    String NOTOPEN = "0";
    /**
     * 上半场
     */
    String FIRSTHALF = "1";
    /**
     * 中场
     */
    String HALFTIME = "2";
    /**
     * 下半场
     */
    String SECONDHALF = "3";
    /**
     * 完场
     **/
    String MATCHFINISH = "-1";


}
