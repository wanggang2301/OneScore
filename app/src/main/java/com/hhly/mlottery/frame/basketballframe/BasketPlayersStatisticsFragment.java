package com.hhly.mlottery.frame.basketballframe;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketPlayerNameAdapter;
import com.hhly.mlottery.adapter.basketball.BasketPlayerStatisticsAdapter;
import com.hhly.mlottery.bean.basket.basketstatistics.BasketPlayerStatisticsBean;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.net.VolleyContentFast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    /**主客队选中*/
    private boolean isCheckedGuest=true;
    BasketPlayerStatisticsBean mData;



    BasketPlayerStatisticsAdapter mAdapter;
    List<BasketPlayerStatisticsBean.DataEntity.HomePlayerStatsEntity> list=new ArrayList<>();

    View mView;
    LinearLayoutManager mLinearLayoutManager;

    private final static int VIEW_STATUS_LOADING = 1;
    private final static int VIEW_STATUS_SUCCESS = 2;
    private final static int VIEW_STATUS_NET_ERROR = 3;

    /**队名数据*/
    List<String> mNames=new ArrayList<>();
    BasketPlayerNameAdapter mNameAdapter;


    public static BasketPlayersStatisticsFragment newInstance() {
        BasketPlayersStatisticsFragment basketPlayersStatisticsFragment = new BasketPlayersStatisticsFragment();
        return basketPlayersStatisticsFragment;
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
                case VIEW_STATUS_SUCCESS:

                    break;
                case VIEW_STATUS_NET_ERROR:
                    // 展示错误界面
                    break;
            }
        }
    };

    private void initView() {


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
        mGuestName.setOnClickListener(this);
        mHomeName.setOnClickListener(this);

    }

    private void initData() {
        Map<String,String> params=new HashMap<>();
        params.put("thirdId","3666697");
        String url="http://m.13322.com/mlottery/core/IOSBasketballDetail.findPlayerStats.do";
        VolleyContentFast.requestJsonByGet(url,params, new VolleyContentFast.ResponseSuccessListener<BasketPlayerStatisticsBean>() {
            @Override
            public void onResponse(BasketPlayerStatisticsBean jsonObject) {

                if(jsonObject.getResult()==200){
                    mData=jsonObject;
                    loadData();

                }

                mData=jsonObject;

                list.clear();

                list.addAll(jsonObject.getData().getHomePlayerStats());
/*                list.addAll(jsonObject.getData().getHomePlayerStats());
                list.addAll(jsonObject.getData().getHomePlayerStats());*/
                Log.e("???????",list.size()+"");
                mAdapter.notifyDataSetChanged();
                mNames.clear();
                for(BasketPlayerStatisticsBean.DataEntity.HomePlayerStatsEntity entity:list){
                    mNames.add(entity.getPlayerName());
                }
                mNameAdapter.notifyDataSetChanged();


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
        ImageLoader.load(MyApp.getContext(),mData.getData().getGuestLogoUrl(),R.mipmap.basket_default).into(mGuestIcon);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.basket_player_guest_team_name:

                break;
            case R.id.basket_player_home_team_name:
                break;
        }

    }
}
