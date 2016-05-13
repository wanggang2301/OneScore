package com.hhly.mlottery.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.view.IsBottomScrollView;
import com.hhly.mlottery.widget.ProgressWebView;
import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.exception.CyanException;
import com.sohu.cyan.android.sdk.http.CyanRequestListener;
import com.sohu.cyan.android.sdk.http.response.SubmitResp;
import com.sohu.cyan.android.sdk.http.response.TopicLoadResp;
import com.umeng.analytics.MobclickAgent;

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
    private TextView mCommentCount;//显示评论数
    private EditText mEditText;//输入评论
    private TextView mSend;//发送评论
    private CyanSdk sdk;
    private long topicid;//畅言分配的文章ID，通过loadTopic接口获取
    private int cmt_sum;//评论总数
    private static final String INTENT_PARAMS_URL = "url";
    private static final String INTENT_PARAMS_TITLE = "title";
    private static final int JUMP_QUESTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        sdk = CyanSdk.getInstance(this);
        //单点登录
        CyUtils.loginSso("heheheid", "lzfgege", sdk);
        initView();
        initScrollView();//解决adjustresize和透明状态栏的冲突
        initData();
        //获取评论信息 无需登录  这里主要拿评论总数和文章id
        loadTopic();
        initEvent();

//        getCommentCount();

    }

    private void initScrollView() {
        //解决adjustresize和透明状态栏的冲突
        final ScrollView scrollview = (ScrollView) findViewById(R.id.scrollview);
        final IsBottomScrollView isbottomscrollview = (IsBottomScrollView) findViewById(R.id.isbottomscrollview);
        final View decview = getWindow().getDecorView();
        decview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decview.getWindowVisibleDisplayFrame(r);
                int screenheight = decview.getRootView().getHeight();
                int h = screenheight - r.bottom;
//                Log.e("lzfh", h + "");
                if (h > 0) {//软键盘显示
//                    ToastTools.ShowQuickCenter(WebActivity.this, "软件盘显示");
//                    Log.e("lzf", "软件盘显示");
                    mSend.setVisibility(View.VISIBLE);
                    mCommentCount.setVisibility(View.GONE);
                    mEditText.setHint("");

                } else if (h == 0) {//软键盘隐藏
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
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) scrollview.getLayoutParams();
                lp.setMargins(0, 0, 0, h);
                scrollview.requestLayout();
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
            title = getIntent().getStringExtra(INTENT_PARAMS_TITLE);
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
                Intent intent = new Intent(this, CounselCommentActivity.class);
                intent.putExtra(INTENT_PARAMS_URL, url);
                intent.putExtra(INTENT_PARAMS_TITLE, title);
                startActivityForResult(intent, JUMP_QUESTCODE);
//                startActivity(intent);
                break;
            case R.id.public_img_back://返回
                finish();
                break;
            case R.id.iv_send://发送评论
                if (TextUtils.isEmpty(mEditText.getText())) {//没有输入内容
                    ToastTools.ShowQuickCenter(this, getResources().getString(R.string.warn_nullcontent));
                } else {//有输入内容
                    if (CyUtils.isLogin) {//已登录
                        CyUtils.submitComment(topicid, mEditText.getText() + "", sdk, this);
                    } else {//未登录
                        ToastTools.ShowQuickCenter(this, getResources().getString(R.string.warn_submitfail));
                        CyUtils.loginSso("heheheid", "lzfgege", sdk);
                    }
                    CyUtils.hideKeyBoard(this);
                    mEditText.clearFocus();
                }
                break;
        }
    }


    //获取评论的一切消息  无需登录
    public void loadTopic() {
        sdk.loadTopic("", url, title, null, 0, 0, "", null, 1, 10, new CyanRequestListener<TopicLoadResp>() {
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
        ToastTools.ShowQuickCenter(this, getResources().getString(R.string.warn_submitfail));
        mSend.setVisibility(View.VISIBLE);
        mCommentCount.setVisibility(View.GONE);

    }

    //接收全部评论页面返回的评论总数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    }

}