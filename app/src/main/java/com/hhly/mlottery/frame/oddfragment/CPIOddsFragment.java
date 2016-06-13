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
import com.hhly.mlottery.bean.websocket.WebFootBallSocketOdds;
import com.hhly.mlottery.bean.websocket.WebFootBallSocketTime;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.CPIFragment;
import com.hhly.mlottery.util.StringUtils;
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
    public List<NewOddsInfo.AllInfoBean> mAllInfoBean1 = new ArrayList<>();
    public List<NewOddsInfo.AllInfoBean> mAllInfoBean2 = new ArrayList<>();
    public List<NewOddsInfo.AllInfoBean> mAllInfoBean3 = new ArrayList<>();
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
        switchd("", 0);
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
                switchd("", 0);
            }
        });

    }

    /**
     * 传日期和各盘口类型
     *
     * @param date
     * @param type
     */
    public void InitData(String date, final String type, final int requestType) {
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
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_NEW_ODDS, map, new VolleyContentFast.ResponseSuccessListener<NewOddsInfo>() {
            @Override
            public synchronized void onResponse(final NewOddsInfo json) {

                if (json != null && json.getCode() != 500 && !json.getCompany().isEmpty() && !json.getFileterTags().isEmpty() && !StringUtils.isEmpty(json.getCurrDate())) {

                    if (getParentFragment() != null) {
                        //如果不是日期或者刷新过来的
                        if (requestType != 1 && requestType != 2) {
                            //获取当前日期
                            mCpiframen.currentDate = json.getCurrDate().trim();
                            //设置日期
                            mCpiframen.public_txt_date.setText(mCpiframen.currentDate);
                            //推算前6天后七天
                            mCpiframen.mMapDayList = mCpiframen.getDate();
                            //默认选中当天
                            mCpiframen.selectPosition = 6;
                        }
                        mCpiframen.companys = json.getCompany();

                    }
                    mFileterTagsBean = json.getFileterTags();
                    if (type.equals(CPIFragment.TYPE_PLATE)) {
                        mAllInfoBean1 = json.getAllInfo();
                    } else if (type.equals(CPIFragment.TYPE_BIG)) {
                        mAllInfoBean2 = json.getAllInfo();
                    } else if (type.equals(CPIFragment.TYPE_OP)) {
                        mAllInfoBean3 = json.getAllInfo();
                    }
                    //如果是日期传过来的
                    if (requestType == 1) {
                        checkedIdsHot();
                        checkedCompanys(type);
                    }
                    //刷新
                    else if (requestType == 2) {
                        checkedCompanys(type);
                    }
                    //否则直接加载判断（第一次加载）
                    else {
                        checkedIdsHot();
                        for (NewOddsInfo.CompanyBean companyBean : json.getCompany()) {
                            if (Arrays.binarySearch(defualtCompanyIds, 0, defualtCompanyIds.length, companyBean.getComId()) >= 0) {
                                companyBean.setIsChecked(true);
                            } else {
                                companyBean.setIsChecked(false);
                            }
                        }
                        mCpiframen.filtrateDate();
                        if (!mCpiframen.companysName.isEmpty() && !CpiFiltrateActivity.mCheckedIds.isEmpty() && !"".equals(type)) {
                            selectCompany(mCpiframen.companysName, CpiFiltrateActivity.mCheckedIds, type);
                        } else if (CpiFiltrateActivity.mCheckedIds.isEmpty()) {
                            mHandler.sendEmptyMessage(NODATA_CHILD);
                        }

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
     * 请求类型
     *
     * @param dates
     */
    public void switchd(String dates, int requestType) {
        InitData(dates, mParam1, requestType);
    }

    /**
     * 筛选热门的id
     */
    private void checkedIdsHot() {
        CpiFiltrateActivity.mCheckedIds.clear();
        for (NewOddsInfo.FileterTagsBean fileterTagsBean : mFileterTagsBean) {
            if (fileterTagsBean.isHot()) {
                CpiFiltrateActivity.mCheckedIds.add(fileterTagsBean.getLeagueId());
            }
        }
    }


    /**
     * 查找当前选中的公司默认给选中，进行数据加载处理
     *
     * @param oddType 盘口类型
     */
    private void checkedCompanys(String oddType) {
        for (int h = 0; h < mCpiframen.companys.size(); h++) {
            if (mCpiframen.companysName.contains(mCpiframen.companys.get(h).getComName())) {
                mCpiframen.companys.get(h).setIsChecked(true);
            } else {
                mCpiframen.companys.get(h).setIsChecked(false);
            }
        }
        if (!mCpiframen.companysName.isEmpty() && !CpiFiltrateActivity.mCheckedIds.isEmpty() && !"".equals(oddType)) {
            selectCompany(mCpiframen.companysName, CpiFiltrateActivity.mCheckedIds, oddType);
        } else if (CpiFiltrateActivity.mCheckedIds.isEmpty()) {
            mHandler.sendEmptyMessage(NODATA_CHILD);
        }
    }

    /**
     * 传选中公司的集合
     *
     * @param comNameList 所选中联赛id的集合
     * @param mCheckedIds 公司类型
     * @param comPanyType
     */
    public void selectCompany(List<String> comNameList, List<String> mCheckedIds, String comPanyType) {
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
     *
     * @param hotsAllInfoTemps 选中公司的集合
     * @param comNameLists     所选中联赛id的集合
     * @param mCheckedIds      传选中公司的集合
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
        if (!mShowInfoBeans.isEmpty()) {
            if (cpiRecyclerViewAdapter != null) {
                cpiRecyclerViewAdapter.setAllInfoBean(mShowInfoBeans);
                cpiRecyclerViewAdapter.notifyDataSetChanged();
            } else {
                cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mShowInfoBeans, mContext, comPanyType);
                cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);

            }

            mHandler.sendEmptyMessage(SUCCESS);// 请求成功

        } else {
            mHandler.sendEmptyMessage(NODATA_CHILD);// 内容无数据
        }

    }

    /**
     * 时间和主客队比分
     *
     * @param webSocketTimeAndScore
     */
    public void upDateTimeAndScore(WebFootBallSocketTime webSocketTimeAndScore, String oddType) {
            for (int h = 0; h < mShowInfoBeans.size(); h++) {
                //如果赛事里面id有推送过来的id
                if (mShowInfoBeans.get(h).getMatchInfo().getMatchId().equals(webSocketTimeAndScore.getThirdId())) {
                    NewOddsInfo.AllInfoBean.MatchInfoBean matchInfoBean = mShowInfoBeans.get(h).getMatchInfo();
                    //如果是时间传过来的
                    if ("time".equals(oddType)) {
                        String statusOrigin = webSocketTimeAndScore.getData().get("statusOrigin");
                        int status= Integer.parseInt(statusOrigin);
                        String keepTime = webSocketTimeAndScore.getData().get("keepTime");
                        /*0:未开, 1:上半场, 2:中场, 3:下半场, 4:加时, 5:点球
                       -1:完场, -10:取消, -11:待定, -12:腰斩, -13:中断, -14:推迟, -100:隐藏. */
                        switch (status) {
                            case 0:
                                matchInfoBean.setIsShowTitle(false);
                                break;
                            case 1:
                                matchInfoBean.setIsShowTitle(true);
                                matchInfoBean.setKeepTime(keepTime);
                                break;
                            case 2:
                               if(Integer.parseInt(keepTime) > 45) {
                                   //如果还是上半场，但是大于45分钟了显示45+
                                   matchInfoBean.setKeepTime("45+");
                                }else{
                                   matchInfoBean.setKeepTime(keepTime);
                               }
                                matchInfoBean.setIsShowTitle(true);
                                break;
                            case 3:
                                matchInfoBean.setIsShowTitle(true);
                                break;
                            case 4:
                                matchInfoBean.setIsShowTitle(true);
                                break;
                            case 5:
                                matchInfoBean.setIsShowTitle(true);
                                break;
                            case -1:
                                break;
                            case -10:
                                break;
                            case -11:
                                break;
                            case -12:
                                break;
                            case -13:
                                break;
                            case -14:
                                break;
                            case -100:
                                break;
                            default:
                                break;
                        }
                    }else if("score".equals(oddType)){
                        //如果是主客队比分的
                        String matchResult = webSocketTimeAndScore.getData().get("matchResult");
                        if(!StringUtils.isEmpty(matchResult)) {
                            //1-5代表正常开赛，这个还需沟通
                            matchInfoBean.setMatchState("1");
                            matchInfoBean.setMatchResult(matchResult);
                        }
                    }
                    if (cpiRecyclerViewAdapter != null) {
                        System.out.println(">>mAllshuaxi时间刷新了");
                        cpiRecyclerViewAdapter.setAllInfoBean(mShowInfoBeans);
                        cpiRecyclerViewAdapter.notifyDataSetChanged();

                }
            }

        }
        //否者就是主客队比分
//        else {
//
//        }

    }

    /**
     * 赔率推送赔率
     */
    public void upDateOdds(WebFootBallSocketOdds webSKOdds, int oddType) {
        //如果是亚盘的
        if (oddType == 1) {
            filtrateSocket(webSKOdds, 1);
        }
        //如果是欧赔的
        else if (oddType == 2) {
            filtrateSocket(webSKOdds, 2);
        }
        //如果是大小的
        else if (oddType == 3) {
            filtrateSocket(webSKOdds, 3);
        }

    }

    /**
     * 根据推送过来的赔率更新单个item数据
     *
     * @param webSocketOdds
     */
    private void filtrateSocket(WebFootBallSocketOdds webSocketOdds, int typeNum) {
        for (int h = 0; h < mShowInfoBeans.size(); h++) {
            //如果赛事里面id有推送过来的id
            if (mShowInfoBeans.get(h).getMatchInfo().getMatchId().equals(webSocketOdds.getThirdId())) {
                //如果推送的数据不为空
                if (!webSocketOdds.getData().isEmpty()) {

                    for (int e = 0; e < webSocketOdds.getData().size(); e++) {

                        for (int j = 0; j < mShowInfoBeans.get(h).getComList().size(); j++) {

                            if (webSocketOdds.getData().get(e).get("comId").equals(mShowInfoBeans.get(h).getComList().get(j).getComId())) {
                                NewOddsInfo.AllInfoBean.ComListBean comListBean = mShowInfoBeans.get(h).getComList().get(j);
                                Map<String, String> mapDate = webSocketOdds.getData().get(e);
                                //如果即时主队数据不为空
                                if (!StringUtils.isEmpty(mapDate.get("leftOdds"))) {
                                    setOddType(comListBean, mapDate.get("leftOdds"), comListBean.getCurrLevel().getLeft(), "left");
                                    comListBean.getCurrLevel().setLeft(mapDate.get("leftOdds"));
                                }
                                //如果即时盘口数据不为空
                                if (!StringUtils.isEmpty(mapDate.get("mediumOdds"))) {
                                    if (typeNum == 2) {//如果是欧赔
                                        setOddType(comListBean, mapDate.get("mediumOdds"), comListBean.getCurrLevel().getMiddle(), "middle2");
                                    } else {//否则就是大小 和亚盘了
                                        setOddType(comListBean, mapDate.get("mediumOdds"), comListBean.getCurrLevel().getMiddle(), "middle13");
                                    }
                                    comListBean.getCurrLevel().setMiddle(mapDate.get("mediumOdds"));
                                }
                                //如果即时客队数据不为空
                                if (!StringUtils.isEmpty(mapDate.get("rightOdds"))) {
                                    setOddType(comListBean, mapDate.get("rightOdds"), comListBean.getCurrLevel().getRight(), "right");
                                    comListBean.getCurrLevel().setRight(mapDate.get("rightOdds"));
                                }
                                if (cpiRecyclerViewAdapter != null) {
                                    System.out.println(">>mAllshuaxi刷新了");
                                    cpiRecyclerViewAdapter.setAllInfoBean(mShowInfoBeans);
                                    cpiRecyclerViewAdapter.notifyDataSetChanged();

                                }
                            }
                        }

                    }

                }

            }
        }
    }

    /**
     * @param currentComBean
     * @param currentDate
     * @param nextDate
     * @param about          左中右 （主 盘 客）
     */
    private void setOddType(NewOddsInfo.AllInfoBean.ComListBean currentComBean, String currentDate, String nextDate, String about) {
        if (about.equals("right")) {
            if (Double.parseDouble(currentDate) > Double.parseDouble(nextDate)) {
                currentComBean.getCurrLevel().setRightUp(1);

            } else if (Double.parseDouble(currentDate) < Double.parseDouble(nextDate)) {
                currentComBean.getCurrLevel().setRightUp(-1);

            } else {
                currentComBean.getCurrLevel().setRightUp(0);
            }
        } else if (about.equals("left")) {
            if (Double.parseDouble(currentDate) > Double.parseDouble(nextDate)) {
                currentComBean.getCurrLevel().setLeftUp(1);

            } else if (Double.parseDouble(currentDate) < Double.parseDouble(nextDate)) {
                currentComBean.getCurrLevel().setLeftUp(-1);

            } else {
                currentComBean.getCurrLevel().setLeftUp(0);
            }
        } else if (about.equals("middle2")) {//欧赔的盘口
            if (Double.parseDouble(currentDate) > Double.parseDouble(nextDate)) {
                currentComBean.getCurrLevel().setMiddleUp(1);

            } else if (Double.parseDouble(currentDate) < Double.parseDouble(nextDate)) {
                currentComBean.getCurrLevel().setMiddleUp(-1);

            } else {
                currentComBean.getCurrLevel().setMiddleUp(0);
            }
        } else if (about.equals("middle13")) {//亚盘，大小球盘口
            if (Double.parseDouble(currentDate) > Double.parseDouble(nextDate)) {
                currentComBean.getCurrLevel().setCurrTextBgColor("red");
                System.out.println(">>>red 推送的" + currentDate + ">>>当前的" + nextDate + ">>>颜色" + currentComBean.getCurrLevel().getCurrTextBgColor());
            } else if (Double.parseDouble(currentDate) < Double.parseDouble(nextDate)) {
                currentComBean.getCurrLevel().setCurrTextBgColor("green");
                System.out.println(">>>green 推送的" + currentDate + ">>>当前的" + nextDate + ">>>颜色" + currentComBean.getCurrLevel().getCurrTextBgColor());
            } else {
                currentComBean.getCurrLevel().setCurrTextBgColor("black");
                System.out.println(">>>black 推送的" + currentDate + ">>>当前的" + nextDate + ">>>颜色" + currentComBean.getCurrLevel().getCurrTextBgColor());
            }
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
//                    mCpiframen.public_date_layout.setVisibility(View.GONE);
                    mCpiframen.public_img_company.setVisibility(View.GONE);
                    mCpiframen.public_img_filter.setVisibility(View.GONE);
                    break;
                case ERROR://访问失败
                    cpi_fl_plate_noData.setVisibility(View.GONE);
                    cpi_odds_recyclerView.setVisibility(View.GONE);
                    cpi_fl_plate_loading.setVisibility(View.GONE);
                    mCpiframen.mRefreshLayout.setRefreshing(false);
                    cpi_fl_plate_networkError.setVisibility(View.VISIBLE);
                    mCpiframen.isVisible = true;
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
//                    mCpiframen.public_date_layout.setVisibility(View.GONE);
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
