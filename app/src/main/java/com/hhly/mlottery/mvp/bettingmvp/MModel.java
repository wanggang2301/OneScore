package com.hhly.mlottery.mvp.bettingmvp;

import java.util.Map;

/**
 * Created by：XQyi on 2017/5/2 10:47
 * Use:MVP_model接口
 */
public interface MModel {

    /**
     * 数据请求
     * @param url
     */
    void loadData(String url , Map<String ,String> parametMap);
}
