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
import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.bean.enums.BasketOddsTypeEnum;
import com.hhly.mlottery.util.HandicapUtils;

/**
 * @author: Wangg
 * @Name：IndexOddsItemView
 * @Description:
 * @Created on:2017/3/20  21:27.
 */

public class IndexOddsItemView extends LinearLayout {
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

    LinearLayout ll_middle;

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
    public void bindData(BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean data, String oddsType) {


        mCompanyName.setText(data.getComName());

        BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean.OddsDataBean currLevel = null;
        //初始赔率
        BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean.OddsDataBean preLevel = null;
        if (data.getOddsData().size() == 2) {
            currLevel = data.getOddsData().get(0);
            //初始赔率
            preLevel = data.getOddsData().get(1);
        } else if (data.getOddsData().size() == 1) {
            currLevel = data.getOddsData().get(0);
            preLevel = new BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean.OddsDataBean();
        } else {
            currLevel = new BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean.OddsDataBean();
            preLevel = new BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean.OddsDataBean();
        }

        // 左
        int leftUp = currLevel.getLeftOddsTrend();
        if (leftUp == -1) {
            mNowLeft.setTextColor(green);
        } else if (leftUp == 1) {
            mNowLeft.setTextColor(red);
        } else {
            mNowLeft.setTextColor(black);
        }

        int middleUp = currLevel.getHandicapValueTrend();
        if (BasketOddsTypeEnum.EURO.equals(oddsType)) {
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
        int rightUp = currLevel.getRightOddsTrend();
        if (rightUp == -1) {
            mNowRight.setTextColor(green);
        } else if (rightUp == 1) {
            mNowRight.setTextColor(red);
        } else {
            mNowRight.setTextColor(black);
        }

        String currLevelMiddle = currLevel.getHandicapValue();


        String preLevelMiddle = preLevel.getHandicapValue();
        // 转换盘口
        if (BasketOddsTypeEnum.ASIALET.equals(oddsType)) {//亚盘
            ll_middle.setVisibility(View.VISIBLE);

            mNowCenter.setText(HandicapUtils.changeHandicap(currLevelMiddle));
            mDefaultCenter.setText(HandicapUtils.changeHandicap(preLevelMiddle));
        } else if (BasketOddsTypeEnum.ASIASIZE.equals(oddsType)) {//大小
            ll_middle.setVisibility(View.VISIBLE);

            mNowCenter.setText(HandicapUtils.changeHandicapByBigLittleBall(currLevelMiddle));
            mDefaultCenter.setText(HandicapUtils.changeHandicapByBigLittleBall(preLevelMiddle));
        } else if (BasketOddsTypeEnum.EURO.equals(oddsType)) {//欧赔
            //不用转换盘口

            ll_middle.setVisibility(View.GONE);
            mNowCenter.setText(currLevelMiddle);
            mDefaultCenter.setText(preLevelMiddle);
        }

        //即赔
        mNowLeft.setText(currLevel.getLeftOdds());
        mNowRight.setText(currLevel.getRightOdds());
        //初赔
        mDefaultLeft.setText(preLevel.getLeftOdds());
        mDefaultRight.setText(preLevel.getRightOdds());
    }

    public IndexOddsItemView(Context context) {
        super(context);
        init();
    }

    public IndexOddsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndexOddsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IndexOddsItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

        ll_middle = (LinearLayout) view.findViewById(R.id.ll_middle);


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
