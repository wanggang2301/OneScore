package com.hhly.mlottery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Scroller;

import com.bumptech.glide.Glide;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.WebActivity;
import com.hhly.mlottery.bean.footballDetails.CounselBean;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.MyLineChart;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 资讯轮播图数据适配器
 * Created by hhly204 on 2016/4/18
 */
public class CounselPageAdapter extends PagerAdapter {
    private Context mContext;
    private long downIime;// 轮播图按下时间
    private long upTime;// 轮播图按下松开时间
    private LunboTask mTask;// 轮播图控制器
    private final int BANNER_PLAY_TIME = 5000;// 自动轮播间隔时长
    private final int BANNER_ANIM_TIME = 500;// 轮播切换动画的时长
    private ViewPager mViewPager;//
    private String mHeadName;//头名称
    private List<CounselBean.InfoIndexBean.AdsBean> mAdsList;//
    public final static String INTENT_PARAM_THIRDID = "thirdId";
    public final static String INTENT_PARAM_JUMPURL = "key";
    public final static String INTENT_PARAM_TYPE = "type";
    public final static String INTENT_PARAM_TITLE = "infoTypeName";

    public CounselPageAdapter(Context context, List<CounselBean.InfoIndexBean.AdsBean> mAdsList, ViewPager mViewPager,String mHeadName) {
        this.mContext = context;
        this.mAdsList = mAdsList;
        this.mViewPager = mViewPager;
        this.mHeadName=mHeadName;

        if (mTask == null) {
            mTask = new LunboTask();
            mTask.startLunbo();
        }
        setScroller();

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
        if (mAdsList.size() != 0) {
            position %= mAdsList.size();
            if (position < 0) {
                position = mAdsList.size() + position;
            }
        }
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        String picUrl = null;
        if (mAdsList.size() != 0) {
            picUrl = mAdsList.get(position).getPicUrl();
        }
        ImageLoader.load(mContext,picUrl,R.mipmap.counsel_binner).into(imageView);
        final int index = position;
        imageView.setOnTouchListener(new View.OnTouchListener() {
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

        container.addView(imageView);
        return imageView;
    }

    /**
     * 轮播图片点击事件-> 跳转指令
     *
     * @param index 当前对象下标
     */
    private void jumpInstruction(final int index) {
        Intent intent = new Intent(mContext, WebActivity.class);
        String jumpurl = mAdsList.get(index).getJumpAddr();
        boolean isRelateMatch = mAdsList.get(index).isRelateMatch();
        String ThirdId = mAdsList.get(index).getThirdId();
        int type = mAdsList.get(index).getType();

        String  title = mAdsList.get(index ).getTitle();//标题
//      String  subtitle = mAdsList.get(index ).getSubTitle();//轮播图没有副标题
        String imageurl = mAdsList.get(index ).getPicUrl();//图片url

        if (isRelateMatch) {
            intent.putExtra(INTENT_PARAM_THIRDID, ThirdId);
            intent.putExtra(INTENT_PARAM_TYPE, type);
        }
        intent.putExtra(INTENT_PARAM_TITLE, mHeadName);
        intent.putExtra(INTENT_PARAM_JUMPURL, jumpurl);
        intent.putExtra("title", title);
        intent.putExtra("imageurl", imageurl);
        mContext.startActivity(intent);

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void start() {
        if (mTask != null) {
            mTask.startLunbo();
            MyLineChart a;
        }
    }

    public void end() {
        if (mTask != null) {
            mTask.stopLunbo();
            L.i("position=stopLunbo()" );
        }
    }

    /**
     * 轮播控制中心
     */
   public  class LunboTask extends Handler implements Runnable {
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

        if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1) {
            mViewPager.setCurrentItem(0, true);
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        }
    }

    public void setScroller() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            MyScroller mScroller = new MyScroller(mViewPager.getContext(),
                    new AccelerateInterpolator());
            mField.set(mViewPager, mScroller);
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
}
