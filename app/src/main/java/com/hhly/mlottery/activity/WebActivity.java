package com.hhly.mlottery.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.callback.ShareCopyLinkCallBack;
import com.hhly.mlottery.callback.ShareTencentCallBack;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ShareConstants;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.widget.ProgressWebView;
import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.exception.CyanException;
import com.sohu.cyan.android.sdk.http.CyanRequestListener;
import com.sohu.cyan.android.sdk.http.response.SubmitResp;
import com.sohu.cyan.android.sdk.http.response.TopicCountResp;
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
    private String imageurl;// 图片地址
    private String title;// 标题
    private String subtitle;// 副标题
    private int mType;// 关联赛事类型
    private String mThird;// 关联赛事ID
    private String infoTypeName;// 资讯类型名
    private TextView mCommentCount;//评论数
    private EditText mEditText;//输入评论
    private TextView mSend;//发送评论
    private CyanSdk sdk;
    private long topicid;//畅言分配的文章ID，通过loadTopic接口获取
    private int cmt_sum;//评论总数
    private static final String INTENT_PARAMS_URL = "url";

    private ImageView public_btn_set;

    private ShareTencentCallBack mShareTencentCallBack;

    private ShareCopyLinkCallBack mShareCopyLinkCallBack;


    private Tencent mTencent;

    private SharePopupWindow sharePopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        //初始化畅言
        CyUtils.initCy(this);
        sdk = CyanSdk.getInstance(this);
        //单点登录
        CyUtils.loginSso("heheheid", "lzfgege", sdk);
        initView();
        initData();
        //获取评论信息 无需登录  这里主要拿评论总数和文章id
        loadTopic();
        initEvent();

//        getCommentCount();
    }

    public void getCommentCount() {
        //获取某个新闻的评论数
        sdk.getCommentCount("", url, 0, new CyanRequestListener<TopicCountResp>() {
            @Override
            public void onRequestSucceeded(TopicCountResp data) {
                Toast.makeText(WebActivity.this, String.valueOf(data.count), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestFailed(CyanException e) {
                Toast.makeText(WebActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                    mSend.setEnabled(false);

                } else {
                    mSend.setSelected(true);
                    mSend.setEnabled(true);
                }

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
        public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setImageResource(R.mipmap.share);
        public_btn_set.setVisibility(View.GONE);

        public_btn_set.setOnClickListener(this);


        mPublic_img_back = (ImageView) findViewById(R.id.public_img_back);
        mPublic_txt_title = (TextView) findViewById(R.id.public_txt_title);
        mWebView = (ProgressWebView) findViewById(R.id.baseweb_webview);
        mTv_check_info = (TextView) findViewById(R.id.tv_check_info);
        mCommentCount = (TextView) findViewById(R.id.tv_commentcount);
        mEditText = (EditText) findViewById(R.id.et_comment);
        mSend = (TextView) findViewById(R.id.iv_send);
        mEditText.setOnClickListener(this);
        mCommentCount.setOnClickListener(this);
        mSend.setOnClickListener(this);
        mPublic_img_back.setOnClickListener(this);
        mSend.setVisibility(View.GONE);
        mCommentCount.setVisibility(View.VISIBLE);
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
            imageurl = getIntent().getStringExtra("imageurl");
            title = getIntent().getStringExtra("title");
            subtitle = getIntent().getStringExtra("subtitle");//轮播图没有副标题，所以为null  请知悉
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

            /**加載成功显示 分享按钮*/
            public_btn_set.setVisibility(View.VISIBLE);

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
                startActivity(intent);
                break;
            case R.id.public_img_back://返回
                MobclickAgent.onEvent(mContext, "Football_DataInfo_Exit");
                finish();
                break;
            case R.id.et_comment://输入评论
//                mSend.setVisibility(View.VISIBLE);
//                mCommentCount.setVisibility(View.GONE);
                break;
            case R.id.iv_send://发送评论
                MobclickAgent.onEvent(mContext, "Football_DataInfo_Send");
                if (CyUtils.isLogin) {
                    CyUtils.submitComment(topicid, mEditText.getText() + "", sdk, this);
                } else {
                    ToastTools.ShowQuickCenter(this, "发送失败，请重试");
                    CyUtils.loginSso("heheheid", "lzfgege", sdk);
                }
                hideKeyBoard();
                break;

            case R.id.public_btn_set: //分享
                MobclickAgent.onEvent(mContext, "Football_DataInfo_Share");
                Map<String, String> map = new HashMap<String, String>();
                map.put(ShareConstants.TITLE, title != null ? title : "");
                map.put(ShareConstants.SUMMARY, subtitle != null ? subtitle : "");
                map.put(ShareConstants.TARGET_URL, url != null ? url : "");
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
                break;
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void shareQQ(int falg) {
        mTencent = Tencent.createInstance(ShareConstants.QQ_APP_ID, this);
        final Bundle bundle = new Bundle();
        //这条分享消息被好友点击后的跳转URL。

        String appname = getString(R.string.share_to_qq_app_name);

        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title != null ? title : "");
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, subtitle != null ? subtitle : "");
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

    /**
     * 隐藏原生键盘
     */
    public void hideKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // ToastTools.shortShow(activity, "隐藏键盘");

    }

    //获取评论的一切消息  无需登录
    public void loadTopic() {
        sdk.loadTopic("", url, "title", null, 0, 0, "", null, 1, 10, new CyanRequestListener<TopicLoadResp>() {
            @Override
            public void onRequestSucceeded(TopicLoadResp topicLoadResp) {
                topicid = topicLoadResp.topic_id;
                cmt_sum = topicLoadResp.cmt_sum;
                mCommentCount.setText(cmt_sum + "");
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
        if (!TextUtils.isEmpty(mCommentCount.getText())) {
            int count = Integer.parseInt(mCommentCount.getText().toString()) + 1;
            mCommentCount.setText(count + "");
        }
        mEditText.setText("");
        mSend.setVisibility(View.GONE);
        mCommentCount.setVisibility(View.VISIBLE);
    }

    //评论提交失败回调接口
    @Override
    public void onRequestFailed(CyanException e) {
        ToastTools.ShowQuickCenter(this, "发送失败，请重试");
        mSend.setVisibility(View.VISIBLE);
        mCommentCount.setVisibility(View.GONE);

    }
}