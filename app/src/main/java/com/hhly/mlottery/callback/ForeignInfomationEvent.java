package com.hhly.mlottery.callback;

/**
 * @author: Wangg
 * @Name：ForeignInfomationEvent
 * @Description:
 * @Created on:2016/9/23  15:10.
 */

public class ForeignInfomationEvent {


    public ForeignInfomationEvent(int id, int favroite) {
        this.id = id;
        this.favroite = favroite;
    }

    private int id;

    private int favroite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFavroite() {
        return favroite;
    }

    public void setFavroite(int favroite) {
        this.favroite = favroite;
    }
}
