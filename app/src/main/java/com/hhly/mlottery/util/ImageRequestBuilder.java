package com.hhly.mlottery.util;

import android.support.annotation.IntDef;
import android.widget.ImageView;

import com.bumptech.glide.GenericRequestBuilder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 对 Glide 图片请求封装
 * <p/>
 * Created by loshine on 2016/7/12.
 */
public class ImageRequestBuilder {

    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;
    public static final int PRIORITY_IMMEDIATE = 3;

    @IntDef({PRIORITY_LOW, PRIORITY_NORMAL, PRIORITY_HIGH, PRIORITY_IMMEDIATE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Priority {
    }

    private GenericRequestBuilder builder;

    public ImageRequestBuilder(GenericRequestBuilder builder) {
        this.builder = builder;
    }

    public ImageRequestBuilder priority(@Priority int priority) {
        com.bumptech.glide.Priority pri = null;
        switch (priority) {
            case PRIORITY_HIGH:
                pri = com.bumptech.glide.Priority.HIGH;
                break;
            case PRIORITY_IMMEDIATE:
                pri = com.bumptech.glide.Priority.IMMEDIATE;
                break;
            case PRIORITY_LOW:
                pri = com.bumptech.glide.Priority.LOW;
                break;
            case PRIORITY_NORMAL:
                pri = com.bumptech.glide.Priority.NORMAL;
                break;
        }
        builder = builder.priority(pri);
        return this;
    }

    /**
     * 将图片加载到 ImageView
     *
     * @param imageView imageView
     */
    public void into(ImageView imageView) {
        builder.into(imageView);
    }
}
