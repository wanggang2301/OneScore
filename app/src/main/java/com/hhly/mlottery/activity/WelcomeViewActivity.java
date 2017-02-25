package com.hhly.mlottery.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.widget.OnViewChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenney
 * @ClassName: WelcomeViewActivity
 * @Description: 引导界面
 * @date 2015-12-22 下午12:21:26
 */
public class WelcomeViewActivity extends BaseActivity implements OnViewChangeListener {

    //private MyScrollLayout mScrollLayout;
    private Button startBtn;
    //开机引导页的几张动画
    //  private RelativeLayout wel_layout_img1, wel_layout_img2, wel_layout_img3;
    private RelativeLayout wel_layout_img3;
    private ViewPager welcome_viewpager;
    private int[] imageView;//引导图集合


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomeview);
        initView();
        initImg();
        addView();

    }

    private void initView() {
     /*   mScrollLayout = $(R.id.ScrollLayout);
        startBtn = $(R.id.startBtn);
        startBtn.setOnClickListener(notifyDataSetChanged);
        mScrollLayout.SetOnViewChangeListener(this);
        wel_layout_img1 = $(R.id.wel_layout_img1);
        wel_layout_img2 = $(R.id.wel_layout_img2);*/
        wel_layout_img3 = $(R.id.wel_layout_img3);
        startBtn = $(R.id.startBtn);
        startBtn.setOnClickListener(onClick);
        welcome_viewpager = (ViewPager) findViewById(R.id.welcome_viewpager);

        // 2.监听当前显示的页面，将对应的小圆点设置为选中状态，其它设置为未选中状态
        welcome_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // monitorPoint(position);
                // 3.当滑动到最后一个添加按钮点击进入，
                if (position == imageView.length - 1) {
                    wel_layout_img3.setVisibility(View.VISIBLE);
                } else {
                    wel_layout_img3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 添加图片到view
     */
    private void addView() {
        List<View> list = new ArrayList<View>();
        // 将imageview添加到view
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < imageView.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoader.load(mContext, imageView[i]).into(iv);
//            iv.setImageResource(imageView[i]);
            list.add(iv);
        }
        // 加入适配器
        welcome_viewpager.setAdapter(new GuideViewAdapter(list));

    }

    //判断是什么语言加载什么图片
    public void initImg() {
        //如果是繁体语言环境
       /* if (MyApp.isLanguage.equals("rTW")) {
            wel_layout_img1.setBackgroundResource(R.mipmap.welcome1);
            wel_layout_img2.setBackgroundResource(R.mipmap.welcome2);
            wel_layout_img3.setBackgroundResource(R.mipmap.welcome3);
        } else if (MyApp.isLanguage.equals("rCN")) {
            wel_layout_img1.setBackgroundResource(R.mipmap.welcome1);
            wel_layout_img2.setBackgroundResource(R.mipmap.welcome2);
            wel_layout_img3.setBackgroundResource(R.mipmap.welcome3);
        }*/
        if (MyApp.isLanguage.equals("rTW")) {
            imageView = new int[]{R.mipmap.welcome1, R.mipmap.welcome2,
                    R.mipmap.welcome3};
        } else if (MyApp.isLanguage.equals("rCN")) {
            imageView = new int[]{R.mipmap.welcome1, R.mipmap.welcome2,
                    R.mipmap.welcome3};
        }
    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.startBtn:
                    //第一次启动保存为yes
                    PreferenceUtil.commitString("isFirst", "YES");
                    //第一次启动的时候保存版本号
                    try {
                        // 得到应用程序的包信息对象
                        PackageManager packageManager = getPackageManager();
                        PackageInfo packageInfo = packageManager.getPackageInfo(getApplicationContext().getPackageName(), 0);
                        PreferenceUtil.commitString("versionName", packageInfo.versionName);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                        // 此异常不会发生
                    }

                    startActivity(new Intent(WelcomeViewActivity.this, IndexActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 屏蔽返回键的方法
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                return true;
            case KeyEvent.KEYCODE_BACK:
                return true;
            case KeyEvent.KEYCODE_CALL:
                return true;
            case KeyEvent.KEYCODE_SYM:
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                return true;
            case KeyEvent.KEYCODE_STAR:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void OnViewChange(int view) {
        // TODO Auto-generated method stub

    }

    public class GuideViewAdapter extends PagerAdapter {

        private List<View> list;

        public GuideViewAdapter(List<View> list) {
            this.list = list;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(list.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

    }

}