package com.hhly.mlottery.mvp.bettingmvp.mvpmodel;

import com.hhly.mlottery.mvp.bettingmvp.MModel;
import com.hhly.mlottery.mvp.bettingmvp.MPresenter;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseBean;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by：XQyi on 2017/5/22 14:34
 * Use:支付页面（选择支付方式 [MVP-model 数据请求处理]）
 */
public class MvpBettingOnlinePaymentModel implements MModel {

    private MPresenter mPresenter;

    public MvpBettingOnlinePaymentModel (MPresenter presenter){
        this.mPresenter = presenter;
    }
    @Override
    public void loadData(String url) {

        Map<String , String> map = new HashMap<>();
        map.put("leagueId" , "1");
        VolleyContentFast.requestJsonByGet(url, map, new VolleyContentFast.ResponseSuccessListener<BasketDatabaseBean>() {
            @Override
            public void onResponse(BasketDatabaseBean jsonObject) {
                if (jsonObject == null) {
                    mPresenter.noData();
                }else{
                    mPresenter.loadSuccess(jsonObject);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                    mPresenter.loadFail();
            }
        },BasketDatabaseBean.class);
    }
}
