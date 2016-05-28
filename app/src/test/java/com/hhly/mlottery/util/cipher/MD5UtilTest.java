package com.hhly.mlottery.util.cipher;

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
}