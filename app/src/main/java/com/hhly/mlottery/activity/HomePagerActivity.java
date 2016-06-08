package com.hhly.mlottery.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.DefaultRetryPolicy;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.homePagerAdapter.HomeListBaseAdapter;
import com.hhly.mlottery.bean.UpdateInfo;
import com.hhly.mlottery.bean.homepagerentity.HomeContentEntity;
import com.hhly.mlottery.bean.homepagerentity.HomeMenusEntity;
import com.hhly.mlottery.bean.homepagerentity.HomeOtherListsEntity;
import com.hhly.mlottery.bean.homepagerentity.HomePagerEntity;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.service.umengPushService;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 首页Activity
 * Created by hhly107 on 2016/3/29.com.hhly.mlottery.activity.HomePagerActivity
 */
public class HomePagerActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final java.lang.String TAG = "HomePagerActivity";
    private Context mContext;// 上下文
    private ImageView public_btn_set;// 登录图标
    private SwipeRefreshLayout mSwipeRefreshLayout;// 下拉刷新
    private ListView home_page_list;// 首页列表
    private HomeListBaseAdapter mListBaseAdapter;// ListView数据适配器
    private TextView public_txt_title;

    public HomePagerEntity mHomePagerEntity;// 首页实体对象
    private UpdateInfo mUpdateInfo;// 版本更新对象
    private static final int LOADING_DATA_START = 0;// 加载数据
    private static final int LOADING_DATA_SUCCESS = 1;// 加载成功
    private static final int LOADING_DATA_ERROR = 2;// 加载失败
    private static final int REFRES_DATA_SUCCESS = 3;// 下拉刷新
    private static final int VERSION_UPDATA_SUCCESS = 4;// 版本更新请求成功
    private static final String COMP_VER = "1"; // 完整版
    private static final String PURE_VER = "2"; // 纯净版
    private long mExitTime;// 退出程序...时间

    private String mPushType;// 推送类型
    private String mThirdId;// id
    private Integer mDataType;// 资讯类型 1、篮球  2、足球
    private String mUrl;// 资讯中转URL
    private String mInfoTypeName;// 资讯标题
    private String imageUrl;// 资讯分享图片Url
    private String title;// 资讯分享标题
    private String subTitle;// 资讯分享摘要

    private String version;// 当前版本Name
    private String versionCode;// 当前版本Code
    private String channelNumber;// 当前版本渠道号

    private final int MIN_CLICK_DELAY_TIME = 2000;// 控件点击间隔时间
    private long lastClickTime = 0;
    private int clickCount = 0;// 点击次数

    /**
     * 跳转其他Activity 的requestcode
     */
    public static final int REQUESTCODE_LOGIN = 100;
    public static final int REQUESTCODE_LOGOUT = 110;
    private ImageView iv_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = HomePagerActivity.this;
        channelNumber = getAppMetaData(mContext, "UMENG_CHANNEL");// 获取渠道号
        getVersion();// 获取版本号
        pushData();
        initView();
        initData();
        initEvent();
        startService(new Intent(mContext, umengPushService.class));
        L.d("xxx", "version:" + version);
        L.d("xxx", "versionCode:" + versionCode);
        L.d("xxx", "channelNumber:" + channelNumber);
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    private String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            L.d(e.getMessage());
        }
        return resultData;
    }

    /**
     * 获取版本号
     */
    private void getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            versionCode = String.valueOf(info.versionCode);
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

    /**
     * 接收推送消息
     */
    private void pushData() {
        MobclickAgent.setDebugMode(AppConstants.isDebugMode);//测试的时候的数据需要设置debug模式
        PushAgent mPushAgent = PushAgent.getInstance(mContext);
        mPushAgent.enable();// 开启推送
        mPushAgent.onAppStart();// 统计应用启动
        pushMessageSkip();// 页面跳转处理

        // 使用友盟统计分析Android 4.6.3 对Fragment统计，开发者需要：来禁止默认的Activity页面统计方式。首先在程序入口处调用
//        MobclickAgent.openActivityDurationTrack(false);
        /*String device_token = UmengRegistrar.getRegistrationId(mContext);
        L.d("xxx","device_token是: " + device_token);
        L.d("xxx"," mPushAgent.isEnabled(): " + mPushAgent.isEnabled());*/
    }

    /**
     * 处理推送消息和跳转处理
     */
    private void pushMessageSkip() {
        Bundle mBundle = getIntent().getExtras();// 获取推送key value
        if (mBundle != null) {
            mPushType = mBundle.getString("pushType");
            mThirdId = mBundle.getString("thirdId");
            mUrl = mBundle.getString("dataUrl");
            mInfoTypeName = mBundle.getString("dataTitle");
            imageUrl = mBundle.getString("imageUrl");
            title = mBundle.getString("title");
            subTitle = mBundle.getString("subTitle");

            try {
                mDataType = mBundle.getString("dataType") == null ? 0 : Integer.parseInt(mBundle.getString("dataType"));
            } catch (NumberFormatException e) {
                L.d(e.getMessage());
            }
            if (!TextUtils.isEmpty(mPushType)) {
                switch (mPushType) {
                    case "lottery":// 彩票列表
                        startActivity(new Intent(mContext, NumbersActivity.class));
                        break;
                    case "lotteryInfo":// 彩票详情页面
                        if (!TextUtils.isEmpty(mThirdId)) {
                            Intent lotteryIntent = new Intent(mContext, NumbersInfoBaseActivity.class);
                            lotteryIntent.putExtra("numberName", mThirdId);
                            startActivity(lotteryIntent);
                        }
                        break;
                    case "football":// 足球列表
                        startActivity(new Intent(mContext, FootballActivity.class));
                        break;
                    case "footballInfo":// 足球详情页面
                        if (!TextUtils.isEmpty(mThirdId)) {
                            Intent footIntent = new Intent(mContext, FootballMatchDetailActivity.class);
                            footIntent.putExtra("currentFragmentId", -1);
                            footIntent.putExtra("thirdId", mThirdId);
                            startActivity(footIntent);
                            L.d("xxx", "mThirdId: " + mThirdId);
                        }
                        break;
                    case "dataInfo":// 资讯详情页面
                        if (!TextUtils.isEmpty(mUrl)) {
                            Intent basketDataIntent = new Intent(mContext, WebActivity.class);
                            basketDataIntent.putExtra("thirdId", mThirdId);
                            basketDataIntent.putExtra("key", mUrl);
                            basketDataIntent.putExtra("type", mDataType);
                            basketDataIntent.putExtra("infoTypeName", mInfoTypeName);
                            basketDataIntent.putExtra("imageurl", imageUrl);
                            basketDataIntent.putExtra("title", title);
                            basketDataIntent.putExtra("subtitle", subTitle);
                            startActivity(basketDataIntent);
                            L.d("xxx", "mUrl: " + mUrl);
                        }
                        break;
                    case "basketball":// 篮球列表
                        startActivity(new Intent(mContext, BasketListActivity.class));
                        break;
                    case "basketballInfo":// 篮球详情页面
                        if (!TextUtils.isEmpty(mThirdId)) {
                            Intent basketIntent = new Intent(mContext, BasketDetailsActivity.class);
                            basketIntent.putExtra("thirdId", mThirdId);
                            startActivity(basketIntent);
                        }
                        break;
                }
                if (!isTaskRoot()) {// 如果当前界面启动了在跳转后把当前界面关掉，避免返回两次，启动了isTaskRoot（）=false
                    this.finish();
                }
            }
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        public_btn_set.setOnClickListener(new View.OnClickListener() { // 登录跳转
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePagerActivity.this, HomeUserOptionsActivity.class));
                MobclickAgent.onEvent(mContext, "HomePagerUserSetting");
            }
        });
        if (AppConstants.isTestEnv) {
            public_txt_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                        lastClickTime = currentTime;
                        clickCount = 0;
                    } else {
                        clickCount += 1;
                        if (clickCount == 5) {
                            startActivity(new Intent(mContext, DebugConfigActivity.class));
                            finish();
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        if (mListBaseAdapter != null) {
            mListBaseAdapter.start();// 启动轮播图
        }
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("HomePagerActivity");
    }


    @Override
    public void onPause() {
        if (mListBaseAdapter != null) {
            mListBaseAdapter.end();// 结束轮播图
        }
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("HomePagerActivity");
    }

    /**
     * 初始化数据
     */
    private void initData() {
        try {
            readObjectFromFile();// 获取本地数据
            versionUpdate();// 版本更新
            getRequestData(0);// 获取网络数据
        } catch (Exception e) {
            L.d("获取数据异常：" + e.getMessage());
        }
    }

    /**
     * 检查版本更新
     */
    private void versionUpdate() {
        Map<String, String> map = new HashMap<String, String>();
        if (AppConstants.fullORsimple) {
            map.put("versionType", PURE_VER);
        } else {
            map.put("versionType", COMP_VER);
        }
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_VERSION_UPDATE, map, new DefaultRetryPolicy(2000, 1, 1), new VolleyContentFast.ResponseSuccessListener<UpdateInfo>() {
            @Override
            public synchronized void onResponse(final UpdateInfo json) {
                if (json != null) {
                    mUpdateInfo = json;
                    mHandler.sendEmptyMessage(VERSION_UPDATA_SUCCESS);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        }, UpdateInfo.class);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        setContentView(R.layout.home_page_main);

        ImageView public_img_back = (ImageView) findViewById(R.id.public_img_back);
        public_img_back.setVisibility(View.GONE);
        ImageView public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);

        iv_account = (ImageView) findViewById(R.id.iv_account);
        if (CommonUtils.isLogin()) {
            iv_account.setImageResource(R.mipmap.login);
        } else {
            iv_account.setImageResource(R.mipmap.logout);
        }
        iv_account.setVisibility(View.VISIBLE);
        iv_account.setOnClickListener(this);

        public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setVisibility(View.VISIBLE);
        public_btn_set.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.home_user_setting));// 设置登录图标

        public_txt_title = (TextView) findViewById(R.id.public_txt_title);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.home_page_swiperefreshlayout);// 下拉刷新
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));
        home_page_list = (ListView) findViewById(R.id.home_page_list);// 首页列表
    }

    /**
     * 后台数据请求
     */
    public synchronized void getRequestData(final int num) {
        mHandler.sendEmptyMessage(LOADING_DATA_START);// 开始加载数据
        // 设置参数
        Map<String, String> myPostParams = new HashMap<>();
        myPostParams.put("version", version);
        myPostParams.put("versionCode", versionCode);
        myPostParams.put("channelNumber", channelNumber);
        VolleyContentFast.requestStringByGet(BaseURLs.URL_HOME_PAGER_INFO, myPostParams, null, new VolleyContentFast.ResponseSuccessListener<String>() {
            @Override
            public void onResponse(String jsonObject) {
                if (jsonObject != null) {// 请求成功
                    try {
                        mHomePagerEntity = JSON.parseObject(jsonObject, HomePagerEntity.class);
                        PreferenceUtil.commitString(AppConstants.HOME_PAGER_DATA_KEY, jsonObject);// 保存首页缓存数据
                        L.d("xxx", "保存数据到本地！jsonObject:" + jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isAuditHandle(mHomePagerEntity);
                    if (mHomePagerEntity.getResult() == 200) {
                        switch (num) {
                            case 0:// 首次加载
                                mHandler.sendEmptyMessage(LOADING_DATA_SUCCESS);
                                break;
                            case 1:// 下拉刷新
                                mHandler.sendEmptyMessage(REFRES_DATA_SUCCESS);
                                break;
                        }
                    } else {
                        mHandler.sendEmptyMessage(LOADING_DATA_ERROR);// 加载失败
                    }
                } else {
                    mHandler.sendEmptyMessage(LOADING_DATA_ERROR);// 加载失败
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                // 请求失败
                L.d("xxx", "请求失败");
                mHandler.sendEmptyMessage(LOADING_DATA_ERROR);// 加载失败
            }
        });
    }

    /**
     * 审核数据处理
     *
     * @审核中需要隐藏彩票相关内容
     */
    private void isAuditHandle(HomePagerEntity jsonObject) {
        // 过虑彩票菜单
        HomeMenusEntity menusEntity = new HomeMenusEntity();
        List<HomeContentEntity> contentList = new ArrayList<>();
        if (jsonObject.getMenus() == null || jsonObject.getMenus().getContent() == null || jsonObject.getMenus().getContent().size() == 0) {
            // 不处理
        } else {
            for (int i = 0, len = jsonObject.getMenus().getContent().size(); i < len; i++) {
                HomeContentEntity homeContentEntity = jsonObject.getMenus().getContent().get(i);
                if (homeContentEntity != null) {
                    switch (homeContentEntity.getJumpAddr()) {
                        case "30":
                        case "31":
                        case "350":
                        case "32":
                        case "33":
                        case "34":
                        case "35":
                        case "36":
                        case "37":
                        case "38":
                        case "39":
                        case "310":
                        case "311":
                        case "312":
                        case "313":
                        case "314":
                        case "315":
                        case "316":
                        case "317":
                        case "318":
                        case "319":
                        case "320":
                        case "321":
                        case "322":
                        case "323":
                            // 正在审核中，不显示彩票信息
                            if ("false".equals(jsonObject.getIsAudit())) {
                                contentList.add(homeContentEntity);
                            }
                            break;
                        default:
                            contentList.add(homeContentEntity);
                            break;
                    }
                }
            }
            menusEntity.setContent(contentList);
            menusEntity.setResult(jsonObject.getMenus().getResult());
        }
        // 过虑彩票条目
        List<HomeOtherListsEntity> otherList = new ArrayList<>();
        if (jsonObject.getOtherLists() == null || jsonObject.getOtherLists().size() == 0) {
            // 不处理
        } else {
            for (int i = 0, len = jsonObject.getOtherLists().size(); i < len; i++) {
                HomeOtherListsEntity homeOtherListsEntity = jsonObject.getOtherLists().get(i);
                if (homeOtherListsEntity != null) {
                    if (homeOtherListsEntity.getContent() != null) {
                        if (homeOtherListsEntity.getContent().getLabType() == 3 && "true".equals(jsonObject.getIsAudit())) {
                            // 正在审核中，不显示彩票信息
                        } else {
                            // 审核完成，显示全部内容
                            otherList.add(homeOtherListsEntity);
                        }
                    }
                }
            }
        }
        mHomePagerEntity.setOtherLists(otherList);
        mHomePagerEntity.setMenus(menusEntity);
    }

    /**
     * 下拉刷新调用
     */
    @Override
    public void onRefresh() {
        getRequestData(1);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING_DATA_START:
                    mSwipeRefreshLayout.setRefreshing(true);
                    break;
                case LOADING_DATA_SUCCESS:
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (mHomePagerEntity != null) {
                        mListBaseAdapter = new HomeListBaseAdapter(mContext, mHomePagerEntity);
                        home_page_list.setAdapter(mListBaseAdapter);
                    }
                    break;
                case LOADING_DATA_ERROR:
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.number_net_error), Toast.LENGTH_SHORT).show();
                    break;
                case REFRES_DATA_SUCCESS:// 下拉刷新
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (mHomePagerEntity != null) {
                        mListBaseAdapter = new HomeListBaseAdapter(mContext, mHomePagerEntity);
                        home_page_list.setAdapter(mListBaseAdapter);
                    }
                    break;
                case VERSION_UPDATA_SUCCESS:// 检查版本更新
                    try {
                        boolean isNewVersion = true;
                        int serverVersion = Integer.parseInt(mUpdateInfo.getVersion()); // 取得服务器上的版本code
                        int currentVersion = Integer.parseInt(versionCode);// 获取当前版本code
                        L.d("xxx", "serverVersion:" + serverVersion);
                        L.d("xxx", "currentVersion:" + currentVersion);
                        if (currentVersion < serverVersion) {// 有更新
                            String versionIgnore = PreferenceUtil.getString(AppConstants.HOME_PAGER_VERSION_UPDATE_KEY, null);// 获取本地忽略版本
                            L.d("xxx", "versionIgnore:" + versionIgnore);
                            if (versionIgnore != null) {
                                if (versionIgnore.contains("#")) {
                                    String[] split = versionIgnore.split("#");
                                    for (int i = 0, len = split.length; i < len; i++) {
                                        if (serverVersion == Integer.parseInt(split[i])) {
                                            isNewVersion = false;
                                            break;
                                        }
                                    }
                                    if (isNewVersion) {
                                        promptVersionUp();
                                    }
                                } else {
                                    if (serverVersion != Integer.parseInt(versionIgnore)) {
                                        promptVersionUp();
                                    }
                                }
                            } else {
                                promptVersionUp();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /**
     * 新版本更新提示
     */
    private void promptVersionUp() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppThemeDialog);//  android.R.style.Theme_Material_Light_Dialog
            builder.setCancelable(false);// 设置对话框以外不可点击
            builder.setTitle(mContext.getResources().getString(R.string.about_soft_update));// 提示标题
            String mMessage = mUpdateInfo.getDescription();// 获取提示内容
            if (mUpdateInfo != null) {
                if (mMessage.contains("#")) {
                    mMessage = mMessage.replace("#", "\n");// 换行处理
                }
                builder.setMessage(mMessage);// 提示内容
            }
            builder.setPositiveButton(mContext.getResources().getString(R.string.basket_analyze_update), new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //Toast.makeText(mContext, "更新", Toast.LENGTH_SHORT).show();
                    DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(mUpdateInfo.getUrl());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    //指定在WIFI状态下，执行下载操作。
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                    //是否允许漫游状态下，执行下载操作
                    request.setAllowedOverRoaming(false);//方法来设置，是否同意漫游状态下 执行操作。 （true，允许； false 不允许；默认是允许的。）
                    //是否允许“计量式的网络连接”执行下载操作
                    request.setAllowedOverMetered(false);// 默认是允许的。
                    //request.setTitle("一比分新版本下载");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setMimeType("application/vnd.android.package-archive");
                    L.d("xxx", "download path = " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                    request.setDestinationInExternalPublicDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), "ybf.apk");
                    // 设置为可被媒体扫描器找到
                    request.allowScanningByMediaScanner();
                    // 设置为可见和可管理
                    request.setVisibleInDownloadsUi(true);
                    long id = downloadManager.enqueue(request);
                    L.d("xxx", "id = " + id);
                }
            });
            builder.setNegativeButton(mContext.getResources().getString(R.string.basket_analyze_dialog_cancle), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    // Toast.makeText(mContext, "取消", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNeutralButton(mContext.getResources().getString(R.string.home_pager_version_update), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String versionIgnore = PreferenceUtil.getString(AppConstants.HOME_PAGER_VERSION_UPDATE_KEY, null);
                    if (versionIgnore != null) {
                        versionIgnore = versionIgnore + "#" + mUpdateInfo.getVersion();
                    } else {
                        versionIgnore = String.valueOf(mUpdateInfo.getVersion());
                    }
                    PreferenceUtil.commitString(AppConstants.HOME_PAGER_VERSION_UPDATE_KEY, versionIgnore);
                    L.d("xxx", "PreferenceUtil...." + PreferenceUtil.getString(AppConstants.HOME_PAGER_VERSION_UPDATE_KEY, null));
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本地数据
     */
    public void readObjectFromFile() {
        L.d("xxx", "获取本地数据.");
        String jsondata = PreferenceUtil.getString(AppConstants.HOME_PAGER_DATA_KEY, null);
        if (jsondata != null) {
            mHomePagerEntity = JSON.parseObject(jsondata, HomePagerEntity.class);
            mListBaseAdapter = new HomeListBaseAdapter(mContext, mHomePagerEntity);
            home_page_list.setAdapter(mListBaseAdapter);
        } else {
            showDefData();
        }
    }


    /**
     * 无网络时显示默认数据
     */
    private void showDefData() {
        try {
            String defDataJson = "{\"banners\": {\"content\": [{\"jumpType\": 0,\"picUrl\": \"xxx\"}],\"result\": 200},\"menus\": {\"content\": [{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"足球比分\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"足球视频\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"足球指数\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"足球数据\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"足球资讯\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"篮球比分\"}],\"result\": 200},\"otherLists\": [{\"content\": {\"bodys\": [{\"date\": \"\",\"guestHalfScore\": 0,\"guestId\": 177,\"guestLogoUrl\": \"xxx\",\"guestScore\": 0,\"guestteam\": \"\",\"homeHalfScore\": 0,\"homeId\": 180,\"homeLogoUrl\": \"xxx\",\"homeScore\": 0,\"hometeam\": \"\",\"jumpType\": 2,\"raceColor\": \"#9933FF\",\"raceId\": \"1\",\"racename\": \"\",\"statusOrigin\": \"0\",\"thirdId\": \"307689\",\"time\": \"\"},{\"date\": \"\",\"guestHalfScore\": 0,\"guestId\": 6164,\"guestLogoUrl\": \"xxx\",\"guestScore\": 0,\"guestteam\": \"\",\"homeHalfScore\": 0,\"homeId\": 6162,\"homeLogoUrl\": \"xxx\",\"homeScore\": 0,\"hometeam\": \"\",\"jumpType\": 2,\"raceColor\": \"#000080\",\"raceId\": \"511\",\"racename\": \"\",\"statusOrigin\": \"0\",\"thirdId\": \"312127\",\"time\": \"\"}],\"labType\": 1},\"result\": 200},{\"content\": {\"bodys\": [{\"date\": \"\",\"jumpType\": 1,\"time\": \"\",\"title\": \"\"},{\"date\": \"\", \"jumpType\": 1,\"picUrl\": \"xxx\",\"time\": \"\",\"title\": \"\"}],\"labType\": 2},\"result\": 200}], \"result\": 200}";
            mHomePagerEntity = JSON.parseObject(defDataJson, HomePagerEntity.class);

            mListBaseAdapter = new HomeListBaseAdapter(mContext, mHomePagerEntity);
            home_page_list.setAdapter(mListBaseAdapter);
        } catch (Exception e) {
            L.d("json解析失败：" + e.getMessage());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                UiUtils.toast(this, getResources().getString(R.string.main_exit_text), 1000);
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);// 退出APP
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_account:
                MobclickAgent.onEvent(mContext, "LoginActivity_Start");
                if (CommonUtils.isLogin()) {
                    goToAccountActivity();
                } else {
                    goToLoginActivity();
                }
                break;
            default:
                break;
        }
    }

    private void goToLoginActivity() {
        startActivityForResult(new Intent(this, LoginActivity.class), REQUESTCODE_LOGIN);
    }

    private void goToAccountActivity() {
        startActivityForResult(new Intent(this, AccountActivity.class), REQUESTCODE_LOGOUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_LOGIN) {
                // 登录成功返回
                L.d(TAG, "登录成功");
                iv_account.setImageResource(R.mipmap.login);
            } else if (requestCode == REQUESTCODE_LOGOUT) {
                L.d(TAG, "注销成功");
                iv_account.setImageResource(R.mipmap.logout);
            }
        }

    }
}
