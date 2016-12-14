package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.chartBallAdapter.ChartBallAdapter;
import com.hhly.mlottery.bean.BarrageBean;
import com.hhly.mlottery.bean.chart.ChartReceive;
import com.hhly.mlottery.bean.chart.SendMessageBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.chartBallFragment.EmojiFragment;
import com.hhly.mlottery.frame.chartBallFragment.LocalFragment;
import com.hhly.mlottery.adapter.football.BasePagerAdapter;
import com.hhly.mlottery.frame.footframe.eventbus.ChartBallContentEntitiy;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;
import io.github.rockerhieu.emojicon.EmojiconEditText;


/**
 * desc:聊球输入框
 * data:2016-12-08 bay:tangrr107
 */
public class ChartballActivity extends BaseActivity implements View.OnClickListener {


    public static String TAG = "ChartballActivity";
    public EmojiconEditText mEditText;//输入评论
    private TextView mSend;//发送评论
    private LinearLayout ll_gallery_content;
    private ImageView iv_gallery;
    private InputMethodManager inputMethodManager;
    private RelativeLayout rl_local;
    private RelativeLayout rl_emoji;
    private ViewPager view_pager_local;
    private ViewPager view_pager_emoji;
    List<Fragment> localFragments = new ArrayList<>();
    List<Fragment> emojiFragments = new ArrayList<>();

    private final static int LOCAL_PAGER_SIZE = 10;
    private final static int EMOJI_PAGER_SIZE = 26;
    private LinearLayout local_point;
    private LinearLayout emoji_point;
    private int localPagerSize;
    private int emojiPagerSize;

    private LinearLayout ll_local_content;
    private LinearLayout ll_emoji_content;
    private final static String MATCH_THIRD_ID = "thirdId";
    private String mThirdId;
    private String callName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartball_layout);
        /**当前评论小窗口不统计*/
        MobclickAgent.openActivityDurationTrack(false);

        // Make us non-modal, so that others can receive touch events.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        // ...but notify us that it happened.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        setContentView(R.layout.activity_chartball_layout);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        mThirdId= bundle.getString(MATCH_THIRD_ID);
        Log.i("sdasd","mThirdId="+mThirdId);

        initWindow();
        initView();
        initData();
        initEvent();
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

        view_pager_emoji.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < emojiPagerSize; i++) {
                    emoji_point.getChildAt(i).setEnabled(i == position % emojiPagerSize ? true : false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initData() {
        if (getIntent() != null) {
            callName = getIntent().getStringExtra("CALL_NAME");
            if (!TextUtils.isEmpty(callName)) {
                mEditText.setText(Html.fromHtml("<font color='#0090ff'>" + callName + "</font>"));
                mEditText.setSelection(mEditText.getText().length());
                mSend.setSelected(true);
            }
        }
        // 添加数据
        BasePagerAdapter localPagerAdapter = new BasePagerAdapter(getSupportFragmentManager());
        localPagerSize = AppConstants.localIcon.length % LOCAL_PAGER_SIZE == 0 ? AppConstants.localIcon.length / LOCAL_PAGER_SIZE : AppConstants.localIcon.length / LOCAL_PAGER_SIZE + 1;
        for (int i = 0; i < localPagerSize; i++) {
            ArrayList<Integer> list = new ArrayList<>();
            int startIndex = i * LOCAL_PAGER_SIZE;
            int endIndex = (i + 1) * LOCAL_PAGER_SIZE <= AppConstants.localIcon.length ? (i + 1) * LOCAL_PAGER_SIZE : AppConstants.localIcon.length;
            for (int j = startIndex; j < endIndex; j++) {
                list.add(AppConstants.localIcon[j]);
            }
            localFragments.add(LocalFragment.newInstance(list));
        }
        localPagerAdapter.addFragments(localFragments);
        view_pager_local.setAdapter(localPagerAdapter);

        BasePagerAdapter emojiPagerAdapter = new BasePagerAdapter(getSupportFragmentManager());
        emojiPagerSize = AppConstants.emojiList.length % EMOJI_PAGER_SIZE == 0 ? AppConstants.emojiList.length / EMOJI_PAGER_SIZE : AppConstants.emojiList.length / EMOJI_PAGER_SIZE + 1;
        for (int i = 0; i < emojiPagerSize; i++) {
            ArrayList<String> emojilist = new ArrayList<>();
            int startIndex = i * EMOJI_PAGER_SIZE;
            int endIndex = (i + 1) * EMOJI_PAGER_SIZE <= AppConstants.emojiList.length ? (i + 1) * EMOJI_PAGER_SIZE : AppConstants.emojiList.length;
            for (int j = startIndex; j < endIndex; j++) {
                emojilist.add(AppConstants.emojiList[j]);
            }
            emojiFragments.add(EmojiFragment.newInstance(emojilist));
        }
        emojiPagerAdapter.addFragments(emojiFragments);
        view_pager_emoji.setAdapter(emojiPagerAdapter);

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
        emoji_point.removeAllViews();
        if (emojiPagerSize >= 2) {
            int dp = DisplayUtil.dip2px(mContext, 5);// 添加小圆点
            for (int i = 0; i < emojiPagerSize; i++) {
                View view = new View(mContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp, dp);
                if (i != 0) {
                    lp.leftMargin = dp;
                }
                view.setEnabled(i == 0);
                view.setBackgroundResource(R.drawable.v_lunbo_point_selector);
                view.setLayoutParams(lp);
                emoji_point.addView(view);
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
        findViewById(R.id.iv_select_icon).setOnClickListener(this);
        findViewById(R.id.iv_select_icon).setSelected(true);
        findViewById(R.id.iv_select_emoji).setOnClickListener(this);
        ll_local_content = (LinearLayout) findViewById(R.id.ll_local_content);
        ll_emoji_content = (LinearLayout) findViewById(R.id.ll_emoji_content);
        rl_local = (RelativeLayout) findViewById(R.id.rl_local);
        rl_emoji = (RelativeLayout) findViewById(R.id.rl_emoji);

        view_pager_local = (ViewPager) findViewById(R.id.view_pager_local);
        view_pager_emoji = (ViewPager) findViewById(R.id.view_pager_emoji);
        local_point = (LinearLayout) findViewById(R.id.local_point);
        emoji_point = (LinearLayout) findViewById(R.id.emoji_point);
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

                        sendMessage( mEditText.getText().toString(),null);
                        // TODO 向会话列表发送输入内容
                        EventBus.getDefault().post(new ChartReceive.DataBean.ChatHistoryBean(mEditText.getText().toString(),new ChartReceive.DataBean.ChatHistoryBean.FromUserBean(AppConstants.register.getData().getUser().getUserId()
                                ,AppConstants.register.getData().getUser().getHeadIcon(),AppConstants.register.getData().getUser().getNickName())));

                        EventBus.getDefault().post(new BarrageBean("http://pic.13322.com/icons/avatar/male/1.png", mEditText.getText().toString()));

                        // 发送之后 清空输入框
                        mEditText.setText("");

                        // 隐藏软键盘
                        if (ChartballActivity.this.getCurrentFocus() != null) {
                            inputMethodManager.hideSoftInputFromWindow(ChartballActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        finish();

                    } else {
                        //跳转登录界面
                        Intent intent1 = new Intent(this, LoginActivity.class);
                        startActivityForResult(intent1, CyUtils.JUMP_COMMENT_QUESTCODE);
                    }
                }

                break;
            case R.id.et_emoji_input:// 输入
                if (!CommonUtils.isLogin()) {
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

            case R.id.iv_select_icon:// 显示自定义表情
            case R.id.ll_local_content:
                rl_local.setVisibility(View.VISIBLE);
                rl_emoji.setVisibility(View.GONE);
                ll_local_content.setBackgroundResource(R.color.white);
                ll_emoji_content.setBackgroundResource(R.color.home_item_bg);
                break;
            case R.id.iv_select_emoji:// 显示emoji表情
            case R.id.ll_emoji_content:
                rl_local.setVisibility(View.GONE);
                rl_emoji.setVisibility(View.VISIBLE);
                ll_local_content.setBackgroundResource(R.color.home_item_bg);
                ll_emoji_content.setBackgroundResource(R.color.white);
                break;
        }

    }
   //发送消息后台
    private void sendMessage(String message ,String toUserId) {



        Map<String, String> params = new HashMap<>();
        params.put("sourceName", "android");
        params.put("chatType", "football");
        params.put("thirdId", mThirdId);
        params.put("msgCode", "1");//1普通消息 2@消息 3系统消息 4在线人数 5进入聊天室
        params.put("loginToken", AppConstants.register.getData().getLoginToken());
        params.put("toUserId", "");
        params.put("deviceId", AppConstants.deviceToken);
        params.put("message ", message);
        params.put("msgId", UUID.randomUUID().toString());
        Log.i("zczxc","thirdId="+mThirdId);
        Log.i("zczxc","loginToken="+ AppConstants.register.getData().getLoginToken());
        Log.i("zczxc","deviceId="+AppConstants.deviceToken);
        Log.i("zczxc","msgId="+ UUID.randomUUID().toString());
        VolleyContentFast.requestJsonByGet(BaseURLs.MESSAGE_LIST, params,
                new VolleyContentFast.ResponseSuccessListener<SendMessageBean>() {
                    @Override
                    public void onResponse(SendMessageBean receive) {
                        if (!receive.getResult().equals("200")) {
                            Log.i(TAG,"消息发送失败");
                            return;
                        }


                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                        Log.i(TAG,"消息发送失败");
                    }
                }, SendMessageBean.class
        );




    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            finish();
            // 隐藏软键盘
            if (ChartballActivity.this.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(ChartballActivity.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return true;
        }
        return super.onTouchEvent(event);
    }
}

