package com.hhly.mlottery.frame.footframe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketDatabaseDetailsAdapter;
import com.hhly.mlottery.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author: yixq
 * Created by A on 2016/7/15.
 */
public class BasketDatasaseHandicapFragment extends Fragment{

    private View mView;
    private static final String PARAM = "param";
    private TextView mDatabaseName; // 排名\队名
    private TextView mYisai;//已赛
    private TextView mUp;//上盘
    private TextView mDown;//下盘
    private TextView mYzs;//赢走输
    private RadioGroup mRadioGroup;
    private RadioButton mAllRadioButon;
    private RadioButton mHomeRadioButon;
    private RadioButton mGuestRadioButon;
    private NoScrollListView mListView;
    private BasketDatabaseDetailsAdapter mAdapter;
    private List mData;
    private LinearLayout mListData;
    private LinearLayout mLoadRefresh; // 网络异常
    private TextView mNodata; // 暂无数据
    private FrameLayout mLoading;// 加载中 loading...

    private final static int VIEW_STATUS_LOADING = 1; //加载中
    private final static int VIEW_STATUS_SUCCESS = 2; // 加载成功
    private final static int VIEW_STATUS_NET_ERROR = 3; // 请求失败
    private final static int VIEW_STATUS_NET_NODATA = 4; // 暂无数据


    public static BasketDatasaseHandicapFragment newInstance(String param) {
        BasketDatasaseHandicapFragment fragment = new BasketDatasaseHandicapFragment();
        Bundle args = new Bundle();
        args.putString(PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.basket_database_details_handicap, container, false);

        initView();
        initData();
        return mView;
    }

    private void initView(){

        mDatabaseName = (TextView)mView.findViewById(R.id.basket_database_name);
        mYisai = (TextView)mView.findViewById(R.id.basket_database_yisai);
        mUp = (TextView)mView.findViewById(R.id.basket_database_up);
        mDown = (TextView)mView.findViewById(R.id.basket_database_down);
        mYzs = (TextView)mView.findViewById(R.id.basket_database_yzs);

        mRadioGroup = (RadioGroup)mView.findViewById(R.id.gendergroup);
        mAllRadioButon = (RadioButton)mView.findViewById(R.id.basket_database_details_all);
        mAllRadioButon.setChecked(true);//设置默认选中（all）
        mHomeRadioButon = (RadioButton)mView.findViewById(R.id.basket_database_details_home);
        mGuestRadioButon = (RadioButton)mView.findViewById(R.id.basket_database_details_guest);

        mListView = (NoScrollListView)mView.findViewById(R.id.basket_database_handicap_list);
        mListView.setFocusable(false);//让listview失去焦点，处理加载时listview置顶情况.

        mListData = (LinearLayout)mView.findViewById(R.id.basket_database_details_data);
        mLoadRefresh = (LinearLayout)mView.findViewById(R.id.basket_database_details_refresh);
        mNodata = (TextView)mView.findViewById(R.id.basket_database_details_nodata);
        mLoading = (FrameLayout)mView.findViewById(R.id.basket_database_loading_details);

        onClick();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case VIEW_STATUS_LOADING:
                    mLoading.setVisibility(View.VISIBLE);
                    mListData.setVisibility(View.GONE);
                    mLoadRefresh.setVisibility(View.GONE);
                    mNodata.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_SUCCESS:
                    mLoading.setVisibility(View.GONE);
                    mListData.setVisibility(View.VISIBLE);
                    mLoadRefresh.setVisibility(View.GONE);
                    mNodata.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_NET_ERROR:
                    mLoading.setVisibility(View.GONE);
                    mListData.setVisibility(View.GONE);
                    mLoadRefresh.setVisibility(View.VISIBLE);
                    mNodata.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_NET_NODATA:
                    mLoading.setVisibility(View.GONE);
                    mListData.setVisibility(View.GONE);
                    mLoadRefresh.setVisibility(View.GONE);
                    mNodata.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    Handler mHandlerTT = new Handler();
   private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        }
    };
    private void initData(){

//        mHandlerTT.postDelayed(mRun , 1000);
//        mHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        mData = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            mData.add(i+1+ "Penta kill");
        }

        mAdapter = new BasketDatabaseDetailsAdapter(getContext() , mData, R.layout.basket_database_details_item);

        mListView.setAdapter(mAdapter);
        mHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
    }

    /**
     * RadioGroup的点击（全部、主、客切换）
     */
    private void onClick(){
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == mAllRadioButon.getId()) {

                    Toast.makeText(getContext(), "全部", Toast.LENGTH_SHORT).show();

                }else if(checkedId == mHomeRadioButon.getId()){

                    Toast.makeText(getContext(), "主场", Toast.LENGTH_SHORT).show();

                }else if(checkedId == mGuestRadioButon.getId()){

                    Toast.makeText(getContext(), "客场", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
