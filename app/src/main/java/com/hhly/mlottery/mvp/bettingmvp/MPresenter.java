package com.hhly.mlottery.mvp.bettingmvp;


import java.util.Map;

/**
 * Created by：XQyi on 2017/5/2 10:47
 * Use: MVP_Presenter接口
 */
public interface MPresenter<T> {

    /**
     * 接口请求
     * @param url
     */
    void loadData(String url , Map<String ,String> parametMap);

    /**
     * 请求成功
     */
    void loadSuccess(T t);

    /**
     * 请求失败
     */
    void loadFail();

    /**
     * 暂无数据
     */
    void noData();
}
