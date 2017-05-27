package com.hhly.mlottery.frame.footballframe.bowl.data;

import android.content.Context;

import com.hhly.mlottery.frame.footballframe.bowl.data.repository.BowlReposeitory;

import javax.inject.Inject;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/4/8  14:26.
 */

public class DataManager {

    @Inject
    public BowlReposeitory bowlReposeitory;   //在DataModule中已经被注入，dataManager里面可以直接使用

    public DataManager(Context context, String apiHostUrl, String timeZone, String lang) {

        DaggerDataComponent.builder()
                .dataModule(new DataModule(context, apiHostUrl, timeZone, lang))
                .build()
                .inject(this);


    }
}
