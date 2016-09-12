package com.hhly.mlottery.bean.footballDetails;

/**
 * description:
 * author: yixq
 * Created by A on 2016/9/9.
 */
public class FootballRankingData {

    /**
         tid: 747,
         name: "捷克",
         round: 10,
         win: 7,
         equ: 1,
         fail: 2,
         goal: 19,
         loss: 14,
         abs: 5,
         score: 22
     */

    private int tid;
    private String name;
    private int round;
    private int win;
    private int equ;
    private int fail;
    private int goal;
    private int loss;
    private int abs;
    private int score;

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getEqu() {
        return equ;
    }

    public void setEqu(int equ) {
        this.equ = equ;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public int getAbs() {
        return abs;
    }

    public void setAbs(int abs) {
        this.abs = abs;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
