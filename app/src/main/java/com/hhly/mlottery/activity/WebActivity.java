package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.ProgressWebView;
import com.umeng.analytics.MobclickAgent;

/**
 * WebView显示H5
 * 107
 */
public class WebActivity extends BaseActivity {

    private ProgressWebView mWebView;
    private TextView mTv_check_info;// 关联跳转按钮
    private ImageView mPublic_img_back;// 返回
    private TextView mPublic_txt_title;// 标题

    private String url;// 要显示的H5网址
    private int mType;// 关联赛事类型
    private String mThird;// 关联赛事ID
    private String infoTypeName;// 资讯类型名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mPublic_img_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 跳转关联赛事详情
        if (mType != 0 && !TextUtils.isEmpty(mThird)) {
            mTv_check_info.setVisibility(View.VISIBLE);
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
                }
            });
        } else {
            mTv_check_info.setVisibility(View.GONE);
        }
    }

    private void initView() {
        ImageView public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);
        ImageView public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setVisibility(View.GONE);

        mPublic_img_back = (ImageView) findViewById(R.id.public_img_back);
        mPublic_txt_title = (TextView) findViewById(R.id.public_txt_title);
        mWebView = (ProgressWebView) findViewById(R.id.baseweb_webview);
        mTv_check_info = (TextView) findViewById(R.id.tv_check_info);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    protected void initData() {
        try {
            url = getIntent().getStringExtra("key");
            mType = getIntent().getIntExtra("type", 0);
            mThird = getIntent().getStringExtra("thirdId");
            infoTypeName = getIntent().getStringExtra("infoTypeName");
            mPublic_txt_title.setText(infoTypeName);

            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

            });
            mWebView.loadUrl(url);
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
}