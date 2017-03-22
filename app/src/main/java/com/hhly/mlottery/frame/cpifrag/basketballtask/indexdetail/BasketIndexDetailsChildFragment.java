package com.hhly.mlottery.frame.cpifrag.basketballtask.indexdetail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.index.BasketIndexDetailsBean;
import com.hhly.mlottery.mvp.ViewFragment;

/**
 * @author wangg
 * @desc 篮球指数详情child_frament
 * @date 2017/03/22
 */
public class BasketIndexDetailsChildFragment extends ViewFragment<BasketIndexDetailsContract.IndexDetailsChildPresenter> implements BasketIndexDetailsContract.IndexDetailsChildView {
    private View mView;
    private String oddType;

    public BasketIndexDetailsChildFragment() {
    }

    public static BasketIndexDetailsChildFragment newInstance() {
        BasketIndexDetailsChildFragment basketIndexDetailsChildFragment = new BasketIndexDetailsChildFragment();
        return basketIndexDetailsChildFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_index_details_child, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.showRequestData("", "4432337", oddType);
    }

    @Override
    public void showRequestDataView(BasketIndexDetailsBean b) {

    }

    @Override
    public void showLoadView() {

    }

    @Override
    public void noData() {

    }

    @Override
    public void onError() {

    }
}
