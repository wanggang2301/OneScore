package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.custom.CustomMyDataAdapter;
import com.hhly.mlottery.bean.basket.BasketAllOddBean;
import com.hhly.mlottery.bean.basket.BasketOddBean;
import com.hhly.mlottery.bean.custombean.CustomMineBean.CustomMatchScore;
import com.hhly.mlottery.bean.custombean.CustomMineBean.CustomMineDataBean;
import com.hhly.mlottery.bean.custombean.CustomMineBean.CustomMineFirstDataBean;
import com.hhly.mlottery.bean.custombean.CustomMineBean.CustomMineScondDataBean;
import com.hhly.mlottery.bean.custombean.CustomMineBean.CustomMineThirdDataBean;
import com.hhly.mlottery.bean.custombean.customlistdata.CustomSendDataBean;
import com.hhly.mlottery.bean.focusAndPush.BasketballConcernListBean;
import com.hhly.mlottery.bean.websocket.WebBasketAllOdds;
import com.hhly.mlottery.bean.websocket.WebBasketMatch;
import com.hhly.mlottery.bean.websocket.WebBasketOdds;
import com.hhly.mlottery.bean.websocket.WebBasketOdds5;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.basketballframe.basketnewfragment.BasketballFocusNewFragment;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CustomListEvent;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.CustomDetailsEvent;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.LoadMoreRecyclerView;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by yixq on 2016/12/5.
 * mail：yixq@13322.com
 * describe: 我的定制页
 */

public class CustomActivity extends BaseWebSocketActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    private LoadMoreRecyclerView mCustomRecycle;
    private LinearLayoutManager mLinearLayoutManager;
    private CustomMyDataAdapter mAdapter;
    private List mFirstData;
    private TextView mCustomText;
    private ImageView mBack;
    private LinearLayout mErrorll;
    private TextView mrefshTxt;
    private TextView mNoData;
    private LinearLayout mNoCustom;
    private TextView mCustomTxt;
    private ExactSwipeRefreshLayout mRefresh;

    private final static int VIEW_STATUS_LOADING = 1;//请求中
    private final static int VIEW_STATUS_NET_NO_DATA = 2;//暂无数据
    private final static int VIEW_STATUS_SUCCESS = 3;//请求成功
    private final static int VIEW_STATUS_NET_ERROR = 4;//请求失败
    private final static int VIEW_STATUS_CUSTOM_NO_DATA = 5;//暂无定制
    private final static int VIEW_STATUS_CUSTOM_REFRESH_ONCLICK = 6;//点击刷新
    private LinearLayout monClickLoading;
    private String sendUrl;
    private Map<String, String> sendMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * 推送  必须定义在 super.onCreate前面，否则 订阅不成功
         */
        setWebSocketUri(BaseURLs.WS_SERVICE);
//        setWebSocketUri("ws://192.168.10.242:61634");
//        setTopic("USER.topic.basketball");
        setTopic(BaseUserTopics.customBasketball);
        super.onCreate(savedInstanceState);
        //注册
        EventBus.getDefault().register(this);

        setContentView(R.layout.custom_activity);

        initView();

        setState(VIEW_STATUS_LOADING);
//        initData();
        mLoadHandler.postDelayed(mRun, 500);
    }

    private void initView() {

        /**头部布局*/
        TextView mCustomTitle = (TextView) findViewById(R.id.public_txt_title);
        mCustomTitle.setText(getResources().getString(R.string.my_focus));

        mBack = (ImageView) findViewById(R.id.public_img_back);
        mBack.setOnClickListener(this);

        mCustomText = (TextView) findViewById(R.id.right_tv);
        mCustomText.setVisibility(View.VISIBLE);
        mCustomText.setText(getResources().getString(R.string.custom_redact_cus));
        mCustomText.setOnClickListener(this);

        //下拉控件
        mRefresh = (ExactSwipeRefreshLayout) findViewById(R.id.custom_refresh_layout);
        mRefresh.setColorSchemeResources(R.color.bg_header);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getApplicationContext(), StaticValues.REFRASH_OFFSET_END));

        /**网络、数据 异常布局*/
        // 网络不给力提示
        mErrorll = (LinearLayout) findViewById(R.id.error_layout);
        //刷新点击按键
        mrefshTxt = (TextView) findViewById(R.id.reloading_txt);
        mrefshTxt.setOnClickListener(this);
        //暂无数据提示
        mNoData = (TextView) findViewById(R.id.nodata_txt);
        //还未定制任何赛事
        mNoCustom = (LinearLayout) findViewById(R.id.to_custom_ll);
        //去定制按键
        mCustomTxt = (TextView) findViewById(R.id.to_custom);
        mCustomTxt.setOnClickListener(this);

        //点击刷新是显示正在加载中....
        monClickLoading = (LinearLayout) findViewById(R.id.custom_loading_ll);


        /**内容布局*/
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);

        mCustomRecycle = (LoadMoreRecyclerView) findViewById(R.id.custom_recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCustomRecycle.setLayoutManager(mLinearLayoutManager);
    }

    /**
     * 数据展示状态设置
     */
    private void setState(int state) {

        if (state == VIEW_STATUS_LOADING) {
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(true);
        } else if (state == VIEW_STATUS_SUCCESS) {
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(false);
        } else if (state == VIEW_STATUS_CUSTOM_REFRESH_ONCLICK) {
            mRefresh.setVisibility(View.GONE);
            mRefresh.setRefreshing(true);
        } else {
            mRefresh.setVisibility(View.GONE);
            mRefresh.setRefreshing(false);
        }

        monClickLoading.setVisibility((state == VIEW_STATUS_CUSTOM_REFRESH_ONCLICK) ? View.VISIBLE : View.GONE);
        mErrorll.setVisibility((state == VIEW_STATUS_NET_ERROR) ? View.VISIBLE : View.GONE);
        mNoData.setVisibility((state == VIEW_STATUS_NET_NO_DATA) ? View.VISIBLE : View.GONE);
        mNoCustom.setVisibility((state == VIEW_STATUS_CUSTOM_NO_DATA) ? View.VISIBLE : View.GONE);


    }


    /**
     * 子线程 处理数据加载
     */
    Handler mLoadHandler = new Handler();
    Handler mCustomHandler = new Handler();

    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            initData(0);
        }
    };
    private Runnable mCuntomRun = new Runnable() {
        @Override
        public void run() {
            initData(1);
        }
    };


    private void initData(final int type) {

        getBasketballUserConcern();//进入详情页时先获取该账户的关注id （多设备关注同步）

//        String url = "http://192.168.10.242:8181/mlottery/core/basketballCommonMacth.findBasketballMyConcernMatch.do" ;
//                "lang=zh&userId=13714102745&deviceId=21126FC4-DAF0-40DC-AF5C-1AD33EFB5F67";
        String url = BaseURLs.CUSTOM_MINE_CUS_URL;

        Map<String, String> map = new HashMap<>();
        String userid = AppConstants.register.getUser().getUserId();
        String deviceid = AppConstants.deviceToken;
        map.put("userId", userid);
        map.put("deviceId", deviceid);

        VolleyContentFast.requestJsonByGet(url, map, new VolleyContentFast.ResponseSuccessListener<CustomMineDataBean>() {


            @Override
            public void onResponse(CustomMineDataBean jsonData) {

                if (jsonData == null || jsonData.getData() == null) {
                    setState(VIEW_STATUS_NET_NO_DATA);
                    return;
                } else if (jsonData.getData().getLeagueItem() == null || jsonData.getData().getLeagueItem().size() == 0) {
                    setState(VIEW_STATUS_CUSTOM_NO_DATA);

                    return;
                } else {

                    mFirstData = new ArrayList<>();
                    mFirstData = jsonData.getData().getLeagueItem();

                    /** type = 1  是完成定制后重新请求数据 适配器重建*/
                    if (mAdapter == null || type == 1) {

                        if (mAdapter != null) {
                            /***在(删除/在我的数据结构中插入数据），需要清理回收池，然后(notify)
                             * recycleView处理数据变更时内部绑定view会复用，使用新数据时需要清理回收池，否则会 IndexOutOfBoundsException */
                            mCustomRecycle.getRecycledViewPool().clear();
                            mAdapter.notifyDataSetChanged();
                        }

                        mAdapter = new CustomMyDataAdapter(getApplicationContext(), mFirstData);
                        mCustomRecycle.setAdapter(mAdapter);
                    } else {
                        updateAdapter();
                    }

                    setOnItemClick(mFirstData);

                    /**必须先添加中间层，再添加最内层 顺序不可逆*/
                    addSecondTier();

//                    addThirdTier(); // 展开比赛层
                    /**必须先添加中间层，再添加最内层 顺序不可逆*/

                    setState(VIEW_STATUS_SUCCESS);

                    connectWebSocket();
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                setState(VIEW_STATUS_NET_ERROR);
            }
        }, CustomMineDataBean.class);
    }

    /**
     * 添加中间层数据（日期层）
     */
    private void addSecondTier() {
        /**
         * 记录添加中间层（日期层）的位置
         */
        int addSecondIndex = 1;
        for (int i = 0; i < mFirstData.size(); i++) {
            if (mFirstData.get(i) instanceof CustomMineFirstDataBean) {
                CustomMineFirstDataBean firstData = (CustomMineFirstDataBean) mFirstData.get(i);
                mAdapter.addAllChild(firstData.getMatchData(), addSecondIndex);
                addSecondIndex += (firstData.getMatchData().size() + 1);
            }
        }
    }

    /**
     * 添加最内层数据（比赛层）
     */
    private void addThirdTier() {
        /**
         * 添加最内层的位置
         */
        int addThirdIndex = 1;
        /**
         * 遍历时是否是最外层，是最外层时设为 1 ，添加子项时位置往后一位
         */
        int isGround = 0;

        for (int i = 0; i < mFirstData.size(); i++) {

            if (mFirstData.get(i) instanceof CustomMineScondDataBean) {

                CustomMineScondDataBean secondData = (CustomMineScondDataBean) mFirstData.get(i);

                mAdapter.addAllChild(secondData.getMatchItems(), (addThirdIndex + isGround));

                addThirdIndex += (secondData.getMatchItems().size() + 1 + isGround);
                L.d("yxq22222", " x == " + addThirdIndex + " y == " + isGround + " size == " + secondData.getMatchItems().size());

                isGround = 0;
            }
            if (mFirstData.get(i) instanceof CustomMineFirstDataBean) {
                isGround += 1; /** += 防止出现中间有某联赛下有无数据情况 添加数据时位置不对情况 */
            }
        }
    }


    private void setOnItemClick(final List<?> mData) {

        mAdapter.setOnItemClickLitener(new CustomMyDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ImageView isOpen) {

                if (mData.get(position) instanceof CustomMineFirstDataBean) {//判断点击的是不是最外层 0

                    CustomMineFirstDataBean firstData = (CustomMineFirstDataBean) mData.get(position);
                    if (mData.size() == (position + 1)) {
                        //如果是最后一项

                        if (isOpen != null) {
                            isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_2);
                        }

                        mAdapter.addAllChild(firstData.getMatchData(), position + 1);
                    } else {
                        if (mData.get(position + 1) instanceof CustomMineFirstDataBean) {//折叠状态

                            if (isOpen != null) {
                                isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_2);
                            }

                            mAdapter.addAllChild(firstData.getMatchData(), position + 1);
                        } else {
                            /**
                             * 记录子层数据（赛事层），若日期层打开状态需要连比赛层一并删除
                             */
                            int thirdDataSize = 0;
                            for (CustomMineScondDataBean data : firstData.getMatchData()) {
                                if (data.isUnfold()) {
                                    thirdDataSize += data.getMatchItems().size();
                                    L.d("yxq==========>>> ", "a+size " + data.getMatchItems().size());
                                    L.d("yxq==========>>> ", "a+过程 " + thirdDataSize);

                                    data.setUnfold(false);// 遍历过后状态改为 false （折叠状态） ps:防止二次点击状态冲突
                                }
                            }
                            L.d("yxq==========>>> ", "删除 " + (thirdDataSize + firstData.getMatchData().size()));
                            if (isOpen != null) {
                                isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_1);
                            }

                            mAdapter.deleteAllChild(position + 1, thirdDataSize + firstData.getMatchData().size());//删除 二级item 和 已展开的三级item 条数
                        }
                    }

                } else if (mData.get(position) instanceof CustomMineScondDataBean) { //判断点击的是不是 中间层 0
                    CustomMineScondDataBean parent = (CustomMineScondDataBean) mData.get(position);
                    if ((position + 1) == mData.size()) {//判断是否为最后一个元素
                        parent.setUnfold(true);

                        if (isOpen != null) {
                            isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_2);
                        }

                        mAdapter.addAllChild(parent.getMatchItems(), position + 1);
                    } else {
                        if (mData.get(position + 1) instanceof CustomMineFirstDataBean || mData.get(position + 1) instanceof CustomMineScondDataBean) {//为折叠状态需要添加儿子
                            parent.setUnfold(true);

                            if (isOpen != null) {
                                isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_2);
                            }

                            mAdapter.addAllChild(parent.getMatchItems(), position + 1);
                        } else if (mData.get(position + 1) instanceof CustomMineThirdDataBean) {//为展开状态需要删除儿子
                            parent.setUnfold(false);

                            if (isOpen != null) {
                                isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_1);
                            }

                            mAdapter.deleteAllChild(position + 1, parent.getMatchItems().size());
                        }
                    }
                } else {// 否则为最内层（赛事层）比赛的点击事件这里写

                    CustomMineThirdDataBean parent = (CustomMineThirdDataBean) mData.get(position);
                    Intent intent = new Intent(CustomActivity.this, BasketDetailsActivityTest.class);
                    intent.putExtra(BasketDetailsActivityTest.BASKET_THIRD_ID, parent.getThirdId());//跳转到详情
                    intent.putExtra(BasketDetailsActivityTest.BASKET_MATCH_STATUS, parent.getMatchStatus());//跳转到详情
                    intent.putExtra("currentfragment", 4);//代表定制页跳转
                    intent.putExtra(BasketDetailsActivityTest.BASKET_MATCH_LEAGUEID, parent.getLeagueId());
                    intent.putExtra(BasketDetailsActivityTest.BASKET_MATCH_MATCHTYPE, parent.getMatchType());
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                }
            }
        });
    }

    /**
     * 请求关注列表。登录后跟刷新，都会请求
     */
    public void getBasketballUserConcern() {
        //请求后台，及时更新关注赛事内容
        String userId = "";
        if (AppConstants.register != null && AppConstants.register != null && AppConstants.register.getUser() != null) {
            userId = AppConstants.register.getUser().getUserId();
        }
        if (userId != null && userId != "") {
            String url = " http://192.168.31.68:8080/mlottery/core/androidBasketballMatch.findConcernVsThirdIds.do";
            String deviceId = AppConstants.deviceToken;
            //devicetoken 友盟。
            String umengDeviceToken = PreferenceUtil.getString(AppConstants.uMengDeviceToken, "");
            Map<String, String> params = new HashMap<>();
            params.put("userId", userId);
            params.put("deviceId", deviceId);
            VolleyContentFast.requestJsonByPost(BaseURLs.BASKET_FIND_MATCH, params, new VolleyContentFast.ResponseSuccessListener<BasketballConcernListBean>() {
                @Override
                public void onResponse(BasketballConcernListBean jsonObject) {
                    if (jsonObject.getResult().equals("200")) {
                        //将关注写入文件
                        StringBuffer sb = new StringBuffer();
                        for (String thirdId : jsonObject.getConcerns()) {
                            if ("".equals(sb.toString())) {
                                sb.append(thirdId);
                            } else {
                                sb.append("," + thirdId);
                            }
                        }
                        PreferenceUtil.commitString(BasketballFocusNewFragment.BASKET_FOCUS_IDS, sb.toString());
//                        focusCallback();
                    }
                }
            }, new VolleyContentFast.ResponseErrorListener() {
                @Override
                public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                }
            }, BasketballConcernListBean.class);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册 取消绑定
        EventBus.getDefault().unregister(this);
        mLoadHandler.removeCallbacksAndMessages(null);
        mCustomHandler.removeCallbacksAndMessages(null);
        mSocketHandler.removeCallbacksAndMessages(null);
    }

    /*****************************************以下推送方法**********************************************/

    @Override
    protected void onTextResult(String text) {

        L.d("yxq===1221=== ", text);

        if (mAdapter == null) {
            return;
        }
        String type = "";
        try {
            JSONObject jsonObject = new JSONObject(text);
            type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!"".equals(type)) {
            Message msg = Message.obtain();
            msg.obj = text;
            msg.arg1 = Integer.parseInt(type);
            mSocketHandler.sendMessage(msg);
        }
    }

    @Override
    protected void onConnectFail() {

    }

    @Override
    protected void onDisconnected() {

    }

    @Override
    protected void onConnected() {

    }

    Handler mSocketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            L.e(TAG, "__handleMessage__");
            L.e(TAG, "msg.arg1 = " + msg.arg1);
            if (msg.arg1 == 100) {  //type 为100 ==> 比分推送
                String ws_json = (String) msg.obj;
                L.e(TAG, "ws_json =CustomScore " + ws_json);
                WebBasketMatch mWebBasketMatch = null;
                try {
                    mWebBasketMatch = JSON.parseObject(ws_json, WebBasketMatch.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    mWebBasketMatch = JSON.parseObject(ws_json, WebBasketMatch.class);
                }

                updateListViewItemStatus(mWebBasketMatch);  //比分更新
            } else if (msg.arg1 == 101) {  //type 为101 ==> 赔率推送
                String ws_json = (String) msg.obj;
                L.e(TAG, "ws_json" + ws_json);
                WebBasketOdds mWebBasketOdds = null;
                try {
                    mWebBasketOdds = JSON.parseObject(ws_json, WebBasketOdds.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    mWebBasketOdds = JSON.parseObject(ws_json, WebBasketOdds.class);
                }
                updateListViewItemOdd(mWebBasketOdds);
            }


        }
    };

    private void updateListViewItemStatus(WebBasketMatch webBasketMatch) {
        Map<String, String> data = webBasketMatch.getData();
        synchronized (mFirstData) {

            for (int i = 0; i < mFirstData.size(); i++) {
                if (mFirstData.get(i) instanceof CustomMineFirstDataBean) {

                    CustomMineFirstDataBean firstdata = (CustomMineFirstDataBean) mFirstData.get(i);
                    for (CustomMineScondDataBean seconddata : firstdata.getMatchData()) {
                        for (CustomMineThirdDataBean third : seconddata.getMatchItems()) {

                            if (third.getThirdId().equals(webBasketMatch.getThirdId())) {

                                if (third.getMatchScore() != null) {
                                    String newscoreguest = third.getMatchScore().getGuestScore() + ""; //推送之前的比分客队
                                    String newscorehome = third.getMatchScore().getHomeScore() + "";

                                    //判断推送前后的比分是否变化, 变化==>开启动画
                                    if (data.get("guestScore") != null && data.get("homeScore") != null) {
                                        if (!data.get("guestScore").equals(newscoreguest)) { //isScroll 为 true：正在滑动  滑动中不启动动画;
                                            third.setGuestAnim(true);
                                        } else {
                                            third.setGuestAnim(false);
                                        }
                                        if (!data.get("homeScore").equals(newscorehome)) {
                                            third.setHomeAnim(true);
                                        } else {
                                            third.setHomeAnim(false);
                                        }
                                        updateMatchStatus(third, data);// 修改Match里面的数据
                                    }
                                } else {
                                    /**
                                     * 未开始（VS）==>开始时候的处理
                                     */
                                    CustomMatchScore score = new CustomMatchScore();

                                    for (Map.Entry<String, String> entry : data.entrySet()) {
                                        switch (entry.getKey()) {
                                            case "guest1":
                                                score.setGuest1(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "guest2":
                                                score.setGuest2(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "guest3":
                                                score.setGuest3(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "guest4":
                                                score.setGuest4(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "guestOt1":
                                                score.setGuestOt1(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "guestOt2":
                                                score.setGuestOt2(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "guestOt3":
                                                score.setGuestOt3(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "guestScore":
                                                score.setGuestScore(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "home1":
                                                score.setHome1(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "home2":
                                                score.setHome2(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "home3":
                                                score.setHome3(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "home4":
                                                score.setHome4(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "homeOt1":
                                                score.setHomeOt1(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "homeOt2":
                                                score.setHomeOt2(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "homeOt3":
                                                score.setHomeOt3(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "homeScore":
                                                score.setHomeScore(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "addTime":
                                                score.setAddTime(Integer.parseInt(entry.getValue()));
                                                break;
                                            case "remainTime":
                                                score.setRemainTime(entry.getValue());
                                                break;
                                            default:
                                                break;
                                        }
                                    }

                                    third.setMatchScore(score);
                                    updateMatchStatus(third, data);
                                }
                                updateAdapter();
                                break;
                            }

                        }
                    }
                }
            }
        }
    }

    private void updateListViewItemOdd(WebBasketOdds webBasketOdds) {
        WebBasketAllOdds data = webBasketOdds.getData();
        synchronized (mFirstData) {

            for (int i = 0; i < mFirstData.size(); i++) {
                if (mFirstData.get(i) instanceof CustomMineFirstDataBean) {

                    CustomMineFirstDataBean firstdata = (CustomMineFirstDataBean) mFirstData.get(i);
                    for (CustomMineScondDataBean seconddata : firstdata.getMatchData()) {

                        for (CustomMineThirdDataBean third : seconddata.getMatchItems()) {

                            if (third.getThirdId().equals(webBasketOdds.getThirdId())) {
                                updateMatchOdd(third, data);
                                updateAdapter();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 更新单个比赛状态
     */
    //比分
    private void updateMatchStatus(CustomMineThirdDataBean matchItems, Map<String, String> data) {
        if (null != data.get("section")) {
            if (!data.get("section").equals("0")) {
                matchItems.setSection(Integer.parseInt(data.get("section")));
            }
        }
        if (null != data.get("matchStatus")) {
            matchItems.setMatchStatus(Integer.parseInt(data.get("matchStatus")) + "");
        }
        if (null != data.get("remainTime") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setRemainTime(data.get("remainTime"));
        }
        if (null != data.get("homeScore") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setHomeScore(Integer.parseInt(data.get("homeScore")));
        }
        if (null != data.get("guestScore") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setGuestScore(Integer.parseInt(data.get("guestScore")));
        }

        if (null != data.get("home1") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setHome1(Integer.parseInt(data.get("home1")));
        }
        if (null != data.get("guest1") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setGuest1(Integer.parseInt(data.get("guest1")));
        }
        if (null != data.get("home2") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setHome2(Integer.parseInt(data.get("home2")));
        }
        if (null != data.get("guest2") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setGuest2(Integer.parseInt(data.get("guest2")));
        }
        if (null != data.get("home3") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setHome3(Integer.parseInt(data.get("home3")));
        }
        if (null != data.get("guest3") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setGuest3(Integer.parseInt(data.get("guest3")));
        }
        if (null != data.get("home4") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setHome4(Integer.parseInt(data.get("home4")));
        }
        if (null != data.get("guest4") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setGuest4(Integer.parseInt(data.get("guest4")));
        }
        if (null != data.get("homeOt1") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setHomeOt1(Integer.parseInt(data.get("homeOt1")));
        }
        if (null != data.get("guestOt1") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setGuestOt1(Integer.parseInt(data.get("guestOt1")));
        }
        if (null != data.get("homeOt2") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setHomeOt2(Integer.parseInt(data.get("homeOt2")));
        }
        if (null != data.get("guestOt2") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setGuestOt2(Integer.parseInt(data.get("guestOt2")));
        }
        if (null != data.get("homeOt3") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setHomeOt3(Integer.parseInt(data.get("homeOt3")));
        }
        if (null != data.get("guestOt3") && matchItems.getMatchScore() != null) {
            matchItems.getMatchScore().setGuestOt3(Integer.parseInt(data.get("guestOt3")));
        }
    }

    //赔率
    private void updateMatchOdd(CustomMineThirdDataBean matchItems, WebBasketAllOdds dataodd) {
        L.e(TAG, "update matchchildern odd id = " + matchItems.getThirdId());

        List<WebBasketOdds5> mOddsList = new ArrayList<>();
        WebBasketOdds5 mAsiaSize = dataodd.getAsiaSize(); //大小球
        WebBasketOdds5 mEuro = dataodd.getEuro();//欧赔
        WebBasketOdds5 mAsiaLet = dataodd.getAsiaLet();//亚盘
        mOddsList.add(mAsiaLet);
        mOddsList.add(mEuro);
        mOddsList.add(mAsiaSize);

        for (WebBasketOdds5 map : mOddsList) {
            if (map != null) {
                if (map.getCrown() != null) {
                    if (map.getCrown().get("handicap").equals("asiaLet") && matchItems.getMatchOdds() != null) {
                        Map<String, BasketAllOddBean> odds = matchItems.getMatchOdds();
                        BasketAllOddBean odd = odds.get("asiaLet");

                        if (odd == null) {//
                            odd = new BasketAllOddBean();
                            BasketOddBean modd = odd.getCrown();
                            if (modd == null) {
                                modd = new BasketOddBean();
                            }
                            modd.setHandicap("asiaLet");
                            modd.setHandicapValue(map.getCrown().get("handicapValue"));
                            modd.setRightOdds(map.getCrown().get("rightOdds"));
                            modd.setLeftOdds(map.getCrown().get("leftOdds"));
                            odd.setCrown(modd);
                            odds.put("asiaLet", odd);
                        }

                        if (odd.getCrown() != null) {
                            L.w(TAG, "odd = " + odd);
                            odd.getCrown().setLeftOdds(map.getCrown().get("leftOdds"));
                            odd.getCrown().setRightOdds(map.getCrown().get("rightOdds"));
                            odd.getCrown().setHandicapValue(map.getCrown().get("handicapValue"));
                        }

                    } else if (map.getCrown().get("handicap").equals("asiaSize") && matchItems.getMatchOdds() != null) {
                        Map<String, BasketAllOddBean> odds = matchItems.getMatchOdds();
                        BasketAllOddBean odd = odds.get("asiaSize");
                        if (odd == null) {//
                            odd = new BasketAllOddBean();
                            BasketOddBean modd = odd.getCrown();
                            if (modd == null) {
                                modd = new BasketOddBean();
                            }
                            modd.setHandicap("asiaSize");
                            modd.setHandicapValue(map.getCrown().get("handicapValue"));
                            modd.setRightOdds(map.getCrown().get("rightOdds"));
                            modd.setLeftOdds(map.getCrown().get("leftOdds"));
                            odd.setCrown(modd);
                            odds.put("asiaSize", odd);
                        }

                        if (odd.getCrown() != null) {
                            L.w(TAG, "odd = " + odd);
                            odd.getCrown().setLeftOdds(map.getCrown().get("leftOdds"));
                            odd.getCrown().setRightOdds(map.getCrown().get("rightOdds"));
                            odd.getCrown().setHandicapValue(map.getCrown().get("handicapValue"));
                        }
                    }
                }
                if (map.getEuro() != null) {
                    if (map.getEuro().get("handicap").equals("euro") && matchItems.getMatchOdds() != null) {
                        Map<String, BasketAllOddBean> odds = matchItems.getMatchOdds();
                        BasketAllOddBean odd = odds.get("euro");
                        if (odd == null) {//
                            odd = new BasketAllOddBean();
                            BasketOddBean modd = odd.getEuro();
                            if (modd == null) {
                                modd = new BasketOddBean();
                            }
                            modd.setHandicap("euro");
                            modd.setRightOdds(map.getEuro().get("rightOdds"));
                            modd.setLeftOdds(map.getEuro().get("leftOdds"));
                            odd.setEuro(modd);
                            odds.put("euro", odd);
                        }
                        if (odd.getEuro() != null) {
                            L.w(TAG, "odd = " + odd);
                            odd.getEuro().setLeftOdds(map.getEuro().get("leftOdds"));
                            odd.getEuro().setRightOdds(map.getEuro().get("rightOdds"));
                        }
                    }
                }
            }
        }
    }

    /*****************************************以上推送方法**********************************************/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_tv:
                startActivity(new Intent(CustomActivity.this, CustomListActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                break;
            case R.id.public_img_back:

                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.reloading_txt:
                if (isSecondData) {
                    setState(VIEW_STATUS_CUSTOM_REFRESH_ONCLICK);
                    secondInitData(sendUrl, sendMap);
                } else {
                    setState(VIEW_STATUS_CUSTOM_REFRESH_ONCLICK);
                    mLoadHandler.postDelayed(mRun, 500);
                }
                break;
            case R.id.to_custom:
                startActivity(new Intent(CustomActivity.this, CustomListActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                break;
        }
    }

    /**
     * 设置返回
     */
    public void onEventMainThread(CustomListEvent customListEvent) {
        /**正在加载中*/
        setState(VIEW_STATUS_CUSTOM_REFRESH_ONCLICK);

        L.d("yxq123456 ", customListEvent.getmLeagueMsg() + " **** " + customListEvent.getmTeamMsg());
        sendUrl = BaseURLs.CUSTOM_SENDID_CUS_URL;
        String userids = AppConstants.register.getUser().getUserId();
        String deviceid = AppConstants.deviceToken;
        String devicetoken = PreferenceUtil.getString(AppConstants.uMengDeviceToken, "");

        sendMap = new HashMap<>();
        sendMap.put("userId", userids);
        sendMap.put("deviceId", deviceid);
        sendMap.put("deviceToken", devicetoken);
        sendMap.put("teamIdsByleagueId", customListEvent.getmTeamMsg());//关注球队的id
        sendMap.put("leagueIds", customListEvent.getmLeagueMsg());//关注联赛的id

        secondInitData(sendUrl, sendMap);
    }

    /**
     * 详情页返回
     */
    public void onEventMainThread(CustomDetailsEvent event) {
    }

    /**
     * 定制完成请求的接口
     *
     * @param url
     * @param map
     */

    private boolean isSecondData = false; //记录是否是在定制完成后的请求失败，即 定制id发送不成功时 刷新接口需要先发送id

    private void secondInitData(String url, Map<String, String> map) {
        /**这里从列表回来先发送 id 得到返回码 200 再重新请求本页接口数据*/

        VolleyContentFast.requestJsonByGet(url, map, new VolleyContentFast.ResponseSuccessListener<CustomSendDataBean>() {
            @Override
            public void onResponse(CustomSendDataBean bean) {

                if (bean.getResult() == 200) {
                    isSecondData = false;
                    mCustomHandler.postDelayed(mCuntomRun, 500);
                }
                L.d("yxq===========请求成功");
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                isSecondData = true;
                setState(VIEW_STATUS_NET_ERROR);
                L.d("yxq===========请求失败");
            }
        }, CustomSendDataBean.class);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        setState(VIEW_STATUS_LOADING);
        mCustomHandler.postDelayed(mCuntomRun, 500);
    }

    private void updateAdapter() {
        if (mAdapter == null) {
            return;
        }
        mAdapter.setData(mFirstData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
