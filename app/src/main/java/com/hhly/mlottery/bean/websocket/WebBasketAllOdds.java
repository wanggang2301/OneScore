package com.hhly.mlottery.bean.websocket;

/**
 * Created by A on 2016/2/18.
 */
public class WebBasketAllOdds {

    private WebBasketOdds5 asiaLet;
    private WebBasketOdds5 asiaSize;
    private WebBasketOdds5 euro;

    public WebBasketOdds5 getAsiaLet() {
        return asiaLet;
    }

    public void setAsiaLet(WebBasketOdds5 asiaLet) {
        this.asiaLet = asiaLet;
    }

    public WebBasketOdds5 getAsiaSize() {
        return asiaSize;
    }

    public void setAsiaSize(WebBasketOdds5 asiaSize) {
        this.asiaSize = asiaSize;
    }

    public WebBasketOdds5 getEuro() {
        return euro;
    }

    public void setEuro(WebBasketOdds5 euro) {
        this.euro = euro;
    }
}
