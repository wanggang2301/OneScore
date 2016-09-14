package com.hhly.mlottery.frame;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.FootballMatchDetailActivityTest;
import com.hhly.mlottery.bean.FirstEvent;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.RongYunUtils;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 描  述：融云会话界面
 * 作  者：tangrr@13322.com
 * 时  间：2016/8/5
 */
public class ConversationFragment extends FragmentActivity implements View.OnClickListener, RongIM.UserInfoProvider {

    private UserInfo mUserInfo;// 当前用户信息
    private Context mContext;
    private LinearLayout ll_comment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        setContentView(R.layout.conversationlist);
        mContext = this;
        Toast.makeText(mContext, mContext.getResources().getString(R.string.welcome_chart_room), Toast.LENGTH_SHORT).show();
        L.d("xxx","APPKEY_RONGYUN: " + AppConstants.APPKEY_RONGYUN);
        L.d("xxx","APPSECRET_RONGYUN: " + AppConstants.APPSECRET_RONGYUN);
        ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
        // 设置聊天室背景色为透明
//        findViewById(R.id.conversation).setBackgroundColor(getResources().getColor(R.color.chart_room_bg_color));
        final View conversation = findViewById(R.id.conversation);
        int size = PreferenceUtil.getInt(RongYunUtils.CHART_ROOM_TRANSPARENT_SIZE, -1);
        if (size == -1) {
            size = 204;
        } else {
            size = (int) (size * 2.55);
        }
        String s = Integer.toHexString(size);// 80%
        if (s.length() <= 1) {
            s = "0" + s;
        }
        conversation.setBackgroundColor(Color.parseColor("#" + s + "000000"));
        final LinearLayout ll_setting = (LinearLayout) findViewById(R.id.ll_setting);
        ll_setting.setBackgroundColor(Color.parseColor("#" + s + "000000"));
        final TextView tv_pb_size = (TextView) findViewById(R.id.tv_pb_size);
        tv_pb_size.setText(mContext.getResources().getString(R.string.rong_chart_bg_pb) + "("+PreferenceUtil.getInt(RongYunUtils.CHART_ROOM_TRANSPARENT_SIZE,80)+"):");

        findViewById(R.id.ll_top).setOnClickListener(this);
        findViewById(R.id.iv_exit).setOnClickListener(this);// 关闭
        SeekBar sb_adjust = (SeekBar) findViewById(R.id.sb_adjust);
        sb_adjust.setProgress(PreferenceUtil.getInt(RongYunUtils.CHART_ROOM_TRANSPARENT_SIZE,80));
        sb_adjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                L.d("xxx", "当前进度：" + progress);
                String str = Integer.toHexString((int) (progress * 2.55));
                L.d("xxx", "转换后：：：：" + (int) (progress * 2.55));
                L.d("xxx", "十六进制：str:::" + str);
                if (str.length() <= 1) {
                    str = "0" + str;
                }
                conversation.setBackgroundColor(Color.parseColor("#" + str + "000000"));
                ll_setting.setBackgroundColor(Color.parseColor("#" + str + "000000"));
                tv_pb_size.setText(mContext.getResources().getString(R.string.rong_chart_bg_pb) + "(" + progress + "):");
                PreferenceUtil.commitInt(RongYunUtils.CHART_ROOM_TRANSPARENT_SIZE,progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.overridePendingTransition(R.anim.slide_in_from_bottom, 0);// 开启动画

        // 状态栏设为透明后软键盘监听失效，这里重新加载软键盘监听
        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);
                int screenHeight = decorView.getRootView().getHeight();
                int heightDifference = screenHeight - r.bottom;
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_comment.getLayoutParams();
                params.setMargins(0, 0, 0, heightDifference);
                ll_comment.requestLayout();
            }
        });

        mUserInfo = new UserInfo(AppConstants.register.getData().getUser().getUserId(), AppConstants.register.getData().getUser().getNickName(), Uri.parse(PreferenceUtil.getString(AppConstants.HEADICON, "xxx")));

        RongIM.setUserInfoProvider(this, true);
        RongIM.getInstance().setCurrentUserInfo(mUserInfo);
        RongIM.getInstance().setMessageAttachedUserInfo(true);

    }

    @Override
    public void finish() {
        L.d("xxx", "关闭聊天界面!!!!");
        super.finish();
        RongYunUtils.quitChatRoom();// 退出聊天室
        RongYunUtils.isJoinChartRoom = false;
        this.overridePendingTransition(0, R.anim.slide_in_from_top);// 关闭动画
        EventBus.getDefault().post(new FirstEvent(RongYunUtils.CHART_ROOM_EXIT));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_top:

                break;
            case R.id.iv_exit:// 关闭按钮
                this.finish();
                break;
        }
    }

    /**
     * 返回用户信息
     *
     * @param s
     * @return
     */
    @Override
    public UserInfo getUserInfo(String s) {
        return mUserInfo;
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.d("xxx","聊天界面显示...");
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("ConversationFragment");
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.d("xxx","聊天界面隐藏...");
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("ConversationFragment");
    }
}
