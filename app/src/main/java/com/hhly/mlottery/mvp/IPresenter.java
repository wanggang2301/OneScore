package com.hhly.mlottery.mvp;

/**
 * 描    述：Presenter接口
 * 作    者：mady@13322.com
 * 时    间：2017/3/16
 */
public interface IPresenter <V extends IView> {

    void attachView(V view);

    void detachView();
}
