package com.hhly.mlottery.frame.footframe;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.MatchDetail;
import com.hhly.mlottery.util.StringUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

/**
 * @author wang gang
 * @date 2016/6/3 9:53
 * @des ${TODO}
 */
public class PreHeadInfoFrament extends Fragment {

    private View mView;

    public static final int PREHEADINFO_TYPE_PRE = 0x01;//赛前
    public static final int PREHEADINFO_TYPE_ING = 0x10;//赛中赛后
    private static final String PREHEADINFO_PARAM = "PREHEADINFO_PARAM";
    private static final String PREHEADINFO_TYPE = "PREHEADINFO_TYPE";

    private MatchDetail mMatchDetail;
    private int mViewType = 0;

    private ImageView iv_home_icon;
    private ImageView iv_guest_icon;

    private TextView tv_homename;
    private TextView tv_guestname;

    private TextView racename;

    private TextView score;

    private TextView date;

    private RelativeLayout mMatchTypeLayout;

    private int mType = 0;

    private TextView mMatchType1;

    private TextView mMatchType2;

    public static PreHeadInfoFrament newInstance() {
        PreHeadInfoFrament fragment = new PreHeadInfoFrament();
      /*  Bundle args = new Bundle();
        args.putParcelable(PREHEADINFO_PARAM, matchDetail);
        args.putInt(PREHEADINFO_TYPE, mType);

        fragment.setArguments(args);*/
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_pre_headinfo, container, false);
        if (getArguments() != null) {
            mMatchDetail = getArguments().getParcelable(PREHEADINFO_PARAM);
            mType = getArguments().getInt(PREHEADINFO_TYPE);
        }

        initView();

        return mView;
    }


    private void initView() {
        iv_home_icon = (ImageView) mView.findViewById(R.id.iv_home_icon);
        iv_guest_icon = (ImageView) mView.findViewById(R.id.iv_guest_icon);
        tv_homename = (TextView) mView.findViewById(R.id.tv_home_name);
        tv_guestname = (TextView) mView.findViewById(R.id.tv_guest_name);
        racename = (TextView) mView.findViewById(R.id.race_name);
        score = (TextView) mView.findViewById(R.id.score);
        date = (TextView) mView.findViewById(R.id.date);
        mMatchTypeLayout = (RelativeLayout) mView.findViewById(R.id.football_match_detail_matchtype_layout);
        mMatchType1 = (TextView) mView.findViewById(R.id.football_match_detail_matchtype1);
        mMatchType2 = (TextView) mView.findViewById(R.id.football_match_detail_matchtype2);
    }

    public void initData(MatchDetail mMatchDetail) {
        loadImage(mMatchDetail.getHomeTeamInfo().getUrl(), iv_home_icon);
        loadImage(mMatchDetail.getGuestTeamInfo().getUrl(), iv_guest_icon);
        tv_homename.setText(mMatchDetail.getHomeTeamInfo().getName());
        tv_guestname.setText(mMatchDetail.getGuestTeamInfo().getName());


        //赛事类型

        if (mMatchDetail.getMatchType1() == null && mMatchDetail.getMatchType2() == null) {
            mMatchTypeLayout.setVisibility(View.GONE);
            racename.setVisibility(View.VISIBLE);
        } else {

            if (StringUtils.isEmpty(mMatchDetail.getMatchType1())) {
                mMatchType1.setVisibility(View.GONE);
            }

            if (StringUtils.isEmpty(mMatchDetail.getMatchType2())) {
                mMatchType2.setVisibility(View.GONE);
            }
            mMatchType1.setText(StringUtils.nullStrToEmpty(mMatchDetail.getMatchType1()));
            mMatchType2.setText(StringUtils.nullStrToEmpty(mMatchDetail.getMatchType2()));
            mMatchTypeLayout.setVisibility(View.VISIBLE);
            racename.setVisibility(View.GONE);
        }

        String startTime = mMatchDetail.getMatchInfo().getStartTime();
        if (date == null) {
            return;
        }
        if (!StringUtils.isEmpty(startTime) && startTime.length() == 16) {
            date.setText(startTime);
        } else {
            date.setText("");//开赛时间
        }
    }


    //加载图片
    private void loadImage(String imageUrl, final ImageView imageView) {
        VolleyContentFast.requestImage(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    public void setScoreText(String msg) {
        score.setText(msg);
    }

    public void setScoreClolor(int id) {
        score.setTextColor(id);
    }
}
