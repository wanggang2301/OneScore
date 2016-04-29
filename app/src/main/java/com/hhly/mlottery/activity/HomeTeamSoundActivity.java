package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.footframe.FocusFragment;
import com.hhly.mlottery.frame.footframe.ImmediateFragment;
import com.hhly.mlottery.frame.footframe.ResultFragment;
import com.hhly.mlottery.frame.footframe.ScheduleFragment;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

public class HomeTeamSoundActivity extends BaseActivity implements OnClickListener {

	private final static int HOMETEAMGOAL = 0x10;
	private final static int GUESTTEAMGOAL = 0x20;
	private final static int ODDS = 0x30;

	private RadioButton radio1;
	private RadioButton radio2;
	private RadioButton radio3;
	private RadioButton radio4;

	private RelativeLayout rl_sound1;
	private RelativeLayout rl_sound2;
	private RelativeLayout rl_sound3;
	private RelativeLayout rl_sound4;

	private ImageView ibuttom_back;

	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private TextView title;

	SoundPool soundPool;

	HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();

	private int soundId;

	Intent intent;

	private String type;
	String resultstring;

	private int currentFragmentId=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_sound);
		initView();
		initData();

	}

	private void initView() {

		ibuttom_back = (ImageView) findViewById(R.id.public_img_back);
		ibuttom_back.setImageResource(R.mipmap.back);
		tv1 = (TextView) findViewById(R.id.tv_sound1);
		tv2 = (TextView) findViewById(R.id.tv_sound2);
		tv3 = (TextView) findViewById(R.id.tv_sound3);
		tv4 = (TextView) findViewById(R.id.tv_sound4);

		rl_sound1 = (RelativeLayout) findViewById(R.id.rl_sound1);
		rl_sound2 = (RelativeLayout) findViewById(R.id.rl_sound2);
		rl_sound3 = (RelativeLayout) findViewById(R.id.rl_sound3);
		rl_sound4 = (RelativeLayout) findViewById(R.id.rl_sound4);

		title = (TextView) findViewById(R.id.public_txt_title);

		radio1 = (RadioButton) findViewById(R.id.rd1);
		radio2 = (RadioButton) findViewById(R.id.rd2);
		radio3 = (RadioButton) findViewById(R.id.rd3);
		radio4 = (RadioButton) findViewById(R.id.rd4);

		ibuttom_back.setOnClickListener(this);

		// radio1.setOnClickListener(this);
		// radio2.setOnClickListener(this);
		// radio3.setOnClickListener(this);
		// radio4.setOnClickListener(this);

		rl_sound1.setOnClickListener(this);
		rl_sound2.setOnClickListener(this);
		rl_sound3.setOnClickListener(this);
		rl_sound4.setOnClickListener(this);


		findViewById(R.id.public_btn_set).setVisibility(View.GONE);
		findViewById(R.id.public_btn_filter).setVisibility(View.GONE);

	}

	private void initData() {

		resultstring = "";
		soundId = 0;
		type = "";
		intent = getIntent();

		type = intent.getStringExtra("type");
		currentFragmentId=intent.getIntExtra("currentFragmentId",0);

		if (type.equals("home") || type.equals("guest")) {

			soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5); // 
			soundMap.put(1, soundPool.load(this, R.raw.sound1, 1)); // 
			soundMap.put(2, soundPool.load(this, R.raw.sound2, 1));
			soundMap.put(3, soundPool.load(this, R.raw.sound3, 1));
			tv1.setText(R.string.set_sound1_txt);
			tv2.setText(R.string.set_sound2_txt);
			tv3.setText(R.string.set_sound3_txt);
			tv4.setText(R.string.set_nosound_txt);
			if (type.equals("home")) {
				title.setText(R.string.set_home_txt);
				soundId = PreferenceUtil.getInt(MyConstants.HOSTTEAMINDEX, 1);
			}
			if (type.equals("guest")) {
				title.setText(R.string.set_guest_txt);
				soundId = PreferenceUtil.getInt(MyConstants.GUESTTEAM, 2);
			}

			if (soundId == 1) {
				radio1.setChecked(true);
			}
			if (soundId == 2) {
				radio2.setChecked(true);
			}
			if (soundId == 3) {
				radio3.setChecked(true);
			}
			if (soundId == 4) {
				radio4.setChecked(true);
			}

		}

		if (type.equals("odd")) {

			title.setText(R.string.set_odd_txt);
			tv1.setText(R.string.set_asialet_txt);
			tv2.setText(R.string.set_euro_txt);
			tv3.setText(R.string.set_asiasize_txt);
			tv4.setText(R.string.set_oddghide_txt);

			boolean asize = PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false);
			boolean eur = PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, false);
			boolean alet = PreferenceUtil.getBoolean(MyConstants.RBSECOND, true);
			boolean noshow = PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false);

			radio1.setChecked(alet);
			radio2.setChecked(eur);
			radio3.setChecked(asize);
			radio4.setChecked(noshow);
			// Log.i("dd", R.string.valueOf(""+alet+eur+asize+noshow));

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_sound1:
			radio1.setChecked(true);
			radio2.setChecked(false);
			radio3.setChecked(false);
			radio4.setChecked(false);

			if (type.equals("home") || type.equals("guest")) {
				soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
			}
			soundId = 1;

			save();

			break;
		case R.id.rl_sound2:
			radio2.setChecked(true);
			radio1.setChecked(false);
			radio3.setChecked(false);
			radio4.setChecked(false);
			if (type.equals("home") || type.equals("guest")) {
				soundPool.play(soundMap.get(2), 1, 1, 0, 0, 1);
			}

			soundId = 2;
			save();
			break;
		case R.id.rl_sound3:
			radio3.setChecked(true);
			radio1.setChecked(false);
			radio2.setChecked(false);
			radio4.setChecked(false);

			if (type.equals("home") || type.equals("guest")) {
				soundPool.play(soundMap.get(3), 1, 1, 0, 0, 1);
			}

			soundId = 3;
			save();
			break;
		case R.id.rl_sound4:
			radio4.setChecked(true);
			radio1.setChecked(false);
			radio2.setChecked(false);
			radio3.setChecked(false);
			soundId = 4;
			save();
			break;

		case R.id.public_img_back:
			MobclickAgent.onEvent(mContext,"Football_Setting_HomeTeam_Goal_Exit");
			Intent intent = new Intent();
			intent.putExtra("resultType", resultstring);

			if (currentFragmentId==0){
				ImmediateFragment.imEventBus.post(currentFragmentId);
			}else if (currentFragmentId==1){
				ResultFragment.resultEventBus.post(currentFragmentId);

			}else if (currentFragmentId==2){
				L.i("102","赛程发送");
				ScheduleFragment.schEventBus.post(currentFragmentId);
			}else if (currentFragmentId==3){
				FocusFragment.focusEventBus.post(currentFragmentId);
			}

			setResult(Activity.RESULT_OK, intent);
			this.finish();
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			intent.putExtra("resultType", resultstring);
			if (currentFragmentId==0){
				ImmediateFragment.imEventBus.post(currentFragmentId);
			}else if (currentFragmentId==1){
				ResultFragment.resultEventBus.post(currentFragmentId);
			}else if (currentFragmentId==2){
				ScheduleFragment.schEventBus.post(currentFragmentId);
			}else if (currentFragmentId==3){
				FocusFragment.focusEventBus.post(currentFragmentId);
			}

			setResult(Activity.RESULT_OK, intent);
			this.finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private void save() {

		if (type.equals("home")) {
			PreferenceUtil.commitInt(MyConstants.HOSTTEAMINDEX, soundId);
			resultstring = (soundId == 4) ? getResources().getString(R.string.set_nosound_txt) : getResources().getString(R.string.set_sound_txt) + soundId;
		}

		if (type.equals("guest")) {
			PreferenceUtil.commitInt(MyConstants.GUESTTEAM, soundId);
			resultstring = (soundId == 4) ? getResources().getString(R.string.set_nosound_txt) : getResources().getString(R.string.set_sound_txt) + soundId;
		}

		if (type.equals("odd")) {

			PreferenceUtil.commitBoolean(MyConstants.RBSECOND, radio1.isChecked());
			PreferenceUtil.commitBoolean(MyConstants.RBOCOMPENSATE, radio2.isChecked());
			PreferenceUtil.commitBoolean(MyConstants.rbSizeBall, radio3.isChecked());
			PreferenceUtil.commitBoolean(MyConstants.RBNOTSHOW, radio4.isChecked());
			if (radio3.isChecked()) {
				resultstring = getResources().getString(R.string.set_asiasize_txt);

			}

			if (radio2.isChecked()) {
				resultstring = getResources().getString(R.string.set_euro_txt);

			}

			if (radio1.isChecked()) {
				resultstring = getResources().getString(R.string.set_asialet_txt);

			}

			if (radio4.isChecked()) {
				resultstring = getResources().getString(R.string.set_oddghide_txt);

			}
		}
	}
}
