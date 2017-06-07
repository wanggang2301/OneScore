package com.hhly.mlottery.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.InformationBean;
import com.hhly.mlottery.bean.UmengInfo;
import com.hhly.mlottery.bean.UpdateInfo;
import com.hhly.mlottery.bean.WelcomeUrl;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.cpifrag.CpiFragment;
import com.hhly.mlottery.frame.datafrag.DataFragment;
import com.hhly.mlottery.frame.homefrag.HomeFragment;
import com.hhly.mlottery.frame.infofrag.InfoFragment;
import com.hhly.mlottery.frame.scorefrag.ScoreFragment;
import com.hhly.mlottery.service.umengPushService;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CountDown;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.FragmentUtils;
import com.hhly.mlottery.util.HandMatchId;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import rx.functions.Action1;
import rx.functions.Func1;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * 1.2.3改版后的新首页
 */
public class IndexActivity extends BaseActivity implements View.OnClickListener {

    public final static int HOME_FRAGMENT = 0; //首页
    public final static int SCORE_FRAGMENT = 1;   //比分
    public final static int INFO_FRAGMENT = 2;   //情报
    public final static int CPI_FRAGMENT = 3;  //指数
    public final static int DATA_FRAGMENT = 4;    //资料库
    private static final String COMP_VER = "1"; // 完整版
    private static final String PURE_VER = "2"; // 纯净版
    private Context mContext;

    private RadioGroup mRadioGroup;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    public int currentPosition = 0;// 足球界面Fragment下标
    public int fragmentIndex = 0;

    private UpdateInfo mUpdateInfo;// 版本更新对象
    public PushAgent mPushAgent;
    private String mPushType;// 推送类型
    private String mThirdId;// id
    private String REQTYPE = "3";// 终端类型 3手机app
    private String TERID = "";// 终端id

    private CountDown countDown;
    private ImageView imageAD;// 启动图
    private TextView mTv_verycode;
    private LinearLayout mCount_down;
    private FrameLayout ffWelcome;
    // 权限
    private String[] mPermissions = {
            WRITE_EXTERNAL_STORAGE,
            READ_PHONE_STATE,
            CAMERA,
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
    };

    boolean isLanguageChanger = false;// 语言切换重启app
    private long mExitTime;// 退出程序...时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = IndexActivity.this;
        setContentView(R.layout.activity_index);

        if (getIntent() != null) {
            isLanguageChanger = getIntent().getBooleanExtra("languageChanger", false);
        }

        if (!isLanguageChanger) {
            // 正常启动
            initView();
            initData();
        } else {
            // 跳过启动页
            initHomeData();
            versionUpdate();
        }
    }

    private void initHomeData() {
        initFragment();
        pushData();
        getUmeng();
        startService(new Intent(mContext, umengPushService.class));
    }

    private void showHomePager() {
        ffWelcome.setVisibility(View.GONE);

        //纯净版，默认不显示赔率
        if (AppConstants.fullORsimple && !PreferenceUtil.getBoolean(MyConstants.DEFUALT_SETTING, false)) {
            PreferenceUtil.commitBoolean(MyConstants.RBSECOND, false);
            PreferenceUtil.commitBoolean(MyConstants.rbSizeBall, false);
            PreferenceUtil.commitBoolean(MyConstants.RBOCOMPENSATE, false);
            PreferenceUtil.commitBoolean(MyConstants.RBNOTSHOW, true);
            PreferenceUtil.commitBoolean(MyConstants.DEFUALT_SETTING, true);
        }

        versionUpdate();// 版本更新
    }

    private void initView() {
        imageAD = (ImageView) findViewById(R.id.imageAD);
        // 设置开机页图片
//        ImageLoader.load(mContext, AppConstants.getBootPageId()).into(imageAD);
        imageAD.setBackgroundResource(AppConstants.getBootPageId());

        ffWelcome = (FrameLayout) findViewById(R.id.ff_welcome);
        mTv_verycode = (TextView) findViewById(R.id.tv_verycode);
        mTv_verycode.setOnClickListener(this);
        mCount_down = (LinearLayout) findViewById(R.id.count_down);
    }


    /**
     * 初始化界面View
     */
    private void initFragment() {
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        fragments.add(new HomeFragment());
        fragments.add(new ScoreFragment());
        fragments.add(new InfoFragment());
        fragments.add(new CpiFragment());
        fragments.add(new DataFragment());

        switch (currentPosition) {
            case HOME_FRAGMENT:
                switchFragment(HOME_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_home)).setChecked(true);
                break;
            case SCORE_FRAGMENT:
                switchFragment(SCORE_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_score)).setChecked(true);
                break;
            case INFO_FRAGMENT:
                switchFragment(INFO_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_info)).setChecked(true);
                break;
            case CPI_FRAGMENT:
                switchFragment(CPI_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_cpi)).setChecked(true);
                break;
            case DATA_FRAGMENT:
                switchFragment(DATA_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_data)).setChecked(true);
                break;

            default:
                break;
        }

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.rb_home:
                        MobclickAgent.onEvent(mContext, "Football_Score");
                        currentPosition = HOME_FRAGMENT;
                        switchFragment(HOME_FRAGMENT);
                        break;
                    case R.id.rb_score:
                        MobclickAgent.onEvent(mContext, "Football_News");
                        currentPosition = SCORE_FRAGMENT;
                        switchFragment(SCORE_FRAGMENT);

                        break;
                    case R.id.rb_info:
                        MobclickAgent.onEvent(mContext, "Football_Data");
                        currentPosition = INFO_FRAGMENT;
                        switchFragment(INFO_FRAGMENT);
                        break;
                    case R.id.rb_cpi:
                        MobclickAgent.onEvent(mContext, "Football_Video");
                        currentPosition = CPI_FRAGMENT;
                        switchFragment(CPI_FRAGMENT);

                        break;
                    case R.id.rb_data:
                        MobclickAgent.onEvent(mContext, "Football_CPI");
                        currentPosition = DATA_FRAGMENT;
                        switchFragment(DATA_FRAGMENT);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getStartImageUrl();
            }
        }, 1000);
    }

    /**
     * 请求广告图
     */
    private void getStartImageUrl() {
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_STARTPIC, null, new DefaultRetryPolicy(2000, 1, 1), new VolleyContentFast.ResponseSuccessListener<WelcomeUrl>() {
            @Override
            public synchronized void onResponse(final WelcomeUrl json) {
                initHomeData();
                if (json != null && json.getResult() == 200) {
                    if (TextUtils.isEmpty(json.getUrl())) {
                        mCount_down.setVisibility(View.GONE);
                        getPermissions();
                    } else {
                        mCount_down.setVisibility(View.VISIBLE);
                        int mDuration = json.getDuration() * 1000 + 699;
                        final int finalMDuration = mDuration;
                        Glide.with(getApplicationContext())
                                .load(json.getUrl())
                                .error(R.mipmap.home_menu_icon_def)
                                .placeholder(R.mipmap.home_menu_icon_def)
                                .crossFade()
                                .into(new SimpleTarget<GlideDrawable>() {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                        imageAD.setImageDrawable(resource);
                                        countDown.start();
                                    }

                                    @Override
                                    public void onLoadStarted(Drawable placeholder) {
                                        super.onLoadStarted(placeholder);
                                        //加载开始
                                        toCountdown(finalMDuration);
                                    }

                                    @Override
                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                        super.onLoadFailed(e, errorDrawable);
                                        getPermissions();
                                    }
                                });
                    }
                } else {
                    mCount_down.setVisibility(View.GONE);
                    getPermissions();
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                initHomeData();
                mCount_down.setVisibility(View.GONE);
                getPermissions();
            }
        }, WelcomeUrl.class);
    }

    /**
     * 加载广告图
     *
     * @param duration 显示时长
     */
    private void toCountdown(int duration) {
        countDown = CountDown.getDefault(duration, 1000, new CountDown.CountDownCallback() {
            @Override
            public void onFinish() {
                getPermissions();
                if (countDown != null) {
                    countDown.cancel();
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
                if (mTv_verycode != null) {
                    mTv_verycode.setVisibility(View.VISIBLE);
                    mTv_verycode.setText(millisUntilFinished / CountDown.TIMEOUT_INTERVEL + "" + mContext.getResources().getString(R.string.skip));
                }
            }
        });
    }

    private void getPermissions() {
        RxPermissions.getInstance(this)
                .request(mPermissions)
                .map(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean granted) {
                        if (granted) {

                        }
                        return granted;
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        showHomePager();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable t) {
                        t.printStackTrace();
                        showHomePager();
                    }
                });
    }

    public void switchFragment(int position) {
        fragmentIndex = position;// 当前fragment下标
        L.d("xxx", "当前Fragment下标：" + fragmentIndex);
        fragmentManager = getSupportFragmentManager();
        currentFragment = FragmentUtils.switchFragment(fragmentManager, R.id.ly_content, currentFragment, fragments.get(position).getClass(), null, false, fragments.get(position).getClass().getSimpleName() + position, false);
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

    /**
     * 接收推送消息
     */
    private void pushData() {
        MobclickAgent.setDebugMode(AppConstants.isTestEnv);//测试的时候的数据需要设置debug模式
        mPushAgent = PushAgent.getInstance(mContext);
        mPushAgent.enable();// 开启推送
        String device_token = UmengRegistrar.getRegistrationId(this);
        PreferenceUtil.commitString(AppConstants.uMengDeviceToken, device_token); //存入友盟的deviceToken
        mPushAgent.onAppStart();// 统计应用启动
        pushMessageSkip();// 页面跳转处理

        L.d("xxx", "device_token: " + device_token);
//        String device_id = DeviceInfo.getDeviceId(MyApp.getContext());

    }

    /**
     * 处理推送消息和跳转处理
     */
    private void pushMessageSkip() {
        Bundle mBundle = getIntent().getExtras();// 获取推送key value
        if (mBundle != null) {
            mPushType = mBundle.getString("pushType");
            mThirdId = mBundle.getString("thirdId");
            L.d("xxx", "mPushType:" + mPushType);
            L.d("xxx", "mThirdId:" + mThirdId);
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
                        // startActivity(new Intent(mContext, FootballActivity.class));
                        break;
                    case "footballInfo":// 足球详情页面
                        if (!TextUtils.isEmpty(mThirdId)) {

                            if (HandMatchId.handId(mContext, mThirdId)) {
                                Intent footIntent = new Intent(mContext, FootballMatchDetailActivity.class);
                                footIntent.putExtra("currentFragmentId", -1);
                                footIntent.putExtra("thirdId", mThirdId);
                                startActivity(footIntent);
                            }
                            L.d("xxx", "mThirdId: " + mThirdId);
                        }
                        break;
                    case "dataInfo":// 资讯详情页面
                        if (!TextUtils.isEmpty(mThirdId)) {
                            getInformationByThirdId(mThirdId);// 资讯ID不为空，则请求接口相对应数据
                        }
                        break;
                    case "basketball":// 篮球列表
//
                        break;
                    case "basketballInfo":// 篮球详情页面
                        if (!TextUtils.isEmpty(mThirdId)) {
                            Intent basketIntent = new Intent(mContext, BasketDetailsActivityTest.class);
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
     * 通过资讯Id获取相关资讯信息
     */
    private void getInformationByThirdId(String thirdId) {
        Map<String, String> map = new HashMap<>();
        map.put("infoId", thirdId);// 赛事Id
        map.put("version", MyApp.version);
        map.put("versionCode", String.valueOf(MyApp.versionCode));
        map.put("channelNumber", MyApp.channelNumber);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_INFORMATION_BY_THIRDID, map, new VolleyContentFast.ResponseSuccessListener<InformationBean>() {
            @Override
            public synchronized void onResponse(final InformationBean info) {
                if (info != null) {
                    if (info.getInfo() != null) {

                        L.d("xxx", "推送资讯访问成功！");
                        Intent intent = new Intent(IndexActivity.this, WebActivity.class);
                        intent.putExtra("key", info.getInfo().getInfoUrl());// URL
                        intent.putExtra("imageurl", info.getInfo().getPicUrl());// 图片URl
                        intent.putExtra("infoTypeName", info.getInfo().getInfoTypeName());// 资讯标题
                        intent.putExtra("subtitle", info.getInfo().getSummary());// 分享副标题
                        intent.putExtra("type", info.getInfo().getType());// 关系赛事类型
                        intent.putExtra("thirdId", info.getInfo().getThirdId());// 关系赛事Id
                        intent.putExtra("title", info.getInfo().getTitle());
//                        intent.putExtra("reqMethod", info.getInfo().getRelateMatch());// 是否关联赛事
                        startActivity(intent);
                    }
                } else {
                    L.d("xxx", "InformationBean==null>>>>");
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                L.d("xxx", "推送资讯访问ERROR！");
            }
        }, InformationBean.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_verycode:
                getPermissions();
                if (countDown != null) {
                    countDown.cancel();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 上传手机信息到体彩数据库
     */
    private void getUmeng() {
        new Thread() {
            @Override
            public void run() {
                Location location = null;
                LocationManager locationManager;

                // 获取经纬度
                String serviceName = Context.LOCATION_SERVICE;
                locationManager = (LocationManager) getSystemService(serviceName);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(true);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                String provider = locationManager.getBestProvider(criteria, true);
                if (!TextUtils.isEmpty(provider)) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    location = locationManager.getLastKnownLocation(provider);
                }
                // 获取经纬度end

                RequestParams params = new RequestParams();
                params.addBodyParameter("REQTYPE", REQTYPE);
                TERID = PreferenceUtil.getString(AppConstants.TERID, "");// 端口id
                params.addBodyParameter("TERID", TERID);
                params.addBodyParameter("IMEI", DeviceInfo.getDeviceId(mContext));
                params.addBodyParameter("IMSI", DeviceInfo.getSubscriberId(mContext));
                params.addBodyParameter("DN", DeviceInfo.getManufacturer());// 手机厂商
                params.addBodyParameter("DT", DeviceInfo.getModel());// 手机型号
                if (location != null) {// 如果获取到当前位置
                    MyApp.LA = location.getLatitude();
                    MyApp.LO = location.getLongitude();
                    params.addBodyParameter("LA", "" + MyApp.LA);// 经度
                    params.addBodyParameter("LO", "" + MyApp.LO);// 纬度
                } else {// 如果当前位置没获取到
                    params.addBodyParameter("LA", "");// 经度
                    params.addBodyParameter("LO", "");// 纬度
                }

                params.addBodyParameter("OS", "android");// 终端手机操作系统
                params.addBodyParameter("CHANNEL", MyApp.channelNumber);// umeng渠道号
                params.addBodyParameter("APPVER", MyApp.version);// 版本号
                try {
                    new HttpUtils().send(HttpMethod.POST, BaseURLs.UMENG_CHANNEL_URL, params, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            try {
                                if (responseInfo != null) {
                                    String text = responseInfo.result;
                                    UmengInfo mUmengInfo = JSONObject.parseObject(text, UmengInfo.class);
                                    PreferenceUtil.commitString(AppConstants.TERID, mUmengInfo.getTERID());

                                    L.d("上传用户信息成功:" + responseInfo.toString());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                        }
                    });
                } catch (JSONException e) {
                }
            }
        }.start();
    }

    /**
     * 检查版本更新
     */
    private void versionUpdate() {
        Map<String, String> map = new HashMap<>();
        if (AppConstants.fullORsimple) {
            map.put("versionType", PURE_VER);
        } else {
            map.put("versionType", COMP_VER);
        }

        // 各版本升级参数
        switch (MyApp.isPackageName) {
            case AppConstants.PACKGER_NAME_ZH:
                map.put("localeType", AppConstants.LOCALETYPE_ZH);
                break;
            case AppConstants.PACKGER_NAME_TH:
                map.put("localeType", AppConstants.LOCALETYPE_TH);
                break;
            case AppConstants.PACKGER_NAME_VN:
                map.put("localeType", AppConstants.LOCALETYPE_VN);
                break;
            case AppConstants.PACKGER_NAME_VN_HN:
                map.put("localeType", AppConstants.LOCALETYPE_VN_HN);
                break;
            case AppConstants.PACKGER_NAME_UK:
                map.put("localeType", AppConstants.LOCALETYPE_UK);
                break;
            default:
                break;
        }

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_VERSION_UPDATE, map, new DefaultRetryPolicy(2000, 1, 1), new VolleyContentFast.ResponseSuccessListener<UpdateInfo>() {
            @Override
            public synchronized void onResponse(final UpdateInfo json) {
                if (json != null) {
                    mUpdateInfo = json;
                    checkUpdata();
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        }, UpdateInfo.class);
    }

    /**
     * 检查版本更新
     */
    private void checkUpdata() {
        try {
            boolean isNewVersion = true;
            int serverVersion = Integer.parseInt(mUpdateInfo.getVersion()); // 取得服务器上的版本code
            if (MyApp.versionCode < serverVersion) {// 有更新
                String versionIgnore = PreferenceUtil.getString(AppConstants.HOME_PAGER_VERSION_UPDATE_KEY, null);// 获取本地忽略版本
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
    }

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
                    // 先判断是否为网络数据连接
                    ConnectivityManager connectMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo info = connectMgr.getActiveNetworkInfo();
                    if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // 判断是否是手机网络
                        promptNetInfo();
                    } else {
                        downloadUpgrade();
                    }
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
        final ProgressDialog progressBar = new ProgressDialog(mContext);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setMessage(mContext.getResources().getString(R.string.download_update));
        progressBar.setMax(100);
        progressBar.setProgress(0);
        final File saveFile = new File(Environment.getExternalStorageDirectory().getPath() + "/ybf_full_GF1001.apk");
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

    /**
     * 安装新版本apk
     *
     * @param file
     */
    private void installAPK(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
