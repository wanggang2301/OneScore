package com.hhly.mlottery.frame.cpifrag.basketballtask.indexdetail;

import com.hhly.mlottery.bean.basket.index.BasketIndexDetailsBean;
import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

/**
 * @author: Wangg
 * @Name：BasketIndexDetailsContract
 * @Description:
 * @Created on:2017/3/22  9:49.
 */

public interface BasketIndexDetailsContract {

    interface IndexDetailsView extends IView {
        void initFgView();
    }

    interface IndexDetailsPresenter extends IPresenter<BasketIndexDetailsContract.IndexDetailsView> {
        void initFg();
    }


    //亚盘、大小球、欧赔

    interface IndexDetailsChildView extends IView {
        void showLoadView();

        void showRequestDataView(BasketIndexDetailsBean o);

        void noData();
    }

    interface IndexDetailsChildPresenter extends IPresenter<BasketIndexDetailsContract.IndexDetailsChildView> {
        void showRequestData(String comId, String thirdId, String oddsType);
    }

}
