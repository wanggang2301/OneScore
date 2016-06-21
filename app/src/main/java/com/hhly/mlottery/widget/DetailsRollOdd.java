package com.hhly.mlottery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.BottomOddsItem;
import com.hhly.mlottery.util.L;

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

    public void updateOdds(BottomOddsItem bottomOddsItem) {

        L.d("改变DetailsRollballFragment", "bottomOddsItem=" + bottomOddsItem.getLeftUp() + "-" + bottomOddsItem.getMiddleUp() + "-" + bottomOddsItem.getRightUp());

        if (isNULLOrEmpty(bottomOddsItem.getLeft()) || isNULLOrEmpty(bottomOddsItem.getMiddle()) || isNULLOrEmpty(bottomOddsItem.getRight())) {
            live_left.setText("");
            live_middle.setText("封");
            live_middle.setTextColor(mContext.getResources().getColor(R.color.white));
            live_middle.setBackgroundResource(R.color.analyze_left);
            live_right.setText("");
        } else {
            live_left.setText(bottomOddsItem.getLeft());

            live_middle.setText(bottomOddsItem.getMiddle());

            live_right.setText(bottomOddsItem.getRight());

            setTextViewColor(live_left, 0, bottomOddsItem.getLeftUp());
            setTextViewColor(live_middle, 1, bottomOddsItem.getMiddleUp());
            setTextViewColor(live_right, 0, bottomOddsItem.getRightUp());
        }
    }


    public void setTableLayoutData(List<BottomOddsItem> bottomOddsItem) {


        //初盘
        if (isNULLOrEmpty(bottomOddsItem.get(0).getLeft()) || isNULLOrEmpty(bottomOddsItem.get(0).getMiddle()) || "-".equals(isNULLOrEmpty(bottomOddsItem.get(0).getRight()))) {
            first_left.setText("-");
            first_left.setText("-");
            first_left.setTextColor(mContext.getResources().getColor(R.color.white));
            first_middle.setBackgroundResource(R.color.analyze_left);
            first_middle.setTextColor(mContext.getResources().getColor(R.color.white));
            first_middle.setBackgroundResource(R.color.analyze_left);
            first_right.setText("-");
            first_right.setTextColor(mContext.getResources().getColor(R.color.white));
            first_right.setBackgroundResource(R.color.analyze_left);
        } else {
            first_left.setText(bottomOddsItem.get(0).getLeft());
            first_middle.setText(bottomOddsItem.get(0).getMiddle());
            first_right.setText(bottomOddsItem.get(0).getRight());
            setTextViewColor(first_left, 0, bottomOddsItem.get(0).getLeftUp());
            setTextViewColor(first_middle, 1, bottomOddsItem.get(0).getMiddleUp());
            setTextViewColor(first_right, 0, bottomOddsItem.get(0).getRightUp());
        }


        //即盘

        if (isNULLOrEmpty(bottomOddsItem.get(1).getLeft()) || isNULLOrEmpty(bottomOddsItem.get(1).getMiddle()) || isNULLOrEmpty(bottomOddsItem.get(1).getRight())) {
            live_left.setText("");
            live_middle.setText("封");
            live_middle.setTextColor(mContext.getResources().getColor(R.color.white));
            live_middle.setBackgroundResource(R.color.analyze_left);
            live_right.setText("");
        } else {
            live_left.setText(bottomOddsItem.get(1).getLeft());

            live_middle.setText(bottomOddsItem.get(1).getMiddle());

            live_right.setText(bottomOddsItem.get(1).getRight());

            setTextViewColor(live_left, 0, bottomOddsItem.get(1).getLeftUp());
            setTextViewColor(live_middle, 1, bottomOddsItem.get(1).getMiddleUp());
            setTextViewColor(live_right, 0, bottomOddsItem.get(1).getRightUp());
        }


    }


    private boolean isNULLOrEmpty(String s) {
        if (s == null || "".equals(s)) {
            return true;
        } else {
            return false;

        }
    }

    private void setTextViewColor(TextView textView, int flag, String b) {
        textView.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
        textView.setBackgroundResource(R.color.white);
        if ("1".equals(b)) {
            if (flag == 0) {
                textView.setTextColor(mContext.getResources().getColor(R.color.odds_details));
            } else {
                textView.setTextColor(mContext.getResources().getColor(R.color.white));
                textView.setBackgroundResource(R.color.analyze_left);
            }
        } else if ("-1".equals(b)) {
            if (flag == 0) {
                textView.setTextColor(mContext.getResources().getColor(R.color.odds_left));
            } else {
                textView.setTextColor(mContext.getResources().getColor(R.color.white));
                textView.setBackgroundResource(R.color.odds_left);
            }
        } else if ("0".equals(b)) {
            textView.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
            textView.setBackgroundResource(R.color.white);
        }
    }
}
