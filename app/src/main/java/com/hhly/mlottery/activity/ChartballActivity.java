package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.BasePagerAdapter;
import com.hhly.mlottery.bean.chart.ChartReceive;
import com.hhly.mlottery.frame.chartBallFragment.LocalFragment;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.ToastTools;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * desc:聊球输入框
 * data:2016-12-08 bay:tangrr107
 */
public class ChartballActivity extends BaseActivity implements View.OnClickListener {


    private static final int TEXT_CHANGED = 1;
    public static String TAG = "ChartballActivity";
    public EditText mEditText;//输入评论
    private TextView mSend;//发送评论
    public LinearLayout ll_gallery_content;
    private ImageView iv_gallery;
    private InputMethodManager inputMethodManager;
    private ViewPager view_pager_local;
    List<Fragment> localFragments = new ArrayList<>();

    private final static int LOCAL_PAGER_SIZE = 10;
    private LinearLayout local_point;
    private int localPagerSize;

    private final static String MATCH_THIRD_ID = "thirdId";
    private String mThirdId;
    private String callName;// @昵称
    private String callUserId;// @昵称id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**当前评论小窗口不统计*/
        MobclickAgent.openActivityDurationTrack(false);

        // Make us non-modal, so that others can receive touch events.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        // ...but notify us that it happened.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        setContentView(R.layout.activity_chartball_layout);

        initWindow();
        initView();
        initData();
        initEvent();
        IntentFilter intentFilter = new IntentFilter("CLOSE_INPUT_ACTIVITY");
        registerReceiver(mBroadcastReceiver, intentFilter);

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TEXT_CHANGED:
                    isContains = false;
                    mEditText.setText(mText);
                    mEditText.setSelection(mEditText.getText().length());
                    break;
            }
        }
    };
    String mText;
    boolean isContains = false;// 是否去掉@用户

    private void initEvent() {

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(callName)) {
                    if (!s.toString().contains("@" + callName + ":") && isContains) {
                        mText = s.toString();
                        mHandler.sendEmptyMessage(TEXT_CHANGED);
                    }
                }
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

        view_pager_local.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < localPagerSize; i++) {
                    local_point.getChildAt(i).setEnabled(i == position % localPagerSize ? true : false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        if (getIntent() != null) {
            mThirdId = getIntent().getStringExtra(MATCH_THIRD_ID);
            callName = getIntent().getStringExtra("CALL_NAME");
            callUserId = getIntent().getStringExtra("CALL_USER_ID");
            if (!TextUtils.isEmpty(callName)) {
                mEditText.setText(Html.fromHtml("<font color='#0090ff'>@" + callName + ":</font>"));
                mEditText.setSelection(mEditText.getText().length());
                isContains = true;
                mSend.setSelected(true);
            }
        }
        // 添加数据
        BasePagerAdapter localPagerAdapter = new BasePagerAdapter(getSupportFragmentManager());
        localPagerSize = AppConstants.localIcon.length % LOCAL_PAGER_SIZE == 0 ? AppConstants.localIcon.length / LOCAL_PAGER_SIZE : AppConstants.localIcon.length / LOCAL_PAGER_SIZE + 1;
        for (int i = 0; i < localPagerSize; i++) {
            ArrayList<Integer> list = new ArrayList<>();
            ArrayList<String> listName = new ArrayList<>();
            int startIndex = i * LOCAL_PAGER_SIZE;
            int endIndex = (i + 1) * LOCAL_PAGER_SIZE <= AppConstants.localIcon.length ? (i + 1) * LOCAL_PAGER_SIZE : AppConstants.localIcon.length;
            for (int j = startIndex; j < endIndex; j++) {
                list.add(AppConstants.localIcon[j]);
                listName.add(getLocalName()[j]);
            }
            localFragments.add(LocalFragment.newInstance(list, listName));
        }
        localPagerAdapter.addFragments(localFragments);
        view_pager_local.setAdapter(localPagerAdapter);

        // 添加小圆点
        local_point.removeAllViews();
        if (localPagerSize >= 2) {
            int dp = DisplayUtil.dip2px(mContext, 5);// 添加小圆点
            for (int i = 0; i < localPagerSize; i++) {
                View view = new View(mContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp, dp);
                if (i != 0) {
                    lp.leftMargin = dp;
                }
                view.setEnabled(i == 0);
                view.setBackgroundResource(R.drawable.v_lunbo_point_selector);
                view.setLayoutParams(lp);
                local_point.addView(view);
            }
        }
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
        inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);

        mEditText = (EditText) findViewById(R.id.et_emoji_input);
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
        findViewById(R.id.iv_select_icon).setOnClickListener(this);
        findViewById(R.id.iv_select_icon).setSelected(true);

        view_pager_local = (ViewPager) findViewById(R.id.view_pager_local);
        local_point = (LinearLayout) findViewById(R.id.local_point);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send://发送评论
                MobclickAgent.onEvent(MyApp.getContext(), "BasketDetailsActivityTest_TalkSend");
                if (TextUtils.isEmpty(mEditText.getText())) {//没有输入内容
                    if (TextUtils.isEmpty(mEditText.getText().toString().trim())) {
                        ToastTools.showQuickCenter(this, getResources().getString(R.string.warn_nullcontent));
                    }
                } else {//有输入内容
                    if (DeviceInfo.isLogin()) {//已登录华海
                        ChartReceive.DataBean.ChatHistoryBean chatHistoryBean;

                        if (!TextUtils.isEmpty(callName)) {
                            if (mEditText.getText().toString().contains("@" + callName + ":")) {
                                String replace = mEditText.getText().toString().replace("@" + callName + ":", "");
                                chatHistoryBean = new ChartReceive.DataBean.ChatHistoryBean(null, 2, replace.trim(), new ChartReceive.DataBean.ChatHistoryBean.FromUserBean(AppConstants.register.getUser().getUserId()
                                        , AppConstants.register.getUser().getImageSrc(), AppConstants.register.getUser().getNickName()), new ChartReceive.DataBean.ChatHistoryBean.ToUser(callUserId, null, callName), null);
                            } else {
                                chatHistoryBean = new ChartReceive.DataBean.ChatHistoryBean(null, 1, mEditText.getText().toString().trim(), new ChartReceive.DataBean.ChatHistoryBean.FromUserBean(AppConstants.register.getUser().getUserId()
                                        , AppConstants.register.getUser().getImageSrc(), AppConstants.register.getUser().getNickName()), new ChartReceive.DataBean.ChatHistoryBean.ToUser(), null);
                            }
                        } else {
                            chatHistoryBean = new ChartReceive.DataBean.ChatHistoryBean(null, 1, mEditText.getText().toString().trim(), new ChartReceive.DataBean.ChatHistoryBean.FromUserBean(AppConstants.register.getUser().getUserId()
                                    , AppConstants.register.getUser().getImageSrc(), AppConstants.register.getUser().getNickName()), new ChartReceive.DataBean.ChatHistoryBean.ToUser(), null);
                        }
                        // 向会话列表发送输入内容
                        EventBus.getDefault().post(chatHistoryBean);

                        // 发送之后 清空输入框
                        mEditText.setText("");

                        hideKeyOrGallery();
                    } else {
                        //跳转登录界面
                        Intent intent1 = new Intent(this, LoginActivity.class);
                        startActivityForResult(intent1, CyUtils.JUMP_COMMENT_QUESTCODE);
                    }
                }

                break;
            case R.id.et_emoji_input:// 输入
                if (!DeviceInfo.isLogin()) {
                    //跳转登录界面
                    Intent intent1 = new Intent(ChartballActivity.this, LoginActivity.class);
                    startActivityForResult(intent1, CyUtils.JUMP_COMMENT_QUESTCODE);
                } else {
                    ll_gallery_content.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_gallery:
                // 隐藏软键盘
                if (ChartballActivity.this.getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(ChartballActivity.this.getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                ll_gallery_content.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            hideKeyOrGallery();
            return true;
        }
        return super.onTouchEvent(event);
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
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 关闭当前activity
            EventBus.getDefault().post(new ChartReceive.DataBean.ChatHistoryBean(null, 1, "EXIT_CURRENT_ACTIVITY", new ChartReceive.DataBean.ChatHistoryBean.FromUserBean(AppConstants.register.getUser().getUserId()
                    , AppConstants.register.getUser().getImageSrc(), "EXIT_CURRENT_ACTIVITY"), new ChartReceive.DataBean.ChatHistoryBean.ToUser(), null));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 隐藏软键盘和表情
     */
    public void hideKeyOrGallery() {
        ll_gallery_content.setVisibility(View.GONE);
        // 隐藏软键盘
        if (ChartballActivity.this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(ChartballActivity.this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 本地表情描述，需和AppConstants.localIcon里的资源文件保持一致
     *
     * @return
     */
    private String[] getLocalName() {
        String[] localIconName = {
                mContext.getResources().getString(R.string.local_juyijing),
                mContext.getResources().getString(R.string.local_shihuale),
                mContext.getResources().getString(R.string.local_zailaiyijiao),
                mContext.getResources().getString(R.string.local_mengkuanle),
                mContext.getResources().getString(R.string.local_yayajing),
                mContext.getResources().getString(R.string.local_dacaipan),
                mContext.getResources().getString(R.string.local_jinggaoni),
                mContext.getResources().getString(R.string.local_sesese),
                mContext.getResources().getString(R.string.local_qiudatui),
                mContext.getResources().getString(R.string.local_youqingguanlaoye),
                mContext.getResources().getString(R.string.local_fubaila),
                mContext.getResources().getString(R.string.local_shoumila),
                mContext.getResources().getString(R.string.local_touzhele),
                mContext.getResources().getString(R.string.local_shangtiantai),
                mContext.getResources().getString(R.string.local_chitule),
                mContext.getResources().getString(R.string.local_xihushuiya)
        };
        return localIconName;
    }
}

