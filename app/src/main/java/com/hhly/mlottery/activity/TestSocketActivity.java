package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.L;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestSocketActivity extends BaseWebSocketActivity {


    private static final String TAG = "zxcvbntestsocket";

    @BindView(R.id.start_socket)
    Button startSocket;
    @BindView(R.id.close_socket)
    Button closeSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         *  basketURL===ws://m.13322.com/ws
         *  USER.topic.basketball.score.4053077.zh  3846287
         *
         */


        setWebSocketUri("ws://m.13322.com/ws");
        setTopic("USER.topic.basketball.score.3846357.zh");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_socket);
        ButterKnife.bind(this);

    }

    @Override
    protected void onTextResult(String text) {
        L.d(TAG, "___________onConnected");
        L.d(TAG, "=========" + text);

    }

    @Override
    protected void onConnectFail() {
        L.d(TAG, "___________onConnectFail");

    }

    @Override
    protected void onDisconnected() {
        L.d(TAG, "___________onDisconnected");

    }

    @Override
    protected void onConnected() {

        L.d(TAG, "___________onConnected");
    }


    @OnClick({R.id.start_socket, R.id.close_socket})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_socket:
                L.d(TAG, "___________start_socket");

                connectWebSocket();

                break;
            case R.id.close_socket:
                L.d(TAG, "___________close_socket");

                closeWebSocket();
                break;
        }
    }
}
