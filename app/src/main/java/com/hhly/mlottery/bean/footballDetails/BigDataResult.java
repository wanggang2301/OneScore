package com.hhly.mlottery.bean.footballDetails;

/**
 * 大数据预测接口返回结果
 * <p/>
 * Created by loshine on 2016/7/19.
 */
public class BigDataResult {

    private int result;
    private BigDataForecast bigDataForecast;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public BigDataForecast getBigDataForecast() {
        return bigDataForecast;
    }

    public void setBigDataForecast(BigDataForecast bigDataForecast) {
        this.bigDataForecast = bigDataForecast;
    }
}
