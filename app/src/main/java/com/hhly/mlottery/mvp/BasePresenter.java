package com.hhly.mlottery.mvp;

import com.hhly.mlottery.MyApp;

import data.DataManager;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描    述：Presenter 基类
 * 作    者：mady@13322.com
 * 时    间：2017/3/16
 */
public abstract class BasePresenter<V extends IView> implements IPresenter<V> {

    protected final String TAG = getClass().getSimpleName();
    protected V mView; //子类presenter可以接调用

    public static String NODATA = "1";
    public static String NETERROR = "2";

    public DataManager mDataManager;


    CompositeSubscription mCompositeSubscription;

    private BasePresenter() {
        // dataManagerr配置
        //mDataManager  = MyApp.get
        mDataManager = MyApp.getDataManager();

    }

    public BasePresenter(V view) {
        this();
        attachView(view);
    }

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        //Rx取消注册
        onUnSubscribe();

    }


    /**
     * rxJava取消注册，以免内存泄露
     */
    protected void onUnSubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public <T> void addSubscription(Observable<T> observable, Subscriber<T> subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        Subscription subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        mCompositeSubscription.add(subscription);
    }
}
