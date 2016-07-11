package com.hhly.mlottery.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.enums.StatusEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * RecyclerView 的 EmptyView
 * <p/>
 * Created by loshine on 2016/6/30.
 */
public class EmptyView extends FrameLayout {

    public static final int LOADING_TYPE_PIC = 0;
    public static final int LOADING_TYPE_TXT = 1;

    @IntDef({LOADING_TYPE_PIC, LOADING_TYPE_TXT})
    @Retention(RetentionPolicy.SOURCE)
    @interface Type {
    }

    View mNoDataView;
    View mErrorView;
    View mLoadingView;

    View mProgress;
    View mLoadingText;

    public EmptyView(Context context) {
        super(context);
        init();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.layout_empty_view, this);
        mNoDataView = view.findViewById(R.id.nodata);
        mErrorView = view.findViewById(R.id.error);

        mProgress = view.findViewById(R.id.progress);
        mLoadingText = view.findViewById(R.id.loading);

        setLoadingType(LOADING_TYPE_PIC);
    }

    /**
     * 设置加载中的类型
     *
     * @param type type
     */
    public void setLoadingType(@Type int type) {
        switch (type) {
            case LOADING_TYPE_PIC:
                mLoadingView = mProgress;
                mLoadingText.setVisibility(GONE);
                break;
            case LOADING_TYPE_TXT:
                mLoadingView = mLoadingText;
                mProgress.setVisibility(GONE);
                break;
        }
    }

    /**
     * 设置状态
     *
     * @param status status
     */
    public void setStatus(@StatusEnum.Status int status) {
        mErrorView.setVisibility(status == StatusEnum.ERROR ? VISIBLE : GONE);
        mLoadingView.setVisibility(status == StatusEnum.LOADING ? VISIBLE : GONE);
        mNoDataView.setVisibility(status == StatusEnum.NORMAL ? VISIBLE : GONE);
    }

    /**
     * 设置错误重新加载
     *
     * @param listener listener
     */
    public void setOnErrorClickListener(View.OnClickListener listener) {
        mErrorView.findViewById(R.id.reloading_txt)
                .setOnClickListener(listener);
    }
}