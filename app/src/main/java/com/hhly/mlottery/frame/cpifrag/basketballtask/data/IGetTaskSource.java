package com.hhly.mlottery.frame.cpifrag.basketballtask.data;

/**
 * @author: Wangg
 * @Name：IGetTaskSource
 * @Description:
 * @Created on:2017/3/17  17:15.
 */

public interface IGetTaskSource {

    //篮球指数列表
    void getBasketIndexCenter( String date, String type, OnTaskDataListener.BasketIndex iGetTaskData);

    //篮球指数列表详情
    void getBasketIndexCenterDetails(String comId, String thirdId, String oddsType, OnTaskDataListener.BasketIndexDetails iGetTaskData);

}
