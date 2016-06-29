package com.hhly.mlottery.bean.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Fragment、Activity 状态枚举
 * <p/>
 * Created by loshine on 2016/6/29.
 */
public interface StatusEnum {

    int LOADING = 1; // 亚盘
    int NO_DATA = 0; // 大小球
    int ERROR = -1; // 欧赔

    /**
     * 定义注解限制类型
     */
    @IntDef({LOADING, NO_DATA, ERROR})
    @Retention(RetentionPolicy.SOURCE)
    @interface Status {
    }
}
