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
    private List<NewOddsInfo.AllInfoBean> mAllInfoBean = new ArrayList<>();
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
        switchd("", "", false);
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
                switchd("", "", false);
            }
        });

    }

    public void InitData(String date, final int cpiNumber, final String company, final boolean isHot) {
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
                        String type = "";
                        if (cpiNumber == 0) {
                            //如果公司不等于空
                            type = "plate";
                        } else if (cpiNumber == 1) {
                            type = "big";
                        } else if (cpiNumber == 2) {
                            type = "op";
                        }

                        if (!"".equals(company)) {
                            selectCompany(company);
                            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBeans, mContext, type);
                            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                        }
                        //如果选择的是热门
                        else if (isHot) {
                            for (int i = 0; i < mAllInfoBean.size(); i++) {
                                if (mAllInfoBean.get(i).isHot()) {
                                    hotsTemp.add(mAllInfoBean.get(i));
                                }
                            }
                            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(hotsTemp, mContext, type);
                            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);

                        }   //如果不选择热门
                        else if (!isHot) {
                            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBean, mContext, type);
                            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);

                        } else {
                            //直接加载热门
                            for (int i = 0; i < mAllInfoBean.size(); i++) {
                                if (mAllInfoBean.get(i).isHot()) {
                                    hotsTemp.add(mAllInfoBean.get(i));
                                }
                            }
                            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(hotsTemp, mContext, "plate");
                            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                        }

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
     * 时间，公司，类别,是否为热门
     *
     * @param dates
     * @param companyBeans
     */
    public void switchd(String dates, String companyBeans, boolean isSelect) {

        switch (mParam1) {
            case "plate":
                //时间和公司为空代表第一次
                if ("".equals(dates) && "".equals(companyBeans) && isSelect == false) {
                    //亚盘
                    InitData(UiUtils.getDay(0), 0, "", false);

                } else if ("".equals(dates) && "".equals(companyBeans) && isSelect == true) {
                    //亚盘
                    InitData(UiUtils.getDay(0), 0, "", true);
                }
                //如果时间不为空，公司为空，传时间，公司传空
                else if (!"".equals(dates) && "".equals(companyBeans) && isSelect == false) {
                    InitData(dates.trim(), 0, "", false);

                } else if (!"".equals(dates) && "".equals(companyBeans) && isSelect == true) {
                    InitData(dates.trim(), 0, "", true);

                }
                //如果时间为空，公司不为空，传时间，公司传空
                else if (!"".equals(dates) && !"".equals(companyBeans) && isSelect == false) {
                    InitData(dates.trim(), 0, companyBeans, false);
                } else if (!"".equals(dates) && !"".equals(companyBeans) && isSelect == true) {
                    InitData(dates.trim(), 0, companyBeans, true);
                }
                //如果时间为空，公司不为空，传当前时间，传公司
                else if ("".equals(dates) && !"".equals(companyBeans) && isSelect == false) {
                    InitData(UiUtils.getDay(0), 0, companyBeans, false);
                } else if ("".equals(dates) && !"".equals(companyBeans) && isSelect == true) {
                    InitData(UiUtils.getDay(0), 0, companyBeans, true);
                }
                break;
            case "big":
                //大小
                //时间和公司为空代表第一次
                if ("".equals(dates) && "".equals(companyBeans) && isSelect == false) {
                    //亚盘
                    InitData(UiUtils.getDay(0), 1, "", false);

                } else if ("".equals(dates) && "".equals(companyBeans) && isSelect == true) {
                    //亚盘
                    InitData(UiUtils.getDay(0), 1, "", true);
                }
                //如果时间不为空，公司为空，传时间，公司传空
                else if (!"".equals(dates) && "".equals(companyBeans) && isSelect == false) {
                    InitData(dates.trim(), 1, "", false);

                } else if (!"".equals(dates) && "".equals(companyBeans) && isSelect == true) {
                    InitData(dates.trim(), 1, "", true);

                }
                //如果时间为空，公司不为空，传时间，公司传空
                else if (!"".equals(dates) && !"".equals(companyBeans) && isSelect == false) {
                    InitData(dates.trim(), 1, companyBeans, false);
                } else if (!"".equals(dates) && !"".equals(companyBeans) && isSelect == true) {
                    InitData(dates.trim(), 1, companyBeans, true);
                }
                //如果时间为空，公司不为空，传当前时间，传公司
                else if ("".equals(dates) && !"".equals(companyBeans) && isSelect == false) {
                    InitData(UiUtils.getDay(0), 1, companyBeans, false);
                } else if ("".equals(dates) && !"".equals(companyBeans) && isSelect == true) {
                    InitData(UiUtils.getDay(0), 1, companyBeans, true);
                }
                break;
            case "op":
                //欧赔
                //时间和公司为空代表第一次
                if ("".equals(dates) && "".equals(companyBeans) && isSelect == false) {
                    //亚盘
                    InitData(UiUtils.getDay(0), 2, "", false);

                } else if ("".equals(dates) && "".equals(companyBeans) && isSelect == true) {
                    //亚盘
                    InitData(UiUtils.getDay(0), 2, "", true);

                }
                //如果时间不为空，公司为空，传时间，公司传空
                else if (!"".equals(dates) && "".equals(companyBeans) && isSelect == false) {
                    InitData(dates.trim(), 2, "", false);

                } else if (!"".equals(dates) && "".equals(companyBeans) && isSelect == true) {
                    InitData(dates.trim(), 2, "", true);

                }
                //如果时间不为空，公司不为空，传时间，公司传空
                else if (!"".equals(dates) && !"".equals(companyBeans) && isSelect == false) {
                    InitData(dates.trim(), 2, companyBeans, false);
                } else if (!"".equals(dates) && !"".equals(companyBeans) && isSelect == true) {
                    InitData(dates.trim(), 2, companyBeans, true);
                }
                //如果时间为空，公司不为空，传当前时间，传公司
                else if ("".equals(dates) && !"".equals(companyBeans) && isSelect == false) {
                    InitData(UiUtils.getDay(0), 2, companyBeans, false);
                } else if ("".equals(dates) && !"".equals(companyBeans) && isSelect == true) {
                    InitData(UiUtils.getDay(0), 2, companyBeans, true);
                }

                break;
            default:
                break;


        }
    }

    /**
     * 选着筛选公司
     */
    private void selectCompany(String company) {
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
