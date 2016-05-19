package com.hhly.mlottery.util.net;

/**
 * Created by lyx on 2016/5/19.
 */
public interface AccountResultCode {
    /**成功*/
    int SUCC = 0;

    /**系统错误*/
    int SYSTEM_ERROR =1;

    /**传递参数有误*/
    int PARAM_ERROR =2;

    /**没有对应的协议*/
    int NO_AGGREEMENT =3;

    /**邮箱格式错误*/
    int MAIL_FORMAT_ERROR =4;

    /**该邮箱已注册 反之对应0*/
    int MAIL_ALREADY_REGISTER =5;

    /**验证码错误*/
    int VERIFY_CODE_ERROR =6;

    /**该用户已激活*/
    int USER_EXIST =7;

    /**验证码已失效*/
    int VERIFY_CODE_INVALIDATE =8;

    /**该手机号已注册 反之对应0*/
    int PHONE_ALREADY_EXIST =9;

    /**两次发送间隔不得小于60秒*/
    int INTERVEL_LESS =10;

    /**登录名或者密码错误*/
    int USERNAME_PASS_ERROR =11;

    /**手机号码不合法*/
    int PHONE_FORMAT_ERROR =12;

    /**用户不存在*/
    int USER_NOT_EXIST =13;

    /**短信发送失败*/
    int MESSAGE_SEND_FAIL =14;

    /**该昵称已存在 反之对应0*/
    int NICKNAME_EXIST =15;

    /**用户名已存在 反之对应0*/
    int USERNAME_EXIST =16;

    /**登录账号错误*/
    int USERNAME_ERROR =17;

    /**平台不存在*/
    int PLATFORM_NOT_EXIST =18;

    /**操作类型不存在*/
    int OPERATOR_TYPE_NOT_EXIST =19;

    /**获取qq用户信息失败*/
    int GET_QQ_INFO_FAIL =20;

    /**获取微博用户信息失败*/
    int GET_WEIBO_INFO_FAIL =21;

}
