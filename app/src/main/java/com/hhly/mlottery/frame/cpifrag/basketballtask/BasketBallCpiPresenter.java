package com.hhly.mlottery.frame.cpifrag.basketballtask;

import com.hhly.mlottery.mvp.BasePresenter;

/**
 * @author: Wangg
 * @Name：BasketBallCpiPresenter
 * @Description:
 * @Created on:2017/3/16  12:11.
 */

public class BasketBallCpiPresenter extends BasePresenter<BasketBallContract.CpiView> implements BasketBallContract.CpiPresenter {


    public BasketBallCpiPresenter(BasketBallContract.CpiView view) {
        super(view);
        mView = view;
    }


    @Override
    public void switchFg() {







        mView.switchFgView();

    }

}
