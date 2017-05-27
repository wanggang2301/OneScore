package com.hhly.mlottery.frame.cpifrag.basketballtask;

import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.GetTaskSource;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.IGetTaskSource;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.OnTaskDataListener;
import com.hhly.mlottery.frame.footballframe.bowl.data.bean.BasketIndex;
import com.hhly.mlottery.frame.footballframe.bowl.data.repository.BasketIndexReposeitory;
import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.util.L;

import rx.Observable;
import rx.Subscriber;

/**
 * @author: Wangg
 * @Name：BasketBallOddPresenter
 * @Description:
 * @Created on:2017/3/16  14:45.
 */

public class BasketBallOddPresenter extends BasePresenter<BasketBallContract.OddView> implements BasketBallContract.OddPresenter {


    private IGetTaskSource iGetTaskSource;

    private BasketIndexBean basketIndexBean;

    private BasketIndexReposeitory basketIndexReposeitory;


    public BasketBallOddPresenter(BasketBallContract.OddView view) {
        super(view);
        basketIndexReposeitory = mDataManager.basketIndexReposeitory;

    }


    @Override
    public void showRequestData(String date, String type) {
        iGetTaskSource = new GetTaskSource();
        iGetTaskSource.getBasketIndexCenter(date, type, new OnTaskDataListener.BasketIndex() {
            @Override
            public void getDataError() {
                mView.onError();
            }

            @Override
            public void getDataSucess(BasketIndexBean b) {

                basketIndexBean = b;
                mView.showResponseDataView();
            }


            @Override
            public void getNoData(String date) {
                mView.noData(date);

            }
        });


    }

    @Override
    public void showLoad() {
        mView.showLoadView();

        Observable<BasketIndex> observable = basketIndexReposeitory.getIndexList("", "asiaLet", "1");

        addSubscription(observable, new Subscriber<BasketIndex>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BasketIndex basketIndex) {

                L.d("xxccvvbb", "______" + basketIndex.getData().getAllInfo().get(0).getHomeTeam());

            }
        });


    }

    @Override
    public BasketIndexBean getRequestData() {
        return basketIndexBean;
    }
}
