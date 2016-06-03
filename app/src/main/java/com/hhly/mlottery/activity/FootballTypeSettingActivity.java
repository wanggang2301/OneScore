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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * 彩票种类设置
 */
public class FootballTypeSettingActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

    private SwitchCompat cb_goal_shake;

    private RelativeLayout rl_hometeam_goal;
    private RelativeLayout rl_guestteam_goal;

    private RelativeLayout rl_odd;

    private SwitchCompat cb_rc_shake;

    private SwitchCompat cb_rc_sound;

    private SwitchCompat cb_notice;

    private ImageView ib_back;

    private TextView tv_home_sound_type;
    private TextView tv_guest_sound_type;
    private TextView tv_odd_type;

    private Intent intent;

    Vibrator vibrator;

    SoundPool soundPool;

    HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();

    private final static int HOMETEAMGOAL = 0x10;
    private final static int GUESTTEAMGOAL = 0x20;
    private final static int ODDS = 0x30;

    private int currentFragmentId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示标题
        super.onCreate(savedInstanceState);

        initView();// 初始化界面
        initData();// 初始化数据
        initEvent();// 初始化事件

        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundMap.put(1, soundPool.load(this, R.raw.sound3, 1)); // 红牌声音
    }

    private void initEvent() {
        cb_goal_shake.setOnCheckedChangeListener(this);
        cb_rc_shake.setOnCheckedChangeListener(this);
        cb_rc_sound.setOnCheckedChangeListener(this);
        cb_notice.setOnCheckedChangeListener(this);

        ib_back.setOnClickListener(this);

    }

    private void initData() {
        Intent intent=getIntent();
        currentFragmentId=intent.getIntExtra("currentFragmentId",0);

        cb_goal_shake.setChecked(PreferenceUtil.getBoolean(MyConstants.VIBRATEGOALHINT, true));
        if (PreferenceUtil.getBoolean(MyConstants.VIBRATEGOALHINT, true)) {
            cb_goal_shake.setChecked(true);
        } else {
            cb_goal_shake.setChecked(false);
        }

        cb_rc_shake.setChecked(PreferenceUtil.getBoolean(MyConstants.VIBRATEREDHINT, true));
        if (PreferenceUtil.getBoolean(MyConstants.VIBRATEREDHINT, true)) {
            cb_rc_shake.setChecked(true);
        } else {
            cb_rc_shake.setChecked(false);
        }

        cb_rc_sound.setChecked(PreferenceUtil.getBoolean(MyConstants.VOICEREDHINT, true));
        if (PreferenceUtil.getBoolean(MyConstants.VOICEREDHINT, true)) {
            cb_rc_sound.setChecked(true);
        } else {
            cb_rc_sound.setChecked(false);
        }

        cb_notice.setChecked(PreferenceUtil.getBoolean(MyConstants.GAMEATTENTION, true));
        if (PreferenceUtil.getBoolean(MyConstants.GAMEATTENTION, true)) {
            cb_notice.setChecked(true);
        } else {
            cb_notice.setChecked(false);
        }


        setHomeSoundTxt();
        setGuestSoundText();





        boolean asize = PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false);
        boolean eur = PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, false);
        boolean alet = PreferenceUtil.getBoolean(MyConstants.RBSECOND, true);
        boolean noshow = PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false);
        if (asize) {
            tv_odd_type.setText(R.string.set_asiasize_txt);
        }

        if (eur) {
            tv_odd_type.setText(R.string.set_euro_txt);
        }

        if (alet) {
            tv_odd_type.setText(R.string.set_asialet_txt);
        }

        if (noshow) {
            tv_odd_type.setText(R.string.set_oddghide_txt);
        }


    }

    private void initView() {
        setContentView(R.layout.mlottery_type_setting);

        cb_goal_shake = (SwitchCompat) findViewById(R.id.goal_shake);

        rl_hometeam_goal = (RelativeLayout) findViewById(R.id.rl_hometeam_goal);
        rl_guestteam_goal = (RelativeLayout) findViewById(R.id.rl_guestteam_goal);
        rl_odd = (RelativeLayout) findViewById(R.id.rl_odd);

        cb_rc_shake = (SwitchCompat) findViewById(R.id.rc_shake);

        cb_rc_sound = (SwitchCompat) findViewById(R.id.rc_sound);

        cb_notice = (SwitchCompat) findViewById(R.id.cb_notice);

        ib_back = (ImageView) findViewById(R.id.public_img_back);
        ib_back.setImageResource(R.mipmap.back);

        tv_guest_sound_type = (TextView) findViewById(R.id.guest_sound_type);
        tv_home_sound_type = (TextView) findViewById(R.id.home_sound_type);
        tv_odd_type = (TextView) findViewById(R.id.odd_type);

        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

        rl_hometeam_goal.setOnClickListener(this);

        rl_guestteam_goal.setOnClickListener(this);

        rl_odd.setOnClickListener(this);

        ((TextView) findViewById(R.id.public_txt_title)).setText(R.string.set_txt);

        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_hometeam_goal:
                MobclickAgent.onEvent(mContext,"Football_Setting_HomeTeam_Goal");
                intent = new Intent(FootballTypeSettingActivity.this, FootballTypeSettingDetailsActivity.class);
                intent.putExtra("type", "home");
                startActivityForResult(intent, HOMETEAMGOAL);

                break;
            case R.id.rl_guestteam_goal:
                MobclickAgent.onEvent(mContext,"Football_Setting_GuestTeam_Goal");
                intent = new Intent(FootballTypeSettingActivity.this, FootballTypeSettingDetailsActivity.class);
                intent.putExtra("type", "guest");
                startActivityForResult(intent, GUESTTEAMGOAL);

                break;

            case R.id.rl_odd:
                MobclickAgent.onEvent(mContext,"Football_Setting_Rl_Odd");
                intent = new Intent(FootballTypeSettingActivity.this, FootballTypeSettingDetailsActivity.class);
                intent.putExtra("type", "odd");
                Bundle bundle=new Bundle();
                bundle.putInt("currentFragmentId",currentFragmentId);
                intent.putExtras(bundle);

                startActivityForResult(intent, ODDS);
                break;
            case R.id.public_img_back:
                MobclickAgent.onEvent(mContext,"Football_Setting_Exit");
             //   setResult(Activity.RESULT_OK);
                finish();

                break;
            default:
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
         //   setResult(Activity.RESULT_OK);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        intent = data;
        if (requestCode == HOMETEAMGOAL && resultCode == Activity.RESULT_OK) {

            if (!intent.getStringExtra("resultType").equals("")) {
                tv_home_sound_type.setText(intent.getStringExtra("resultType"));
            } else {
                setHomeSoundTxt();

            }

        }

        if (requestCode == GUESTTEAMGOAL && resultCode == Activity.RESULT_OK) {
            if (!intent.getStringExtra("resultType").equals("")) {
                tv_guest_sound_type.setText(intent.getStringExtra("resultType"));
            } else {
                setGuestSoundText();
            }

        }

        if (requestCode == ODDS && resultCode == Activity.RESULT_OK) {
            if (!intent.getStringExtra("resultType").equals("")) {
                tv_odd_type.setText(intent.getStringExtra("resultType"));
            } else {
                boolean asize = PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false);
                boolean eur = PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, false);
                boolean alet = PreferenceUtil.getBoolean(MyConstants.RBSECOND, true);
                boolean noshow = PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false);
                if (asize) {
                    tv_odd_type.setText(R.string.set_asiasize_txt);
                }
                if (eur) {
                    tv_odd_type.setText(R.string.set_euro_txt);
                }

                if (alet) {
                    tv_odd_type.setText(R.string.set_asialet_txt);
                }

                if (noshow) {
                    tv_odd_type.setText(R.string.set_oddghide_txt);
                }

            }

        }
    }

    private void setHomeSoundTxt(){
        switch (PreferenceUtil.getInt(MyConstants.HOSTTEAMINDEX, 1)){
            case 1:
                tv_home_sound_type.setText(R.string.set_sound1_txt);
                break;
            case 2:
                tv_home_sound_type.setText(R.string.set_sound2_txt);
                break;
            case 3:
                tv_home_sound_type.setText(R.string.set_sound3_txt);
                break;
            case 4:
                tv_home_sound_type.setText(R.string.set_nosound_txt);
                break;
            default:
                tv_home_sound_type.setText(R.string.set_sound1_txt);
                break;
        }

    }

    public void setGuestSoundText(){
        switch (PreferenceUtil.getInt(MyConstants.GUESTTEAM, 2)){
            case 1:
                tv_guest_sound_type.setText(R.string.set_sound1_txt);
                break;
            case 2:
                tv_guest_sound_type.setText(R.string.set_sound2_txt);
                break;
            case 3:
                tv_guest_sound_type.setText(R.string.set_sound3_txt);
                break;
            case 4:
                tv_guest_sound_type.setText(R.string.set_nosound_txt);
                break;
            default:
                tv_guest_sound_type.setText(R.string.set_sound1_txt);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        // TODO Auto-generated method stub

        switch (button.getId()) {
            case R.id.goal_shake:
                MobclickAgent.onEvent(mContext,"Football_Setting_Goal_Shake");
                PreferenceUtil.commitBoolean(MyConstants.VIBRATEGOALHINT, cb_goal_shake.isChecked());
                if (cb_goal_shake.isChecked()) {
                    vibrator.vibrate(1000);
                }

                break;
            case R.id.rc_shake:
                MobclickAgent.onEvent(mContext,"Football_Setting_Rc_Shake");
                PreferenceUtil.commitBoolean(MyConstants.VIBRATEREDHINT, cb_rc_shake.isChecked());
                if (cb_rc_shake.isChecked()) {
                    vibrator.vibrate(1000);
                }
                break;

            case R.id.rc_sound:
                MobclickAgent.onEvent(mContext,"Football_Setting_Rc_Sound");
                PreferenceUtil.commitBoolean(MyConstants.VOICEREDHINT, cb_rc_sound.isChecked());
                if (cb_rc_sound.isChecked()) {
                    soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
                }

                break;
            case R.id.cb_notice:
                MobclickAgent.onEvent(mContext,"Football_Setting_Cb_Notice");
                PreferenceUtil.commitBoolean(MyConstants.GAMEATTENTION, cb_notice.isChecked());
                break;

            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("FootballTypeSettingActivity");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("FootballTypeSettingActivity");
    }
}
