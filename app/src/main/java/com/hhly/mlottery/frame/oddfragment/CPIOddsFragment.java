package com.hhly.mlottery.frame.oddfragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.CpiFiltrateActivity;
import com.hhly.mlottery.adapter.cpiadapter.CPIRecyclerViewAdapter;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.CPIFragment;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 103TJL on 2016/4/6.
 * 新版 亚盘，大小球，欧赔
 */
public class CPIOddsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String[] defualtCompanyIds = {"3", "45"};
    private String mParam1;
    private String mParam2;
    private View mView;
    private Context mContext;
    private RecyclerView cpi_odds_recyclerView;
    private CPIRecyclerViewAdapter cpiRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    public static List<NewOddsInfo.AllInfoBean> mAllInfoBean1 = new ArrayList<>();
    public static List<NewOddsInfo.AllInfoBean> mAllInfoBean2 = new ArrayList<>();
    public static List<NewOddsInfo.AllInfoBean> mAllInfoBean3 = new ArrayList<>();
    public List<NewOddsInfo.AllInfoBean> mAllInfo = new ArrayList<>();
    public List<NewOddsInfo.AllInfoBean> mShowInfoBeans = new ArrayList<>();
    //热门联赛筛选
    public static List<NewOddsInfo.FileterTagsBean> mFileterTagsBean = new ArrayList<>();
    private static final int ERROR = -1;//访问失败
    private static final int SUCCESS = 0;// 访问成功
    private static final int STARTLOADING = 1;// 数据加载中
    private static final int NODATA = 400;// 暂无数据
    private static final int NODATA_CHILD = 500;// 里面内容暂无数据
    private FrameLayout cpi_fl_plate_loading;// 正在加载中
    public FrameLayout cpi_fl_plate_networkError;// 加载失败
    public FrameLayout cpi_fl_plate_noData;// 暂无数据
    private TextView cpi_plate_reLoading;// 刷新
    private CPIFragment mCpiframen;

    public static CPIOddsFragment newInstance(String param1, String param2) {
        CPIOddsFragment fragment = new CPIOddsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_cpi_odds, container, false);//item_cpi_odds
        InitView();
        switchd("", false);
        return mView;
    }

    private void InitView() {
        mCpiframen = ((CPIFragment) getParentFragment());
        linearLayoutManager = new LinearLayoutManager(mContext);
        cpi_odds_recyclerView = (RecyclerView) mView.findViewById(R.id.cpi_odds_recyclerView);
        cpi_odds_recyclerView.setHasFixedSize(true);
        cpi_odds_recyclerView.setLayoutManager(linearLayoutManager);

        //正在加载中
        cpi_fl_plate_loading = (FrameLayout) mView.findViewById(R.id.cpi_fl_plate_loading);
        //加载失败
        cpi_fl_plate_networkError = (FrameLayout) mView.findViewById(R.id.cpi_fl_plate_networkError);
        //暂无数据
        cpi_fl_plate_noData = (FrameLayout) mView.findViewById(R.id.cpi_fl_plate_noData);
        //刷新
        cpi_plate_reLoading = (TextView) mView.findViewById(R.id.cpi_plate_reLoading);// 刷新
        cpi_plate_reLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchd("", false);
                mCpiframen.public_txt_date.setText(UiUtils.requestByGetDay(0));
                mCpiframen.mMapDayList = mCpiframen.getDate();
                mCpiframen.selectPosition = 6;
            }
        });

    }

    /**
     * 传日期和各盘口类型
     *
     * @param date
     * @param type
     */
    public void InitData(String date, final String type, final boolean isDate) {
        mHandler.sendEmptyMessage(STARTLOADING);
        Map<String, String> map = new HashMap<>();

        if (type.equals(CPIFragment.TYPE_PLATE)) {
            map.put("type", "1");
        } else if (type.equals(CPIFragment.TYPE_BIG)) {
            map.put("type", "3");
        } else if (type.equals(CPIFragment.TYPE_OP)) {
            map.put("type", "2");
        }
        if (!"".equals(date)) {
            map.put("date", date);
        }

        // 2、连接服务器
        VolleyContentFast.requestJsonByPost(BaseURLs.URL_NEW_ODDS, map, new VolleyContentFast.ResponseSuccessListener<NewOddsInfo>() {
            @Override
            public synchronized void onResponse(final NewOddsInfo json) {

                if (json != null && json.getCode() != 500) {

                    if (getParentFragment() != null) {
                        mCpiframen.currentDate = json.getCurrDate();
                        mCpiframen.companys = json.getCompany();
                    }
                    mFileterTagsBean = json.getFileterTags();
                    if (type.equals(CPIFragment.TYPE_PLATE)) {
                        mAllInfoBean1.clear();
                        mAllInfoBean1 = json.getAllInfo();
                    } else if (type.equals(CPIFragment.TYPE_BIG)) {
                        mAllInfoBean2.clear();
                        mAllInfoBean2 = json.getAllInfo();
                    } else if (type.equals(CPIFragment.TYPE_OP)) {
                        mAllInfoBean3.clear();
                        mAllInfoBean3 = json.getAllInfo();
                    }
                    CpiFiltrateActivity.mCheckedIds.clear();
                    for (NewOddsInfo.FileterTagsBean fileterTagsBean : mFileterTagsBean) {
                        if (fileterTagsBean.isHot()) {
                            CpiFiltrateActivity.mCheckedIds.add(fileterTagsBean.getLeagueId());
                        }
                    }
                    //如果是日期传过来的
                    if (isDate) {
                        for (int h = 0; h < mCpiframen.companys.size(); h++) {
                            if (mCpiframen.companysName.contains(mCpiframen.companys.get(h).getComName())) {
                                mCpiframen.companys.get(h).setIsChecked(true);
                            } else {
                                mCpiframen.companys.get(h).setIsChecked(false);
                            }

                        }
                        selectCompany(mCpiframen.companysName, CpiFiltrateActivity.mCheckedIds, type);
                    }
                    //否则直接加载判断
                    else {
                        for (NewOddsInfo.CompanyBean companyBean : json.getCompany()) {
                            if (Arrays.binarySearch(defualtCompanyIds, 0, defualtCompanyIds.length, companyBean.getComId()) >= 0) {
                                companyBean.setIsChecked(true);
                            } else {
                                companyBean.setIsChecked(false);
                            }
                        }
                        mCpiframen.filtrateDate();
                        selectCompany(mCpiframen.companysName, CpiFiltrateActivity.mCheckedIds, type);

                    }

                } else {
                    mHandler.sendEmptyMessage(NODATA);// 暂无数据
                }
            }

        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERROR);// 加载失败
            }
        }, NewOddsInfo.class);
    }


    /**
     * 时间
     * @param dates
     */
    public void switchd(String dates, boolean isDate) {
        InitData(dates, mParam1, isDate);
    }

    /**
     * 传选中公司的集合
     * @param comNameList
     * 所选中联赛id的集合
     * @param mCheckedIds
     * 公司类型
     * @param comPanyType
     */
    public void selectCompany( List<String> comNameList, List<String> mCheckedIds, String comPanyType) {
        if (CPIFragment.TYPE_PLATE.equals(comPanyType)) {

            setComPany(mAllInfoBean1, comNameList, mCheckedIds, comPanyType);

        } else if (CPIFragment.TYPE_BIG.equals(comPanyType)) {
            setComPany(mAllInfoBean2, comNameList, mCheckedIds, comPanyType);

        } else if (CPIFragment.TYPE_OP.equals(comPanyType)) {
            setComPany(mAllInfoBean3, comNameList, mCheckedIds, comPanyType);
        }

    }

    /**
     * 每个不同赔率的全部数据
     * @param hotsAllInfoTemps
     * 选中公司的集合
     * @param comNameLists
     * 所选中联赛id的集合
     * @param mCheckedIds
     * 传选中公司的集合
     * @param comPanyType
     */
    private void setComPany(List<NewOddsInfo.AllInfoBean> hotsAllInfoTemps, List<String> comNameLists, List<String> mCheckedIds, String comPanyType) {
        mAllInfo.clear();
        mShowInfoBeans.clear();
        for (int k = 0; k < hotsAllInfoTemps.size(); k++) {
            NewOddsInfo.AllInfoBean pAllInfoBean = new NewOddsInfo.AllInfoBean();
            List<NewOddsInfo.AllInfoBean.ComListBean> mComListBeanList = new ArrayList<>();
            for (int j = 0; j < hotsAllInfoTemps.get(k).getComList().size(); j++) {

                for (int h = 0; h < comNameLists.size(); h++) {
                    if (hotsAllInfoTemps.get(k).getComList().get(j).getComName().equals(comNameLists.get(h))) {
                        NewOddsInfo.AllInfoBean.ComListBean mComListBean = new NewOddsInfo.AllInfoBean.ComListBean();
                        mComListBean = hotsAllInfoTemps.get(k).getComList().get(j);
                        mComListBeanList.add(mComListBean);

                    }
                }
            }
            for (int i = 0; i < mCheckedIds.size(); i++) {
                if (hotsAllInfoTemps.get(k).getLeagueId().equals(mCheckedIds.get(i))) {

                    pAllInfoBean.setComList(mComListBeanList);
                    pAllInfoBean.setMatchInfo(hotsAllInfoTemps.get(k).getMatchInfo());
                    pAllInfoBean.setHot(hotsAllInfoTemps.get(k).isHot());
                    pAllInfoBean.setLeagueColor(hotsAllInfoTemps.get(k).getLeagueColor());
                    pAllInfoBean.setLeagueId(hotsAllInfoTemps.get(k).getLeagueId());
                    pAllInfoBean.setLeagueName(hotsAllInfoTemps.get(k).getLeagueName());
                    mAllInfo.add(pAllInfoBean);
                }
            }

        }
        //循环判断每个getComList是否为空，为空的不加进去
        for (int p = 0; p < mAllInfo.size(); p++) {
            NewOddsInfo.AllInfoBean pAllInfo = new NewOddsInfo.AllInfoBean();
            if (!mAllInfo.get(p).getComList().isEmpty()) {
                pAllInfo.setComList(mAllInfo.get(p).getComList());
                pAllInfo.setMatchInfo(mAllInfo.get(p).getMatchInfo());
                pAllInfo.setHot(mAllInfo.get(p).isHot());
                pAllInfo.setLeagueColor(mAllInfo.get(p).getLeagueColor());
                pAllInfo.setLeagueId(mAllInfo.get(p).getLeagueId());
                pAllInfo.setLeagueName(mAllInfo.get(p).getLeagueName());
                mShowInfoBeans.add(pAllInfo);
            }
        }
       if(!mShowInfoBeans.isEmpty()) {
           if (cpiRecyclerViewAdapter != null) {
               cpiRecyclerViewAdapter.setAllInfoBean(mShowInfoBeans);
               cpiRecyclerViewAdapter.notifyDataSetChanged();
           } else {
               cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mShowInfoBeans, mContext, comPanyType);
               cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);

           }
           mHandler.sendEmptyMessage(SUCCESS);// 请求成功
       }else{
           mHandler.sendEmptyMessage(NODATA_CHILD);// 内容无数据
       }

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:// 访问成功
                    mCpiframen.public_img_filter.setVisibility(View.VISIBLE);
                    mCpiframen.mRefreshLayout.setRefreshing(false);
                    cpi_fl_plate_loading.setVisibility(View.GONE);
                    cpi_fl_plate_networkError.setVisibility(View.GONE);
                    cpi_fl_plate_noData.setVisibility(View.GONE);
                    cpi_odds_recyclerView.setVisibility(View.VISIBLE);
                    mCpiframen.public_date_layout.setVisibility(View.VISIBLE);
                    mCpiframen.public_img_company.setVisibility(View.VISIBLE);
                    break;
                case STARTLOADING://正在加载的时候
                    cpi_fl_plate_networkError.setVisibility(View.GONE);
                    cpi_fl_plate_noData.setVisibility(View.GONE);
                    cpi_odds_recyclerView.setVisibility(View.GONE);
                    cpi_fl_plate_loading.setVisibility(View.VISIBLE);
                    mCpiframen.mRefreshLayout.setRefreshing(true);
                    mCpiframen.public_date_layout.setVisibility(View.GONE);
                    mCpiframen.public_img_company.setVisibility(View.GONE);
                    mCpiframen.public_img_filter.setVisibility(View.GONE);
                    break;
                case ERROR://访问失败
                    cpi_fl_plate_noData.setVisibility(View.GONE);
                    cpi_odds_recyclerView.setVisibility(View.GONE);
                    cpi_fl_plate_loading.setVisibility(View.GONE);
                    mCpiframen.mRefreshLayout.setRefreshing(false);
                    cpi_fl_plate_networkError.setVisibility(View.VISIBLE);
                    mCpiframen.public_date_layout.setVisibility(View.GONE);
                    mCpiframen.public_img_company.setVisibility(View.GONE);
                    mCpiframen.public_img_filter.setVisibility(View.GONE);
                    break;
                case NODATA://没有数据
                    cpi_odds_recyclerView.setVisibility(View.GONE);
                    cpi_fl_plate_loading.setVisibility(View.GONE);
                    mCpiframen.mRefreshLayout.setRefreshing(false);
                    cpi_fl_plate_networkError.setVisibility(View.GONE);
                    cpi_fl_plate_noData.setVisibility(View.VISIBLE);
                    mCpiframen.public_date_layout.setVisibility(View.GONE);
                    mCpiframen.public_img_company.setVisibility(View.GONE);
                    mCpiframen.public_img_filter.setVisibility(View.GONE);
                    break;
                case NODATA_CHILD://内容没有数据
                    cpi_odds_recyclerView.setVisibility(View.GONE);
                    cpi_fl_plate_loading.setVisibility(View.GONE);
                    mCpiframen.mRefreshLayout.setRefreshing(false);
                    cpi_fl_plate_networkError.setVisibility(View.GONE);
                    cpi_fl_plate_noData.setVisibility(View.VISIBLE);
                    mCpiframen.public_date_layout.setVisibility(View.VISIBLE);
                    mCpiframen.public_img_company.setVisibility(View.VISIBLE);
                    mCpiframen.public_img_filter.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };
}
