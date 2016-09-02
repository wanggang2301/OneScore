package com.hhly.mlottery.bean.footballDetails.database;

import java.util.List;

/**
 * 描述:  ${TODO}
 * 作者:  wangg@13322.com
 * 时间:  2016/9/2 17:13
 */
public class LeagueDataBase {
    private String code;
    private List<DataBaseBean> internation;
    private List<NationBean> nation;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataBaseBean> getInternation() {
        return internation;
    }

    public void setInternation(List<DataBaseBean> internation) {
        this.internation = internation;
    }

    public List<NationBean> getNation() {
        return nation;
    }

    public void setNation(List<NationBean> nation) {
        this.nation = nation;
    }
}
