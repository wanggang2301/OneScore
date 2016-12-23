package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.custom.CustomListAdapter;
import com.hhly.mlottery.bean.custombean.customlistdata.CustomFristBean;
import com.hhly.mlottery.bean.custombean.customlistdata.CustomListBean;
import com.hhly.mlottery.bean.custombean.customlistdata.CustomSecondBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CustomListEvent;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.LoadMoreRecyclerView;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by yixq on 2016/12/5.
 * mail：yixq@13322.com
 * describe: 定制列表页
 */

public class CustomListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private LinearLayoutManager mLinearLayoutManager;
    private List mFirstData;
    private CustomListAdapter mAdapter;
    private LoadMoreRecyclerView mCustomListRecycle;

    private CustomFocusClickListener mCustomFocusClickListener;
    private List<CustomFristBean> mAllDataList;
    private TextView mCustomText;
    private ImageView mBack;

    public static final String CUSTOM_LEAGUE_FOCUSID = "custom_leagueId_focus_ids";
    public static final String CUSTOM_TEAM_FOCUSID = "custom_team_focus_ids";

    private final static int VIEW_STATUS_LOADING = 1;//请求中
    private final static int VIEW_STATUS_NET_NO_DATA = 2;//暂无数据
    private final static int VIEW_STATUS_SUCCESS = 3;//请求成功
    private final static int VIEW_STATUS_NET_ERROR = 4;//请求失败
    private final static int VIEW_STATUS_CUSTOM_NO_DATA = 5;//暂无定制
    private final static int VIEW_STATUS_CUSTOM_REFRESH_ONCLICK = 6;//点击刷新
    private LinearLayout mErrorll;
    private TextView mrefshTxt;
    private TextView mNoData;
    private LinearLayout mNoCustom;
    private TextView mCustomTxt;
    private LinearLayout monClickLoading;
    private ExactSwipeRefreshLayout mRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.custom_activity);

        initView();
        setState(VIEW_STATUS_LOADING);
        mLoadHandler.postDelayed(mRun, 500);
    }

    // 定义关注监听
    public interface CustomFocusClickListener {
        void FocusOnClick(View view, String dataId, CustomFristBean firstData);

        void FocusOnClick(View view, String dataId, CustomSecondBean secondData);
    }

    /**
     * 子线程 处理数据加载
     */
    Handler mLoadHandler = new Handler();
    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            initData();
        }
    };

    private void initView() {
        /**头部布局*/
        TextView mCustomTitle = (TextView) findViewById(R.id.public_txt_title);
        mCustomTitle.setText(getResources().getString(R.string.custom_list_cus));

        mBack = (ImageView) findViewById(R.id.public_img_back);
        mBack.setOnClickListener(this);

        mCustomText = (TextView) findViewById(R.id.tv_right);
        mCustomText.setVisibility(View.VISIBLE);
        mCustomText.setText(getResources().getString(R.string.custom_accomplish_cus));
        mCustomText.setOnClickListener(this);

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);

        //下拉控件
        mRefresh = (ExactSwipeRefreshLayout) findViewById(R.id.custom_refresh_layout);
        mRefresh.setColorSchemeResources(R.color.bg_header);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getApplicationContext(), StaticValues.REFRASH_OFFSET_END));

        /**网络、数据 异常布局*/
        // 网络不给力提示
        mErrorll = (LinearLayout) findViewById(R.id.error_layout);
        //刷新点击按键
        mrefshTxt = (TextView) findViewById(R.id.reloading_txt);
        mrefshTxt.setOnClickListener(this);
        //暂无数据提示
        mNoData = (TextView) findViewById(R.id.nodata_txt);
        //还未定制任何赛事
        mNoCustom = (LinearLayout) findViewById(R.id.to_custom_ll);
        //去定制按键
        mCustomTxt = (TextView) findViewById(R.id.to_custom);
        mCustomTxt.setOnClickListener(this);

        //点击刷新是显示正在加载中....
        monClickLoading = (LinearLayout) findViewById(R.id.custom_loading_ll);


        /**内容布局*/
        mCustomListRecycle = (LoadMoreRecyclerView) findViewById(R.id.custom_recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCustomListRecycle.setLayoutManager(mLinearLayoutManager);

    }

    /**
     * 数据展示状态设置
     */
    private void setState(int state) {

        if (state == VIEW_STATUS_LOADING) {
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(true);
        } else if (state == VIEW_STATUS_SUCCESS) {
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(false);
        } else if (state == VIEW_STATUS_CUSTOM_REFRESH_ONCLICK) {
            mRefresh.setVisibility(View.GONE);
            mRefresh.setRefreshing(true);
        } else {
            mRefresh.setVisibility(View.GONE);
            mRefresh.setRefreshing(false);
        }
        monClickLoading.setVisibility((state == VIEW_STATUS_CUSTOM_REFRESH_ONCLICK) ? View.VISIBLE : View.GONE);
        mErrorll.setVisibility((state == VIEW_STATUS_NET_ERROR) ? View.VISIBLE : View.GONE);
        mNoData.setVisibility((state == VIEW_STATUS_NET_NO_DATA) ? View.VISIBLE : View.GONE);
        mNoCustom.setVisibility((state == VIEW_STATUS_CUSTOM_NO_DATA) ? View.VISIBLE : View.GONE);
    }

    private String currentLeagueid;
    private String currentTemaid;

    private void initData() {

//        String url = "http://192.168.10.242:8181/mlottery/core/basketballCommonMacth.findHotLeagueAndTeamConcern.do";
        String url = BaseURLs.CUSTOM_LIST_CUS_URL;

        final Map<String, String> map = new HashMap();
        String userid = AppConstants.register.getData().getUser().getUserId();
        String deviceid = AppConstants.deviceToken;
        map.put("userId", userid);
        map.put("deviceId", deviceid);


        VolleyContentFast.requestJsonByGet(url, map, new VolleyContentFast.ResponseSuccessListener<CustomListBean>() {
            @Override
            public void onResponse(CustomListBean json) {

                mFirstData = new ArrayList();

                mFirstData = json.getConcernLeagueAndTeam().getLeagueConcerns();

                mAllDataList = new ArrayList<>();
                /**这里必须 for 对新对象赋值  直接引用数据(mFirstData)源变化会跟着变化*/
                for (int i = 0; i < mFirstData.size(); i++) {
                    mAllDataList.add((CustomFristBean) mFirstData.get(i));
                }

                String currentFucusLeagueId = PreferenceUtil.getString(CUSTOM_LEAGUE_FOCUSID, "");
                String currentFucusTeamId = PreferenceUtil.getString(CUSTOM_TEAM_FOCUSID, "");

                if (currentFucusLeagueId.equals("") && currentFucusTeamId.equals("")) {

                    for (int i = 0; i < mFirstData.size(); i++) {
                        if (mFirstData.get(i) instanceof CustomFristBean) {
                            CustomFristBean data = (CustomFristBean) mFirstData.get(i);
                            L.d("yxq==1222====league ", "isConcern " + data.isConcern());
                            if (data.isConcern()) {
                                String fucusLeagueId = PreferenceUtil.getString(CUSTOM_LEAGUE_FOCUSID, "");
                                if (fucusLeagueId.equals("")) {
                                    PreferenceUtil.commitString(CUSTOM_LEAGUE_FOCUSID, data.getLeagueId() + "_A");
                                } else {
                                    PreferenceUtil.commitString(CUSTOM_LEAGUE_FOCUSID, fucusLeagueId + "," + data.getLeagueId() + "_A");
                                }
                            }

                            for (CustomSecondBean sendData : data.getTeamConcerns()) {
                                L.d("yxq==1222====team ", "isConcern " + sendData.isConcern());
                                if (sendData.isConcern()) {
                                    String fucusTeamId = PreferenceUtil.getString(CUSTOM_TEAM_FOCUSID, "");
                                    if (fucusTeamId.equals("")) {
                                        PreferenceUtil.commitString(CUSTOM_TEAM_FOCUSID, sendData.getTeamId() + "_B");
                                    } else {
                                        PreferenceUtil.commitString(CUSTOM_TEAM_FOCUSID, fucusTeamId + "," + sendData.getTeamId() + "_B");
                                    }
                                }
                            }
                        }
                    }
                }

                currentLeagueid = PreferenceUtil.getString(CUSTOM_LEAGUE_FOCUSID, "");
                currentTemaid = PreferenceUtil.getString(CUSTOM_TEAM_FOCUSID, "");

                L.d("yxq1220", "=完成前==mLeagueIdBuff= " + currentLeagueid.toString());
                L.d("yxq1220", "=完成前=mTemaIdBuff= " + currentTemaid.toString());

                fucusEvent();


                //刷新时清空recycleview 回收池
                if (mAdapter != null) {
                    mCustomListRecycle.getRecycledViewPool().clear();
                    mAdapter.notifyDataSetChanged();
                }
                mAdapter = new CustomListAdapter(getApplicationContext(), mFirstData);
                mCustomListRecycle.setAdapter(mAdapter);

                mAdapter.setmFocus(mCustomFocusClickListener);

                setOnItemClick(mFirstData);

                addSecondTier();
                setState(VIEW_STATUS_SUCCESS);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                setState(VIEW_STATUS_NET_ERROR);
            }
        }, CustomListBean.class);
    }

    private void updateAdapter() {
        if (mAdapter == null) {
            return;
        }
        mAdapter.setData(mFirstData);
        mAdapter.notifyDataSetChanged();
    }

    private String sendLeagueId() {

        String fucusId = PreferenceUtil.getString(CUSTOM_LEAGUE_FOCUSID, "");
        String[] onlyId = fucusId.split("[,]");

        StringBuffer msb = new StringBuffer();
        for (CustomFristBean firstdata : mAllDataList) {

            for (String id : onlyId) {
                if (id.replaceAll("_A", "").equals(firstdata.getLeagueId())) {
                    if ("".equals(msb.toString())) {
                        msb.append(firstdata.getLeagueId());
                    } else {
                        msb.append("," + firstdata.getLeagueId());
                    }
                }
            }
        }

        String reslut = msb.toString();
        L.d("yxq1220", "jsonLeagueId ==*****" + reslut);
        return reslut;
    }

    private String sendTeamId() {

        String fucusId = PreferenceUtil.getString(CUSTOM_TEAM_FOCUSID, "");
        String[] onlyId = fucusId.split("[,]");

        Map<String, List<Integer>> sendMap = new LinkedHashMap<>();
        for (CustomFristBean firstdata : mAllDataList) {

            List<Integer> mlist = new ArrayList<>();

            boolean isConcern = false;
            for (CustomSecondBean second : firstdata.getTeamConcerns()) {

                for (String id : onlyId) {
                    String sub = id.replaceAll("_B", "");

                    if (sub.equals(second.getTeamId())) {
                        isConcern = true;
                        mlist.add(Integer.parseInt(second.getTeamId()));
                    }
                }
            }
            if (isConcern) {
                sendMap.put(firstdata.getLeagueId(), mlist);
            }
        }

        Gson gson = new Gson();
        String reslut = gson.toJson(sendMap);
        L.d("yxq1220", "jsonTeamId ==*****" + reslut);

        return reslut;
    }

    private void addSecondTier() {
        /**
         * 记录添加中间层（）的位置
         */
        int addSecondIndex = 1;
        for (int i = 0; i < mFirstData.size(); i++) {
            if (mFirstData.get(i) instanceof CustomFristBean) {
                CustomFristBean firstData = (CustomFristBean) mFirstData.get(i);

                mAdapter.addAllChild(firstData.getTeamConcerns(), addSecondIndex);
                addSecondIndex += (firstData.getTeamConcerns().size() + 1);
            }
        }
    }

    private void setOnItemClick(final List<?> mData) {
        mAdapter.setOnItemClickLitener(new CustomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ImageView isOpen) {

                if (mData.get(position) instanceof CustomFristBean) { //判断点击的是不是 中间层 0
                    CustomFristBean parent = (CustomFristBean) mData.get(position);
                    if ((position + 1) == mData.size()) {//判断是否为最后一个元素

                        if (isOpen != null) {
                            isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_2);
                        }

                        mAdapter.addAllChild(parent.getTeamConcerns(), position + 1);
                    } else {
                        if (mData.get(position + 1) instanceof CustomFristBean) {//为折叠状态需要添加儿子
                            if (isOpen != null) {
                                isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_2);
                            }
                            mAdapter.addAllChild(parent.getTeamConcerns(), position + 1);
                        } else if (mData.get(position + 1) instanceof CustomSecondBean) {//为展开状态需要删除儿子
                            if (isOpen != null) {
                                isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_1);
                            }
                            mAdapter.deleteAllChild(position + 1, parent.getTeamConcerns().size());
                        }
                    }
                } else {// 否则为最内层（赛事层）比赛的点击事件这里写
                    // TODO***************************************************
                }
            }
        });
    }

    public void fucusEvent() {
        mCustomFocusClickListener = new CustomFocusClickListener() {
            @Override
            public void FocusOnClick(View view, String dataId, CustomFristBean firstData) {
                boolean isFucus = (boolean) view.getTag();

                if (!isFucus) {// 未选中 --> 选中
                    addId(dataId, 0);
                    view.setTag(false);
                    firstData.setConcern(false);

                } else { //选中 --> 未选中
                    deletaId(dataId, 0);
                    view.setTag(true);
                    firstData.setConcern(true);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void FocusOnClick(View view, String dataId, CustomSecondBean secondData) {
                boolean isFucus = (boolean) view.getTag();
                if (!isFucus) {// 未选中 --> 选中
                    addId(dataId, 1);
                    view.setTag(false);
                    secondData.setConcern(false);
                } else { //选中 --> 未选中
                    deletaId(dataId, 1);
                    view.setTag(true);
                    secondData.setConcern(true);
                }
                mAdapter.notifyDataSetChanged();
            }
        };
    }

    private void addId(String thirdid, int type) {
        if (type == 0) {
            String fucusId = PreferenceUtil.getString(CUSTOM_LEAGUE_FOCUSID, "");
            if (fucusId.equals("")) {
                PreferenceUtil.commitString(CUSTOM_LEAGUE_FOCUSID, thirdid);
            } else {
                PreferenceUtil.commitString(CUSTOM_LEAGUE_FOCUSID, fucusId + "," + thirdid);
            }
        } else {
            String fucusId = PreferenceUtil.getString(CUSTOM_TEAM_FOCUSID, "");
            if (fucusId.equals("")) {
                PreferenceUtil.commitString(CUSTOM_TEAM_FOCUSID, thirdid);
            } else {
                PreferenceUtil.commitString(CUSTOM_TEAM_FOCUSID, fucusId + "," + thirdid);
            }
        }
    }

    private void deletaId(String thirdid, int type) {
        if (type == 0) {
            String fucusId = PreferenceUtil.getString(CUSTOM_LEAGUE_FOCUSID, "");
            String[] arrId = fucusId.split("[,]");
            StringBuffer mSbuf = new StringBuffer();
            for (String id : arrId) {
                if (!id.equals(thirdid)) {
                    if ("".equals(mSbuf.toString())) {
                        mSbuf.append(id);
                    } else {
                        mSbuf.append("," + id);
                    }
                }
            }
            PreferenceUtil.commitString(CUSTOM_LEAGUE_FOCUSID, mSbuf.toString());
        } else {
            String fucusId = PreferenceUtil.getString(CUSTOM_TEAM_FOCUSID, "");
            String[] arrId = fucusId.split("[,]");
            StringBuffer mSbuf = new StringBuffer();
            for (String id : arrId) {
                if (!id.equals(thirdid)) {
                    if ("".equals(mSbuf.toString())) {
                        mSbuf.append(id);
                    } else {
                        mSbuf.append("," + id);
                    }
                }
            }
            PreferenceUtil.commitString(CUSTOM_TEAM_FOCUSID, mSbuf.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:

                String mTemaJsonId = sendTeamId();
                String mLeagueJsonId = sendLeagueId();

                L.d("yxq1220", "=完成后==mLeagueIdBuff= " + PreferenceUtil.getString(CUSTOM_LEAGUE_FOCUSID, ""));
                L.d("yxq1220", "=完成后=mTemaIdBuff= " + PreferenceUtil.getString(CUSTOM_TEAM_FOCUSID, ""));

                L.d("yxq1220", "================点击完成=============================");
                EventBus.getDefault().post(new CustomListEvent(mLeagueJsonId, mTemaJsonId));
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.public_img_back:

                if (currentLeagueid != null) {
                    PreferenceUtil.commitString(CUSTOM_LEAGUE_FOCUSID, currentLeagueid.toString());
                }
                if (currentTemaid != null) {
                    PreferenceUtil.commitString(CUSTOM_TEAM_FOCUSID, currentTemaid.toString());
                }

                L.d("yxq1220", "=返回后==mLeagueIdBuff= " + PreferenceUtil.getString(CUSTOM_LEAGUE_FOCUSID, ""));
                L.d("yxq1220", "=返回后=mTemaIdBuff= " + PreferenceUtil.getString(CUSTOM_TEAM_FOCUSID, ""));

                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.reloading_txt:
                setState(VIEW_STATUS_CUSTOM_REFRESH_ONCLICK);
                mLoadHandler.postDelayed(mRun, 500);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            if (currentLeagueid != null) {
                PreferenceUtil.commitString(CUSTOM_LEAGUE_FOCUSID, currentLeagueid.toString());
            }
            if (currentTemaid != null) {
                PreferenceUtil.commitString(CUSTOM_TEAM_FOCUSID, currentTemaid.toString());
            }

            finish();
            overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRefresh() {

        setState(VIEW_STATUS_LOADING);
        mLoadHandler.postDelayed(mRun, 500);
    }
}
