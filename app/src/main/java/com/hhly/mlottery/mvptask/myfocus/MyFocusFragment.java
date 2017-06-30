package com.hhly.mlottery.mvptask.myfocus;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wangg
 * @desc 我的关注
 * @date 2017/05/31
 */
public class MyFocusFragment extends Fragment {

    private Activity mActivity;

    @BindView(R.id.focus_back)
    ImageView focusBack;
    @BindView(R.id.focus_radio_group)
    RadioGroup focusRadioGroup;
    @BindView(R.id.focus_search)
    ImageView focusSearch;


    private FragmentManager fragmentManager;
    private List<Fragment> fragments;
    private Fragment currentFragment;

    private int type;
    boolean isSaveFocus = false;


    public static MyFocusFragment newInstance(int type) {
        MyFocusFragment myFocusFragment = new MyFocusFragment();


        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        myFocusFragment.setArguments(bundle);

        return myFocusFragment;
    }

    public MyFocusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_focus, container, false);
        ButterKnife.bind(this, view);

        L.d("type", "type=" + type);

        initEvent();
        return view;
    }


    private void initEvent() {

        fragments = new ArrayList<>();

        if (type == 0) {
            fragments.add(MyFocusChildFragment.newInstance());
            fragments.add(MyFocusChildFragment.newInstance());
        } else {
            fragments.add(AddMyFocusChildFragment.newInstance());
            fragments.add(AddMyFocusChildFragment.newInstance());
        }


        focusRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                int id = group.getCheckedRadioButtonId();
                int currenIndex = 0;
                switch (id) {
                    case R.id.focus_football:
                        currenIndex = 0;
                        break;
                    case R.id.focus_basket:
                        currenIndex = 1;
                        break;
                }

                switchContent(currentFragment, fragments.get(currenIndex));
            }
        });

        switchFragment(0);

    }


    private void switchFragment(int position) {


        fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.myfocus_content, fragments.get(position));
        fragmentTransaction.commit();
        currentFragment = fragments.get(position);
    }


    private void switchContent(Fragment from, Fragment to) {
        if (currentFragment != to) {
            currentFragment = to;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(from).add(R.id.myfocus_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                fragmentTransaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }


    @OnClick({R.id.focus_back, R.id.focus_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.focus_back:
                if (type == 0) {
                    isDeletedFocusMatch();
                } else {
                    mActivity.finish();
                }

                break;
            case R.id.focus_search:
                break;
        }
    }


    private void isDeletedFocusMatch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.AlertDialog);
        final AlertDialog alertDialog = builder.create();
        LayoutInflater infla = LayoutInflater.from(mActivity);
        View alertDialogView = infla.inflate(R.layout.myfocus_delete_notice, null);
        TextView tv_unsave = (TextView) alertDialogView.findViewById(R.id.tv_unsave);
        TextView tv_save = (TextView) alertDialogView.findViewById(R.id.tv_save);
        tv_unsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                mActivity.finish();

            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //保存数据

                alertDialog.dismiss();
                mActivity.finish();

            }
        });

        alertDialog.show();
        alertDialog.getWindow().setContentView(alertDialogView);
        alertDialog.setCanceledOnTouchOutside(true);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }
}
