package com.hhly.mlottery.bean.footballDetails.database;

import java.util.List;

/**
 * 描述:  ${TODO}
 * 作者:  wangg@13322.com
 * 时间:  2016/9/2 17:14
 */
public class NationBean {

    private String id;
    private String name;
    private String pic;
    private boolean isShow = false;

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    private List<DataBaseBean> leagueMenues;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<DataBaseBean> getLeagueMenues() {
        return leagueMenues;
    }

    public void setLeagueMenues(List<DataBaseBean> leagueMenues) {
        this.leagueMenues = leagueMenues;
    }
}
