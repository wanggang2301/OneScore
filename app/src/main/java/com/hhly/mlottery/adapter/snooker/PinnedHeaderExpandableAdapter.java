package com.hhly.mlottery.adapter.snooker;

import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.snookerbean.SnookerRaceListitemBean;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;
import com.hhly.mlottery.view.GrapeGridview;
import com.hhly.mlottery.view.SnookerPinnedHeaderExpandableListView;

import java.util.List;

public class PinnedHeaderExpandableAdapter extends BaseExpandableListAdapter implements SnookerPinnedHeaderExpandableListView.HeaderAdapter {
    private List<SnookerRaceListitemBean.DataBean.MatchListBean> mGroupDataList;//父类view 数据
    List<List<String>> mChildrenDataList;//子view数据
    private Context mContext;
    private SnookerPinnedHeaderExpandableListView listView;
    private LayoutInflater inflater;
    List<SnookerRaceListitemBean.DataBean.MatchListBean> matchList;
    //比赛的状态
    private String state;
    private int mMatchKind;
    private ChoseHeadInformationItemAdapter choseHeadInformationAdapter;
    private ImageView iconfont;
    private TextView live_item_day_tx;


    public PinnedHeaderExpandableAdapter(List<SnookerRaceListitemBean.DataBean.MatchListBean> matchList, List<List<String>> childDataList, Context mContext, SnookerPinnedHeaderExpandableListView explistview_live) {
        this.mChildrenDataList = childDataList;
        this.mContext = mContext;
        this.mGroupDataList = matchList;
        this.listView = explistview_live;
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
     * <p>
     * 由此引伸出，如果该对象不能被转换并显示正确的数据，这个方法就会调用getChildView(int, int, boolean, View, ViewGroup)来创建一个视图(View)对象。
     *
     * @param parent 指定位置上的子元素返回的视图对象
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        L.i("groupPosition", "groupPosition===" + groupPosition + "");
        L.i("groupPosition", "childPosition===" + childPosition + "");

        Holder holder = null;
        if (null == convertView) {
            convertView = createChildrenView();
            holder = new Holder();


            holder.gridview = (GrapeGridview) convertView.findViewById(R.id.male_gridview);


            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Log.i("dasd", "datas.size()=====" + mChildrenDataList.get(groupPosition));
        choseHeadInformationAdapter = new ChoseHeadInformationItemAdapter(mContext, mChildrenDataList.get(groupPosition), R.layout.snooker_race_child_item_tv);

        holder.gridview.setAdapter(choseHeadInformationAdapter);

        return convertView;
    }

    public class Holder {
        GrapeGridview gridview;
    }

    /**
     * 获取指定组中的子元素个数
     *
     * @param groupPosition 组位置（决定返回哪个组的子元素个数）
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
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
        RelativeLayout parentLayout = (RelativeLayout) View.inflate(mContext, R.layout.snooker_race_group, null);
        //日期
        live_item_day_tx = (TextView) parentLayout.findViewById(R.id.live_item_day_txt);
        //左参赛人员
        TextView snooker_left_name = (TextView) parentLayout.findViewById(R.id.snooker_left_name);
        //有参赛人员
        TextView snooker_right_name = (TextView) parentLayout.findViewById(R.id.snooker_right_name);
        //比赛详情
        TextView snooker_race_total_score = (TextView) parentLayout.findViewById(R.id.snooker_race_total_score);
        //下拉
        iconfont = (ImageView) parentLayout.findViewById(R.id.iconfont);
        live_item_day_tx.setText(mGroupDataList.get(groupPosition).getMatchDate());
        snooker_left_name.setText(mGroupDataList.get(groupPosition).getPlayerNameAcn());
        snooker_right_name.setText(mGroupDataList.get(groupPosition).getPlayerNameBcn());
        snooker_race_total_score.setText(mGroupDataList.get(groupPosition).getScore());
        if (isExpanded) {
            iconfont.setImageResource(R.mipmap.iconfont_xiala_2);
        } else {
            iconfont.setImageResource(R.mipmap.iconfont_xiala_1);
        }
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
        return inflater.inflate(R.layout.snokker_race_child_item, null);
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
       /* SnookerRaceListitemBean.DataBean.MatchListBean groupData = this.mGroupDataList.get(groupPosition);
        String  date =ResultDateUtil.getWeekOfDate(DateUtil.parseDate(ResultDateUtil.getDate(0,mGroupDataList.get(groupPosition))));
        String day=DateUtil.getTodayorTomorrow(mGroupDataList.get(groupPosition));
        ((TextView) header.findViewById(R.id.live_item_day_txt)).setText(groupData);
        ((TextView) header.findViewById(R.id.video_week)).setText(date);
        ((TextView) header.findViewById(R.id.video_day)).setText(day);*/
    }

    private SparseIntArray groupStatusMap = new SparseIntArray();

    //    @Override
//    暂时注释，需要点击头部的时候打开
    public void setGroupClickStatus(int groupPosition, int status) {

        groupStatusMap.put(groupPosition, status);
    }

    @Override
    public int getGroupClickStatus(int groupPosition) {
        if (groupStatusMap.keyAt(groupPosition) >= 0) {
            return groupStatusMap.get(groupPosition);
        } else {
            return 0;
        }
    }

    public class ChoseHeadInformationItemAdapter extends CommonAdapter<String> {


        List<String> datas;
        Context mContext;
        private TextView textView;
        private TextView head_item;

        public ChoseHeadInformationItemAdapter(Context context, List<String> datas, int layoutId) {
            super(context, datas, layoutId);
            this.mContext = context;
            this.datas = datas;

        }


        @Override
        public void newconvert(ViewHolder holder, String s, int position) {
            textView = holder.getView(R.id.item1);
            head_item = holder.getView(R.id.head_item);
            head_item.setText(position + 1 + "");
            textView.setText(s);
        }

        @Override
        public void convert(ViewHolder holder, String datas) {


        }
    }

}
