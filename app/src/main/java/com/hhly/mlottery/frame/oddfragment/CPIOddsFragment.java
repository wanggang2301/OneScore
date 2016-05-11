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
import com.hhly.mlottery.adapter.OddDetailsLeftAdapter;
import com.hhly.mlottery.adapter.cpiadapter.CPIRecyclerViewAdapter;
import com.hhly.mlottery.bean.UpdateInfo;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.frame.CPIFragment;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
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
    private String mParam1;
    private String mParam2;
    private View mView;
    private Context mContext;
    private RecyclerView cpi_odds_recyclerView;
    private CPIRecyclerViewAdapter cpiRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    public static List<NewOddsInfo.AllInfoBean> mAllInfoBean = new ArrayList<>();
    private List<NewOddsInfo.AllInfoBean> mAllInfoBeans = new ArrayList<>();
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
    private String type = "";//类型区分，亚盘，大小，欧赔

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
     * @param cpiNumber
     */
    public void InitData(String date, final int cpiNumber) {
        mHandler.sendEmptyMessage(STARTLOADING);
        String stUrl = "http://192.168.10.242:8181/mlottery/core/footBallIndexCenter.findAndroidIndexCenter.do?";
        Map<String, String> map = new HashMap<>();

        if (cpiNumber == 0) {
            //亚盘
            map.put("date", date);
            map.put("type", "1");
        } else if (cpiNumber == 1) {
            //大小
            map.put("date", date);
            map.put("type", "3");
        } else if (cpiNumber == 2) {
            //欧赔
            map.put("date", date);
            map.put("type", "2");
        }

        // 2、连接服务器
        VolleyContentFast.requestJsonByPost(stUrl, map, new VolleyContentFast.ResponseSuccessListener<NewOddsInfo>() {
            @Override
            public synchronized void onResponse(final NewOddsInfo json) {

                if (json != null) {
                    mAllInfoBean = json.getAllInfo();
                    mFileterTagsBean = json.getFileterTags();
                    mCompanyBean = json.getCompany();
                    if (mAllInfoBean != null && mAllInfoBean.size() > 0) {
                        if (cpiNumber == 0) {
                            type = "plate";
                        } else if (cpiNumber == 1) {
                            type = "big";
                        } else if (cpiNumber == 2) {
                            type = "op";
                        }
                        for (int i = 0; i < mAllInfoBean.size(); i++) {
                            if (mAllInfoBean.get(i).isHot()) {
                                //筛选热门
                                hotsTemp.add(mAllInfoBean.get(i));
                            }
                        }
                        //筛选公司
                        selectCompany(hotsTemp);
                        cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, type);
                        cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);

                    }
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
    public void selectedHot(boolean isHot) {
        if (!isHot) {
            selectCompany(mAllInfoBean);
            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, type);
            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);

        } else {
            //直接加载热门
            for (int i = 0; i < mAllInfoBean.size(); i++) {
                if (mAllInfoBean.get(i).isHot()) {
                    hotsTemp.add(mAllInfoBean.get(i));
                }
            }
            selectCompany(hotsTemp);
            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, type);
            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
        }
    }

    /**
     * 时间
     *
     * @param dates
     */
    public void switchd(String dates) {

        switch (mParam1) {
            case "plate":
                //时间为空
                if ("".equals(dates)) {
                    InitData(UiUtils.getDay(0), 0);

                } else {
                    InitData(dates.trim(), 0);
                }
                break;
            case "big":
                //大小
                if ("".equals(dates)) {
                    InitData(UiUtils.getDay(0), 1);

                } else {
                    InitData(dates.trim(), 1);
                }
                break;
            case "op":
                //欧赔
                if ("".equals(dates)) {
                    InitData(UiUtils.getDay(0), 2);

                } else {
                    InitData(dates.trim(), 2);
                }

                break;
            default:
                break;


        }
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
    public void selectCompany2(List<NewOddsInfo.AllInfoBean> hotsAllInfoTemp,List<String> comNameList) {
        mAllInfoBeans.clear();
        for (int k = 0; k < hotsAllInfoTemp.size(); k++) {
            NewOddsInfo.AllInfoBean pAllInfoBean = new NewOddsInfo.AllInfoBean();
            List<NewOddsInfo.AllInfoBean.ComListBean> mComListBeanList = new ArrayList<>();
            for (int j = 0; j < hotsAllInfoTemp.get(k).getComList().size(); j++) {

                for (int h=0;h<comNameList.size();h++){

                if (hotsAllInfoTemp.get(k).getComList().get(j).getComName().equals(comNameList.get(h))) {
                    NewOddsInfo.AllInfoBean.ComListBean mComListBean = new NewOddsInfo.AllInfoBean.ComListBean();
                    mComListBean = hotsAllInfoTemp.get(k).getComList().get(j);
                    mComListBeanList.add(mComListBean);
                }
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
        cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, type);
        cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
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
