package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.BasketMatchFilter;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by A on 2016/1/18.
 * 筛选 adapter
 */
public class FiltrateAdapter extends CommonAdapter<BasketMatchFilter> {


//    private BasketFiltrateActivity.FiltrateClickListener mChickedListener;
//
//    public void setmChickedListener(BasketFiltrateActivity.FiltrateClickListener mChickedListener) {
//        this.mChickedListener = mChickedListener;
//    }

//    private boolean isNull = false;


    private ImageView mImage;

    public FiltrateAdapter(Context context, List<BasketMatchFilter> datas, int layoutId) {
        super(context, datas, layoutId);


    }
    @Override
    public void convert(final ViewHolder holder, final BasketMatchFilter mFilter) {

//        if (mFilter != null) {

        if(mFilter.getLeagueId().equals("-1")){
            holder.setText(R.id.item_category_name_1, mFilter.getLeagueName());
            holder.setVisible(R.id.item_category_icon_1, false);
            holder.setBackgroundRes(R.id.iv_basket_filtate, R.mipmap.shaixuan_icon_shoucang);
        }else {

            String url = mFilter.getLeagueLogoUrl();

            /**
             * 判断联赛名长度大于6时，只显示6个字符（多出部分不显示）
             */
            String mLeagueName = mFilter.getLeagueName();
            if(mLeagueName.length() > 6){
                //length大于6时,截取前六个字符显示
                mLeagueName = mLeagueName.substring(0,6);//--> [0,6)
            }
            holder.setText(R.id.item_category_name_1, mLeagueName);

            /**
             * ImageLoader -->加载图片
             */
            mImage = holder.getView(R.id.item_category_icon_1);
            ImageLoader.load(mContext,url,R.mipmap.basket_default).into(mImage);

            if (mFilter.isChecked()) {//不选中->选中
                holder.setBackgroundRes(R.id.iv_basket_filtate, R.mipmap.shaixuan_icon_shoucang_hover);
            } else {//选中->不选中
                holder.setBackgroundRes(R.id.iv_basket_filtate, R.mipmap.shaixuan_icon_shoucang);
            }


            holder.setOnClickListener(R.id.item_all, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                Toast.makeText(mContext, "item_all==>" + holder.getPosition(), Toast.LENGTH_SHORT).show();
                    if (!mFilter.isChecked()) {//不选中->选中

                        holder.setBackgroundRes(R.id.iv_basket_filtate, R.mipmap.shaixuan_icon_shoucang_hover);
                    } else {//选中->不选中

                        holder.setBackgroundRes(R.id.iv_basket_filtate, R.mipmap.shaixuan_icon_shoucang);
                    }
                    onCheckListener.onCheck(mFilter);
                }
            });
        }
    }

    private FiltrateAdapter.OnCheckListener onCheckListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public interface OnCheckListener {
        void onCheck(BasketMatchFilter mFilter);
    }


}
