package com.hhly.mlottery.bean.basket.infomation;

import java.util.List;

/**
 * Created by www on 2016/7/18.
 */
public class Be {

    private B b; //父亲

    private List<B> list; //孩子

    public Be(B b, List<B> list) {
        this.b = b;
        this.list = list;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public List<B> getList() {
        return list;
    }

    public void setList(List<B> list) {
        this.list = list;
    }

}
