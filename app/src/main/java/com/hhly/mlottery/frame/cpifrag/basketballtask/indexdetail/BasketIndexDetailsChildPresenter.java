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
    public void showRequestData(String comId, String thirdId, String oddsType) {
        iGetTaskSource = new GetTaskSource();
        iGetTaskSource.getBasketIndexCenterDetails(comId, thirdId, oddsType, new OnTaskDataListener.BasketIndexDetails() {
            @Override
            public void getDataSucess(BasketIndexDetailsBean o) {
                mView.showRequestDataView(o);
            }

            @Override
            public void getDataError() {
                mView.onError();
            }

            @Override
            public void getNoData() {

                mView.noDataView();
            }
        });

    }


    @Override
    public void showLoad() {
        mView.showLoadView();
    }

    //只为点击公司获取赔率列表使用
    @Override
    public void getRequestComOddsData(String comId, String thirdId, String oddsType) {
        iGetTaskSource = new GetTaskSource();
        iGetTaskSource.getBasketIndexCenterDetails(comId, thirdId, oddsType, new OnTaskDataListener.BasketIndexDetails() {
            @Override
            public void getDataSucess(BasketIndexDetailsBean o) {
                mView.showLoadView();
                mView.getComOddsFromComId(o);
            }

            @Override
            public void getDataError() {
                mView.onError();
            }

            @Override
            public void getNoData() {

                mView.noDataView();
            }
        });
    }
}
