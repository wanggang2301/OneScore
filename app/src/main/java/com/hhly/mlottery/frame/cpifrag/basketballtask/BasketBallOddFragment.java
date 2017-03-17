package com.hhly.mlottery.frame.cpifrag.basketballtask;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.enums.OddsTypeEnum;
import com.hhly.mlottery.bean.snookerbean.SnookerRankBean;
import com.hhly.mlottery.databinding.FragmentBasketBallOddBinding;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.util.L;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketBallOddFragment extends ViewFragment<BasketBallContract.OddPresenter> implements BasketBallContract.OddView {


    FragmentBasketBallOddBinding mBinding;

    public BasketBallOddFragment() {
    }

    public static BasketBallOddFragment newInstance(@OddsTypeEnum.OddsType String type) {
        BasketBallOddFragment basketBallOddFragment = new BasketBallOddFragment();
        return basketBallOddFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_basket_ball_odd, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new BasketBallOddPresenter(this);

        mBinding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示数据
                mPresenter.showRequestData("zh", "7", "30", "1");
            }
        });


    }

    @Override
    public void showLoadView() {
        Toast.makeText(mActivity, "加载", Toast.LENGTH_SHORT).show();
        L.d("bingd", "加载中_____________");

    }

    @Override
    public void showRequestDataView(SnookerRankBean o) {
        Toast.makeText(mActivity, "成功", Toast.LENGTH_SHORT).show();
        L.d("bingd", "成功_____________" + o.getWorldRankingList().get(0).getName());

    }

    @Override
    public void noData() {

    }

    @Override
    public void onError() {
        Toast.makeText(mActivity, "出错", Toast.LENGTH_SHORT).show();
        L.d("bingd", "出错_____________");

    }
}
