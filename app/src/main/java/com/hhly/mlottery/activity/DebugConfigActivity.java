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

public class DebugConfigActivity extends BaseActivity {
    private final static String TAG = "DebugConfigActivity";
    public final static int ZH_1332255 = 1;
    public final static int TH_1332255 = 2;
    public final static int VN_1332255 = 3;
    public final static int ZH_13322 = 4;
    public final static int TH_13322 = 5;
    public final static int VN_13322 = 6;
    public final static int VN_13366 = 7;

    public final static int WS_13322_ZH = 9;
    public final static int WS_13322_TH = 10;
    public final static int WS_13322_VN = 11;
    public final static int WS_13366_VN = 12;
    public final static int WS_1332255_ZH = 13;
    public final static int WS_1332255_TH = 14;
    public final static int WS_1332255_VN = 15;

    public final static int DIY_INPUT = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        /**当前Activity界面不统计*/
        MobclickAgent.openActivityDurationTrack(false);

        Button config_submit = (Button) findViewById(R.id.config_submit);
        Button bt_zidingyi = (Button) findViewById(R.id.bt_zidingyi);

        //国内测试环境
        findViewById(R.id.config_ceshi_zh).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_tuisong)).check(R.id.config_ws_zh);
            }
        });

        //泰国测试环境
        findViewById(R.id.config_ceshi_th).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_tuisong)).check(R.id.config_ws_th);
            }
        });

        //越南测试环境
        findViewById(R.id.config_ceshi_vn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_tuisong)).check(R.id.config_ws_vn);
            }
        });

        //国内生产环境
        findViewById(R.id.config_shengchan).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_tuisong)).check(R.id.config_shengchan_ws);
            }
        });

        //泰国生产环境
        findViewById(R.id.config_shengchan_th).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_tuisong)).check(R.id.config_shengchan_ws_th);
            }
        });

        //越南南生产环境
        findViewById(R.id.config_shengchan_vn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_tuisong)).check(R.id.config_shengchan_ws_vn);
            }
        });

        //越南北生产环境
        findViewById(R.id.config_shengchan_vn_hn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_tuisong)).check(R.id.config_shengchan_ws_vn_hn);
            }
        });

        config_submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                RadioGroup config_huanjing = (RadioGroup) findViewById(R.id.config_huanjing);

                // URL环境
                if (config_huanjing.getCheckedRadioButtonId() == R.id.config_ceshi_zh) {
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, ZH_1332255);
                } else if (config_huanjing.getCheckedRadioButtonId() == R.id.config_ceshi_th) {
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, TH_1332255);
                } else if (config_huanjing.getCheckedRadioButtonId() == R.id.config_ceshi_vn) {
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, VN_1332255);
                } else if (config_huanjing.getCheckedRadioButtonId() == R.id.config_shengchan) {
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, ZH_13322);
                } else if (config_huanjing.getCheckedRadioButtonId() == R.id.config_shengchan_th) {
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, TH_13322);
                } else if (config_huanjing.getCheckedRadioButtonId() == R.id.config_shengchan_vn) {
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, VN_13322);
                } else if (config_huanjing.getCheckedRadioButtonId() == R.id.config_shengchan_vn_hn) {
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, VN_13366);
                }


                RadioGroup config_tuisong = (RadioGroup) findViewById(R.id.config_tuisong);

                // 推送环境
                if (config_tuisong.getCheckedRadioButtonId() == R.id.config_ws_zh) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_1332255_ZH);
                } else if (config_tuisong.getCheckedRadioButtonId() == R.id.config_ws_th) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_1332255_TH);
                } else if (config_tuisong.getCheckedRadioButtonId() == R.id.config_ws_vn) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_1332255_VN);
                } else if (config_tuisong.getCheckedRadioButtonId() == R.id.config_shengchan_ws) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_13322_ZH);
                } else if (config_tuisong.getCheckedRadioButtonId() == R.id.config_shengchan_ws_th) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_13322_TH);
                } else if (config_tuisong.getCheckedRadioButtonId() == R.id.config_shengchan_ws_vn) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_13322_VN);
                } else if (config_tuisong.getCheckedRadioButtonId() == R.id.config_shengchan_ws_vn_hn) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_13366_VN);
                }

                startActivity(new Intent(DebugConfigActivity.this, WelcomeActivity.class));
                System.exit(0);


            }
        });


        //自定义环境

        bt_zidingyi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   EditText ed = (EditText) findViewById(R.id.et_input);
                String str = ed.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(DebugConfigActivity.this, "不能为空！", Toast.LENGTH_SHORT).show();
                    return;

                }

                PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, DIY_INPUT);
                PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, DIY_INPUT);


                L.d("huanjing", str);

                PreferenceUtil.commitString("DIY_INPUT", str);*/

                //System.exit(0);
                //   public LeagueCup(int type, List<String> thirdId, String racename, String raceId, int count, boolean hot) {


            }
        });
    }

}