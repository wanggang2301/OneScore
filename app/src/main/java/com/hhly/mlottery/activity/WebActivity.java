package com.hhly.mlottery.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.callback.ShareCopyLinkCallBack;
import com.hhly.mlottery.callback.ShareTencentCallBack;
import com.hhly.mlottery.frame.ChatFragment;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ShareConstants;
import com.hhly.mlottery.widget.ProgressWebView;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;


/**
 * WebView显示H5
 * 107
 */
public class WebActivity extends BaseActivity implements OnClickListener {

    private ProgressWebView mWebView;
    private TextView mTv_check_info;// 关联跳转按钮
    private ImageView mPublic_img_back;// 返回
    private TextView mPublic_txt_title;// 标题
    private String url;// 要显示的H5网址
    private String token;// 登录参数
    private String reqMethod;// 是否传参数
    private String imageurl;// 图片地址
    private String title;// 标题
    private String subtitle;// 副标题
    private int mType;// 关联赛事类型
    private String mThird;// 关联赛事ID
    private String infoTypeName;// 资讯类型名
    private static final String INTENT_PARAMS_TITLE = "title";
    private ImageView public_btn_set;
    private ShareTencentCallBack mShareTencentCallBack;
    private ShareCopyLinkCallBack mShareCopyLinkCallBack;
    private Tencent mTencent;
    private SharePopupWindow sharePopupWindow;
    private float y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        // 跳转关联赛事详情
        if (mType != 0 && !TextUtils.isEmpty(mThird)) {
            mTv_check_info.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mType) {
                        case 1:// 篮球
                        {
                            Intent intent = new Intent(mContext, BasketDetailsActivity.class);
                            intent.putExtra("thirdId", mThird);
                            mContext.startActivity(intent);
                            break;
                        }
                        case 2:// 足球
                        {
                            Intent intent = new Intent(mContext, FootballMatchDetailActivity.class);
                            intent.putExtra("thirdId", mThird);
                            intent.putExtra("currentFragmentId", -1);
                            mContext.startActivity(intent);
                            break;
                        }
                    }
//                    startActivity(new Intent(WebActivity.this,InputActivity.class));
                }
            });
        }
    }


    private void initView() {
        ImageView public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);
        public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setImageResource(R.mipmap.share);
        public_btn_set.setVisibility(View.VISIBLE);

        public_btn_set.setOnClickListener(this);

        mPublic_img_back = (ImageView) findViewById(R.id.public_img_back);
        mPublic_txt_title = (TextView) findViewById(R.id.public_txt_title);
        mWebView = (ProgressWebView) findViewById(R.id.baseweb_webview);
        mTv_check_info = (TextView) findViewById(R.id.tv_check_info);
//        startyY = mTv_check_info.getY();
        mTv_check_info.setVisibility(View.GONE);
        mPublic_img_back.setOnClickListener(this);
        mWebView.setOnCustomScroolChangeListener(new ProgressWebView.ScrollInterface() {
            @Override
            public void onSChanged(int l, int t, int oldl, int oldt) {
                y = mWebView.getContentHeight() * mWebView.getScale() - (mWebView.getHeight() + mWebView.getScrollY());
                if (y < 3) {

                    //已经处于底端
//                    mTv_check_info.setVisibility(View.VISIBLE);
                    if (mType != 0 && !TextUtils.isEmpty(mThird)) {
                        mTv_check_info.setVisibility(View.VISIBLE);
                    } else {
                        mTv_check_info.setVisibility(View.GONE);
                    }
                } else {
                    if (mTv_check_info.getVisibility() == View.VISIBLE) {//给一个容差  避免临界点闪烁
                        if (y > 150) {
                            mTv_check_info.setVisibility(View.GONE);
//                            d();
                        } else {
                            mTv_check_info.setVisibility(View.VISIBLE);
                        }
                    }


                }
//                initAnimosion(t - oldt);
            }
        });
        WebSettings webSettings = mWebView.getSettings();
        // 不用缓存
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false);
        String us = webSettings.getUserAgentString();
        webSettings.setUserAgentString(us.replace("Android", "yibifen Android"));//给useagent加个标识yibifen
    }

    protected void initData() {
        try {
            Intent intent = getIntent();
            url = intent.getStringExtra("key");
            L.d("CommonUtils初始url" + url);
//            url = "http://192.168.33.14:8080/gameweb/h5/index";
//            url = "http://192.168.37.6:8080/gameweb/h5/index";
            imageurl = intent.getStringExtra("imageurl");
            title = intent.getStringExtra(INTENT_PARAMS_TITLE);
            subtitle = intent.getStringExtra("subtitle");//轮播图没有副标题，所以为null  请知悉
            mType = intent.getIntExtra("type", 0);
            mThird = intent.getStringExtra("thirdId");
            infoTypeName = intent.getStringExtra("infoTypeName");
            token = AppConstants.register.getData().getLoginToken();
            String deviceId = AppConstants.deviceToken;
            reqMethod = intent.getStringExtra("reqMethod");
            mPublic_txt_title.setText(infoTypeName);
//            if (!TextUtils.isEmpty(token) && reqMethod != null && reqMethod.equals("post")) {//不是新闻资讯的时候隐藏分享和评论
//                public_btn_set.setVisibility(View.GONE);
//            } else {//token为空，说明是资讯，显示分享和评论
//                public_btn_set.setVisibility(View.VISIBLE);
//                //添加评论功能  评论功能已单独封装成一个模块  调用的时候  只要以下代码就行
//                ChatFragment chatFragment = new ChatFragment();
//                CyUtils.addComment(chatFragment, url, title, false, false, getSupportFragmentManager(), R.id.comment);
//            }
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    // TODO Auto-generated method stub
//
//                    super.onPageFinished(view, url);
//                    L.i("lzftype=" + mType + "thirtid=" + mThird);
//                    if (mType != 0 && !TextUtils.isEmpty(mThird)) {
//                        mTv_check_info.setVisibility(View.VISIBLE);
//                    } else {
//                        mTv_check_info.setVisibility(View.GONE);
//                    }
//                }
            });
//            //其他页传过来的reqMethod为post时，提交token  否则不提交
//            if (reqMethod != null && token != null && reqMethod.equals("post")) {
//
////                mWebView.postUrl(url, token.getBytes("utf-8"));
////                url = url + "?loginToken=" + token + "&deviceToken=" + deviceId;
//                url = url.replace("{loginToken}", token);
//                url = url.replace("{deviceToken}", deviceId);
//            }
            if (url != null) {
                //添加评论功能  评论功能已单独封装成一个模块  调用的时候  只要以下代码就行
                ChatFragment chatFragment = new ChatFragment();
                CyUtils.addComment(chatFragment, url, title, false, false, getSupportFragmentManager(), R.id.comment);
                if (url.contains("comment=false")) {
                    getSupportFragmentManager().beginTransaction().remove(chatFragment).commit();//移除评论
                }
                if (url.contains("share=false")) {
                    public_btn_set.setVisibility(View.GONE);//隐藏分享
                }
                url = url.replace("{loginToken}", token);
                url = url.replace("{deviceToken}", deviceId);
            }
            mWebView.loadUrl(url);
            L.d("lzf:" + "imageurl=" + imageurl + "title" + title + "subtitle" + subtitle);
            Log.d("CommonUtils:", "token=" + token + "reqMethod" + reqMethod + "url=" + url);

            mShareTencentCallBack = new ShareTencentCallBack() {
                @Override
                public void onClick(int flag) {
                    shareQQ(flag);
                }
            };

            mShareCopyLinkCallBack = new ShareCopyLinkCallBack() {
                @Override
                public void onClick() {
                    ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(url);
                }
            };

            L.d("lzf:" + "imageurl=" + imageurl + "title" + title + "subtitle" + subtitle);
        } catch (Exception e) {
            L.d("initData初始化失败:" + e.getMessage());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("WebActivity");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("WebActivity");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back://返回
                if (mWebView.canGoBack()) {
                    mWebView.goBack();// 返回上一页面

                } else {
                    MobclickAgent.onEvent(mContext, "Football_DataInfo_Exit");
                    finish();
                }
                break;
            case R.id.public_btn_set: //分享
                //  @style/AppTheme.BlackStatusBar.ColorGreen
                String summary = "";
                if (subtitle == null || "".equals(subtitle)) {
                    summary = getString(R.string.share_summary_default);
                } else {
                    summary = subtitle;
                }

                MobclickAgent.onEvent(mContext, "Football_DataInfo_Share");
                Map<String, String> map = new HashMap<String, String>();
                map.put(ShareConstants.TITLE, title != null ? title : mContext.getResources().getString(R.string.share_recommend));
                map.put(ShareConstants.SUMMARY, summary);
                map.put(ShareConstants.TARGET_URL, url != null ? url : "http://m.13322.com");
                map.put(ShareConstants.IMAGE_URL, imageurl != null ? imageurl : "");
                sharePopupWindow = new SharePopupWindow(this, public_btn_set, map);
                sharePopupWindow.setmShareTencentCallBack(mShareTencentCallBack);
                sharePopupWindow.setmShareCopyLinkCallBack(mShareCopyLinkCallBack);
                sharePopupWindow.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });

                backgroundAlpha(0.5f);
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    private void shareQQ(int falg) {
        mTencent = Tencent.createInstance(ShareConstants.QQ_APP_ID, this);
        final Bundle bundle = new Bundle();
        //这条分享消息被好友点击后的跳转URL。

        String appname = getString(R.string.share_to_qq_app_name);

        String summary = "";
        if (subtitle == null || "".equals(subtitle)) {
            summary = getString(R.string.share_summary_default);
        } else {
            summary = subtitle;
        }

        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title != null ? title : "");
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url != null ? url : "");
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageurl != null ? imageurl : "");
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, appname);
        bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, falg);
        mTencent.shareToQQ(WebActivity.this, bundle, qqShareListener);
    }

    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
        }

        @Override
        public void onComplete(Object response) {
        }

        @Override
        public void onError(UiError e) {
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();// 返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}