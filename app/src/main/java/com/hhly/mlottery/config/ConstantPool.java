package com.hhly.mlottery.config;

/**
 * Created by：XQyi on 2017/5/23 14:31
 * Use: 常量池
 */
public class ConstantPool {

    //AppId,官网申请的合法id 微信
    public final static String APP_ID = "wx7ef1da85d0f485e1"; //wx2a5538052969956e

    /*内页推介页面(推荐玩法&胜平负选择)*/
    public final static int PLAY_SPF = 0;
    public final static int PLAY_LQSPF = 1;
    public final static int SPF_S = 0;
    public final static int SPF_P = 1;
    public final static int SPF_F = 2;

    /* 支付方式  支付宝(默认) 0 ；微信 1 ；余额 2 */
    public final static Integer PAY_ZFB = 0; //ZFB支付
    public final static Integer PAY_WEIXIN = 1;//weixin支付
    public final static Integer PAY_YU_E = 2;// 余额支付

    /*竞彩推荐列表设置页面*/
    public final static String ALL_LEAGUE = "allLrague";//所有的联赛
    public final static String CURR_LEAGUE = "currLrague";// 当前(选中)的联赛
    public final static String CURR_PALY_TYPE = "currPlayType";// 当前(选中)的玩法
    public final static String PROMOTION_ID = "promotionId";// 推介的id
    public final static String TO_DETAILS_PROMOTION_ID = "todetailspromotionId";// 传到详情的推介的id
    public final static String BETTING_ITEM_DATA = "bettingItemData";// 单条推介数据


}
