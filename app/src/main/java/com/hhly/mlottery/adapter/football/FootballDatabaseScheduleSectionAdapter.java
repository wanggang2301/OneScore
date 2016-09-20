package com.hhly.mlottery.adapter.football;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.ScheduleDatasBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * description:
 * author: yixq
 * Created by A on 2016/9/7.
 */

public class FootballDatabaseScheduleSectionAdapter
        extends BaseSectionQuickAdapter<FootballDatabaseScheduleSectionAdapter.Section> {

    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    public FootballDatabaseScheduleSectionAdapter(List<Section> data) {
        super(R.layout.item_basket_datatbase_schedule, R.layout.item_basket_datatbase_schedule_title, data);
        mImageLoader = ImageLoader.getInstance();
        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageOnLoading(R.mipmap.score_default)   //默认图片
                .showImageForEmptyUri(R.mipmap.score_default)    //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.mipmap.score_default)// 加载失败显示的图片
                .resetViewBeforeLoading(true)
                .build();
    }

    @Override
    protected void convertHead(BaseViewHolder holder, Section section) {
        holder.setText(R.id.title, section.header);
    }

    @Override
    protected void convert(BaseViewHolder holder, Section section) {
        ScheduleDatasBean match = section.t;
        holder.setText(R.id.home_name, match.getHomeName())
                .setText(R.id.guest_name, match.getGuestName());
//        boolean notStart = match.getHomeScore() == 0 && match.getGuestScore() == 0;
//        View middleLine = holder.getView(R.id.middle_line);
//        View homeScore = holder.getView(R.id.home_score);
//        View guestScore = holder.getView(R.id.guest_score);
//        View time = holder.getView(R.id.time);
//
//        middleLine.setVisibility(notStart ? View.INVISIBLE : View.VISIBLE);
//        homeScore.setVisibility(notStart ? View.INVISIBLE : View.VISIBLE);
//        guestScore.setVisibility(notStart ? View.INVISIBLE : View.VISIBLE);
//
//        if (notStart) {
//            holder.setText(R.id.time, match.getTime());
//        } else {
//            holder.setText(R.id.home_score, match.getHomeScore() + "")
//                    .setText(R.id.guest_score, match.getGuestScore() + "");
//        }

        /**
         * 比赛状态 0:未开, 1:上半场, 2:中场, 3:下半场, 4:加时, 5:点球, -1:完场, -10:取消, -11:待定, -12:腰斩,
         * -13:中断, -14:推迟
         */
        switch (match.getMatchState()){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case -1:
                holder.setText(R.id.time , match.getMatchResult());
                break;
            case -10:
//                holder.setText(R.id.time , "取消");
                holder.setText(R.id.time , mContext.getString(R.string.fragme_home_quxiao_text));
                break;
            case -11:
                holder.setText(R.id.time , mContext.getString(R.string.fragme_home_daiding_text));
                break;
            case -12:
                holder.setText(R.id.time , mContext.getString(R.string.fragme_home_yaozhan_text));
                break;
            case -13:
                holder.setText(R.id.time , mContext.getString(R.string.fragme_home_zhongduan_text));
                break;
            case -14:
                holder.setText(R.id.time , mContext.getString(R.string.fragme_home_tuichi_text));
                break;

        }
//        holder.setText(R.id.time , match.getMatchResult());
        holder.setVisible(R.id.time , true);
        holder.setText(R.id.middle_line , "");

        ImageView homeLogo = holder.getView(R.id.home_logo);
        ImageView guestLogo = holder.getView(R.id.guest_logo);
        mImageLoader.displayImage(match.getHomePic(), homeLogo, mOptions);
        mImageLoader.displayImage(match.getGuestPic(), guestLogo, mOptions);
    }

    public static class Section extends SectionEntity<ScheduleDatasBean> {

        public Section(boolean isHeader, String header) {
            super(isHeader, header);
        }

        public Section(ScheduleDatasBean match) {
            super(match);
        }
    }
}
