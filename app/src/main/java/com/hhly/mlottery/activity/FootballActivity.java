package com.hhly.mlottery.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.CPIFragment;
import com.hhly.mlottery.frame.CounselFragment;
import com.hhly.mlottery.frame.ScoresFragment;
import com.hhly.mlottery.frame.VideoFragment;
import com.hhly.mlottery.frame.footframe.FocusFragment;
import com.hhly.mlottery.frame.footframe.ImmediateFragment;
import com.hhly.mlottery.frame.footframe.InformationFragment;
import com.hhly.mlottery.frame.footframe.ResultFragment;
import com.hhly.mlottery.frame.footframe.ScheduleFragment;
import com.hhly.mlottery.frame.oddfragment.BasketScoresFragment;
import com.hhly.mlottery.util.FragmentUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/****
 * @author wangg
 * @name:FootballActivity
 * @describe:足球（比分、资讯、数据、视频、指数）
 */
public class FootballActivity extends BaseActivity {

    private final static int SCORES_FRAGMENT = 0; //比分
    private final static int NEWS_FRAGMENT = 1;   //资讯
    private final static int DATA_FRAGMENT = 2;   //数据
    private final static int VIDEO_FRAGMENT = 3;  //视频
    private final static int CPI_FRAGMENT = 4;    //指数
    private final static int BASKET_FRAGMENT = 5;    //篮球
    private Context mContext;

    private RadioGroup mRadioGroup;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private int currentPosition = 0;
    public LinearLayout ly_tab_bar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_basefootball);
        this.mContext = this;
        MobclickAgent.openActivityDurationTrack(true);
        currentPosition = getIntent().getIntExtra("football", 0);
        initView();
        initData();
    }

    /**
     * 初始化界面View
     */
    private void initView() {
        ly_tab_bar = (LinearLayout) findViewById(R.id.ly_tab_bar);
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        fragments.add(new ScoresFragment());
        fragments.add(new CounselFragment());
        fragments.add(new InformationFragment());
        fragments.add(new VideoFragment());
        fragments.add(CPIFragment.newInstance());
        fragments.add(new BasketScoresFragment());
    }

    private void initData() {
        switch (currentPosition) {
            case SCORES_FRAGMENT:
                switchFragment(SCORES_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_football)).setChecked(true);
                break;
            case NEWS_FRAGMENT:
                switchFragment(NEWS_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_news)).setChecked(true);
                break;
            case DATA_FRAGMENT:
                switchFragment(DATA_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_data)).setChecked(true);
                break;
            case VIDEO_FRAGMENT:
                switchFragment(VIDEO_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_video)).setChecked(true);
                break;
            case CPI_FRAGMENT:
                switchFragment(CPI_FRAGMENT);
                ((RadioButton) findViewById(R.id.rb_cpi)).setChecked(true);
                break;
            case BASKET_FRAGMENT:
                switchFragment(BASKET_FRAGMENT);
                ly_tab_bar.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.rb_football:
                        MobclickAgent.onEvent(mContext, "Football_Score");
                        currentPosition = SCORES_FRAGMENT;
                        switchFragment(SCORES_FRAGMENT);
                        break;
                    case R.id.rb_news:
                        MobclickAgent.onEvent(mContext, "Football_News");
                        currentPosition = NEWS_FRAGMENT;
                        switchFragment(NEWS_FRAGMENT);
                        break;
                    case R.id.rb_data:
                        MobclickAgent.onEvent(mContext, "Football_Data");
                        currentPosition = DATA_FRAGMENT;
                        switchFragment(DATA_FRAGMENT);
                        break;
                    case R.id.rb_video:
                        MobclickAgent.onEvent(mContext, "Football_Video");
                        currentPosition = VIDEO_FRAGMENT;
                        switchFragment(VIDEO_FRAGMENT);
                        break;
                    case R.id.rb_cpi:
                        MobclickAgent.onEvent(mContext, "Football_CPI");
                        currentPosition = CPI_FRAGMENT;
                        switchFragment(CPI_FRAGMENT);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void switchFragment(int position) {
        fragmentManager = getSupportFragmentManager();
        currentFragment = FragmentUtils.switchFragment(fragmentManager, R.id.ly_content, currentFragment,
                fragments.get(position).getClass(), null, false,
                fragments.get(position).getClass().getSimpleName() + position, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CpiFiltrateActivity.mCheckedIds.clear();
        CpiFiltrateActivity.isDefualHot = true;
        if (ImmediateFragment.imEventBus != null) {
            ImmediateFragment.imEventBus.unregister(ImmediateFragment.class);
        }
        if (ResultFragment.resultEventBus != null) {
            ResultFragment.resultEventBus.unregister(ResultFragment.class);
        }
        if (ScheduleFragment.schEventBus != null) {
            ScheduleFragment.schEventBus.unregister(ScheduleFragment.class);
        }
        if (FocusFragment.focusEventBus != null) {
            FocusFragment.focusEventBus.unregister(FocusFragment.class);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
