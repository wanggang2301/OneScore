package com.hhly.mlottery.frame.cpifrag.basketballtask.indexdetail;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.hhly.mlottery.bean.enums.BasketOddsTypeEnum;
import com.hhly.mlottery.mvp.ViewFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author wangg
 * @desc 篮球指数指数详情fg
 * @date 2017/03/22  10:47
 */
public class BasketIndexDetailsFragment extends ViewFragment<BasketIndexDetailsContract.IndexDetailsPresenter> implements BasketIndexDetailsContract.IndexDetailsView {

    @BindView(R.id.public_img_back)
    ImageView publicImgBack;
    @BindView(R.id.public_txt_left_title)
    TextView publicTxtLeftTitle;
    @BindView(R.id.public_btn_set)
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

    private String thirdId, comId, oddType;


    private final String TAG = this.getClass().getSimpleName();


    public static BasketIndexDetailsFragment newInstance(String thirdId, String comId, String oddType) {
        BasketIndexDetailsFragment basketIndexDetailsFragment = new BasketIndexDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("thirdId", thirdId);
        bundle.putString("comId", comId);
        bundle.putString("oddType", oddType);
        basketIndexDetailsFragment.setArguments(bundle);
        return basketIndexDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            thirdId = getArguments().getString("thirdId");
            comId = getArguments().getString("comId");
            oddType = getArguments().getString("oddType");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_index_details, container, false);
        ButterKnife.bind(this, mView);
        mPresenter.initFg();
        return mView;
    }

    @Override
    public BasketIndexDetailsContract.IndexDetailsPresenter initPresenter() {
        return new BasketIndexDetailsFresenter(this);
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

        fragments.add(BasketIndexDetailsChildFragment.newInstance(thirdId, "", BasketOddsTypeEnum.ASIALET));
        fragments.add(BasketIndexDetailsChildFragment.newInstance(thirdId, "", BasketOddsTypeEnum.ASIASIZE));
        fragments.add(BasketIndexDetailsChildFragment.newInstance(thirdId, "", BasketOddsTypeEnum.EURO));


        Bundle bundle = new Bundle();
        bundle.putString("thirdId", thirdId);
        bundle.putString("comId", comId);
        bundle.putString("oddType", oddType);

        switch (oddType) {
            case BasketOddsTypeEnum.ASIALET:
                fragments.get(0).setArguments(bundle);
                break;

            case BasketOddsTypeEnum.ASIASIZE:
                fragments.get(1).setArguments(bundle);
                break;

            case BasketOddsTypeEnum.EURO:
                fragments.get(2).setArguments(bundle);
                break;
        }


        cpiDetailsFragmentAdapter = new CpiDetailsFragmentAdapter(getChildFragmentManager(), fragments);
        cpiDetailsViewpager.setOffscreenPageLimit(2);//设置预加载页面的个数。
        cpiDetailsViewpager.setAdapter(cpiDetailsFragmentAdapter);
        //根据标识界面传过来的值默认选中那个界面

        switch (oddType) {
            case BasketOddsTypeEnum.ASIALET:
                cpiDetailsViewpager.setCurrentItem(0);
                break;

            case BasketOddsTypeEnum.ASIASIZE:
                cpiDetailsViewpager.setCurrentItem(1);
                break;

            case BasketOddsTypeEnum.EURO:
                cpiDetailsViewpager.setCurrentItem(2);
                break;
        }

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
