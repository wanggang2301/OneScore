package com.hhly.mlottery.adapter.homePagerAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.homepagerentity.HomeContentEntity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 种类总入口GridView数据适配器
 * Created by hhly107 on 2016/3/30.
 */
public class HomeGridAdapter extends BaseAdapter {

    private Context mContext;
    private HomeListBaseAdapter.ViewHolder mTopHolder;// 头部ViewHolder

    private ViewHolder mViewHolder;// ViewHolder
    private DisplayImageOptions options;// 设置ImageLoder参数
    List<HomeContentEntity> mData;


    public HomeGridAdapter(Context context, List<HomeContentEntity> list) {
        this.mContext = context;
        this.mData = list;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.home_menu_icon_def).showImageOnFail(R.mipmap.home_menu_icon_def)
                .cacheInMemory(true).bitmapConfig(Bitmap.Config.ARGB_8888)
                .cacheOnDisc(true).considerExifParams(true).build();
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
            convertView.setTag(mViewHolder);
        }
        mViewHolder = (ViewHolder) convertView.getTag();
        HomeContentEntity mContentEntity = (HomeContentEntity) getItem(position);

        if (mContentEntity.getPicUrl() == null) {
            mViewHolder.iv_icon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.home_menu_icon_def));
        } else {
            ImageLoader.getInstance().displayImage(mContentEntity.getPicUrl(), mViewHolder.iv_icon, options);// 设置图标
        }
        mViewHolder.tv_name.setText(mContentEntity.getTitle());

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
    }
}
