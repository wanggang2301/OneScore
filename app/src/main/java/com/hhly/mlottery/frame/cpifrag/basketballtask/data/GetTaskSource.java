package com.hhly.mlottery.frame.cpifrag.basketballtask.data;

import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.bean.basket.index.BasketIndexDetailsBean;
import com.hhly.mlottery.util.CollectionUtils;

import rx.Subscriber;

/**
 * @author: Wangg
 * @Name：GetTaskData
 * @Description:
 * @Created on:2017/3/17  17:20.
 */

public class GetTaskSource implements IGetTaskSource {
    @Override
    public void getBasketIndexCenter(String date, String type, final OnTaskDataListener.BasketIndex iGetTaskData) {
        HttpMethods.getInstance().getBasketIndexCenter(new Subscriber<BasketIndexBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iGetTaskData.getDataError();
            }

            @Override
            public void onNext(BasketIndexBean o) {
                if (o.getResult() == 200) {

                    if (o == null || !CollectionUtils.notEmpty(o.getData().getAllInfo())) {
                        iGetTaskData.getNoData(o.getData().getCurrDate());
                        return;
                    }


                    iGetTaskData.getDataSucess(o);
                } else {
                    iGetTaskData.getDataError();
                }
            }
        }, date, type);
    }

    @Override
    public void getBasketIndexCenterDetails(String comId, String thirdId, String oddsType, final OnTaskDataListener.BasketIndexDetails iGetTaskData) {
        HttpMethods.getInstance().getBasketIndexCenterDetails(new Subscriber<BasketIndexDetailsBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iGetTaskData.getDataError();
            }

            @Override
            public void onNext(BasketIndexDetailsBean basketIndexDetailsBean) {
                if (basketIndexDetailsBean != null && basketIndexDetailsBean.getComLists() != null) {
                    if (basketIndexDetailsBean.getOddsData() != null) {
                        iGetTaskData.getDataSucess(basketIndexDetailsBean);
                    } else {
                        iGetTaskData.getNoData("");
                    }
                } else {
                    iGetTaskData.getDataError();
                }
            }
        }, comId, thirdId, oddsType);

    }
}


