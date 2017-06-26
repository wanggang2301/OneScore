package com.hhly.mlottery.mvp.bettingmvp.eventbusconfig;

/**
 * Created by：XQyi on 2017/6/22 15:42
 * Use: 发布成功后通知页面更新
 */
public class IssueSuccessResulyEventBus {

    boolean issueSucce;
    public IssueSuccessResulyEventBus(boolean issueSucce){
        this.issueSucce = issueSucce;
    }

    public boolean issueSucce() {
        return issueSucce;
    }

    public void setIssueSucce(boolean issueSucce) {
        this.issueSucce = issueSucce;
    }
}
