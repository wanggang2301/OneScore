package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.AccessTokenKeeper;
import com.hhly.mlottery.util.ShareConstants;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang gang
 * @date 2016/5/6 11:02
 * @des ${TODO}
 */
public class SharePopupWindow extends PopupWindow implements IWeiboHandler.Response {


    private final static int WEB_CHAT = 0;
    private final static int WEB_FRIENDS = 1;
    private final static int QQ = 2;
    private final static int QZONE = 3;
    private final static int SINA = 4;
    private final static int COPY = 5;

    private Context mContext;
    private PopupWindow popupWindow;
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    // 图片封装为一个数组
    private int[] icon = {R.mipmap.webchat, R.mipmap.friends, R.mipmap.qq, R.mipmap.qzone, R.mipmap.sina, R.mipmap.copy};
    private int[] iconName = {R.string.webchat, R.string.friends, R.string.qq, R.string.qzone, R.string.sina, R.string.copy};

    private Button share;


    private IWXAPI api;

    public static Tencent mTencent;

    private IWeiboShareAPI mWeiboShareAPI;

    private int mExtarFlag = 0x00;



    public SharePopupWindow(Context mContext, Button btn_share) {
        this.mContext = mContext;
        share=btn_share;
        data_list = new ArrayList<Map<String, Object>>();
        getData();
        String[] from = {"image", "text"};
        int[] to = {R.id.image, R.id.text};
        sim_adapter = new SimpleAdapter(mContext, data_list, R.layout.item_share, from, to);
        showPopupWindow();
    }

    public List<Map<String, Object>> getData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", mContext.getResources().getString(iconName[i]).toString());
            data_list.add(map);
        }
        return data_list;
    }


    private void showPopupWindow() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.share_popmenu, null);
        gview = (GridView) view.findViewById(R.id.gview);
        gview.setAdapter(sim_adapter);
        TextView bt_clear = (TextView) view.findViewById(R.id.bt_clear);
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        if (popupWindow == null) {
            popupWindow = new PopupWindow(mContext);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
//			popupWindow.setFocusable(true); // 设置PopupWindow可获得焦点
            popupWindow.setTouchable(true); // 设置PopupWindow可触摸
            popupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
            popupWindow.setContentView(view);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnimationStyle(R.style.popuStyle);    //设置 popupWindow 动画样式
        }

        popupWindow.showAtLocation(share, Gravity.BOTTOM, 0, 0);
        popupWindow.update();


        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case WEB_CHAT:
                        Toast.makeText(mContext, "微信", Toast.LENGTH_SHORT).show();
                        shareweixin(0);
                        break;
                    case WEB_FRIENDS:
                        Toast.makeText(mContext, "朋友圈", Toast.LENGTH_SHORT).show();
                        shareweixin(1);
                        break;
                    case QQ:
                        mExtarFlag &= (0xFFFFFFFF - QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                        shareQQ();
                        Toast.makeText(mContext, "QQ", Toast.LENGTH_SHORT).show();
                        break;
                    case QZONE:
                        mExtarFlag |= QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN;
                        shareQQ();
                        Toast.makeText(mContext, "空间", Toast.LENGTH_SHORT).show();
                        break;
                    case SINA:
                        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, ShareConstants.SINA);

                        mWeiboShareAPI.registerApp();


                        shareSina();
                        //注册
                        Toast.makeText(mContext, "新浪微博", Toast.LENGTH_SHORT).show();
                        break;
                    case COPY:
                        ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        cmb.setText("复制的内容：www.13322.com");
                        Toast.makeText(mContext, "复制链接成功", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

    }


    private void shareweixin(int flag) {

        api = WXAPIFactory.createWXAPI(mContext, ShareConstants.WEB_CHAT_APP_ID, false);

        if (!api.isWXAppInstalled()) {
            Toast.makeText(mContext, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://baidu.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = "标题";
        msg.description = "描述：一比分微信分享测试";

        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag;
        boolean flg = api.sendReq(req);

    }


    public void shareQQ() {

        if (mTencent == null) {
            mTencent = Tencent.createInstance(ShareConstants.QQ_APP_ID, mContext);
        }

        final Bundle bundle = new Bundle();
        //这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, "来自一比分");
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, "豆瓣音乐-Lollipop &lt;Stand By Me> 1990 字数不够？再加点？");
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://douban.fm/?start=8508g3c27g-3&amp;cid=-3");
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://img3.douban.com/lpic/s3635685.jpg");
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用");
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);

        mTencent.shareToQQ((Activity) mContext, bundle, qqShareListener);
    }


    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {

        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
        }
    };


    public void shareSina() {

        boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
        int supportApiLevel = mWeiboShareAPI.getWeiboAppSupportAPI();

        sendMultiMessage();

    }


    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = "微博分享";
        return textObject;
    }

    private void sendMultiMessage() {

        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        // if (hasText) {
        weiboMessage.textObject = getTextObj();

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        AuthInfo authInfo = new AuthInfo(mContext, ShareConstants.SINA, ShareConstants.REDIRECT_URL, ShareConstants.SCOPE);

        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(mContext);

        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }

        mWeiboShareAPI.sendRequest((Activity)mContext, request, authInfo, token,
                new WeiboAuthListener() {
                    @Override
                    public void onWeiboException(WeiboException arg0) {
                    }

                    @Override
                    public void onComplete(Bundle bundle) {
                        // TODO Auto-generated method stub
                        Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                        AccessTokenKeeper.writeAccessToken(mContext, newToken);
                        Toast.makeText(mContext, "onAuthorizeComplete token = " + newToken.getToken(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                    }
                });
    }


    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    share.setText("微博分享成功");
                    Toast.makeText(mContext, "成功", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(mContext, "取消", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(mContext, "失败" + "Error Message: " + baseResp.errMsg,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
