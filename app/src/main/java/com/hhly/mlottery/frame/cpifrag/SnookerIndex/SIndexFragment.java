package com.hhly.mlottery.frame.cpifrag.SnookerIndex;


import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.frame.cpifrag.SnookerIndex.SnookerChildFragment.SnookerIndexChildFragment;
import com.hhly.mlottery.frame.scorefrag.ScoreSwitchFg;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.widget.BallChoiceArrayAdapter;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 斯诺克指数外层界面
 * created by mdy 155
 */

public class SIndexFragment extends ViewFragment<SIndexContract.Presenter>implements SIndexContract.View,View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private View mView;
    @BindView(R.id.public_date_layout)
    LinearLayout mLiDateSelect;

    @BindView(R.id.public_txt_date)
    TextView mTextDateSelect;

    @BindView(R.id.public_img_company)
    ImageView mCompanySelect;

    @BindView(R.id.ll_match_select)
    LinearLayout mMatchSelect;

    @BindView(R.id.tv_match_name)
    TextView mTextMatch;
    @BindView(R.id.iv_match)
    ImageView mImgMatch;

    @BindView(R.id.sindex_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.sindex_refresh_layout)
    ExactSwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.sindex_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.sindex_header)
    LinearLayout mLinearHeader;

    private TabsAdapter mTabsAdapter;

    private List<Fragment> fragments;

    private String[] mItems;

    public SIndexFragment() {
        // Required empty public constructor
    }


    public static SIndexFragment newInstance(String param1, String param2) {
        SIndexFragment fragment = new SIndexFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_sindex,container,false);

        ButterKnife.bind(this,mView);
        initView();
        setListener();
        initData();
        return mView;
    }

    private void initView() {

        mTextMatch.setText("斯诺克");
        mRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                        //刷新各个fragment
                    }
                },1000);
            }
        });
        String mTitles[]={mActivity.getResources().getString(R.string.odd_plate_rb_txt),mActivity.getResources().getString(R.string.asiasize),
                mActivity.getResources().getString(R.string.odd_op_rb_txt),"单双"};

        mItems = getResources().getStringArray(R.array.zhishu_select);

        fragments=new ArrayList<>();
        fragments.add( SnookerIndexChildFragment.newInstance("euro"));
        fragments.add(SnookerIndexChildFragment.newInstance("asiasize"));
        fragments.add(SnookerIndexChildFragment.newInstance("asialet"));
        fragments.add(SnookerIndexChildFragment.newInstance("SingleDouble"));


        mTabsAdapter=new TabsAdapter(getFragmentManager());
        mTabsAdapter.setTitles(mTitles);
        mTabsAdapter.addFragments(fragments);
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void setListener() {
        mLiDateSelect.setOnClickListener(this);
        mMatchSelect.setOnClickListener(this);
        mCompanySelect.setOnClickListener(this);
    }

    private void initData() {

    }
    @Override
    public void onError() {

    }

    /**
     * 弹出切换球类的下拉
     */
    private void switchMatch(View v){
        mImgMatch.setImageResource(R.mipmap.nav_icon_up);
        backgroundAlpha(getActivity(), 0.5f);
        popWindow(v);
    }

    private void popWindow(final View v) {
        final View mView = View.inflate(mActivity, R.layout.pop_select, null);
        // 创建ArrayAdapter对象
        BallChoiceArrayAdapter mAdapter = new BallChoiceArrayAdapter(mActivity, mItems, 1); //在第几个

        ListView listview = (ListView) mView.findViewById(R.id.match_type);
        listview.setAdapter(mAdapter);


        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(mLinearHeader);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //tv_match_name.setText(((TextView) view.findViewById(R.id.tv)).getText().toString());
                // iv_match.setImageResource(R.mipmap.nav_icon_cbb);
                EventBus.getDefault().post(new ScoreSwitchFg(position));

                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mImgMatch.setImageResource(R.mipmap.nav_icon_cbb);
                backgroundAlpha(getActivity(), 1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
    @Override
    public void fangfa() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_date_layout:

                break;
            case R.id.public_img_company:

                break;
            case R.id.ll_match_select: //切换比赛
                switchMatch(v);
                break;
        }

    }
}
