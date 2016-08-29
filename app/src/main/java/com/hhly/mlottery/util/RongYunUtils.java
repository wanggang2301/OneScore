package com.hhly.mlottery.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.bean.FirstEvent;
import com.hhly.mlottery.bean.RongChatRoomInfoBean;
import com.hhly.mlottery.bean.RongTokenBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.receiver.RongMessageReceive;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
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
    public static final int CHART_ROOM_QUESTCODE_FOOT = 5;// 足球跳转登录界面的请求码
    public static final int CHART_ROOM_QUESTCODE_BASKET = 6;// 篮球跳转登录界面的请求码

    public static final String CHART_ROOM_ID = "chartRoomId";// 聊天室id  key
    public static final String CHART_ROOM_NAME = "chartBall";// 聊天室名称
    public static final String USER_TOKEN = "userToken";// 用户Token key
    public static final String USER_ID = "userId";// 用户id
    public static final String CHART_ROOM_EXIT = "chartRoomExit";// 关闭传话界面
    public static final String RONG_CONNENT_OK = "RongConnentOK";// 连接融云服务器
    public static final String CREATE_CHARTROOM_OK = "CreateChartRoomOK";// 创建聊天室
    public static final String CHART_ROOM_COUNT_OK = "chartRoomCountOK";// 获取到聊天室人数
    public static final String CHART_ROOM_TRANSPARENT_SIZE = "chartRoomTransparentSize";// 调节聊天界面透明度值

    public static boolean isRongConnent = false;// 连接融云服务器状态
    public static boolean isCreateChartRoom = false;// 创建聊天室状态
    public static boolean isJoinChartRoom = false;// 进入聊天室状态

    public static int chartRoomCount;// 聊天室在线人数

    /**
     * 初始化融云服务器连接
     *
     * @param mContext
     */
    public static void initRongIMConnect(Context mContext) {
        String mToken = PreferenceUtil.getString(USER_TOKEN, "");// 获取本地用户Token
        String userid = PreferenceUtil.getString(USER_ID, "");// 获取本地用户id
        String currentUserid = AppConstants.register.getData().getUser().getUserId();// 获取当前登录的用户id
        L.d(TAG, "userid: " + userid);
        L.d(TAG, "currentUserid: " + currentUserid);
        if (TextUtils.isEmpty(userid) || !currentUserid.equals(userid) || TextUtils.isEmpty(mToken)) {
            postToken(mContext, currentUserid);// 获取用户Token
        } else {
            connect(mContext, mToken);
        }
    }

    /**
     * 获取当前聊天室在线人数
     */
    public static void getChatRoomCount(String chatRoomId){
        Map<String, String> map = new HashMap<>();
        map.put("chatroomId",chatRoomId);
        map.put("count","500");
        map.put("order","1");

        VolleyContentFast.requestRongYun(BaseURLs.RONG_CHARTROOM_COUNT, map, new VolleyContentFast.ResponseSuccessListener<RongChatRoomInfoBean>() {
            @Override
            public void onResponse(RongChatRoomInfoBean jsonObject) {
                System.out.println(TAG + "获取当前聊天室在线人数成功getCode==" + jsonObject.getCode());
                if (jsonObject.getCode() == 200) {
                    chartRoomCount = jsonObject.getTotal();
                    EventBus.getDefault().post(new FirstEvent(CHART_ROOM_COUNT_OK));
                    System.out.println(TAG + "当前聊天室人数为：==" + jsonObject.getTotal());
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                System.err.println(TAG + "获取当前聊天室在线人数失败getCode==error" + JSON.toJSONString(exception));
            }
        }, RongChatRoomInfoBean.class);
    }

    /**
     * 创建聊天室
     */
    public static void createChatRoom(final String roomId) {
        if (TextUtils.isEmpty(roomId)) {
            return;
        }
        String mRoomId = PreferenceUtil.getString(CHART_ROOM_ID, "");// 获取本地聊天室id
        // 判断有没有创建
        if (TextUtils.isEmpty(mRoomId) || !roomId.equals(mRoomId)) {
            // 没有——创建
            Map<String, String> map = new HashMap<>();
            map.put("chatroom[" + CHART_ROOM_ID + "]", CHART_ROOM_NAME);// 传入聊天室id 和 名称
            VolleyContentFast.requestRongYun(BaseURLs.RONG_CHARTROOM_ID, map, new VolleyContentFast.ResponseSuccessListener<RongTokenBean>() {
                @Override
                public void onResponse(RongTokenBean jsonObject) {
                    System.out.println(TAG + "创建聊天室成功getCode==" + jsonObject.getCode());
                    if (jsonObject.getCode() == 200) {
                        isCreateChartRoom = true;
                        EventBus.getDefault().post(new FirstEvent(CREATE_CHARTROOM_OK));
//                        getChatRoomCount(roomId);
                        PreferenceUtil.commitString(CHART_ROOM_ID, roomId);
                        L.d("xxx", "保存聊天室id：" + PreferenceUtil.getString(CHART_ROOM_ID, "xxx"));
                    }
                }
            }, new VolleyContentFast.ResponseErrorListener() {
                @Override
                public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                    System.err.println(TAG + "创建聊天室失败getCode==error" + JSON.toJSONString(exception));
                }
            }, RongTokenBean.class);
        }else{
            isCreateChartRoom = true;
            EventBus.getDefault().post(new FirstEvent(CREATE_CHARTROOM_OK));
//            getChatRoomCount(roomId);
        }
    }

    /**
     * 进入聊天室
     *
     * @param mContext 上下文
     */
    public static void joinChatRoom(Context mContext, String roomId) {
        if (TextUtils.isEmpty(roomId)) {
            return;
        }
        Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().processName).buildUpon().appendPath("conversation").appendPath(Conversation.ConversationType.CHATROOM.getName().toLowerCase()).appendQueryParameter("targetId", roomId).appendQueryParameter("title", "").build();
        Intent intent = new Intent("android.intent.action.VIEW", uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
//        RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.CHATROOM, roomId, null);
        L.d("xxx", "joinChatRoom...");
    }

    /**
     * 退出聊天室
     */
    public static void quitChatRoom() {
        RongIM.getInstance().quitChatRoom(PreferenceUtil.getString(CHART_ROOM_ID, ""), new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                L.d("xxx", "exit chartRoom Ok");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                L.d("xxx", "exit chartRoom Error:" + errorCode);
            }
        });
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private static void connect(final Context mContext, String token) {
        L.d(TAG + "token: " + token);
        if (mContext.getApplicationInfo().packageName.equals(MyApp.getCurProcessName(mContext.getApplicationContext()))) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    System.out.println(TAG + "--onTokenIncorrect");
                }

                @Override
                public void onSuccess(String s) {
                    isRongConnent = true;
                    EventBus.getDefault().post(new FirstEvent(RONG_CONNENT_OK));
                    System.out.println(TAG + "连接融云成功--onSuccess  s:" + s);
                    RongIM.setOnReceiveMessageListener(new RongMessageReceive());// 消息接收监听
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
    private static void postToken(final Context mContext, final String currentUserid) {
        String userTestPhoto = "http://m.1332255.com/news/upload/shortcut/3daaffb1bf5e427b84313de5bd9181bd.png";// 用户使用测试头像
        Map<String, String> map = new HashMap<>();
        map.put("userId", AppConstants.register.getData().getUser().getUserId());
        map.put("name", AppConstants.register.getData().getUser().getNickName());
        map.put("portraitUri", userTestPhoto);// 头像
        L.d("xxx", "userId: " + AppConstants.register.getData().getUser().getUserId());
        L.d("xxx", "name: " + AppConstants.register.getData().getUser().getNickName());
        VolleyContentFast.requestRongYun(BaseURLs.RONG_USER_TOKEN, map, new VolleyContentFast.ResponseSuccessListener<RongTokenBean>() {
            @Override
            public void onResponse(RongTokenBean jsonObject) {
                System.out.println(TAG + "token==" + jsonObject.getToken());
                if (jsonObject == null || jsonObject.getToken() == null) {
                    // 获取Token失败
                } else {
                    connect(mContext, jsonObject.getToken());
                    PreferenceUtil.commitString(USER_TOKEN, jsonObject.getToken());
                    PreferenceUtil.commitString(USER_ID, currentUserid);
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
