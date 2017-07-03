package com.hhly.mlottery.mvptask.myfocus;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.MyFocusActivity;
import com.hhly.mlottery.adapter.myfocus.IDeleteMyFocus;
import com.hhly.mlottery.adapter.myfocus.MyFocusPinnedHeaderExpandableAdapter;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.view.MyFocusPinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import data.bean.myfocus.FocusBean;

/**
 * @author wangg
 * @desc 我的关注
 */
public class MyFocusChildFragment extends Fragment {


    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.explistview)
    MyFocusPinnedHeaderExpandableListView explistview;
    @BindView(R.id.tv_noRaces)
    TextView tvNoRaces;
    private List<FocusBean> list;

    private View mView;

    MyFocusPinnedHeaderExpandableAdapter adapter;

    private String[][] childrenData = new String[10][10];
    private String[] groupData = new String[10];
    private int expandFlag = -1;//控制列表的展开
    private Activity mActivity;

    private View headView;

    private LayoutInflater layoutInflater;

    private IDeleteMyFocus iDeleteMyFocus;


    public static MyFocusChildFragment newInstance() {
        MyFocusChildFragment myFocusChildFragment = new MyFocusChildFragment();
        return myFocusChildFragment;
    }

    public MyFocusChildFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_focus_child, container, false);
        ButterKnife.bind(this, mView);
        initData();
        initEvent();
        return mView;
    }


    /**
     * 初始化数据
     */
    private void initData() {

        list = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            List<FocusBean.Bean> child = new ArrayList<>();

            for (int j = 0; j < 3; j++) {
                child.add(new FocusBean.Bean("国安" + i + "-" + j, true));
            }
            list.add(new FocusBean("中超" + i, true, child));
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
        adapter = new MyFocusPinnedHeaderExpandableAdapter(list, mActivity, explistview);
        explistview.setAdapter(adapter);


        //设置单个分组展开
        //explistview.setOnGroupClickListener(new GroupClickListener());
    }


    private void initEvent() {
        iDeleteMyFocus = new IDeleteMyFocus() {
            @Override
            public void deleteMyFocusGroup(int groupPosition) {

                list.remove(groupPosition);


                if (list.size() == 0) {

                    explistview.setVisibility(View.GONE);

                    tvNoRaces.setVisibility(View.VISIBLE);
                    return;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void deleteMyFocusChild(int groupPosition, int childPosition) {


                list.get(groupPosition).getList().remove(childPosition);
                L.d("ffgghh", groupPosition + "____" + childPosition);

                if (list.get(groupPosition).getList().size() == 0) {
                    list.remove(groupPosition);

                    L.d("ffgghh", "删除父亲节点");


                    if (list.size() == 0) {
                        explistview.setVisibility(View.GONE);
                        tvNoRaces.setVisibility(View.VISIBLE);
                        return;
                    }
                }


                L.d("ffgghh", "==" + list.size());
                L.d("ffgghh", "==" + list.get(0).getList().size());


                adapter.notifyDataSetChanged();
            }


        };

        adapter.setiDeleteMyFocus(iDeleteMyFocus);
    }


    @OnClick(R.id.tv_add)
    public void onClick() {
        Intent intent = new Intent(mActivity, MyFocusActivity.class);
        intent.putExtra("type", 1);
        mActivity.startActivity(intent);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }


}
