package com.hhly.mlottery.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.util.HandicapUtils;

/**
 * 指数列表 RecyclerView 内一条赔率的 View
 * <p/>
 * Created by Administrator on 2016/6/14.
 * Email: longs@13322.com
 */
public class CpiOddsItemView extends LinearLayout {

    public static final int ASIA = 1; // 亚盘
    public static final int EURO = 2; // 欧赔
    public static final int BIG_SMALL = 3; // 大小球

    // 公司名称
    TextView cpi_item_list_company_txt;
    // 即赔 主队，盘口，客队
    TextView cpi_item_list_home_txt;
    TextView cpi_item_list_odds_txt;
    TextView cpi_item_list_guest_txt;
    // 初赔 主队，盘口，客队
    TextView cpi_item_list_home2_txt;
    TextView cpi_item_list_odds2_txt;
    TextView cpi_item_list_guest2_txt;
    // 分割线
    View divider;

    private int oddType;

    public void setType(int type) {
        this.oddType = type;
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

        cpi_item_list_company_txt = (TextView) view.findViewById(R.id.cpi_item_list_company_txt);

        cpi_item_list_home_txt = (TextView) view.findViewById(R.id.cpi_item_list_home_txt);
        cpi_item_list_odds_txt = (TextView) view.findViewById(R.id.cpi_item_list_odds_txt);
        cpi_item_list_guest_txt = (TextView) view.findViewById(R.id.cpi_item_list_guest_txt);

        cpi_item_list_home2_txt = (TextView) view.findViewById(R.id.cpi_item_list_home2_txt);
        cpi_item_list_odds2_txt = (TextView) view.findViewById(R.id.cpi_item_list_odds2_txt);
        cpi_item_list_guest2_txt = (TextView) view.findViewById(R.id.cpi_item_list_guest2_txt);

        divider = view.findViewById(R.id.divider);
    }

    /**
     * 绑定数据，在此之前必须使用 setType(int type) 设置类型
     *
     * @param data
     */
    public void bindData(@NonNull NewOddsInfo.AllInfoBean.ComListBean data) {
        NewOddsInfo.AllInfoBean.ComListBean.CurrLevelBean currLevel = data.getCurrLevel();
        NewOddsInfo.AllInfoBean.ComListBean.PreLevelBean preLevel = data.getPreLevel();

        if (currLevel != null && preLevel != null) {
            // 设置公司名称
            cpi_item_list_company_txt.setText(data.getComName());

            // 左边颜色
            int leftUp = currLevel.getLeftUp();
            if (leftUp == -1) {
                cpi_item_list_home_txt.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.fall_color));
            } else if (leftUp == 1) {
                cpi_item_list_home_txt.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.analyze_left));
            } else {
                cpi_item_list_home_txt.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.black));
            }

            // 盘口颜色
            if ("green".equals(currLevel.getCurrTextBgColor())) {
                cpi_item_list_odds_txt.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.white));
                cpi_item_list_odds_txt.setBackgroundResource(R.color.fall_color);
            } else if ("red".equals(currLevel.getCurrTextBgColor())) {
                cpi_item_list_odds_txt.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.white));
                cpi_item_list_odds_txt.setBackgroundResource(R.color.analyze_left);
            } else if ("black".equals(currLevel.getCurrTextBgColor())) {
                cpi_item_list_odds_txt.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.black));
                cpi_item_list_odds_txt.setBackgroundResource(R.color.transparency);
            } else {
                // 没设置背景颜色（即欧赔）
                if (currLevel.getMiddleUp() == -1) {
                    cpi_item_list_odds_txt.setTextColor(
                            ContextCompat.getColor(getContext(), R.color.fall_color));
                } else if (currLevel.getMiddleUp() == 1) {
                    cpi_item_list_odds_txt.setTextColor(
                            ContextCompat.getColor(getContext(), R.color.analyze_left));
                } else {
                    cpi_item_list_odds_txt.setTextColor(
                            ContextCompat.getColor(getContext(), R.color.black));
                }
            }

            // 右边颜色
            if (currLevel.getRightUp() == -1) {
                cpi_item_list_guest_txt.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.fall_color));
            } else if (currLevel.getRightUp() == 1) {
                cpi_item_list_guest_txt.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.analyze_left));
            } else {
                cpi_item_list_guest_txt.setTextColor(
                        ContextCompat.getColor(getContext(), R.color.black));
            }

            if (ASIA == oddType) {
                // 亚盘转换盘口
                cpi_item_list_odds_txt.setText(HandicapUtils.changeHandicap(currLevel.getMiddle()));
                cpi_item_list_odds2_txt.setText(HandicapUtils.changeHandicap(preLevel.getMiddle()));
                cpi_item_list_odds_txt.setWidth(200);
            } else if (BIG_SMALL == oddType) {
                // 大小球
                cpi_item_list_odds_txt.setText(
                        HandicapUtils.changeHandicapByBigLittleBall(currLevel.getMiddle()));
                cpi_item_list_odds2_txt.setText(
                        HandicapUtils.changeHandicapByBigLittleBall(preLevel.getMiddle()));
                cpi_item_list_odds_txt.setWidth(120);
            } else if (EURO == oddType) {
                // 欧赔不用转换盘口
                cpi_item_list_odds_txt.setText(currLevel.getMiddle());
                cpi_item_list_odds2_txt.setText(preLevel.getMiddle());
            }

            //即赔
            cpi_item_list_home_txt.setText(currLevel.getLeft());
            cpi_item_list_guest_txt.setText(currLevel.getRight());
            //初赔
            cpi_item_list_home2_txt.setText(preLevel.getLeft());
            cpi_item_list_guest2_txt.setText(preLevel.getRight());
        }
    }

    /**
     * 隐藏分割线
     */
    public void hideDivider() {
        divider.setVisibility(GONE);
    }
}
