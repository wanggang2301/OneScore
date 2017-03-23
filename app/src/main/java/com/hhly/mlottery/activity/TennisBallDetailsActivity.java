package com.hhly.mlottery.activity;

import android.os.Bundle;

import com.hhly.mlottery.R;
import com.hhly.mlottery.config.BaseURLs;

/**
 * desc:网球内页
 * Created by 107_tangrr on 2017/3/21 0021.
 */

public class TennisBallDetailsActivity extends BaseWebSocketActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic("USER.topic.tennis.score");

//        setContentView(R.layout.tennis_details_activity);
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
