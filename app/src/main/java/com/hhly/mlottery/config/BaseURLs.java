package com.hhly.mlottery.config;

import com.hhly.mlottery.activity.DebugConfigActivity;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;


/**
 * @author Tenney
 * @ClassName: BaseURLs
 * @Description: 所有接口
 * @date 2015-10-14 上午10:46:14
 */
public class BaseURLs {

    //                              __
    //                        __   (__`\
    //                       (__`\   \\`\
    //                        `\\`\   \\ \
    //                          `\\`\  \\ \
    //                            `\\`\#\\ \#
    //                              \_ ##\_ |##
    //                              (___)(___)##
    //                               (0)  (0)`\##
    //                                |~   ~ , \##
    //                                |      |  \##
    //                                |     /\   \##         __..---'''''-.._.._
    //                                |     | \   `\##  _.--'                _  `.
    //                                Y     |  \    `##'                     \`\  \
    //                               /      |   \                             | `\ \
    //                              /_...___|    \                            |   `\\
    //                             /        `.    |      MA DONG YUN         /      ##
    //                            |          |    |                         /      ####
    //                            |          |    |                        /       ####
    //                            | () ()    |     \     |          |  _.-'         ##
    //                            `.        .'      `._. |______..| |-'|
    //                              `------'           | | | |    | || |
    //                                                 | | | |    | || |
    //                                                 | | | |    | || |
    //                                                 | | | |    | || |
    //                                           _____ | | | |____| || |
    //                                          /     `` |-`/     ` |` |
    //                                          \________\__\_______\__\
    //                                           """""""""   """""""'"""

    /**
     * 公网
     */
    private static String HOST = getHost();


    private static String getHost() {

        if (AppConstants.isTestEnv) {//开发不需要修改下面代码
            int url_config = PreferenceUtil.getInt(MyConstants.URL_HOME_CONFIG, DebugConfigActivity.URL_1332255);

            if (url_config == DebugConfigActivity.URL_1332255) {
                return "m.1332255.com:81";//测试环境
            } else if (url_config == DebugConfigActivity.URL_242) {
                return "192.168.10.242:8181";//开发环境。
            } else if (url_config == DebugConfigActivity.URL_93) {
                return "183.61.172.93:8096"; // 典哥新加
            } else if (url_config == DebugConfigActivity.DIY_INPUT) {
                return PreferenceUtil.getString("DIY_INPUT", "m.13322.com"); // 自定义环境地址
            } else {
                return "m.13322.com";
            }
        }

        return "m.13322.com";//发布版本

    }
    /*
    * 上传图片公网
    * */

    private static String PHOST = getPHost();

    private static String getPHost() {
        if (AppConstants.isTestEnv) {//开发不需要修改下面代码
            int url_config = PreferenceUtil.getInt(MyConstants.URL_HOME_CONFIG, DebugConfigActivity.URL_1332255);

            if (url_config == DebugConfigActivity.URL_1332255) {
                return "file.1332255.com:81";//测试环境
            } else if (url_config == DebugConfigActivity.URL_242) {
                return "192.168.10.242:8181";//开发环境。
            } else {
                return "file.13322.com";
            }
        }
        return "file.13322.com";//发布版本
    }


    /**
     * 内网
     */
    private final static String HTTP = "http://";

    private final static String HTTPS = "https://";

    private final static String URL_SPLITTER = "/";

    private final static String WS = "ws://";
    /**
     * 推送公网
     */

    private static String WS_HOST = getWsHost();

    private static String getWsHost() {

        //测试scoket链接数 ActiveMQ
        //return "192.168.31.19:8061";

        if (AppConstants.isTestEnv) {//开发不需要修改下面代码
            int ws_config = PreferenceUtil.getInt(MyConstants.WS_HOME_CONFIG, DebugConfigActivity.WS_242);
            if (ws_config == DebugConfigActivity.WS_82) {
                return "m.1332255.com:81/ws";
            } else if (ws_config == DebugConfigActivity.WS_242) {
                return "192.168.10.242:61634";
            } else if (ws_config == DebugConfigActivity.DIY_INPUT) {
                return PreferenceUtil.getString("DIY_INPUT", "m.13322.com/ws");// 自定义
            } else {
                return "m.13322.com/ws";
            }
        }
        return "m.13322.com/ws";//开发，发布改这里
    }

    /**
     * 推送内网
     */


    public final static String WS_SERVICE = WS + WS_HOST;

    public final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER + "mlottery/core/";
    /*新搜索特制url*/
    public final static String NEW_URL_API_HOST = HTTP + HOST + URL_SPLITTER + "mlottery/core";
    /*上传图片新URl*/
    public final static String NEW_URL_API_PHOST = HTTP + PHOST + URL_SPLITTER + "upload/";
    //视频直播
    private final static String URL_MATCHVIDEO_DATA = "matchVideo.findVideoInfoApp.do";

    public final static String LANGUAGE_PARAM = "lang";
    public final static String TIMEZONE_PARAM = "timeZone";

    /**
     * 繁体
     */
    public final static String LANGUAGE_SWITCHING_TW = "zh-TW";
    /**
     * 简体
     */
    public final static String LANGUAGE_SWITCHING_CN = "zh";
    /**
     * 英文
     */
    public final static String LANGUAGE_SWITCHING_EN = "en";
    /**
     * 韩语
     */
    public final static String LANGUAGE_SWITCHING_KO = "ko";
    /**
     * 印度尼西亚
     */
    public final static String LANGUAGE_SWITCHING_ID = "id";
    /**
     * 泰语
     */
    public final static String LANGUAGE_SWITCHING_TH = "th";
    /**
     * 越南语
     */
    public final static String LANGUAGE_SWITCHING_VI = "vi";


    /**
     * 彩票开奖
     */
    // 所有彩票最后一期开奖结果
//    private final static String URL_LASTLOTTERY = "lastLotteryResults.findLastLotteryResults.do";
//    private final static String URL_LASTLOTTERY = "lastLotteryResults.findNewAndroidSecLastLotteryResults.do";// 新增了6个彩种
    private final static String URL_LASTLOTTERY = "lastLotteryResults.findAndroidSecLastLotteryResults.do";// 新增了8个彩种

    // 香港开奖
    private final static String URL_LHCDetailed = "detailedLotteryResults.findLhcDetailedResults.do";

    // 重庆时时彩
    private final static String URL_CQSSCDetailed = "detailedLotteryResults.findCQSSCDetailedResults.do";

    // 江西时时彩
    private final static String URL_JXSSCDetailed = "detailedLotteryResults.findJXSSCDetailedResults.do";

    // 新疆时时彩
    private final static String URL_XJSSCDetailed = "detailedLotteryResults.findXJSSCDetailedResults.do";

    // 云南时时彩
    private final static String URL_YNSSCDetailed = "detailedLotteryResults.findYNSSCDetailedResults.do";

    // 七星彩
//    private final static String URL_QXCDetailed = "detailedLotteryResults.findQXCDetailedResults.do";
    private final static String URL_QXCDetailed = "detailedLotteryResults.findNewQXCAndroidDetailedResults.do";// 此接口新增了奖金池

    // 广东11选5
    private final static String URL_D11X5Detailed = "detailedLotteryResults.findD11X5DetailedResults.do";

    // 广东快乐10分
    private final static String URL_DKL10Detailed = "detailedLotteryResults.findDKL10DetailedResults.do";

    // 湖北11选5
    private final static String URL_HB11X5Detailed = "detailedLotteryResults.findHB11X5DetailedResults.do";

    // 江苏11选5
    private final static String URL_JS11X5Detailed = "detailedLotteryResults.findJS11X5DetailedResults.do";

    // 江西11选5
    private final static String URL_JX11X5Detailed = "detailedLotteryResults.findJX11X5DetailedResults.do";

    // 安徽快3
    private final static String URL_AHK3Detailed = "detailedLotteryResults.findAHK3DetailedResults.do";

    // 湖南快乐10分
    private final static String URL_HNKL10Detailed = "detailedLotteryResults.findHNKL10DetailedResults.do";

    // 快乐8
    private final static String URL_KL8Detailed = "detailedLotteryResults.findKL8DetailedResults.do";

    // 吉林快3
    private final static String URL_JLK3Detailed = "detailedLotteryResults.findJLK3DetailedResults.do";

    // 辽宁11选5
    private final static String URL_LN11X5Detailed = "detailedLotteryResults.findLN11X5DetailedResults.do";

    // 北京赛车
    private final static String URL_PK10Detailed = "detailedLotteryResults.findPK10DetailedResults.do";

    // 江苏快3
    private final static String URL_JSK3Detailed = "detailedLotteryResults.findJSK3DetailedResults.do";

    // 时时乐
    private final static String URL_SSLDetailed = "detailedLotteryResults.findSSLDetailedResults.do";

    // 广西快3
    private final static String URL_GXK3Detailed = "detailedLotteryResults.findGXK3DetailedResults.do";

    // 幸运农场
    private final static String URL_CQKL10Detailed = "detailedLotteryResults.findCQKL10DetailedResults.do";

    // 山东11选5
    private final static String URL_SD11X5Detailed = "detailedLotteryResults.findSD11X5DetailedResults.do";

    // 天津时时彩
    private final static String URL_TJSSCDetailed = "detailedLotteryResults.findTJSSCDetailedResults.do";

    // 双色球
//    private final static String URL_SSQDetailed = "detailedLotteryResults.findSSQAndroidDetailedResults.do";
    private final static String URL_SSQDetailed = "detailedLotteryResults.findSSQAndroidSecDetailedResults.do";

    // 排列3
//    private final static String URL_PL3Detailed = "detailedLotteryResults.findPL3AndroidDetailedResults.do";
    private final static String URL_PL3Detailed = "detailedLotteryResults.findPL3AndroidSecDetailedResults.do";

    // 排列5
//    private final static String URL_PL5Detailed = "detailedLotteryResults.findPL5AndroidDetailedResults.do";
    private final static String URL_PL5Detailed = "detailedLotteryResults.findPL5AndroidSecDetailedResults.do";

    // 福彩3D
    private final static String URL_F3DDetailed = "detailedLotteryResults.findF3DAndroidDetailedResults.do";

    // 七乐彩
    private final static String URL_QLCDetailed = "detailedLotteryResults.findQLCAndroidDetailedResults.do";

    // 大乐透
//    private final static String URL_DLTDetailed = "detailedLotteryResults.findDLTAndroidDetailedResults.do";
    private final static String URL_DLTDetailed = "detailedLotteryResults.findDLTAndroidSecDetailedResults.do";

    // 胜负彩
    private final static String URL_SFCDetailed = "detailedLotteryResults.findSFCAndroidSecDetailedResults.do";

    // 6场半全场
    private final static String URL_LCBQCDetailed = "detailedLotteryResults.findZC6AndroidSecDetailedResults.do";

    // 4场进球
    private final static String URL_SCJQDetailed = "detailedLotteryResults.findJQ4AndroidSecDetailedResults.do";

    /*
     *
     * 获取当前期的开奖结果及下一期的开奖时间
     */
    // 香港开奖
    private final static String URL_LHCNextAndNew = "nextAndNewLotteryResults.findLhcNextAndNewResults.do";

    // 七星彩开奖
    private final static String URL_QXCCNextAndNew = "nextAndNewLotteryResults.findQXCNextAndNewResults.do";


    // 重庆时时彩
    private final static String URL_CQSSCNextAndNew = "detailedLotteryResults.findCQSSCNextAndNewResults.do";

    // 江西时时彩
    private final static String URL_JXSSCNextAndNew = "detailedLotteryResults.findJXSSCNextAndNewResults.do";

    // 新疆时时彩
    private final static String URL_XJSSCNextAndNew = "detailedLotteryResults.findXJSSCNextAndNewResults.do";

    // 云南时时彩
    private final static String URL_YNSSCNextAndNew = "detailedLotteryResults.findYNSSCNextAndNewResults.do";

    // 七星彩
    private final static String URL_QXCNextAndNew = "detailedLotteryResults.findQXCNextAndNewResults.do";

    // 广东11选5
    private final static String URL_D11X5NextAndNew = "detailedLotteryResults.findD11X5NextAndNewResults.do";

    // 广东快乐10分
    private final static String URL_DKL10NextAndNew = "detailedLotteryResults.findDKL10NextAndNewResults.do";

    // 湖北11选5
    private final static String URL_HB11X5NextAndNew = "detailedLotteryResults.findHB11X5NextAndNewResults.do";

    // 江苏11选5
    private final static String URL_JS11X5NextAndNew = "detailedLotteryResults.findJS11X5NextAndNewResults.do";

    // 江西11选5
    private final static String URL_JX11X5NextAndNew = "detailedLotteryResults.findJX11X5NextAndNewResults.do";

    // 安徽快3
    private final static String URL_AHK3NextAndNew = "detailedLotteryResults.findAHK3NextAndNewResults.do";

    // 湖南快乐10分
    private final static String URL_HNKL10NextAndNew = "detailedLotteryResults.findHNKL10NextAndNewResults.do";

    // 快乐8
    private final static String URL_KL8NextAndNew = "detailedLotteryResults.findKL8NextAndNewResults.do";

    // 吉林快3
    private final static String URL_JLK3NextAndNew = "detailedLotteryResults.findJLK3NextAndNewResults.do";

    // 辽宁11选5
    private final static String URL_LN11X5NextAndNew = "detailedLotteryResults.findLN11X5NextAndNewResults.do";

    // 北京赛车
    private final static String URL_PK10NextAndNew = "detailedLotteryResults.findPK10NextAndNewResults.do";

    // 江苏快3
    private final static String URL_JSK3NextAndNew = "detailedLotteryResults.findJSK3NextAndNewResults.do";

    // 时时乐
    private final static String URL_SSLNextAndNew = "detailedLotteryResults.findSSLNextAndNewResults.do";

    // 广西快3
    private final static String URL_GXK3NextAndNew = "detailedLotteryResults.findGXK3NextAndNewResults.do";

    // 幸运农场
    private final static String URL_CQKL10NextAndNew = "detailedLotteryResults.findCQKL10NextAndNewResults.do";

    // 山东11选5
    private final static String URL_SD11X5NextAndNew = "detailedLotteryResults.findSD11X5NextAndNewResults.do";

    // 天津时时彩
    private final static String URL_TJSSCNextAndNew = "detailedLotteryResults.findTJSSCNextAndNewResults.do";

    //赛场
    private final static String URL_FOOTBALL_DETAIL = "footBallMatch.queryAndroidMatchInfos.do";

    //赛场新接口
    private final static String URL_FOOTBALL_DETAIL_FIRST = "footBallMatch.queryAndroidFirstMatchInfos.do";


    //赛场——点赞
    private final static String URL_FOOTBALL_DETAIL_LIKE = "footBallMatch.updLike.do";
    private final static String URL_BASKETBALLBALL_DETAIL_LIKE = "basketballDetail.updLike.do";

    //直播加载更多

    private final static String URL_FOOTBALL_LIVE_TEXT = "footBallMatch.findPreLiveText.do";

    //检查是否漏消息
    private final static String URL_FOOTBALL_IS_LOST_MSG = "footBallMatch.findLiveTextDirect.do";


    //数据统计
    private final static String URL_FOOTBALL_DETAIL_STATISTICAL_DATA = "footBallMatch.findStatisticalDatas.do";
    //指数分析
    private final static String FOOTBALLMATCH_MATCHODD = "footBallMatch.matchOdd.do";
    //指数详情
    private final static String FOOTBALLMATCH_MATCHODD_DETAILS = "footBallMatch.matchOddDetail.do";

    //分析
    private final static String URL_FOOTBALL_ANALYSIS = "footBallMatch.matchAnalysis.do";
    // 阵容首发
    private final static String URL_FOOTBALL_DETAIL_FINDLINEUP = "footBallMatch.findLineUp.do";
    // 角球走势
    private final static String URL_FOOTBALL_DETAIL_FINDCORNERKICKCOUNTS = "footBallMatch.findCornerKickCounts.do";
    // 攻防走势
    private final static String URL_FOOTBALL_DETAIL_FINDDEFENSECOUNTS = "footBallMatch.findDefenseCounts.do";
    // 角球和攻防走势
    private final static String URL_FOOTBALL_DETAIL_FINDCORNERANDDANGER = "footBallMatch.findCornerAndDanger.do";
    //视频直播
    public final static String URL_FOOTBALL_DETAIL_URL_MATCHVIDEO_DATA = URL_API_HOST + URL_MATCHVIDEO_DATA;
    // 二期改版首页接口
//    private final static String URL_HOME_PAGER_DATA = "mainPage.findAndroidMainRsts.do";
    private final static String URL_HOME_PAGER_DATA = "mainPage.findAndroidLotteryMainRsts.do";// 1.2.0版本首页新接口


    //足球内页改版滚球赔率接口和详情接口

    private final static String URL_FOOTBALL_DETAIL_BALLLISTOVERVIEW = "footballBallList.ballListOverview.do";

    private final static String URL_FOOTBALL_DETAIL_BALLLISTDETAIL = "footballBallList.ballListDetail.do";


    /**
     * 彩票开奖
     */

    // 所有彩票最后一期开奖结果
    public final static String URL_LASTLOTTERYResults = URL_API_HOST + URL_LASTLOTTERY;

    // 香港开奖
    public final static String URL_LHCDetailedResults = URL_API_HOST + URL_LHCDetailed;

    // 重庆时时彩
    public final static String URL_CQSSCDetailedResults = URL_API_HOST + URL_CQSSCDetailed;

    // 江西时时彩
    public final static String URL_JXSSCDetailedResults = URL_API_HOST + URL_JXSSCDetailed;

    // 新疆时时彩
    public final static String URL_XJSSCDetailedResults = URL_API_HOST + URL_XJSSCDetailed;

    // 云南时时彩
    public final static String URL_YNSSCDetailedResults = URL_API_HOST + URL_YNSSCDetailed;

    // 七星彩
    public final static String URL_QXCDetailedResults = URL_API_HOST + URL_QXCDetailed;

    // 广东11选5
    public final static String URL_D11X5DetailedResults = URL_API_HOST + URL_D11X5Detailed;

    // 广东快乐10分
    public final static String URL_DKL10DetailedResults = URL_API_HOST + URL_DKL10Detailed;

    // 湖北11选5
    public final static String URL_HB11X5DetailedResults = URL_API_HOST + URL_HB11X5Detailed;

    // 江苏11选5
    public final static String URL_JS11X5DetailedResults = URL_API_HOST + URL_JS11X5Detailed;

    // 江西11选5
    public final static String URL_JX11X5DetailedResults = URL_API_HOST + URL_JX11X5Detailed;

    // 安徽快3
    public final static String URL_AHK3DetailedResults = URL_API_HOST + URL_AHK3Detailed;

    // 湖南快乐10分
    public final static String URL_HNKL10DetailedResults = URL_API_HOST + URL_HNKL10Detailed;

    // 快乐8
    public final static String URL_KL8DetailedResults = URL_API_HOST + URL_KL8Detailed;

    // 吉林快3
    public final static String URL_JLK3DetailedResults = URL_API_HOST + URL_JLK3Detailed;

    // 辽宁11选5
    public final static String URL_LN11X5DetailedResults = URL_API_HOST + URL_LN11X5Detailed;

    // 北京赛车
    public final static String URL_PK10DetailedResults = URL_API_HOST + URL_PK10Detailed;

    // 江苏快3
    public final static String URL_JSK3DetailedResults = URL_API_HOST + URL_JSK3Detailed;

    // 时时乐
    public final static String URL_SSLDetailedResults = URL_API_HOST + URL_SSLDetailed;

    // 广西快3
    public final static String URL_GXK3DetailedResults = URL_API_HOST + URL_GXK3Detailed;

    // 幸运农场
    public final static String URL_CQKL10DetailedResults = URL_API_HOST + URL_CQKL10Detailed;

    // 山东11选5
    public final static String URL_SD11X5DetailedResults = URL_API_HOST + URL_SD11X5Detailed;

    // 天津时时彩
    public final static String URL_TJSSCDetailedResults = URL_API_HOST + URL_TJSSCDetailed;

    // 双色球
    public final static String URL_SSQDetailedResults = URL_API_HOST + URL_SSQDetailed;

    // 排列3
    public final static String URL_PL3DetailedResults = URL_API_HOST + URL_PL3Detailed;

    // 排列5
    public final static String URL_PL5DetailedResults = URL_API_HOST + URL_PL5Detailed;

    // 福彩3D
    public final static String URL_F3DDetailedResults = URL_API_HOST + URL_F3DDetailed;

    // 七乐彩
    public final static String URL_QLCDetailedResults = URL_API_HOST + URL_QLCDetailed;

    // 大乐透
    public final static String URL_DLTDetailedResults = URL_API_HOST + URL_DLTDetailed;

    // 胜负彩
    public final static String URL_SFCDetailedResults = URL_API_HOST + URL_SFCDetailed;

    // 6场半全场
    public final static String URL_LCBQCDetailedResults = URL_API_HOST + URL_LCBQCDetailed;

    // 4场进球
    public final static String URL_SCJQDetailedResults = URL_API_HOST + URL_SCJQDetailed;

    /*
     *
     * 获取当前期的开奖结果及下一期的开奖时间
     */
    // 香港开奖
    public final static String URL_LHCNextAndNewResults = URL_API_HOST + URL_LHCNextAndNew;

    // 七星彩
    public final static String URL_QXCCNextAndNewResults = URL_API_HOST + URL_QXCCNextAndNew;


    // 重庆时时彩
    public final static String URL_CQSSCNextAndNewResults = URL_API_HOST + URL_CQSSCNextAndNew;

    // 江西时时彩
    public final static String URL_JXSSCNextAndNewResults = URL_API_HOST + URL_JXSSCNextAndNew;

    // 新疆时时彩
    public final static String URL_XJSSCNextAndNewResults = URL_API_HOST + URL_XJSSCNextAndNew;

    // 云南时时彩
    public final static String URL_YNSSCNextAndNewResults = URL_API_HOST + URL_YNSSCNextAndNew;

    // 七星彩
    public final static String URL_QXCNextAndNewResults = URL_API_HOST + URL_QXCNextAndNew;

    // 广东11选5
    public final static String URL_D11X5NextAndNewResults = URL_API_HOST + URL_D11X5NextAndNew;

    // 广东快乐10分
    public final static String URL_DKL10NextAndNewResults = URL_API_HOST + URL_DKL10NextAndNew;

    // 湖北11选5
    public final static String URL_HB11X5NextAndNewResults = URL_API_HOST + URL_HB11X5NextAndNew;

    // 江苏11选5
    public final static String URL_JS11X5NextAndNewResults = URL_API_HOST + URL_JS11X5NextAndNew;

    // 江西11选5
    public final static String URL_JX11X5NextAndNewResults = URL_API_HOST + URL_JX11X5NextAndNew;

    // 安徽快3
    public final static String URL_AHK3NextAndNewResults = URL_API_HOST + URL_AHK3NextAndNew;

    // 湖南快乐10分
    public final static String URL_HNKL10NextAndNewResults = URL_API_HOST + URL_HNKL10NextAndNew;

    // 快乐8
    public final static String URL_KL8NextAndNewResults = URL_API_HOST + URL_KL8NextAndNew;

    // 吉林快3
    public final static String URL_JLK3NextAndNewResults = URL_API_HOST + URL_JLK3NextAndNew;

    // 辽宁11选5
    public final static String URL_LN11X5NextAndNewResults = URL_API_HOST + URL_LN11X5NextAndNew;

    // 北京赛车
    public final static String URL_PK10NextAndNewResults = URL_API_HOST + URL_PK10NextAndNew;

    // 江苏快3
    public final static String URL_JSK3NextAndNewResults = URL_API_HOST + URL_JSK3NextAndNew;

    // 时时乐
    public final static String URL_SSLNextAndNewResults = URL_API_HOST + URL_SSLNextAndNew;

    // 广西快3
    public final static String URL_GXK3NextAndNewResults = URL_API_HOST + URL_GXK3NextAndNew;

    // 幸运农场
    public final static String URL_CQKL10NextAndNewResults = URL_API_HOST + URL_CQKL10NextAndNew;

    // 山东11选5
    public final static String URL_SD11X5NextAndNewResults = URL_API_HOST + URL_SD11X5NextAndNew;

    // 天津时时彩
    public final static String URL_TJSSCNextAndNewResults = URL_API_HOST + URL_TJSSCNextAndNew;

    /**
     * 体育彩票
     */

    private final static String URL_MAIN = "matchResults.main.findMainResult.do";

    //即时(增加队徽请求字段)
    private final static String URL_IMMEDIATE = "androidMatchResults.findImmediateMatchs.do";

    // 滚球
    private final static String URL_ROLLBALL = "matchResults.findBallList.do";

    // 热门和焦点
    private final static String URL_HOT = "matchResults.findHotRaceIds.do";

    //赛果(增加队徽请求字段)
    private final static String URL_RESULT = "androidMatchResults.findResultOfBallMatch.do";// 新接口

    //赛程(增加队徽请求字段)
    private final static String URL_CEASELESS = "androidMatchResults.findCeaselessMatch.do";

    //关注(增加队徽请求字段)
    private final static String URL_FOCUS = "androidMatchResults.findFocusMatchs.do";


    // 刷新
    private final static String URL_INCREMENT = "matchResults.findIncrementMatchs.do";

    public final static String URL_MainMatchs = URL_API_HOST + URL_MAIN;

    public final static String URL_ImmediateMatchs = URL_API_HOST + URL_IMMEDIATE;

    public final static String URL_Rollball = URL_API_HOST + URL_ROLLBALL;

    public final static String URL_ResultMatchs = URL_API_HOST + URL_RESULT;

    public final static String URL_CeaselessMatchs = URL_API_HOST + URL_CEASELESS;

    public final static String URL_FocusMatchs = URL_API_HOST + URL_FOCUS;

    public final static String URL_IncrementMatchs = URL_API_HOST + URL_INCREMENT;

    public final static String URL_Hot_focus = URL_API_HOST + URL_HOT;

    //赛场
    public final static String URL_FOOTBALL_DETAIL_INFO_FIRST = URL_API_HOST + URL_FOOTBALL_DETAIL_FIRST;


    //赛场点赞
    public final static String URL_FOOTBALL_DETAIL_LIKE_INFO = URL_API_HOST + URL_FOOTBALL_DETAIL_LIKE;
    public final static String URL_BASKETBALLBALL_DETAIL_LIKE_INFO = URL_API_HOST + URL_BASKETBALLBALL_DETAIL_LIKE;

    //直播加载更多
    public final static String URL_FOOTBALL_LIVE_TEXT_INFO = URL_API_HOST + URL_FOOTBALL_LIVE_TEXT;

    //是否漏消息
    public final static String URL_FOOTBALL_IS_LOST_MSG_INFO = URL_API_HOST + URL_FOOTBALL_IS_LOST_MSG;


    //赛场统计数据
    public final static String URL_FOOTBALL_DETAIL_STATISTICAL_DATA_INFO = URL_API_HOST + URL_FOOTBALL_DETAIL_STATISTICAL_DATA;
    // 阵容
    public final static String URL_FOOTBALL_DETAIL_FINDLINEUP_INFO = URL_API_HOST + URL_FOOTBALL_DETAIL_FINDLINEUP;

    //走势图表 进攻 危险进攻 射正 射偏

    public final static String URL_FOOTBALL_DETAIL_FINDTRENDFORM_INFO = URL_API_HOST + "trendForm.findTrendForm.do";


    //分析
    public final static String URL_FOOTBALL_DETAIL_ANALYSIS_INFO = URL_API_HOST + URL_FOOTBALL_ANALYSIS;
    // 二期首页
    public final static String URL_HOME_PAGER_INFO = URL_API_HOST + URL_HOME_PAGER_DATA;

    // 版本更新"http://192.168.10.66:8080/mlottery/core/apkDownload.apkUpdate.do";


    //足球内页滚球赔率
    public final static String URL_FOOTBALL_DETAIL_BALLLISTOVERVIEW_INFO = URL_API_HOST + URL_FOOTBALL_DETAIL_BALLLISTOVERVIEW;

    public final static String URL_FOOTBALL_DETAIL_BALLLISTDETAIL_INFO = URL_API_HOST + URL_FOOTBALL_DETAIL_BALLLISTDETAIL;


    //足球分析详情
    public final static String URL_FOOTBALL_HTTP = "http://";
    public final static String URL_FOOTBALL_PROJECT = "/mlottery/core/";
    public final static String URL_FOOTBALL_FINDANALYSISDETAIL = "footBallMatch.findAnalysisDetail.do";

    public final static String URL_FOOTBALL_ANALYZE_DETAILS = URL_FOOTBALL_HTTP + HOST + URL_FOOTBALL_PROJECT + URL_FOOTBALL_FINDANALYSISDETAIL;           // http://192.168.10.242:8181/mlottery/core/footBallMatch.findAnalysisDetail.do";


    // 首页数据测试URL
    public final static String URL_MAINDEMO = URL_API_HOST + "main.findMainResult.do";

    //首页BannerURL
    public final static String URL_MAINBANNER = URL_API_HOST + "androidAds.findPicAdsAche.do";

    // 版本更新"http://192.168.10.66:8080/mlottery/core/apkDownload.apkUpdate.do";

    private final static String VERSION_UPDATE = "apkDownload.apkUpdate.do";

    public final static String URL_VERSION_UPDATE = URL_API_HOST + VERSION_UPDATE;
    //public final static String URL_VERSION_UPDATE = "http://192.168.1.166:8181/mlottery/core/apkDownload.apkUpdate.do";
    //public final static String URL_VERSION_UPDATE = "http://m.1332255.com/mlottery/core/apkDownload.apkUpdate.do";
    /**
     * 保存umeng渠道号，提交渠道号
     */
    public final static String UMENG_CHANNEL_URL = "http://kj.13322.com/traffic/api";// 体彩正式环境
//    public final static String UMENG_CHANNEL_URL = "http://183.61.172.88:8343/traffic/api";// 体彩测试环境

    //用户留存率分析
    public final static String USER_ACTION_ANALYSIS_URL = "http://union.13322.com/traffic/api/appRetention";// 体彩正式环境
//    public final static String USER_ACTION_ANALYSIS_URL = "http://183.61.172.88:8343/traffic/api/appRetention";// 体彩测试环境

    //指数详情
    public final static String URL_FOOTBALL_MATCHODD_DETAILS = URL_API_HOST + FOOTBALLMATCH_MATCHODD_DETAILS;
    //指数分析
    public final static String URL_FOOTBALL_MATCHODD = URL_API_HOST + FOOTBALLMATCH_MATCHODD;
    /**
     * 篮球接口  "http://192.168.10.242:8181/mlottery/core/basketballMatch.findLiveMatch.do";
     */
//	public final static String URL_BASKET_HTTP = "http://192.168.10.242:8181"; //测试
    public final static String URL_BASKET_HTTP = "http://";
    public final static String URL_BASKET_PROJECT = "/mlottery/core/";
    //即时字段
    public final static String URL_BASKET_FINDLIVEMATCH = "basketballMatch.findLiveMatch.do";
    //赛果字段
    public final static String URL_BASKET_FINDFINISHEDNATCH = "basketballMatch.findFinishedMatch.do";
    //赛程字段
    public final static String URL_BASKET_FINDSCHEDULEDNATCH = "basketballMatch.findScheduledMatch.do";
    //关注字段
    public final static String URL_BASKET_FINDFAVOURITEMATCH = "basketballMatch.findFavouriteMatch.do";
    //头部比分字段
    public final static String URL_BASKET_FINDSCORE = "basketballDetail.findScore.do";
    //篮球赔率界面接口
    public final static String URL_BASKET_FINDODDS = "basketballDetail.findOdds.do";

    //即时接口  //HOST ==>  192.168.10.242:8181 / m.1332255.com
    public final static String URL_BASKET_IMMEDIATE = URL_BASKET_HTTP + HOST + URL_BASKET_PROJECT + URL_BASKET_FINDLIVEMATCH;
    //赛果接口
    public final static String URL_BASKET_RESULT = URL_BASKET_HTTP + HOST + URL_BASKET_PROJECT + URL_BASKET_FINDFINISHEDNATCH;
    //赛程接口
    public final static String URL_BASKET_SCHEDULE = URL_BASKET_HTTP + HOST + URL_BASKET_PROJECT + URL_BASKET_FINDSCHEDULEDNATCH;
    //头部字段
    public final static String URL_BASKET_DETAILS = HTTP + HOST + URL_BASKET_PROJECT + URL_BASKET_FINDSCORE;
    //篮球赔率界面
    public final static String URL_BASKET_ODDS = HTTP + HOST + URL_BASKET_PROJECT + URL_BASKET_FINDODDS;

    //篮球推送
//	public final static String URL_BASKET_SOCKET = "ws://192.168.10.242:61634/ws";
//	public final static String URL_BASKET_SOCKET = "ws://m.1332255.com/ws";
//	public final static String URL_BASKET_SOCKET = "ws://"+ HOST + "/ws";
    public final static String URL_BASKET_SOCKET = "ws://" + WS_HOST;

    /**
     * 篮球详情接口
     */

    //篮球资料库
    public final static String URL_BASKET_INFORMATION = URL_API_HOST + "basketballData.findLeagueHierarchy.do";


    //篮球资料库详情
    public final static String URL_BASKET_DATABASE_DETAILS = URL_API_HOST + "basketballData.findLeagueHeader.do"; // http://192.168.31.43:8888/mlottery/core/basketballData.findLeagueHeader.do
    // 赛程
    public final static String URL_BASKET_DATABASE_SCHEDULE = URL_API_HOST + "basketballData.findSchedule.do";
    // 排行
    public final static String URL_BASKET_DATABASE_RANKING = URL_API_HOST + "basketballData.findRanking.do";

    //篮球资料库积分
//    public final static String URL_FOOTBALL_DATABASE_INTEGRAL = "http://192.168.10.242:8181/mlottery/core/basketballData.findRanking117.do";
    public final static String URL_FOOTBALL_DATABASE_INTEGRAL = URL_API_HOST + "basketballData.findRanking117.do";
    //让分盘
    public final static String URL_BASKET_DATABASE_HANDICAP_DETAILS = URL_API_HOST + "basketballData.findAsiaLet.do"; // http://192.168.31.43:8888/mlottery/core/basketballData.findAsiaLet.do
    //大小盘
    public final static String URL_BASKET_DATABASE_BIG_SMALL_DETAILS = URL_API_HOST + "basketballData.findAsiaSize.do"; // "http://192.168.31.43:8888/mlottery/core/basketballData.findAsiaSize.do";

    //统计
    public final static String URL_BASKET_DATABASE_STATISTIC_DETAILS = URL_API_HOST + "basketballData.findStatistic.do"; // "http://192.168.31.43:8888/mlottery/core/basketballData.findAsiaSize.do";

    public final static String URL_BASKET_FINDANALYSIS = "basketballDetail.findAnalysis.do";
    public final static String URL_BASKET_FINDANALYSISDETAIL = "basketballDetail.findAnalysisDetail.do";
    public final static String URL_BASKET_FINDODDSDETAIL = "basketballDetail.findOddsDetail.do";
    //篮球分析
    public final static String URL_BASKET_ANALYZE = URL_BASKET_HTTP + HOST + URL_BASKET_PROJECT + URL_BASKET_FINDANALYSIS; //"http://192.168.10.242:8181/mlottery/core/basketballDetail.findAnalysis.do";
    //篮球分析详情
    public final static String URL_BASKET_ANALYZE_DETAILS = URL_BASKET_HTTP + HOST + URL_BASKET_PROJECT + URL_BASKET_FINDANALYSISDETAIL; //"http://192.168.10.242:8181/mlottery/core/basketballDetail.findAnalysisDetail.do";

    //赔率详情
    public final static String URL_BASKET_ODDS_DETAILS = URL_BASKET_HTTP + HOST + URL_BASKET_PROJECT + URL_BASKET_FINDODDSDETAIL; //http://192.168.10.242:8181/mlottery/core/basketballDetail.findOddsDetail.do

    //足球联赛轮次请求url
    public final static String URL_FOOTBALL_LEAGUEROUND = URL_API_HOST + "footballLeagueData.qLeagueRound.do";
    //足球联赛赛程请求url
    public final static String URL_FOOTBALL_LEAGUERACE = URL_API_HOST + "footballLeagueData.qLeagueRace.do";

    //足球资料库
    public final static String URL_FOOTBALL_DATABASE = URL_API_HOST + "androidLeagueData.findAndroidDataMenu.do";

    public final static String URL_FOOTBALL_DATABASE_SCHEDULE_FIRST = URL_API_HOST + "androidLeagueData.findAndroidLeagueRound.do";//"http://192.168.31.8:8080/mlottery/core/androidLeagueData.findAndroidLeagueRound.do";
    public final static String URL_FOOTBALL_DATABASE_SCHEDULE_UNFIRST = URL_API_HOST + "androidLeagueData.findAndroidLeagueRace.do";//"http://192.168.31.8:8080/mlottery/core/androidLeagueData.findAndroidLeagueRace.do";
    //足球资料库积分
    public final static String URL_FOOTBALL_DATABASE_INTEGRAL_FIRST = URL_API_HOST + "androidLeagueData.findAndroidLeagueScore.do";//"http://192.168.31.8:8080/mlottery/core/androidLeagueData.findAndroidLeagueScore.do";
    //足球资料库详情头部
    public final static String URL_FOOTBALL_DATABASE_HEADER = URL_API_HOST + "androidLeagueData.findAndroidFootballLeagueHeader.do";//http://192.168.31.8:8080/mlottery/core/androidLeagueData.findAndroidFootballLeagueHeader.do

    //让分盘
    public final static String URL_FOOTBALL_DATABASE_HANDICAP_DETAILS = URL_API_HOST + "androidLeagueData.findAndroidLeagueHdp.do"; // http://192.168.33.32:8080/mlottery/core/androidLeagueData.findAndroidLeagueHdp.do
    //大小盘
    public final static String URL_FOOTBALL_DATABASE_BIG_SMALL_DETAILS = URL_API_HOST + "androidLeagueData.findAndroidLeagueOu.do"; // "http://192.168.33.32:8080/mlottery/core/androidLeagueData.findAndroidLeagueOu.do";

    //统计
    public final static String URL_FOOTBALL_DATABASE_STATISTIC_DETAILS = URL_API_HOST + "androidLeagueData.findAndroidStatics.do"; // "http://192.168.33.32:8080/mlottery/core/androidLeagueData.findAndroidStatics.do";


    //足球赛事提点
    public final static String URL_LEAGUESTATISTICSTODAY = URL_API_HOST + "toDayMatchStatistics.findTodayMatchStatistics.do";

    //联赛列表
    public final static String URL_QLIBARYLEAGUES = "footballLeagueData.qLibaryLeagues.do";
    public final static String URL_INFORMATION = URL_API_HOST + URL_QLIBARYLEAGUES;
    //查询联赛所有赛季时间和默认积分榜
    public final static String URL_QLEAGUEDATE = "footballLeagueData.qLeagueDate.do";
    public final static String URL_FOOT_QLEAGUEDATE = URL_API_HOST + URL_QLEAGUEDATE;
    //查询联赛详情
    public final static String URL_QLEAGUESCORE = "footballLeagueData.qLeagueScore.do";
    public final static String URL_FOOT_QLEAGUESCORE = URL_API_HOST + URL_QLEAGUEDATE;
    // 联赛 qLeagueRace

    //足球资讯列表请求url
    public final static String URL_FOOTBALL_INFOLIST = URL_API_HOST + "info.findAndroidInfoLstWithVideo.do";
    //足球资讯头和首页请求url
    public final static String URL_FOOTBALL_INFOINDEX = URL_API_HOST + "info.findAndroidIndexInfoWithVideo.do";

    public final static String URL_FEEDBACK_ADD = URL_API_HOST + "feedback.addFeedBack.do";
    /**
     * 新版指数
     */
    public final static String URL_NEW_ODDS = URL_API_HOST + "footBallIndexCenter.findAndroidIndexCenter.do";
    /**
     * 指数推送
     */
    public final static String URL_CPI_SOCKET = WS_SERVICE;
    /**
     * 新版足球分析接口
     */
    public final static String URL_NEW_ANALYZE = URL_API_HOST + "footBallMatch.findAnalysisOverview.do";

    //开机屏地址获取
    public final static String STARTPIC = "mainPage.findAndroidStartupPic.do";
    public final static String URL_STARTPIC = URL_API_HOST + STARTPIC;
    /**
     * 发送验证码
     */
    public final static String URL_SENDSMSCODE = URL_API_HOST + "androidUserCenter.sendSmsCode.do";
    /**
     * 注册
     */
    public final static String URL_REGISTER = URL_API_HOST + "androidUserCenter.register.do";
    /**
     * 登录
     */
    public final static String URL_LOGIN = URL_API_HOST + "androidUserCenter.login.do";
    /**
     * 登出
     */
    public final static String URL_LOGOUT = URL_API_HOST + "androidUserCenter.logout.do";
    /**
     * 重置密码
     */
    public final static String URL_RESETPASSWORD = URL_API_HOST + "androidUserCenter.resetPassword.do";
    /**
     * 修改昵称
     */
    public final static String URL_EDITNICKNAME = URL_API_HOST + "androidUserCenter.editNickname.do";
    /**
     * 修改密码
     */
    public final static String URL_CHANGEPASSWORD = URL_API_HOST + "androidUserCenter.changePassword.do";

    /**
     * 资讯Id查询
     */
    public final static String URL_INFORMATION_BY_THIRDID = URL_API_HOST + "info.findSingleInfo.do";
    /*QQ登录*/
    public final static String URL_QQ_LOGIN = URL_API_HOST + "androidUserCenter. loginQQ.do";
    /*QQ登录*/
    public final static String URL_SINA_LOGIN = URL_API_HOST + "androidUserCenter.loginWeibo.do";

    public final static String URL_WEIXIN_LOGIN = URL_API_HOST + "androidUserCenter.loginWeChat.do";
    /**
     * 足球情报 - 大数据预测
     */
    public final static String URL_INTELLIGENCE_BIG_DATA = URL_API_HOST + "footBallMatch.findIntelligence.do";


    /*模糊搜索*/
    public final static String FUZZYSEARCH = "/basketballData.fuzzySearch.do";

    /**
     * --融云 获取用户Token URL--
     */
    public final static String RONG_USER_TOKEN = "https://api.cn.ronghub.com/user/getToken.json";
    /**
     * --融云 获取聊天室ID URL--
     */
    public final static String RONG_CHARTROOM_ID = "https://api.cn.ronghub.com/chatroom/create.json";
    /**
     * --融去 获取聊天室人数 URL--
     */
    public final static String RONG_CHARTROOM_COUNT = "https://api.cn.ronghub.com/chatroom/user/query.json";
    /*头像图片上传*/
    public final static String UPLOADIMAGE = NEW_URL_API_PHOST + "uploadImage.do";
    /*头像URL上传*/
    public final static String UPDATEHEADICON = URL_API_HOST + "androidUserCenter.updateHeadIcon.do";

    // 情报中心
    public final static String URL_INFO_CENTER = URL_API_HOST + "footBallMatch.findIntelligenceList.do";
    /*视频直播*/
    public final static String VIDEOINFO = URL_API_HOST + "matchVideo.findAndroidVideoInfo.do";
    /*模糊搜索*/
    public final static String SEARCHMATCHLEAGUES = "/androidLeagueData.searchMatchLeagues.do";
    /*检测第三方登录*/
    public final static String TESTINGISLOGIN = URL_API_HOST + "systemSettings.findThirdPartyLoginSettings.do";


    /**
     * 国外资讯
     */
    public final static String URL_FOREIGN_INFOMATION = URL_API_HOST + "overseasInformation.findOverseasInformation.do";
    public final static String URL_FOREIGN_INFOMATION_DETAILS_UPDLIKE = URL_API_HOST + "overseasInformation.updLike.do";

    /**
     * 关注绑定用户与推送
     */
    //足球关注界面url
    public final static String FOCUS_FRAGMENT_CONCERN = URL_API_HOST + "pushSetting.findFocusMatchs.do";
    //足球界面点击关注跟取消关注
    public final static String FOOTBALL_ADD_FOCUS = URL_API_HOST + "pushSetting.followMatch.do";
    //是否接受推送通知
    public final static String FOOTBALL_USER_SET = URL_API_HOST + "pushSetting.followUserPushSetting.do";
    //获取足球关注列表
    public final static String FOOTBALL_FIND_MATCH = URL_API_HOST + "pushSetting.loginUserFindMatch.do";
    //篮球关注页面url
    public final static String BASKET_FOCUS = URL_API_HOST + "androidBasketballMatch.findCancelAfterConcernList.do";
    //篮球点击关注
    public final static String BASKETBALL_ADD_FOCUS = URL_API_HOST + "androidBasketballMatch.customConcernVS.do";
    //取消
    public final static String BASKETBALL_DELETE_FOCUS = URL_API_HOST + "androidBasketballMatch.cancelCustomConcernVS.do";
    //是否接受通知
    public final static String BASKET_USER_SET = URL_API_HOST + "androidBasketballMatch.updatePushStatus.do";
    //获取篮球关注列表
    public final static String BASKET_FIND_MATCH = URL_API_HOST + "androidBasketballMatch.findConcernVsThirdIds.do";

    //用户注销
    public final static String EXIT_PUSH_ONLINE = URL_API_HOST + "pushSetting.exitUpdateOnlile.do";
    //上传性别的公用接口
    public final static String UPDATEUSERINFO = URL_API_HOST + "androidUserCenter.updateUserInfo.do";
    //明星头像
    public final static String FINDHEADICONS = URL_API_HOST + "systemSettings.findHeadIcons.do";

    //篮球内页文字直播
    public final static String BASKET_DETAIL_TEXTLIVE = URL_API_HOST + "basketballDetail.findTextLive.do";  //basketballDetail  改接口
    //篮球内页球队统计
    public final static String BASKET_DETAIL_TEAM = URL_API_HOST + "basketballDetail.findTeamStats.do";
    //篮球内页球员统计
    public final static String BASKET_DETAIL_PLAYER = URL_API_HOST + "basketballDetail.findPlayerStats.do";
    /**
     * 斯洛克
     */
    public final static String SNOOKER_LIST_URL = URL_API_HOST + "snookerMatch.getFirstSnookerMatch.do";
    public final static String SNOOKER_LIST_LOADMORE_URL = URL_API_HOST + "snookerMatch.getSnookerLeagues.do"; // http://m.1332255.com:81/mlottery/core/snookerMatch.getSnookerLeagues.do?dateLeaguesId=2016-11-20_125358,2016-11-19_125358

    /**
     * 我的定制
     */
    //我的定制页
    public final static String CUSTOM_MINE_CUS_URL = URL_API_HOST + "basketballCommonMacth.findBasketballMyConcernMatch.do";//http://192.168.10.242:8181/mlottery/core/basketballCommonMacth.findBasketballMyConcernMatch.do
    //定制完成 发送id
    public final static String CUSTOM_SENDID_CUS_URL = URL_API_HOST + "basketballCommonMacth.customHotLeagueAndTeamConcern.do";//http://192.168.10.242:8181/mlottery/core/basketballCommonMacth.customHotLeagueAndTeamConcern.do
    //定制列表页
    public final static String CUSTOM_LIST_CUS_URL = URL_API_HOST + "basketballCommonMacth.findHotLeagueAndTeamConcern.do";//"http://192.168.10.242:8181/mlottery/core/basketballCommonMacth.findHotLeagueAndTeamConcern.do";


    //精彩瞬间个数
    public final static String FOOTBALL_DETAIL_COLLECTION_COUNT = URL_API_HOST + "operation.findMatchCollectionCount.do";

    //精彩瞬间资源
    public final static String FOOTBALL_DETAIL_COLLECTION = URL_API_HOST + "operation.findMatchCollection.do";

    //产品建议列表
    public final static String PRODUCT_ADVICE_LIKE = URL_API_HOST + "feedback.addFeedBackLikes.do";
    //产品建议列表
    public final static String PRODUCT_ADVICE_LIKE_LIST = URL_API_HOST + "feedback.findAndroidFeedBackDetail.do";

    // 聊球消息列表接口--足球
    public final static String MESSAGE_LIST_FOOTBALL = HTTP + HOST + URL_SPLITTER + "chatServer/chat/chatHistoryList/football";
    // 聊球发送消息--足球
    public final static String MESSAGE_SEND_FOOTBALL = HTTP + HOST + URL_SPLITTER + "chatServer/chat/sendChatMessage/android/football";
    // 聊球消息列表接口--篮球
    public final static String MESSAGE_LIST_BASKET = HTTP + HOST + URL_SPLITTER + "chatServer/chat/chatHistoryList/basketball";
    // 聊球发送消息--篮球
    public final static String MESSAGE_SEND_BASKET = HTTP + HOST + URL_SPLITTER + "chatServer/chat/sendChatMessage/android/basketball";
    // 聊球举报接口
    public final static String REPORT_USER = HTTP + HOST + URL_SPLITTER + "chatServer/chat/reportUser/android";

    // 香港彩详情页——统计接口
    public final static String LOTTERY_INFO_STARTIS_URL = URL_API_HOST + "chartLottery.findAndroidStatisChartDate.do";
    // 香港彩详情页——图表接口
    public final static String LOTTERY_INFO_CHART_URL = URL_API_HOST + "chartLottery.findLastAndroidLotteryResults.do";

    //多屏篮球列表
    public final static String MULTIPLE_BASKET_LIST_URL = URL_API_HOST + "basketballMatch.findMultiScreenMatchList.do";//"http://192.168.33.71:8080/mlottery/core/basketballMatch.findMultiScreenMatchList.do";

    // 邀请码
    public final static String INVITED_RUL = URL_API_HOST + "androidUserCenter.getInviteCode.do";


    /*****************************************************************************************************************
     *下面URL为HTML页面
     *
     * 注意和接口区分URL拼接方式
     */

    //足球内页赛事分享
    private final static String URL_FOOTBALL_DETAIL_SHARE = "live/bifen/index.html?id=";
    //足球内页赛事分享
    public final static String URL_FOOTBALL_DETAIL_INFO_SHARE = HTTP + HOST + URL_SPLITTER + URL_FOOTBALL_DETAIL_SHARE;

    public final static String URL_FOOTBALLDETAIL_H5 = HTTP + HOST + URL_SPLITTER + "live/footballodds_graphic.html";

    public final static String URL_BASKETBALLDETAIL_H5 = HTTP + HOST + URL_SPLITTER + "live/basket_graphic.html";

    //邀请码分享
    public final static String INVITED_ACTIVITY_URL = HTTP + HOST + URL_SPLITTER + "download_yqm.html";


    /***********************************************************************************************************************/

    //                                                     _(\_/)
    //                                                   ,((((^`\
    //                                                  ((((  (6 \
    //                                                ,((((( ,    \
    //                            ,,,_              ,(((((  /"._  ,`,
    //                           ((((\\ ,...       ,((((   /    `-.-'
    //                           )))  ;'    `"'"'""((((   (
    //                          (((  /            (((      \
    //                           )) |                      |
    //                          ((  |        .       '     |
    //                          ))  \     _ '      `t   ,.')
    //                          (   |   y;- -,-""'"-.\   \/
    //                          )   / ./  ) /         `\  \
    //                             |./   ( (           / /'
    //                             ||     \\          //'|
    //                             ||      \\       _//'||
    //                             ||       ))     |_/  ||
}