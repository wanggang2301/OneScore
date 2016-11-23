package com.hhly.mlottery.util;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;

import java.util.TimeZone;

public class AppConstants {
	/** 是否是测试环境，true为测试，false为生产环境 */
	public static final boolean isTestEnv = true;
	/** true是国际版|false内地版 */
	public static final boolean isGOKeyboard = false;
	/** true是纯净版|false完整版 */
	public static final boolean fullORsimple = false;
	/** 是否是测试连接，测试连接用于阶段测试使用 */
	public static final boolean isTestLink = false;
	/**
	 * 判断是否上传奔溃日志到友盟
	 */
	public static final boolean isUploadCrash = false;

    /**
     * 项目时区 8中国，7泰国、越南 ，0英语
     */
    public static int timeZone = 8;// 默认为中国时区

    /**
     * 版本升级区分
     */
    public static final String LOCALETYPE_ZH = "1";// 国内版升级
    public static final String LOCALETYPE_I18N = "2";// 国际版升级
    public static final String LOCALETYPE_VN_HN = "20";// 越南北版升级
    public static final String LOCALETYPE_VN = "21";// 越南版升级
    public static final String LOCALETYPE_TH = "22";// 泰语版升级
    public static final String LOCALETYPE_UK = "23";// 英文版升级

    /**
     * 包名
     * @desc 用包名判断当前的时区或参数的选择
     */
    public static final String PACKGER_NAME_ZH = "com.hhly.mlottery";      // 国内版包名
    public static final String PACKGER_NAME_TH = "com.th.hhly.mlottery";   // 泰国版包名
    public static final String PACKGER_NAME_VN_HN = "vn.hn.hhly.mlottery"; // 越南北包名
    public static final String PACKGER_NAME_VN = "vn.hhly.mlottery";       // 越南南包名
    public static final String PACKGER_NAME_UK = "uk.hhly.mlottery";       // 英文版包名

    public static String ACCESS_LINK = "";// API
    public static String USER_AGENT = "";
    public static String AccessKey = "";
    public static String DeviceKey = "";
    public static String VersionNumber = "version=1.0&";// 接口版本号
    public static String TerminalType = "terminalType=2&";// 终端类型1盒子2安卓手机3是IOS
    public static String mac = "mac=1111111&";
    public static String PlayType = "playType=1&";// 播放类型//playType 1 点播|2
    // 直播时移|3回看
    public static String EpisodeId = "episodeId=1&";// 第几集
    public static String PlayTypeTwo = "playType=2&";// 直播
    public static String SearchType = "searchType=0&";// 搜索类型

    // 融云环境KEY
    public static String APPKEY_RONGYUN = "";
    public static String APPSECRET_RONGYUN = "";

    // public static String i18n = "en";// 测试英文语言环境

    // 1至46个数字
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int TEN = 10;
    public static final int ELEVEN = 11;
    public static final int TWELVE = 12;
    public static final int THIRTEEN = 13;
    public static final int FOURTEEN = 14;
    public static final int FIFTEEN = 15;
    public static final int SIRTEEN = 16;
    public static final int SEVENTEEN = 17;
    public static final int EIGHTEEN = 18;
    public static final int NINETEEN = 19;
    public static final int TWENTY = 20;
    public static final int TWENTY_ONE = 21;
    public static final int TWENTY_TWO = 22;
    public static final int TWENTY_THREE = 23;
    public static final int TWENTY_FOUR = 24;
    public static final int TWENTY_FINE = 25;
    public static final int TWENTY_SIX = 26;
    public static final int TWENTY_SEVEN = 27;
    public static final int TWENTY_ENGHT = 28;
    public static final int TWENTY_NINE = 29;
    public static final int THIRTY = 30;
    public static final int THIRTY_ONE = 31;
    public static final int THIRTY_TWO = 32;
    public static final int THIRTY_THREE = 33;
    public static final int THIRTY_FOUR = 34;
    public static final int THIRTY_FIVE = 35;
    public static final int THIRTY_SIX = 36;
    public static final int THIRTY_SEVEN = 37;
    public static final int THIRTY_EIGHT = 38;
    public static final int THIRTY_NINE = 39;
    public static final int FORTY = 40;
    public static final int FORTY_ONE = 41;
    public static final int FORTY_TWO = 42;
    public static final int FORTY_THREE = 43;
    public static final int FORTY_FOUR = 44;
    public static final int FORTY_FIVE = 45;
    public static final int FORTY_SIX = 46;
    public static final int FORTY_SEVEN = 47;
    public static final int FORTY_EIGHT = 48;
    public static final int FORTY_NINE = 49;

    // 北京赛车开奖号码
    public static int[] numberCarNos = {R.mipmap.car_no1, R.mipmap.car_no2, R.mipmap.car_no3, R.mipmap.car_no4, R.mipmap.car_no5, R.mipmap.car_no6, R.mipmap.car_no7,
            R.mipmap.car_no8, R.mipmap.car_no9, R.mipmap.car_no10};

    // 幸运农场开奖号码
    public static int[] numberXYLCs = {R.mipmap.number_bg_xylc01, R.mipmap.number_bg_xylc02, R.mipmap.number_bg_xylc03, R.mipmap.number_bg_xylc04, R.mipmap.number_bg_xylc05,
            R.mipmap.number_bg_xylc06, R.mipmap.number_bg_xylc07, R.mipmap.number_bg_xylc08, R.mipmap.number_bg_xylc09, R.mipmap.number_bg_xylc10, R.mipmap.number_bg_xylc11,
            R.mipmap.number_bg_xylc12, R.mipmap.number_bg_xylc13, R.mipmap.number_bg_xylc14, R.mipmap.number_bg_xylc15, R.mipmap.number_bg_xylc16, R.mipmap.number_bg_xylc17,
            R.mipmap.number_bg_xylc18, R.mipmap.number_bg_xylc19, R.mipmap.number_bg_xylc20};

    // 江苏快三开奖号码
    public static int[] numberKSNos = {R.mipmap.number_bg_ks_no1, R.mipmap.number_bg_ks_no2, R.mipmap.number_bg_ks_no3, R.mipmap.number_bg_ks_no4, R.mipmap.number_bg_ks_no5,
            R.mipmap.number_bg_ks_no6};

    // 彩票开奖URL
    public static String[] numberHistoryURLs = {BaseURLs.URL_LASTLOTTERYResults,// 0所有最后一期开奖结果
            BaseURLs.URL_LHCDetailedResults,// 1香港开奖
            BaseURLs.URL_CQSSCDetailedResults,// 2重庆时时彩
            BaseURLs.URL_JXSSCDetailedResults,// 3江西时时彩
            BaseURLs.URL_XJSSCDetailedResults,// 4新疆时时彩
            BaseURLs.URL_YNSSCDetailedResults,// 5云南时时彩
            BaseURLs.URL_QXCDetailedResults,// 6七星彩
            BaseURLs.URL_D11X5DetailedResults,// 7广东11选5
            BaseURLs.URL_DKL10DetailedResults,// 8广东快乐10分
            BaseURLs.URL_HB11X5DetailedResults,// 9湖北11选5
            BaseURLs.URL_AHK3DetailedResults,// 10安徽快三
            BaseURLs.URL_HNKL10DetailedResults,// 11湖南快乐10分
            BaseURLs.URL_KL8DetailedResults,// 12快乐8
            BaseURLs.URL_JLK3DetailedResults,// 13吉林快三
            BaseURLs.URL_LN11X5DetailedResults,// 14辽宁11选5
            BaseURLs.URL_PK10DetailedResults,// 15北京赛车
            BaseURLs.URL_JSK3DetailedResults,// 16江苏快三
            BaseURLs.URL_SSLDetailedResults,// 17时时乐
            BaseURLs.URL_GXK3DetailedResults,// 18广西快三
            BaseURLs.URL_CQKL10DetailedResults,// 19幸运农场
            BaseURLs.URL_JS11X5DetailedResults,// 20江苏11选5
            BaseURLs.URL_JX11X5DetailedResults,// 21江西11选5
            BaseURLs.URL_SD11X5DetailedResults,// 22山东11选5
            BaseURLs.URL_TJSSCDetailedResults,// 23天津时时彩
            BaseURLs.URL_SSQDetailedResults,// 24双色球
            BaseURLs.URL_PL3DetailedResults,// 25排列3
            BaseURLs.URL_PL5DetailedResults,// 26排列5
            BaseURLs.URL_F3DDetailedResults,// 27福彩3D
            BaseURLs.URL_QLCDetailedResults,// 28七乐彩
            BaseURLs.URL_DLTDetailedResults // 29大乐透
    };

    // 香港彩和七星彩的下一期开奖URL
    public static String[] nextNumberOpenURL = {BaseURLs.URL_LHCNextAndNewResults, BaseURLs.URL_QXCCNextAndNewResults};

    // 香港彩正在开奖中的动画效果gif
    public static int[] numberHKOpenGIF = {R.mipmap.number_anim_yell1, R.mipmap.number_anim_yell2, R.mipmap.number_anim_yell3, R.mipmap.number_anim_yell4, R.mipmap.number_anim_yell5,
            R.mipmap.number_anim_yell6, R.mipmap.number_anim_yell6, R.mipmap.number_anim_yell7};

    // 香港彩正在开奖中的动画效果gif
    public static int[] numberQXCOpenGIF = {R.mipmap.number_anim_red1, R.mipmap.number_anim_red2, R.mipmap.number_anim_red3, R.mipmap.number_anim_red4, R.mipmap.number_anim_blue1,
            R.mipmap.number_anim_blue2, R.mipmap.number_anim_blue3};

    // public static String VERSION_NAME = "";
    // public static int VERSION_CODE = 0;
    // public static String PACKAGE_NAME = "";
    // public static String CHANNEL_NAME = "1000001";
    // public static String TQ_VERSION = "tq2.1";
    // 服务器API版本
    public static String CHAONENG_API_TECH = "http://183.61.172.82:8096/mlottery/core/";// API内网
    public static String CHAONENG_API_PUBLISH = "http://183.61.172.82:8096/mlottery/core/";// API公网

    static {
        if (isTestLink) {
            ACCESS_LINK = CHAONENG_API_TECH;
        } else {
            ACCESS_LINK = CHAONENG_API_PUBLISH;
        }

        if (isGOKeyboard) {
            APPKEY_RONGYUN = "bmdehs6pddeds";
            APPSECRET_RONGYUN = "2gFKWZMbtEj7";
        } else {
            APPKEY_RONGYUN = "ik1qhw0919m8p";
            APPSECRET_RONGYUN = "oyHzmKPlNKG";
        }
    }

    // 首页跳转key_value
    public static final String FOTTBALL_KEY = "football";// 足球界面跳转Key
    public static final int FOTTBALL_SCORE_VALUE = 0;// 足球比分跳转value
    public static final int BASKETBALL_SCORE_VALUE = 5;// 篮球比分跳转value
    public static final int FOTTBALL_INFORMATION_VALUE = 1;// 足球资讯跳转value
    public static final String FOTTBALL_INFO_LABEL_KEY = "sportsInfoIndex";// 足球资讯标签Key
    public static final int FOTTBALL_DATA_VALUE = 2;// 足球数据跳转value
    public static final int FOTTBALL_VIDEO_VALUE = 3;// 足球视频跳转value
    public static final int FOTTBALL_EXPONENT_VALUE = 4;// 足球指数跳转value
    public static final String BASKETBALL_KEY = "basketball";// 篮球界面跳转Key
    public static final int BASKETBALL_SCORE_KEY = 0;// 篮球即时跳转value
    public static final int BASKETBALL_AMIDITHION_VALUE = 1;// 篮球赛果跳转value
    public static final int BASKETBALL_COMPETITION_VALUE = 2;// 篮球赛程跳转value
    public static final int BASKETBALL_ATTENTION_VALUE = 3;// 篮球关注跳转value
    public static final String LOTTERY_KEY = "numberName";// 彩票界面跳转Key
    public static final String HOME_PAGER_DATA_KEY = "homePagerDataKey";// 首页缓存数据key
    public static final String HOME_PAGER_VERSION_UPDATE_KEY = "homePagerVersionUpdate";// 首页忽略此版本



	// ============= account begin ==============
    /**保存userId的key*/
    public static final String HEADICON = "headIcon";
	/**保存userId的key*/
	public static final String SPKEY_USERID = "userId";
	/**保存token的key*/
	public static final String SPKEY_TOKEN = "token";
	/**保存nickName的key*/
	public static final String SPKEY_NICKNAME = "nickName";
	/**保存loginAccount的key*/
	public static final String SPKEY_LOGINACCOUNT = "loginAccount";
    /**保存友盟的deviceToken*/
    public static final String uMengDeviceToken="uMengDeviceToken";
	/**应用启动的时候初始化*/
	public static String deviceToken ="";
	/**应用启动的时候初始化*/
	public static Register register = null;
	// ============= account end ==============


    /**
     * 首页热门赛事足球背景图片
     */
    public static int[] homePageScoreFootBG = {R.mipmap.home_pager_score_football01_bg, R.mipmap.home_pager_score_football02_bg, R.mipmap.home_pager_score_football03_bg, R.mipmap.home_pager_score_football04_bg, R.mipmap.home_pager_score_football05_bg, R.mipmap.home_pager_score_football06_bg, R.mipmap.home_pager_score_football07_bg};
    /**
     * 首页热门赛事篮球背景图片
     */
    public static int[] homePageScoreBasketBG = {R.mipmap.home_pager_score_basketball01_bg, R.mipmap.home_pager_score_basketball02_bg};
	//防止用户恶意注册添加的sign字段。
	public static final String SIGN_KEY ="B2A7748BF1FCAF6326979E1B86DC0C60";

    /**这个是把devicetoken存入到sharedprefrence中的键*/
    public static final String DEVICETOKEN ="devicetoken";

    /**-当前手机的终端ID-*/
    public static final String TERID = "terid";

    /**
     * 临时测试数据
     *
     * @return
     */
    public static String getTestData() {
        return  "{\"serverTime\":\"1479809998940\",\"numLotteryResults\":[" +
                "{\"name\":\"1\",\"issue\":\"2016134\",\"nextIssue\":\"2016135\",\"nextTime\":\"2016-11-22 21:30:00\",\"time\":\"2016-11-19 21:30:00\",\"numbers\":\"42,06,37,02,24,23#32\",\"zodiac\":\"兔,兔,猴,羊,鸡,狗#牛\"}," +
                "{\"name\":\"24\",\"issue\":\"2016136\",\"nextIssue\":\"2016137\",\"nextTime\":\"2016-11-22 21:15:00\",\"time\":\"2016-11-20 21:15:00\",\"numbers\":\"02,07,10,20,27,29#03\",\"jackpot\":\"989371808\",\"sales\":\"396457052\",\"firstCount\":\"8\",\"firstBonus\":\"7561764\",\"secondCount\":\"117\",\"secondBonus\":\"218954\",\"thirdCount\":\"1877\",\"thirdBonus\":\"3000\",\"fourthCount\":\"81173\",\"fourthBonus\":\"200\",\"fifthCount\":\"1494578\",\"fifthBonus\":\"10\",\"sixthCount\":\"10996400\",\"sixthBonus\":\"5\"}," +
                "{\"name\":\"29\",\"issue\":\"2016137\",\"nextIssue\":\"2016138\",\"nextTime\":\"2016-11-23 20:30:00\",\"time\":\"2016-11-21 20:30:00\",\"numbers\":\"07,20,23,29,34#02,10\",\"jackpot\":\"3807220000\",\"sales\":\"181006344\",\"firstCount\":\"2\",\"firstBonus\":\"10000000\",\"firstAddCount\":\"0\",\"firstAddBonus\":\"0\",\"secondCount\":\"63\",\"secondBonus\":\"119261\",\"secondAddCount\":\"14\",\"secondAddBonus\":\"71556\",\"thirdCount\":\"766\",\"thirdBonus\":\"3582\",\"thirdAddCount\":\"264\",\"thirdAddBonus\":\"2149\",\"fourthCount\":\"34941\",\"fourthBonus\":\"200\",\"fourthAddCount\":\"10590\",\"fourthAddBonus\":\"100\",\"fifthCount\":\"604177\",\"fifthBonus\":\"10\",\"fifthAddCount\":\"203195\",\"fifthAddBonus\":\"5\",\"sixthCount\":\"5256234\",\"sixthBonus\":\"5\"}," +
                "{\"name\":\"25\",\"issue\":\"2016318\",\"nextIssue\":\"2016319\",\"nextTime\":\"2016-11-22 20:30:00\",\"time\":\"2016-11-20 20:30:00\",\"numbers\":\"1,4,2\",\"jackpot\":null,\"sales\":\"14213084\",\"dirCount\":\"1976\",\"dirBonus\":\"1040\",\"groupCount\":\"3229\",\"groupBonus\":\"173\"}," +
                "{\"name\":\"26\",\"issue\":\"2016318\",\"nextIssue\":\"2016319\",\"nextTime\":\"2016-11-22 20:30:00\",\"time\":\"2016-11-20 20:30:00\",\"numbers\":\"1,4,2,0,2\",\"jackpot\":null,\"sales\":\"8821464\",\"dirCount\":\"18\",\"dirBonus\":\"200000\"}," +
                "{\"name\":\"30\",\"issue\":\"2016172\",\"nextIssue\":null,\"nextTime\":null,\"time\":null,\"numbers\":\"1,3,0,3,3,3,3,0,3,0,3,0,3,3\",\"footballLotteryIssueResultData\":[{\"homeName\":\"哥伦比\",\"guestName\":\"智 利\",\"kickOffTime\":\"2016-11-11 04:30:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":\"1\",\"halfDrawcode\":null,\"sc_round\":\"1\"},{\"homeName\":\"乌拉圭\",\"guestName\":\"厄瓜多\",\"kickOffTime\":\"2016-11-11 07:00:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":\"3\",\"halfDrawcode\":null,\"sc_round\":\"2\"},{\"homeName\":\"巴拉圭\",\"guestName\":\"秘 鲁\",\"kickOffTime\":\"2016-11-11 07:30:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":\"0\",\"halfDrawcode\":null,\"sc_round\":\"3\"},{\"homeName\":\"委内瑞\",\"guestName\":\"玻利维\",\"kickOffTime\":\"2016-11-11 07:30:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":\"3\",\"halfDrawcode\":null,\"sc_round\":\"4\"},{\"homeName\":\"巴 西\",\"guestName\":\"阿根廷\",\"kickOffTime\":\"2016-11-11 07:45:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":\"3\",\"halfDrawcode\":null,\"sc_round\":\"5\"},{\"homeName\":\"捷 克\",\"guestName\":\"挪 威\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":\"3\",\"halfDrawcode\":null,\"sc_round\":\"6\"},{\"homeName\":\"北 爱\",\"guestName\":\"阿塞拜\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":\"3\",\"halfDrawcode\":null,\"sc_round\":\"7\"},{\"homeName\":\"圣马力\",\"guestName\":\"德 国\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":\"0\",\"halfDrawcode\":null,\"sc_round\":\"8\"},{\"homeName\":\"丹 麦\",\"guestName\":\"哈萨克\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":\"3\",\"halfDrawcode\":null,\"sc_round\":\"9\"},{\"homeName\":\"罗马尼\",\"guestName\":\"波 兰\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":\"0\",\"halfDrawcode\":null,\"sc_round\":\"10\"},{\"homeName\":\"英格兰\",\"guestName\":\"苏格兰\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":\"3\",\"halfDrawcode\":null,\"sc_round\":\"11\"},{\"homeName\":\"马耳他\",\"guestName\":\"斯洛文\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":\"0\",\"halfDrawcode\":null,\"sc_round\":\"12\"},{\"homeName\":\"斯洛伐\",\"guestName\":\"立陶宛\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":\"3\",\"halfDrawcode\":null,\"sc_round\":\"13\"},{\"homeName\":\"法 国\",\"guestName\":\"瑞 典\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":\"3\",\"halfDrawcode\":null,\"sc_round\":\"14\"}],\"prizeTime\":\"--\",\"saleTime\":\"--\",\"stopTime\":\"--\",\"lotteryTime\":\"--\",\"footballSecLottery\":{\"rsales\":null,\"rfirCount\":null,\"rfirSinBon\":null},\"footballFirlottery\":{\"jackpot\":null,\"sales\":null,\"firCount\":null,\"firSinBon\":null,\"secCount\":null,\"secSinBon\":null}}," +
                "{\"name\":\"31\",\"issue\":\"2016176\",\"nextIssue\":null,\"nextTime\":null,\"time\":null,\"numbers\":\"--,--,--,--,--,--,--,--,--,--,--,--\",\"footballLotteryIssueResultData\":[{\"homeName\":\"哥伦比\",\"guestName\":\"智 利\",\"kickOffTime\":\"2016-11-11 04:30:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":null,\"halfDrawcode\":null,\"sc_round\":\"1\"},{\"homeName\":\"巴拉圭\",\"guestName\":\"秘 鲁\",\"kickOffTime\":\"2016-11-11 07:30:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":null,\"halfDrawcode\":null,\"sc_round\":\"2\"},{\"homeName\":\"巴 西\",\"guestName\":\"阿根廷\",\"kickOffTime\":\"2016-11-11 07:45:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":null,\"halfDrawcode\":null,\"sc_round\":\"3\"},{\"homeName\":\"捷 克\",\"guestName\":\"挪 威\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":null,\"halfDrawcode\":null,\"sc_round\":\"4\"},{\"homeName\":\"英格兰\",\"guestName\":\"苏格兰\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":null,\"halfDrawcode\":null,\"sc_round\":\"5\"},{\"homeName\":\"法 国\",\"guestName\":\"瑞 典\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":null,\"halfScore\":null,\"fullDrawcode\":null,\"halfDrawcode\":null,\"sc_round\":\"6\"}],\"prizeTime\":\"--\",\"saleTime\":\"--\",\"stopTime\":\"--\",\"lotteryTime\":\"--\",\"footballFirlottery\":{\"jackpot\":null,\"sales\":null,\"firCount\":null,\"firSinBon\":null,\"secCount\":null,\"secSinBon\":null}}," +
                "{\"name\":\"32\",\"issue\":\"2016176\",\"nextIssue\":null,\"nextTime\":\"2016-11-22 20:30:00\",\"time\":null,\"numbers\":\"0,0,2,1,3+,0,2,1\",\"footballLotteryIssueResultData\":[{\"homeName\":\"哥伦比\",\"guestName\":\"智 利\",\"kickOffTime\":\"2016-11-11 04:30:00\",\"fullScore\":\"0-0\",\"halfScore\":null,\"fullDrawcode\":null,\"halfDrawcode\":null,\"sc_round\":\"1\"},{\"homeName\":\"捷 克\",\"guestName\":\"挪 威\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":\"2-1\",\"halfScore\":null,\"fullDrawcode\":null,\"halfDrawcode\":null,\"sc_round\":\"2\"},{\"homeName\":\"英格兰\",\"guestName\":\"苏格兰\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":\"3-0\",\"halfScore\":null,\"fullDrawcode\":null,\"halfDrawcode\":null,\"sc_round\":\"3\"},{\"homeName\":\"法 国\",\"guestName\":\"瑞 典\",\"kickOffTime\":\"2016-11-12 03:45:00\",\"fullScore\":\"2-1\",\"halfScore\":null,\"fullDrawcode\":null,\"halfDrawcode\":null,\"sc_round\":\"4\"}],\"prizeTime\":\"--\",\"saleTime\":\"--\",\"stopTime\":\"--\",\"lotteryTime\":\"--\",\"footballFirlottery\":{\"jackpot\":null,\"sales\":null,\"firCount\":null,\"firSinBon\":null,\"secCount\":null,\"secSinBon\":null}}," +
                "{\"name\":\"2\",\"issue\":\"20161122072\",\"nextIssue\":\"20161122073\",\"nextTime\":\"2016-11-22 18:10:40\",\"time\":\"2016-11-22 18:00:54\",\"numbers\":\"4,6,6,2,2\"}," +
                "{\"name\":\"3\",\"issue\":\"20160516100\",\"nextIssue\":\"20160317039\",\"nextTime\":\"2016-03-17 15:36:22\",\"time\":\"2016-05-16 20:22:41\",\"numbers\":\"9,2,3,3,0\"}," +
                "{\"name\":\"4\",\"issue\":\"2016112250\",\"nextIssue\":\"2016112251\",\"nextTime\":\"2016-11-22 18:30:11\",\"time\":\"2016-11-22 18:20:23\",\"numbers\":\"8,0,1,6,0\"}," +
                "{\"name\":\"5\",\"issue\":\"20161122051\",\"nextIssue\":\"20161122052\",\"nextTime\":\"2016-11-22 18:29:06\",\"time\":\"2016-11-22 18:19:12\",\"numbers\":\"6,6,6,7,8\"}," +
                "{\"name\":\"6\",\"issue\":\"2016137\",\"nextIssue\":\"2016138\",\"nextTime\":\"2016-11-22 20:30:00\",\"time\":\"2016-11-20 20:30:00\",\"numbers\":\"7,6,9,4,0,4,3\",\"jackpot\":\"20061940\"}," +
                "{\"name\":\"7\",\"issue\":\"2016112255\",\"nextIssue\":\"2016112256\",\"nextTime\":\"2016-11-22 18:21:00\",\"time\":\"2016-11-22 18:11:02\",\"numbers\":\"05,03,11,08,06\"}," +
                "{\"name\":\"8\",\"issue\":\"2016112255\",\"nextIssue\":\"2016112256\",\"nextTime\":\"2016-11-22 18:22:10\",\"time\":\"2016-11-22 18:12:22\",\"numbers\":\"03,07,18,14,02,01,13,12\"}," +
                "{\"name\":\"20\",\"issue\":\"2016112259\",\"nextIssue\":\"2016112260\",\"nextTime\":\"2016-11-22 18:26:47\",\"time\":\"2016-11-22 18:17:17\",\"numbers\":\"02,09,10,07,06\"}," +
                "{\"name\":\"10\",\"issue\":\"20161122057\",\"nextIssue\":\"20161122058\",\"nextTime\":\"2016-11-22 18:21:58\",\"time\":\"2016-11-22 18:12:10\",\"numbers\":\"1,4,4\"}," +
                "{\"name\":\"11\",\"issue\":\"20161122056\",\"nextIssue\":\"20161122057\",\"nextTime\":\"2016-11-22 18:30:18\",\"time\":\"2016-11-22 18:20:30\",\"numbers\":\"11,17,13,03,19,02,05,18\"}," +
                "{\"name\":\"14\",\"issue\":\"160611081\",\"nextIssue\":\"160611082\",\"nextTime\":\"2016-06-11 22:19:45\",\"time\":\"2016-06-11 22:09:55\",\"numbers\":\"5,10,11,1,4\"}," +
                "{\"name\":\"15\",\"issue\":\"588112\",\"nextIssue\":\"588113\",\"nextTime\":\"2016-11-22 18:22:42\",\"time\":\"2016-11-22 18:17:47\",\"numbers\":\"10,06,05,04,01,03,02,08,09,07\"}," +
                "{\"name\":\"16\",\"issue\":\"20161122057\",\"nextIssue\":\"20161122058\",\"nextTime\":\"2016-11-22 18:10:49\",\"time\":\"2016-11-22 18:01:01\",\"numbers\":\"3,4,6\"}," +
                "{\"name\":\"17\",\"issue\":\"2016112216\",\"nextIssue\":\"2016112217\",\"nextTime\":\"2016-11-22 18:30:30\",\"time\":\"2016-11-22 18:00:33\",\"numbers\":\"2,9,6\"}," +
                "{\"name\":\"18\",\"issue\":\"20161122053\",\"nextIssue\":\"20161122054\",\"nextTime\":\"2016-11-22 18:27:14\",\"time\":\"2016-11-22 18:17:26\",\"numbers\":\"5,5,5\"}," +
                "{\"name\":\"19\",\"issue\":\"20161122063\",\"nextIssue\":\"20161122064\",\"nextTime\":\"2016-11-22 18:23:49\",\"time\":\"2016-11-22 18:14:01\",\"numbers\":\"19,18,15,12,10,05,20,06\"}," +
                "{\"name\":\"22\",\"issue\":\"2016112256\",\"nextIssue\":\"2016112257\",\"nextTime\":\"2016-11-22 18:26:00\",\"time\":\"2016-11-22 18:15:57\",\"numbers\":\"09,08,07,05,06\"}," +
                "{\"name\":\"23\",\"issue\":\"20161122056\",\"nextIssue\":\"20161122057\",\"nextTime\":\"2016-11-22 18:30:28\",\"time\":\"2016-11-22 18:20:40\",\"numbers\":\"5,6,7,2,9\"}]}";
    }

}
