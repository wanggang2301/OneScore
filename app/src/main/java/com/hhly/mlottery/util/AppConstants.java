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

    // 彩票种类名称
    /*public static String[] numberNames = { MyApp.getContext().getResources().getString(R.string.number_cz_hk), // "香港彩"
            MyApp.getContext().getResources().getString(R.string.number_cz_cq_ssc), // "重庆时时彩"
			MyApp.getContext().getResources().getString(R.string.number_cz_jx_ssc), // "江西时时彩"
			MyApp.getContext().getResources().getString(R.string.number_cz_xj_ssc),// "新疆时时彩"
			MyApp.getContext().getResources().getString(R.string.number_cz_yn_ssc), // "云南时时彩"
			MyApp.getContext().getResources().getString(R.string.number_cz_qxc),// "七星彩"
			MyApp.getContext().getResources().getString(R.string.number_cz_gd_syxw), // "广东十一选五"
			MyApp.getContext().getResources().getString(R.string.number_cz_gd_klsf), // "广东快乐十分"
			MyApp.getContext().getResources().getString(R.string.number_cz_hb_syxw), // "湖北十一选五"
			MyApp.getContext().getResources().getString(R.string.number_cz_ah_ks), // "安徽快三"
			MyApp.getContext().getResources().getString(R.string.number_cz_hn_klsf),// "湖南快乐十分"
			MyApp.getContext().getResources().getString(R.string.number_cz_kl8),// "快乐8奖"
			MyApp.getContext().getResources().getString(R.string.number_cz_jl_ks),// "吉林快三"
			MyApp.getContext().getResources().getString(R.string.number_cz_ln_xyxw),// "辽宁十一选五"
			MyApp.getContext().getResources().getString(R.string.number_cz_bj_sc),// "北京赛车"
			MyApp.getContext().getResources().getString(R.string.number_cz_js_ks),// "江苏快三"
			MyApp.getContext().getResources().getString(R.string.number_cz_ssl),// "时时乐"
			MyApp.getContext().getResources().getString(R.string.number_cz_gx_ks),// "广西快三"
			MyApp.getContext().getResources().getString(R.string.number_cz_xylc),// "幸运农场"
			MyApp.getContext().getResources().getString(R.string.number_cz_js_syxw),// "江苏十一选五"
			MyApp.getContext().getResources().getString(R.string.number_cz_jx_syxw),// "江西十一选五"
			MyApp.getContext().getResources().getString(R.string.number_cz_sd_syxw),// "山东十一选五"
			MyApp.getContext().getResources().getString(R.string.number_cz_tj_ssc) // "天津时时彩"
	};*/

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
            BaseURLs.URL_TJSSCDetailedResults // 23天津时时彩
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
        return "{\"serverTime\":\"1478154686486\",\"numLotteryResults\":[" +
                "{\"name\":\"24\",\"issue\":\"2016122\",\"nextIssue\":\"2016123\",\"nextTime\":\"2016-12-05 13:15:00\",\"time\":\"2016-10-18 13:15:00\",\"numbers\":\"15,22,23,24,28,29#08\",\"jackpot\":\"1089129612\"}," +
                "{\"name\":\"29\",\"issue\":\"2016123\",\"nextIssue\":\"2016124\",\"nextTime\":\"2016-12-05 12:30:00\",\"time\":\"2016-10-19 12:30:00\",\"numbers\":\"04,05,13,20,32#01,10\",\"jackpot\":\"3651000000\"}," +
                "{\"name\":\"28\",\"issue\":\"2016122\",\"nextIssue\":\"2016123\",\"nextTime\":\"2016-12-05 13:15:00\",\"time\":\"2016-10-17 13:15:00\",\"numbers\":\"01,09,14,16,17,19,21#13\",\"jackpot\":\"4176721\"}," +
                "{\"name\":\"27\",\"issue\":\"2016286\",\"nextIssue\":\"2016287\",\"nextTime\":\"2016-12-05 12:30:00\",\"time\":\"2016-10-19 12:30:00\",\"numbers\":\"9,0,7\"}," +
                "{\"name\":\"25\",\"issue\":\"2016286\",\"nextIssue\":\"2016287\",\"nextTime\":\"2016-12-05 12:30:00\",\"time\":\"2016-10-19 12:30:00\",\"numbers\":\"6,2,3\"}," +
                "{\"name\":\"26\",\"issue\":\"2016286\",\"nextIssue\":\"2016287\",\"nextTime\":\"2016-12-05 12:30:00\",\"time\":\"2016-10-19 12:30:00\",\"numbers\":\"6,2,3,3,2\"}," +
                "{\"name\":\"1\",\"issue\":\"2016121\",\"nextIssue\":\"2016121\",\"nextTime\":\"2016-12-03 18:30:00\",\"time\":\"2016-10-20 13:30:00\",\"numbers\":\"01,02,03,04,05,06#07\",\"zodiac\":\"猴,猴,猴,猴,猴,猴#猴\"}," +
                "{\"name\":\"2\",\"issue\":\"20161027076\",\"nextIssue\":\"20161027077\",\"nextTime\":\"2016-12-05 10:50:40\",\"time\":\"2016-10-27 10:40:44\",\"numbers\":\"4,2,0,0,1\"}," +
                "{\"name\":\"3\",\"issue\":\"20160222084\",\"nextIssue\":\"20160318084\",\"nextTime\":\"2016-12-05 15:13:07\",\"time\":\"2016-02-22 15:14:40\",\"numbers\":\"3,1,6,6,8\"}," +
                "{\"name\":\"4\",\"issue\":\"2016102752\",\"nextIssue\":\"2016102753\",\"nextTime\":\"2016-12-05 10:50:11\",\"time\":\"2016-10-27 10:40:23\",\"numbers\":\"6,8,6,9,4\"}," +
                "{\"name\":\"5\",\"issue\":\"20161027053\",\"nextIssue\":\"20161027054\",\"nextTime\":\"2016-12-05 10:41:30\",\"time\":\"2016-10-27 10:41:33\",\"numbers\":\"6,8,6,7,8\"}," +
                "{\"name\":\"6\",\"issue\":\"2016123\",\"nextIssue\":\"2016124\",\"nextTime\":\"2016-11-05 18:30:00\",\"time\":\"2016-10-18 12:30:00\",\"numbers\":\"6,5,2,1,9,7,7\",\"jackpot\":\"9058157\"}," +
                "{\"name\":\"7\",\"issue\":\"2016102758\",\"nextIssue\":\"2016102759\",\"nextTime\":\"2016-12-05 10:51:00\",\"time\":\"2016-10-27 10:42:01\",\"numbers\":\"02,08,03,11,04\"}," +
                "{\"name\":\"8\",\"issue\":\"2016102758\",\"nextIssue\":\"2016102759\",\"nextTime\":\"2016-12-05 10:52:09\",\"time\":\"2016-10-27 10:42:21\",\"numbers\":\"01,17,11,03,15,12,04,16\"}," +
                "{\"name\":\"20\",\"issue\":\"2016102761\",\"nextIssue\":\"2016102762\",\"nextTime\":\"2016-12-05 10:46:47\",\"time\":\"2016-10-27 10:37:17\",\"numbers\":\"07,08,01,06,02\"}," +
                "{\"name\":\"10\",\"issue\":\"20161027060\",\"nextIssue\":\"20161027061\",\"nextTime\":\"2016-12-05 10:51:51\",\"time\":\"2016-10-27 10:42:03\",\"numbers\":\"1,1,5\"}," +
                "{\"name\":\"11\",\"issue\":\"20161027058\",\"nextIssue\":\"20161027059\",\"nextTime\":\"2016-12-05 10:49:29\",\"time\":\"2016-10-27 10:39:41\",\"numbers\":\"20,04,09,05,17,07,10,11\"}," +
                "{\"name\":\"15\",\"issue\":\"583463\",\"nextIssue\":\"583464\",\"nextTime\":\"2016-12-05 10:47:43\",\"time\":\"2016-10-27 10:42:48\",\"numbers\":\"08,05,10,04,07,02,06,01,09,03\"}," +
                "{\"name\":\"16\",\"issue\":\"20161027061\",\"nextIssue\":\"20161027062\",\"nextTime\":\"2016-12-05 10:52:06\",\"time\":\"2016-10-27 10:42:18\",\"numbers\":\"2,3,6\"}," +
                "{\"name\":\"17\",\"issue\":\"2016102717\",\"nextIssue\":\"2016102718\",\"nextTime\":\"2016-12-05 11:00:30\",\"time\":\"2016-10-27 10:31:18\",\"numbers\":\"9,1,3\"}," +
                "{\"name\":\"18\",\"issue\":\"20161027055\",\"nextIssue\":\"20161027056\",\"nextTime\":\"2016-12-05 10:47:11\",\"time\":\"2016-10-27 10:37:23\",\"numbers\":\"2,6,6\"}," +
                "{\"name\":\"19\",\"issue\":\"20161027066\",\"nextIssue\":\"20161027067\",\"nextTime\":\"2016-12-05 10:53:50\",\"time\":\"2016-10-27 10:44:02\",\"numbers\":\"02,18,09,13,12,07,15,16\"}," +
                "{\"name\":\"22\",\"issue\":\"2016102759\",\"nextIssue\":\"2016102760\",\"nextTime\":\"2016-12-05 10:56:00\",\"time\":\"2016-10-27 10:45:50\",\"numbers\":\"01,10,04,09,03\"}," +
                "{\"name\":\"23\",\"issue\":\"20161027058\",\"nextIssue\":\"20161027059\",\"nextTime\":\"2016-12-05 10:49:19\",\"time\":\"2016-10-27 10:39:31\",\"numbers\":\"1,7,7,4,6\"}]}";
    }

}
