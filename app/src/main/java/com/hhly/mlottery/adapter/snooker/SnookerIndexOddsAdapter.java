package com.hhly.mlottery.adapter.snooker;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.enums.OddsTypeEnum;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.bean.snookerbean.snookerIndexBean.SnookerIndexBean;
import com.hhly.mlottery.frame.cpifrag.SnookerIndex.SIndexFragment;
import com.hhly.mlottery.util.HandicapUtils;

import java.util.List;

/**
 * 描    述：展示一场比赛赔率的adapter
 * 作    者：mady@13322.com
 * 时    间：2017/3/21
 */
public class SnookerIndexOddsAdapter extends BaseQuickAdapter<SnookerIndexBean.AllInfoEntity.ComListEntity> {

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
    String mType;


    public SnookerIndexOddsAdapter(List<SnookerIndexBean.AllInfoEntity.ComListEntity> data,String type) {
        super(R.layout.item_snooker_index_odds,data);

        green = ContextCompat.getColor(MyApp.getContext(), R.color.fall_color);
        red = ContextCompat.getColor(MyApp.getContext(), R.color.analyze_left);
        white = ContextCompat.getColor(MyApp.getContext(), R.color.white);
        black = ContextCompat.getColor(MyApp.getContext(), R.color.black);
        mType=type;
    }

    @Override
    protected void convert(BaseViewHolder holder, SnookerIndexBean.AllInfoEntity.ComListEntity comListEntity) {
//        holder.setText(R.id.cpi_item_list_company_txt,comListEntity.getComName());
//
//        holder.setText(R.id.cpi_item_list_home_txt,comListEntity.getCurrLevel().getLeft());
//        holder.setText(R.id.cpi_item_list_home2_txt,comListEntity.getPreLevel().getLeft());
//        holder.setText(R.id.cpi_item_list_guest_txt,comListEntity.getCurrLevel().getRight());
//        holder.setText(R.id.cpi_item_list_guest2_txt,comListEntity.getPreLevel().getRight());
         onlyWin=holder.getView(R.id.snooker_odds_only_win);

        mCompanyName=holder.getView(R.id.cpi_item_list_company_txt);
        mDefaultLeft=holder.getView(R.id.cpi_item_list_home2_txt);//初赔是home2
        mDefaultRight=holder.getView(R.id.cpi_item_list_guest2_txt);
        mDefaultCenter=holder.getView(R.id.cpi_item_list_odds2_txt);

        mNowLeft=holder.getView(R.id.cpi_item_list_home_txt);
        mNowRight=holder.getView(R.id.cpi_item_list_guest_txt);
        mNowCenter=holder.getView(R.id.cpi_item_list_odds_txt);

        bindData(comListEntity,mType);
    }

    /**
     * 绑定数据显示
     *
     * @param data     data
     * @param oddsType 类型
     */
    public void bindData(SnookerIndexBean.AllInfoEntity.ComListEntity data, String oddsType) {

        SnookerIndexBean.AllInfoEntity.ComListEntity.LevelEntity currLevel = data.getCurrLevel();
        SnookerIndexBean.AllInfoEntity.ComListEntity.LevelEntity preLevel= data.getPreLevel();

        mCompanyName.setText(data.getComName());

        // 左
        int leftUp = currLevel.getLeftStatus();
        if (leftUp == -1) {
            mNowLeft.setTextColor(green);
        } else if (leftUp == 1) {
            mNowLeft.setTextColor(red);
        } else {
            mNowLeft.setTextColor(black);
        }
        if(SIndexFragment.SINGLE_DOUBLE.equals(mType)){ //独赢没有中间
            onlyWin.setVisibility(View.GONE);
        }

        int middleUp = currLevel.getMiddleStatus();
        if (SIndexFragment.ODDS_EURO.equals(oddsType)) {
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
}
