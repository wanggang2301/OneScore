package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.frame.basketballframe.BasketInfomationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wang gang
 * @date 2016/7/14 17:07
 * @des 篮球资料库
 */
public class BasketballInformationActivity extends BaseActivity {

    @BindView(R.id.public_img_back)
    ImageView publicImgBack;
    @BindView(R.id.public_txt_title)
    TextView publicTxtTitle;
    @BindView(R.id.public_btn_set)
    ImageView publicBtnSet;
    @BindView(R.id.public_btn_filter)
    ImageView publicBtnFilter;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private TabsAdapter mTabsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_information);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        publicTxtTitle.setText("篮球资料库");
        publicBtnFilter.setVisibility(View.GONE);

        String[] titles = {"热门", "欧洲", "美洲", "亚洲", "非洲", "国际"};
        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        mTabsAdapter.setTitles(titles);
        mTabsAdapter.addFragments(BasketInfomationFragment.newInstance(), BasketInfomationFragment.newInstance(), BasketInfomationFragment.newInstance(), BasketInfomationFragment.newInstance(), BasketInfomationFragment.newInstance(), BasketInfomationFragment.newInstance());
        viewpager.setOffscreenPageLimit(2);//设置预加载页面的个数。
        viewpager.setAdapter(mTabsAdapter);
        tabLayout.setupWithViewPager(viewpager);

    }

    @OnClick({R.id.public_img_back, R.id.public_btn_set})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_img_back:
                finish();
                break;
            case R.id.public_btn_set:
                break;
        }
    }
}
