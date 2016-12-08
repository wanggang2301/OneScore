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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.footframe.eventbus.ChartBallContentEntitiy;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.ToastTools;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import io.github.rockerhieu.emojicon.EmojiconEditText;


public class ChartballActivity extends BaseActivity implements View.OnClickListener {

    private EmojiconEditText mEditText;//输入评论
    private TextView mSend;//发送评论
    private LinearLayout ll_gallery_content;
    private ImageView iv_gallery;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartball_layout);
        /**当前评论小窗口不统计*/
        MobclickAgent.openActivityDurationTrack(false);

        initWindow();
        initView();
        IntentFilter intentFilter = new IntentFilter("closeself");
        registerReceiver(mBroadcastReceiver, intentFilter);

    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ChartballActivity.this.setResult(CyUtils.RESULT_CODE);
            CyUtils.hideKeyBoard(ChartballActivity.this);
            finish();
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
        mEditText = (EmojiconEditText) findViewById(R.id.et_emoji_input);
        mEditText.requestFocus();
        mEditText.setSelected(true);
        mEditText.setFocusableInTouchMode(true);
        mSend = (TextView) findViewById(R.id.tv_send);
        mEditText.setOnClickListener(this);
        mSend.setOnClickListener(this);
        mSend.setVisibility(View.VISIBLE);

        ll_gallery_content = (LinearLayout) findViewById(R.id.ll_gallery_content);
        iv_gallery = (ImageView) findViewById(R.id.iv_gallery);
        iv_gallery.setOnClickListener(this);

        inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);

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
            case R.id.tv_send://发送评论
                MobclickAgent.onEvent(MyApp.getContext(), "BasketDetailsActivityTest_TalkSend");
                if (TextUtils.isEmpty(mEditText.getText())) {//没有输入内容
                    ToastTools.showQuickCenter(this, getResources().getString(R.string.warn_nullcontent));
                } else {//有输入内容
                    if (CommonUtils.isLogin()) {//已登录华海


                    } else {
                        //跳转登录界面
                        Intent intent1 = new Intent(this, LoginActivity.class);
                        startActivityForResult(intent1, CyUtils.JUMP_COMMENT_QUESTCODE);
                    }

                }

                // 向会话列表发送输入内容
                EventBus.getDefault().post(new ChartBallContentEntitiy(mEditText.getText().toString()));
                finish();
                break;
            case R.id.et_emoji_input:// 输入
                if (!CommonUtils.isLogin()) {
                    //跳转登录界面
                    Intent intent1 = new Intent(ChartballActivity.this, LoginActivity.class);
                    startActivityForResult(intent1, CyUtils.JUMP_COMMENT_QUESTCODE);
                }
                ll_gallery_content.setVisibility(View.GONE);
                break;
            case R.id.iv_gallery:

                // 隐藏软键盘
                if (ChartballActivity.this.getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(ChartballActivity.this.getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                // 让键盘隐藏后再显示
                new Thread(){
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ll_gallery_content.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }.start();

                break;
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(CyUtils.RESULT_BACK);//这里也需要通知  因为在本窗口关闭的时候那边的假输入框需要显示
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

