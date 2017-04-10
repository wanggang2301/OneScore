package com.hhly.mlottery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.L;

/**
 */
public class ToolbarScrollerView extends LinearLayout {


    private static final String TAG = "ToolbarScrollerView";

    public ToolbarScrollerView(Context context) {
        super(context);
        init(null, 0);
    }

    public ToolbarScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);

    }

    public ToolbarScrollerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ToolbarScrollerView);
        int toolbarHeight = (int) typedArray.getDimension(R.styleable.ToolbarScrollerView_toolbarHeight, 100);
        int toolbarBackGroundHeight = (int) typedArray.getDimension(R.styleable.ToolbarScrollerView_toolbarBackGroundHeight, 100);

        maxPaddingTop = toolbarBackGroundHeight - toolbarHeight;

        typedArray.recycle();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        //L.e(TAG, "onInterceptTouchEvent " + ev.getAction());

        return super.onInterceptTouchEvent(ev);
    }


    private int initPaddingTop;
    private int initY;
    private int initX;

    private boolean isSmallToolbar = false;

    private int maxPaddingTop = 500;

    private boolean animRunnigUp = false;
    private boolean animRunnigDown = false;

    private int speed = 5;

    private int paddingExtent = 60;

    private ImageView mToolbarImg;
    private LinearLayout mToolbarContainer;

    public void setToolbarContainer(LinearLayout toolbarContainer) {
        this.mToolbarContainer = toolbarContainer;
    }

    public void setToolbarImg(ImageView toolbarImg) {
        this.mToolbarImg = toolbarImg;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        //L.e(TAG, "dispatchTouchEvent " + ev.getAction());
        if (mToolbarContainer == null || mToolbarImg == null|| isSmallToolbar) {
            return super.dispatchTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //L.e(TAG, "dispatchTouchEvent down");

                initPaddingTop = mToolbarContainer.getPaddingTop();
                if (initPaddingTop != -maxPaddingTop) {
                    isSmallToolbar = false;
                }
                initY = (int) ev.getY();
                initX = (int) ev.getX();

                //L.e(TAG, "initY = " + initY);
                animRunnigUp = false;
                animRunnigDown = false;
                //return false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = initY - (int) ev.getY();
                int deltaX = initX - (int) ev.getX();
                if (deltaY >= 60 && deltaX <= 30 && !isSmallToolbar) {

                    if (mToolbarContainer.getPaddingTop() <= 0 && mToolbarContainer.getPaddingTop() > -maxPaddingTop) {
                        if (initPaddingTop - deltaY > 0) {
                            setPaddingTop(0);
                        } else {
                            setPaddingTop((int) ((initPaddingTop - deltaY) / 1.6));
                        }
                    }
                    if (mToolbarContainer.getPaddingTop() <= -maxPaddingTop) {
                        isSmallToolbar = true;
                        setPaddingTop(-maxPaddingTop);
                    }
                    if (mToolbarContainer.getPaddingTop() == 0) {
                        isSmallToolbar = false;
                    }
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
                //L.e(TAG, "dispatchTouchEvent UP");
                animRunnigUp = true;
//                if (!isSmallToolbar) {
                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ////L.e(TAG, "__run___");
                        int paddingTop = mToolbarContainer.getPaddingTop();
                        if (paddingTop != 0 && paddingTop > -maxPaddingTop && animRunnigUp) {
                            //L.e(TAG, "padding....paddingTop = " + paddingTop);
                            paddingTop = paddingTop - paddingExtent;
                            if (paddingTop <= -maxPaddingTop) {
                                isSmallToolbar = true;
                                setPaddingTop(-maxPaddingTop);
                            } else {
                                setPaddingTop(paddingTop);
                            }

                            postInvalidate();


                            handler.postDelayed(this, speed);
                        }
                    }
                });

//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    }).start();
//                }

                break;

        }

        boolean dispatch = super.dispatchTouchEvent(ev);
        //L.d(TAG, "dispatch = " + dispatch);
        return dispatch;
    }


    public void openToolbar() {
        animRunnigDown = true;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ////L.e(TAG, "__run___");
                int paddingTop = mToolbarContainer.getPaddingTop();
                if (animRunnigDown && paddingTop < 0) {
                    //L.e(TAG, "padding....paddingTop = " + paddingTop);
                    paddingTop = paddingTop + paddingExtent;

                    if (paddingTop >= 0) {
                        isSmallToolbar = false;
                        setPaddingTop(0);
                    } else {
                        setPaddingTop(paddingTop);
                    }
                    postInvalidate();
                    handler.postDelayed(this, speed);
                }
            }
        });
    }

    int layoutHeight = 0;

    public void setPaddingTop(int paddingTop) {
        mToolbarContainer.setPadding(0, paddingTop, 0, 0);
        if (paddingChangedListener != null) {
            float percentageF = Math.abs((float) paddingTop / maxPaddingTop);
            int percentage = (int) (percentageF * 100);
            paddingChangedListener.onPaddingChanged(percentage);
        }

        if (layoutHeight == 0) {
            layoutHeight = mToolbarImg.getHeight();
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mToolbarImg.getWidth(), layoutHeight + paddingTop);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mToolbarImg.setLayoutParams(params);

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //L.e(TAG, "onTouchEvent " + ev.getAction());
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

    private PaddingChangedListener paddingChangedListener;

    public void setPaddingChangedListener(PaddingChangedListener paddingChangedListener) {
        this.paddingChangedListener = paddingChangedListener;
    }

    public interface PaddingChangedListener {

        void onPaddingChanged(int percentage);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);
    }
}
