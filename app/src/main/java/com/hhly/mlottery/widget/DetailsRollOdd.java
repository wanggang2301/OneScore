package com.hhly.mlottery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.hhly.mlottery.R;

/**
 * @author wang gang
 * @date 2016/6/6 10:06
 * @des ${TODO}
 */
public class DetailsRollOdd extends FrameLayout {

    private Context mContext;

    public DetailsRollOdd(Context context) {
        this(context, null);
    }

    public DetailsRollOdd(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public DetailsRollOdd(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }


    private void initView(){
        LayoutInflater.from(mContext).inflate(R.layout.details_roll_odd, this);
    }

}
