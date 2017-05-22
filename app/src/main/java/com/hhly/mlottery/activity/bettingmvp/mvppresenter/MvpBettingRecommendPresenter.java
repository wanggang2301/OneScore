package com.hhly.mlottery.activity.bettingmvp.mvppresenter;

import com.hhly.mlottery.activity.bettingmvp.MModel;
import com.hhly.mlottery.activity.bettingmvp.MPresenter;
import com.hhly.mlottery.activity.bettingmvp.MView;
import com.hhly.mlottery.activity.bettingmvp.mvpmodel.MvpBettingRecommendModel;
import com.hhly.mlottery.bean.basket.BasketNewRootBean;

/**
 * Created by：XQyi on 2017/5/2 10:56
 * Use:竞彩推荐列表（MVP-Presenter 业务分配 逻辑处理）
 */
public class MvpBettingRecommendPresenter implements MPresenter<BasketNewRootBean> {

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
