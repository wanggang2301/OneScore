package com.hhly.mlottery.adapter.basketball;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdatabase.ScheduledMatch;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */

public class BasketballDatabaseScheduleSectionAdapter
        extends BaseSectionQuickAdapter<BasketballDatabaseScheduleSectionAdapter.Section> {

    private ImageLoader mImageLoader;

    public BasketballDatabaseScheduleSectionAdapter(List<Section> data) {
        super(R.layout.item_basket_datatbase_schedule, R.layout.item_basket_datatbase_schedule_title, data);
        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    protected void convertHead(BaseViewHolder holder, Section section) {
        holder.setText(R.id.title, section.header);
    }

    @Override
    protected void convert(BaseViewHolder holder, Section section) {
        ScheduledMatch match = section.t;
        holder.setText(R.id.home_score, match.getHomeScore() + "")
                .setText(R.id.guest_score, match.getGuestScore() + "")
                .setText(R.id.home_name, match.getHomeTeamName())
                .setText(R.id.guest_name, match.getGuestTeamName());
        ImageView homeLogo = holder.getView(R.id.home_logo);
        ImageView guestLogo = holder.getView(R.id.guest_logo);
        mImageLoader.displayImage(match.getHomeTeamIconUrl(), homeLogo);
        mImageLoader.displayImage(match.getGuestTeamIconUrl(), guestLogo);
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
