package com.hhly.mlottery.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.fastjson.JSON;
import com.sohu.cyan.android.sdk.api.CallBack;
import com.sohu.cyan.android.sdk.api.Config;
import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.entity.AccountInfo;
import com.sohu.cyan.android.sdk.exception.CyanException;
import com.sohu.cyan.android.sdk.http.CyanRequestListener;
import com.sohu.cyan.android.sdk.http.response.AttachementResp;
import com.sohu.cyan.android.sdk.http.response.CommentActionResp;
import com.sohu.cyan.android.sdk.http.response.UserCommentResp;
import com.sohu.cyan.android.sdk.http.response.UserInfoResp;

import java.io.File;

/**
 * @author lzf
 * @ClassName:
 * @Description: 暢言工具類
 * @date
 */
public class CyUtils {
    public static boolean isLogin = false;
    public static final int JUMP_COMMENT_QUESTCODE = 3;//跳转登录界面的请求码
    public static final int RESULT_CODE = 5;
    public static final int RESULT_BACK = 11;
    public static final int JUMP_QUESTCODE = 1;//跳转全部评论页面的请求码
    public static final int SINGLE_PAGE_COMMENT = 30;//一页获取的评论数  30
    public static final int JUMP_RESULTCODE = 2;//一页获取的评论数
    public static final int RESULT_OK = -1;//登录界面返回的结果码
    public static final String ISHIDDENCOMMENTCOUNT = "isHiddenCommentCount";
    public static final String ISSHOWCOMMENT = "isShowComment";
    public static final String INTENT_PARAMS_SID = "sourceid";
    public static final String INTENT_PARAMS_TITLE = "title";
    public static String nickname = "";

    //初始化畅言
    public static void initCy(Context context) {
        Config config = new Config();
        try {
            if (AppConstants.isGOKeyboard) {
                CyanSdk.register(context, "cyslrkBTR", "021bf43427836304a81c1ff382f326e3",
                        "http://10.2.58.251:8081/login-success.html", config);//国际版
            } else {
                CyanSdk.register(context, "cysu2zve0", "cc350c756399934b20fc692beb0418f2",
                        "http://10.2.58.251:8081/login-success.html", config);//中国版
            }
            L.i("lzf初始化畅言成功");
        } catch (CyanException e) {
            e.printStackTrace();
            L.i("lzf初始化畅言失败");
        }
    }

    //添加评论功能  评论功能已单独封装成一个模块  调用的时候  只要以下代码就行
    public static void addComment(Fragment fragment, String url, String title, boolean ishiddencommentcount, boolean isshowcomment, FragmentManager fragmentManager, int container) {
        Bundle bundle = new Bundle();
        bundle.putString(CyUtils.INTENT_PARAMS_SID, url);//需要评论的文章的url或者其他唯一标识
        bundle.putString(CyUtils.INTENT_PARAMS_TITLE, title);//需要评论的文章的标题
        bundle.putBoolean(CyUtils.ISHIDDENCOMMENTCOUNT, ishiddencommentcount);//是否隐藏评论数按钮
        bundle.putBoolean(CyUtils.ISSHOWCOMMENT, isshowcomment);//是否显示评论列表
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().add(container, fragment).commit();
    }

    //单点登录
    public static void loginSso(String id, String nickname, CyanSdk sdk) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.isv_refer_id = id;//应用自己的用户id
        accountInfo.nickname = nickname;//应用自己的用户昵称
        sdk.setAccountInfo(accountInfo, new CallBack() {

            @Override
            public void success() {
                // token
//                Set<String> set = CyanSdk.getCookie();
                L.i("lzf登录成功");
                isLogin = true;
            }

            @Override
            public void error(CyanException e) {
                L.i("lzf登录失败");
                isLogin = false;
            }
        });
    }

    /*
    topicId	畅言分配的文章ID，通过loadTopic接口获取
    content	评论内容
    replyId	回复的评论，默认为0
    attachUrl	附件图片地址，通过attachUpload接口上传后获取
    score	打分
    appType	平台类型，40:ipone,41:ipad,43:android
    listener	实现CyanRequestListener接口类的实例
     */
    //提交评论 需登录  否则提交失败
    public static void submitComment(long topicid, String comment, CyanSdk sdk, CyanRequestListener requestListener) {

        try {
            sdk.submitComment(topicid, comment, 0, "", 43, 5.0f, "7", requestListener);

        } catch (CyanException e) {
            e.printStackTrace();
            L.i("lzfsendfail");
        }

    }

    /**
     * 隐藏原生键盘
     */
    public static void hideKeyBoard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    /**
     * 获取用户信息
     */
    public static void getUserInfo(CyanSdk sdk) {
        try {
            sdk.getUserInfo(new CyanRequestListener<UserInfoResp>() {
                @Override
                public void onRequestSucceeded(UserInfoResp userInfoResp) {
                    nickname = userInfoResp.nickname;

                }

                @Override
                public void onRequestFailed(CyanException e) {

                }
            });
        } catch (CyanException e) {
            e.printStackTrace();
        }

    }

    /**
     * 上传图片
     */
    public static void attachUpload(CyanSdk sdk, File file) {
        try {
            sdk.attachUpload(file, new CyanRequestListener<AttachementResp>() {
                @Override
                public void onRequestSucceeded(AttachementResp attachementResp) {
                    L.i(attachementResp.url);

                }

                @Override
                public void onRequestFailed(CyanException e) {

                }
            });
        } catch (CyanException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户做出的评论
     */
    public static void getUserComments(CyanSdk sdk, int page, int pagesize) {
        try {
            sdk.getUserComments(page, pagesize, new CyanRequestListener<UserCommentResp>() {
                @Override
                public void onRequestSucceeded(UserCommentResp userCommentResp) {

                }

                @Override
                public void onRequestFailed(CyanException e) {

                }
            });
        } catch (CyanException e) {
            e.printStackTrace();

        }
    }

    /**
     * 评论顶踩
     */
    public static void commentAction(CyanSdk sdk, long topicid, long commentid, CyanSdk.CommentActionType type) {
//        commentAction(sdk,1,2,CyanSdk.CommentActionType.CAI);
        try {
            sdk.commentAction(topicid, commentid, type, new CyanRequestListener<CommentActionResp>() {
                @Override
                public void onRequestSucceeded(CommentActionResp commentActionResp) {

                }

                @Override
                public void onRequestFailed(CyanException e) {

                }
            });
        } catch (CyanException e) {
            e.printStackTrace();
        }
    }
}
