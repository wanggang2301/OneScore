package com.hhly.mlottery.frame.cpifrag.basketballtask.indexdetail;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.mvp.ViewFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hhly.mlottery.R.id.public_btn_set;
import static com.hhly.mlottery.R.id.public_img_back;
import static com.hhly.mlottery.R.id.public_txt_left_title;

/**
 * @author wangg
 * @desc 篮球指数指数详情fg
 * @date 2017/03/22  10:47
 */
public class BasketIndexDetailsFragment extends ViewFragment<BasketIndexDetailsContract.IndexDetailsPresenter> implements BasketIndexDetailsContract.IndexDetailsView {

    @BindView(public_img_back)
    ImageView publicImgBack;
    @BindView(public_txt_left_title)
    TextView publicTxtLeftTitle;
    @BindView(public_btn_set)
    ImageView publicBtnSet;
    @BindView(R.id.public_btn_filter)
    ImageView publicBtnFilter;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.cpi_details_viewpager)
    ViewPager cpiDetailsViewpager;
    @BindView(R.id.public_txt_title)
    TextView publicTxtTitle;
    private View mView;
    private List<Fragment> fragments;
    private CpiDetailsFragmentAdapter cpiDetailsFragmentAdapter;

    public static BasketIndexDetailsFragment newInstance() {
        BasketIndexDetailsFragment basketIndexDetailsFragment = new BasketIndexDetailsFragment();
        return basketIndexDetailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_index_details, container, false);
        ButterKnife.bind(this, mView);
        mPresenter = new BasketIndexDetailsFresenter(this);
        mPresenter.initFg();
        return mView;
    }


    @Override
    public void initFgView() {
        publicTxtTitle.setVisibility(View.GONE);
        //左边标题
        publicTxtLeftTitle.setVisibility(View.VISIBLE);
        publicTxtLeftTitle.setText(R.string.football_detail_odds_tab);
        //返回
        publicImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        //设置
        publicBtnSet.setVisibility(View.GONE);
        publicBtnFilter.setVisibility(View.GONE);

        initViewPager();
    }

    @Override
    public void onError() {

    }

    //初始化viewPager
    private void initViewPager() {
        fragments = new ArrayList<>();
        //亚盘
        fragments.add(BasketIndexDetailsChildFragment.newInstance());
        fragments.add(BasketIndexDetailsChildFragment.newInstance());
        fragments.add(BasketIndexDetailsChildFragment.newInstance());
        cpiDetailsFragmentAdapter = new CpiDetailsFragmentAdapter(getChildFragmentManager(), fragments);
        cpiDetailsViewpager.setOffscreenPageLimit(2);//设置预加载页面的个数。
        cpiDetailsViewpager.setAdapter(cpiDetailsFragmentAdapter);
        //根据标识界面传过来的值默认选中那个界面
   /*     if ("palte".equals(stType)) {
            mViewPager.setCurrentItem(0);
        } else if ("big".equals(stType)) {
            mViewPager.setCurrentItem(1);
        } else if ("op".equals(stType)) {
            mViewPager.setCurrentItem(2);
        }*/

        tabs.setupWithViewPager(cpiDetailsViewpager);
        //标记是否初始化
    }


    class CpiDetailsFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList;
        public CpiDetailsFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.mFragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.odd_plate_rb_txt);
                case 1:
                    return getString(R.string.asiasize);
                case 2:
                    return getString(R.string.odd_op_rb_txt);
                default:
                    return getString(R.string.odd_plate_rb_txt);
            }
        }
    }
}
