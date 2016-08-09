package com.hhly.mlottery.bean.basket.basketdatabase;

import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */

public class BasketDatabaseLeagueMost {

    private List<BasketDatabaseMostDat> bestDefensive; //	Array
    private List<BasketDatabaseMostDat> bestOffensive;//	Array
    private List<BasketDatabaseMostDat> undefended;//	Array
    private List<BasketDatabaseMostDat> unoffsensive;//	Array


    public List<BasketDatabaseMostDat> getUnoffsensive() {
        return unoffsensive;
    }

    public void setUnoffsensive(List<BasketDatabaseMostDat> unoffsensive) {
        this.unoffsensive = unoffsensive;
    }

    public List<BasketDatabaseMostDat> getBestDefensive() {
        return bestDefensive;
    }

    public void setBestDefensive(List<BasketDatabaseMostDat> bestDefensive) {
        this.bestDefensive = bestDefensive;
    }

    public List<BasketDatabaseMostDat> getBestOffensive() {
        return bestOffensive;
    }

    public void setBestOffensive(List<BasketDatabaseMostDat> bestOffensive) {
        this.bestOffensive = bestOffensive;
    }

    public List<BasketDatabaseMostDat> getUndefended() {
        return undefended;
    }

    public void setUndefended(List<BasketDatabaseMostDat> undefended) {
        this.undefended = undefended;
    }
}
