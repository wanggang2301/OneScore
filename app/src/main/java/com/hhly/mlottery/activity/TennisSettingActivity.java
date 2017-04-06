package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.tennisball.TennisEventBus;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;

import de.greenrobot.event.EventBus;

/**
 * 网球设置页
 * 2017-03-21 tangrr
 */
public class TennisSettingActivity extends BaseActivity implements View.OnClickListener {


    private ImageView mBackImage;
    private TextView mTitle;

    private RelativeLayout mAlet;//亚盘
    private RelativeLayout mEur;//欧赔
    private RelativeLayout mSize;//大小球
    private RelativeLayout mNoShow;//不显示

    private RadioButton mAletRb;
    private RadioButton mEurRb;
    private RadioButton mSizeRb;
    private RadioButton mNoShowRb;
    private final String TENNIS_ODDS = "tennis_odds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tennis_setting_layout);

        initView();
        setStatus();

    }

    public void initView() {

        //隐藏公共头部多余图标
        ImageView mSetImage = (ImageView) findViewById(R.id.public_btn_set);
        mSetImage.setVisibility(View.GONE);
        ImageView mFilterImage = (ImageView) findViewById(R.id.public_btn_filter);
        mFilterImage.setVisibility(View.GONE);

        mTitle = (TextView) findViewById(R.id.public_txt_title);
        mTitle.setText(R.string.tennis_setting_title);

        mBackImage = (ImageView) findViewById(R.id.public_img_back);
        mBackImage.setOnClickListener(this);

        mAlet = (RelativeLayout) findViewById(R.id.rl_tennis_alet);
        mAlet.setOnClickListener(this);
        mEur = (RelativeLayout) findViewById(R.id.rl_tennis_eur);
        mEur.setOnClickListener(this);
        mSize = (RelativeLayout) findViewById(R.id.rl_tennis_size);
        mSize.setOnClickListener(this);
        mNoShow = (RelativeLayout) findViewById(R.id.rl_tennis_noshow);
        mNoShow.setOnClickListener(this);

        mAletRb = (RadioButton) findViewById(R.id.rd_tennis_alet);
        mEurRb = (RadioButton) findViewById(R.id.rd_tennis_eur);
        mSizeRb = (RadioButton) findViewById(R.id.rd_tennis_size);
        mNoShowRb = (RadioButton) findViewById(R.id.rd_tennis_noshow);

    }

    private void setStatus() {
        //设置默认初始化值
        boolean alet = PreferenceUtil.getBoolean(MyConstants.TENNIS_ALET, true); //亚盘
        boolean eur = PreferenceUtil.getBoolean(MyConstants.TENNIS_EURO, false);//欧赔
        boolean size = PreferenceUtil.getBoolean(MyConstants.TENNIS_ASIZE, false);//大小球
        boolean noshow = PreferenceUtil.getBoolean(MyConstants.TENNIS_NOTSHOW, false);//不显示

        mAletRb.setChecked(alet);
        mEurRb.setChecked(eur);
        mSizeRb.setChecked(size);
        mNoShowRb.setChecked(noshow);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            EventBus.getDefault().post(new TennisEventBus(TENNIS_ODDS));
            finish();
            overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_tennis_alet:
                mAletRb.setChecked(true);
                mEurRb.setChecked(false);
                mSizeRb.setChecked(false);
                mNoShowRb.setChecked(false);
                break;
            case R.id.rl_tennis_eur:
                mAletRb.setChecked(false);
                mEurRb.setChecked(true);
                mSizeRb.setChecked(false);
                mNoShowRb.setChecked(false);
                break;
            case R.id.rl_tennis_size:
                mAletRb.setChecked(false);
                mEurRb.setChecked(false);
                mSizeRb.setChecked(true);
                mNoShowRb.setChecked(false);
                break;
            case R.id.rl_tennis_noshow:
                mAletRb.setChecked(false);
                mEurRb.setChecked(false);
                mSizeRb.setChecked(false);
                mNoShowRb.setChecked(true);
                break;
            case R.id.public_img_back:
                EventBus.getDefault().post(new TennisEventBus(TENNIS_ODDS));
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
        }
        save();
    }

    private void save() {
        PreferenceUtil.commitBoolean(MyConstants.TENNIS_ALET, mAletRb.isChecked());
        PreferenceUtil.commitBoolean(MyConstants.TENNIS_EURO, mEurRb.isChecked());
        PreferenceUtil.commitBoolean(MyConstants.TENNIS_ASIZE, mSizeRb.isChecked());
        PreferenceUtil.commitBoolean(MyConstants.TENNIS_NOTSHOW, mNoShowRb.isChecked());
    }
}
