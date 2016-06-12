package com.hhly.mlottery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.BottomOddsItem;

import java.util.List;

/**
 * @author wang gang
 * @date 2016/6/6 10:06
 * @des ${TODO}
 */
public class DetailsRollOdd extends FrameLayout {
    private Context mContext;
    private TextView tv_title;
    private TextView live_left;
    private TextView live_middle;
    private TextView live_right;
    private TextView first_left;
    private TextView first_middle;
    private TextView first_right;


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


    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.details_roll_odd, this);
        tv_title = (TextView) findViewById(R.id.title);
        live_left = (TextView) findViewById(R.id.live_left);
        live_middle = (TextView) findViewById(R.id.live_middle);
        live_right = (TextView) findViewById(R.id.live_right);
        first_left = (TextView) findViewById(R.id.first_left);
        first_middle = (TextView) findViewById(R.id.first_middle);
        first_right = (TextView) findViewById(R.id.first_right);
    }


    public void setTitle(String title) {
        tv_title.setText(title);
    }


    public void setTableLayoutData(List<BottomOddsItem> bottomOddsItem) {
        live_left.setText(bottomOddsItem.get(1).getHomeOdd());
        live_middle.setText(bottomOddsItem.get(1).getHand());
        live_right.setText(bottomOddsItem.get(1).getGuestOdd());
        first_left.setText(bottomOddsItem.get(0).getHomeOdd());
        first_middle.setText(bottomOddsItem.get(0).getHand());
        first_right.setText(bottomOddsItem.get(0).getGuestOdd());

    }
}
