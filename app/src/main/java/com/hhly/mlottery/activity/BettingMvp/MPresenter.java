package com.hhly.mlottery.activity.BettingMvp;

import com.hhly.mlottery.bean.basket.BasketNewRootBean;

/**
 * Created by：XQyi on 2017/5/2 10:47
 * Use:
 */
public interface MPresenter {

    /**
     * 接口请求
     * @param url
     */
    void loadData(String url);

    /**
     * 请求成功
     */
    void loadSuccess(BasketNewRootBean beanData);

    /**
     * 请求失败
     */
    void loadFail();

    /**
     * 暂无数据
     */
    void noData();
}
