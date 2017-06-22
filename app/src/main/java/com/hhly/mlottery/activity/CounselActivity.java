package com.hhly.mlottery.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.CounselFragmentAdapter;
import com.hhly.mlottery.bean.footballDetails.CounselBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.CounselChildFragment;
import com.hhly.mlottery.frame.ForeignInfomationFragment;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.List;

/*
  lzf
  资讯页面
 */
public class CounselActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private static final int FOREIGNNEWS = 14; //境外新闻单独有一个fragment

    public static final int HEAD_LOADING = 0;//头部正在加载
    public static final int HEAD_NODATA = 1;//头部没有数据
    public static final int HEAD_NETERROR = 2;//头部网络错误
    public static final int HEAD_SUCESS = 3;//头部加载成功
    public static final int HEAD_FAIL = 4;//头部加载失败
    public static final String BUNDLE_PARM_TITLE = "headTitle";
    public static final String BUNDLE_PARM_INFOTYPE = "infoType";
    private LinearLayout reloadwhenfail;//头数据加载失败时的视图
    private TextView reLoading;//头数据加载失败时，点击重新加载
    private TextView nodataorloading;//头数据加载失败时，点击重新加载
    private List<Fragment> mList = new ArrayList();
    private CounselFragmentAdapter mCounselFragmentAdapter;
    private List<Boolean> isImageLeft = new ArrayList<>();//是否图片左边布局
    private ImageView public_img_back, public_btn_filter, public_btn_set;
    private TextView public_txt_title, public_txt_left_title;//标题，暂无数据
    private List<CounselBean.InfoIndexBean.HeadTitlesBean> mHeadList;//头数据集合
    private List<CounselBean.InfoIndexBean.AdsBean> mAdsList;//轮播数据集合
    private List<CounselBean.InfoIndexBean.InfosBean> mInfosList;//资讯列表数据集合
    private ArrayList<String> mHeadName = new ArrayList<>();//头名称集合
    private ArrayList<Integer> infotype = new ArrayList<>();//信息类型集合
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context mContext;
    private Integer currentIndex;// 显示下标

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HEAD_LOADING:
                    //头部正在下载
                    reloadwhenfail.setVisibility(View.GONE);
                    nodataorloading.setVisibility(View.VISIBLE);
                    nodataorloading.setText(R.string.loading_data_txt);
                    mTabLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    break;
                case HEAD_NODATA:
                    //头部暂没数据
                    reloadwhenfail.setVisibility(View.GONE);
                    nodataorloading.setVisibility(View.VISIBLE);
                    nodataorloading.setText(R.string.nodata);
                    mTabLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);

                    break;
                case HEAD_NETERROR:
                    //头部网络错误
                    reloadwhenfail.setVisibility(View.VISIBLE);
                    nodataorloading.setVisibility(View.GONE);
                    mTabLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case HEAD_SUCESS:
                    //头部下载成功
                    reloadwhenfail.setVisibility(View.GONE);
                    nodataorloading.setVisibility(View.GONE);
                    mTabLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case HEAD_FAIL:
                    //头部下载失败
                    reloadwhenfail.setVisibility(View.VISIBLE);
                    nodataorloading.setVisibility(View.GONE);
                    mTabLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_counsel);
        currentIndex = getIntent().getExtras().getInt("currentIndex");

        mContext = this;
        initView();
        //请求头数据
        loadHeadData(BaseURLs.URL_FOOTBALL_INFOINDEX);
    }

    //请求头数据  发起网络请求
    private void loadHeadData(String url) {

        //正在加载数据
        mHandler.sendEmptyMessage(HEAD_LOADING);
        //语言不用传，已封装好自动追加
//        String urlx = "http://192.168.10.242:8181/mlottery/core/info.findAndroidIndexInfo.do";
        VolleyContentFast.requestJsonByGet(url, new VolleyContentFast.ResponseSuccessListener<CounselBean>() {
            @Override
            public synchronized void onResponse(final CounselBean json) {
                //请求成功
                if ("200".equals(json.getResult() + "")) {
//                    ToastTools.showQuickCenter(mContext, json.getResult() + "");
                    //没有数据
                    if (json == null) {
                        mHandler.sendEmptyMessage(HEAD_NODATA);//暂时没有数据
                    } else {
                        //有数据
                        mHeadList = json.getInfoIndex().getHeadTitles();
                        mAdsList = json.getInfoIndex().getAds();
                        mInfosList = json.getInfoIndex().getInfos();
                        //添加头数据
                        if (mHeadList.size() == 0) {
                            mHandler.sendEmptyMessage(HEAD_NODATA);//暂时没有数据

                        } else {
                            for (int i = 0; i < mHeadList.size(); i++) {
                                if (!TextUtils.isEmpty(mHeadList.get(i).getTitle())) {
                                    mHeadName.add(mHeadList.get(i).getTitle());
                                }
                                isImageLeft.add(mHeadList.get(i).isImageLeft());
                                infotype.add(mHeadList.get(i).getInfoType());

                            }
                            setupViewPager();
                            mHandler.sendEmptyMessage(HEAD_SUCESS);//加载数据成功
                        }

                    }
                } else {
                    //请求失败
                    mHandler.sendEmptyMessage(HEAD_FAIL);//请求失败;
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                mHandler.sendEmptyMessage(HEAD_NETERROR);//请求失败;
            }
        }, CounselBean.class);
    }

    //根据头数据的长度  来构建viewpager的数据源
    private void initData() {
        for (int i = 0; i < mHeadName.size(); i++) {

            if (FOREIGNNEWS != mHeadList.get(i).getInfoType()) {
                CounselChildFragment mCounselChildFragment = new CounselChildFragment();
                Bundle bundle = new Bundle();
                //传递数据给第一个fg
                if (i == 0) {
                    mCounselChildFragment.setInfosList(mInfosList);
                    mCounselChildFragment.setAdsList(mAdsList);
                }
                bundle.putString(BUNDLE_PARM_TITLE, mHeadName.get(i));//头名称
                bundle.putInt("index", i);//当前fg标识
                bundle.putBoolean("isImageLeft", isImageLeft.get(i));//是否图片左边布局
                bundle.putInt(BUNDLE_PARM_INFOTYPE, infotype.get(i));//信息类型集合
                mCounselChildFragment.setArguments(bundle);
                mList.add(mCounselChildFragment);
            } else { //墙外资讯
                ForeignInfomationFragment mForeignInfomationFragment = ForeignInfomationFragment.newInstance();
                mList.add(mForeignInfomationFragment);
            }
        }
    }


    private void setupViewPager() {
        if (mHeadName.size() > 4) {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
        for (int i = 0; i < mHeadName.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mHeadName.get(i)));
        }
        initData();
        mCounselFragmentAdapter = new CounselFragmentAdapter(getSupportFragmentManager(), mList, mHeadName, mContext);
        mViewPager.setAdapter(mCounselFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(currentIndex == null ? 0 : currentIndex - 1);
        mTabLayout.setTabsFromPagerAdapter(mCounselFragmentAdapter);
    }

    public void initView() {
        //网络异常
        reloadwhenfail = (LinearLayout) findViewById(R.id.network_exception_layout);
        reLoading = (TextView) findViewById(R.id.network_exception_reload_btn);
        nodataorloading = (TextView) findViewById(R.id.dataloding_ornodata);
        reLoading.setOnClickListener(this);

        //标题
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setVisibility(View.VISIBLE);
        public_txt_title.setText(R.string.foot_title);
        public_txt_left_title = (TextView) findViewById(R.id.public_txt_left_title);
        public_txt_left_title.setVisibility(View.GONE);

        //筛选
        public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);

        //设置
        public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setVisibility(View.GONE);
        //返回
        public_img_back = (ImageView) findViewById(R.id.public_img_back);
        public_img_back.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.counselfragment_viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.counselfragment_SwipeRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(this, StaticValues.REFRASH_OFFSET_END));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_img_back:
                finish();
                break;
            case R.id.network_exception_reload_btn:
                loadHeadData(BaseURLs.URL_FOOTBALL_INFOINDEX);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
