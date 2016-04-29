package com.hhly.mlottery.bean.basket;

/**
 * Created by A on 2016/1/4.
 */
public class BasketOddsBean {

    private BasketOddBean asiaSize;//	Object
    private BasketOddBean euro;//	Object
    private BasketOddBean asiaLet;//	Object

    public BasketOddBean getEuro() {
        return euro;
    }

    public void setEuro(BasketOddBean euro) {
        this.euro = euro;
    }

    public BasketOddBean getAsiaLet() {
        return asiaLet;
    }

    public void setAsiaLet(BasketOddBean asiaLet) {
        this.asiaLet = asiaLet;
    }

    public BasketOddBean getAsiaSize() {
        return asiaSize;
    }

    public void setAsiaSize(BasketOddBean asiaSize) {
        this.asiaSize = asiaSize;
    }
}
