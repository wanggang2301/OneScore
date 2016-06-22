package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.ToastTools;
import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.exception.CyanException;
import com.sohu.cyan.android.sdk.http.CyanRequestListener;
import com.sohu.cyan.android.sdk.http.response.SubmitResp;
import com.umeng.analytics.MobclickAgent;

/**
 * @author lzf
 * @ClassName: InputActivity
 * @Description: 输入评论的窗口
 * @date
 */
public class InputActivity extends Activity implements View.OnClickListener, CyanRequestListener<SubmitResp> {

    private EditText mEditText;//输入评论
    private TextView mSend;//发送评论
    private CyanSdk sdk;
    private long topicid;//畅言分配的文章ID，通过loadTopic接口获取
    private boolean issubmitFinish = true;//是否提交评论结束

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        initWindow();
        initView();
        topicid = getIntent().getLongExtra(CyUtils.INTENT_PARAMS_SID, 0);
        IntentFilter intentFilter = new IntentFilter("closeself");
        registerReceiver(mBroadcastReceiver, intentFilter);

    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("closeself")){
                InputActivity.this.setResult(CyUtils.RESULT_CODE);
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    private void initWindow() {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
    }

    private void initView() {
        sdk = CyanSdk.getInstance(this);
        mEditText = (EditText) findViewById(R.id.et_comment);
        mEditText.requestFocus();
        mEditText.setSelected(true);
        mSend = (TextView) findViewById(R.id.iv_send);
        mEditText.setOnClickListener(this);
        mSend.setOnClickListener(this);
        mSend.setVisibility(View.VISIBLE);
        TextView mCommentCount = (TextView) findViewById(R.id.tv_commentcount);//评论数
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
                mSend.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_send://发送评论
                if (TextUtils.isEmpty(mEditText.getText())) {//没有输入内容
                    ToastTools.ShowQuickCenter(this, getResources().getString(R.string.warn_nullcontent));
                } else {//有输入内容
                    if (CommonUtils.isLogin()) {//已登录华海
                        if (CyUtils.isLogin) {//已登录畅言
                            if (issubmitFinish) {//是否提交完成，若提交未完成，则不再重复提交
                                issubmitFinish = false;
                                CyUtils.submitComment(topicid, mEditText.getText() + "", sdk, this);
                            }
                        } else {//未登录
                            ToastTools.ShowQuickCenter(this, getResources().getString(R.string.warn_submitfail));
                            CyUtils.loginSso(AppConstants.register.getData().getUser().getUserId(), AppConstants.register.getData().getUser().getNickName(), sdk);
                        }
                        CyUtils.hideKeyBoard(this);
                    } else {
                        //跳转登录界面
                        Intent intent1 = new Intent(this, LoginActivity.class);
                        startActivityForResult(intent1, CyUtils.JUMP_COMMENT_QUESTCODE);
                    }

                }
                MobclickAgent.onEvent(this, "Football_CounselCommentActivity_Send");
                break;
            case R.id.et_comment://输入
                if (!CommonUtils.isLogin()) {
                    //跳转登录界面
                    Intent intent1 = new Intent(InputActivity.this, LoginActivity.class);
                    startActivityForResult(intent1, CyUtils.JUMP_COMMENT_QUESTCODE);
                }
                break;
        }

    }

    //评论提交成功回调接口
    @Override
    public void onRequestSucceeded(SubmitResp submitResp) {
        mEditText.setText("");
        issubmitFinish = true;
        setResult(CyUtils.RESULT_CODE);
//        ToastTools.ShowQuickCenter(this,getResources().getString(R.string.succed_send));
        finish();

    }

    //评论提交失败回调接口
    @Override
    public void onRequestFailed(CyanException e) {
        issubmitFinish = true;
        ToastTools.ShowQuickCenter(this, getResources().getString(R.string.warn_submitfail));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(CyUtils.RESULT_CODE);//这里也需要通知  因为在本窗口关闭的时候那边的假输入框需要显示
            finish();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}

