package com.hhly.mlottery.bean.footballDetails.footballdatabasebean;

/**
 * author: yixq
 * Created by Administrator on 2016/10/9.
 * 足球资料库统计Bean
 */

public class FootballDatabaseStatisticBean {

    /*
    statics: {},   33846
    code: 200,
    top: {}
     */

    private DatabaseStaticBean statics;
    private DatabaseTopBean top;
    private int code;

    public DatabaseStaticBean getStatics() {
        return statics;
    }

    public void setStatics(DatabaseStaticBean statics) {
        this.statics = statics;
    }

    public DatabaseTopBean getTop() {
        return top;
    }

    public void setTop(DatabaseTopBean top) {
        this.top = top;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
