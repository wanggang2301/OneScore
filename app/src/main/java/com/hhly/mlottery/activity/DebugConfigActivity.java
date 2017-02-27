package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

public class DebugConfigActivity extends BaseActivity {

    private final static String TAG = "DebugConfigActivity";

    public final static int URL_13322 = 1;
    public final static int URL_1332255 = 2;
    public final static int URL_242 = 5;
    public final static int URL_1332255_2 = 7;

    public final static int WS_13322 = 3;
    public final static int WS_242 = 4;
    public final static int WS_1332255 = 6;
    public final static int DIY_INPUT = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        /**当前Activity界面不统计*/
        MobclickAgent.openActivityDurationTrack(false);

        Button config_submit = (Button) findViewById(R.id.config_submit);
        Button bt_zidingyi = (Button) findViewById(R.id.bt_zidingyi);

        //开发环境
        findViewById(R.id.config_kaifa).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_tuisong)).check(R.id.config_kaifa_ws);
            }
        });

        //测试环境
        findViewById(R.id.config_ceshi).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_tuisong)).check(R.id.config_ceshi_ws);
            }
        });

        //生产环境
        findViewById(R.id.config_shengchan).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_tuisong)).check(R.id.config_shengchan_ws);
            }
        });

        config_submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                RadioGroup config_huanjing = (RadioGroup) findViewById(R.id.config_huanjing);

                if (config_huanjing.getCheckedRadioButtonId() == R.id.config_kaifa) {  //开发
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, URL_242);

                } else if (config_huanjing.getCheckedRadioButtonId() == R.id.config_ceshi) { //测试
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, URL_1332255);

                } else if (config_huanjing.getCheckedRadioButtonId() == R.id.config_ceshi_2) {  //测试2
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, URL_1332255_2);

                } else if (config_huanjing.getCheckedRadioButtonId() == R.id.config_shengchan) { //生产
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, URL_13322);
                }


                RadioGroup config_tuisong = (RadioGroup) findViewById(R.id.config_tuisong);

                //开发推送
                if (config_tuisong.getCheckedRadioButtonId() == R.id.config_kaifa_ws) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_242);

                    //测试推送
                } else if (config_tuisong.getCheckedRadioButtonId() == R.id.config_ceshi_ws) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_1332255);

                    //生产推送
                } else if (config_tuisong.getCheckedRadioButtonId() == R.id.config_shengchan_ws) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_13322);
                }

                startActivity(new Intent(DebugConfigActivity.this, WelcomeActivity.class));
                System.exit(0);


            }
        });


        //自定义环境

        bt_zidingyi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText ed = (EditText) findViewById(R.id.et_input);
                String str = ed.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(DebugConfigActivity.this, "不能为空！", Toast.LENGTH_SHORT).show();
                    return;

                }

                PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, DIY_INPUT);
                PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, DIY_INPUT);


                L.d("huanjing", str);

                PreferenceUtil.commitString("DIY_INPUT", str);

                startActivity(new Intent(DebugConfigActivity.this, WelcomeActivity.class));

                System.exit(0);

            }
        });
    }

}