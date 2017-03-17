package com.hhly.mlottery.frame.cpifrag.basketballtask;

import com.hhly.mlottery.bean.snookerbean.SnookerRankBean;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.GetTaskSource;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.IGetTaskSource;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.OnTaskDataListener;
import com.hhly.mlottery.mvp.BasePresenter;

/**
 * @author: Wangg
 * @Name：BasketBallOddPresenter
 * @Description:
 * @Created on:2017/3/16  14:45.
 */

public class BasketBallOddPresenter extends BasePresenter<BasketBallContract.OddView> implements BasketBallContract.OddPresenter {


    private IGetTaskSource iGetTaskSource;

    public BasketBallOddPresenter(BasketBallContract.OddView view) {
        super(view);
    }


    @Override
    public void showRequestData(String lang, String timeZone, String pageSize, String pageNum) {
        mView.showLoadView();  //加载loadView
        iGetTaskSource = new GetTaskSource();
        iGetTaskSource.getBasketIndexCenter(lang, timeZone, pageSize, pageNum, new OnTaskDataListener() {
            @Override
            public void getDataError() {
                mView.onError();
            }

            @Override
            public void getDataSucess(SnookerRankBean o) {
                mView.showRequestDataView(o);
            }

            @Override
            public void getNoData() {
                mView.noData();
            }
        });


    }
}
