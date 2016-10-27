package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketballDatabaseDetailsActivity;
import com.hhly.mlottery.bean.basket.infomation.LeagueBean;
import com.hhly.mlottery.util.ImageLoader;

import java.util.List;

/**
 * @author wang gang
 * @date 2016/7/15 10:48
 * @des 篮球资料库洲际赛事Adapter
 */
public class BasketInfoGridAdapter extends BaseAdapter {

    private static final String LEAGUE = "league";

    private List<LeagueBean> mList;
    private Context mContext;


    public BasketInfoGridAdapter(Context context, List<LeagueBean> list) {
        this.mContext = context;
        this.mList = list;


    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MatchViewHolder mViewHolder;


        if (convertView == null) {
            mViewHolder = new MatchViewHolder();
            convertView = View.inflate(mContext, R.layout.basket_gridview_item, null);
            mViewHolder.rl = (RelativeLayout) convertView.findViewById(R.id.rl);
            mViewHolder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            mViewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MatchViewHolder) convertView.getTag();
        }

        if ("".equals(mList.get(position).getLeagueName())) {
            mViewHolder.icon.setImageDrawable(null);
            mViewHolder.name.setText("");
            mViewHolder.rl.setEnabled(false);


        } else {
            if (mList.get(position).getLeagueLogoUrl() == null || "".equals(mList.get(position).getLeagueLogoUrl())) {
                mViewHolder.icon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.basket_info_default));
            } else {
                ImageLoader.load(mContext,mList.get(position).getLeagueLogoUrl(),R.mipmap.basket_info_default).into(mViewHolder.icon);

            }
            mViewHolder.name.setText(mList.get(position).getLeagueName());
            mViewHolder.rl.setEnabled(true);
        }

        mViewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, BasketballDatabaseDetailsActivity.class);
                intent.putExtra(LEAGUE, mList.get(position));
                mContext.startActivity(intent);
                //Toast.makeText(mContext, "当前选中的是:" + mList.get(position).getLeagueId(), Toast.LENGTH_SHORT).show();

            }
        });


        return convertView;
    }


    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private static class MatchViewHolder {

        private TextView name;
        private ImageView icon;
        private RelativeLayout rl;
    }
}
