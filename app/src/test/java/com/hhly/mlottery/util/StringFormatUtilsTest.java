package com.hhly.mlottery.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/8/30
 */
public class StringFormatUtilsTest {
    @Test
    public void toPercentString() throws Exception {
        L.d(StringFormatUtils.toPercentString(0.23*0.25+0.27*0.25));
    }

}