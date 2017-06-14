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
//    public static final int THIRTY_THREE = 33;
//    public static final int THIRTY_FOUR = 34;
//    public static final int THIRTY_FIVE = 35;
//    public static final int THIRTY_SIX = 36;
//    public static final int THIRTY_SEVEN = 37;
//    public static final int THIRTY_EIGHT = 38;
//    public static final int THIRTY_NINE = 39;
//    public static final int FORTY = 40;
//    public static final int FORTY_ONE = 41;
//    public static final int FORTY_TWO = 42;
//    public static final int FORTY_THREE = 43;
//    public static final int FORTY_FOUR = 44;
//    public static final int FORTY_FIVE = 45;
//    public static final int FORTY_SIX = 46;
//    public static final int FORTY_SEVEN = 47;
//    public static final int FORTY_EIGHT = 48;
//    public static final int FORTY_NINE = 49;

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
    public static final String AVAILABLEBALANCE = "availableBalance";
    /**
     *   /**
     * 可用余额
     */
    public static final String CASHBALANCE = "cashBalance";
    /**
     *   /**
     * 订阅
     */
    public static final String BUYCOUNT = "buyCount";
    /**
     *   /**
     * 推荐文章
     */
    public static final String PUSHCOUNT = "pushCount";
    /**
     *   /**
     *可提现余额
     */
    public static final String SPKEY_USERID = "userId";
    /**
     * 保存用户专家
     */
    public static final String ISEXPERT = "isExpert";

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
