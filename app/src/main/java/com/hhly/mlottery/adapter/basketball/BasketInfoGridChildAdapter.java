package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.infomation.LeagueBean;
import com.hhly.mlottery.util.ImageLoader;

import java.util.List;

/**
 * @author wang gang
 * @date 2016/7/15 17:50
 * @des 篮球资料里列表国家赛事子GridViewAdapter
 */
public class BasketInfoGridChildAdapter extends BaseAdapter {
    private List<LeagueBean> mList;
    private Context mContext;


    public BasketInfoGridChildAdapter(Context context, List<LeagueBean> list) {
        this.mContext = context;
        this.mList = list;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MatchViewHolder mViewHolder;


        if (convertView == null) {
            mViewHolder = new MatchViewHolder();
            convertView = View.inflate(mContext, R.layout.basket_gridview_item_child, null);
            mViewHolder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            mViewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MatchViewHolder) convertView.getTag();
        }

        if ("".equals(mList.get(position).getLeagueName().toString()) || mList.get(position).getLeagueName() == null) {
            mViewHolder.icon.setImageDrawable(null);
            mViewHolder.name.setText("");

        } else {

            if (mList.get(position).getLeagueLogoUrl() == null || "".equals(mList.get(position).getLeagueLogoUrl())) {
                mViewHolder.icon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.basket_info_default));
            } else {
                ImageLoader.load(mContext,mList.get(position).getLeagueLogoUrl(),R.mipmap.basket_info_default).into(mViewHolder.icon);
            }

            mViewHolder.name.setText(mList.get(position).getLeagueName().toString());
        }


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
    }
}
