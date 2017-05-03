package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.bettingbean.WeiFuTongPayidDataBean;
import com.hhly.mlottery.bean.bettingbean.WeiXinPayidDataBean;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/19.
 */

public class BettingOnlinePaymentActivity extends BaseActivity implements View.OnClickListener {
    //AppId,官网申请的合法id
    private static final String APP_ID = "wx2a5538052969956e";
    // IWXAPI 是第三方app和微信通信的openapi接口
//    private IWXAPI api;

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
                WeiXinPayData();
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
        VolleyContentFast.requestJsonByPost(aliPayUrl, map, new VolleyContentFast.ResponseSuccessListener<WeiXinPayidDataBean>() {
            @Override
            public void onResponse(WeiXinPayidDataBean jsonObject) {

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        },WeiXinPayidDataBean.class);

    }

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
}