package com.hhly.mlottery.bean.numbersBean;

import java.io.Serializable;

/**
 * desc:
 * Created by 107_tangrr on 2017/1/12 0012.
 */

public class CoedNotAppearBean implements Serializable {

    private int coedNotAppear;
    private String key;

    public int getCoedNotAppear() {
        return coedNotAppear;
    }

    public void setCoedNotAppear(int coedNotAppear) {
        this.coedNotAppear = coedNotAppear;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
