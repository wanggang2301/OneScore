package com.hhly.mlottery.frame.cpifrag.basketballtask;


import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.enums.OddsTypeEnum;
import com.hhly.mlottery.databinding.FragmentBasketBallCpiBinding;
import com.hhly.mlottery.frame.BallType;
import com.hhly.mlottery.frame.scorefrag.ScoreSwitchFg;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.widget.BallChoiceArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author wangg
 * @des:篮球指数
 * @date:2017/3/16
 */
public class BasketBallCpiFrament extends ViewFragment<BasketBallContract.CpiPresenter> implements BasketBallContract.CpiView {

    FragmentBasketBallCpiBinding mBinding;

    private String[] mItems;
    private Activity mActivity;
    private List<BasketBallOddFragment> mFragments;

    public BasketBallCpiFrament() {

    }

    public static BasketBallCpiFrament newInstace() {
        BasketBallCpiFrament basketBallCpiFrament = new BasketBallCpiFrament();
        return basketBallCpiFrament;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_basket_ball_cpi, container, false);
        mBinding.dHeasder.tvMatchName.setText(getResources().getString(R.string.basketball_txt));
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mItems = getResources().getStringArray(R.array.zhishu_select);
        mPresenter = new BasketBallCpiPresenter(this);

        //初始化头部View和事件
        mPresenter.initFg();
    }


    @Override
    public void initFgView() {
        initClickEvent();
        initViewPager();
    }

    private void initClickEvent() {
        mBinding.dHeasder.llMatchSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.dHeasder.ivMatch.setImageResource(R.mipmap.nav_icon_up);
                backgroundAlpha(getActivity(), 0.5f);
                popWindow();
            }
        });


        //时间选择
        mBinding.dHeasder.publicDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //公司选择
        mBinding.dHeasder.publicImgCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //赛事筛选
        mBinding.dHeasder.publicImgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    /**
     * 初始化 ViewPager
     */
    private void initViewPager() {
        mFragments = new ArrayList<>();
        mFragments.add(BasketBallOddFragment.newInstance(OddsTypeEnum.PLATE));
        mFragments.add(BasketBallOddFragment.newInstance(OddsTypeEnum.BIG));
        mFragments.add(BasketBallOddFragment.newInstance(OddsTypeEnum.OP));
        CPIPagerAdapter pagerAdapter = new CPIPagerAdapter(getChildFragmentManager());
        mBinding.cpiViewpager.setOffscreenPageLimit(3);
        mBinding.cpiViewpager.setAdapter(pagerAdapter);
        mBinding.tabs.setupWithViewPager(mBinding.cpiViewpager);
    }


    /**
     * 球类切换Dialog
     */
    private void popWindow() {
        final View mView = View.inflate(mActivity, R.layout.pop_select, null);
        // 创建ArrayAdapter对象
        BallChoiceArrayAdapter mAdapter = new BallChoiceArrayAdapter(mActivity, mItems, BallType.BASKETBALL);

        ListView listview = (ListView) mView.findViewById(R.id.match_type);
        listview.setAdapter(mAdapter);
        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(mBinding.dHeasder.headerLayout);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new ScoreSwitchFg(position));
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mBinding.dHeasder.ivMatch.setImageResource(R.mipmap.nav_icon_cbb);
                backgroundAlpha(getActivity(), 1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    /**
     * ViewPager 适配器
     */
    class CPIPagerAdapter extends FragmentStatePagerAdapter {

        public CPIPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BasketBallOddFragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
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
