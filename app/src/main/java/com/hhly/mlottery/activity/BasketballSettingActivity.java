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
import android.widget.ToggleButton;

import com.hhly.mlottery.R;
import com.hhly.mlottery.R.string;
import com.hhly.mlottery.frame.basketballframe.FocusBasketballFragment;
import com.hhly.mlottery.frame.basketballframe.ImmedBasketballFragment;
import com.hhly.mlottery.frame.basketballframe.ResultBasketballFragment;
import com.hhly.mlottery.frame.basketballframe.ScheduleBasketballFragment;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: BasketballSettingActivity 
 * @Description: 篮球设置
 * @author yixq
 * @date 2015-12-29 下午4:09:32
 */

public class BasketballSettingActivity extends BaseActivity implements OnClickListener,OnCheckedChangeListener {

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
	
	private ImageView mBack;
	
	String resultstring;
	Intent intent;
	private int mCurrentId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basketball_setting);
		
		initView();
		initData();
	}
	
	private void initView() {
		
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
		
		mBack = (ImageView) findViewById(R.id.ib_back);
		mBack.setOnClickListener(this);
		
	}

	private void initData(){

		mTb_score.setChecked(PreferenceUtil.getBoolean(MyConstants.HALF_FULL_SCORE, true));
		if (PreferenceUtil.getBoolean(MyConstants.HALF_FULL_SCORE,true)){
			mTb_score.setChecked(true);
		}else{
			mTb_score.setChecked(false);
		}

		mTb_Point_spread.setChecked(PreferenceUtil.getBoolean(MyConstants.SCORE_DIFFERENCE,true));
		if (PreferenceUtil.getBoolean(MyConstants.SCORE_DIFFERENCE,true)){
			mTb_Point_spread.setChecked(true);
		}else{
			mTb_Point_spread.setChecked(false);
		}

		mTb_single_score.setChecked(PreferenceUtil.getBoolean(MyConstants.SINGLE_SCORE,true));
		if (PreferenceUtil.getBoolean(MyConstants.SINGLE_SCORE,true)){
			mTb_single_score.setChecked(true);
		}else{
			mTb_single_score.setChecked(false);
		}

		mTb_ranking.setChecked(PreferenceUtil.getBoolean(MyConstants.HOST_RANKING,true));
		if (PreferenceUtil.getBoolean(MyConstants.HOST_RANKING,true)){
			mTb_ranking.setChecked(true);
		}else{
			mTb_ranking.setChecked(false);
		}

		resultstring = "";
		intent = getIntent();

		mCurrentId = intent.getIntExtra("currentfragment",0);
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
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_alet:
			MobclickAgent.onEvent(mContext,"Basketball_Setting_Alet");
			mRd_alet.setChecked(true);
			mRd_eur.setChecked(false);
			mRd_asize.setChecked(false);
			mRd_noshow.setChecked(false);
			save();
			break;
		case R.id.rl_eur:
			MobclickAgent.onEvent(mContext,"Basketball_Setting_Eur");
			mRd_alet.setChecked(false);
			mRd_eur.setChecked(true);
			mRd_asize.setChecked(false);
			mRd_noshow.setChecked(false);
			save();
			break;
		case R.id.rl_asize:
			MobclickAgent.onEvent(mContext,"Basketball_Setting_Asize");
			mRd_alet.setChecked(false);
			mRd_eur.setChecked(false);
			mRd_asize.setChecked(true);
			mRd_noshow.setChecked(false);
			save();
			break;
		case R.id.rl_noshow:
			MobclickAgent.onEvent(mContext,"Basketball_Setting_Noshow");
			mRd_alet.setChecked(false);
			mRd_eur.setChecked(false);
			mRd_asize.setChecked(false);
			mRd_noshow.setChecked(true);
			save();
			break;

		case R.id.ib_back:
			MobclickAgent.onEvent(mContext,"Basketball_Setting_Exit");
			Intent intent = new Intent();
			intent.putExtra("resultType", resultstring);

			if (mCurrentId==0){
				ImmedBasketballFragment.BasketImmedEventBus.post(mCurrentId);
			}else if (mCurrentId==1){
				ResultBasketballFragment.BasketResultEventBus.post(mCurrentId);
			}else if (mCurrentId==2){
//				L.i("102","赛程发送");
				ScheduleBasketballFragment.BasketScheduleEventBus.post(mCurrentId);
			}else if (mCurrentId==3){
				FocusBasketballFragment.BasketFocusEventBus.post(mCurrentId);
			}
//			ImmedBasketballFragment.BasketImmedEventBus.post(0);
			setResult(Activity.RESULT_OK,intent);
			finish();
			overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton cb , boolean ischecked){
		switch (cb.getId()) {
		case R.id.tb_score:
			MobclickAgent.onEvent(mContext,"Basketball_Setting_Score");
			PreferenceUtil.commitBoolean(MyConstants.HALF_FULL_SCORE, mTb_score.isChecked());
			break;

		case R.id.tb_Point_spread:
			MobclickAgent.onEvent(mContext,"Basketball_Setting_Spread");
			PreferenceUtil.commitBoolean(MyConstants.SCORE_DIFFERENCE, mTb_Point_spread.isChecked());
			break;

		case R.id.tb_single_score:
			MobclickAgent.onEvent(mContext,"Basketball_Setting_SingleScore");
			PreferenceUtil.commitBoolean(MyConstants.SINGLE_SCORE, mTb_single_score.isChecked());
			break;

		case R.id.tb_ranking:
			MobclickAgent.onEvent(mContext,"Basketball_Setting_Ranking");
			PreferenceUtil.commitBoolean(MyConstants.HOST_RANKING, mTb_ranking.isChecked());
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

			if (mCurrentId==0){
				ImmedBasketballFragment.BasketImmedEventBus.post(mCurrentId);
			}else if (mCurrentId==1){
				ResultBasketballFragment.BasketResultEventBus.post(mCurrentId);
			}else if (mCurrentId==2){
//				L.i("102","赛程发送");
				ScheduleBasketballFragment.BasketScheduleEventBus.post(mCurrentId);
			}else if (mCurrentId==3){
				FocusBasketballFragment.BasketFocusEventBus.post(mCurrentId);
			}

			setResult(Activity.RESULT_OK,intent);
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
			resultstring = getResources().getString(string.set_asialet_txt);
		}
		if (mRd_asize.isChecked()) {
			resultstring = getResources().getString(string.set_asiasize_txt);
		}
		if (mRd_eur.isChecked()) {
			resultstring = getResources().getString(string.set_euro_txt);
		}
		if (mRd_noshow.isChecked()) {
			resultstring = getResources().getString(string.set_oddghide_txt);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("BasketballSettingActivity");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("BasketballSettingActivity");
	}
}
