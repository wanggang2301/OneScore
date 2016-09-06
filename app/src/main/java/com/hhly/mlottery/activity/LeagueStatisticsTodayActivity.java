package com.hhly.mlottery.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.frame.footframe.LeagueStatisticsTodayFragment;

/**
 * @author: Wangg
 * @Name：LeagueStatisticsTodayActivity
 * @Description: 今日联赛统计
 * @Created on:2016/9/1  15:28.
 */
public class LeagueStatisticsTodayActivity extends BaseActivity implements View.OnClickListener {

    private ImageView publicImgBack;
    private TextView publicTxtTitle;
    private ImageView publicBtnSet;
    private TabLayout tabLayout;
    private ViewPager viewpager;

    private Context mContext;
    private FragmentManager fragmentManager;
    private TabsAdapter mTabsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_statistics_today);
        mContext = getApplicationContext();
        initView();
    }

    private void initView() {

        publicImgBack = (ImageView) findViewById(R.id.public_img_back);
        publicBtnSet = (ImageView) findViewById(R.id.public_btn_set);
        publicTxtTitle = (TextView) findViewById(R.id.public_txt_title);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        publicTxtTitle.setText(mContext.getResources().getString(R.string.league_statistics_today_title));
        publicBtnSet.setOnClickListener(this);
        publicImgBack.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        String[] titles = mContext.getResources().getStringArray(R.array.league_statistics_today_tabs);
        mTabsAdapter = new TabsAdapter(fragmentManager);
        mTabsAdapter.setTitles(titles);
        mTabsAdapter.addFragments(LeagueStatisticsTodayFragment.newInstance(0),
                LeagueStatisticsTodayFragment.newInstance(1),
                LeagueStatisticsTodayFragment.newInstance(2),
                LeagueStatisticsTodayFragment.newInstance(3),
                LeagueStatisticsTodayFragment.newInstance(4));
        viewpager.setOffscreenPageLimit(1);//设置预加载页面的个数。
        viewpager.setAdapter(mTabsAdapter);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_btn_set:
                showDialog();
                break;
            case R.id.public_img_back:
                this.finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        final AlertDialog alertDialog = builder.create();
        LayoutInflater infla = LayoutInflater.from(this);
        View alertDialogView = infla.inflate(R.layout.league_statistics_today_alertdialog, null);
        Button btn_cancel = (Button) alertDialogView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        alertDialog.getWindow().setContentView(alertDialogView);
        alertDialog.setCanceledOnTouchOutside(true);
    }
}
