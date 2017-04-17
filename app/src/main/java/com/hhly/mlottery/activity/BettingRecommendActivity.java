package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hhly.mlottery.R;

/**
 * Created by yixq on 2017/4/13.
 * mail：yixq@13322.com
 * describe:
 */

public class BettingRecommendActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_betting_recommend);

        initView();

    }

    private void initView(){

        TextView mTitlt = (TextView) findViewById(R.id.public_txt_title);
        mTitlt.setText("竞彩推荐");
        findViewById(R.id.public_btn_filter).setVisibility(View.INVISIBLE);//隐藏筛选



    }
}
