package com.hhly.mlottery.frame.basketballframe.basketballteam.resultschedule;

import com.hhly.mlottery.mvp.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import data.bean.BasketTeamResultBean;
import data.bean.Page;
import data.repository.BasketTeamDataRepository;
import rx.Observable;
import rx.Subscriber;

/**
 * 描    述：篮球赛程赛果
 * 作    者：mady@13322.com
 * 时    间：2017/6/20
 */
public class BasketResultPresenter extends BasePresenter<BasketResultContract.View> implements BasketResultContract.Presenter {

    BasketTeamDataRepository mRepository;
    private Page mPage;
    private List<BasketTeamResultBean.TeamMatchDataEntity> mListData=new ArrayList<>();

    public BasketResultPresenter(BasketResultContract.View view) {
        super(view);
        mPage=new Page(1,10);
        mRepository=mDataManager.mTeamDataRepository;
    }

    @Override
    public Page getPage() {
        return mPage;
    }

    @Override
    public void refreshData(String season, String leagueId, String teamId) {
        mPage.index=1;
        Observable<BasketTeamResultBean> observable=mRepository.getMatchResult(season,leagueId,teamId,mPage.index+"");


        addSubscription(observable, new Subscriber<BasketTeamResultBean>() {
            @Override
            public void onCompleted() {
                mView.setRefresh(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.showNoData();
                mView.setRefresh(false);
            }

            @Override
            public void onNext(BasketTeamResultBean bean) {
                if(bean.getResult()==200&&null!=bean.getTeamMatchData()&&bean.getTeamMatchData().size()!=0){
                    mListData.clear();
                    mListData.addAll(bean.getTeamMatchData());
                    mView.recyclerNotify();
                    mPage.index++;
                }else {
                    mView.showNoData();
                }
            }
        });
    }

    @Override
    public void refreshDataByPage(String season, String leagueId, String teamId) {
        Observable<BasketTeamResultBean> observable=mRepository.getMatchResult(season,leagueId,teamId,mPage.index+"");
        addSubscription(observable, new Subscriber<BasketTeamResultBean>() {
            @Override
            public void onCompleted() {
                mPage.index++;
            }

            @Override
            public void onError(Throwable e) {
                mView.onError();
            }

            @Override
            public void onNext(BasketTeamResultBean bean) {
                if(bean.getResult()==200&&null!=bean.getTeamMatchData()&&bean.getTeamMatchData().size()!=0){
                    mView.showNextPage(bean.getTeamMatchData());
                }else {
                    mView.showNoMoreData();
                }
            }
        });
    }

    @Override
    public List<BasketTeamResultBean.TeamMatchDataEntity> getListData() {
        return mListData;
    }
}
