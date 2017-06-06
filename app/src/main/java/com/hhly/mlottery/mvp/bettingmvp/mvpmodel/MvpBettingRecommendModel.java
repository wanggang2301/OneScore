package com.hhly.mlottery.mvp.bettingmvp.mvpmodel;

import com.hhly.mlottery.bean.bettingbean.BettingListDataBean;
import com.hhly.mlottery.mvp.bettingmvp.MModel;
import com.hhly.mlottery.mvp.bettingmvp.MPresenter;
import com.hhly.mlottery.bean.basket.BasketNewRootBean;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.Map;

/**
 * Created by：XQyi on 2017/5/2 10:55
 * Use:竞彩推荐列表（MVP-Model 数据请求）
 */
public class MvpBettingRecommendModel implements MModel {

    private MPresenter mPresenter;
    public MvpBettingRecommendModel(MPresenter mPresenter){
        this.mPresenter = mPresenter;
    }

    @Override
    public void loadData(String url , Map<String ,String> parametMap) {

        VolleyContentFast.requestJsonByGet(url,parametMap , new VolleyContentFast.ResponseSuccessListener<BettingListDataBean>() {
            @Override
            public void onResponse(BettingListDataBean jsonBean) {
                if (jsonBean == null || jsonBean.getCode() != 200) {
                    mPresenter.noData();
                    return;
                }
                mPresenter.loadSuccess(jsonBean);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mPresenter.loadFail();
            }
        },BettingListDataBean.class);
    }
}
