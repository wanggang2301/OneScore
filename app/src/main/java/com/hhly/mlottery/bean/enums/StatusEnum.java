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

    int LOADING = 1; // 加载中
    int NORMAL = 0; // 正常（无数据）
    int ERROR = -1; // 错误

    /**
     * 定义注解限制类型
     */
    @IntDef({LOADING, NORMAL, ERROR})
    @Retention(RetentionPolicy.SOURCE)
    @interface Status {
    }
}
