package com.hhly.mlottery.frame.cpifrag.SnookerIndex.SnookerChildFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.hhly.mlottery.bean.snookerbean.snookerIndexBean.SnookerIndexBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.BallType;
import com.hhly.mlottery.mvp.BasePresenter;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * 描    述：
 * 作    者：mady@13322.com
 * 时    间：2017/3/20
 */
public class SnookerIndexChildPresenter extends BasePresenter<SnookerIndexChildContract.View>implements SnookerIndexChildContract.Presenter {

    private List<SnookerIndexBean.CompanyEntity> mCompanyList=new ArrayList<>();
    private List<SnookerIndexBean.AllInfoEntity> mData=new ArrayList<>();
    private List<SnookerIndexBean.AllInfoEntity> mRawData=new ArrayList<>();

    public static String NODATA="1";
    public static String NETERROR="2";

    public SnookerIndexChildPresenter(SnookerIndexChildContract.View view) {
        super(view);

        mData=new ArrayList<>();

    }

    @Override
    public void refreshByDate(String date,String type,int ballType) {
        boolean isFirst=false;
        if(date.equals("")){ //第一次请求无地址
            date=DateUtil.getMomentDate();
            isFirst=true;
        }
//        String url="http://m.1332255.com:81/mlottery/core/snookerOdds.getSnookerOddsByDate.do";
        String url="";
        Map<String,String> params=new HashMap<>();
        if(ballType== BallType.SNOOKER){
            url=BaseURLs.SNOOKER_INDEX_LIST;
            params.put("oddType",type);
        }else{
            url="http://192.168.31.1:8080/mlottery/core/mlottery/tennisIndexData.findIndexDataList.do";
            url=BaseURLs.TENNIS_INDEX_LIST;
            params.put("type",type);
        }

        params.put("date",date);
        final boolean finalIsFirst = isFirst;
        VolleyContentFast.requestJsonByGet(url, params,new VolleyContentFast.ResponseSuccessListener<SnookerIndexBean>() {
            @Override
            public void onResponse(SnookerIndexBean jsonObject) {
                if(jsonObject.getCode()!=200) {
                    if(finalIsFirst){
                        mView.setParentData(jsonObject);
                    }
                    mView.showNoData(NODATA);}
                else {
                    mRawData=jsonObject.getAllInfo();

                    mView.handleCompany(jsonObject.getCompany());
                    if(finalIsFirst){
                        mView.setParentData(jsonObject);
                    }

                    filterCompany();

                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mView.showNoData(NETERROR);
            }
        },SnookerIndexBean.class);
    }


    @Override
    public List<SnookerIndexBean.AllInfoEntity> getData() {
        return mData;
    }

    /**
     * 筛选公司
     */
    @Override
    public void filterCompany(){
        mData.clear();
        for(SnookerIndexBean.AllInfoEntity entity:mRawData){
            SnookerIndexBean.AllInfoEntity clone=entity.clone();
            List<SnookerIndexBean.AllInfoEntity.ComListEntity> comList=clone.getComList();
            ListIterator<SnookerIndexBean.AllInfoEntity.ComListEntity> iterator=comList.listIterator();
            while (iterator.hasNext()){
                SnookerIndexBean.AllInfoEntity.ComListEntity next=iterator.next();
                if(!isOddsShow(next)){
                    iterator.remove();
                }
            }
            if(comList.size()>0){
                mData.add(clone);
            }
        }
        if(mData.size()==0){mView.showNoData(NODATA);}
        else{
            mView.recyclerViewNotify(); //mData是recycleView的数据源
        }

    }

    /**
     * 是否是显示的赔率
     *
     * @param comListBean ComListBean
     * @return 是否显示
     */
    private boolean isOddsShow(SnookerIndexBean.AllInfoEntity.ComListEntity comListBean) {
        boolean show = false;
        ArrayList<SnookerIndexBean.CompanyEntity> companyList = (ArrayList<SnookerIndexBean.CompanyEntity>) mView.getCompanyList();
        if (companyList != null && companyList.size() > 0) {
            for (SnookerIndexBean.CompanyEntity company : companyList) {
                if (comListBean.getComName().equals(company.getComName()) && company.isChecked()) {
                    show = true;
                }
            }
        } else {

        }
        return show;
    }

}
