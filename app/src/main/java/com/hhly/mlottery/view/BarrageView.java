package com.hhly.mlottery.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.UiUtils;

import java.util.Random;


/**
 * Created by yuely198 on 2016/12/5.
 */

public class BarrageView extends RelativeLayout {
    private Context mContext;
    private BarrageHandler mHandler = new BarrageHandler();
    private Random random = new Random(System.currentTimeMillis());
    private static final long BARRAGE_GAP_MIN_DURATION = 1000;//两个弹幕的最小间隔时间
    private static final long BARRAGE_GAP_MAX_DURATION = 2000;//两个弹幕的最大间隔时间
    private int maxSpeed = 10000;//速度，ms
    private int minSpeed = 5000;//速度，ms
    private int maxSize = 30;//文字大小，dp
    private int minSize = 15;//文字大小，dp

    private int totalHeight = 0;
    private int lineHeight = 0;//每一行弹幕的高度
    private int totalLine = 0;//弹幕的行数
    private String[] itemText = {"文博是傻逼!!!!!!", "不为谁而建的群", "老马加油!!!!", "抢占沙发。。。。。。", "************", "为你留下第一滴泪",
            "我不会轻易的狗带", "嘿嘿", "这是我见过的最长长长长长长长长长长长的评论", "淑贞美美哒~", "梓旋美美哒~", "小余美美哒~"};
    private int textCount;
    private RelativeLayout relativeLayout;
    private ImageView imageView;
    private TextView textView;

    private static int SCREEN_WIDTH;
    private int heightPixels;
    //    private List<BarrageItem> itemList = new ArrayList<BarrageItem>();

    public BarrageView(Context context) {
        this(context, null);
    }

    public BarrageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarrageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        textCount = itemText.length;
        int duration = (int) ((BARRAGE_GAP_MAX_DURATION - BARRAGE_GAP_MIN_DURATION) * Math.random());
        mHandler.sendEmptyMessageDelayed(0, duration);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        totalHeight = 200;
        lineHeight = 30;
        totalLine = totalHeight / lineHeight;
        Log.i("sdasdasdas","totalLine="+totalLine);
        Log.i("sdasdasdas","getMeasuredHeight="+totalHeight);
        Log.i("sdasdasdas","lineHeight="+lineHeight);
    }

    public void setDatas(String url,String msg) {
        Log.i("sdasdasdas","我发送消息来了");
        generateItem();
        textView.setText(msg);
    }

    private void generateItem() {
        BarrageItem item = new BarrageItem();
        SCREEN_WIDTH = getResources().getDisplayMetrics().widthPixels;
        heightPixels = getResources().getDisplayMetrics().heightPixels;
        //String tx = itemText[(int) (Math.random() * textCount)];
        // int sz = (int) (minSize + (maxSize - minSize) * Math.random());
        relativeLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.barrage_item, null);
        imageView = (ImageView) relativeLayout.findViewById(R.id.face2);//头像
        textView = (TextView) relativeLayout.findViewById(R.id.content2);//文字
    /*    item.textView = new TextView(mContext);
        item.textView.setText(tx);
        item.textView.setTextSize(sz);
        item.textView.setTextColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        item.textMeasuredWidth = (int) getTextWidth(item, tx, sz);*/
        item.moveSpeed = (int) (minSpeed + (maxSpeed - minSpeed) * Math.random());

        if (totalLine == 0) {
           // UiUtils.toast(mContext,"我哦進來了-");
            totalHeight = 200;
            lineHeight = 30;
            totalLine = totalHeight / lineHeight;
        }
        item.verticalPos = random.nextInt(totalLine) * lineHeight;
//        itemList.add(item);
        showBarrageItem(item);
    }

    private void showBarrageItem(final BarrageItem item) {

        int leftMargin = this.getRight() - this.getLeft() - this.getPaddingLeft();
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.topMargin = item.verticalPos;
        this.addView(relativeLayout, params);
        Animation anim = generateTranslateAnim(relativeLayout, SCREEN_WIDTH);
        Log.i("sdasdasdas","SCREEN_WIDTH"+SCREEN_WIDTH);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i("sdasdasdas","我走了开始");
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.i("sdasdasdas","我走了结束>>>>>");
                //relativeLayout.clearAnimation();
                //  relativeLayout.setVisibility(GONE);
                //
                // BarrageView.this.removeView(relativeLayout);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        relativeLayout.startAnimation(anim);
    }

    private TranslateAnimation generateTranslateAnim(RelativeLayout item, int leftMargin) {

        TranslateAnimation anim = new TranslateAnimation(SCREEN_WIDTH, -SCREEN_WIDTH, 0, 0);
        anim.setDuration(8000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setFillAfter(true);
        return anim;
    }

    /**
     * 计算TextView中字符串的长度
     *
     * @param text 要计算的字符串
     * @param Size 字体大小
     * @return TextView中字符串的长度
     */
    public float getTextWidth(BarrageItem item, String text, float Size) {
        Rect bounds = new Rect();
        TextPaint paint;
        paint = item.textView.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    /**
     * 获得每一行弹幕的最大高度
     *
     * @return
     */
    private int getLineHeight() {
        BarrageItem item = new BarrageItem();
        String tx = itemText[0];
        item.textView = new TextView(mContext);
        item.textView.setText(tx);
        item.textView.setTextSize(maxSize);

        Rect bounds = new Rect();
        TextPaint paint;
        paint = item.textView.getPaint();
        paint.getTextBounds(tx, 0, tx.length(), bounds);
        return bounds.height();
    }

    class BarrageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //  generateItem();
            //每个弹幕产生的间隔时间随机
            int duration = (int) ((BARRAGE_GAP_MAX_DURATION - BARRAGE_GAP_MIN_DURATION) * Math.random());
            this.sendEmptyMessageDelayed(0, duration);
        }
    }
}