package com.hhly.mlottery.frame.oddfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.OddDetailsLeftAdapter;
import com.hhly.mlottery.adapter.cpiadapter.CPIRecyclerViewAdapter;
import com.hhly.mlottery.bean.UpdateInfo;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.frame.CPIFragment;
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
    private List<NewOddsInfo.AllInfoBean> mAllInfoBean;
    private List<NewOddsInfo.AllInfoBean> mAllInfoBeans = new ArrayList<>();


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
        switchd("", "");
        return mView;
    }

    private void InitView() {
        linearLayoutManager = new LinearLayoutManager(mContext);
        cpi_odds_recyclerView = (RecyclerView) mView.findViewById(R.id.cpi_odds_recyclerView);
        cpi_odds_recyclerView.setHasFixedSize(true);
        cpi_odds_recyclerView.setLayoutManager(linearLayoutManager);

    }

    public void InitData(String date, final int cpiNumber, final String company) {
        //http://192.168.10.242:8989/mlottery/core/footBallIndexCenter.findIosIndexCenter.do?date=2016-05-04&lang=zh&type=1
        String stUrl = "http://192.168.10.242:8181/mlottery/core/footBallIndexCenter.findIosIndexCenter.do?";
        Map<String, String> map = new HashMap<>();
//        System.out.println(">>>date+" + date);

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
                    if (mAllInfoBean != null && mAllInfoBean.size() > 0) {
                        if (cpiNumber == 0) {
                            //如果公司不等于空
                            if (!"".equals(company)) {
                                mAllInfoBeans.clear();
                                for (int i = 0; i < mAllInfoBean.size(); i++) {
                                    NewOddsInfo.AllInfoBean pAllInfoBean = new NewOddsInfo.AllInfoBean();
                                    List<NewOddsInfo.AllInfoBean.ComListBean> mComListBeanList = new ArrayList<>();
                                    for (int j = 0; j < mAllInfoBean.get(i).getComList().size(); j++) {
                                        System.out.println(">>>companyBeans+" + company);
                                        if (mAllInfoBean.get(i).getComList().get(j).getComName().equals(company)) {
                                            NewOddsInfo.AllInfoBean.ComListBean mComListBean = new NewOddsInfo.AllInfoBean.ComListBean();
                                            mComListBean = mAllInfoBean.get(i).getComList().get(j);
                                            mComListBeanList.add(mComListBean);
                                        }

                                    }
                                    pAllInfoBean.setComList(mComListBeanList);
                                    pAllInfoBean.setMatchInfo(mAllInfoBean.get(i).getMatchInfo());
                                    pAllInfoBean.setHot(mAllInfoBean.get(i).isHot());
                                    pAllInfoBean.setLeagueColor(mAllInfoBean.get(i).getLeagueColor());
                                    pAllInfoBean.setLeagueId(mAllInfoBean.get(i).getLeagueId());
                                    pAllInfoBean.setLeagueName(mAllInfoBean.get(i).getLeagueName());
                                    mAllInfoBeans.add(pAllInfoBean);

                                }

                                cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, "plate");
                                cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                            } else {
                                //公司为空直接加载全部
                                cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBean, mContext, "plate");
                                cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                            }
                        } else if (cpiNumber == 1) {
                            //如果公司不等于空
                            if (!"".equals(company)) {
                                mAllInfoBeans.clear();
                                for (int i = 0; i < mAllInfoBean.size(); i++) {
                                    NewOddsInfo.AllInfoBean pAllInfoBean = new NewOddsInfo.AllInfoBean();
                                    List<NewOddsInfo.AllInfoBean.ComListBean> mComListBeanList = new ArrayList<>();
                                    for (int j = 0; j < mAllInfoBean.get(i).getComList().size(); j++) {
                                        if (mAllInfoBean.get(i).getComList().get(j).getComName().equals(company)) {
                                            NewOddsInfo.AllInfoBean.ComListBean mComListBean = new NewOddsInfo.AllInfoBean.ComListBean();
                                            mComListBean = mAllInfoBean.get(i).getComList().get(j);
                                            mComListBeanList.add(mComListBean);
                                        }

                                    }
                                    pAllInfoBean.setComList(mComListBeanList);
                                    pAllInfoBean.setMatchInfo(mAllInfoBean.get(i).getMatchInfo());
                                    pAllInfoBean.setHot(mAllInfoBean.get(i).isHot());
                                    pAllInfoBean.setLeagueColor(mAllInfoBean.get(i).getLeagueColor());
                                    pAllInfoBean.setLeagueId(mAllInfoBean.get(i).getLeagueId());
                                    pAllInfoBean.setLeagueName(mAllInfoBean.get(i).getLeagueName());
                                    mAllInfoBeans.add(pAllInfoBean);

                                }
                                cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, "big");
                                cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                            } else {
                                //公司为空直接加载全部
                                cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBean, mContext, "big");
                                cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                            }

                        } else if (cpiNumber == 2) {
                            //如果公司不等于空
                            if (!"".equals(company)) {
                                mAllInfoBeans.clear();
                                for (int i = 0; i < mAllInfoBean.size(); i++) {
                                    NewOddsInfo.AllInfoBean pAllInfoBean = new NewOddsInfo.AllInfoBean();
                                    List<NewOddsInfo.AllInfoBean.ComListBean> mComListBeanList = new ArrayList<>();
                                    for (int j = 0; j < mAllInfoBean.get(i).getComList().size(); j++) {
                                        if (mAllInfoBean.get(i).getComList().get(j).getComName().equals(company)) {
                                            NewOddsInfo.AllInfoBean.ComListBean mComListBean = new NewOddsInfo.AllInfoBean.ComListBean();
                                            mComListBean = mAllInfoBean.get(i).getComList().get(j);
                                            mComListBeanList.add(mComListBean);
                                        }

                                    }
                                    pAllInfoBean.setComList(mComListBeanList);
                                    pAllInfoBean.setMatchInfo(mAllInfoBean.get(i).getMatchInfo());
                                    pAllInfoBean.setHot(mAllInfoBean.get(i).isHot());
                                    pAllInfoBean.setLeagueColor(mAllInfoBean.get(i).getLeagueColor());
                                    pAllInfoBean.setLeagueId(mAllInfoBean.get(i).getLeagueId());
                                    pAllInfoBean.setLeagueName(mAllInfoBean.get(i).getLeagueName());
                                    mAllInfoBeans.add(pAllInfoBean);

                                }
                                cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, "op");
                                cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                            } else {
                                //公司为空直接加载全部
                                cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBean, mContext, "op");
                                cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                            }


                        }

                    }

                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, NewOddsInfo.class);
    }

    /**
     * 时间，公司，类别
     *
     * @param dates
     * @param companyBeans
     */
    public void switchd(String dates, String companyBeans) {

        switch (mParam1) {
            case "plate":
                //时间和公司为空代表第一次
                if ("".equals(dates) && "".equals(companyBeans)) {
                    //亚盘
                    InitData(UiUtils.getDay(0), 0, "");

                }
                //如果时间不为空，公司为空，传时间，公司传空
                else if (!"".equals(dates) && "".equals(companyBeans)) {
                    InitData(dates.trim(), 0, "");

                }
                //如果时间为空，公司不为空，传时间，公司传空
                else if (!"".equals(dates) && !"".equals(companyBeans)) {
                    InitData(dates.trim(), 0, companyBeans);
                }
                //如果时间为空，公司不为空，传当前时间，传公司
                else if ("".equals(dates) && !"".equals(companyBeans)) {
                    InitData(UiUtils.getDay(0), 0, companyBeans);
                }
                break;
            case "big":
                //大小
                //时间和公司为空代表第一次
                if ("".equals(dates) && "".equals(companyBeans)) {
                    //亚盘
                    InitData(UiUtils.getDay(0), 1, "");

                }
                //如果时间不为空，公司为空，传时间，公司传空
                else if (!"".equals(dates) && "".equals(companyBeans)) {
                    InitData(dates.trim(), 1, "");

                }
                //如果时间为空，公司不为空，传时间，公司传空
                else if (!"".equals(dates) && !"".equals(companyBeans)) {
                    InitData(dates.trim(), 1, companyBeans);
                }
                //如果时间为空，公司不为空，传当前时间，传公司
                else if ("".equals(dates) && !"".equals(companyBeans)) {
                    InitData(UiUtils.getDay(0), 1, companyBeans);
                }
                break;
            case "op":
                //欧赔
                //时间和公司为空代表第一次
                if ("".equals(dates) && "".equals(companyBeans)) {
                    //亚盘
                    InitData(UiUtils.getDay(0), 2, "");

                }
                //如果时间不为空，公司为空，传时间，公司传空
                else if (!"".equals(dates) && "".equals(companyBeans)) {
                    InitData(dates.trim(), 2, "");

                }
                //如果时间不为空，公司不为空，传时间，公司传空
                else if (!"".equals(dates) && !"".equals(companyBeans)) {
                    InitData(dates.trim(), 2, companyBeans);
                }
                //如果时间为空，公司不为空，传当前时间，传公司
                else if ("".equals(dates) && !"".equals(companyBeans)) {
                    InitData(UiUtils.getDay(0), 2, companyBeans);
                }

                break;
            default:
                break;


        }
    }
}
