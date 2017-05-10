package com.hhly.mlottery.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.hhly.mlottery.R;
import com.hhly.mlottery.alipay.PayResult;
import com.hhly.mlottery.bean.bettingbean.WeiFuTongPayidDataBean;
import com.hhly.mlottery.bean.bettingbean.WeiXinPayidDataBean;
import com.hhly.mlottery.bean.bettingbean.ZFBPayDataBean;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;
import android.os.Handler;

/**
 * Created by Administrator on 2017/4/19.
 */

public class BettingOnlinePaymentActivity extends BaseActivity implements View.OnClickListener {
    //AppId,官网申请的合法id
    private static final String APP_ID = "wx2a5538052969956e";
    // IWXAPI 是第三方app和微信通信的openapi接口
//    private IWXAPI api;

    private static final int SDK_PAY_FLAG = 1;
    private ImageView mBack;
    private LinearLayout mConfirmPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.betting_recommend_online_payment_lay);
        initView();
    }

    private void initView(){
        TextView title = (TextView) findViewById(R.id.public_txt_title);
        title.setText("在线支付");
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        mBack = (ImageView) findViewById(R.id.public_img_back);
        mBack.setOnClickListener(this);
        mConfirmPay = (LinearLayout)findViewById(R.id.betting_confirm_pay);
        mConfirmPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back:
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.betting_confirm_pay:
                Toast.makeText(mContext, "去支付", Toast.LENGTH_SHORT).show();
//                WeiXinPayData();
                ALiPayData();
                break;
        }
    }

    /**
     * 威富通支付参数接口
     */
    private void WeiFuTongPayData(){
        Map<String, String> params = new HashMap<>();
        params.put("service", "unified.trade.pay");//接口类型
        params.put("outTradeNo", "ybf20170419");//商户订单号
        params.put("body", "测试购买商品");//商品描述
        params.put("totalFee", "1");//总金额 (分)

        String url = "http://192.168.31.15:8083/sunon-web-api/pay/unifiedTradePay";
        VolleyContentFast.requestJsonByPost(url,params , new VolleyContentFast.ResponseSuccessListener<WeiFuTongPayidDataBean>() {
            @Override
            public void onResponse(WeiFuTongPayidDataBean jsondata) {
                WeiFuTongPayidDataBean.PayData.PayDataMap mDetailsData = jsondata.getData().getDataMap();
                L.d("yxq_WFTPay===" , jsondata.getMsg() + " >>token_id= " + mDetailsData.getToken_id());
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        },WeiFuTongPayidDataBean.class);
    }

    /**
     * 微信支付参数接口
     */
    private void WeiXinPayData(){
        Map<String, String> params = new HashMap<>();
        params.put("service", "pay.weixin.raw.app");//接口类型
        params.put("outTradeNo", "ybf20170421");//商户订单号
        params.put("body", "测试购买推荐");//商品描述
        params.put("totalFee", "1");//总金额 (分)
        params.put("appId", APP_ID);//appid

        String url = "http://192.168.31.15:8083/sunon-web-api/pay/unifiedTradePay";
        VolleyContentFast.requestJsonByPost(url,params , new VolleyContentFast.ResponseSuccessListener<WeiXinPayidDataBean>() {
            @Override
            public void onResponse(WeiXinPayidDataBean jsondata) {
                WeiXinPayidDataBean.PayDataWX.PayDataMapWX.PayInfo payInfoData = jsondata.getData().getDataMap().getPay_info();
                L.d("yxq_WXPay===" , jsondata.getMsg() + " >>appid= " + payInfoData.getAppid());
                toPay(payInfoData);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                Toast.makeText(mContext, "后台接口访问失败", Toast.LENGTH_SHORT).show();
            }
        },WeiXinPayidDataBean.class);
    }

    /**
     * 支付宝参数接口
     */
    private void ALiPayData(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("outTradeNo", String.valueOf(System.currentTimeMillis()));
        map.put("subject", "测试商品");
        map.put("body", "商品描述");
        map.put("totalAmount", "0.01");
        map.put("timeoutExpress", "5d");

        String aliPayUrl = "http://192.168.31.15:8081/app-pay/alipay/tradeAppPay";
        VolleyContentFast.requestJsonByPost(aliPayUrl, map, new VolleyContentFast.ResponseSuccessListener<ZFBPayDataBean>() {
            @Override
            public void onResponse(ZFBPayDataBean jsonBean) {
                L.d("qwer_AliPay===>>" , "body = " + jsonBean.getBody());
                toAliPay(jsonBean.getBody());
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                L.d("qwer_AliPay===>>" , "error");
            }
        },ZFBPayDataBean.class);

    }

    /**
     * 调用微信支付
     * @param payInfo
     */
    private void toPay(WeiXinPayidDataBean.PayDataWX.PayDataMapWX.PayInfo payInfo){
        //注册appid
        IWXAPI api = WXAPIFactory.createWXAPI(this, APP_ID);// 通过WXAPIFactory工厂，获取IWXAPI的实例
        if (!api.isWXAppInstalled()) {
            Toast.makeText(this, mContext.getResources().getString(R.string.share_uninstall_webchat), Toast.LENGTH_SHORT).show();
        }
        api.registerApp(APP_ID); //将appid注册到微信

        //赋值微信支付所需参数
        PayReq payReq = new PayReq();
        payReq.appId = payInfo.getAppid();
        payReq.partnerId = payInfo.getPartnerid();
        payReq.prepayId = payInfo.getPrepayid();
        payReq.packageValue = payInfo.getPackages();
        payReq.nonceStr = payInfo.getNoncestr();
        payReq.timeStamp = payInfo.getTimestamp();
        payReq.sign = payInfo.getSign();
        api.sendReq(payReq);
    }

    /**
     * 调用支付宝支付
     */
    private void toAliPay(final String orderInfo){

        Runnable aliPayRun = new Runnable() {
            @Override
            public void run() {
                PayTask aliPay = new PayTask(BettingOnlinePaymentActivity.this);
                Map<String , String> payMap = aliPay.payV2(orderInfo , true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = payMap;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(aliPayRun);
        payThread.start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SDK_PAY_FLAG:{
                    PayResult mResult = new PayResult((Map<String , String>)msg.obj);
                    String result = mResult.getResult();
                    String resultStatus = mResult.getResultStatus();

                    L.d("qwer_asd " , result + " == " + resultStatus);
                    if ( TextUtils.equals(resultStatus , "9000")) {
                        Toast.makeText(mContext, "支付成功 > " + resultStatus, Toast.LENGTH_SHORT).show();
                    }else{
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mContext, "结果确认中 > " + resultStatus, Toast.LENGTH_SHORT).show();
                        }else if (TextUtils.equals(resultStatus, "6001")) {
                            Toast.makeText(mContext, "支付取消 > " + resultStatus, Toast.LENGTH_SHORT).show();
                        }else if (TextUtils.equals(resultStatus, "6002")) {
                            Toast.makeText(mContext, "网络异常 > " + resultStatus, Toast.LENGTH_SHORT).show();
                        }else if (TextUtils.equals(resultStatus, "5000")) {
                            Toast.makeText(mContext, "重复请求 > " + resultStatus, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mContext, "支付失败 > " + resultStatus, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 判断是否安装有支付宝客户端
     * @param context
     * @return
     */
    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }
}