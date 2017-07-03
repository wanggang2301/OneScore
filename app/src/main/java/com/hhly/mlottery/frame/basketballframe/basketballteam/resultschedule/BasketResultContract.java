package com.hhly.mlottery.frame.basketballframe.basketballteam.resultschedule;

import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

import java.util.List;

import data.bean.BasketTeamResultBean;
import data.bean.Page;
import data.bean.RechargeBean;

/**
 * 描    述：篮球球队赛程赛果
 * 作    者：mady@13322.com
 * 时    间：2017/6/20
 */
public class BasketResultContract {

    interface View extends IView{
        void recyclerNotify();
        void showNoData();
        void setRefresh(boolean refresh);
        void showNoMoreData();
        void showNextPage(List<BasketTeamResultBean.TeamMatchDataEntity> recordEntity);
    }

    interface Presenter extends IPresenter<View> {
        Page getPage();
        void refreshData(String season,String leagueId,String teamId );
        void refreshDataByPage(String season,String leagueId,String teamId );
        List<BasketTeamResultBean.TeamMatchDataEntity> getListData();
    }
}
