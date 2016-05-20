package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.basketballframe.ImmedBasketballFragment;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * @Description: 篮球比分列表页面的 Activity
 * @author yixq
 * Created by A on 2016/4/7.
 */
public class BasketListActivity extends FragmentActivity implements View.OnClickListener {

    private final static String TAG = "BasketballFragment";

    private String CHECKED_FRAGMENT = "checked_fragment";
    private String BASKET_HOME_TYPE = "basketball";

    private int mHomeType; //首次进入显示状态值

    // private View view;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frage_basketball);
        /**
         * 获得首次进入时候的状态值
         */
        if (getIntent().getExtras() != null) {
            mHomeType = getIntent().getExtras().getInt(BASKET_HOME_TYPE);
            L.d("BASKET_HOME_TYPE : ", mHomeType + "");
        }

        initView();
        focusCallback();// 加载关注数
    }

    private void initView() {
        mImmediateBtn = (Button) findViewById(R.id.football_footer_immediate_btn);
        mResultBtn = (Button) findViewById(R.id.football_footer_result_btn);
        mScheduleBtn = (Button) findViewById(R.id.football_footer_schedule_btn);
        mFocusBtn = (Button) findViewById(R.id.football_footer_focus_btn);

        mImmediateBtn.setOnClickListener(this);
        mResultBtn.setOnClickListener(this);
        mScheduleBtn.setOnClickListener(this);
        mFocusBtn.setOnClickListener(this);

        if (mHomeType == 0) {
            mImmediateBtn.setSelected(true);// 默认选中’即时‘
            mImmediateBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_selected));
            PreferenceUtil.commitInt(CHECKED_FRAGMENT, 0);//设置选中的fragment;
            mImmediateFragment = ImmedBasketballFragment.newInstance(ImmedBasketballFragment.TYPE_IMMEDIATE);
            fragmentManager = getSupportFragmentManager();// getChildFragmentManager();
            fragmentManager.beginTransaction().add(R.id.content_fragment0, mImmediateFragment, lastFragmentTag).commit();
            mImmediateBtn.setClickable(false);// 默认让’即时‘不可点
            isfilsttime = ISIMMEDIATE;
        } else if (mHomeType == 1) {
            mResultBtn.setSelected(true);// 默认选中’赛果‘
            mResultBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_selected));
            PreferenceUtil.commitInt(CHECKED_FRAGMENT, 1);//设置选中的fragment;
            mResultFragment = ImmedBasketballFragment.newInstance(ImmedBasketballFragment.TYPE_RESULT);
            fragmentManager = getSupportFragmentManager();// getChildFragmentManager();
            fragmentManager.beginTransaction().add(R.id.content_fragment0, mResultFragment, lastFragmentTag).commit();
            mResultBtn.setClickable(false);// 默认让’赛果‘不可点
            isfilsttime = ISRESULT;
        } else if (mHomeType == 2) {
            mScheduleBtn.setSelected(true);// 默认选中’赛程‘
            mScheduleBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_selected));
            PreferenceUtil.commitInt(CHECKED_FRAGMENT, 2);//设置选中的fragment;
            mScheduleFragment = ImmedBasketballFragment.newInstance(ImmedBasketballFragment.TYPE_SCHEDULE);
            fragmentManager = getSupportFragmentManager();// getChildFragmentManager();
            fragmentManager.beginTransaction().add(R.id.content_fragment0, mScheduleFragment, lastFragmentTag).commit();
            mScheduleBtn.setClickable(false);// 默认让’赛程‘不可点
            isfilsttime = ISSCHEDULE;
        } else if (mHomeType == 3) {
            mFocusBtn.setSelected(true);// 默认选中’关注‘
            mFocusBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_selected));
            PreferenceUtil.commitInt(CHECKED_FRAGMENT, 3);//设置选中的fragment;
            mFocusFragment = ImmedBasketballFragment.newInstance(ImmedBasketballFragment.TYPE_FOCUS);
            fragmentManager = getSupportFragmentManager();// getChildFragmentManager();
            fragmentManager.beginTransaction().add(R.id.content_fragment0, mFocusFragment, lastFragmentTag).commit();
            mFocusBtn.setClickable(false);// 默认让’关注‘不可点
            isfilsttime = ISFOCUS;
        }
        mRedPoint = (ImageView) findViewById(R.id.football_footer_focus_redpoint);
    }

    /**四个Fragment页面统计*/
    private boolean isImmediate = false;
    private boolean isResult = false;
    private boolean isSchedule = false;
    private boolean isFocus = false;
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
                MobclickAgent.onEvent(mContext, "Basketball_immediate");
                if (isResult) {
                    MobclickAgent.onPageEnd("Basketball_ResultFragment");
                    isResult = false;
                    L.d("zzz", "Basketball_ResultFragment:隐藏");
                }
                if (isSchedule) {
                    MobclickAgent.onPageEnd("Basketball_ScheduleFragment");
                    isSchedule = false;
                    L.d("zzz", "Basketball_ScheduleFragment:隐藏");
                }
                if (isFocus) {
                    MobclickAgent.onPageEnd("Basketball_FocusFragment");
                    isFocus = false;
                    L.d("zzz", "Basketball_FocusFragment:隐藏");
                }
                MobclickAgent.onPageStart("Basketball_ImmediateFragment");
                isImmediate = true;
                L.d("zzz", "Basketball_ImmediateFragment:显示");
                isfilsttime = ISIMMEDIATE;

                mImmediateBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_selected));
                mResultBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
                mScheduleBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
                mFocusBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));

                mImmediateBtn.setClickable(false);
                mResultBtn.setClickable(true);
                mScheduleBtn.setClickable(true);
                mFocusBtn.setClickable(true);

                if (mImmediateFragment == null) {
                    mImmediateFragment = ImmedBasketballFragment.newInstance(ImmedBasketballFragment.TYPE_IMMEDIATE);
                    fragmentManager.beginTransaction().add(R.id.content_fragment0, mImmediateFragment, lastFragmentTag).commit();
                } else {
                    fragmentManager.beginTransaction().show(mImmediateFragment).commit();
                }

                break;
            case R.id.football_footer_result_btn: // 赛果
                MobclickAgent.onEvent(mContext, "Basketball_Result");
                if (isImmediate) {
                    MobclickAgent.onPageEnd("Basketball_ImmediateFragment");
                    isImmediate = false;
                    L.d("zzz", "Basketball_ImmediateFragment:隐藏");
                }
                if (isSchedule) {
                    MobclickAgent.onPageEnd("Basketball_ScheduleFragment");
                    isSchedule = false;
                    L.d("zzz", "Basketball_ScheduleFragment:隐藏");
                }
                if (isFocus) {
                    MobclickAgent.onPageEnd("Basketball_FocusFragment");
                    isFocus = false;
                    L.d("zzz", "Basketball_FocusFragment:隐藏");
                }
                MobclickAgent.onPageStart("Basketball_ResultFragment");
                isResult = true;
                L.d("zzz", "Basketball_ResultFragment:显示");
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

                if (mResultFragment == null) {
                    mResultFragment = ImmedBasketballFragment.newInstance(ImmedBasketballFragment.TYPE_RESULT);
                    fragmentManager.beginTransaction().add(R.id.content_fragment0, mResultFragment, lastFragmentTag).commit();
                } else {
                    fragmentManager.beginTransaction().show(mResultFragment).commit();
                }

                break;
            case R.id.football_footer_schedule_btn: // 赛程
                MobclickAgent.onEvent(mContext, "Basketball_Schedule");
                if (isImmediate) {
                    MobclickAgent.onPageEnd("Basketball_ImmediateFragment");
                    isImmediate = false;
                    L.d("zzz", "Basketball_ImmediateFragment:隐藏");
                }
                if (isResult) {
                    MobclickAgent.onPageEnd("Basketball_ResultFragment");
                    isResult = false;
                    L.d("zzz", "Basketball_ResultFragment:隐藏");
                }
                if (isFocus) {
                    MobclickAgent.onPageEnd("Basketball_FocusFragment");
                    isFocus = false;
                    L.d("zzz", "Basketball_FocusFragment:隐藏");
                }
                MobclickAgent.onPageStart("Basketball_ScheduleFragment");
                isSchedule = true;
                L.d("zzz", "Basketball_ScheduleFragment:显示");
                isfilsttime = ISSCHEDULE;

                mImmediateBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
                mResultBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
                mScheduleBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_selected));
                mFocusBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));

                mImmediateBtn.setClickable(true);
                mResultBtn.setClickable(true);
                mScheduleBtn.setClickable(false);
                mFocusBtn.setClickable(true);

                if (mScheduleFragment == null) {
                    mScheduleFragment = ImmedBasketballFragment.newInstance(ImmedBasketballFragment.TYPE_SCHEDULE);
                    fragmentManager.beginTransaction().add(R.id.content_fragment0, mScheduleFragment, lastFragmentTag).commit();
                } else {
                    fragmentManager.beginTransaction().show(mScheduleFragment).commit();
                }
                break;
            case R.id.football_footer_focus_btn: // 关注
                MobclickAgent.onEvent(mContext, "Basketball_Focus");
                if (isImmediate) {
                    MobclickAgent.onPageEnd("Basketball_ImmediateFragment");
                    isImmediate = false;
                    L.d("zzz", "Basketball_ImmediateFragment:隐藏");
                }
                if (isResult) {
                    MobclickAgent.onPageEnd("Basketball_ResultFragment");
                    isResult = false;
                    L.d("zzz", "Basketball_ResultFragment:隐藏");
                }
                if (isSchedule) {
                    MobclickAgent.onPageEnd("Basketball_ScheduleFragment");
                    isSchedule = false;
                    L.d("zzz", "Basketball_ScheduleFragment:隐藏");
                }
                MobclickAgent.onPageStart("Basketball_FocusFragment");
                isFocus = true;
                L.d("zzz", "Basketball_FocusFragment:显示");
                isfilsttime = ISFOCUS;

                mImmediateBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
                mResultBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
                mScheduleBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_normal));
                mFocusBtn.setTextColor(getResources().getColor(R.color.textcolor_football_footer_selected));

                mImmediateBtn.setClickable(true);
                mResultBtn.setClickable(true);
                mScheduleBtn.setClickable(true);
                mFocusBtn.setClickable(false);

                mFocusFragment = ImmedBasketballFragment.newInstance(ImmedBasketballFragment.TYPE_FOCUS);
                fragmentManager.beginTransaction().add(R.id.content_fragment0, mFocusFragment, lastFragmentTag).commit();
                break;
            default:
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mImmediateBtn.isSelected()) {
            MobclickAgent.onPageStart("Basketball_ImmediateFragment");
            isImmediate = true;
            L.d("zzz", "Basketball_ImmediateFragment:显示");
        }
        if (mResultBtn.isSelected()) {
            MobclickAgent.onPageStart("Basketball_ResultFragment");
            isResult = true;
            L.d("zzz", "Basketball_ResultFragment:显示");
        }
        if (mScheduleBtn.isSelected()) {
            MobclickAgent.onPageStart("Basketball_ScheduleFragment");
            isSchedule = true;
            L.d("zzz", "Basketball_ScheduleFragment:显示");
        }
        if (mFocusBtn.isSelected()) {
            MobclickAgent.onPageStart("Basketball_FocusFragment");
            isFocus = true;
            L.d("zzz", "Basketball_FocusFragment:显示");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mImmediateBtn.isSelected()) {
            MobclickAgent.onPageEnd("Basketball_ImmediateFragment");
            isImmediate = false;
            L.d("zzz", "Basketball_ImmediateFragment:隐藏");
        }
        if (mResultBtn.isSelected()) {
            MobclickAgent.onPageEnd("Basketball_ResultFragment");
            isResult = false;
            L.d("zzz", "Basketball_ResultFragment:隐藏");
        }
        if (mScheduleBtn.isSelected()) {
            MobclickAgent.onPageEnd("Basketball_ScheduleFragment");
            isSchedule = false;
            L.d("zzz", "Basketball_ScheduleFragment:隐藏");
        }
        if (mFocusBtn.isSelected()) {
            MobclickAgent.onPageEnd("Basketball_FocusFragment");
            isFocus = false;
            L.d("zzz", "Basketball_FocusFragment:隐藏");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("BasketballFragment", "___onActivityResult___");


        if (requestCode == ImmedBasketballFragment.REQUEST_FILTERCODE || requestCode == ImmedBasketballFragment.REQUEST_SETTINGCODE) {
            switch (isfilsttime) {
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
    }

    /**
     * 改Activity容易被销毁时（如异常退出）执行，用户主动销毁时（back键返回）不执行
     * 重写onSaveInstanceState ,不执行super方法（异常退出时不让系统保留原有fragment）
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    public void focusCallback() {
        String focusIds = PreferenceUtil.getString("basket_focus_ids", "");
        String[] arrayId = focusIds.split("[,]");
        if ("".equals(focusIds) || arrayId.length == 0) {
            mRedPoint.setVisibility(View.GONE);
        } else {
            mRedPoint.setVisibility(View.VISIBLE);
        }
    }
}
