package com.hhly.mlottery.bean.footballDetails.footballdatabasebean;

import java.util.List;

/**
 * author: yixq
 * Created by Administrator on 2016/10/9.
 * 足球资料库大小球Bean
 */

public class FootballDatabaseBigSmallBean {

    /**
     * home: [],
     guest: [],
     code: 200,
     all: []
     */
    private List<DatabaseBigSmallBean> home;
    private List<DatabaseBigSmallBean> guest;
    private List<DatabaseBigSmallBean> all;
    private int code;

    public List<DatabaseBigSmallBean> getHome() {
        return home;
    }

    public void setHome(List<DatabaseBigSmallBean> home) {
        this.home = home;
    }

    public List<DatabaseBigSmallBean> getGuest() {
        return guest;
    }

    public void setGuest(List<DatabaseBigSmallBean> guest) {
        this.guest = guest;
    }

    public List<DatabaseBigSmallBean> getAll() {
        return all;
    }

    public void setAll(List<DatabaseBigSmallBean> all) {
        this.all = all;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
