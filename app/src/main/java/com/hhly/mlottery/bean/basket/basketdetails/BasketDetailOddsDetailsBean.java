package com.hhly.mlottery.bean.basket.basketdetails;

import java.util.List;

/**
 * Created by andy on 2016/4/12 15:58.
 * <p/>
 * 描述：篮球详情界面赔率详情界面的实体类
 */
public class BasketDetailOddsDetailsBean {


    private String oddsId;

    private List<OddsDataEntity> oddsData;

    public String getOddsId() {
        return oddsId;
    }

    public void setOddsId(String oddsId) {
        this.oddsId = oddsId;
    }

    public List<OddsDataEntity> getOddsData() {
        return oddsData;
    }

    public void setOddsData(List<OddsDataEntity> oddsData) {
        this.oddsData = oddsData;
    }

}
