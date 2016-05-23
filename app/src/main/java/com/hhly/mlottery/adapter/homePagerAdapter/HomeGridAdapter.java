package com.hhly.mlottery.adapter.homePagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketListActivity;
import com.hhly.mlottery.activity.FootballActivity;
import com.hhly.mlottery.activity.LoginActivity;
import com.hhly.mlottery.activity.NumbersActivity;
import com.hhly.mlottery.activity.NumbersInfoBaseActivity;
import com.hhly.mlottery.activity.WebActivity;
import com.hhly.mlottery.bean.homepagerentity.HomeContentEntity;
import com.hhly.mlottery.bean.homepagerentity.HomePagerEntity;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.L;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

/**
 * 种类总入口GridView数据适配器
 * Created by hhly107 on 2016/3/30.
 */
public class HomeGridAdapter extends BaseAdapter {

    private Context mContext;
    private HomePagerEntity mHomePagerEntity;
    private HomeListBaseAdapter.ViewHolder mTopHolder;// 头部ViewHolder

    private ViewHolder mViewHolder;// ViewHolder
    private DisplayImageOptions options;// 设置ImageLoder参数
    private final int MIN_CLICK_DELAY_TIME = 1000;// 控件点击间隔时间
    private long lastClickTime = 0;

    /**
     * 构造
     *
     * @param context         上下文
     * @param homePagerEntity 首页实体类
     * @param topHolder       显示容器
     */
    public HomeGridAdapter(Context context, HomePagerEntity homePagerEntity, HomeListBaseAdapter.ViewHolder topHolder) {
        this.mContext = context;
        this.mHomePagerEntity = homePagerEntity;
        this.mTopHolder = topHolder;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.home_menu_icon_def).showImageOnFail(R.mipmap.home_menu_icon_def)
                .cacheInMemory(true).bitmapConfig(Bitmap.Config.ARGB_8888)
                .cacheOnDisc(true).considerExifParams(true).build();

        initGridListener();
    }

    /**
     * GridView事件监听事件
     */
    private void initGridListener() {
        try {
            mTopHolder.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                        lastClickTime = currentTime;
                        HomeContentEntity homeContentEntity = mHomePagerEntity.getMenus().getContent().get(position);
                        int jumpType = homeContentEntity.getJumpType();// 跳转类型
                        String jumpAddr = homeContentEntity.getJumpAddr();// 跳转地址
                        String reqMethod = homeContentEntity.getReqMethod();// 跳转方式
                        if (!TextUtils.isEmpty(jumpAddr)) {
                            switch (jumpType) {
                                case 0:// 无
                                    break;
                                case 1:// 页面
                                {
                                    if(jumpAddr.contains("?token")){// 请求需要带参
                                        if (CommonUtils.isLogin()) {// 判断用户是否登录
                                            Intent intent = new Intent(mContext, WebActivity.class);
                                            intent.putExtra("key", jumpAddr.substring(0,jumpAddr.indexOf("?")));// 跳转地址
                                            intent.putExtra("reqMethod", reqMethod);// 跳转方式 get or post
                                            intent.putExtra("token", AppConstants.register.getData().getLoginToken());// 用户token
                                            mContext.startActivity(intent);
                                        } else {// 跳转到登录界面
                                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                                        }
                                    }else {// 其它
                                        Intent intent = new Intent(mContext, WebActivity.class);
                                        intent.putExtra("key", jumpAddr);
                                        intent.putExtra("reqMethod", reqMethod);// 跳转方式 get or post
                                        mContext.startActivity(intent);
                                    }
                                    /*if ("41".equals(jumpAddr)) {// 游戏竞猜
                                        if (CommonUtils.isLogin()) {// 判断用户是否登录
                                            Intent intent = new Intent(mContext, WebActivity.class);
                                            intent.putExtra("key", homeContentEntity.getOutUrl());// 跳转地址
                                            intent.putExtra("isComment", jumpAddr);// 41
                                            intent.putExtra("token", AppConstants.register.getData().getLoginToken());// 用户token
                                            mContext.startActivity(intent);
                                        } else {// 跳转到登录界面
                                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                                        }
                                    } else {// 其它
                                        Intent intent = new Intent(mContext, WebActivity.class);
                                        intent.putExtra("key", jumpAddr);
                                        mContext.startActivity(intent);
                                    }*/
                                    break;
                                }
                                case 2:// 跳内页
                                    switch (jumpAddr) {
                                        case "10":// 足球指数
                                        {
                                            Intent intent = new Intent(mContext, FootballActivity.class);
                                            intent.putExtra(AppConstants.FOTTBALL_KEY, AppConstants.FOTTBALL_EXPONENT_VALUE);
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Football_Index");
                                        }
                                        break;
                                        case "11":// 足球数据
                                        {
                                            Intent intent = new Intent(mContext, FootballActivity.class);
                                            intent.putExtra(AppConstants.FOTTBALL_KEY, AppConstants.FOTTBALL_DATA_VALUE);
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Football_Data");
                                        }
                                        break;
                                        case "12":// 足球资讯
                                        {
                                            Intent intent = new Intent(mContext, FootballActivity.class);
                                            intent.putExtra(AppConstants.FOTTBALL_KEY, AppConstants.FOTTBALL_INFORMATION_VALUE);
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Football_Information");
                                        }
                                        break;
                                        case "13":// 足球比分
                                        {
                                            Intent intent = new Intent(mContext, FootballActivity.class);
                                            intent.putExtra(AppConstants.FOTTBALL_KEY, AppConstants.FOTTBALL_SCORE_VALUE);
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Football_Score");
                                        }
                                        break;
                                        case "14":// 足球视频
                                        {
                                            Intent intent = new Intent(mContext, FootballActivity.class);
                                            intent.putExtra(AppConstants.FOTTBALL_KEY, AppConstants.FOTTBALL_VIDEO_VALUE);
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Football_Video");
                                        }
                                        break;
                                        case "20":// 篮球即时比分
                                        {
                                            Intent intent = new Intent(mContext, BasketListActivity.class);
                                            intent.putExtra(AppConstants.BASKETBALL_KEY, AppConstants.BASKETBALL_SCORE_KEY);
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Basketball_Score");
                                        }
                                        break;
                                        case "21":// 篮球赛果
                                        {
                                            Intent intent = new Intent(mContext, BasketListActivity.class);
                                            intent.putExtra(AppConstants.BASKETBALL_KEY, AppConstants.BASKETBALL_AMIDITHION_VALUE);
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Basketball_Amidithion");
                                        }
                                        break;
                                        case "22":// 篮球赛程
                                        {
                                            Intent intent = new Intent(mContext, BasketListActivity.class);
                                            intent.putExtra(AppConstants.BASKETBALL_KEY, AppConstants.BASKETBALL_COMPETITION_VALUE);
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Basketball_Competition");
                                        }
                                        break;
                                        case "23":// 篮球关注
                                        {
                                            Intent intent = new Intent(mContext, BasketListActivity.class);
                                            intent.putExtra(AppConstants.BASKETBALL_KEY, AppConstants.BASKETBALL_ATTENTION_VALUE);
                                            mContext.startActivity(intent);
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Basketball_Attention");
                                        }
                                        break;
                                        case "24":// 篮球资讯
                                            Toast.makeText(mContext, "篮球资讯", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "30":// 彩票开奖
                                            mContext.startActivity(new Intent(mContext, NumbersActivity.class));
                                            MobclickAgent.onEvent(mContext, "HomePager_Menu_Lottery_List");
                                            break;
                                        case "350":// 彩票资讯
                                            Toast.makeText(mContext, "彩票资讯", Toast.LENGTH_SHORT).show();
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

    @Override
    public int getCount() {
        if (mHomePagerEntity == null || mHomePagerEntity.getMenus() == null || mHomePagerEntity.getMenus().getContent() == null || mHomePagerEntity.getMenus().getContent().size() == 0) {
            return 0;
        }
        return mHomePagerEntity.getMenus().getContent().size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.home_page_item_menu_icon, null);
            mViewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_menu_icon_home);
            mViewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_menu_name_home);
            convertView.setTag(mViewHolder);
        }
        mViewHolder = (ViewHolder) convertView.getTag();
        HomeContentEntity mContentEntity = (HomeContentEntity) getItem(position);

        if (mContentEntity.getPicUrl() == null) {
            mViewHolder.iv_icon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.home_menu_icon_def));
        } else {
            ImageLoader.getInstance().displayImage(mContentEntity.getPicUrl(), mViewHolder.iv_icon, options);// 设置图标
        }
        mViewHolder.tv_name.setText(mContentEntity.getTitle());

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return mHomePagerEntity.getMenus().getContent().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
    }
}
