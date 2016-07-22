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
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketballDatabaseDetailsActivity;
import com.hhly.mlottery.bean.basket.infomation.LeagueBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * @author wang gang
 * @date 2016/7/15 10:48
 * @des ${TODO}
 */
public class BasketInfoGridAdapter extends BaseAdapter {

    private static final String LEAGUEID="leagueId";

    private DisplayImageOptions options; //
    private com.nostra13.universalimageloader.core.ImageLoader universalImageLoader;
    private List<LeagueBean> mList;
    private Context mContext;


    public BasketInfoGridAdapter(Context context, List<LeagueBean> list) {
        this.mContext = context;
        this.mList = list;

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageOnLoading(R.mipmap.basket_default)   //默认图片
                .showImageForEmptyUri(R.mipmap.basket_default)    //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.mipmap.basket_default)// 加载失败显示的图片
                .resetViewBeforeLoading(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext).build();
        universalImageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance(); //初始化
        universalImageLoader.init(config);

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
                mViewHolder.icon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.basket_default));
            } else {
                universalImageLoader.displayImage(mList.get(position).getLeagueLogoUrl(), mViewHolder.icon, options);
            }
            mViewHolder.name.setText(mList.get(position).getLeagueName());
            mViewHolder.rl.setEnabled(true);
        }

        mViewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, BasketballDatabaseDetailsActivity.class);
                intent.putExtra(LEAGUEID,mList.get(position).getLeagueId());
                mContext.startActivity(intent);
                Toast.makeText(mContext, "当前选中的是:" + mList.get(position).getLeagueId(), Toast.LENGTH_SHORT).show();

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
