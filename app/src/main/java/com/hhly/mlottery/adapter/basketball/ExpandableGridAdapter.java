package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketballDatabaseDetailsActivity;
import com.hhly.mlottery.bean.basket.infomation.B;
import com.hhly.mlottery.bean.basket.infomation.Be;
import com.hhly.mlottery.callback.BasketInfomationCallBack;
import com.hhly.mlottery.widget.MyGridViewInfo;

import java.util.ArrayList;
import java.util.List;

public class ExpandableGridAdapter extends BaseExpandableListAdapter implements AdapterView.OnItemClickListener {

    private static final int NUM0 = 0;
    private static final int NUM1 = 1;
    private static final int NUM2 = 2;
    private static final int NUM3 = 3;
    private int parentItem = 0;
    private int groPosition = -1;
    public boolean isInitChildAdapter = false;

    public BasketInfomationCallBack basketInfomationCallBack;

    private BasketInfoGridChildAdapter basketInfoGridChildAdapter;
    private Context mContext;
    private MyGridViewInfo gridview;

    private ImageView lastCheckedOption;

    private List<B> child_array;
    private List<List<Be>> allDatas;

    public void setBasketInfomationCallBack(BasketInfomationCallBack basketInfomationCallBack) {
        this.basketInfomationCallBack = basketInfomationCallBack;
    }

    public ExpandableGridAdapter(Context context, List<List<Be>> lists) {
        this.mContext = context;
        // this.list = list;
        //this.child_text_array = child_text_array;
        this.allDatas = lists;
    }

    /**
     * 获取一级标签总数
     */
    @Override
    public int getGroupCount() {
        return allDatas.size();
    }

    /**
     * 获取一级标签下二级标签的总数
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        // 这里返回1是为了让ExpandableListView只显示一个ChildView，否则在展开
        // ExpandableListView时会显示和ChildCount数量相同的GridView
        return 1;
    }

    /**
     * 获取一级标签内容
     */
    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    /**
     * 获取一级标签下二级标签的内容
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    /**
     * 获取一级标签的ID
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 获取二级标签的ID
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 指定位置相应的组视图
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 对一级标签进行设置
     */
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {

        final ListViewHolder listViewHolder;

        if (convertView == null) {
            listViewHolder = new ListViewHolder();
            convertView = (LinearLayout) LinearLayout.inflate(mContext, R.layout.basket_info_country_item, null);

            listViewHolder.rl0 = (RelativeLayout) convertView.findViewById(R.id.rl0);
            listViewHolder.rl1 = (RelativeLayout) convertView.findViewById(R.id.rl1);
            listViewHolder.rl2 = (RelativeLayout) convertView.findViewById(R.id.rl2);
            listViewHolder.rl3 = (RelativeLayout) convertView.findViewById(R.id.rl3);

            listViewHolder.icon0 = (ImageView) convertView.findViewById(R.id.iv_icon0);
            listViewHolder.name0 = (TextView) convertView.findViewById(R.id.tv_name0);
            listViewHolder.iv0 = (ImageView) convertView.findViewById(R.id.iv0);


            listViewHolder.icon1 = (ImageView) convertView.findViewById(R.id.iv_icon1);
            listViewHolder.name1 = (TextView) convertView.findViewById(R.id.tv_name1);
            listViewHolder.iv1 = (ImageView) convertView.findViewById(R.id.iv1);


            listViewHolder.icon2 = (ImageView) convertView.findViewById(R.id.iv_icon2);
            listViewHolder.name2 = (TextView) convertView.findViewById(R.id.tv_name2);
            listViewHolder.iv2 = (ImageView) convertView.findViewById(R.id.iv2);


            listViewHolder.icon3 = (ImageView) convertView.findViewById(R.id.iv_icon3);
            listViewHolder.name3 = (TextView) convertView.findViewById(R.id.tv_name3);
            listViewHolder.iv3 = (ImageView) convertView.findViewById(R.id.iv3);

            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ListViewHolder) convertView.getTag();
        }


        if (allDatas.get(groupPosition).size() > NUM0) {
            listViewHolder.icon0.setImageResource(R.mipmap.basket_default);
            listViewHolder.name0.setText(allDatas.get(groupPosition).get(0).getB().getName().toString());
            listViewHolder.rl0.setEnabled(true);

        } else {
            listViewHolder.rl0.setEnabled(false);

        }

        if (allDatas.get(groupPosition).size() > NUM1) {
            listViewHolder.icon1.setImageResource(R.mipmap.basket_default);
            listViewHolder.name1.setText(allDatas.get(groupPosition).get(1).getB().getName().toString());
            listViewHolder.rl1.setEnabled(true);

        } else {
            listViewHolder.rl1.setEnabled(false);

        }

        if (allDatas.get(groupPosition).size() > NUM2) {
            listViewHolder.icon2.setImageResource(R.mipmap.basket_default);
            listViewHolder.name2.setText(allDatas.get(groupPosition).get(2).getB().getName().toString());
            listViewHolder.rl2.setEnabled(true);

        } else {
            listViewHolder.rl2.setEnabled(false);

        }

        if (allDatas.get(groupPosition).size() > NUM3) {
            listViewHolder.icon3.setImageResource(R.mipmap.basket_default);
            listViewHolder.name3.setText(allDatas.get(groupPosition).get(3).getB().getName().toString());
            listViewHolder.rl3.setEnabled(true);
        } else {
            listViewHolder.rl3.setEnabled(false);
        }


        listViewHolder.rl0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (basketInfomationCallBack != null) {

                    parentItem = NUM0;

                    if (groPosition == -1) {
                        groPosition = groupPosition;
                    } else {
                        if (groPosition != groupPosition) {
                            isInitChildAdapter = false;
                            groPosition = groupPosition;
                        }
                    }


                    if (isInitChildAdapter) {
                        updateAdapter(groupPosition, NUM0);
                    }

                    if(lastCheckedOption != null){
                        lastCheckedOption.setVisibility(View.INVISIBLE);
                    }
                    lastCheckedOption = listViewHolder.iv0;
                    lastCheckedOption.setVisibility(View.VISIBLE);

                   /* if (listViewHolder.iv0.getVisibility()==View.VISIBLE){
                        listViewHolder.iv0.setVisibility(View.INVISIBLE);
                    }else {
                        listViewHolder.iv0.setVisibility(View.VISIBLE);
                        listViewHolder.iv1.setVisibility(View.INVISIBLE);
                        listViewHolder.iv2.setVisibility(View.INVISIBLE);
                        listViewHolder.iv3.setVisibility(View.INVISIBLE);

                    }*/

                    basketInfomationCallBack.onClick(v, groupPosition, NUM0);
                }

            }
        });
        listViewHolder.rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (basketInfomationCallBack != null) {
                    parentItem = NUM1;
                    if (groPosition == -1) {
                        groPosition = groupPosition;
                    } else {
                        if (groPosition != groupPosition) {
                            isInitChildAdapter = false;
                            groPosition = groupPosition;
                        }
                    }

                    if (isInitChildAdapter) {
                        updateAdapter(groupPosition, NUM1);
                    }
                    // Log.d("112", "ff=" + groupPosition);
                   /* if (listViewHolder.iv1.getVisibility()==View.VISIBLE){
                        listViewHolder.iv1.setVisibility(View.INVISIBLE);
                    }else {
                        listViewHolder.iv1.setVisibility(View.VISIBLE);
                        listViewHolder.iv0.setVisibility(View.INVISIBLE);
                        listViewHolder.iv2.setVisibility(View.INVISIBLE);
                        listViewHolder.iv3.setVisibility(View.INVISIBLE);

                    }*/
                    if(lastCheckedOption != null){
                        lastCheckedOption.setVisibility(View.INVISIBLE);
                    }
                    lastCheckedOption = listViewHolder.iv1;
                    lastCheckedOption.setVisibility(View.VISIBLE);
                    basketInfomationCallBack.onClick(v, groupPosition, NUM1);
                }

            }
        });
        listViewHolder.rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (basketInfomationCallBack != null) {
                    parentItem = NUM2;
                    if (groPosition == -1) {
                        groPosition = groupPosition;
                    } else {
                        if (groPosition != groupPosition) {
                            isInitChildAdapter = false;
                            groPosition = groupPosition;
                        }
                    }

                    //Log.d("112", isInitChildAdapter + "");

                    if (isInitChildAdapter) {
                        updateAdapter(groupPosition, NUM2);
                    }
                    if(lastCheckedOption != null){
                        lastCheckedOption.setVisibility(View.INVISIBLE);
                    }
                    lastCheckedOption = listViewHolder.iv2;
                    lastCheckedOption.setVisibility(View.VISIBLE);
                    basketInfomationCallBack.onClick(v, groupPosition, NUM2);
                }

            }
        });
        listViewHolder.rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (basketInfomationCallBack != null) {
                    parentItem = NUM3;

                    if (groPosition == -1) {
                        groPosition = groupPosition;
                    } else {
                        if (groPosition != groupPosition) {
                            isInitChildAdapter = false;
                            groPosition = groupPosition;
                        }
                    }
                    // Log.d("112", isInitChildAdapter + "");

                    if (isInitChildAdapter) {
                        updateAdapter(groupPosition, NUM3);
                    }
                    if(lastCheckedOption != null){
                        lastCheckedOption.setVisibility(View.INVISIBLE);
                    }
                    lastCheckedOption = listViewHolder.iv3;
                    lastCheckedOption.setVisibility(View.VISIBLE);
                    basketInfomationCallBack.onClick(v, groupPosition, NUM3);
                }

            }
        });


        return convertView;
    }

    private void updateAdapter(int groupPostion, int childPostion) {
        child_array.clear();
        for (B b : allDatas.get(groupPostion).get(childPostion).getList()) {
            child_array.add(b);
        }
        basketInfoGridChildAdapter.notifyDataSetChanged();
    }


    /**
     * 对一级标签下的二级标签进行设置
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.basket_info_country_item_child, null);
        gridview = (MyGridViewInfo) convertView.findViewById(R.id.gridview_child);

       // Log.d("112", "getchilview" + "gro=" + groupPosition + "item=" + parentItem);
        child_array = new ArrayList<>();

        for (B b : allDatas.get(groupPosition).get(parentItem).getList()) {
            child_array.add(b);
        }

       // Log.d("112", child_array.size() + "----" + allDatas.get(groupPosition).get(parentItem).getList().size());
        basketInfoGridChildAdapter = new BasketInfoGridChildAdapter(mContext, child_array);
        gridview.setAdapter(basketInfoGridChildAdapter);
        gridview.setOnItemClickListener(this);

        isInitChildAdapter = true;
        return convertView;
    }

    /**
     * 当选择子节点的时候，调用该方法
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, BasketballDatabaseDetailsActivity.class);

        mContext.startActivity(intent);
//        Toast.makeText(mContext, "当前选中的是:" + groPosition + "---" + position, Toast.LENGTH_SHORT).show();*/
    }
    /**
     * 数据ViewHolder
     */

    private static class ListViewHolder {
        ImageView icon0;
        TextView name0;
        ImageView iv0;

        ImageView icon1;
        TextView name1;
        ImageView iv1;


        ImageView icon2;
        TextView name2;
        ImageView iv2;


        ImageView icon3;
        TextView name3;
        ImageView iv3;


        RelativeLayout rl0;
        RelativeLayout rl1;
        RelativeLayout rl2;
        RelativeLayout rl3;

    }
}