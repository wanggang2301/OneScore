package com.hhly.mlottery.adapter.custom;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.view.CircleImageView;

import java.util.List;
import java.util.Random;

import data.bean.SubsRecordBean;

/**
 * @author: Wangg
 * @name：xxx
 * @description: 订阅记录Adapter
 * @created on:2017/6/2  10:59.
 */

public class SubsRecordAdapter extends BaseQuickAdapter<SubsRecordBean.PurchaseRecordsBean.ListBean> {

    private Context mContext;

    public SubsRecordAdapter(Context context, List<SubsRecordBean.PurchaseRecordsBean.ListBean> data) {
        super(R.layout.betting_recommend_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final SubsRecordBean.PurchaseRecordsBean.ListBean b) {
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
            baseViewHolder.setText(R.id.betting_concede_points_spf, mContext.getResources().getString(R.string.jingcaidanguan_txt));
        } else if (1 == b.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, mContext.getResources().getString(R.string.yapan_txt));
        } else if (2 == b.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, mContext.getResources().getString(R.string.daxiaoqiu_txt));
        }

        baseViewHolder.setText(R.id.betting_home_name, b.getHomeName());
        baseViewHolder.setText(R.id.betting_guest_name, b.getGuestName());
        baseViewHolder.setText(R.id.betting_price, String.valueOf("￥ " + b.getPrice() + ".00"));
        baseViewHolder.setText(R.id.betting_buy_num, String.valueOf(getBuyNum(b.getCount())) + mContext.getResources().getString(R.string.yigoumai_txt));
        baseViewHolder.setText(R.id.textView11, mContext.getResources().getString(R.string.chakan_txt));
        baseViewHolder.setText(R.id.betting_recommended_reason, mContext.getResources().getString(R.string.tuijianliyou_txt) + (TextUtils.isEmpty(b.getContext()) ? "" : b.getContext()));

        baseViewHolder.getView(R.id.textView11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //跳转到记录详情

                Toast.makeText(mContext, "敬請期待", Toast.LENGTH_SHORT).show();

                //   BettingListDataBean.PromotionData.BettingListData bettingListData = new BettingListDataBean.PromotionData.BettingListData();

/*
                String imgUrl = itemData.getPhotoUrl();
                ImageLoader.load(mContext,imgUrl,R.mipmap.football_analyze_default).into(portraitImg);
                detailsHomeName.setText(filtraNull(itemData.getHomeName()));
                detailsGuestName.setText(filtraNull(itemData.getGuestName()));
                detailsWeek.setText(filtraNull(itemData.getSerNum()));
                detailsLuague.setText(filtraNull(itemData.getLeagueName()));
                detailsDate.setText(filtraNull(itemData.getReleaseDate()));
                */
            /*
                bettingListData.setId(b.getId());
                bettingListData.setHomeName(b.getHomeName());
                bettingListData.setGuestName(b.getGuestName());
                bettingListData.setPrice(String.valueOf(b.getPrice()));

                bettingListData.setPhotoUrl(b.getHeadImg());
                bettingListData.setLeagueName(b.getLeagueName());




                Intent intent = new Intent(mContext, MvpBettingPayDetailsActivity.class);*/


                //MvpBettingPayDetailsActivity
            }
        });


    }


    private String getSpecialistGrade(String level) {
        if (TextUtils.isEmpty(level)) {
            return "白银专家";
        }

        String levelGrade = mContext.getResources().getString(R.string.baiyin_txt);

        switch (level) {
            case "1":
                levelGrade = mContext.getResources().getString(R.string.baiyin_txt);
                break;
            case "2":
                levelGrade = mContext.getResources().getString(R.string.huangjin_txt);

                break;
            case "3":
                levelGrade = mContext.getResources().getString(R.string.baijin_txt);

                break;
            case "4":
                levelGrade = mContext.getResources().getString(R.string.zuanshi_txt);

                break;
        }
        return levelGrade;
    }


    private String getLastAccuracy(int win, int err) {
        return mContext.getResources().getString(R.string.zhuduijin) + (win + err) + mContext.getResources().getString(R.string.zhong_txt) + win;
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
