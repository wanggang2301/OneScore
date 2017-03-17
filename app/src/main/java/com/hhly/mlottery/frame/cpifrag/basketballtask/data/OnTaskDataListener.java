package com.hhly.mlottery.frame.cpifrag.basketballtask.data;

import com.hhly.mlottery.bean.snookerbean.SnookerRankBean;

/**
 * @author: Wangg
 * @Name：IGetTaskData
 * @Description:
 * @Created on:2017/3/17  16:24.
 */

public interface OnTaskDataListener {

    void getDataError();

    void getDataSucess(SnookerRankBean o);

    void getNoData();


}
