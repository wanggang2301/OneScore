package com.hhly.mlottery.frame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketballInformationActivity;
import com.hhly.mlottery.activity.CounselActivity;
import com.hhly.mlottery.activity.InfoCenterActivity;
import com.hhly.mlottery.activity.LeagueStatisticsTodayActivity;
import com.hhly.mlottery.activity.LoginActivity;
import com.hhly.mlottery.activity.NumbersActivity;
import com.hhly.mlottery.activity.NumbersInfoBaseActivity;
import com.hhly.mlottery.activity.VideoActivity;
import com.hhly.mlottery.activity.WebActivity;
import com.hhly.mlottery.adapter.homePagerAdapter.HomeGridAdapter;
import com.hhly.mlottery.bean.homepagerentity.HomeContentEntity;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.MyGridView;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * 描  述：首页菜单入口 Fragment
 * 作  者：tangrr@13322.com
 * 时  间：2016/9/19
 */

public class HomeMuenFragment extends Fragment {

    private static final String DATA_KEY = "mData";
    private View mView;
    List<HomeContentEntity> mData;
    private MyGridView gridView;
    private final int MIN_CLICK_DELAY_TIME = 1000;// 控件点击间隔时间
    private long lastClickTime = 0;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mData = (List<HomeContentEntity>) args.getSerializable(DATA_KEY);
        }
        filterData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = View.inflate(getContext(), R.layout.home_page_menu_fragment, null);
        mContext = getContext();
        initView();
        initGridListener();

        return mView;
    }

    /**
     * 过滤跳数据
     */
    private void filterData() {
        Iterator<HomeContentEntity> iterator = mData.iterator();
        while (iterator.hasNext()) {
            String jumpAddr = iterator.next().getJumpAddr();
            switch (jumpAddr) {
                case "10":// 足球指数
                case "11":// 足球数据
                case "13":// 足球比分
                case "20":// 篮球即时比分
                case "21":// 篮球赛果
                case "22":// 篮球赛程
                case "23":// 篮球关注
                case "24":// 篮球资讯
                case "350":// 彩票资讯
                case "80":// 多屏动画列表
                    iterator.remove();
                    break;
            }
        }
    }

    private void initView() {
        gridView = (MyGridView) mView.findViewById(R.id.home_page_item_gridView);
        gridView.setAdapter(new HomeGridAdapter(getContext(), mData));
    }

    public static HomeMuenFragment newInstance(List<HomeContentEntity> list) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA_KEY, (Serializable) list);
        HomeMuenFragment fragment = new HomeMuenFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * GridView事件监听事件
     */
    private void initGridListener() {
        try {
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                        lastClickTime = currentTime;
                        HomeContentEntity homeContentEntity = mData.get(position);
                        int jumpType = homeContentEntity.getJumpType();// 跳转类型
                        String jumpAddr = homeContentEntity.getJumpAddr();// 跳转地址
                        Integer labSeq = homeContentEntity.getLabSeq();// 体育资讯下标
                        String title = homeContentEntity.getTitle();// 跳转标题
                        String reqMethod = homeContentEntity.getReqMethod();// 跳转方式
                        if (!TextUtils.isEmpty(jumpAddr)) {
                            switch (jumpType) {
                                case 0:// 无
                                    break;
                                case 1:// 页面
                                {
                                    if (jumpAddr.contains("{loginToken}")) {// 是否需要登录
                                        if (CommonUtils.isLogin()) {// 判断用户是否登录
                                            Intent intent = new Intent(getContext(), WebActivity.class);
                                            intent.putExtra("key", jumpAddr);// 跳转地址
                                            intent.putExtra("infoTypeName", title);
                                            intent.putExtra("reqMethod", reqMethod);// 跳转方式 get or post
//                                            intent.putExtra("token", AppConstants.register.getData().getLoginToken());// 用户token
                                            getContext().startActivity(intent);
                                        } else {// 跳转到登录界面
                                            getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                                        }
                                    } else {// 其它
                                        Intent intent = new Intent(getContext(), WebActivity.class);
                                        intent.putExtra("key", jumpAddr);
                                        intent.putExtra("infoTypeName", title);
                                        intent.putExtra("reqMethod", reqMethod);// 跳转方式 get or post
                                        getContext().startActivity(intent);
                                    }
                                    break;
                                }
                                case 2:// 跳内页
//                                    int sportsInfoIndex = 0;
                                    switch (jumpAddr) {
                                        case "10":// 足球指数
//                                        {
//                                            Intent intent = new Intent(mContext, FootballActivity.class);
//                                            intent.putExtra(AppConstants.FOTTBALL_KEY, AppConstants.FOTTBALL_EXPONENT_VALUE);
//                                            mContext.startActivity(intent);
//                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Football_Index");
//                                        }
                                            break;
                                        case "11":// 足球数据
//                                        {
//                                            Intent intent = new Intent(mContext, FootballActivity.class);
//                                            intent.putExtra(AppConstants.FOTTBALL_KEY, AppConstants.FOTTBALL_DATA_VALUE);
//                                            mContext.startActivity(intent);
//                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Football_Data");
//                                        }
                                            break;
                                        case "12":// 足球资讯
                                        {
                                            mContext.startActivity(new Intent(mContext, CounselActivity.class));
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Football_Information");
                                        }
                                        break;
                                        case "13":// 足球比分
//                                        {
//                                            Intent intent = new Intent(mContext, FootballActivity.class);
//                                            intent.putExtra(AppConstants.FOTTBALL_KEY, AppConstants.FOTTBALL_SCORE_VALUE);
//                                            mContext.startActivity(intent);
//                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Football_Score");
//                                        }
                                            break;
                                        case "14":// 足球视频
                                        {
                                            mContext.startActivity(new Intent(mContext, VideoActivity.class));
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Football_Video");
                                        }
                                        break;
                                        case "20":// 篮球即时比分
//                                        {
//                                            mContext.startActivity(new Intent(mContext, BasketballScoresActivity.class));
//                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Basketball_Score");
//                                        }
                                        break;
                                        case "21":// 篮球赛果
//                                        {
//                                            Intent intent = new Intent(mContext, FootballActivity.class);
//                                            intent.putExtra(AppConstants.FOTTBALL_KEY, AppConstants.BASKETBALL_SCORE_VALUE);
//                                            intent.putExtra(AppConstants.BASKETBALL_KEY, AppConstants.BASKETBALL_AMIDITHION_VALUE);
//                                            mContext.startActivity(intent);
//                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Basketball_Amidithion");
//                                        }
                                        break;
                                        case "22":// 篮球赛程
//                                        {
//                                            Intent intent = new Intent(mContext, FootballActivity.class);
//                                            intent.putExtra(AppConstants.FOTTBALL_KEY, AppConstants.BASKETBALL_SCORE_VALUE);
//                                            intent.putExtra(AppConstants.BASKETBALL_KEY, AppConstants.BASKETBALL_COMPETITION_VALUE);
//                                            mContext.startActivity(intent);
//                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Basketball_Competition");
//                                        }
                                        break;
                                        case "23":// 篮球关注
//                                        {
//                                            Intent intent = new Intent(mContext, FootballActivity.class);
//                                            intent.putExtra(AppConstants.FOTTBALL_KEY, AppConstants.BASKETBALL_SCORE_VALUE);
//                                            intent.putExtra(AppConstants.BASKETBALL_KEY, AppConstants.BASKETBALL_ATTENTION_VALUE);
//                                            mContext.startActivity(intent);
//                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Basketball_Attention");
//                                        }
                                        break;
                                        case "24":// 篮球资讯
                                            //Toast.makeText(mContext, "篮球资讯", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "25":// 篮球资料库
                                            mContext.startActivity(new Intent(mContext, BasketballInformationActivity.class));
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Basketball_Info");
                                            break;
                                        case "30":// 彩票开奖
                                            mContext.startActivity(new Intent(mContext, NumbersActivity.class));
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_List");
                                            break;
                                        case "350":// 彩票资讯
                                            //Toast.makeText(mContext, "彩票资讯", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "31":// 香港开奖
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.ONE));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_HK");
                                        }
                                        break;
                                        case "32":// 重庆时时彩
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWO));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SSC_CQ");
                                        }
                                        break;
                                        case "33":// 江西时时彩
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.THREE));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SSC_JX");
                                        }
                                        break;
                                        case "34":// 新疆时时彩
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.FOUR));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SSC_XJ");
                                        }
                                        break;
                                        case "35":// 云南时时彩
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.FIVE));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SSC_YN");
                                        }
                                        break;
                                        case "36":// 七星彩
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.SIX));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_QXC");
                                        }
                                        break;
                                        case "37":// 广东11选5
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.SEVEN));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SYXW_GD");
                                        }
                                        break;
                                        case "38":// 广东快乐10分
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.EIGHT));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_KLSF_GD");
                                        }
                                        break;
                                        case "39":// 湖北11选5
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.NINE));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SYXW_HB");
                                        }
                                        break;
                                        case "310":// 安徽快3
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TEN));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_KS_AH");
                                        }
                                        break;
                                        case "311":// 湖南快乐10分
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.ELEVEN));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_KLSF_HN");
                                        }
                                        break;
                                        case "312":// 快乐8
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWELVE));
                                            mContext.startActivity(intent);
                                        }
                                        break;
                                        case "313":// 吉林快三
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.THIRTEEN));
                                            mContext.startActivity(intent);
                                        }
                                        break;
                                        case "314":// 辽宁11选5
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.FOURTEEN));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SYXW_LN");
                                        }
                                        break;
                                        case "315":// 北京赛车
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.FIFTEEN));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_BJSC");
                                        }
                                        break;
                                        case "316":// 江苏快3
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.SIRTEEN));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_KS_JS");
                                        }
                                        break;
                                        case "317":// 时时乐
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.SEVENTEEN));
                                            mContext.startActivity(intent);
                                        }
                                        break;
                                        case "318":// 广西快三
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.EIGHTEEN));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_KS_GX");
                                        }
                                        break;
                                        case "319":// 幸运农场
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.NINETEEN));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_KLSF_XYLC");
                                        }
                                        break;
                                        case "320":// 江苏11选5
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SYXW_JS");
                                        }
                                        break;
                                        case "321":// 江西11选5
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_ONE));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SYXW_JX");
                                        }
                                        break;
                                        case "322":// 山东11选5
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_TWO));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SYXW_SD");
                                        }
                                        break;
                                        case "323":// 天津时时彩
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_THREE));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SSC_TJ");
                                        }
                                        break;
                                        case "60":// 情报中心
                                        {
                                            mContext.startActivity(new Intent(mContext, InfoCenterActivity.class));
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_InfoCenterActivity");
                                        }
                                        break;
                                        case "19":// 今日联赛统计
                                        {
                                            mContext.startActivity(new Intent(mContext, LeagueStatisticsTodayActivity.class));
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_LeagueStatisticsTodayActivity");
                                        }
                                        break;
                                        case "324":// 双色球
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_FOUR));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SSQ");
                                        }
                                        break;
                                        case "325":// 大乐透
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_NINE));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_DLT");
                                        }
                                        break;
                                        case "326":// 排列三
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_FINE));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_PL3");
                                        }
                                        break;
                                        case "327":// 排列五
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_SIX));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_PL5");
                                        }
                                        break;
                                        case "328":// 胜负彩
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.THIRTY));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SFC");
                                        }
                                        break;
                                        case "329":// 六场半全场
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.THIRTY_ONE));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_LCBQC");
                                        }
                                        break;
                                        case "330":// 四场进球彩
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.THIRTY_TWO));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SCJQ");
                                        }
                                        break;
                                        case "331":// 福彩3D
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_SEVEN));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_F3D");
                                        }
                                        break;
                                        case "332":// 七乐彩
                                        {
                                            Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_ENGHT));
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_QLC");
                                        }
                                        break;
                                        case "80":// 多屏动画列表
//                                        {
//                                            if (PreferenceUtil.getBoolean("introduce", true)) {
//                                                mContext.startActivity(new Intent(mContext, MultiScreenIntroduceActivity.class));
//
//                                                PreferenceUtil.commitBoolean("introduce", false);
//                                            } else {
//                                                mContext.startActivity(new Intent(mContext, MultiScreenViewingListActivity.class));
//                                            }
//                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_MultiScreen_Introduce");
//                                        }
                                        break;
                                        case "42":
                                            // TODO 竞彩足球

                                            break;
                                    }
                                    break;
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            L.d("initGridListener失败：" + e.getMessage());
        }
    }
}
