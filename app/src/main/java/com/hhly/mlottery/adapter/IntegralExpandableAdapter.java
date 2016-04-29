package com.hhly.mlottery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballsecond.IntegralBean;
import com.hhly.mlottery.widget.PinnedHeaderExpandableListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @ClassName: OneScore
 * @author:Administrator luyao
 * @Description:
 * @data: 2016/4/8 9:22
 */
public class IntegralExpandableAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.HeaderAdapter, ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {
    private List<String> mGroupDataList;
    private List<List<IntegralBean.LangueScoreBean.ListBean>> mChildrenDataList;
    private Context mContext;
    private PinnedHeaderExpandableListView listView;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private LinearLayout mIntegral;
    String leagueType;//联赛类型
    private List<IntegralBean.LangueScoreBean.ListBean> datas;

    public IntegralExpandableAdapter(List<List<IntegralBean.LangueScoreBean.ListBean>> childrenDataList, List<String> groupDataList
            , Context context, PinnedHeaderExpandableListView listView, String leagueType) {

        this.mGroupDataList = groupDataList;
        this.mChildrenDataList = childrenDataList;
        this.listView = listView;
        this.mContext = context;
        this.leagueType=leagueType;
        inflater = LayoutInflater.from(mContext);
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
        return getCombinedChildId(groupPosition, childPosition);
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
        // mIntegral_ranking = holder.integral_ranking;
        if (null == convertView) {
            convertView = createChildrenView();
            holder = new Holder();
            //名次
            holder.integral_ranking = (TextView) convertView.findViewById(R.id.football_integral_tv);
            //球队名称
            holder.integral_team_name = (TextView) convertView.findViewById(R.id.integral_team_name);
            //队徽
            holder.integral_team_imag = (ImageView) convertView.findViewById(R.id.integral_team_imag);
            //场次
            holder.integral_round = (TextView) convertView.findViewById(R.id.integral_round);
            //胜
            holder.integral_win = (TextView) convertView.findViewById(R.id.integral_win);
            //平
            holder.integral_equ = (TextView) convertView.findViewById(R.id.integral_equ);
            //负
            holder.integral_fail = (TextView) convertView.findViewById(R.id.integral_fail);
            //得
            holder.integral_goal = (TextView) convertView.findViewById(R.id.integral_goal);
            //失
            holder.integral_loss = (TextView) convertView.findViewById(R.id.integral_loss);
            //净胜
            holder.integral_abs = (TextView) convertView.findViewById(R.id.integral_abs);
            //得分
            holder.integral_score = (TextView) convertView.findViewById(R.id.integral_score);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //主队图片

        String mPreurl = "http://pic.13322.com/icons/teams/100/";
        String mFix = ".png";
        String home_images = mPreurl + mChildrenDataList.get(groupPosition).get(childPosition).getTid() + mFix;

        //名次
        holder.integral_ranking.setText((childPosition + 1) + "");
        if ("2".equals(leagueType)) {
            System.out.print("leagueType============="+leagueType);
            if (childPosition < 2) {
                holder.integral_ranking.setTextColor(mContext.getResources().getColor(R.color.foot_integal_se));
            } else {
                holder.integral_ranking.setTextColor(mContext.getResources().getColor(R.color.live_text1));
            }
        } else {
            if (childPosition < 3) {

                holder.integral_ranking.setTextColor(mContext.getResources().getColor(R.color.foot_integal_se));
            } else {

                holder.integral_ranking.setTextColor(mContext.getResources().getColor(R.color.live_text1));

            }
        }

        //队伍名称
        holder.integral_team_name.setText(mChildrenDataList.get(groupPosition).get(childPosition).getName());
        //队徽
        ImageLoader.getInstance().displayImage(home_images, holder.integral_team_imag, options);
        //比赛场次
        holder.integral_round.setText(mChildrenDataList.get(groupPosition).get(childPosition).getRound() + "");
        //胜
        holder.integral_win.setText(mChildrenDataList.get(groupPosition).get(childPosition).getWin() + "");
        //平
        holder.integral_equ.setText(mChildrenDataList.get(groupPosition).get(childPosition).getEqu() + "");
        //负
        holder.integral_fail.setText(mChildrenDataList.get(groupPosition).get(childPosition).getFail() + "");
        //得
        holder.integral_goal.setText(mChildrenDataList.get(groupPosition).get(childPosition).getGoal() + "");
        //失
        holder.integral_loss.setText(mChildrenDataList.get(groupPosition).get(childPosition).getLoss() + "");
        //净球
        holder.integral_abs.setText(mChildrenDataList.get(groupPosition).get(childPosition).getAbs() + "");
        //得分
        holder.integral_score.setText(mChildrenDataList.get(groupPosition).get(childPosition).getScore() + "");
        holder.integral_score.getPaint().setFakeBoldText(true);

        return convertView;
    }

    //不可点击
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return true;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
    }

/*
    public void updateDatas(List<List<IntegralBean.LangueScoreBean.ListBean>> childrenDataList, List<String> groupDataList) {
        this.mChildrenDataList=childrenDataList;
        this.mGroupDataList=groupDataList;

    }
*/

    public class Holder {
        TextView integral_ranking; //排名
        //球队名称
        TextView integral_team_name;
        //队徽
        ImageView integral_team_imag;
        //场次
        TextView integral_round;
        //胜
        TextView integral_win;
        //平
        TextView integral_equ;
        //负
        TextView integral_fail;
        //得
        TextView integral_goal;
        //失
        TextView integral_loss;
        //净胜
        TextView integral_abs;
        //得分
        TextView integral_score;


    }

    /**
     * 获取指定组中的子元素个数
     *
     * @param groupPosition 组位置（决定返回哪个组的子元素个数）
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition < 0) {
            return 0;
        }
        return mChildrenDataList.get(groupPosition) == null ? 0 : mChildrenDataList.get(groupPosition).size();
        //return mChildrenDataList.get(groupPosition).size();
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
        LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.integral_items_group, null);
        TextView live_item_day_tx = (TextView) view.findViewById(R.id.integral_grouping);
        live_item_day_tx.setText(mGroupDataList.get(groupPosition));

        if ("empty".equals(mGroupDataList.get(groupPosition)) || mGroupDataList.get(groupPosition) == null) {
            live_item_day_tx.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            live_item_day_tx.setText(mGroupDataList.get(groupPosition));
        }


        return view;
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
        return inflater.inflate(R.layout.integral_item_child, null);
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
