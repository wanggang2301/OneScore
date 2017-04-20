package com.hhly.mlottery.frame;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.ShareBean;
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
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang gang
 * @date 2016/6/15 14:58
 * @des 社会化分享
 */
public class ShareFragment extends BottomSheetDialogFragment implements IWeiboHandler.Response {


    private View mView;

    private final static String SHARE_PARM = "SHARE_PARM";

    private final static int WE_CHAT = 0; //微信好友
    private final static int WE_FRIENDS = 1;    //微信朋友圈
    private final static int QQ = 2;    //QQ
    private final static int QZONE = 3;  //QQ空间
    private final static int SINA = 4;    //新浪微博
    private final static int COPY = 5;    //复制链接
    private final static int MAX_IMAGE_SIZE = 200;

    private int[] icon = {R.mipmap.webchat, R.mipmap.friends, R.mipmap.qq, R.mipmap.qzone, R.mipmap.sina, R.mipmap.copy};
    private int[] iconName = {R.string.webchat, R.string.friends, R.string.qq, R.string.qzone, R.string.sina, R.string.copy};

    private List<Map<String, Object>> data_list;
    private Context mContext;
    private GridView gview;
    private SimpleAdapter sim_adapter;

    private IWXAPI api;    //微信Api
    private IWeiboShareAPI mWeiboShareAPI;    //新浪微博Api

    private Tencent mTencent;

    private ShareBean mShareBean;

    //测试各类分享成功回调log
    private static String MYLOG_TYPE = "info";// 输入日志类型，w代表只输出告警信息等，v代表输出所有信息
    private static String MYLOG_PATH_SDCARD_DIR = "/sdcard/";// 日志文件在sdcard中的路径
    private static String MYLOGFILEName = "Log.txt";// 本类输出的日志文件名称
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格式
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
    private static Boolean MYLOG_SWITCH = false; // 日志文件总开关

    public static ShareFragment newInstance(ShareBean shareBean) {
        Bundle args = new Bundle();
        args.putParcelable(SHARE_PARM, shareBean);
        ShareFragment fragment = new ShareFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mShareBean = args.getParcelable(SHARE_PARM);
        }
        this.mContext = getActivity();
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mView = View.inflate(getContext(), R.layout.share_popmenu, null);
        initView();
        dialog.setContentView(mView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) mView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();


        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setHideable(true);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        ShareFragment.this.dismiss();
                    }
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });
        }
    }

    private void initView() {

        TextView bt_clear = (TextView) mView.findViewById(R.id.bt_clear);

        this.data_list = new ArrayList<Map<String, Object>>();
        getData();
        String[] from = {"image", "text"};
        int[] to = {R.id.image, R.id.text};
        this.sim_adapter = new SimpleAdapter(mContext, data_list, R.layout.item_share, from, to);
        gview = (GridView) mView.findViewById(R.id.gview);
        gview.setAdapter(sim_adapter);

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareFragment.this.dismiss();
            }
        });


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
                            ShareFragment.this.dismiss();
                            return;
                        }

                        onClickShare(0);
                        ShareFragment.this.dismiss();
                        break;
                    case QZONE:

                        if (!isQQClientAvailable(mContext)) {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.share_uninstall_qq), Toast.LENGTH_SHORT).show();
                            ShareFragment.this.dismiss();
                            return;
                        }
                        onClickShare(1);
                        ShareFragment.this.dismiss();

                        break;
                    case SINA:
                        if (!NoDoubleClickUtils.isDoubleClick()) {
                            mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, ShareConstants.SINA);
                            mWeiboShareAPI.registerApp();
                            shareSina();
                            //sddd
                        }
                        break;
                    case COPY:
                        if (mShareBean.getCopy() != null || !mShareBean.getCopy().equals("")) {
                            ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            cmb.setText(mShareBean.getCopy());
                            ShareFragment.this.dismiss();
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.share_copy), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
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
     * 判断是否安装QQ客户端
     */
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

    private void onClickShare(int flag) {
        mTencent = Tencent.createInstance(ShareConstants.QQ_APP_ID, mContext);
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);

        String title = mShareBean.getTitle();
        String appname = getString(R.string.share_to_qq_app_name);
        String summary = mShareBean.getSummary();

        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mShareBean.getTarget_url());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mShareBean.getImage_url());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appname);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, flag);
        mTencent.shareToQQ(getActivity(), params, qqShareListener);
    }


    /**
     * 微信好友、微信朋友圈分享
     */
    private void shareweixin(final int flag) {

        api = WXAPIFactory.createWXAPI(mContext, ShareConstants.WE_CHAT_APP_ID, false);
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.share_uninstall_webchat), Toast.LENGTH_SHORT).show();
            ShareFragment.this.dismiss();
            return;
        }
        VolleyContentFast.requestImage(mShareBean.getImage_url(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = mShareBean.getTarget_url();
                final WXMediaMessage wxmsg = new WXMediaMessage(webpage);
                wxmsg.title = mShareBean.getTitle();
                wxmsg.description = mShareBean.getSummary();
                if (response.getWidth() > 200 || response.getHeight() > 200) {
                    response = ThumbnailUtils.extractThumbnail(response, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE);
                }
                wxmsg.setThumbImage(response);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = wxmsg;
                req.scene = flag;
                boolean flg = api.sendReq(req);

               /* if (flg) {
                    writeLogtoFile(MYLOG_TYPE, "shareFragment", "微信成功");

                } else {
                    writeLogtoFile(MYLOG_TYPE, "shareFragment", "微信失败");

                }*/
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = mShareBean.getTarget_url();
                final WXMediaMessage wxmsg = new WXMediaMessage(webpage);
                wxmsg.title = mShareBean.getTitle();
                wxmsg.description = mShareBean.getSummary();
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                wxmsg.setThumbImage(bitmap);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = wxmsg;
                req.scene = flag;
                boolean flg = api.sendReq(req);
            }
        });
        ShareFragment.this.dismiss();
    }

    /**
     * 新浪微博分享
     */
    private void shareSina() {
        VolleyContentFast.requestImage(mShareBean.getImage_url(), new Response.Listener<Bitmap>() {
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
                mediaObject.title = mShareBean.getTitle();
                mediaObject.description = mShareBean.getSummary();
                mediaObject.setThumbImage(response);
                mediaObject.actionUrl = mShareBean.getTarget_url();
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
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                imageObject.setImageObject(bitmap);
                weiboMessage.imageObject = imageObject;
                WebpageObject mediaObject = new WebpageObject();
                mediaObject.identify = Utility.generateGUID();
                mediaObject.title = mShareBean.getTitle();
                mediaObject.description = mShareBean.getSummary();
                mediaObject.setThumbImage(bitmap);
                mediaObject.actionUrl = mShareBean.getTarget_url();
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
        ShareFragment.this.dismiss();
    }


    private TextObject getTextObj() {
        String text = mContext.getResources().getString(R.string.share_from) + mShareBean.getTitle() + "\n" + mShareBean.getSummary() + "\n";
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }


    /**
     * 新浪微博分享反馈
     */
    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    //writeLogtoFile(MYLOG_TYPE, "shareFragment", "微博成功");
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    //writeLogtoFile(MYLOG_TYPE, "shareFragment", "微博取消");

                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    // writeLogtoFile(MYLOG_TYPE, "shareFragment", "微博失败");


                    break;
            }
        }
    }

    //wsdkf 


    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            // writeLogtoFile(MYLOG_TYPE, "shareFragment", "QQ取消");
        }

        @Override
        public void onComplete(Object response) {
            // writeLogtoFile(MYLOG_TYPE, "shareFragment", "QQ取消");
        }

        @Override
        public void onError(UiError e) {
            // writeLogtoFile(MYLOG_TYPE, "shareFragment", "QQ失败");
        }
    };


    private static void writeLogtoFile(String mylogtype, String tag, String text) {// 新建或打开日志文件

        if (MYLOG_SWITCH) {
            Date nowtime = new Date();
            String needWriteFiel = logfile.format(nowtime);
            String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype
                    + "    " + tag + "    " + text;
            File file = new File(MYLOG_PATH_SDCARD_DIR, needWriteFiel
                    + MYLOGFILEName);
            try {
                FileWriter filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
                BufferedWriter bufWriter = new BufferedWriter(filerWriter);
                bufWriter.write(needWriteMessage);
                bufWriter.newLine();
                bufWriter.close();
                filerWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
