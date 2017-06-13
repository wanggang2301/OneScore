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


    @Override
    protected void onDraw(Canvas canvas) {

        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[0];
            if (drawableLeft != null && !TextUtils.isEmpty(getHint())) {
                Rect r = drawableLeft.getBounds();
                Rect rect = new Rect();
                getPaint().getTextBounds(getHint().toString(), 0, 1, rect);/*Hint.length>=1*/
//                int ds = r.bottom - rect.bottom;
                int ds = r.top - rect.top;
                setGravity(Gravity.TOP);
                canvas.translate(0, ds/2);
            }
        }
        super.onDraw(canvas);
    }
}
