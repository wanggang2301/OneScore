package com.hhly.mlottery.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.UmengInfo;
import com.hhly.mlottery.bean.UpdateInfo;
import com.hhly.mlottery.bean.WelcomeUrl;
import com.hhly.mlottery.bean.basket.BasketRoot;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.FileUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.core.BitmapCache;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Tenney
 * @ClassName: WelcomeActivity
 * @Description: 启动界面
 * @date 2015-11-5 上午11:32:35
 */
public class WelcomeActivity extends BaseActivity {
    private static final int GET_SAME_SUCCESS = 1;
    private static final int GET_IMAGE_SUCCESS = 2;
    private static final int INIT_IMAGE_ERROR = 3;
    private static final int GET_IMAGE_NODATA = 4;
    private boolean isToHome = true;
    private ImageView imageAD;// 启动图

    private Context mContext;
    private View view;
    private UpdateInfo info;
    private UmengInfo mUmengInfo;// umeng

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private TextView tvpercent;

    private TextView tv_currversion;

    private static final int GET_INFO_SUCCESS = 10; // 获取版本信息成功
    private static final int IO_ERROR = 13; // IO异常
    private static final int INFO_IS_NULL = 15; // 超时

    private boolean interceptFlag = false;

    private Thread downLoadThread;

    // 安装状态
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;

    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/download/";

    private static String saveFileName = savePath + "com.hhly.mlottery.apk";

    private int progress;

    private Message msg;

    private AlertDialog downLoadlDialog;

    private static final String COMP_VER = "1"; // 完整版
    private static final String PURE_VER = "2"; // 纯净版

    private Thread thread;

    private boolean IsInitInfo = false;
    private Timer timer;

    private String REQTYPE = "3";// 终端类型 3手机app
    private String TERID = "";// 终端id
    private String CHANNEL_ID = "";// umeng渠道号、
    HttpUtils httpUtils = new HttpUtils();
    HttpHandler httpHandler;
    //	private RequestQueue mRequestQueue;
    private Location location;
    //	private LocationManager locationManager;
    public PackageManager mPackageManager;
    public static PackageInfo mPackageInfo;
    private String mStartimageUrl;

    private com.nostra13.universalimageloader.core.ImageLoader universalImageLoader;
    private DisplayImageOptions options;


    @SuppressWarnings("unused")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        imageAD = (ImageView) findViewById(R.id.imageAD);
        mContext = this;
        mPackageManager = mContext.getPackageManager();
        try {
            mPackageInfo = mPackageManager.getPackageInfo(mContext.getPackageName(), 0); // 得到应用程序的包信息对象
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            // 此异常不会发生
        }
        ;

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .resetViewBeforeLoading(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(options)
                .build();
        universalImageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance(); //初始化
        universalImageLoader.init(config);
/*		 获取经纬度
//		String serviceName = Context.LOCATION_SERVICE;
//		locationManager = (LocationManager) getSystemService(serviceName);
//		Criteria criteria = new Criteria();
//		criteria.setAccuracy(Criteria.ACCURACY_FINE);
//		criteria.setAltitudeRequired(false);
//		criteria.setBearingRequired(false);
//		criteria.setCostAllowed(true);
//		criteria.setPowerRequirement(Criteria.POWER_LOW);
//		String provider = locationManager.getBestProvider(criteria, true);
//		location = locationManager.getLastKnownLocation(provider);
		获取经纬度end */

        getUmeng();
        msg = Message.obtain();

       /* thread = new Thread(new CheckVersionTask());
        thread.start();*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getStartImageUrl();
            }
        }, 1000);
        //启动页加载图片url
        // getStartImageUrl();
      /*  final long start = System.currentTimeMillis(); // 记录起始时间

        timer = new Timer();

        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                long end = System.currentTimeMillis();
                //Log.i("time2", "" + (end - start));

                if ((end - start) < 3000) {
                    //Log.i("time2", "检查是否初始化info：" + IsInitInfo);

                    if (IsInitInfo) {
                        timer.cancel();
                        handler.sendMessage(msg);
                    }

                } else {
                    //Log.i("time2", "失败");
                    timer.cancel();
                    msg.what = TIMEOUT_ERROR;
                    handler.sendMessage(msg);
                }
            }
        };

        timer.schedule(timerTask, 1000, 1000);*/
        //如果是国际版
        if (AppConstants.isGOKeyboard) {
            if (MyApp.isLanguage.equals("rTW")) {
                imageAD.setBackgroundResource(R.mipmap.welcome_tw);
            } else {
                imageAD.setBackgroundResource(R.mipmap.welcome_en);
            }
        } else {//如果是国内版
            if (MyApp.isLanguage.equals("rCN")) {// 如果是中文简体

                imageAD.setBackgroundResource(R.mipmap.welcome);
            } else if (MyApp.isLanguage.equals("rTW")) {
                imageAD.setBackgroundResource(R.mipmap.welcome_tw);
            }
        }

        //纯净版，默认不显示赔率
        if (AppConstants.fullORsimple && !PreferenceUtil.getBoolean(MyConstants.DEFUALT_SETTING, false)) {
            PreferenceUtil.commitBoolean(MyConstants.RBSECOND, false);
            PreferenceUtil.commitBoolean(MyConstants.rbSizeBall, false);
            PreferenceUtil.commitBoolean(MyConstants.RBOCOMPENSATE, false);
            PreferenceUtil.commitBoolean(MyConstants.RBNOTSHOW, true);
            PreferenceUtil.commitBoolean(MyConstants.DEFUALT_SETTING, true);
        }

    }

    /* //设置启动页背景
    private void  setBackgroundImage(){


    }*/
    // 启动动画
    public void setHideTranslateAnimation() {

        if (!isToHome) {
            return;
        }
        isToHome = false;

        // 防止闪屏
        AlphaAnimation aa = new AlphaAnimation(1.0f, 1.0f);
        aa.setDuration(2000);
        aa.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                //判断更新
                //gotoHomeActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                thread = new Thread(new CheckVersionTask());
                thread.start();

            }
        });
        imageAD.startAnimation(aa);
    }

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


        if (readOAuth2() == null) {

            params.addBodyParameter("TERID", TERID);
        } else if (readOAuth2() != null) {
            params.addBodyParameter("TERID", readOAuth2());

        }
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
                    if (responseInfo != null) {
                        String text = responseInfo.result;
//                        System.out.println(">>>>text"+text);

                        try {
                            mUmengInfo = JSONObject.parseObject(text, UmengInfo.class);
                        } catch (Exception e) {

                        }
                        if (readOAuth2() == null) {// 如果终端id为空
                            fileSave(mUmengInfo.getTERID());
                        }
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

    private class CheckVersionTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            checkVersionUpdate();

        }
    }

    private void getStartImageUrl() {

        // 1、取得启动也url
        String serverUrl = BaseURLs.URL_STARTPIC;
        //String serverUrl = "http://192.168.31.48:8888/mlottery/core/mainPage.findAndroidStartupPic.do";
        // 2、连接服务器
        VolleyContentFast.requestJsonByGet(serverUrl, null, new DefaultRetryPolicy(3000, 1, 1), new VolleyContentFast.ResponseSuccessListener<WelcomeUrl>() {
            @Override
            public synchronized void onResponse(final WelcomeUrl json) {
                if ("200".equals(json.getResult() + "") && json != null) {

                    if (json.getUrl().isEmpty()) {
                        //没有图片 不显示
                        imageHandler.sendEmptyMessage(GET_IMAGE_NODATA);
                        return;
                    } else {
                        if (PreferenceUtil.getString(MyConstants.START_IMAGE_URL, "").equals(json.getUrl()) && !PreferenceUtil.getString(MyConstants.START_IMAGE_URL, "").isEmpty()) {
                            mStartimageUrl = PreferenceUtil.getString(MyConstants.START_IMAGE_URL, "");
                            imageHandler.sendEmptyMessage(GET_IMAGE_SUCCESS);
                            return;
                        } else {
                            mStartimageUrl = json.getUrl();
                            //保存启动页图片url
                            //网络请求图片成功
                            imageHandler.sendEmptyMessage(GET_IMAGE_SUCCESS);
                        }
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
                imageHandler.sendEmptyMessage(INIT_IMAGE_ERROR);

            }
        }, WelcomeUrl.class);

//
    }

    private void checkVersionUpdate() {

        // 1、取得服务器地址

        String serverUrl = BaseURLs.URL_VERSION_UPDATE; // 取得服务器地址
        Map<String, String> map = new HashMap<String, String>();

        if (AppConstants.fullORsimple) {
            map.put("versionType", PURE_VER);
        } else {
            map.put("versionType", COMP_VER);
        }

        // 2、连接服务器
        VolleyContentFast.requestJsonByGet(serverUrl, map, new DefaultRetryPolicy(3000, 1, 1), new VolleyContentFast.ResponseSuccessListener<UpdateInfo>() {
            @Override
            public synchronized void onResponse(final UpdateInfo json) {
                if (json != null) {
                    info = json;
                    //  msg.what = GET_INFO_SUCCESS;
                    IsInitInfo = true;
                    handler.sendEmptyMessage(GET_INFO_SUCCESS);
                    saveFileName = savePath + "com.hhly.mlottery-" + info.getVersion() + "-" + new Date().getTime() + ".apk";
                } else {
                    msg.what = INFO_IS_NULL;
                    handler.sendEmptyMessage(INFO_IS_NULL);
                    IsInitInfo = true;
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                msg.what = IO_ERROR;
                handler.sendEmptyMessage(IO_ERROR);
                IsInitInfo = true;
            }
        }, UpdateInfo.class);


    }

    private Handler imageHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case INIT_IMAGE_ERROR://网络请求失败
                    setHideTranslateAnimation();
                    break;
                case GET_IMAGE_SUCCESS://图片获取成功
                    //判断本地缓存是否存在该图片  是显示 不是替换
                    universalImageLoader.displayImage(mStartimageUrl, imageAD, options);
                    PreferenceUtil.commitString(MyConstants.START_IMAGE_URL, mStartimageUrl);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setHideTranslateAnimation();
                        }
                    }, 2000);

                    /*universalImageLoader.displayImage(mStartimageUrl, imageAD, options);
                    setHideTranslateAnimation();*/

                    break;
                case GET_IMAGE_NODATA://无图片显示
                    setHideTranslateAnimation();

                    break;

                default:
                    break;
            }


        }
    };
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            //Log.i("time2", "==" + "handleMessage" + "--" + msg.what);
            switch (msg.what) {

                case IO_ERROR:
                    gotoHomeActivity();
                    break;
                case INFO_IS_NULL:
                    gotoHomeActivity();
                   break;

                case GET_INFO_SUCCESS:
                    int serverVersion = Integer.parseInt(info.getVersion()); // 取得服务器上的版本号
                    int currentVersion = mPackageInfo.versionCode; // 取得当前应用的版本号
                    if (currentVersion >= serverVersion) { // 判断两个版本号是否相同
                        //Log.i("time2", "==" + "版本相同");
                        //setHideTranslateAnimation();
                        gotoHomeActivity();
                    } else {
                        //Log.i("time2", "==" + "有更新");
                        checkNewVersion();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    tvpercent.setText(String.valueOf(progress) + "%");
                    mProgress.setProgress(progress);

                    break;
                case DOWN_OVER://下载完通知
                    gotoHomeActivity();
                    installApk();
                    downLoadlDialog.cancel();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {

            httpHandler = httpUtils.download(info.getUrl(), saveFileName, true, true, new RequestCallBack<File>() {


                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    progress = (int) (((float) current / total) * 100);
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    mHandler.sendEmptyMessage(DOWN_OVER);
                }

                @Override
                public void onFailure(HttpException error, String msg) {

                }
            });



           /* try {
//                URL url = new URL(info.getUrl());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();

                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }

                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);

                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {

                    int numread = is.read(buf);
                    count += numread;

                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);

                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    //
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.
                fos.write(count);//关闭输入流数据
                fos.close();
                is.close();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

        }
    };

    private void downloadApk() {

        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }


    /**
     * 安装apk
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        startActivity(i);

    }

    private void checkNewVersion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        final AlertDialog mAlertDialog = builder.create();
        LayoutInflater infla = LayoutInflater.from(this);
        view = infla.inflate(R.layout.about_check_new_version, null);
        ((TextView) view.findViewById(R.id.dialog_message)).setText(info.getDescription().toString().replace("|", "\n"));
        view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mAlertDialog.dismiss();
                interceptFlag = false;
                L.d("xxx", "点击更新");
                uploadNewVersion();
            }
        });

        view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mAlertDialog.dismiss();
                gotoHomeActivity();

                if (httpHandler != null) {
                    httpHandler.cancel();
                }

            }
        });

        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(view);
    }

    private void uploadNewVersion() {
        L.d("xxx", "准备更新");
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        downLoadlDialog = builder.create();
        LayoutInflater infla = LayoutInflater.from(this);
        view = infla.inflate(R.layout.about_upload_new_version, null);
        tvpercent = (TextView) view.findViewById(R.id.dialog_updatemessage);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);

        view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                downLoadlDialog.dismiss();
                interceptFlag = true;
                gotoHomeActivity();
            }
        });
        downLoadlDialog.setCancelable(false);//进度条的dialog
        downLoadlDialog.show();
        downLoadlDialog.getWindow().setContentView(view);
        downloadApk();
    }

    /**
     * 保存端口id到sd卡
     *
     * @param treID
     */
    public void fileSave(String treID) {
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (httpHandler != null) {
            httpHandler.cancel();
        }
        // universalImageLoader.clearDiskCache();

    }

    /**
     * 从SD卡中取得保存的对象
     *
     * @return
     */
    public String readOAuth2() {
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
    }
}
