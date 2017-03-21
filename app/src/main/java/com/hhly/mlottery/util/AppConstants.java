package com.hhly.mlottery.util;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;

import java.util.HashMap;
import java.util.Map;

public class AppConstants {
    /**
     * 是否是测试环境，true为测试，false为生产环境
     */
    public static final boolean isTestEnv = true;
    /**
     * true是国际版|false内地版
     */
    public static final boolean isGOKeyboard = false;
    /**
     * true是纯净版|false完整版
     */
    public static final boolean fullORsimple = false;

    // 是否是测试连接，测试连接用于阶段测试使用
//    public static final boolean isTestLink = false;
    /**
     * 判断是否上传奔溃日志到友盟
     */
    public static final boolean isUploadCrash = false;


    /**
     * 开机页
     */
    public static final int getBootPageId() {

        switch (MyApp.isLanguage) {
            case "rCN":
                return R.mipmap.welcome;
            case "rTW":
                return R.mipmap.welcome_tw;
//            case "rEN":
//            case "rTH":
//            case "rVI":
            default:
                return R.mipmap.welcome;
        }
    }

    /**
     * 引导页
     */
    public static final int[] getGuidePage() {

        return new int[]{R.mipmap.welcome1, R.mipmap.welcome2, R.mipmap.welcome3};
    }


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
     *
     * @desc 用包名判断当前的时区或参数的选择
     */
    public static final String PACKGER_NAME_ZH = "com.hhly.mlottery";      // 国内版包名
    public static final String PACKGER_NAME_TH = "com.th.hhly.mlottery";   // 泰国版包名
    public static final String PACKGER_NAME_VN_HN = "vn.hn.hhly.mlottery"; // 越南北包名
    public static final String PACKGER_NAME_VN = "vn.hhly.mlottery";       // 越南南包名
    public static final String PACKGER_NAME_UK = "uk.hhly.mlottery";       // 英文版包名

    // 网球关注
    public static final String TENNIS_BALL_FOCUS = "tennisBallFocus";

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
            BaseURLs.URL_DLTDetailedResults, // 29大乐透
            BaseURLs.URL_SFCDetailedResults, // 30胜负彩
            BaseURLs.URL_LCBQCDetailedResults, // 31 六场半全场
            BaseURLs.URL_SCJQDetailedResults // 32 4场进球
    };

    // 香港彩正在开奖中的动画效果gif
    public static int[] numberHKOpenGIF = {R.mipmap.number_anim_yell1, R.mipmap.number_anim_yell2, R.mipmap.number_anim_yell3, R.mipmap.number_anim_yell4, R.mipmap.number_anim_yell5,
            R.mipmap.number_anim_yell6, R.mipmap.number_anim_yell6, R.mipmap.number_anim_yell7};

    // 香港彩正在开奖中的动画效果gif
    public static int[] numberQXCOpenGIF = {R.mipmap.number_anim_red1, R.mipmap.number_anim_red2, R.mipmap.number_anim_red3, R.mipmap.number_anim_red4, R.mipmap.number_anim_blue1,
            R.mipmap.number_anim_blue2, R.mipmap.number_anim_blue3};

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
    /**
     * 保存userId的key
     */
    public static final String HEADICON = "headIcon";
    /**
     * 保存userId的key
     */
    public static final String SPKEY_USERID = "userId";
    /**
     * 保存token的key
     */
    public static final String SPKEY_TOKEN = "token";
    /**
     * 保存nickName的key
     */
    public static final String SPKEY_NICKNAME = "nickName";
    /**
     * 保存loginAccount的key
     */
    public static final String SPKEY_LOGINACCOUNT = "loginAccount";
    /**
     * 保存友盟的deviceToken
     */
    public static final String uMengDeviceToken = "uMengDeviceToken";
    /**
     * 保存SEX
     */
    public static final String SEX = "sex";
    /**
     * 应用启动的时候初始化
     */
    public static String deviceToken = "";
    /**
     * 应用启动的时候初始化
     */
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
    public static final String SIGN_KEY = "B2A7748BF1FCAF6326979E1B86DC0C60";

    /**
     * 这个是把devicetoken存入到sharedprefrence中的键
     */
    public static final String DEVICETOKEN = "devicetoken";

    /**
     * -当前手机的终端ID-
     */
    public static final String TERID = "terid";

    /**
     * emoji表情
     */
    public static String[] emojiList = {
            "\ue415", "\ue056", "\ue057", "\ue414",
            "\ue405", "\ue106", "\ue418", "\ue417", "\ue40d",
            "\ue40a", "\ue404", "\ue105", "\ue412", "\ue40e",
            "\ue402", "\ue108", "\ue403", "\ue058", "\ue407",
            "\ue401", "\ue40f", "\ue40b", "\ue406", "\ue413",
            "\ue411", "\ue410", "\ue107", "\ue059", "\ue416",
            "\ue408", "\ue40c", "\ue409", "\ue11a", "\ue10c",
            "\ue32c", "\ue32a", "\ue32d", "\ue328", "\ue32b",
            "\ue022", "\ue023", "\ue327", "\ue329", "\ue32e",
            "\ue32f", "\ue335", "\ue334", "\ue021", "\ue337",
            "\ue020", "\ue336", "\ue13c", "\ue330", "\ue331",
            "\ue326", "\ue03e", "\ue11d", "\ue05a", "\ue00e",
            "\ue421", "\ue420", "\ue00d", "\ue010", "\ue011",
            "\ue41e", "\ue012", "\ue422", "\ue22e", "\ue22f",
            "\ue231", "\ue230", "\ue427", "\ue41d", "\ue00f",
            "\ue41f", "\ue14c", "\ue201", "\ue115", "\ue428",
            "\ue51f", "\ue429", "\ue424", "\ue423", "\ue253",
            "\ue426", "\ue111", "\ue425", "\ue31e", "\ue31f",
            "\ue31d", "\ue001", "\ue002", "\ue005", "\ue004",
            "\ue51a", "\ue519", "\ue518", "\ue515", "\ue516",
            "\ue517", "\ue51b", "\ue152", "\ue04e", "\ue51c",
            "\ue51e", "\ue11c", "\ue536", "\ue003", "\ue41c",
            "\ue41b", "\ue419", "\ue41a", "\ue04a", "\ue04b",
            "\ue049", "\ue048", "\ue04c", "\ue13d", "\ue443",
            "\ue43e", "\ue04f", "\ue052", "\ue053", "\ue524",
            "\ue52c", "\ue52a", "\ue531", "\ue050", "\ue527",
            "\ue051", "\ue10b", "\ue52b", "\ue52f", "\ue528",
            "\ue01a", "\ue134", "\ue530", "\ue529", "\ue526",
            "\ue340", "\ue34d", "\ue339", "\ue147", "\ue343",
            "\ue33c", "\ue33a", "\ue43f", "\ue34b", "\ue046",
            "\ue345", "\ue346", "\ue348", "\ue347", "\ue34a",
            "\ue349"
    };
    /**
     * 本地个性化表情
     */
    public static int[] localIcon = {
            R.mipmap.chart_ball_juyijing,
            R.mipmap.chart_ball_shihuale,
            R.mipmap.chart_ball_zailaiyijiao,
            R.mipmap.chart_ball_mengkuanle,
            R.mipmap.chart_ball_yayajing,
            R.mipmap.chart_ball_dacaipan,
            R.mipmap.chart_ball_jinggaoni,
            R.mipmap.chart_ball_sesese,
            R.mipmap.chart_ball_qiudatui,
            R.mipmap.chart_ball_youqingguanlaoye,
            R.mipmap.chart_ball_fubaila,
            R.mipmap.chart_ball_shoumila,
            R.mipmap.chart_ball_touzhele,
            R.mipmap.chart_ball_shangtiantai,
            R.mipmap.chart_ball_chitule,
            R.mipmap.chart_ball_xihushuiya
    };

    public static Map<String, Integer> localMap = new HashMap<>();

    static {
        localMap.put("[吃土了]", R.mipmap.chart_ball_chitule);
        localMap.put("[打裁判]", R.mipmap.chart_ball_dacaipan);
        localMap.put("[腐败啦]", R.mipmap.chart_ball_fubaila);
        localMap.put("[警告你]", R.mipmap.chart_ball_jinggaoni);
        localMap.put("[菊一紧]", R.mipmap.chart_ball_juyijing);
        localMap.put("[懵圈了]", R.mipmap.chart_ball_mengkuanle);
        localMap.put("[求大腿]", R.mipmap.chart_ball_qiudatui);
        localMap.put("[射射射]", R.mipmap.chart_ball_sesese);
        localMap.put("[上天台]", R.mipmap.chart_ball_shangtiantai);
        localMap.put("[石化了]", R.mipmap.chart_ball_shihuale);
        localMap.put("[收米啦]", R.mipmap.chart_ball_shoumila);
        localMap.put("[偷着乐]", R.mipmap.chart_ball_touzhele);
        localMap.put("[西湖水呀]", R.mipmap.chart_ball_xihushuiya);
        localMap.put("[压压惊]", R.mipmap.chart_ball_yayajing);
        localMap.put("[有请关老爷]", R.mipmap.chart_ball_youqingguanlaoye);
        localMap.put("[再来一角]", R.mipmap.chart_ball_zailaiyijiao);
    }

}
