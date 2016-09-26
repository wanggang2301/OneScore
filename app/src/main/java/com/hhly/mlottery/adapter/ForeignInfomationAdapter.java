package com.hhly.mlottery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.ForeignInfomationDetailsActivity;
import com.hhly.mlottery.activity.PicturePreviewActivity;
import com.hhly.mlottery.bean.foreigninfomation.OverseasInformationListBean;
import com.hhly.mlottery.bean.foreigninfomation.TightBean;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:  境外资讯Adapter
 * 作者:  wangg@13322.com
 * 时间:  2016/9/12 12:11
 */
public class ForeignInfomationAdapter extends BaseQuickAdapter<OverseasInformationListBean> {
    private Context mContext;
    private List<OverseasInformationListBean> list;


    private DisplayImageOptions optionsLogo; //


    private DisplayImageOptions options; //
    private com.nostra13.universalimageloader.core.ImageLoader universalImageLoader;


    public ForeignInfomationAdapter(Context context, List<OverseasInformationListBean> data) {
        super(R.layout.item_foreign_infomation, data);
        this.mContext = context;
        this.list = data;

        optionsLogo = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageOnLoading(R.mipmap.center_head)   //默认图片
                .showImageForEmptyUri(R.mipmap.center_head)    //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.mipmap.center_head)// 加载失败显示的图片
                .resetViewBeforeLoading(true)
                .build();

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageOnLoading(R.mipmap.counsel_depth)   //默认图片
                .showImageForEmptyUri(R.mipmap.counsel_depth)    //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.mipmap.counsel_depth)// 加载失败显示的图片
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


        universalImageLoader.displayImage(o.getAvatar(), (CircleImageView) viewHolder.getView(R.id.civ_logo), optionsLogo);

        long mNumberTime = o.getCurrentTimestamp() - o.getTimestamp();

        long month = mNumberTime / (1000 * 60 * 60 * 24 * 30);// 获取月

        long dd = mNumberTime / (1000 * 60 * 60 * 24);// 获取天

        long hh = mNumberTime / (1000 * 60 * 60);// 获取相差小时

        long mm = mNumberTime / (1000 * 60);// 获取相差分

        long ss = mNumberTime / 1000;// 获取相差秒

        String timeMsg = "";

        if (month != 0) {
            timeMsg = dd + mContext.getResources().getString(R.string.foreign_month);
        } else if (dd != 0) {
            timeMsg = dd + mContext.getResources().getString(R.string.foreign_day);
        } else if (hh != 0) {
            timeMsg = hh + mContext.getResources().getString(R.string.foreign_hour);
        } else if (mm != 0) {
            timeMsg = mm + mContext.getResources().getString(R.string.foreign_minites);
        } else {
            timeMsg = mContext.getResources().getString(R.string.foreign_now);
        }

        o.setSendtime(timeMsg);

        viewHolder.setText(R.id.tv_time, timeMsg);


        viewHolder.setText(R.id.tv_name_en, o.getFullname());

        if (o.getFullnameTranslation() == null) {
            viewHolder.getView(R.id.tv_name_ch).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.tv_name_ch).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_name_ch, o.getFullnameTranslation());
        }
        viewHolder.setText(R.id.tv_content_en, o.getContent());

        if (o.getContentTranslation() == null) {
            viewHolder.getView(R.id.tv_content_zh).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.tv_content_zh).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_content_zh, o.getContentTranslation());
        }


        viewHolder.setText(R.id.tv_tight, o.getFavorite() + "");

        if (o.getPhoto() == null) {
            viewHolder.getView(R.id.iv_photo).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.iv_photo).setVisibility(View.VISIBLE);
            universalImageLoader.displayImage(o.getPhoto(), (ImageView) viewHolder.getView(R.id.iv_photo), options);
        }


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ForeignInfomationDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("detailsData", o);
                mContext.startActivity(intent);
            }
        });


        linearLayout.findViewById(R.id.ll_cmt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ForeignInfomationDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("detailsData", o);
                mContext.startActivity(intent);
            }
        });

        linearLayout.findViewById(R.id.iv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (o.getPhoto() != null) {
                    String url = o.getPhoto();
                    Intent intent = new Intent(mContext, PicturePreviewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url", url);
                    mContext.startActivity(intent);
                }
            }
        });

        linearLayout.findViewById(R.id.ll_zan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tightClick((TextView) viewHolder.getView(R.id.tv_tight), o);
            }
        });
    }


    private void tightClick(final TextView textView, final OverseasInformationListBean oil) {
        Map<String, String> params = new HashMap<>();
        params.put("id", oil.getId() + "");
        String url = "http://192.168.31.178:8080/mlottery/core/overseasInformation.updLike.do";
        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<TightBean>() {
            @Override
            public void onResponse(TightBean tightBean) {
                if (!tightBean.getResult().equals("200")) {
                    return;
                }
                L.d("789456", "adapter====" + tightBean.getFavorite());

                textView.setText(tightBean.getFavorite() + "");
                oil.setFavorite(tightBean.getFavorite());

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, TightBean.class);
    }
}

