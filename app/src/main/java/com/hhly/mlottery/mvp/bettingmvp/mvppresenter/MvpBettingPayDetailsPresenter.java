package com.hhly.mlottery.mvp.bettingmvp.mvppresenter;

import com.hhly.mlottery.mvp.bettingmvp.MModel;
import com.hhly.mlottery.mvp.bettingmvp.MPresenter;
import com.hhly.mlottery.mvp.bettingmvp.MView;
import com.hhly.mlottery.mvp.bettingmvp.mvpmodel.MvpBettingPayDetailsModel;

import java.util.Map;

/**
 * Created by：XQyi on 2017/5/22 11:09
 * Use:竞彩单关页面[MVP_presenter 逻辑处理 分配]
 */
public class MvpBettingPayDetailsPresenter implements MPresenter<Object> {

    private MView mView;
    private MModel mModel;

    public MvpBettingPayDetailsPresenter(MView mView){
        this.mView = mView;
        mModel = new MvpBettingPayDetailsModel(this);
    }


    @Override
    public void loadData(String url , Map<String ,String> parametMap) {
        mModel.loadData(url , parametMap);
    }

    @Override
    public void loadSuccess(Object o) {
        mView.loadSuccessView(o);
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
