package com.hhly.mlottery.adapter.myfocus;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.view.MyFocusPinnedHeaderExpandableListView;

import java.util.List;

import data.bean.myfocus.FocusBean;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/6/23  14:21.
 */


public class MyFocusPinnedHeaderExpandableAdapter extends BaseExpandableListAdapter implements MyFocusPinnedHeaderExpandableListView.HeaderAdapter {
    /* private String[][] childrenData;
     private String[] groupData;
 */
    private List<FocusBean> data;

    private Context context;
    private MyFocusPinnedHeaderExpandableListView listView;
    private LayoutInflater inflater;

    private IDeleteMyFocus iDeleteMyFocus;

    public void setiDeleteMyFocus(IDeleteMyFocus iDeleteMyFocus) {
        this.iDeleteMyFocus = iDeleteMyFocus;
    }

    public MyFocusPinnedHeaderExpandableAdapter(List<FocusBean> list, Context context, MyFocusPinnedHeaderExpandableListView listView) {

        this.data = list;
        this.context = context;
        this.listView = listView;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = createChildrenView();
        }
        TextView text = (TextView) view.findViewById(R.id.childto);
        final ImageView ivchildStar = (ImageView) view.findViewById(R.id.childStar);
        if (data.get(groupPosition).getList().get(childPosition).isSelected()) {
            ivchildStar.setImageResource(R.mipmap.attention_star);
        } else {
            ivchildStar.setImageResource(R.mipmap.attention_star2);
        }

        ivchildStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iDeleteMyFocus != null) {
                    iDeleteMyFocus.deleteMyFocusChild(groupPosition, childPosition);
                }


             /*   data.get(groupPosition).getList().remove(childPosition);
                notifyDataSetChanged();
*/
            }
        });

        text.setText(data.get(groupPosition).getList().get(childPosition).getName());
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = createGroupView();
        }

        ImageView iv = (ImageView) view.findViewById(R.id.groupIncome);


        if (isExpanded) {
            iv.setImageResource(R.mipmap.iconfont_xiala_2);
        } else {
            iv.setImageResource(R.mipmap.iconfont_xiala_1);
        }

        final ImageView ivGroupStar = (ImageView) view.findViewById(R.id.groupStar);

        if (data.get(groupPosition).isSelected()) {
            ivGroupStar.setImageResource(R.mipmap.attention_star);
        } else {
            ivGroupStar.setImageResource(R.mipmap.attention_star2);
        }


        ivGroupStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iDeleteMyFocus != null) {
                    iDeleteMyFocus.deleteMyFocusGroup(groupPosition);
                }
/*

                data.remove(groupPosition);

                L.d("myfocus", "ivGroupStar");


                notifyDataSetChanged();
*/


            }
        });


        TextView text = (TextView) view.findViewById(R.id.groupto);
        text.setText(data.get(groupPosition).getName());
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private View createChildrenView() {
        return inflater.inflate(R.layout.item_myfocus_child, null);
    }

    private View createGroupView() {
        return inflater.inflate(R.layout.item_myfocus_group, null);
    }

    @Override
    public int getHeaderState(int groupPosition, int childPosition) {
        final int childCount = getChildrenCount(groupPosition);
        if (childPosition == childCount - 1) {
            return PINNED_HEADER_GONE;
        } else if (childPosition == -1
                && !listView.isGroupExpanded(groupPosition)) {
            return PINNED_HEADER_GONE;
        } else {
            return PINNED_HEADER_GONE;
        }
    }

    @Override
    public void configureHeader(View header, final int groupPosition,
                                int childPosition, int alpha) {
        // String groupData = this.groupData[groupPosition];
        ((TextView) header.findViewById(R.id.groupto)).setText(data.get(groupPosition).getName());


        final ImageView ivGroupStar = (ImageView) header.findViewById(R.id.groupStar);

        if (data.get(groupPosition).isSelected()) {
            ivGroupStar.setImageResource(R.mipmap.attention_star);
        } else {
            ivGroupStar.setImageResource(R.mipmap.attention_star2);
        }

    }

    private SparseIntArray groupStatusMap = new SparseIntArray();

    @Override
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
}
