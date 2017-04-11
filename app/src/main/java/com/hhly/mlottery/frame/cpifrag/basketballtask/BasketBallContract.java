package com.hhly.mlottery.frame.cpifrag.basketballtask;

import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
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
        void initFgView();
    }

    interface CpiPresenter extends IPresenter<BasketBallContract.CpiView> {
        void initFg();
    }


    //亚盘、大小球、欧赔

    interface OddView extends IView {
        void showLoadView();

        void showResponseDataView();

        void noData(String date);
    }

    interface OddPresenter extends IPresenter<BasketBallContract.OddView> {
        void showRequestData(String date, String type);

        BasketIndexBean getRequestData();

        void showLoad();

    }
}
