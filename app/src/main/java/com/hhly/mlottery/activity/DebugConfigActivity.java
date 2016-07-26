package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

public class DebugConfigActivity extends BaseActivity{

    private final static String TAG = "DebugConfigActivity";

    public final static int URL_13322 = 1;
    public final static int URL_1332255 = 2;
    public final static int URL_242 = 5;

    public final static int WS_13322 = 3;
    public final static int WS_242 = 4;
    public final static int WS_82 = 6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config);

        /**当前Activity界面不统计*/
        MobclickAgent.openActivityDurationTrack(false);

        Button config_submit = (Button) findViewById(R.id.config_submit);


        //开发环境
        findViewById(R.id.config_rb5).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_rg2)).check(R.id.config_rb3);
            }
        });
        //测试环境
        findViewById(R.id.config_rb1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_rg2)).check(R.id.config_rb6);
            }
        });
        //生产环境
        findViewById(R.id.config_rb2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_rg2)).check(R.id.config_rb4);
            }
        });

        config_submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                RadioGroup config_rg1 = (RadioGroup) findViewById(R.id.config_rg1);

                if (config_rg1.getCheckedRadioButtonId() == R.id.config_rb1) {
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, URL_1332255);
                } else if (config_rg1.getCheckedRadioButtonId() == R.id.config_rb2) {
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, URL_13322);
                } else if (config_rg1.getCheckedRadioButtonId() == R.id.config_rb5) {
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, URL_242);
                }

                RadioGroup config_rg2 = (RadioGroup) findViewById(R.id.config_rg2);
                if (config_rg2.getCheckedRadioButtonId() == R.id.config_rb3) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_242);
                } else if (config_rg2.getCheckedRadioButtonId() == R.id.config_rb4) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_13322);
                } else if (config_rg2.getCheckedRadioButtonId() == R.id.config_rb6) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_82);
                }

//                startActivity(new Intent(DebugConfigActivity.this, TestActivity.class));
                startActivity(new Intent(DebugConfigActivity.this, WelcomeActivity.class));
                System.exit(0);

//				finish();

            }
        });

    }

}
