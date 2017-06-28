package com.hhly.mlottery.adapter.basketball;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdatabase.ScheduledMatch;
import com.hhly.mlottery.frame.basketballframe.BasketDatabaseScheduleFragment;
import com.hhly.mlottery.util.ImageLoader;

import java.util.List;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */

public class BasketballDatabaseScheduleSectionAdapter
        extends BaseSectionQuickAdapter<BasketballDatabaseScheduleSectionAdapter.Section> {

    private BasketDatabaseScheduleFragment.BasketballDetailsClickListener basketballDetailsClickListener;
    public void setBasketballDetailsClickListener(BasketDatabaseScheduleFragment.BasketballDetailsClickListener basketballDetailsClickListener){
        this.basketballDetailsClickListener = basketballDetailsClickListener;
    }

    public BasketballDatabaseScheduleSectionAdapter(List<Section> data) {
        super(R.layout.item_basket_datatbase_schedule, R.layout.item_basket_datatbase_schedule_title, data);

    }

    @Override
    protected void convertHead(BaseViewHolder holder, Section section) {
        holder.setText(R.id.title, section.header);
    }

    @Override
    protected void convert(BaseViewHolder holder, Section section) {
        final ScheduledMatch match = section.t;
        holder.setText(R.id.home_name, match.getHomeTeamName())
                .setText(R.id.guest_name, match.getGuestTeamName());
        boolean notStart = match.getHomeScore() == 0 && match.getGuestScore() == 0;
        View middleLine = holder.getView(R.id.middle_line);
        View homeScore = holder.getView(R.id.home_score);
        View guestScore = holder.getView(R.id.guest_score);
        View time = holder.getView(R.id.time);

        middleLine.setVisibility(notStart ? View.INVISIBLE : View.VISIBLE);
        homeScore.setVisibility(notStart ? View.INVISIBLE : View.VISIBLE);
        guestScore.setVisibility(notStart ? View.INVISIBLE : View.VISIBLE);

        if (notStart) {
            holder.setText(R.id.time, match.getTime());
        } else {
            holder.setText(R.id.home_score, match.getHomeScore() + "")
                    .setText(R.id.guest_score, match.getGuestScore() + "");
        }

        time.setVisibility(notStart ? View.VISIBLE : View.INVISIBLE);

        ImageView homeLogo = holder.getView(R.id.home_logo);
        ImageView guestLogo = holder.getView(R.id.guest_logo);
        ImageLoader.load(mContext,match.getHomeTeamIconUrl(),R.mipmap.basket_default).into(homeLogo);
        ImageLoader.load(mContext,match.getGuestTeamIconUrl(),R.mipmap.basket_default).into(guestLogo);

        holder.setOnClickListener(R.id.item_id, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (basketballDetailsClickListener != null){
                    basketballDetailsClickListener.DetailsOnClick(v , match.thirdId);
                }
            }
        });
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
