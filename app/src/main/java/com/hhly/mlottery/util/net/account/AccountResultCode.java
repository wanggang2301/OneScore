package com.hhly.mlottery.util.net.account;

/**
 * Created by lyx on 2016/5/19.
 * 账户模块请求返回码 ， 比如 login、register 等
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

    /**用户未登陆*/
    int USER_NOT_LOGIN =22;

    /**真实姓名错误*/
    int REALNAMW_ERROR =41;

    /**身份证格式错误*/
    int ID_CARD_FORMAT_ERROR =42;

    /**密码错误*/
    int PASSWORD_ERROR =43;

    /**用户名敏感*/
    int NICKNAME_SENSITIVE =70;

    /**后台报错*/
    int SERVER_ERROR =500;

    /**同一个手机号一天只能发送五次短信*/
    int ONLY_FIVE_EACHDAY = 25;

}
