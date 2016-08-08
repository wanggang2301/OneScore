package com.hhly.mlottery.frame;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.hhly.mlottery.R;

/**
 * 描  述：融云会话界面
 * 作  者：tangrr@13322.com
 * 时  间：2016/8/5
 */
public class ConversationFragment extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversationlist);
        // 设置聊天室背景图
//        findViewById(R.id.conversation).setBackgroundResource(R.mipmap.back);
    }
}
