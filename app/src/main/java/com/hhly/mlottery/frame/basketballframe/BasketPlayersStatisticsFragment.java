package com.hhly.mlottery.frame.basketballframe;


import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.adapter.basketball.BasketPlayerNameAdapter;
import com.hhly.mlottery.adapter.basketball.BasketPlayerStatisticsAdapter;
import com.hhly.mlottery.bean.basket.basketstatistics.BasketPlayerStatisticsBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.net.VolleyContentFast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 球员统计fragmemt
 */
public class BasketPlayersStatisticsFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.recycler_basket_player)
    RecyclerView mRecyclerView;
    @BindView(R.id.recycler_basket_player_name)
    RecyclerView mRecyclerViewPlayer;
    @BindView(R.id.basket_player_guest_team_icon)
    ImageView mGuestIcon;
    @BindView(R.id.basket_player_home_team_icon)
    ImageView mHomeIcon;
    @BindView(R.id.basket_player_guest_team_name)
    TextView  mGuestName;
    @BindView(R.id.basket_player_home_team_name)
    TextView  mHomeName;
    @BindView(R.id.rl_guest_team)
    RelativeLayout mRlGuest;
    @BindView(R.id.rl_home_team)
    RelativeLayout mRlHome;

    /**主客队选中*/
    private boolean isCheckedGuest=true;
    BasketPlayerStatisticsBean mData;

    /**
     * 异常界面
     */
    @BindView(R.id.basket_odds_net_error)
     LinearLayout mExceptionLayout;
    /**
     * 无数据的界面
     */
    @BindView(R.id.odds_nodata_container)
    RelativeLayout mNodataLayout;
    /**
     * 点击刷新
     */
    @BindView(R.id.network_exception_reload_btn)
     TextView mBtnRefresh;
    /**
     * 加载中
     */
    @BindView(R.id.basket_player_progressbar)
     FrameLayout mProgressBarLayout;
    /**
     * 数据界面
     */
    @BindView(R.id.ll_basket_player_data)
    LinearLayout mDataLayout;



    BasketPlayerStatisticsAdapter mAdapter;
    List<BasketPlayerStatisticsBean.PlayerStatsEntity> list=new ArrayList<>();

    View mView;
    LinearLayoutManager mLinearLayoutManager;

    private final static int VIEW_STATUS_LOADING = 1;
    private final static int VIEW_STATUS_SUCCESS = 2;
    private final static int VIEW_STATUS_NET_ERROR = 3;
    private final static int VIEW_STATUS_NO_DATA=4;

    /**队名数据*/
    List<String> mNames=new ArrayList<>();
    BasketPlayerNameAdapter mNameAdapter;


    public static BasketPlayersStatisticsFragment newInstance() {
        BasketPlayersStatisticsFragment basketPlayersStatisticsFragment = new BasketPlayersStatisticsFragment();
        return basketPlayersStatisticsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_basket_players_statistics, container, false);
        ButterKnife.bind(this,mView);
        initView();
        setListener();
        initData();
        return mView ;
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case VIEW_STATUS_LOADING:
                    mExceptionLayout.setVisibility(View.GONE);
                    mProgressBarLayout.setVisibility(View.VISIBLE);
                    mNodataLayout.setVisibility(View.GONE);
                    mDataLayout.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_SUCCESS:
                    mProgressBarLayout.setVisibility(View.GONE);
                    mExceptionLayout.setVisibility(View.GONE);
                    mDataLayout.setVisibility(View.VISIBLE);
                    break;
                case VIEW_STATUS_NET_ERROR:
                    mProgressBarLayout.setVisibility(View.GONE);
                    mNodataLayout.setVisibility(View.GONE);
                    mExceptionLayout.setVisibility(View.VISIBLE);
                    mDataLayout.setVisibility(View.GONE);
                    break;
                    // 无数据界面
                case VIEW_STATUS_NO_DATA:
                    mNodataLayout.setVisibility(View.VISIBLE);
                    mDataLayout.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void initView() {
        mProgressBarLayout.setVisibility(View.GONE);
        mNodataLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);

        mAdapter=new BasketPlayerStatisticsAdapter(list);

        mLinearLayoutManager=new LinearLayoutManager(getContext());
        StaggeredGridLayoutManager mStaggerLayoutManager=new StaggeredGridLayoutManager(11,StaggeredGridLayoutManager.HORIZONTAL);
        mRecyclerView.setNestedScrollingEnabled(true);

        mStaggerLayoutManager.canScrollVertically();
        mStaggerLayoutManager.canScrollHorizontally();

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(true);

        mRecyclerView.setAdapter(mAdapter);

        mNameAdapter=new BasketPlayerNameAdapter(mNames);
        mRecyclerViewPlayer.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewPlayer.setAdapter(mNameAdapter);


    }

    /**
     * 设置监听事件
     */
    private void setListener() {
//        mGuestName.setOnClickListener(this);
//        mHomeName.setOnClickListener(this);
        mBtnRefresh.setOnClickListener(this);
        mRlGuest.setOnClickListener(this);
        mRlHome.setOnClickListener(this);

    }

    private void initData() {

        handler.sendEmptyMessage(VIEW_STATUS_LOADING);

        Map<String,String> params=new HashMap<>();
        params.put("thirdId", BasketDetailsActivityTest.mThirdId);
        VolleyContentFast.requestJsonByGet(BaseURLs.BASKET_DETAIL_PLAYER,params, new VolleyContentFast.ResponseSuccessListener<BasketPlayerStatisticsBean>() {
            @Override
            public void onResponse(BasketPlayerStatisticsBean jsonObject) {

                if(jsonObject.getResult()==200){
                    handler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
                    mData=jsonObject;
                    loadData();

                }
            }


        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                handler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);

            }
        }, BasketPlayerStatisticsBean.class);

    }

    /**
     * 请求数据之后的操作
     */
    private void loadData() {
        mGuestName.setText(mData.getData().getGuestTeam());
        mHomeName.setText(mData.getData().getHomeTeam());
        if(getActivity()!=null){
            ImageLoader.load(getActivity(),mData.getData().getGuestLogoUrl(),R.mipmap.basket_default).into(mGuestIcon);
            ImageLoader.load(getActivity(),mData.getData().getHomeLogoUrl(),R.mipmap.basket_default).into(mHomeIcon);
        }
        list.clear();

        list.addAll(mData.getData().getGuestPlayerStats());
        if(list.size()==0){
            handler.sendEmptyMessage(VIEW_STATUS_NO_DATA);
        }
        mAdapter.notifyDataSetChanged();
        mNames.clear();
        for(BasketPlayerStatisticsBean.PlayerStatsEntity entity:list){
            mNames.add(entity.getPlayerName());
        }
        mNameAdapter.notifyDataSetChanged();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_guest_team:
                if(!isCheckedGuest){ //防止不停的点击就不停的设置颜色
                    mGuestName.setTextColor(MyApp.getContext().getResources().getColor(R.color.tabtitle));
                    mHomeName.setTextColor(MyApp.getContext().getResources().getColor(R.color.mdy_333));
                    list.clear();
                    list.addAll(mData.getData().getGuestPlayerStats());
                    if(list.size()==0){
                        mNodataLayout.setVisibility(View.VISIBLE);
                    }else{
                        mNodataLayout.setVisibility(View.GONE);
                    }
                    mAdapter.notifyDataSetChanged();
                    mNames.clear();
                    for(BasketPlayerStatisticsBean.PlayerStatsEntity entity:list){
                        mNames.add(entity.getPlayerName());
                    }
                    mNameAdapter.notifyDataSetChanged();
                }
               isCheckedGuest=true;

                break;
            case R.id.rl_home_team:
                if(isCheckedGuest){
                    mGuestName.setTextColor(MyApp.getContext().getResources().getColor(R.color.mdy_333));
                    mHomeName.setTextColor(MyApp.getContext().getResources().getColor(R.color.tabtitle));
                    list.clear();
                    list.addAll( mData.getData().getHomePlayerStats());
                    if(list.size()==0){
                        mNodataLayout.setVisibility(View.VISIBLE);
                    }else{
                        mNodataLayout.setVisibility(View.GONE);
                    }
                    mAdapter.notifyDataSetChanged();
                    mNames.clear();
                    for(BasketPlayerStatisticsBean.PlayerStatsEntity entity:list){
                        mNames.add(entity.getPlayerName());
                    }
                    mNameAdapter.notifyDataSetChanged();
                }
               isCheckedGuest=false;
                break;
            case R.id.network_exception_reload_btn:
                handler.sendEmptyMessage(VIEW_STATUS_LOADING);
                initData();
                break;
        }

    }

    /**
     * eventbus
     * @param basketDetailLiveTextRefreshEventBus
     */
    public void onEventMainThread(BasketDetailPlayersStatisticsRefresh basketDetailLiveTextRefreshEventBus) {
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
