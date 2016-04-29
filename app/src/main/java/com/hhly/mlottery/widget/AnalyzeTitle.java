package com.hhly.mlottery.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;

/**
 * Created by andy on 2016/3/4.
 * 分析界面的标题，点击按钮收起对应部分
 */
public class AnalyzeTitle extends FrameLayout {

    private Context context;
    /**
     * 标题
     */
    private TextView mTitle;
    /**
     * 点击时旋转按钮
     */
    private ImageView mButton;
    /**
     * 收起的布局
     */
    public LinearLayout mLayout;

    /**
     * 点击整个标题栏收缩
     */
    private RelativeLayout mTitleLayout;

    /**收起动画*/
    ObjectAnimator buttonRotate1;
    /**展开动画*/
    ObjectAnimator buttonRotate;

    private boolean isVisible = true;


    public AnalyzeTitle(Context context) {
        this(context, null);
    }

    public AnalyzeTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public AnalyzeTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
        initAnim();

    }

    /**
     * 初始化视图
     */
    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.analyze_title, this);
        mTitle = (TextView) findViewById(R.id.analyze_title);

        mButton = (ImageView) findViewById(R.id.analyze_button);

        mLayout = (LinearLayout) findViewById(R.id.analyze_root);

        mTitleLayout = (RelativeLayout) findViewById(R.id.analyze_layout_title);

        mTitleLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isVisible) {
                    buttonRotate.start();
                    mLayout.setVisibility(View.GONE);
                    isVisible = false;
                } else {
                    buttonRotate1.start();
                    mLayout.setVisibility(View.VISIBLE);
                    isVisible = true;
                }

            }
        });
    }

    /**
     * 设置标题
     *
     * @param text
     */
    public void setTitle(String text) {
        mTitle.setText(text);
    }


    /**
     * 添加布局
     *
     * @param id
     */
    public void addLayout(int id) {
        mLayout.removeAllViews();
        LayoutInflater.from(context).inflate(id, mLayout);
    }


    /**
     * 初始化动画
     */
    private void initAnim() {
        buttonRotate = ObjectAnimator.ofFloat(mButton, "rotation", 0, 180);
        buttonRotate1 = ObjectAnimator.ofFloat(mButton, "rotation", 180, 0);

        buttonRotate.setDuration(200);
        buttonRotate1.setDuration(200);


    }
}
