package com.hhly.mlottery.bean.basket.BasketDetails;

import java.util.List;

/**
 * Created by A on 2016/4/5.
 */
public class BasketAnalyzeBean {

    private BasketAnalyzeContentBean guestData; //	Object
    private BasketAnalyzeContentBean homeData; //	Object

    public BasketAnalyzeContentBean getGuestData() {
        return guestData;
    }

    public void setGuestData(BasketAnalyzeContentBean guestData) {
        this.guestData = guestData;
    }

    public BasketAnalyzeContentBean getHomeData() {
        return homeData;
    }

    public void setHomeData(BasketAnalyzeContentBean homeData) {
        this.homeData = homeData;
    }
}
