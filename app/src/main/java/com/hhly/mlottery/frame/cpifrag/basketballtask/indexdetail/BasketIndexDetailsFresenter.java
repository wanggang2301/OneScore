package com.hhly.mlottery.frame.cpifrag.basketballtask.indexdetail;

import com.hhly.mlottery.mvp.BasePresenter;

/**
 * @author: Wangg
 * @Name：BasketIndexDetailsFresenter
 * @Description:
 * @Created on:2017/3/22  9:49.
 */

public class BasketIndexDetailsFresenter extends BasePresenter<BasketIndexDetailsContract.IndexDetailsView> implements BasketIndexDetailsContract.IndexDetailsPresenter {
    public BasketIndexDetailsFresenter(BasketIndexDetailsContract.IndexDetailsView view) {
        super(view);
        mView = view;
    }


    @Override
    public void initFg() {
        mView.initFgView();
    }
}
