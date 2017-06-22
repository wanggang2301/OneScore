package com.hhly.mlottery.mvp.bettingmvp.eventbusconfig;

/**
 * Created by：XQyi on 2017/6/22 12:03
 * Use: 返回发布页面event
 */
public class IssueResultEventBus {

    boolean issueResult;
    public IssueResultEventBus(boolean issueResult){
        this.issueResult = issueResult;
    }

    public boolean issueResult() {
        return issueResult;
    }

    public void setIssueResult(boolean issueResult) {
        this.issueResult = issueResult;
    }
}
