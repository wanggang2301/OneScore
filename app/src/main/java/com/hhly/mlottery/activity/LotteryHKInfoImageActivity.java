package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.ShareBean;
import com.hhly.mlottery.bean.numbersBean.LotteryInfoHKChartBean;
import com.hhly.mlottery.frame.ShareFragment;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.widget.ZoomViewPager;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * desc:香港彩详情图表图片查看
 * Created by 107_tangrr on 2017/1/14 0014.
 */

public class LotteryHKInfoImageActivity extends BaseActivity implements View.OnClickListener {

    private List<LotteryInfoHKChartBean.DataBean> mData = new ArrayList<>();
    private ZoomViewPager view_pager;
    private TextView public_txt_title;
    private int index = -1;
    private PicAdapter adapter;
    private ImageView iv_right;
    private ImageView iv_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hk_info_image);

        mData = (List<LotteryInfoHKChartBean.DataBean>) getIntent().getSerializableExtra("data");
        index = getIntent().getIntExtra("index", -1);

        initView();
    }

    private void initView() {

        findViewById(R.id.public_img_back).setOnClickListener(this);
        ImageView btn_set = (ImageView) findViewById(R.id.public_btn_set);
        btn_set.setOnClickListener(this);

        if (CommonUtils.isZH()) {
            btn_set.setVisibility(View.VISIBLE);
        } else {
            btn_set.setVisibility(View.GONE);
        }

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_left.setOnClickListener(this);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_right.setOnClickListener(this);
        btn_set.setImageResource(R.mipmap.share);// 设置为分享图片
        view_pager = (ZoomViewPager) findViewById(R.id.view_pager);
        adapter = new PicAdapter();
        view_pager.setAdapter(adapter);

        if (index != -1) {
            public_txt_title.setText(mData.get(index).getTitle() == null ? "" : mData.get(index).getTitle());// 设置图片标题
            view_pager.setCurrentItem(index);

            if (index == 0) {
                iv_left.setVisibility(View.GONE);
            }
            if (index >= mData.size() - 1) {
                iv_right.setVisibility(View.GONE);
            }
        }

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                public_txt_title.setText(mData.get(position).getTitle() == null ? "" : mData.get(position).getTitle());// 设置图片标题

                index = position;

                if (position <= 0) {
                    iv_left.setVisibility(View.GONE);
                } else {
                    iv_left.setVisibility(View.VISIBLE);
                }

                if (position >= mData.size() - 1) {
                    iv_right.setVisibility(View.GONE);
                } else {
                    iv_right.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_img_back:
                finish();
                break;
            case R.id.public_btn_set:// 分享
                ShareBean shareBean = new ShareBean();
                shareBean.setTitle(mData.get(index).getTitle());
                shareBean.setSummary(mData.get(index).getSummary());
                shareBean.setImage_url(mData.get(index).getPicUrl());
                shareBean.setTarget_url(mData.get(index).getJumpAddr());
                shareBean.setCopy(mData.get(index).getJumpAddr());
                ShareFragment share = ShareFragment.newInstance(shareBean);
                share.show(getSupportFragmentManager(), "LotteryHKInfoImageActivity");
                break;
            case R.id.iv_left:// 上一张
                iv_right.setVisibility(View.VISIBLE);
                if (index > 0) {
                    index--;
                    public_txt_title.setText(mData.get(index).getTitle() == null ? "" : mData.get(index).getTitle());// 设置图片标题
                    view_pager.setCurrentItem(index);
                    if (index <= 0) {
                        iv_left.setVisibility(View.GONE);
                    }
                } else {
                    iv_left.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_right:// 下一张
                iv_left.setVisibility(View.VISIBLE);
                if (index < mData.size() - 1) {
                    index++;
                    public_txt_title.setText(mData.get(index).getTitle() == null ? "" : mData.get(index).getTitle());// 设置图片标题
                    view_pager.setCurrentItem(index);
                    if (index >= mData.size() - 1) {
                        iv_right.setVisibility(View.GONE);
                    }
                } else {
                    iv_right.setVisibility(View.GONE);
                }
                break;
        }
    }

    //数据适配器
    class PicAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = View.inflate(mContext, R.layout.activity_hk_info_image_item, null);
            PhotoView photo_view = (PhotoView) view.findViewById(R.id.photo_view);

            try {
                Glide.with(mContext).load(mData.get(position).getLotteryImg()).error(R.mipmap.impage_loading_error).into(photo_view);
            } catch (Exception e) {
                e.printStackTrace();
            }

            container.addView(view);
            return view;
        }
    }
}
