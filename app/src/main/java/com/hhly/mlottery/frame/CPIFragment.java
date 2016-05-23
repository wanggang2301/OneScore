package com.hhly.mlottery.frame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.CpiFiltrateActivity;
import com.hhly.mlottery.activity.FootballActivity;
import com.hhly.mlottery.adapter.cpiadapter.CpiCompanyAdapter;
import com.hhly.mlottery.adapter.cpiadapter.CpiDateAdapter;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.frame.oddfragment.CPIOddsFragment;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tenney on 2016/4/6.
 * 指数
 */
public class CPIFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    public final static String TYPE_BIG = "big";
    public final static String TYPE_PLATE = "plate";
    public final static String TYPE_OP = "op";

    private Context mContext;
    private View mView;

    private ImageView public_img_back, public_btn_filter, public_btn_set;
    private TextView public_txt_title, public_txt_left_title;//标题
    private CPIFragmentAdapter mCPIViewPagerAdapter;
    public ExactSwipeRefrashLayout mRefreshLayout;
    public LinearLayout public_date_layout;//显示时间的layout
    public TextView public_txt_date;//显示时间的textview
    //热门，公司，筛选
    public ImageView public_img_hot, public_img_company;
    /**
     * 筛选联赛
     */
    public ImageView public_img_filter;

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
    //是否选中的图片
    private ImageView cpi_img_checked;
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

    public String currentDate = "";
    //判断是否选中选择热门
//    public static boolean isHot = true;
    public List<NewOddsInfo.CompanyBean> companys = new ArrayList<>();
    public List<String> companysName = new ArrayList<>();
    private CPIOddsFragment mCPIOddsFragment, mCPIOddsFragment2, mCPIOddsFragment3;
    public List<Map<String, String>> mMapDayList;
    //判断是否是日期选择
    private boolean isFirst = false;
    //默认选择当天，当点击item后改变选中的position
    public int selectPosition = 6;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
                .build());
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_cpi, container, false);
        mMapDayList = getDate();
        initView();
        initViewPager();
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
        //显示时间的textview
        public_txt_date = (TextView) mView.findViewById(R.id.public_txt_date);
        public_txt_date.setText(UiUtils.requestByGetDay(0));
        public_txt_date.setOnClickListener(this);
        //热门，公司，筛选
        public_img_hot = (ImageView) mView.findViewById(R.id.public_img_hot);
        public_img_hot.setOnClickListener(this);
        public_img_hot.setVisibility(View.GONE);
        public_img_hot.setSelected(true);

        public_img_company = (ImageView) mView.findViewById(R.id.public_img_company);
        public_img_company.setOnClickListener(this);

        public_img_filter = (ImageView) mView.findViewById(R.id.public_img_filter);
        public_img_filter.setOnClickListener(this);
        public_img_filter.setVisibility(View.INVISIBLE);
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
//        fragments.add(CPIOddsFragment.newInstance("plate", ""));
//        //大小
//        fragments.add(CPIOddsFragment.newInstance("big", ""));
//        //欧赔
//        fragments.add(CPIOddsFragment.newInstance("op", ""));
        //亚盘
        mCPIOddsFragment = CPIOddsFragment.newInstance(TYPE_PLATE, "");
        fragments.add(mCPIOddsFragment);
        //大小
        mCPIOddsFragment2 = CPIOddsFragment.newInstance(TYPE_BIG, "");
        fragments.add(mCPIOddsFragment2);
        //欧赔
        mCPIOddsFragment3 = CPIOddsFragment.newInstance(TYPE_OP, "");
        fragments.add(mCPIOddsFragment3);


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
                        mTab2.setTextColor(getResources().getColor(R.color.line_football_footer));
                        mTab3.setTextColor(getResources().getColor(R.color.line_football_footer));
                        break;
                    case 1:
                        mTab2.setTextColor(getResources().getColor(R.color.white));
                        mRefreshLayout.setEnabled(true);
                        mTab1.setTextColor(getResources().getColor(R.color.line_football_footer));
                        mTab3.setTextColor(getResources().getColor(R.color.line_football_footer));
                        break;
                    case 2:
                        mTab3.setTextColor(getResources().getColor(R.color.white));
                        mRefreshLayout.setEnabled(true);
                        mTab2.setTextColor(getResources().getColor(R.color.line_football_footer));
                        mTab1.setTextColor(getResources().getColor(R.color.line_football_footer));
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
//                if (isHot) {
//                    public_img_hot.setSelected(true);
//                    isHot = false;
////                    for (Fragment fragment : fragments) {
////                        ((CPIOddsFragment) fragment).selectedHot(true);
////
////                    }
//                    mCPIOddsFragment.selectedHot(true,"plate");
//                    mCPIOddsFragment2.selectedHot(true, "big");
//                    mCPIOddsFragment3.selectedHot(true, "op");
//                } else {
//                    //否则取消热门筛选
//                    public_img_hot.setSelected(false);
//                    isHot = true;
////                    for (Fragment fragment : fragments) {
////                        ((CPIOddsFragment) fragment).selectedHot(false);
////                    }
//                    mCPIOddsFragment.selectedHot(false,"plate");
//                    mCPIOddsFragment2.selectedHot(false, "big");
//                    mCPIOddsFragment3.selectedHot(false,"op");


//                if (companys.size() != 0) {
//                    isHot = !isHot;
//                    public_img_hot.setSelected(isHot);
//                    mCPIOddsFragment.filtrateData(isHot, checkedCompanys, CpiFiltrateActivity.mCheckedIds);
//                    mCPIOddsFragment2.filtrateData(isHot, checkedCompanys, CpiFiltrateActivity.mCheckedIds);
//                    mCPIOddsFragment3.filtrateData(isHot, checkedCompanys, CpiFiltrateActivity.mCheckedIds);
//
//                }


//                }
                break;
            case R.id.public_img_company://点击公司
                setDialog(1);//代表公司
                break;
            case R.id.public_img_filter://点击筛选
//                if (CPIOddsFragment.mFileterTagsBean.size() == 0 && CPIOddsFragment.mFileterTagsBean == null)
//                    return;
                Intent intent = new Intent(mContext, CpiFiltrateActivity.class);
                //如果选择的是日期不传选中的
                if (isFirst) {
                    intent.putExtra("fileterTags", (Serializable) CPIOddsFragment.mFileterTagsBean);
                    ddList.clear();
                    intent.putExtra("linkedListChecked", ddList);
                    isFirst = false;
                }
                //否者要传选中的id
                else {
                    intent.putExtra("fileterTags", (Serializable) CPIOddsFragment.mFileterTagsBean);
                    intent.putExtra("linkedListChecked", ddList);
                }
                startActivityForResult(intent, 10086);

                break;
            default:
                break;
        }
    }

    LinkedList<String> ddList = new LinkedList();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == 10086) {
            switch (resultCode) {
                case 0:
                    ArrayList<String> checkedIdExtra = (ArrayList<String>) data.getSerializableExtra("key");
                    ddList.clear();
                    ddList.addAll(checkedIdExtra);
//                    mCPIOddsFragment.filtrateData(companys, ddList);
//                    mCPIOddsFragment2.filtrateData(companys, ddList);
//                    mCPIOddsFragment3.filtrateData(companys, ddList);
                    companysName.clear();
                    for (int k = 0; k < companys.size(); k++) {
                        if (companys.get(k).isChecked()) {
                            companysName.add(companys.get(k).getComName());
                        }
                    }
                    mCPIOddsFragment.selectCompany2(CPIOddsFragment.mAllInfoBean1, companysName, ddList, TYPE_PLATE);
                    mCPIOddsFragment2.selectCompany2(CPIOddsFragment.mAllInfoBean2, companysName, ddList, TYPE_BIG);
                    mCPIOddsFragment3.selectCompany2(CPIOddsFragment.mAllInfoBean3, companysName, ddList, TYPE_OP);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCPIOddsFragment.cpi_fl_plate_networkError.getVisibility() == View.VISIBLE) {
                    mMapDayList = getDate();
                    public_txt_date.setText(UiUtils.requestByGetDay(0));
                    selectPosition = 3;
                    for (Fragment fragment : fragments) {
                        ((CPIOddsFragment) fragment).switchd("");
                    }
                } else {
                    companysName.clear();
                    for (int k = 0; k < companys.size(); k++) {
                        if (companys.get(k).isChecked()) {
                            companysName.add(companys.get(k).getComName());
                        }
                    }
                    mCPIOddsFragment.selectCompany2(CPIOddsFragment.mAllInfoBean1, companysName, CpiFiltrateActivity.mCheckedIds, TYPE_PLATE);
                    mCPIOddsFragment2.selectCompany2(CPIOddsFragment.mAllInfoBean2, companysName, CpiFiltrateActivity.mCheckedIds, TYPE_BIG);
                    mCPIOddsFragment3.selectCompany2(CPIOddsFragment.mAllInfoBean3, companysName, CpiFiltrateActivity.mCheckedIds, TYPE_OP);
                }
                mRefreshLayout.setRefreshing(false);
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

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 1000);
            dialog_list.setLayoutParams(params);
            titleView.setText(R.string.tip);
            //这个是显示日期的适配器
            cpiDateAdapter = new CpiDateAdapter(mContext, mMapDayList);
            dialog_list.setAdapter(cpiDateAdapter);
            //默认选中当天
            cpiDateAdapter.setDefSelect(selectPosition);
            dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    companys.clear();
                    // 记录点击的 item 位置
                    selectPosition = position;
                    //设置标题时间
                    public_txt_date.setText(mMapList.get(position).get("date"));
                    for (Fragment fragment : fragments) {
                        ((CPIOddsFragment) fragment).switchd(mMapList.get(position).get("date"));
                    }
                    isFirst = true;
                    // 关闭 dialog弹窗
                    mAlertDialog.dismiss();
                }
            });
        } else {
            //否则就是公司
            titleView.setText(R.string.odd_company_txt);
//            cpiCompanyAdapter = new CpiCompanyAdapter(mContext, getComPany(mCompanyBean), dialog_list);
//            SimpleAdapter companyAdapter = new SimpleAdapter(mContext, getDate(), R.layout.item_dialog_company, new String[]{"date"}, new int[]{R.id.item_checkedTextView});
//            dialog_list.setAdapter(cpiCompanyAdapter);

            cpiCompanyAdapter = new CpiCompanyAdapter(mContext, companys);
            dialog_list.setAdapter(cpiCompanyAdapter);
            //设置你的listview的item不能被获取焦点,焦点由listview里的控件获得
            dialog_list.setItemsCanFocus(false);
            //设置多选
            dialog_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            //确定按钮
            view.findViewById(R.id.cpi_view_id).setVisibility(View.VISIBLE);
            cpi_btn_ok.setVisibility(View.VISIBLE);

            final boolean[] tempCompanyCheckedStatus = new boolean[companys.size()];
            for (int n = 0; n < companys.size(); n++) {
                tempCompanyCheckedStatus[n] = companys.get(n).isChecked();
            }
            dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, final int position, long arg3) {
                    checktv = (CheckedTextView) parent.getChildAt(position).findViewById(R.id.item_checkedTextView);
                    cpi_img_checked = (ImageView) parent.getChildAt(position).findViewById(R.id.item_img_checked);
                    checktv.setChecked(!checktv.isChecked());
                    //如果是选中
                    if (checktv.isChecked()) {
                        cpi_img_checked.setBackground(mContext.getResources().getDrawable(R.mipmap.cpi_img_select_true));
                    } else {
                        cpi_img_checked.setBackground(mContext.getResources().getDrawable(R.mipmap.cpi_img_select));
                    }
                    tempCompanyCheckedStatus[position] = checktv.isChecked();
                }
            });

            cpi_btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = 0;
                    for (boolean status : tempCompanyCheckedStatus) {
                        if (status) {
                            count++;
                        }
                    }

                    if (count < 1) {
                        UiUtils.toast(mContext, "请选择公司", Toast.LENGTH_SHORT);
                        return;
                    }

                    for (int n = 0; n < companys.size(); n++) {
                        companys.get(n).setIsChecked(tempCompanyCheckedStatus[n]);
                    }
                    companysName.clear();
                    for (int k = 0; k < companys.size(); k++) {
                        if (companys.get(k).isChecked()) {
                            companysName.add(companys.get(k).getComName());
                        }
                    }
//                    mCPIOddsFragment.filtrateData(companys, CpiFiltrateActivity.mCheckedIds);
//                    mCPIOddsFragment2.filtrateData(companys, CpiFiltrateActivity.mCheckedIds);
//                    mCPIOddsFragment3.filtrateData(companys, CpiFiltrateActivity.mCheckedIds);
                    mCPIOddsFragment.selectCompany2(CPIOddsFragment.mAllInfoBean1, companysName, CpiFiltrateActivity.mCheckedIds, TYPE_PLATE);
                    mCPIOddsFragment2.selectCompany2(CPIOddsFragment.mAllInfoBean2, companysName, CpiFiltrateActivity.mCheckedIds, TYPE_BIG);
                    mCPIOddsFragment3.selectCompany2(CPIOddsFragment.mAllInfoBean3, companysName, CpiFiltrateActivity.mCheckedIds, TYPE_OP);

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
    public List<Map<String, String>> getDate() {
        mMapList = new ArrayList<>();
        new Thread() {
            @Override
            public void run() {
                mMap = new HashMap<>();
                mMap.put("date", UiUtils.requestByGetDay(-6));
                mMapList.add(mMap);

                mMap = new HashMap<>();
                mMap.put("date", UiUtils.requestByGetDay(-5));
                mMapList.add(mMap);

                mMap = new HashMap<>();
                mMap.put("date", UiUtils.requestByGetDay(-4));
                mMapList.add(mMap);

                mMap = new HashMap<>();
                mMap.put("date", UiUtils.requestByGetDay(-3));
                mMapList.add(mMap);

                mMap = new HashMap<>();
                mMap.put("date", UiUtils.requestByGetDay(-2));
                mMapList.add(mMap);

                mMap = new HashMap<>();
                mMap.put("date", UiUtils.requestByGetDay(-1));
                mMapList.add(mMap);

                mMap = new HashMap<>();
                mMap.put("date", UiUtils.requestByGetDay(0));
                mMapList.add(mMap);

                mMap = new HashMap<>();
                mMap.put("date", UiUtils.requestByGetDay(1));
                mMapList.add(mMap);

                mMap = new HashMap<>();
                mMap.put("date", UiUtils.requestByGetDay(2));
                mMapList.add(mMap);

                mMap = new HashMap<>();
                mMap.put("date", UiUtils.requestByGetDay(3));
                mMapList.add(mMap);

                mMap = new HashMap<>();
                mMap.put("date", UiUtils.requestByGetDay(4));
                mMapList.add(mMap);

                mMap = new HashMap<>();
                mMap.put("date", UiUtils.requestByGetDay(5));
                mMapList.add(mMap);

                mMap = new HashMap<>();
                mMap.put("date", UiUtils.requestByGetDay(6));
                mMapList.add(mMap);

                mMap = new HashMap<>();
                mMap.put("date", UiUtils.requestByGetDay(7));
                mMapList.add(mMap);
            }
        }.start();
        return mMapList;
    }

    private class CPIFragmentAdapter extends FragmentPagerAdapter {


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
