package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.ShareBean;
import com.hhly.mlottery.frame.ShareFragment;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.MvpChargeMoneyActivity;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.HandMatchId;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.ProgressWebView;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by：XQyi on 2017/6/14 16:08
 * Use: 德州H5 页面WebView（view）
 */
public class TexasWebActivity extends BaseActivity implements OnClickListener {

    private ProgressWebView mWebView;
    private RelativeLayout mRelativeLayout;
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

    private float y;

    private ShareFragment mShareFragment;

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
        if (mType != 0 && !TextUtils.isEmpty(mThird) && !"0".equals(mThird)) {
            mTv_check_info.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mType) {
                        case 1:// 篮球
                        {
                            Intent intent = new Intent(mContext, BasketDetailsActivityTest.class);
                            intent.putExtra("thirdId", mThird);
                            mContext.startActivity(intent);
                            break;
                        }
                        case 2:// 足球
                        {
                            if (HandMatchId.handId(mContext, mThird)) {

                                Intent intent = new Intent(mContext, FootballMatchDetailActivity.class);
                                intent.putExtra("thirdId", mThird);
                                intent.putExtra("currentFragmentId", -1);
                                mContext.startActivity(intent);
                            }
                            break;
                        }
                    }
                }
            });
        }
    }


    private void initView() {
        findViewById(R.id.comment).setVisibility(View.GONE);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.header_layout);
        linearLayout.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        getResources().getDimensionPixelOffset(R.dimen.header_height_short)));
        linearLayout.setPadding(0, 0, 0, 0);
        ImageView public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);
        public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setVisibility(View.GONE);
//        public_btn_set.setImageResource(R.mipmap.share);
//
//        if (DeviceInfo.isZH()) {
//            public_btn_set.setVisibility(View.VISIBLE);
//        } else {
//            public_btn_set.setVisibility(View.GONE);
//        }
//
//        public_btn_set.setOnClickListener(this);

        mPublic_img_back = (ImageView) findViewById(R.id.public_img_back);
        mPublic_txt_title = (TextView) findViewById(R.id.public_txt_title);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.webview_container);
        mWebView = new ProgressWebView(this, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(layoutParams);
        mRelativeLayout.addView(mWebView);
        mTv_check_info = (TextView) findViewById(R.id.tv_check_info);
        mTv_check_info.setVisibility(View.GONE);
        mPublic_img_back.setOnClickListener(this);
        mWebView.setOnCustomScroolChangeListener(new ProgressWebView.ScrollInterface() {
            @Override
            public void onSChanged(int l, int t, int oldl, int oldt) {
                y = mWebView.getContentHeight() * mWebView.getScale() - (mWebView.getHeight() + mWebView.getScrollY());
                if (y < 3) {
                    if (mType != 0 && !TextUtils.isEmpty(mThird) && !"0".equals(mThird)) {
                        mTv_check_info.setVisibility(View.VISIBLE);
                    } else {
                        mTv_check_info.setVisibility(View.GONE);
                    }
                } else {
                    if (mTv_check_info.getVisibility() == View.VISIBLE) {//给一个容差  避免临界点闪烁
                        if (y > 150) {
                            mTv_check_info.setVisibility(View.GONE);
                        } else {
                            mTv_check_info.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        mWebView.addJavascriptInterface(new YBFJavascriptHandler(), "YBF");

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
            imageurl = intent.getStringExtra("imageurl");
            title = intent.getStringExtra(INTENT_PARAMS_TITLE);
            subtitle = intent.getStringExtra("subtitle");//轮播图没有副标题，所以为null  请知悉
            mType = intent.getIntExtra("type", 0);
            mThird = intent.getStringExtra("thirdId");
            infoTypeName = intent.getStringExtra("infoTypeName");
            token = AppConstants.register.getToken();
            String deviceId = AppConstants.deviceToken;
            reqMethod = intent.getStringExtra("reqMethod");
            mPublic_txt_title.setText(infoTypeName);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

//                    if (url.equals("http://texas.1332255.com:4007/VideoGameWeb/mobile/zhubo_roomType-2.html")) {
//                        L.d(TAG, "shouldOverrideUrlLoading: " + url);
//                        startActivity(new Intent(TexasWebActivity.this,MvpChargeMoneyActivity.class));
//                        return true;
//                    }else{
//
//                        mWebView.loadUrl(url);
//                        L.d("lzfshiping", url);
//                        return false;
//                    }
                    view.loadUrl(url);
                    L.d("lzfshiping", url);
                    return true;
                }

            });
            if (url != null) {
//                if (url.contains("share=false")) {
//                    public_btn_set.setVisibility(View.GONE);//隐藏分享
//                }
                url = url.replace("{loginToken}", token);
                url = url.replace("{deviceToken}", deviceId);
            }

            mWebView.loadUrl(url);
            L.d("lzf:" + "imageurl=" + imageurl + "title" + title + "subtitle" + subtitle);

        } catch (Exception e) {
            L.d("initData初始化失败:" + e.getMessage());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
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
//            case R.id.public_btn_set: //分享
//                //  @style/AppTheme.BlackStatusBar.ColorGreen
//
//
//                MobclickAgent.onEvent(mContext, "Football_DataInfo_Share");
//
//                ShareBean shareBean = new ShareBean();
//
//                String summary = "";
//                if (subtitle == null || "".equals(subtitle)) {
//                    summary = getString(R.string.share_summary_default);
//                } else {
//                    summary = subtitle;
//                }
//                shareBean.setTitle(title != null ? title : mContext.getResources().getString(R.string.share_recommend));
//                shareBean.setSummary(summary);
//                shareBean.setTarget_url(url != null ? url : "http://m.13322.com");
//                shareBean.setImage_url(imageurl != null ? imageurl : "");
//                shareBean.setCopy(url);
//
//                mShareFragment = ShareFragment.newInstance(shareBean);
//                mShareFragment.show(getSupportFragmentManager(), "bottomShare");
        }
    }


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

    public class YBFJavascriptHandler {
        /**
         * 跳充值
         */
        @JavascriptInterface
        public void toChargeMoneyAct(){
            startActivity(new Intent(TexasWebActivity.this,MvpChargeMoneyActivity.class));
        }
        /**
         * 跳登录
         */
        @JavascriptInterface
        public void toLoginAct(){
            startActivity(new Intent(TexasWebActivity.this , LoginActivity.class));
        }

    }

}