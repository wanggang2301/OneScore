package com.hhly.mlottery.mvp.bettingmvp.mvpview;

import android.app.Activity;
import android.content.Context;
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
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BettingRecommendSettingActivity;
import com.hhly.mlottery.activity.LoginActivity;
import com.hhly.mlottery.activity.RecommendedExpertDetailsActivity;
import com.hhly.mlottery.bean.bettingbean.BettingListDataBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.mvp.bettingmvp.MView;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.BettingDetailsResuleEventBusEntity;
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
public class MvpBettingRecommendActivity extends Activity implements MView<BettingListDataBean>, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    /** 签名参数 */
    private String PARAM_PAGE_SIZE = "pageSize";//每页条数
    private String PARAM_PAGE_NO = "pageNo";//页码
    private String PARAM_USER_ID = "userId";//用户id
    private String PARAM_KEY = "key";//联赛key
    private String PARAM_TYPE = "type";//玩法type
    private String PARAM_LANG = "lang";
    private String PARAM_TIMEZONE = "timeZone";
    private String PARAM_SIGN = "sign";//参数签名

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
    private Context mContext;

    List<BettingListDataBean.LeagueNameData> allLeague;// 所有的联赛
    List<BettingListDataBean.LeagueNameData> currLeague;//选中的联赛
    boolean filtrate = false;//是否筛选过联赛



    private int PAGE_SIZE=10; //每页的最大数量
    private String playType = "-1";//选中的玩法（-1 全部）
    private List<String> leagueKey = new ArrayList<>();//选中的联赛
    private String leagueKeys = "";//选中联赛的key -string

    private int pageNum = 1;//页码 (初次请求第一页)
    private int pageSize = 10;//每页条数（分页加载每页条数）
    private boolean hasNextPage;//是否有下一页
    private View mOnloadingView;
    private View mNoLoadingView;
    /**
     * 设置是否可点击
     */
    private boolean toSetting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting_recommend);

        EventBus.getDefault().register(this);
        mContext = this;
        mvpBettingRecommendPresenter = new MvpBettingRecommendPresenter(this);
        initView();
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
            pageNum = 1; //重新加载，当前页复位
            initData(playType , leagueKeys , pageNum , pageSize);
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

        mOnloadingView = getLayoutInflater().inflate(R.layout.onloading, (ViewGroup) mRecycleView.getParent(),false);
        mNoLoadingView=getLayoutInflater().inflate(R.layout.nomoredata, (ViewGroup) mRecycleView.getParent(),false);

    }

    private void initData(String type , String key , int pageNum , int pageSize){

//        String url = "http://192.168.10.242:8098/promotion/info/list";
//        String url = "http://m.1332255.com:81/promotion/info/list";
        String url = BaseURLs.URL_RECOMEND_LIST;

        String userid = AppConstants.register.getUser() == null ? "" : AppConstants.register.getUser().getUserId();
        Map<String ,String> mapPrament = new HashMap<>();

        mapPrament.put(PARAM_PAGE_SIZE , pageSize +"");
        mapPrament.put(PARAM_PAGE_NO , pageNum + "");
        mapPrament.put(PARAM_USER_ID , userid);
        mapPrament.put(PARAM_KEY , key);
        mapPrament.put(PARAM_TYPE , type);
        mapPrament.put(PARAM_LANG , MyApp.getLanguage());
        mapPrament.put(PARAM_TIMEZONE , AppConstants.timeZone + "");
        String signs = SignUtils.getSign(BaseURLs.PARAMENT_RECOMMEND_LIST, mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put(PARAM_PAGE_SIZE , pageSize +""); //每页条数
        map.put(PARAM_PAGE_NO , pageNum + "");//页码
        map.put(PARAM_USER_ID , userid);//用户id
        map.put(PARAM_KEY , key);//联赛key
        map.put(PARAM_TYPE , type);
        map.put(PARAM_SIGN , signs);

        L.d("qwer== >> " + signs);

        mvpBettingRecommendPresenter.loadData(url , map);
    }


    public void upDataAdapter(){
        if (mAdapter == null) {
            return;
        }
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void loadSuccessView(BettingListDataBean beanData) {

        if (beanData.getPromotionList() == null || beanData.getPromotionList().getList().size() == 0) {
            setStatus(SHOW_STATUS_NO_DATA);
            return;
        }

        toSetting = true; //设置是否可点击

        hasNextPage = beanData.getPromotionList().isHasNextPage();

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
        mAdapter = new BettingRecommendMvpAdapter(mContext , listData);

        mAdapter.setLoadingView(mOnloadingView);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.openLoadMore(0 , true);

        setListener();
        mAdapter.setmBuyClick(mBettingBuyClickListener);
        mAdapter.setmGameDetailsClick(mBettingGameDetailsClickListener);
        mAdapter.setmSpecialistClick(mBettingSpecialistClickListener);
//        if (mAdapter == null) {
//            mAdapter = new BettingRecommendMvpAdapter(mContext , listData);
//
//            mAdapter.setLoadingView(mOnloadingView);
//            mRecycleView.setAdapter(mAdapter);
//            mAdapter.openLoadMore(0 , true);
//
//            setListener();
//            mAdapter.setmBuyClick(mBettingBuyClickListener);
//            mAdapter.setmGameDetailsClick(mBettingGameDetailsClickListener);
//            mAdapter.setmSpecialistClick(mBettingSpecialistClickListener);
//        }else{
//            upDataAdapter();
//        }
    }

    @Override
    public void loadFailView() {
        setStatus(SHOW_STATUS_ERROR);
//        toSetting = false;
//        Toast.makeText(mContext, "网络请求失败~！！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadNoData() {
        setStatus(SHOW_STATUS_NO_DATA);
        toSetting = false;
//        Toast.makeText(mContext, "暂无数据~！！", Toast.LENGTH_SHORT).show();
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
//                startActivity(new Intent(mContext , MvpChargeMoneyActivity.class));
                break;
            case R.id.public_btn_set:
                if (toSetting) {
                    Intent mIntent = new Intent(mContext , BettingRecommendSettingActivity.class);
                    mIntent.putExtra(ConstantPool.ALL_LEAGUE, (Serializable)allLeague);//所有的
                    mIntent.putExtra(ConstantPool.CURR_LEAGUE , (Serializable)currLeague);//选中的
                    mIntent.putExtra(ConstantPool.CURR_PALY_TYPE , playType);//选中的
                    startActivity(mIntent);
                    overridePendingTransition(R.anim.push_left_in , R.anim.push_fix_out);
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        setStatus(SHOW_STATUS_LOADING);
        mLoadHandler.postDelayed(mRun, 0);
    }

    private BettingBuyClickListener mBettingBuyClickListener;
    // 购买(查看)的点击监听
    public interface BettingBuyClickListener {
        void BuyOnClick(View view , BettingListDataBean.PromotionData.BettingListData s);
    }

    private BettingSpecialistClickListener mBettingSpecialistClickListener;
    // 专家详情的点击监听
    public interface BettingSpecialistClickListener {
        void SpecialistOnClick(View view , BettingListDataBean.PromotionData.BettingListData s);
    }

    private BettingGameDetailsClickListener mBettingGameDetailsClickListener;
    // 比赛内页点击监听
    public interface BettingGameDetailsClickListener{
        void GameDetailsOnClick(View view , BettingListDataBean.PromotionData.BettingListData s);
    }
    /**
     * 购买(查看)的点击事件
     */
    public void buyClicked(){
        mBettingBuyClickListener = new BettingBuyClickListener() {
            @Override
            public void BuyOnClick(View view, BettingListDataBean.PromotionData.BettingListData listData) {
                Intent mIntent = new Intent(mContext , MvpBettingPayDetailsActivity.class);
                mIntent.putExtra(ConstantPool.TO_DETAILS_PROMOTION_ID , listData.getId());//推介id

                mIntent.putExtra("expertId",listData.getUserid());
                mIntent.putExtra("winPoint",listData.getWinPoint());
                mIntent.putExtra("errPoint",listData.getErrPoint());

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
            public void SpecialistOnClick(View view, BettingListDataBean.PromotionData.BettingListData s) {
//                Toast.makeText(mContext, "专家** " + s, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(mContext,RecommendedExpertDetailsActivity.class);
                intent.putExtra("expertId",s.getUserid());
                intent.putExtra("winPoint",s.getWinPoint());
                intent.putExtra("errPoint",s.getErrPoint());
                startActivity(intent);
               // L.d("yxq-0418=== " , "点击了*专家** " + s);
            }
        };
    }
    /**
     * 比赛内页点击事件
     */
    public void gameDetailsClick(){
        mBettingGameDetailsClickListener = new BettingGameDetailsClickListener() {
            @Override
            public void GameDetailsOnClick(View view, BettingListDataBean.PromotionData.BettingListData s) {
                Intent mIntent = new Intent(mContext , MvpBettingPayDetailsActivity.class);
                mIntent.putExtra(ConstantPool.TO_DETAILS_PROMOTION_ID , s.getId());//推介id

                mIntent.putExtra("expertId",s.getUserid());
                mIntent.putExtra("winPoint",s.getWinPoint());
                mIntent.putExtra("errPoint",s.getErrPoint());

                startActivity(mIntent);
                overridePendingTransition(R.anim.push_left_in , R.anim.push_fix_out);
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
                        if (hasNextPage) {
                            loadMoreData(playType , leagueKeys);
                        }else{
                            Toast.makeText(mContext, mContext.getResources().getText(R.string.nodata_txt), Toast.LENGTH_SHORT).show();
//                            mOnloadingView.findViewById(R.id.loading_text)
                            mAdapter.addFooterView(mNoLoadingView);
                        }
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
        mLoadHandler.postDelayed(mRun, 0);

    }

    /**
     * 详情页面返回
     * @param detailsResuleEventBusEntity
     */
    public void onEventMainThread(BettingDetailsResuleEventBusEntity detailsResuleEventBusEntity){

        for (BettingListDataBean.PromotionData.BettingListData currlist : listData) {
            if (currlist.getId().equals(detailsResuleEventBusEntity.getCurrentId())) {
                currlist.setLookStatus("**");
                break;
            }
        }
        upDataAdapter();
    }

    /**
     * 加载更多
     */
    private void loadMoreData(String type , String key){
        pageNum += 1;
        L.d("上拉加载、、、" + pageNum);
//        String url = "http://192.168.10.242:8092/promotion/info/list";
//        String url = "http://m.1332255.com:81/promotion/info/list";
        String url = BaseURLs.URL_RECOMEND_LIST;
        String userid = AppConstants.register.getUser() == null ? "" : AppConstants.register.getUser().getUserId();
        Map<String ,String> mapPrament = new HashMap<>();

        mapPrament.put(PARAM_PAGE_SIZE , pageSize +"");
        mapPrament.put(PARAM_PAGE_NO , pageNum + "");
        mapPrament.put(PARAM_USER_ID , userid);
        mapPrament.put(PARAM_KEY , key);
        mapPrament.put(PARAM_TYPE , type);
        mapPrament.put(PARAM_LANG , MyApp.getLanguage());
        mapPrament.put(PARAM_TIMEZONE , AppConstants.timeZone + "");
//        mapPrament.put("pageSize" , pageSize +""); //每页条数
//        mapPrament.put("pageNo" , pageNum + "");//页码
//        mapPrament.put("userId" , userid);//用户id
//        mapPrament.put("key" , key);//联赛key
//        mapPrament.put("type" , type);
//        mapPrament.put("lang" , MyApp.getLanguage());
//        mapPrament.put("timeZone" , AppConstants.timeZone + "");
        String signs = SignUtils.getSign(BaseURLs.PARAMENT_RECOMMEND_LIST, mapPrament);

        Map<String ,String> map = new HashMap<>();
//        map.put("pageSize" , pageSize +""); //每页条数
//        map.put("pageNo" , pageNum + "");//页码
//        map.put("userId" , userid);//用户id
//        map.put("key" , key);//联赛key
//        map.put("type" , type);
//        map.put("sign" , signs);
        map.put(PARAM_PAGE_SIZE , pageSize +""); //每页条数
        map.put(PARAM_PAGE_NO , pageNum + "");//页码
        map.put(PARAM_USER_ID , userid);//用户id
        map.put(PARAM_KEY , key);//联赛key
        map.put(PARAM_TYPE , type);
        map.put(PARAM_SIGN , signs);

        L.d("qwer== >> " + signs);

        VolleyContentFast.requestJsonByGet(url,map , new VolleyContentFast.ResponseSuccessListener<BettingListDataBean>() {
            @Override
            public void onResponse(BettingListDataBean jsonBean) {
                if (jsonBean.getCode() == 200) {

                    if (jsonBean.getPromotionList() != null) {

                        hasNextPage = jsonBean.getPromotionList().isHasNextPage();

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
}
