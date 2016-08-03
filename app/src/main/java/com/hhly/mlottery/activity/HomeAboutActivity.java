package com.hhly.mlottery.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.UpdateInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.XRTextView;
import com.umeng.analytics.MobclickAgent;

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
    private UpdateInfo mUpdateInfo;// 新版本对象
    private TextView tv_currversion;

    private static final int GET_INFO_START = 0; // 开始请求
    private static final int GET_INFO_SUCCESS = 1; // 获取版本信息成功
    private static final int GET_INFO_ERROR = 2; // 网络访问失败
    private static final String COMP_VER = "1"; // 完整版
    private static final String PURE_VER = "2"; // 纯净版
    private TextView tv1, tv2;//订阅号//服务号
    private TextView mTv7;

    private String versionCode;// 当前版本code
    private String version;// 当前版本name
    private RelativeLayout about_detail_progressbar;// 网络请求进度条

    private boolean isCheckVersion = true;// 是否正在请求版本更新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = HomeAboutActivity.this;
        getVersion();
        initView();
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
        tv_currversion.setText(version);// 设置当前版本

        about_detail_progressbar = (RelativeLayout) findViewById(R.id.about_detail_progressbar);
    }

    /**
     * 获取当前版本号
     */
    private void getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            versionCode = String.valueOf(info.versionCode);
            version = info.versionName;
        } catch (Exception e) {
            L.d(e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        String string;
        switch (v.getId()) {
            case R.id.public_img_back:
                finish();
                MobclickAgent.onEvent(mContext, "AboutWe_Exit");
                break;
            case R.id.btn_version_update:
                MobclickAgent.onEvent(mContext, "AboutWe_UpdateVersion");
                if (isCheckVersion) {
                    checkVersionUp();
                }
                break;
            case R.id.tv1://订阅号
                MobclickAgent.onEvent(mContext, "AboutWe_Subscription");
                string = "live13322";
                UiUtils.copyTexts(string, mContext);
                UiUtils.toast(mContext, getResources().getString(R.string.about_copy_success) + '"' + string + '"');
                break;
            case R.id.tv7://服务号
                String number = mTv7.getText().toString();
                Uri uri = Uri.parse("tel:" + number);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_INFO_START:// 开始请求
                    about_detail_progressbar.setVisibility(View.VISIBLE);
                    isCheckVersion = false;
                    break;
                case GET_INFO_SUCCESS:// 访问成功
                    about_detail_progressbar.setVisibility(View.GONE);
                    isCheckVersion = true;
                    int serverVersion = Integer.parseInt(mUpdateInfo.getVersion()); // 取得服务器上的版本code
                    int currentVersion = Integer.parseInt(versionCode);// 获取当前版本code
                    if (currentVersion < serverVersion) {// 有更新
                        promptVersionUp();
                    } else {
                        versionUpError(true);
                    }
                    break;
                case GET_INFO_ERROR:// 访问失败
                    isCheckVersion = true;
                    about_detail_progressbar.setVisibility(View.GONE);
                    versionUpError(false);
                    break;
            }
        }
    };

    /**
     * 新版本更新提示
     */
    private void promptVersionUp() {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext, R.style.AppThemeDialog);//  android.R.style.Theme_Material_Light_Dialog
            builder.setCancelable(false);// 设置对话框以外不可点击
            builder.setTitle(mContext.getResources().getString(R.string.about_soft_update));// 提示标题
            if (mUpdateInfo != null) {
                String mMessage = mUpdateInfo.getDescription();// 获取提示内容
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
                    MobclickAgent.onEvent(mContext, "AboutWe_UpdateVersion_Click");
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.version_update_title), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(mContext, "更新", Toast.LENGTH_SHORT).show();
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
                    String mAppName = mUpdateInfo.getUrl().substring(mUpdateInfo.getUrl().lastIndexOf("/"),mUpdateInfo.getUrl().length());
                    L.d("xxx","mAppName:>>" + mAppName);
                    request.setDestinationInExternalPublicDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), mAppName);
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
                }
            });
            android.support.v7.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查版本更新
     */
    private void checkVersionUp() {
        mHandler.sendEmptyMessage(GET_INFO_START);// 开始请求
        // 参数设置
        Map<String, String> map = new HashMap<>();
        if (AppConstants.fullORsimple) {
            map.put("versionType", PURE_VER);
        } else {
            map.put("versionType", COMP_VER);
        }
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_VERSION_UPDATE, map, new VolleyContentFast.ResponseSuccessListener<UpdateInfo>() {
            @Override
            public synchronized void onResponse(final UpdateInfo json) {
                if (json != null) {
                    mUpdateInfo = json;
                    mHandler.sendEmptyMessage(GET_INFO_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(GET_INFO_ERROR);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(GET_INFO_ERROR);
            }
        }, UpdateInfo.class);
    }


    /**
     * 无法更新提示
     */
    private void versionUpError(boolean isRn) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
            final AlertDialog mAlertDialog = builder.create();
            view = View.inflate(mContext, R.layout.about_no_new_version, null);
            TextView dialog_message = (TextView) view.findViewById(R.id.dialog_message);
            if (isRn) {
                dialog_message.setText(mContext.getResources().getString(R.string.about_notice_msg));
            } else {
                dialog_message.setText(mContext.getResources().getString(R.string.about_net_failed));
            }
            view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mAlertDialog.dismiss();
                }
            });
            mAlertDialog.setCancelable(false);
            mAlertDialog.show();
            mAlertDialog.getWindow().setContentView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
//        MobclickAgent.onPageStart("HomeAboutActivity");
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
//        MobclickAgent.onPageEnd("HomeAboutActivity");
    }
}
