package com.hhly.mlottery.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.widget.ProgressWebView;

public class SnookerPlayerInfoActivity extends Activity {

    private ProgressWebView mWebView;

    private TextView tv_nopage;

    private String url;

    private String playerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            playerId = getIntent().getStringExtra("playerId");
        }

        setContentView(R.layout.activity_snooker_player_info);
        initView();
        loadAnim();
    }

    private void initView() {
        tv_nopage = (TextView) findViewById(R.id.tv_nopage);
        mWebView = (ProgressWebView) findViewById(R.id.webview);

       //url = BaseURLs.URL_SNOOKER_INFO_PLAYER_INFO_H5 + "?lang=" + appendLanguage() + "&playerId=" + playerId;
         url = "http://192.168.31.131:9000/snookerData/playerInfo.html?lang=" + appendLanguage() + "&playId=" + playerId;
    }


    public void loadAnim() {
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

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mWebView.setVisibility(View.GONE);
                tv_nopage.setVisibility(View.VISIBLE);
            }
        });

        mWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
    }

    /**
     * 根据选择语言，改变推送接口语言环境
     *
     * @return
     */
    private String appendLanguage() {
        String lang = "zh";//默认中文
        if (MyApp.isLanguage.equals("rCN")) {
            // 如果是中文简体的语言环境
            lang = BaseURLs.LANGUAGE_SWITCHING_CN;
        } else if (MyApp.isLanguage.equals("rTW")) {
            // 如果是中文繁体的语言环境
            lang = BaseURLs.LANGUAGE_SWITCHING_TW;
        } else if (MyApp.isLanguage.equals("rEN")) {
            // 如果是英文环境
            lang = BaseURLs.LANGUAGE_SWITCHING_EN;
        } else if (MyApp.isLanguage.equals("rKO")) {
            // 如果是韩语环境
            lang = BaseURLs.LANGUAGE_SWITCHING_KO;
        } else if (MyApp.isLanguage.equals("rID")) {
            // 如果是印尼语
            lang = BaseURLs.LANGUAGE_SWITCHING_ID;
        } else if (MyApp.isLanguage.equals("rTH")) {
            // 如果是泰语
            lang = BaseURLs.LANGUAGE_SWITCHING_TH;
        } else if (MyApp.isLanguage.equals("rVI")) {
            // 如果是越南语
            lang = BaseURLs.LANGUAGE_SWITCHING_VI;
        }

        return lang.trim();
    }
}
