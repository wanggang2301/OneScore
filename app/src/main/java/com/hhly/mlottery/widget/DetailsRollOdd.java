package com.hhly.mlottery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.HandicapUtils;

import java.util.List;

import data.bean.BottomOddsItem;

/**
 * @author wang gang
 * @date 2016/6/6 10:06
 * @des ${}
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


    private static final int ALET = 1;
    private static final int EUR = 2;
    private static final int ASIZE = 3;  //大小球


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


        if ("-".equals(bottomOddsItem.getLeft()) || "-".equals(bottomOddsItem.getMiddle()) || "-".equals(bottomOddsItem.getRight())) {

            live_left.setText("");
            live_middle.setText(mContext.getResources().getString(R.string.fragme_home_fengpan_text));
            live_middle.setTextColor(mContext.getResources().getColor(R.color.white));
            live_middle.setBackgroundResource(R.color.analyze_left);
            live_right.setText("");

        } else if (isNULLOrEmpty(bottomOddsItem.getLeft()) || isNULLOrEmpty(bottomOddsItem.getMiddle()) || isNULLOrEmpty(bottomOddsItem.getRight())) {
            live_left.setText("-");
            live_left.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
            live_left.setBackgroundResource(R.color.white);

            live_middle.setText("-");
            live_middle.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
            live_middle.setBackgroundResource(R.color.white);

            live_right.setText("-");
            live_right.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
            live_right.setBackgroundResource(R.color.white);
        } else {
            live_left.setText(bottomOddsItem.getLeft());

            live_middle.setText(bottomOddsItem.getMiddle());

            live_right.setText(bottomOddsItem.getRight());

            setTextViewColor(live_left, 0, bottomOddsItem.getLeftUp());
            setTextViewColor(live_middle, 1, bottomOddsItem.getMiddleUp());
            setTextViewColor(live_right, 0, bottomOddsItem.getRightUp());
        }
    }


    public void setTableLayoutData(List<BottomOddsItem> bottomOddsItem, int type) {


        //初盘
        if (isNULLOrEmpty(bottomOddsItem.get(0).getLeft()) || isNULLOrEmpty(bottomOddsItem.get(0).getMiddle()) || isNULLOrEmpty(bottomOddsItem.get(0).getRight())) {

            first_left.setText("-");
            first_left.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));

            first_middle.setText("-");
            first_middle.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));

            first_right.setText("-");
            first_right.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
        } else {

            first_left.setText(bottomOddsItem.get(0).getLeft());

            if (type == ALET) {
                first_middle.setText(HandicapUtils.changeHandicap(bottomOddsItem.get(0).getMiddle()));
                setTextViewColor(first_middle, 1, bottomOddsItem.get(0).getMiddleUp());

            } else if (type == ASIZE) {
                first_middle.setText(HandicapUtils.changeHandicapByBigLittleBall(bottomOddsItem.get(0).getMiddle()));
                setTextViewColor(first_middle, 1, bottomOddsItem.get(0).getMiddleUp());

            } else if (type == EUR) {
                first_middle.setText(bottomOddsItem.get(0).getMiddle());
                setTextViewColor(first_middle, 0, bottomOddsItem.get(0).getMiddleUp());
            }

            first_right.setText(bottomOddsItem.get(0).getRight());
            setTextViewColor(first_left, 0, bottomOddsItem.get(0).getLeftUp());
            setTextViewColor(first_right, 0, bottomOddsItem.get(0).getRightUp());
        }


        //即盘

        if ("-".equals(bottomOddsItem.get(1).getLeft()) || "-".equals(bottomOddsItem.get(1).getMiddle()) || "-".equals(bottomOddsItem.get(1).getRight())) {
            live_left.setText("");

            live_middle.setText(mContext.getResources().getString(R.string.fragme_home_fengpan_text));
            live_middle.setTextColor(mContext.getResources().getColor(R.color.white));
            live_middle.setBackgroundResource(R.color.analyze_left);

            live_right.setText("");
        } else if (isNULLOrEmpty(bottomOddsItem.get(1).getLeft()) || isNULLOrEmpty(bottomOddsItem.get(1).getMiddle()) || isNULLOrEmpty(bottomOddsItem.get(1).getRight())) {

            live_left.setText("-");
            live_left.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));

            live_middle.setText("-");
            live_middle.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));

            live_right.setText("-");
            live_right.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
        } else {
            live_left.setText(bottomOddsItem.get(1).getLeft());

            if (type == ALET) {
                live_middle.setText(HandicapUtils.changeHandicap(bottomOddsItem.get(1).getMiddle()));
                setTextViewColor(live_middle, 1, bottomOddsItem.get(1).getMiddleUp());

            } else if (type == ASIZE) {
                live_middle.setText(HandicapUtils.changeHandicapByBigLittleBall(bottomOddsItem.get(1).getMiddle()));
                setTextViewColor(live_middle, 1, bottomOddsItem.get(1).getMiddleUp());

            } else if (type == EUR) {
                live_middle.setText(bottomOddsItem.get(1).getMiddle());
                setTextViewColor(live_middle, 0, bottomOddsItem.get(1).getMiddleUp());

            }


            live_right.setText(bottomOddsItem.get(1).getRight());

            setTextViewColor(live_left, 0, bottomOddsItem.get(1).getLeftUp());
            setTextViewColor(live_right, 0, bottomOddsItem.get(1).getRightUp());
        }
    }


    private boolean isNULLOrEmpty(String s) {
        return s == null || "".equals(s);
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
