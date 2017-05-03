package com.hhly.mlottery.activity.BettingMvp;

import com.hhly.mlottery.bean.basket.BasketNewRootBean;

/**
 * Created by：XQyi on 2017/5/2 10:47
 * Use:
 */
public interface MView {

    /**
     * 请求成功显示
     */
    void loadSuccessView(BasketNewRootBean beanData);

    /**
     * 请求失败显示
     */
    void loadFailView();

    /**
     * 暂无数据显示
     */
    void loadNoData();
}
