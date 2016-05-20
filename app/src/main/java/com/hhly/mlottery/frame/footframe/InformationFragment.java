package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballActivity;
import com.hhly.mlottery.activity.FootballInformationActivity;
import com.hhly.mlottery.adapter.InformationDataAdapter;
import com.hhly.mlottery.bean.footballDetails.InforListData;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;
import com.umeng.analytics.MobclickAgent;


import java.util.List;

/**
 * @ClassName: OneScore
 * @author:Administrator luyao
 * @Description: 足球数据
 * @data: 2016/3/29 16:30
 */
public class InformationFragment extends Fragment implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = "InformationFragment";

    public static final int LOAD_DATA_STATUS_INIT = 0;
    public static final int LOAD_DATA_STATUS_LOADING = 1;//正在加载状态
    public static final int LOAD_DATA_STATUS_SUCCESS = 2;//加载成功状态
    public static final int LOAD_DATA_STATUS_ERROR = 3;//加载失败状态

    private int mLoadDataStatus = LOAD_DATA_STATUS_INIT;// 加载数据状态
    private ExactSwipeRefrashLayout mSwipeRefreshLayout;// 下拉刷新
    private boolean isNetSuccess = true;// 告诉筛选页面数据是否加载成功
    private boolean isLoadedData = false; // 判断是否加载过数据
    private View view;
    private Context mContext;
    private ImageView mImg_back;//返回
    private ListView mListview;

    private LinearLayout mLoadingLayout;//正在加载
    private LinearLayout mErrorLayout;//加载异常
    private TextView mReloadBtn; //刷新按钮

    private List<InforListData.LeagueListBean> mAllLeagu;//数据集
    private TextView mReloadBtu;//刷新
    private final static int VIEW_STATUS_LOADING = 1;//正在加载
    private final static int VIEW_STATUS_SUCCESS = 3;//加载成功
    private final static int VIEW_STATUS_NET_ERROR = 4;//加载失败
    private final static int VIEW_STATUS_FLITER_NO_DATA = 5;//无数据

    private final int MIN_CLICK_DELAY_TIME = 1000;// 控件点击间隔时间
    private long lastClickTime = 0;

    private Handler mViewHandler = new Handler() {
        public void handleMessage(Message msg) {
            isNetSuccess = true;
            switch (msg.what) {
                case VIEW_STATUS_LOADING:
                    mErrorLayout.setVisibility(View.GONE);
                    //mListView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    if (!isLoadedData) {
                        mLoadingLayout.setVisibility(View.VISIBLE);
                    }
                    mErrorLayout.setVisibility(View.GONE);
                    mLoadDataStatus = LOAD_DATA_STATUS_LOADING;
                    break;
                case VIEW_STATUS_SUCCESS:
                    //mScreen.setVisibility(View.VISIBLE);
                    mLoadingLayout.setVisibility(View.GONE);
                    mErrorLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    //mListView.setVisibility(View.VISIBLE);
                    mLoadDataStatus = LOAD_DATA_STATUS_SUCCESS;

                    break;
                case VIEW_STATUS_NET_ERROR:

                    if (isLoadedData) {
                        Toast.makeText(mContext, R.string.exp_net_status_txt, Toast.LENGTH_SHORT).show();
                    } else {
                        mLoadingLayout.setVisibility(View.GONE);
                        //mSwipeRefreshLayout.setVisibility(View.GONE);
                        mErrorLayout.setVisibility(View.VISIBLE);
                        mLoadDataStatus = LOAD_DATA_STATUS_ERROR;
                    }
                    mSwipeRefreshLayout.setRefreshing(false);

                    isNetSuccess = false;
                    break;
                case VIEW_STATUS_FLITER_NO_DATA:
                    mLoadingLayout.setVisibility(View.GONE);
                    mErrorLayout.setVisibility(View.GONE);
                    //mListView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mLoadDataStatus = LOAD_DATA_STATUS_SUCCESS;
                    break;
                default:
                    break;
            }
        }
    };
    private LinearLayout mList_header;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.football_infor_listing, container, false);

        mContext = getActivity();
        //初始化数据
        initView();
        initData();
        initEvent();
        return view;

    }

    private void initEvent() {

    //给ListView添加监听事件
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 position=position-1;  //这里是因为listview添加headerView   so...postion算上headerView的!
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    Intent intent = new Intent(getActivity(), FootballInformationActivity.class);
                    intent.putExtra("lid", mAllLeagu.get(position).getLid() + "");//传递联赛ID
                    intent.putExtra("leagueType", mAllLeagu.get(position).getType() + "");
                    startActivity(intent);
                    MobclickAgent.onEvent(mContext, "Football_InformationFragment");
                }
            }
        });


    }

    private synchronized void initData() {

        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);// 正在加载数据中
        if (getActivity() == null) {
            return;
        }
        String url = BaseURLs.URL_INFORMATION;
        //请求数据
        VolleyContentFast.requestJsonByGet(url, new VolleyContentFast.ResponseSuccessListener<InforListData>() {

            private InformationDataAdapter mInforDataAdapter;

            @Override
            public synchronized void onResponse(final InforListData json) {

                if (json == null) {
                    //网络请求失败
                    mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);// 正在加载数据中
                    return;
                }
                if (json.getCode() == 200) {
                    //获取联赛列表数据
                    mAllLeagu = json.getLeagueList();
                    //  获取所有的联赛列表

                    //设置适配器
                    mInforDataAdapter = new InformationDataAdapter(mContext, mAllLeagu, R.layout.football_infor_distribution);
                    if (!isLoadedData) {
                        mListview.addHeaderView(mList_header,null,false);
                    }
                    mListview.setAdapter(mInforDataAdapter);

                }
                isLoadedData = true;
                //网络请求成功
                mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                //发送请求失败请求
                mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        }, InforListData.class);


    }

    private void initView() {

        //

        //获取listview控件
        mListview = (ListView) view.findViewById(R.id.infor_listview);
        mList_header = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.football_infor_listheader,mListview,false);
        //获取返回控件
        mImg_back = (ImageView) view.findViewById(R.id.infor_img_back);
        mImg_back.setOnClickListener(this);


        //加载状态图标
        mLoadingLayout = (LinearLayout) view.findViewById(R.id.football_infor_loading_ll);
        mErrorLayout = (LinearLayout) view.findViewById(R.id.network_exception_layout);
        mReloadBtn = (TextView) view.findViewById(R.id.network_exception_reload_btn);
        mReloadBtn.setOnClickListener(this);

        //下拉刷新
        mSwipeRefreshLayout = (ExactSwipeRefrashLayout) view.findViewById(R.id.football_infor_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        //刷新按钮
        mReloadBtu = (TextView) view.findViewById(R.id.network_exception_reload_btn);
        mReloadBtu.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.network_exception_reload_btn:
                mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                initData();
                break;
            case R.id.infor_img_back:
                ((FootballActivity) getActivity()).finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 500);

    }
    private boolean isHidden;// 当前Fragment是否显示
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (hidden) {
            MobclickAgent.onPageEnd("InformationFragment");
        } else {
            MobclickAgent.onPageStart("InformationFragment");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("InformationFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!isHidden){
            MobclickAgent.onPageEnd("InformationFragment");
        }
    }
}



