package com.hhly.mlottery;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;

import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.CrashException;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.DataBus;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.Locale;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import io.rong.imkit.RongIM;


/**
 * @author Tenney
 * @ClassName: MyApp
 * @Description: TODO
 * @date 2015-10-15 上午9:48:39
 */
public class MyApp extends Application {
    private static MyApp appcontext = null;
    public static Resources mResources;
    public static Configuration mConfiguration;
    public static DisplayMetrics mDm;
    public static Locale mLocale;
    /**
     * 获取语言
     */
    public static String isLanguage;

    @Override
    public void onCreate() {
        appcontext = this;
        initImageLoader(this);
//		CyUtils.initCy(this);
        // 初始化PreferenceUtil
        PreferenceUtil.init(this);
        //初始化获取语言环境
        mResources = appcontext.getResources();
        mConfiguration = mResources.getConfiguration();
        mDm = mResources.getDisplayMetrics();
        mLocale = mConfiguration.locale;
        // 根据上次的语言设置，重新设置语言
        switchLanguage(PreferenceUtil.getString("language", ""));
        isLanguage = switchLanguage(PreferenceUtil.getString("language", ""));
        // 捕获异常
        if (AppConstants.isTestEnv) {
            CrashException crashException = CrashException.getInstance();
            crashException.init(getApplicationContext());
        }
        VolleyContentFast.init(this);
        //初始化畅言
        CyUtils.initCy(this);
        initUserInfo();
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) || "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            // 初始化融云
            RongIM.init(this);
        }

        /**
         * OkHttpFinal(此初始化只是简单赋值不会阻塞线程)
         */
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());

        super.onCreate();
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
        CommonUtils.initRegisterInfo();
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

    public void initImageLoader(Context context) {
        // 用ImageLoader必须在application
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(5)
                // 线程池内加载的数量
                .denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

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
                }
//			else if (language.equals("rCN")) {// 选择中文简体
//				mConfiguration.locale = Locale.SIMPLIFIED_CHINESE;
//			}
                else if (language.equals("rEN")) {// 选择英文
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
                // 设置应用语言类型
                //如果当前语言环境为简体
//			if(mLocale.toString().equals(Locale.SIMPLIFIED_CHINESE.toString())){
//				mConfiguration.locale = Locale.SIMPLIFIED_CHINESE;
//				language = "rCN";
//			}
                //如果当前语言环境为繁体
                if (mLocale.toString().equals(Locale.TAIWAN.toString())) {
                    mConfiguration.locale = Locale.TAIWAN;
                    language = "rTW";
                }
                //如果当前语言环境为英语
                else if (mLocale.toString().equals(Locale.US.toString())) {
                    mConfiguration.locale = Locale.US;
                    language = "rEN";
                }
                //如果当前语言环境为韩语
                else if (mLocale.toString().equals(Locale.KOREA.toString())) {
                    mConfiguration.locale = Locale.KOREA;
                    language = "rKO";
                }
                //如果当前语言环境为印度尼西亚
                else if (mLocale.toString().equals("in_ID")) {
                    mConfiguration.locale = new Locale("in", "ID");
                    language = "rID";
                }
                //如果当前语言环境为泰语
                else if (mLocale.toString().equals("th_TH")) {
                    mConfiguration.locale = new Locale("th", "TH");
                    language = "rTH";
                }
                //如果当前语言环境为越南语
                else if (mLocale.toString().equals("vi_VN")) {
                    mConfiguration.locale = new Locale("vi", "VN");
                    language = "rVI";
                } else {//默认英语
                    language = "rEN";
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
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        System.exit(0);
    }
}
