package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketState;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 描    述：提供WebSocket功能的Activity基类
 * 作    者：chenml@13322.com
 * 时    间：2016/9/12
 */
public abstract class BaseWebSocketActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();


    /**
     * 默认websocket的uri
     */
//    private final static String DEFUAL_WEBSOCKET_URI = "ws://192.168.10.239:61634";
    private final static String DEFUAL_WEBSOCKET_URI = "ws://183.61.172.89:61634";
    /**
     * 默认websocket的服务器用户名
     */
    private final static String DEFUAL_SERVER_NAME = "happywin";
    /**
     * 默认websocket的服务器密码
     */
    private final static String DEFUAL_SERVER_PASSWORD = "happywin";
    /**
     * 默认websocket的主题
     */
    private final static String DEFUAL_TOPIC = "INFO_BANNER.topic.game.yxjc";
    /**
     * 默认websocket连接超时时间
     */
    private final static int DEFUAL_TIMEOUT = 60 * 1000;


    private String mWebSocketUri = DEFUAL_WEBSOCKET_URI;
    private String mServerName = DEFUAL_SERVER_NAME;
    private String mServerPassword = DEFUAL_SERVER_PASSWORD;
    private String mTopic = DEFUAL_TOPIC;

    private WebSocket ws;

//    private boolean isDestroy = false;

    private final static int DEFUAL_RETRY_CONNECT = 3;

    private int retryConnect = 0;


    /**
     * 修改websocket的uri
     *
     * @param webSocketUri
     */
    protected void setWebSocketUri(String webSocketUri) {
        this.mWebSocketUri = webSocketUri;
    }

    /**
     * 修改websocket的服务器用户名
     *
     * @param serverName
     */
    protected void setServerName(String serverName) {
        this.mServerName = serverName;
    }

    /**
     * 修改websocket的服务器密码
     *
     * @param serverPassword
     */
    protected void setServerPassword(String serverPassword) {
        this.mServerPassword = serverPassword;
    }

    /**
     * 修改websocket的主题
     *
     * @param topic
     */
    protected void setTopic(String topic) {
        this.mTopic = topic;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        L.d(TAG, "__onCreate__");
        WebSocketFactory factory = new WebSocketFactory();

        try {
            ws = factory.createSocket(mWebSocketUri, DEFUAL_TIMEOUT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ws.addListener(new BaseWebSocketActivity.MyWebSocketAdapter());

        L.d(TAG, "WebSocket State = " + ws.getState());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.d(TAG, "__onDestroy__");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ws != null) {
                    synchronized (this) {
                        ws.disconnect();
                    }
                    ws = null;
                }
            }
        }).start();
        MyApp.getRefWatcher().watch(this);
    }

    public WebSocketState getSocketState() {
        return ws.getState();
    }


    /**
     * 不公开的重连方法，用于本类调用。
     */
    private void connect() {
        try {
            if (ws != null) {
                synchronized (this) {
                    if (ws != null) {
                        if (ws.getState().equals(WebSocketState.CREATED)) {
                            ws.connect();
                        } else if (ws.getState().equals(WebSocketState.CLOSED)) {
                            ws = ws.recreate().connect();
                        }
                    }
                }
            }
        } catch (WebSocketException | IOException e) {
            onConnectFail();
        }
    }

    /**
     * 子类可调用的重连方法
     */
    protected void connectWebSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        }).start();
    }

    /**
     * 子类可调用的关闭方法
     */
    protected void closeWebSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ws != null) {
                    synchronized (this) {
                        if (ws != null) {
                            ws.disconnect();
                        }
                    }
                }
            }
        }).start();
    }


    protected abstract void onTextResult(String text);

    protected abstract void onConnectFail();

    protected abstract void onDisconnected();

    protected abstract void onConnected();


    public class MyWebSocketAdapter extends WebSocketAdapter {
        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            super.onConnected(websocket, headers);
            L.d(TAG, "onConnected");
            StringBuilder builder = new StringBuilder();
            builder.append("CONNECT\nlogin:")
                    .append(mServerName)
                    .append("\npasscode:")
                    .append(mServerPassword)
                    .append("\naccept-version:1.1,1.0\nheart-beat:5000,5000\n")
                    .append("\n");
            websocket.sendText(builder.toString());
            BaseWebSocketActivity.this.onConnected();
        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            super.onTextMessage(websocket, text);
            L.d(TAG, "onTextMessage");
            L.d(TAG, "websocket = " + websocket.toString());
            L.d(TAG, "text = " + text);
            if (text.startsWith("CONNECTED")) {
                StringBuilder builder = new StringBuilder();
                builder.append("SUBSCRIBE\nid:");
                String id = "android" + DeviceInfo.getDeviceId(getApplicationContext());
                builder.append(MD5Util.getMD5(id));
                builder.append("\ndestination:/topic/");
                builder.append(mTopic);
                builder.append("\n\n");
//                websocket.sendText("SUBSCRIBE\nid:" +  + "\ndestination:/topic/INFO_BANNER.topic.game.yxjc\n\n");
                websocket.sendText(builder.toString());

                return;
            } else if (text.startsWith("MESSAGE")) {

                String[] msgs = text.split("\n");
                String json = msgs[msgs.length - 1];
                json = json.substring(0, json.length() - 1);
                onTextResult(json);

            }
            websocket.sendText("\n");
        }

        @Override
        public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {
            super.onStateChanged(websocket, newState);
            L.d(TAG, "onStateChanged");
            L.d(TAG, "newState = " + newState);
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
            super.onError(websocket, cause);
            L.d(TAG, "onError");
            L.d(TAG, "cause" + cause.getMessage());
        }

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
            super.onConnectError(websocket, exception);
            L.d(TAG, "onConnectError");

        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
            L.d(TAG, "onDisconnected");
            BaseWebSocketActivity.this.onDisconnected();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
