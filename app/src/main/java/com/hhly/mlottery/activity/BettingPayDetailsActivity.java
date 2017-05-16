package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;

/**
 * Created by：XQyi on 2017/4/18 11:12
 * Use:竞彩单关页面（详情内容页）
 */

public class BettingPayDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBack;
    private LinearLayout mToPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.betting_recommend_details);
        initView();
    }

    private void initView(){
        TextView title = (TextView) findViewById(R.id.public_txt_title);
        title.setText("竞彩单关");
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        mBack = (ImageView) findViewById(R.id.public_img_back);
        mBack.setOnClickListener(this);
        mToPay = (LinearLayout)findViewById(R.id.betting_topay_ll);
        mToPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back:
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.betting_topay_ll:
                Intent mIntent = new Intent(mContext , BettingOnlinePaymentActivity.class);
                startActivity(mIntent);
                overridePendingTransition(R.anim.push_left_in , R.anim.push_fix_out);
                break;
        }
    }
}
