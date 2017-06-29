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
import com.hhly.mlottery.activity.CounselActivity;
import com.hhly.mlottery.activity.FootballCornerActivity;
import com.hhly.mlottery.activity.FootballMatchActivity;
import com.hhly.mlottery.activity.LeagueStatisticsTodayActivity;
import com.hhly.mlottery.activity.LoginActivity;
import com.hhly.mlottery.activity.NumbersActivity;
import com.hhly.mlottery.activity.NumbersInfoBaseActivity;
import com.hhly.mlottery.activity.VideoActivity;
import com.hhly.mlottery.activity.WebActivity;
import com.hhly.mlottery.adapter.homePagerAdapter.HomeGridAdapter;
import com.hhly.mlottery.bean.homepagerentity.HomeContentEntity;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.MvpBettingRecommendActivity;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.widget.MyGridView;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
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
                                case 6:// h5登录跳转
                                    if (DeviceInfo.isLogin()) {
                                        Intent intent = new Intent(getContext(), WebActivity.class);
                                        intent.putExtra("key", jumpAddr);// 跳转地址
                                        intent.putExtra("infoTypeName", title);
                                        intent.putExtra("reqMethod", reqMethod);// 跳转方式 get or post
                                        intent.putExtra("noShare" , true);
                                        getContext().startActivity(intent);
                                    } else {// 跳转到登录界面
                                        getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                                    }


                                    break;
                                case 1:// 页面
                                {
                                    if (jumpAddr.contains("{loginToken}")) {// 是否需要登录
                                        if (DeviceInfo.isLogin()) {// 判断用户是否登录
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
                                    switch (jumpAddr) {
                                        case "10":// 足球指数
                                            break;
                                        case "11":// 足球数据
                                            break;
                                        case "12":// 足球资讯
                                            Intent foot_intent = new Intent(mContext, CounselActivity.class);
                                            foot_intent.putExtra("currentIndex", labSeq);
                                            mContext.startActivity(foot_intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Football_Information");
                                            break;
                                        case "13":// 足球比分
                                            break;
                                        case "14":// 足球视频
                                            mContext.startActivity(new Intent(mContext, VideoActivity.class));
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Football_Video");
                                            break;
                                        case "20":// 篮球即时比分
                                            break;
                                        case "21":// 篮球赛果
                                            break;
                                        case "22":// 篮球赛程
                                            break;
                                        case "23":// 篮球关注
                                            break;
                                        case "24":// 篮球资讯
                                            break;
                                        case "25":// 篮球资料库
                                            break;
                                        case "30":// 彩票开奖
                                            mContext.startActivity(new Intent(mContext, NumbersActivity.class));
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_List");
                                            break;
                                        case "350":// 彩票资讯
                                            break;
                                        case "31":// 香港开奖
                                            Intent hk_intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            hk_intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.ONE));
                                            mContext.startActivity(hk_intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_HK");
                                            break;
                                        case "32":// 重庆时时彩
                                            Intent intent32 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent32.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWO));
                                            mContext.startActivity(intent32);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SSC_CQ");
                                            break;
                                        case "33":// 江西时时彩
                                            Intent intent33 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent33.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.THREE));
                                            mContext.startActivity(intent33);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SSC_JX");
                                            break;
                                        case "34":// 新疆时时彩
                                            Intent intent34 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent34.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.FOUR));
                                            mContext.startActivity(intent34);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SSC_XJ");
                                            break;
                                        case "35":// 云南时时彩
                                            Intent intent35 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent35.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.FIVE));
                                            mContext.startActivity(intent35);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SSC_YN");
                                            break;
                                        case "36":// 七星彩
                                            Intent intent36 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent36.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.SIX));
                                            mContext.startActivity(intent36);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_QXC");
                                            break;
                                        case "37":// 广东11选5
                                            Intent intent37 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent37.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.SEVEN));
                                            mContext.startActivity(intent37);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SYXW_GD");
                                            break;
                                        case "38":// 广东快乐10分
                                            Intent intent38 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent38.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.EIGHT));
                                            mContext.startActivity(intent38);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_KLSF_GD");
                                            break;
                                        case "39":// 湖北11选5
                                            Intent intent39 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent39.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.NINE));
                                            mContext.startActivity(intent39);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SYXW_HB");
                                            break;
                                        case "310":// 安徽快3
                                            Intent intent310 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent310.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TEN));
                                            mContext.startActivity(intent310);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_KS_AH");
                                            break;
                                        case "311":// 湖南快乐10分
                                            Intent intent311 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent311.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.ELEVEN));
                                            mContext.startActivity(intent311);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_KLSF_HN");
                                            break;
                                        case "312":// 快乐8
                                            Intent intent312 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent312.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWELVE));
                                            mContext.startActivity(intent312);
                                            break;
                                        case "313":// 吉林快三
                                            Intent intent313 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent313.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.THIRTEEN));
                                            mContext.startActivity(intent313);
                                            break;
                                        case "314":// 辽宁11选5
                                            Intent intent314 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent314.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.FOURTEEN));
                                            mContext.startActivity(intent314);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SYXW_LN");
                                            break;
                                        case "315":// 北京赛车
                                            Intent intent315 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent315.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.FIFTEEN));
                                            mContext.startActivity(intent315);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_BJSC");
                                            break;
                                        case "316":// 江苏快3
                                            Intent intent316 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent316.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.SIRTEEN));
                                            mContext.startActivity(intent316);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_KS_JS");
                                            break;
                                        case "317":// 时时乐
                                            Intent intent317 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent317.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.SEVENTEEN));
                                            mContext.startActivity(intent317);
                                            break;
                                        case "318":// 广西快三
                                            Intent intent318 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent318.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.EIGHTEEN));
                                            mContext.startActivity(intent318);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_KS_GX");
                                            break;
                                        case "319":// 幸运农场
                                            Intent intent319 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent319.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.NINETEEN));
                                            mContext.startActivity(intent319);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_KLSF_XYLC");
                                            break;
                                        case "320":// 江苏11选5
                                            Intent intent320 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent320.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY));
                                            mContext.startActivity(intent320);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SYXW_JS");
                                            break;
                                        case "321":// 江西11选5
                                            Intent intent321 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent321.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_ONE));
                                            mContext.startActivity(intent321);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SYXW_JX");
                                            break;
                                        case "322":// 山东11选5
                                            Intent intent322 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent322.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_TWO));
                                            mContext.startActivity(intent322);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SYXW_SD");
                                            break;
                                        case "323":// 天津时时彩
                                            Intent intent323 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent323.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_THREE));
                                            mContext.startActivity(intent323);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SSC_TJ");
                                            break;
                                        case "60":// 情报中心
                                            break;
                                        case "19":// 今日联赛统计
                                            mContext.startActivity(new Intent(mContext, LeagueStatisticsTodayActivity.class));
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_LeagueStatisticsTodayActivity");
                                            break;
                                        case "324":// 双色球
                                            Intent intent324 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent324.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_FOUR));
                                            mContext.startActivity(intent324);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SSQ");
                                            break;
                                        case "325":// 大乐透
                                            Intent intent325 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent325.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_NINE));
                                            mContext.startActivity(intent325);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_DLT");
                                            break;
                                        case "326":// 排列三
                                            Intent intent326 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent326.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_FINE));
                                            mContext.startActivity(intent326);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_PL3");
                                            break;
                                        case "327":// 排列五
                                            Intent intent327 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent327.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_SIX));
                                            mContext.startActivity(intent327);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_PL5");
                                            break;
                                        case "328":// 胜负彩
                                            Intent intent328 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent328.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.THIRTY));
                                            mContext.startActivity(intent328);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SFC");
                                            break;
                                        case "329":// 六场半全场
                                            Intent intent329 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent329.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.THIRTY_ONE));
                                            mContext.startActivity(intent329);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_LCBQC");
                                            break;
                                        case "330":// 四场进球彩
                                            Intent intent330 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent330.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.THIRTY_TWO));
                                            mContext.startActivity(intent330);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_SCJQ");
                                            break;
                                        case "331":// 福彩3D
                                            Intent intent331 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent331.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_SEVEN));
                                            mContext.startActivity(intent331);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_F3D");
                                            break;
                                        case "332":// 七乐彩
                                            Intent intent332 = new Intent(mContext, NumbersInfoBaseActivity.class);
                                            intent332.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_ENGHT));
                                            mContext.startActivity(intent332);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_QLC");
                                            break;
                                        case "80":// 多屏动画列表
                                            break;
                                        case "44":// 足球竞彩
                                            mContext.startActivity(new Intent(mContext, FootballMatchActivity.class));
                                            break;
                                        case "92":// 竞彩推介
                                            mContext.startActivity(new Intent(mContext, MvpBettingRecommendActivity.class));
                                            break;
                                        case "101": //角球比分
                                            mContext.startActivity(new Intent(mContext, FootballCornerActivity.class));
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
