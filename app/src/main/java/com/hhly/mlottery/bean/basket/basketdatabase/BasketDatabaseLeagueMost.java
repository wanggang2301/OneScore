package com.hhly.mlottery.bean.basket.basketdatabase;

import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */

public class BasketDatabaseLeagueMost {

    private List<BasketDatabaseMostDat> strongestDefense; //	Array
    private List<BasketDatabaseMostDat> weakestDefense;//	Array
    private List<BasketDatabaseMostDat> strongestAttack;//	Array
    private List<BasketDatabaseMostDat> weakestAttack;//	Array

    public List<BasketDatabaseMostDat> getStrongestDefense() {
        return strongestDefense;
    }

    public void setStrongestDefense(List<BasketDatabaseMostDat> strongestDefense) {
        this.strongestDefense = strongestDefense;
    }

    public List<BasketDatabaseMostDat> getWeakestDefense() {
        return weakestDefense;
    }

    public void setWeakestDefense(List<BasketDatabaseMostDat> weakestDefense) {
        this.weakestDefense = weakestDefense;
    }

    public List<BasketDatabaseMostDat> getStrongestAttack() {
        return strongestAttack;
    }

    public void setStrongestAttack(List<BasketDatabaseMostDat> strongestAttack) {
        this.strongestAttack = strongestAttack;
    }

    public List<BasketDatabaseMostDat> getWeakestAttack() {
        return weakestAttack;
    }

    public void setWeakestAttack(List<BasketDatabaseMostDat> weakestAttack) {
        this.weakestAttack = weakestAttack;
    }
}
