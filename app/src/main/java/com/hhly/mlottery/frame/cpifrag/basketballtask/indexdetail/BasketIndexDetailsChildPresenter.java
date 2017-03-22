package com.hhly.mlottery.frame.cpifrag.basketballtask.indexdetail;

import com.hhly.mlottery.bean.basket.index.BasketIndexDetailsBean;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.GetTaskSource;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.IGetTaskSource;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.OnTaskDataListener;
import com.hhly.mlottery.mvp.BasePresenter;

/**
 * @author: Wangg
 * @Name：BasketIndexDetailsChildPresenter
 * @Description:
 * @Created on:2017/3/22  9:49.
 */

public class BasketIndexDetailsChildPresenter extends BasePresenter<BasketIndexDetailsContract.IndexDetailsChildView> implements BasketIndexDetailsContract.IndexDetailsChildPresenter {


    private IGetTaskSource iGetTaskSource;

    public BasketIndexDetailsChildPresenter(BasketIndexDetailsContract.IndexDetailsChildView view) {
        super(view);
    }

    @Override
    public void showRequestData(String timeZone, String comId, String thirdId, String oddsType) {
        iGetTaskSource = new GetTaskSource();
        iGetTaskSource.getBasketIndexCenterDetails(timeZone, comId, thirdId, oddsType, new OnTaskDataListener.BasketIndexDetails() {
            @Override
            public void getDataSucess(BasketIndexDetailsBean o) {

                mView.showLoadView();


                mView.showRequestDataView(o);
            }

            @Override
            public void getDataError() {

                mView.onError();

            }

            @Override
            public void getNoData() {

                mView.noData();
            }
        });

    }
}
