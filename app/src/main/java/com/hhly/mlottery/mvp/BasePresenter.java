package com.hhly.mlottery.mvp;

/**
 * 描    述：Presenter 基类
 * 作    者：mady@13322.com
 * 时    间：2017/3/16
 */
public abstract class BasePresenter<V extends IView>implements IPresenter<V> {

    protected final String TAG=getClass().getSimpleName();
    protected V mView; //子类presenter可以接调用

    private BasePresenter(){
        // dataManagerr配置
    }
    public BasePresenter(V view){
        this();
        attachView(view);
    }

    @Override
    public void attachView(V view) {
        mView=view;
    }

    @Override
    public void detachView() {
        //Rx取消注册
    }
}
