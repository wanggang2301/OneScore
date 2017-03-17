package com.hhly.mlottery.frame.cpifrag.basketballtask;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.enums.OddsTypeEnum;
import com.hhly.mlottery.mvp.ViewFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketBallOddFragment extends ViewFragment<BasketBallContract.OddPresenter> implements BasketBallContract.OddView {


    public BasketBallOddFragment() {
    }

    public static BasketBallOddFragment newInstance(@OddsTypeEnum.OddsType String type) {
        BasketBallOddFragment basketBallOddFragment = new BasketBallOddFragment();
        return basketBallOddFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket_ball_odd, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new BasketBallOddPresenter(this);

        //显示loadView
        mPresenter.showLoad();


        //显示数据
        mPresenter.showRequestData();


        //请求数据错误
        mPresenter.requestError();

    }

    @Override
    public void showLoadView() {

    }

    @Override
    public void showRequestDataView() {

    }

    @Override
    public void onError() {

    }
}
