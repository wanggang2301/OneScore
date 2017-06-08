package com.hhly.mlottery.mvp.bettingmvp.mvpview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.bettingbean.BalanceDataBean;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.BettingBuyResultEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.PayMentZFBResultEventBusEntity;
import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PayMentUtils;
import com.hhly.mlottery.util.net.SignUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.UnitsUtil;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by：XQyi on 2017/6/1 16:08
 * Use:充值页面(view)
 */
public class MvpChargeMoneyActivity extends Activity implements View.OnClickListener {

    private Context mContext;
    private RadioButton priceCardA;
    private RadioButton priceCardB;
    private RadioButton priceCardC;
    private RadioButton priceCardD;

    private String currMoney;//当前所选金额
    private EditText paymentMoney;
    private RadioButton paymentZFBRb;
    private RadioButton paymentWeiXinRb;
    private LinearLayout mPaymentMoney;

    /**
     * 充值方式  支付宝(默认) 0 ；微信 1
     */
    private Integer PAYMENT_MONEY = ConstantPool.PAY_ZFB;

    /**
     * pay 参数的URL
     */
//    String payUrl = "http://192.168.31.207:8092/pay/recharge";
    String payUrl = "http://m.1332255.com:81/user/pay/recharge";
//      String payUrl = "http://192.168.10.242:8092/user/pay/recharge";

    /**
     * 余额查询的接口
     */
//    String balanceUrl = "http://192.168.10.242:8099/user/pay/balance";
    String balanceUrl = "http://m.1332255.com:81/user/pay/balance";
    private TextView balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chongzhi_activity);
        mContext = this;
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    private void initView(){
        ImageView mBack = (ImageView) findViewById(R.id.back);
        mBack.setOnClickListener(this);

        balance = (TextView) findViewById(R.id.balance_txt);
        paymentMoney = (EditText) findViewById(R.id.payment_money_edit);

        priceCardA = (RadioButton) findViewById(R.id.price_card_a);
        priceCardA.setOnClickListener(this);
        priceCardB = (RadioButton) findViewById(R.id.price_card_b);
        priceCardB.setOnClickListener(this);
        priceCardC = (RadioButton) findViewById(R.id.price_card_c);
        priceCardC.setOnClickListener(this);
        priceCardD = (RadioButton) findViewById(R.id.price_card_d);
        priceCardD.setOnClickListener(this);

        priceCardA.setChecked(false);
        priceCardB.setChecked(false);
        priceCardC.setChecked(false);
        priceCardD.setChecked(false);

        paymentZFBRb = (RadioButton) findViewById(R.id.chongzhi_zfb);
        paymentWeiXinRb = (RadioButton) findViewById(R.id.chongzhi_weixin);
        paymentZFBRb.setChecked(true);
        paymentWeiXinRb.setChecked(false);

        RelativeLayout paymentZFBRl = (RelativeLayout) findViewById(R.id.chongzhi_zfb_rl);
        paymentZFBRl.setOnClickListener(this);
        RelativeLayout paymentWeiXinRl = (RelativeLayout) findViewById(R.id.chongzhi_weixin_rl);
        paymentWeiXinRl.setOnClickListener(this);

        mPaymentMoney = (LinearLayout) findViewById(R.id.payment_money_ll);
        mPaymentMoney.setOnClickListener(this);

        paymentMoney.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    paymentMoney.setCursorVisible(true);// 再次点击显示光标
                }
                priceCardA.setChecked(false);
                priceCardB.setChecked(false);
                priceCardC.setChecked(false);
                priceCardD.setChecked(false);
//                paymentMoney.setText("");
                return false;
            }
        });

    }

    private void initData(){
        String userid = AppConstants.register.getUser().getUserId();
        String token = AppConstants.register.getToken();

        Map<String ,String> mapPrament = new HashMap<>();

        mapPrament.put("userId" , userid);//用户ID
        mapPrament.put("loginToken" , token);//登陆的token
        mapPrament.put("lang" , "zh");
        mapPrament.put("timeZone" , "8");
        String signs = SignUtils.getSign("/user/pay/balance" , mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put("userId" , userid);//用户ID
        map.put("loginToken" , token);//登陆的token
        map.put("sign" , signs);//签名 和 登陆的时候签名一样

        L.d("qwer== >> " + signs);

        VolleyContentFast.requestJsonByPost(balanceUrl, map, new VolleyContentFast.ResponseSuccessListener<BalanceDataBean>() {
            @Override
            public void onResponse(BalanceDataBean jsonObject) {
                if (jsonObject == null || jsonObject.getCode() != 200) {
                    L.d("balance == >>>" , "余额接口无数据返回");
                    return;
                }else{
                    if (jsonObject.getData() != null) {
                        L.d("balance == >>>" , "返回成功");
                        String balancess = UnitsUtil.fenToYuan(jsonObject.getData().getBalance().getAvailableBalance()); //元转分
                        balance.setText(jsonObject.getData().getBalance() == null ? "--" : filtraNull(balancess));
                    }
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                L.d("balance == >>>" , "后台接口访问失败");
            }
        },BalanceDataBean.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.price_card_a:
                priceCardA.setChecked(true);
                priceCardB.setChecked(false);
                priceCardC.setChecked(false);
                priceCardD.setChecked(false);
                paymentMoney.setText("10");
                paymentMoney.setSelection(paymentMoney.getText().length());
//                paymentMoney.setHint("10");
                break;
            case R.id.price_card_b:
                priceCardA.setChecked(false);
                priceCardB.setChecked(true);
                priceCardC.setChecked(false);
                priceCardD.setChecked(false);
                paymentMoney.setText("20");
                paymentMoney.setSelection(paymentMoney.getText().length());
                break;
            case R.id.price_card_c:
                priceCardA.setChecked(false);
                priceCardB.setChecked(false);
                priceCardC.setChecked(true);
                priceCardD.setChecked(false);
                paymentMoney.setText("50");
                paymentMoney.setSelection(paymentMoney.getText().length());
                break;
            case R.id.price_card_d:
                priceCardA.setChecked(false);
                priceCardB.setChecked(false);
                priceCardC.setChecked(false);
                priceCardD.setChecked(true);
                paymentMoney.setText("100");
                paymentMoney.setSelection(paymentMoney.getText().length());
                break;

            case R.id.chongzhi_zfb_rl:
                paymentZFBRb.setChecked(true);
                paymentWeiXinRb.setChecked(false);
                PAYMENT_MONEY = ConstantPool.PAY_ZFB;
                break;
            case R.id.chongzhi_weixin_rl:
                paymentZFBRb.setChecked(false);
                paymentWeiXinRb.setChecked(true);
                PAYMENT_MONEY = ConstantPool.PAY_WEIXIN;
                break;
            case R.id.payment_money_ll:
                //充值
                
                int money = 0;//金额
                try {
                    money = Integer.parseInt(paymentMoney.getText().toString()); //获取充值金额
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }

                L.d("输入的金额 ：" , money + "");

                if (money > 10000) {
                    Toast.makeText(mContext, "单次充值金额最大为10000元", Toast.LENGTH_SHORT).show();
                }else{
                    String moneyIn =  UnitsUtil.yuanToFen(paymentMoney.getText().toString());//获取充值金额 元==>分

                    L.d("充值的金额 ：" , moneyIn);
                    switch (PAYMENT_MONEY){
                        case 0:
                            PayMentUtils.ALiPayData(mContext , MvpChargeMoneyActivity.this ,payUrl ,getDataMap("4" , moneyIn));
                            break;
                        case 1:
                            PayMentUtils.WeiXinPayData(mContext , payUrl , getDataMap("3" , moneyIn));
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
    private Map<String, String> getDataMap(String service , String money){
//        Map<String, String> map = new HashMap<String, String>();
//        String userid = AppConstants.register.getUser().getUserId();
//        String token = AppConstants.deviceToken;
//        String sign = AppConstants.SIGN_KEY;
//
//        map.put("userId" , userid);//用户ID
//        map.put("service" , service);//3 微信 4 支付宝
//        map.put("tradeAmount" , "1");//金额 分
//        map.put("loginToken" , token);//登陆的token
//        map.put("sign" , sign);//签名 和 登陆的时候签名一样

        String userid = AppConstants.register.getUser().getUserId();
        String token = AppConstants.register.getToken();

        Map<String ,String> mapPrament = new HashMap<>();

        mapPrament.put("userId" , userid);//用户ID
        mapPrament.put("service" , service);//3 微信 4 支付宝
        mapPrament.put("tradeAmount" , money);//金额 分
        mapPrament.put("loginToken" , token);//登陆的token
        mapPrament.put("lang" , "zh");
        mapPrament.put("timeZone" , "8");
        String signs = SignUtils.getSign("/user/pay/recharge" , mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put("userId" , userid);//用户ID
        map.put("service" , service);//3 微信 4 支付宝
        map.put("tradeAmount" , money);//金额 分
        map.put("loginToken" , token);//登陆的token
        map.put("sign" , signs);//签名 和 登陆的时候签名一样

        L.d("qwer== >> " + signs);

        return map;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 支付宝支付的返回码处理
     * @param result
     */
    public void onEventMainThread(PayMentZFBResultEventBusEntity result){

        String resultStatus = result.getResult();

        if (TextUtils.equals(resultStatus, "9000")) {
            Toast.makeText(mContext, "支付成功 > " + resultStatus, Toast.LENGTH_SHORT).show();
            /** 支付成功后回到当前页刷新余额接口*/
            initData();
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
    /**
     * 微信支付的返回
     * @param buyResultEventBusEntity
     */
    public void onEventMainThread(BettingBuyResultEventBusEntity buyResultEventBusEntity){
        if (buyResultEventBusEntity.isSuccessBuy()) {
            /** 支付成功后回到当前页刷新余额接口*/
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

    //    /**
//     * 支付宝参数接口
//     */
//    private void ALiPayData(){
//        Map<String, String> map = new HashMap<String, String>();
//        String userid = AppConstants.register.getData().getUser().getUserId();
//        String token = AppConstants.deviceToken;
//        String sign = AppConstants.SIGN_KEY;
//
//        map.put("userId" , userid);//用户ID
//        map.put("service" , "4");//3 微信 4 支付宝
//        map.put("tradeAmount" , "1");//金额 分
//        map.put("token" , token);//登陆的token
//        map.put("sign" , sign);//签名 和 登陆的时候签名一样
//
////        map.put("service" , "alipay.trade.app.pay");
////        map.put("outTradeNo", String.valueOf(System.currentTimeMillis()));
////        map.put("body", "商品描述");
////        map.put("totalFee", "1");
////        map.put("expireTime", "120");
//////        map.put("subject", "测试商品");
//
////        String aliPayUrl = "http://192.168.31.15:8081/app-pay/alipay/tradeAppPay"; 192.168.31.207:8092/pay/recharge?userId=HHLY00000166&service=3&tradeAmount=1&token=a&sign=aa
//        String aliPayUrl = "http://192.168.31.207:8092/pay/recharge"; //?userId=HHLY00000166&service=3&tradeAmount=1&token=a&sign=aa
//        VolleyContentFast.requestJsonByPost(aliPayUrl, map, new VolleyContentFast.ResponseSuccessListener<PaymentZFBBean>() {
//            @Override
//            public void onResponse(PaymentZFBBean jsonBean) {
//
//                L.d("qwer_AliPay===>>" , "body = " + jsonBean.getData().getBody());
//                toAliPay(jsonBean.getData().getBody());
//            }
//        }, new VolleyContentFast.ResponseErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
//                L.d("qwer_AliPay===>>" , "error");
//                Toast.makeText(mContext, "后台接口访问失败", Toast.LENGTH_SHORT).show();
//            }
//        },PaymentZFBBean.class);
//
//    }
//    /**
//     * 调用支付宝支付
//     */
//    private static final int SDK_PAY_FLAG = 1;
//    private void toAliPay(final String orderInfo){
//
//        Runnable aliPayRun = new Runnable() {
//            @Override
//            public void run() {
//                PayTask aliPay = new PayTask(MvpChargeMoneyActivity.this);
////                checkAliPayInstalled(getApplicationContext());
////                L.d("qwer_asd = " + aliPay.getVersion());
////                H5PayResultModel h5PayResultModel = aliPay.h5Pay(orderInfo, true);
//                Map<String , String> payMap = aliPay.payV2(orderInfo , true);
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = payMap;
//                mHandler.sendMessage(msg);
//            }
//        };
//        // 必须异步调用
//        Thread payThread = new Thread(aliPayRun);
//        payThread.start();
//    }
//
//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case SDK_PAY_FLAG:{
//                    PayResult mResult = new PayResult((Map<String , String>)msg.obj);
//                    String result = mResult.getResult();
//                    String resultStatus = mResult.getResultStatus();
//
//                    L.d("qwer_asd " , result + " == " + resultStatus);
//                    if ( TextUtils.equals(resultStatus , "9000")) {
//                        Toast.makeText(mContext, "支付成功 > " + resultStatus, Toast.LENGTH_SHORT).show();
//                    }else{
//                        if (TextUtils.equals(resultStatus, "8000")) {
//                            Toast.makeText(mContext, "结果确认中 > " + resultStatus, Toast.LENGTH_SHORT).show();
//                        }else if (TextUtils.equals(resultStatus, "6001")) {
//                            Toast.makeText(mContext, "支付取消 > " + resultStatus, Toast.LENGTH_SHORT).show();
//                        }else if (TextUtils.equals(resultStatus, "6002")) {
//                            Toast.makeText(mContext, "网络异常 > " + resultStatus, Toast.LENGTH_SHORT).show();
//                        }else if (TextUtils.equals(resultStatus, "5000")) {
//                            Toast.makeText(mContext, "重复请求 > " + resultStatus, Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(mContext, "支付失败 > " + resultStatus, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//                break;
//                default:
//                    break;
//            }
//        }
//    };
//
//    /**
//     * 判断是否安装有支付宝客户端
//     * @param context
//     * @return
//     */
//    public static boolean checkAliPayInstalled(Context context) {
//        Uri uri = Uri.parse("alipays://platformapi/startApp");
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
//        return componentName != null;
//    }
//    /**
//     * 微信支付参数接口
//     */
//    private void WeiXinPayData(){
////        Random rand = new Random();
////        int i = rand.nextInt(100);
////        int ii = rand.nextInt(100);
////        int iii = rand.nextInt(100);
//
//
//        Map<String, String> params = new HashMap<>();
//        String userid = AppConstants.register.getData().getUser().getUserId();
//        String token = AppConstants.deviceToken;
//        String sign = AppConstants.SIGN_KEY;
//
//        params.put("userId" , userid);//用户ID
//        params.put("service" , "3");//3 微信 4 支付宝
//        params.put("tradeAmount" , "1");//金额 分
//        params.put("token" , token);//登陆的token
//        params.put("sign" , sign);//签名 和 登陆的时候签名一样
//
//////        params.put("service", "pay.weixin.raw.app");//接口类型
////        params.put("service", "wxpay.trade.app.pay");//接口类型
////        params.put("outTradeNo", "ybf201705270" + i+ii+iii);//商户订单号
////        params.put("body", "购买_推荐");//商品描述
////        params.put("totalFee", "1");//总金额 (分)
////        params.put("expireTime", "120");//订单有效时间
//////        params.put("appId", APP_ID);//appid
//
////        String url = "http://192.168.31.15:8083/sunon-web-api/pay/unifiedTradePay"; 192.168.31.207:8092/pay/recharge?userId=HHLY00000166&service=3&tradeAmount=1&token=a&sign=aa
//        String url = "http://192.168.31.207:8092/pay/recharge"; //?userId=HHLY00000166&service=3&tradeAmount=1&token=a&sign=aa
//        VolleyContentFast.requestJsonByPost(url,params , new VolleyContentFast.ResponseSuccessListener<PaymentWeiXinBean>() {
//            @Override
//            public void onResponse(PaymentWeiXinBean jsondata) {
//                if (jsondata == null || jsondata.getData() == null) {
//                    return;
//                }
////                if (jsondata.getData().getDataMap() == null) {
////                    Toast.makeText(mContext, jsondata.getMsg(), Toast.LENGTH_SHORT).show();
////                    return;
////                }else{
////                    WeiXinPayidDataBean.PayDataWX.PayDataMapWX payInfoData = jsondata.getData().getDataMap();
////                    L.d("yxq_WXPay===" , jsondata.getMsg() + " >>appid= " + payInfoData.getAppid());
////                    toPay(payInfoData);
////                }
//                PaymentWeiXinBean.DataDetailsBean payData = jsondata.getData();
//                toWeiXinPay(payData);
//            }
//        }, new VolleyContentFast.ResponseErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
//                Toast.makeText(mContext, "后台接口访问失败", Toast.LENGTH_SHORT).show();
//            }
//        },PaymentWeiXinBean.class);
//    }
//    /**
//     * 调用微信支付
//     * @param payInfo
//     */
//    private void toWeiXinPay(PaymentWeiXinBean.DataDetailsBean payInfo){
//        //注册appid
//        IWXAPI api = WXAPIFactory.createWXAPI(this, ConstantPool.APP_ID);// 通过WXAPIFactory工厂，获取IWXAPI的实例
//        if (!api.isWXAppInstalled()) {
//            Toast.makeText(this, mContext.getResources().getString(R.string.share_uninstall_webchat), Toast.LENGTH_SHORT).show();
//        }
//        api.registerApp(ConstantPool.APP_ID); //将appid注册到微信
//
//        //赋值微信支付所需参数
//        PayReq payReq = new PayReq();
//        payReq.appId = payInfo.getAppid();
//        payReq.partnerId = payInfo.getPartnerid();
//        payReq.prepayId = payInfo.getPrepayid();
//        payReq.packageValue = payInfo.getPackages();
//        payReq.nonceStr = payInfo.getNoncestr();
//        payReq.timeStamp = payInfo.getTimestamp();
//        payReq.sign = payInfo.getSign();
//        api.sendReq(payReq);
//    }
}
