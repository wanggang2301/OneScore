package com.hhly.mlottery.frame.footballframe.corner;

import com.hhly.mlottery.bean.corner.CornerListBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.PreferenceUtil;
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

    Map<String,String> params=new HashMap<>();

    @Override
    public List<CornerListBean.CornerEntity> getData() {
        return mData;
    }

    @Override
    public void refreshDataByPage(String type, final int position, boolean refresh) {

        if(position>7){
            mView.showNoMoreData();

            return;
        }
        String url;
        if(type.equals("0")){
            url=BaseURLs.CORNER_LIST;
//            params.put("date","2017-05-19");
        }else {url=BaseURLs.CORNER_LIST;
            params.put("ids", PreferenceUtil.getString(StaticValues.CORNER_FOCUS_ID,""));
        }

        List mDatelist = new ArrayList<String>();
        for (int i = 1; i >-7; i--) {
            mDatelist.add(DateUtil.getDate(i, DateUtil.getMomentDate()));
        }
        params.put("date", mDatelist.get(position).toString());

        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<CornerListBean>() {
            @Override
            public void onResponse(CornerListBean jsonObject) {
                if(jsonObject.getResult()==200&&jsonObject.getCorner().size()!=0){
                    mView.setDateListPosition(position);
                    mView.showNextPage(jsonObject.getCorner());

                }else {
                    mView.showNoMoreData();
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mView.showNextPageError();
            }
        },CornerListBean.class);



    }


    @Override
    public void refreshData(String type,int position , boolean refresh) {
        mView.refreshShow(refresh);
        String url;
        if(type.equals("0")){
            url=BaseURLs.CORNER_LIST;
//            params.put("date","2017-05-19");
        }else {url=BaseURLs.CORNER_LIST;
            params.put("ids", PreferenceUtil.getString(StaticValues.CORNER_FOCUS_ID,""));
        }

        List mDatelist = new ArrayList<String>();
        for (int i = 1; i > -7; i--) {
            mDatelist.add(DateUtil.getDate(i, DateUtil.getMomentDate()));
        }
        params.put("date", mDatelist.get(position).toString());


        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<CornerListBean>() {
            @Override
            public void onResponse(CornerListBean jsonObject) {
               if(jsonObject.getResult()==200&&jsonObject.getCorner().size()!=0){
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

        mView.setDateListPosition(position);
    }
}
