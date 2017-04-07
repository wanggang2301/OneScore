package com.hhly.mlottery.adapter.videolive;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.videobean.NewMatchVideoinfo;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.ResultDateUtil;
import com.hhly.mlottery.widget.PinnedHeaderExpandableListView;

import java.util.List;

/**
 * Created by yuely198 on 2017/3/21.
 */

public class NewPinnedHeaderExpandableAdapter  extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.HeaderAdapter {
    private String mPreurl;//前缀
    private String mFix;//后缀
    private List<String> mGroupDataList;//父类view 数据
    List<List<NewMatchVideoinfo.MatchVideoBean.SptVideoMoreInfoDtoListBean>> mChildrenDataList;//子view数据
    private Context mContext;
    private PinnedHeaderExpandableListView listView;
    private LayoutInflater inflater;
    //比赛的状态
    private String state;
    private int mMatchKind;

    public NewPinnedHeaderExpandableAdapter( List<List<NewMatchVideoinfo.MatchVideoBean.SptVideoMoreInfoDtoListBean>> childrenDataList, List<String> groupDataList
            , Context mContext, PinnedHeaderExpandableListView listView, String mPreurl, String mFix) {
        this.mGroupDataList = groupDataList;
        this.mChildrenDataList = childrenDataList;
        this.mContext = mContext;
        this.listView = listView;
        this.mPreurl = mPreurl;
        this.mFix = mFix;
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
     *                      由此引伸出，如果该对象不能被转换并显示正确的数据，这个方法就会调用getChildView(int, int, boolean, View, ViewGroup)来创建一个视图(View)对象。
     * @param parent        指定位置上的子元素返回的视图对象
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        NewPinnedHeaderExpandableAdapter.Holder holder = null;
        if (null == convertView) {
            convertView = createChildrenView();
            holder = new NewPinnedHeaderExpandableAdapter.Holder();
            //主队
            holder.live_left_child_txt = (TextView) convertView.findViewById(R.id.live_left_child_txt);
            //客队
            holder.live_left_bottom_child_txt = (TextView) convertView.findViewById(R.id.live_left_bottom_child_txt);
            //视频直播开始时间
            holder.live_right_time_txt = (TextView) convertView.findViewById(R.id.live_right_time_txt);

            holder.direct_seeding_photo= (ImageView) convertView.findViewById(R.id.direct_seeding_photo);
            //主队icon
            holder.live_home_icon_img = (ImageView) convertView.findViewById(R.id.live_home_icon_img);
            //客队icon
            holder.live_guest_icon_img = (ImageView) convertView.findViewById(R.id.live_guest_icon_img);
            //西甲，英超
            holder.live_item_child_txt = (TextView) convertView.findViewById(R.id.live_item_child_txt);
            holder.tv_icon_time = (ImageView) convertView.findViewById(R.id.tv_icon_time);


            //综合
            holder.tv_text_on = (TextView) convertView.findViewById(R.id.tv_text_on);
            //
            holder.re_duizhan_noon= (LinearLayout) convertView.findViewById(R.id.re_duizhan_noon);
            holder.re_duizhan= (LinearLayout) convertView.findViewById(R.id.re_duizhan);
            holder.live_home_icon_img1= (ImageView) convertView.findViewById(R.id.live_duizhan_icon_img);
            convertView.setTag(holder);
        } else {
            holder = (NewPinnedHeaderExpandableAdapter.Holder) convertView.getTag();
        }
        //如果是直播中"1 2 3 4 5"都属于直播中
        state = mChildrenDataList.get(groupPosition).get(childPosition).statusOrigin;
        //联赛类型
        mMatchKind = mChildrenDataList.get(groupPosition).get(childPosition).matchKind;

        //主队图片
        String home_images = mPreurl + mChildrenDataList.get(groupPosition).get(childPosition).hmId + mFix;
        //客队图片
        String guest_images = mPreurl + mChildrenDataList.get(groupPosition).get(childPosition).awId + mFix;

        if(mChildrenDataList.get(groupPosition).get(childPosition).liveAndBFZ == 2){
            //非对战未直播
            holder.re_duizhan_noon.setVisibility(View.VISIBLE);
            holder.re_duizhan.setVisibility(View.GONE);
            holder.tv_icon_time.setVisibility(View.VISIBLE);
            //综合icon
            ImageLoader.load(mContext,mChildrenDataList.get(groupPosition).get(childPosition).zonghePic,R.mipmap.home_score_item_icon_def).into(holder.live_home_icon_img1);
            holder.tv_text_on.setText(mChildrenDataList.get(groupPosition).get(childPosition).zongheName);

            /***********************************/
            //主队icon
            holder.direct_seeding_photo.setImageResource(R.mipmap.tv_icon_un);
            holder.live_item_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).lgName);
            holder.live_right_time_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).matchTime);
            holder.live_right_time_txt.setTextColor(mContext.getResources().getColor(R.color.res_name_color));
        }
        else if(mChildrenDataList.get(groupPosition).get(childPosition).liveAndBFZ == 4){
            //非对战直播
            holder.re_duizhan_noon.setVisibility(View.VISIBLE);
            holder.re_duizhan.setVisibility(View.GONE);
            holder.tv_icon_time.setVisibility(View.GONE);
            //综合con
            ImageLoader.load(mContext,mChildrenDataList.get(groupPosition).get(childPosition).zonghePic,R.mipmap.home_score_item_icon_def).into(holder.live_home_icon_img1);
            holder.tv_text_on.setText(mChildrenDataList.get(groupPosition).get(childPosition).zongheName);

            holder.direct_seeding_photo.setImageResource(R.mipmap.tv_icon_ing);
            holder.live_item_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).lgName);
            holder.live_right_time_txt.setText(mContext.getResources().getString(R.string.direct_seedinging));
            holder.live_right_time_txt.setTextColor(mContext.getResources().getColor(R.color.material_red));

        } else if(mChildrenDataList.get(groupPosition).get(childPosition).liveAndBFZ == 8){
            //对战 直播综合
            holder.re_duizhan_noon.setVisibility(View.GONE);
            holder.re_duizhan.setVisibility(View.VISIBLE);
            holder.tv_icon_time.setVisibility(View.VISIBLE);
            //主队篮足球
            //ImageLoader.getInstance().displayImage(mChildrenDataList.get(groupPosition).get(childPosition).homePic, holder.live_home_icon_img, options);
            ImageLoader.load(mContext, mChildrenDataList.get(groupPosition)
                    .get(childPosition).homePic,R.mipmap.home_score_item_icon_def).into( holder.live_home_icon_img);

            ImageLoader.load(mContext,mChildrenDataList.get(groupPosition)
                    .get(childPosition).guestPic,R.mipmap.home_score_item_icon_def).into( holder.live_guest_icon_img);
            //主队
            holder.live_left_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).hmName);
            // 客队
            holder.live_left_bottom_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).awName);

            holder.live_item_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).lgName);
            //显示直播中的布局
            holder.direct_seeding_photo.setImageResource(R.mipmap.tv_icon_un);
            holder.live_right_time_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).matchTime);
            holder.live_right_time_txt.setTextColor(mContext.getResources().getColor(R.color.res_name_color));

        }else if(mChildrenDataList.get(groupPosition).get(childPosition).liveAndBFZ == 6){
            //对战未直播
            holder.re_duizhan_noon.setVisibility(View.GONE);
            holder.re_duizhan.setVisibility(View.VISIBLE);
            holder.tv_icon_time.setVisibility(View.GONE);
            //主队
            ImageLoader.load(mContext,mChildrenDataList.get(groupPosition).get(childPosition).homePic,R.mipmap.home_score_item_icon_def).into(holder.live_home_icon_img);
            //客队icon
            ImageLoader.load(mContext,mChildrenDataList.get(groupPosition).get(childPosition).guestPic,R.mipmap.home_score_item_icon_def).into(holder.live_guest_icon_img);
            //主队
            holder.live_left_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).hmName);
            // 客队
            holder.live_left_bottom_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).awName);
            ////////////////////////
            holder.live_item_child_txt.setText(mChildrenDataList.get(groupPosition).get(childPosition).lgName);
            //显示直播中的布局

            holder.direct_seeding_photo.setImageResource(R.mipmap.tv_icon_ing);
            holder.live_right_time_txt.setText(mContext.getResources().getString(R.string.direct_seedinging));
            holder.live_right_time_txt.setTextColor(mContext.getResources().getColor(R.color.material_red));
        }

        return convertView;
    }

    public class Holder {

        TextView live_left_child_txt; //主队名称
        TextView live_left_bottom_child_txt; //客队名称

        TextView live_right_time_txt; //(视频暂未开始)开始时间

        // ImageView live_right_child_img; //直播或者未直播的图片
        ImageView live_home_icon_img; //主队图标
        ImageView live_guest_icon_img; //客队图标

        TextView live_item_child_txt; //西甲，英超
        ImageView direct_seeding_photo;//直播图片
        TextView tv_text_on;
        LinearLayout re_duizhan;
        LinearLayout re_duizhan_noon;
        ImageView live_home_icon_img1;
        ImageView tv_icon_time;
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
        RelativeLayout parentLayout = (RelativeLayout) View.inflate(mContext, R.layout.item_live_header, null);
        TextView live_item_day_tx = (TextView) parentLayout.findViewById(R.id.live_item_day_txt);

        TextView mVideo_week= (TextView) parentLayout.findViewById(R.id.video_week);
        //今天or明天
        TextView mVideo_day = (TextView) parentLayout.findViewById(R.id.video_day);
        live_item_day_tx.setText(DateUtil.convertDateToNation(mGroupDataList.get(groupPosition)));
        mVideo_week.setText(ResultDateUtil.getWeekOfDate(DateUtil.parseDate(ResultDateUtil.getDate(0,mGroupDataList.get(groupPosition)))));
        mVideo_day.setText(DateUtil.getTodayorTomorrow(mGroupDataList.get(groupPosition)));
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
        return inflater.inflate(R.layout.item_new_live_child, null);
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
        String groupData = this.mGroupDataList.get(groupPosition);
        String  date =ResultDateUtil.getWeekOfDate(DateUtil.parseDate(ResultDateUtil.getDate(0,mGroupDataList.get(groupPosition))));
        String day=DateUtil.getTodayorTomorrow(mGroupDataList.get(groupPosition));
        ((TextView) header.findViewById(R.id.live_item_day_txt)).setText(DateUtil.convertDateToNation(groupData));
        ((TextView) header.findViewById(R.id.video_week)).setText(date);
        ((TextView) header.findViewById(R.id.video_day)).setText(day);
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
