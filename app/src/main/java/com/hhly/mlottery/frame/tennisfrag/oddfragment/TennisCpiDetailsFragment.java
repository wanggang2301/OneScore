package com.hhly.mlottery.frame.tennisfrag.oddfragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.OddDetailsLeftAdapter;
import com.hhly.mlottery.adapter.cpiadapter.CpiDetailsAdatper;
import com.hhly.mlottery.adapter.tennisball.TennisCpiDetailsAdatper;
import com.hhly.mlottery.bean.oddsbean.OddsDetailsDataInfo;
import com.hhly.mlottery.bean.tennisball.datails.odds.TennisOddsDetailsDataInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网球内页亚盘，大小球，欧赔详情Fragment
 */
public class TennisCpiDetailsFragment extends Fragment {
    private static final String ARG_ODDTYPE = "oddType";
    private static final String ARG_THIRDID = "thirdId";
    private static final String ARG_INDEX = "index";
    private static final String ARG_LEFT_NAME = "leftName";
    private static final String ARG_COMPAN_NAME = "companName";

    private String oddType;
    private int index;
    private View mView;
    private ArrayList<String> leftNameList = new ArrayList<>();
    private Context mContext;
    //	标题名称id
    private TextView cpi_home_details_txt_id, cpi_dish_details_txt_id, cpi_guest_details_txt_id;
    private TennisOddDetailsLeftAdapter oddDetailsLeftAdapter;
    private ListView cpi_tails_left_listview;
    //赛事id
    private String mThirdId;
    //公司id
    private String companName;
    private PinnedHeaderExpandableListView cpi_odds_tetails_right_listview;
    //新版指数详情右边数据
    private TennisCpiDetailsAdatper mCpiDetailsAdatper;
    private FrameLayout cpi_right_fl_plate_noData, cpi_right_fl_plate_networkError, cpi_right_fl_plate_loading;
    private TextView cpi_txt_reLoading;
    private static final int ERROR = -1;//访问失败
    private static final int SUCCESS = 0;// 访问成功
    private static final int STARTLOADING = 1;// 数据加载中
    private static final int NODATA = 400;// 暂无数据
    private List<TennisOddsDetailsDataInfo.DataBean> groupListDetailsEntity = new ArrayList<>();

    public static TennisCpiDetailsFragment newInstance(String oddType, String thirdId, int index, ArrayList<String> nameList, String comName) {
        TennisCpiDetailsFragment fragment = new TennisCpiDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ODDTYPE, oddType);
        args.putString(ARG_THIRDID, thirdId);
        args.putInt(ARG_INDEX, index);
        args.putStringArrayList(ARG_LEFT_NAME, nameList);
        args.putString(ARG_COMPAN_NAME, comName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            oddType = getArguments().getString(ARG_ODDTYPE);
            mThirdId = getArguments().getString(ARG_THIRDID);
            index = getArguments().getInt(ARG_INDEX);
            leftNameList = getArguments().getStringArrayList(ARG_LEFT_NAME);
            companName = getArguments().getString(ARG_COMPAN_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_cpi_details_odds, container, false);//item_cpi_odds
        InitView();
        DetailsLeftData();
        return mView;
    }

    private void InitView() {
        cpi_right_fl_plate_noData = (FrameLayout) mView.findViewById(R.id.cpi_right_fl_plate_noData);
        cpi_right_fl_plate_networkError = (FrameLayout) mView.findViewById(R.id.cpi_right_fl_plate_networkError);
        cpi_right_fl_plate_loading = (FrameLayout) mView.findViewById(R.id.cpi_right_fl_plate_loading);
        cpi_txt_reLoading = (TextView) mView.findViewById(R.id.cpi_txt_reLoading);
        //详情
        cpi_home_details_txt_id = (TextView) mView.findViewById(R.id.cpi_home_details_txt_id);
        cpi_dish_details_txt_id = (TextView) mView.findViewById(R.id.cpi_dish_details_txt_id);
        cpi_guest_details_txt_id = (TextView) mView.findViewById(R.id.cpi_guest_details_txt_id);
        cpi_tails_left_listview = (ListView) mView.findViewById(R.id.cpi_tails_left_listview);
        cpi_odds_tetails_right_listview = (PinnedHeaderExpandableListView) mView.findViewById(R.id.cpi_odds_tetails_right_listview);
        cpi_odds_tetails_right_listview.setChildDivider(ContextCompat.getDrawable(mContext, R.color.line_football_footer));


        if ("3".equals(oddType)) {
            //大小球
            cpi_dish_details_txt_id.setVisibility(View.VISIBLE);
            cpi_home_details_txt_id.setText(R.string.foot_odds_asize_left);
            cpi_dish_details_txt_id.setText(R.string.foot_odds_asize_middle);
            cpi_guest_details_txt_id.setText(R.string.foot_odds_asize_right);
        } else if ("2".equals(oddType)) {
            //欧赔
            cpi_dish_details_txt_id.setVisibility(View.GONE);
            cpi_home_details_txt_id.setText(R.string.odd_home_op_txt);
//            cpi_dish_details_txt_id.setText("");
            cpi_guest_details_txt_id.setText(R.string.odd_guest_op_txt);
        } else if ("1".equals(oddType)) {
            //亚盘
            cpi_dish_details_txt_id.setVisibility(View.VISIBLE);
            cpi_home_details_txt_id.setText(R.string.odd_home_op_txt);
            cpi_dish_details_txt_id.setText(R.string.foot_odds_asize_middle);
            cpi_guest_details_txt_id.setText(R.string.odd_guest_op_txt);
        }
        // 访问失败，点击刷新
        cpi_txt_reLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 请求数据
                RightData(companName);
            }
        });
    }


    /**
     * 新版详情左边的数据
     */
    public void DetailsLeftData() {
        oddDetailsLeftAdapter = new TennisOddDetailsLeftAdapter(mContext, leftNameList);
        cpi_tails_left_listview.setAdapter(oddDetailsLeftAdapter);
        //根据传过去的postion更改选中的item选中背景
        oddDetailsLeftAdapter.setSelect(index);
        // 指数详情右边数据
        RightData(companName);

        // 详情左边list点击事件
        cpi_tails_left_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.d("tennis", "详情左边list点击事件:" + position);
                //根据传过去的postion更改选中的item选中背景
                oddDetailsLeftAdapter.setSelect(position);
                //根据position获取list集合里面id
                companName = leftNameList.get(position);
                if (mCpiDetailsAdatper != null) {
                    //切换指数详情右边listview之前刷新adapter
                    //清除父类和子类view的数据，刷新 mOddsDetailsAdapter
                    mCpiDetailsAdatper.clearData();
                    //指数详情右边数据
                    RightData(companName);
                }
            }
        });
    }


    private List<String> groupDataList = new ArrayList<>();//指数 博彩详情实体类（头部）
    private List<List<TennisOddsDetailsDataInfo.DataBean.DetailsBean>> childDetailsList = new ArrayList<>();//指数 博彩详情实体类

    //新版详情右边的数据
    public void RightData(String comName) {
        mHandler.sendEmptyMessage(STARTLOADING);
        Map<String, String> myPostParams = new HashMap<>();
        myPostParams.put("oddCompany", comName);
        myPostParams.put("oddType", oddType);
        myPostParams.put("matchIds", mThirdId);

        // 2、连接服务器
        VolleyContentFast.requestJsonByGet(BaseURLs.TENNIS_DATAILS_ODDS_INFO_URL, myPostParams, new VolleyContentFast.ResponseSuccessListener<TennisOddsDetailsDataInfo>() {
            @Override
            public synchronized void onResponse(final TennisOddsDetailsDataInfo json) {
                if (json != null && json.getData() != null) {
                    groupListDetailsEntity = json.getData();
                    if (groupListDetailsEntity != null) {
                        for (int i = 0; i < groupListDetailsEntity.size(); i++) {
                            //循环添加父view数据(日期)
                            groupDataList.add(groupListDetailsEntity.get(i).getDate());   //足球里面的全部日期  现在篮球没有
                            //添加子view数据(拿子类的DetailsEntity)
                            childDetailsList.add(groupListDetailsEntity.get(i).getDetails());
                            //i=0的时候拿到第一条设置一个标识”初盘“
                            if (i == 0) {
                                groupListDetailsEntity.get(i).getDetails().get(i).setSelectTag("tag");
                            }
                            // //倒序，排列子view的数据
                            Collections.reverse(groupListDetailsEntity.get(i).getDetails());
                        }

                        //倒序，排列父view
                        Collections.reverse(groupDataList);

                        //倒序，排列子view
                        Collections.reverse(childDetailsList);
                        //判断主队的数据
                        for (int i = 0; i < childDetailsList.size(); i++) {
                            for (int j = 0; j < childDetailsList.get(i).size(); j++) {
                                if (j == childDetailsList.get(i).size() - 1) {//一天里面的最后一个或者一天里面只有一个的那一个
                                    if ((i + 1) == childDetailsList.size()) {//如果第一天只有一个则不设置颜色
                                        continue;
                                    }
                                    TennisOddsDetailsDataInfo.DataBean.DetailsBean currentModel = childDetailsList.get(i).get(j);
                                    TennisOddsDetailsDataInfo.DataBean.DetailsBean nextModel = childDetailsList.get(i + 1).get(0);
                                    //设置主队
                                    setOddDetailColor(currentModel, currentModel.getHomeOdd(), nextModel.getHomeOdd(), 1);
                                    //设置客队
                                    setOddDetailColor(currentModel, currentModel.getGuestOdd(), nextModel.getGuestOdd(), 2);
                                    //盘口
                                    setOddDetailColor(currentModel, currentModel.getHand(), nextModel.getHand(), 3);

                                } else {
                                    TennisOddsDetailsDataInfo.DataBean.DetailsBean currentModel = childDetailsList.get(i).get(j);
                                    TennisOddsDetailsDataInfo.DataBean.DetailsBean nextModel = childDetailsList.get(i).get(j + 1);

                                    setOddDetailColor(currentModel, currentModel.getHomeOdd(), nextModel.getHomeOdd(), 1);
                                    setOddDetailColor(currentModel, currentModel.getGuestOdd(), nextModel.getGuestOdd(), 2);
                                    setOddDetailColor(currentModel, currentModel.getHand(), nextModel.getHand(), 3);
                                }
                            }
                        }
                        if ("1".equals(oddType)) {
                            //亚盘
                            mCpiDetailsAdatper = new TennisCpiDetailsAdatper(childDetailsList, groupDataList, mContext, cpi_odds_tetails_right_listview, "one", groupListDetailsEntity);
                        } else if ("3".equals(oddType)) {
                            //大小球
                            mCpiDetailsAdatper = new TennisCpiDetailsAdatper(childDetailsList, groupDataList, mContext, cpi_odds_tetails_right_listview, "two", groupListDetailsEntity);
                        } else if ("2".equals(oddType)) {
                            //欧赔
                            mCpiDetailsAdatper = new TennisCpiDetailsAdatper(childDetailsList, groupDataList, mContext, cpi_odds_tetails_right_listview, "three", groupListDetailsEntity);
                        }
                        cpi_odds_tetails_right_listview.setAdapter(mCpiDetailsAdatper);
                        for (int i = 0; i < groupListDetailsEntity.size(); i++) {
                            //默认打开全部的父类view
                            cpi_odds_tetails_right_listview.expandGroup(i);
                        }
                        if (childDetailsList.size() == 0) {
                            mHandler.sendEmptyMessage(NODATA);
                        } else {
                            mHandler.sendEmptyMessage(SUCCESS);
                        }
                    } else {
                        mHandler.sendEmptyMessage(NODATA);
                    }

                } else {
                    mHandler.sendEmptyMessage(NODATA);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERROR);
            }
        }, TennisOddsDetailsDataInfo.class);
    }

    private void setOddDetailColor(TennisOddsDetailsDataInfo.DataBean.DetailsBean currentDetailsEntity, double currentOdd, double nextOdd, int type) {
        if (currentOdd < nextOdd) {
            setOddDetailColorByType(currentDetailsEntity, type, "green");
        } else if (currentOdd > nextOdd) {
            setOddDetailColorByType(currentDetailsEntity, type, "red");
        } else if (currentOdd == nextOdd) {
            setOddDetailColorByType(currentDetailsEntity, type, "black");
        }
    }

    private void setOddDetailColorByType(TennisOddsDetailsDataInfo.DataBean.DetailsBean currentDetailsEntity, int type, String color) {
        if (type == 1) {//主队对比后的颜色
            currentDetailsEntity.setHomeColor(color);
        } else if (type == 2) {
            currentDetailsEntity.setGuestColor(color);
        } else { //盘口
            currentDetailsEntity.setDishColor(color);
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:// 访问成功
                    cpi_right_fl_plate_noData.setVisibility(View.GONE);
                    cpi_right_fl_plate_networkError.setVisibility(View.GONE);
                    cpi_right_fl_plate_loading.setVisibility(View.GONE);
                    cpi_odds_tetails_right_listview.setVisibility(View.VISIBLE);
                    break;
                case STARTLOADING://正在加载的时候
                    cpi_right_fl_plate_noData.setVisibility(View.GONE);
                    cpi_right_fl_plate_networkError.setVisibility(View.GONE);
                    cpi_right_fl_plate_loading.setVisibility(View.VISIBLE);
                    cpi_odds_tetails_right_listview.setVisibility(View.GONE);
                    break;
                case ERROR://访问失败
                    cpi_right_fl_plate_noData.setVisibility(View.GONE);
                    cpi_right_fl_plate_networkError.setVisibility(View.VISIBLE);
                    cpi_right_fl_plate_loading.setVisibility(View.GONE);
                    cpi_odds_tetails_right_listview.setVisibility(View.GONE);
                    break;
                case NODATA://没有数据
                    cpi_right_fl_plate_noData.setVisibility(View.VISIBLE);
                    cpi_right_fl_plate_networkError.setVisibility(View.GONE);
                    cpi_right_fl_plate_loading.setVisibility(View.GONE);
                    cpi_odds_tetails_right_listview.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };
}
