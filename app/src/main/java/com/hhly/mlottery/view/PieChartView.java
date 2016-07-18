package com.hhly.mlottery.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * 百分比数字扇形图
 * <p>
 * Created by loshine on 2016/7/15.
 */
public class PieChartView extends View {

    private String mContent; /* 中间的文本 */
    private double mPercent; /* 百分比 */
    private int mColor; /* 扇形颜色 */
    private int mBackgroundColor; /* 圆形背景色 */
    private int mCenterBackgroundColor; /* 中央背景色 */
    private int mTextColor; /* 文本颜色 */

    private Paint mBackgroundPaint; /* 背景画笔 */
    private Paint mPiePaint; /* 扇形画笔 */
    private Paint mCenterPaint; /* 中央圆形区域画笔 */
    private Paint mTextPaint; /* 文本画笔 */

    public PieChartView(Context context) {
        super(context);
        initPaints();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaints();
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaints();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
        initPaints();
    }

    /**
     * 初始化属性
     *
     * @param context context
     * @param attrs   attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {

    }

    /**
     * 初始化画笔操作
     */
    private void initPaints() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.GRAY);
//        private Paint mPiePaint; /* 扇形画笔 */
//        private Paint mCenterPaint; /* 中央圆形区域画笔 */
//        private Paint mTextPaint; /* 文本画笔 */
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(100, 100, 100, mBackgroundPaint);
    }
}
