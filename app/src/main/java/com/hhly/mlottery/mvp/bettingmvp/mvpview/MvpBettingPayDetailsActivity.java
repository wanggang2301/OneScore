package com.hhly.mlottery.mvp.bettingmvp.mvpview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BaseActivity;
import com.hhly.mlottery.activity.LoginActivity;
import com.hhly.mlottery.bean.bettingbean.BettingDetailsBean;
import com.hhly.mlottery.bean.bettingbean.BettingListDataBean;
import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.mvp.bettingmvp.MView;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.BettingBuyResultEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.LoadingResultEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.mvppresenter.MvpBettingPayDetailsPresenter;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.SignUtils;
import com.hhly.mlottery.view.CircleImageView;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by：XQyi on 2017/4/18 11:12
 * Use:竞彩单关页面[MVP_view  页面展示]
 */

public class MvpBettingPayDetailsActivity extends Activity implements MView<BettingDetailsBean>, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    private ImageView mBack;
    private LinearLayout mToPay;
    private MvpBettingPayDetailsPresenter mvpBettingPayDetailsPresenter;
    private TextView mSecialistName;
    private CircleImageView portraitImg;
    private TextView detailsWeek;
    private TextView detailsNum;
    private TextView detailsLuague;
    private TextView detailsHomeName;
    private TextView detailsGuestName;
    private TextView detailsDate;
    private TextView detailsTime;
    private TextView detailsHomeWinOdds;
    private ImageView detailsHomeWinImg;
    private TextView detailsDrawOdds;
    private ImageView detailsDrawImg;
    private TextView detailsGuestWinOdds;
    private ImageView detailsGuestImg;
    private TextView detailsContext;
    private ImageView detailsContextBg;
    private TextView detailsPrice;
    private ExactSwipeRefreshLayout mRefresh;
    private LinearLayout mErrorLayout;
    private TextView mRefreshTxt;
    private LinearLayout mLoadingLayout;
    private TextView mNoDataLayout;
//    private BettingListDataBean.PromotionData.BettingListData itemData;
    private LinearLayout toPayll;

    private boolean mayPay;
    private String promotionId;
    private TextView detailsHandicp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.betting_recommend_details);

        EventBus.getDefault().register(this);

        mContext = this;
        mvpBettingPayDetailsPresenter = new MvpBettingPayDetailsPresenter(this);

        initView();
        setStatus(SHOW_STATUS_LOADING);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

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
        TextView title = (TextView) findViewById(R.id.public_txt_title);
        title.setText(mContext.getResources().getText(R.string.betting_title_details));
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        mBack = (ImageView) findViewById(R.id.public_img_back);
        mBack.setOnClickListener(this);
        mToPay = (LinearLayout)findViewById(R.id.betting_topay_ll);
        mToPay.setOnClickListener(this);

        toPayll = (LinearLayout)findViewById(R.id.betting_topay_all);

        //下拉控件
        mRefresh = (ExactSwipeRefreshLayout) findViewById(R.id.betting_refresh_layout);
        mRefresh.setColorSchemeResources(R.color.bg_header);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getApplicationContext(), StaticValues.REFRASH_OFFSET_END));

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

        portraitImg = (CircleImageView)findViewById(R.id.portrait_img);
        mSecialistName = (TextView) findViewById(R.id.betting_recommend_specialist_name);
        detailsWeek = (TextView) findViewById(R.id.betting_details_week);
        detailsNum = (TextView) findViewById(R.id.betting_details_num);
        detailsLuague = (TextView) findViewById(R.id.betting_details_league);
        detailsHomeName = (TextView) findViewById(R.id.betting_details_home_name);
        detailsHandicp = (TextView) findViewById(R.id.betting_details_handicap);
        detailsGuestName = (TextView) findViewById(R.id.betting_details_guest_name);
        detailsDate = (TextView) findViewById(R.id.betting_details_date);
        detailsTime = (TextView) findViewById(R.id.betting_details_time);
        detailsHomeWinOdds = (TextView) findViewById(R.id.betting_details_homewin_odds);
        detailsHomeWinImg = (ImageView) findViewById(R.id.betting_details_homewin_img);
        detailsDrawOdds = (TextView) findViewById(R.id.betting_details_draw_odds);
        detailsDrawImg = (ImageView) findViewById(R.id.betting_details_draw_img);
        detailsGuestWinOdds = (TextView) findViewById(R.id.betting_details_guestwin_odds);
        detailsGuestImg = (ImageView) findViewById(R.id.betting_details_guestwin_img);
        detailsContext = (TextView) findViewById(R.id.betting_details_txt);
        detailsContextBg = (ImageView) findViewById(R.id.betting_details_txt_bg);
        detailsPrice = (TextView) findViewById(R.id.detting_details_price);

        //默认隐藏
        toPayll.setVisibility(View.GONE);
        detailsContextBg.setVisibility(View.GONE);
    }

    private void initData(){

//        Serializable allLeague = getIntent().getSerializableExtra(ConstantPool.BETTING_ITEM_DATA);
//        itemData = (BettingListDataBean.PromotionData.BettingListData)allLeague;

        promotionId = getIntent().getStringExtra(ConstantPool.TO_DETAILS_PROMOTION_ID);

//        if (itemData.getLookStatus().equals("2")) {
//            toPayll.setVisibility(View.VISIBLE);
//            detailsContextBg.setVisibility(View.VISIBLE);
//        }else{
//            toPayll.setVisibility(View.GONE);
//            detailsContextBg.setVisibility(View.GONE);
//        }


        L.d("qwertyui===>>> " , promotionId);
        //http://192.168.10.242:8092/promotion/info/detail?
        // userId=hhly90662&promotionId=643&sign=007ec32c4f7279cfd49260c408528c0412
//        String url = "http://192.168.10.242:8092/promotion/info/detail";
        String url = "http://m.1332255.com:81/promotion/info/detail";
        String userid = AppConstants.register.getUser().getUserId();
        Map<String ,String> mapPrament = new HashMap<>();

        mapPrament.put("userId" , userid);//用户id
        mapPrament.put("promotionId" , promotionId); //推荐ID
        mapPrament.put("lang" , "zh");
        mapPrament.put("timeZone" , "8");
        String signs = SignUtils.getSign("/promotion/info/detail" , mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put("userId" , userid);//用户id
        map.put("promotionId" , promotionId); //推荐ID
        map.put("sign" , signs);

        L.d("qwer== >> " + signs);


        mvpBettingPayDetailsPresenter.loadData(url , map);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reloading_txt:
                setStatus(SHOW_STATUS_REFRESH_ONCLICK);
                initData();
                break;
            case R.id.public_img_back:
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.betting_topay_ll:

                if (DeviceInfo.isLogin()) {
                    Intent mIntent = new Intent(mContext , MvpBettingOnlinePaymentActivity.class);
                    mIntent.putExtra(ConstantPool.PROMOTION_ID , promotionId);
                    startActivity(mIntent);
                    overridePendingTransition(R.anim.push_left_in , R.anim.push_fix_out);
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.putExtra(ConstantPool.BETTING_LOAD , true);
                    startActivity(intent);
                }
                break;


        }
    }

    @Override
    public void loadSuccessView(BettingDetailsBean basebean) {

        BettingDetailsBean.DetailsBeanData detailsData = basebean.getDetail();
        BettingDetailsBean.MatchInfoBeanData matchInfoData = basebean.getMatchInfo();
        setStatus(SHOW_STATUS_SUCCESS);
        if (detailsData != null) {

                detailsWeek.setText(filtraNull(detailsData.getSerNum()));
                detailsHomeWinOdds.setText(filtraNull(detailsData.getLeftOdds()));
                detailsDrawOdds.setText(filtraNull(detailsData.getMidOdds()));
                detailsGuestWinOdds.setText(filtraNull(detailsData.getRightOdds()));
                detailsHandicp.setText("(" + filtraNull(detailsData.getHandicap()) + ")");
                detailsPrice.setText("￥ " + filtraNull(detailsData.getPrice()));

                if (detailsData.getChoose() != null) {
                    switch (detailsData.getChoose()){
                        case "1":
                            detailsHomeWinImg.setBackgroundResource(R.mipmap.jingcai_icon_sel);
                            break;
                        case "0":
                            detailsDrawImg.setBackgroundResource(R.mipmap.jingcai_icon_sel);
                            break;
                        case "":
                            detailsGuestImg.setBackgroundResource(R.mipmap.jingcai_icon_sel);
                            break;
                    }

                }
                if (detailsData.getChoose1() != null) {
                    switch (detailsData.getChoose1()){
                        case "1":
                            detailsHomeWinImg.setBackgroundResource(R.mipmap.jingcai_icon_sel);
                            break;
                        case "0":
                            detailsDrawImg.setBackgroundResource(R.mipmap.jingcai_icon_sel);
                            break;
                        case "":
                            detailsGuestImg.setBackgroundResource(R.mipmap.jingcai_icon_sel);
                            break;
                    }
                }
        }
        if (matchInfoData != null) {

            if (matchInfoData.getLookStatus().equals("2")) {
                toPayll.setVisibility(View.VISIBLE);
                detailsContextBg.setVisibility(View.VISIBLE);
            }else{
                toPayll.setVisibility(View.GONE);
                detailsContextBg.setVisibility(View.GONE);
            }

            String imgUrl = matchInfoData.getPhotoUrl();
            ImageLoader.load(mContext,imgUrl,R.mipmap.center_head).into(portraitImg);
            detailsHomeName.setText(filtraNull(matchInfoData.getHomeName()));
            detailsGuestName.setText(filtraNull(matchInfoData.getGuestName()));
            detailsLuague.setText(filtraNull(matchInfoData.getLeagueName()));
            mSecialistName.setText(filtraNull(matchInfoData.getNickname()));
            detailsDate.setText(filtraNull(matchInfoData.getMatchDateTime()));

            if (matchInfoData.getLookStatus().equals("2")) {
                detailsContext.setText(getApplicationContext().getResources().getText(R.string.betting_txt_pay_check_result));
            }else{
                detailsContext.setText(filtraNull(basebean.getDetail().getContext()));
            }
        }



    }

    @Override
    public void loadFailView() {
        setStatus(SHOW_STATUS_ERROR);
//        Toast.makeText(mContext, "网络请求失败~！！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadNoData() {
        setStatus(SHOW_STATUS_NO_DATA);
//        Toast.makeText(mContext, "暂无数据~！！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        setStatus(SHOW_STATUS_LOADING);
        initData();
    }

    //购买完成后的返回
    public void onEventMainThread(BettingBuyResultEventBusEntity buyResultEventBusEntity){

        if (buyResultEventBusEntity.isSuccessBuy()) {
            mayPay = false; //购买的返回 不可二次点击
            setStatus(SHOW_STATUS_REFRESH_ONCLICK);
            initData();
        }
    }
    //登录完成后的返回（刷新 得到新的lookstart状态）
    public void onEventMainThread(LoadingResultEventBusEntity loadingResultEventBusEntity){
        if (loadingResultEventBusEntity.isLoadResult()) {
            setStatus(SHOW_STATUS_REFRESH_ONCLICK);
            initData();
        }
    }

    private String filtraNull(String str){

        if (str == null) {
            return "--";
        }else{
            return str;
        }
    }
}
