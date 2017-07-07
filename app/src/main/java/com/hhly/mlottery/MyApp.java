package com.hhly.mlottery;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;

import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CrashException;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.DataBus;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.imagecrop.ActivityStack;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tendcloud.tenddata.TCAgent;

import java.util.Locale;

import javax.inject.Inject;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import data.DataManager;

/**
 * @author Tenney
 * @ClassName: MyApp
 * @Description:
 * @date 2015-10-15 上午9:48:39
 */
public class MyApp extends Application {
    private static MyApp appcontext;
    public static Resources mResources;
    public static Configuration mConfiguration;
    public static DisplayMetrics mDm;
    public static Locale mLocale;
    public static String isLanguage = "";   // 获取语言
    public static String isPackageName;// 获取当前包名
    public static double LA;// 用户所在经度
    public static double LO;// 用户所在纬度
    public static String version;// 当前版本Name
    public static int versionCode;// 当前版本Code
    public static String channelNumber;// 当前版本渠道号

    @Inject
    DataManager mDataManager;

    private static RefWatcher mRefWatcher;
    public ActivityStack mActivityStack;

    @Override
    public void onCreate() {
        appcontext = this;
        // 初始化Activity 栈
        mActivityStack = new ActivityStack();
        // 子线程中做初始化操作，提升APP打开速度
//        new Thread() {
//            @Override
//            public void run() {

                initLeakCanary();
                // 初始化TalkingData统计
                TCAgent.LOG_ON = true;
                TCAgent.init(appcontext, DeviceInfo.getAppMetaData(appcontext, "TD_APP_ID"), DeviceInfo.getAppMetaData(appcontext, "TD_CHANNEL_ID"));
                // true: 开启自动捕获
                TCAgent.setReportUncaughtExceptions(!AppConstants.isTestEnv);

                // 初始化PreferenceUtil
                PreferenceUtil.init(appcontext);


                //初始化获取语言环境
                mResources = appcontext.getResources();
                mConfiguration = mResources.getConfiguration();
                mDm = mResources.getDisplayMetrics();
                mLocale = mConfiguration.locale;

                // 获取当前包名
                isPackageName = appcontext.getPackageName();
                // 设置时区
                settingTimeZone();

                // 根据上次的语言设置，重新设置语言
                isLanguage = switchLanguage(PreferenceUtil.getString("language", ""));

                // 捕获异常
                CrashException crashException = CrashException.getInstance();
                crashException.init(getApplicationContext());

                // 初始化Vollery
                VolleyContentFast.init(appcontext);

                //初始化畅言
                CyUtils.initCy(appcontext);
                initUserInfo();

                // OkHttpFinal(此初始化只是简单赋值不会阻塞线程)
                OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
                OkHttpFinal.getInstance().init(builder.build());

                // 获取当前apk版本信息
                getVersion();

                initDagger();

//            }
//        }.start();

        super.onCreate();
    }

    /**
     * 获取了LeakCanary
     * @return
     */
    public static RefWatcher getRefWatcher() {
        return mRefWatcher;
    }
    /**
     * 初始化 LeakCanary
     */
    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        mRefWatcher = LeakCanary.install(this);
    }

    //初始化Dagger注入
    public void initDagger() {
        DaggerMyAppComponent.builder()
                .myAppModule(new MyAppModule(this, BaseURLs.URL_MVP_API_HOST, AppConstants.timeZone + "", getLanguage()))
                .build()
                .inject(this);
    }

    /**
     * 获取 data.DataManager
     *
     * @return dataManager
     */
    public static DataManager getDataManager() {
        return get().mDataManager;
    }

    public static MyApp get() {
        return appcontext;
    }

    /**
     * 设置时区
     */
    private void settingTimeZone() {
        switch (isPackageName) {
            case AppConstants.PACKGER_NAME_ZH:// 国内版
                AppConstants.timeZone = 8;
                break;
            case AppConstants.PACKGER_NAME_TH:// 泰国版
            case AppConstants.PACKGER_NAME_VN_HN:// 越南北版
            case AppConstants.PACKGER_NAME_VN:// 越南南版
                AppConstants.timeZone = 7;
                break;
            case AppConstants.PACKGER_NAME_UK:// 英文版
                AppConstants.timeZone = 0;
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);// 解决友盟分析问题
    }

    /**
     * 获取当前进程名
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private void initUserInfo() {
        DeviceInfo.initRegisterInfo();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        DataBus.onTerminate();
    }

    // 单例模式
    public static MyApp getInstance() {
        return appcontext;
    }

    public static Context getContext() {
        return appcontext;
    }


    /**
     * 语言切换 选择
     *
     * @param language
     */
    public static String switchLanguage(String language) {
        //如果是国际版
        if (AppConstants.isGOKeyboard) {
            if (!"".equals(language)) {//默认语言不等于空的情况
                // 设置应用语言类型
                if (language.equals("rTW")) {// 选择中文繁体
                    mConfiguration.locale = Locale.TAIWAN;
                } else if (language.equals("rCN")) {// 选择中文简体
                    mConfiguration.locale = Locale.SIMPLIFIED_CHINESE;
                } else if (language.equals("rEN")) {// 选择英文
                    mConfiguration.locale = Locale.US;
                } else if (language.equals("rKO")) {// 选着韩文
                    mConfiguration.locale = Locale.KOREA;
                } else if (language.equals("rID")) {// 选着印度尼西亚
                    mConfiguration.locale = new Locale("in", "ID");
                } else if (language.equals("rTH")) {// 选着泰语
                    mConfiguration.locale = new Locale("th", "TH");
                } else if (language.equals("rVI")) {// 选着越南语
                    mConfiguration.locale = new Locale("vi", "VN");
                }
                mResources.updateConfiguration(mConfiguration, mDm);
                // 保存设置语言的类型
                PreferenceUtil.commitString("language", language);
            } else if ("".equals(language)) {//如果语言初始化的值等于空
                switch (MyApp.isPackageName) {
                    case AppConstants.PACKGER_NAME_TH:// 泰国版
                        if (mLocale.toString().equals(Locale.TAIWAN.toString())) {
                            mConfiguration.locale = Locale.TAIWAN;
                            language = "rTW";
                        } else if (mLocale.toString().equals(Locale.US.toString())) {
                            mConfiguration.locale = Locale.US;
                            language = "rEN";
                        } else if (mLocale.toString().equals("th_TH")) {
                            mConfiguration.locale = new Locale("th", "TH");
                            language = "rTH";
                        } else {
                            language = "rTH";
                        }
                        break;
                    case AppConstants.PACKGER_NAME_VN_HN:// 越南北版
                    case AppConstants.PACKGER_NAME_VN:// 越南南版
                        if (mLocale.toString().equals(Locale.TAIWAN.toString())) {
                            mConfiguration.locale = Locale.TAIWAN;
                            language = "rTW";
                        } else if (mLocale.toString().equals(Locale.US.toString())) {
                            mConfiguration.locale = Locale.US;
                            language = "rEN";
                        } else if (mLocale.toString().equals("th_TH")) {
                            mConfiguration.locale = new Locale("th", "TH");
                            language = "rVI";
                        } else {
                            language = "rVI";
                        }
                        break;
                    case AppConstants.PACKGER_NAME_UK:// 英文版
                        if (mLocale.toString().equals(Locale.TAIWAN.toString())) {
                            mConfiguration.locale = Locale.TAIWAN;
                            language = "rTW";
                        } else if (mLocale.toString().equals(Locale.US.toString())) {
                            mConfiguration.locale = Locale.US;
                            language = "rEN";
                        } else if (mLocale.toString().equals("th_TH")) {
                            mConfiguration.locale = new Locale("th", "TH");
                            language = "rCN";
                        } else {
                            language = "rEN";
                        }
                        break;
                    default:
                        break;
                }
            }

        } else {//如果是国内版
            if (!"".equals(language)) {//默认语言不等于空的情况
                // 设置应用语言类型
                if (language.equals("rTW")) {// 选择中文繁体
                    mConfiguration.locale = Locale.TAIWAN;
                } else if (language.equals("rCN")) {// 选择中文简体
                    mConfiguration.locale = Locale.SIMPLIFIED_CHINESE;
                }
                mResources.updateConfiguration(mConfiguration, mDm);
                // 保存设置语言的类型
                PreferenceUtil.commitString("language", language);
            } else if ("".equals(language)) {//如果默认语言等于空
                //如果当前语言环境为简体
                if (mLocale.toString().equals(Locale.SIMPLIFIED_CHINESE.toString())) {
                    mConfiguration.locale = Locale.SIMPLIFIED_CHINESE;
                    language = "rCN";
                }
                //如果当前语言环境为繁体
                else if (mLocale.toString().equals(Locale.TAIWAN.toString())) {
                    mConfiguration.locale = Locale.TAIWAN;
                    language = "rTW";
                } else {
                    //默认为中文简体
                    language = "rCN";
                }
            }

        }
        return language;
    }


    /**
     * 更改系统字体大小不会影响app字体大小
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 重写onConfigurationChanged方法当系统语言变更后即时更新
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        System.exit(0);
    }

    /**
     * 获取版本号
     */
    private void getVersion() {
        try {
            channelNumber = DeviceInfo.getAppMetaData(this, "UMENG_CHANNEL");// 获取渠道号

            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            versionCode = info.versionCode;
            String xxx = info.versionName.replace(".", "#");
            String[] split = xxx.split("#");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                sb = sb.append(split[i]);
            }
            version = sb.toString();
        } catch (Exception e) {
            L.d(e.getMessage());
        }
    }

    /*只返回语言参数*/
    public static String getLanguage() {

        if (isLanguage.equals("rCN")) {
            // 如果是中文简体的语言环境
            return BaseURLs.LANGUAGE_SWITCHING_CN;
        } else if (isLanguage.equals("rTW")) {
            // 如果是中文繁体的语言环境
            return BaseURLs.LANGUAGE_SWITCHING_TW;
        } else if (isLanguage.equals("rEN")) {
            // 如果是英文环境
            return BaseURLs.LANGUAGE_SWITCHING_EN;
        } else if (isLanguage.equals("rKO")) {
            // 如果是韩语环境
            return BaseURLs.LANGUAGE_SWITCHING_KO;
        } else if (isLanguage.equals("rID")) {
            // 如果是印尼语
            return BaseURLs.LANGUAGE_SWITCHING_ID;
        } else if (isLanguage.equals("rTH")) {
            // 如果是泰语
            return BaseURLs.LANGUAGE_SWITCHING_TH;
        } else if (isLanguage.equals("rVI")) {
            // 如果是越南语
            return BaseURLs.LANGUAGE_SWITCHING_VI;
        }
        return null;
    }
}
