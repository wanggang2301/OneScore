package com.hhly.mlottery.adapter.homePagerAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.homepagerentity.HomeContentEntity;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.PreferenceUtil;

import java.util.List;

/**
 * 种类总入口GridView数据适配器
 * Created by hhly107 on 2016/3/30.
 */
public class HomeGridAdapter extends BaseAdapter {

    private Context mContext;
    private HomeListBaseAdapter.ViewHolder mTopHolder;// 头部ViewHolder

    private ViewHolder mViewHolder;// ViewHolder
    List<HomeContentEntity> mData;


    public HomeGridAdapter(Context context, List<HomeContentEntity> list) {
        this.mContext = context;
        this.mData = list;

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.home_page_item_menu_icon, null);
            mViewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_menu_icon_home);
            mViewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_menu_name_home);
            mViewHolder.red_not = convertView.findViewById(R.id.home_menu_red_dot);
            convertView.setTag(mViewHolder);
        }
        mViewHolder = (ViewHolder) convertView.getTag();
        HomeContentEntity mContentEntity = (HomeContentEntity) getItem(position);

        if (mContentEntity.getPicUrl() == null) {
            mViewHolder.iv_icon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.home_menu_icon_def));
        } else {
            ImageLoader.load(mContext, mContentEntity.getPicUrl(), R.mipmap.home_menu_icon_def).into(mViewHolder.iv_icon);

        }
        mViewHolder.tv_name.setText(mContentEntity.getTitle());

        // 添加红点  多屏动画
        if ("80".equals(mContentEntity.getJumpAddr())) {
            if (PreferenceUtil.getBoolean(AppConstants.ANIMATION_RED_KEY, false)) {
                mViewHolder.red_not.setVisibility(View.GONE);
            } else {
                mViewHolder.red_not.setVisibility(View.VISIBLE);
            }
        }
        if("30".equals(mContentEntity.getJumpAddr()) || "31".equals(mContentEntity.getJumpAddr())){
            if (PreferenceUtil.getBoolean(AppConstants.LOTTERY_HK_RED_KEY, false)) {
                mViewHolder.red_not.setVisibility(View.GONE);
            } else {
                mViewHolder.red_not.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        View red_not;
    }
}
