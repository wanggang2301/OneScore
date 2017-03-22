package com.hhly.mlottery.frame.cpifrag.basketballtask.data;

import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.bean.snookerbean.SnookerRankBean;

import rx.Subscriber;

/**
 * @author: Wangg
 * @Name：GetTaskData
 * @Description:
 * @Created on:2017/3/17  17:20.
 */

public class GetTaskSource implements IGetTaskSource {
    @Override
    public void getBasketIndexCenter(String lang, String timeZone, String date, String type, final OnTaskDataListener iGetTaskData) {
        HttpMethods.getInstance().getFootballDetailsData(new Subscriber<BasketIndexBean>() {
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
                  /*  if (o.get() == null || "".equals(o.getData())) {
                        iGetTaskData.getNoData();
                    } else {
                        iGetTaskData.getDataSucess(o);
                    }*/

                    iGetTaskData.getDataSucess(o);
                } else {
                    iGetTaskData.getDataError();
                }
            }
        }, lang, timeZone, date, type);
    }
}
