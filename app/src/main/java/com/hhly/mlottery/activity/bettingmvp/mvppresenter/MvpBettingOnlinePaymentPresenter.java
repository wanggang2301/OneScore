package com.hhly.mlottery.activity.bettingmvp.mvppresenter;

import com.hhly.mlottery.activity.bettingmvp.MModel;
import com.hhly.mlottery.activity.bettingmvp.MPresenter;
import com.hhly.mlottery.activity.bettingmvp.MView;
import com.hhly.mlottery.activity.bettingmvp.mvpmodel.MvpBettingOnlinePaymentModel;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseBean;

/**
 * Created by：XQyi on 2017/5/22 14:35
 * Use:支付页面（选择支付方式 [MVP-presenter 业务分配 逻辑处理]）
 */
public class MvpBettingOnlinePaymentPresenter implements MPresenter<BasketDatabaseBean> {

    private MModel mModel;
    private MView mView;

    public MvpBettingOnlinePaymentPresenter(MView view){
        this.mView = view;
        mModel = new MvpBettingOnlinePaymentModel(this);
    }

    @Override
    public void loadData(String url) {
        mModel.loadData(url);
    }

    @Override
    public void loadSuccess(BasketDatabaseBean basketDatabaseBean) {
        mView.loadSuccessView(basketDatabaseBean);
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
