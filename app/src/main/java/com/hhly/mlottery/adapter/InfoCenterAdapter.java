package com.hhly.mlottery.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.infoCenterBean.ListEntity;

import java.util.List;

/**
 * 描  述：
 * 作  者：tangrr@13322.com
 * 时  间：2016/9/6
 */

public class InfoCenterAdapter extends BaseQuickAdapter<ListEntity> {

    Context mContext;

    public InfoCenterAdapter(Context context, int layoutResId, List<ListEntity> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ListEntity listEntity) {
        baseViewHolder.setText(R.id.tv_league_title, listEntity.leagueName + " " + listEntity.time);
        baseViewHolder.setText(R.id.tv_home_name, listEntity.homeName);
        baseViewHolder.setText(R.id.tv_guest_name, listEntity.guestName);
        baseViewHolder.setText(R.id.tv_home_data, listEntity.homeData);
        baseViewHolder.setText(R.id.tv_guest_data, listEntity.guestData);

        if (!TextUtils.isEmpty(listEntity.homeData) && !TextUtils.isEmpty(listEntity.guestData)) {
            if (listEntity.homeData.contains("%") && listEntity.guestData.contains("%")) {
                double home = Double.parseDouble(listEntity.homeData.substring(0, listEntity.homeData.lastIndexOf("%")));
                double guest = Double.parseDouble(listEntity.guestData.substring(0, listEntity.guestData.lastIndexOf("%")));
                double value = home / (home + guest) * 100;
                baseViewHolder.setProgress(R.id.progressbar, (int) value);
            } else {
                double home = Double.parseDouble(listEntity.homeData);
                double guest = Double.parseDouble(listEntity.guestData);
                double value = home / (home + guest) * 100;
                baseViewHolder.setProgress(R.id.progressbar, (int) value);
            }
        }
        switch (listEntity.indexType) {
            case 1:
                baseViewHolder.setText(R.id.tv_index_type, mContext.getResources().getString(R.string.info_center_end_index));
                break;
            case 2:
                baseViewHolder.setText(R.id.tv_index_type, mContext.getResources().getString(R.string.info_center_results_index));
                break;
            case 3:
                baseViewHolder.setText(R.id.tv_index_type, mContext.getResources().getString(R.string.info_center_size_disc_index));
                break;
            case 4:
                baseViewHolder.setText(R.id.tv_index_type, mContext.getResources().getString(R.string.info_center_asian_plate_index));
                break;
        }
        if (listEntity.flag == 1) {// 全部情报
            switch (listEntity.type) {
                case 1:
                    baseViewHolder.setText(R.id.tv_home_flag, mContext.getResources().getString(R.string.info_center_type1));
                    baseViewHolder.setText(R.id.tv_guest_flag, mContext.getResources().getString(R.string.info_center_type1));
                    break;
                case 2:
                    baseViewHolder.setText(R.id.tv_home_flag, mContext.getResources().getString(R.string.info_center_type2));
                    baseViewHolder.setText(R.id.tv_guest_flag, mContext.getResources().getString(R.string.info_center_type2));
                    break;
                case 3:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type3), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type3), listEntity.guestData));
                    break;
                case 4:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type4), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type4), listEntity.handicap));
                    break;
                case 5:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type5), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type5), listEntity.handicap));
                    break;
                case 6:
                    baseViewHolder.setText(R.id.tv_home_flag, mContext.getResources().getString(R.string.info_center_type6));
                    baseViewHolder.setText(R.id.tv_guest_flag, mContext.getResources().getString(R.string.info_center_type6));
                    break;
                case 7:
                    baseViewHolder.setText(R.id.tv_home_flag, mContext.getResources().getString(R.string.info_center_type7));
                    baseViewHolder.setText(R.id.tv_guest_flag, mContext.getResources().getString(R.string.info_center_type7));
                    break;
                case 8:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type8), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type8), listEntity.guestData));
                    break;
                case 9:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type9), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type9), listEntity.guestData));
                    break;
                case 10:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type10), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type10), listEntity.handicap));
                    break;
                case 11:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type11), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type11), listEntity.handicap));
                    break;
                case 12:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type12), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type12), listEntity.guestData));
                    break;
                case 13:
                    baseViewHolder.setText(R.id.tv_home_flag, mContext.getResources().getString(R.string.info_center_type13));
                    baseViewHolder.setText(R.id.tv_guest_flag, mContext.getResources().getString(R.string.info_center_type13));
                    break;
                case 14:
                    baseViewHolder.setText(R.id.tv_home_flag, mContext.getResources().getString(R.string.info_center_type14));
                    baseViewHolder.setText(R.id.tv_guest_flag, mContext.getResources().getString(R.string.info_center_type14));
                    break;
                case 15:
                    baseViewHolder.setText(R.id.tv_home_flag, mContext.getResources().getString(R.string.info_center_type15));
                    baseViewHolder.setText(R.id.tv_guest_flag, mContext.getResources().getString(R.string.info_center_type15));
                    break;
                case 16:
                    baseViewHolder.setText(R.id.tv_home_flag, mContext.getResources().getString(R.string.info_center_type16));
                    baseViewHolder.setText(R.id.tv_guest_flag, mContext.getResources().getString(R.string.info_center_type16));
                    break;
                case 17:
                    baseViewHolder.setText(R.id.tv_home_flag, mContext.getResources().getString(R.string.info_center_type17));
                    baseViewHolder.setText(R.id.tv_guest_flag, mContext.getResources().getString(R.string.info_center_type17));
                    break;
                case 18:
                    baseViewHolder.setText(R.id.tv_home_flag, mContext.getResources().getString(R.string.info_center_type18));
                    baseViewHolder.setText(R.id.tv_guest_flag, mContext.getResources().getString(R.string.info_center_type18));
                    break;
                case 19:
                    baseViewHolder.setText(R.id.tv_home_flag, mContext.getResources().getString(R.string.info_center_type19));
                    baseViewHolder.setText(R.id.tv_guest_flag, mContext.getResources().getString(R.string.info_center_type19));
                    break;
                case 20:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type20), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type20), listEntity.guestData));
                    break;
                case 21:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type21), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type21), listEntity.handicap));
                    break;
                case 22:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type22), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type22), listEntity.handicap));
                    break;
                case 23:
                    baseViewHolder.setText(R.id.tv_home_flag, mContext.getResources().getString(R.string.info_center_type23));
                    baseViewHolder.setText(R.id.tv_guest_flag, mContext.getResources().getString(R.string.info_center_type23));
                    break;
                case 24:
                    baseViewHolder.setText(R.id.tv_home_flag, mContext.getResources().getString(R.string.info_center_type24));
                    baseViewHolder.setText(R.id.tv_guest_flag, mContext.getResources().getString(R.string.info_center_type24));
                    break;
                case 25:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type25), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type25), listEntity.guestData));
                    break;
                case 26:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type26), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type26), listEntity.guestData));
                    break;
                case 27:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type27), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type27), listEntity.handicap));
                    break;
                case 28:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type28), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type28), listEntity.handicap));
                    break;
                case 29:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_type29), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_type29), listEntity.guestData));
                    break;
                default:
                    break;
            }
        } else if (listEntity.flag == 2) {// 相同主客场情报  主场：basket_analyze_home  客场：basket_analyze_guest
            switch (listEntity.type) {
                case 1:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type1), mContext.getResources().getString(R.string.basket_analyze_home)));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type1), mContext.getResources().getString(R.string.basket_analyze_guest)));
                    break;
                case 2:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type2), mContext.getResources().getString(R.string.basket_analyze_home)));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type2), mContext.getResources().getString(R.string.basket_analyze_guest)));
                    break;
                case 3:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type3), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type3), listEntity.guestData));
                    break;
                case 4:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type4), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type4), listEntity.handicap));
                    break;
                case 5:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type5), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type5), listEntity.handicap));
                    break;
                case 6:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type6), mContext.getResources().getString(R.string.basket_analyze_home)));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type6), mContext.getResources().getString(R.string.basket_analyze_guest)));
                    break;
                case 7:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type7), mContext.getResources().getString(R.string.basket_analyze_home)));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type7), mContext.getResources().getString(R.string.basket_analyze_guest)));
                    break;
                case 8:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type8), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type8), listEntity.guestData));
                    break;
                case 9:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type9), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type9), listEntity.guestData));
                    break;
                case 10:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type10), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type10), listEntity.handicap));
                    break;
                case 11:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type11), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type11), listEntity.handicap));
                    break;
                case 12:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type12), mContext.getResources().getString(R.string.basket_analyze_home), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type12), mContext.getResources().getString(R.string.basket_analyze_guest), listEntity.guestData));
                    break;
                case 13:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type13)));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type13)));
                    break;
                case 14:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type14)));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type14)));
                    break;
                case 15:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type15)));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type15)));
                    break;
                case 16:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type16)));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type16)));
                    break;
                case 17:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type17)));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type17)));
                    break;
                case 18:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type18), mContext.getResources().getString(R.string.basket_analyze_home)));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type18), mContext.getResources().getString(R.string.basket_analyze_guest)));
                    break;
                case 19:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type19), mContext.getResources().getString(R.string.basket_analyze_home)));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type19), mContext.getResources().getString(R.string.basket_analyze_guest)));
                    break;
                case 20:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type20), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type20), listEntity.guestData));
                    break;
                case 21:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type21), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type21), listEntity.handicap));
                    break;
                case 22:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type22), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type22), listEntity.handicap));
                    break;
                case 23:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type23), mContext.getResources().getString(R.string.basket_analyze_home)));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type23), mContext.getResources().getString(R.string.basket_analyze_guest)));
                    break;
                case 24:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type24), mContext.getResources().getString(R.string.basket_analyze_home)));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type24), mContext.getResources().getString(R.string.basket_analyze_guest)));
                    break;
                case 25:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type25), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type25), listEntity.guestData));
                    break;
                case 26:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type26), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type26), listEntity.guestData));
                    break;
                case 27:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type27), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type27), listEntity.handicap));
                    break;
                case 28:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type28), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type28), listEntity.handicap));
                    break;
                case 29:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type29), listEntity.homeData));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type29), listEntity.guestData));
                    break;
                case 30:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type30), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type30), listEntity.handicap));
                    break;
                case 31:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type31), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type31), listEntity.handicap));
                    break;
                case 32:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type32), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type32), listEntity.handicap));
                    break;
                case 33:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type33), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type33), listEntity.handicap));
                    break;
                case 34:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type34), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type34), listEntity.handicap));
                    break;
                case 35:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type35), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type35), listEntity.handicap));
                    break;
                case 36:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type36), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type36), listEntity.handicap));
                    break;
                case 37:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type37), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type37), listEntity.handicap));
                    break;
                case 38:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type38), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type38), listEntity.handicap));
                    break;
                case 39:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type39), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type39), listEntity.handicap));
                    break;
                case 40:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type40), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type40), listEntity.handicap));
                    break;
                case 41:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type41), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type41), listEntity.handicap));
                    break;
                case 42:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type42), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type42), listEntity.handicap));
                    break;
                case 43:
                    baseViewHolder.setText(R.id.tv_home_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type43), listEntity.handicap));
                    baseViewHolder.setText(R.id.tv_guest_flag, String.format(mContext.getResources().getString(R.string.info_center_other_type43), listEntity.handicap));
                    break;
                default:
                    break;
            }
        }
    }
}
