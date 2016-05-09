package com.hhly.mlottery.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.AccessTokenKeeper;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
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
import com.tencent.mm.sdk.modelmsg.WXTextObject;
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

public class DebugConfigActivity extends BaseActivity implements IWeiboHandler.Response {

    private final static String TAG = "DebugConfigActivity";

    public final static int URL_13322 = 1;
    public final static int URL_1332255 = 2;
    public final static int URL_242 = 5;

    public final static int WS_13322 = 3;
    public final static int WS_242 = 4;
    public final static int WS_82 = 6;

    private IWXAPI api;

    private final static int WEB_CHAT = 0;
    private final static int WEB_FRIENDS = 1;
    private final static int QQ = 2;
    private final static int QZONE = 3;
    private final static int SINA = 4;
    private final static int COPY = 5;

    /**
     * 分享测试
     */

    public static Tencent mTencent;
    private PopupWindow popupWindow;
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    // 图片封装为一个数组
    private int[] icon = {R.mipmap.webchat, R.mipmap.friends, R.mipmap.qq, R.mipmap.qzone, R.mipmap.sina, R.mipmap.copy};
    private int[] iconName = {R.string.webchat, R.string.friends, R.string.qq, R.string.qzone, R.string.sina, R.string.copy};

    private Button share;
    private int mExtarFlag = 0x00;


    private IWeiboShareAPI mWeiboShareAPI;

    SharePopupWindow shareFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config);


        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, ShareConstants.SINA);

        mWeiboShareAPI.registerApp();

        Button config_submit = (Button) findViewById(R.id.config_submit);


        //开发环境
        findViewById(R.id.config_rb5).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_rg2)).check(R.id.config_rb3);
            }
        });
        //测试环境
        findViewById(R.id.config_rb1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_rg2)).check(R.id.config_rb6);
            }
        });
        //生产环境
        findViewById(R.id.config_rb2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup) findViewById(R.id.config_rg2)).check(R.id.config_rb4);
            }
        });


        findViewById(R.id.weixin).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

               // shareFragment=new SharePopupWindow(mContext,share);
            }
        });


        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String[] from = {"image", "text"};
        int[] to = {R.id.image, R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.item_share, from, to);


        share = (Button) findViewById(R.id.share);

        share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();


            }
        });


        config_submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                RadioGroup config_rg1 = (RadioGroup) findViewById(R.id.config_rg1);

                if (config_rg1.getCheckedRadioButtonId() == R.id.config_rb1) {
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, URL_1332255);
                } else if (config_rg1.getCheckedRadioButtonId() == R.id.config_rb2) {
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, URL_13322);
                } else if (config_rg1.getCheckedRadioButtonId() == R.id.config_rb5) {
                    PreferenceUtil.commitInt(MyConstants.URL_HOME_CONFIG, URL_242);
                }

                RadioGroup config_rg2 = (RadioGroup) findViewById(R.id.config_rg2);
                if (config_rg2.getCheckedRadioButtonId() == R.id.config_rb3) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_242);
                } else if (config_rg2.getCheckedRadioButtonId() == R.id.config_rb4) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_13322);
                } else if (config_rg2.getCheckedRadioButtonId() == R.id.config_rb6) {
                    PreferenceUtil.commitInt(MyConstants.WS_HOME_CONFIG, WS_82);
                }

                startActivity(new Intent(DebugConfigActivity.this, WelcomeActivity.class));
                System.exit(0);

//				finish();


            }
        });

    }


    public List<Map<String, Object>> getData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", getResources().getString(iconName[i]).toString());
            data_list.add(map);
        }
        return data_list;
    }


    private void showPopupWindow() {

        View view = LayoutInflater.from(DebugConfigActivity.this).inflate(R.layout.share_popmenu, null);
        gview = (GridView) view.findViewById(R.id.gview);
        gview.setAdapter(sim_adapter);
        TextView bt_clear = (TextView) view.findViewById(R.id.bt_clear);
        bt_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        if (popupWindow == null) {
            popupWindow = new PopupWindow(DebugConfigActivity.this);
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
                        Toast.makeText(getApplicationContext(), "微信", Toast.LENGTH_SHORT).show();
                        shareweixin(0);
                        break;
                    case WEB_FRIENDS:
                        Toast.makeText(getApplicationContext(), "朋友圈", Toast.LENGTH_SHORT).show();
                        shareweixin(1);
                        break;
                    case QQ:
                        mExtarFlag &= (0xFFFFFFFF - QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                        shareQQ();
                        Toast.makeText(getApplicationContext(), "QQ", Toast.LENGTH_SHORT).show();
                        break;
                    case QZONE:
                        mExtarFlag |= QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN;
                        shareQQ();
                        Toast.makeText(getApplicationContext(), "空间", Toast.LENGTH_SHORT).show();
                        break;
                    case SINA:
                        shareSina();
                        //注册
                        Toast.makeText(getApplicationContext(), "新浪微博", Toast.LENGTH_SHORT).show();
                        break;
                    case COPY:
                        ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        cmb.setText("复制的内容：www.13322.com");
                        Toast.makeText(getApplicationContext(), "复制链接成功", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void share() {

        api = WXAPIFactory.createWXAPI(this, ShareConstants.WEB_CHAT_APP_ID, false);


        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = "你好啊，微信分享！";

        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
        // msg.title = "Will be ignored";
        msg.description = "你好啊，微信分享！";

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        //: SendMessageToWX.Req.WXSceneSession;

        // 调用api接口发送数据到微信
        api.sendReq(req);


    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    private void shareweixin(int flag) {

        api = WXAPIFactory.createWXAPI(this, ShareConstants.WEB_CHAT_APP_ID, false);


        if (!api.isWXAppInstalled()) {
            Toast.makeText(this, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://baidu.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = "标题";
        msg.description = "描述：一比分微信分享测试";

        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag;
        boolean flg = api.sendReq(req);

    }

    public void shareQQ() {

        if (mTencent == null) {
            mTencent = Tencent.createInstance(ShareConstants.QQ_APP_ID, this);
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

        mTencent.shareToQQ(this, bundle, qqShareListener);
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

        AuthInfo authInfo = new AuthInfo(this, ShareConstants.SINA, ShareConstants.REDIRECT_URL, ShareConstants.SCOPE);

        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());

        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }

        mWeiboShareAPI.sendRequest(this, request, authInfo, token,
                new WeiboAuthListener() {
                    @Override
                    public void onWeiboException(WeiboException arg0) {
                    }

                    @Override
                    public void onComplete(Bundle bundle) {
                        // TODO Auto-generated method stub
                        Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                        AccessTokenKeeper.writeAccessToken(getApplicationContext(), newToken);
                        Toast.makeText(getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "成功", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this, "失败" + "Error Message: " + baseResp.errMsg,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
