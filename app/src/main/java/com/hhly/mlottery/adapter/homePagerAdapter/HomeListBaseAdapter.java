package com.hhly.mlottery.adapter.homePagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.ExpertsActivity;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.activity.IndexActivity;
import com.hhly.mlottery.activity.NumbersActivity;
import com.hhly.mlottery.activity.NumbersInfoBaseActivity;
import com.hhly.mlottery.activity.ProductAdviceActivity;
import com.hhly.mlottery.activity.WebActivity;
import com.hhly.mlottery.bean.homepagerentity.HomeBodysEntity;
import com.hhly.mlottery.bean.homepagerentity.HomeBodysLottery;
import com.hhly.mlottery.bean.homepagerentity.HomeContentEntity;
import com.hhly.mlottery.bean.homepagerentity.HomeOtherListsEntity;
import com.hhly.mlottery.bean.homepagerentity.HomePagerEntity;
import com.hhly.mlottery.bean.productadvice.ProductUserLike;
import com.hhly.mlottery.callback.ProductListener;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.FootBallDetailTypeEnum;
import com.hhly.mlottery.frame.HomeMuenFragment;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.HandMatchId;
import com.hhly.mlottery.util.HomeNumbersSplit;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.NumberDataUtils;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.CircleImageView;
import com.hhly.mlottery.widget.WrapContentHeightViewPager;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页ListView数据适配器
 * Created by hhly107 on 2016/3/30.
 */
public class HomeListBaseAdapter extends BaseAdapter {

    private Context mContext;
    private ViewHolder mViewHolder;// ViewHolder
    private ViewHolderOther mViewHolderOther;// ViewHolderOther
    private HomePagerEntity mHomePagerEntity;// 首页实体对象
    //    private HomeGridAdapter mGridAdapter;// GridView数据适配器
    private HomePagerAdapter mPagerAdapter;// ViewPager数据适配器

    private ImageView score01_icon, score01_home_icon, score01_guest_icon, data_info_icon01, hk_icon, ks01_number, ks02_number, ks03_number, qxc_icon, klsf_icon, ks_icon, ssc_icon, iv_expert_icon;
    private ImageView bjsc_icon, bjsc01_number, bjsc02_number, bjsc03_number, bjsc04_number, bjsc05_number, bjsc06_number, bjsc07_number, bjsc08_number, bjsc09_number, bjsc10_number;
    private TextView score01_title, score01_home_name, score01_home_score, score01_vs, score01_desc, score01_guest_name, score01_guest_score, qxc_issue, qxc01_number, ks_name, ssc_name, bjsc_name, tv_expert_title;
    private TextView data_info_title01, data_info_date01, hk_issue, hk_hk01, hk_zodiacs01, hk_hk02, hk_zodiacs02, hk_hk03, qxc02_number, klsf_name, qxc03_number, hk_name, qxc_name, qxc07_number, tv_expert_subtitle;
    private TextView hk_zodiacs03, hk_hk04, hk_zodiacs04, hk_hk05, hk_zodiacs05, hk_hk06, hk_zodiacs06, hk_hk07, hk_zodiacs07, bjsc_issue, klsf_issue, klsf01_number, klsf02_number, klsf03_number, klsf04_number, tv_expert_info_source;
    private TextView klsf05_number, klsf06_number, klsf07_number, klsf08_number, ks_issue, ssc_issue, ssc01_number, ssc02_number, ssc03_number, ssc04_number, ssc05_number, qxc04_number, qxc05_number, qxc06_number;
    private List<TextView> hk_numbers, hk_zodiacs, qxc_numbers, klsf_numbers, ssc_numbers;// 彩种号码View集合
    private List<ImageView> bjsc_numbers, ks_numbers;

    private List<View> scoreViewList = new ArrayList<>();// 热门赛事条目集合
    private List<View> dataInfoViewList = new ArrayList<>();// 热门资讯条目集合
    private List<View> lotteryViewList = new ArrayList<>();// 彩票条目集合
    private List<View> expertViewList = new ArrayList<>();// 专家专栏条目集合

    private final int MIN_CLICK_DELAY_TIME = 1000;// 控件点击间隔时间
    private long lastClickTime = 0;

    private List<Fragment> fragmentList = new ArrayList<>();
    private int circularSize = 0;// 入口小圆点
    private List<View> lottery_item_list;// 新彩票条目集合
    private List<TextView> lottery_item_name_list;// 新彩票入口名称
    private List<TextView> lottery_item_desc_list;// 新彩票入口描述
    private List<ImageView> lottery_item_icon_list;// 新彩票入口图标
    private View lotteryItemView;// 新彩票条目
    private LinearLayout ll_lottery_item_title;
    /**
     * 产品建议的view
     */
    private View mProductItemView;
    ImageView adviceUserIcon;
    TextView adviceUserName;
    TextView adviceUserTime;
    ImageView adviceImgUserLike;
    TextView adviceTextUserLike;
    TextView adviceUserContent;
    TextView adviceProductReply;

    private ProductListener mProductListener;


    /**
     * 构造
     *
     * @param context         上下文
     * @param homePagerEntity 首页实体对象
     */
    public HomeListBaseAdapter(Context context, HomePagerEntity homePagerEntity) {
        this.mContext = context;
        this.mHomePagerEntity = homePagerEntity;

        initData();
        init();// 初始化
        initEvent();// 初始化事件
    }

    private void initData() {
        int size = 0;
        if (mHomePagerEntity == null || mHomePagerEntity.getMenus() == null || mHomePagerEntity.getMenus().getContent() == null || mHomePagerEntity.getMenus().getContent().size() == 0) {

        } else {
            circularSize = mHomePagerEntity.getMenus().getContent().size() % 8 == 0 ? mHomePagerEntity.getMenus().getContent().size() / 8 : mHomePagerEntity.getMenus().getContent().size() / 8 + 1;
            size = mHomePagerEntity.getMenus().getContent().size();
        }
        for (int i = 0; i < circularSize; i++) {
            List<HomeContentEntity> list = new ArrayList<>();
            int startIndex = i * 8;
            int endIndex = (i + 1) * 8 <= size ? (i + 1) * 8 : size;
            for (int j = startIndex; j < endIndex; j++) {
                list.add(mHomePagerEntity.getMenus().getContent().get(j));
            }
            fragmentList.add(HomeMuenFragment.newInstance(list));
        }
        L.d("xxx", "fragmentList:" + fragmentList.size());
    }

    public void start() {
        if (mPagerAdapter != null) {
            mPagerAdapter.start();
        }
    }

    public void end() {
        if (mPagerAdapter != null) {
            mPagerAdapter.end();
        }
    }


    /**
     * 初始化事件
     */
    private void initEvent() {
        try {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                for (int i = 0, len = mHomePagerEntity.getOtherLists().size(); i < len; i++) {
                    HomeOtherListsEntity homeOtherListsEntity = mHomePagerEntity.getOtherLists().get(i);
                    List<HomeBodysEntity> bodys = homeOtherListsEntity.getContent().getBodys();
                    int labType = homeOtherListsEntity.getContent().getLabType();// 条目类型
                    for (int j = 0, len1 = bodys.size(); j < len1; j++) {
                        final int jumpType = bodys.get(j).getJumpType();// 跳转类型
                        final String jumpAddr = bodys.get(j).getJumpAddr();// 跳转地址
                        final String thirdId = bodys.get(j).getThirdId();// 赛事ID
                        final String leagueId = bodys.get(j).getLeagueId();
//                        final String isRelateMatch = bodys.get(j).getRelateMatch();// 是否有关联赛事
                        final int type = bodys.get(j).getType();// 关联赛事类型
                        final int matchType = bodys.get(j).getMatchType();// 关联赛事类型-->专家专栏的
                        final String infoTypeName = bodys.get(j).getInfoTypeName();// 赛事类型名
                        final String imageurl = bodys.get(j).getPicUrl();// 分享图片Url
                        final String title = bodys.get(j).getTitle();// 分享标题
                        final String summary = bodys.get(j).getSummary();// 分享摘要
                        final String lotteryName = bodys.get(j).getName();// 彩票name
                        final List<HomeBodysLottery> lotteryList = bodys.get(j).getLottery();// 彩票Item
                        final String status = bodys.get(j).getStatusOrigin();// 比赛状态
                        switch (labType) {
                            case 1:// 热门赛事
                                scoreViewList.get(j).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        MobclickAgent.onEvent(mContext, "HomePager_Competition_Item");
                                        if (!TextUtils.isEmpty(jumpAddr)) {
                                            switch (jumpType) {
                                                case 0:// 无
                                                    break;
                                                case 1:// 页面
                                                {
                                                    Intent intent = new Intent(mContext, WebActivity.class);
                                                    intent.putExtra("key", jumpAddr);
                                                    mContext.startActivity(intent);
                                                    break;
                                                }
                                                case 2:// 内页
                                                {
                                                    if ("13".equals(jumpAddr)) {// 足球内页13

                                                        if (HandMatchId.handId(mContext, thirdId)) {
                                                            Intent intent = new Intent(mContext, FootballMatchDetailActivity.class);
                                                            intent.putExtra("thirdId", thirdId);
                                                            intent.putExtra("currentFragmentId", -1);
                                                            intent.putExtra(FootBallDetailTypeEnum.CURRENT_TAB_KEY, "0".equals(status) ? FootBallDetailTypeEnum.FOOT_DETAIL_ANALYSIS : FootBallDetailTypeEnum.FOOT_DETAIL_LIVE);
                                                            mContext.startActivity(intent);
                                                        }
                                                    } else if ("20".equals(jumpAddr)) {// 篮球内页20
                                                        Intent intent = new Intent(mContext, BasketDetailsActivityTest.class);
                                                        intent.putExtra("thirdId", thirdId);
                                                        intent.putExtra("leagueId", leagueId);
                                                        mContext.startActivity(intent);
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                });
                                break;
                            case 2:// 热门资讯
                                dataInfoViewList.get(j).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        MobclickAgent.onEvent(mContext, "HomePager_Date_Item");
                                        if (!TextUtils.isEmpty(jumpAddr)) {
                                            switch (jumpType) {
                                                case 0:// 无
                                                    break;
                                                case 1:// 页面
                                                {

                                                    Intent intent = new Intent(mContext, WebActivity.class);
                                                    intent.putExtra("key", jumpAddr);
                                                    intent.putExtra("imageurl", imageurl);
                                                    intent.putExtra("title", title);// 分享标题
                                                    intent.putExtra("subtitle", summary);
                                                    intent.putExtra("infoTypeName", infoTypeName);
//                                                    if ("true".equals(isRelateMatch)) {
                                                    intent.putExtra("type", type);
                                                    intent.putExtra("thirdId", thirdId);
//                                                    }
                                                    mContext.startActivity(intent);
                                                    break;
                                                }
                                                case 2:// 内页
                                                {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                });
                                break;
                            case 3:// 彩票开奖
                                lotteryViewList.get(j).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        MobclickAgent.onEvent(mContext, "HomePager_Lottery_Item");
                                        if (!TextUtils.isEmpty(jumpAddr)) {
                                            switch (jumpType) {
                                                case 0:// 无
                                                    break;
                                                case 1:// 页面
                                                {
                                                    Intent intent = new Intent(mContext, WebActivity.class);
                                                    intent.putExtra("key", jumpAddr);
                                                    mContext.startActivity(intent);
                                                    break;
                                                }
                                                case 2:// 内页
                                                {
                                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
//                                                    String numberName = jumpAddr.substring(1, jumpAddr.toCharArray().length);
                                                    intent.putExtra("numberName", lotteryName);
                                                    mContext.startActivity(intent);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                });
                                break;
                            case 7:// 1.2.0版新增彩票入口条目
                                ll_lottery_item_title.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        MobclickAgent.onEvent(mContext, "HomePager_New_Lottery_Item");
                                        mContext.startActivity(new Intent(mContext, NumbersActivity.class));
                                    }
                                });
                                for (int m = 0, lenm = lottery_item_list.size(); m < lenm; m++) {
                                    final int index = m;
                                    lottery_item_list.get(m).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            MobclickAgent.onEvent(mContext, "HomePager_New_Lottery_Item_Info");
                                            if (index < lotteryList.size()) {
                                                HomeBodysLottery lottery = lotteryList.get(index);
                                                switch (lottery.getJumpType()) {
                                                    case 0:// 无
                                                        break;
                                                    case 1:// 页面
                                                    {
                                                        Intent intent = new Intent(mContext, WebActivity.class);
                                                        intent.putExtra("key", lottery.getJumpAddr());
                                                        mContext.startActivity(intent);
                                                        break;
                                                    }
                                                    case 2:// 内页
                                                    {
                                                        Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                                        intent.putExtra("numberName", lottery.getName());
                                                        mContext.startActivity(intent);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                                break;
                            case 4:
                                expertViewList.get(j).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!TextUtils.isEmpty(jumpAddr)) {
                                            switch (jumpType) {
                                                case 0:// 无
                                                    break;
                                                case 1:// 页面
                                                {
                                                    Intent intent = new Intent(mContext, WebActivity.class);
                                                    intent.putExtra("key", jumpAddr);
                                                    intent.putExtra("imageurl", imageurl);
                                                    intent.putExtra("title", title);// 分享标题
                                                    intent.putExtra("subtitle", summary);
                                                    intent.putExtra("infoTypeName", infoTypeName);
//                                                    if ("true".equals(isRelateMatch)) {
                                                    intent.putExtra("type", matchType == 0 ? 2 : matchType);
                                                    intent.putExtra("thirdId", thirdId);
//                                                    }
                                                    mContext.startActivity(intent);
                                                    break;
                                                }
                                                case 2:// 内页
                                                    break;
                                            }
                                        }
                                    }
                                });
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            L.d("initEvent初始化失败：" + e.getMessage());
        }
    }

    /**
     * 获取热门赛事条目
     *
     * @return 热门赛事条目
     */
    private View getScoreView() {
        View view = View.inflate(mContext, R.layout.home_page_item_score_item, null);
        score01_title = (TextView) view.findViewById(R.id.tv_game_title);
        score01_icon = (ImageView) view.findViewById(R.id.iv_game_icon_bottom);
        score01_home_icon = (ImageView) view.findViewById(R.id.iv_game_icon_home);
        score01_home_name = (TextView) view.findViewById(R.id.tv_game_name_home);
        score01_home_score = (TextView) view.findViewById(R.id.tv_game_score_home);
        score01_vs = (TextView) view.findViewById(R.id.tv_game_score_vs);
        score01_desc = (TextView) view.findViewById(R.id.tv_game_score_desc);
        score01_guest_icon = (ImageView) view.findViewById(R.id.iv_game_icon_guest);
        score01_guest_name = (TextView) view.findViewById(R.id.tv_game_name_guest);
        score01_guest_score = (TextView) view.findViewById(R.id.tv_game_score_guest);
        return view;
    }

    /**
     * 获取热门资讯条目
     *
     * @return 热门资讯条目
     */
    private View getDataInfoView() {
        View view = View.inflate(mContext, R.layout.home_page_item_data_info_item, null);
        data_info_icon01 = (ImageView) view.findViewById(R.id.iv_data_info_icon01);
        data_info_title01 = (TextView) view.findViewById(R.id.tv_data_info_title01);
        data_info_date01 = (TextView) view.findViewById(R.id.tv_data_info_date01);
        return view;
    }

    /**
     * 获取专家专栏条目_1.2.2版
     *
     * @return 专家专栏条目
     */
    private View getExpertView() {
        View view = View.inflate(mContext, R.layout.home_page_expert_item, null);
        iv_expert_icon = (ImageView) view.findViewById(R.id.iv_expert_icon);
        tv_expert_title = (TextView) view.findViewById(R.id.tv_expert_title);
        tv_expert_subtitle = (TextView) view.findViewById(R.id.tv_expert_sub_title);
        tv_expert_info_source = (TextView) view.findViewById(R.id.tv_expert_info_source);
        return view;
    }

    /**
     * 获取1.2.0版新加彩票入口条目
     *
     * @return 开奖查询
     */
    private View getLotteryItemView() {
        View view = View.inflate(mContext, R.layout.home_page_item_lottery, null);
        ll_lottery_item_title = (LinearLayout) view.findViewById(R.id.ll_lottery_item_title);
        TextView home_lottery_item_name1 = (TextView) view.findViewById(R.id.home_lottery_item_name1);
        TextView home_lottery_item_name2 = (TextView) view.findViewById(R.id.home_lottery_item_name2);
        TextView home_lottery_item_name3 = (TextView) view.findViewById(R.id.home_lottery_item_name3);
        TextView home_lottery_item_name4 = (TextView) view.findViewById(R.id.home_lottery_item_name4);
        TextView home_lottery_item_name5 = (TextView) view.findViewById(R.id.home_lottery_item_name5);
        TextView home_lottery_item_desc2 = (TextView) view.findViewById(R.id.home_lottery_item_desc2);
        TextView home_lottery_item_desc3 = (TextView) view.findViewById(R.id.home_lottery_item_desc3);
        TextView home_lottery_item_desc4 = (TextView) view.findViewById(R.id.home_lottery_item_desc4);
        TextView home_lottery_item_desc5 = (TextView) view.findViewById(R.id.home_lottery_item_desc5);
        ImageView home_lottery_item_icon1 = (ImageView) view.findViewById(R.id.home_lottery_item_icon1);
        ImageView home_lottery_item_icon2 = (ImageView) view.findViewById(R.id.home_lottery_item_icon2);
        ImageView home_lottery_item_icon3 = (ImageView) view.findViewById(R.id.home_lottery_item_icon3);
        ImageView home_lottery_item_icon4 = (ImageView) view.findViewById(R.id.home_lottery_item_icon4);
        ImageView home_lottery_item_icon5 = (ImageView) view.findViewById(R.id.home_lottery_item_icon5);
        lottery_item_list = new ArrayList<>();
        lottery_item_list.add(view.findViewById(R.id.rl_lottery_item1));
        lottery_item_list.add(view.findViewById(R.id.ll_lottery_item2));
        lottery_item_list.add(view.findViewById(R.id.ll_lottery_item3));
        lottery_item_list.add(view.findViewById(R.id.ll_lottery_item4));
        lottery_item_list.add(view.findViewById(R.id.ll_lottery_item5));
        lottery_item_name_list = new ArrayList<>();
        lottery_item_name_list.add(home_lottery_item_name1);
        lottery_item_name_list.add(home_lottery_item_name2);
        lottery_item_name_list.add(home_lottery_item_name3);
        lottery_item_name_list.add(home_lottery_item_name4);
        lottery_item_name_list.add(home_lottery_item_name5);
        lottery_item_desc_list = new ArrayList<>();
        lottery_item_desc_list.add(null);// 第一条彩种没有描述信息
        lottery_item_desc_list.add(home_lottery_item_desc2);
        lottery_item_desc_list.add(home_lottery_item_desc3);
        lottery_item_desc_list.add(home_lottery_item_desc4);
        lottery_item_desc_list.add(home_lottery_item_desc5);
        lottery_item_icon_list = new ArrayList<>();
        lottery_item_icon_list.add(home_lottery_item_icon1);
        lottery_item_icon_list.add(home_lottery_item_icon2);
        lottery_item_icon_list.add(home_lottery_item_icon3);
        lottery_item_icon_list.add(home_lottery_item_icon4);
        lottery_item_icon_list.add(home_lottery_item_icon5);
        return view;
    }

    /**
     * 获取产品建议View
     */
    private View getmProductItemView() {
        View view = View.inflate(mContext, R.layout.item_advice, null);
        adviceUserIcon = (ImageView) view.findViewById(R.id.advice_user_icon);
        adviceUserName = (TextView) view.findViewById(R.id.advice_user_name);
        adviceUserTime = (TextView) view.findViewById(R.id.advice_user_time);
        adviceImgUserLike = (ImageView) view.findViewById(R.id.advice_user_img_like);
        adviceTextUserLike = (TextView) view.findViewById(R.id.advice_user_like);
        adviceUserContent = (TextView) view.findViewById(R.id.advice_user_content);
        adviceProductReply = (TextView) view.findViewById(R.id.advice_product_reply);

        return view;
    }

    /**
     * 获取香港开奖条目
     *
     * @return 香港开奖条目
     */
    private View getLotteryHKView() {
        View view = View.inflate(mContext, R.layout.home_page_item_number_hk, null);
        hk_icon = (ImageView) view.findViewById(R.id.iv_number_zodiacs_icon);
        hk_name = (TextView) view.findViewById(R.id.tv_number_name_hk_home);
        hk_issue = (TextView) view.findViewById(R.id.tv_number_issue_hk_home);
        hk_hk01 = (TextView) view.findViewById(R.id.tv_number_hk01_home);
        hk_zodiacs01 = (TextView) view.findViewById(R.id.tv_number_zodiacs01_home);
        hk_hk02 = (TextView) view.findViewById(R.id.tv_number_hk02_home);
        hk_zodiacs02 = (TextView) view.findViewById(R.id.tv_number_zodiacs02_home);
        hk_hk03 = (TextView) view.findViewById(R.id.tv_number_hk03_home);
        hk_zodiacs03 = (TextView) view.findViewById(R.id.tv_number_zodiacs03_home);
        hk_hk04 = (TextView) view.findViewById(R.id.tv_number_hk04_home);
        hk_zodiacs04 = (TextView) view.findViewById(R.id.tv_number_zodiacs04_home);
        hk_hk05 = (TextView) view.findViewById(R.id.tv_number_hk05_home);
        hk_zodiacs05 = (TextView) view.findViewById(R.id.tv_number_zodiacs05_home);
        hk_hk06 = (TextView) view.findViewById(R.id.tv_number_hk06_home);
        hk_zodiacs06 = (TextView) view.findViewById(R.id.tv_number_zodiacs06_home);
        hk_hk07 = (TextView) view.findViewById(R.id.tv_number_hk07_home);
        hk_zodiacs07 = (TextView) view.findViewById(R.id.tv_number_zodiacs07_home);

        hk_numbers = new ArrayList<>();
        hk_numbers.add(hk_hk01);
        hk_numbers.add(hk_hk02);
        hk_numbers.add(hk_hk03);
        hk_numbers.add(hk_hk04);
        hk_numbers.add(hk_hk05);
        hk_numbers.add(hk_hk06);
        hk_numbers.add(hk_hk07);

        hk_zodiacs = new ArrayList<>();
        hk_zodiacs.add(hk_zodiacs01);
        hk_zodiacs.add(hk_zodiacs02);
        hk_zodiacs.add(hk_zodiacs03);
        hk_zodiacs.add(hk_zodiacs04);
        hk_zodiacs.add(hk_zodiacs05);
        hk_zodiacs.add(hk_zodiacs06);
        hk_zodiacs.add(hk_zodiacs07);
        return view;
    }

    /**
     * 获取七星彩条目
     *
     * @return 七星彩条目
     */
    private View getLotteryyQXCView() {
        View view = View.inflate(mContext, R.layout.home_page_item_number_qxc, null);
        qxc_icon = (ImageView) view.findViewById(R.id.iv_number_qxc_home);
        qxc_name = (TextView) view.findViewById(R.id.tv_number_qxc_name_home);
        qxc_issue = (TextView) view.findViewById(R.id.tv_number_issue_qxc_home);
        qxc01_number = (TextView) view.findViewById(R.id.tv_number_qxc01_home);
        qxc02_number = (TextView) view.findViewById(R.id.tv_number_qxc02_home);
        qxc03_number = (TextView) view.findViewById(R.id.tv_number_qxc03_home);
        qxc04_number = (TextView) view.findViewById(R.id.tv_number_qxc04_home);
        qxc05_number = (TextView) view.findViewById(R.id.tv_number_qxc05_home);
        qxc06_number = (TextView) view.findViewById(R.id.tv_number_qxc06_home);
        qxc07_number = (TextView) view.findViewById(R.id.tv_number_qxc07_home);
        qxc_numbers = new ArrayList<>();
        qxc_numbers.add(qxc01_number);
        qxc_numbers.add(qxc02_number);
        qxc_numbers.add(qxc03_number);
        qxc_numbers.add(qxc04_number);
        qxc_numbers.add(qxc05_number);
        qxc_numbers.add(qxc06_number);
        qxc_numbers.add(qxc07_number);
        return view;
    }

    /**
     * 获取北京赛车条目
     *
     * @return 北京赛车条目
     */
    private View getLotteryBJSCView() {
        View view = View.inflate(mContext, R.layout.home_page_item_number_bjsc, null);
        bjsc_icon = (ImageView) view.findViewById(R.id.iv_number_bjsc_icon);
        bjsc_name = (TextView) view.findViewById(R.id.tv_number_bjsc_name);
        bjsc_issue = (TextView) view.findViewById(R.id.tv_number_issue_bjsc_home);
        bjsc01_number = (ImageView) view.findViewById(R.id.iv_number_bjsc01_home);
        bjsc02_number = (ImageView) view.findViewById(R.id.iv_number_bjsc02_home);
        bjsc03_number = (ImageView) view.findViewById(R.id.iv_number_bjsc03_home);
        bjsc04_number = (ImageView) view.findViewById(R.id.iv_number_bjsc04_home);
        bjsc05_number = (ImageView) view.findViewById(R.id.iv_number_bjsc05_home);
        bjsc06_number = (ImageView) view.findViewById(R.id.iv_number_bjsc06_home);
        bjsc07_number = (ImageView) view.findViewById(R.id.iv_number_bjsc07_home);
        bjsc08_number = (ImageView) view.findViewById(R.id.iv_number_bjsc08_home);
        bjsc09_number = (ImageView) view.findViewById(R.id.iv_number_bjsc09_home);
        bjsc10_number = (ImageView) view.findViewById(R.id.iv_number_bjsc10_home);
        bjsc_numbers = new ArrayList<>();
        bjsc_numbers.add(bjsc01_number);
        bjsc_numbers.add(bjsc02_number);
        bjsc_numbers.add(bjsc03_number);
        bjsc_numbers.add(bjsc04_number);
        bjsc_numbers.add(bjsc05_number);
        bjsc_numbers.add(bjsc06_number);
        bjsc_numbers.add(bjsc07_number);
        bjsc_numbers.add(bjsc08_number);
        bjsc_numbers.add(bjsc09_number);
        bjsc_numbers.add(bjsc10_number);
        return view;
    }

    /**
     * 获取快乐十分条目
     *
     * @return 快乐十分条目
     */
    private View getLotteryKLSFView() {
        View view = View.inflate(mContext, R.layout.home_page_item_number_klsf, null);
        klsf_icon = (ImageView) view.findViewById(R.id.iv_number_klsf_home);
        klsf_name = (TextView) view.findViewById(R.id.tv_number_klsf_name_home);
        klsf_issue = (TextView) view.findViewById(R.id.tv_number_issue_klsf_home);
        klsf01_number = (TextView) view.findViewById(R.id.tv_number_klsf01_home);
        klsf02_number = (TextView) view.findViewById(R.id.tv_number_klsf02_home);
        klsf03_number = (TextView) view.findViewById(R.id.tv_number_klsf03_home);
        klsf04_number = (TextView) view.findViewById(R.id.tv_number_klsf04_home);
        klsf05_number = (TextView) view.findViewById(R.id.tv_number_klsf05_home);
        klsf06_number = (TextView) view.findViewById(R.id.tv_number_klsf06_home);
        klsf07_number = (TextView) view.findViewById(R.id.tv_number_klsf07_home);
        klsf08_number = (TextView) view.findViewById(R.id.tv_number_klsf08_home);
        klsf_numbers = new ArrayList<>();
        klsf_numbers.add(klsf01_number);
        klsf_numbers.add(klsf02_number);
        klsf_numbers.add(klsf03_number);
        klsf_numbers.add(klsf04_number);
        klsf_numbers.add(klsf05_number);
        klsf_numbers.add(klsf06_number);
        klsf_numbers.add(klsf07_number);
        klsf_numbers.add(klsf08_number);
        return view;
    }

    /**
     * 获取快三条目
     *
     * @return 快三条目
     */
    private View getLotteryKSView() {
        View view = View.inflate(mContext, R.layout.home_page_item_number_ks, null);
        ks_icon = (ImageView) view.findViewById(R.id.iv_number_ks_home);
        ks_name = (TextView) view.findViewById(R.id.tv_number_ks_name_home);
        ks_issue = (TextView) view.findViewById(R.id.tv_number_issue_ks_home);
        ks01_number = (ImageView) view.findViewById(R.id.iv_number_ks01_home);
        ks02_number = (ImageView) view.findViewById(R.id.iv_number_ks02_home);
        ks03_number = (ImageView) view.findViewById(R.id.iv_number_ks03_home);
        ks_numbers = new ArrayList<>();
        ks_numbers.add(ks01_number);
        ks_numbers.add(ks02_number);
        ks_numbers.add(ks03_number);
        return view;
    }

    /**
     * 获取时时彩和十一选五条目
     *
     * @return 时时彩和十一选五条目
     */
    private View getLotterySSCView() {
        View view = View.inflate(mContext, R.layout.home_page_item_number_ssc, null);
        ssc_icon = (ImageView) view.findViewById(R.id.iv_number_ssc_home);
        ssc_name = (TextView) view.findViewById(R.id.tv_number_ssc_name_home);
        ssc_issue = (TextView) view.findViewById(R.id.tv_number_issue_ssc_home);
        ssc01_number = (TextView) view.findViewById(R.id.tv_number_ssc01_home);
        ssc02_number = (TextView) view.findViewById(R.id.tv_number_ssc02_home);
        ssc03_number = (TextView) view.findViewById(R.id.tv_number_ssc03_home);
        ssc04_number = (TextView) view.findViewById(R.id.tv_number_ssc04_home);
        ssc05_number = (TextView) view.findViewById(R.id.tv_number_ssc05_home);
        ssc_numbers = new ArrayList<>();
        ssc_numbers.add(ssc01_number);
        ssc_numbers.add(ssc02_number);
        ssc_numbers.add(ssc03_number);
        ssc_numbers.add(ssc04_number);
        ssc_numbers.add(ssc05_number);
        return view;
    }

    /**
     * 获取默认开奖条目
     *
     * @return 默认开奖条目
     */
    private View getLotteryDefView() {
        return View.inflate(mContext, R.layout.home_page_item_number_def, null);
    }

    /**
     * 初始化数据
     */
    private void init() {
        try {
            for (int i = 0, len = mHomePagerEntity.getOtherLists().size(); i < len; i++) {
                int labType = mHomePagerEntity.getOtherLists().get(i).getContent().getLabType();// 获取类型
                List<HomeBodysEntity> bodys = mHomePagerEntity.getOtherLists().get(i).getContent().getBodys();
                for (int j = 0, len1 = bodys.size(); j < len1; j++) {
                    HomeBodysEntity homeBodysEntity = bodys.get(j);
                    switch (labType) {
                        case 1:// 1、	热门赛事
                        {
                            View scoreView = getScoreView();
                            scoreViewList.add(scoreView);
                            if ("13".equals(homeBodysEntity.getJumpAddr())) {// 足球比分
                                score01_icon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.home_foot_def_bg));// 设置背景图片
                                switch (homeBodysEntity.getStatusOrigin()) {
                                    case "-1":// 完场
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.score_red), "—", mContext.getResources().getString(R.string.fragme_home_wanchang_text));
                                        break;
                                    case "0":// 未开
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.content_txt_light_grad), "VS", null);
                                        break;
                                    case "1":// 上半场
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.fragme_home_shangbanchang_text));
                                        break;
                                    case "2":// 中场
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.fragme_home_zhongchang_text));
                                        break;
                                    case "3":// 下半场
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.fragme_home_xiabanchang_text));
                                        break;
                                    case "4":// 加时
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.fottball_home_jiashi));
                                        break;
                                    case "5":// 点球
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.fottball_home_dianqiu));
                                        break;
                                    case "-10":// 取消
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.fottball_home_quxiao));
                                        break;
                                    case "-11":// 待定
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.fottball_home_daiding));
                                        break;
                                    case "-12":// 腰斩
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.fottball_home_yaozhan));
                                        break;
                                    case "-13":// 中断
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.fottball_home_zhongduan));
                                        break;
                                    case "-14":// 推迟
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.fottball_home_tuichi));
                                        break;
                                    case "-100":// 取消直播
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.fottball_home_quxiaozhibo));
                                        break;
                                }
                                score01_title.setText(homeBodysEntity.getRacename());// 设置赛事名称
                                score01_title.setTextColor(Color.parseColor(homeBodysEntity.getRaceColor()));// 标题颜色
                                score01_home_name.setText(homeBodysEntity.getHometeam());// 设置主队队名
                                try {
                                    Glide.with(MyApp.getContext()).load(homeBodysEntity.getHomeLogoUrl()).error(R.mipmap.home_score_item_icon_def).into(score01_home_icon);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                score01_guest_name.setText(homeBodysEntity.getGuestteam());// 设置客队队名
                                try {
                                    Glide.with(MyApp.getContext()).load(homeBodysEntity.getGuestLogoUrl()).error(R.mipmap.home_score_item_icon_def).into(score01_guest_icon);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if ("20".equals(homeBodysEntity.getJumpAddr())) {// 篮球比分
                                score01_icon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.home_basket_def_bg));// 设置背景图片
                                switch (homeBodysEntity.getMatchStatus()) {
                                    //比赛状态 0:未开赛,1:一节,2:二节,5:1'OT，以此类推，-1:完场,-2:待定,-3:中断,-4:取消,-5:推迟,50中场
                                    case 0:// 未开赛
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.content_txt_light_grad), "VS", null);
                                        break;
                                    case 1:// 第一节
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.basket_home_diyijie));
                                        break;
                                    case 2:// 第二节
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.basket_home_dierjie));
                                        break;
                                    case 3:// 第三节
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.basket_home_disanjie));
                                        break;
                                    case 4:// 第四节
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.basket_home_dishijie));
                                        break;
                                    case 5:// 1'OT
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", "1'OT");
                                        break;
                                    case 6:// 2'OT
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", "2'OT");
                                        break;
                                    case 7:// 3'OT
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", "3'OT");
                                        break;
                                    case 8:// 4'OT
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", "4'OT");
                                        break;
                                    case 9:// 5'OT
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", "5'OT");
                                        break;
                                    case -1:// 完场
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.score_red), "—", mContext.getResources().getString(R.string.fragme_home_wanchang_text));
                                        break;
                                    case -2:// 待定
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.basket_undetermined));
                                        break;
                                    case -3:// 中断
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.basket_interrupt));
                                        break;
                                    case -4:// 取消
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.basket_cancel));
                                        break;
                                    case -5:// 推迟
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.basket_postpone));
                                        break;
                                    case 50:// 中场
                                        settingScoreItemData(homeBodysEntity, mContext.getResources().getColor(R.color.colorPrimary), "—", mContext.getResources().getString(R.string.fragme_home_zhongchang_text));
                                        break;

                                }
                                score01_title.setText(homeBodysEntity.getLeagueName());// 设置赛事名称
                                score01_title.setTextColor(Color.parseColor(homeBodysEntity.getLeagueColor()));// 标题颜色
                                // 篮球客队在前主队在后
                                score01_guest_name.setText(homeBodysEntity.getHometeam());// 设置主队队名
                                try {
                                    Glide.with(MyApp.getContext()).load(homeBodysEntity.getHomeLogoUrl()).error(R.mipmap.home_score_item_icon_def).into(score01_guest_icon);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                score01_home_name.setText(homeBodysEntity.getGuestteam());// 设置客队队名
                                try {
                                    Glide.with(MyApp.getContext()).load(homeBodysEntity.getGuestLogoUrl()).error(R.mipmap.home_score_item_icon_def).into(score01_home_icon);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                        case 2:// 2、	热点资讯
                        {
                            View dataInfoView = getDataInfoView();// 获取布局对象
                            dataInfoViewList.add(dataInfoView);

                            try {
                                Glide.with(MyApp.getContext()).load(homeBodysEntity.getPicUrl()).error(R.mipmap.home_data_info_def).into(data_info_icon01);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Date curDate = new Date(System.currentTimeMillis());// 获取当前日期
                            String mDate = DateUtil.formatDate(curDate);
                            if (!TextUtils.isEmpty(homeBodysEntity.getDate())) {
                                if (mDate.equals(homeBodysEntity.getDate())) {// 设置时间
                                    data_info_date01.setText(mContext.getResources().getString(R.string.home_pager_current_date) + " " + homeBodysEntity.getTime());
                                } else {
                                    try {
                                        data_info_date01.setText(DateUtil.convertDateToNationMD(DateUtil.getAssignDate(homeBodysEntity.getDate())) + " " + homeBodysEntity.getTime());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        data_info_date01.setText("");
                                    }
                                }
                            }
                            data_info_title01.setText(homeBodysEntity.getTitle());// 设置标题
                        }
                        break;
                        case 3:// 3、	彩票开奖
                        {

                            switch (homeBodysEntity.getName()) {
                                case "1":// 香港彩
                                    lotteryHKAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_hk));
                                    break;
                                case "6":// 七星彩
                                    lotteryQXCAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_qxc));
                                    break;
                                case "15":// 北京赛车
                                    lotteryBJSCAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_bj_sc));
                                    break;
                                case "8":// 广东快乐十分
                                    lotteryKLSFAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_gd_klsf));
                                    break;
                                case "11":// 湖南快乐十分
                                    lotteryKLSFAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_hn_klsf));
                                    break;
                                case "19":// 幸运农场
                                    lotteryKLSFAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_xylc));
                                    break;
                                case "2":// 重庆时时彩
                                    lotterySSCAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_cq_ssc));
                                    break;
                                case "3":// 江西时时彩
                                    lotterySSCAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_jx_ssc));
                                    break;
                                case "4":// 新疆时时彩
                                    lotterySSCAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_xj_ssc));
                                    break;
                                case "5":// 云南时时彩
                                    lotterySSCAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_yn_ssc));
                                    break;
                                case "23":// 天津时时彩
                                    lotterySSCAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_tj_ssc));
                                    break;
                                case "7":// 广东十一选五
                                    lotterySSCAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_gd_syxw));
                                    break;
                                case "9":// 湖北十一选五
                                    lotterySSCAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_hb_syxw));
                                    break;
                                case "20":// 江苏十一选五
                                    lotterySSCAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_js_syxw));
                                    break;
                                case "21":// 江西十一选五
                                    lotterySSCAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_jx_syxw));
                                    break;
                                case "22":// 山东十一选五
                                    lotterySSCAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_sd_syxw));
                                    break;
                                case "10":// 安徽快3
                                    lotteryKSAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_ah_ks));
                                    break;
                                case "16":// 江苏快3
                                    lotteryKSAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_js_ks));
                                    break;
                                case "18":// 广西快3
                                    lotteryKSAddView(homeBodysEntity, mContext.getResources().getString(R.string.number_cz_gx_ks));
                                    break;
                            }
                        }
                        break;
                        case 7:// 1.2.0版新彩票条目入口
                            // 初始化控件
                            lotteryItemView = getLotteryItemView();

                            // 设置控件数据
                            for (int k = 0, lens = bodys.get(j).getLottery().size(); k < lens; k++) {
                                HomeBodysLottery mLottery = bodys.get(j).getLottery().get(k);
                                NumberDataUtils.setTextTitle(mContext, lottery_item_name_list.get(k), mLottery.getName());
                                try {
                                    ImageLoader.loadFitCenter(mContext, mLottery.getPicUrl(), R.mipmap.home_number_item_icon_def).into(lottery_item_icon_list.get(k));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (k != 0) {
                                    switch (mLottery.getName()) {
                                        case "24":// 双色球
                                        case "29":// 大乐透
                                            // 显示奖金池 用亿做单位保留两们小数
                                            String bonus;
                                            if (!TextUtils.isEmpty(mLottery.getJackpot())) {
                                                if (mLottery.getJackpot().length() >= 9) {
                                                    Double str = Double.parseDouble(mLottery.getJackpot()) / 100000000;
                                                    DecimalFormat df = new DecimalFormat("0.00");
                                                    bonus = df.format(str);
                                                    lottery_item_desc_list.get(k).setText(TextUtils.isEmpty(bonus) ? mContext.getResources().getString(R.string.home_number_desc_bonus) : mContext.getResources().getString(R.string.home_number_desc_bonus) + bonus + mContext.getResources().getString(R.string.home_number_desc_yi));
                                                } else {
                                                    Double str = Double.parseDouble(mLottery.getJackpot()) / 10000;
                                                    DecimalFormat df = new DecimalFormat("0.00");
                                                    bonus = df.format(str);
                                                    lottery_item_desc_list.get(k).setText(TextUtils.isEmpty(bonus) ? mContext.getResources().getString(R.string.home_number_desc_bonus) : mContext.getResources().getString(R.string.home_number_desc_bonus) + bonus + mContext.getResources().getString(R.string.home_number_desc_wan));
                                                }
                                            } else {
                                                lottery_item_desc_list.get(k).setText(mContext.getResources().getString(R.string.home_number_desc_bonus));
                                            }
                                            break;
                                        case "6":// 七星彩
                                        case "28":// 七乐彩
                                        case "30":
                                        case "31":
                                        case "32":
                                            // 显示奖金池 用万做单位保留两们小数
                                            String bonus1 = null;
                                            if (!TextUtils.isEmpty(mLottery.getJackpot())) {
                                                Double str = Double.parseDouble(mLottery.getJackpot()) / 10000;
                                                DecimalFormat df = new DecimalFormat("0.00");
                                                bonus1 = df.format(str);
                                            }
                                            lottery_item_desc_list.get(k).setText(TextUtils.isEmpty(bonus1) ? mContext.getResources().getString(R.string.home_number_desc_bonus) : mContext.getResources().getString(R.string.home_number_desc_bonus) + bonus1 + mContext.getResources().getString(R.string.home_number_desc_wan));
                                            break;
                                        default:
                                            // 其它没有奖金池 显示描述信息
                                            NumberDataUtils.numberAddDesc(mContext, lottery_item_desc_list.get(k), mLottery.getName());
                                            break;
                                    }
                                }
                            }
                            break;
                        case 5: //产品建议
                            addLike(bodys.get(0), bodys); //只有一条数据
                            break;
                        case 4:// 专家专栏
                        {
                            View expertView = getExpertView();// 获取布局对象
                            expertViewList.add(expertView);
                            try {
                                Glide.with(MyApp.getContext()).load(homeBodysEntity.getPicUrl()).error(R.mipmap.home_data_info_def).into(iv_expert_icon);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            tv_expert_title.setText(homeBodysEntity.getTitle());
                            tv_expert_subtitle.setText(homeBodysEntity.getSubTitle());
                            tv_expert_info_source.setText(homeBodysEntity.getInfoSource());
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            L.d("xxx", "init初始化失败：" + e.getMessage());
        }
    }

    /**
     * 点赞功能
     */
    public void addLike(final HomeBodysEntity entity, List<HomeBodysEntity> bodys) {

        mProductItemView = getmProductItemView();
        adviceUserName.setText(bodys.get(0).getNickName());
        adviceUserTime.setText(bodys.get(0).getSendTime().substring(0, 10));
        try {
            ImageLoader.load(mContext, bodys.get(0).getUserImg(), R.mipmap.center_head).into(adviceUserIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adviceUserContent.setText(bodys.get(0).getContent());
        adviceProductReply.setText(bodys.get(0).getReplyContent());
        adviceTextUserLike.setText(bodys.get(0).getLikes() + "");
        //点赞
        String likeId = PreferenceUtil.getString(ProductAdviceActivity.LIKE_IDS, "");
        String[] idArray = likeId.split("[,]");

        for (String id : idArray) {
            if (id.equals(entity.getId())) {
                adviceImgUserLike.setImageResource(R.mipmap.advice_like_red);
                adviceImgUserLike.setTag(true);
                break;
            } else {
                adviceImgUserLike.setImageResource(R.mipmap.advice_like_white);
                adviceImgUserLike.setTag(false);
            }
        }
        adviceImgUserLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = (boolean) v.getTag();
                if (isCheck) {
                    //不请求后台
                } else {
                    adviceImgUserLike.setImageResource(R.mipmap.advice_like_red);
                    adviceImgUserLike.setTag(true);
                    adviceTextUserLike.setText((entity.getLikes() + 1) + "");
                    //网络请求
                    addFocusId(entity.getId());
                }
            }
        });

    }

    /**
     * 添加点赞
     *
     * @param id
     */
    public void addFocusId(String id) {
        String likeIds = PreferenceUtil.getString(ProductAdviceActivity.LIKE_IDS, "");
        String[] idArray = likeIds.split("[,]");

        if ("".equals(likeIds)) {
            PreferenceUtil.commitString(ProductAdviceActivity.LIKE_IDS, id);
        } else {
            for (String hasid : idArray) {
                if (hasid.equals(id)) {
                    break;
                } else {
                    PreferenceUtil.commitString(ProductAdviceActivity.LIKE_IDS, likeIds + "," + id);
                }
            }
        }
        String deviceId = AppConstants.deviceToken;
        String userId = "";
        if (AppConstants.register != null && AppConstants.register != null && AppConstants.register.getUser() != null) {
            userId = AppConstants.register.getUser().getUserId();
        }
        //thirdId
        String url = "http://192.168.33.45:8080/mlottery/core/feedback.addFeedBackLikes.do";
        Map<String, String> params = new HashMap<>();

        params.put("deviceId", deviceId);
        params.put("userId", userId);
        params.put("feedbackId", id);

        VolleyContentFast.requestJsonByPost(BaseURLs.PRODUCT_ADVICE_LIKE, params, new VolleyContentFast.ResponseSuccessListener<ProductUserLike>() {
            @Override
            public void onResponse(ProductUserLike like) {
                if (like.getResult() == 200) {
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        }, ProductUserLike.class);


    }

    /**
     * 热门赛事条目数据设置
     *
     * @param homeBodysEntity 数据对象
     * @param color           显示颜色
     * @param vs              VS
     * @param desc            显示状态
     */
    private void settingScoreItemData(HomeBodysEntity homeBodysEntity, int color, String vs, String desc) {
        if ("VS".equals(vs)) {
            score01_home_score.setText("");
            score01_guest_score.setText("");
            score01_vs.setText(mContext.getResources().getString(R.string.tennis_match_not_start));
            Date curDate = new Date(System.currentTimeMillis());// 获取当前日期
            String mDate = DateUtil.formatDate(curDate);
            if (mDate.equals(homeBodysEntity.getDate())) {
                score01_desc.setText(mContext.getResources().getString(R.string.home_pager_current_date) + " " + homeBodysEntity.getTime());
            } else {
                try {
                    score01_desc.setText(DateUtil.convertDateToNationMD(homeBodysEntity.getDate().substring(5, homeBodysEntity.getDate().length())) + " " + homeBodysEntity.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                    score01_desc.setText("");
                }
            }
        } else {
            score01_home_score.setText(homeBodysEntity.getHomeScore());
            score01_home_score.setTextColor(color);
            score01_guest_score.setText(homeBodysEntity.getGuestScore());
            score01_guest_score.setTextColor(color);
            score01_desc.setText(desc);
            score01_vs.setText(vs);
        }
        score01_vs.setTextColor(color);
    }

    /**
     * 快三添加Item
     *
     * @param homeBodysEntity 实体类对象
     * @param lotteryName     彩种名称
     */
    private void lotteryKSAddView(HomeBodysEntity homeBodysEntity, String lotteryName) {
        View lotteryView = getLotteryKSView();
        lotteryViewList.add(lotteryView);
        try {
            Glide.with(MyApp.getContext()).load(homeBodysEntity.getPicUrl()).error(R.mipmap.home_number_item_icon_def).into(ks_icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ks_name.setText(lotteryName);
        ks_issue.setText(homeBodysEntity.getIssue());// 设置期号
        List<String> numbers = HomeNumbersSplit.getSplitData(homeBodysEntity.getNumbers());
        if (numbers != null) {
            for (int k = 0, len2 = numbers.size(); k < len2; k++) {
                ks_numbers.get(k).setImageDrawable(mContext.getResources().getDrawable(AppConstants.numberKSNos[Integer.parseInt(numbers.get(k)) - 1]));
            }
        }
    }

    /**
     * 快乐十分添加Item
     *
     * @param homeBodysEntity 实体类对象
     * @param lotteryName     彩种名称
     */
    private void lotteryKLSFAddView(HomeBodysEntity homeBodysEntity, String lotteryName) {
        View lotteryView = getLotteryKLSFView();
        lotteryViewList.add(lotteryView);
        try {
            Glide.with(MyApp.getContext()).load(homeBodysEntity.getPicUrl()).error(R.mipmap.home_number_item_icon_def).into(klsf_icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        klsf_name.setText(lotteryName);
        klsf_issue.setText(homeBodysEntity.getIssue());// 设置期号
        List<String> numbers = HomeNumbersSplit.getSplitData(homeBodysEntity.getNumbers());
        if (numbers != null) {
            if ("19".equals(homeBodysEntity.getName())) {
                for (int k = 0, len2 = numbers.size(); k < len2; k++) {
//                    klsf_numbers.get(k).setBackground(mContext.getResources().getDrawable(AppConstants.numberXYLCs[Integer.parseInt(numbers.get(k)) - 1]));
                    klsf_numbers.get(k).setBackgroundResource(AppConstants.numberXYLCs[Integer.parseInt(numbers.get(k)) - 1]);
                }
            } else {
                for (int k = 0, len2 = numbers.size(); k < len2; k++) {
                    klsf_numbers.get(k).setText(numbers.get(k));
                    if ("19".equals(numbers.get(k)) || "20".equals(numbers.get(k))) {
//                        klsf_numbers.get(k).setBackground(mContext.getResources().getDrawable(R.mipmap.number_bg_red));
                        klsf_numbers.get(k).setBackgroundResource(R.mipmap.number_bg_red);
                    } else {
//                        klsf_numbers.get(k).setBackground(mContext.getResources().getDrawable(R.mipmap.number_bg_blue));
                        klsf_numbers.get(k).setBackgroundResource(R.mipmap.number_bg_blue);
                    }
                }
            }
        }
    }

    /**
     * 北京赛车添加Item
     *
     * @param homeBodysEntity 实体类对象
     * @param lotteryName     彩种名称
     */
    private void lotteryBJSCAddView(HomeBodysEntity homeBodysEntity, String lotteryName) {
        View lotteryView = getLotteryBJSCView();
        lotteryViewList.add(lotteryView);
        try {
            Glide.with(MyApp.getContext()).load(homeBodysEntity.getPicUrl()).error(R.mipmap.home_number_item_icon_def).into(bjsc_icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bjsc_name.setText(lotteryName);
        bjsc_issue.setText(homeBodysEntity.getIssue());// 设置期号
        List<String> numbers = HomeNumbersSplit.getSplitData(homeBodysEntity.getNumbers());
        if (numbers != null) {
            for (int k = 0, len2 = numbers.size(); k < len2; k++) {
                bjsc_numbers.get(k).setImageDrawable(mContext.getResources().getDrawable(AppConstants.numberCarNos[Integer.parseInt(numbers.get(k)) - 1]));
            }
        }
    }

    /**
     * 七星彩添加Item
     *
     * @param homeBodysEntity 实体类对象
     * @param lotteryName     彩种名称
     */
    private void lotteryQXCAddView(HomeBodysEntity homeBodysEntity, String lotteryName) {
        View lotteryView = getLotteryyQXCView();
        lotteryViewList.add(lotteryView);
        try {
            Glide.with(MyApp.getContext()).load(homeBodysEntity.getPicUrl()).error(R.mipmap.home_number_item_icon_def).into(qxc_icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        qxc_name.setText(lotteryName);
        qxc_issue.setText(homeBodysEntity.getIssue());// 设置期号
        List<String> numbers = HomeNumbersSplit.getSplitData(homeBodysEntity.getNumbers());
        if (numbers != null) {
            for (int k = 0, len2 = numbers.size(); k < len2; k++) {
                qxc_numbers.get(k).setText(numbers.get(k));
                if (k < 4) {
//                    qxc_numbers.get(k).setBackground(mContext.getResources().getDrawable(R.mipmap.number_bg_red));
                    qxc_numbers.get(k).setBackgroundResource(R.mipmap.number_bg_red);
                } else {
//                    qxc_numbers.get(k).setBackground(mContext.getResources().getDrawable(R.mipmap.number_bg_blue));
                    qxc_numbers.get(k).setBackgroundResource(R.mipmap.number_bg_blue);
                }
            }
        }
    }

    /**
     * 香港彩添加Item
     *
     * @param homeBodysEntity 实体类对象
     * @param lotteryName     彩种名称
     */
    private void lotteryHKAddView(HomeBodysEntity homeBodysEntity, String lotteryName) {
        View lotteryView = getLotteryHKView();
        lotteryViewList.add(lotteryView);
        try {
            Glide.with(MyApp.getContext()).load(homeBodysEntity.getPicUrl()).error(R.mipmap.home_number_item_icon_def).into(hk_icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        hk_name.setText(lotteryName);
        hk_issue.setText(homeBodysEntity.getIssue());// 设置期号
        List<String> numbers = HomeNumbersSplit.getSplitData(homeBodysEntity.getNumbers());
        if (numbers != null) {
            for (int k = 0, len2 = numbers.size(); k < len2; k++) {
                String string = numbers.get(k);// 给个位数号码前面加上0
                if (string.length() == 1) {
                    string = "0" + string;
                }
                hk_numbers.get(k).setText(string);
                switch (Integer.parseInt(numbers.get(k))) {
                    case 1:
                    case 2:
                    case 7:
                    case 8:
                    case 12:
                    case 13:
                    case 18:
                    case 19:
                    case 23:
                    case 24:
                    case 29:
                    case 30:
                    case 34:
                    case 35:
                    case 40:
                    case 45:
                    case 46:
                        hk_numbers.get(k).setBackgroundResource(R.mipmap.number_bg_red);
                        break;
                    case 3:
                    case 4:
                    case 9:
                    case 10:
                    case 14:
                    case 15:
                    case 20:
                    case 25:
                    case 26:
                    case 31:
                    case 36:
                    case 37:
                    case 41:
                    case 42:
                    case 47:
                    case 48:
                        hk_numbers.get(k).setBackgroundResource(R.mipmap.number_bg_blue);
                        break;
                    case 5:
                    case 6:
                    case 11:
                    case 16:
                    case 17:
                    case 21:
                    case 22:
                    case 27:
                    case 28:
                    case 32:
                    case 33:
                    case 38:
                    case 39:
                    case 43:
                    case 44:
                    case 49:
                        hk_numbers.get(k).setBackgroundResource(R.mipmap.number_bg_green);
                        break;
                }
            }
        }
        List<String> zodiacs = HomeNumbersSplit.getSplitData(homeBodysEntity.getZodiac());
        if (zodiacs != null) {
            for (int k = 0, len2 = zodiacs.size(); k < len2; k++) {
                String zod = "";
                switch (zodiacs.get(k)) {
                    // 牛、马、羊、鸡、狗、猪
                    // 鼠、虎、兔、龙、蛇、猴
                    case "牛":
                        zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_nu);
                        break;
                    case "马":
                        zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_ma);
                        break;
                    case "羊":
                        zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_ya);
                        break;
                    case "鸡":
                        zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_ji);
                        break;
                    case "狗":
                        zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_gou);
                        break;
                    case "猪":
                        zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_zhu);
                        break;
                    case "鼠":
                        zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_su);
                        break;
                    case "虎":
                        zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_hu);
                        break;
                    case "兔":
                        zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_tu);
                        break;
                    case "龙":
                        zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_long);
                        break;
                    case "蛇":
                        zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_se);
                        break;
                    case "猴":
                        zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_hou);
                        break;
                }
                String zodIndex = "牛马羊鸡狗猪鼠虎兔龙蛇猴龍馬雞豬猴";
                if (zodIndex.indexOf(zod) != -1) {
                    hk_zodiacs.get(k).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);// 中文
                } else {
                    hk_zodiacs.get(k).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);// 英文
                }
                hk_zodiacs.get(k).setText(zod);
            }
        }
    }

    /**
     * 时时彩|十一选五添加Item
     *
     * @param homeBodysEntity 实体类对象
     * @param lotteryName     彩种名称
     */
    private void lotterySSCAddView(HomeBodysEntity homeBodysEntity, String lotteryName) {
        View lotteryView = getLotterySSCView();
        lotteryViewList.add(lotteryView);
        try {
            Glide.with(MyApp.getContext()).load(homeBodysEntity.getPicUrl()).error(R.mipmap.home_number_item_icon_def).into(ssc_icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ssc_name.setText(lotteryName);
        ssc_issue.setText(homeBodysEntity.getIssue());// 设置期号
        List<String> numbers = HomeNumbersSplit.getSplitData(homeBodysEntity.getNumbers());
        if (numbers != null) {
            for (int k = 0, len2 = numbers.size(); k < len2; k++) {
                ssc_numbers.get(k).setText(numbers.get(k));
//                ssc_numbers.get(k).setBackground(mContext.getResources().getDrawable(R.mipmap.number_bg_red));
                ssc_numbers.get(k).setBackgroundResource(R.mipmap.number_bg_red);
            }
        }
    }


    @Override
    public int getCount() {
        if (mHomePagerEntity == null || mHomePagerEntity.getOtherLists() == null || mHomePagerEntity.getOtherLists().size() == 0) {
            return 1;
        }
        return mHomePagerEntity.getOtherLists().size() + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case 0:
                    mViewHolder = new ViewHolder();
                    convertView = View.inflate(mContext, R.layout.home_page_item_menu, null);
                    mViewHolder.mViewPager = (ViewPager) convertView.findViewById(R.id.home_page_item_viewPager);
                    mViewHolder.mViewPagerMenu = (WrapContentHeightViewPager) convertView.findViewById(R.id.home_view_pager);
                    mViewHolder.ll_point = (LinearLayout) convertView.findViewById(R.id.ll_point);
                    mViewHolder.ll_menu_point = (LinearLayout) convertView.findViewById(R.id.ll_menu_point);

                    mPagerAdapter = new HomePagerAdapter(mContext, mHomePagerEntity, mViewHolder);
                    mViewHolder.mViewPager.setAdapter(mPagerAdapter);// 轮播图适配数据

                    try {
                        if (mHomePagerEntity == null || mHomePagerEntity.getBanners() == null || mHomePagerEntity.getBanners().getContent() == null || mHomePagerEntity.getBanners().getContent().size() == 0) {

                        } else {
                            int currentIndex = (Integer.MAX_VALUE / 2) % mHomePagerEntity.getBanners().getContent().size() == 0 ? (Integer.MAX_VALUE / 2) : (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2) % mHomePagerEntity.getBanners().getContent().size();
                            mViewHolder.mViewPager.setCurrentItem(currentIndex);// 设置当前轮播图下标
                        }
                    } catch (Exception e) {
                        L.d("设置轮播图下标失败：" + e.getMessage());
                    }

                    /*给首页菜单入口添加小圆点标记--------start--------------------*/
                    mViewHolder.ll_menu_point.removeAllViews();
                    if (circularSize >= 2) {
                        int dp = DisplayUtil.dip2px(mContext, 5);// 添加小圆点
                        for (int i = 0; i < circularSize; i++) {
                            View view = new View(mContext);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp, dp);
                            if (i != 0) {
                                lp.leftMargin = dp;
                            }
                            view.setEnabled(i == 0);
                            view.setBackgroundResource(R.drawable.v_lunbo_point_selector);
                            view.setLayoutParams(lp);
                            mViewHolder.ll_menu_point.addView(view);
                        }
                    }
                    /*给首页菜单入口添加小圆点标记--------end--------------------*/
                    mViewHolder.mViewPagerMenu.setAdapter(new FragmentStatePagerAdapter(((IndexActivity) mContext).getSupportFragmentManager()) {
                        @Override
                        public Fragment getItem(int position) {
                            return fragmentList.get(position);
                        }

                        @Override
                        public int getCount() {
                            if (mHomePagerEntity == null || mHomePagerEntity.getMenus() == null || mHomePagerEntity.getMenus().getContent() == null || mHomePagerEntity.getMenus().getContent().size() == 0) {
                                return 0;
                            } else {
                                return mHomePagerEntity.getMenus().getContent().size() % 8 == 0 ? mHomePagerEntity.getMenus().getContent().size() / 8 : mHomePagerEntity.getMenus().getContent().size() / 8 + 1;
                            }
                        }
                    });
                    final int finalLen = circularSize;
                    mViewHolder.mViewPagerMenu.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            for (int i = 0; i < finalLen; i++) {
                                mViewHolder.ll_menu_point.getChildAt(i).setEnabled(i == position % finalLen ? true : false);
                            }
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                    convertView.setTag(mViewHolder);
                    break;
                case 1:
                    mViewHolderOther = new ViewHolderOther();
                    convertView = View.inflate(mContext, R.layout.home_page_items, null);
                    mViewHolderOther.tv_title = (TextView) convertView.findViewById(R.id.tv_home_item_title);
                    mViewHolderOther.ll_content = (LinearLayout) convertView.findViewById(R.id.ll_home_item_content);
                    mViewHolderOther.tv_more_advice = (TextView) convertView.findViewById(R.id.tv_home_item_more);
                    mViewHolderOther.view_title_split = convertView.findViewById(R.id.view_title_split);
                    convertView.setTag(mViewHolderOther);
                    break;
            }
        }
        if (type != 0) {
            mViewHolderOther = (ViewHolderOther) convertView.getTag();
            if (mViewHolderOther.ll_content != null) {
                mViewHolderOther.ll_content.removeAllViews();
            }
            boolean addViewScore = false;
            boolean addViewDataInfo = false;
            boolean addViewLottery = false;
            boolean addViewExpert = false;
            boolean expertName = false;
            if (getItem(position) != null) {
                final HomeContentEntity mContent = (HomeContentEntity) getItem(position);
                if (mContent.getLabType() == 5) {
                    mViewHolderOther.tv_more_advice.setVisibility(View.VISIBLE);
                } else {
                    mViewHolderOther.tv_more_advice.setVisibility(View.GONE);
                }
                if (mContent.getLabType() == 7) {
                    mViewHolderOther.tv_title.setVisibility(View.GONE);
                    mViewHolderOther.view_title_split.setVisibility(View.GONE);
                } else {
                    mViewHolderOther.tv_title.setVisibility(View.VISIBLE);
                    mViewHolderOther.view_title_split.setVisibility(View.VISIBLE);
                }
                for (int i = 0, len = mContent.getBodys().size(); i < len; i++) {
                    switch (mContent.getLabType()) {
                        case 1: // 1、	热门赛事.
                            if (addViewScore) {
                                View splitView = View.inflate(mContext, R.layout.split_view, null);
                                mViewHolderOther.ll_content.addView(splitView);// 添加分割线
                            }
                            mViewHolderOther.tv_title.setText(mContext.getResources().getString(R.string.hot_score_txt));
                            View scoreView = scoreViewList.get(i);
                            ViewParent parentScore = scoreView.getParent();
                            if (parentScore != null) {
                                ((ViewGroup) parentScore).removeAllViews();
                            }
                            mViewHolderOther.ll_content.addView(scoreView);
                            addViewScore = true;
                            break;
                        case 2:// 2、	热门资讯
                            if (addViewDataInfo) {
                                View splitView = View.inflate(mContext, R.layout.split_view, null);
                                mViewHolderOther.ll_content.addView(splitView);// 添加分割线
                            }
                            mViewHolderOther.tv_title.setText(mContext.getResources().getString(R.string.hor_data_info_txt));
                            View dataInfoView = dataInfoViewList.get(i);
                            ViewParent parentDataInfo = dataInfoView.getParent();
                            if (parentDataInfo != null) {
                                ((ViewGroup) parentDataInfo).removeAllViews();
                            }
                            mViewHolderOther.ll_content.addView(dataInfoView);
                            addViewDataInfo = true;
                            break;
                        case 3:// 3、	彩票开奖
                            if (addViewLottery) {
                                View splitView = View.inflate(mContext, R.layout.split_view, null);
                                mViewHolderOther.ll_content.addView(splitView);// 添加分割线
                            }
                            mViewHolderOther.tv_title.setText(mContext.getResources().getString(R.string.frame_home_jieguo_txt));
                            View lotteryView = lotteryViewList.get(i);
                            ViewParent parentLottery = lotteryView.getParent();
                            if (parentLottery != null) {
                                ((ViewGroup) parentLottery).removeAllViews();
                            }
                            mViewHolderOther.ll_content.addView(lotteryView);
                            addViewLottery = true;
                            break;
                        case 5:
                            mViewHolderOther.tv_title.setText(mContext.getResources().getString(R.string.title_product_advice));
                            mViewHolderOther.tv_more_advice.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mProductListener != null) {
                                        mProductListener.toProductActivity();
                                    }
                                }
                            });

                            ViewParent productItemView = mProductItemView.getParent();
                            if (productItemView != null) {
                                ((ViewGroup) productItemView).removeAllViews();
                            }
                            mViewHolderOther.ll_content.addView(mProductItemView);

                            break;
                        case 7:
                            ViewParent parentItemView = lotteryItemView.getParent();
                            if (parentItemView != null) {
                                ((ViewGroup) parentItemView).removeAllViews();
                            }
                            mViewHolderOther.ll_content.addView(lotteryItemView);
                            break;
                        case 4:
                            if (!expertName) {
                                expertName = true;
                                if (mHomePagerEntity != null && mHomePagerEntity.getHeadTitles() != null && mHomePagerEntity.getHeadTitles().getContent() != null && mHomePagerEntity.getHeadTitles().getContent().size() != 0) {
                                    View expertNameView = addExpertNameItem();
                                    mViewHolderOther.ll_content.addView(expertNameView);
                                }
                                View splitView = View.inflate(mContext, R.layout.split_view, null);
                                mViewHolderOther.ll_content.addView(splitView);
                            }

                            if (addViewExpert) {
                                View splitView = View.inflate(mContext, R.layout.split_view, null);
                                mViewHolderOther.ll_content.addView(splitView);// 添加分割线
                            }
                            mViewHolderOther.tv_title.setText(mContext.getResources().getString(R.string.home_expert_title));
                            View expertView = expertViewList.get(i);
                            ViewParent parentExpert = expertView.getParent();
                            if (parentExpert != null) {
                                ((ViewGroup) parentExpert).removeAllViews();
                            }
                            mViewHolderOther.ll_content.addView(expertView);
                            mViewHolderOther.tv_more_advice.setVisibility(View.GONE);
                            addViewExpert = true;
                            break;
                    }
                }
            }
        }
        return convertView;
    }

    int expertid1, expertid2, expertid3, expertid4;

    /**
     * 添加专家专栏的专家名单
     *
     * @return
     */
    private View addExpertNameItem() {
        View view = View.inflate(mContext, R.layout.home_page_expert_name_item, null);
        LinearLayout ll_expert_name_item1 = (LinearLayout) view.findViewById(R.id.ll_expert_name_item1);
        LinearLayout ll_expert_name_item02 = (LinearLayout) view.findViewById(R.id.ll_expert_name_item02);
        LinearLayout ll_expert_content1 = (LinearLayout) view.findViewById(R.id.ll_expert_content1);
        LinearLayout ll_expert_content2 = (LinearLayout) view.findViewById(R.id.ll_expert_content2);
        LinearLayout ll_expert_content3 = (LinearLayout) view.findViewById(R.id.ll_expert_content3);
        LinearLayout ll_expert_content4 = (LinearLayout) view.findViewById(R.id.ll_expert_content4);
        LinearLayout ll_expert_content01 = (LinearLayout) view.findViewById(R.id.ll_expert_content01);
        LinearLayout ll_expert_content02 = (LinearLayout) view.findViewById(R.id.ll_expert_content02);
        LinearLayout ll_expert_content03 = (LinearLayout) view.findViewById(R.id.ll_expert_content03);
        CircleImageView iv_expert_icon1 = (CircleImageView) view.findViewById(R.id.iv_expert_icon1);
        CircleImageView iv_expert_icon2 = (CircleImageView) view.findViewById(R.id.iv_expert_icon2);
        CircleImageView iv_expert_icon3 = (CircleImageView) view.findViewById(R.id.iv_expert_icon3);
        CircleImageView iv_expert_icon4 = (CircleImageView) view.findViewById(R.id.iv_expert_icon4);
        CircleImageView iv_expert_icon01 = (CircleImageView) view.findViewById(R.id.iv_expert_icon01);
        CircleImageView iv_expert_icon02 = (CircleImageView) view.findViewById(R.id.iv_expert_icon02);
        CircleImageView iv_expert_icon03 = (CircleImageView) view.findViewById(R.id.iv_expert_icon03);
        TextView tv_expert_name1 = (TextView) view.findViewById(R.id.tv_expert_name1);
        TextView tv_expert_name2 = (TextView) view.findViewById(R.id.tv_expert_name2);
        TextView tv_expert_name3 = (TextView) view.findViewById(R.id.tv_expert_name3);
        TextView tv_expert_name4 = (TextView) view.findViewById(R.id.tv_expert_name4);
        TextView tv_expert_name01 = (TextView) view.findViewById(R.id.tv_expert_name01);
        TextView tv_expert_name02 = (TextView) view.findViewById(R.id.tv_expert_name02);
        TextView tv_expert_name03 = (TextView) view.findViewById(R.id.tv_expert_name03);

        int size = mHomePagerEntity.getHeadTitles().getContent().size();
        if (size == 1) {
            ll_expert_name_item1.setVisibility(View.VISIBLE);
            ll_expert_name_item02.setVisibility(View.GONE);
            ll_expert_content1.setVisibility(View.VISIBLE);
            try {
                Glide.with(MyApp.getContext()).load(mHomePagerEntity.getHeadTitles().getContent().get(0).getIcon()).error(R.mipmap.home_menu_icon_def).into(iv_expert_icon1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_expert_name1.setText(mHomePagerEntity.getHeadTitles().getContent().get(0).getTitle());
            expertid1 = mHomePagerEntity.getHeadTitles().getContent().get(0).getExpertId();
        } else if (size == 2) {
            ll_expert_name_item1.setVisibility(View.VISIBLE);
            ll_expert_name_item02.setVisibility(View.GONE);
            ll_expert_content1.setVisibility(View.VISIBLE);
            try {
                Glide.with(MyApp.getContext()).load(mHomePagerEntity.getHeadTitles().getContent().get(0).getIcon()).error(R.mipmap.home_menu_icon_def).into(iv_expert_icon1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_expert_name1.setText(mHomePagerEntity.getHeadTitles().getContent().get(0).getTitle());
            expertid1 = mHomePagerEntity.getHeadTitles().getContent().get(0).getExpertId();
            ll_expert_content2.setVisibility(View.VISIBLE);
            try {
                Glide.with(MyApp.getContext()).load(mHomePagerEntity.getHeadTitles().getContent().get(1).getIcon()).error(R.mipmap.home_menu_icon_def).into(iv_expert_icon2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_expert_name2.setText(mHomePagerEntity.getHeadTitles().getContent().get(1).getTitle());
            expertid2 = mHomePagerEntity.getHeadTitles().getContent().get(1).getExpertId();
        } else if (size == 3) {
            ll_expert_name_item1.setVisibility(View.GONE);
            ll_expert_name_item02.setVisibility(View.VISIBLE);
            try {
                Glide.with(MyApp.getContext()).load(mHomePagerEntity.getHeadTitles().getContent().get(0).getIcon()).error(R.mipmap.home_menu_icon_def).into(iv_expert_icon01);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_expert_name01.setText(mHomePagerEntity.getHeadTitles().getContent().get(0).getTitle());
            expertid1 = mHomePagerEntity.getHeadTitles().getContent().get(0).getExpertId();
            try {
                Glide.with(MyApp.getContext()).load(mHomePagerEntity.getHeadTitles().getContent().get(1).getIcon()).error(R.mipmap.home_menu_icon_def).into(iv_expert_icon02);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_expert_name02.setText(mHomePagerEntity.getHeadTitles().getContent().get(1).getTitle());
            expertid2 = mHomePagerEntity.getHeadTitles().getContent().get(1).getExpertId();
            try {
                Glide.with(MyApp.getContext()).load(mHomePagerEntity.getHeadTitles().getContent().get(2).getIcon()).error(R.mipmap.home_menu_icon_def).into(iv_expert_icon03);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_expert_name03.setText(mHomePagerEntity.getHeadTitles().getContent().get(2).getTitle());
            expertid3 = mHomePagerEntity.getHeadTitles().getContent().get(2).getExpertId();
        } else if (size >= 4) {
            ll_expert_name_item1.setVisibility(View.VISIBLE);
            ll_expert_name_item02.setVisibility(View.GONE);
            ll_expert_content1.setVisibility(View.VISIBLE);
            try {
                Glide.with(MyApp.getContext()).load(mHomePagerEntity.getHeadTitles().getContent().get(0).getIcon()).error(R.mipmap.home_menu_icon_def).into(iv_expert_icon1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_expert_name1.setText(mHomePagerEntity.getHeadTitles().getContent().get(0).getTitle());
            expertid1 = mHomePagerEntity.getHeadTitles().getContent().get(0).getExpertId();
            ll_expert_content2.setVisibility(View.VISIBLE);
            try {
                Glide.with(MyApp.getContext()).load(mHomePagerEntity.getHeadTitles().getContent().get(1).getIcon()).error(R.mipmap.home_menu_icon_def).into(iv_expert_icon2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_expert_name2.setText(mHomePagerEntity.getHeadTitles().getContent().get(1).getTitle());
            expertid2 = mHomePagerEntity.getHeadTitles().getContent().get(1).getExpertId();
            ll_expert_content3.setVisibility(View.VISIBLE);
            try {
                Glide.with(MyApp.getContext()).load(mHomePagerEntity.getHeadTitles().getContent().get(2).getIcon()).error(R.mipmap.home_menu_icon_def).into(iv_expert_icon3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_expert_name3.setText(mHomePagerEntity.getHeadTitles().getContent().get(2).getTitle());
            expertid3 = mHomePagerEntity.getHeadTitles().getContent().get(2).getExpertId();
            ll_expert_content4.setVisibility(View.VISIBLE);
            try {
                Glide.with(MyApp.getContext()).load(mHomePagerEntity.getHeadTitles().getContent().get(3).getIcon()).error(R.mipmap.home_menu_icon_def).into(iv_expert_icon4);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_expert_name4.setText(mHomePagerEntity.getHeadTitles().getContent().get(3).getTitle());
            expertid4 = mHomePagerEntity.getHeadTitles().getContent().get(3).getExpertId();
        }

        ll_expert_content1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinDataInfo(expertid1);
            }
        });
        ll_expert_content2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinDataInfo(expertid2);
            }
        });
        ll_expert_content3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinDataInfo(expertid3);
            }
        });
        ll_expert_content4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinDataInfo(expertid4);
            }
        });
        ll_expert_content01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinDataInfo(expertid1);
            }
        });
        ll_expert_content02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinDataInfo(expertid2);
            }
        });
        ll_expert_content03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinDataInfo(expertid3);
            }
        });
        return view;
    }

    private void joinDataInfo(int expertId) {
        Intent intent = new Intent(mContext, ExpertsActivity.class);
        intent.putExtra("expertId", String.valueOf(expertId));
        mContext.startActivity(intent);
    }

    /**
     * 跳转到产品界面的监听
     *
     * @param listener
     */
    public void setToProductListener(ProductListener listener) {
        this.mProductListener = listener;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return null;
        }
        if (mHomePagerEntity != null && mHomePagerEntity.getOtherLists() != null && mHomePagerEntity.getOtherLists().size() > 0) {
            return mHomePagerEntity.getOtherLists().get(position - 1).getContent();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * 头部ViewHolder
     */
    public static class ViewHolder {
        ViewPager mViewPager;// 轮播图
        WrapContentHeightViewPager mViewPagerMenu;// 主入口
        LinearLayout ll_point;// 小圆点
        LinearLayout ll_menu_point;// 小圆点
    }

    /**
     * 其它条目ViewHolder
     */
    public static class ViewHolderOther {
        TextView tv_title;// 标题
        LinearLayout ll_content;// 条目
        TextView tv_more_advice; //产品建议条目的更多
        View view_title_split;
    }
}
