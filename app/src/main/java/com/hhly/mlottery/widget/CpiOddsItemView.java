package com.hhly.mlottery.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.frame.CPIFragment;
import com.hhly.mlottery.util.HandicapUtils;

/**
 * 指数 RecyclerView 一条中的一条赔率
 * <p>
 * Created by loshine on 2016/6/21.
 */
public class CpiOddsItemView extends LinearLayout {

    // 公司名称
    TextView mCompanyName;
    // 即赔 主队，盘口，客队
    TextView mNowLeft;
    TextView mNowCenter;
    TextView mNowRight;
    // 初赔 主队，盘口，客队
    TextView mDefaultLeft;
    TextView mDefaultCenter;
    TextView mDefaultRight;

    View mDivider;

    int green;
    int red;
    int white;
    int black;

    /**
     * 绑定数据显示
     *
     * @param data     data
     * @param oddsType 类型
     */
    public void bindData(NewOddsInfo.AllInfoBean.ComListBean data, String oddsType) {

        NewOddsInfo.AllInfoBean.ComListBean.CurrLevelBean currLevel = data.getCurrLevel();
        NewOddsInfo.AllInfoBean.ComListBean.PreLevelBean preLevel = data.getPreLevel();

        mCompanyName.setText(data.getComName());

        // 左
        int leftUp = currLevel.getLeftUp();
        if (leftUp == -1) {
            mNowLeft.setTextColor(green);
        } else if (leftUp == 1) {
            mNowLeft.setTextColor(red);
        } else {
            mNowLeft.setTextColor(black);
        }

        int middleUp = currLevel.getMiddleUp();
        if (CPIFragment.TYPE_OP.equals(oddsType)) {
            //欧赔
            if (middleUp == -1) {
                mNowCenter.setTextColor(green);
            } else if (middleUp == 1) {
                mNowCenter.setTextColor(red);
            } else {
                mNowCenter.setTextColor(black);
            }
        } else {
            // 亚盘和大小
            if (middleUp == -1) {
                mNowCenter.setTextColor(white);
                mNowCenter.setBackgroundResource(R.color.fall_color);
            } else if (middleUp == 1) {
                mNowCenter.setTextColor(white);
                mNowCenter.setBackgroundResource(R.color.analyze_left);
            } else {
                mNowCenter.setTextColor(black);
                mNowCenter.setBackgroundResource(R.color.transparency);
            }
        }

        // 右
        int rightUp = currLevel.getRightUp();
        if (rightUp == -1) {
            mNowRight.setTextColor(green);
        } else if (rightUp == 1) {
            mNowRight.setTextColor(red);
        } else {
            mNowRight.setTextColor(black);
        }

        String currLevelMiddle = currLevel.getMiddle();
        String preLevelMiddle = preLevel.getMiddle();
        // 转换盘口
        if (CPIFragment.TYPE_PLATE.equals(oddsType)) {//亚盘
            mNowCenter.setText(HandicapUtils.changeHandicap(currLevelMiddle));
            mDefaultCenter.setText(HandicapUtils.changeHandicap(preLevelMiddle));
        } else if (CPIFragment.TYPE_BIG.equals(oddsType)) {//大小
            mNowCenter.setText(HandicapUtils.changeHandicapByBigLittleBall(currLevelMiddle));
            mDefaultCenter.setText(HandicapUtils.changeHandicapByBigLittleBall(preLevelMiddle));
        } else if (CPIFragment.TYPE_OP.equals(oddsType)) {//欧赔
            //不用转换盘口
            mNowCenter.setText(currLevelMiddle);
            mDefaultCenter.setText(preLevelMiddle);
        }

        //即赔
        mNowLeft.setText(currLevel.getLeft());
        mNowRight.setText(currLevel.getRight());
        //初赔
        mDefaultLeft.setText(preLevel.getLeft());
        mDefaultRight.setText(preLevel.getRight());
    }

    public CpiOddsItemView(Context context) {
        super(context);
        init();
    }

    public CpiOddsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CpiOddsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CpiOddsItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_cpi_odds_list, this);
        mCompanyName = (TextView) view.findViewById(R.id.cpi_item_list_company_txt);

        mNowLeft = (TextView) view.findViewById(R.id.cpi_item_list_home_txt);
        mNowCenter = (TextView) view.findViewById(R.id.cpi_item_list_odds_txt);
        mNowRight = (TextView) view.findViewById(R.id.cpi_item_list_guest_txt);

        mDefaultLeft = (TextView) view.findViewById(R.id.cpi_item_list_home2_txt);
        mDefaultCenter = (TextView) view.findViewById(R.id.cpi_item_list_odds2_txt);
        mDefaultRight = (TextView) view.findViewById(R.id.cpi_item_list_guest2_txt);

        mDivider = view.findViewById(R.id.divider);

        initColor();
    }

    /**
     * 隐藏分割线
     */
    public void hideDivider() {
        mDivider.setVisibility(INVISIBLE);
    }

    /**
     * 显示分割线
     */
    public void showDivider() {
        mDivider.setVisibility(VISIBLE);
    }

    private void initColor() {
        green = ContextCompat.getColor(getContext(), R.color.fall_color);
        red = ContextCompat.getColor(getContext(), R.color.analyze_left);
        white = ContextCompat.getColor(getContext(), R.color.white);
        black = ContextCompat.getColor(getContext(), R.color.black);
    }
}
