package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.numbersframe.CurrentNumberFragment;
import com.hhly.mlottery.frame.numbersframe.HKLotteryChartFragment;
import com.hhly.mlottery.frame.numbersframe.HKLotteryStartFragment;
import com.hhly.mlottery.frame.numbersframe.HistoryNumberFragment;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.NumberDataUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 开奖详情页面展示
 *
 * @author Tenney
 * @ClassName: NumbersInfoActivity
 * @Description: 彩票详情数据展示页
 * @date 2015-10-19 下午4:24:45
 */
public class NumbersInfoBaseActivity extends BaseActivity implements OnClickListener {

    private String mNumberName;// 彩种名称

    private FrameLayout current;
    private FrameLayout historyh;
    private HistoryNumberFragment historyhFragment;
    private static CurrentNumberFragment currentFragment;
    private TextView public_txt_title;

    public boolean isHistoryPager;
    private LinearLayout ll_bottom_menu;// 底部导航菜单
    private ImageView public_btn_set;
    private FrameLayout fl_numberContext_info;// 彩票详情数据
    private FrameLayout fl_other_content;// 统计和图表显示
    public AppCompatSpinner public_txt_spinner;// 菜单下拉器

    private HKLotteryStartFragment hkLotteryStartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取传递的数据
        mNumberName = null;
        mNumberName = getIntent().getStringExtra(AppConstants.LOTTERY_KEY);
        /**不统计当前Activity*/
        MobclickAgent.openActivityDurationTrack(false);

        setContentView(R.layout.numbers_main_info);

        initView();
        initData();
        initFragment();
    }

    private void initFragment() {
        isHistoryPager = false;
        currentFragment = new CurrentNumberFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mNumberName", mNumberName);
        currentFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_numberContext_current, currentFragment).commit();

    }

    private void initData() {
        settingTitle();
    }

    // 设置标题名
    private void settingTitle(){
        if (!TextUtils.isEmpty(mNumberName)) {
            NumberDataUtils.setTextTitle(mContext, public_txt_title, mNumberName);// 设置开奖标题
        } else {
            public_txt_title.setText("  ");
        }
    }

    private void initView() {

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setImageResource(R.mipmap.number_history_icon);
        public_btn_set.setOnClickListener(this);
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);

        current = $(R.id.fl_numberContext_current);
        historyh = $(R.id.fl_numberContext_historyh);
        MobclickAgent.onEvent(mContext, "Lottery_Info_News");

        ll_bottom_menu = (LinearLayout) findViewById(R.id.ll_bottom_menu);
        fl_numberContext_info = (FrameLayout) findViewById(R.id.fl_numberContext_info);
        fl_other_content = (FrameLayout) findViewById(R.id.fl_other_content);
        findViewById(R.id.rb_open_lottery).setOnClickListener(this);
        ((RadioButton) findViewById(R.id.rb_open_lottery)).setChecked(true);
        findViewById(R.id.rb_statistics).setOnClickListener(this);
        findViewById(R.id.rb_chart).setOnClickListener(this);
        public_txt_spinner = (AppCompatSpinner) findViewById(R.id.public_txt_spinner);

        if("1".equals(mNumberName)){
            ll_bottom_menu.setVisibility(View.VISIBLE);
        }else{
            ll_bottom_menu.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back:// 关闭
                MobclickAgent.onEvent(mContext, "Lottery_Info_Exit");

                if (isHistoryPager) {// 如果当前为历史详情，则返回到历史列表
                    historyhFragment.setValue(true);
                    current.setVisibility(View.GONE);
                    historyh.setVisibility(View.VISIBLE);
                    isHistoryPager = false;
                } else {
                    if (current.getVisibility() == View.GONE) {
                        current.setVisibility(View.VISIBLE);
                        historyh.setVisibility(View.GONE);
                    } else {
                        finish();
                    }
                }
                break;
            case R.id.public_btn_set:// 历史开奖
                MobclickAgent.onEvent(mContext, "Lottery_Info_History");
                isHistoryPager = true;
                current.setVisibility(View.GONE);
                historyh.setVisibility(View.VISIBLE);

                historyhFragment = new HistoryNumberFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mNumberName", mNumberName);
                historyhFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_numberContext_historyh, historyhFragment).commit();

                break;
            case R.id.rb_open_lottery:
                fl_numberContext_info.setVisibility(View.VISIBLE);
                public_btn_set.setVisibility(View.VISIBLE);
                fl_other_content.setVisibility(View.GONE);
                public_txt_spinner.setVisibility(View.GONE);
                settingTitle();
                break;
            case R.id.rb_statistics:
                fl_numberContext_info.setVisibility(View.GONE);
                public_btn_set.setVisibility(View.GONE);
                fl_other_content.setVisibility(View.VISIBLE);
                public_txt_spinner.setVisibility(View.VISIBLE);
                public_txt_title.setText(mContext.getResources().getString(R.string.home_lottery_info_start_title));

                hkLotteryStartFragment = new HKLotteryStartFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_other_content, hkLotteryStartFragment).commit();

                break;
            case R.id.rb_chart:
                fl_numberContext_info.setVisibility(View.GONE);
                public_btn_set.setVisibility(View.GONE);
                fl_other_content.setVisibility(View.VISIBLE);
                public_txt_spinner.setVisibility(View.GONE);
                settingTitle();

                getSupportFragmentManager().beginTransaction().replace(R.id.fl_other_content, new HKLotteryChartFragment()).commit();

                break;
        }
    }
}
