package com.hhly.mlottery.mvp.bettingmvp.mvpview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BaseActivity;
import com.hhly.mlottery.bean.bettingbean.BettingOrderDataBean;
import com.hhly.mlottery.mvp.bettingmvp.MView;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.BettingBuyResultEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.PayMentZFBResultEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.mvppresenter.MvpBettingOnlinePaymentPresenter;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseBean;
import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PayMentUtils;
import com.hhly.mlottery.util.net.SignUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by：XQyi on 2017/4/19 11:12
 * Use:支付页面（选择支付方式 [MVP-view 页面展示]）
 */

public class MvpBettingOnlinePaymentActivity extends Activity implements MView<BettingOrderDataBean>, View.OnClickListener {

    // IWXAPI 是第三方app和微信通信的openapi接口
//    private IWXAPI api;

    //订单接口
//    String payUrl = "http://192.168.31.15:8081/sunon-web-api/pay/unifiedTradePay";
    String payUrl = "http://192.168.31.207:8099/user/pay/recharge";
//    String payUrl = "http://m.1332255.com:81/user/pay/recharge";
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


        //http://192.168.10.242:8092/promotion/order/create?
        // userId=hhly90531&promotionId=642&sign=59891f91c988198909c527399031d7f111&channel=1&token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE0OTYzNzY2NjIsInN1YiI6IntcImlkXCI6XCJoaGx5OTA1MzFcIixcInBob25lTnVtXCI6XCIxMzI2Njc1MjM4NlwifSJ9.2hmsToL-ex9LXRbWI44cuDhqKqZva_qBPG1pKB_IVfU

        String url = "http://192.168.10.242:8092/promotion/order/create";
        String userid = AppConstants.register.getUser().getUserId();
        String token = AppConstants.register.getToken();

        Map<String ,String> mapPrament = new HashMap<>();

        mapPrament.put("userId" , userid);//用户id
        mapPrament.put("promotionId" , promId); //推荐ID
        mapPrament.put("channel" , "1"); //0：PC 1：安卓 2:IOS 3:H5
        mapPrament.put("loginToken" , token); //logintoken
        mapPrament.put("appType" , "2"); //appType 2:android
        mapPrament.put("lang" , "zh");
        mapPrament.put("timeZone" , "8");
        String signs = SignUtils.getSign("/promotion/order/create" , mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put("userId" , userid);//用户id
        map.put("promotionId" , promId); //推荐ID
        map.put("channel" , "1"); //0：PC 1：安卓 2:IOS 3:H5
        map.put("loginToken" , token); //logintoken
        map.put("appType" , "2"); //appType 2:android
        map.put("sign" , signs);

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
                    Toast.makeText(mContext, "当前余额不足，请选择其它支付方式", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.public_img_back:
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.betting_confirm_pay:
                if (orderCreate) {
                    switch (MODE_PAYMENT){
                        case 0:
                            L.d("支付方式 = ","支付宝");
                            PayMentUtils.ALiPayData(mContext , MvpBettingOnlinePaymentActivity.this , payUrl , getDataMap("4"));
                            break;
                        case 1:
                            L.d("支付方式 = ","微信");
                            PayMentUtils.WeiXinPayData(mContext , payUrl , getDataMap("3"));
                            break;
                        case 2:
                            L.d("支付方式 = ","余额支付");
                            orderPay();// 充值成功调余额支付接口
                            break;

                    }
                }

                break;
        }
    }
    /**
     * 获得请求参数的 map
     * @param service
     * @return 用于post请求的参数
     */
    private Map<String, String> getDataMap(String service){

        String userid = AppConstants.register.getUser().getUserId();
        String token = AppConstants.register.getToken();

        Map<String ,String> mapPrament = new HashMap<>();

        mapPrament.put("userId" , userid);//用户ID
        mapPrament.put("service" , service);//3 微信 4 支付宝
        mapPrament.put("tradeAmount" , "1");//金额 分
        mapPrament.put("loginToken" , token);//登陆的token
        mapPrament.put("lang" , "zh");
        mapPrament.put("timeZone" , "8");
        String signs = SignUtils.getSign("/user/pay/recharge" , mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put("userId" , userid);//用户ID
        map.put("service" , service);//3 微信 4 支付宝
        map.put("tradeAmount" , "1");//金额 分
        map.put("loginToken" , token);//登陆的token
        map.put("sign" , signs);//签名 和 登陆的时候签名一样

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

            mBalance.setText(orderDataBean.getData().getAmount() + " F");
            mPayPrice.setText("￥ " + orderDataBean.getData().getPayPrice());

            if (blance >= price) { //余额大于单价
                balanceFully = true; //余额足够
            }else{
                balanceFully = false;
            }
        }

    }

    @Override
    public void loadFailView() {
        Toast.makeText(mContext, "网络请求失败~！！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadNoData() {
        Toast.makeText(mContext, "暂无数据~！！", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(PayMentZFBResultEventBusEntity result){

        String resultStatus = result.getResult();

        if (TextUtils.equals(resultStatus, "9000")) {
            Toast.makeText(mContext, "支付成功 > " + resultStatus, Toast.LENGTH_SHORT).show();
            //TODO=====  充值成功调起余额接口
//            orderPay();// 充值成功调余额支付接口
        } else {
            if (TextUtils.equals(resultStatus, "8000")) {
                Toast.makeText(mContext, "结果确认中 > " + resultStatus, Toast.LENGTH_SHORT).show();
            } else if (TextUtils.equals(resultStatus, "6001")) {
                Toast.makeText(mContext, "支付取消 > " + resultStatus, Toast.LENGTH_SHORT).show();
            } else if (TextUtils.equals(resultStatus, "6002")) {
                Toast.makeText(mContext, "网络异常 > " + resultStatus, Toast.LENGTH_SHORT).show();
            } else if (TextUtils.equals(resultStatus, "5000")) {
                Toast.makeText(mContext, "重复请求 > " + resultStatus, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "支付失败 > " + resultStatus, Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void onEventMainThread(BettingBuyResultEventBusEntity buyResultEventBusEntity){
        if (buyResultEventBusEntity.isSuccessBuy()) {
            orderPay();
        }
    }
    /**
        订单支付（余额扣款）
     */
    public void orderPay(){

        //http://192.168.10.242:8092/promotion/order/pay?
        // userId=hhly90531&promotionId=643&sign=982f065d9f9c942d3e5466d93a417d73aa&channel=1&token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE0OTYzNzY2NjIsInN1YiI6IntcImlkXCI6XCJoaGx5OTA1MzFcIixcInBob25lTnVtXCI6XCIxMzI2Njc1MjM4NlwifSJ9.2hmsToL-ex9LXRbWI44cuDhqKqZva_qBPG1pKB_IVfU&payType=3

        String orderPayUrl = "http://192.168.10.242:8092/promotion/order/pay";

        String userid = AppConstants.register.getUser().getUserId();
        String token = AppConstants.register.getToken();

        Map<String ,String> mapPrament = new HashMap<>();

        mapPrament.put("userId" , userid);//用户id
        mapPrament.put("promotionId" , promId); //推荐ID
        mapPrament.put("appType" , "2"); //appType 2:android
        mapPrament.put("loginToken" , token); //logintoken
        mapPrament.put("payType" , "3"); //1微信2支付宝3账户余额  （目前只支持3）
        mapPrament.put("lang" , "zh");
        mapPrament.put("timeZone" , "8");
        String signs = SignUtils.getSign("/promotion/order/pay" , mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put("userId" , userid);//用户id
        map.put("promotionId" , promId); //推荐ID
        map.put("appType" , "2"); //appType 2:android
        map.put("loginToken" , token); //logintoken
        map.put("payType" , "3"); //1微信2支付宝3账户余额  （目前只支持3）
        map.put("sign" , signs);

        L.d("qwer== >> " + signs);

        VolleyContentFast.requestJsonByGet(orderPayUrl, map, new VolleyContentFast.ResponseSuccessListener<BettingOrderDataBean>() {
            @Override
            public void onResponse(BettingOrderDataBean jsonObject) {
                if (jsonObject == null || jsonObject.getCode() == 3000) {
                    L.d("qweradf==> " , "余额扣款成功");
                    EventBus.getDefault().post(new BettingBuyResultEventBusEntity(true));
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