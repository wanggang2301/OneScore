package com.hhly.mlottery.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
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
import com.hhly.mlottery.frame.footballframe.eventbus.ScoresMatchSettingEventBusEntity;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 足球设置
 */
public class FootballTypeSettingActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

    private final String ALET = "1";
    private final String EUR = "2";
    private final String SIZE = "3";

    private SwitchCompat cb_notice;

    private SwitchCompat sb_goal;
    private SwitchCompat sb_red;
    private SwitchCompat sb_shake;
    private SwitchCompat sb_sound;
    private SwitchCompat sb_push;


    private ImageView ib_back;

    private RelativeLayout mAlet; //亚盘
    private RelativeLayout mEur;  //欧赔
    private RelativeLayout mAsize;//大小球
    private RelativeLayout mNoshow; // 无

    private RadioButton mRd_alet;
    private RadioButton mRd_eur;
    private RadioButton mRd_asize;
    private RadioButton mRd_noshow;

//    String resultstring;

    Vibrator vibrator;

    SoundPool soundPool;

    HashMap<Integer, Integer> soundMap = new HashMap<>();

    private int currentFragmentId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示标题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mlottery_type_setting);

        initView();// 初始化界面
        initData();// 初始化数据
        initEvent();// 初始化事件

        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundMap.put(1, soundPool.load(this, R.raw.sound3, 1)); // 红牌声音
    }

    private void initEvent() {

        cb_notice.setOnCheckedChangeListener(this);

        ib_back.setOnClickListener(this);
        //
        sb_goal.setOnCheckedChangeListener(this);
        sb_red.setOnCheckedChangeListener(this);
        sb_shake.setOnCheckedChangeListener(this);
        sb_sound.setOnCheckedChangeListener(this);
        sb_push.setOnCheckedChangeListener(this);


    }

    private void initData() {
        Intent intent = getIntent();
        currentFragmentId = intent.getIntExtra("currentFragmentId", 0);

        //第一次进来根据文件选择选中哪个。默认是亚盘和欧赔
        boolean asize = PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false);
        boolean eur = PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, true);
        boolean alet = PreferenceUtil.getBoolean(MyConstants.RBSECOND, true);
        boolean noshow = PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false);

        mRd_alet.setChecked(alet);
        mRd_eur.setChecked(eur);
        mRd_asize.setChecked(asize);
        mRd_noshow.setChecked(noshow);

        if(mRd_alet.isChecked()){selectList.add(ALET);}
        if(mRd_eur.isChecked()){selectList.add(EUR);}
        if(mRd_asize.isChecked()){selectList.add(SIZE);}
        if(mRd_noshow.isChecked()){selectList.clear();}

        //设置按钮开关的选中状态

        cb_notice.setChecked(PreferenceUtil.getBoolean(MyConstants.GAMEATTENTION, true));
        if (PreferenceUtil.getBoolean(MyConstants.GAMEATTENTION, true)) {
            cb_notice.setChecked(true);
        } else {
            cb_notice.setChecked(false);
        }

        if (PreferenceUtil.getBoolean(MyConstants.GOAL, true)) {
            sb_goal.setChecked(true);
        } else {
            sb_goal.setChecked(false);
        }
        if (PreferenceUtil.getBoolean(MyConstants.RED_CARD, true)) {
            sb_red.setChecked(true);
        } else {
            sb_red.setChecked(false);
        }
        if (PreferenceUtil.getBoolean(MyConstants.SHAKE, true)) {
            sb_shake.setChecked(true);
        } else {
            sb_shake.setChecked(false);
        }
        if (PreferenceUtil.getBoolean(MyConstants.SOUND, true)) {
            sb_sound.setChecked(true);
        } else {
            sb_sound.setChecked(false);
        }

        //推送关注比赛
        if (PreferenceUtil.getBoolean(MyConstants.FOOTBALL_PUSH_FOCUS, true)) {
            sb_push.setChecked(true);
        } else {
            sb_push.setChecked(false);
        }


    }

    private void initView() {

        ib_back = (ImageView) findViewById(R.id.public_img_back);
        //赔率提示
        mAlet = (RelativeLayout) findViewById(R.id.rl_set_alet);
        mAlet.setOnClickListener(this);
        mEur = (RelativeLayout) findViewById(R.id.rl_set_eur);
        mEur.setOnClickListener(this);
        mAsize = (RelativeLayout) findViewById(R.id.rl_set_asize);
        mAsize.setOnClickListener(this);
        mNoshow = (RelativeLayout) findViewById(R.id.rl_set_noshow);
        mNoshow.setOnClickListener(this);

        mRd_alet = (RadioButton) findViewById(R.id.set_rd_alet);
        mRd_eur = (RadioButton) findViewById(R.id.set_rd_eur);
        mRd_asize = (RadioButton) findViewById(R.id.set_rd_asize);
        mRd_noshow = (RadioButton) findViewById(R.id.set_rd_noshow);

        //震动提示
        sb_goal = (SwitchCompat) findViewById(R.id.sb_goal);
        sb_red = (SwitchCompat) findViewById(R.id.sb_red_card);
        sb_shake = (SwitchCompat) findViewById(R.id.sb_shake);
        sb_sound = (SwitchCompat) findViewById(R.id.sb_sound);
        cb_notice = (SwitchCompat) findViewById(R.id.cb_notice);
        sb_push = (SwitchCompat) findViewById(R.id.sb_push);


        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);


        ((TextView) findViewById(R.id.public_txt_title)).setText(R.string.set_txt);

        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
    }

    List<String> selectList = new ArrayList<>(2);


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //赔率提示
            case R.id.rl_set_alet:
                mRd_alet.setChecked(!mRd_alet.isChecked());
                if(mRd_alet.isChecked()){
                    if(selectList.size() != 2){
                        selectList.add(ALET);
                    }else{
                        selectList.remove(0);
                        selectList.add(ALET);
                    }
                }else{
                    selectList.remove(ALET);
                }
                save();
                break;
            case R.id.rl_set_eur:
                mRd_eur.setChecked(!mRd_eur.isChecked());
                if(mRd_eur.isChecked()){
                    if(selectList.size() != 2){
                        selectList.add(EUR);
                    }else{
                        selectList.remove(0);
                        selectList.add(EUR);
                    }
                }else{
                    selectList.remove(EUR);
                }
                save();
                break;
            case R.id.rl_set_asize:
                mRd_asize.setChecked(!mRd_asize.isChecked());
                if(mRd_asize.isChecked()){
                    if(selectList.size() != 2){
                        selectList.add(SIZE);
                    }else{
                        selectList.remove(0);
                        selectList.add(SIZE);
                    }
                }else{
                    selectList.remove(SIZE);
                }
                save();
                break;
            case R.id.rl_set_noshow:
                selectList.clear();
                mRd_noshow.setChecked(true);
                save();
                break;
            case R.id.public_img_back:
                MobclickAgent.onEvent(mContext, "Football_Setting_Exit");
//                Intent intent = new Intent();
//                intent.putExtra("resultType", resultstring);

                eventbus();
//                setResult(Activity.RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            default:
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            Intent intent = new Intent();
//            intent.putExtra("resultType", resultstring);

            eventbus();

//            setResult(Activity.RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void eventbus() {
       /* if (currentFragmentId == 1) {
            ImmediateFragment.imEventBus.post(currentFragmentId);
        } else if (currentFragmentId == 2) {
            EventBus.getDefault().post(new ScoresMatchSettingEventBusEntity(currentFragmentId));

        } else if (currentFragmentId == 3) {
            EventBus.getDefault().post(new ScoresMatchSettingEventBusEntity(currentFragmentId));
        } else if (currentFragmentId == 4) {
            FocusFragment.focusEventBus.post(currentFragmentId);
        }*/

        EventBus.getDefault().post(new ScoresMatchSettingEventBusEntity(currentFragmentId));

    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {

        switch (button.getId()) {
            case R.id.sb_goal:
                PreferenceUtil.commitBoolean(MyConstants.GOAL, sb_goal.isChecked());
//                if (cb_goal_shake.isChecked()) {
//                    vibrator.vibrate(1000);
//                }

                break;
            case R.id.sb_red_card:
                PreferenceUtil.commitBoolean(MyConstants.RED_CARD, sb_red.isChecked());
//                if (cb_rc_shake.isChecked()) {
//                    vibrator.vibrate(1000);
//                }
                break;

            case R.id.sb_shake:
                PreferenceUtil.commitBoolean(MyConstants.SHAKE, sb_shake.isChecked());
                if (sb_shake.isChecked()) {
                    vibrator.vibrate(1000);
                }

                break;
            case R.id.sb_sound:
                PreferenceUtil.commitBoolean(MyConstants.SOUND, sb_sound.isChecked());

                if (sb_sound.isChecked()) {
                    soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
                }
                break;
            case R.id.cb_notice:
                PreferenceUtil.commitBoolean(MyConstants.GAMEATTENTION, cb_notice.isChecked());
                break;
            case R.id.sb_push:
                PreferenceUtil.commitBoolean(MyConstants.FOOTBALL_PUSH_FOCUS, sb_push.isChecked());

                if (sb_push.isChecked()) {
                    requestServer("true");
                } else {
                    requestServer("false");
                }
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
        String uMengDeviceToken = PreferenceUtil.getString(AppConstants.uMengDeviceToken, "");
        Map<String, String> params = new HashMap<>();
        params.put("deviceId", deviceId);
        params.put("userId", userId);
        params.put("statusPush", isPush);
        params.put("deviceToken", uMengDeviceToken);
        params.put("appNo", "11");
        VolleyContentFast.requestJsonByPost(BaseURLs.FOOTBALL_USER_SET, params, new VolleyContentFast.ResponseSuccessListener<String>() {
            @Override
            public void onResponse(String jsonObject) {
                L.d("AAA", "足球推送开关请求成功");

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        }, String.class);

    }

    private void save() {
        mRd_alet.setChecked(selectList.contains(ALET));
        mRd_eur.setChecked(selectList.contains(EUR));
        mRd_asize.setChecked(selectList.contains(SIZE));
        mRd_noshow.setChecked(selectList.size() == 0);


        PreferenceUtil.commitBoolean(MyConstants.RBSECOND, mRd_alet.isChecked()); //亚盘
        PreferenceUtil.commitBoolean(MyConstants.RBOCOMPENSATE, mRd_eur.isChecked());//欧赔
        PreferenceUtil.commitBoolean(MyConstants.rbSizeBall, mRd_asize.isChecked());//大小球
        PreferenceUtil.commitBoolean(MyConstants.RBNOTSHOW, mRd_noshow.isChecked()); //不显示

//        if (mRd_alet.isChecked()) {
//            resultstring = getResources().getString(R.string.set_asialet_txt);
//        }
//        if (mRd_asize.isChecked()) {
//            resultstring = getResources().getString(R.string.set_asiasize_txt);
//        }
//        if (mRd_eur.isChecked()) {
//            resultstring = getResources().getString(R.string.set_euro_txt);
//        }
//        if (mRd_noshow.isChecked()) {
//            resultstring = getResources().getString(R.string.set_oddghide_txt);
//        }

    }
}
