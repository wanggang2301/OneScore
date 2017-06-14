package com.hhly.mlottery.util.net.account;

/**
 * Created by lyx on 2016/5/19.
 * 账户模块请求返回码 ， 比如 login、register 等
 */
public interface AccountResultCode {
    /**
     * 成功
     */
    int SUCC = 200;

    /**
     * 系统错误
     */
    int SYSTEM_ERROR = 1;

    /**
     * 传递参数有误
     */
    int PARAM_ERROR = 1001;

    /**
     * 没有对应的协议
     */
    int NO_AGGREEMENT = 3;

    /**
     * 邮箱格式错误
     */
    int MAIL_FORMAT_ERROR = 4;

    /**
     * 该邮箱已注册 反之对应0
     */
    int MAIL_ALREADY_REGISTER = 5;

    /**
     * 验证码错误
     */
    int VERIFY_CODE_ERROR = 1011;

    /**
     * 该用户已激活
     */
    int USER_EXIST = 7;

    /**
     * 验证码已失效
     */
    int VERIFY_CODE_INVALIDATE = 8;

    /**
     * 该手机号已注册
     */
    int PHONE_ALREADY_EXIST = 1005;

    /**
     * 该手机号未注册
     */
    int PHONE_ALREADY_NEXIST = 1006;

    /**
     * 两次发送间隔不得小于60秒
     */
    int INTERVEL_LESS = 10;

    /**
     * 登录名或者密码错误
     */
    int USERNAME_PASS_ERROR = 1002;

    /**
     * 手机号码不合法
     */
    int PHONE_FORMAT_ERROR = 1004;

    /**
     * 用户不存在
     */
    int USER_NOT_EXIST = 1003;

    /**
     * 短信发送失败
     */
    int MESSAGE_SEND_FAIL = 14;

    /**
     * 该昵称已存在 反之对应0
     */
    int NICKNAME_EXIST = 15;

    /**
     * 用户名已存在 反之对应0
     */
    int USERNAME_EXIST = 16;

    /**
     * 登录账号错误
     */
    int USERNAME_ERROR = 17;

    /**
     * 平台不存在
     */
    int PLATFORM_NOT_EXIST = 18;

    /**
     * 操作类型不存在
     */
    int OPERATOR_TYPE_NOT_EXIST = 19;

    /**
     * 获取qq用户信息失败
     */
    int GET_QQ_INFO_FAIL = 20;

    /**
     * 获取微博用户信息失败
     */
    int GET_WEIBO_INFO_FAIL = 21;

    /**
     * 用户未登陆
     */
    int USER_NOT_LOGIN = 22;

    /**
     * 真实姓名错误
     */
    int REALNAMW_ERROR = 41;

    /**
     * 身份证格式错误
     */
    int ID_CARD_FORMAT_ERROR = 42;

    /**
     * 密码错误
     */
    int PASSWORD_ERROR = 43;

    /**
     * 用户名敏感
     */
    int NICKNAME_SENSITIVE = 70;

    /**
     * 后台报错
     */
    int SERVER_ERROR = 500;

    /**
     * 同一个手机号一天只能发送五次短信
     */
    int ONLY_FIVE_EACHDAY = 1008;

    /*邀请码不存在*/
    int INVITED_NUMBER_NOON = 143;

    /*禁止登录用户*/
    int NO_LOGON_USER = 1009;

    /*未知错误*/
    int UNKNOWN_ERROR = 1010;

    /*令牌(token)为空*/
    int TOKEN_NULL = 1012;

    /*用户名与手机号不匹配*/
    int PHONE_MISMATCHING = 1007;

    /*令牌(token)失效*/
    int TOKEN_INVALID = 1013;

    /*签名为空*/
    int SIGNATURE_EMPTY = 1014;

    /*签名不匹配*/
    int SIGNATURE_MISMATCHING = 1015;

    /*令牌(token)与个人信息不匹配*/
    int TOKEN_MISMATCHING = 1016;

    /*修改专家简介信息成功*/
    int REVISE_PROFILE_SUCCESSFULLY = 2000;

    /*身份证号码格式错误*/
    int NUMBER_FORMAT_IS_WRONG = 2001;

    /*专家认证审核中*/
    int EXPERT_CERTIFICATION_AUDIT = 2002;

    /*用户已经是专家*/
    int USER_ALREADY_EXPERT = 2003;

    /*用户id不能为空*/
    int USER_NOT_NULL = 2004;

    /*用户未提交专家认证信息*/
    int   NOT_SUBMIT_EXPERT_AUTHENTICATION  = 2005;

    /*语言参数错误*/
    int  LANGUAGE_PARAMETER_ERROR = 2006;

    /** 用户在其他设备登录 */
    int TOKEN_LOSE_EFFICACY_BY_OTHER_LOGIN_ERROR_CODE = 1000;

}
