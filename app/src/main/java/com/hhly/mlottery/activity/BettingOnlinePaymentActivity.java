package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.bettingbean.Weixinpayidbean;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/19.
 */

public class BettingOnlinePaymentActivity extends BaseActivity implements View.OnClickListener {

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

                Map<String, String> params = new HashMap<>();
                params.put("service", "unified.trade.pay");//接口类型
                params.put("outTradeNo", "ybf20170419");//商户订单号
                params.put("body", "测试购买商品");//商品描述
                params.put("totalFee", "1");//总金额 (分)

                String url = "http://192.168.31.15:8083/sunon-web-api/pay/unifiedTradePay";

                VolleyContentFast.requestJsonByPost(url,params , new VolleyContentFast.ResponseSuccessListener<Weixinpayidbean>() {

                    @Override
                    public void onResponse(Weixinpayidbean jsondata) {

                        Weixinpayidbean.PayData.PayDataMap mDetailsData = jsondata.getData().getDataMap();

                        L.d("yxq0419===" , jsondata.getMsg() + " >>token_id= " + mDetailsData.getToken_id());

                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                    }
                },Weixinpayidbean.class);




                break;
        }
    }
}
