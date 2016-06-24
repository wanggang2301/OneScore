package com.hhly.mlottery.frame.oddfragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivityTest;
import com.hhly.mlottery.adapter.OddDetailsLeftAdapter;
import com.hhly.mlottery.adapter.OddsAdapter;
import com.hhly.mlottery.adapter.OddsDetailsAdapter;
import com.hhly.mlottery.bean.oddsbean.OddsDataInfo;
import com.hhly.mlottery.bean.oddsbean.OddsDetailsDataInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 亚盘
 * Created by 103TJL on 2016/3/4.
 */
public class PlateFragment extends Fragment implements View.OnClickListener {


    private NestedScrollView mNestedScrollView;
    private NestedScrollView mNestedScrollView_details;

    private View mView;
    private Context mContext;
    private OddsAdapter mOddsAdapter;//指数 博彩数据adapter
    private List<OddsDataInfo.ListOddEntity> mListOddEntity;//指数 博彩数据实体类
    private ListView mListView;//指数list
    private static final int ERROR = -1;//访问失败
    private static final int SUCCESS = 0;// 访问成功
    private static final int STARTLOADING = 1;// 数据加载中
    private static final int NODATA = 400;// 暂无数据
    private LinearLayout plate_linearlayout;//指数公司主队标题layout
    private OddDetailsLeftAdapter oddDetailsLeftAdapter;

    //指数详情
    private ListView mLeft_listview;//指数详情listview（左边）
    private PinnedHeaderExpandableListView mRight_listview;//指数详情listview(右边)
    private LinearLayout odds_details_layout;//指数详情layout
    private OddsDetailsAdapter mOddsDetailsAdapter;//指数 博彩详情adapter
    private static final int RIGHT_SUCCESS = 10;// 详情右边访问成功
    private static final int RIGHT_ERROR = -10;//右边访问失败
    private static final int RIGHT_STARTLOADING = 100;// 右边数据加载中
    private static final int RIGHT_NODATA = 4000;// 暂无数据
    private List<String> groupDataList = new ArrayList<>();//指数 博彩详情实体类（头部）
    private List<List<OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity>> childDetailsList = new ArrayList<>();//指数 博彩详情实体类
    private TextView odds_back_txt;//指数详情的左边listview的返回
    private int mSize;
    private String stIdposition;
    //加载数据
    private LinearLayout ll_plate_main;// 有数据时候布局
    private FrameLayout fl_plate_loading;// 正在加载中
    private FrameLayout fl_plate_networkError;// 加载失败
    private FrameLayout fl_plate_noData;// 暂无数据

    private TextView plate_reLoading;// 刷新
    //右边加载数据
    private FrameLayout right_fl_plate_loading;// 正在加载中
    private FrameLayout right_fl_plate_networkError;// 加载失败
    private FrameLayout right_fl_plate_noData;// 详情暂无数据
    private TextView right_plate_reLoading;// 刷新
    ////获取赛事id
//    private String mThirdId="294060";
    private String mThirdId;

    private String oddType;//	1:亚冠 2:欧赔 3:大小

    private String stKey, stKey2, stKey3;//	1:亚冠 2:欧赔 3:大小
    //	标题名称id
    private TextView plate_home_txt_id, plate_dish_txt_id, plate_guest_txt_id, plate_home_details_txt_id, plate_dish_details_txt_id, plate_guest_details_txt_id;
    private TextView fl_plate_txt_view;//标题分割线

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.plate_frame, container, false);
        stKey = getArguments().getString("key1");
        stKey2 = getArguments().getString("key2");
        stKey3 = getArguments().getString("key3");
        if ("one".equals(stKey)) {//亚盘
            oddType = "1";
        } else if ("two".equals(stKey2)) {//欧赔
            oddType = "2";
        } else if ("three".equals(stKey3)) {//大小球
            oddType = "3";
        }
        InitView();
        InitData();
        return mView;
    }

    private void InitView() {
        //	标题分割线
        fl_plate_txt_view = (TextView) mView.findViewById(R.id.fl_plate_txt_view);
        //	标题名称id
        plate_home_txt_id = (TextView) mView.findViewById(R.id.plate_home_txt_id);
        plate_dish_txt_id = (TextView) mView.findViewById(R.id.plate_dish_txt_id);
        plate_guest_txt_id = (TextView) mView.findViewById(R.id.plate_guest_txt_id);
        //详情
        plate_home_details_txt_id = (TextView) mView.findViewById(R.id.plate_home_details_txt_id);
        plate_dish_details_txt_id = (TextView) mView.findViewById(R.id.plate_dish_details_txt_id);
        plate_guest_details_txt_id = (TextView) mView.findViewById(R.id.plate_guest_details_txt_id);
        if ("1".equals(oddType)) {//亚盘
            //显示默认字体 （默认字体为亚盘）
        } else if ("2".equals(oddType)) {//欧赔
            //欧赔，设置主胜，平，客胜，平且3列相等
            plate_home_txt_id.setText(mContext.getResources().getText(R.string.odd_home_op_txt));
            plate_dish_txt_id.setText(mContext.getResources().getText(R.string.odd_dish_op_txt));
            plate_guest_txt_id.setText(mContext.getResources().getText(R.string.odd_guest_op_txt));
            //详情
            plate_home_details_txt_id.setText(mContext.getResources().getText(R.string.odd_home_op_txt));
            plate_dish_details_txt_id.setText(mContext.getResources().getText(R.string.odd_dish_op_txt));
            plate_guest_details_txt_id.setText(mContext.getResources().getText(R.string.odd_guest_op_txt));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, AbsListView.LayoutParams.MATCH_PARENT);
            params.weight = 3;
            //设置盘口的宽度
            plate_dish_txt_id.setLayoutParams(params);//设置指数界面的标题文字
            plate_dish_details_txt_id.setLayoutParams(params);//设置指数详情界面的标题文字

        } else if ("3".equals(oddType)) {//大小球
            //欧赔，和亚盘样式一样
            plate_home_txt_id.setText(mContext.getResources().getText(R.string.odd_home_big_txt));
            plate_dish_txt_id.setText(mContext.getResources().getText(R.string.odd_dish_big_txt));
            plate_guest_txt_id.setText(mContext.getResources().getText(R.string.odd_guest_big_txt));
            //详情
            plate_home_details_txt_id.setText(mContext.getResources().getText(R.string.odd_home_big_txt));
            plate_dish_details_txt_id.setText(mContext.getResources().getText(R.string.odd_dish_big_txt));
            plate_guest_details_txt_id.setText(mContext.getResources().getText(R.string.odd_guest_big_txt));
        }
        //数据加载的layout
        // ll_plate_main = (LinearLayout) mView.findViewById(R.id.ll_plate_main);
        fl_plate_loading = (FrameLayout) mView.findViewById(R.id.fl_plate_loading);
        fl_plate_networkError = (FrameLayout) mView.findViewById(R.id.fl_plate_networkError);
        //暂无数据
        fl_plate_noData = (FrameLayout) mView.findViewById(R.id.fl_plate_noData);
        plate_reLoading = (TextView) mView.findViewById(R.id.plate_reLoading);// 刷新
        //详情右边的layout
        right_fl_plate_loading = (FrameLayout) mView.findViewById(R.id.right_fl_plate_loading);
        right_fl_plate_networkError = (FrameLayout) mView.findViewById(R.id.right_fl_plate_networkError);
        //详情指数暂无数据
        right_fl_plate_noData = (FrameLayout) mView.findViewById(R.id.right_fl_plate_noData);
        right_plate_reLoading = (TextView) mView.findViewById(R.id.right_plate_reLoading);// 刷新

        //指数公司名称标题布局
        plate_linearlayout = (LinearLayout) mView.findViewById(R.id.plate_linearlayout);
        //指数博彩数据
        mListView = (ListView) mView.findViewById(R.id.plate_frame_listview);


        mNestedScrollView = (NestedScrollView) mView.findViewById(R.id.nested_scroll_view);
        mNestedScrollView_details = (NestedScrollView) mView.findViewById(R.id.nested_scroll_view_details);


        //指数界面listview跳转
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Map<String, String>> obList = new ArrayList<>();// 建立一个数组存储listview上显示的数据
                for (int m = 0; m < mListOddEntity.size(); m++) {// initData为一个list类型的数据源
                    Map<String, String> obMap = new HashMap<>();
                    obMap.put("id", mListOddEntity.get(m).getId());
                    obMap.put("name", mListOddEntity.get(m).getName());
                    obList.add(obMap);
                }
                //点击指数页面，传值给详情界面
                LeftData(obList, mListOddEntity.get(position).getId(), position);
            }
        });
        //指数博彩数据详情list
        mLeft_listview = (ListView) mView.findViewById(R.id.odds_tetails_left_listview);
        //返回
        odds_back_txt = (TextView) mView.findViewById(R.id.odds_back_txt);
        odds_back_txt.setOnClickListener(this);

        mRight_listview = (PinnedHeaderExpandableListView) mView.findViewById(R.id.odds_tetails_right_listview);
        //设置悬浮头部VIEW
//      mRight_listview.setHeaderView(getLayoutInflater().inflate(R.layout.item_odds_header, mRight_listview,false));
        mRight_listview.setHeaderView(getActivity().getLayoutInflater().inflate(R.layout.item_odds_header, mRight_listview, false));
        //指数详细称标题布局
        odds_details_layout = (LinearLayout) mView.findViewById(R.id.odds_details_layout);
        mRight_listview.setChildDivider(getResources().getDrawable(R.color.line_football_footer));

        //解决与下拉刷新的冲突
        mRight_listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mNestedScrollView_details.stopNestedScroll();
                        mRight_listview.setNestedScrollingEnabled(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //只有listview滑到顶部才可以下拉刷新
                        if (mRight_listview.getFirstVisiblePosition() != 0) {
                            // ((FootballMatchDetailActivityTest) getActivity()).mRefreshLayout.setEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mNestedScrollView_details.stopNestedScroll();
                        mRight_listview.setNestedScrollingEnabled(true);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        // ((FootballMatchDetailActivityTest) getActivity()).mRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        //只有listview滑到顶部才可以下拉刷新
                        if (mListView.getFirstVisiblePosition() != 0) {
                            //  ((FootballMatchDetailActivityTest) getActivity()).mRefreshLayout.setEnabled(false);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // ((FootballMatchDetailActivityTest) getActivity()).mRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
        mLeft_listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        //只有listview滑到顶部才可以下拉刷新
                     /*   if (mLeft_listview.getFirstVisiblePosition() != 0) {
                          //  ((FootballMatchDetailActivityTest) getActivity()).mRefreshLayout.setEnabled(false);
                        }*/
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // ((FootballMatchDetailActivityTest) getActivity()).mRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });


        // 访问失败，点击刷新
        plate_reLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 请求数据
                InitData();
            }
        });
        // 访问失败，详情右边点击刷新
        right_plate_reLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //指数详情右边数据
                RightData(stIdposition);
            }
        });

    }

    public void InitData() {
        // 设置参数
        mHandler.sendEmptyMessage(STARTLOADING);// 正在加载数据中
        if (getActivity() == null) {
            return;
        }
        mThirdId = ((FootballMatchDetailActivityTest) getActivity()).mThirdId;
        Map<String, String> myPostParams = new HashMap<>();
        myPostParams.put("thirdId", mThirdId);
        myPostParams.put("oddType", oddType);

        VolleyContentFast.requestJsonByPost(BaseURLs.URL_FOOTBALL_MATCHODD, myPostParams, new VolleyContentFast.ResponseSuccessListener<OddsDataInfo>() {
            @Override
            public void onResponse(OddsDataInfo jsonObject) {
                // 访问成功
                if (jsonObject != null) {
                    mListOddEntity = jsonObject.getListOdd();

                    if (mListOddEntity != null && mListOddEntity.size() > 0) {

                        mListOddEntity.addAll(mListOddEntity); //测试滑动listview
                        mListOddEntity.addAll(mListOddEntity); //测试滑动listview
                        mListOddEntity.addAll(mListOddEntity); //测试滑动listview

                        if ("1".equals(oddType)) {//亚盘
                            mOddsAdapter = new OddsAdapter(mContext, mListOddEntity, stKey);
                        } else if ("2".equals(oddType)) {//欧赔
                            mOddsAdapter = new OddsAdapter(mContext, mListOddEntity, stKey2);
                        } else if ("3".equals(oddType)) {//大小球
                            mOddsAdapter = new OddsAdapter(mContext, mListOddEntity, stKey3);
                        }
                        //添加指数数据设置adapter
                        mListView.setAdapter(mOddsAdapter);
                        mHandler.sendEmptyMessage(SUCCESS);// 访问成功
                    } else {
                        mHandler.sendEmptyMessage(NODATA);//没数据
                    }

                } else {
                    // 后台没请求到数据
                    mHandler.sendEmptyMessage(NODATA);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERROR);// 访问失败
            }
        }, OddsDataInfo.class);
    }


    /**
     * //指数详情的左边列表
     *
     * @param list        获得list
     *                    //     * @param idPosition 获得当前position的对应的 id
     * @param positionNum 获得当前的position
     */

    private void LeftData(final List<Map<String, String>> list, String idPosition, int positionNum) {
        //指数页面gone
        //  mListView.setVisibility(View.GONE);
        mNestedScrollView.setVisibility(View.GONE);
        //指数布局gone
        plate_linearlayout.setVisibility(View.GONE);
        //指数详情界面visible
        odds_details_layout.setVisibility(View.VISIBLE);
        oddDetailsLeftAdapter = new OddDetailsLeftAdapter(mContext, list);
//        SimpleAdapter smAdapter=new SimpleAdapter(mContext,list,R.layout.item_odds_left,new String[]{"name"},new int[]{R.id.odds_left_txt});
        mLeft_listview.setAdapter(oddDetailsLeftAdapter);
        //根据传过去的postion更改选中的item选中背景
        oddDetailsLeftAdapter.setDefSelect(positionNum);
        stIdposition = idPosition;
        //指数详情右边数据
        RightData(stIdposition);
        //详情左边list点击事件
        mLeft_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //根据传过去的postion更改选中的item选中背景
                oddDetailsLeftAdapter.setDefSelect(position);
                //根据position获取list集合里面id
                String stId = list.get(position).get("id");
                if (mOddsDetailsAdapter != null) {
                    //切换指数详情右边listview之前刷新adapter
                    //清除父类和子类view的数据，刷新 mOddsDetailsAdapter
                    mOddsDetailsAdapter.clearData();
                    stIdposition = stId;
                    //指数详情右边数据
                    RightData(stIdposition);
                }


            }
        });
    }

    //指数详情右边的数据
    private void RightData(String idPosition) {
//        String url = "http://192.168.10.152:8181/mlottery/core/footBallMatch.matchOddDetail.do?";
        mHandler.sendEmptyMessage(RIGHT_STARTLOADING);// 访问成功
        Map<String, String> myPostParams = new HashMap<>();
        myPostParams.put("companyId", idPosition);
        myPostParams.put("oddType", oddType);
        myPostParams.put("thirdId", mThirdId);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_MATCHODD_DETAILS, myPostParams, new VolleyContentFast.ResponseSuccessListener<OddsDetailsDataInfo>() {
            @Override
            public void onResponse(OddsDetailsDataInfo jsonObject) {
                // 访问成功
                if (jsonObject != null) {
                    //首先拿到json对象赋值给DetailsEntity对象
                    List<OddsDetailsDataInfo.DetailsEntity> groupListDetailsEntity = jsonObject.getDetails();
                    if (groupListDetailsEntity != null) {
                        mSize = groupListDetailsEntity.size();
                        if (groupListDetailsEntity.size() > 0) {
                            for (int i = 0; i < mSize; i++) {
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

                                        if (currentModel.getScore() != null) {//如果当前的比分不等于空
                                            if (!currentModel.getScore().equals(nextModel.getScore())) {
                                                currentModel.setScoreVisible(true);
                                            }
                                        }


                                    } else {
                                        OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity currentModel = childDetailsList.get(i).get(j);
                                        OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity nextModel = childDetailsList.get(i).get(j + 1);

                                        setOddDetailColor(currentModel, currentModel.getHomeOdd(), nextModel.getHomeOdd(), 1);
                                        setOddDetailColor(currentModel, currentModel.getGuestOdd(), nextModel.getGuestOdd(), 2);
                                        setOddDetailColor(currentModel, currentModel.getHand(), nextModel.getHand(), 3);

                                        if (currentModel.getScore() != null) {//如果当前的比分不等于空
                                            if (!currentModel.getScore().equals(nextModel.getScore())) {
                                                currentModel.setScoreVisible(true);
                                            }
                                        }
                                    }
                                }
                            }

                            if ("1".equals(oddType)) {//亚盘
                                mOddsDetailsAdapter = new OddsDetailsAdapter(childDetailsList, groupDataList, mContext, mRight_listview, stKey);
                            } else if ("2".equals(oddType)) {//欧赔
                                mOddsDetailsAdapter = new OddsDetailsAdapter(childDetailsList, groupDataList, mContext, mRight_listview, stKey2);
                            } else if ("3".equals(oddType)) {//大小球
                                mOddsDetailsAdapter = new OddsDetailsAdapter(childDetailsList, groupDataList, mContext, mRight_listview, stKey3);
                            }

                            mRight_listview.setAdapter(mOddsDetailsAdapter);
                            //设置 默认打开的(父类的) group
                            for (int i = 0; i < mSize; i++) {
                                mRight_listview.expandGroup(i);
                            }

                            mHandler.sendEmptyMessage(RIGHT_SUCCESS);// 指数详情访问成功
                        } else {
                            mHandler.sendEmptyMessage(RIGHT_NODATA);//没数据
                        }
                    } else {
                        // 后台没请求到数据
                        mHandler.sendEmptyMessage(RIGHT_NODATA);
                    }
                } else {
                    // 后台没请求到数据
                    mHandler.sendEmptyMessage(RIGHT_ERROR);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(RIGHT_ERROR);// 访问失败
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
                case SUCCESS:
                    mOddsAdapter.notifyDataSetChanged();//指数 访问成功
                    fl_plate_loading.setVisibility(View.GONE);
                    fl_plate_networkError.setVisibility(View.GONE);
                    fl_plate_txt_view.setVisibility(View.GONE);
                    // ll_plate_main.setVisibility(View.VISIBLE);

                    mListView.setVisibility(View.VISIBLE);
                    break;
                case STARTLOADING://正在加载的时候
                    fl_plate_loading.setVisibility(View.VISIBLE);
                    fl_plate_txt_view.setVisibility(View.VISIBLE);
                    fl_plate_networkError.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                    break;
                case ERROR://访问失败
                    fl_plate_loading.setVisibility(View.GONE);
                    fl_plate_networkError.setVisibility(View.VISIBLE);
                    fl_plate_txt_view.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    break;
                case NODATA://没有数据
                    fl_plate_loading.setVisibility(View.GONE);
                    fl_plate_networkError.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                    fl_plate_txt_view.setVisibility(View.VISIBLE);
                    fl_plate_noData.setVisibility(View.VISIBLE);
                    break;
                case RIGHT_SUCCESS:
                    mOddsDetailsAdapter.notifyDataSetChanged();//指数详情（右边数据）
                    right_fl_plate_loading.setVisibility(View.GONE);
                    right_fl_plate_networkError.setVisibility(View.GONE);
                    mRight_listview.setVisibility(View.VISIBLE);
                    break;
                case RIGHT_STARTLOADING://正在加载的时候
                    right_fl_plate_loading.setVisibility(View.VISIBLE);
                    right_fl_plate_networkError.setVisibility(View.GONE);
                    mRight_listview.setVisibility(View.GONE);
                    break;
                case RIGHT_ERROR://访问失败
                    right_fl_plate_loading.setVisibility(View.GONE);
                    right_fl_plate_networkError.setVisibility(View.VISIBLE);
                    mRight_listview.setVisibility(View.GONE);
                    break;
                case RIGHT_NODATA://详情没有数据
                    right_fl_plate_loading.setVisibility(View.GONE);
                    right_fl_plate_networkError.setVisibility(View.GONE);
                    mRight_listview.setVisibility(View.GONE);
                    right_fl_plate_noData.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.odds_back_txt://返回
                //指数页面显示
                mListView.setVisibility(View.VISIBLE);
                //mNestedScrollView.setVisibility(View.VISIBLE);
                //指数布局显示
                plate_linearlayout.setVisibility(View.VISIBLE);
                //指数详情界面隐藏
                odds_details_layout.setVisibility(View.GONE);
                //如果listview不等于空
                if (mRight_listview != null && mOddsDetailsAdapter != null) {
                    //清除父类和子类view的数据，刷新adapter
                    mOddsDetailsAdapter.clearData();

                }

                break;
            default:
                break;

        }
    }

}
