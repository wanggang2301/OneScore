package com.hhly.mlottery.adapter.custom;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.mvptask.data.model.SubsRecordBean;
import com.hhly.mlottery.view.CircleImageView;

import java.util.List;
import java.util.Random;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/6/2  10:59.
 */

public class SubsRecordAdapter extends BaseQuickAdapter<SubsRecordBean.PurchaseRecordsBean.ListBean> {

    private Context mContext;

    public SubsRecordAdapter(Context context, List<SubsRecordBean.PurchaseRecordsBean.ListBean> data) {
        super(R.layout.betting_recommend_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SubsRecordBean.PurchaseRecordsBean.ListBean b) {
        Glide.with(mContext).load(b.getHeadImg()).into((CircleImageView) baseViewHolder.getView(R.id.betting_portrait_img));

        baseViewHolder.setText(R.id.betting_specialist_name, b.getNickName());


        baseViewHolder.setText(R.id.betting_specialist_grade, getSpecialistGrade(b.getLevels()));

        baseViewHolder.setVisible(R.id.betting_lainzhong, false);

        if (b.getWinpointThreeDays() == 0) {
            baseViewHolder.setVisible(R.id.betting_lately_accuracy, false);
        } else {
            baseViewHolder.setVisible(R.id.betting_lately_accuracy, true);

            baseViewHolder.setText(R.id.betting_lately_accuracy, getLastAccuracy(b.getWinpointThreeDays(), b.getErrpointThreeDays()));

        }


        baseViewHolder.setText(R.id.betting_league_name, b.getLeagueName());
        baseViewHolder.setVisible(R.id.betting_round, false);
        baseViewHolder.setText(R.id.betting_date, b.getMatchDate());
        baseViewHolder.setText(R.id.betting_week, b.getMatchTime());
        if (0 == b.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, "竞彩单关");
        } else if (1 == b.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, "亚盘");
        } else if (2 == b.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, "大小球");
        }


        baseViewHolder.setText(R.id.betting_home_name, b.getHomeName());
        baseViewHolder.setText(R.id.betting_guest_name, b.getGuestName());
        baseViewHolder.setText(R.id.betting_price, String.valueOf("￥ " + b.getPrice() + ".00"));
        baseViewHolder.setText(R.id.betting_buy_num, String.valueOf(getBuyNum(b.getCount())) + "人已购买");
        baseViewHolder.setText(R.id.textView11, "查看");
        baseViewHolder.setText(R.id.betting_recommended_reason, "推荐理由:" + (TextUtils.isEmpty(b.getContext()) ? "" : b.getContext()));

    }


    private String getSpecialistGrade(String level) {
        if (TextUtils.isEmpty(level)) {
            return "白银专家";
        }

        String levelGrade = "白银专家";

        switch (level) {
            case "1":
                levelGrade = "白银专家";
                break;
            case "2":
                levelGrade = "黄金专家";

                break;
            case "3":
                levelGrade = "白金专家";

                break;
            case "4":
                levelGrade = "钻石专家";

                break;
        }

        return levelGrade;

    }


    private String getLastAccuracy(int win, int err) {
        return "近" + (win + err) + "中" + win;
    }


    int nums[] = {3, 5, 10};

    private int getBuyNum(String bugNum) {
        if (TextUtils.isEmpty(bugNum)) {
            return nums[new Random().nextInt(3)];
        }

        if (Integer.parseInt(bugNum) == 0) {
            return nums[new Random().nextInt(3)];
        } else if (Integer.parseInt(bugNum) < 10 && Integer.parseInt(bugNum) > 0) {
            return Integer.parseInt(bugNum) * 10;
        } else {
            return Integer.parseInt(bugNum);
        }
    }


}
