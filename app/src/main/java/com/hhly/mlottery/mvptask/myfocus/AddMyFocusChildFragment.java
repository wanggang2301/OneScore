package com.hhly.mlottery.mvptask.myfocus;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.myfocus.AddMyFocusPinnedHeaderExpandableAdapter;
import com.hhly.mlottery.view.MyFocusPinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import data.bean.myfocus.FocusBean;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/6/26  15:40.
 */

public class AddMyFocusChildFragment extends Fragment {


    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.explistview)
    MyFocusPinnedHeaderExpandableListView explistview;


    private List<FocusBean> list;
    private View mView;


    AddMyFocusPinnedHeaderExpandableAdapter adapter;

    private String[][] childrenData = new String[10][10];
    private String[] groupData = new String[10];
    private int expandFlag = -1;//控制列表的展开
    private Activity mActivity;

    private View headView;

    private LayoutInflater layoutInflater;


    public static AddMyFocusChildFragment newInstance() {
        AddMyFocusChildFragment addMyFocusChildFragment = new AddMyFocusChildFragment();
        return addMyFocusChildFragment;
    }

    public AddMyFocusChildFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_focus_child, container, false);
        ButterKnife.bind(this, view);

        initView();
        initData();

        return view;

        //  initData();

    }

    /*
     * 初始化VIEW*/

    private void initView() {
        tvAdd.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            List<FocusBean.Bean> child = new ArrayList<>();

            for (int j = 0; j < 10; j++) {
                child.add(new FocusBean.Bean("国安" + i + "-" + j, false));
            }
            list.add(new FocusBean("中超" + i, false, child));
        }


   /*     for (int i = 0; i < 10; i++) {
            groupData[i] = "中超" + i;
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                childrenData[i][j] = "国安" + i + "-" + j;
            }
        }*/

        //  View  headView = inflater.inflate(R.layout.my_focus_group_head, explistview, false);


        //设置悬浮头部VIEW
        explistview.setHeaderView(LayoutInflater.from(this.mActivity).inflate(R.layout.my_focus_group_head, explistview, false));
        adapter = new AddMyFocusPinnedHeaderExpandableAdapter(list, mActivity, explistview);
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


    public void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }
}