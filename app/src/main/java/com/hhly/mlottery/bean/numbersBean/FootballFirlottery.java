package com.hhly.mlottery.bean.numbersBean;

import java.io.Serializable;

/**
 * desc:
 * Created by 107_tangrr on 2016/11/22 0022.
 */

public class FootballFirlottery implements Serializable {

    private String jackpot; //奖金池
    private String sales;//全国销量
    private String firCount;//一等奖中奖注数
    private String firSinBon;//任选9场一等奖单注奖金
    private String secCount;
    private String secSinBon;

    public String getJackpot() {
        return jackpot;
    }

    public void setJackpot(String jackpot) {
        this.jackpot = jackpot;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getFirCount() {
        return firCount;
    }

    public void setFirCount(String firCount) {
        this.firCount = firCount;
    }

    public String getFirSinBon() {
        return firSinBon;
    }

    public void setFirSinBon(String firSinBon) {
        this.firSinBon = firSinBon;
    }

    public String getSecCount() {
        return secCount;
    }

    public void setSecCount(String secCount) {
        this.secCount = secCount;
    }

    public String getSecSinBon() {
        return secSinBon;
    }

    public void setSecSinBon(String secSinBon) {
        this.secSinBon = secSinBon;
    }
}
