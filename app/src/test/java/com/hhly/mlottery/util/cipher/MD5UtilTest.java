package com.hhly.mlottery.util.cipher;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.util.DeviceInfo;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * 作者：chenml on 2016/5/26 11:21
 * 邮箱：chenml@13322.com
 * 描述：
 */
public class MD5UtilTest extends TestCase {

    @Test
    public void testGetMD5() throws Exception {
        System.out.println(MD5Util.getMD5("hhly123456"));
    }

    @Test
    public void testGetSign(){
       // System.out.println(DeviceInfo.getSign("15079785931","868666021469647","B2A7748BF1FCAF6326979E1B86DC0C60"));
       // System.out.println(DeviceInfo.getSign("haha","hehe","hehe").length());
    }

    @Test
    public void testDeviceToken(){
        System.out.println(DeviceInfo.getDeviceId(MyApp.getContext()));
    }









}