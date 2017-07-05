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
import android.widget.ImageView;
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
import com.hhly.mlottery.bean.basket.BasketMatchBean;
import com.hhly.mlottery.bean.basket.BasketMatchFilter;
import com.hhly.mlottery.bean.multiplebean.BasketMultipleRoot;
import com.hhly.mlottery.bean.multiplebean.MultipleByValueBean;
import com.hhly.mlottery.callback.RequestHostFocusCallBack;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.footballframe.eventbus.ScoresMatchFilterEventBusEntity;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.FiltrateCupsMap;
import com.hhly.mlottery.util.HotFocusUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MultipleBasketFilterListEvent;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by yixq on 2017/1/5.
 * mail：yixq@13322.com
 * describe:多屏动画列表页
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
    private ImageView mFilterTxt;

    private LinearLayout mErrorLayout;
    private TextView mReload;
    private TextView mNodata;
    private LinearLayout mLoading;
    private RelativeLayout mUnfocus;
    private ExactSwipeRefreshLayout mRefresh;

    private final static int BASKET_TYPE = 2;//篮球标记
    private final static int FOOTBALL_TYPE = 1;//足球标记
    private int borf = BASKET_TYPE;//区分足篮球[默认篮球]、

    private List<MultipleByValueBean> byValue = new ArrayList<>();//传参的list
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            MultipleByValueBean mCurrentId = (MultipleByValueBean) getIntent().getExtras().get("thirdId");
            borf = mCurrentId.getType();
            byValue.add(mCurrentId);
        }
        mContext = getApplicationContext();

        setContentView(R.layout.multiple_animation_list_act);
        EventBus.getDefault().register(this);

        initView();
        setState(MULTIPLE_STATUS_LOADING);

        mHandler.postDelayed(mRun, 500);
    }

    private void initView() {

        mRadioGroup = (RadioGroup) findViewById(R.id.multiple_gendergroup);
        mBasketRadioButton = (RadioButton) findViewById(R.id.multiple_basket);
        mFootballRadioButton = (RadioButton) findViewById(R.id.multiple_football);
        //默认选中
        if (borf == BASKET_TYPE) {
            mBasketRadioButton.setChecked(true);
        } else if (borf == FOOTBALL_TYPE) {
            mFootballRadioButton.setChecked(true);
        }
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

        mFilterTxt = (ImageView) findViewById(R.id.tv_right);
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

        //返回按钮
        mBack = (ImageView) findViewById(R.id.public_img_back);
        mBack.setOnClickListener(this);
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
    private final static int MULTIPLE_STATUS_FOCUS_NO_DATA = 5;//筛选0场
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

            initBasketData();

        } else if (borf == FOOTBALL_TYPE) {

            initFootballData();

        }
    }

    /* 数据加载状态 [筛选点击时的判断] 0 加载失败  1 加载成功 else 加载中*/
    public static int isBasketLoad = -1;
    public static int isFootballLoad = -1;
    private List<BasketMatchBean> mMatchdata = new ArrayList<>();//match的 内容(json)

    //筛选的数据
    public static List<BasketMatchFilter> mChickedFilter = new ArrayList<>();//选中的
    public static List<BasketMatchFilter> mAllFilter = new ArrayList<>();//所有的联赛
    private int mSize; //记录共有几天的数据
    //内容数据
    private List<BasketMatchBean> showDataList = new ArrayList<BasketMatchBean>();//显示的比赛list
    private boolean isBasketFilter = false;  //是否赛选过篮球

    /**
     * 篮球接口请求
     */
    private void initBasketData() {

        mFootballRecycler.setVisibility(View.GONE);
        mBasketRecycle.setVisibility(View.VISIBLE);

//        String url = "http://192.168.33.71:8080/mlottery/core/basketballMatch.findMultiScreenMatchList.do";//http://192.168.33.71:8080/mlottery/core/basketballMatch.findMultiScreenMatchList.do?lang=zh&timeZone=7

        VolleyContentFast.requestJsonByGet(BaseURLs.MULTIPLE_BASKET_LIST_URL, new VolleyContentFast.ResponseSuccessListener<BasketMultipleRoot>() {
            @Override
            public void onResponse(BasketMultipleRoot json) {


                if (getApplicationContext() == null) {
                    return;
                }

                isBasketLoad = 1;
                if (json == null || json.getMatchData() == null || json.getMatchData().size() == 0) {
                    setState(MULTIPLE_STATUS_NET_NO_DATA);
                    return;
                }
                mMatchdata = json.getMatchData();
                mAllFilter = json.getMatchFilter();

                showDataList.clear();
                /**
                 *判断是否 经过筛选
                 */
                if (isBasketFilter) { //已筛选

                    if (FiltrateCupsMap.basketImmedateCups.length == 0) {
                        List<BasketMatchFilter> noCheckedFilters = new ArrayList<>();
                        mChickedFilter = noCheckedFilters;//筛选0场后，再次进入赛选页面 显示已选中0场（全部不选中）

                        setState(MULTIPLE_STATUS_NET_NO_DATA);
                        return;
                    } else {
                        for (BasketMatchBean matchBean : mMatchdata) {
                            for (String checkedId : FiltrateCupsMap.basketImmedateCups) {
                                if (matchBean.getLeagueId().equals(checkedId)) {
                                    showDataList.add(matchBean);
                                }
                            }
                        }
                    }

                } else { //未筛选

                    for (BasketMatchBean lists : mMatchdata) {
                        showDataList.add(lists);
                    }
                    mChickedFilter = mAllFilter;//默认选中全部
                }

                L.d("yxq0117--AAAA-", "showDataList >>>> = " + showDataList.size());
                //* 判断是否有选中的比赛
                currentClick(BASKET_TYPE);

                if (mBasketAdapter == null) {
                    mBasketAdapter = new MultipleListBasketAdapter(mContext, showDataList);
                    mBasketAdapter.setmOnItemClickListener(new MultipleListBasketAdapter.MultipleRecyclerViewBasketItemClickListener() {
                        @Override
                        public void onItemClick(View view, String data, int pos, BasketMatchBean matchData) {

                            int datasize = byValue.size();
                            if (datasize == 0) {
                                MultipleByValueBean valueBean = new MultipleByValueBean();
                                valueBean.setType(BASKET_TYPE);
                                valueBean.setThirdId(data);
                                byValue.add(valueBean);

                                matchData.setBasketChicks(!matchData.isBasketChicks());
                                mBasketAdapter.notifyDataSetChanged();
                                L.d("yxq===012A ", "添加了对象 A " + valueBean.getThirdId());
                                L.d("yxq===012A ", "byValue.size() = " + byValue.size() + " ** ");
                            } else if (datasize < 3) {

                                boolean isSame = false;//记录所选id是否已选中
                                int indext = -1;//记录所选id在已选对象中的索引位置
                                for (int i = 0; i < byValue.size(); i++) {
                                    if (byValue.get(i).getThirdId().equals(data)) {
                                        isSame = true;
                                        indext = i;
                                        L.d("yxq===012B ", "删除对象 " + data);
                                        break;
                                    }
                                }
                                if (isSame) {
                                    if (indext != -1) {
                                        byValue.remove(indext);
                                    }
                                } else {
                                    MultipleByValueBean valueBeana = new MultipleByValueBean();
                                    valueBeana.setType(BASKET_TYPE);
                                    valueBeana.setThirdId(data);
                                    byValue.add(valueBeana);
                                    L.d("yxq===012B ", "添加了对象 B " + valueBeana.getThirdId());
                                }

                                matchData.setBasketChicks(!matchData.isBasketChicks());
                                mBasketAdapter.notifyDataSetChanged();
                                L.d("yxq===012B ", "byValue.size() = " + byValue.size() + " ** ");
                            } else if (datasize >= 3) {
                                boolean isRemove = true;
                                for (int i = 0; i < byValue.size(); i++) {
                                    if (byValue.get(i).getThirdId().equals(data)) {
                                        byValue.remove(i);
                                        isRemove = false;
                                        matchData.setBasketChicks(!matchData.isBasketChicks());
                                        mBasketAdapter.notifyDataSetChanged();
                                        break;
                                    }
                                }
                                if (isRemove) {
                                    Toast.makeText(mContext, getApplicationContext().getResources().getString(R.string.multi_toast_upper_match), Toast.LENGTH_SHORT).show();
                                }
                                L.d("yxq===012C ", "byValue.size() = " + byValue.size() + " ** ");
                            }
                        }
                    });
                    mBasketRecycle.setAdapter(mBasketAdapter);

                } else {
//                    mBasketAdapter.setData(showDataList);
                    mBasketAdapter.notifyDataSetChanged();
                }

                setState(MULTIPLE_STATUS_SUCCESS);

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                isBasketLoad = 0;
                setState(MULTIPLE_STATUS_NET_ERROR);
            }
        }, BasketMultipleRoot.class);
    }

    /**
     * 判断是否有选中的比赛
     */
    private void currentClick(int type) {
        if (type == BASKET_TYPE) {
            //默认不选中
            for (BasketMatchBean data : showDataList) {
                data.setBasketChicks(false);
            }
            if (byValue != null || byValue.size() != 0) {
                for (int i = 0; i < byValue.size(); i++) {
                    if (byValue.get(i).getType() == BASKET_TYPE) {
                        for (int j = 0; j < showDataList.size(); j++) {
                            if (showDataList.get(j).getThirdId().equals(byValue.get(i).getThirdId())) {
                                showDataList.get(j).setBasketChicks(true);//设为选中
                            }
                        }
                    }
                }
            }
        } else if (type == FOOTBALL_TYPE) {
            for (Match data : mMatchs) {
                data.setFootballChicks(false);
            }
            if (byValue != null || byValue.size() != 0) {
                for (int i = 0; i < byValue.size(); i++) {
                    if (byValue.get(i).getType() == FOOTBALL_TYPE) {
                        for (int j = 0; j < mMatchs.size(); j++) {
                            if (mMatchs.get(j).getThirdId().equals(byValue.get(i).getThirdId())) {
                                mMatchs.get(j).setFootballChicks(true);//设为选中
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 足球接口请求
     */
    private void initFootballData() {
        mFootballRecycler.setVisibility(View.VISIBLE);
        mBasketRecycle.setVisibility(View.GONE);

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_ImmediateMatchs, new VolleyContentFast.ResponseSuccessListener<ImmediateMatchs>() {
            @Override
            public synchronized void onResponse(final ImmediateMatchs matchs) {
                if (matchs == null || matchs.getImmediateMatch() == null) {
                    setState(MULTIPLE_STATUS_NET_NO_DATA);
                    return;
                }

                isFootballLoad = 1;
                // 获取所有赛程
                mAllMatchs = matchs.getImmediateMatch();
                mMatchs = new ArrayList<Match>();

                final String teamLogoPre = matchs.getTeamLogoPre();
                final String teamLogoSuff = matchs.getTeamLogoSuff();

                HotFocusUtils hotFocusUtils = new HotFocusUtils();
                hotFocusUtils.loadHotFocusData(getApplicationContext(), new RequestHostFocusCallBack() {

                    @Override
                    public void callBack(HotFocusLeagueCup hotFocusLeagueCup) {
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
                        //* 判断是否有选中的比赛
                        currentClick(FOOTBALL_TYPE);

                        if (mFootballAdapter == null) {
                            mFootballAdapter = new MultipleListFootballAdapter(mContext, mMatchs, teamLogoPre, teamLogoSuff);
                            mFootballAdapter.setmOnItemClickListener(new MultipleListFootballAdapter.MultipleRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, String data, int position, Match matchData) {

                                    int datasize = byValue.size();
                                    if (datasize == 0) {
                                        MultipleByValueBean valueBean = new MultipleByValueBean();
                                        valueBean.setType(FOOTBALL_TYPE);
                                        valueBean.setThirdId(data);
                                        byValue.add(valueBean);

                                        matchData.setFootballChicks(!matchData.isFootballChicks());
                                        mFootballAdapter.notifyDataSetChanged();
                                        L.d("yxq===011A ", "添加了对象 A " + valueBean.getThirdId());
                                        L.d("yxq===011A ", "byValue.size() = " + byValue.size() + " ** ");
                                    } else if (datasize < 3) {

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
                                        } else {
                                            MultipleByValueBean valueBeana = new MultipleByValueBean();
                                            valueBeana.setType(FOOTBALL_TYPE);
                                            valueBeana.setThirdId(data);
                                            byValue.add(valueBeana);
                                            L.d("yxq===011B ", "添加了对象 B " + valueBeana.getThirdId());
                                        }

                                        matchData.setFootballChicks(!matchData.isFootballChicks());

                                        mFootballAdapter.notifyDataSetChanged();
                                        L.d("yxq===011B ", "byValue.size() = " + byValue.size() + " ** ");
                                    } else if (datasize >= 3) {
                                        boolean isRemove = true;
                                        for (int i = 0; i < byValue.size(); i++) {
                                            if (byValue.get(i).getThirdId().equals(data)) {
                                                byValue.remove(i);
                                                isRemove = false;
                                                matchData.setFootballChicks(!matchData.isFootballChicks());
                                                mFootballAdapter.notifyDataSetChanged();
                                                break;
                                            }
                                        }
                                        if (isRemove) {
                                            Toast.makeText(mContext, getApplicationContext().getResources().getString(R.string.multi_toast_upper_match), Toast.LENGTH_SHORT).show();
                                        }
                                        L.d("yxq===011C ", "byValue.size() = " + byValue.size() + " ** ");
                                    }
                                }
                            });
                            mFootballRecycler.setAdapter(mFootballAdapter);
                        } else {
                            mFootballAdapter.updateDatas(mMatchs);
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
                isFootballLoad = 0;
                setState(MULTIPLE_STATUS_NET_ERROR);

            }
        }, ImmediateMatchs.class);
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
                    isBasketLoad = -1;
                    setState(MULTIPLE_STATUS_LOADING);
                    mHandler.postDelayed(mRun, 500);
                } else if (checkedId == mFootballRadioButton.getId()) {
                    borf = FOOTBALL_TYPE;
                    isFootballLoad = -1;
                    setState(MULTIPLE_STATUS_LOADING);
                    mHandler.postDelayed(mRun, 500);
                }
            }
        });
    }

    public static boolean isCheckedDefualt = false;// true为默认选中全部，但是在筛选页面不选中的

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                if (borf == FOOTBALL_TYPE) {

                    if (isFootballLoad == 0) { //加载失败
                        Intent intent = new Intent(getApplicationContext(), FiltrateMatchConfigActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(FiltrateMatchConfigActivity.NET_STATUS, false);
                        bundle.putInt("currentFragmentId", 1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (isFootballLoad == 1) { //加载成功
                        Intent intent = new Intent(getApplicationContext(), FiltrateMatchConfigActivity.class);
                        Bundle bundle = new Bundle();
                        LeagueCup[] allCups = mCups.toArray(new LeagueCup[]{});
                        bundle.putParcelableArray(FiltrateMatchConfigActivity.ALL_CUPS, allCups);// 传值到筛选页面的全部联赛，数据类型是LeagueCup[]
                        bundle.putParcelableArray(FiltrateMatchConfigActivity.CHECKED_CUPS,
                                mCheckedCups);// 传值到筛选页面的已经选择的联赛，数据类型是LeagueCup[]
                        bundle.putBoolean(FiltrateMatchConfigActivity.CHECKED_DEFUALT, isCheckedDefualt);// 是否默认选择
                        bundle.putBoolean(FiltrateMatchConfigActivity.NET_STATUS, true);
                        bundle.putInt("currentFragmentId", 1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, getResources().getText(R.string.basket_loading_txt), Toast.LENGTH_SHORT).show();
                    }
                } else if (borf == BASKET_TYPE) {
                    if (isBasketLoad == 1) {
                        Intent intent = new Intent(getApplicationContext(), BasketFiltrateActivity.class);
                        intent.putExtra("MatchAllFilterDatas", (Serializable) mAllFilter);//Serializable 序列化传值（所有联赛数据）
                        intent.putExtra("MatchChickedFilterDatas", (Serializable) mChickedFilter);//Serializable 序列化传值（选中的联赛数据）
                        intent.putExtra("currentfragment", 3);
                        startActivity(intent);
                        this.overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                    } else if (isBasketLoad == 0) {
                        Toast.makeText(mContext, getResources().getText(R.string.immediate_unconection), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, getResources().getText(R.string.basket_loading_txt), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.reloading_txt:
                //TODO===========
                if (borf == BASKET_TYPE) {
                    isBasketLoad = -1;
                }
                if (borf == FOOTBALL_TYPE) {
                    isFootballLoad = -1;
                }
                setState(MULTIPLE_STATUS_FOCUS_REFRESH_ONCLICK);
                mHandler.postDelayed(mRun, 500);
                break;
            case R.id.multi_ok:
                if (byValue == null || byValue.size() == 0) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.multi_toast_clicks_match), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MultiScreenViewingListActivity.this, MultiScreenViewActivity.class);
                    intent.putExtra("byValue", (ArrayList) byValue);
                    startActivity(intent);
                }
                break;
            case R.id.public_img_back:

                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
        }
    }

    /**
     * 足球筛选返回
     * 接受消息的页面实现
     */
    public void onEventMainThread(ScoresMatchFilterEventBusEntity scoresMatchFilterEventBusEntity) {
//        if (borf == FOOTBALL_TYPE) {

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
            //* 判断是否有选中的比赛
            currentClick(FOOTBALL_TYPE);

            updateFootballAdapter();
            isCheckedDefualt = (boolean) map.get(FiltrateMatchConfigActivity.CHECKED_DEFUALT);

            if (mMatchs.size() == 0) {// 没有比赛
                setState(MULTIPLE_STATUS_FOCUS_NO_DATA);
            } else {
                setState(MULTIPLE_STATUS_SUCCESS);
            }
        }
//        }
    }

    /**
     * 篮球筛选返回
     */
    public void onEventMainThread(MultipleBasketFilterListEvent multipleBasketFilterListEvent) {
//        if (borf == BASKET_TYPE) {

        Map<String, Object> map = multipleBasketFilterListEvent.getMap();

        L.d("yxq0117---", "**************&&&&&&&&****************");
        String[] checkedIds = (String[]) ((List) map.get(BasketFiltrateActivity.CHECKED_CUPS_IDS)).toArray(new String[]{});

        L.d("yxq0117AA------------checkedIds.length------", checkedIds.length + "");
        isBasketFilter = true;

        FiltrateCupsMap.basketImmedateCups = checkedIds;
        if (checkedIds.length == 0) {
            List<BasketMatchFilter> noCheckedFilters = new ArrayList<>();
            mChickedFilter = noCheckedFilters;//筛选0场后，再次进入赛选页面 显示已选中0场（全部不选中）

            setState(MULTIPLE_STATUS_FOCUS_NO_DATA);
        } else {

            L.d("yxq0117BB------------showDataList.size()------", showDataList.size() + "");
            showDataList.clear();
            for (BasketMatchBean matchBean : mMatchdata) {// 遍历所有数据 得到筛选后的
                boolean isExistId = false;
                for (String checkedId : checkedIds) {
                    if (matchBean.getLeagueId().equals(checkedId)) {
                        isExistId = true;

                        break;
                    }
                }
                if (isExistId) {
                    showDataList.add(matchBean);
                }
            }
            List<BasketMatchFilter> checkedFilters = new ArrayList<>();
            // 清除原来选中的(重新赋值选中的)
            for (BasketMatchFilter allFilter : mAllFilter) {
                for (String checkedId : checkedIds) {
                    if (allFilter.getLeagueId().equals(checkedId)) {
                        checkedFilters.add(allFilter);
                    }
                }
            }
            mChickedFilter = checkedFilters;
            L.d("yxq0117CC---", "showDataList >>>> = " + showDataList.size()); //showDataList
            //* 判断是否有选中的比赛
            currentClick(BASKET_TYPE);
            updateBasketAdapter();
            setState(MULTIPLE_STATUS_SUCCESS);
        }
//        }
    }

    /**
     * 多屏内页返回
     */
    public void onEventMainThread(List<MultipleByValueBean> value) {

//        Toast.makeText(mContext, "多屏内页返回 == " + value.size(), Toast.LENGTH_SHORT).show();
        byValue.clear();
        byValue = value;
        currentClick(borf);
        if (borf == BASKET_TYPE) {
            updateBasketAdapter();
        } else if (borf == FOOTBALL_TYPE) {
            updateFootballAdapter();
        }
    }

    private void updateFootballAdapter() {
        if (mFootballAdapter == null) {
            return;
        }
        mFootballAdapter.updateDatas(mMatchs);
        mFootballAdapter.notifyDataSetChanged();
    }

    private void updateBasketAdapter() {
        if (mBasketAdapter == null) {
            return;
        }
//        mBasketAdapter.setData(showDataList);
        mBasketAdapter.notifyDataSetChanged();
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
