package com.hhly.mlottery.mvp.bettingmvp.mvppresenter;

import com.hhly.mlottery.bean.bettingbean.BettingListDataBean;
import com.hhly.mlottery.mvp.bettingmvp.MModel;
import com.hhly.mlottery.mvp.bettingmvp.MPresenter;
import com.hhly.mlottery.mvp.bettingmvp.MView;
import com.hhly.mlottery.mvp.bettingmvp.mvpmodel.MvpFootballBettingIssueModel;

import java.util.Map;

/**
 * Created by：XQyi on 2017/6/15 12:28
 * Use: 足球内页推介列表（MVP-Presenter 业务分配 逻辑处理）
 */
public class MvpFootballBettingIssuePresenter implements MPresenter<BettingListDataBean>{


    private MModel mModel;
    private MView mView;
    public MvpFootballBettingIssuePresenter(MView mView){
        this.mView = mView;
        this.mModel = new MvpFootballBettingIssueModel(this);
    }

    @Override
    public void loadData(String url, Map<String, String> parametMap) {
        mModel.loadData(url , parametMap);
    }

    @Override
    public void loadSuccess(BettingListDataBean bettingListDataBean) {
        mView.loadSuccessView(bettingListDataBean);
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
