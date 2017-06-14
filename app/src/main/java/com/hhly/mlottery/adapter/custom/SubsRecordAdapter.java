package com.hhly.mlottery.adapter.custom;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.RecommendedExpertDetailsActivity;
import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.MvpBettingPayDetailsActivity;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.view.CircleImageView;

import java.util.List;

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
        super(R.layout.subs_record_item, data);
        mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final SubsRecordBean.PurchaseRecordsBean.ListBean b) {


   /*     // Uri uri = new Uri.Builder().build();
        Glide.with(mContext).load(b.getHeadImg()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                baseViewHolder.setImageBitmap(R.id.betting_portrait_img, resource);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                baseViewHolder.setImageResource(R.id.betting_portrait_img, R.mipmap.specialist_default);
                //history_item_pic.setVisibility(View.GONE);
            }
        }); //方法中设置asBitmap可以设置回调类型
*/


        L.d("headimg", b.getHeadImg());
        Glide.with(mContext).load(b.getHeadImg()).placeholder(R.mipmap.specialist_default).into((CircleImageView) baseViewHolder.getView(R.id.betting_portrait_img));
        baseViewHolder.setText(R.id.betting_specialist_name, b.getNickName());
        baseViewHolder.setText(R.id.betting_specialist_grade, getSpecialistGrade(b.getLevels()));
        baseViewHolder.setVisible(R.id.betting_lainzhong, false);

    /*    if (b.getWinpointThreeDays() == 0) {
            baseViewHolder.setVisible(R.id.betting_lately_accuracy, false);
        } else {*/
        // baseViewHolder.setVisible(R.id.betting_lately_accuracy, true);
        baseViewHolder.setText(R.id.betting_lately_accuracy, getLastAccuracy(b.getWinpointThreeDays(), b.getErrpointThreeDays()));
        // }

        baseViewHolder.setText(R.id.betting_league_name, b.getLeagueName());
        baseViewHolder.setVisible(R.id.betting_round, false);
        baseViewHolder.setText(R.id.betting_date, b.getMatchDate());
        baseViewHolder.setVisible(R.id.betting_week, true);
        baseViewHolder.setText(R.id.betting_week, b.getMatchTime());
     /*   if (0 == b.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, mContext.getResources().getString(R.string.jingcaidanguan_txt));
        } else if (1 == b.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, mContext.getResources().getString(R.string.yapan_txt));
        } else if (2 == b.getType()) {
            baseViewHolder.setText(R.id.betting_concede_points_spf, mContext.getResources().getString(R.string.daxiaoqiu_txt));
        }*/

        baseViewHolder.setText(R.id.betting_concede_points_spf, b.getTypeStr());


        baseViewHolder.setText(R.id.betting_home_name, b.getHomeName());
        baseViewHolder.setText(R.id.betting_guest_name, b.getGuestName());
        baseViewHolder.setText(R.id.betting_price, String.valueOf("￥ " + b.getPrice() + ".00"));
        baseViewHolder.setText(R.id.betting_buy_num, String.valueOf(b.getCount()) + mContext.getResources().getString(R.string.yigoumai_txt));
        baseViewHolder.setText(R.id.textView11, mContext.getResources().getString(R.string.chakan_txt));
        baseViewHolder.setText(R.id.betting_recommended_reason, mContext.getResources().getString(R.string.tuijianliyou_txt) + (TextUtils.isEmpty(b.getContext()) ? "" : b.getContext()));


        baseViewHolder.getView(R.id.rl_specialist_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(mContext, mContext.getResources().getString(R.string.developing), Toast.LENGTH_SHORT).show();
                //专家详情
                Intent intent = new Intent(mContext, RecommendedExpertDetailsActivity.class);
                intent.putExtra("expertId", b.getUserId());
                intent.putExtra("winPoint", b.getWinpointThreeDays() + "");
                intent.putExtra("errPoint", b.getErrpointThreeDays() + "");


                mContext.startActivity(intent);
            }
        });

        baseViewHolder.getView(R.id.textView11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //跳转到记录详情

                Intent intent = new Intent(mContext, MvpBettingPayDetailsActivity.class);
                intent.putExtra(ConstantPool.TO_DETAILS_PROMOTION_ID, b.getId());
                mContext.startActivity(intent);
            }
        });
    }


    private String getSpecialistGrade(String level) {
        if (TextUtils.isEmpty(level)) {
            return mContext.getResources().getString(R.string.baiyin_txt);
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


    /*int nums[] = {3, 5, 10};

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
    }*/
}
