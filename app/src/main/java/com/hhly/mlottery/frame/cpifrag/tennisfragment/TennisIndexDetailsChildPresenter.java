package com.hhly.mlottery.frame.cpifrag.tennisfragment;

import com.hhly.mlottery.bean.basket.index.BasketIndexDetailsBean;
import com.hhly.mlottery.bean.tennisball.tennisindex.TennisIndexDetailsBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.GetTaskSource;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.IGetTaskSource;
import com.hhly.mlottery.frame.cpifrag.basketballtask.data.OnTaskDataListener;
import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描    述：网球指数详情页presenter
 * 作    者：mady@13322.com
 * 时    间：2017/4/7
 */
public class TennisIndexDetailsChildPresenter extends BasePresenter<TennisIndexDetailsContract.IndexDetailsChildView>implements TennisIndexDetailsContract.IndexDetailsChildPresenter{

    private boolean isFirstRequest = false;

    private List<TennisIndexDetailsBean.DataEntity.ComListEntity> comListsBeanList;
    private List<TennisIndexDetailsBean.DataEntity.OddsDataEntity> oddsDataBeanList;


    public TennisIndexDetailsChildPresenter(TennisIndexDetailsContract.IndexDetailsChildView view) {
        super(view);
    }


    @Override
    public List<TennisIndexDetailsBean.DataEntity.ComListEntity> getComLists() {
        return comListsBeanList;
    }

    @Override
    public List<TennisIndexDetailsBean.DataEntity.OddsDataEntity> getOddsLists() {
        return oddsDataBeanList;
    }

    @Override
    public void showRequestData(String comId, String thirdId, String oddsType) {

        Map<String,String> params=new HashMap<>();

        params.put("matchId",thirdId);
        params.put("comId",comId);
        params.put("type",oddsType);
        //volley请求

        VolleyContentFast.requestJsonByGet(BaseURLs.TENNIS_INDEX_DETAILS, params, new VolleyContentFast.ResponseSuccessListener<TennisIndexDetailsBean>() {
            @Override
            public void onResponse(TennisIndexDetailsBean o) {
                if(o.getResult()==200){
                    if(o.getData().getOddsData()!=null&&o.getData().getOddsData().size()!=0){
                        comListsBeanList = o.getData().getComList();
                        oddsDataBeanList = o.getData().getOddsData();

                        if (!isFirstRequest) {
                            mView.setTitle();
                            mView.showLeftListView();
                            isFirstRequest = true;
                        }

                        mView.showRightRecyclerView();
                        mView.showRequestSucess();
                    }
                    else{ //无数据
                        mView.setTitle();

                        mView.noDataView();
                    }

                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                mView.setTitle();

                mView.onError();
                //isFirstRequest = false;
            }
        },TennisIndexDetailsBean.class);
    }


    @Override
    public void showLoad() {
        mView.showLoadView();
    }

}
