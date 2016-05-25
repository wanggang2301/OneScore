package com.hhly.mlottery.util.net.account;

/**
 * Created by lyx on 2016/5/20.
 */
public interface RequestField {
    /**账号 , 不一定是手机号*/
    String ACCOUNT = "account";

    String PASSWORD = "password";

    /**账号注册类型(1用户名2手机号3邮箱)*/
    String TYPE = "type";

    String SMSCODE = "smsCode";

    String IP = "ip";

    /**设备令牌*/
    String DEVICETOKEN = "deviceToken";

    String PHONE = "phone";

    /**操作类型(1,手机注册2绑定手机3修改登录密码4忘记密码）*/
    String OPERATETYPE  = "operateType";

    /**验证类型 1用户名 2 手机号 3 邮箱*/
    String ACCOUNTTYPE = "accountType";

    String OLDPASSWORD = "oldPassword";

    String NEWPASSWORD = "newPassword";

    String LOGINTOKEN = "loginToken";

}
