package com.hhly.mlottery.frame.homefrag;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.DefaultRetryPolicy;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.AccountDetailActivity;
import com.hhly.mlottery.activity.DebugConfigActivity;
import com.hhly.mlottery.activity.FootballEventAnimationFragment;
import com.hhly.mlottery.activity.HomeUserOptionsActivity;
import com.hhly.mlottery.activity.ProductAdviceActivity;
import com.hhly.mlottery.adapter.homePagerAdapter.HomeListBaseAdapter;
import com.hhly.mlottery.bean.UpdateInfo;
import com.hhly.mlottery.bean.homepagerentity.HomeBodysEntity;
import com.hhly.mlottery.bean.homepagerentity.HomeContentEntity;
import com.hhly.mlottery.bean.homepagerentity.HomeMenusEntity;
import com.hhly.mlottery.bean.homepagerentity.HomeOtherListsEntity;
import com.hhly.mlottery.bean.homepagerentity.HomePagerEntity;
import com.hhly.mlottery.callback.ProductListener;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;

import static android.app.Activity.RESULT_OK;

/**
 * 首页
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String TAG = "HomeFragment";

    private View mView;

    private Context mContext;// 上下文
    private ImageView iv_home_pic;// 登录图标
    private SwipeRefreshLayout mSwipeRefreshLayout;// 下拉刷新
    private ListView home_page_list;// 首页列表
    private HomeListBaseAdapter mListBaseAdapter;// ListView数据适配器
    private TextView tv_home_name;

    public HomePagerEntity mHomePagerEntity;// 首页实体对象
    private static final int LOADING_DATA_START = 0;// 加载数据
    private static final int LOADING_DATA_SUCCESS = 1;// 加载成功
    private static final int LOADING_DATA_ERROR = 2;// 加载失败
    private static final int REFRES_DATA_SUCCESS = 3;// 下拉刷新
    private static final int REFRESH_ADVICE = 6;

    private final int MIN_CLICK_DELAY_TIME = 2000;// 控件点击间隔时间
    private long lastClickTime = 0;
    private int clickCount = 0;// 点击次数

    private int REQUEST_CODE = 1;

    public ProductListener mListener;

    private Activity mActivity;

    // 当前版本的菜单入口范围
    private String[] menuList = {
//            "10",// 足球指数
//            "11",// 足球数据
//            "13",// 足球比分
//            "20",// 篮球即时比分
//            "21",// 篮球赛果
//            "22",// 篮球赛程
//            "23",// 篮球关注
//            "24",// 篮球资讯
//            "350",// 彩票资讯
//            "80",// 多屏动画列表
//            "60",// 情报中心


            "12", // 体育资讯
            "14", // 体育视频
            "30", // 彩票开奖
            "31", // 香港开奖
            "32", // 重庆时时彩
            "33", // 江西时时彩
            "34", // 新疆时时彩
            "35", // 云南时时彩
            "36", // 七星彩
            "37",// 广东11选5
            "38",// 广东快乐10分
            "39",// 湖北11选5
            "310",// 安徽快3
            "311",// 湖南快乐10分
            "312",// 快乐8
            "313",// 吉林快三
            "314",// 辽宁11选5
            "315",// 北京赛车
            "316",// 江苏快3
            "317",// 时时乐
            "318",// 广西快三
            "319",// 幸运农场
            "320",// 江苏11选5
            "321",// 江西11选5
            "322",// 山东11选5
            "323",// 天津时时彩
            "19",// 今日联赛统计
            "324",// 双色球
            "325",// 大乐透
            "326",// 排列三
            "327",// 排列五
            "328",// 胜负彩
            "329",// 六场半全场
            "330",// 四场进球彩
            "331",// 福彩3D
            "332",// 七乐彩
            "44",// 竞彩足球
            "101",// 角球比分
            "92"// 竞彩推介
    };

    /**
     * 跳转其他Activity 的requestcode
     */
    public static final int REQUESTCODE_LOGIN = 100;
    public static final int REQUESTCODE_LOGOUT = 110;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = mActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        initData();
        initEvent();
        return mView;
    }

    /**
     * 初始化布局
     */
    private void initView() {

        iv_home_pic = (ImageView) mView.findViewById(R.id.iv_home_pic);
        iv_home_pic.setOnClickListener(this);

        if (DeviceInfo.isLogin()) {
            iv_home_pic.setImageResource(R.mipmap.login);
        } else {
            iv_home_pic.setImageResource(R.mipmap.logout);
        }

        tv_home_name = (TextView) mView.findViewById(R.id.tv_home_name);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.home_page_swiperefreshlayout);// 下拉刷新
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));
        home_page_list = (ListView) mView.findViewById(R.id.home_page_list);// 首页列表

        mListener = new ProductListener() {
            @Override
            public void toProductActivity() {
                startActivityForResult(new Intent(mContext, ProductAdviceActivity.class), REQUEST_CODE);
            }
        };
    }

    /**
     * 初始化数据
     */
    private void initData() {
        try {
            readObjectFromFile();// 获取本地数据
            getRequestData(0);// 获取网络数据
        } catch (Exception e) {
            L.d("获取数据异常：" + e.getMessage());
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {

        if (AppConstants.isTestEnv) {
            tv_home_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                        lastClickTime = currentTime;
                        clickCount = 0;
                    } else {
                        clickCount += 1;
                        if (clickCount == 5) {
                            startActivity(new Intent(mContext, DebugConfigActivity.class));
                            getActivity().finish();
                        }
                    }
                }
            });
        }

        home_page_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            private SparseArray recordSp = new SparseArray(0);
            private int mCurrentfirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                mCurrentfirstVisibleItem = firstVisibleItem;
                View firstView = view.getChildAt(0);
                if (null != firstView) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();
                    itemRecord.top = firstView.getTop();
                    recordSp.append(firstVisibleItem, itemRecord);

                    try {
                        int h = getScrollY();//滚动距离
                        if (h <= 100) {
                            tv_home_name.setAlpha(h / 100f);
                        } else {
                            tv_home_name.setAlpha(1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            private int getScrollY() {
                int height = 0;
                for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
                    ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
                    height += itemRecod.height;
                }
                ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
                if (null == itemRecod) {
                    itemRecod = new ItemRecod();
                }
                return height - itemRecod.top;
            }

            class ItemRecod {
                int height = 0;
                int top = 0;
            }
        });


    }

    /**
     * 后台数据请求
     */
    public synchronized void getRequestData(final int num) {
        if (num != 2) {
            mHandler.sendEmptyMessage(LOADING_DATA_START);// 开始加载数据
        }
        // 设置参数
        Map<String, String> myPostParams = new HashMap<>();
        myPostParams.put("version", MyApp.version);
        myPostParams.put("versionCode", String.valueOf(MyApp.versionCode));
        myPostParams.put("channelNumber", MyApp.channelNumber);

        VolleyContentFast.requestStringByGet(BaseURLs.URL_HOME_PAGER_INFO, myPostParams, null, new VolleyContentFast.ResponseSuccessListener<String>() {
            @Override
            public void onResponse(String jsonObject) {
                if (jsonObject != null) {// 请求成功
                    try {
                        mHomePagerEntity = JSON.parseObject(jsonObject, HomePagerEntity.class);
                        Iterator<HomeContentEntity> iterator = mHomePagerEntity.getMenus().getContent().iterator();
                        /**---------屏蔽多余首页菜单入口--Start--------------------------*/
                        while (iterator.hasNext()) {
                            HomeContentEntity entity = iterator.next();
                            // 当前菜单集合不包含此菜单
                            if (entity.getJumpType() == 2) {
                                if (!Arrays.asList(menuList).contains(entity.getJumpAddr())) {
                                    iterator.remove();
                                }
                            }
                        }
                        /**---------屏蔽多余首页菜单入口--End--------------------------*/

                        /**----将百度渠道的游戏竞猜和彩票相关去除掉--Start---*/
                        if ("B1001".equals(MyApp.channelNumber) || "B1002".equals(MyApp.channelNumber) || "B1003".equals(MyApp.channelNumber) || "Q01116".equals(MyApp.channelNumber)) {
                            // 处理条目入口
                            Iterator<HomeOtherListsEntity> iteratorItem = mHomePagerEntity.getOtherLists().iterator();
                            while (iteratorItem.hasNext()) {
                                HomeOtherListsEntity nextEntity = iteratorItem.next();
                                if (nextEntity.getContent().getLabType() == 3 || nextEntity.getContent().getLabType() == 7) {
                                    iteratorItem.remove();
                                }
                            }
                            // 处理菜单入口
                            while (iterator.hasNext()) {
                                HomeContentEntity b = iterator.next();
                                if ("遊戲競猜".equals(b.getTitle()) || "游戏竞猜".equals(b.getTitle()) ||
                                        "30".equals(b.getJumpAddr()) ||
                                        "31".equals(b.getJumpAddr()) ||
                                        "32".equals(b.getJumpAddr()) ||
                                        "33".equals(b.getJumpAddr()) ||
                                        "34".equals(b.getJumpAddr()) ||
                                        "35".equals(b.getJumpAddr()) ||
                                        "36".equals(b.getJumpAddr()) ||
                                        "37".equals(b.getJumpAddr()) ||
                                        "38".equals(b.getJumpAddr()) ||
                                        "39".equals(b.getJumpAddr()) ||
                                        "310".equals(b.getJumpAddr()) ||
                                        "311".equals(b.getJumpAddr()) ||
                                        "312".equals(b.getJumpAddr()) ||
                                        "313".equals(b.getJumpAddr()) ||
                                        "314".equals(b.getJumpAddr()) ||
                                        "315".equals(b.getJumpAddr()) ||
                                        "316".equals(b.getJumpAddr()) ||
                                        "317".equals(b.getJumpAddr()) ||
                                        "318".equals(b.getJumpAddr()) ||
                                        "319".equals(b.getJumpAddr()) ||
                                        "320".equals(b.getJumpAddr()) ||
                                        "321".equals(b.getJumpAddr()) ||
                                        "322".equals(b.getJumpAddr()) ||
                                        "323".equals(b.getJumpAddr()) ||
                                        "324".equals(b.getJumpAddr()) ||
                                        "325".equals(b.getJumpAddr()) ||
                                        "326".equals(b.getJumpAddr()) ||
                                        "327".equals(b.getJumpAddr()) ||
                                        "328".equals(b.getJumpAddr()) ||
                                        "329".equals(b.getJumpAddr()) ||
                                        "330".equals(b.getJumpAddr()) ||
                                        "331".equals(b.getJumpAddr()) ||
                                        "332".equals(b.getJumpAddr())) {
                                    iterator.remove();
                                }
                            }
                        }
                        /**----将百度渠道的游戏竞猜和彩票相关去除掉--End---*/
                        PreferenceUtil.commitString(AppConstants.HOME_PAGER_DATA_KEY, jsonObject);// 保存首页缓存数据
                        L.d("xxx", "保存数据到本地！jsonObject:" + jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isAuditHandle(mHomePagerEntity);
                    if (mHomePagerEntity.getResult() == 200) {
                        switch (num) {
                            case 0:// 首次加载
                                mHandler.sendEmptyMessage(LOADING_DATA_SUCCESS);
                                break;
                            case 1:// 下拉刷新
                                mHandler.sendEmptyMessage(REFRES_DATA_SUCCESS);
                                break;
                            case 2: //产品建议界面返回
                                // 只是为了请求一下新数据。不做其他处理
                                mHandler.sendEmptyMessage(REFRESH_ADVICE);
                                break;
                        }
                    } else {
                        mHandler.sendEmptyMessage(LOADING_DATA_ERROR);// 加载失败
                    }
                } else {
                    mHandler.sendEmptyMessage(LOADING_DATA_ERROR);// 加载失败
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                // 请求失败
                L.d("xxx", "请求失败");
                mHandler.sendEmptyMessage(LOADING_DATA_ERROR);// 加载失败
            }
        });
    }


    /**
     * 审核数据处理
     *
     * @审核中需要隐藏彩票相关内容
     */
    private void isAuditHandle(HomePagerEntity jsonObject) {
        // 过虑彩票菜单
        HomeMenusEntity menusEntity = new HomeMenusEntity();
        List<HomeContentEntity> contentList = new ArrayList<>();
        if (jsonObject.getMenus() == null || jsonObject.getMenus().getContent() == null || jsonObject.getMenus().getContent().size() == 0) {
            // 不处理
        } else {
            for (int i = 0, len = jsonObject.getMenus().getContent().size(); i < len; i++) {
                HomeContentEntity homeContentEntity = jsonObject.getMenus().getContent().get(i);
                if (homeContentEntity != null && homeContentEntity.getJumpAddr() != null) {
                    switch (homeContentEntity.getJumpAddr()) {
                        case "30":
                        case "31":
                        case "350":
                        case "32":
                        case "33":
                        case "34":
                        case "35":
                        case "36":
                        case "37":
                        case "38":
                        case "39":
                        case "310":
                        case "311":
                        case "312":
                        case "313":
                        case "314":
                        case "315":
                        case "316":
                        case "317":
                        case "318":
                        case "319":
                        case "320":
                        case "321":
                        case "322":
                        case "323":
                            // 正在审核中，不显示彩票信息
                            if ("false".equals(jsonObject.getIsAudit())) {
                                contentList.add(homeContentEntity);
                            }
                            break;
                        default:
                            contentList.add(homeContentEntity);
                            break;
                    }
                }
            }
            menusEntity.setContent(contentList);
            menusEntity.setResult(jsonObject.getMenus().getResult());
        }
        // 过虑彩票条目
        List<HomeOtherListsEntity> otherList = new ArrayList<>();
        if (jsonObject.getOtherLists() == null || jsonObject.getOtherLists().size() == 0) {
            // 不处理
        } else {
            for (int i = 0, len = jsonObject.getOtherLists().size(); i < len; i++) {
                HomeOtherListsEntity homeOtherListsEntity = jsonObject.getOtherLists().get(i);
                if (homeOtherListsEntity != null) {
                    if (homeOtherListsEntity.getContent() != null) {
                        if (homeOtherListsEntity.getContent().getLabType() == 3 && "true".equals(jsonObject.getIsAudit())) {
                            // 正在审核中，不显示彩票信息
                        } else {
                            // 审核完成，显示全部内容
                            otherList.add(homeOtherListsEntity);
                        }
                    }
                }
            }
        }
        mHomePagerEntity.setOtherLists(otherList);
        mHomePagerEntity.setMenus(menusEntity);
    }


    /**
     * 获取本地数据
     */
    public void readObjectFromFile() {
        L.d("xxx", "获取本地数据.");
        String jsondata = PreferenceUtil.getString(AppConstants.HOME_PAGER_DATA_KEY, null);
        if (jsondata != null) {
            mHomePagerEntity = JSON.parseObject(jsondata, HomePagerEntity.class);
            mListBaseAdapter = new HomeListBaseAdapter(mContext, mHomePagerEntity);
            //设置跳转监听
            mListBaseAdapter.setToProductListener(mListener);
            home_page_list.setAdapter(mListBaseAdapter);

        } else {
            showDefData();
        }
    }


    /**
     * 无网络时显示默认数据
     */
    private void showDefData() {
        try {
            String defDataJson = "{\"banners\": {\"content\": [{\"jumpType\": 0,\"picUrl\": \"xxx\"}],\"result\": 200},\"menus\": {\"content\": [{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"},{\"jumpType\": 2,\"picUrl\": \"xxx\",\"title\": \"\"}],\"result\": 200},\"otherLists\": [{\"content\": {\"bodys\": [{\"date\": \"\",\"guestHalfScore\": 0,\"guestId\": 177,\"guestLogoUrl\": \"xxx\",\"guestScore\": 0,\"guestteam\": \"\",\"homeHalfScore\": 0,\"homeId\": 180,\"homeLogoUrl\": \"xxx\",\"homeScore\": 0,\"hometeam\": \"\",\"jumpType\": 2,\"raceColor\": \"#9933FF\",\"raceId\": \"1\",\"racename\": \"\",\"statusOrigin\": \"0\",\"thirdId\": \"307689\",\"time\": \"\"},{\"date\": \"\",\"guestHalfScore\": 0,\"guestId\": 6164,\"guestLogoUrl\": \"xxx\",\"guestScore\": 0,\"guestteam\": \"\",\"homeHalfScore\": 0,\"homeId\": 6162,\"homeLogoUrl\": \"xxx\",\"homeScore\": 0,\"hometeam\": \"\",\"jumpType\": 2,\"raceColor\": \"#000080\",\"raceId\": \"511\",\"racename\": \"\",\"statusOrigin\": \"0\",\"thirdId\": \"312127\",\"time\": \"\"}],\"labType\": 1},\"result\": 200},{\"content\": {\"bodys\": [{\"date\": \"\",\"jumpType\": 1,\"time\": \"\",\"title\": \"\"},{\"date\": \"\", \"jumpType\": 1,\"picUrl\": \"xxx\",\"time\": \"\",\"title\": \"\"}],\"labType\": 2},\"result\": 200}], \"result\": 200}";
            mHomePagerEntity = JSON.parseObject(defDataJson, HomePagerEntity.class);

            mListBaseAdapter = new HomeListBaseAdapter(mContext, mHomePagerEntity);

            //设置跳转监听
            mListBaseAdapter.setToProductListener(mListener);

            home_page_list.setAdapter(mListBaseAdapter);
        } catch (Exception e) {
            L.d("json解析失败：" + e.getMessage());
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING_DATA_START:
                    mSwipeRefreshLayout.setRefreshing(true);
                    break;
                case LOADING_DATA_SUCCESS:
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (mHomePagerEntity != null) {
                        mListBaseAdapter = new HomeListBaseAdapter(mContext, mHomePagerEntity);
                        //设置跳转监听
                        mListBaseAdapter.setToProductListener(mListener);
                        home_page_list.setAdapter(mListBaseAdapter);
                    }
                    break;
                case LOADING_DATA_ERROR:
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.number_net_error), Toast.LENGTH_SHORT).show();
                    break;
                case REFRES_DATA_SUCCESS:// 下拉刷新
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (mHomePagerEntity != null) {
                        mListBaseAdapter = new HomeListBaseAdapter(mContext, mHomePagerEntity);
                        //设置跳转监听
                        mListBaseAdapter.setToProductListener(mListener);
                        home_page_list.setAdapter(mListBaseAdapter);
                    }
                    break;
                case REFRESH_ADVICE: //刷新首页点赞数
                    for (int i = 0, len = mHomePagerEntity.getOtherLists().size(); i < len; i++) {
                        int labType = mHomePagerEntity.getOtherLists().get(i).getContent().getLabType();// 获取类型
                        List<HomeBodysEntity> bodys = mHomePagerEntity.getOtherLists().get(i).getContent().getBodys();
                        for (int j = 0, len1 = bodys.size(); j < len1; j++) {
                            if (labType == 5) { //产品建议
                                mListBaseAdapter.addLike(bodys.get(0), bodys);
                                mListBaseAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onRefresh() {
        getRequestData(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_home_pic:
                MobclickAgent.onEvent(mContext, "HomePager_User_Info_Start");
                startActivityForResult(new Intent(mContext, HomeUserOptionsActivity.class), REQUESTCODE_LOGIN);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_LOGIN) {
                // 登录成功返回
                L.d(TAG, "登录成功");
                iv_home_pic.setImageResource(R.mipmap.login);
            } else if (requestCode == REQUESTCODE_LOGOUT) {
                L.d(TAG, "注销成功");
                iv_home_pic.setImageResource(R.mipmap.logout);
            }
        } else if (resultCode == ProductAdviceActivity.RESULT_CODE) {
            getRequestData(2);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }
}
