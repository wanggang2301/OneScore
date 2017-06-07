package com.hhly.mlottery.adapter;

import android.content.Context;
import android.text.Html;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.OddsDetailsDataInfo;
import com.hhly.mlottery.util.HandicapUtils;
import com.hhly.mlottery.util.StringUtils;
import com.hhly.mlottery.widget.PinnedHeaderExpandableListView;

import java.util.List;

/**
 * Created by 103TJL on 2016/3/8.
 * 指数博彩适配器BaseExpandableListAdapter
 */
public class OddsDetailsAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.HeaderAdapter {

    private List<String> mGroupDataList;//父类view 数据
    List<List<OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity>> mChildrenDataList;//子view数据
    private Context mContext;
    private PinnedHeaderExpandableListView listView;
    private LayoutInflater inflater;
    private String stKey;

    public OddsDetailsAdapter(List<List<OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity>> childrenDataList, List<String> groupDataList
            , Context mContext, PinnedHeaderExpandableListView listView,String stKey) {
        this.mGroupDataList = groupDataList;
        this.mChildrenDataList = childrenDataList;
        this.mContext = mContext;
        this.listView = listView;
        this.stKey = stKey;
        inflater = LayoutInflater.from(this.mContext);
    }

    /**
     * 获取指定组中的指定子元素数据。    返回值      返回指定子元素数据。
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildrenDataList.get(groupPosition).get(childPosition);
    }

    /**
     * 获取指定组中的指定子元素ID，这个ID在组里一定是唯一的。联合ID
     * （getCombinedChildId(long, long)）在所有条目（所有组和所有元素）中也是唯一的。
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    /**
     * 设置 子内容 item数据
     *
     * @param groupPosition 组位置（该组内部含有子元素）
     * @param childPosition 子元素位置（决定返回哪个视图）
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象。注意：在使用前你应该检查一下这个视图对象是否非空并且这个对象的类型是否合适。
     *                      由此引伸出，如果该对象不能被转换并显示正确的数据，这个方法就会调用getChildView(int, int, boolean, View, ViewGroup)来创建一个视图(View)对象。
     * @param parent        指定位置上的子元素返回的视图对象
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (null == convertView) {
            convertView = createChildrenView();
            holder = new Holder();
            //时间和比分
            holder.odds_details_timeAndscore_child_txt = (TextView) convertView.findViewById(R.id.odds_details_timeAndscore_txt);
            //主队分数
            holder.odds_details_home_child_txt = (TextView) convertView.findViewById(R.id.odds_details_home_txt);
            //盘口
            holder.odds_details_dish_child_txt = (TextView) convertView.findViewById(R.id.odds_details_dish_txt);
            //客队分数
            holder.odds_details_guest_child_txt = (TextView) convertView.findViewById(R.id.odds_details_guest_txt);
            holder.odds_details_dish_layout = (LinearLayout) convertView.findViewById(R.id.odds_details_dish_layout);
            if ("one".equals(stKey)) {//亚盘
                //无需改变
            } else if ("two".equals(stKey)) {//欧赔
                TableRow.LayoutParams params = new TableRow.LayoutParams(0, AbsListView.LayoutParams.MATCH_PARENT);
                params.weight = 3;
                params.rightMargin=1;

                holder.odds_details_dish_layout.setLayoutParams(params);
            } else if ("three".equals(stKey)) {//大小球
                //无需改变
            }
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }
        final OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity model = mChildrenDataList.get(groupPosition).get(childPosition);


        //如果详情比分为空
        if (model.getScore() == null) {
            holder.odds_details_timeAndscore_child_txt.setText (model.getTime());
        } else {

            if(model.isScoreVisible()){
                holder.odds_details_timeAndscore_child_txt.setText(Html.fromHtml(mChildrenDataList.get(groupPosition).get(childPosition).getTime()
                        + "<br/>" + "<font color=#ff0000>" + mChildrenDataList.get(groupPosition).get(childPosition).getScore()+"</font>"));
            }else{
                holder.odds_details_timeAndscore_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).getTime());
            }
        }


        //判断是主队的数据

        holder.odds_details_home_child_txt.setText(String.format("%.2f",model.getHomeOdd()));
        if ("green".equals(model.getHomeColor())) {
            holder.odds_details_home_child_txt.setTextColor(mContext.getResources().getColor(R.color.tabhost));
        } else if ("red".equals(model.getHomeColor())) {
            holder.odds_details_home_child_txt.setTextColor(mContext.getResources().getColor(R.color.homwe_lhc_red));
        } else if ("black".equals(model.getHomeColor())) {
            holder.odds_details_home_child_txt.setTextColor(mContext.getResources().getColor(R.color.content_txt_dark_grad));
        }
       //客队
        holder.odds_details_guest_child_txt.setText(String.format("%.2f",model.getGuestOdd()));
        if ("green".equals(model.getGuestColor())) {
            holder.odds_details_guest_child_txt.setTextColor(mContext.getResources().getColor(R.color.tabhost));
        } else if ("red".equals(model.getGuestColor())) {
            holder.odds_details_guest_child_txt.setTextColor(mContext.getResources().getColor(R.color.homwe_lhc_red));
        } else if ("black".equals(model.getGuestColor())) {
            holder.odds_details_guest_child_txt.setTextColor(mContext.getResources().getColor(R.color.content_txt_dark_grad));
        }

        //盘口
//        holder.odds_details_dish_child_txt.setText(data.model.getHand() + "");
        if ("one".equals(stKey)) {//亚盘
            //转换盘口
            holder.odds_details_dish_child_txt.setText(HandicapUtils.changeHandicap(String.format("%.2f",model.getHand()) ));
        } else if ("two".equals(stKey)) {//欧赔
            //不用变
            holder.odds_details_dish_child_txt.setText(String.format("%.2f",model.getHand()));
        } else if ("three".equals(stKey)) {//大小球
            //转为大小球
            holder.odds_details_dish_child_txt.setText(HandicapUtils.changeHandicapByBigLittleBall(model.getHand()+""));
        }

        if ("green".equals(model.getDishColor())) {
            holder.odds_details_dish_child_txt.setTextColor(mContext.getResources().getColor(R.color.tabhost));
        } else if ("red".equals(model.getDishColor())) {
            holder.odds_details_dish_child_txt.setTextColor(mContext.getResources().getColor(R.color.homwe_lhc_red));
        } else if ("black".equals(model.getDishColor())) {
            holder.odds_details_dish_child_txt.setTextColor(mContext.getResources().getColor(R.color.content_txt_dark_grad));
        }

        //主队分数
//        holder.odds_details_home_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).getHomeOdd() + "");
        //盘口
//        holder.odds_details_dish_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).getHand() + "");
        //客队分数
//        holder.odds_details_guest_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).getGuestOdd() + "");

        return convertView;
    }
    /**
     * 子类的holder
     */
    public class Holder {

        TextView odds_details_timeAndscore_child_txt; //时间和比分
        TextView odds_details_home_child_txt; //主队分数
        TextView odds_details_dish_child_txt; //盘口
        TextView odds_details_guest_child_txt; //客队分数
        LinearLayout odds_details_dish_layout;

    }

    /**
     * 获取指定组中的子元素个数
     *
     * @param groupPosition 组位置（决定返回哪个组的子元素个数）
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
//        return mChildrenDataList.get(groupPosition).size();
        return mChildrenDataList.size() > 0 ? mChildrenDataList.get(groupPosition).size() : 0;
    }

    /**
     * 获取指定组中的数据
     *
     * @param groupPosition 组位置
     * @return 返回组中的数据，也就是该组中的子元素数据
     */
    @Override
    public Object getGroup(int groupPosition) {
        return mGroupDataList.get(groupPosition);
    }

    /**
     * 获取组的个数
     *
     * @return 组的个数
     */
    @Override
    public int getGroupCount() {

        return mGroupDataList.size();
    }

    /**
     * 获取指定组的ID，这个组ID必须是唯一的。联合ID(参见getCombinedGroupId(long))在所有条目(所有组和所有元素)中也是唯一的
     *
     * @param groupPosition
     * @return
     */
    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    /**
     * 父类，头部控件
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        LinearLayout parentLayout = (LinearLayout) View.inflate(mContext, R.layout.item_odds_header, null);
        TextView live_item_day_tx = (TextView) parentLayout.findViewById(R.id.odds_details_data_txt);
        live_item_day_tx.setText(mGroupDataList.get(groupPosition));
        return parentLayout;
    }

    /**
     * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
     *
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 是否选中指定位置上的子元素。
     *
     * @param groupPosition 组位置（该组内部含有这个子元素）
     * @param childPosition 子元素位置
     * @return 是否选中子元素
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private View createChildrenView() {
        return inflater.inflate(R.layout.item_odds_details_child, null);
    }


    @Override
    public int getHeaderState(int groupPosition, int childPosition) {
        final int childCount = getChildrenCount(groupPosition);
        if (childPosition == childCount - 1) {
            return PINNED_HEADER_PUSHED_UP;
        } else if (childPosition == -1
                && !listView.isGroupExpanded(groupPosition)) {
            return PINNED_HEADER_GONE;
        } else {
            return PINNED_HEADER_VISIBLE;
        }
    }

    /**
     * 设置置顶的  item数据（父类）
     */
    @Override
    public void configureHeader(View header, int groupPosition,
                                int childPosition, int alpha) {

        String groupData = this.mGroupDataList.get(groupPosition).toString();
        ((TextView) header.findViewById(R.id.odds_details_data_txt)).setText(groupData);

    }

    private SparseIntArray groupStatusMap = new SparseIntArray();

//    @Override
//    暂时注释，需要点击头部的时候打开
//    public void setGroupClickStatus(int groupPosition, int status) {
//        groupStatusMap.put(groupPosition, status);
//    }

    @Override
    public int getGroupClickStatus(int groupPosition) {
        if (groupStatusMap.keyAt(groupPosition) >= 0) {
            return groupStatusMap.get(groupPosition);
        } else {
            return 0;
        }
    }

    /**
     * 清除父类view和子类view的数据
     */
    public void clearData() {
        // clear the data
        mGroupDataList.clear();
        mChildrenDataList.clear();
        notifyDataSetChanged();
    }
}
