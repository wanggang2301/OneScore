package com.hhly.mlottery.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.ForeignInfomationDetailsAdapter;
import com.hhly.mlottery.bean.foreigninfomation.OverseasInformationListBean;
import com.hhly.mlottery.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 描述:  境外资讯详情Activity
 * 作者:  wangg@13322.com
 * 时间:  2016/9/12 12:11
 */

public class ForeignInfomationDetailsActivity extends BaseActivity {
    @BindView(R.id.recycler_view_details)
    RecyclerView recyclerViewDetails;
    @BindView(R.id.public_img_back)
    ImageView publicImgBack;
    @BindView(R.id.public_btn_set)
    ImageView publicBtnSet;
    @BindView(R.id.iv_tight)
    ImageView ivTight;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.public_txt_title)
    TextView publicTxtTitle;
    @BindView(R.id.header_layout)
    LinearLayout headerLayout;
    @BindView(R.id.civ_logo)
    CircleImageView civLogo;

    @BindView(R.id.tv_name_ch)
    TextView tvNameCh;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_tight)
    TextView tvTight;
    @BindView(R.id.iv_comment)
    ImageView ivComment;
    @BindView(R.id.tv_name_en)
    TextView tvNameEn;
    @BindView(R.id.tv_content_en)
    TextView tvContentEn;
    @BindView(R.id.tv_content_zh)
    TextView tvContentZh;
    private ForeignInfomationDetailsAdapter foreignInfomationDetailsAdapter;
    private List<Integer> list;
    private boolean focusable = false;

    private OverseasInformationListBean moilBean;


    private DisplayImageOptions options; //
    private ImageLoader universalImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_infomation_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        focusable = intent.getBooleanExtra("focusable", false);
        moilBean = intent.getParcelableExtra("detailsData");
        initView();
        initViewData();


        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        foreignInfomationDetailsAdapter = new ForeignInfomationDetailsAdapter(getApplicationContext(), list);
        recyclerViewDetails.setAdapter(foreignInfomationDetailsAdapter);
    }

    private void initView() {

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageOnLoading(R.mipmap.score_default)   //默认图片
                .showImageForEmptyUri(R.mipmap.score_default)    //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.mipmap.score_default)// 加载失败显示的图片
                .resetViewBeforeLoading(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext).build();
        universalImageLoader = ImageLoader.getInstance(); //初始化
        universalImageLoader.init(config);


        publicBtnSet.setVisibility(View.GONE);
        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        if (focusable) {
            recyclerViewDetails.setFocusable(true);
        } else {
            recyclerViewDetails.setFocusable(false);
        }
    }

    private void initViewData() {
        universalImageLoader.displayImage(moilBean.getAvatar(), civLogo, options);

        long mNumberTime = moilBean.getCurrentTimestamp() - moilBean.getTimestamp();
        long month = mNumberTime / (1000 * 60 * 60 * 24 * 30);// 获取月
        long dd = mNumberTime / (1000 * 60 * 60 * 24);// 获取天
        long hh = mNumberTime / (1000 * 60 * 60);// 获取相差小时
        long mm = mNumberTime / (1000 * 60);// 获取相差分
        long ss = mNumberTime / 1000;// 获取相差秒

        String timeMsg = "";

        if (month != 0) {
            timeMsg = dd + "月前";
        } else if (dd != 0) {
            timeMsg = dd + "天前";
        } else if (hh != 0) {
            timeMsg = hh + "小时前";
        } else if (mm != 0) {
            timeMsg = mm + "分钟前";
        } else {
            timeMsg = "刚刚";
        }

        tvTime.setText(timeMsg);
        tvNameEn.setText(moilBean.getFullname());
        tvNameCh.setText(moilBean.getFullnameTranslation());
        tvContentEn.setText(moilBean.getContent());
        tvContentZh.setText(moilBean.getContentTranslation());
        tvTight.setText(moilBean.getFavorite() + "");
        universalImageLoader.displayImage(moilBean.getPhoto(), ivPhoto, options);
    }

    @OnClick({R.id.public_img_back, R.id.tv_comment, R.id.iv_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_img_back:
                this.finish();
                break;
            case R.id.tv_comment:
                break;

            case R.id.iv_photo:
                String url = moilBean.getPhoto();
                Intent intent = new Intent(ForeignInfomationDetailsActivity.this, PicturePreviewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
        }
    }
}
