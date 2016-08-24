package com.hhly.mlottery.receiver;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * 描  述：融云聊天室会话监听
 * 作  者：tangrr@13322.com
 * 时  间：2016/8/5
 */
public class RongMessageReceive implements RongIMClient.OnReceiveMessageListener{
    /**
     * 收到消息的处理。
     * @param message 收到的消息实体。
     * @param i  剩余未拉取消息数目。
     * @return
     */
    @Override
    public boolean onReceived(Message message, int i) {
        //开发者根据自己需求自行处理
        return false;
    }
}
