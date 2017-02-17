package com.hhly.mlottery.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.cpifrag.CpiFragment;
import com.hhly.mlottery.frame.datafrag.DataFragment;
import com.hhly.mlottery.frame.homefrag.HomeFragment;
import com.hhly.mlottery.frame.infofrag.InfoFragment;
import com.hhly.mlottery.frame.scorefrag.ScoreFragment;
import com.hhly.mlottery.util.FragmentUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends BaseActivity {

    public final static int HOME_FRAGMENT = 0; //首页
    public final static int SCORE_FRAGMENT = 1;   //比分
    public final static int INFO_FRAGMENT = 2;   //情报
    public final static int CPI_FRAGMENT = 3;  //指数
    public final static int DATA_FRAGMENT = 4;    //资料库
    private Context mContext;

    private RadioGroup mRadioGroup;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    public int currentPosition = 0;// 足球界面Fragment下标
    public int fragmentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = IndexActivity.this;

        setContentView(R.layout.activity_index);

        initView();
        initData();
    }


    /**
     * 初始化界面View
     */
    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        fragments.add(new HomeFragment());
        fragments.add(new ScoreFragment());
        fragments.add(new InfoFragment());
        fragments.add(new CpiFragment());
        fragments.add(new DataFragment());
    }

    private void initData() {
        switch (currentPosition) {
            case HOME_FRAGMENT:
                switchFragment(HOME_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_home)).setChecked(true);
                break;
            case SCORE_FRAGMENT:
                switchFragment(SCORE_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_score)).setChecked(true);
                break;
            case INFO_FRAGMENT:
                switchFragment(INFO_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_info)).setChecked(true);
                break;
            case CPI_FRAGMENT:
                switchFragment(CPI_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_cpi)).setChecked(true);
                break;
            case DATA_FRAGMENT:
                switchFragment(DATA_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_data)).setChecked(true);
                break;

            default:
                break;
        }

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.rb_home:
                        MobclickAgent.onEvent(mContext, "Football_Score");
                        currentPosition = HOME_FRAGMENT;
                        switchFragment(HOME_FRAGMENT);
                        break;
                    case R.id.rb_score:
                        MobclickAgent.onEvent(mContext, "Football_News");
//
                        currentPosition = SCORE_FRAGMENT;
                        switchFragment(SCORE_FRAGMENT);

                        break;
                    case R.id.rb_info:
                        MobclickAgent.onEvent(mContext, "Football_Data");
//                        if (currentPosition == SCORES_FRAGMENT) {
//                            ((ScoresFragment) currentFragment).disconnectWebSocket();
//                        }
                        currentPosition = INFO_FRAGMENT;
                        switchFragment(INFO_FRAGMENT);
                        break;
                    case R.id.rb_cpi:
                        MobclickAgent.onEvent(mContext, "Football_Video");
//                        if (currentPosition == SCORES_FRAGMENT) {
//                            ((ScoresFragment) currentFragment).disconnectWebSocket();
//                        }
                        currentPosition = CPI_FRAGMENT;
                        switchFragment(CPI_FRAGMENT);

                        break;
                    case R.id.rb_data:
                        MobclickAgent.onEvent(mContext, "Football_CPI");
//                        if (currentPosition == SCORES_FRAGMENT) {
//                            ((ScoresFragment) currentFragment).disconnectWebSocket();
//                        }
                        currentPosition = DATA_FRAGMENT;
                        switchFragment(DATA_FRAGMENT);
                        break;
                    default:
                        break;
                }

                //测试切换到其他页面能否收到推送消息
                //EventBus.getDefault().post(new ScoreFragmentWebSocketEntity(currentPosition));

            }
        });
    }

    public void switchFragment(int position) {
        fragmentIndex = position;// 当前fragment下标
        L.d("xxx", "当前Fragment下标：" + fragmentIndex);
        fragmentManager = getSupportFragmentManager();
        currentFragment = FragmentUtils.switchFragment(fragmentManager, R.id.ly_content, currentFragment, fragments.get(position).getClass(), null, false, fragments.get(position).getClass().getSimpleName() + position, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    private long mExitTime;// 退出程序...时间


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                UiUtils.toast(this, getResources().getString(R.string.main_exit_text), 1000);
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);// 退出APP
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
