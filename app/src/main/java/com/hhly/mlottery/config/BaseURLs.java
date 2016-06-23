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


    /**
     * 公网
     */
    private static String HOST = getHost();


    private static String getHost() {
        if (AppConstants.isTestEnv) {//开发不需要修改下面代码
            int url_config = PreferenceUtil.getInt(MyConstants.URL_HOME_CONFIG, DebugConfigActivity.URL_1332255);

            if (url_config == DebugConfigActivity.URL_1332255) {
                return "m.1332255.com";
            } else if (url_config == DebugConfigActivity.URL_242) {
//                return "192.168.31.65:8181";
                return "192.168.10.242:8181";
//                return "192.168.12.242:8181";//余勇俊测试
            } else {
                return "m.13322.com";
            }
        }

        return "m.13322.com";

    }

    /**
     * 内网
     */
//	private final static String HOST = "m.1332255.com";
    private final static String HTTP = "http://";

    private final static String HTTPS = "https://";

    private final static String URL_SPLITTER = "/";

    private final static String WS = "ws://";
    /**
     * 推送公网
     */

    private static String WS_HOST = getWsHost();

    private static String getWsHost() {
        if (AppConstants.isTestEnv) {//开发不需要修改下面代码
            int ws_config = PreferenceUtil.getInt(MyConstants.WS_HOME_CONFIG, DebugConfigActivity.WS_242);
            if (ws_config == DebugConfigActivity.WS_82) {
                return "m.1332255.com/ws";
            } else if (ws_config == DebugConfigActivity.WS_242) {
                return "192.168.10.242:61634";
//                return "192.168.12.242:61634";//余勇俊测试
            } else {
                return "m.13322.com/ws";
            }
        }
        return "m.13322.com/ws";//开发，发布改这里
        //	return  "m.13322.com/ws";//开发，发布改这里
    }

    /**
     * 推送内网
     */
//	private final static String WS_HOST = "192.168.10.242:61634";
    public final static String WS_NAME = "happywin";
    public final static String WS_PASSWORD = "happywin";

    public final static String WS_SERVICE = WS + WS_HOST;

    public final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER + "mlottery/core/";
    //视频直播
    private final static String URL_MATCHVIDEO_DATA = "matchVideo.findVideoInfoApp.do";

    public final static String LANGUAGE_PARAM = "lang";

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
    private final static String URL_LASTLOTTERY = "lastLotteryResults.findLastLotteryResults.do";

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
    private final static String URL_QXCDetailed = "detailedLotteryResults.findQXCDetailedResults.do";

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
    //赛场——点赞
    private final static String URL_FOOTBALL_DETAIL_LIKE = "footBallMatch.updLike.do";

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
    private final static String URL_HOME_PAGER_DATA = "mainPage.findAndroidMainRsts.do";


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
    // main.findMainResult.do

    // 即时
    //  private final static String URL_IMMEDIATE = "matchResults.findImmediateMatchs.do";

    //即时(增加队徽请求字段)
    private final static String URL_IMMEDIATE = "androidMatchResults.findImmediateMatchs.do";


    // 热门和焦点
    private final static String URL_HOT = "matchResults.findHotRaceIds.do";


    // 赛果
    //  private final static String URL_RESULT = "appMatchResults.findResultOfBallMatch.do";//

    //赛果(增加队徽请求字段)
    private final static String URL_RESULT = "androidMatchResults.findResultOfBallMatch.do";// 新接口


    // 赛程
    //  private final static String URL_CEASELESS = "appMatchResults.findCeaselessMatch.do";

    //赛程(增加队徽请求字段)
    private final static String URL_CEASELESS = "androidMatchResults.findCeaselessMatch.do";


    // 关注
    //  private final static String URL_FOCUS = "matchResults.findFocusMatchs.do";


    //关注(增加队徽请求字段)
    private final static String URL_FOCUS = "androidMatchResults.findFocusMatchs.do";


    // 刷新
    private final static String URL_INCREMENT = "matchResults.findIncrementMatchs.do";

    public final static String URL_MainMatchs = URL_API_HOST + URL_MAIN;

    public final static String URL_ImmediateMatchs = URL_API_HOST + URL_IMMEDIATE;

    public final static String URL_ResultMatchs = URL_API_HOST + URL_RESULT;

    public final static String URL_CeaselessMatchs = URL_API_HOST + URL_CEASELESS;

    public final static String URL_FocusMatchs = URL_API_HOST + URL_FOCUS;

    public final static String URL_IncrementMatchs = URL_API_HOST + URL_INCREMENT;

    public final static String URL_Hot_focus = URL_API_HOST + URL_HOT;

    //赛场
    public final static String URL_FOOTBALL_DETAIL_INFO = URL_API_HOST + URL_FOOTBALL_DETAIL;
    //赛场点赞
    public final static String URL_FOOTBALL_DETAIL_LIKE_INFO = URL_API_HOST + URL_FOOTBALL_DETAIL_LIKE;

    //直播加载更多
    public final static String URL_FOOTBALL_LIVE_TEXT_INFO = URL_API_HOST + URL_FOOTBALL_LIVE_TEXT;

    //是否漏消息
    public final static String URL_FOOTBALL_IS_LOST_MSG_INFO = URL_API_HOST + URL_FOOTBALL_IS_LOST_MSG;


    //赛场统计数据
    public final static String URL_FOOTBALL_DETAIL_STATISTICAL_DATA_INFO = URL_API_HOST + URL_FOOTBALL_DETAIL_STATISTICAL_DATA;
    // 阵容
    public final static String URL_FOOTBALL_DETAIL_FINDLINEUP_INFO = URL_API_HOST + URL_FOOTBALL_DETAIL_FINDLINEUP;
    // 角球
    public final static String URL_FOOTBALL_DETAIL_FINDCORNERKICKCOUNTS_INFO = URL_API_HOST + URL_FOOTBALL_DETAIL_FINDCORNERKICKCOUNTS;
    // 攻防
    public final static String URL_FOOTBALL_DETAIL_FINDDEFENSECOUNTS_INFO = URL_API_HOST + URL_FOOTBALL_DETAIL_FINDDEFENSECOUNTS;
    // 角球和攻防
    public final static String URL_FOOTBALL_DETAIL_FINDCORNERANDDANGER_INFO = URL_API_HOST + URL_FOOTBALL_DETAIL_FINDCORNERANDDANGER;
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
//	public final static String UMENG_CHANNEL_URL = "http://192.168.10.242:8083/traffic/api";
    public final static String UMENG_CHANNEL_URL = "http://kj.13322.com/traffic/api";

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
    //关注接口
    public final static String URL_BASKET_FOCUS = URL_BASKET_HTTP + HOST + URL_BASKET_PROJECT + URL_BASKET_FINDFAVOURITEMATCH;
    //头部字段
    public final static String URL_BASKET_DETAILS = HTTP + HOST + URL_BASKET_PROJECT + URL_BASKET_FINDSCORE;
    //篮球赔率界面
    public final static String URL_BASKET_ODDS = HTTP + HOST + URL_BASKET_PROJECT + URL_BASKET_FINDODDS;

    //篮球推送
//	public final static String URL_BASKET_SCOKET = "ws://192.168.10.242:61634/ws";
//	public final static String URL_BASKET_SCOKET = "ws://m.1332255.com/ws";
//	public final static String URL_BASKET_SCOKET = "ws://"+ HOST + "/ws";
    public final static String URL_BASKET_SCOKET = "ws://" + WS_HOST;

    /**
     * 篮球详情接口
     */
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
    public final static String URL_FOOTBALL_INFOLIST = URL_API_HOST + "info.findAndroidLstInfo.do";
    //足球资讯头和首页请求url
    public final static String URL_FOOTBALL_INFOINDEX = URL_API_HOST + "info.findAndroidIndexInfo.do";

    public final static String URL_FEEDBACK_ADD = URL_API_HOST + "feedback.addFeedBack.do";
    /**
     * 新版指数
     */
    public final static String URL_NEW_ODDS = URL_API_HOST + "footBallIndexCenter.findAndroidIndexCenter.do";
    /**新版足球分析接口*/
    public final static String URL_NEW_ANALYZE=URL_API_HOST+"footBallMatch.findAnalysisOverview.do";

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


}
