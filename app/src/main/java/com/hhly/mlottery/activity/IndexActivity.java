package com.hhly.mlottery.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.InformationBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.cpifrag.CpiFragment;
import com.hhly.mlottery.frame.datafrag.DataFragment;
import com.hhly.mlottery.frame.homefrag.HomeFragment;
import com.hhly.mlottery.frame.infofrag.InfoFragment;
import com.hhly.mlottery.frame.scorefrag.ScoreFragment;
import com.hhly.mlottery.service.umengPushService;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.FragmentUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1.2.3改版后的新首页
 */
public class IndexActivity extends BaseActivity {

    public final static int HOME_FRAGMENT = 0; //首页
    public final static int SCORE_FRAGMENT = 1;   //比分
    public final static int INFO_FRAGMENT = 2;   //情报
    public final static int CPI_FRAGMENT = 3;  //指数
    public final static int DATA_FRAGMENT = 4;    //资料库
    private Context mContext;

    private RadioGroup mRadioGroup;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    public int currentPosition = 0;// 足球界面Fragment下标
    public int fragmentIndex = 0;

    public PushAgent mPushAgent;
    private String mPushType;// 推送类型
    private String mThirdId;// id
    private String version;// 当前版本Name
    private String versionCode;// 当前版本Code
    private String channelNumber;// 当前版本渠道号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = IndexActivity.this;
        setContentView(R.layout.activity_index);
        channelNumber = DeviceInfo.getAppMetaData(mContext, "UMENG_CHANNEL");// 获取渠道号
        getVersion();
        pushData();

        initView();
        initData();
        startService(new Intent(mContext, umengPushService.class));
    }


    /**
     * 初始化界面View
     */
    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        fragments.add(new HomeFragment());
        fragments.add(new ScoreFragment());
        fragments.add(new InfoFragment());
        fragments.add(new CpiFragment());
        fragments.add(new DataFragment());
    }

    private void initData() {
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
//
                        currentPosition = SCORE_FRAGMENT;
                        switchFragment(SCORE_FRAGMENT);

                        break;
                    case R.id.rb_info:
                        MobclickAgent.onEvent(mContext, "Football_Data");
//                        if (currentPosition == SCORES_FRAGMENT) {
//                            ((ScoresFragment) currentFragment).disconnectWebSocket();
//                        }
                        currentPosition = INFO_FRAGMENT;
                        switchFragment(INFO_FRAGMENT);
                        break;
                    case R.id.rb_cpi:
                        MobclickAgent.onEvent(mContext, "Football_Video");
//                        if (currentPosition == SCORES_FRAGMENT) {
//                            ((ScoresFragment) currentFragment).disconnectWebSocket();
//                        }
                        currentPosition = CPI_FRAGMENT;
                        switchFragment(CPI_FRAGMENT);

                        break;
                    case R.id.rb_data:
                        MobclickAgent.onEvent(mContext, "Football_CPI");
//                        if (currentPosition == SCORES_FRAGMENT) {
//                            ((ScoresFragment) currentFragment).disconnectWebSocket();
//                        }
                        currentPosition = DATA_FRAGMENT;
                        switchFragment(DATA_FRAGMENT);
                        break;
                    default:
                        break;
                }

                //测试切换到其他页面能否收到推送消息
                //EventBus.getDefault().post(new ScoreFragmentWebSocketEntity(currentPosition));

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
    protected void onDestroy() {
        super.onDestroy();
    }

    private long mExitTime;// 退出程序...时间


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
                            Intent footIntent = new Intent(mContext, FootballMatchDetailActivity.class);
                            footIntent.putExtra("currentFragmentId", -1);
                            footIntent.putExtra("thirdId", mThirdId);
                            startActivity(footIntent);
                            L.d("xxx", "mThirdId: " + mThirdId);
                        }
                        break;
                    case "dataInfo":// 资讯详情页面
                        if (!TextUtils.isEmpty(mThirdId)) {
                            getInformationByThirdId(mThirdId);// 资讯ID不为空，则请求接口相对应数据
                        }
                        break;
                    case "basketball":// 篮球列表
//                        Intent intent = new Intent(mContext, FootballActivity.class);
//                        intent.putExtra(AppConstants.FOTTBALL_KEY, AppConstants.BASKETBALL_SCORE_VALUE);
//                        mContext.startActivity(intent);
                      //  Intent intent = new Intent(mContext, BasketballScoresActivity.class);
                       // mContext.startActivity(intent);
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
        map.put("version", version);
        map.put("versionCode", versionCode);
        map.put("channelNumber", channelNumber);
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
                        intent.putExtra("reqMethod", info.getInfo().getRelateMatch());// 是否关联赛事
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
}
