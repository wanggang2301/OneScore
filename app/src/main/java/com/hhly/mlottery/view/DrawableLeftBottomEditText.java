package com.hhly.mlottery.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;

/**
 * Created by：XQyi on 2017/6/13 17:14
 * Use:
 */
public class DrawableLeftBottomEditText extends EditText{

    public DrawableLeftBottomEditText(Context context) {
        super(context);
    }

    public DrawableLeftBottomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableLeftBottomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


//    @Override
//    protected void onDraw(Canvas canvas) {
//
//        Drawable[] drawables = getCompoundDrawables();
//        if (drawables != null) {
//            Drawable drawableLeft = drawables[0];
//            if (drawableLeft != null && !TextUtils.isEmpty(getHint())) {
//                Rect r = drawableLeft.getBounds();
//                Rect rect = new Rect();
//                getPaint().getTextBounds(getHint().toString(), 0, 1, rect);/*Hint.length>=1*/
////                int ds = r.bottom - rect.bottom;
//                int ds = r.top - rect.top;
//                setGravity(Gravity.TOP);
//                canvas.translate(0, ds/2);
//            }
//        }
//        super.onDraw(canvas);
//    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        Drawable[] drawables = getCompoundDrawables();
//        if (drawables != null) {
//            Drawable drawableLeft = drawables[0];
//            if (drawableLeft != null) {
//                float textWidth = getPaint().measureText(getText().toString());
//                int drawablePadding = getCompoundDrawablePadding();
//                int drawableWidth = 0;
//                drawableWidth = drawableLeft.getIntrinsicWidth();
//                float bodyWidth = textWidth + drawableWidth + drawablePadding;
//                canvas.translate((getWidth() - bodyWidth) / 2, 0);
//            }
//        }
//        super.onDraw(canvas);
//    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        Drawable[] drawables = getCompoundDrawables();
//        if (drawables != null) {
//            Drawable drawableLeft = drawables[2];
//            if (drawableLeft != null) {
//
//                float textWidth = getPaint().measureText(getText().toString());
//                int drawablePadding = getCompoundDrawablePadding();
//                int drawableWidth = 0;
//                drawableWidth = drawableLeft.getIntrinsicWidth();
//                float bodyWidth = textWidth + drawableWidth + drawablePadding;
//                setPadding(0, 0, (int)(getWidth() - bodyWidth), 0);
//                canvas.translate((getWidth() - bodyWidth) / 2, 0);
//            }
//        }
//        super.onDraw(canvas);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[1];
            if (drawableLeft != null) {

                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                setPadding(0, 0, (int)(getWidth() - bodyWidth), 0);
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            }
        }
        super.onDraw(canvas);
    }




//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        //All these logic should happen before onDraw()
//        Drawable[] drawables = getCompoundDrawables();//drawables always not null;
//        Drawable drawableLeft = drawables[0],
//                drawableTop = drawables[1],
//                drawableRight = drawables[2],
//                drawableBottom = drawables[3];
//
//        String text = getText().toString();
//        float textWidth = getPaint().measureText(text, 0, text.length());
//        double textHeight = getLineHeight() * getLineCount();
//
//        int totalDrawablePaddingH = 0;//the total horizontal padding of drawableLeft and drawableRight
//        int totalDrawablePaddingV = 0;//the total vertical padding of drawableTop and drawableBottom
//        int totalDrawableWidth = 0;//the total width of drawableLeft and drawableRight
//        int totalDrawableHeight = 0;//the total height of drawableTop and drawableBottom
//        float totalWidth;//the total width of drawableLeft , drawableRight and text
//        float totalHeight;//the total height of drawableTop , drawableBottom and text
//        int paddingH;//the horizontal padding,used both left and right
//        int paddingV;//the vertical padding,used both top and bottom
//
//        // measure width
//        if (drawableLeft != null) {
//            totalDrawableWidth += drawableLeft.getIntrinsicWidth();
//            totalDrawablePaddingH += getCompoundDrawablePadding();//drawablePadding
//        }
//        if (drawableRight != null) {
//            totalDrawableWidth += drawableRight.getIntrinsicWidth();
//            totalDrawablePaddingH += getCompoundDrawablePadding();
//        }
//        totalWidth = textWidth + totalDrawableWidth + totalDrawablePaddingH;
//        paddingH = (int) (getWidth() - totalWidth) / 2;
//
//        // measure height
//        if (drawableTop != null) {
//            totalDrawableHeight += drawableTop.getIntrinsicHeight();
//            totalDrawablePaddingV += getCompoundDrawablePadding();
//        }
//        if (drawableBottom != null) {
//            totalDrawableHeight += drawableBottom.getIntrinsicHeight();
//            totalDrawablePaddingV += getCompoundDrawablePadding();
//        }
//        totalHeight = (float) (textHeight + totalDrawableHeight + totalDrawablePaddingV);
//        paddingV = (int) (getHeight() - totalHeight) / 2;
//
//        // reset padding.
//        setPadding(paddingH, paddingV, paddingH, paddingV);//this method calls invalidate() inside;
//    }
}
