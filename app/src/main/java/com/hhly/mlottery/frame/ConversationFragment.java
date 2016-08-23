package com.hhly.mlottery.frame;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.FirstEvent;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.RongYunUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Random;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.TextInputProvider;
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
        ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
        // 设置聊天室背景色为透明
        findViewById(R.id.conversation).setBackgroundColor(getResources().getColor(R.color.chart_room_bg_color));

        findViewById(R.id.ll_top).setOnClickListener(this);

        this.overridePendingTransition(R.anim.slide_in_from_bottom,0);// 开启动画

        // 状态栏设为透明后软键盘监听失效，这里重新加载软键盘监听
        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);
                int screenHeight = decorView.getRootView().getHeight();
                int heightDifference = screenHeight - r.bottom;
                LinearLayout.LayoutParams params = ( LinearLayout.LayoutParams) ll_comment.getLayoutParams();
                params.setMargins(0,0,0,heightDifference);
                ll_comment.requestLayout();
            }
        });


//        String userTestPhoto = "http://m.1332255.com/news/upload/shortcut/69c426f0f0974d4b8aae0826da71f751.png";// 用户使用测试头像
        String userTestPhoto = AppConstants.register.getData().getUser().getHeadIcon();// 用户头像
        mUserInfo = new UserInfo(AppConstants.register.getData().getUser().getUserId(), AppConstants.register.getData().getUser().getNickName(), Uri.parse(userTestPhoto));

        RongIM.setUserInfoProvider(this, true);
        RongIM.getInstance().setCurrentUserInfo(mUserInfo);
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        RongIM.getInstance().refreshUserInfoCache(mUserInfo);// 刷新本地用户缓存
    }

    @Override
    public void finish() {
        L.d("xxx", "关闭聊天界面!!!!");
        super.finish();
        RongYunUtils.quitChatRoom();// 退出聊天室
        RongYunUtils.isJoinChartRoom = false;
        this.overridePendingTransition(0,R.anim.slide_in_from_top);// 关闭动画
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_top:
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
}
