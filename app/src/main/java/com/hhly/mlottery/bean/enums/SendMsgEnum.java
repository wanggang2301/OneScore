package com.hhly.mlottery.bean.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * desc:聊球发送消息状态
 * Created by 107_tangrr on 2016/12/24 0024.
 */

public interface SendMsgEnum {
    int SEND_LOADING = 1; // 发送中
    int SEND_SUCCESS = 0; // 发送成功
    int SEND_ERROR = -1; // 发送失败

    @IntDef({SEND_LOADING,SEND_SUCCESS,SEND_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    @interface SendMsg{
    }
}
