package com.hhly.mlottery.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.enums.TennisOddsTypeEnum;
import com.hhly.mlottery.bean.snookerbean.snookerIndexBean.SnookerIndexBean;
import com.hhly.mlottery.frame.BallType;
import com.hhly.mlottery.frame.cpifrag.SnookerIndex.SIndexFragment;
import com.hhly.mlottery.util.HandicapUtils;

/**
 * 描    述：斯诺克指数数据
 * 作    者：mady@13322.com
 * 时    间：2017/3/22
 */
public class SnookerIndexItemView  extends LinearLayout{
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
    LinearLayout onlyWin;

    View mDivider;

    int green;
    int red;
    int white;
    int black;

    public SnookerIndexItemView(Context context) {
        super(context);
        init();
    }

    public SnookerIndexItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SnookerIndexItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        View view = inflate(getContext(), R.layout.item_snooker_index_odds, this);
        mCompanyName = (TextView) view.findViewById(R.id.cpi_item_list_company_txt);

        mNowLeft = (TextView) view.findViewById(R.id.cpi_item_list_home_txt);
        mNowCenter = (TextView) view.findViewById(R.id.cpi_item_list_odds_txt);
        mNowRight = (TextView) view.findViewById(R.id.cpi_item_list_guest_txt);

        mDefaultLeft = (TextView) view.findViewById(R.id.cpi_item_list_home2_txt);
        mDefaultCenter = (TextView) view.findViewById(R.id.cpi_item_list_odds2_txt);
        mDefaultRight = (TextView) view.findViewById(R.id.cpi_item_list_guest2_txt);
        onlyWin= (LinearLayout) view.findViewById(R.id.snooker_odds_only_win);

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


    /**
     * 绑定数据显示
     *
     * @param data     data
     * @param oddsType 类型
     */
    public void bindData(SnookerIndexBean.AllInfoEntity.ComListEntity data, String oddsType,int ballType) {

        SnookerIndexBean.AllInfoEntity.ComListEntity.LevelEntity currLevel = data.getCurrLevel();
        SnookerIndexBean.AllInfoEntity.ComListEntity.LevelEntity preLevel= data.getPreLevel();

        mCompanyName.setText(data.getComName());


        if(ballType== BallType.SNOOKER){
            // 左
            int leftUp = currLevel.getLeftStatus();
            if (leftUp == -1) {
                mNowLeft.setTextColor(green);
            } else if (leftUp == 1) {
                mNowLeft.setTextColor(red);
            } else {
                mNowLeft.setTextColor(black);
            }
            if(SIndexFragment.SINGLE_DOUBLE.equals(oddsType)||SIndexFragment.ODDS_EURO.equals(oddsType)){ //独赢没有中间
                onlyWin.setVisibility(View.GONE);
            }

            int middleUp = currLevel.getMiddleStatus();
            if (SIndexFragment.ODDS_EURO.equals(oddsType)||SIndexFragment.SINGLE_DOUBLE.equals(oddsType)) { //斯诺克欧赔单双或者网球欧赔
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
            int rightUp = currLevel.getRightStatus();
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
            if (SIndexFragment.ODDS_LET.equals(oddsType)) {//亚盘
                mNowCenter.setText(HandicapUtils.changeHandicap(currLevelMiddle));
                mDefaultCenter.setText(HandicapUtils.changeHandicap(preLevelMiddle));
            } else if (SIndexFragment.ODDS_SIZE.equals(oddsType)) {//大小
                mNowCenter.setText(HandicapUtils.changeHandicapByBigLittleBall(currLevelMiddle));
                mDefaultCenter.setText(HandicapUtils.changeHandicapByBigLittleBall(preLevelMiddle));
            } else if (SIndexFragment.ODDS_EURO.equals(oddsType)) {//欧赔
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
        else { //网球
            // 左
            int leftUp = currLevel.getLeftStatus();
            if (leftUp == -1) {
                mNowLeft.setTextColor(green);
            } else if (leftUp == 1) {
                mNowLeft.setTextColor(red);
            } else {
                mNowLeft.setTextColor(black);
            }

            if(TennisOddsTypeEnum.EURO.equals(oddsType)){ //网球欧赔
                onlyWin.setVisibility(View.GONE);
            }

            int middleUp = currLevel.getMiddleStatus();
            if (TennisOddsTypeEnum.EURO.equals(oddsType)) { //斯诺克欧赔单双或者网球欧赔
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
            int rightUp = currLevel.getRightStatus();
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
            if (TennisOddsTypeEnum.ASIALET.equals(oddsType)) {//亚盘
                mNowCenter.setText(HandicapUtils.changeHandicap(currLevelMiddle));
                mDefaultCenter.setText(HandicapUtils.changeHandicap(preLevelMiddle));
            } else if (TennisOddsTypeEnum.ASIASIZE.equals(oddsType)) {//大小
                mNowCenter.setText(HandicapUtils.changeHandicapByBigLittleBall(currLevelMiddle));
                mDefaultCenter.setText(HandicapUtils.changeHandicapByBigLittleBall(preLevelMiddle));
            } else if (TennisOddsTypeEnum.EURO.equals(oddsType)) {//欧赔
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

    }
}
