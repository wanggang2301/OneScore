package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.VideoHighLights;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.ImageLoaderUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.ViewPagerFixed;
import com.hhly.mlottery.widget.ZoomViewPager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * 视频集锦
 */

public class PlayHighLightActivity extends Activity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnCompletionListener {
    private ZoomViewPager mViewPager;
    private MyPagerAdapter mAdapter;

    private final static String GIF_TYPE = "0";
    private final static String VIDEO_TYPE = "1";


    private final static int REQUEST_ERROR = -1;
    private final static int REQUEST_SUCESS = 1;
    private final static int REQUEST_LOAD = 0;


    /***
     * viewpager的根视图数据集合
     ***/
    private List<ViewGroup> mViewList;

    /***
     * 当前页面索引
     ***/
    private int currentItem = 0;

    /***
     * 上一个页面索引
     ***/
    private int lastItem = 0;

    /***
     * 页面的视频控件集合
     ***/
    private List<View> mVideoViewList;

    /***
     * 页面视频缓冲图集合
     ***/
    private List<View> mCacheViewList;
    private List<VideoHighLights.DataBean> mList;
    private TextView tvImageIndex;
    private TextView tv_text;
    private ImageView iv_back;
    private ProgressBar pb_load;
    private RelativeLayout rl_content;
    private LinearLayout ll_error;
    private TextView reloading_txt;
    private String mThirdId;
    private String match_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        L.d("wangg", "onCreate");
        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            mThirdId = intent.getStringExtra("thirdId");
            match_type = intent.getStringExtra("match_type");
        }

        L.d("zxcvbn", "mThirdId=" + mThirdId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_high_light);
        initView();
        // initializeData();


        getRequestGif();
    }

    private void initView() {

        pb_load = (ProgressBar) findViewById(R.id.pb_load);
        rl_content = (RelativeLayout) findViewById(R.id.rl_content);

        ll_error = (LinearLayout) findViewById(R.id.error);
        reloading_txt = (TextView) findViewById(R.id.reloading_txt);
        mViewPager = (ZoomViewPager) this.findViewById(R.id.mViewPager);
        tvImageIndex = (TextView) findViewById(R.id.tv_image_index);
        tv_text = (TextView) findViewById(R.id.tv_text);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        mViewList = new ArrayList<>();
        mVideoViewList = new ArrayList<>();
        mCacheViewList = new ArrayList<>();
        mList = new ArrayList<>();

        reloading_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRequestGif();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case REQUEST_LOAD:
                    pb_load.setVisibility(View.VISIBLE);
                    rl_content.setVisibility(View.GONE);
                    ll_error.setVisibility(View.GONE);

                    break;
                case REQUEST_SUCESS:
                    pb_load.setVisibility(View.GONE);
                    rl_content.setVisibility(View.VISIBLE);
                    ll_error.setVisibility(View.GONE);

                    break;


                case REQUEST_ERROR:
                    pb_load.setVisibility(View.GONE);
                    rl_content.setVisibility(View.GONE);
                    ll_error.setVisibility(View.VISIBLE);
                    break;
            }

        }
    };

    private void getRequestGif() {
        handler.sendEmptyMessage(REQUEST_LOAD);
        Map<String, String> parms = new HashMap<>();
        parms.put("thirdId", mThirdId);
        parms.put("matchType", match_type);  //足球1 篮球2

        VolleyContentFast.requestJsonByGet(BaseURLs.FOOTBALL_DETAIL_COLLECTION, parms, new VolleyContentFast.ResponseSuccessListener<VideoHighLights>() {
            @Override
            public void onResponse(VideoHighLights jsonObject) {
                if (jsonObject.getResult() == 200) {
                    if (jsonObject.getData() != null && jsonObject.getData().size() > 0) {
                        mList = jsonObject.getData();
                        initializeData();
                    }
                } else {
                    handler.sendEmptyMessage(REQUEST_ERROR);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                handler.sendEmptyMessage(REQUEST_ERROR);
            }
        }, VideoHighLights.class);
    }

    private void initializeData() {
        for (int i = 0; i < mList.size(); i++) {
            ViewGroup viewGroup = (ViewGroup) View.inflate(this, R.layout.page_layout, null);
            VideoView mVideoView = (VideoView) viewGroup.findViewById(R.id.mVideoView);
            PhotoView mImageView = (PhotoView) viewGroup.findViewById(R.id.pv_show_image);
            ImageView mView = (ImageView) viewGroup.findViewById(R.id.mView);
            // resourceType==0 是图片资源
            if (GIF_TYPE.equals(mList.get(i).getResourceType())) {
                mVideoView.setVisibility(View.GONE);
                mView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                mViewList.add(viewGroup);
                mVideoViewList.add(mImageView);
            } else {
                mImageView.setVisibility(View.GONE);
                mVideoView.setVisibility(View.VISIBLE);
                mView.setVisibility(View.VISIBLE);
                if (mList.get(i).getImageUrl() != null) {   //视频图片
                    Glide.with(getApplicationContext()).load(mList.get(i).getImageUrl()).into(mView);
                }
                mVideoView.setVideoURI(Uri.parse(mList.get(i).getResourceUrl()));
                tv_text.setText(mList.get(i).getDescription());
                setListener(mVideoView);
                mViewList.add(viewGroup);
                mVideoViewList.add(mVideoView);
            }
            mCacheViewList.add(mView);
        }
        mAdapter = new MyPagerAdapter(this, mViewList);
        mViewPager.setAdapter(mAdapter);

        if (mList.size() > 1) {
            tvImageIndex.setVisibility(View.VISIBLE);
            tvImageIndex.setText((0 + 1) + "/" + mList.size());
        } else {
            tvImageIndex.setVisibility(View.GONE);
        }


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pageIndex) {

                lastItem = currentItem;
                currentItem = pageIndex;

                int index = pageIndex % mList.size();
                tvImageIndex.setText((index + 1) + "/" + mList.size());
                tv_text.setText(mList.get(currentItem).getDescription());

                if ((mVideoViewList.get(lastItem)) instanceof VideoView) {  //如果上一个是video
                    if ((mVideoViewList.get(lastItem)) != null) {
                        if (((VideoView) mVideoViewList.get(lastItem)).isPlaying()) {   //如果是true
                            mList.get(lastItem).setmIsPlaying(true);
                            // 记录播放状态
                            ((VideoView) (mVideoViewList.get(lastItem))).pause();
                        } else {
                            // 记录播放状态*ess5
                            mList.get(lastItem).setmIsPlaying(false);
                        }
                        // 记录播放进度
                        mList.get(lastItem).setmCurrentPositions(((VideoView) mVideoViewList.get(lastItem)).getCurrentPosition());
                    }
                }

                if (mVideoViewList.get(currentItem) instanceof VideoView) {
                    if (mVideoViewList.get(currentItem) != null) {
                        ((VideoView) mVideoViewList.get(currentItem)).seekTo(mList.get(currentItem).getmCurrentPositions());
                        if (mList.get(currentItem).ismIsPlaying()) {
                            ((VideoView) mVideoViewList.get(currentItem)).start();
                        }
                    }
                } else {
                    PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher((PhotoView) mVideoViewList.get(currentItem));
                    photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    photoViewAttacher.setMinimumScale(1F);
                    ImageLoaderUtils.displayScaleImage(getApplicationContext(), (PhotoView) mVideoViewList.get(currentItem), mList.get(currentItem).getResourceUrl(), photoViewAttacher);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        if (GIF_TYPE.equals(mList.get(0).getResourceType())) {
            PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher((PhotoView) mVideoViewList.get(0));
            photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
            photoViewAttacher.setMinimumScale(1F);
            ImageLoaderUtils.displayScaleImage(getApplicationContext(), (PhotoView) mVideoViewList.get(0), mList.get(0).getResourceUrl(), photoViewAttacher);
        } else {
            if (mVideoViewList.get(0) != null) {
                ((VideoView) mVideoViewList.get(0)).start();
                mList.get(0).setmIsPlaying(true);
            }
        }

        tv_text.setText(mList.get(currentItem).getDescription());
        handler.sendEmptyMessage(REQUEST_SUCESS);
    }


    private void setListener(VideoView videoView) {
        videoView.setOnInfoListener(this);
        videoView.setOnCompletionListener(this);
        videoView.setOnErrorListener(this);
        videoView.setOnPreparedListener(this);
    }

    class MyPagerAdapter extends android.support.v4.view.PagerAdapter {
        Context context;
        List<ViewGroup> mViewList;

        public MyPagerAdapter(Context context, List<ViewGroup> list) {
            this.context = context;
            this.mViewList = list;
        }

        @Override
        public int getCount() {
            return mViewList != null ? mViewList.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (View) arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }


    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mVideoViewList.get(currentItem) != null) {
            if (mVideoViewList.get(currentItem) instanceof VideoView) {
                mList.get(currentItem).setmCurrentPositions(0);
                mList.get(currentItem).setmIsPlaying(true);
                ((VideoView) mVideoViewList.get(currentItem)).resume();
                ((VideoView) mVideoViewList.get(currentItem)).start();
            }
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            if (mCacheViewList.get(currentItem) != null) {
                if (mVideoViewList.get(currentItem) instanceof VideoView) {
                    mCacheViewList.get(currentItem).setVisibility(View.VISIBLE);
                }
            }
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            // 此接口每次回调完START就回调END,若不加上判断就会出现缓冲图标一闪一闪的卡顿现象
            if (mp.isPlaying()) {
                if (mCacheViewList.get(currentItem) != null) {
                    if (mVideoViewList.get(currentItem) instanceof VideoView) {
                        mCacheViewList.get(currentItem).setVisibility(View.GONE);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // 这里设置为true防止弹出对话框，屏蔽原始出错的处理
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mCacheViewList.get(currentItem) != null) {
            if (mVideoViewList.get(currentItem) instanceof VideoView) {
                mCacheViewList.get(currentItem).setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (mVideoViewList.size() > 0) {
            if ((mVideoViewList.get(currentItem)) != null) {
                if (mVideoViewList.get(currentItem) instanceof VideoView) {
                    if (((VideoView) mVideoViewList.get(currentItem)).isPlaying()) {
                        mList.get(currentItem).setmIsPlaying(true);
                        ((VideoView) mVideoViewList.get(currentItem)).pause();
                    } else {
                        mList.get(currentItem).setmIsPlaying(false);
                    }
                    mList.get(currentItem).setmCurrentPositions(((VideoView) mVideoViewList.get(currentItem)).getCurrentPosition());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoViewList.size() > 0) {
            if (mVideoViewList.get(currentItem) != null) {
                if (mVideoViewList.get(currentItem) instanceof VideoView) {
                    ((VideoView) mVideoViewList.get(currentItem)).stopPlayback();
                    ((VideoView) mVideoViewList.get(currentItem)).suspend();
                }
            }
        }
        clearList();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        L.d("wangg", "onResume");
        if (mVideoViewList.size() > 0) {
            if (mVideoViewList.get(currentItem) != null) {
                if (mVideoViewList.get(currentItem) instanceof VideoView) {
                    ((VideoView) mVideoViewList.get(currentItem)).seekTo(mList.get(currentItem).getmCurrentPositions());
                    if (mList.get(currentItem).ismIsPlaying()) {
                        ((VideoView) mVideoViewList.get(currentItem)).start();
                    }
                }
            }
        }
    }

    /**
     * 清空static数据
     */
    private void clearList() {
        mVideoViewList.clear();
        mCacheViewList.clear();
        mList.clear();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
