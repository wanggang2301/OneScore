package com.hhly.mlottery.frame.basketballframe.basketballteam.basketballteamdata;

import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

import java.util.List;

import data.bean.BasketTeamDataBean;
import data.bean.BasketTeamPlayerBean;

/**
 * 描    述：篮球球队球员数据
 * 作    者：mady@13322.com
 * 时    间：2017/6/20
 */
public class BasketDataContract {

    interface View extends IView{
        void recyclerNotify(); //
        void setRefresh(boolean refresh);
        void showRankInfo();
        void showWinRateDescription();
        void showForwardPlayer();
        void showCenterPlayer();
        void showDefender();
        void showNoData();
    }
    interface Presenter extends IPresenter<View>{

        void refreshData(String season ,String leagueId,String teamId );

        BasketTeamDataBean.TeamInfoEntity getRankInfo();
        BasketTeamDataBean.TeamInfoEntity.MatchStatEntity getWinRate();
        List<BasketTeamPlayerBean> getForward();
        List<BasketTeamPlayerBean> getCenter();
        List<BasketTeamPlayerBean> getDefender();

    }
}
