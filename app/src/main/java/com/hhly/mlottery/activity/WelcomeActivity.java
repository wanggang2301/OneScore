package com.hhly.mlottery.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.UmengInfo;
import com.hhly.mlottery.bean.WelcomeUrl;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CountDown;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author Tenney
 * @ClassName: WelcomeActivity
 * @Description: 启动界面
 * @date 2015-11-5 上午11:32:35
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener {
    private static final int GET_SAME_SUCCESS = 1;
    private static final int GET_IMAGE_SUCCESS = 2;
    private static final int INIT_IMAGE_ERROR = 3;
    private static final int GET_IMAGE_NODATA = 4;
    private boolean isToHome = true;
    private ImageView imageAD;// 启动图

    private Context mContext;
    private UmengInfo mUmengInfo;// umeng

    private String REQTYPE = "3";// 终端类型 3手机app
    private String TERID = "";// 终端id
    private String CHANNEL_ID = "";// umeng渠道号、
    HttpUtils httpUtils = new HttpUtils();
    HttpHandler httpHandler;
    private Location location;
    private LocationManager locationManager;
    public PackageManager mPackageManager;
    public static PackageInfo mPackageInfo;
    private String mStartimageUrl;

    public static final int TIMEOUT_INTERVEL = 1000;
    /**
     * 倒计时 默认3s , 间隔1s
     */
    private CountDown countDown;
    private TextView mTv_verycode;
    private LinearLayout mCount_down;
    private int mDuration;
    private SimpleTarget target;

    @SuppressWarnings("unused")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        imageAD = (ImageView) findViewById(R.id.imageAD);
        mContext = this;
        mPackageManager = mContext.getPackageManager();
        try {
            mPackageInfo = mPackageManager.getPackageInfo(mContext.getPackageName(), 0); // 得到应用程序的包信息对象
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            // 此异常不会发生
        }

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
            location = locationManager.getLastKnownLocation(provider);
        }

        // 获取经纬度end

        getUmeng();

        if (AppConstants.isGOKeyboard) {
            ImageLoader.load(mContext,R.mipmap.welcome_tw).into(imageAD);
//            imageAD.setBackgroundResource(R.mipmap.welcome_tw);
           /* if (MyApp.isLanguage.equals("rTW")) {
                imageAD.setBackgroundResource(R.mipmap.welcome_tw);
            } else {
                imageAD.setBackgroundResource(R.mipmap.welcome_en);
            }*/
        } else {//如果是国内版
            if (MyApp.isLanguage.equals("rCN")) {// 如果是中文简体
//                imageAD.setBackgroundResource(R.mipmap.welcome);
                ImageLoader.load(mContext,R.mipmap.welcome).into(imageAD);
            } else if (MyApp.isLanguage.equals("rTW")) {
//                imageAD.setBackgroundResource(R.mipmap.welcome_tw);
                ImageLoader.load(mContext,R.mipmap.welcome_tw).into(imageAD);
            }
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getStartImageUrl();
            }
        }, 1000);

        //如果是国际版

        //纯净版，默认不显示赔率
        if (AppConstants.fullORsimple && !PreferenceUtil.getBoolean(MyConstants.DEFUALT_SETTING, false)) {
            PreferenceUtil.commitBoolean(MyConstants.RBSECOND, false);
            PreferenceUtil.commitBoolean(MyConstants.rbSizeBall, false);
            PreferenceUtil.commitBoolean(MyConstants.RBOCOMPENSATE, false);
            PreferenceUtil.commitBoolean(MyConstants.RBNOTSHOW, true);
            PreferenceUtil.commitBoolean(MyConstants.DEFUALT_SETTING, true);
        }

    }

    private void initView() {

        mTv_verycode = (TextView) findViewById(R.id.tv_verycode);
        mTv_verycode.setOnClickListener(this);
        mCount_down = (LinearLayout) findViewById(R.id.count_down);
        mCount_down.setVisibility(View.GONE);
    }

    private void cancelCountDown() {
        if (countDown != null) {
            countDown.cancel();
        }
    }

    /*// 启动动画
    public void setHideTranslateAnimation() {
        if (!isToHome) {
            return;
        }
        isToHome = false;
        // 防止闪屏
        AlphaAnimation aa = new AlphaAnimation(1.0f, 1.0f);
        aa.setDuration(1000);
        aa.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                //判断更新
                //gotoHomeActivity();
                cancelCountDown();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                //动画加载完  判断是否更新
//                thread = new Thread(new CheckVersionTask());
//                thread.start();
               // gotoHomeActivity();

            }
        });
        imageAD.startAnimation(aa);
    }*/

    private void gotoHomeActivity() {
        //判断是否是第一次启动
        //如果是已经启动了 是“YES”
        //如果本地保存的versionname等于应用的版本号（例：1.0.4）
        if (PreferenceUtil.getString("isFirst", "").equals("YES") && PreferenceUtil.getString("versionName", "").equals(mPackageInfo.versionName)) {
            startActivity(new Intent(this, HomePagerActivity.class));
            this.finish();
        }

        //否则就是第一次启动
        else {
            startActivity(new Intent(this, WelcomeViewActivity.class));
            this.finish();
        }


    }

    /**
     * 获取youmeng渠道号，保存下来 "TERID.out"
     */
    private void getUmeng() {

        RequestParams params = new RequestParams();
        params.addBodyParameter("REQTYPE", REQTYPE);
        TERID = PreferenceUtil.getString(AppConstants.TERID, "");// 端口id
        System.out.println("WelcomeActivity:TERID: >" + TERID);
        params.addBodyParameter("TERID", TERID);
        params.addBodyParameter("IMEI", DeviceInfo.getDeviceId(mContext));
        params.addBodyParameter("IMSI", DeviceInfo.getSubscriberId(mContext));
        params.addBodyParameter("DN", DeviceInfo.getManufacturer());// 手机厂商
        params.addBodyParameter("DT", DeviceInfo.getModel());// 手机型号
        if (location != null) {// 如果获取到当前位置
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            params.addBodyParameter("LA", "" + lat);// 经度
            params.addBodyParameter("LO", "" + lng);// 纬度
        } else if (location == null) {// 如果当前位置没获取到
            params.addBodyParameter("LA", "");// 经度
            params.addBodyParameter("LO", "");// 纬度
        }

        params.addBodyParameter("OS", "android");// 终端手机操作系统
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

            if (appInfo.metaData != null) {
                CHANNEL_ID = appInfo.metaData.getString("UMENG_CHANNEL");
            }


        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        params.addBodyParameter("CHANNEL", CHANNEL_ID);// umeng渠道号
        params.addBodyParameter("APPVER", mPackageInfo.versionName);// 版本号
        try {
            httpUtils.send(HttpMethod.POST, BaseURLs.UMENG_CHANNEL_URL, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    try {
                        if (responseInfo != null) {
                            String text = responseInfo.result;
                            mUmengInfo = JSONObject.parseObject(text, UmengInfo.class);
                            System.out.println("WelcomeActivity:mUmengInfo.getTERID(): >" + mUmengInfo.getTERID());
                            PreferenceUtil.commitString(AppConstants.TERID, mUmengInfo.getTERID());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    // UiUtils.toast(mContext, "d>>"+msg);
                }
            });
        } catch (JSONException e) {
            // TODO: handle exception
//			L.e("fastjson异常:"+e);
        }
    }

    private void getStartImageUrl() {
        // 1、取得启动也url
        String serverUrl = BaseURLs.URL_STARTPIC;
        //String serverUrl = "http://192.168.31.48:8888/mlottery/core/mainPage.findAndroidStartupPic.do";
        // 2、连接服务器
        VolleyContentFast.requestJsonByGet(serverUrl, null, new DefaultRetryPolicy(2000, 1, 1), new VolleyContentFast.ResponseSuccessListener<WelcomeUrl>() {
            @Override
            public synchronized void onResponse(final WelcomeUrl json) {
                if ("200".equals(json.getResult() + "") && json != null) {

                    if (json.getUrl().isEmpty() || json.getUrl() == null) {
                        //没有图片 不显示
                        imageHandler.sendEmptyMessage(GET_IMAGE_NODATA);
                    } else {
                        if (PreferenceUtil.getString(MyConstants.START_IMAGE_URL, "").equals(json.getUrl())) {
                            mStartimageUrl = PreferenceUtil.getString(MyConstants.START_IMAGE_URL, "");
                            imageHandler.sendEmptyMessage(GET_IMAGE_SUCCESS);
                        } else {
                            mStartimageUrl = json.getUrl();
                            //网络请求图片成功
                            imageHandler.sendEmptyMessage(GET_IMAGE_SUCCESS);
                        }
                        mDuration = json.getDuration() * 1000 + 699;

                    }

                } else {
                    //如过json为空代表无图片显示
                    imageHandler.sendEmptyMessage(GET_IMAGE_NODATA);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                //请求失败
                mCount_down.setVisibility(View.GONE);
                imageHandler.sendEmptyMessage(INIT_IMAGE_ERROR);

            }
        }, WelcomeUrl.class);

//
    }

    private void toCountdown(int duration) {
        countDown = CountDown.getDefault(duration, TIMEOUT_INTERVEL, new CountDown.CountDownCallback() {
            @Override
            public void onFinish() {
                //setHideTranslateAnimation();
                cancelCountDown();
                gotoHomeActivity();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                if (mTv_verycode != null) {

                    L.d(TAG, millisUntilFinished / CountDown.TIMEOUT_INTERVEL + mContext.getResources().getString(R.string.skip));
                    mTv_verycode.setVisibility(View.VISIBLE);
                    mTv_verycode.setText(millisUntilFinished / CountDown.TIMEOUT_INTERVEL + mContext.getResources().getString(R.string.skip));
                }
            }
        });


    }

    private Handler imageHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case INIT_IMAGE_ERROR://网络请求失败
                    //setHideTranslateAnimation();
                    gotoHomeActivity();
                    break;
                case GET_IMAGE_SUCCESS://图片获取成功
                    //判断本地缓存是否存在该图片  是显示 不是替换
                    mCount_down.setVisibility(View.VISIBLE);


                    Glide.with(mContext)
                            .load(mStartimageUrl)
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
                                    toCountdown(mDuration);
                                }

                                @Override
                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                    super.onLoadFailed(e, errorDrawable);
                                    gotoHomeActivity();
                                }
                            });

                    PreferenceUtil.commitString(MyConstants.START_IMAGE_URL, mStartimageUrl);
                 /*   new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setHideTranslateAnimation();
                        }
                    }, 2000);*/
             /*       new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cancelCountDown();
                        }
                    }, 3000);
*/
                    break;
                case GET_IMAGE_NODATA://无图片显示
                    //setHideTranslateAnimation();
                    mCount_down.setVisibility(View.GONE);
                    gotoHomeActivity();
                    break;

                default:
                    break;
            }


        }
    };

    /**
     * 保存端口id到sd卡
     *
     * @param
     */
    /*public void fileSave(String treID) {
        // 保存在本地
        try {
            // 通过openFileOutput方法得到一个输出流，方法参数为创建的文件名（不能有斜杠），操作模式
            FileOutputStream fos = this.openFileOutput(".TERID.out", Context.MODE_WORLD_READABLE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(treID);// 写入
            fos.close(); // 关闭输出流
            // Toast.makeText(this, "保存umengID成功", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Toast.makeText(this, "出现异常1", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            // Toast.makeText(this, "出现异常2", Toast.LENGTH_LONG).show();
        }

        // 保存在sd卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目录
            File sdFile = new File(sdCardDir, ".TERID.out");
            try {
                FileOutputStream fos = new FileOutputStream(sdFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(treID);// 写入
                fos.close(); // 关闭输出流
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // Toast.makeText(this, "成功保存到sd卡", Toast.LENGTH_LONG).show();

        }
    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (httpHandler != null) {
            httpHandler.cancel();
        }
        // universalImageLoader.clearDiskCache();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_verycode:
                gotoHomeActivity();
                cancelCountDown();
                finish();
                break;

            default:
                break;
        }

    }

    /**
     * 从SD卡中取得保存的对象
     *
     * @return
     */
    /*public String readOAuth2() {
        String oAuth_1 = null;
        File sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目录
        File sdFile = new File(sdCardDir, ".TERID.out");
        try {
            FileInputStream fis = new FileInputStream(sdFile); // 获得输入流
            ObjectInputStream ois = new ObjectInputStream(fis);
            oAuth_1 = (String) ois.readObject();
            ois.close();
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OptionalDataException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return oAuth_1;
    }*/
}
