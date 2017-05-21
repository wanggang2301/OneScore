package com.hhly.mlottery.frame.footballframe.corner;

import com.hhly.mlottery.bean.corner.CornerListBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描    述：角球Presenter
 * 作    者：mady@13322.com
 * 时    间：2017/5/18
 */
public class CornerPresenter extends BasePresenter<CornerContract.View> implements CornerContract.Presenter {

    private List<CornerListBean.CornerEntity> mData;
    public CornerPresenter(CornerContract.View view) {
        super(view);
        mData=new ArrayList<>();
    }


    @Override
    public List<CornerListBean.CornerEntity> getData() {
        return mData;
    }

    @Override
    public void refreshData(String type) {
        String url;
        if(type.equals("0")){
            url=BaseURLs.CORNER_LIST;
        }else {url="";
        }

        Map<String,String> params=new HashMap<>();

        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<CornerListBean>() {
            @Override
            public void onResponse(CornerListBean jsonObject) {
               if(jsonObject.getResult()==200){
                   mData.clear();
                   mData.addAll(jsonObject.getCorner());
                   mView.recyclerNotify();
               }else {
                   mView.showNoData(NODATA);
               }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mView.showNoData(NETERROR);
            }
        },CornerListBean.class);
    }
}
