package com.hhly.mlottery.mvptask.myfocus;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.myfocus.MyFocusPinnedHeaderExpandableAdapter;
import com.hhly.mlottery.view.MyFocusPinnedHeaderExpandableListView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangg
 * @desc 我的关注
 */
public class MyFocusChildFragment extends Fragment {


    @BindView(R.id.explistview)
    MyFocusPinnedHeaderExpandableListView explistview;


    MyFocusPinnedHeaderExpandableAdapter adapter;

    private String[][] childrenData = new String[10][10];
    private String[] groupData = new String[10];
    private int expandFlag = -1;//控制列表的展开
    private Activity mActivity;

    private View headView;


    public static MyFocusChildFragment newInstance() {
        MyFocusChildFragment myFocusChildFragment = new MyFocusChildFragment();
        return myFocusChildFragment;
    }

    public MyFocusChildFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_focus_child, container, false);
        headView = inflater.inflate(R.layout.my_focus_group_head, explistview, false);
        ButterKnife.bind(this, view);

        initData();
        return view;
    }


    /**
     * 初始化数据
     */
    private void initData() {
        for (int i = 0; i < 10; i++) {
            groupData[i] = "分组" + i;
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                childrenData[i][j] = "好友" + i + "-" + j;
            }
        }
        //设置悬浮头部VIEW
        explistview.setHeaderView(headView);
        adapter = new MyFocusPinnedHeaderExpandableAdapter(childrenData, groupData, mActivity, explistview);
        explistview.setAdapter(adapter);

        //设置单个分组展开
        //explistview.setOnGroupClickListener(new GroupClickListener());
    }

    class GroupClickListener implements ExpandableListView.OnGroupClickListener {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {
            if (expandFlag == -1) {
                // 展开被选的group
                explistview.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                explistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            } else if (expandFlag == groupPosition) {
                explistview.collapseGroup(expandFlag);
                expandFlag = -1;
            } else {
                explistview.collapseGroup(expandFlag);
                // 展开被选的group
                explistview.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                explistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            }
            return true;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

}
