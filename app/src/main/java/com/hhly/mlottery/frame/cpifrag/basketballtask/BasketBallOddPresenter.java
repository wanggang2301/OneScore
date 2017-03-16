package com.hhly.mlottery.frame.cpifrag.basketballtask;

import com.hhly.mlottery.mvp.BasePresenter;

/**
 * @author: Wangg
 * @Name：BasketBallOddPresenter
 * @Description:
 * @Created on:2017/3/16  14:45.
 */

public class BasketBallOddPresenter extends BasePresenter<BasketBallContract.OddView> implements BasketBallContract.OddPresenter {
    public BasketBallOddPresenter(BasketBallContract.OddView view) {
        super(view);
    }
}
