package com.hhly.mlottery.activity;

import android.os.Bundle;

import com.hhly.mlottery.R;

public class SnookerMatchDetail extends BaseWebSocketActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooker_match_detail);
    }




    @Override
    protected void onTextResult(String text) {

    }

    @Override
    protected void onConnectFail() {

    }

    @Override
    protected void onDisconnected() {

    }

    @Override
    protected void onConnected() {

    }
}
