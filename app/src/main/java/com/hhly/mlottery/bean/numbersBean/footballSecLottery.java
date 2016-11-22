package com.hhly.mlottery.bean.numbersBean;

import java.io.Serializable;

/**
 * desc:
 * Created by 107_tangrr on 2016/11/22 0022.
 */

public class FootballSecLottery implements Serializable{

    private String rsales; // 任选9场全国销量
    private String rfirCount; // 一等奖中奖注数
    private String rfirSinBon; // 任选9场一等奖单注奖金

    public String getRsales() {
        return rsales;
    }

    public void setRsales(String rsales) {
        this.rsales = rsales;
    }

    public String getRfirCount() {
        return rfirCount;
    }

    public void setRfirCount(String rfirCount) {
        this.rfirCount = rfirCount;
    }

    public String getRfirSinBon() {
        return rfirSinBon;
    }

    public void setRfirSinBon(String rfirSinBon) {
        this.rfirSinBon = rfirSinBon;
    }
}
