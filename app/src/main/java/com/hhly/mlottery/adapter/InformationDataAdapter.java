package com.hhly.mlottery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballsecond.InforListData;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;


/**
 * @ClassName: OneScore
 * @author:Administrator luyao
 * @Description:  资料库第一列表数据adapter
 * @data: 2016/3/30 11:40
 */
public class InformationDataAdapter extends CommonAdapter<InforListData.LeagueListBean> {


    private final Context mContext;
    private com.nostra13.universalimageloader.core.ImageLoader universalImageLoader;
    private final DisplayImageOptions options;
    private ImageView mTeam_icon;

    public InformationDataAdapter(Context context, List<InforListData.LeagueListBean> datas, int layoutId) {
        super(context, datas, layoutId);
        mContext = context;
        this.mDatas=datas;

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageOnLoading(R.mipmap.football_default)   //默认图片
                .showImageForEmptyUri(R.mipmap.basket_default)    //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.mipmap.basket_default)// 加载失败显示的图片
                .resetViewBeforeLoading(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext).build();
        universalImageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance(); //初始化
        universalImageLoader.init(config);


    }



    public void updateDatas(List<InforListData.LeagueListBean> datas) {
        this.mDatas = datas;
    }

    //获取子布局控件


    @Override
    public void convert(ViewHolder holder, InforListData.LeagueListBean listDatas) {


          int leagueId=listDatas.getLid();
          String logoUrl="http://pic.13322.com/icons/league/"+leagueId+".png";
          mTeam_icon = (ImageView) holder.getConvertView().findViewById(R.id.football_logo_img);
         //加载图片
          universalImageLoader.displayImage(logoUrl, mTeam_icon, options);

          //获取联赛名称
          holder.setText(R.id.football_item_team, listDatas.getName());


    }
}
