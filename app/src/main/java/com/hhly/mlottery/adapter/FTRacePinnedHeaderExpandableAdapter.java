package com.hhly.mlottery.adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.LeagueRoundInfo;
import com.hhly.mlottery.widget.PinnedHeaderExpandableListView;

import java.util.List;

/**
 * @author  lzf
 * @ClassName:
 * @Description:
 * @date 4.6
 */
public class FTRacePinnedHeaderExpandableAdapter  extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.HeaderAdapter{
    private List<String> mGroupDataList;//父类view 数据
    private List<List<LeagueRoundInfo.RaceBean.ListBean >>mChildrenDataList;//子view数据
    private Context mContext;
    private PinnedHeaderExpandableListView listView;
    private LayoutInflater inflater;

    public FTRacePinnedHeaderExpandableAdapter(List<String> groupDataList, List<List<LeagueRoundInfo.RaceBean.ListBean >> childrenDataList, Context context, PinnedHeaderExpandableListView listView) {
        this.mGroupDataList = groupDataList;
        this.mChildrenDataList = childrenDataList;
        this.mContext = context;
        this.listView = listView;
        inflater = LayoutInflater.from(this.mContext);
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
     * 获取指定组中的子元素个数
     *
     * @param groupPosition 组位置（决定返回哪个组的子元素个数）
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildrenDataList.get(groupPosition).size();
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
     * 获取指定组中的指定子元素数据。    返回值      返回指定子元素数据。
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildrenDataList.get(groupPosition).get(childPosition);
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
     * 获取指定组中的指定子元素ID，这个ID在组里一定是唯一的。联合ID
     * （getCombinedChildId(long, long)）在所有条目（所有组和所有元素）中也是唯一的。
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
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

//

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.agendafragment_lv_item_header, null);
        TextView live_item_day_tx = (TextView) view.findViewById(R.id.tv_agendafg_lv_gropname);
        RelativeLayout top= (RelativeLayout) view.findViewById(R.id.top);
        if (mGroupDataList.size()==0){//没有数据
            top.setVisibility(View.GONE);
        }else {//有数据
            if(mGroupDataList.get(groupPosition).equals("empty")){
                top.setVisibility(View.GONE);

            }else {
                top.setVisibility(View.VISIBLE);
                live_item_day_tx.setText(mGroupDataList.get(groupPosition)+mContext.getString(R.string.information_agendal_group));
            }

        }

        return view;
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
            holder=new Holder();
            holder.tv_agendafg_lv_data = (TextView) convertView.findViewById(R.id.tv_agendafg_lv_data);
            holder.tv_agendafg_lv_time = (TextView) convertView.findViewById(R.id.tv_agendafg_lv_time);
            holder.tv_agendafg_lv_teamnamel = (TextView) convertView.findViewById(R.id.tv_agendafg_lv_teamnamel);
            holder.tv_agendafg_lv_teamnamer = (TextView) convertView.findViewById(R.id.tv_agendafg_lv_teamnamer);
            holder.tv_agendafg_lv_score = (TextView) convertView.findViewById(R.id.tv_agendafg_lv_score);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.tv_agendafg_lv_data.setText(mChildrenDataList.get(groupPosition).get(childPosition).getRDate());
        holder.tv_agendafg_lv_time.setText(mChildrenDataList.get(groupPosition).get(childPosition).getRTime());
        holder.tv_agendafg_lv_teamnamel.setText( mChildrenDataList.get(groupPosition).get(childPosition).getHomeName());
        holder.tv_agendafg_lv_teamnamer.setText( mChildrenDataList.get(groupPosition).get(childPosition).getGuestName());
        if( mChildrenDataList.get(groupPosition).get(childPosition).getMatchResult()!=null){
            holder.tv_agendafg_lv_score.setText(mChildrenDataList.get(groupPosition).get(childPosition).getMatchResult());
            holder.tv_agendafg_lv_score.setTextColor(mContext.getResources().getColor(R.color.pad_channel_listitem_title_flag));
        }else{

            holder.tv_agendafg_lv_score.setText("--");
          holder.tv_agendafg_lv_score.setTextColor(mContext.getResources().getColor(R.color.calendar_header));

        }


        return convertView;
    }

    public class Holder{
     TextView tv_agendafg_lv_data;//日期
     TextView tv_agendafg_lv_time;//时间
     TextView tv_agendafg_lv_teamnamel;//主场队名
     TextView tv_agendafg_lv_teamnamer;//客场队名
     TextView tv_agendafg_lv_score;//比分


    }
    private View createChildrenView() {
        return inflater.inflate(R.layout.agendafragment_lv_item, null);
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
     * 设置置顶的  item数据
     */
    @Override
    public void configureHeader(View header, int groupPosition,
                                int childPosition, int alpha) {

        String groupData = this.mGroupDataList.get(groupPosition).toString();
        ((TextView) header.findViewById(R.id.tv_agendafg_lv_gropname)).setText(groupData);

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
}
