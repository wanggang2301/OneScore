package com.hhly.mlottery.util;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.bean.RongTokenBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.receiver.RongMessageReceive;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;


/**
 * @author hhly204
 * @ClassName: RongYunUtils
 * @Description: 融云工具类
 * @date 2016.7.20
 */
public class RongYunUtils {
    public static final String TAG = "RongYunUtils";
    //测试appkey和appsecret
    public static final String APPKEY = "3argexb6r6c1e";
    public static final String APPSECRET = "l0Fu9Kn0uqO";
    public static final String CHART_ROOM_ID = "chartRoomId";// 聊天室id  key
    public static final String CHART_ROOM_NAME = "chartBall";// 聊天室名称
    public static final String USER_TOKEN = "userToken";// 用户Token key

    /**
     * 进入聊天室
     *
     * @param roomId
     */
    public static void intoChartRoom(Context mContext,String roomId) {
        if (TextUtils.isEmpty(roomId)) {
            return;
        }
        String mToken = PreferenceUtil.getString(USER_TOKEN, "");// 获取本地用户Token
        if (TextUtils.isEmpty(mToken)) {
            postToken(mContext,roomId);// 获取用户Token
        } else {
            connect(mContext,mToken,roomId);
        }
    }

    /**
     * 创建聊天室
     */
    private static void createChatRoom(final Context mContext, final String roomId) {
        String mRoomId = PreferenceUtil.getString(CHART_ROOM_ID, "");// 获取本地聊天室id
        // 判断有没有创建
        if (!TextUtils.isEmpty(mRoomId) && roomId.equals(mRoomId)) {
            // 有--直接进入聊天室
            RongIM.getInstance().startConversation(MyApp.getContext(), Conversation.ConversationType.CHATROOM, CHART_ROOM_ID, null);
        } else {
            // 没有——创建
            Map<String, String> map = new HashMap<>();
            map.put("chatroom[" + CHART_ROOM_ID + "]", CHART_ROOM_NAME);// 传入聊天室id 和 名称
            VolleyContentFast.requestRongYun(BaseURLs.RONG_CHARTROOM_ID, map, new VolleyContentFast.ResponseSuccessListener<RongTokenBean>() {
                @Override
                public void onResponse(RongTokenBean jsonObject) {
                    System.out.println(TAG + "创建聊天室成功getCode==" + jsonObject.getCode());
                    if (jsonObject.getCode() == 200) {
                        // 进入聊天室
                        RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.CHATROOM, CHART_ROOM_ID, null);
                        PreferenceUtil.commitString("CHART_ROOM_ID", roomId);
                    }
                }
            }, new VolleyContentFast.ResponseErrorListener() {
                @Override
                public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                    System.err.println(TAG + "创建聊天室失败getCode==error" + JSON.toJSONString(exception));
                }
            }, RongTokenBean.class);
        }
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private static void connect(final Context mContext, String token, final String roomId) {
        if (mContext.getApplicationInfo().packageName.equals(MyApp.getCurProcessName(mContext.getApplicationContext()))) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    System.out.println(TAG + "--onTokenIncorrect");
                }

                @Override
                public void onSuccess(String s) {
                    System.out.println(TAG + "连接融云成功--onSuccess  s:" + s);
                    RongIM.setOnReceiveMessageListener(new RongMessageReceive());// 消息接收监听
                    createChatRoom(mContext,roomId);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    System.out.println(TAG + "连接融云失败--onError  ErrorCode:" + errorCode);
                }
            });
        }
    }

    /**
     * 从融云服务器 获取用户token
     */
    private static void postToken(final Context mContext, final String roomId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", AppConstants.register.getData().getUser().getUserId());
        map.put("name", AppConstants.register.getData().getUser().getNickName());
        map.put("portraitUri", "xxx");// 头像暂时无
        VolleyContentFast.requestRongYun(BaseURLs.RONG_USER_TOKEN, map, new VolleyContentFast.ResponseSuccessListener<RongTokenBean>() {
            @Override
            public void onResponse(RongTokenBean jsonObject) {
                System.out.println(TAG + "token==" + jsonObject.getToken());
                if (jsonObject == null || jsonObject.getToken() == null) {
                   // 获取Token失败
                }else{
                    connect(mContext,jsonObject.getToken(),roomId);
                    PreferenceUtil.commitString(USER_TOKEN,jsonObject.getToken());
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                System.err.println(TAG + "token==error" + JSON.toJSONString(exception));
            }
        }, RongTokenBean.class);
    }

    public static String sha1(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(data.getBytes());
        StringBuffer buf = new StringBuffer();
        byte[] bits = md.digest();
        for (int i = 0; i < bits.length; i++) {
            int a = bits[i];
            if (a < 0) a += 256;
            if (a < 16) buf.append("0");
            buf.append(Integer.toHexString(a));
        }
        return buf.toString();
    }
}
