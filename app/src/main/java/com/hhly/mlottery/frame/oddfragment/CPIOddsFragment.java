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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.CpiFiltrateActivity;
import com.hhly.mlottery.adapter.OddDetailsLeftAdapter;
import com.hhly.mlottery.adapter.cpiadapter.CPIRecyclerViewAdapter;
import com.hhly.mlottery.bean.UpdateInfo;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.frame.CPIFragment;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    public static List<NewOddsInfo.AllInfoBean> mAllInfoBean = new ArrayList<>();
    public static List<NewOddsInfo.AllInfoBean> mAllInfoBean1 = new ArrayList<>();
    public static List<NewOddsInfo.AllInfoBean> mAllInfoBean2 = new ArrayList<>();
    private List<NewOddsInfo.AllInfoBean> mAllInfoBeans = new ArrayList<>();
    private List<NewOddsInfo.AllInfoBean> mShowInfoBeans = new ArrayList<>();
    //选着热门
    List<NewOddsInfo.AllInfoBean> hotsTemp = new ArrayList<>();
    //热门联赛筛选
    public static List<NewOddsInfo.FileterTagsBean> mFileterTagsBean = new ArrayList<>();
    //全部公司
    public static List<NewOddsInfo.CompanyBean> mCompanyBean = new ArrayList<>();
    private static final int ERROR = -1;//访问失败
    private static final int SUCCESS = 0;// 访问成功
    private static final int STARTLOADING = 1;// 数据加载中
    private static final int NODATA = 400;// 暂无数据
    private FrameLayout cpi_fl_plate_loading;// 正在加载中
    private FrameLayout cpi_fl_plate_networkError;// 加载失败
    private FrameLayout cpi_fl_plate_noData;// 暂无数据
    private TextView cpi_plate_reLoading;// 刷新
//    private String type = "";//类型区分，亚盘，大小，欧赔

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
        switchd("");
        return mView;
    }

    private void InitView() {
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
                switchd("");
            }
        });

    }

    /**
     * 传日期和各盘口类型
     *
     * @param date
     * @param type
     */
    public void InitData(String date, final String type) {
        mHandler.sendEmptyMessage(STARTLOADING);
        String stUrl = "http://192.168.10.242:8181/mlottery/core/footBallIndexCenter.findAndroidIndexCenter.do?";
        Map<String, String> map = new HashMap<>();

        if (type.equals(CPIFragment.TYPE_PLATE)) {
            map.put("type", "1");
        } else if (type.equals(CPIFragment.TYPE_BIG)) {
            map.put("type", "2");
        } else if (type.equals(CPIFragment.TYPE_OP)) {
            map.put("type", "3");
        }


        if (!"".equals(date)) {
            map.put("date", date);
        }

        // 2、连接服务器
        VolleyContentFast.requestJsonByPost(stUrl, map, new VolleyContentFast.ResponseSuccessListener<NewOddsInfo>() {
            @Override
            public synchronized void onResponse(final NewOddsInfo json) {

                if (json != null) {
                    if (getParentFragment() != null) {
                        ((CPIFragment) getParentFragment()).currentDate = json.getCurrDate();
                        ((CPIFragment) getParentFragment()).companys = json.getCompany();
                    }
                    mAllInfoBeans = json.getAllInfo();
                    List<NewOddsInfo.CompanyBean> defualtCompanyList = new ArrayList<>();

                    for (NewOddsInfo.CompanyBean companyBean : json.getCompany()) {
                        if (Arrays.binarySearch(defualtCompanyIds, 0, defualtCompanyIds.length, companyBean.getComId()) >= 0) {
                            defualtCompanyList.add(companyBean);
                        }
                    }

                    mFileterTagsBean = json.getFileterTags();


                    CpiFiltrateActivity.mCheckedIds.clear();
                    for (NewOddsInfo.FileterTagsBean fileterTagsBean : mFileterTagsBean) {
                        if (fileterTagsBean.isHot()) {
                            CpiFiltrateActivity.mCheckedIds.add(fileterTagsBean.getLeagueId());
                        }
                    }


                    filtrateData(CPIFragment.isHot, defualtCompanyList, CpiFiltrateActivity.mCheckedIds);


                    cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mShowInfoBeans, mContext, type);
                    cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);

//                    }
                    mHandler.sendEmptyMessage(SUCCESS);// 请求成功
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
     * 是否为热门
     */
    public void selectedHot(boolean isHot, String comp) {
        if (!isHot) {
            if ("plate".equals(comp)) {
                if (CPIFragment.comNameList.size() == 0) {
                    System.out.println(">>>22mAllInfoBean" + mAllInfoBean);
                    selectCompany(mAllInfoBean);
                    cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, "plate");
                    cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                } else {
                    System.out.println(">>>mAllInfoBean" + mAllInfoBean + "ggggggggg" + CPIFragment.comNameList);
                    selectCompany2(mAllInfoBean, CPIFragment.comNameList, "plate");
                }

            } else if ("big".equals(comp)) {
                if (CPIFragment.comNameList2.size() == 0) {
                    selectCompany(mAllInfoBean1);
                    cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, "big");
                    cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                } else {
                    selectCompany2(mAllInfoBean1, CPIFragment.comNameList2, "big");
                }
            } else if ("op".equals(comp)) {
                if (CPIFragment.comNameList3.size() == 0) {
                    selectCompany(mAllInfoBean2);
                    cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, "op");
                    cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                } else {
                    selectCompany2(mAllInfoBean2, CPIFragment.comNameList3, "op");
                }
            }


        } else {
            //直接加载热门
            if ("plate".equals(comp)) {
                if (CPIFragment.comNameList.size() == 0) {
                    for (int i = 0; i < mAllInfoBean.size(); i++) {
                        if (mAllInfoBean.get(i).isHot()) {
                            //筛选热门
                            hotsTemp.add(mAllInfoBean.get(i));
                        }
                    }
                    selectCompany(hotsTemp);
                    cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, "plate");
                    cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                } else {
                    selectCompany2(CPIFragment.hotsTemp1, CPIFragment.comNameList, "plate");
                }
            } else if ("big".equals(comp)) {
                if (CPIFragment.comNameList2.size() == 0) {
                    for (int i = 0; i < mAllInfoBean1.size(); i++) {
                        if (mAllInfoBean1.get(i).isHot()) {
                            //筛选热门
                            hotsTemp.add(mAllInfoBean1.get(i));
                        }
                    }
                    selectCompany(hotsTemp);
                    cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, "big");
                    cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                } else {
                    selectCompany2(CPIFragment.hotsTemp2, CPIFragment.comNameList2, "big");
                }
            } else if ("op".equals(comp)) {
                if (CPIFragment.comNameList3.size() == 0) {
                    for (int i = 0; i < mAllInfoBean2.size(); i++) {
                        if (mAllInfoBean2.get(i).isHot()) {
                            //筛选热门
                            hotsTemp.add(mAllInfoBean2.get(i));
                        }
                    }
                    selectCompany(hotsTemp);
                    cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, "op");
                    cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                } else {
                    selectCompany2(CPIFragment.hotsTemp3, CPIFragment.comNameList3, "op");
                }
            }

        }
    }


    public void setLeagueIdChecked(List<NewOddsInfo.AllInfoBean> LeagueIdChecked, String comPany) {
        if ("plate".equals(comPany)) {
            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(LeagueIdChecked, mContext, "plate");
            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
        } else if ("big".equals(comPany)) {
            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(LeagueIdChecked, mContext, "big");
            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
        } else if ("op".equals(comPany)) {
            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(LeagueIdChecked, mContext, "op");
            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
        }

    }

    /**
     * 时间
     *
     * @param dates
     */
    public void switchd(String dates) {
        InitData(dates, mParam1);
    }


    public void filtrateData(boolean isHot, List<NewOddsInfo.CompanyBean> comNameList, List<String> checkedIdExtra) {
        mShowInfoBeans.clear();
        mShowInfoBeans.addAll(mAllInfoBeans);
        List<NewOddsInfo.AllInfoBean> hotList = filtrateHot(mShowInfoBeans, isHot);
        filtrateCompany(hotList, comNameList);
        filtrateLeague(hotList, checkedIdExtra);
    }

    private List<NewOddsInfo.AllInfoBean> filtrateHot(List<NewOddsInfo.AllInfoBean> allInfoBeans, boolean isHot) {
        if (!isHot) {
            return allInfoBeans;
        }
        List<NewOddsInfo.AllInfoBean> hotList = new ArrayList<>();
        for (NewOddsInfo.AllInfoBean bean : allInfoBeans) {
            if (bean.isHot()) {
                hotList.add(bean);
            }
        }
        return hotList;
    }

    private void filtrateCompany(List<NewOddsInfo.AllInfoBean> allInfoBeans, List<NewOddsInfo.CompanyBean> companyList) {
        for (NewOddsInfo.AllInfoBean bean : allInfoBeans) {
            for (NewOddsInfo.AllInfoBean.ComListBean company : bean.getComList()) {

                boolean isContainCompany = false;
                for (NewOddsInfo.CompanyBean companyBean : companyList) {
                    if (companyBean.getComId().equals(company.getComId())) {
                        isContainCompany = true;
                        break;
                    }
                }

                if (isContainCompany) {
                    company.setIsShow(true);
                } else {
                    company.setIsShow(false);
                }
            }
        }
    }

    private List<NewOddsInfo.AllInfoBean> filtrateLeague(List<NewOddsInfo.AllInfoBean> allInfoBeans, List<String> checkedIdExtra) {

        List<NewOddsInfo.AllInfoBean> filtrateList = new ArrayList<>();

        for (NewOddsInfo.AllInfoBean bean : allInfoBeans) {
            if (checkedIdExtra.contains(bean.getLeagueId())) {
                filtrateList.add(bean);
            }
        }

        return filtrateList;
    }


    /**
     * 筛选公司
     */
    public void selectCompany(List<NewOddsInfo.AllInfoBean> hotsAllInfoTemp) {
        mAllInfoBeans.clear();
        for (int k = 0; k < hotsAllInfoTemp.size(); k++) {
            NewOddsInfo.AllInfoBean pAllInfoBean = new NewOddsInfo.AllInfoBean();
            List<NewOddsInfo.AllInfoBean.ComListBean> mComListBeanList = new ArrayList<>();
            for (int j = 0; j < hotsAllInfoTemp.get(k).getComList().size(); j++) {
                if (hotsAllInfoTemp.get(k).getComList().get(j).getComName().equals("皇冠") || hotsAllInfoTemp.get(k).getComList().get(j).getComName().equals("浩博")) {
                    NewOddsInfo.AllInfoBean.ComListBean mComListBean = new NewOddsInfo.AllInfoBean.ComListBean();
                    mComListBean = hotsAllInfoTemp.get(k).getComList().get(j);
                    mComListBeanList.add(mComListBean);
                }
            }
            pAllInfoBean.setComList(mComListBeanList);
            pAllInfoBean.setMatchInfo(hotsAllInfoTemp.get(k).getMatchInfo());
            pAllInfoBean.setHot(hotsAllInfoTemp.get(k).isHot());
            pAllInfoBean.setLeagueColor(hotsAllInfoTemp.get(k).getLeagueColor());
            pAllInfoBean.setLeagueId(hotsAllInfoTemp.get(k).getLeagueId());
            pAllInfoBean.setLeagueName(hotsAllInfoTemp.get(k).getLeagueName());
            mAllInfoBeans.add(pAllInfoBean);

        }
    }

    public void selectCompany2(List<NewOddsInfo.AllInfoBean> hotsAllInfoTemp, List<String> comNameList, String comPany) {
        if ("plate".equals(comPany)) {
            setComPany(hotsAllInfoTemp, comNameList);
            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, "plate");
            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
        } else if ("big".equals(comPany)) {
            setComPany(hotsAllInfoTemp, comNameList);
            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, "big");
            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
        } else if ("op".equals(comPany)) {
            setComPany(hotsAllInfoTemp, comNameList);
            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, "op");
            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
        }

    }

    private void setComPany(List<NewOddsInfo.AllInfoBean> hotsAllInfoTemps, List<String> comNameLists) {
        mAllInfoBeans.clear();
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
            pAllInfoBean.setComList(mComListBeanList);
            pAllInfoBean.setMatchInfo(hotsAllInfoTemps.get(k).getMatchInfo());
            pAllInfoBean.setHot(hotsAllInfoTemps.get(k).isHot());
            pAllInfoBean.setLeagueColor(hotsAllInfoTemps.get(k).getLeagueColor());
            pAllInfoBean.setLeagueId(hotsAllInfoTemps.get(k).getLeagueId());
            pAllInfoBean.setLeagueName(hotsAllInfoTemps.get(k).getLeagueName());
            mAllInfoBeans.add(pAllInfoBean);

        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:// 访问成功
                    CPIFragment.public_img_filter.setVisibility(View.VISIBLE);
                    cpi_fl_plate_loading.setVisibility(View.GONE);
                    cpi_fl_plate_networkError.setVisibility(View.GONE);
                    cpi_fl_plate_noData.setVisibility(View.GONE);
                    cpi_odds_recyclerView.setVisibility(View.VISIBLE);
                    break;
                case STARTLOADING://正在加载的时候
                    cpi_fl_plate_networkError.setVisibility(View.GONE);
                    cpi_fl_plate_noData.setVisibility(View.GONE);
                    cpi_odds_recyclerView.setVisibility(View.GONE);
                    cpi_fl_plate_loading.setVisibility(View.VISIBLE);
                    break;
                case ERROR://访问失败
                    cpi_fl_plate_noData.setVisibility(View.GONE);
                    cpi_odds_recyclerView.setVisibility(View.GONE);
                    cpi_fl_plate_loading.setVisibility(View.GONE);
                    cpi_fl_plate_networkError.setVisibility(View.VISIBLE);
                    break;
                case NODATA://没有数据
                    cpi_odds_recyclerView.setVisibility(View.GONE);
                    cpi_fl_plate_loading.setVisibility(View.GONE);
                    cpi_fl_plate_networkError.setVisibility(View.GONE);
                    cpi_fl_plate_noData.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };
}
