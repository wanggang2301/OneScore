package com.hhly.mlottery.bean.footballDetails;

import java.util.List;

import data.bean.BottomOddsItem;

/**
 * @author wang gang
 * @date 2016/6/7 11:54

 * @des ${}
 */
public class BottomOdds {

    private String result;

    private List<BottomOddsItem> asianlistOdd;
    private List<BottomOddsItem> europelistOdd;
    private List<BottomOddsItem> overunderlistOdd;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<BottomOddsItem> getAsianlistOdd() {
        return asianlistOdd;
    }

    public void setAsianlistOdd(List<BottomOddsItem> asianlistOdd) {
        this.asianlistOdd = asianlistOdd;
    }

    public List<BottomOddsItem> getEuropelistOdd() {
        return europelistOdd;
    }

    public void setEuropelistOdd(List<BottomOddsItem> europelistOdd) {
        this.europelistOdd = europelistOdd;
    }

    public List<BottomOddsItem> getOverunderlistOdd() {
        return overunderlistOdd;
    }

    public void setOverunderlistOdd(List<BottomOddsItem> overunderlistOdd) {
        this.overunderlistOdd = overunderlistOdd;
    }




}
