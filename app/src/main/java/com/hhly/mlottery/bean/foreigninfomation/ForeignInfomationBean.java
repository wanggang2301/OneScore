package com.hhly.mlottery.bean.foreigninfomation;

import java.util.List;

/**
 * @author: Wangg
 * @Name：ForeignInfomationBean
 * @Description:
 * @Created on:2016/9/20  16:58.
 */

public class ForeignInfomationBean {

    private String result;
    private long datetime;


    private List<OverseasInformationListBean> overseasInformationList;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public List<OverseasInformationListBean> getOverseasInformationList() {
        return overseasInformationList;
    }

    public void setOverseasInformationList(List<OverseasInformationListBean> overseasInformationList) {
        this.overseasInformationList = overseasInformationList;
    }
}
