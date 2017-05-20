package com.hhly.mlottery.frame.cpifrag.basketballtask;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketBallIndexFiltrateActivity;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.bean.enums.BasketOddsTypeEnum;
import com.hhly.mlottery.bean.websocket.WebBasketMatch;
import com.hhly.mlottery.bean.websocket.WebBasketOdds;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.config.FootBallMatchFilterTypeEnum;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.frame.BallType;
import com.hhly.mlottery.frame.cpifrag.CloseCpiWebSocketEventBus;
import com.hhly.mlottery.frame.oddfragment.DateChooseDialogFragment;
import com.hhly.mlottery.frame.oddfragment.basketoddframent.BasketCompanyChooseDialogFragment;
import com.hhly.mlottery.frame.scorefrag.ScoreSwitchFg;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.widget.BallChoiceArrayAdapter;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.hhly.mlottery.R.id.public_date_layout;

/**
 * @author wangg
 * @des:篮球指数
 * @date:2017/3/16
 */
public class BasketBallCpiFrament extends BaseWebSocketFragment implements ExactSwipeRefreshLayout.OnRefreshListener {

    @BindView(public_date_layout)
    LinearLayout publicDateLayout;
    @BindView(R.id.tv_match_name)
    TextView tvMatchName;
    @BindView(R.id.ll_match_select)
    LinearLayout llMatchSelect;
    @BindView(R.id.public_img_company)
    ImageView publicImgCompany;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.cpi_viewpager)
    ViewPager cpiViewpager;
    @BindView(R.id.iv_match)
    ImageView ivMatch;
    @BindView(R.id.header_layout)
    LinearLayout headerLayout;
    @BindView(R.id.public_img_filter)
    ImageView publicImgFilter;
    @BindView(R.id.public_txt_date)
    TextView publicTxtDate;
    @BindView(R.id.cpi_refresh_layout)
    ExactSwipeRefreshLayout cpiRefreshLayout;
    private String[] mItems;
    private Activity mActivity;
    private List<BasketBallOddFragment> mFragments;
    private View mView;
    private BasketCompanyChooseDialogFragment mCompanyChooseDialogFragment; // 公司选择
    private DateChooseDialogFragment mDateChooseDialogFragment; // 日期选择

    //公司euro,asiaLet,asiaSize
    private Map<String, List<BasketIndexBean.DataBean.CompanyBean>> companyMap;


    private LinkedList<String> filterLeagueList; // 过滤信息数据源
    private static final int startFilterRequestCode = 10086;
    private String currentDate; // 当前日期
    private String choosenDate; // 选中日期

    private String oddType = BasketOddsTypeEnum.ASIALET;

    public BasketBallCpiFrament() {
    }

    public static BasketBallCpiFrament newInstace() {
        BasketBallCpiFrament basketBallCpiFrament = new BasketBallCpiFrament();
        return basketBallCpiFrament;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setWebSocketUri(BaseURLs.WS_SERVICE);
//        setTopic("USER.topic.basketball");
        setTopic(BaseUserTopics.oddsBasket);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_ball_cpi, container, false);
        ButterKnife.bind(this, mView);
        tvMatchName.setText(getResources().getString(R.string.basketball_txt));
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mItems = getResources().getStringArray(R.array.zhishu_select);
/*
        mPresenter = new BasketBallCpiPresenter(this);
        //初始化头部View和事件
        mPresenter.initFg();*/
        initViewPager();

        companyMap = new ArrayMap<>();
    }


    /**
     * 初始化 ViewPager
     */
    private void initViewPager() {
        cpiRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        cpiRefreshLayout.setOnRefreshListener(this);
        cpiRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mActivity, StaticValues.REFRASH_OFFSET_END));

        mFragments = new ArrayList<>();
        mFragments.add(BasketBallOddFragment.newInstance(BasketOddsTypeEnum.ASIALET));
        mFragments.add(BasketBallOddFragment.newInstance(BasketOddsTypeEnum.ASIASIZE));
        mFragments.add(BasketBallOddFragment.newInstance(BasketOddsTypeEnum.EURO));
        CPIPagerAdapter pagerAdapter = new CPIPagerAdapter(getChildFragmentManager());
        cpiViewpager.setOffscreenPageLimit(3);
        cpiViewpager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(cpiViewpager);

        cpiViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        oddType = BasketOddsTypeEnum.ASIALET;
                        break;
                    case 1:
                        oddType = BasketOddsTypeEnum.ASIASIZE;
                        break;

                    case 2:
                        oddType = BasketOddsTypeEnum.EURO;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //开启推送
        connectWebSocket();

    }

    /**
     * 获取公司列表
     *
     * @return 公司列表
     */
    public Map<String, List<BasketIndexBean.DataBean.CompanyBean>> getCompanyMap() {
        return companyMap; //亚盘
    }

    public void showRightButton() {
        publicImgCompany.setVisibility(View.VISIBLE);
        publicImgFilter.setVisibility(View.VISIBLE);
        publicDateLayout.setVisibility(View.VISIBLE);
    }


    /**
     * 显示公司选择
     */
    public void showCompanyChooseDialog() {
        maybeInitCompanyChooseDialog();

    }

    /**
     * 初始化公司选择Dialog
     */
    private void maybeInitCompanyChooseDialog() {
        ArrayList<BasketIndexBean.DataBean.CompanyBean> arrayList = (ArrayList<BasketIndexBean.DataBean.CompanyBean>) companyMap.get(oddType);

        if (CollectionUtils.notEmpty(arrayList)) {
            mCompanyChooseDialogFragment = BasketCompanyChooseDialogFragment.newInstance(arrayList,
                    new BasketCompanyChooseDialogFragment.OnFinishSelectionListener() {
                        @Override
                        public void onFinishSelection() {
                            //对公司的筛选

                            switch (oddType) {
                                case BasketOddsTypeEnum.ASIALET:
                                    mFragments.get(0).updateFilterData();
                                    break;
                                case BasketOddsTypeEnum.ASIASIZE:
                                    mFragments.get(1).updateFilterData();
                                    break;
                                case BasketOddsTypeEnum.EURO:
                                    mFragments.get(2).updateFilterData();
                                    break;

                                default:
                                    break;
                            }
                        }
                    });

            if (!mCompanyChooseDialogFragment.isVisible()) {
                mCompanyChooseDialogFragment.show(getChildFragmentManager(), "companyChooseDialog");
            }
        }
    }


    @Override
    public void onRefresh() {
        refreshAllChildFragments();
        connectWebSocket();
    }


    public void setRefreshHide() {
        cpiRefreshLayout.setRefreshing(false);
    }

    public void setRefreshVisible() {
        cpiRefreshLayout.setRefreshing(true);
    }


    /**
     * 球类切换Dialog
     */
    private void popWindow() {
        final View mView = View.inflate(mActivity, R.layout.pop_select, null);
        // 创建ArrayAdapter对象
        BallChoiceArrayAdapter mAdapter = new BallChoiceArrayAdapter(mActivity, mItems, BallType.BASKETBALL);

        ListView listview = (ListView) mView.findViewById(R.id.match_type);
        listview.setAdapter(mAdapter);
        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(headerLayout);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.d("websocket123", ">>>>>>>>篮球指数关闭");

                closeWebSocket();
                EventBus.getDefault().post(new ScoreSwitchFg(position));
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivMatch.setImageResource(R.mipmap.nav_icon_cbb);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }


    /**
     * 显示日期选择 dialog
     */
    private void showDateChooseDialog() {
        maybeInitDateChooseDialog();
        if (!mDateChooseDialogFragment.isVisible()) {
            mDateChooseDialogFragment.show(getChildFragmentManager(), "dateChooseFragment");
        }
    }


    /**
     * 日期选择 Fragment 初始化
     */
    private void maybeInitDateChooseDialog() {
        if (mDateChooseDialogFragment != null) return;
        mDateChooseDialogFragment = DateChooseDialogFragment.newInstance(currentDate,
                new DateChooseDialogFragment.OnDateChooseListener() {
                    @Override
                    public void onDateChoose(String date) {
                        choosenDate = date;
                        publicTxtDate.setText(DateUtil.convertDateToNation(date));
                        cpiRefreshLayout.setRefreshing(true);
                        refreshAllChildFragments();
                    }
                });
    }


    /**
     * 刷新所有子 fragment
     */
    public void refreshAllChildFragments() {
//        mFragments.get(mViewPager.getCurrentItem()).refreshData(currentDate);
        for (BasketBallOddFragment fragment : mFragments) {
            // 未选日期的时候 choosenDate 为null，则请求当天数据
            // 选择日期之后 choosenDate 为选择的日期，请求选择日期的数据
            fragment.refreshData(choosenDate);
        }
    }


    @OnClick({public_date_layout, R.id.ll_match_select, R.id.public_img_company, R.id.public_img_filter})
    public void onClick(View view) {
        switch (view.getId()) {
            case public_date_layout:
                showDateChooseDialog();
                break;

            case R.id.ll_match_select:
                ivMatch.setImageResource(R.mipmap.nav_icon_up);
                backgroundAlpha(getActivity(), 0.5f);
                popWindow();

                break;
            case R.id.public_img_company:
                showCompanyChooseDialog();
                break;

            case R.id.public_img_filter:
                Intent intent = new Intent(mActivity, BasketBallIndexFiltrateActivity.class);

                L.d("tagsss", "数量2==" + getCurrentFragment().getFilterTagList().size());


                intent.putExtra("fileterTags", (Serializable) getCurrentFragment().getFilterTagList());
                intent.putExtra("linkedListChecked", filterLeagueList);
                startActivityForResult(intent, startFilterRequestCode);

                break;
        }
    }


    public BasketBallOddFragment getCurrentFragment() {
        return mFragments.get(cpiViewpager.getCurrentItem());
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != startFilterRequestCode || resultCode != Activity.RESULT_CANCELED || data == null) {
            return;
        }


        ArrayList<String> checkedIdList = (ArrayList<String>) data.getSerializableExtra("key");
        if (filterLeagueList == null) filterLeagueList = new LinkedList<>();
        filterLeagueList.clear();
        filterLeagueList.addAll(checkedIdList);

        L.d("saveId", filterLeagueList.size() + "");

        PreferenceUtil.setDataList(FootBallMatchFilterTypeEnum.BASKET_INDEX, filterLeagueList);


        for (BasketBallOddFragment fragment : mFragments) {
            fragment.updateFilterData();
        }


    }


    /**
     * 设置当前日期
     *
     * @param currentDate currentDate, 形如 2016-06-23
     */
    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
        if (TextUtils.isEmpty(choosenDate)) {
            publicTxtDate.setText(DateUtil.convertDateToNation(currentDate));
            if (!publicDateLayout.isShown()) publicDateLayout.setVisibility(View.VISIBLE);
            if (!publicTxtDate.isShown()) publicTxtDate.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 获取过滤信息
     *
     * @return 过滤信息
     */
    public LinkedList<String> getFilterLeagueList() {
        return filterLeagueList;
    }

    /**
     * ViewPager 适配器
     */
    class CPIPagerAdapter extends FragmentStatePagerAdapter {

        public CPIPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BasketBallOddFragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.odd_plate_rb_txt);
                case 1:
                    return getString(R.string.asiasize);
                case 2:
                    return getString(R.string.odd_op_rb_txt);
                default:
                    return getString(R.string.odd_plate_rb_txt);
            }
        }
    }

    @Override
    protected void onTextResult(final String text) {
        L.d("websocket123", "_______篮球指数推送==" + text);


        tabs.post(new Runnable() {
            @Override
            public void run() {
                handleMessage(text);
            }
        });
    }

    /**
     * 处理 websocket 传来的数据
     *
     * @param jsonString jsonString
     */
    private void handleMessage(String jsonString) {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        L.d("pushtextscore", "推送数据==" + jsonString);

        int type = jsonObject.getIntValue("type");
        if (type == 100) {  //比分
            L.d("pushtextscore", "比分推送数据==" + jsonString);

            updateTimeAndStatus(jsonString);
        } else if (type == 101) { //赔率

            L.d("pushtextstatus", "赔率推送数据==" + jsonString);

            updateOdds(jsonString);
        }
    }


    private void updateTimeAndStatus(String jsonString) {
        WebBasketMatch mWebBasketMatch = null;
        try {
            mWebBasketMatch = JSON.parseObject(jsonString, WebBasketMatch.class);
        } catch (Exception e) {
            jsonString = jsonString.substring(0, jsonString.length() - 1);
            mWebBasketMatch = JSON.parseObject(jsonString, WebBasketMatch.class);
        }

        for (BasketBallOddFragment fragment : mFragments) {
            fragment.updatePushScoreAndStatus(mWebBasketMatch);
        }
    }

    private void updateOdds(String jsonString) {
        WebBasketOdds mWebBasketOdds = null;
        try {
            mWebBasketOdds = JSON.parseObject(jsonString, WebBasketOdds.class);
        } catch (Exception e) {
            jsonString = jsonString.substring(0, jsonString.length() - 1);
            mWebBasketOdds = JSON.parseObject(jsonString, WebBasketOdds.class);
        }

        mFragments.get(0).updatePushOdd(mWebBasketOdds.getThirdId(), BasketOddsTypeEnum.ASIALET, mWebBasketOdds.getData().getAsiaLet());
        mFragments.get(1).updatePushOdd(mWebBasketOdds.getThirdId(), BasketOddsTypeEnum.ASIASIZE, mWebBasketOdds.getData().getAsiaSize());
        mFragments.get(2).updatePushOdd(mWebBasketOdds.getThirdId(), BasketOddsTypeEnum.EURO, mWebBasketOdds.getData().getEuro());
    }


    @Override
    protected void onConnected() {

    }

    @Override
    protected void onConnectFail() {

    }

    @Override
    protected void onDisconnected() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        closeWebSocket();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeWebSocket();
    }

    public void onEventMainThread(CloseCpiWebSocketEventBus closeWebSocketEventBus) {

        if (closeWebSocketEventBus.isVisible()) {
            L.d("websocket123", "篮球 指数 关闭 fg");
            closeWebSocket();
        } else {
            if (closeWebSocketEventBus.getIndex() == BallType.BASKETBALL) {
                L.d("websocket123", "篮球 指数 打开 fg");

                connectWebSocket();
            }
        }
    }
}
