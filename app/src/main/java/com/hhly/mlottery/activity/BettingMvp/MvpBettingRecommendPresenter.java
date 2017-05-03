package com.hhly.mlottery.activity.BettingMvp;

import com.hhly.mlottery.bean.basket.BasketNewRootBean;

/**
 * Created by：XQyi on 2017/5/2 10:56
 * Use:
 */
public class MvpBettingRecommendPresenter implements MPresenter {

    private MModel mModel;
    private MView mView;
    public MvpBettingRecommendPresenter(MView mView){
        this.mView = mView;
        this.mModel = new MvpBettingRecommendModel(this);
    }


    @Override
    public void loadData(String url) {
        mModel.loadData(url);
    }

    @Override
    public void loadSuccess(BasketNewRootBean beanData) {
        mView.loadSuccessView(beanData);
    }

    @Override
    public void loadFail() {
        mView.loadFailView();
    }

    @Override
    public void noData() {
        mView.loadNoData();
    }
}
