package com.hhly.mlottery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.ForeignInfomationDetailsActivity;
import com.hhly.mlottery.activity.PicturePreviewActivity;
import com.hhly.mlottery.bean.foreigninfomation.OverseasInformationListBean;
import com.hhly.mlottery.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * 描述:  境外资讯Adapter
 * 作者:  wangg@13322.com
 * 时间:  2016/9/12 12:11
 */
public class ForeignInfomationAdapter extends BaseQuickAdapter<OverseasInformationListBean> {
    private Context mContext;
    private List<OverseasInformationListBean> list;
    private int num = 0;

    private DisplayImageOptions options; //
    private com.nostra13.universalimageloader.core.ImageLoader universalImageLoader;


    public ForeignInfomationAdapter(Context context, List<OverseasInformationListBean> data) {
        super(R.layout.item_foreign_infomation, data);
        this.mContext = context;
        this.list = data;


        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageOnLoading(R.mipmap.score_default)   //默认图片
                .showImageForEmptyUri(R.mipmap.score_default)    //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.mipmap.score_default)// 加载失败显示的图片
                .resetViewBeforeLoading(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext).build();
        universalImageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance(); //初始化
        universalImageLoader.init(config);
    }


    @Override
    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return super.getViewHolderPosition(viewHolder);
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, final OverseasInformationListBean o) {
        LinearLayout linearLayout = viewHolder.getView(R.id.item_ll);


        universalImageLoader.displayImage(o.getAvatar(), (CircleImageView) viewHolder.getView(R.id.civ_logo), options);

        long mNumberTime = o.getCurrentTimestamp() - o.getTimestamp();

        long month = mNumberTime / (1000 * 60 * 60 * 24 * 30);// 获取月

        long dd = mNumberTime / (1000 * 60 * 60 * 24);// 获取天

        long hh = mNumberTime / (1000 * 60 * 60);// 获取相差小时

        long mm = mNumberTime / (1000 * 60);// 获取相差分

        long ss = mNumberTime / 1000;// 获取相差秒

        String timeMsg = "";

        if (month != 0) {
            timeMsg = dd + "月前";
        } else if (dd != 0) {
            timeMsg = dd + "天前";
        } else if (hh != 0) {
            timeMsg = hh + "小时前";
        } else if (mm != 0) {
            timeMsg = mm + "分钟前";
        } else {
            timeMsg = "刚刚";
        }


        viewHolder.setText(R.id.tv_time, timeMsg);


        viewHolder.setText(R.id.tv_name_en, o.getFullname());
        viewHolder.setText(R.id.tv_name_ch, o.getFullnameTranslation());
        viewHolder.setText(R.id.tv_content_en, o.getContent());


        viewHolder.setText(R.id.tv_content_zh, o.getContentTranslation());
        viewHolder.setText(R.id.tv_tight, o.getFavorite() + "");

        universalImageLoader.displayImage(o.getPhoto(), (ImageView) viewHolder.getView(R.id.iv_photo), options);


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ForeignInfomationDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("focusable", false);
                mContext.startActivity(intent);
            }
        });


        linearLayout.findViewById(R.id.ll_cmt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ForeignInfomationDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("focusable", false);
                // intent.putExtra("detailsData", o);


                mContext.startActivity(intent);
            }
        });

        linearLayout.findViewById(R.id.iv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://hiphotos.baidu.com/%B3%F5%BC%B6%BE%D1%BB%F7%CA%D6/pic/item/929b56443840bfc6b3b7dc64.jpg";


                Intent intent = new Intent(mContext, PicturePreviewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("url", url);
                mContext.startActivity(intent);
            }
        });

        linearLayout.findViewById(R.id.ll_zan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.setText(R.id.tv_tight, (num++) + "");
            }
        });
    }
}

