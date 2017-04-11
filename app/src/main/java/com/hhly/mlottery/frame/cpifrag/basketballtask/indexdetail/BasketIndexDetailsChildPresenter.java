package com.hhly.mlottery.frame.cpifrag.basketballtask.indexdetail;

import com.hhly.mlottery.bean.basket.index.BasketIndexDetailsBean;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.GetTaskSource;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.IGetTaskSource;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.OnTaskDataListener;
import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.util.L;

import java.util.List;

/**
 * @author: Wangg
 * @Name：BasketIndexDetailsChildPresenter
 * @Description:
 * @Created on:2017/3/22  9:49.
 */

public class BasketIndexDetailsChildPresenter extends BasePresenter<BasketIndexDetailsContract.IndexDetailsChildView> implements BasketIndexDetailsContract.IndexDetailsChildPresenter {

    private IGetTaskSource iGetTaskSource;

    private boolean isFirstRequest = false;

    private List<BasketIndexDetailsBean.ComListsBean> comListsBeanList;
    private List<BasketIndexDetailsBean.OddsDataBean> oddsDataBeanList;


    public BasketIndexDetailsChildPresenter(BasketIndexDetailsContract.IndexDetailsChildView view) {
        super(view);
    }


    @Override
    public List<BasketIndexDetailsBean.ComListsBean> getComLists() {
        return comListsBeanList;
    }

    @Override
    public List<BasketIndexDetailsBean.OddsDataBean> getOddsLists() {
        return oddsDataBeanList;
    }

    @Override
    public void showRequestData(String comId, String thirdId, String oddsType) {
        iGetTaskSource = new GetTaskSource();
        iGetTaskSource.getBasketIndexCenterDetails(comId, thirdId, oddsType, new OnTaskDataListener.BasketIndexDetails() {
            @Override
            public void getDataSucess(BasketIndexDetailsBean o) {
                comListsBeanList = o.getComLists();
                oddsDataBeanList = o.getOddsData();

                if (!isFirstRequest) {
                    mView.setTitle();
                    mView.showLeftListView();
                    isFirstRequest = true;
                    L.d("presenter", "____刷新左边listview");
                }

                L.d("presenter", "____刷新右边recyclerview");

                mView.showRightRecyclerView();
                mView.showRequestSucess();
            }

            @Override
            public void getDataError() {
                mView.setTitle();

                mView.onError();
                //isFirstRequest = false;
            }

            @Override
            public void getNoData(String date) {
                mView.setTitle();

                mView.noDataView();
            }
        });
    }


    @Override
    public void showLoad() {
        mView.showLoadView();
    }

  /*  //只为点击公司获取赔率列表使用
    @Override
    public void getRequestComOddsData(String comId, String thirdId, String oddsType) {
        iGetTaskSource = new GetTaskSource();
        iGetTaskSource.getBasketIndexCenterDetails(comId, thirdId, oddsType, new OnTaskDataListener.BasketIndexDetails() {
            @Override
            public void getDataSucess(BasketIndexDetailsBean o) {
                basketIndexDetailsBean = o;
                mView.showRightRecyclerView();
                mView.showRequestSucess();

            }

            @Override
            public void getDataError() {
                mView.onErrorComOddFromComId();
            }

            @Override
            public void getNoData() {
                mView.noDataView();
            }
        });
    }*/
}
