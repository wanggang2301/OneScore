package com.hhly.mlottery.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.BettingBuyResultEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.MvpBettingOnlinePaymentActivity;
import com.hhly.mlottery.util.L;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

/**
 * @author yixq
 * @date 2017-04-19
 * @des 微信支付的返回 回调类
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.entry);TODO*********

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
//        data.api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
//        api = WXAPIFactory.createWXAPI(this, "wx2a5538052969956e");
        api = WXAPIFactory.createWXAPI(this, ConstantPool.APP_ID);
        api.handleIntent(getIntent(), this);
        L.d("微信支付..." , "onCreate()");
//        regBtn = (Button) findViewById(R.id.reg_btn);
//        regBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // 将该app注册到微信
//                data.api.registerApp(Constants.APP_ID);
//            }
//        });
//
//        gotoBtn = (Button) findViewById(R.id.goto_send_btn);
//        gotoBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(WXPayEntryActivity.this, SendToWXActivity.class));
//                finish();
//            }
//        });
//
//        launchBtn = (Button) findViewById(R.id.launch_wx_btn);
//        launchBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(WXPayEntryActivity.this, "launch result = " + data.api.openWXApp(), Toast.LENGTH_LONG).show();
//            }
//        });
//
//        checkBtn = (Button) findViewById(R.id.check_timeline_supported_btn);
//        checkBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                int wxSdkVersion = data.api.getWXAppSupportAPI();
//                if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
//                    Toast.makeText(WXPayEntryActivity.this, "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline supported", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(WXPayEntryActivity.this, "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline not supported", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//        scanBtn = (Button) findViewById(R.id.scan_qrcode_login_btn);
//        scanBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(WXPayEntryActivity.this, ScanQRCodeLoginActivity.class));
//                finish();
//            }
//        });

//        //注意：
//        //第三方开发者如果使用透明界面来实现WXPayEntryActivity，需要判断handleIntent的返回值，如果返回值为false，
//        // 则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
//        try {
//            data.api.handleIntent(getIntent(), this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
        L.d("微信支付..." , "onNewIntent()");
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
//        switch (req.getType()) {
//            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//                goToGetMsg();
//                break;
//            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                goToShowMsg((ShowMessageFromWX.Req) req);
//                break;
//            default:
//                break;
//        }
        L.d("微信支付..." , "onReq()");
    }

    /**
     *  // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
     * 微信APP会返回到商户APP并回调onResp函数，需要在该函数中接收通知，判断返回错误码，
     * 如果支付成功则去后台查询支付结果再展示用户实际支付结果。
     * 注意一定不能以客户端返回作为用户支付的结果，应以服务器端的接收的支付通知或查询API返回的结果为准
     * @param resp
     */
    @Override
    public void onResp(BaseResp resp) {
        L.d("微信支付...onReq()" , "微信回调成功...resp.errCode= " + resp.errCode);

//        Toast.makeText(this, "resp.errCode = " + resp.errCode , Toast.LENGTH_SHORT).show();

        if(resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){//支付成功的回调码

            switch (resp.errCode){
                case BaseResp.ErrCode.ERR_OK:
                    L.d("完成回调..." , "支付成功");
                    EventBus.getDefault().post(new BettingBuyResultEventBusEntity(true));
//                    MvpBettingOnlinePaymentActivity.orderPay(); // 充值成功调用余额扣款接口
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    L.d("完成回调..." , "支付失败");
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    L.d("完成回调..." , "取消支付");
                    finish();
                    break;
                default:
                    L.d("完成回调..." , "支付未完成");
                    finish();
                    break;
            }
        }
    }

//    private void goToGetMsg() {
//        Intent intent = new Intent(this, GetFromWXActivity.class);
//        intent.putExtras(getIntent());
//        startActivity(intent);
//        finish();
//    }
//
//    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
//        WXMediaMessage wxMsg = showReq.message;
//        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
//
//        StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
//        msg.append("description: ");
//        msg.append(wxMsg.description);
//        msg.append("\n");
//        msg.append("extInfo: ");
//        msg.append(obj.extInfo);
//        msg.append("\n");
//        msg.append("filePath: ");
//        msg.append(obj.filePath);
//
//        Intent intent = new Intent(this, ShowFromWXActivity.class);
//        intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
//        intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
//        intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
//        startActivity(intent);
//        finish();
//    }
}
