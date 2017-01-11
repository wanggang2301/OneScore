package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.multipleAdapter.MultipleListBasketAdapter;
import com.hhly.mlottery.adapter.multipleAdapter.MultipleListFootballAdapter;
import com.hhly.mlottery.bean.HotFocusLeagueCup;
import com.hhly.mlottery.bean.ImmediateMatchs;
import com.hhly.mlottery.bean.LeagueCup;
import com.hhly.mlottery.bean.Match;
import com.hhly.mlottery.bean.multiplebean.MultipleByValueBean;
import com.hhly.mlottery.callback.RequestHostFocusCallBack;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.footframe.eventbus.ScoresMatchFilterEventBusEntity;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.FiltrateCupsMap;
import com.hhly.mlottery.util.HotFocusUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by yixq on 2017/1/5.
 * mail：yixq@13322.com
 * describe:
 */

public class MultiScreenViewingListActivity extends Activity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    protected Context mContext;

    private TextView mConfirm;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mBasketRecycle;
    private RecyclerView mFootballRecycler;
    private MultipleListBasketAdapter mBasketAdapter;
    private RadioGroup mRadioGroup;
    private RadioButton mBasketRadioButton;
    private RadioButton mFootballRadioButton;
    private MultipleListFootballAdapter mFootballAdapter;
    private List<Match> mAllMatchs;// 所有比赛
    private List<Match> mMatchs;// 显示的比赛

    public static List<LeagueCup> mCups;// 全部联赛
    public static LeagueCup[] mCheckedCups;
    // 判断是否加载过数据
    private boolean isLoadedData = false;
    private TextView mFilterTxt;

    private int LOAD_DATA_STATUS_LOADING = 0;//加载中
    private int LOAD_DATA_STATUS_SUCCESS = 1;//加载成功
    private int LOAD_DATA_STATUS_ERROR = 2;//加载失败
    private LinearLayout mErrorLayout;
    private TextView mReload;
    private TextView mNodata;
    private LinearLayout mLoading;
    private RelativeLayout mUnfocus;
    private ExactSwipeRefreshLayout mRefresh;

    private final static int BASKET_TYPE = 0;//请求中
    private final static int FOOTBALL_TYPE = 1;//请求中
    private int borf = BASKET_TYPE;//区分足篮球[默认篮球]、

    private List<MultipleByValueBean> byValue = new LinkedList<>();//传参的list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        setContentView(R.layout.multiple_animation_list_act);
        EventBus.getDefault().register(this);

        initView();
        setState(MULTIPLE_STATUS_LOADING);
//        initData();
        mHandler.postDelayed(mRun, 500);
    }

    private void initView() {

        mRadioGroup = (RadioGroup) findViewById(R.id.multiple_gendergroup);
        mBasketRadioButton = (RadioButton) findViewById(R.id.multiple_basket);
        mBasketRadioButton.setChecked(true);//默认选中篮球
        mFootballRadioButton = (RadioButton) findViewById(R.id.multiple_football);
        setRadioGroupClick();

        mConfirm = (TextView) findViewById(R.id.multi_ok);
        mConfirm.setOnClickListener(this);

        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mBasketRecycle = (RecyclerView) findViewById(R.id.basket_recyclerview);
        mBasketRecycle.setLayoutManager(mLinearLayoutManager);

        LinearLayoutManager mLinearFootballManager = new LinearLayoutManager(getApplicationContext());
        mLinearFootballManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFootballRecycler = (RecyclerView) findViewById(R.id.football_recyclerview);
        mFootballRecycler.setLayoutManager(mLinearFootballManager);

        mFilterTxt = (TextView) findViewById(R.id.tv_right);
        mFilterTxt.setOnClickListener(this);

        /*数据异常UI显示*/
        //网络不给力
        mErrorLayout = (LinearLayout) findViewById(R.id.error_layout);
        //刷新
        mReload = (TextView) findViewById(R.id.reloading_txt);
        mReload.setOnClickListener(this);
        //暂无数据
        mNodata = (TextView) findViewById(R.id.nodata_txt);
        //加载中...
        mLoading = (LinearLayout) findViewById(R.id.custom_loading_ll);
        //暂无关注
        mUnfocus = (RelativeLayout) findViewById(R.id.multiple_unfocus_ll);
        TextView mNoFiltrate = (TextView) findViewById(R.id.multiple_no_data_tv);
        mNoFiltrate.setText(R.string.immediate_no_data);

        //下拉
        mRefresh = (ExactSwipeRefreshLayout) findViewById(R.id.custom_refresh_layout);
        mRefresh.setColorSchemeResources(R.color.bg_header);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getApplicationContext(), StaticValues.REFRASH_OFFSET_END));

    }

    Handler mHandler = new Handler();
    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            initData();
        }
    };

    private final static int MULTIPLE_STATUS_LOADING = 1;//请求中
    private final static int MULTIPLE_STATUS_NET_NO_DATA = 2;//暂无数据
    private final static int MULTIPLE_STATUS_SUCCESS = 3;//请求成功
    private final static int MULTIPLE_STATUS_NET_ERROR = 4;//请求失败
    private final static int MULTIPLE_STATUS_FOCUS_NO_DATA = 5;//暂无关注
    private final static int MULTIPLE_STATUS_FOCUS_REFRESH_ONCLICK = 6;//点击刷新

    /**
     * 数据展示状态设置
     */
    private void setState(int state) {

        if (state == MULTIPLE_STATUS_LOADING) {
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(true);
        } else if (state == MULTIPLE_STATUS_SUCCESS) {
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(false);
        } else if (state == MULTIPLE_STATUS_FOCUS_REFRESH_ONCLICK) {
            mRefresh.setVisibility(View.GONE);
            mRefresh.setRefreshing(true);
        } else {
            mRefresh.setVisibility(View.GONE);
            mRefresh.setRefreshing(false);
        }

        mLoading.setVisibility((state == MULTIPLE_STATUS_FOCUS_REFRESH_ONCLICK) ? View.VISIBLE : View.GONE);
        mErrorLayout.setVisibility((state == MULTIPLE_STATUS_NET_ERROR) ? View.VISIBLE : View.GONE);
        mNodata.setVisibility((state == MULTIPLE_STATUS_NET_NO_DATA) ? View.VISIBLE : View.GONE);
        mUnfocus.setVisibility((state == MULTIPLE_STATUS_FOCUS_NO_DATA) ? View.VISIBLE : View.GONE);


    }

    private void initData() {
        if (borf == BASKET_TYPE) {

            List<String> mBasketData = setdata(0);
            if (mBasketAdapter == null) {
                mBasketAdapter = new MultipleListBasketAdapter(mContext, mBasketData);
                mBasketRecycle.setAdapter(mBasketAdapter);
            } else {
                mBasketAdapter.notifyDataSetChanged();
            }
            mFootballRecycler.setVisibility(View.GONE);
            mBasketRecycle.setVisibility(View.VISIBLE);

            setState(MULTIPLE_STATUS_SUCCESS);

        } else if (borf == FOOTBALL_TYPE) {

            mFootballRecycler.setVisibility(View.VISIBLE);
            mBasketRecycle.setVisibility(View.GONE);

            VolleyContentFast.requestJsonByGet(BaseURLs.URL_ImmediateMatchs, new VolleyContentFast.ResponseSuccessListener<ImmediateMatchs>() {
                @Override
                public synchronized void onResponse(final ImmediateMatchs matchs) {
                    if (matchs == null || matchs.getImmediateMatch() == null) {
                        setState(MULTIPLE_STATUS_NET_NO_DATA);
                        return;
                    }

                    // 获取所有赛程
                    mAllMatchs = matchs.getImmediateMatch();
                    mMatchs = new ArrayList<Match>();

                    final String teamLogoPre = matchs.getTeamLogoPre();
                    final String teamLogoSuff = matchs.getTeamLogoSuff();

                    HotFocusUtils hotFocusUtils = new HotFocusUtils();
                    hotFocusUtils.loadHotFocusData(getApplicationContext(), new RequestHostFocusCallBack() {

                        @Override
                        public void callBack(HotFocusLeagueCup hotFocusLeagueCup) {
                            // hotFocusLeagueCup = null;
                            List<String> hotList = null;

                            if (hotFocusLeagueCup == null) {
                                hotList = new ArrayList<String>();
                            } else {
                                hotList = hotFocusLeagueCup.getHotLeagueIds();
                            }

                            if (FiltrateCupsMap.immediateCups.length != 0) {// 判断是否已经筛选过
                                for (Match m : mAllMatchs) {// 已选择的
                                    for (String checkedId : FiltrateCupsMap.immediateCups) {
                                        if (m.getRaceId().equals(checkedId)) {
                                            mMatchs.add(m);
                                            break;
                                        }
                                    }
                                }
                            } else {// 没有筛选过
                                for (Match m : mAllMatchs) {// 默认显示热门赛程
                                    for (String hotId : hotList) {
                                        if (m.getRaceId().equals(hotId)) {
                                            mMatchs.add(m);
                                            break;
                                        }
                                    }
                                }
                            }
                            mCups = matchs.getAll();
//                            mNoDataTextView.setText(R.string.immediate_no_data);
                            if (mMatchs.size() == 0) {// 没有热门赛事，显示全部

                                mMatchs.addAll(mAllMatchs);
                                mCheckedCups = mCups.toArray(new LeagueCup[mCups.size()]);
                                if (mMatchs.size() == 0) {// 一个赛事都没有，显示“暂无赛事”


                                    isLoadedData = true;
                                    setState(MULTIPLE_STATUS_NET_NO_DATA);
                                    return;
                                }// 否则走下面，不需要再写
                            } else {
                                List<LeagueCup> tempHotCups = new ArrayList<LeagueCup>();

                                if (FiltrateCupsMap.immediateCups.length != 0) {
                                    for (LeagueCup cup : mCups) {
                                        for (String checkedId : FiltrateCupsMap.immediateCups) {
                                            if (cup.getRaceId().equals(checkedId)) {
                                                tempHotCups.add(cup);
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    for (LeagueCup cup : mCups) {
                                        for (String hotId : hotList) {
                                            if (cup.getRaceId().equals(hotId)) {
                                                tempHotCups.add(cup);
                                                break;
                                            }
                                        }
                                    }
                                }
                                mCheckedCups = tempHotCups.toArray(new LeagueCup[tempHotCups.size()]);
                            }

                            if (mFootballAdapter == null) {
                                mFootballAdapter = new MultipleListFootballAdapter(mContext, mMatchs, teamLogoPre, teamLogoSuff);
                                mFootballAdapter.setmOnItemClickListener(new MultipleListFootballAdapter.MultipleRecyclerViewItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, String data, int position, Match matchData) {

                                        int datasize = byValue.size();
                                        if (datasize == 0) {
                                            MultipleByValueBean valueBean = new MultipleByValueBean();
                                            valueBean.setType(1);
                                            valueBean.setThirdId(data);
                                            byValue.add(valueBean);

                                            matchData.setChicks(!matchData.isChicks());
                                            mFootballAdapter.notifyDataSetChanged();
                                            L.d("yxq===011A ", "添加了对象 A " + valueBean.getThirdId());
                                            Toast.makeText(mContext,  "选择 "+byValue.size(), Toast.LENGTH_SHORT).show();
                                            L.d("yxq===011A ", "byValue.size() = " + byValue.size() + " ** ");
                                        }else if (datasize < 3){

                                            boolean isSame = false;//记录所选id是否已选中
                                            int indext = -1;//记录所选id在已选对象中的索引位置
                                            for (int i = 0; i < byValue.size(); i++) {
                                                if (byValue.get(i).getThirdId().equals(data)) {
                                                    isSame = true;
                                                    indext = i;
                                                    L.d("yxq===011B ", "删除对象 " + data);
                                                    break;
                                                }
                                            }
                                            if (isSame) {
                                                if (indext != -1) {
                                                    byValue.remove(indext);
                                                }
                                            }else{
                                                MultipleByValueBean valueBeana = new MultipleByValueBean();
                                                valueBeana.setType(1);
                                                valueBeana.setThirdId(data);
                                                byValue.add(valueBeana);
                                                L.d("yxq===011B ", "添加了对象 B " + valueBeana.getThirdId());
                                            }

                                            matchData.setChicks(!matchData.isChicks());
                                            mFootballAdapter.notifyDataSetChanged();
                                            Toast.makeText(mContext, "选择 "+ byValue.size(), Toast.LENGTH_SHORT).show();
                                            L.d("yxq===011B ", "byValue.size() = " + byValue.size() + " ** ");
                                        }else if (datasize >= 3) {
                                            for (int i = 0; i < byValue.size(); i++) {
                                                if (byValue.get(i).getThirdId().equals(data)) {
                                                    byValue.remove(i);

                                                    matchData.setChicks(!matchData.isChicks());
                                                    mFootballAdapter.notifyDataSetChanged();
                                                    break;
                                                }
                                            }
//                                            matchData.setChicks(!matchData.isChicks());
//                                            mFootballAdapter.notifyDataSetChanged();
                                            Toast.makeText(mContext, "选择 "+ byValue.size(), Toast.LENGTH_SHORT).show();
                                            L.d("yxq===011C ", "byValue.size() = " + byValue.size() + " ** ");
                                        }
                                    }
                                });
                                mFootballRecycler.setAdapter(mFootballAdapter);
                            } else {
//                                updateAdapter();
                                mFootballAdapter.notifyDataSetChanged();
                            }

                            isLoadedData = true;
                            setState(MULTIPLE_STATUS_SUCCESS);
                        }
                    });

                }
            }, new VolleyContentFast.ResponseErrorListener() {
                @Override
                public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                    setState(MULTIPLE_STATUS_NET_ERROR);
                    Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }, ImmediateMatchs.class);

        }
    }

    /**
     * RadioGroup的点击（足篮球切换）
     */
    private void setRadioGroupClick() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == mBasketRadioButton.getId()) {
                    borf = BASKET_TYPE;
                    setState(MULTIPLE_STATUS_LOADING);
                    mHandler.postDelayed(mRun, 500);
                    Toast.makeText(mContext, "篮球 " + borf, Toast.LENGTH_SHORT).show();
                } else if (checkedId == mFootballRadioButton.getId()) {
                    borf = FOOTBALL_TYPE;
                    setState(MULTIPLE_STATUS_LOADING);
                    mHandler.postDelayed(mRun, 500);
                    Toast.makeText(mContext, "足球 " + borf, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<String> setdata(int type) {

        List<String> data = new ArrayList<>();

        if (type == 0) {
            for (int i = 0; i < 30; i++) {
                data.add("篮球 " + i);
            }
        } else if (type == 1) {
            for (int i = 0; i < 30; i++) {
                data.add("足球 " + i);
            }
        }
        return data;
    }

    public static boolean isCheckedDefualt = false;// true为默认选中全部，但是在筛选页面不选中

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                if (borf == FOOTBALL_TYPE) {

                    Toast.makeText(mContext, "筛选", Toast.LENGTH_SHORT).show();
                    switch (1) {
                        case 0://加载失败
                            Intent intent = new Intent(getApplicationContext(), FiltrateMatchConfigActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(FiltrateMatchConfigActivity.NET_STATUS, false);
                            bundle.putInt("currentFragmentId", 1);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                        case 1://加载成功
                            intent = new Intent(getApplicationContext(), FiltrateMatchConfigActivity.class);
                            bundle = new Bundle();
                            LeagueCup[] allCups = mCups.toArray(new LeagueCup[]{});
                            bundle.putParcelableArray(FiltrateMatchConfigActivity.ALL_CUPS, allCups);// 传值到筛选页面的全部联赛，数据类型是LeagueCup[]
                            bundle.putParcelableArray(FiltrateMatchConfigActivity.CHECKED_CUPS,
                                    mCheckedCups);// 传值到筛选页面的已经选择的联赛，数据类型是LeagueCup[]
                            bundle.putBoolean(FiltrateMatchConfigActivity.CHECKED_DEFUALT, isCheckedDefualt);// 是否默认选择
                            bundle.putBoolean(FiltrateMatchConfigActivity.NET_STATUS, true);
                            bundle.putInt("currentFragmentId", 1);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                        case 2://加载中
                            Toast.makeText(getApplicationContext(), R.string.toast_data_loading, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }
                break;
            case R.id.reloading_txt:
                //TODO===========
                setState(MULTIPLE_STATUS_FOCUS_REFRESH_ONCLICK);
                mHandler.postDelayed(mRun, 500);
                break;
            case R.id.multi_ok:
                if (byValue == null || byValue.size() == 0) {
                    Toast.makeText(getApplicationContext(), "请选择比赛", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "已选择 " + byValue.size() + " 场比赛", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 筛选返回
     * 接受消息的页面实现
     */
    public void onEventMainThread(ScoresMatchFilterEventBusEntity scoresMatchFilterEventBusEntity) {
        if (scoresMatchFilterEventBusEntity.getFgIndex() == 1) {
            Map<String, Object> map = scoresMatchFilterEventBusEntity.getMap();
            String[] checkedIds = (String[]) ((LinkedList) map.get(FiltrateMatchConfigActivity.RESULT_CHECKED_CUPS_IDS)).toArray(new String[]{});
            FiltrateCupsMap.immediateCups = checkedIds;
            mMatchs.clear();
            for (Match match : mAllMatchs) {
                boolean isExistId = false;
                for (String checkedId : checkedIds) {
                    if (match.getRaceId().equals(checkedId)) {
                        isExistId = true;
                        break;
                    }
                }
                if (isExistId) {
                    mMatchs.add(match);
                }
            }
            List<LeagueCup> leagueCupList = new ArrayList<LeagueCup>();

            for (LeagueCup cup : mCups) {
                boolean isExistId = false;
                for (String checkedId : checkedIds) {
                    if (checkedId.equals(cup.getRaceId())) {
                        isExistId = true;
                        break;
                    }
                }

                if (isExistId) {
                    leagueCupList.add(cup);
                }
            }

            mCheckedCups = leagueCupList.toArray(new LeagueCup[]{});
            updateFootballAdapter();
            isCheckedDefualt = (boolean) map.get(FiltrateMatchConfigActivity.CHECKED_DEFUALT);

            if (mMatchs.size() == 0) {// 没有比赛
//                mViewHandler.sendEmptyMessage(VIEW_STATUS_FLITER_NO_DATA);
                setState(MULTIPLE_STATUS_FOCUS_NO_DATA);
            } else {
//                mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
                setState(MULTIPLE_STATUS_SUCCESS);
            }
        }
    }

    private void updateFootballAdapter() {
        if (mFootballAdapter == null) {
            return;
        }
        mFootballAdapter.updateDatas(mMatchs);
        mFootballAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        setState(MULTIPLE_STATUS_LOADING);
        mHandler.postDelayed(mRun, 500);
    }
}
