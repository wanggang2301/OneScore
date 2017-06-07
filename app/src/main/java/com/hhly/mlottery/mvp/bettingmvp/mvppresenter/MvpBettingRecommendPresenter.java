package com.hhly.mlottery.mvp.bettingmvp.mvppresenter;

import com.hhly.mlottery.bean.bettingbean.BettingListDataBean;
import com.hhly.mlottery.mvp.bettingmvp.MModel;
import com.hhly.mlottery.mvp.bettingmvp.MPresenter;
import com.hhly.mlottery.mvp.bettingmvp.MView;
import com.hhly.mlottery.mvp.bettingmvp.mvpmodel.MvpBettingRecommendModel;
import com.hhly.mlottery.bean.basket.BasketNewRootBean;

import java.util.Map;

/**
 * Created by：XQyi on 2017/5/2 10:56
 * Use:竞彩推荐列表（MVP-Presenter 业务分配 逻辑处理）
 */
public class MvpBettingRecommendPresenter implements MPresenter<BettingListDataBean> {

    private MModel mModel;
    private MView mView;
    public MvpBettingRecommendPresenter(MView mView){
        this.mView = mView;
        this.mModel = new MvpBettingRecommendModel(this);
    }


    @Override
    public void loadData(String url , Map<String ,String> parametMap) {
        mModel.loadData(url , parametMap);
    }

    @Override
    public void loadSuccess(BettingListDataBean beanData) {
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
