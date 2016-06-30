package com.hhly.mlottery.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.enums.StatusEnum;

/**
 * RecyclerView 的 EmptyView
 * <p/>
 * Created by loshine on 2016/6/30.
 */
public class EmptyView extends FrameLayout {

    View mNoDataView;
    View mErrorView;
    View mLoadingView;

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
        mLoadingView = view.findViewById(R.id.progress);
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