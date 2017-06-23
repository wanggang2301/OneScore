package com.hhly.mlottery.mvp.bettingmvp.mvpmodel;

import com.hhly.mlottery.bean.bettingbean.BettingOrderDataBean;
import com.hhly.mlottery.mvp.bettingmvp.MModel;
import com.hhly.mlottery.mvp.bettingmvp.MPresenter;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseBean;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by：XQyi on 2017/5/22 14:34
 * Use:支付页面（选择支付方式 [MVP-data.model 数据请求处理]）
 */
public class MvpBettingOnlinePaymentModel implements MModel {

    private MPresenter mPresenter;

    public MvpBettingOnlinePaymentModel (MPresenter presenter){
        this.mPresenter = presenter;
    }
    @Override
    public void loadData(String url , Map<String ,String> parametMap) {

        VolleyContentFast.requestJsonByGet(url, parametMap, new VolleyContentFast.ResponseSuccessListener<BettingOrderDataBean>() {
            @Override
            public void onResponse(BettingOrderDataBean jsonObject) {
                if (jsonObject == null || jsonObject.getCode() == null) {
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
        },BettingOrderDataBean.class);
    }
}
