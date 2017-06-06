package com.hhly.mlottery.mvp.bettingmvp.mvpview;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BaseActivity;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.PayMentZFBResultEventBusEntity;
import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.PayMentUtils;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by：XQyi on 2017/6/1 16:08
 * Use:充值页面(view)
 */
public class MvpChargeMoneyActivity extends BaseActivity implements View.OnClickListener {

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
    String payUrl = "http://192.168.31.207:8092/pay/recharge";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chongzhi_activity);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView(){
        ImageView mBack = (ImageView) findViewById(R.id.back);
        mBack.setOnClickListener(this);

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
                switch (PAYMENT_MONEY){
                    case 0:
//                        ALiPayData();
                        PayMentUtils.ALiPayData(mContext , MvpChargeMoneyActivity.this ,payUrl ,getDataMap("4"));
//                        String result = PayMentUtils.getPayResult();
//                        if (result == null) {
//                            L.d("result0602 = " , " n u l l ");
//                        }else{
//                            L.d("result0602 = " , result);
//                            switch (result){
//                                case "9000":
//
//                                    break;
//                                case "8000":
//                                    break;
//                                case "6001":
//                                    break;
//                                case "6002":
//                                    break;
//                                case "5000":
//                                    break;
//                            }
//                        }
//                        Toast.makeText(mContext, result + " !", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
//                        WeiXinPayData();
                        PayMentUtils.WeiXinPayData(mContext , payUrl , getDataMap("3"));
                        break;
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
        Map<String, String> map = new HashMap<String, String>();
        String userid = AppConstants.register.getData().getUser().getUserId();
        String token = AppConstants.deviceToken;
        String sign = AppConstants.SIGN_KEY;

        map.put("userId" , userid);//用户ID
        map.put("service" , service);//3 微信 4 支付宝
        map.put("tradeAmount" , "1");//金额 分
        map.put("loginToken" , token);//登陆的token
        map.put("sign" , sign);//签名 和 登陆的时候签名一样

        return map;
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
