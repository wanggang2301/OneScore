package com.hhly.mlottery.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.UpdateInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.XRTextView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 关于我们
 * Created by hhly107 on 2016/4/6.
 */
public class HomeAboutActivity extends BaseActivity implements View.OnClickListener {
    private View view;
    private ImageView public_img_back;// 返回到菜单
    private Context mContext;
    private TextView public_txt_title;// 标题

    private TextView btn_version_update;
    private XRTextView tv_msg;//关于文字描述

    private UpdateInfo info;

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private TextView tvpercent;

    private TextView tv_currversion;

    private static final int GET_INFO_SUCCESS = 10; // 获取版本信息成功
    private static final int SERVER_ERROR = 11; // 服务器异常
    private static final int IO_ERROR = 13; // IO异常

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

    private boolean onclickFlage = true;

    private static final String COMP_VER = "1"; // 完整版
    private static final String PURE_VER = "2"; // 纯净版
    private TextView tv1, tv2;//订阅号//服务号
    private TextView mTv7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = HomeAboutActivity.this;
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.frage_about);
        tv_msg = (XRTextView) findViewById(R.id.tv_msg);
        if (AppConstants.fullORsimple) {
            tv_msg.setText(getResources().getString(R.string.about_product_msg_txt_simple));
        } else {
            tv_msg.setText(getResources().getString(R.string.about_product_msg_txt));
        }

        mTv7 = (TextView) findViewById(R.id.tv7);
        mTv7.setOnClickListener(this);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setOnClickListener(this);
      //  tv2 = (TextView) findViewById(R.id.tv2);
       // tv2.setOnClickListener(this);

        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(getResources().getString(R.string.about_us_txt));

        public_img_back = (ImageView) findViewById(R.id.public_img_back);
        public_img_back.setImageDrawable(getResources().getDrawable(R.mipmap.back));
        public_img_back.setOnClickListener(this);

        btn_version_update = (TextView) findViewById(R.id.btn_version_update);

        btn_version_update.setOnClickListener(this);

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);

        tv_currversion = (TextView) findViewById(R.id.tv_currversion);

        tv_currversion.setText(getVersionName());

    }

    private void initData() {

        new Thread(new CheckVersionTask()) {
        }.start();

    }

    @Override
    public void onClick(View v) {
        String string;
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.public_img_back:
                finish();
                MobclickAgent.onEvent(mContext,"AboutWe_Exit");
                break;

            case R.id.btn_version_update:
                MobclickAgent.onEvent(mContext,"AboutWe_UpdateVersion");
                if (onclickFlage) {
                    if (info != null) {
                        int serverVersion = Integer.parseInt(info.getVersion()); // 取得服务器上的版本号
                        int currentVersion = getVersionCode(); // 取得当前应用的版本号

                        if (currentVersion >= serverVersion) { // 判断两个版本号是否相同
                        /*
                         *
						 * 版本相同，直接进入主界面
						 */
                            onclickFlage = false;
                            noNewVersion();
                        } else {
                            onclickFlage = false;
                            checkNewVersion();
                        }
                    } else {
                        onclickFlage = false;
                        noNewVersion();
                    }

                }

                break;
            case R.id.tv1://订阅号
                MobclickAgent.onEvent(mContext,"AboutWe_Subscription");
                string = "live13322";
                UiUtils.copyTexts(string, mContext);
                UiUtils.toast(mContext, getResources().getString(R.string.about_copy_success) + '"' + string + '"');
                break;
           /* case R.id.tv2://服务号
                MobclickAgent.onEvent(mContext,"AboutWe_Server");
                string = "win13322";
                UiUtils.copyTexts(string, mContext);
                UiUtils.toast(mContext, getResources().getString(R.string.about_copy_success) + '"' + string + '"');
                break;*/
               case R.id.tv7://服务号
                   String number = mTv7.getText().toString();
                   //用intent启动拨打电话
                   Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));
                   startActivity(intent);
                   break;
            default:
                break;
        }

    }

    private class CheckVersionTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            msg = Message.obtain();
            // 1、取得服务器地址
            String serverUrl = BaseURLs.URL_VERSION_UPDATE; // 取得服务器地址

            Map<String, String> map = new HashMap<String, String>();
            if (AppConstants.fullORsimple) {
                map.put("versionType", PURE_VER);
            } else {
                map.put("versionType", COMP_VER);
            }

            VolleyContentFast.requestJsonByGet(serverUrl, map, new VolleyContentFast.ResponseSuccessListener<UpdateInfo>() {
                @Override
                public synchronized void onResponse(final UpdateInfo json) {
                    if (json != null) {
                        info = json;
                        L.i("init", "====initSucess===");

                    }
                }
            }, new VolleyContentFast.ResponseErrorListener() {
                @Override
                public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                    L.i("init", "====initFailed===");
                }
            }, UpdateInfo.class);


        }

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case IO_ERROR:

                    onclickFlage = false;
                    netConnFailed();
                    break;

                case GET_INFO_SUCCESS:

                    int serverVersion = Integer.parseInt(info.getVersion()); // 取得服务器上的版本号
                    int currentVersion = getVersionCode(); // 取得当前应用的版本号
                    if (currentVersion >= serverVersion) { // 判断两个版本号是否相同
                    /*
                     *
					 * 版本相同，直接进入主界面
					 */
                        onclickFlage = false;
                        noNewVersion();
                    } else {
                        onclickFlage = false;
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
                case DOWN_OVER:

                    installApk();
                    downLoadlDialog.cancel();
                    break;

                default:
                    break;
            }
        }

        ;
    };
    HttpUtils httpUtils = new HttpUtils();
    HttpHandler httpHandler;
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            saveFileName = savePath + "com.hhly.mlottery-" + info.getVersion() + "-" + new Date().getTime() + ".apk";

            httpHandler = httpUtils.download(info.getUrl(), saveFileName, true, true, new RequestCallBack<File>() {

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    Log.d(TAG,"___onLoading___");
                    progress = (int) (((float) current / total) * 100);
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Log.d(TAG,"___onLoading___");
                    mHandler.sendEmptyMessage(DOWN_OVER);
                }

                @Override
                public void onFailure(HttpException error, String msg) {

                }
            });



            /*try {

                URL url = new URL(info.getUrl());
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
                fos.write(count);//关闭输入流
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
     * 取得应用的版本号
     *
     * @return
     */
    public int getVersionCode() {

        PackageManager pm = mContext.getPackageManager(); // 取得包管理器的对象，这样就可以拿到应用程序的管理对象
        try {
            PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0); // 得到应用程序的包信息对象
            return info.versionCode; // 取得应用程序的版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // 此异常不会发生
            return -1;
        }
    }

    public String getVersionName() {

        PackageManager pm = mContext.getPackageManager(); // 取得包管理器的对象，这样就可以拿到应用程序的管理对象
        try {
            PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0); // 得到应用程序的包信息对象
            return info.versionName; // 取得应用程序的版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // 此异常不会发生
            return null;
        }
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

    private void noNewVersion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        final AlertDialog mAlertDialog = builder.create();
        //LayoutInflater infla = LayoutInflater.from(mContext);
        view = View.inflate(mContext, R.layout.about_no_new_version, null);
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mAlertDialog.dismiss();
                onclickFlage = true;
            }
        });

        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(view);
    }

    private void netConnFailed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        final AlertDialog mAlertDialog = builder.create();
        //LayoutInflater infla = LayoutInflater.from(mContext);
        view = View.inflate(mContext, R.layout.net_conn_failed, null);
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mAlertDialog.dismiss();
                onclickFlage = true;
            }
        });

        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(view);
    }

    private void checkNewVersion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);

        final AlertDialog mAlertDialog = builder.create();
        //LayoutInflater infla = LayoutInflater.from(mContext);
        view = View.inflate(mContext, R.layout.about_check_new_version, null);
        ((TextView) view.findViewById(R.id.dialog_message)).setText(info.getDescription().toString().replace("|", "\n"));
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mAlertDialog.dismiss();
                MobclickAgent.onEvent(mContext,"AboutWe_UpdateVersion_Click");
                interceptFlag = false;
                onclickFlage = true;
                uploadNewVersion();
            }
        });

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mAlertDialog.dismiss();
                onclickFlage = true;

            }
        });

        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(view);
    }

    private void uploadNewVersion() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        downLoadlDialog = builder.create();
        //LayoutInflater infla = LayoutInflater.from(mContext);
        view = View.inflate(mContext, R.layout.about_upload_new_version, null);

        tvpercent = (TextView) view.findViewById(R.id.dialog_updatemessage);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                downLoadlDialog.dismiss();
                interceptFlag = true;

                if (httpHandler != null) {
                    httpHandler.cancel();
                }
            }
        });
        downLoadlDialog.setCancelable(false);//下载进度条的dialog
        downLoadlDialog.show();
        downLoadlDialog.getWindow().setContentView(view);
        downloadApk();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (httpHandler != null) {
            httpHandler.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("HomeAboutActivity");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("HomeAboutActivity");
    }
}
