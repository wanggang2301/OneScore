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

    /* 支付参数请求的标记*/
    public final static String WEIXIN_SERVICE = "3";//微信
    public final static String ZEB_SERVICE = "4";//支付宝

    /*竞彩推荐列表设置页面*/
    public final static String ALL_LEAGUE = "allLrague";//所有的联赛
    public final static String CURR_LEAGUE = "currLrague";// 当前(选中)的联赛
    public final static String CURR_PALY_TYPE = "currPlayType";// 当前(选中)的玩法
    public final static String PROMOTION_ID = "promotionId";// 推介的id
    public final static String TO_DETAILS_PROMOTION_ID = "todetailspromotionId";// 传到详情的推介的id
    public final static String BETTING_ITEM_DATA = "bettingItemData";// 单条推介数据
    public final static String BETTING_LOAD = "bettingLoad";// 登录后返回码

    /*支付宝返回码*/
    public final static String PAY_RESULT_STATUS_SUCCESS = "9000";//支付成功
    public final static String PAY_RESULT_STATUS_QUXIAO = "6001";//支付取消
    public final static String PAY_RESULT_STATUS_QUE_REN_ZHONG = "8000";//结果确认中
    public final static String PAY_RESULT_STATUS_NEW_WORK_ANOMALY = "6002";//网络异常
    public final static String PAY_RESULT_STATUS_REPEAT_REQUEST = "5000";//重复请求

    /*参数type*/
    public final static String PARAMENT_APP_TYPE = "2"; // appType 2:android
    public final static String PARAMENT_CHANNEL = "1"; //0：PC 1：安卓 2:IOS 3:H5
    public final static String PARAMENT_PAY_TYPE = "3"; //1微信2支付宝3账户余额  （目前只支持3）

    /*跳转登录页完成登录后返回码*/
    public final static String PUBLIC_INPUT_PARAMEMT = "inputparament"; //intent 传入参数
    public final static String PAY_DETAILS_RESULT = "paydetailsresult"; //支付页面返回（需要重新生成订单）
    public final static String PAY_CHARGE_MONEY_RESULT = "paychargemoneyresult"; //充值页面返回（需要重新查询余额）
    public final static String PAY_ISSUE_RESULT = "issueresult"; //发布页面返回（需要重新请求玩法数据）



}
