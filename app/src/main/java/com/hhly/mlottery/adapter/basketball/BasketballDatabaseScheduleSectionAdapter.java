package com.hhly.mlottery.adapter.basketball;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdatabase.ScheduledMatch;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */

public class BasketballDatabaseScheduleSectionAdapter
        extends BaseSectionQuickAdapter<BasketballDatabaseScheduleSectionAdapter.Section> {

    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    public BasketballDatabaseScheduleSectionAdapter(List<Section> data) {
        super(R.layout.item_basket_datatbase_schedule, R.layout.item_basket_datatbase_schedule_title, data);
        mImageLoader = ImageLoader.getInstance();
        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageOnLoading(R.mipmap.basket_default)   //默认图片
                .showImageForEmptyUri(R.mipmap.basket_default)    //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.mipmap.basket_default)// 加载失败显示的图片
                .resetViewBeforeLoading(true)
                .build();
    }

    @Override
    protected void convertHead(BaseViewHolder holder, Section section) {
        holder.setText(R.id.title, section.header);
    }

    @Override
    protected void convert(BaseViewHolder holder, Section section) {
        ScheduledMatch match = section.t;
        holder.setText(R.id.home_name, match.getHomeTeamName())
                .setText(R.id.guest_name, match.getGuestTeamName());
        boolean notStart = match.getHomeScore() == 0 && match.getGuestScore() == 0;
        if (notStart) {
            holder.setText(R.id.time, match.getTime());
        } else {
            holder.setText(R.id.home_score, match.getHomeScore() + "")
                    .setText(R.id.guest_score, match.getGuestScore() + "");
        }
        View middleLine = holder.getView(R.id.middle_line);
        View homeScore = holder.getView(R.id.home_score);
        View guestScore = holder.getView(R.id.guest_score);
        View time = holder.getView(R.id.time);

        middleLine.setVisibility(notStart ? View.INVISIBLE : View.VISIBLE);
        homeScore.setVisibility(notStart ? View.INVISIBLE : View.VISIBLE);
        guestScore.setVisibility(notStart ? View.INVISIBLE : View.VISIBLE);

        time.setVisibility(notStart ? View.VISIBLE : View.INVISIBLE);

        ImageView homeLogo = holder.getView(R.id.home_logo);
        ImageView guestLogo = holder.getView(R.id.guest_logo);
        mImageLoader.displayImage(match.getHomeTeamIconUrl(), homeLogo, mOptions);
        mImageLoader.displayImage(match.getGuestTeamIconUrl(), guestLogo, mOptions);
    }

    public static class Section extends SectionEntity<ScheduledMatch> {

        public Section(boolean isHeader, String header) {
            super(isHeader, header);
        }

        public Section(ScheduledMatch match) {
            super(match);
        }
    }
}
