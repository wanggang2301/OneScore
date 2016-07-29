package com.hhly.mlottery.bean.enums;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/6/28.
 */
public interface OddsTypeEnum {
    String PLATE = "plate"; // 亚盘
    String BIG = "big"; // 大小球
    String OP = "op"; // 欧赔

    /**
     * 定义注解限制类型
     */
    @StringDef({PLATE, BIG, OP})
    @Retention(RetentionPolicy.SOURCE)
    @interface OddsType {
    }
}