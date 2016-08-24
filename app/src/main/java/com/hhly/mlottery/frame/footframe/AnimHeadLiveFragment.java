package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.ProgressWebView;

/**
 * 描述:  足球内页动画直播
 * 作者:  wangg@13322.com
 * 时间:  2016/8/8 10:35
 */
public class AnimHeadLiveFragment extends Fragment {

    private static final String TAG = "AnimHeadLiveFragment";
    private static final String THIRDID = "thirdId";

    private static final String baseURL = "http://192.168.31.107:9000/live/footballodds_graphic.html?thirdId=";

    private String thirdId;
    private Context context;
    private View mView;
    private ProgressWebView mWebView;

    private TextView tv_nopage;

    private String url;


    public static AnimHeadLiveFragment newInstance(String thirdId) {
        Bundle bundle = new Bundle();
        bundle.putString(THIRDID, thirdId);
        AnimHeadLiveFragment animHeadLiveFragment = new AnimHeadLiveFragment();
        animHeadLiveFragment.setArguments(bundle);
        return animHeadLiveFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_anim_head_live, container, false);
        if (getArguments() != null) {
            thirdId = getArguments().getString(THIRDID);
        }
        context = getActivity();
        tv_nopage = (TextView) mView.findViewById(R.id.tv_nopage);
        mWebView = (ProgressWebView) mView.findViewById(R.id.webview);

        //url = "http://m.1332255.com/news/infomationhtml/20160817/2016081730808.html";

        L.d("456789", "动画直播" + thirdId);

        //  url = baseURL + thirdId;
        url = BaseURLs.URL_FOOTBALLDETAIL_H5 + "?thirdId=" + thirdId;

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


      /*  L.d(TAG, thirdId);

        String url = "http://192.168.31.107:3000/act/footballodds/footballodds_graphic.html?thirdId=355174";

        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);          //支持缩放
        settings.setBuiltInZoomControls(true);  //启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        settings.setLoadWithOverviewMode(true);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        webView.loadUrl(url);
        webView.setWebViewClient(new MyWebViewClient());*/

        return mView;

    }

   /* public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "-MyWebViewClient->shouldOverrideUrlLoading()--");
            view.loadUrl(url);
            return true;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "-MyWebViewClient->onPageStarted()--");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "-MyWebViewClient->onPageFinished()--");
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            Log.d(TAG, "-MyWebViewClient->onReceivedError()--");
            //这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
            //view.loadData(errorHtml, "text/html", "UTF-8");
            webView.setVisibility(View.GONE);
            tv_nopage.setVisibility(View.VISIBLE);
        }
    }
*/

}
