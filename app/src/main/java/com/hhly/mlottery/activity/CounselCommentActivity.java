package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.CounselComentLvAdapter;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.view.PullUpRefreshListView;
import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.entity.Comment;
import com.sohu.cyan.android.sdk.exception.CyanException;
import com.sohu.cyan.android.sdk.http.CyanRequestListener;
import com.sohu.cyan.android.sdk.http.response.SubmitResp;
import com.sohu.cyan.android.sdk.http.response.TopicLoadResp;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * lzf
 * 全部评论展示页面
 */
public class CounselCommentActivity extends BaseActivity implements OnClickListener, SwipeRefreshLayout.OnRefreshListener, CyanRequestListener<SubmitResp> {


    private ImageView mPublic_img_back;// 返回
    private TextView mPublic_txt_title;// 标题
    private TextView mCommentCount;//评论数
    private EditText mEditText;//输入评论
    private TextView mSend;//发送评论
    private TextView mNoData;//暂无评论
    private SwipeRefreshLayout mSwipeRefreshLayout;//发送评论
    private PullUpRefreshListView mListView;//发送评论
    private CounselComentLvAdapter mAdapter;
    private ArrayList<Comment> mCommentArrayList;//最新评论数据
    private CyanSdk sdk;
    private String url;
    private static final String INTENT_PARAMS_URL = "url";
    private long topicid;//畅言分配的文章ID，通过loadTopic接口获取

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselcomment);
        Intent intent = getIntent();
        url = intent.getStringExtra(INTENT_PARAMS_URL);
        sdk = CyanSdk.getInstance(this);
        initView();
        initListView();
        if (!TextUtils.isEmpty(url)) {
            loadTopic(url);
        }


    }

    private void initListView() {
        mAdapter = new CounselComentLvAdapter(this);
        mListView.setAdapter(mAdapter);
    }


    private void initView() {
        ImageView public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);
        ImageView public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setVisibility(View.GONE);
        mPublic_img_back = (ImageView) findViewById(R.id.public_img_back);
        mPublic_txt_title = (TextView) findViewById(R.id.public_txt_title);
        mNoData = (TextView) findViewById(R.id.nodata);
        mPublic_txt_title.setText("全部留言");
        mListView = (PullUpRefreshListView) findViewById(R.id.comment_lv);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.comment_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(this, StaticValues.REFRASH_OFFSET_END));


        mCommentCount = (TextView) findViewById(R.id.tv_commentcount);
        mEditText = (EditText) findViewById(R.id.et_comment);
        mSend = (TextView) findViewById(R.id.iv_send);
        mEditText.setOnClickListener(this);
        mCommentCount.setOnClickListener(this);
        mSend.setOnClickListener(this);
        mPublic_img_back.setOnClickListener(this);
        mSend.setVisibility(View.VISIBLE);
        mCommentCount.setVisibility(View.GONE);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(mEditText.getText())) {
                    mSend.setSelected(false);
                } else {
                    mSend.setSelected(true);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commentcount://评论数
                break;
            case R.id.public_img_back://返回
                MobclickAgent.onEvent(mContext, "Football_CounselCommentActivity_Exit");
                finish();
                break;
            case R.id.et_comment://输入评论
//                mSend.setVisibility(View.VISIBLE);
//                mCommentCount.setVisibility(View.GONE);
                break;
            case R.id.iv_send://发送评论
                MobclickAgent.onEvent(mContext, "Football_CounselCommentActivity_Send");
                if (TextUtils.isEmpty(mEditText.getText())){//没有输入内容
                    ToastTools.ShowQuickCenter(this, "请先输入内容");
                }else {//有输入内容
                    if (CyUtils.isLogin) {//已登录
                        CyUtils.submitComment(topicid, mEditText.getText() + "", sdk, this);
                    } else {//未登录
                        ToastTools.ShowQuickCenter(this, "发送失败，请重试");
                        CyUtils.loginSso("heheheid", "lzfgege", sdk);
                    }
                }

                hideKeyBoard();
                break;
        }
    }

    /**
     * 隐藏原生键盘
     */
    public void hideKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // ToastTools.shortShow(activity, "隐藏键盘");

    }

    //下拉刷新回调
    @Override
    public void onRefresh() {
        loadTopic(url);
    }

    //获取评论的一切消息  无需登录  并刷新listview
    public void loadTopic(String url) {
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        sdk.loadTopic("", url, "title", null, 150, 150, "", null, 1, 10, new CyanRequestListener<TopicLoadResp>() {
            @Override
            public void onRequestSucceeded(TopicLoadResp topicLoadResp) {
                topicid = topicLoadResp.topic_id;
                mCommentArrayList = topicLoadResp.comments;
                if (mCommentArrayList.size() == 0) {
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mNoData.setVisibility(View.VISIBLE);
                } else {
                    mAdapter.setInfosList(mCommentArrayList);
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mNoData.setVisibility(View.GONE);
                    mListView.setSelection(0);
                }
                mSwipeRefreshLayout.setRefreshing(false);
                L.i("lzf最新列表=" + mCommentArrayList.size());
                L.i("lzf最热列表=" + topicLoadResp.hots.size());
//                for (int i=0;i<mCommentArrayList.size();i++){
//                    L.e("lzf内容=" + mCommentArrayList.get(i).content); //评论的内容
//                    L.e("lzf时间=" + mCommentArrayList.get(i).create_time); //评论的时间
//                    String s1= DateUtil.transferLongToDate(mCommentArrayList.get(i).create_time);
//                    L.e("lzf时间=" +s1); //评论的时间
//
////                    L.e("lzf用户名=" + mCommentArrayList.get(i).passport); ;//评论的用户名
//                }

            }

            @Override
            public void onRequestFailed(CyanException e) {
                L.i("lzftopicid=" + e.toString());
                L.i("lzfcmt_sum=" + e.toString());
                if (mCommentArrayList != null && mCommentArrayList.size() != 0) {

                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                } else {
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mNoData.setVisibility(View.VISIBLE);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //评论提交成功回调接口
    @Override
    public void onRequestSucceeded(SubmitResp submitResp) {
        mEditText.setText("");
        //刷新界面
        loadTopic(url);
    }

    //评论提交失败回调接口
    @Override
    public void onRequestFailed(CyanException e) {
        ToastTools.ShowQuickCenter(this, "发送失败，请重试");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("Football_CounselCommentActivity");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("Football_CounselCommentActivity");
    }
}