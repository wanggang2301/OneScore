package com.hhly.mlottery.frame.cpifrag.tennisfragment;

import com.hhly.mlottery.bean.basket.index.BasketIndexDetailsBean;
import com.hhly.mlottery.bean.tennisball.tennisindex.TennisIndexDetailsBean;
import com.hhly.mlottery.frame.cpifrag.basketballtask.indexdetail.BasketIndexDetailsContract;
import com.hhly.mlottery.mvp.IPresenter;
import com.hhly.mlottery.mvp.IView;

import java.util.List;

/**
 * 描    述：网球详情页契约类
 * 作    者：mady@13322.com
 * 时    间：2017/4/7
 */
public interface TennisIndexDetailsContract {

    interface IndexDetailsView extends IView {
        void initFgView();
    }

    interface IndexDetailsPresenter extends IPresenter<TennisIndexDetailsContract.IndexDetailsView> {
        void initFg();
    }


    //亚盘、大小球、欧赔

    interface IndexDetailsChildView extends IView {
        void showLoadView();

/*
        void onErrorComOddFromComId();*/

        void noDataView();

        void showLeftListView();

        void showRightRecyclerView();

        void setTitle();

        void showRequestSucess();

    }

    interface IndexDetailsChildPresenter extends IPresenter<TennisIndexDetailsContract.IndexDetailsChildView> {
        void showRequestData(String comId, String thirdId, String oddsType);

        // void getRequestComOddsData(String comId, String thirdId, String oddsType);

        void showLoad();

        List<TennisIndexDetailsBean.DataEntity.ComListEntity> getComLists();

        List<TennisIndexDetailsBean.DataEntity.OddsDataEntity> getOddsLists();

    }
}
