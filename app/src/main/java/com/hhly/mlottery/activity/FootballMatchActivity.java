package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;

/**
 * Created by yuely198 on 2017/3/16.
 */

public class FootballMatchActivity extends BaseActivity implements View.OnClickListener {


    private View mView;
    private TextView public_txt_title;
    private ImageView public_btn_filter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }


    /*初始化视图*/
    private void initView() {

        setContentView(R.layout.activity_footballmatch);
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText("竞彩足球");
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        //筛选按钮
        public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
        findViewById(R.id.public_img_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.public_img_back:

                finish();
                break;
            default:
                break;


        }

    }
}
