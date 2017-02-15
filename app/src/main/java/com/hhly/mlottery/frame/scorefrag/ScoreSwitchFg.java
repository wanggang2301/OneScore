package com.hhly.mlottery.frame.scorefrag;

/**
 * @author: Wangg
 * @Name：ScoreSwitchFg
 * @Description:
 * @Created on:2017/2/14  11:39.
 */

public class ScoreSwitchFg {

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position;
    private int prePosition;

    public int getPrePosition() {
        return prePosition;
    }

    public void setPrePosition(int prePosition) {
        this.prePosition = prePosition;
    }

    public ScoreSwitchFg(int prePosition, int position) {
        this.prePosition = prePosition;
        this.position = position;
    }
}
