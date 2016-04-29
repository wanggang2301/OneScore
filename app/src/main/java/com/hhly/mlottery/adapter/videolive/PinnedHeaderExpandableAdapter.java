package com.hhly.mlottery.adapter.videolive;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.videobean.MatchVideoInfo;
import com.hhly.mlottery.widget.PinnedHeaderExpandableListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class PinnedHeaderExpandableAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.HeaderAdapter {
    private String mPreurl;//前缀
    private String mFix;//后缀
    private List<String> mGroupDataList;//父类view 数据
    List<List<MatchVideoInfo.MatchVideoEntity.SptVideoInfoDtoListEntity>> mChildrenDataList;//子view数据
    private Context mContext;
    private PinnedHeaderExpandableListView listView;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    //比赛的状态
    private String state;

    public PinnedHeaderExpandableAdapter(List<List<MatchVideoInfo.MatchVideoEntity.SptVideoInfoDtoListEntity>> childrenDataList, List<String> groupDataList
            , Context mContext, PinnedHeaderExpandableListView listView, String mPreurl, String mFix) {
        this.mGroupDataList = groupDataList;
        this.mChildrenDataList = childrenDataList;
        this.mContext = mContext;
        this.listView = listView;
        this.mPreurl = mPreurl;
        this.mFix = mFix;
        inflater = LayoutInflater.from(this.mContext);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.live_default).showImageOnFail(R.mipmap.live_default)
                .cacheInMemory(true).bitmapConfig(Bitmap.Config.ARGB_8888)
                .cacheOnDisc(true).considerExifParams(true).build();
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
            //主队
            holder.live_left_child_txt = (TextView) convertView.findViewById(R.id.live_left_child_txt);
            //客队
            holder.live_left_bottom_child_txt = (TextView) convertView.findViewById(R.id.live_left_bottom_child_txt);
            //视频直播开始时间
            holder.live_right_time_txt = (TextView) convertView.findViewById(R.id.live_right_time_txt);
            //暂未直播layout
            holder.live_right_child_layout = (LinearLayout) convertView.findViewById(R.id.live_right_child_layout);
            //直播中txt..
            holder.live_right_child_txt = (TextView) convertView.findViewById(R.id.live_right_child_txt);
            //直播或者未直播图
            holder.live_right_child_img = (ImageView) convertView.findViewById(R.id.live_right_child_img);
            //主队icon
            holder.live_home_icon_img = (ImageView) convertView.findViewById(R.id.live_home_icon_img);
            //客队icon
            holder.live_guest_icon_img = (ImageView) convertView.findViewById(R.id.live_guest_icon_img);
            //西甲，英超
            holder.live_item_child_txt = (TextView) convertView.findViewById(R.id.live_item_child_txt);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //如果是直播中"1 2 3 4 5"都属于直播中
        state = mChildrenDataList.get(groupPosition).get(childPosition).getStatusOrigin();
        //主队图片
        String home_images = mPreurl + mChildrenDataList.get(groupPosition).get(childPosition).getHmId() + mFix;
        //客队图片
        String guest_images = mPreurl + mChildrenDataList.get(groupPosition).get(childPosition).getAwId() + mFix;
        //如果没开赛
        if (mChildrenDataList.get(groupPosition).get(childPosition).getStatusOrigin().equals("0")) {
            //西甲，英超
            holder.live_item_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).getRacename());
            //主队icon
            ImageLoader.getInstance().displayImage(home_images, holder.live_home_icon_img, options);
            //客队icon
            ImageLoader.getInstance().displayImage(guest_images, holder.live_guest_icon_img, options);
            //主队
            holder.live_left_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).getHometeam());
            // 客队
            holder.live_left_bottom_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).getGuestteam());
            //显示开赛时间
            holder.live_right_time_txt.setText(" " + mChildrenDataList.get(groupPosition).get(childPosition).getMatchTime());
            //如果没开赛显示没开赛的图片
            holder.live_right_child_img.setBackground(mContext.getResources().getDrawable(R.mipmap.live_iconfont_default));
            //隐藏直播中的布局
            holder.live_right_child_txt.setVisibility(View.GONE);
            //显示没开赛布局
            holder.live_right_child_layout.setVisibility(View.VISIBLE);
        }
        //如果开赛了，直播中..
        else if ("1".equals(state) || "2".equals(state )|| "3".equals(state)|| "4".equals(state) || "5".equals(state)) {
            //西甲，英超
            holder.live_item_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).getRacename());
            //主队icon
            ImageLoader.getInstance().displayImage(home_images, holder.live_home_icon_img, options);
            //客队icon
            ImageLoader.getInstance().displayImage(guest_images, holder.live_guest_icon_img, options);
            //主队
            holder.live_left_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).getHometeam());
            // 客队
            holder.live_left_bottom_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).getGuestteam());
            //如果开赛显示开赛的图片
            holder.live_right_child_img.setBackground(mContext.getResources().getDrawable(R.mipmap.live_iconfont));
            //隐藏未开始布局
            holder.live_right_child_layout.setVisibility(View.GONE);
            //显示直播中的布局
            holder.live_right_child_txt.setVisibility(View.VISIBLE);
            holder.live_right_child_txt.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        //如果等于“-1”完场
       else if("-1".equals(state)){
         //不给他设 值 了
        }

        return convertView;
    }

    public class Holder {

        TextView live_left_child_txt; //主队名称
        TextView live_left_bottom_child_txt; //客队名称
        TextView live_right_child_txt; //视频直播中..

        LinearLayout live_right_child_layout; //视频暂未开始的layout
        TextView live_right_time_txt; //(视频暂未开始)开始时间

        ImageView live_right_child_img; //直播或者未直播的图片
        ImageView live_home_icon_img; //主队图标
        ImageView live_guest_icon_img; //客队图标

        TextView live_item_child_txt; //西甲，英超


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

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        LinearLayout parentLayout = (LinearLayout) View.inflate(mContext, R.layout.item_live_header, null);
        TextView live_item_day_tx = (TextView) parentLayout.findViewById(R.id.live_item_day_txt);
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
        return inflater.inflate(R.layout.item_live_child, null);
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
        ((TextView) header.findViewById(R.id.live_item_day_txt)).setText(groupData);

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
