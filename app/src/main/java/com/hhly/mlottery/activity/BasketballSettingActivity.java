package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.basketballframe.BasketFocusEventBus;
import com.hhly.mlottery.frame.footballframe.eventbus.BasketScoreSettingEventBusEntity;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * @author yixq
 * @ClassName: BasketballSettingActivity
 * @Description: 篮球设置
 * @date 2015-12-29 下午4:09:32
 */

public class BasketballSettingActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

    private RelativeLayout mAlet; //亚盘
    private RelativeLayout mEur;  //欧赔
    private RelativeLayout mAsize;//大小球
    private RelativeLayout mNoshow; // 无

    private RadioButton mRd_alet;
    private RadioButton mRd_eur;
    private RadioButton mRd_asize;
    private RadioButton mRd_noshow;

    private SwitchCompat mTb_score; // 半全场总分
    private SwitchCompat mTb_Point_spread; //总分差
    private SwitchCompat mTb_single_score; //单节比分
    private SwitchCompat mTb_ranking; //排名
    private SwitchCompat mTb_push; //推送关注消息

    private ImageView mBack;

    String resultstring;
    Intent intent;
    private int mCurrentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basketball_setting);

        initView();
        initData();
    }

    private void initView() {
        TextView public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(R.string.basket_setting_tittle);

        mAlet = (RelativeLayout) findViewById(R.id.rl_alet);
        mAlet.setOnClickListener(this);
        mEur = (RelativeLayout) findViewById(R.id.rl_eur);
        mEur.setOnClickListener(this);
        mAsize = (RelativeLayout) findViewById(R.id.rl_asize);
        mAsize.setOnClickListener(this);
        mNoshow = (RelativeLayout) findViewById(R.id.rl_noshow);
        mNoshow.setOnClickListener(this);

        mRd_alet = (RadioButton) findViewById(R.id.rd_alet);
        mRd_eur = (RadioButton) findViewById(R.id.rd_eur);
        mRd_asize = (RadioButton) findViewById(R.id.rd_asize);
        mRd_noshow = (RadioButton) findViewById(R.id.rd_noshow);

        mTb_score = (SwitchCompat) findViewById(R.id.tb_score);
//		mTb_score.setOnClickListener(this);
        mTb_score.setOnCheckedChangeListener(this);

        mTb_Point_spread = (SwitchCompat) findViewById(R.id.tb_Point_spread);
        mTb_Point_spread.setOnCheckedChangeListener(this);
        mTb_single_score = (SwitchCompat) findViewById(R.id.tb_single_score);
        mTb_single_score.setOnCheckedChangeListener(this);
        mTb_ranking = (SwitchCompat) findViewById(R.id.tb_ranking);
        mTb_ranking.setOnCheckedChangeListener(this);
        mTb_push = (SwitchCompat) findViewById(R.id.tb_push);
        mTb_push.setOnCheckedChangeListener(this);

        mBack = (ImageView) findViewById(R.id.public_img_back);
        mBack.setOnClickListener(this);

        findViewById(R.id.public_btn_set).setVisibility(View.GONE); //隐藏设置按键
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE); //隐藏筛选按键

    }

    private void initData() {

        mTb_score.setChecked(PreferenceUtil.getBoolean(MyConstants.HALF_FULL_SCORE, true));
        if (PreferenceUtil.getBoolean(MyConstants.HALF_FULL_SCORE, true)) {
            mTb_score.setChecked(true);
        } else {
            mTb_score.setChecked(false);
        }

        mTb_Point_spread.setChecked(PreferenceUtil.getBoolean(MyConstants.SCORE_DIFFERENCE, true));
        if (PreferenceUtil.getBoolean(MyConstants.SCORE_DIFFERENCE, true)) {
            mTb_Point_spread.setChecked(true);
        } else {
            mTb_Point_spread.setChecked(false);
        }

        mTb_single_score.setChecked(PreferenceUtil.getBoolean(MyConstants.SINGLE_SCORE, true));
        if (PreferenceUtil.getBoolean(MyConstants.SINGLE_SCORE, true)) {
            mTb_single_score.setChecked(true);
        } else {
            mTb_single_score.setChecked(false);
        }

        mTb_ranking.setChecked(PreferenceUtil.getBoolean(MyConstants.HOST_RANKING, true));
        if (PreferenceUtil.getBoolean(MyConstants.HOST_RANKING, true)) {
            mTb_ranking.setChecked(true);
        } else {
            mTb_ranking.setChecked(false);
        }

        //推送关注比赛按钮
        mTb_push.setChecked(PreferenceUtil.getBoolean(MyConstants.BASKETBALL_PUSH_FOCUS, true));
        if (PreferenceUtil.getBoolean(MyConstants.BASKETBALL_PUSH_FOCUS, true)) {
            mTb_push.setChecked(true);
        } else {
            mTb_push.setChecked(false);
        }

        resultstring = "";
        intent = getIntent();

        mCurrentId = intent.getIntExtra("currentfragment", 0);
        //设置默认初始化值
        boolean asize = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_rbSizeBall, false); //大小球
        boolean eur = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_RBOCOMPENSATE, false);//欧赔
        boolean alet = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_RBSECOND, true); //亚盘
        boolean noshow = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_RBNOTSHOW, false);//不显示

        mRd_alet.setChecked(alet);
        mRd_eur.setChecked(eur);
        mRd_asize.setChecked(asize);
        mRd_noshow.setChecked(noshow);

    }

    @Override
    public void onClick(View v) {
        // Auto-generated method stub
        switch (v.getId()) {
            case R.id.rl_alet:
                MobclickAgent.onEvent(mContext, "Basketball_Setting_Alet");
                mRd_alet.setChecked(true);
                mRd_eur.setChecked(false);
                mRd_asize.setChecked(false);
                mRd_noshow.setChecked(false);
                save();
                break;
            case R.id.rl_eur:
                MobclickAgent.onEvent(mContext, "Basketball_Setting_Eur");
                mRd_alet.setChecked(false);
                mRd_eur.setChecked(true);
                mRd_asize.setChecked(false);
                mRd_noshow.setChecked(false);
                save();
                break;
            case R.id.rl_asize:
                MobclickAgent.onEvent(mContext, "Basketball_Setting_Asize");
                mRd_alet.setChecked(false);
                mRd_eur.setChecked(false);
                mRd_asize.setChecked(true);
                mRd_noshow.setChecked(false);
                save();
                break;
            case R.id.rl_noshow:
                MobclickAgent.onEvent(mContext, "Basketball_Setting_Noshow");
                mRd_alet.setChecked(false);
                mRd_eur.setChecked(false);
                mRd_asize.setChecked(false);
                mRd_noshow.setChecked(true);
                save();
                break;

            case R.id.public_img_back:
                MobclickAgent.onEvent(mContext, "Basketball_Setting_Exit");
                Intent intent = new Intent();
                intent.putExtra("resultType", resultstring);

                if (mCurrentId == 0) {
                    EventBus.getDefault().post(new BasketScoreSettingEventBusEntity(mCurrentId + ""));
                } else if (mCurrentId == 1) {
                    EventBus.getDefault().post(new BasketScoreSettingEventBusEntity(mCurrentId + ""));
                } else if (mCurrentId == 2) {
                    EventBus.getDefault().post(new BasketScoreSettingEventBusEntity(mCurrentId + ""));
                } else if (mCurrentId == 3) {
                    EventBus.getDefault().post(new BasketFocusEventBus());
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            default:
                break;
        }
    }

    /**
     * 开关的选择与否
     *
     * @param cb
     * @param ischecked
     */
    @Override
    public void onCheckedChanged(CompoundButton cb, boolean ischecked) {
        switch (cb.getId()) {
            case R.id.tb_score:
                MobclickAgent.onEvent(mContext, "Basketball_Setting_Score");
                PreferenceUtil.commitBoolean(MyConstants.HALF_FULL_SCORE, mTb_score.isChecked());
                break;

            case R.id.tb_Point_spread:
                MobclickAgent.onEvent(mContext, "Basketball_Setting_Spread");
                PreferenceUtil.commitBoolean(MyConstants.SCORE_DIFFERENCE, mTb_Point_spread.isChecked());
                break;

            case R.id.tb_single_score:
                MobclickAgent.onEvent(mContext, "Basketball_Setting_SingleScore");
                PreferenceUtil.commitBoolean(MyConstants.SINGLE_SCORE, mTb_single_score.isChecked());
                break;

            case R.id.tb_ranking:
                MobclickAgent.onEvent(mContext, "Basketball_Setting_Ranking");
                PreferenceUtil.commitBoolean(MyConstants.HOST_RANKING, mTb_ranking.isChecked());
                break;
            case R.id.tb_push:
                PreferenceUtil.commitBoolean(MyConstants.BASKETBALL_PUSH_FOCUS, mTb_push.isChecked());

                // 把是否接受推送消息的状态传给服务器
                if (mTb_push.isChecked()) {
                    requestServer("true"); //接收推送
                } else {
                    requestServer("false");
                }

                break;

            default:
                break;
        }
    }

    /**
     * 把是否接受关注提交给后台
     *
     * @param isPush
     */
    private void requestServer(String isPush) {
        String deviceId = AppConstants.deviceToken;
        String userId = AppConstants.register.getUser().getUserId();
        Map<String, String> params = new HashMap<>();
        params.put("deviceId", deviceId);
        params.put("userId", userId);
        params.put("isNotice", isPush);
        String url = " http://192.168.31.68:8080/mlottery/core/androidBasketballMatch.updatePushStatus.do";
        VolleyContentFast.requestJsonByPost(BaseURLs.BASKET_USER_SET, params, new VolleyContentFast.ResponseSuccessListener<String>() {
            @Override
            public void onResponse(String jsonObject) {
                L.d("AAA", "篮球推送开关请求成功");

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        }, String.class);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.putExtra("resultType", resultstring);

            if (mCurrentId == 0) {
                EventBus.getDefault().post(new BasketScoreSettingEventBusEntity(mCurrentId + ""));
            } else if (mCurrentId == 1) {
                EventBus.getDefault().post(new BasketScoreSettingEventBusEntity(mCurrentId + ""));
            } else if (mCurrentId == 2) {
                EventBus.getDefault().post(new BasketScoreSettingEventBusEntity(mCurrentId + ""));
            } else if (mCurrentId == 3) {
                EventBus.getDefault().post(new BasketFocusEventBus());
            }

            setResult(Activity.RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void save() {
        PreferenceUtil.commitBoolean(MyConstants.BASKETBALL_RBSECOND, mRd_alet.isChecked()); //亚盘
        PreferenceUtil.commitBoolean(MyConstants.BASKETBALL_RBOCOMPENSATE, mRd_eur.isChecked());//欧赔
        PreferenceUtil.commitBoolean(MyConstants.BASKETBALL_rbSizeBall, mRd_asize.isChecked());//大小球
        PreferenceUtil.commitBoolean(MyConstants.BASKETBALL_RBNOTSHOW, mRd_noshow.isChecked()); //不显示

        if (mRd_alet.isChecked()) {
            resultstring = getResources().getString(R.string.set_asialet_txt);
        }
        if (mRd_asize.isChecked()) {
            resultstring = getResources().getString(R.string.set_asiasize_txt);
        }
        if (mRd_eur.isChecked()) {
            resultstring = getResources().getString(R.string.set_euro_txt);
        }
        if (mRd_noshow.isChecked()) {
            resultstring = getResources().getString(R.string.set_oddghide_txt);
        }
    }
}
