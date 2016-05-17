package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hhly.mlottery.R;
import com.hhly.mlottery.callback.ShareCopyLinkCallBack;
import com.hhly.mlottery.callback.ShareTencentCallBack;
import com.hhly.mlottery.util.AccessTokenKeeper;
import com.hhly.mlottery.util.NoDoubleClickUtils;
import com.hhly.mlottery.util.ShareConstants;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
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
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang gang
 * @date 2016/5/6 11:02
 * @des 分享
 */
public class SharePopupWindow extends PopupWindow implements IWeiboHandler.Response {

    private final static int WE_CHAT = 0; //微信好友
    private final static int WE_FRIENDS = 1;    //微信朋友圈
    private final static int QQ = 2;    //QQ
    private final static int QZONE = 3;  //QQ空间
    private final static int SINA = 4;    //新浪微博
    private final static int COPY = 5;    //复制链接
    private final static int MAX_IMAGE_SIZE = 200;

    private int[] icon = {R.mipmap.webchat, R.mipmap.friends, R.mipmap.qq, R.mipmap.qzone, R.mipmap.sina, R.mipmap.copy};
    private int[] iconName = {R.string.webchat, R.string.friends, R.string.qq, R.string.qzone, R.string.sina, R.string.copy};

    private Map<String, String> map = new HashMap<>();
    private List<Map<String, Object>> data_list;
    private Context mContext;
    private GridView gview;
    private ImageView share;
    private SimpleAdapter sim_adapter;
    public PopupWindow popupWindow;

    private IWXAPI api;    //微信Api
    private IWeiboShareAPI mWeiboShareAPI;    //新浪微博Api

    public ShareTencentCallBack mShareTencentCallBack;
    public ShareCopyLinkCallBack mShareCopyLinkCallBack;

    public void setmShareTencentCallBack(ShareTencentCallBack mShareTencentCallBack) {
        this.mShareTencentCallBack = mShareTencentCallBack;
    }
    public void setmShareCopyLinkCallBack(ShareCopyLinkCallBack mShareCopyLinkCallBack) {
        this.mShareCopyLinkCallBack = mShareCopyLinkCallBack;
    }

    public SharePopupWindow(Activity mContext, ImageView btn_share, Map<String, String> data) {
        this.mContext = mContext;
        this.share = btn_share;
        this.map = data;
        this.data_list = new ArrayList<Map<String, Object>>();
        getData();
        String[] from = {"image", "text"};
        int[] to = {R.id.image, R.id.text};
        this.sim_adapter = new SimpleAdapter(mContext, data_list, R.layout.item_share, from, to);
        showPopupWindow();
    }

    private List<Map<String, Object>> getData() {
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", mContext.getResources().getString(iconName[i]).toString());
            data_list.add(map);
        }
        return data_list;
    }


    /**
     * 分享弹窗
     * */
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
            popupWindow.setFocusable(true); // 设置PopupWindow可获得焦点
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
                    case WE_CHAT:
                        // Toast.makeText(mContext, "微信", Toast.LENGTH_SHORT).show();
                        shareweixin(0);
                        break;
                    case WE_FRIENDS:
                        //Toast.makeText(mContext, "朋友圈", Toast.LENGTH_SHORT).show();
                        shareweixin(1);
                        break;
                    case QQ:
                        if (!isQQClientAvailable(mContext)) {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.share_uninstall_qq), Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
                            return;
                        }

                        if (mShareTencentCallBack != null) {
                            mShareTencentCallBack.onClick(0);
                        }
                        popupWindow.dismiss();
                        // Toast.makeText(mContext, "QQ", Toast.LENGTH_SHORT).show();
                        break;
                    case QZONE:

                        if (!isQQClientAvailable(mContext)) {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.share_uninstall_qq), Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();

                            return;
                        }
                        if (mShareTencentCallBack != null) {
                            mShareTencentCallBack.onClick(1);
                        }
                        popupWindow.dismiss();

                        // Toast.makeText(mContext, "空间", Toast.LENGTH_SHORT).show();
                        break;
                    case SINA:
                        if (!NoDoubleClickUtils.isDoubleClick()) {
                            mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, ShareConstants.SINA);
                            mWeiboShareAPI.registerApp();
                            shareSina();
                        }
                        //注册
                        //   Toast.makeText(mContext, "新浪微博", Toast.LENGTH_SHORT).show();
                        break;
                    case COPY:
                        if (mShareCopyLinkCallBack != null) {
                            mShareCopyLinkCallBack.onClick();
                        }
                        popupWindow.dismiss();
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.share_copy), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    /**
     * 判断是否安装QQ客户端
     * */
    private boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 微信好友、微信朋友圈分享
     */
    private void shareweixin(final int flag) {
        api = WXAPIFactory.createWXAPI(mContext, ShareConstants.WE_CHAT_APP_ID, false);
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.share_uninstall_webchat), Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
            return;
        }
        VolleyContentFast.requestImage(map.get(ShareConstants.IMAGE_URL), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = map.get(ShareConstants.TARGET_URL);
                final WXMediaMessage wxmsg = new WXMediaMessage(webpage);
                wxmsg.title = map.get(ShareConstants.TITLE);
                wxmsg.description = map.get(ShareConstants.SUMMARY);
                if (response.getWidth() > 200 || response.getHeight() > 200) {
                    response = ThumbnailUtils.extractThumbnail(response, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE);
                }
                wxmsg.setThumbImage(response);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = wxmsg;
                req.scene = flag;
                boolean flg = api.sendReq(req);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = map.get(ShareConstants.TARGET_URL);
                final WXMediaMessage wxmsg = new WXMediaMessage(webpage);
                wxmsg.title = map.get(ShareConstants.TITLE);
                wxmsg.description = map.get(ShareConstants.SUMMARY);
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.share_default);
                wxmsg.setThumbImage(bitmap);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = wxmsg;
                req.scene = flag;
                boolean flg = api.sendReq(req);
            }
        });

        popupWindow.dismiss();
    }


    private TextObject getTextObj() {
        String text = mContext.getResources().getString(R.string.share_from) + map.get(ShareConstants.TITLE) + "\n" + map.get(ShareConstants.SUMMARY) + "\n";
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }

    /**
     * 新浪微博分享
     * */
    private void shareSina() {
        VolleyContentFast.requestImage(map.get(ShareConstants.IMAGE_URL), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                // 1. 初始化微博的分享消息
                WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                weiboMessage.textObject = getTextObj();
                ImageObject imageObject = new ImageObject();
                if (response.getWidth() > 200 || response.getHeight() > 200) {
                    response = ThumbnailUtils.extractThumbnail(response, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE);
                }

                imageObject.setImageObject(response);
                weiboMessage.imageObject = imageObject;
                WebpageObject mediaObject = new WebpageObject();
                mediaObject.identify = Utility.generateGUID();
                mediaObject.title = map.get(ShareConstants.TITLE);
                mediaObject.description = map.get(ShareConstants.SUMMARY);
                mediaObject.setThumbImage(response);
                mediaObject.actionUrl = map.get(ShareConstants.TARGET_URL);
                mediaObject.defaultText = "Webpage";
                weiboMessage.mediaObject = mediaObject;

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
                mWeiboShareAPI.sendRequest((Activity) mContext, request, authInfo, token, new WeiboAuthListener() {
                    @Override
                    public void onWeiboException(WeiboException arg0) {
                    }

                    @Override
                    public void onComplete(Bundle bundle) {
                        // TODO Auto-generated method stub
                        Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                        AccessTokenKeeper.writeAccessToken(mContext, newToken);
                        // Toast.makeText(mContext, "onAuthorizeComplete token = " + newToken.getToken(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                    }
                });

            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //出错加载默认图片
                WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                weiboMessage.textObject = getTextObj();
                ImageObject imageObject = new ImageObject();
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.share_default);
                imageObject.setImageObject(bitmap);
                weiboMessage.imageObject = imageObject;
                WebpageObject mediaObject = new WebpageObject();
                mediaObject.identify = Utility.generateGUID();
                mediaObject.title = map.get(ShareConstants.TITLE);
                mediaObject.description = map.get(ShareConstants.SUMMARY);
                mediaObject.setThumbImage(bitmap);
                mediaObject.actionUrl = map.get(ShareConstants.TARGET_URL);
                mediaObject.defaultText = "Webpage";
                weiboMessage.mediaObject = mediaObject;

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

                mWeiboShareAPI.sendRequest((Activity) mContext, request, authInfo, token, new WeiboAuthListener() {
                    @Override
                    public void onWeiboException(WeiboException arg0) {
                    }

                    @Override
                    public void onComplete(Bundle bundle) {
                        // TODO Auto-generated method stub
                        Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                        AccessTokenKeeper.writeAccessToken(mContext, newToken);
                        //Toast.makeText(mContext, "onAuthorizeComplete token = " + newToken.getToken(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }
        });
        popupWindow.dismiss();
    }


    /**
     * 新浪微博分享反馈
     * */
    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    //   Toast.makeText(mContext, "微博成功", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    // Toast.makeText(mContext, "微博取消", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    // Toast.makeText(mContext, "微博失败" + "Error Message: " + baseResp.errMsg,Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
