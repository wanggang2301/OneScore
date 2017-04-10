package com.hhly.mlottery.adapter.football;

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

import com.bumptech.glide.Glide;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballDatabaseDetailsActivity;
import com.hhly.mlottery.bean.footballDetails.database.DataBaseBean;
import com.hhly.mlottery.bean.footballDetails.database.NationBean;
import com.hhly.mlottery.callback.BasketInfomationCallBack;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.MyGridViewInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:  ${}
 * 作者:  wangg@13322.com
 * 时间:  2016/9/2 17:38
 */
public class FootBallExpandableGridAdapter extends BaseExpandableListAdapter implements AdapterView.OnItemClickListener {
    private static final int NUM0 = 0;
    private static final int NUM1 = 1;
    private static final int NUM2 = 2;
    private static final int NUM3 = 3;
    private int parentItem = 0;
    private int groPosition = -1;
    public boolean isInitChildAdapter = false;

    private static final String LEAGUE = "league";

    public BasketInfomationCallBack basketInfomationCallBack;

    private FootBallGridChildAdapter footBallGridChildAdapter;
    private Context mContext;
    private MyGridViewInfo gridview;

    private int lastParentPostion = -1;
    private int lastChildPosition = -1;

    private List<DataBaseBean> child_array;
    private List<List<NationBean>> allDatas;

    public void setBasketInfomationCallBack(BasketInfomationCallBack basketInfomationCallBack) {
        this.basketInfomationCallBack = basketInfomationCallBack;
    }

    public FootBallExpandableGridAdapter(Context context, List<List<NationBean>> lists) {
        this.mContext = context;

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
            convertView = LinearLayout.inflate(mContext, R.layout.basket_info_country_item, null);

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
            if (allDatas.get(groupPosition).get(0).getPic() == null || "".equals(allDatas.get(groupPosition).get(0).getPic())) {
                listViewHolder.icon0.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.basket_info_default));
            } else {
                if(mContext!=null){
                    ImageLoader.load(mContext,allDatas.get(groupPosition).get(0).getPic(),R.mipmap.basket_info_default).into(listViewHolder.icon0);
                }

            }

            listViewHolder.name0.setText(allDatas.get(groupPosition).get(0).getName().toString());
            listViewHolder.rl0.setEnabled(true);

            if (allDatas.get(groupPosition).get(0).isShow()) {
                listViewHolder.iv0.setVisibility(View.VISIBLE);
            } else {
                listViewHolder.iv0.setVisibility(View.INVISIBLE);
            }

        } else {
            listViewHolder.rl0.setEnabled(false);
            listViewHolder.iv0.setVisibility(View.INVISIBLE);

        }

        if (allDatas.get(groupPosition).size() > NUM1) {
            if (allDatas.get(groupPosition).get(1).getPic() == null || "".equals(allDatas.get(groupPosition).get(1).getPic())) {
                listViewHolder.icon1.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.basket_info_default));
            } else {
                ImageLoader.load(mContext,allDatas.get(groupPosition).get(1).getPic(),R.mipmap.basket_info_default).into(listViewHolder.icon1);
            }

            listViewHolder.name1.setText(allDatas.get(groupPosition).get(1).getName().toString());
            listViewHolder.rl1.setEnabled(true);
            if (allDatas.get(groupPosition).get(1).isShow()) {
                listViewHolder.iv1.setVisibility(View.VISIBLE);
            } else {
                listViewHolder.iv1.setVisibility(View.INVISIBLE);
            }


        } else {
            listViewHolder.rl1.setEnabled(false);
            listViewHolder.icon1.setImageDrawable(null);
            listViewHolder.name1.setText("");
            listViewHolder.iv1.setVisibility(View.INVISIBLE);

        }

        if (allDatas.get(groupPosition).size() > NUM2) {
            if (allDatas.get(groupPosition).get(2).getPic() == null || "".equals(allDatas.get(groupPosition).get(2).getPic())) {
                listViewHolder.icon2.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.basket_info_default));
            } else {
                ImageLoader.load(mContext,allDatas.get(groupPosition).get(2).getPic(),R.mipmap.basket_info_default).into(listViewHolder.icon2);

            }

            listViewHolder.name2.setText(allDatas.get(groupPosition).get(2).getName().toString());
            listViewHolder.rl2.setEnabled(true);
            if (allDatas.get(groupPosition).get(2).isShow()) {
                listViewHolder.iv2.setVisibility(View.VISIBLE);
            } else {
                listViewHolder.iv2.setVisibility(View.INVISIBLE);
            }

        } else {
            listViewHolder.rl2.setEnabled(false);
            listViewHolder.icon2.setImageDrawable(null);
            listViewHolder.name2.setText("");
            listViewHolder.iv2.setVisibility(View.INVISIBLE);

        }

        if (allDatas.get(groupPosition).size() > NUM3) {
            if (allDatas.get(groupPosition).get(3).getPic() == null || "".equals(allDatas.get(groupPosition).get(3).getPic())) {
                listViewHolder.icon3.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.basket_info_default));
            } else {
                ImageLoader.load(mContext,allDatas.get(groupPosition).get(3).getPic(),R.mipmap.basket_info_default).into(listViewHolder.icon3);

            }

            listViewHolder.name3.setText(allDatas.get(groupPosition).get(3).getName().toString());
            listViewHolder.rl3.setEnabled(true);
            if (allDatas.get(groupPosition).get(3).isShow()) {
                listViewHolder.iv3.setVisibility(View.VISIBLE);
            } else {
                listViewHolder.iv3.setVisibility(View.INVISIBLE);
            }
        } else {
            listViewHolder.rl3.setEnabled(false);
            listViewHolder.icon3.setImageDrawable(null);
            listViewHolder.name3.setText("");
            listViewHolder.iv3.setVisibility(View.INVISIBLE);

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


                    if (lastParentPostion != -1 && lastChildPosition != -1) {
                        if (lastParentPostion != groupPosition || lastChildPosition != NUM0) {
                            allDatas.get(lastParentPostion).get(lastChildPosition).setIsShow(false);
                        }
                    }

                    L.d("123147", allDatas.get(groupPosition).get(NUM0).isShow() + "");

                    if (allDatas.get(groupPosition).get(NUM0).isShow()) {
                        L.d("123147", allDatas.get(groupPosition).get(NUM0).isShow() + "");
                        allDatas.get(groupPosition).get(NUM0).setIsShow(false);
                    } else {
                        allDatas.get(groupPosition).get(NUM0).setIsShow(true);
                        L.d("123147", allDatas.get(groupPosition).get(NUM0).isShow() + "");

                    }

                    lastParentPostion = groupPosition;
                    lastChildPosition = NUM0;

                    notifyDataSetChanged();

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

                    if (lastParentPostion != -1 && lastChildPosition != -1) {
                        if (lastParentPostion != groupPosition || lastChildPosition != NUM1) {
                            allDatas.get(lastParentPostion).get(lastChildPosition).setIsShow(false);
                        }
                    }

                    if (allDatas.get(groupPosition).get(NUM1).isShow()) {
                        allDatas.get(groupPosition).get(NUM1).setIsShow(false);
                    } else {
                        allDatas.get(groupPosition).get(NUM1).setIsShow(true);
                    }
                    lastParentPostion = groupPosition;
                    lastChildPosition = NUM1;

                    notifyDataSetChanged();

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

                    if (isInitChildAdapter) {
                        updateAdapter(groupPosition, NUM2);
                    }

                    if (lastParentPostion != -1 && lastChildPosition != -1) {
                        if (lastParentPostion != groupPosition || lastChildPosition != NUM2) {
                            allDatas.get(lastParentPostion).get(lastChildPosition).setIsShow(false);
                        }
                    }

                    if (allDatas.get(groupPosition).get(NUM2).isShow()) {
                        allDatas.get(groupPosition).get(NUM2).setIsShow(false);
                    } else {
                        allDatas.get(groupPosition).get(NUM2).setIsShow(true);
                    }
                    lastParentPostion = groupPosition;
                    lastChildPosition = NUM2;

                    notifyDataSetChanged();

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

                    if (isInitChildAdapter) {
                        updateAdapter(groupPosition, NUM3);
                    }

                    if (lastParentPostion != -1 && lastChildPosition != -1) {
                        if (lastParentPostion != groupPosition || lastChildPosition != NUM3) {
                            allDatas.get(lastParentPostion).get(lastChildPosition).setIsShow(false);
                        }
                    }

                    if (allDatas.get(groupPosition).get(NUM3).isShow()) {
                        allDatas.get(groupPosition).get(NUM3).setIsShow(false);
                    } else {
                        allDatas.get(groupPosition).get(NUM3).setIsShow(true);
                    }
                    lastParentPostion = groupPosition;
                    lastChildPosition = NUM3;
                    notifyDataSetChanged();

                    basketInfomationCallBack.onClick(v, groupPosition, NUM3);
                }

            }
        });


        return convertView;
    }

    private void updateAdapter(int groupPostion, int childPostion) {
        child_array.clear();
        for (DataBaseBean dataBaseBean : allDatas.get(groupPostion).get(childPostion).getLeagueMenues()) {
            child_array.add(dataBaseBean);
        }
        footBallGridChildAdapter.notifyDataSetChanged();
    }


    /**
     * 对一级标签下的二级标签进行设置
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.basket_info_country_item_child, null);
        gridview = (MyGridViewInfo) convertView.findViewById(R.id.gridview_child);

        child_array = new ArrayList<>();

        for (DataBaseBean dataBaseBean : allDatas.get(groupPosition).get(parentItem).getLeagueMenues()) {
            child_array.add(dataBaseBean);
        }

        footBallGridChildAdapter = new FootBallGridChildAdapter(mContext, child_array);
        gridview.setAdapter(footBallGridChildAdapter);
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
        Intent intent = new Intent(mContext, FootballDatabaseDetailsActivity.class);
        intent.putExtra(LEAGUE, allDatas.get(groPosition).get(parentItem).getLeagueMenues().get(position));
        intent.putExtra("isIntegral", false);

        mContext.startActivity(intent);
        // Toast.makeText(mContext, "当前选中的是:" + allDatas.get(groPosition).get(parentItem).getLeagueMenues().get(position).getLeagueId(), Toast.LENGTH_SHORT).show();
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
