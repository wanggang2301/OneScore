package com.hhly.mlottery.mvp.bettingmvp.mvpview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.LoginActivity;
import com.hhly.mlottery.bean.bettingbean.BalanceDataBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.BettingBuyResultEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.PayChargeMoneyResultEventBus;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.PayMentZFBResultEventBusEntity;
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
 * Created by：XQyi on 2017/6/1 16:08
 * Use:充值页面(view)
 */
public class MvpChargeMoneyActivity extends Activity implements View.OnClickListener {

    /** 签名参数 */
    private String PARAM_USER_ID = "userId";//用户id
    private String PARAM_LOGIN_TOKEN = "loginToken";//logintoken
    private String PARAM_SIGN = "sign";//参数签名
    private String PARAM_SERVICE = "service";//3 微信 4 支付宝
    private String PARAM_TRADE_AMOUNT = "tradeAmount";//金额 分
    private String PARAM_LANG = "lang";//语言环境
    private String PARAM_TIMEZONE = "timeZone";//时区环境

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
//    String payUrl = "http://192.168.10.242:8092/user/pay/recharge";
//    String payUrl = "http://m.1332255.com:81/user/pay/recharge";
    String payUrl = BaseURLs.URL_CHARGE_MONEY;


    /**
     * 余额查询的接口
     */
//    String balanceUrl = "http://192.168.10.242:8099/user/pay/balance";
//    String balanceUrl = "http://m.1332255.com:81/user/pay/balance";
    String balanceUrl = BaseURLs.URI_PAY_BALANCE;
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

        mapPrament.put(PARAM_USER_ID , userid);//用户ID
        mapPrament.put(PARAM_LOGIN_TOKEN , token);//登陆的token
        mapPrament.put(PARAM_LANG , MyApp.getLanguage());
        mapPrament.put(PARAM_TIMEZONE , AppConstants.timeZone + "");
        String signs = SignUtils.getSign(BaseURLs.PARAMENT_PAY_BALANCE , mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put(PARAM_USER_ID , userid);//用户ID
        map.put(PARAM_LOGIN_TOKEN , token);//登陆的token
        map.put(PARAM_SIGN , signs);//签名 和 登陆的时候签名一样

        L.d("qwer== >> " + signs);

        VolleyContentFast.requestJsonByPost(balanceUrl, map, new VolleyContentFast.ResponseSuccessListener<BalanceDataBean>() {
            @Override
            public void onResponse(BalanceDataBean jsonObject) {
                if (jsonObject == null || jsonObject.getCode() != 200) {
                    L.d("balance == >>>" , "余额接口无数据返回");
                    if (jsonObject.getCode() == 1012 || jsonObject.getCode() == 1013 || jsonObject.getCode() == 1000) {
                        //token 失效（为空）去登陆
                        AppConstants.register.setToken(null);//token 置空 重新登录
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.putExtra(ConstantPool.PUBLIC_INPUT_PARAMEMT , ConstantPool.PAY_CHARGE_MONEY_RESULT);
                        startActivity(intent);
                    }
                    return;
                }else{
                    if (jsonObject.getData() != null) {
                        L.d("balance == >>>" , "返回成功");
                        String balancess = UnitsUtil.fenToYuan(jsonObject.getData().getBalance().getAvailableBalance()); //元转分
                        balance.setText(jsonObject.getData().getBalance() == null ? "--" : "￥ " + (filtraNull(balancess)));
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
                    Toast.makeText(mContext, mContext.getResources().getText(R.string.betting_max_recharge), Toast.LENGTH_SHORT).show();
                }else if (money == 0){
                    Toast.makeText(mContext, mContext.getResources().getText(R.string.betting_payment_no_zero), Toast.LENGTH_SHORT).show();
                }else{
                    String moneyIn =  UnitsUtil.yuanToFen(paymentMoney.getText().toString());//获取充值金额 元==>分

                    L.d("充值的金额 ：" , moneyIn);
                    switch (PAYMENT_MONEY){
                        case 0:
                            PayMentUtils.ALiPayData(MvpChargeMoneyActivity.this ,payUrl ,getDataMap(ConstantPool.ZEB_SERVICE , moneyIn));
                            break;
                        case 1:
                            PayMentUtils.WeiXinPayData(payUrl , getDataMap(ConstantPool.WEIXIN_SERVICE , moneyIn));
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

        mapPrament.put(PARAM_USER_ID , userid);//用户ID
        mapPrament.put(PARAM_SERVICE , service);//3 微信 4 支付宝
        mapPrament.put(PARAM_TRADE_AMOUNT , money);//金额 分
        mapPrament.put(PARAM_LOGIN_TOKEN , token);//登陆的token
        mapPrament.put(PARAM_LANG , MyApp.getLanguage());
        mapPrament.put(PARAM_TIMEZONE , AppConstants.timeZone + "");
        String signs = SignUtils.getSign(BaseURLs.PARAMENT_CHARGE_MONEY , mapPrament);

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

        if (TextUtils.equals(resultStatus, ConstantPool.PAY_RESULT_STATUS_SUCCESS)) {
            Toast.makeText(mContext, mContext.getResources().getText(R.string.betting_payment_success), Toast.LENGTH_SHORT).show();
            /** 支付成功后回到当前页刷新余额接口*/
            initData();
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
            /** 支付成功后回到当前页刷新余额接口*/
            initData();
        }
    }

    /**
     * 登录页面返回
     * @param chargeMoneyResult
     */
    public void onEventMainThread(PayChargeMoneyResultEventBus chargeMoneyResult){
        if (chargeMoneyResult.isChargeResult()) {
            /**登录失效重新登录后返回 刷余额接口*/
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
