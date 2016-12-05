package com.hhly.mlottery.bean.homepagerentity;

import java.io.Serializable;

/**
 * desc:首页新彩票条目实体
 * Created by 107_tangrr on 2016/11/26 0026.
 */

public class HomeBodysLottery implements Serializable {

    private int jumpType;// 跳转类型	 0无 1页面 2跳内页
    private String jumpAddr;
    private String name;
    private String issue;
    private String numbers;
    private String picUrl;
    private String jackpot;

    public int getJumpType() {
        return jumpType;
    }

    public void setJumpType(int jumpType) {
        this.jumpType = jumpType;
    }

    public String getJumpAddr() {
        return jumpAddr;
    }

    public void setJumpAddr(String jumpAddr) {
        this.jumpAddr = jumpAddr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getJackpot() {
        return jackpot;
    }

    public void setJackpot(String jackpot) {
        this.jackpot = jackpot;
    }
}
