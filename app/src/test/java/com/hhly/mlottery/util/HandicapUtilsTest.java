package com.hhly.mlottery.util;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/8/11
 */
public class HandicapUtilsTest {

    @Test
    @Ignore("not impl")
    public void changeHandicap() throws Exception {

    }

    @Test
    public void changeHandicapByBigLittleBall() throws Exception {
        assertEquals("19.5", HandicapUtils.changeHandicapByBigLittleBall("19.5"));
        assertEquals("19/19.5", HandicapUtils.changeHandicapByBigLittleBall("19.25"));
        assertEquals("6.5/7", HandicapUtils.changeHandicapByBigLittleBall("6.75"));
        assertEquals("6", HandicapUtils.changeHandicapByBigLittleBall("6"));
    }

    @Test
    @Ignore("not impl")
    public void interChangeHandicap() throws Exception {

    }

}