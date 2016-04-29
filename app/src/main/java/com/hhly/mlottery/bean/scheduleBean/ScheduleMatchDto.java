package com.hhly.mlottery.bean.scheduleBean;

/**
 * Created by asus1 on 2016/4/11.
 */
public class ScheduleMatchDto {
    private int type;
    private String date;

    private SchMatch schmatchs;

    public int getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public SchMatch getSchmatchs() {
        return schmatchs;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSchmatchs(SchMatch schmatchs) {
        this.schmatchs = schmatchs;
    }
}
