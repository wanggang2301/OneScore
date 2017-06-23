package com.hhly.mlottery.mvp.bettingmvp.mvpview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.LoginActivity;
import com.hhly.mlottery.bean.bettingbean.BettingOrderDataBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.mvp.bettingmvp.MView;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.BettingBuyResultEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.BettingPaymentResultEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.PayDetailsResultEventBus;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.PayMentZFBResultEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.mvppresenter.MvpBettingOnlinePaymentPresenter;
import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PayMentUtils;
import com.hhly.mlottery.util.net.SignUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.UnitsUtil;
import com.sina.weibo.sdk.api.share.Base;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by：XQyi on 2017/4/19 11:12
 * Use:支付页面（选择支付方式 [MVP-view 页面展示]）
 */

public class MvpBettingOnlinePaymentActivity extends Activity implements MView<BettingOrderDataBean>, View.OnClickListener {

    /** 签名参数 */
    private String PARAM_USER_ID = "userId";//用户id
    private String PARAM_PROMOTION_ID = "promotionId";//推荐ID
    private String PARAM_SIGN = "sign";//参数签名
    private String PARAM_CHANNEL = "channel";// 0：PC 1：安卓 2:IOS 3:H5
    private String PARAM_LOGIN_TOKEN = "loginToken";//logintoken
    private String PARAM_APP_TYPE = "appType";//appType 2:android

    private String PARAM_SERVICE = "service";//3 微信 4 支付宝
    private String PARAM_TRADE_AMOUNT = "tradeAmount";//金额 分

    private String PARAM_PAY_TYPE = "payType";//1微信2支付宝3账户余额  （目前只支持3）

    private String PARAM_LANG = "lang";
    private String PARAM_TIMEZONE = "timeZone";

    //订单接口
//    String payUrl = "http://192.168.31.15:8081/sunon-web-api/pay/unifiedTradePay";
//    String payUrl = "http://192.168.31.207:8099/user/pay/recharge";
//    String payUrl = "http://m.1332255.com:81/user/pay/recharge";
    String payUrl = BaseURLs.URL_RECHARGE_PAY;
    private Context mContext;
    /**
     * 支付方式  支付宝(默认) 0 ；微信 1 ；余额 2
     */
    private Integer MODE_PAYMENT = ConstantPool.PAY_ZFB;

    private static final int SDK_PAY_FLAG = 1;
    private ImageView mBack;
    private LinearLayout mConfirmPay;
    private RadioButton mPayZFB;
    private RadioButton mPayWeiXin;
    private RadioButton mPayYuE;
    private MvpBettingOnlinePaymentPresenter paymentPresenter;
    private TextView mBalance;

    private boolean balanceFully = false;// 余额是否足够
    private boolean orderCreate = false;// 订单是否创建成功
    private TextView mPayPrice;
    private static String promId;

    private String moneyIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.betting_recommend_online_payment_lay);
        mContext = this;
        EventBus.getDefault().register(this);
        paymentPresenter = new MvpBettingOnlinePaymentPresenter(this);
        initView();
        initData();
    }

    private void initView(){
        TextView title = (TextView) findViewById(R.id.public_txt_title);
        title.setText(mContext.getResources().getText(R.string.betting_title_onlinepay));
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        mBack = (ImageView) findViewById(R.id.public_img_back);
        mBack.setOnClickListener(this);
        mConfirmPay = (LinearLayout)findViewById(R.id.betting_confirm_pay);
        mConfirmPay.setOnClickListener(this);

        RelativeLayout zfbRl = (RelativeLayout)findViewById(R.id.pay_zfb_rl);
        zfbRl.setOnClickListener(this);
        RelativeLayout wxRl = (RelativeLayout)findViewById(R.id.pay_weixin_rl);
        wxRl.setOnClickListener(this);
        RelativeLayout yueRl = (RelativeLayout)findViewById(R.id.pay_yu_e_rl);
        yueRl.setOnClickListener(this);

        mPayZFB = (RadioButton)findViewById(R.id.pay_zfb);
        mPayWeiXin = (RadioButton)findViewById(R.id.pay_weixin);
        mPayYuE = (RadioButton)findViewById(R.id.pay_yu_e);

        //默认选择ZFB
        mPayZFB.setChecked(true);
        mPayWeiXin.setChecked(false);
        mPayYuE.setChecked(false);

        mBalance = (TextView) findViewById(R.id.betting_online_balance);
        mPayPrice = (TextView) findViewById(R.id.betting_pay_price);
    }

    private void initData(){
        promId = getIntent().getStringExtra(ConstantPool.PROMOTION_ID);
//        String url = "http://192.168.10.242:8092/promotion/order/create";
//        String url = "http://m.1332255.com:81/promotion/order/create";
        String url = BaseURLs.URL_ORDER_CREATE;
        String userid = AppConstants.register.getUser().getUserId();
        String token = AppConstants.register.getToken();

        Map<String ,String> mapPrament = new HashMap<>();

        mapPrament.put(PARAM_USER_ID , userid);//用户id
        mapPrament.put(PARAM_PROMOTION_ID , promId); //推荐ID
        mapPrament.put(PARAM_CHANNEL , ConstantPool.PARAMENT_CHANNEL); //0：PC 1：安卓 2:IOS 3:H5
        mapPrament.put(PARAM_LOGIN_TOKEN , token); //logintoken
        mapPrament.put(PARAM_APP_TYPE , ConstantPool.PARAMENT_APP_TYPE); //appType 2:android
        mapPrament.put(PARAM_LANG , MyApp.getLanguage());
        mapPrament.put(PARAM_TIMEZONE , AppConstants.timeZone + "");
        String signs = SignUtils.getSign(BaseURLs.PARAMENT_ORDER_CREATE , mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put(PARAM_USER_ID , userid);//用户id
        map.put(PARAM_PROMOTION_ID , promId); //推荐ID
        map.put(PARAM_CHANNEL , ConstantPool.PARAMENT_CHANNEL); //0：PC 1：安卓 2:IOS 3:H5
        map.put(PARAM_LOGIN_TOKEN , token); //logintoken
        map.put(PARAM_APP_TYPE , ConstantPool.PARAMENT_APP_TYPE); //appType 2:android
        map.put(PARAM_SIGN , signs);

        L.d("qwer== >> " + signs);

        paymentPresenter.loadData(url , map);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay_zfb_rl:
                mPayZFB.setChecked(true);
                mPayWeiXin.setChecked(false);
                mPayYuE.setChecked(false);
                MODE_PAYMENT = ConstantPool.PAY_ZFB;
                break;
            case R.id.pay_weixin_rl:
                mPayZFB.setChecked(false);
                mPayWeiXin.setChecked(true);
                mPayYuE.setChecked(false);
                MODE_PAYMENT = ConstantPool.PAY_WEIXIN;
                break;
            case R.id.pay_yu_e_rl:
                if (balanceFully) {
                    mPayZFB.setChecked(false);
                    mPayWeiXin.setChecked(false);
                    mPayYuE.setChecked(true);
                    MODE_PAYMENT = ConstantPool.PAY_YU_E;
                }else{
                    Toast.makeText(mContext, mContext.getResources().getText(R.string.betting_not_sufficient_funds), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.public_img_back:
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.betting_confirm_pay:
                if (orderCreate) {

                    String moneyIns =  UnitsUtil.yuanToFen(moneyIn);//获取充值金额 元==>分

                    switch (MODE_PAYMENT){
                        case 0:
                            L.d("支付方式 = ","支付宝");
                            PayMentUtils.ALiPayData(MvpBettingOnlinePaymentActivity.this , payUrl , getDataMap(ConstantPool.ZEB_SERVICE , moneyIns));
                            break;
                        case 1:
                            L.d("支付方式 = ","微信");
                            PayMentUtils.WeiXinPayData(payUrl , getDataMap(ConstantPool.WEIXIN_SERVICE , moneyIns));
                            break;
                        case 2:
                            L.d("支付方式 = ","余额支付");
                            orderPay();// 调余额支付接口
                            break;

                    }
                }else{
//                    Toast.makeText(mContext, mContext.getResources().getText(R.string.betting_network_anomaly), Toast.LENGTH_SHORT).show();
                    //刷新接口
                    initData();
                }

                break;
        }
    }
    /**
     * 获得请求参数的 map
     * @param service
     * @return 用于post请求的参数
     */
    private Map<String, String> getDataMap(String service , String money){

        String userid = AppConstants.register.getUser().getUserId();
        String token = AppConstants.register.getToken();

        Map<String ,String> mapPrament = new HashMap<>();

        mapPrament.put(PARAM_USER_ID , userid);//用户ID
        mapPrament.put(PARAM_SERVICE , service);//3 微信 4 支付宝
        mapPrament.put(PARAM_TRADE_AMOUNT , money);//金额 分
        mapPrament.put(PARAM_LOGIN_TOKEN , token);//登陆的token
        mapPrament.put(PARAM_LANG , MyApp.getLanguage());
        mapPrament.put(PARAM_TIMEZONE , AppConstants.timeZone + "");
        String signs = SignUtils.getSign(BaseURLs.PARAMENT_RECHARGE_PAY , mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put(PARAM_USER_ID , userid);//用户ID
        map.put(PARAM_SERVICE , service);//3 微信 4 支付宝
        map.put(PARAM_TRADE_AMOUNT , money);//金额 分
        map.put(PARAM_LOGIN_TOKEN , token);//登陆的token
        map.put(PARAM_SIGN , signs);//签名 和 登陆的时候签名一样

        L.d("qwer== >> " + signs);
        return map;
    }

    @Override
    public void loadSuccessView(BettingOrderDataBean orderDataBean) {

        if (orderDataBean.getCode() == 3000) {
            orderCreate = true; // 创建订单成功

            int price = 0;//单价
            int blance = 0;//余额
            try {
                price = Integer.parseInt(orderDataBean.getData().getPayPrice());
                blance = Integer.parseInt(orderDataBean.getData().getAmount());
            }catch (NumberFormatException e){
                e.printStackTrace();
            }

            moneyIn = orderDataBean.getData().getPayPrice();//付款金额

            mBalance.setText("￥ " + UnitsUtil.fenToYuan(orderDataBean.getData().getAmount()));
            mPayPrice.setText("￥ " + orderDataBean.getData().getPayPrice());

            if (blance >= (price*100)) { //余额大于单价(接口返回单价的单位是元)
                balanceFully = true; //余额足够
            }else{
                balanceFully = false;
            }
        }else if(orderDataBean.getCode() == 1012 || orderDataBean.getCode() == 1013 || orderDataBean.getCode() == 1000){
            //token 失效（为空）去登陆
            AppConstants.register.setToken(null);//token 置空 重新登录

            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra(ConstantPool.PUBLIC_INPUT_PARAMEMT , ConstantPool.PAY_DETAILS_RESULT);
            startActivity(intent);
        }
    }

    @Override
    public void loadFailView() {
//        Toast.makeText(mContext, "网络请求失败~！！", Toast.LENGTH_SHORT).show();.
        L.d("网络请求失败 !");
    }

    @Override
    public void loadNoData() {
//        Toast.makeText(mContext, "暂无数据~！！", Toast.LENGTH_SHORT).show();
        L.d("暂无数据 !");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 支付宝付款返回
     * @param result
     */
    public void onEventMainThread(PayMentZFBResultEventBusEntity result){

        String resultStatus = result.getResult();

        if (TextUtils.equals(resultStatus, ConstantPool.PAY_RESULT_STATUS_SUCCESS)) {
            Toast.makeText(mContext, mContext.getResources().getText(R.string.betting_payment_success), Toast.LENGTH_SHORT).show();
            //TODO=====  充值成功调起余额接口
            orderPay();// 充值成功调余额支付接口
        } else {
            if (TextUtils.equals(resultStatus, ConstantPool.PAY_RESULT_STATUS_QUXIAO)) {
                Toast.makeText(mContext, mContext.getResources().getText(R.string.betting_payment_cancle), Toast.LENGTH_SHORT).show();
            }
            L.d("支付返回码==>> " , "" + resultStatus);
        }
    }

    /**
     * 微信支付的返回
     * @param buyResultEventBusEntity
     */
    public void onEventMainThread(BettingBuyResultEventBusEntity buyResultEventBusEntity){
        if (buyResultEventBusEntity.isSuccessBuy()) {
            orderPay();
        }
    }
    /**
     * 登录页面返回
     */
    public void onEventMainThread(PayDetailsResultEventBus detailsResultEventBus){
        if (detailsResultEventBus.isDetailsResult()) {
            initData();
        }
    }
    /**
        订单支付（余额扣款）
     */
    public void orderPay(){

//        String orderPayUrl = "http://192.168.10.242:8092/promotion/order/pay";
//        String orderPayUrl = "http://m.1332255.com:81/promotion/order/pay";
        String orderPayUrl = BaseURLs.URL_ORDER_PAY;

        String userid = AppConstants.register.getUser().getUserId();
        String token = AppConstants.register.getToken();

        Map<String ,String> mapPrament = new HashMap<>();

        mapPrament.put(PARAM_USER_ID , userid);//用户id
        mapPrament.put(PARAM_PROMOTION_ID , promId); //推荐ID
        mapPrament.put(PARAM_APP_TYPE , ConstantPool.PARAMENT_APP_TYPE); //appType 2:android
        mapPrament.put(PARAM_LOGIN_TOKEN , token); //logintoken
        mapPrament.put(PARAM_PAY_TYPE , ConstantPool.PARAMENT_PAY_TYPE); //1微信2支付宝3账户余额  （目前只支持3）
        mapPrament.put(PARAM_LANG , MyApp.getLanguage());
        mapPrament.put(PARAM_TIMEZONE , AppConstants.timeZone + "");
        String signs = SignUtils.getSign(BaseURLs.PARAMENT_ORDER_PAY , mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put(PARAM_USER_ID , userid);//用户id
        map.put(PARAM_PROMOTION_ID , promId); //推荐ID
        map.put(PARAM_APP_TYPE , ConstantPool.PARAMENT_APP_TYPE); //appType 2:android
        map.put(PARAM_LOGIN_TOKEN , token); //logintoken
        map.put(PARAM_PAY_TYPE , ConstantPool.PARAMENT_PAY_TYPE); //1微信2支付宝3账户余额  （目前只支持3）
        map.put(PARAM_SIGN , signs);

        L.d("qwer== >> " + signs);

        VolleyContentFast.requestJsonByGet(orderPayUrl, map, new VolleyContentFast.ResponseSuccessListener<BettingOrderDataBean>() {
            @Override
            public void onResponse(BettingOrderDataBean jsonObject) {
                if (jsonObject.getCode() == 3000) {
                    L.d("qweradf==> " , "余额扣款成功");
                    EventBus.getDefault().post(new BettingPaymentResultEventBusEntity(true));
                    finish();
                    return;
                }else{
                    L.d("qweradf==> " , "扣款失败" + jsonObject.getCode());
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                L.d("qweradf==> " , "扣款接口访问失败");
            }
        },BettingOrderDataBean.class);
    }

}