package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;

/**
 * 描述:  足球内页动画直播
 * 作者:  wangg@13322.com
 * 时间:  2016/8/8 10:35
 */
public class AnimHeadLiveFragment extends Fragment {

    private static String TAG = "anim";


    //事件pic坐标

    private static float[] homeGoalPic = {20, 50};
    private static float[] guestGoalPic = {20, 50};
    private static float[] homeCornerPpic = {20, 50};
    private static float[] guestCornerPpic = {20, 50};


    private static float ViewPagerHeight = 200;  //dp
    private static float picWidth = 566;
    private static float picHeight = 700;

    private float scaleFactorWidth; //比例因子宽
    private float scaleFactorHeight; //比例因子高
    private float screenWidth; //屏幕宽度px
    private float layoutHeight; //布局高度px
    private float x_gif; //动画坐标位置
    private float y_gif;
    // private float[] realXY = new float[2];


    private View mView;
    private Context mContext;

    public static AnimHeadLiveFragment newInstance() {
        AnimHeadLiveFragment animHeadLiveFragment = new AnimHeadLiveFragment();
        return animHeadLiveFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_anim_head_live, container, false);
        if (getActivity() != null) {
            mContext = getActivity();
        }

        initData();
        return mView;
    }


    private void initData() {

        layoutHeight = DisplayUtil.dip2px(mContext, ViewPagerHeight);
        if (getActivity() != null) {
            DisplayMetrics metric = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
            screenWidth = metric.widthPixels;     // 屏幕宽度（像素）

            scaleFactorWidth = screenWidth / picWidth;
            scaleFactorHeight = layoutHeight / picHeight;

            L.d(TAG, "screenWidth=" + screenWidth);
            L.d(TAG, "scaleFactorWidth=" + scaleFactorWidth);
            L.d(TAG, "scaleFactorHeight=" + scaleFactorHeight);

        }
    }

    /***
     * 实际坐标
     * 事件实际x坐标 = 图片事件 宽度 pic_x 坐标 * scaleFactorWidth - 图片实际宽度*0.5
     * 事件实际y坐标 = 图片事件 高度 pic_y 坐标 * scaleFactorHeight +图片实际高度*0.5
     */
    public void setAnimLive(String code) {
        switch (code) {
            case "":

                break;
            default:
                break;
        }


        //显示gif事件


    }
}
