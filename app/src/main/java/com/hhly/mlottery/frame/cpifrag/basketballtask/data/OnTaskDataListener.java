package com.hhly.mlottery.frame.cpifrag.basketballtask.data;

import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.bean.basket.index.BasketIndexDetailsBean;

/**
 * @author: Wangg
 * @Name：IGetTaskData
 * @Description:
 * @Created on:2017/3/17  16:24.
 */

public interface OnTaskDataListener {

    void getDataError();

    void getNoData(String date);

    interface BasketIndex extends OnTaskDataListener {
        void getDataSucess(BasketIndexBean o);
    }

    interface BasketIndexDetails extends OnTaskDataListener {
        void getDataSucess(BasketIndexDetailsBean o);
    }


}
