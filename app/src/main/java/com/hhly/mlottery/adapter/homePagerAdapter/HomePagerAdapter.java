package com.hhly.mlottery.adapter.homePagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.CounselActivity;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.activity.HomeUserOptionsActivity;
import com.hhly.mlottery.activity.LoginActivity;
import com.hhly.mlottery.activity.NumbersActivity;
import com.hhly.mlottery.activity.NumbersInfoBaseActivity;
import com.hhly.mlottery.activity.VideoActivity;
import com.hhly.mlottery.activity.WebActivity;
import com.hhly.mlottery.bean.homepagerentity.HomeBannersEntity;
import com.hhly.mlottery.bean.homepagerentity.HomeContentEntity;
import com.hhly.mlottery.bean.homepagerentity.HomePagerEntity;
import com.hhly.mlottery.config.FootBallDetailTypeEnum;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.HandMatchId;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页轮播图数据适配器
 * Created by hhly107 on 2016/3/30.
 */
public class HomePagerAdapter extends PagerAdapter {
    private Context mContext;
    private HomePagerEntity mHomePagerEntity;
    private long downIime;// 轮播图按下时间
    private long upTime;// 轮播图按下松开时间
    private LunboTask mTask;// 轮播图控制器
    private final int BANNER_PLAY_TIME = 10000;// 自动轮播间隔时长
    private final int BANNER_ANIM_TIME = 500;// 轮播切换动画的时长
    private HomeListBaseAdapter.ViewHolder mTopHolder;// 头部ViewHolder

    private final int MIN_CLICK_DELAY_TIME = 1000;// 控件点击间隔时间
    private long lastClickTime = 0;

    public HomePagerAdapter(Context context, HomePagerEntity homePagerEntity, HomeListBaseAdapter.ViewHolder topHolder) {
        this.mContext = context;
        this.mHomePagerEntity = homePagerEntity;
        this.mTopHolder = topHolder;

        setBannersDefPic();
        onDrawPoint();
        setScroller();
        initBannerListener();
    }

    /**
     * 设置默认轮播图数据
     */
    private void setBannersDefPic() {
        if (mHomePagerEntity != null) {
            if (mHomePagerEntity.getBanners() == null) {// 设置默认轮播图数据
                HomeBannersEntity banners = new HomeBannersEntity();
                List<HomeContentEntity> content = new ArrayList<>();
                HomeContentEntity entity = new HomeContentEntity();
                entity.setPicUrl("xxx");
                content.add(entity);
                banners.setContent(content);
                mHomePagerEntity.setBanners(banners);
            }
        }
        if (mTask == null) {// 开启轮播图
            mTask = new LunboTask();
            mTask.startLunbo();
        }
    }

    /**
     * 轮播图监听事件
     */
    private void initBannerListener() {
        mTopHolder.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int len = 0;
                if (mHomePagerEntity == null || mHomePagerEntity.getBanners() == null || mHomePagerEntity.getBanners().getContent() == null || mHomePagerEntity.getBanners().getContent().size() == 0) {

                } else {
                    len = mHomePagerEntity.getBanners().getContent().size();
                }
                for (int i = 0; i < len; i++) {
                    mTopHolder.ll_point.getChildAt(i).setEnabled(i == position % len);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 绘制小圆点
     */
    private void onDrawPoint() {

        int dp = DisplayUtil.dip2px(mContext, 5);// 添加小圆点
        int len = 0;
        if (mHomePagerEntity == null || mHomePagerEntity.getBanners() == null || mHomePagerEntity.getBanners().getContent() == null || mHomePagerEntity.getBanners().getContent().size() == 0) {

        } else {
            len = mHomePagerEntity.getBanners().getContent().size();
        }
        for (int i = 0; i < len; i++) {
            View view = new View(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp, dp);
            if (i != 0) {
                lp.leftMargin = dp;
            }
            view.setEnabled(i == 0);
            view.setBackgroundResource(R.drawable.v_lunbo_point_selector);
            view.setLayoutParams(lp);
            mTopHolder.ll_point.addView(view);
        }
        mTopHolder.mViewPager.setPageTransformer(true, new DepthPageTransformer());// 设置轮播图弹出样式
    }


    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mHomePagerEntity == null || mHomePagerEntity.getBanners() == null || mHomePagerEntity.getBanners().getContent() == null || mHomePagerEntity.getBanners().getContent().size() == 0) {

        } else {
            position %= mHomePagerEntity.getBanners().getContent().size();
            if (position < 0) {
                position = mHomePagerEntity.getBanners().getContent().size() + position;
            }
        }
        ImageView iv = new ImageView(mContext);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        String picUrl = null;
        if (mHomePagerEntity == null || mHomePagerEntity.getBanners() == null || mHomePagerEntity.getBanners().getContent() == null || mHomePagerEntity.getBanners().getContent().size() == 0) {

        } else {
            picUrl = mHomePagerEntity.getBanners().getContent().get(position).getPicUrl();
        }
        ImageLoader.load(mContext, picUrl, R.mipmap.home_carousel_default).into(iv);

        final int index = position;
        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downIime = System.currentTimeMillis();
                        mTask.stopLunbo();
                        break;
                    case MotionEvent.ACTION_UP:
                        upTime = System.currentTimeMillis();
                        mTask.startLunbo();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mTask.startLunbo();
                        break;
                }
                if (downIime != 0 && upTime != 0 && (upTime - downIime) <= 200 && (upTime - downIime) >= 0) {
                    jumpInstruction(index);
                }
                return true;
            }
        });

        container.addView(iv);
        return iv;
    }

    /**
     * 轮播图片点击事件-> 跳转指令
     *
     * @param index 当前对象下标
     */
    private void jumpInstruction(int index) {
        MobclickAgent.onEvent(mContext, "HomePager_Banner");
        try {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                String jumpAddr = mHomePagerEntity.getBanners().getContent().get(index).getJumpAddr();
                int jumpType = mHomePagerEntity.getBanners().getContent().get(index).getJumpType();
                Integer labSeq = mHomePagerEntity.getBanners().getContent().get(index).getLabSeq();
                String title = mHomePagerEntity.getBanners().getContent().get(index).getTitle();
                String picUrl = mHomePagerEntity.getBanners().getContent().get(index).getPicUrl();

                if (!TextUtils.isEmpty(jumpAddr)) {
                    switch (jumpType) {
                        case 5:// 调用浏览器
                        {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(jumpAddr);
                            intent.setData(content_url);
                            mContext.startActivity(intent);
                        }
                        break;
                        case 0:// 无
                            break;
                        case 6:// 德州跳转
                            if (DeviceInfo.isLogin()) {
                                Intent intent = new Intent(mContext, WebActivity.class);
                                intent.putExtra("key", jumpAddr);// 跳转地址
                                intent.putExtra("infoTypeName", title);
                                intent.putExtra("imageurl", picUrl);
                                intent.putExtra("title", title);
                                intent.putExtra("subtitle", "");
                                mContext.startActivity(intent);
                            } else {// 跳转到登录界面
                                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            }
                            break;
                        case 1:// 页面
                        {
                            if (jumpAddr.contains("{loginToken}")) {// 是否需要登录
                                if (DeviceInfo.isLogin()) {// 判断用户是否登录
                                    Intent intent = new Intent(mContext, WebActivity.class);
                                    intent.putExtra("key", jumpAddr);// 跳转地址
                                    intent.putExtra("infoTypeName", title);
                                    intent.putExtra("imageurl", picUrl);
                                    intent.putExtra("title", title);
                                    intent.putExtra("subtitle", "");
//                                    intent.putExtra("token", AppConstants.register.getData().getLoginToken());// 用户token
                                    mContext.startActivity(intent);
                                } else {
                                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                                }
                            } else {
                                Intent intent = new Intent(mContext, WebActivity.class);
                                intent.putExtra("key", jumpAddr);// 跳转地址
                                intent.putExtra("infoTypeName", title);
                                intent.putExtra("imageurl", picUrl);
                                intent.putExtra("title", title);
                                intent.putExtra("subtitle", "");
                                mContext.startActivity(intent);
                            }
                            break;
                        }
                        case 2:// 内页
                            switch (jumpAddr) {
                                case "10":// 足球指数
                                    break;
                                case "11":// 足球数据
                                    break;
                                case "12":// 体育资讯
                                    Intent foot_intent = new Intent(mContext, CounselActivity.class);
                                    foot_intent.putExtra("currentIndex", labSeq);
                                    mContext.startActivity(foot_intent);
                                    MobclickAgent.onEvent(mContext, "HomePager_Menu_Football_Information");
                                    break;
                                case "13":// 足球比分
                                    break;
                                case "14":// 足球视频
                                    mContext.startActivity(new Intent(mContext, VideoActivity.class));
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
                                case "30":// 彩票开奖
                                    mContext.startActivity(new Intent(mContext, NumbersActivity.class));
                                    break;
                                case "350":// 彩票资讯
                                    break;
                                case "31":// 香港开奖
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.ONE));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "32":// 重庆时时彩
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWO));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "33":// 江西时时彩
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.THREE));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "34":// 新疆时时彩
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.FOUR));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "35":// 云南时时彩
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.FIVE));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "36":// 七星彩
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.SIX));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "37":// 广东11选5
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.SEVEN));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "38":// 广东快乐10分
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.EIGHT));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "39":// 湖北11选5
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.NINE));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "310":// 安徽快3
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TEN));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "311":// 湖南快乐10分
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.ELEVEN));
                                    mContext.startActivity(intent);
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
                                }
                                break;
                                case "315":// 北京赛车
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.FIFTEEN));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "316":// 江苏快3
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.SIRTEEN));
                                    mContext.startActivity(intent);
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
                                }
                                break;
                                case "319":// 幸运农场
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.NINETEEN));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "320":// 江苏11选5
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "321":// 江西11选5
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_ONE));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "322":// 山东11选5
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_TWO));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "323":// 天津时时彩
                                {
                                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                                    intent.putExtra(AppConstants.LOTTERY_KEY, String.valueOf(AppConstants.TWENTY_THREE));
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "80":// 多屏动画列表
                                    break;
                                case "90":// 个人中心
                                    mContext.startActivity(new Intent(mContext, HomeUserOptionsActivity.class));
                                    break;
                                case "42":// 香港彩票图表页面
                                    break;
                            }
                            break;
                        case 3:// 轮播图跳转足球和篮球内页
                            if (jumpAddr.contains("&")) {
                                String[] split = jumpAddr.split("&");
                                switch (split[0]) {
                                    case "18":// 足球
                                    {
                                        if (HandMatchId.handId(mContext, split[1])) {

                                            Intent intent = new Intent(mContext, FootballMatchDetailActivity.class);
                                            intent.putExtra("thirdId", split[1]);
                                            intent.putExtra("currentFragmentId", 0);
                                            intent.putExtra(FootBallDetailTypeEnum.CURRENT_TAB_KEY, FootBallDetailTypeEnum.FOOT_DETAIL_LIVE);
                                            mContext.startActivity(intent);
                                        }
                                    }
                                    break;
                                    case "26":// 篮球
                                    {
                                        Intent intent = new Intent(mContext, BasketDetailsActivityTest.class);
                                        intent.putExtra("thirdId", split[1]);
                                        intent.putExtra("chart_ball_view", 1);
                                        // 后台固定只配置NBA赛事
                                        intent.putExtra("leagueId", 1);
                                        intent.putExtra("matchType", 1);
                                        mContext.startActivity(intent);
                                    }
                                    break;
                                }
                            }
                            break;
                    }
                }
            }
        } catch (Exception e) {
            L.d("jumpInstruction失败：" + e.getMessage());
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void start() {
        if (mTask != null) {
            mTask.startLunbo();
        }
    }

    public void end() {
        if (mTask != null) {
            mTask.stopLunbo();
        }
    }

    /**
     * 轮播控制中心
     */
    class LunboTask extends Handler implements Runnable {
        public void startLunbo() {
            stopLunbo();// 开始轮播之前清除一下原来的消息
            postDelayed(this, BANNER_PLAY_TIME);
        }

        public void stopLunbo() {
            removeCallbacksAndMessages(null);// 清除所有消息和回调
        }

        @Override
        public void run() {
            setViewPagerItem();
            postDelayed(this, BANNER_PLAY_TIME);
        }
    }

    public void setViewPagerItem() {

        if (mTopHolder.mViewPager.getCurrentItem() == mTopHolder.mViewPager.getAdapter().getCount() - 1) {
            mTopHolder.mViewPager.setCurrentItem(0, true);
        } else {
            mTopHolder.mViewPager.setCurrentItem(mTopHolder.mViewPager.getCurrentItem() + 1, true);
        }
    }

    public void setScroller() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            MyScroller mScroller = new MyScroller(mTopHolder.mViewPager.getContext(),
                    new AccelerateInterpolator());
            mField.set(mTopHolder.mViewPager, mScroller);
        } catch (Exception ee) {
            L.d("Exception: " + ee.getMessage());
        }
    }

    class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context);
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy,
                                int duration) {
            super.startScroll(startX, startY, dx, dy, BANNER_ANIM_TIME);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, BANNER_ANIM_TIME);
        }
    }

    class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                        * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
