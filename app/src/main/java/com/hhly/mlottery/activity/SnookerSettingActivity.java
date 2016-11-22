package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.SnookerSettingEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by yixq on 2016/11/15.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerSettingActivity extends BaseActivity implements View.OnClickListener {


    private ImageView mBackImage;
    private TextView mTitle;

    private RelativeLayout mAlet;//亚盘
    private RelativeLayout mEur;//欧赔
    private RelativeLayout mAsize;//大小盘
    private RelativeLayout mSingleTwins;//单双
    private RelativeLayout mNoShow;//不显示

    private RadioButton mAletRb;
    private RadioButton mEurRb;
    private RadioButton mAsizeRb;
    private RadioButton mSingleTwinRb;
    private RadioButton mNoShowRb;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.snooker_setting_layout);

        initView();

    }

    public void initView(){

        //隐藏公共头部多余图标
        ImageView mSetImage =(ImageView)findViewById(R.id.public_btn_set);
        mSetImage.setVisibility(View.GONE);
        ImageView mFilterImage =(ImageView)findViewById(R.id.public_btn_filter);
        mFilterImage.setVisibility(View.GONE);

        mTitle = (TextView) findViewById(R.id.public_txt_title);
        mTitle.setText(R.string.snooker_setting_title);

        mBackImage = (ImageView) findViewById(R.id.public_img_back);
        mBackImage.setOnClickListener(this);

        mAlet = (RelativeLayout) findViewById(R.id.rl_snooker_alet);
        mAlet.setOnClickListener(this);
        mEur = (RelativeLayout) findViewById(R.id.rl_snooker_eur);
        mEur.setOnClickListener(this);
        mAsize = (RelativeLayout) findViewById(R.id.rl_snooker_asize);
        mAsize.setOnClickListener(this);
        mSingleTwins = (RelativeLayout) findViewById(R.id.rl_snooker_single_twins);
        mSingleTwins.setOnClickListener(this);
        mNoShow = (RelativeLayout) findViewById(R.id.rl_snooker_noshow);
        mNoShow.setOnClickListener(this);

        mAletRb = (RadioButton) findViewById(R.id.rd_snooker_alet);
        mEurRb = (RadioButton) findViewById(R.id.rd_snooker_eur);
        mAsizeRb = (RadioButton) findViewById(R.id.rd_snooker_asize);
        mSingleTwinRb = (RadioButton) findViewById(R.id.rd_snooker_single_twin);
        mNoShowRb = (RadioButton) findViewById(R.id.rd_snooker_noshow);

    }

    public void setStatus(){
        //设置默认初始化值
        boolean alet = PreferenceUtil.getBoolean(MyConstants.SNOOKER_ALET, true); //亚盘
        boolean eur = PreferenceUtil.getBoolean(MyConstants.SNOOKER_EURO, false);//欧赔
        boolean asize = PreferenceUtil.getBoolean(MyConstants.SNOOKER_ASIZE, false); //大小盘
        boolean singleTwins = PreferenceUtil.getBoolean(MyConstants.SNOOKER_SINGLETWIN , false);//单双
        boolean noshow = PreferenceUtil.getBoolean(MyConstants.SNOOKER_NOTSHOW, false);//不显示

        mAletRb.setChecked(alet);
        mEurRb.setChecked(eur);
        mAsizeRb.setChecked(asize);
        mSingleTwinRb.setChecked(singleTwins);
        mNoShowRb.setChecked(noshow);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.putExtra("result", result);

//            SnookerListActivity.SnookerListEventBus.post(0);
            EventBus.getDefault().post(new SnookerSettingEvent("111"));

            setResult(Activity.RESULT_OK,intent);
            finish();
            overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
            return true;

        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_snooker_alet:
//                MobclickAgent.onEvent(mContext , "Snooker_Setting_Alet");
                mAletRb.setChecked(true);
                mEurRb.setChecked(false);
                mAsizeRb.setChecked(false);
                mSingleTwinRb.setChecked(false);
                mNoShowRb.setChecked(false);
                save();
                break;
            case R.id.rl_snooker_eur:
//                MobclickAgent.onEvent(mContext , "Snooker_Setting_Eur");
                mAletRb.setChecked(false);
                mEurRb.setChecked(true);
                mAsizeRb.setChecked(false);
                mSingleTwinRb.setChecked(false);
                mNoShowRb.setChecked(false);
                save();
                break;
            case R.id.rl_snooker_asize:
//                MobclickAgent.onEvent(mContext , "Snooker_Setting_Asize");
                mAletRb.setChecked(false);
                mEurRb.setChecked(false);
                mAsizeRb.setChecked(true);
                mSingleTwinRb.setChecked(false);
                mNoShowRb.setChecked(false);
                save();
                break;
            case R.id.rl_snooker_single_twins:
//                MobclickAgent.onEvent(mContext , "Snooker_Setting_Twins");
                mAletRb.setChecked(false);
                mEurRb.setChecked(false);
                mAsizeRb.setChecked(false);
                mSingleTwinRb.setChecked(true);
                mNoShowRb.setChecked(false);
                save();
                break;
            case R.id.rl_snooker_noshow:
//                MobclickAgent.onEvent(mContext , "Snooker_Setting_Noshow");
                mAletRb.setChecked(false);
                mEurRb.setChecked(false);
                mAsizeRb.setChecked(false);
                mSingleTwinRb.setChecked(false);
                mNoShowRb.setChecked(true);
                save();
                break;
            case R.id.public_img_back:
//                MobclickAgent.onEvent(mContext , "Snooker_Setting_Exit");
                Intent intent = new Intent();

                intent.putExtra("result" , result);
                EventBus.getDefault().post(new SnookerSettingEvent("111"));

                setResult(Activity.RESULT_OK , intent);
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);

                break;
        }
    }

    private void save(){

        PreferenceUtil.commitBoolean(MyConstants.SNOOKER_ALET , mAletRb.isChecked());
        PreferenceUtil.commitBoolean(MyConstants.SNOOKER_EURO , mEurRb.isChecked());
        PreferenceUtil.commitBoolean(MyConstants.SNOOKER_ASIZE , mAsizeRb.isChecked());
        PreferenceUtil.commitBoolean(MyConstants.SNOOKER_SINGLETWIN , mSingleTwinRb.isChecked());
        PreferenceUtil.commitBoolean(MyConstants.SNOOKER_NOTSHOW , mNoShowRb.isChecked());

        if (mAletRb.isChecked()) {
            result = getResources().getString(R.string.snooker_setting_alet);
        }
        if(mEurRb.isChecked()){
            result = getResources().getString(R.string.snooker_setting_eur);
        }
        if(mAsizeRb.isChecked()){
            result = getResources().getString(R.string.snooker_setting_asize);
        }
        if(mSingleTwinRb.isChecked()){
            result = getResources().getString(R.string.snooker_setting_single_twins);
        }
        if(mNoShowRb.isChecked()){
            result = getResources().getString(R.string.snooker_setting_nos_show);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
