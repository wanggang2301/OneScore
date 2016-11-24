package com.hhly.mlottery.bean;

import java.security.PublicKey;

/**
 * Created by yuely198 on 2016/11/23.
 */

public class ChoseHeadStartBean {

    public  String  startUrl;

    public   ChoseHeadStartBean (String Url){

        startUrl=Url;

    }

    public String getStartUrl() {
        return startUrl;
    }

    public void setStartUrl(String startUrl) {
        this.startUrl = startUrl;
    }
}
