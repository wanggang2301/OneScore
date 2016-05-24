package com.hhly.mlottery.frame.oddfragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.OddDetailsLeftAdapter;
import com.hhly.mlottery.adapter.cpiadapter.CpiDetailsAdatper;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.bean.oddsbean.OddsDetailsDataInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.PinnedHeaderExpandableListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 103TJL on 2016/4/6.
 * 新版 亚盘，大小球，欧赔详情
 */
public class CpiDetailsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_COMID = "mParamComId";
    private static final String ARG_POSITIONNUNBER = "mParamPositionNunber";

    private String mParam1, mParamComId, mParamPositionNunber;
    private List<Map<String, String>> mParam2List;
    private View mView;
    private Context mContext;
    //	标题名称id
    private TextView cpi_home_details_txt_id, cpi_dish_details_txt_id, cpi_guest_details_txt_id;
    private OddDetailsLeftAdapter oddDetailsLeftAdapter;
    private ListView cpi_tails_left_listview;
    //赛事id
    private String mThirdId;
    //公司id
    private String stCompanId;
    private PinnedHeaderExpandableListView  cpi_odds_tetails_right_listview;
    //新版指数详情右边数据
    private CpiDetailsAdatper mCpiDetailsAdatper;
    private FrameLayout cpi_right_fl_plate_noData,cpi_right_fl_plate_networkError,cpi_right_fl_plate_loading;
    private TextView cpi_txt_reLoading;
    private static final int ERROR = -1;//访问失败
    private static final int SUCCESS = 0;// 访问成功
    private static final int STARTLOADING = 1;// 数据加载中
    private static final int NODATA = 400;// 暂无数据

    public static CpiDetailsFragment newInstance(String param1, List listParam2, String mParamComId, String mParamPositionNunber, String mParamType) {
        CpiDetailsFragment fragment = new CpiDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, (Serializable) listParam2);
        args.putString(ARG_COMID, mParamComId);
        args.putString(ARG_POSITIONNUNBER, mParamPositionNunber);
//        args.putString(ARG_TYPE, mParamType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2List = (ArrayList) getArguments().getSerializable(ARG_PARAM2);
            mParamComId = getArguments().getString(ARG_COMID);
            mParamPositionNunber = getArguments().getString(ARG_POSITIONNUNBER);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_cpi_details_odds, container, false);//item_cpi_odds
        InitView();
        //获得赛事id
        mThirdId = mParam2List.get(0).get("thirdid");
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
        cpi_odds_tetails_right_listview.setChildDivider(getResources().getDrawable(R.color.line_football_footer));
        if ("3".equals(mParam1)) {
            //大小球
            cpi_home_details_txt_id.setText(R.string.odd_home_big_txt);
            cpi_dish_details_txt_id.setText(R.string.odd_dish_big_txt);
            cpi_guest_details_txt_id.setText(R.string.odd_guest_big_txt);
        } else if ("2".equals(mParam1)) {
            //欧赔
            cpi_home_details_txt_id.setText(R.string.odd_home_op_txt);
            cpi_dish_details_txt_id.setText(R.string.odd_dish_op_txt);
            cpi_guest_details_txt_id.setText(R.string.odd_guest_op_txt);
        }
        // 访问失败，点击刷新
        cpi_txt_reLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 请求数据
                RightData(stCompanId);
            }
        });
    }


    /**
     * 新版详情左边的数据
     */
    public void DetailsLeftData() {
        oddDetailsLeftAdapter = new OddDetailsLeftAdapter(mContext, mParam2List);
        cpi_tails_left_listview.setAdapter(oddDetailsLeftAdapter);
        //根据传过去的postion更改选中的item选中背景
        oddDetailsLeftAdapter.setDefSelect(Integer.parseInt(mParamPositionNunber));
        // 指数详情右边数据
        RightData(mParamComId);

        // 详情左边list点击事件
        cpi_tails_left_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //根据传过去的postion更改选中的item选中背景
                oddDetailsLeftAdapter.setDefSelect(position);
                //根据position获取list集合里面id
                stCompanId = mParam2List.get(position).get("id");
                if (mCpiDetailsAdatper != null) {
                    //切换指数详情右边listview之前刷新adapter
                    //清除父类和子类view的数据，刷新 mOddsDetailsAdapter
                    mCpiDetailsAdatper.clearData();
                    //指数详情右边数据
                    RightData(stCompanId);
                }


            }
        });
    }

    private List<String> groupDataList = new ArrayList<>();//指数 博彩详情实体类（头部）
    private List<List<OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity>> childDetailsList = new ArrayList<>();//指数 博彩详情实体类

    //新版详情右边的数据
    public void RightData(String idComId) {
        mHandler.sendEmptyMessage(STARTLOADING);
        Map<String, String> myPostParams = new HashMap<>();
        if ("1".equals(mParam1)) {
            //亚盘
            myPostParams.put("companyId", idComId);
            myPostParams.put("oddType", mParam1);
        } else if ("3".equals(mParam1)) {
            //大小
            myPostParams.put("companyId", idComId);
            myPostParams.put("oddType", mParam1);
        } else if ("2".equals(mParam1)) {
            //欧赔
            myPostParams.put("companyId", idComId);
            myPostParams.put("oddType", mParam1);
        }
        myPostParams.put("thirdId", mThirdId);

        // 2、连接服务器
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_MATCHODD_DETAILS,myPostParams, new VolleyContentFast.ResponseSuccessListener<OddsDetailsDataInfo>() {
            @Override
            public synchronized void onResponse(final OddsDetailsDataInfo json) {
                if (json != null) {
                    List<OddsDetailsDataInfo.DetailsEntity> groupListDetailsEntity = json.getDetails();

                    if (groupListDetailsEntity != null && groupListDetailsEntity.size() > 0) {

                        for (int i = 0; i < groupListDetailsEntity.size(); i++) {
                            //循环添加父view数据(日期)
                            groupDataList.add(groupListDetailsEntity.get(i).getDate());
                            //添加子view数据(拿子类的DetailsEntity)
                            childDetailsList.add(groupListDetailsEntity.get(i).getDetails());
                            // //倒序，排列子view的数据
                            Collections.reverse(groupListDetailsEntity.get(i).getDetails());
                        }
                        //倒序，排列父view
                        Collections.reverse(groupDataList);
//                            //倒序，排列子view
                        Collections.reverse(childDetailsList);
                        //判断主队的数据
                        for (int i = 0; i < childDetailsList.size(); i++) {
                            for (int j = 0; j < childDetailsList.get(i).size(); j++) {

                                if (j == childDetailsList.get(i).size() - 1) {//一天里面的最后一个或者一天里面只有一个的那一个

                                    if ((i + 1) == childDetailsList.size()) {//如果第一天只有一个则不设置颜色
                                        continue;
                                    }

                                    OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity currentModel = childDetailsList.get(i).get(j);
                                    OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity nextModel = childDetailsList.get(i + 1).get(0);

                                    //设置主队
                                    setOddDetailColor(currentModel, currentModel.getHomeOdd(), nextModel.getHomeOdd(), 1);
                                    //设置客队
                                    setOddDetailColor(currentModel, currentModel.getGuestOdd(), nextModel.getGuestOdd(), 2);
                                    //盘口
                                    setOddDetailColor(currentModel, currentModel.getHand(), nextModel.getHand(), 3);

                                } else {
                                    OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity currentModel = childDetailsList.get(i).get(j);
                                    OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity nextModel = childDetailsList.get(i).get(j + 1);

                                    setOddDetailColor(currentModel, currentModel.getHomeOdd(), nextModel.getHomeOdd(), 1);
                                    setOddDetailColor(currentModel, currentModel.getGuestOdd(), nextModel.getGuestOdd(), 2);
                                    setOddDetailColor(currentModel, currentModel.getHand(), nextModel.getHand(), 3);
                                }
                            }
                        }
                        if ("1".equals(mParam1)) {
                            //亚盘
                            mCpiDetailsAdatper = new CpiDetailsAdatper(childDetailsList, groupDataList, mContext, cpi_odds_tetails_right_listview, "one");
                        } else if ("3".equals(mParam1)) {
                            //大小球
                            mCpiDetailsAdatper = new CpiDetailsAdatper(childDetailsList, groupDataList, mContext, cpi_odds_tetails_right_listview, "two");
                        } else if ("2".equals(mParam1)) {
                            //欧赔
                            mCpiDetailsAdatper = new CpiDetailsAdatper(childDetailsList, groupDataList, mContext, cpi_odds_tetails_right_listview, "three");
                        }
                        cpi_odds_tetails_right_listview.setAdapter(mCpiDetailsAdatper);
                        for (int i = 0; i <  groupListDetailsEntity.size(); i++) {
                            //默认打开全部的父类view
                            cpi_odds_tetails_right_listview.expandGroup(i);
                        }
                    }
                    mHandler.sendEmptyMessage(SUCCESS);
                }else{
                    mHandler.sendEmptyMessage(NODATA);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERROR);
            }
        }, OddsDetailsDataInfo.class);
    }

    private void setOddDetailColor(OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity currentDetailsEntity, double currentOdd, double nextOdd, int type) {
        if (currentOdd < nextOdd) {
            setOddDetailColorByType(currentDetailsEntity, type, "green");
        } else if (currentOdd > nextOdd) {
            setOddDetailColorByType(currentDetailsEntity, type, "red");
        } else if (currentOdd == nextOdd) {
            setOddDetailColorByType(currentDetailsEntity, type, "black");
        }
    }

    private void setOddDetailColorByType(OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity currentDetailsEntity, int type, String color) {
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
