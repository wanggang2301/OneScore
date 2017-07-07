package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;

/**
 * Created by 103TJL on 2016/1/13.
 * 播放视频的webview
 */
public class PlayWebViewActivity extends BaseActivity implements View.OnClickListener {
    private ImageView public_img_back, public_btn_filter, public_btn_set;
    private TextView public_txt_title;
    private WebView mWebView;
    String stUrl;
    private static final int ERROR = -1;//访问失败
    private static final int SUCCESS = 0;// 访问成功
    private static final int STARTLOADING = 1;// 数据加载中

    //加载数据
    private FrameLayout fl_play_loading;// 正在加载中
    private FrameLayout fl_play_networkError;// 加载失败
    private TextView play_reLoading;// 刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_play);
        Intent in = getIntent();
        stUrl = in.getStringExtra("url");
        InitView();
        InitData();
    }

    public void InitView() {
        fl_play_loading = (FrameLayout) findViewById(R.id.fl_paly_loading);
        fl_play_networkError = (FrameLayout) findViewById(R.id.fl_paly_networkError);
        play_reLoading = (TextView) findViewById(R.id.play_reLoading);
        play_reLoading.setOnClickListener(this);

        public_txt_title = (TextView) findViewById(R.id.public_txt_title);//标题
        public_img_back = (ImageView) findViewById(R.id.public_img_back);//返回
        public_img_back.setOnClickListener(this);
        public_img_back.setImageResource(R.mipmap.number_back_icon);
        public_txt_title.setText(R.string.live_txt);//设置标题
        public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);//筛选
        public_btn_filter.setVisibility(View.GONE);
        public_btn_set = (ImageView) findViewById(R.id.public_btn_set);//设置
        public_btn_set.setVisibility(View.GONE);
        mWebView = (WebView) findViewById(R.id.play_web);
        //支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(false);
        //扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        //加载H5
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
    }

    public void InitData() {

        if (mWebView != null) {
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    mHandler.sendEmptyMessage(SUCCESS);
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, final String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                    mWebView.setVisibility(View.GONE);
                    fl_play_networkError.setVisibility(View.VISIBLE);
//                    mHandler.sendEmptyMessage(ERROR);
                    findViewById(R.id.play_reLoading).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mWebView.loadUrl(failingUrl);
                            LoadThread("Repeat");//第二次加载

                        }
                    });
                }
            });
            loadUrl(stUrl);
        }


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back://返回
                finish();
                break;
            case R.id.play_reLoading://刷新
                InitData();
                break;
            default:
                break;
        }
    }

//     判断加载的地址是否为空
    public void loadUrl(final String url) {
        if (mWebView != null) {
            mWebView.loadUrl(url);
            mHandler.sendEmptyMessage(STARTLOADING);
            LoadThread("Frist");//第一次加载

        }
    }
    public void LoadThread(final String isFrist) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isFrist.equals("Frist")) {//第一次加载
//                                mWebView.reload();
                            } else {//重复加载
                                mWebView.reload();
                                mHandler.sendEmptyMessage(SUCCESS);

                            }
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    fl_play_loading.setVisibility(View.GONE);
                    fl_play_networkError.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);
                    break;
                case STARTLOADING://正在加载的时候
                    fl_play_loading.setVisibility(View.VISIBLE);
                    fl_play_networkError.setVisibility(View.GONE);
                    mWebView.setVisibility(View.GONE);
                    break;
                case ERROR://访问失败
                    fl_play_loading.setVisibility(View.GONE);
                    mWebView.setVisibility(View.GONE);
                    fl_play_networkError.setVisibility(View.VISIBLE);

                    break;

                default:
                    break;
            }
        }
    };


    public void onPause() {//继承自Activity
        mWebView.reload();
        mWebView.destroy();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
