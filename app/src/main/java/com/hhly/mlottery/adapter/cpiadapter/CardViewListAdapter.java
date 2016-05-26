package com.hhly.mlottery.adapter.cpiadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.frame.CPIFragment;
import com.hhly.mlottery.util.HandicapUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by 103TJL on 2016/5/4.
 * cardview里面listview的适配器
 */
public class CardViewListAdapter extends BaseAdapter {
    //公司水位信息
    private List<NewOddsInfo.AllInfoBean.ComListBean> mComList;
    //即时数据
    private String currentData;
    //初赔数据
    private String nextData;
    private Context context;
    private LayoutInflater mInflater;
    private NewOddsInfo.AllInfoBean.ComListBean.CurrLevelBean mCurrLevelBean;
    private NewOddsInfo.AllInfoBean.ComListBean.PreLevelBean mPreLevelBean;
    private String oddType;

    public CardViewListAdapter(Context context, List<NewOddsInfo.AllInfoBean.ComListBean> mComList, String oddType) {
        super();
        this.context = context;
        this.mComList = mComList;
        this.oddType = oddType;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mComList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mComList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_cpi_odds_list, null);
            item = new ListViewItem(convertView);
            convertView.setTag(item);
        } else {
            item = (ListViewItem) convertView.getTag();
        }

        mCurrLevelBean = mComList.get(position).getCurrLevel();
        mPreLevelBean = mComList.get(position).getPreLevel();
        if(mCurrLevelBean!=null && mPreLevelBean!=null){
        //公司名称
        item.cpi_item_list_company_txt.setText(mComList.get(position).getComName());
        //即赔小于初赔
        //主队
        if (mCurrLevelBean.getLeftUp() == -1) {
            item.cpi_item_list_home_txt.setTextColor(context.getResources().getColor(R.color.tabhost));
        }
        //即赔大于初赔
        else if (mCurrLevelBean.getLeftUp() == 1) {
            item.cpi_item_list_home_txt.setTextColor(context.getResources().getColor(R.color.homwe_lhc_red));
        } else {
            item.cpi_item_list_home_txt.setTextColor(context.getResources().getColor(R.color.black));
        }

           //盘口
            if ("green".equals(mCurrLevelBean.getCurrTextBgColor())){
                item.cpi_item_list_odds_txt.setTextColor(context.getResources().getColor(R.color.white));
                item.cpi_item_list_odds_txt.setBackgroundResource(R.color.tabhost);
            } else if ("red".equals(mCurrLevelBean.getCurrTextBgColor())) {
                item.cpi_item_list_odds_txt.setTextColor(context.getResources().getColor(R.color.white));
                item.cpi_item_list_odds_txt.setBackgroundResource(R.color.red);
            } else if("black".equals(mCurrLevelBean.getCurrTextBgColor())){
                item.cpi_item_list_odds_txt.setTextColor(context.getResources().getColor(R.color.black));
                item.cpi_item_list_odds_txt.setBackgroundResource(R.color.transparency);
            }else {
                //欧赔
            if (mCurrLevelBean.getMiddleUp() == -1) {
                item.cpi_item_list_odds_txt.setTextColor(context.getResources().getColor(R.color.tabhost));
            } else if (mCurrLevelBean.getMiddleUp() == 1) {
                item.cpi_item_list_odds_txt.setTextColor(context.getResources().getColor(R.color.homwe_lhc_red));
            } else {
                item.cpi_item_list_odds_txt.setTextColor(context.getResources().getColor(R.color.black));
            }
            }
        //客队
        if (mCurrLevelBean.getRightUp() == -1) {
            item.cpi_item_list_guest_txt.setTextColor(context.getResources().getColor(R.color.tabhost));
        } else if (mCurrLevelBean.getRightUp() == 1) {
            item.cpi_item_list_guest_txt.setTextColor(context.getResources().getColor(R.color.homwe_lhc_red));
        } else {
            item.cpi_item_list_guest_txt.setTextColor(context.getResources().getColor(R.color.black));
        }
        if (CPIFragment.TYPE_PLATE.equals(oddType)) {//亚盘
                //转换盘口
                item.cpi_item_list_odds_txt.setText(HandicapUtils.changeHandicap(mCurrLevelBean.getMiddle()));
                item.cpi_item_list_odds2_txt.setText(HandicapUtils.changeHandicap(mPreLevelBean.getMiddle()));
            item.cpi_item_list_odds_txt.setWidth(200);
        } else if (CPIFragment.TYPE_BIG.equals(oddType)) {//大小

                item.cpi_item_list_odds_txt.setText(HandicapUtils.changeHandicapByBigLittleBall(mCurrLevelBean.getMiddle()));
                item.cpi_item_list_odds2_txt.setText(HandicapUtils.changeHandicapByBigLittleBall(mPreLevelBean.getMiddle()));
            item.cpi_item_list_odds_txt.setWidth(120);

        } else {//欧赔
            //不用转换盘口
                item.cpi_item_list_odds_txt.setText(mCurrLevelBean.getMiddle());
                item.cpi_item_list_odds_txt.setText(mPreLevelBean.getMiddle());
        }

        //即赔
        item.cpi_item_list_home_txt.setText(mCurrLevelBean.getLeft());
        item.cpi_item_list_guest_txt.setText(mCurrLevelBean.getRight());
        //初赔
        item.cpi_item_list_home2_txt.setText(mPreLevelBean.getLeft());
        item.cpi_item_list_guest2_txt.setText(mPreLevelBean.getRight());
        }
        return convertView;
    }

    private static class ListViewItem {
        //公司名称
        public TextView cpi_item_list_company_txt;
        //即赔 主队，盘口，客队
        public TextView cpi_item_list_home_txt;
        public TextView cpi_item_list_odds_txt;
        public TextView cpi_item_list_guest_txt;
        //初赔 主队，盘口，客队
        public TextView cpi_item_list_home2_txt;
        public TextView cpi_item_list_odds2_txt;
        public TextView cpi_item_list_guest2_txt;

        public ListViewItem(View v) {
            cpi_item_list_company_txt = (TextView) v.findViewById(R.id.cpi_item_list_company_txt);

            cpi_item_list_home_txt = (TextView) v.findViewById(R.id.cpi_item_list_home_txt);
            cpi_item_list_odds_txt = (TextView) v.findViewById(R.id.cpi_item_list_odds_txt);
            cpi_item_list_guest_txt = (TextView) v.findViewById(R.id.cpi_item_list_guest_txt);

            cpi_item_list_home2_txt = (TextView) v.findViewById(R.id.cpi_item_list_home2_txt);
            cpi_item_list_odds2_txt = (TextView) v.findViewById(R.id.cpi_item_list_odds2_txt);
            cpi_item_list_guest2_txt = (TextView) v.findViewById(R.id.cpi_item_list_guest2_txt);
        }
    }

    /**
     * 清除数据
     */
    public void cardViewclearData() {
        // clear the data
        mComList.clear();
        notifyDataSetChanged();
    }
}
