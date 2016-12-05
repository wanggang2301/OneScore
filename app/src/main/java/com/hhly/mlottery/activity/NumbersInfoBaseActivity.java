package com.hhly.mlottery.activity;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.numbersframe.CurrentNumberFragment;
import com.hhly.mlottery.frame.numbersframe.HistoryNumberFragment;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.NumberDataUtils;
import com.hhly.mlottery.util.ToastTools;
import com.umeng.analytics.MobclickAgent;

/**
 * 开奖详情页面展示
 *
 * @author Tenney
 * @ClassName: NumbersInfoActivity
 * @Description: TODO
 * @date 2015-10-19 下午4:24:45
 */
public class NumbersInfoBaseActivity extends BaseActivity implements
        OnClickListener {

    //    private ImageView tv_back;// 返回菜单
//    private TextView title;// 设置标题
//    private TextView currentNumberInfo;// 当前开奖
//    private TextView historyNumberInfo;// 历史开奖
    private FragmentManager fragmentManager;
    private String mNumberName;// 彩种名称

    private FrameLayout current;
    private FrameLayout historyh;
    private HistoryNumberFragment historyhFragment;
    private static CurrentNumberFragment currentFragment;
    private TextView public_txt_title;

    public boolean isHistoryPager;

//    public static CurrentNumberFragment getCurrentNumberFragment() {
//        return currentFragment;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取传递的数据
        mNumberName = null;
        mNumberName = getIntent().getStringExtra(AppConstants.LOTTERY_KEY);
        /**不统计当前Activity*/
        MobclickAgent.openActivityDurationTrack(false);

        initView();
        initData();
        initFragment();
        initEvent();
    }

    private void initFragment() {
        isHistoryPager = false;
        currentFragment = new CurrentNumberFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mNumberName", mNumberName);
        currentFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_numberContext_current, currentFragment).commit();

//        fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//
//        currentFragment = new CurrentNumberFragment();
//        Bundle bundle1 = new Bundle();
//        bundle1.putString("mNumberName", mNumberName);
//        currentFragment.setArguments(bundle1);
//        transaction.replace(R.id.fl_numberContext_current, currentFragment);
//
//        historyhFragment = new HistoryNumberFragment();
//        Bundle bundle2 = new Bundle();
//        bundle2.putString("mNumberName", mNumberName);
//        historyhFragment.setArguments(bundle2);
//        transaction.replace(R.id.fl_numberContext_historyh, historyhFragment);
//
//        transaction.commit();
    }

    private void initEvent() {
//        tv_back.setOnClickListener(this);
//        currentNumberInfo.setOnClickListener(this);
//        historyNumberInfo.setOnClickListener(this);
    }

    private void initData() {

        if (!TextUtils.isEmpty(mNumberName)) {
//			 title.setText(AppConstants.numberNames[Integer.parseInt(mNumberName) - 1]);// 设置开奖标题
            NumberDataUtils.setTextTitle(mContext, public_txt_title, mNumberName);// 设置开奖标题
        } else {
            public_txt_title.setText("  ");
        }
    }

    private void initView() {
        setContentView(R.layout.numbers_main_info);

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        ImageView public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setImageResource(R.mipmap.number_history_icon);
        public_btn_set.setOnClickListener(this);
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);


//        tv_back = $(R.id.iv_back_numberInfo);
//        title = $(R.id.tv_title_numberInfo);
        current = $(R.id.fl_numberContext_current);
        historyh = $(R.id.fl_numberContext_historyh);
//        current.setVisibility(View.VISIBLE);
//        currentNumberInfo = $(R.id.tv_current_numberInfo);
//        historyNumberInfo = $(R.id.tv_history_numberInfo);
//        currentNumberInfo.setSelected(true);
        MobclickAgent.onEvent(mContext, "Lottery_Info_News");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.public_img_back:// 关闭
                MobclickAgent.onEvent(mContext, "Lottery_Info_Exit");

                if(isHistoryPager){// 如果当前为历史详情，则返回到历史列表
                    historyhFragment.setValue(true);
                    current.setVisibility(View.GONE);
                    historyh.setVisibility(View.VISIBLE);
                    isHistoryPager = false;
                }else{
                    if(current.getVisibility() == View.GONE){
                        current.setVisibility(View.VISIBLE);
                        historyh.setVisibility(View.GONE);
                    }else{
                        finish();
                    }
                }
                break;
            case R.id.public_btn_set:// 历史开奖
                MobclickAgent.onEvent(mContext,"Lottery_Info_History");
                isHistoryPager = true;
                current.setVisibility(View.GONE);
                historyh.setVisibility(View.VISIBLE);

                historyhFragment = new HistoryNumberFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mNumberName", mNumberName);
                historyhFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_numberContext_historyh, historyhFragment).commit();

                break;
//		case R.id.tv_current_numberInfo:// 当前开奖
//			MobclickAgent.onEvent(mContext,"Lottery_Info_News");
//			current.setVisibility(View.VISIBLE);
//			currentNumberInfo.setSelected(true);
//			currentNumberInfo.setTextColor(Color.WHITE);
//
//			historyh.setVisibility(View.GONE);
//			historyNumberInfo.setSelected(false);
//			historyNumberInfo.setTextColor(getResources().getColor(R.color.bg_header));
//
//			break;
//		case R.id.tv_history_numberInfo:// 历史开奖
//			MobclickAgent.onEvent(mContext,"Lottery_Info_History");
//			currentNumberInfo.setSelected(false);
//			current.setVisibility(View.GONE);
//			currentNumberInfo.setTextColor(getResources().getColor(R.color.bg_header));
//
//			historyNumberInfo.setSelected(true);
//			historyh.setVisibility(View.VISIBLE);
//			historyNumberInfo.setTextColor(Color.WHITE);
//
//			historyhFragment.setValue(true);
//
//			break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
