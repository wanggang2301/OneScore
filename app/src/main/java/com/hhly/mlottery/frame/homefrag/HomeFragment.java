package com.hhly.mlottery.frame.homefrag;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.DefaultRetryPolicy;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.BasketballScoresActivity;
import com.hhly.mlottery.activity.DebugConfigActivity;
import com.hhly.mlottery.activity.FootballActivity;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.activity.HomeUserOptionsActivity;
import com.hhly.mlottery.activity.NumbersActivity;
import com.hhly.mlottery.activity.NumbersInfoBaseActivity;
import com.hhly.mlottery.activity.ProductAdviceActivity;
import com.hhly.mlottery.activity.WebActivity;
import com.hhly.mlottery.adapter.homePagerAdapter.HomeListBaseAdapter;
import com.hhly.mlottery.bean.InformationBean;
import com.hhly.mlottery.bean.UpdateInfo;
import com.hhly.mlottery.bean.homepagerentity.HomeBodysEntity;
import com.hhly.mlottery.bean.homepagerentity.HomeContentEntity;
import com.hhly.mlottery.bean.homepagerentity.HomeMenusEntity;
import com.hhly.mlottery.bean.homepagerentity.HomeOtherListsEntity;
import com.hhly.mlottery.bean.homepagerentity.HomePagerEntity;
import com.hhly.mlottery.callback.ProductListener;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.service.umengPushService;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;

import static android.app.Activity.RESULT_OK;

/**
 * 首页
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String TAG = "HomeFragment";

    private View mView;

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
    private static final int DOWNLOAD_UPGRADE = 5;// 准备开始升级下载
    private static final int REFRESH_ADVICE = 6;
    private static final String COMP_VER = "1"; // 完整版
    private static final String PURE_VER = "2"; // 纯净版

    private String version;// 当前版本Name
    private String versionCode;// 当前版本Code
    private String channelNumber;// 当前版本渠道号

    private final int MIN_CLICK_DELAY_TIME = 2000;// 控件点击间隔时间
    private long lastClickTime = 0;
    private int clickCount = 0;// 点击次数
    private ProgressDialog progressBar;

    private int REQUEST_CODE = 1;

    public ProductListener mListener;

    private Activity mActivity;


    /**
     * 跳转其他Activity 的requestcode
     */
    public static final int REQUESTCODE_LOGIN = 100;
    public static final int REQUESTCODE_LOGOUT = 110;
    private File saveFile;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = mActivity;

        channelNumber = getAppMetaData(mContext, "UMENG_CHANNEL");// 获取渠道号
        getVersion();// 获取版本号
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();

        initData();
        initEvent();
        return mView;
    }


    /**
     * 初始化布局
     */
    private void initView() {
        ImageView public_img_back = (ImageView) mView.findViewById(R.id.public_img_back);
        public_img_back.setVisibility(View.GONE);
        ImageView public_btn_filter = (ImageView) mView.findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);

        public_btn_set = (ImageView) mView.findViewById(R.id.public_btn_set);
        public_btn_set.setVisibility(View.VISIBLE);
        if (CommonUtils.isLogin()) {
            public_btn_set.setImageResource(R.mipmap.login);
        } else {
            public_btn_set.setImageResource(R.mipmap.logout);
        }
        public_btn_set.setOnClickListener(this);

        // public_btn_set.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.home_user_setting));// 设置登录图标

        public_txt_title = (TextView) mView.findViewById(R.id.public_txt_title);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.home_page_swiperefreshlayout);// 下拉刷新
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));
        home_page_list = (ListView) mView.findViewById(R.id.home_page_list);// 首页列表

        mListener = new ProductListener() {
            @Override
            public void toProductActivity() {
                startActivityForResult(new Intent(mContext, ProductAdviceActivity.class), REQUEST_CODE);
            }
        };
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
     * 初始化事件
     */
    private void initEvent() {

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
                            getActivity().finish();
                        }
                    }
                }
            });
        }
    }

    /**
     * 后台数据请求
     */
    public synchronized void getRequestData(final int num) {
        if (num != 2) {
            mHandler.sendEmptyMessage(LOADING_DATA_START);// 开始加载数据
        }
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
                        /**----将百度渠道的游戏竞猜和彩票相关去除掉--Start---*/
                        if ("B1001".equals(channelNumber) || "B1002".equals(channelNumber) || "B1003".equals(channelNumber) || "Q01116".equals(channelNumber)) {
                            // 处理条目入口
                            Iterator<HomeOtherListsEntity> iteratorItem = mHomePagerEntity.getOtherLists().iterator();
                            while (iteratorItem.hasNext()) {
                                HomeOtherListsEntity nextEntity = iteratorItem.next();
                                if (nextEntity.getContent().getLabType() == 3 || nextEntity.getContent().getLabType() == 7) {
                                    iteratorItem.remove();
                                }
                            }
                            // 处理菜单入口
                            Iterator<HomeContentEntity> iterator = mHomePagerEntity.getMenus().getContent().iterator();
                            while (iterator.hasNext()) {
                                HomeContentEntity b = iterator.next();
                                if ("遊戲競猜".equals(b.getTitle()) || "游戏竞猜".equals(b.getTitle()) ||
                                        "30".equals(b.getJumpAddr()) ||
                                        "31".equals(b.getJumpAddr()) ||
                                        "32".equals(b.getJumpAddr()) ||
                                        "33".equals(b.getJumpAddr()) ||
                                        "34".equals(b.getJumpAddr()) ||
                                        "35".equals(b.getJumpAddr()) ||
                                        "36".equals(b.getJumpAddr()) ||
                                        "37".equals(b.getJumpAddr()) ||
                                        "38".equals(b.getJumpAddr()) ||
                                        "39".equals(b.getJumpAddr()) ||
                                        "310".equals(b.getJumpAddr()) ||
                                        "311".equals(b.getJumpAddr()) ||
                                        "312".equals(b.getJumpAddr()) ||
                                        "313".equals(b.getJumpAddr()) ||
                                        "314".equals(b.getJumpAddr()) ||
                                        "315".equals(b.getJumpAddr()) ||
                                        "316".equals(b.getJumpAddr()) ||
                                        "317".equals(b.getJumpAddr()) ||
                                        "318".equals(b.getJumpAddr()) ||
                                        "319".equals(b.getJumpAddr()) ||
                                        "320".equals(b.getJumpAddr()) ||
                                        "321".equals(b.getJumpAddr()) ||
                                        "322".equals(b.getJumpAddr()) ||
                                        "323".equals(b.getJumpAddr()) ||
                                        "324".equals(b.getJumpAddr()) ||
                                        "325".equals(b.getJumpAddr()) ||
                                        "326".equals(b.getJumpAddr()) ||
                                        "327".equals(b.getJumpAddr()) ||
                                        "328".equals(b.getJumpAddr()) ||
                                        "329".equals(b.getJumpAddr()) ||
                                        "330".equals(b.getJumpAddr()) ||
                                        "331".equals(b.getJumpAddr()) ||
                                        "332".equals(b.getJumpAddr())) {
                                    iterator.remove();
                                }
                            }
                        }
                        /**----将百度渠道的游戏竞猜和彩票相关去除掉--End---*/
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
                            case 2: //产品建议界面返回
                                // 只是为了请求一下新数据。不做其他处理
                                mHandler.sendEmptyMessage(REFRESH_ADVICE);
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
                if (homeContentEntity != null && homeContentEntity.getJumpAddr() != null) {
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
     * 获取本地数据
     */
    public void readObjectFromFile() {
        L.d("xxx", "获取本地数据.");
        String jsondata = PreferenceUtil.getString(AppConstants.HOME_PAGER_DATA_KEY, null);
        if (jsondata != null) {
            mHomePagerEntity = JSON.parseObject(jsondata, HomePagerEntity.class);
            mListBaseAdapter = new HomeListBaseAdapter(mContext, mHomePagerEntity);
            //设置跳转监听
            mListBaseAdapter.setToProductListener(mListener);
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
            String defDataJson = "{\"banners\": {\"content\": [{\"jumpType\": 0,\"picUrl\": \"xxx\"}],\"result\": 200},\"menus\": {\"content\": [{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"}],\"result\": 200},\"otherLists\": [{\"content\": {\"bodys\": [{\"date\": \"\",\"guestHalfScore\": 0,\"guestId\": 177,\"guestLogoUrl\": \"xxx\",\"guestScore\": 0,\"guestteam\": \"\",\"homeHalfScore\": 0,\"homeId\": 180,\"homeLogoUrl\": \"xxx\",\"homeScore\": 0,\"hometeam\": \"\",\"jumpType\": 2,\"raceColor\": \"#9933FF\",\"raceId\": \"1\",\"racename\": \"\",\"statusOrigin\": \"0\",\"thirdId\": \"307689\",\"time\": \"\"},{\"date\": \"\",\"guestHalfScore\": 0,\"guestId\": 6164,\"guestLogoUrl\": \"xxx\",\"guestScore\": 0,\"guestteam\": \"\",\"homeHalfScore\": 0,\"homeId\": 6162,\"homeLogoUrl\": \"xxx\",\"homeScore\": 0,\"hometeam\": \"\",\"jumpType\": 2,\"raceColor\": \"#000080\",\"raceId\": \"511\",\"racename\": \"\",\"statusOrigin\": \"0\",\"thirdId\": \"312127\",\"time\": \"\"}],\"labType\": 1},\"result\": 200},{\"content\": {\"bodys\": [{\"date\": \"\",\"jumpType\": 1,\"time\": \"\",\"title\": \"\"},{\"date\": \"\", \"jumpType\": 1,\"picUrl\": \"xxx\",\"time\": \"\",\"title\": \"\"}],\"labType\": 2},\"result\": 200}], \"result\": 200}";
            mHomePagerEntity = JSON.parseObject(defDataJson, HomePagerEntity.class);

            mListBaseAdapter = new HomeListBaseAdapter(mContext, mHomePagerEntity);

            //设置跳转监听
            mListBaseAdapter.setToProductListener(mListener);

            home_page_list.setAdapter(mListBaseAdapter);
        } catch (Exception e) {
            L.d("json解析失败：" + e.getMessage());
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
        map.put("localeType", AppConstants.LOCALETYPE_ZH);// 国内版升级参数
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
                        //设置跳转监听
                        mListBaseAdapter.setToProductListener(mListener);
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
                        //设置跳转监听
                        mListBaseAdapter.setToProductListener(mListener);
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
//                            promptVersionUp();
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
                case DOWNLOAD_UPGRADE:
                    // 先判断是否为网络数据连接
                    ConnectivityManager connectMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo info = connectMgr.getActiveNetworkInfo();
                    if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // 判断是否是手机网络
                        promptNetInfo();
                    } else {
                        downloadUpgrade();
                    }
                    break;
                case REFRESH_ADVICE: //刷新首页点赞数
                    for (int i = 0, len = mHomePagerEntity.getOtherLists().size(); i < len; i++) {
                        int labType = mHomePagerEntity.getOtherLists().get(i).getContent().getLabType();// 获取类型
                        List<HomeBodysEntity> bodys = mHomePagerEntity.getOtherLists().get(i).getContent().getBodys();
                        for (int j = 0, len1 = bodys.size(); j < len1; j++) {
                            if (labType == 5) { //产品建议
                                mListBaseAdapter.addLike(bodys.get(0), bodys);
                                mListBaseAdapter.notifyDataSetChanged();
                            }
                        }
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
                //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mHandler.sendEmptyMessage(DOWNLOAD_UPGRADE);
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
     * 当前连接的网络提示
     */
    private void promptNetInfo() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppThemeDialog);
            builder.setCancelable(false);// 设置对话框以外不可点击
            builder.setTitle(mContext.getResources().getString(R.string.to_update_kindly_reminder));// 提示标题
            builder.setMessage(mContext.getResources().getString(R.string.kindly_reminder_comment));// 提示内容
            builder.setPositiveButton(mContext.getResources().getString(R.string.continue_download), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    downloadUpgrade();
                }
            });
            builder.setNegativeButton(mContext.getResources().getString(R.string.basket_analyze_dialog_cancle), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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
     * 开始下载升级
     */
    private void downloadUpgrade() {
        progressBar = new ProgressDialog(mContext);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setMessage(mContext.getResources().getString(R.string.download_update));
        progressBar.setMax(100);
        progressBar.setProgress(0);
        saveFile = new File(Environment.getExternalStorageDirectory().getPath() + "/ybf_full_GF1001.apk");
        HttpRequest.download(mUpdateInfo.getUrl(), saveFile, new FileDownloadCallback() {
            @Override
            public void onStart() {
                super.onStart();
                progressBar.show();
                Toast.makeText(mContext, mContext.getResources().getString(R.string.version_update_title), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int progress, long networkSpeed) {
                super.onProgress(progress, networkSpeed);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                progressBar.dismiss();
                Toast.makeText(mContext, mContext.getResources().getString(R.string.download_error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDone() {
                super.onDone();
                progressBar.dismiss();
                installAPK(saveFile);
            }
        });
    }

    private void installAPK(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }


    @Override
    public void onRefresh() {
        getRequestData(1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_btn_set:
                MobclickAgent.onEvent(mContext, "HomePager_User_Info_Start");
                goToUserOptionsActivity();
                break;

            default:
                break;
        }
    }

    private void goToUserOptionsActivity() {
        startActivityForResult(new Intent(mContext, HomeUserOptionsActivity.class), REQUESTCODE_LOGIN);
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
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_LOGIN) {
                // 登录成功返回
                L.d(TAG, "登录成功");
                public_btn_set.setImageResource(R.mipmap.login);
            } else if (requestCode == REQUESTCODE_LOGOUT) {
                L.d(TAG, "注销成功");
                public_btn_set.setImageResource(R.mipmap.logout);
            }
        } else if (resultCode == ProductAdviceActivity.RESULT_CODE) {
            getRequestData(2);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;

    }
}
