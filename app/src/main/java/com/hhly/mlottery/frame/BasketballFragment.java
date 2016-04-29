package com.hhly.mlottery.frame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.basketballframe.ImmedBasketballFragment;
import com.hhly.mlottery.util.PreferenceUtil;

/**
 * 
 * @ClassName: BasketballFragment 
 * @Description: 篮球
 * @author yixq
 * @date 2015-12-25 下午6:38:31
 */
@SuppressLint("NewApi")
public class BasketballFragment extends Fragment implements OnClickListener {

	private final static String TAG = "BasketballFragment";

	private String CHECKED_FRAGMENT = "checked_fragment";

	private View view;
	FragmentManager fragmentManager;
//	Fragment fragment;

	Fragment mImmediateFragment;
	Fragment mResultFragment;
	Fragment mScheduleFragment;
	Fragment mFocusFragment;

	private Button mImmediateBtn;
	private Button mResultBtn;
	private Button mScheduleBtn;
	private Button mFocusBtn;

	private Context mContext;

	private String lastFragmentTag = "lastFragmentTag";

	private ImageView mRedPoint;

	private int isfilsttime = 0;//区分是那个界面的设置&筛选
	//当前是哪个界面
	public final int ISIMMEDIATE = 1;
	public final int ISRESULT = 2;
	public final int ISSCHEDULE = 3;
	public final int ISFOCUS = 4;
	// addtjl
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity();
		view = View.inflate(mContext, R.layout.frage_basketball, null);
		initView();

		focusCallback();// 加载关注数
		return view;
	}

	private void initView() {
		mImmediateBtn = (Button) view.findViewById(R.id.football_footer_immediate_btn);
		mResultBtn = (Button) view.findViewById(R.id.football_footer_result_btn);
		mScheduleBtn = (Button) view.findViewById(R.id.football_footer_schedule_btn);
		mFocusBtn = (Button) view.findViewById(R.id.football_footer_focus_btn);

		mImmediateBtn.setOnClickListener(this);
		mResultBtn.setOnClickListener(this);
		mScheduleBtn.setOnClickListener(this);
		mFocusBtn.setOnClickListener(this);

		mImmediateBtn.setSelected(true);// 默认选中’即时‘
		mImmediateBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_selected));

//		fragment = new ImmediateFragment();// 默认选中’即时‘
		PreferenceUtil.commitInt(CHECKED_FRAGMENT, 0);//设置选中的fragment;

		mImmediateFragment = new ImmedBasketballFragment(); //篮球即时
		fragmentManager = getChildFragmentManager();
//		fragmentManager.beginTransaction().replace(R.id.content_fragment0, fragment, lastFragmentTag).commit();
		fragmentManager.beginTransaction().add(R.id.content_fragment0, mImmediateFragment, lastFragmentTag).commit();
		mImmediateBtn.setClickable(false);// 默认让’即时‘不可点
		isfilsttime = ISIMMEDIATE;

		mRedPoint = (ImageView)view.findViewById(R.id.football_footer_focus_redpoint);
	}

	@Override
	public void onClick(View v) {
		mImmediateBtn.setSelected(false);
		mResultBtn.setSelected(false);
		mScheduleBtn.setSelected(false);
		mFocusBtn.setSelected(false);

		v.setSelected(true);

		if (mImmediateFragment != null) {
			fragmentManager.beginTransaction().hide(mImmediateFragment).commit();
		}
		if (mResultFragment != null) {
			fragmentManager.beginTransaction().hide(mResultFragment).commit();
		}
		if (mScheduleFragment != null) {
			fragmentManager.beginTransaction().hide(mScheduleFragment).commit();
		}
		if (mFocusFragment != null) {
			fragmentManager.beginTransaction().hide(mFocusFragment).commit();
		}


		switch (v.getId()) {
		case R.id.football_footer_immediate_btn:// 即时
			isfilsttime = ISIMMEDIATE;

			mImmediateBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_selected));
			mResultBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
			mScheduleBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
			mFocusBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));

			mImmediateBtn.setClickable(false);
			mResultBtn.setClickable(true);
			mScheduleBtn.setClickable(true);
			mFocusBtn.setClickable(true);

//			fragment = new ImmediateFragment();
//			SpTools.putInt(mContext,CHECKED_FRAGMENT,);//设置选中的fragment;
//			fragment = ImmedBasketballFragment.newInstance(ImmedBasketballFragment.TYPE_IMMEDIATE);

			if (mImmediateFragment == null) {
				mImmediateFragment = ImmedBasketballFragment.newInstance(ImmedBasketballFragment.TYPE_IMMEDIATE);
				fragmentManager.beginTransaction().add(R.id.content_fragment0, mImmediateFragment, lastFragmentTag).commit();
			}else{
				fragmentManager.beginTransaction().show(mImmediateFragment).commit();
			}

			break;
		case R.id.football_footer_result_btn: // 赛果
			isfilsttime = ISRESULT;

			mImmediateBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
			mResultBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_selected));
			mScheduleBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
			mFocusBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));

			// 当点击后设置不可点
			mImmediateBtn.setClickable(true);
			mResultBtn.setClickable(false);
			mScheduleBtn.setClickable(true);
			mFocusBtn.setClickable(true);

//			fragment = new ResultFragment();
//			fragment = new ResultBasketballFragment();
//			SpTools.putInt(mContext,CHECKED_FRAGMENT,1);//设置选中的fragment;
			if (mResultFragment == null) {
				mResultFragment = ImmedBasketballFragment.newInstance(ImmedBasketballFragment.TYPE_RESULT);
				fragmentManager.beginTransaction().add(R.id.content_fragment0, mResultFragment, lastFragmentTag).commit();
			}else{
				fragmentManager.beginTransaction().show(mResultFragment).commit();
			}

			break;
		case R.id.football_footer_schedule_btn: // 赛程
			isfilsttime = ISSCHEDULE;

			mImmediateBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
			mResultBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
			mScheduleBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_selected));
			mFocusBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));

			mImmediateBtn.setClickable(true);
			mResultBtn.setClickable(true);
			mScheduleBtn.setClickable(false);
			mFocusBtn.setClickable(true);
//			fragment = new ScheduleFragment();
//			fragment = new ScheduleBasketballFragment();

//			SpTools.putInt(mContext,CHECKED_FRAGMENT,2);//设置选中的fragment;
//			fragment = ImmedBasketballFragment.newInstance(ImmedBasketballFragment.TYPE_SCHEDULE);

			if (mScheduleFragment == null) {
				mScheduleFragment = ImmedBasketballFragment.newInstance(ImmedBasketballFragment.TYPE_SCHEDULE);
				fragmentManager.beginTransaction().add(R.id.content_fragment0, mScheduleFragment, lastFragmentTag).commit();
			}else{
				fragmentManager.beginTransaction().show(mScheduleFragment).commit();
			}
			break;
		case R.id.football_footer_focus_btn: // 关注
			isfilsttime = ISFOCUS;

			mImmediateBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
			mResultBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
			mScheduleBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
			mFocusBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_selected));

			mImmediateBtn.setClickable(true);
			mResultBtn.setClickable(true);
			mScheduleBtn.setClickable(true);
			mFocusBtn.setClickable(false);

//			fragment = new FocusFragment();
			mFocusFragment = ImmedBasketballFragment.newInstance(ImmedBasketballFragment.TYPE_FOCUS);
			fragmentManager.beginTransaction().add(R.id.content_fragment0, mFocusFragment, lastFragmentTag).commit();
			break;
		default:
			break;
		}
//		fragmentManager.beginTransaction().replace(R.id.content_fragment0, fragment, lastFragmentTag).commit();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.d("BasketballFragment", "___onActivityResult___");

//		if (requestCode == ImmedBasketballFragment.REQUEST_FILTERCODE || requestCode == ImmedBasketballFragment.REQUEST_SETTINGCODE) {
//			fragmentManager = getChildFragmentManager();
//			Fragment fragment = fragmentManager.findFragmentByTag(lastFragmentTag);
//			fragment.onActivityResult(requestCode, resultCode, data);
//		}

		if (requestCode == ImmedBasketballFragment.REQUEST_FILTERCODE || requestCode == ImmedBasketballFragment.REQUEST_SETTINGCODE) {
			switch (isfilsttime){
				case ISIMMEDIATE:
					if (mImmediateFragment != null) {
						mImmediateFragment.onActivityResult(requestCode, resultCode, data);
					}
					break;
				case ISRESULT:
					if (mResultFragment != null) {
						mResultFragment.onActivityResult(requestCode, resultCode, data);
					}
					break;
				case ISSCHEDULE:
					if (mScheduleFragment != null) {
						mScheduleFragment.onActivityResult(requestCode, resultCode, data);
					}
					break;
				case ISFOCUS:
					if (mFocusFragment != null) {
						mFocusFragment.onActivityResult(requestCode, resultCode, data);
					}
					break;
				default:
					break;
			}
		}
//		else if(requestCode == ImmedBasketballFragment.REQUEST_SETTINGCODE){
//			if (mImmediateFragment != null) {
//				mImmediateFragment.onActivityResult(requestCode, resultCode, data);
//			}
//			if (mResultFragment != null) {
//				mResultFragment.onActivityResult(requestCode, resultCode, data);
//			}
//			if (mScheduleFragment != null) {
//				mScheduleFragment.onActivityResult(requestCode, resultCode, data);
//			}
//			if (mFocusFragment != null) {
//				mFocusFragment.onActivityResult(requestCode, resultCode, data);
//			}
//		}

	}

	public void focusCallback() {
		String focusIds = PreferenceUtil.getString("basket_focus_ids", "");
		String[] arrayId = focusIds.split("[,]");
			if ("".equals(focusIds)||arrayId.length == 0) {
				mRedPoint.setVisibility(View.GONE);
			} else {
				mRedPoint.setVisibility(View.VISIBLE);
			}
	}
}
