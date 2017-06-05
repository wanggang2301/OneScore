package com.hhly.mlottery.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 描    述：下拉头部放大，上划收缩的layout 。指定某个view放大。现在还没有很好地跟下拉刷新结合。需要改进
 * 作    者：mady@13322.com
 * 时    间：2017/6/3
 */
public class PullToEnlargeCoorDinatorLayout extends CoordinatorLayout {

    //    用于记录下拉位置
    private float y = 0f;
    //    zoomView原本的宽高
    private int zoomViewWidth = 0;
    private int zoomViewHeight = 0;

    private int mAppbarOffSet=0;

    //    是否正在放大
    private boolean mScaling = false;

    //    放大的view，默认为第一个子view
    private View zoomView;
    public PullToEnlargeCoorDinatorLayout(Context context) {
        super(context);
    }

    public PullToEnlargeCoorDinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToEnlargeCoorDinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setZoomView(View zoomView ) {
        this.zoomView = zoomView;
    }

    //    滑动放大系数，系数越大，滑动时放大程度越大
    private float mScaleRatio = 0.4f;
    public void setmScaleRatio(float mScaleRatio) {
        this.mScaleRatio = mScaleRatio;
    }

    //    最大的放大倍数
    private float mScaleTimes = 2f;
    public void setmScaleTimes(int mScaleTimes) {
        this.mScaleTimes = mScaleTimes;
    }

    //    回弹时间系数，系数越小，回弹越快
    private float mReplyRatio = 0.5f;
    public void setmReplyRatio(float mReplyRatio) {
        this.mReplyRatio = mReplyRatio;
    }
    public void setOffSet(int offSet){
        this.mAppbarOffSet=offSet;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (zoomViewWidth <= 0 || zoomViewHeight <=0) {
            zoomViewWidth = zoomView.getMeasuredWidth();
            zoomViewHeight = zoomView.getMeasuredHeight();
        }
        if (zoomView == null || zoomViewWidth <= 0 || zoomViewHeight <= 0) {
            return super.onTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y=ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScaling) {
//                    if (() == 0) {
//                        y = ev.getY();//滑动到顶部时，记录位置
//                    } else {
//                        break;
//                    }
                }
                int distance = (int) ((ev.getY() - y)*mScaleRatio);
                if (distance < 0 ) { //上划
                    return super.onTouchEvent(ev);
                }else if(mAppbarOffSet==0){//若往下滑动
                    mScaling = true;
                    setZoom(distance);
                    return true;
                }

            case MotionEvent.ACTION_UP:
                mScaling = false;
                replyView();
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**放大view*/
    private void setZoom(float s) {
        float scaleTimes = (float) ((zoomViewWidth+s)/(zoomViewWidth*1.0));
//        如超过最大放大倍数，直接返回
        if (scaleTimes > mScaleTimes) return;

        ViewGroup.LayoutParams layoutParams = zoomView.getLayoutParams();
        layoutParams.width = (int) (zoomViewWidth + s);
        layoutParams.height = (int)(zoomViewHeight*((zoomViewWidth+s)/zoomViewWidth));
//        设置控件水平居中
        ((MarginLayoutParams) layoutParams).setMargins(-(layoutParams.width - zoomViewWidth) / 2, 0, 0, 0);
        zoomView.setLayoutParams(layoutParams);
    }

    /**回弹*/
    private void replyView() {
        final float distance = zoomView.getMeasuredWidth() - zoomViewWidth;
        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(distance, 0.0F).setDuration((long) (distance * mReplyRatio));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setZoom((Float) animation.getAnimatedValue());
            }
        });
        anim.start();
    }

}
