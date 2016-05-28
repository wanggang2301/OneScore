package com.hhly.mlottery.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.callback.ShareCopyLinkCallBack;
import com.hhly.mlottery.callback.ShareTencentCallBack;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ShareConstants;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.view.IsBottomScrollView;
import com.hhly.mlottery.widget.ProgressWebView;
import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.exception.CyanException;
import com.sohu.cyan.android.sdk.http.CyanRequestListener;
import com.sohu.cyan.android.sdk.http.response.SubmitResp;
import com.sohu.cyan.android.sdk.http.response.TopicLoadResp;
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
public class WebActivity extends BaseActivity implements OnClickListener, CyanRequestListener<SubmitResp> {

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
    private TextView mCommentCount;//显示评论数
    private EditText mEditText;//输入评论
    private TextView mSend;//发送评论
    private CyanSdk sdk;
    private long topicid;//畅言分配的文章ID，通过loadTopic接口获取
    private int cmt_sum;//评论总数
    private static final String INTENT_PARAMS_URL = "url";
    private static final String INTENT_PARAMS_TITLE = "title";
    private static final int JUMP_QUESTCODE = 1;
    private static final int JUMP_COMMENT_QUESTCODE = 3;
    private int def = 0;

    private ImageView public_btn_set;

    private ShareTencentCallBack mShareTencentCallBack;

    private ShareCopyLinkCallBack mShareCopyLinkCallBack;

    private Tencent mTencent;
    private ScrollView scrollview;
    private SharePopupWindow sharePopupWindow;
    private String model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        sdk = CyanSdk.getInstance(this);
        //单点登录   nickname可以相同  用户id不能相同
        if (CommonUtils.isLogin()) {
            CyUtils.loginSso(AppConstants.register.getData().getUser().getUserId(), AppConstants.register.getData().getUser().getNickName(), sdk);
        }
        initView();
        initScrollView();//解决adjustresize和透明状态栏的冲突
        initData();
        //获取评论信息 无需登录  这里主要拿评论总数和文章id
        loadTopic();
        initEvent();
        model = DeviceInfo.getModel().replace(" ", "");
//        System.out.println("lzf" +model );

//        getCommentCount();
        CyUtils.getUserInfo(sdk);
    }

    private void initScrollView() {
        //解决adjustresize和透明状态栏的冲突
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        final IsBottomScrollView isbottomscrollview = (IsBottomScrollView) findViewById(R.id.isbottomscrollview);
        final View decview = getWindow().getDecorView();
        decview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                reLayout(decview, scrollview);
            }
        });
        //解决mTv_check_info在webview还没加载时显示在顶部的问题
        isbottomscrollview.setOnScrollToBottomLintener(new IsBottomScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener(boolean isBottom) {
                if (isBottom) {
                    if (mType != 0 && !TextUtils.isEmpty(mThird)) {
                        mTv_check_info.setVisibility(View.VISIBLE);
                    } else {
                        mTv_check_info.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    //
//    public void getCommentCount() {
//        //获取某个新闻的评论数
//        sdk.getCommentCount("", url, 0, new CyanRequestListener<TopicCountResp>() {
//            @Override
//            public void onRequestSucceeded(TopicCountResp data) {
//                Toast.makeText(WebActivity.this, String.valueOf(data.count), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onRequestFailed(CyanException e) {
//                Toast.makeText(WebActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    //在某个view里重新布局某个view
    public void reLayout(View decview, View scrollview) {
        Rect r = new Rect();
        decview.getWindowVisibleDisplayFrame(r);
        int screenheight = decview.getRootView().getHeight();
        int h = screenheight - r.bottom;
//                Log.e("lzfh", h + "");
//                Log.e("lzfr.bottom", r.bottom + "");
//                Log.e("lzfscreenheight", screenheight + "");
        if (h > 300) {//软键盘显示
//                    ToastTools.ShowQuickCenter(WebActivity.this, "软件盘显示");
//                    Log.e("lzf", "软件盘显示");
            mSend.setVisibility(View.VISIBLE);
            mCommentCount.setVisibility(View.GONE);
            mEditText.setHint("");
            if (model.equals("m2note")) {
                h -= 145;
            }

        } else if (h < 300) {//软键盘隐藏
            if (h != 0) {
                def = h;//因为有的手机在键盘隐藏时   int h = screenheight - r.bottom;这两个的
                // 差值h不是0，有一个差值，所以把这个差值保存起来，重新layou的时候，减去这个差值
            }
//                    ToastTools.ShowQuickCenter(WebActivity.this, "软件盘隐藏");
//                    Log.e("lzf", "软件盘隐藏");
            if (TextUtils.isEmpty(mEditText.getText())) {
                mSend.setVisibility(View.GONE);
                mCommentCount.setVisibility(View.VISIBLE);
            } else {
                mSend.setVisibility(View.VISIBLE);
                mCommentCount.setVisibility(View.GONE);
            }
            mEditText.setHint(R.string.hint_content);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//api大于19透明状态栏才有效果，这时候才重新布局
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) scrollview.getLayoutParams();
//            int x= DisplayUtil.px2dip(WebActivity.this, h - def);
//            Log.e("lzfh - def", (h - def) + "");
            lp.setMargins(0, 0, 0, h - def);
            scrollview.requestLayout();
        }
    }

    private void initEvent() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mSend.setVisibility(View.VISIBLE);
                mCommentCount.setVisibility(View.GONE);
                if (TextUtils.isEmpty(mEditText.getText())) {
                    mSend.setSelected(false);
                } else {
                    mSend.setSelected(true);
                }
            }
        });
        mEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtils.isLogin()) {
                    //跳转登录界面
                    Intent intent1 = new Intent(WebActivity.this, LoginActivity.class);
                    startActivityForResult(intent1, JUMP_COMMENT_QUESTCODE);
                }
            }
        });
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                mEditText.setSelected(hasFocus);


            }


        });
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
                }
            });
        }
    }


    private void initView() {
        ImageView public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);
        public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setImageResource(R.mipmap.share);
        public_btn_set.setVisibility(View.GONE);

        public_btn_set.setOnClickListener(this);

        mPublic_img_back = (ImageView) findViewById(R.id.public_img_back);
        mPublic_txt_title = (TextView) findViewById(R.id.public_txt_title);
        mWebView = (ProgressWebView) findViewById(R.id.baseweb_webview);
        mTv_check_info = (TextView) findViewById(R.id.tv_check_info);
        mTv_check_info.setVisibility(View.GONE);
        mCommentCount = (TextView) findViewById(R.id.tv_commentcount);
        mEditText = (EditText) findViewById(R.id.et_comment);
        mSend = (TextView) findViewById(R.id.iv_send);
        mEditText.setOnClickListener(this);
        mEditText.clearFocus();
        mEditText.setSelected(false);
        mCommentCount.setOnClickListener(this);
        mSend.setOnClickListener(this);
        mPublic_img_back.setOnClickListener(this);
        mSend.setVisibility(View.GONE);
        mCommentCount.setVisibility(View.VISIBLE);
        WebSettings webSettings = mWebView.getSettings();
        // 优先使用缓存
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false);
        String us = webSettings.getUserAgentString();
//        System.out.println("lzfus="+us);
        webSettings.setUserAgentString(us.replace("Android", "yibifen Android"));//给useagent加个标识yibifen
//        System.out.println("lzfus1=" + webSettings.getUserAgentString());

    }

    protected void initData() {
        try {
            Intent intent = getIntent();
            url = intent.getStringExtra("key");
//            url = "http://192.168.37.6:8080/gameweb/h5/index";
            imageurl = intent.getStringExtra("imageurl");
            title = intent.getStringExtra(INTENT_PARAMS_TITLE);
            subtitle = intent.getStringExtra("subtitle");//轮播图没有副标题，所以为null  请知悉
            mType = intent.getIntExtra("type", 0);
            mThird = intent.getStringExtra("thirdId");
            infoTypeName = intent.getStringExtra("infoTypeName");
            token = intent.getStringExtra("token");
//            token ="fe95688ec6074e1cb4486c0bd3a60c34";
//            String token =AppConstants.register.getData().getLoginToken();
            String deviceId = AppConstants.deviceToken;
//            url="http://game1.1332255.com:8082/h5/index?loginToken="+token+"&deviceToken="+deviceId;
            reqMethod = intent.getStringExtra("reqMethod");
            mPublic_txt_title.setText(infoTypeName);
            if (TextUtils.isEmpty(token)) {//token为空，说明是资讯，显示分享和评论
                public_btn_set.setVisibility(View.VISIBLE);
                scrollview.setVisibility(View.VISIBLE);
            } else {//不是新闻资讯的时候隐藏分享和评论
                public_btn_set.setVisibility(View.GONE);
                scrollview.setVisibility(View.GONE);
            }
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            //其他页传过来的reqMethod为post时，提交token  否则不提交
            if (reqMethod != null && token != null && reqMethod.equals("post")) {

//                mWebView.postUrl(url, token.getBytes("utf-8"));
//                url = url + "?loginToken=" + token + "&deviceToken=" + deviceId;


                url=url.replace("{loginToken}", token);
                url=url.replace("{deviceToken}", deviceId);
            }
            mWebView.loadUrl(url);
            System.out.println("CommonUtilslzfwebview" + url);
            L.d("lzf:" + "imageurl=" + imageurl + "title" + title + "subtitle" + subtitle);
            L.d("CommonUtils:" + "token=" + token + "reqMethod" + reqMethod);

//            /**加載成功显示 分享按钮*/
//            public_btn_set.setVisibility(View.VISIBLE);

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
            case R.id.tv_commentcount://评论数
                MobclickAgent.onEvent(mContext, "Football_DataInfo_CommentCount");
                Intent intent = new Intent(this, CounselCommentActivity.class);
                intent.putExtra(INTENT_PARAMS_URL, url);
                intent.putExtra(INTENT_PARAMS_TITLE, title);
                startActivityForResult(intent, JUMP_QUESTCODE);
//                startActivity(intent);
                break;
            case R.id.public_img_back://返回

                if (mWebView.canGoBack()) {
                    mWebView.goBack();// 返回上一页面

                } else {
                    MobclickAgent.onEvent(mContext, "Football_DataInfo_Exit");
                    finish();
                }

                break;
            case R.id.iv_send://发送评论
                MobclickAgent.onEvent(mContext, "Football_DataInfo_Send");
                if (TextUtils.isEmpty(mEditText.getText())) {//没有输入内容
                    ToastTools.ShowQuickCenter(this, getResources().getString(R.string.warn_nullcontent));
                } else {//有输入内容
                    if (CyUtils.isLogin) {//已登录
                        L.i("lzf提交topicid=" + topicid);
                        CyUtils.submitComment(topicid, mEditText.getText() + "", sdk, this);
                    } else {//未登录
                        if (CommonUtils.isLogin()) {
                            ToastTools.ShowQuickCenter(this, getResources().getString(R.string.warn_submitfail));
                            CyUtils.loginSso(AppConstants.register.getData().getUser().getUserId(), AppConstants.register.getData().getUser().getNickName(), sdk);
                        } else {
                            //跳转登录界面
                            Intent intent1 = new Intent(WebActivity.this, LoginActivity.class);
                            startActivityForResult(intent1, JUMP_COMMENT_QUESTCODE);
                        }

                    }
                    CyUtils.hideKeyBoard(this);
                    mEditText.clearFocus();
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


    //获取评论的一切消息  无需登录
    public void loadTopic() {
        L.e("lzf" + title);
        sdk.loadTopic("", url, title, title, 0, 0, "", null, 1, 10, new CyanRequestListener<TopicLoadResp>() {

            @Override
            public void onRequestSucceeded(TopicLoadResp topicLoadResp) {
                topicid = topicLoadResp.topic_id;
                cmt_sum = topicLoadResp.cmt_sum;
                mCommentCount.setText(cmt_sum + "");
                L.i("lzftopicid第一次=" + topicid);
            }

            @Override
            public void onRequestFailed(CyanException e) {
                L.i("lzftopicid=" + e.toString());
                L.i("lzfcmt_sum=" + e.toString());
            }
        });
    }

    //评论提交成功回调接口
    @Override
    public void onRequestSucceeded(SubmitResp submitResp) {
//        if (!TextUtils.isEmpty(mCommentCount.getText())) {
//            int count = Integer.parseInt(mCommentCount.getText().toString()) + 1;
//            mCommentCount.setText(count + "");
//        }
        loadTopic();
        mEditText.setText("");
        mSend.setVisibility(View.GONE);
        mCommentCount.setVisibility(View.VISIBLE);
        L.e("lzf返回topicid=" + submitResp.id);
    }

    //评论提交失败回调接口
    @Override
    public void onRequestFailed(CyanException e) {
        ToastTools.ShowQuickCenter(this, getResources().getString(R.string.warn_submitfail));
        mSend.setVisibility(View.VISIBLE);
        mCommentCount.setVisibility(View.GONE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //接收全部评论页面返回的评论总数

        if (requestCode == JUMP_QUESTCODE) {
            if (resultCode == 2) {
                int defaultnum = 0;
                if (!TextUtils.isEmpty(mCommentCount.getText())) {
                    defaultnum = Integer.parseInt(mCommentCount.getText().toString());
                }
                String commentcount = data.getIntExtra("cmt_sum", defaultnum) + "";
                mCommentCount.setText(commentcount);
            }
        }
        //接收登录华海成功返回
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                CyUtils.loginSso(AppConstants.register.getData().getUser().getUserId(), AppConstants.register.getData().getUser().getNickName(), sdk);
            }
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
}