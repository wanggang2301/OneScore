package com.hhly.mlottery.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;

import java.util.Map;
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
    private ImageView imagView;
    private LinearLayout linear_max;
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
        totalHeight = 150;
        lineHeight = 20;
        totalLine = totalHeight / lineHeight;

    }

   /* public int resultImageView (String mgs){

        switch (mgs){
            case "[吃土了]":
            return R.mipmap.chart_ball_chitule;
            break;
            case "[打裁判]":
                return  R.mipmap.chart_ball_dacaipan;
            break;
            case "[腐败啦]":
                return  R.mipmap.chart_ball_fubaila;
            break;
            case "[警告你]":
                return R.mipmap.chart_ball_jinggaoni;
            break;
            case "[菊一紧]":
                return R.mipmap.chart_ball_juyijing;
            break;
            case "[懵圈了]":
                return R.mipmap.chart_ball_mengkuanle;
            break;
            case "[求大腿]":
                return R.mipmap.chart_ball_qiudatui;
            break;
            case "[射射射]":
                return R.mipmap.chart_ball_sesese;
            break;
            case "[上天台]":
                return R.mipmap.chart_ball_shangtiantai;
            break;
            case "[石化了]":
                return R.mipmap.chart_ball_shihuale;
            break;
            case "[收米啦]":
                return R.mipmap.chart_ball_shoumila;
            break;
            case "[西湖水呀]":
                return  R.mipmap.chart_ball_xihushuiya;
            break;
            case "[压压惊]":
                return R.mipmap.chart_ball_yayajing;
            break;
            case "[有请关老爷]":
                return R.mipmap.chart_ball_youqingguanlaoye;
            break;
            case "[再来一角]":
                return R.mipmap.chart_ball_zailaiyijiao;
            break;
            default:
                break;
        }
    }*/

    public void setDatas(String url,String msg) {

        generateItem();
        if (msg.startsWith("[")){
            for (Map.Entry<String, Integer> entry : AppConstants.localMap.entrySet()) {
                if (msg != null) {
                    if (msg.equals(entry.getKey())) {
                        textView.setVisibility(View.GONE);
                        imagView.setVisibility(View.VISIBLE);
                        imagView.setImageResource(entry.getValue());

                        break;
                    }
                }
            }
        }else {
            textView.setText(msg);
        }


        Glide.with(MyApp.getContext())
                .load(url)
                .error(R.mipmap.center_head)
                .into(imageView);

    }

    private void generateItem() {
        BarrageItem item = new BarrageItem();
        SCREEN_WIDTH = getResources().getDisplayMetrics().widthPixels;
        heightPixels = getResources().getDisplayMetrics().heightPixels;
        //String tx = itemText[(int) (Math.random() * textCount)];
        // int sz = (int) (minSize + (maxSize - minSize) * Math.random());
        relativeLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.barrage_item, null);
        linear_max = (LinearLayout) relativeLayout.findViewById(R.id.linear_max);
        imageView = (ImageView) relativeLayout.findViewById(R.id.face2);//头像
        textView = (TextView) relativeLayout.findViewById(R.id.content2);//文字
        imagView = (ImageView) relativeLayout.findViewById(R.id.content3);
    /*    item.textView = new TextView(mContext);
        item.textView.setText(tx);
        item.textView.setTextSize(sz);
        item.textView.setTextColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        item.textMeasuredWidth = (int) getTextWidth(item, tx, sz);*/
        item.moveSpeed = (int) (minSpeed + (maxSpeed - minSpeed) * Math.random());

        if (totalLine == 0) {
           // UiUtils.toast(mContext,"我哦進來了-");
            totalHeight = 150;
            lineHeight = 20;
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
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

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

    public void delHandler(){
        if(mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}