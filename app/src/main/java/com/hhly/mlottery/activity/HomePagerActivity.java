package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.homePagerAdapter.HomeListBaseAdapter;
import com.hhly.mlottery.bean.homepagerentity.HomePagerEntity;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

/**
 * 首页Activity
 * Created by hhly107 on 2016/3/29.com.hhly.mlottery.activity.HomePagerActivity
 */
public class HomePagerActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;// 上下文
    private ImageView public_btn_set;// 登录图标
    private SwipeRefreshLayout mSwipeRefreshLayout;// 下拉刷新
    private ListView home_page_list;// 首页列表
    private HomeListBaseAdapter mListBaseAdapter;// ListView数据适配器

    public HomePagerEntity mHomePagerEntity;// 首页实体对象
    private static final int LOADING_DATA_START = 0;// 加载数据
    private static final int LOADING_DATA_SUCCESS = 1;// 加载成功
    private static final int LOADING_DATA_ERROR = 2;// 加载失败
    private static final int REFRES_DATA_SUCCESS = 3;// 下拉刷新
    private long mExitTime;// 退出程序...时间

    private String mPushType;// 推送类型
    private String mThirdId;// id
    private Integer mDataType;// 资讯类型 1、篮球  2、足球
    private String mUrl;// 资讯中转URL
    private String mInfoTypeName;// 资讯标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = HomePagerActivity.this;
        pushData();
        initView();
        initData();
        initEvent();
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
        /*MobclickAgent.openActivityDurationTrack(false);
        String device_token = UmengRegistrar.getRegistrationId(mContext);
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
                            footIntent.putExtra("currentFragmentId", 0);
                            footIntent.putExtra("thirdId", mThirdId);
                            startActivity(footIntent);
                        }
                        break;
                    case "dataInfo":// 资讯详情页面
                        if (!TextUtils.isEmpty(mUrl)) {
                            Intent basketDataIntent = new Intent(mContext, WebActivity.class);
                            basketDataIntent.putExtra("thirdId", mThirdId);
                            basketDataIntent.putExtra("key", mUrl);
                            basketDataIntent.putExtra("type", mDataType);
                            basketDataIntent.putExtra("infoTypeName", mInfoTypeName);
                            startActivity(basketDataIntent);
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
            getRequestData(0);// 获取网络数据
        } catch (Exception e) {
            L.d("获取数据异常：" + e.getMessage());
        }
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

        public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setVisibility(View.VISIBLE);
        public_btn_set.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.home_user_setting));// 设置登录图标

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
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_HOME_PAGER_INFO, new VolleyContentFast.ResponseSuccessListener<HomePagerEntity>() {
            @Override
            public synchronized void onResponse(final HomePagerEntity jsonObject) {
                if (jsonObject != null) {// 请求成功
                    mHomePagerEntity = jsonObject;
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
        }, HomePagerEntity.class);
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
            }
        }
    };

    /**
     * 获取本地数据
     */
    public void readObjectFromFile() {
        File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录
        File sdFile = new File(sdCardDir, "OneScoreData.dat");
        try {
            FileInputStream fis = new FileInputStream(sdFile);   //获得输入流
            ObjectInputStream ois = new ObjectInputStream(fis);
            mHomePagerEntity = (HomePagerEntity) ois.readObject();
            ois.close();
        } catch (Exception e) {
            L.d("获取本地数据失败：" + e.getMessage());
        }
        if (mHomePagerEntity != null) {
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
            String defDataJson = "{\"result\":200,\"menus\":{\"content\":[{\"jumpType\":2,\"jumpAddr\":null,\"title\":\"足球比分\",\"picUrl\":\"xxx\"},{\"jumpType\":2,\"jumpAddr\":null,\"title\":\"足球视频\",\"picUrl\":\"xxx\"},{\"jumpType\":2,\"jumpAddr\":null,\"title\":\"足球指数\",\"picUrl\":\"xxx\"},{\"jumpType\":2,\"jumpAddr\":null,\"title\":\"足球数据\",\"picUrl\":\"xxx\"},{\"jumpType\":2,\"jumpAddr\":null,\"title\":\"足球资讯\",\"picUrl\":\"xxx\"},{\"jumpType\":2,\"jumpAddr\":null,\"title\":\"篮球比分\",\"picUrl\":\"xxx\"},{\"jumpType\":2,\"jumpAddr\":null,\"title\":\"香港开奖\",\"picUrl\":\"xxx\"},{\"jumpType\":2,\"jumpAddr\":null,\"title\":\"彩票开奖\",\"picUrl\":\"xxx\"}],\"result\":200},\"otherLists\":[{\"content\":{\"labType\":1,\"bodys\":[{\"jumpType\":2,\"jumpAddr\":null,\"thirdId\":\"307689\",\"racename\":\"\",\"raceId\":\"1\",\"raceColor\":\"#9933FF\",\"date\":\"\",\"time\":\"\",\"homeId\":180,\"hometeam\":\"\",\"guestId\":177,\"guestteam\":\"\",\"statusOrigin\":\"0\",\"homeScore\":0,\"guestScore\":0,\"homeHalfScore\":0,\"guestHalfScore\":0,\"homeLogoUrl\":\"xxx\",\"guestLogoUrl\":\"xxx\"},{\"jumpType\":2,\"jumpAddr\":null,\"thirdId\":\"312127\",\"racename\":\"\",\"raceId\":\"511\",\"raceColor\":\"#000080\",\"date\":\"\",\"time\":\"\",\"homeId\":6162,\"hometeam\":\"\",\"guestId\":6164,\"guestteam\":\"\",\"statusOrigin\":\"0\",\"homeScore\":0,\"guestScore\":0,\"homeHalfScore\":0,\"guestHalfScore\":0,\"homeLogoUrl\":\"xxx\",\"guestLogoUrl\":\"xxx\"}]},\"result\":200},{\"content\":{\"labType\":2,\"bodys\":[{\"jumpType\":1,\"jumpAddr\":null,\"picUrl\":null,\"title\":\"\",\"date\":\"\",\"time\":\"\"},{\"jumpType\":1,\"jumpAddr\":null,\"picUrl\":\"xxx\",\"title\":\"\",\"date\":\"\",\"time\":\"\"}]},\"result\":200},{\"content\":{\"labType\":3,\"bodys\":[{\"jumpType\":2,\"jumpAddr\":null,\"name\":\"1\",\"issue\":\"-\",\"numbers\":null,\"picUrl\":\"xxx\",\"zodiac\":null},{\"jumpType\":2,\"jumpAddr\":null,\"name\":\"15\",\"issue\":\"-\",\"numbers\":null,\"picUrl\":\"xxx\"}]},\"result\":200}],\"banners\":{\"content\":[{\"jumpType\":0,\"jumpAddr\":null,\"picUrl\":\"xxx\"}],\"result\":200}}";
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
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录
                    File sdFile = new File(sdCardDir, "OneScoreData.dat");
                    try {
                        FileOutputStream fos = new FileOutputStream(sdFile);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(mHomePagerEntity);// 写入
                        fos.close(); // 关闭输出流
                    } catch (Exception e) {
                        L.d("保存数据到本地失败：" + e.getMessage());
                    }
                }
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
