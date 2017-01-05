package com.hhly.mlottery.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.hhly.mlottery.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/***
 * 多屏观看介绍页面
 */

public class MultiScreenIntroduceActivity extends Activity {


    @BindView(R.id.btn_enter)
    TextView btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_screen_introduce);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_enter)
    public void onClick() {

    }
}
