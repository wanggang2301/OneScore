package com.hhly.mlottery.mvp.bettingmvp.mvpview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BaseActivity;
import com.hhly.mlottery.activity.BettingRecommendSettingActivity;
import com.hhly.mlottery.bean.bettingbean.BettingListDataBean;
import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.mvp.bettingmvp.MView;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.BettingSettingResultEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.mvppresenter.MvpBettingRecommendPresenter;
import com.hhly.mlottery.adapter.bettingadapter.BettingRecommendMvpAdapter;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.SignUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by：XQyi on 2017/5/2 10:56
 * Use:竞彩推荐列表（MVP-View 页面展示）
 */
public class MvpBettingRecommendActivity extends BaseActivity implements MView<BettingListDataBean>, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private MvpBettingRecommendPresenter mvpBettingRecommendPresenter;
    private BettingRecommendMvpAdapter mAdapter;
    private TextView mTitlt;
    private ImageView mBack;
    private ImageView mSetting;
    private ExactSwipeRefreshLayout mRefresh;
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLinearLayoutManager;
    private List<BettingListDataBean.PromotionData.BettingListData>  listData = new ArrayList<>();
    private LinearLayout mErrorLayout;
    private TextView mRefreshTxt;
    private LinearLayout mLoadingLayout;
    private TextView mNoDataLayout;

    List<BettingListDataBean.LeagueNameData> allLeague;// 所有的联赛
    List<BettingListDataBean.LeagueNameData> currLeague;//选中的联赛
    boolean filtrate = false;//是否筛选过联赛

    private View mOnLoadingView;
    private View mNoLoadingView;

    private int PAGE_SIZE=10; //每页的最大数量
    private String playType = "-1";//选中的玩法（-1 全部）
    private List<String> leagueKey = new ArrayList<>();//选中的联赛
    private String leagueKeys = "";//选中联赛的key -string

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting_recommend);

        EventBus.getDefault().register(this);
        mContext = this;
        mvpBettingRecommendPresenter = new MvpBettingRecommendPresenter(this);
        initView();
//        initData();
        setStatus(SHOW_STATUS_LOADING);
        mLoadHandler.postDelayed(mRun, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    Handler mLoadHandler = new Handler();
    /**
     * 子线程 处理数据加载
     */
    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            initData(playType , leagueKeys);
        }
    };
    /**
     * 设置显示状态
     *
     * @param status
     */
    //显示状态
    private static final int SHOW_STATUS_LOADING = 1;//加载中
    private static final int SHOW_STATUS_ERROR = 2;//加载失败
    private static final int SHOW_STATUS_NO_DATA = 3;//暂无数据
    private static final int SHOW_STATUS_SUCCESS = 4;//加载成功
    private final static int SHOW_STATUS_REFRESH_ONCLICK = 5;//点击刷新

    private void setStatus(int status) {

        if (status == SHOW_STATUS_LOADING) {
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(true);
        } else if (status == SHOW_STATUS_SUCCESS) {
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(false);
        } else if (status == SHOW_STATUS_REFRESH_ONCLICK) {
            mRefresh.setVisibility(View.GONE);
            mRefresh.setRefreshing(true);
        } else {
            mRefresh.setVisibility(View.GONE);
            mRefresh.setRefreshing(false);
        }
        mLoadingLayout.setVisibility((status == SHOW_STATUS_REFRESH_ONCLICK) ? View.VISIBLE : View.GONE);
        mErrorLayout.setVisibility(status == SHOW_STATUS_ERROR ? View.VISIBLE : View.GONE);
        mNoDataLayout.setVisibility(status == SHOW_STATUS_NO_DATA ? View.VISIBLE : View.GONE);
    }

    private void initView(){
        mTitlt = (TextView) findViewById(R.id.public_txt_title);
        mTitlt.setText(mContext.getResources().getText(R.string.betting_title_list));
        findViewById(R.id.public_btn_filter).setVisibility(View.INVISIBLE);//隐藏筛选

        mBack = (ImageView)findViewById(R.id.public_img_back);
        mBack.setOnClickListener(this);
        mSetting = (ImageView)findViewById(R.id.public_btn_set);
        mSetting.setOnClickListener(this);

        //下拉控件
        mRefresh = (ExactSwipeRefreshLayout) findViewById(R.id.betting_refresh_layout);
        mRefresh.setColorSchemeResources(R.color.bg_header);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getApplicationContext(), StaticValues.REFRASH_OFFSET_END));

        mRecycleView = (RecyclerView)findViewById(R.id.betting_recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(mLinearLayoutManager);

        mOnLoadingView = getLayoutInflater().inflate(R.layout.onloading, (ViewGroup) mRecycleView.getParent(),false);
        mNoLoadingView = getLayoutInflater().inflate(R.layout.nomoredata, (ViewGroup) mRecycleView.getParent(),false);

        //异常状态
        //网络不给力
        mErrorLayout = (LinearLayout) findViewById(R.id.error_layout);
        //刷新
        mRefreshTxt = (TextView) findViewById(R.id.reloading_txt);
        mRefreshTxt.setOnClickListener(this);
        //加载中
        mLoadingLayout = (LinearLayout) findViewById(R.id.custom_loading_ll);
        //暂无数据
        mNoDataLayout = (TextView) findViewById(R.id.nodata_txt);

    }

    private void initData(String type , String key){
        String url = "http://192.168.10.242:8092/promotion/info/list";
        String userid = AppConstants.register.getData().getUser().getUserId();
        Map<String ,String> mapPrament = new HashMap<>();

        mapPrament.put("pageSize" , "10"); //每页条数
        mapPrament.put("pageNo" , "1");//页码
        mapPrament.put("userId" , userid);//用户id
        mapPrament.put("key" , key);//联赛key
        mapPrament.put("type" , type);
        mapPrament.put("lang" , "zh");
        mapPrament.put("timeZone" , "8");
//        mapPrament.put("lang" , "zh");
//        mapPrament.put("timeZone" , "8");
        String signs = SignUtils.getSign("/promotion/info/list" , mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put("pageSize" , "10"); //每页条数
        map.put("pageNo" , "1");//页码
        map.put("userId" , userid);//用户id
        map.put("key" , key);//联赛key
        map.put("type" , type);
        map.put("sign" , signs);

        L.d("qwer== >> " + signs);

        mvpBettingRecommendPresenter.loadData(url , map);
    }

    /**
     * 加载更多
     */
    private int currentPage = 1;
    private void loadMoreData(){
        L.d("上拉加载、、、");
        String url = "http://192.168.10.242:8092/promotion/info/list";
        Map<String ,String> map = new HashMap<>();
        String userid = AppConstants.register.getData().getUser().getUserId();
        String sign = AppConstants.SIGN_KEY;
        map.put("pageSize" , "10"); //每页条数
        map.put("pageNo" , "1");//页码
        map.put("userId" , userid);//用户id
        map.put("key" , "");//联赛key
        map.put("sign" , sign);
        map.put("type" , "-1");

        currentPage = ++currentPage;
        VolleyContentFast.requestJsonByGet(url,map , new VolleyContentFast.ResponseSuccessListener<BettingListDataBean>() {
            @Override
            public void onResponse(BettingListDataBean jsonBean) {
                if (jsonBean.getCode() == 200) {
                    if (jsonBean.getPromotionList() != null) {

                        if (jsonBean.getPromotionList().getList().size() == 0) {

                            mAdapter.notifyDataChangedAfterLoadMore(false);
                            mAdapter.addFooterView(mNoLoadingView);

                        }else{
                            mAdapter.notifyDataChangedAfterLoadMore(jsonBean.getPromotionList().getList(),true);
                        }
                    }
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        },BettingListDataBean.class);

    }

    public void upDataAdapter(){
        if (mAdapter == null) {
            return;
        }
        mAdapter.updateData(listData);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void loadSuccessView(BettingListDataBean beanData) {

        if (beanData.getPromotionList() == null || beanData.getPromotionList().getList().size() == 0) {
            setStatus(SHOW_STATUS_NO_DATA);
            return;
        }

        //所有的联赛
        allLeague = new ArrayList<>();
        //当前的联赛
        currLeague  = new ArrayList<>();


        allLeague = beanData.getLeagueNames();//固定

        if (filtrate) {
            //这里过滤筛选联赛
            for (BettingListDataBean.LeagueNameData leagueCheck : allLeague) {
                if (leagueKey.contains(leagueCheck.getKey())) {
                    currLeague.add(leagueCheck);
                }
            }
        }else{
            //未做过筛选 默认选中所有的
            playType = "-1"; //(-1 选中全部)
            currLeague = beanData.getLeagueNames();//选中的联赛
        }

        setStatus(SHOW_STATUS_SUCCESS);
        listData.clear();
        for (BettingListDataBean.PromotionData.BettingListData data : beanData.getPromotionList().getList()) {
            listData.add(data);
        }

        buyClicked();
        specialistClick();
        gameDetailsClick();

        L.d("listData >> " + listData.size());
        if (mAdapter == null) {
            mAdapter = new BettingRecommendMvpAdapter(mContext , listData);
            mRecycleView.setAdapter(mAdapter);
            mAdapter.setmBuyClick(mBettingBuyClickListener);
            mAdapter.setmSpecialistClick(mBettingSpecialistClickListener);
            mAdapter.setmGameDetailsClick(mBettingGameDetailsClickListener);
        }else{
            upDataAdapter();
        }
    }

    @Override
    public void loadFailView() {
        setStatus(SHOW_STATUS_ERROR);
        Toast.makeText(mContext, "网络请求失败~！！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadNoData() {
        setStatus(SHOW_STATUS_NO_DATA);
        Toast.makeText(mContext, "暂无数据~！！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reloading_txt:
                setStatus(SHOW_STATUS_REFRESH_ONCLICK);
                mLoadHandler.postDelayed(mRun, 0);
                break;
            case R.id.public_img_back:
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.public_btn_set:
                //TODO=====================
                Intent mIntent = new Intent(mContext , BettingRecommendSettingActivity.class);
                mIntent.putExtra(ConstantPool.ALL_LEAGUE, (Serializable)allLeague);//所有的
                mIntent.putExtra(ConstantPool.CURR_LEAGUE , (Serializable)currLeague);//选中的
                mIntent.putExtra(ConstantPool.CURR_PALY_TYPE , playType);//选中的
                startActivity(mIntent);
                overridePendingTransition(R.anim.push_left_in , R.anim.push_fix_out);
                break;
        }
    }

    @Override
    public void onRefresh() {
        //TODO=====================
        setStatus(SHOW_STATUS_LOADING);
        mLoadHandler.postDelayed(mRun, 0);
    }

    private BettingBuyClickListener mBettingBuyClickListener;
    // 购买(查看)的点击监听
    public interface BettingBuyClickListener {
        void BuyOnClick(View view , String s);
    }

    private BettingSpecialistClickListener mBettingSpecialistClickListener;
    // 专家详情的点击监听
    public interface BettingSpecialistClickListener {
        void SpecialistOnClick(View view , String s);
    }

    private BettingGameDetailsClickListener mBettingGameDetailsClickListener;
    // 比赛内页点击监听
    public interface BettingGameDetailsClickListener{
        void GameDetailsOnClick(View view , String s);
    }
    /**
     * 购买(查看)的点击事件
     */
    public void buyClicked(){
        mBettingBuyClickListener = new BettingBuyClickListener() {
            @Override
            public void BuyOnClick(View view, String s) {
//                Toast.makeText(mContext, "点击了购买** " + s, Toast.LENGTH_SHORT).show();
                L.d("yxq-0418=== " , "点击了*购买** " + s);
                Intent mIntent = new Intent(mContext , MvpBettingPayDetailsActivity.class);
                startActivity(mIntent);
                overridePendingTransition(R.anim.push_left_in , R.anim.push_fix_out);
            }
        };
    }
    /**
     * 专家详情点击事件
     */
    public void specialistClick(){
        mBettingSpecialistClickListener = new BettingSpecialistClickListener() {
            @Override
            public void SpecialistOnClick(View view, String s) {
//                Toast.makeText(mContext, "专家** " + s, Toast.LENGTH_SHORT).show();
                L.d("yxq-0418=== " , "点击了*专家** " + s);
            }
        };
    }
    /**
     * 比赛内页点击事件
     */
    public void gameDetailsClick(){
        mBettingGameDetailsClickListener = new BettingGameDetailsClickListener() {
            @Override
            public void GameDetailsOnClick(View view, String s) {
//                Toast.makeText(mContext, "内页跳转** " + s, Toast.LENGTH_SHORT).show();
                L.d("yxq-0418=== " , "点击了*内页跳转** " + s);
            }
        };
    }

    private void setListener(){
        //上拉加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreData();
                    }
                },1000);
            }
        });
    }

    //设置返回
    public void onEventMainThread(BettingSettingResultEventBusEntity resultEventBusEntity){
        L.d("resultEventBusEntity.size = " + resultEventBusEntity.getCurrPaly().size() +"---"+ resultEventBusEntity.getCurrPaly().get(0));
        L.d("resultEventBusEntity.size = " + resultEventBusEntity.getKeyChecked().size() +"---" + resultEventBusEntity.getKeyChecked().get(0));
        filtrate = true;

        StringBuffer sb = new StringBuffer();
        for (String key : resultEventBusEntity.getKeyChecked()) {
            sb.append(key + ",");
        }
        String keys = sb.toString();
        L.d("qwer==>> " , keys);
        keys = keys.substring(0 , keys.length()-1);
        L.d("qwer==>> " , keys);


        // 选中玩法
        playType = resultEventBusEntity.getCurrPaly().get(0);
        //选中联赛字符串（做为请求参数）
        leagueKeys = keys;

        //选中的联赛集合
        leagueKey.clear();
        leagueKey.addAll(resultEventBusEntity.getKeyChecked());

        setStatus(SHOW_STATUS_LOADING);
        listData.clear();
        initData(playType , leagueKeys);

    }
}
