package com.hhly.mlottery.mvp.bettingmvp.mvpmodel;

import com.hhly.mlottery.mvp.bettingmvp.MModel;
import com.hhly.mlottery.mvp.bettingmvp.MPresenter;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseBean;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by：XQyi on 2017/5/22 11:10
 * Use:竞彩单关页面[MVP_model 数据请求 整理]
 */
public class MvpBettingPayDetailsModel implements MModel {

    private MPresenter mPresenter;
    public MvpBettingPayDetailsModel(MPresenter presenter){
        this.mPresenter = presenter;
    }

    @Override
    public void loadData(String url , Map<String ,String> parametMap) {
        Map<String ,String> map = new HashMap<>();
        map.put("leagueId" , "1");
        VolleyContentFast.requestJsonByGet(url,map , new VolleyContentFast.ResponseSuccessListener<BasketDatabaseBean>() {
            @Override
            public void onResponse(BasketDatabaseBean json) {
                if (json == null) {
                    mPresenter.noData();
                }else{
                    mPresenter.loadSuccess(json);
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
