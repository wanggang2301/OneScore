package com.hhly.mlottery.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.NetworkUtils;
import com.hhly.mlottery.util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

public class DebugConfigActivity extends BaseActivity {

    private final static String TAG = "DebugConfigActivity";

    public final static int URL_13322 = 1;
    public final static int URL_1332255 = 2;
    public final static int URL_242 = 5;
    public final static int URL_93 = 7;

    public final static int WS_13322 = 3;
    public final static int WS_242 = 4;
    public final static int WS_82 = 6;
    public final static int DIY_INPUT = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        /**当前Activity界面不统计*/
        MobclickAgent.openActivityDurationTrack(false);

        Button config_submit = (Button) findViewById(R.id.config_submit);
        Button bt_ok = (Button) findViewById(R.id.bt_ok);

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
                } else if (config_rg1.getCheckedRadioButtonId() == R.id.config_rb7) {
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, URL_93);
                }

                RadioGroup config_rg2 = (RadioGroup) findViewById(R.id.config_rg2);
                if (config_rg2.getCheckedRadioButtonId() == R.id.config_rb3) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_242);
                } else if (config_rg2.getCheckedRadioButtonId() == R.id.config_rb4) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_13322);
                } else if (config_rg2.getCheckedRadioButtonId() == R.id.config_rb6) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_82);
                }

                startActivity(new Intent(DebugConfigActivity.this, WelcomeActivity.class));
                System.exit(0);

            }
        });

        bt_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


              /*  EditText ed = (EditText) findViewById(R.id.et_input);
                String str = ed.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(DebugConfigActivity.this, "不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, DIY_INPUT);
                PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, DIY_INPUT);
                PreferenceUtil.commitString("DIY_INPUT", str);
*/

                int type = NetworkUtils.getCurNetworkType(getApplicationContext());
                if (type == 2 || type == 3 || type == 4) {  //wifi環境下
                    startActivity(new Intent(DebugConfigActivity.this, PlayHighLightActivity.class));
                    //wifi
                } else if (type == 1) {//2G  3G  4G 移动数据环境下
                    promptNetInfo();
                }

                //  System.exit(0);
            }
        });
    }

    /**
     * 当前连接的网络提示
     */
    private void promptNetInfo() {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DebugConfigActivity.this, R.style.AppThemeDialog);
            builder.setCancelable(false);// 设置对话框以外不可点击
            builder.setTitle(getApplicationContext().getResources().getString(R.string.to_update_kindly_reminder));// 提示标题
            builder.setMessage(getApplicationContext().getResources().getString(R.string.video_high_light_reminder_comment));// 提示内容
            builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.video_high_light_continue_open), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(DebugConfigActivity.this, PlayHighLightActivity.class));
                }
            });
            builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.basket_analyze_dialog_cancle), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            android.support.v7.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
}
