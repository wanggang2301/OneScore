package com.hhly.mlottery.frame.cpifrag.basketballtask.data;

/**
 * @author: Wangg
 * @Name：IGetTaskSource
 * @Description:
 * @Created on:2017/3/17  17:15.
 */

public interface IGetTaskSource {
    void getBasketIndexCenter(String lang,String timeZone,String pageSize,String pageNum,OnTaskDataListener iGetTaskData);
}
