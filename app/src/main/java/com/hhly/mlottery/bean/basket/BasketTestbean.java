package com.hhly.mlottery.bean.basket;

import java.util.Map;

/**
 * Created by A on 2016/1/15.
 */
public class BasketTestbean {

    private int type;
//    private List<Map<String, Object>> data_list;
    private Map<String, Object> data_list;
    private String tittle;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Map<String, Object> getData_list() {
        return data_list;
    }

    public void setData_list(Map<String, Object> data_list) {
        this.data_list = data_list;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }
}
