package com.hhly.mlottery.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.ShareConstants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;



/**
 * @author wang gang
 * @date 2016/5/4 17:25
 * @des 微信返回结果(必须存在)
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    public static BaseResp RESP=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, ShareConstants.WE_CHAT_APP_ID, false);
        api.handleIntent(getIntent(), this);
        this.finish();
    }
    @Override
    public void onReq(BaseReq arg0) {

          this.finish();

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(getIntent(),this);

    }
    @Override
    public void onResp(BaseResp resp) {

//        LogManager.show(TAG, "resp.errCode:" + resp.errCode + ",resp.errStr:"
//                + resp.errStr, 1);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
               // Toast.makeText(getApplicationContext(), "分享成功eeee", Toast.LENGTH_SHORT).show();
               // System.out.println("success");// String code = ((SendAuth.Resp) resp).code; //即为所需的code\
                //RESP=resp;
                if(resp.getType()== ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){
                    this.finish();
                }
                  String code = ((SendAuth.Resp) resp).code;
                  PreferenceUtil.commitString("code",code);
                this.finish();
                //分享成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
               // Toast.makeText(getApplicationContext(), "分享取消fffff", Toast.LENGTH_SHORT).show();
              //  System.out.println("ERR_USER_CANCEL");
                this.finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
              //  Toast.makeText(getApplicationContext(), "分享成功vvvvv", Toast.LENGTH_SHORT).show();
              //  System.out.println("ERR_AUTH_DENIED");
                this.finish();
                //分享拒绝
                break;
        }
    }





}
