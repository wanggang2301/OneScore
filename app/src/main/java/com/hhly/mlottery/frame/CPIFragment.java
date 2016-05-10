package com.hhly.mlottery.frame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FiltrateMatchConfigActivity;
import com.hhly.mlottery.activity.FootballActivity;
import com.hhly.mlottery.adapter.cpiadapter.CpiCompanyAdapter;
import com.hhly.mlottery.adapter.cpiadapter.CpiDateAdapter;
import com.hhly.mlottery.frame.oddfragment.CPIOddsFragment;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tenney on 2016/4/6.
 * 指数
 */
public class CPIFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private Context mContext;
    private View mView;

    private ImageView public_img_back, public_btn_filter, public_btn_set;
    private TextView public_txt_title, public_txt_left_title;//标题
    private CPIFragmentAdapter mCPIViewPagerAdapter;
    private ExactSwipeRefrashLayout mRefreshLayout;
    private LinearLayout public_date_layout;//显示时间的layout
    private TextView public_txt_date;//显示时间的textview
    //热门，公司，筛选
    private ImageView public_img_hot, public_img_company, public_img_filter;
    //判断是否选中选择热门
    private boolean isSelectHot = true;
    /**
     * 切换Viewpager的标签
     */
    private TextView mTab1, mTab2, mTab3;
    //记录是否完成viewpager初始化
    private boolean isInitViewPager = false;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    /*dialog控件 begin*/
    //日期和公司dialog
    private AlertDialog mAlertDialog;
    //多选，公司名称
    private CheckedTextView checktv;
    //选公司的时候dialog确认按钮
    private Button cpi_btn_ok;
    //dialog标题
    private TextView titleView;
    //dialoglistview
    private ListView dialog_list;
    private List<Map<String, String>> mMapList;
    private Map<String, String> mMap;
    /*dialog控件 end*/
    //日期
    private CpiDateAdapter cpiDateAdapter;
    //公司
    private CpiCompanyAdapter cpiCompanyAdapter;

    private CPIOddsFragment mCPIOddsFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_cpi, container, false);

        initView();
        initViewPager();
        initData();
        return mView;
    }


    private void initView() {
        mRefreshLayout = (ExactSwipeRefrashLayout) mView.findViewById(R.id.cpi_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);

        //中心标题
        public_txt_title = (TextView) mView.findViewById(R.id.public_txt_title);
        public_txt_title.setVisibility(View.GONE);
        //左边标题
        public_txt_left_title = (TextView) mView.findViewById(R.id.public_txt_left_title);
        public_txt_left_title.setVisibility(View.VISIBLE);
        public_txt_left_title.setText(R.string.football_detail_odds_tab);
        //筛选
        public_btn_filter = (ImageView) mView.findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);

        //设置
        public_btn_set = (ImageView) mView.findViewById(R.id.public_btn_set);
        public_btn_set.setVisibility(View.GONE);
        //返回
        public_img_back = (ImageView) mView.findViewById(R.id.public_img_back);
        public_img_back.setOnClickListener(this);
        //显示时间的布局
        public_date_layout = (LinearLayout) mView.findViewById(R.id.public_date_layout);
        public_date_layout.setVisibility(View.VISIBLE);
        //显示时间的textview
        public_txt_date = (TextView) mView.findViewById(R.id.public_txt_date);
        public_txt_date.setText(UiUtils.getDay(0));
        public_txt_date.setOnClickListener(this);
        //热门，公司，筛选
        public_img_hot = (ImageView) mView.findViewById(R.id.public_img_hot);
        public_img_hot.setOnClickListener(this);
        public_img_hot.setVisibility(View.VISIBLE);

        public_img_company = (ImageView) mView.findViewById(R.id.public_img_company);
        public_img_company.setOnClickListener(this);
        public_img_company.setVisibility(View.VISIBLE);

        public_img_filter = (ImageView) mView.findViewById(R.id.public_img_filter);
        public_img_filter.setOnClickListener(this);
        public_img_filter.setVisibility(View.VISIBLE);

    }

    //初始化viewPager
    private void initViewPager() {
        mTab1 = (TextView) mView.findViewById(R.id.cpi_match_detail_tab1);
        mTab2 = (TextView) mView.findViewById(R.id.cpi_match_detail_tab2);
        mTab3 = (TextView) mView.findViewById(R.id.cpi_match_detail_tab3);
        mTab1.setOnClickListener(this);
        mTab2.setOnClickListener(this);
        mTab3.setOnClickListener(this);
        mViewPager = (ViewPager) mView.findViewById(R.id.cpi_viewpager);
        fragments = new ArrayList<>();
        //亚盘
        mCPIOddsFragment = CPIOddsFragment.newInstance("plate", "");
        fragments.add(mCPIOddsFragment);
        //大小
        mCPIOddsFragment = CPIOddsFragment.newInstance("big", "");
        fragments.add(mCPIOddsFragment);
        //欧赔
        mCPIOddsFragment = CPIOddsFragment.newInstance("op", "");
        fragments.add(mCPIOddsFragment);


        mCPIViewPagerAdapter = new CPIFragmentAdapter(getChildFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(2);//设置预加载页面的个数。
        mViewPager.setAdapter(mCPIViewPagerAdapter);
        final LinearLayout tabLine = (LinearLayout) mView.findViewById(R.id.cpi_match_detail_tabline);
        final LinearLayout tabLineLayout = (LinearLayout) mView.findViewById(R.id.cpi_match_detail_tabline_layout);
        int displayWidth = DeviceInfo.getDisplayWidth(mContext);
        tabLine.setLayoutParams(new LinearLayout.LayoutParams(displayWidth / 3, LinearLayout.LayoutParams.WRAP_CONTENT));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int lineWidth = tabLine.getWidth();
                int marginLeft = (int) (lineWidth * (position + positionOffset));
                tabLineLayout.setPadding(marginLeft, 0, 0, 0);
            }

            @Override
            public void onPageSelected(int position) {
                mTab1.setTextColor(getResources().getColor(R.color.white));
                mTab2.setTextColor(getResources().getColor(R.color.white));
                mTab3.setTextColor(getResources().getColor(R.color.white));

                switch (position) {
                    case 0:
                        mTab1.setTextColor(getResources().getColor(R.color.white));
                        mRefreshLayout.setEnabled(true);
                        break;
                    case 1:
                        mTab2.setTextColor(getResources().getColor(R.color.white));
                        mRefreshLayout.setEnabled(true);
                        break;
                    case 2:
                        mTab3.setTextColor(getResources().getColor(R.color.white));
                        mRefreshLayout.setEnabled(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //标记是否初始化
        isInitViewPager = true;


    }

    private void initData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_img_back:
                if (getActivity() == null) return;
                ((FootballActivity) getActivity()).finish();
                break;
            case R.id.cpi_match_detail_tab1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.cpi_match_detail_tab2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.cpi_match_detail_tab3:
                mViewPager.setCurrentItem(2);

                break;
            case R.id.public_txt_date://点击日期的textview
                setDialog(0);//代表日期
                break;
            case R.id.public_img_hot://点击热门
                //如果当前选择了热门
                if (isSelectHot) {
                    public_img_hot.setSelected(true);
                    isSelectHot = false;
                    UiUtils.toast(mContext, "选择热门");
                } else {
                    //否则取消热门筛选
                    public_img_hot.setSelected(false);
                    isSelectHot = true;
                    UiUtils.toast(mContext, "取消热门");
                }
                break;
            case R.id.public_img_company://点击公司
                setDialog(1);//代表公司
                break;
            case R.id.public_img_filter://点击筛选
                startActivity(new Intent(mContext, FiltrateMatchConfigActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                System.out.println(">>>ddd" + mViewPager.getCurrentItem());
//                mRefreshLayout.setRefreshing(false);
//                mCPIOddsFragment.InitData("2016年4月15", 0);
            }
        }, 500);

    }

    /**
     * 初始化 设置 dialog 弹窗
     *
     * @dateAndcompanyNumber 0代表日期，1代表公司
     */
    public void setDialog(int dateAndcompanyNumber) {
        // Dialog 设置
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        View view = View.inflate(mContext, R.layout.alertdialog, null);
        titleView = (TextView) view.findViewById(R.id.titleView);
        dialog_list = (ListView) view.findViewById(R.id.listdate);
        //当选择公司的时候 确认按钮
        cpi_btn_ok = (Button) view.findViewById(R.id.cpi_btn_ok);
        mAlertDialog = mBuilder.create();
        mAlertDialog.setCancelable(true);
        mAlertDialog.setCanceledOnTouchOutside(true);
        //如果选择的是日期
        if (dateAndcompanyNumber == 0) {
            titleView.setText(R.string.tip);
            //这个是显示日期的适配器
            cpiDateAdapter = new CpiDateAdapter(mContext, getDate());
            dialog_list.setAdapter(cpiDateAdapter);
            //默认选中当天
            cpiDateAdapter.setDefSelect(3);
            dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    //设置标题时间
                    public_txt_date.setText(mMapList.get(position).get("date"));
                    mCPIOddsFragment.InitData(mMapList.get(position).get("date"), mViewPager.getCurrentItem());
                    // 关闭 dialog弹窗
                    mAlertDialog.dismiss();
                    // 记录点击的 item_share 位置
                    // mItems = position;
                }
            });
        } else {
            //否则就是公司
            titleView.setText(R.string.odd_company_txt);
            cpiCompanyAdapter = new CpiCompanyAdapter(mContext, getDate(), dialog_list);
//            SimpleAdapter companyAdapter = new SimpleAdapter(mContext, getDate(), R.layout.item_dialog_company, new String[]{"date"}, new int[]{R.id.item_checkedTextView});
            dialog_list.setAdapter(cpiCompanyAdapter);
            //设置你的listview的item不能被获取焦点,焦点由listview里的控件获得
            dialog_list.setItemsCanFocus(false);
            //设置多选
            dialog_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            //确定按钮
            view.findViewById(R.id.cpi_view_id).setVisibility(View.VISIBLE);
            cpi_btn_ok.setVisibility(View.VISIBLE);
            dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                    checktv = (CheckedTextView) parent.getChildAt(position).findViewById(R.id.item_checkedTextView);
                    if (checktv.isChecked()) {
                        checktv.setChecked(false);
                    } else {
                        checktv.setChecked(true);
                    }
//                    mMapList.get(position).get("date");
                    // 关闭 dialog弹窗
//                mAlertDialog.dismiss();
                    // 记录点击的 item_share 位置
//                        mItems = position;
                }
            });
            cpi_btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < getDate().size(); i++) {
                        dialog_list.isItemChecked(i);
                        if (dialog_list.isItemChecked(i) == true) {
                            System.out.println(">>>true++" + mMapList.get(i).get("date"));
                        } else {
                            System.out.println(">>>false++");

                        }
                    }
                    mAlertDialog.dismiss();
                }

            });

        }

        //点击view关闭dialog
        view.findViewById(R.id.alertdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });

        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(view);

    }

    /**
     * 获取日期列表
     *
     * @return
     */
    private List<Map<String, String>> getDate() {
        mMapList = new ArrayList<>();
        mMap = new HashMap<>();
        mMap.put("date", UiUtils.getDay(-3));
        mMapList.add(mMap);

        mMap = new HashMap<>();
        mMap.put("date", UiUtils.getDay(-2));
        mMapList.add(mMap);

        mMap = new HashMap<>();
        mMap.put("date", UiUtils.getDay(-1));
        mMapList.add(mMap);

        mMap = new HashMap<>();
        mMap.put("date", UiUtils.getDay(0));
        mMapList.add(mMap);

        mMap = new HashMap<>();
        mMap.put("date", UiUtils.getDay(1));
        mMapList.add(mMap);

        mMap = new HashMap<>();
        mMap.put("date", UiUtils.getDay(2));
        mMapList.add(mMap);

        mMap = new HashMap<>();
        mMap.put("date", UiUtils.getDay(3));
        mMapList.add(mMap);
        return mMapList;
    }

    public class CPIFragmentAdapter extends FragmentPagerAdapter {


        private List<Fragment> mFragmentList;

        public CPIFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.mFragmentList = fragmentList;
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
