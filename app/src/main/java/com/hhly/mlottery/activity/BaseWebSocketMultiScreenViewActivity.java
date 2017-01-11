package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hhly.mlottery.bean.multiscreenview.WebSocketMultiScreenViewBean;
import com.hhly.mlottery.bean.multiscreenview.WebSocketMultiScreenViewTextBean;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Wangg
 * @Name：BaseWebSocketMultiScreenViewActivity
 * @Description:多屏动画推送websocket
 * @Created on:2017/1/6  14:42.
 */

public abstract class BaseWebSocketMultiScreenViewActivity extends AppCompatActivity {

    private static final int VIEW_TYPE_FOOTBALL = 1;
    private static final int VIEW_TYPE_BASKETBALL = 2;
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
    //  private String mTopic = DEFUAL_TOPIC;

    private WebSocket ws;

//    private boolean isDestroy = false;

    private final static int DEFUAL_RETRY_CONNECT = 3;

    private int retryConnect = 0;


    /**
     * 用户所阅定的所有主题
     */
    List<WebSocketMultiScreenViewBean> allSubTopic = new ArrayList<>();

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
     */
    protected void setTopic(WebSocketMultiScreenViewBean webSocketMultiScreenViewBean) {
        //this.mTopic = topic;

        this.allSubTopic.add(webSocketMultiScreenViewBean);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebSocketFactory factory = new WebSocketFactory();

        try {
            ws = factory.createSocket(mWebSocketUri, DEFUAL_TIMEOUT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ws.addListener(new BaseWebSocketMultiScreenViewActivity.MyWebSocketAdapter());



    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ws != null) {
                    synchronized (ws) {
                        ws.disconnect();
                    }
                    ws = null;
                }
            }
        }).start();
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
                synchronized (ws) {
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
                    synchronized (ws) {
                        if (ws != null) {
                            ws.disconnect();
                        }
                    }
                }
            }
        }).start();
    }


    protected abstract void onTextResult(WebSocketMultiScreenViewTextBean text);

    protected abstract void onConnectFail();

    protected abstract void onDisconnected();

    protected abstract void onConnected();


    public class MyWebSocketAdapter extends WebSocketAdapter {
        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            super.onConnected(websocket, headers);
            StringBuilder builder = new StringBuilder();
            builder.append("CONNECT\nlogin:")
                    .append(mServerName)
                    .append("\npasscode:")
                    .append(mServerPassword)
                    .append("\naccept-version:1.1,1.0\nheart-beat:5000,5000\n")
                    .append("\n");
            websocket.sendText(builder.toString());
            BaseWebSocketMultiScreenViewActivity.this.onConnected();
        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            super.onTextMessage(websocket, text);

            if (text.startsWith("CONNECTED")) {

               /* StringBuilder builder = new StringBuilder();
                builder.append("SUBSCRIBE\nid:");
                String id = "android" + DeviceInfo.getDeviceId(getApplicationContext());
                builder.append(MD5Util.getMD5(id));
                builder.append("\ndestination:/topic/");
                builder.append(mTopic);
                builder.append("\n\n");
//                websocket.sendText("SUBSCRIBE\nid:" +  + "\ndestination:/topic/INFO_BANNER.topic.game.yxjc\n\n");
                websocket.sendText(builder.toString());*/

                subscribeAll();

                return;
            } else if (text.startsWith("MESSAGE")) {

               // Log.d("multiscreen", "" + text);
                String[] msgs = text.split("\n");
                String json = msgs[msgs.length - 1];
                json = json.substring(0, json.length() - 1);

/*
                : text = MESSAGE
                message-id:ID:localhost.localdomain-30963-1482289690687-7:25431:1:1:245
                destination:/topic/USER.topic.basketball.score.4254957.zh
                timestamp:1483692070174
                expires:0
                subscription:android-12312131321
                priority:4*/

/*


                if (text.contains("/topic/USER.topic.app")) {//这是比较粗糙的判断
                    // EventBus.getDefault().post(new FootballEvent(json));
                } else if (text.contains("/topic/USER.topic.basketball")) {
                    // EventBus.getDefault().post(new BasketEvent(json));
                }

                //setTopic("USER.topic.basketball.score." + mThirdId + ".zh");
               // setTopic("USER.topic.liveEvent." + mThirdId + "." + appendLanguage());*/


                for (WebSocketMultiScreenViewBean w : allSubTopic) {

                    if (text.contains("/topic/" + w.getTopic())) {

                        if (w.getType() == VIEW_TYPE_FOOTBALL) {
                            onTextResult(new WebSocketMultiScreenViewTextBean(VIEW_TYPE_FOOTBALL, w.getMatchId(), json));

                        } else if (w.getType() == VIEW_TYPE_BASKETBALL) {
                            onTextResult(new WebSocketMultiScreenViewTextBean(VIEW_TYPE_BASKETBALL, w.getMatchId(), json));

                        }

                    }
/*
                    if (w.getType() == VIEW_TYPE_BASKETBALL) {


                    } else if (w.getType() == VIEW_TYPE_FOOTBALL) {

                    }*/


                }


            }
            websocket.sendText("\n");
        }

        @Override
        public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {
            super.onStateChanged(websocket, newState);

        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
            super.onError(websocket, cause);

        }

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
            super.onConnectError(websocket, exception);

        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
            BaseWebSocketMultiScreenViewActivity.this.onDisconnected();
        }


    }

    void subscribeAll() {
        for (WebSocketMultiScreenViewBean webSocketMultiScreenViewBean : allSubTopic) {
            subscribeTopic(webSocketMultiScreenViewBean.getTopic());
        }

    }

    void subscribeTopic(String topic) {
        StringBuilder builder = new StringBuilder();
        builder.append("SUBSCRIBE\nid:");
        String id = "android-12312131321";
        builder.append(id);
        builder.append("\ndestination:/topic/");
        builder.append(topic);
        builder.append("\n\n");
        ws.sendText(builder.toString());
    }
}

