package com.hhly.mlottery.mvp.bettingmvp;


/**
 * Created by：XQyi on 2017/5/2 10:47
 * Use:MVP_view接口
 */
public interface MView<T>{

    /**
     * 请求成功显示
     */
    void loadSuccessView(T t);

    /**
     * 请求失败显示
     */
    void loadFailView();

    /**
     * 暂无数据显示
     */
    void loadNoData();
}
