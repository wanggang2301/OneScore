package com.hhly.mlottery.bean.numbersBean;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * desc:
 * Created by 107_tangrr on 2017/1/12 0012.
 */

public class NumberAppearBean{
    private int numberAppear;
    private String key;

    public int getNumberAppear() {
        return numberAppear;
    }

    public void setNumberAppear(int numberAppear) {
        this.numberAppear = numberAppear;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
