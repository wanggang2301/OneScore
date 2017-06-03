package com.hhly.mlottery.mvp.bettingmvp.mvpview;

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
import com.hhly.mlottery.mvp.bettingmvp.MView;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.PayMentZFBResultEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.mvppresenter.MvpBettingOnlinePaymentPresenter;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseBean;
import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PayMentUtils;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by：XQyi on 2017/4/19 11:12
 * Use:支付页面（选择支付方式 [MVP-view 页面展示]）
 */

public class MvpBettingOnlinePaymentActivity extends BaseActivity implements MView<BasketDatabaseBean>, View.OnClickListener {

    // IWXAPI 是第三方app和微信通信的openapi接口
//    private IWXAPI api;

    //订单接口
//    String payUrl = "http://192.168.31.15:8081/sunon-web-api/pay/unifiedTradePay";
    String payUrl = "http://192.168.31.207:8092/pay/recharge";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.betting_recommend_online_payment_lay);
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
    }

    private void initData(){
        paymentPresenter.loadData("http://m.13322.com/mlottery/core/basketballData.findLeagueHeader.do");
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
                mPayZFB.setChecked(false);
                mPayWeiXin.setChecked(false);
                mPayYuE.setChecked(true);
                MODE_PAYMENT = ConstantPool.PAY_YU_E;
                break;
            case R.id.public_img_back:
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.betting_confirm_pay:
//                Toast.makeText(mContext, "去支付", Toast.LENGTH_SHORT).show();

                switch (MODE_PAYMENT){
                    case 0:
                        L.d("支付方式 = ","支付宝");
//                        ALiPayData();
                        PayMentUtils.ALiPayData(mContext , MvpBettingOnlinePaymentActivity.this , payUrl , getDataMap("4"));
                        break;
                    case 1:
                        L.d("支付方式 = ","微信");
//                        WeiXinPayData();
                        PayMentUtils.WeiXinPayData(mContext , payUrl , getDataMap("3"));
                        break;
                    case 2:
                        L.d("支付方式 = ","余额支付");
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
    public void loadSuccessView(BasketDatabaseBean basketDatabaseBean) {
        mBalance.setText(basketDatabaseBean.getLeagueId() + " K");
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
}