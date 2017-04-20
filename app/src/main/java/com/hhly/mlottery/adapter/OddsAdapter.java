package com.hhly.mlottery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.OddsDataInfo;
import com.hhly.mlottery.util.HandicapUtils;

import java.util.List;

/**
 * Created by 103TJL on 2016/3/8.
 * 指数博彩适配器
 */
public class OddsAdapter extends BaseAdapter {

    private List<OddsDataInfo.ListOddEntity> oddList;

    private Context context;

    private String stKey;


    public OddsAdapter(Context context, List<OddsDataInfo.ListOddEntity> oddList, String stKey) {
        super();
        this.context = context;
        this.oddList = oddList;
        this.stKey = stKey;
    }

    @Override
    public int getCount() {
        return oddList.size();
    }

    @Override
    public Object getItem(int position) {
        return oddList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(context);
        convertView = _LayoutInflater.inflate(R.layout.item_odds, null);
        if (convertView != null) {
            OddsDataInfo.ListOddEntity.DetailsEntity currentOddList = oddList.get(position).getDetails().get(1);
            OddsDataInfo.ListOddEntity.DetailsEntity nextOddList = oddList.get(position).getDetails().get(0);
            //公司
            ((TextView) convertView.findViewById(R.id.plate_company_txt)).setText(oddList.get(position).getName());
            //（主队）判断初赔即赔 1是即赔，0是初赔 (即赔大于初赔，即赔为红色字体,初赔为绿色)
            if (currentOddList.getHomeOdd() > nextOddList.getHomeOdd()) {
                ((TextView) convertView.findViewById(R.id.plate_homeOdds_txt)).setTextColor(context.getResources().getColor(R.color.homwe_lhc_red));
                ((TextView) convertView.findViewById(R.id.plate_homeOdds_txt2)).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
            }
            ///即赔等于初赔
            else if (currentOddList.getHomeOdd() == nextOddList.getHomeOdd()) {
                ((TextView) convertView.findViewById(R.id.plate_homeOdds_txt2)).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
            }
            //即赔小于初赔
            else {
                ((TextView) convertView.findViewById(R.id.plate_homeOdds_txt)).setTextColor(context.getResources().getColor(R.color.tabhost));
                ((TextView) convertView.findViewById(R.id.plate_homeOdds_txt2)).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
            }
            //（盘口）判断初赔即赔 1是即赔，0是初赔 (即赔大于初赔，即赔为红色字体,初赔为绿色)
            if (currentOddList.getHand() > nextOddList.getHand()) {

                ((TextView) convertView.findViewById(R.id.plate_dish_txt)).setTextColor(context.getResources().getColor(R.color.homwe_lhc_red));
                ((TextView) convertView.findViewById(R.id.plate_dish_txt2)).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));

            } else if (currentOddList.getHand() == nextOddList.getHand()) {

                ((TextView) convertView.findViewById(R.id.plate_dish_txt2)).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));

            } else {

                ((TextView) convertView.findViewById(R.id.plate_dish_txt)).setTextColor(context.getResources().getColor(R.color.tabhost));
                ((TextView) convertView.findViewById(R.id.plate_dish_txt2)).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
            }

            //（客队）判断初赔即赔 1是即赔，0是初赔 (即赔大于初赔，即赔为红色字体,初赔为绿色)
            if (currentOddList.getGuestOdd() > nextOddList.getGuestOdd()) {
                ((TextView) convertView.findViewById(R.id.plate_guestOdds_txt)).setTextColor(context.getResources().getColor(R.color.homwe_lhc_red));
                ((TextView) convertView.findViewById(R.id.plate_guestOdds_txt2)).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
            } else if (currentOddList.getGuestOdd() == nextOddList.getGuestOdd()) {

                ((TextView) convertView.findViewById(R.id.plate_guestOdds_txt2)).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
            } else {
                ((TextView) convertView.findViewById(R.id.plate_guestOdds_txt)).setTextColor(context.getResources().getColor(R.color.tabhost));
                ((TextView) convertView.findViewById(R.id.plate_guestOdds_txt2)).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
            }
            if ("one".equals(stKey)) {//亚盘
                //无需改变
            } else if ("two".equals(stKey)) {//欧赔
                TableRow.LayoutParams params = new TableRow.LayoutParams(0, AbsListView.LayoutParams.MATCH_PARENT);
                params.weight = 3;
                params.rightMargin = 1;
                params.bottomMargin = 1;
                TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, AbsListView.LayoutParams.MATCH_PARENT);
                params2.weight = 3;
                params2.rightMargin = 1;
                convertView.findViewById(R.id.plate_dish_txt).setLayoutParams(params);
                convertView.findViewById(R.id.plate_dish_txt2).setLayoutParams(params2);
            } else if ("three".equals(stKey)) {//大小球
                //无需改变
            }

            //赔率 即赔（getDetails第1个是即赔，第0个是初赔）
            ((TextView) convertView.findViewById(R.id.plate_homeOdds_txt)).setText(String.format("%.2f", currentOddList.getHomeOdd()));
            ((TextView) convertView.findViewById(R.id.plate_guestOdds_txt)).setText(String.format("%.2f", currentOddList.getGuestOdd()));

            //赔率 初赔（getDetails第1个是即赔，第0个是初赔）
            ((TextView) convertView.findViewById(R.id.plate_homeOdds_txt2)).setText(String.format("%.2f", nextOddList.getHomeOdd()));
            ((TextView) convertView.findViewById(R.id.plate_guestOdds_txt2)).setText(String.format("%.2f", nextOddList.getGuestOdd()));
            if ("one".equals(stKey)) {//亚盘
                //转换盘口
                ((TextView) convertView.findViewById(R.id.plate_dish_txt)).setText(HandicapUtils.changeHandicap(currentOddList.getHand() + ""));
                ((TextView) convertView.findViewById(R.id.plate_dish_txt2)).setText(HandicapUtils.changeHandicap(nextOddList.getHand() + ""));
            } else if ("two".equals(stKey)) {//欧赔
                //不用变
                ((TextView) convertView.findViewById(R.id.plate_dish_txt)).setText(String.format("%.2f", currentOddList.getHand()));
                ((TextView) convertView.findViewById(R.id.plate_dish_txt2)).setText(String.format("%.2f", nextOddList.getHand()));
            } else if ("three".equals(stKey)) {//大小球
                //转为大小球
                ((TextView) convertView.findViewById(R.id.plate_dish_txt)).setText(HandicapUtils.changeHandicapByBigLittleBall(currentOddList.getHand() + ""));
                ((TextView) convertView.findViewById(R.id.plate_dish_txt2)).setText(HandicapUtils.changeHandicapByBigLittleBall(nextOddList.getHand() + ""));
            }


        }
        return convertView;
    }
}
