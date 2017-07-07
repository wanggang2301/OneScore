package com.hhly.mlottery.frame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.PLVideoTextureActivity;
import com.hhly.mlottery.activity.WebActivity;
import com.hhly.mlottery.adapter.CounselFragmentLvAdapter;
import com.hhly.mlottery.adapter.CounselPageAdapter;
import com.hhly.mlottery.bean.footballDetails.CounselBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.PullUpRefreshListView;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
  lzf
  资讯子页面
 */
public class CounselChildFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener {
    private View mView;//fragment视图
    private View mHeadView;//头部轮播视图
    private PullUpRefreshListView mListView;
    private String mHeadName; //头名称
    private List<CounselBean.InfoIndexBean.InfosBean> mInfosList;//资讯列表数据集合父fg传来(第一个fragment的数据)
    private List<CounselBean.InfoIndexBean.AdsBean> mAdsList;//轮播图数据集合父fg传来(第一个fragment的数据)
    private List<String> adsurl = new ArrayList<>();//轮播图url
    //    private List<String> adsurljump = new ArrayList<>();//轮播图跳转url
//    private List<String> adstitle = new ArrayList<>();//轮播图标题
    private List<CounselBean.InfoIndexBean.InfosBean> mInfos = new ArrayList<>();//资讯列表数据集合(非第一个fragment的数据)由当前碎片发起网络请求
    private LinearLayout mReloadWhenFail;//头数据加载失败时的视图
    private TextView reLoading;//点击重新加载
    private TextView nodataorloading;//没有数据
    private CounselFragmentLvAdapter mAdapter; //listview适配器
    private ExactSwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新
    int index;//当前fragment
    private boolean isImageLeft;//item是否是左边布局
    private int mCurrentPager = 1;//当前页数
    private TextView mLoadMore;//加载更多
    private ProgressBar mProgressBar;//上拉加载的进度条
    private boolean isRequestFinish = true;//网络请求是否完成
    private Context mContext;
    private LinearLayout tab_news;//轮播导航点
    private CounselPageAdapter mPagerAdapter;//轮播适配器
    private ViewPager mViewPager;
    private TextView mTextView;
    public final static String INTENT_PARAM_THIRDID = "thirdId";
    public final static String INTENT_PARAM_JUMPURL = "key";
    public final static String INTENT_PARAM_INDEX = "index";
    public final static String INTENT_PARAM_TYPE = "type";
    public final static String INTENT_PARAM_TITLE = "infoTypeName";
    public static final String BUNDLE_PARM_TITLE = "headTitle";
    public static final String NET_PARM_PAGE = "currentPage";//网络请求的参数key  页数
    public static final String BUNDLE_PARM_INFOTYPE = "infoType";//网络请求的参数key  列表类型
    private boolean isCreated = false;//当前碎片是否createview
    private boolean isPullDownRefresh = false;//是否是下拉刷新
    private int infotype;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initView(inflater);
        initData();
        addListViewHeadAndInitListView(isImageLeft);
        return mView;
    }

    private void initData() {
        Bundle bundle = getArguments();
        index = bundle.getInt(INTENT_PARAM_INDEX);//当前fg表示
        mHeadName = bundle.getString(BUNDLE_PARM_TITLE);//当前fg的头名称
        isImageLeft = bundle.getBoolean("isImageLeft", false);//是否图片左边布局
        infotype = bundle.getInt(BUNDLE_PARM_INFOTYPE);
    }

    private void initView(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_counsel_child, null);
        mContext = getActivity();
        mReloadWhenFail = (LinearLayout) mView.findViewById(R.id.network_exception_layout);
        reLoading = (TextView) mView.findViewById(R.id.network_exception_reload_btn);
        reLoading.setOnClickListener(this);
        nodataorloading = (TextView) mView.findViewById(R.id.dataloding_ornodata);
        //下拉刷新
        mSwipeRefreshLayout = (ExactSwipeRefreshLayout) mView.findViewById(R.id.swiperefreshlayout_counselfragmentchild);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));
        //listview
        mListView = (PullUpRefreshListView) mView.findViewById(R.id.lv_fg_counsel_child);
        mListView.setOnItemClickListener(this);
        isCreated = true;
    }

    private void addListViewHeadAndInitListView(boolean isleft) {
        //如果是第一个碎片  不用发起网络请求 直接从父fg那边拿到数据
        if (index == 0) {
            mAdapter = new CounselFragmentLvAdapter(isleft, mInfosList, getActivity());
            //轮播图
            if (adsurl.size() != 0) {//轮播数据存在时才添加轮播功能
                addAds();
            }


        } else {//非第一个碎片
            mAdapter = new CounselFragmentLvAdapter(isleft, mInfos, getActivity());
        }
        //上拉加载更多
        pullUpLoad();
        mListView.setAdapter(mAdapter);
        //解决使用懒加载时跳跃点击无法加载的问题,因为viewpager刚进来时只会初始化前2个fg
        // 突然点击第三个时 有两个问题 第一没初始化好，第二 不会触发setUserVisibleHint()所以在初始化完时处于可见且没有数据则加载
        if (getUserVisibleHint() && mInfos.size() == 0) {
            onVisible();
        }
    }

    public static final int NEWS_LOADING = 0;//头部正在加载
    public static final int NEWS_NODATA = 1;//头部没有数据
    public static final int NEWS_NETERROR = 2;//头部网络错误
    public static final int NEWS_SUCESS = 3;//头部加载成功
    public static final int NEWS_FAIL = 4;//头部加载失败
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEWS_LOADING:
                    //列表正在下载
                    mReloadWhenFail.setVisibility(View.GONE);
                    nodataorloading.setVisibility(View.GONE);
                    nodataorloading.setText(R.string.loading_data_txt);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    break;
                case NEWS_NODATA:
                    //列表暂没数据
                    mReloadWhenFail.setVisibility(View.GONE);
                    nodataorloading.setVisibility(View.VISIBLE);
                    nodataorloading.setText(R.string.nodata);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case NEWS_NETERROR:
                    //列表网络错误
                    //解决有数据时  下拉刷新失败以后  重新加载显示在listview上的问题。
                    // 不是下拉刷新时的请求失败，mReloadWhenFail里的刷新无法点击的问题
                    if (isPullDownRefresh) {
                        mReloadWhenFail.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        ToastTools.showQuickCenter(mContext, mContext.getString(R.string.number_exp_net_status_txt));
                    } else {
                        mReloadWhenFail.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                    }
                    nodataorloading.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case NEWS_SUCESS:
                    //列表下载成功
                    mReloadWhenFail.setVisibility(View.GONE);
                    nodataorloading.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    break;
                case NEWS_FAIL:
                    //列表下载失败
                    //解决有数据时  下拉刷新失败以后  重新加载显示在listview上的问题。
                    // 不是下拉刷新时的请求失败，mReloadWhenFail里的刷新无法点击的问题
                    if (isPullDownRefresh) {//是下拉刷新
                        mReloadWhenFail.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        ToastTools.showQuickCenter(mContext, mContext.getString(R.string.number_exp_net_status_txt));
                    } else {
                        mReloadWhenFail.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                    }
                    nodataorloading.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    //请求列表数据  发起网络请求
    private void loadNewsData(String url, String currentPage, String infoType) {

        //正在加载数据
        mHandler.sendEmptyMessage(NEWS_LOADING);

        //语言不用传，已封装好自动追加
        Map<String, String> map = new HashMap<>();
        map.put(NET_PARM_PAGE, currentPage);
        map.put(BUNDLE_PARM_INFOTYPE, infoType);
//        String urlx = "http://192.168.10.242:8181/mlottery/core/info.findAndroidLstInfo.do";
        VolleyContentFast.requestJsonByGet(url, map, new VolleyContentFast.ResponseSuccessListener<CounselBean>() {
            @Override
            public synchronized void onResponse(final CounselBean json) {
                //请求成功
                if ("200".equals(json.getResult() + "")) {
                    //没有数据
                    if (json == null) {
                        mHandler.sendEmptyMessage(NEWS_NODATA);//暂时没有数据
                        ToastTools.showQuickCenter(mContext, "json == null");
                        //
                    } else {
                        //有数据
//                        mInfos = json.getInfoIndex().getInfos();
                        if (json.getInfoIndex().getInfos().size() == 0) {
                            mHandler.sendEmptyMessage(NEWS_NODATA);//暂时没有数据

                        } else {
                            mHandler.sendEmptyMessage(NEWS_SUCESS);//加载数据成功
                            mInfos.clear();
                            mInfos.addAll(json.getInfoIndex().getInfos());
                            mAdapter.setInfosList(mInfos);
                            mAdapter.notifyDataSetChanged();

                        }
                    }
                } else {
                    //请求失败
                    mHandler.sendEmptyMessage(NEWS_FAIL);//请求失败;
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                mHandler.sendEmptyMessage(NEWS_NETERROR);//请求失败;
//
            }
        }, CounselBean.class);
    }


    @Override
    public void onRefresh() {
        //下拉刷新后当前页数重新为1，不然先上拉加载到没有数据  再回去下拉刷新  然后再上拉就没有数据了，其实是有的
        mCurrentPager = 1;
        mLoadMore.setText(R.string.foot_loadmore);
        if (index == 0) {
            //向首页的接口发起请求
            mHandler.sendEmptyMessage(NEWS_SUCESS);
        } else {
            //向列表接口发起请求
            isPullDownRefresh = true;
            loadNewsData(BaseURLs.URL_FOOTBALL_INFOLIST, 1 + "", infotype + "");
//            ToastTools.showQuickCenter(mContext, "mCurrentPager=" + mCurrentPager + "index=" + index);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //加載失敗后的刷新
            case R.id.network_exception_reload_btn:
                //向列表接口发起请求
                loadNewsData(BaseURLs.URL_FOOTBALL_INFOLIST, 1 + "", infotype + "");
                break;
        }
    }

    //请求列表数据  发起网络请求上拉加载
    private void pullUpLoadMore(String url, String currentPage, String infoType) {
        mLoadMore.setText(R.string.foot_loadingmore);
        mProgressBar.setVisibility(View.VISIBLE);
        isRequestFinish = false;
        //语言不用传，已封装好自动追加
        Map<String, String> map = new HashMap<>();
        map.put("currentPage", currentPage);
        map.put("infoType", infoType);
//        String urlx = "http://192.168.10.242:8181/mlottery/core/info.findAndroidLstInfo.do";
        VolleyContentFast.requestJsonByGet(url, map, new VolleyContentFast.ResponseSuccessListener<CounselBean>() {
            @Override
            public synchronized void onResponse(final CounselBean json) {
                mProgressBar.setVisibility(View.GONE);
                isRequestFinish = true;
                mLoadMore.setText(R.string.foot_loadmore);
                //请求成功
                if ("200".equals(json.getResult() + "")) {
                    //没有数据
                    if (json == null) {
                        mLoadMore.setText(R.string.foot_nomoredata);
                        //
                    } else {
                        //有数据
                        if (json.getInfoIndex().getInfos().size() == 0) {
                            mLoadMore.setText(R.string.foot_nomoredata);

                        } else {
                            if (index == 0) {
                                mInfosList.addAll(json.getInfoIndex().getInfos());
                                mAdapter.setInfosList(mInfosList);
                            } else {
                                mInfos.addAll(json.getInfoIndex().getInfos());
                                mAdapter.setInfosList(mInfos);
                            }
                            mAdapter.notifyDataSetChanged();
                            mLoadMore.setText(R.string.foot_loadmore);
                            mHandler.sendEmptyMessage(NEWS_SUCESS);//加载数据成功
                        }
                    }
                } else {
                    //请求失败
                    mLoadMore.setText(R.string.foot_neterror);
                }

            }

        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                //请求失败
                mProgressBar.setVisibility(View.GONE);
                isRequestFinish = true;
                mLoadMore.setText(R.string.foot_neterror);
//
            }
        }, CounselBean.class);
    }

    //动态添加导航
    private void initTab() {
        int dp = DisplayUtil.dip2px(mContext, 5);// 添加小圆点
        for (int i = 0; i < adsurl.size(); i++) {
            View views = new View(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp, dp);
            if (i != 0) {
                params.leftMargin = dp;
            }
            views.setLayoutParams(params);
            views.setBackgroundResource(R.drawable.v_lunbo_point_selector);
            tab_news.addView(views);
            tab_news.getChildAt(i).setEnabled(false);
        }
        tab_news.getChildAt(0).setEnabled(true);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(mContext, WebActivity.class);
        String jumpurl;//跳转url
        String title;//标题
        String subtitle;//副标题
        String imageurl;//图片url
//        boolean isRelateMatch;//是否关联比赛
        String ThirdId;//联赛id
        int type;//联赛id类型  1篮球 2足球
        if(index!=0&&mInfos.size()!=0&&mInfos.get(position).getIsvideonews()!=null&&mInfos.get(position).getIsvideonews().equals("1")){ //视频的点击播放

            Intent intent1=new Intent(mContext, PLVideoTextureActivity.class);
            intent1.putExtra("videoPath",mInfos.get(position).getVideourl());
            mContext.startActivity(intent1);
        }
        else{ //除了视频的，按照之前的逻辑
            if (index == 0) {
                if (mAdsList != null && mAdsList.size() != 0) {//有轮播图
                    //因为头部下标是0，item下标变成从1开始，所以要减去1
                    jumpurl = mInfosList.get(position - 1).getInfoUrl();
                    title = mInfosList.get(position - 1).getTitle();
                    subtitle = mInfosList.get(position - 1).getSubTitle();
                    imageurl = mInfosList.get(position - 1).getPicUrl();

                    ThirdId = mInfosList.get(position - 1).getThirdId();
//                    isRelateMatch = mInfosList.get(position - 1).isRelateMatch();
                    type = mInfosList.get(position - 1).getType();
                } else {//没有轮播图
                    jumpurl = mInfosList.get(position).getInfoUrl();
                    title = mInfosList.get(position).getTitle();
                    subtitle = mInfosList.get(position).getSubTitle();
                    imageurl = mInfosList.get(position).getPicUrl();

                    ThirdId = mInfosList.get(position).getThirdId();
//                    isRelateMatch = mInfosList.get(position).isRelateMatch();
                    type = mInfosList.get(position).getType();
                }


            } else {
                if (mInfos.size() != 0) {
                    jumpurl = mInfos.get(position).getInfoUrl();
                    title = mInfos.get(position).getTitle();
                    subtitle = mInfos.get(position).getSubTitle();
                    imageurl = mInfos.get(position).getPicUrl();
                    ThirdId = mInfos.get(position).getThirdId();
//                    isRelateMatch = mInfos.get(position).isRelateMatch();
                    type = mInfos.get(position).getType();
                } else {
                    jumpurl = "";
                    title = "";
                    subtitle = "";
                    imageurl = "";
                    ThirdId = "";
//                    isRelateMatch = false;
                    type = 1;
                }

            }
//            if (isRelateMatch) {
                intent.putExtra(INTENT_PARAM_THIRDID, ThirdId);
                intent.putExtra(INTENT_PARAM_TYPE, type);
//            }
            intent.putExtra(INTENT_PARAM_TITLE, mHeadName);//头部名称
            intent.putExtra(INTENT_PARAM_JUMPURL, jumpurl);
            intent.putExtra("title", title);
            intent.putExtra("subtitle", subtitle);
            intent.putExtra("imageurl", imageurl);
            mContext.startActivity(intent);
        }

    }

    //添加图片轮播功能
    public void addAds() {
        mHeadView = getActivity().getLayoutInflater().inflate(R.layout.listviewheaderview, null);
        tab_news = (LinearLayout) mHeadView.findViewById(R.id.tab_news);
        mViewPager = (ViewPager) mHeadView.findViewById(R.id.viewPager_header);
        mTextView = (TextView) mHeadView.findViewById(R.id.news_title);
        //添加轮播圆点
        initTab();
        // 创建适配器
        mPagerAdapter = new CounselPageAdapter(mContext, mAdsList, mViewPager, mHeadName);
        mViewPager.setAdapter(mPagerAdapter);
        int currentIndex = (Integer.MAX_VALUE / 2) % adsurl.size() == 0 ? (Integer.MAX_VALUE / 2) : (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2) % adsurl.size();
        mViewPager.setCurrentItem(currentIndex);// 设置当前轮播图下标
        mTextView.setText(mAdsList
                .get(0).getTitle());
        // 绑定动作监听器
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int len = 0;
                if (mAdsList.size() != 0) {
                    len = mAdsList.size();
                }
                //设置轮播导航圆点被选中状态
                for (int i = 0; i < len; i++) {
                    tab_news.getChildAt(i).setEnabled(i == position % len);
                }
                //设置轮播标题
                mTextView.setText(mAdsList
                        .get(position % len).getTitle());
                L.i("position" + position);
                L.i("positionlen" + position % len);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mListView.addHeaderView(mHeadView);
    }


    //上拉加载
    public void pullUpLoad() {
        //listview上拉加载
        View view = getActivity().getLayoutInflater().inflate(R.layout.listfooter_more, null);
        mLoadMore = (TextView) view.findViewById(R.id.load_more);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRequestFinish) {//上一个请求完成才执行这个 不然一直往上拉，会连续发多个请求
                    mCurrentPager++;
                    //向列表接口发起请求
                    if (!mLoadMore.getText().equals(getResources().getString(R.string.foot_nomoredata))) {//没有更多数据的时候，上拉不再发起请求
                        pullUpLoadMore(BaseURLs.URL_FOOTBALL_INFOLIST, mCurrentPager + "", infotype + "");
                    }
                }
            }
        });
        mProgressBar = (ProgressBar) view.findViewById(R.id.pull_to_refresh_progress);
        mLoadMore.setText(R.string.foot_loadmore);
        mListView.initBottomView(view);
        mListView.setMyPullUpListViewCallBack(new PullUpRefreshListView.MyPullUpListViewCallBack() {
            @Override
            public void scrollBottomState() {
                //上拉加载的逻辑
                //index代表当前的页面
                if (isRequestFinish) {//上一个请求完成才执行这个，不然一直往上拉，会连续发多个请求
                    mCurrentPager++;
                    //向列表接口发起请求
                    if (!mLoadMore.getText().equals(getResources().getString(R.string.foot_nomoredata))) {//没有更多数据的时候，上拉不再发起请求
                        pullUpLoadMore(BaseURLs.URL_FOOTBALL_INFOLIST, mCurrentPager + "", infotype + "");
                    }

                }
            }
        });

    }

    //fg切换时回调该方法
    //用来取消viewpager的预加载机制  减少初始化开销
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isCreated && getUserVisibleHint() && mInfos.size() == 0) {
            onVisible();

        }
        if (!isCreated) {
            return;
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        if (index != 0) {
            //发起网络请求数据
            loadNewsData(BaseURLs.URL_FOOTBALL_INFOLIST, mCurrentPager + "", infotype + "");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.i("position=onDestroyView" + index);
        //解决因counsel层滑动引起的轮播小圆点混乱
        if (index == 0) {
            if (mPagerAdapter != null) {
                mPagerAdapter.end();
            }

        }

    }

    public void setInfosList(List<CounselBean.InfoIndexBean.InfosBean> infosList) {
        this.mInfosList = infosList;

    }

    public void setAdsList(List<CounselBean.InfoIndexBean.AdsBean> adsList) {
        this.mAdsList = adsList;
        for (int i = 0; i < mAdsList.size(); i++) {
            adsurl.add(mAdsList.get(i).getPicUrl());
//            adsurljump.add(mAdsList.get(i).getJumpAddr());
//            adstitle.add(mAdsList.get(i).getTitle());
        }

    }
}