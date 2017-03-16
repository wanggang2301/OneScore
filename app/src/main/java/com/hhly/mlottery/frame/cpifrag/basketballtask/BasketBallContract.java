package com.hhly.mlottery.frame.cpifrag.basketballtask;

import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

/**
 * @author: Wangg
 * @Name：BasketBallCpiContract
 * @Description:
 * @Created on:2017/3/16  12:09.
 */

public interface BasketBallContract {


    interface CpiView extends IView {

        void switchFgView();


    }

    interface CpiPresenter extends IPresenter<BasketBallContract.CpiView> {

        void switchFg();

    }


    //亚盘、大小球、欧赔

    interface OddView extends IView {


    }

    interface OddPresenter extends IPresenter<BasketBallContract.OddView> {

    }


}
