package com.hhly.mlottery.frame.basketballframe.basketballteam.basketballteamdata;

import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.util.L;

import java.util.ArrayList;
import java.util.List;

import data.bean.BasketTeamDataBean;
import data.bean.BasketTeamPlayerBean;
import data.repository.BasketTeamDataRepository;
import rx.Observable;
import rx.Subscriber;

/**
 * 描    述：篮球球队球员数据
 *
 * 作    者：mady@13322.com
 * 时    间：2017/6/20
 */
public class BasketDataPresenter extends BasePresenter<BasketDataContract.View> implements BasketDataContract.Presenter {

    private BasketTeamDataRepository mRepository;

    private List<BasketTeamPlayerBean> mListForward=new ArrayList<>();
    private List<BasketTeamPlayerBean> mListCenter=new ArrayList<>();
    private List<BasketTeamPlayerBean> mListDefender=new ArrayList<>();
    private BasketTeamDataBean.TeamInfoEntity mRankInfo;
    private BasketTeamDataBean.TeamInfoEntity.MatchStatEntity mRateInfo;


    public BasketDataPresenter(BasketDataContract.View view) {
        super(view);
        mRepository=mDataManager.mTeamDataRepository;
    }

    @Override
    public void refreshData(String season, String leagueId, String teamId) {
        Observable<BasketTeamDataBean> observable=mRepository.getTeamData(season, leagueId, teamId);
        addSubscription(observable, new Subscriber<BasketTeamDataBean>() {
            @Override
            public void onCompleted() {
                mView.setRefresh(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setRefresh(false);
                mView.showNoData();
                L.e("?????",e.getMessage().toString()+"cuowu");
            }

            @Override
            public void onNext(BasketTeamDataBean bean) {
                if(bean.getResult()==200){
                    L.e("?????","sucesss");
                    mRankInfo=bean.getTeamInfo();
                    mView.showRankInfo();
                    if(null!=mRankInfo){
                        mView.recyclerNotify();
                        mRateInfo=mRankInfo.getMatchStat();
                        mView.showWinRateDescription();
                        if(null!=mRankInfo.getLineUp()){
                            L.e("?????","sucesss???");
                            mListForward.clear();
                            mListCenter.clear();
                            mListDefender.clear();
                            if(null!=mRankInfo.getLineUp().getForward()){
                                mListForward.addAll(mRankInfo.getLineUp().getForward());
                            }
                           if(null!=mRankInfo.getLineUp().getCenter()){
                               mListCenter.addAll(mRankInfo.getLineUp().getCenter());
                           }
                           if(null!=mRankInfo.getLineUp().getDefender()){
                               mListDefender.addAll(mRankInfo.getLineUp().getDefender());
                           }
                            mView.showForwardPlayer();
                            mView.showCenterPlayer();
                            mView.showDefender();
                        }
                    }


                }else {
                    mView.showNoData();
                }
            }
        });
    }

    @Override
    public BasketTeamDataBean.TeamInfoEntity getRankInfo() {
        return mRankInfo;
    }

    @Override
    public BasketTeamDataBean.TeamInfoEntity.MatchStatEntity getWinRate() {
        return mRateInfo;
    }

    @Override
    public List<BasketTeamPlayerBean> getForward() {
        return mListForward;
    }

    @Override
    public List<BasketTeamPlayerBean> getCenter() {
        return mListCenter;
    }

    @Override
    public List<BasketTeamPlayerBean> getDefender() {
        return mListDefender;
    }


}
