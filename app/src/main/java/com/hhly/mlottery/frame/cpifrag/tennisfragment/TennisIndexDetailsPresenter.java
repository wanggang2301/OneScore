package com.hhly.mlottery.frame.cpifrag.tennisfragment;

import com.hhly.mlottery.mvp.BasePresenter;

/**
 * 描    述：网球指数详情页父页
 * 作    者：mady@13322.com
 * 时    间：2017/4/7
 */
public class TennisIndexDetailsPresenter extends BasePresenter<TennisIndexDetailsContract.IndexDetailsView> implements TennisIndexDetailsContract.IndexDetailsPresenter{


    public TennisIndexDetailsPresenter(TennisIndexDetailsContract.IndexDetailsView view) {
        super(view);
        mView=view;
    }

    @Override
    public void initFg() {

        mView.initFgView();
    }
}
