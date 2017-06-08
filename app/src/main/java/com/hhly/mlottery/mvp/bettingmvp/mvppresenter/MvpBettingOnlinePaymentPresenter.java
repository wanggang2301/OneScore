package com.hhly.mlottery.mvp.bettingmvp.mvppresenter;

import com.hhly.mlottery.bean.bettingbean.BettingOrderDataBean;
import com.hhly.mlottery.mvp.bettingmvp.MModel;
import com.hhly.mlottery.mvp.bettingmvp.MPresenter;
import com.hhly.mlottery.mvp.bettingmvp.MView;
import com.hhly.mlottery.mvp.bettingmvp.mvpmodel.MvpBettingOnlinePaymentModel;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseBean;

import java.util.Map;

/**
 * Created by：XQyi on 2017/5/22 14:35
 * Use:支付页面（选择支付方式 [MVP-presenter 业务分配 逻辑处理]）
 */
public class MvpBettingOnlinePaymentPresenter implements MPresenter<BettingOrderDataBean> {

    private MModel mModel;
    private MView mView;

    public MvpBettingOnlinePaymentPresenter(MView view){
        this.mView = view;
        mModel = new MvpBettingOnlinePaymentModel(this);
    }

    @Override
    public void loadData(String url , Map<String ,String> parametMap) {
        mModel.loadData(url , parametMap);
    }

    @Override
    public void loadSuccess(BettingOrderDataBean orderDataBean) {
        mView.loadSuccessView(orderDataBean);
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
