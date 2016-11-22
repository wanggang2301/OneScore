package com.hhly.mlottery.frame.basketballframe;

import com.hhly.mlottery.bean.basket.basketdetails.BasketEachTextLiveBean;

/**
 * @author: Wangg
 * @Name：BasketTextLiveEvent
 * @Description:
 * @Created on:2016/11/22  15:02.
 */

public class BasketTextLiveEvent {
    private BasketEachTextLiveBean basketEachTextLiveBean;

    public BasketTextLiveEvent(BasketEachTextLiveBean basketEachTextLiveBean) {
        this.basketEachTextLiveBean = basketEachTextLiveBean;
    }

    public BasketEachTextLiveBean getBasketEachTextLiveBean() {
        return basketEachTextLiveBean;
    }

    public void setBasketEachTextLiveBean(BasketEachTextLiveBean basketEachTextLiveBean) {
        this.basketEachTextLiveBean = basketEachTextLiveBean;
    }
}
