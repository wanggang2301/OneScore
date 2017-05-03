package com.hhly.mlottery.activity.BettingMvp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BaseActivity;
import com.hhly.mlottery.activity.BettingPayDetailsActivity;
import com.hhly.mlottery.activity.BettingRecommendSettingActivity;
import com.hhly.mlottery.adapter.bettingadapter.BettingRecommendMvpAdapter;
import com.hhly.mlottery.bean.basket.BasketMatchBean;
import com.hhly.mlottery.bean.basket.BasketNewRootBean;
import com.hhly.mlottery.bean.basket.BasketRootBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by：XQyi on 2017/5/2 10:56
 * Use:
 */
public class MvpBettingRecommendActivity extends BaseActivity implements MView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    private MvpBettingRecommendPresenter mvpBettingRecommendPresenter;
    private List<String> list;
    private BettingRecommendMvpAdapter mAdapter;
    private TextView mTitlt;
    private ImageView mBack;
    private ImageView mSetting;
    private ExactSwipeRefreshLayout mRefresh;
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting_recommend);

        mContext = this;
        mvpBettingRecommendPresenter = new MvpBettingRecommendPresenter(this);
        initView();
        initData();
    }

    private void initView(){
        mTitlt = (TextView) findViewById(R.id.public_txt_title);
        mTitlt.setText("竞彩推荐");
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
    }

    private void initData(){
        mvpBettingRecommendPresenter.loadData(BaseURLs.URL_BASKET_NEW_IMMEDIATE);
    }

    public void upDataAdapter(){
        if (mAdapter == null) {
            return;
        }
        mAdapter.updateData(list);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void loadSuccessView(BasketNewRootBean beanData) {

        List<BasketRootBean> data = beanData.getMatchData();

        List<BasketMatchBean> matchBeen = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            for (BasketMatchBean d : data.get(i).getMatch()) {
                matchBeen.add(d);
            }
        }

        list = new ArrayList();
//        for (int i = 0; i < 10; i++) {
//            list.add("专家 " + i + " 号");
//        }
        for (int i = 0; i < matchBeen.size(); i++) {

            list.add("专家 " + matchBeen.get(i).getGuestTeam() + " 号");
        }

        buyClicked();
        specialistClick();
        gameDetailsClick();
        if (mAdapter == null) {

            mAdapter = new BettingRecommendMvpAdapter(mContext , list);
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
        Toast.makeText(mContext, "网络请求失败~！！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadNoData() {
        Toast.makeText(mContext, "暂无数据~！！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back:
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.public_btn_set:
                //TODO=====================
                Intent mIntent = new Intent(mContext , BettingRecommendSettingActivity.class);
                startActivity(mIntent);
                overridePendingTransition(R.anim.push_left_in , R.anim.push_fix_out);
                break;
        }
    }

    @Override
    public void onRefresh() {
        //TODO=====================
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
                Intent mIntent = new Intent(mContext , BettingPayDetailsActivity.class);
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
}
