package com.hhly.mlottery.activity.bettingmvp.mvpmodel;

import com.hhly.mlottery.activity.bettingmvp.MModel;
import com.hhly.mlottery.activity.bettingmvp.MPresenter;
import com.hhly.mlottery.bean.basket.BasketNewRootBean;
import com.hhly.mlottery.util.net.VolleyContentFast;

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
    public void loadData(String url) {

        VolleyContentFast.requestJsonByGet(url, new VolleyContentFast.ResponseSuccessListener<BasketNewRootBean>() {
            @Override
            public void onResponse(BasketNewRootBean jsonBean) {
                if (jsonBean == null) {
                    mPresenter.noData();
                }
                mPresenter.loadSuccess(jsonBean);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mPresenter.loadFail();
            }
        },BasketNewRootBean.class);
    }
}
