package com.hhly.mlottery.bean.homepagerentity;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单对象
 * Created by hhly107 on 2016/4/7.
 */
public class HomeContentEntity implements Serializable {

    private int jumpType;//跳转类型	0无 1页面 2跳内页
    private String jumpAddr;//跳转标识
//    private String outUrl;// 游戏竞猜跳转标识
    private String title;//标题
    private String picUrl;//图标URL
    private int labType;// 标签标识	1、	热门赛事 2、	热点资讯 3、	彩票开奖
    private String reqMethod;// 请求方式
    private Integer labSeq;// 体育资讯类型
    private List<HomeBodysEntity> bodys;

    public Integer getLabSeq() {
        return labSeq;
    }

    public void setLabSeq(Integer labSeq) {
        this.labSeq = labSeq;
    }

    public String getReqMethod() {
        return reqMethod;
    }

    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }

    public int getLabType() {
        return labType;
    }

    public void setLabType(int labType) {
        this.labType = labType;
    }

    public List<HomeBodysEntity> getBodys() {
        return bodys;
    }

    public void setBodys(List<HomeBodysEntity> bodys) {
        this.bodys = bodys;
    }
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
