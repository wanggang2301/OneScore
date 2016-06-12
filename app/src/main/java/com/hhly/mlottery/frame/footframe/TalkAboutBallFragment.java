package com.hhly.mlottery.frame.footframe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.MatchLike;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.ChatFragment;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hhly204
 * @ClassName: TalkAboutBallFragment
 * @Description: 聊球页面
 * @date 2016-6-2
 */
public class TalkAboutBallFragment extends Fragment {
    private View mView;
    private ProgressBar talkballpro;//客队Vs主队点赞的比例
    private ImageView ivHomeLike;//点赞主队
    private ImageView ivGuestLike;//点赞客队
    private TextView tvGuestLikeCount;//点赞客队数
    private TextView tvHomeLikeCount;//点赞主队数
    private String url;// 要显示的H5网址
    private String title;// 标题
    /**
     * 主队点赞
     */
    private ImageView mHomeLike;
    private ImageView mGuestLike;
    /**
     * 点赞动画
     */
    private AnimationSet mRiseHomeAnim;
    private AnimationSet mRiseGuestAnim;
    /**
     * 赛事id
     */
    public String mThirdId;
    public final static String BUNDLE_PARAM_THIRDID = "thirdId";
    //    private String mHomeName;
//    private String mGustName;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ADDKEYHOME = "homeAdd";
    private static final String ADDKEYGUEST = "guestAdd";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        L.d(TAG, "______________onCreate");
        if (getArguments() != null) {
            mThirdId = getArguments().getString(ARG_PARAM1);
//            mHomeName = getArguments().getString(ARG_PARAM2);
//            mGustName = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_talkaboutballfragment, null);
        initView();
        requestData(ADDKEYHOME, "0");
        initAnim();
        addFragment();
        return mView;
    }

    public void addFragment() {
        ChatFragment fragment = new ChatFragment();
        fragment.setOnPullDownRefreshLisener(new ChatFragment.onPullDownRefreshLisener() {
            @Override
            public void onPullDownRefresh() {
                requestData(ADDKEYHOME, "0");
            }
        });
        CyUtils.addComment(fragment, mThirdId, mThirdId, false, true, getChildFragmentManager(), R.id.talkball_chat);
    }

    /** bn
     * 初始化动画
     */
    private void initAnim() {
        mRiseHomeAnim = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, 0f, -50f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        mRiseHomeAnim.addAnimation(translateAnimation);
        mRiseHomeAnim.addAnimation(alphaAnimation);
        mRiseHomeAnim.setDuration(300);
        mRiseHomeAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivHomeLike.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mRiseGuestAnim = new AnimationSet(true);
        mRiseGuestAnim.addAnimation(translateAnimation);
        mRiseGuestAnim.addAnimation(alphaAnimation);
        mRiseGuestAnim.setDuration(300);
        mRiseGuestAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivGuestLike.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    //请求点赞数据
    public void requestData(String addkey, String addcount) {
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", mThirdId);
        params.put(addkey, addcount);
        VolleyContentFast.requestJsonByPost(BaseURLs.URL_FOOTBALL_DETAIL_LIKE_INFO, params, new VolleyContentFast.ResponseSuccessListener<MatchLike>() {
            @Override
            public void onResponse(MatchLike matchLike) {
                tvHomeLikeCount.setText(matchLike.getHomeLike());
                tvGuestLikeCount.setText(matchLike.getGuestLike());
                int homeLikeCount = Integer.parseInt(matchLike.getHomeLike());
                int guestLikeCount = Integer.parseInt(matchLike.getGuestLike());
                talkballpro.setMax(homeLikeCount + guestLikeCount);
                talkballpro.setProgress(homeLikeCount);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        }, MatchLike.class);
    }

    private void initView() {
        talkballpro = (ProgressBar) mView.findViewById(R.id.talkball_pro);
        ivHomeLike = (ImageView) mView.findViewById(R.id.talkball_like_anim_img);
        ivGuestLike = (ImageView) mView.findViewById(R.id.talkbail_guest_like_anim_img);
        tvHomeLikeCount = (TextView) mView.findViewById(R.id.talkball_like_count);
        tvGuestLikeCount = (TextView) mView.findViewById(R.id.talkbail_guest_like_count);
        mHomeLike = (ImageView) mView.findViewById(R.id.talkball_home_like);
        mGuestLike = (ImageView) mView.findViewById(R.id.talkball_guest_like);
        ivHomeLike.setVisibility(View.INVISIBLE);
        ivGuestLike.setVisibility(View.INVISIBLE);
        mHomeLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivHomeLike.setVisibility(View.VISIBLE);
                ivHomeLike.startAnimation(mRiseHomeAnim);
//                //String url = "http://192.168.10.242:8181/mlottery/core/footBallMatch.updLike.do";
                requestData(ADDKEYHOME, "1");
            }
        });

        mGuestLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivGuestLike.setVisibility(View.VISIBLE);
                ivGuestLike.startAnimation(mRiseGuestAnim);
                //String url = "http://192.168.10.242:8181/mlottery/core/footBallMatch.updLike.do";
                requestData(ADDKEYGUEST, "1");
            }
        });
        setClickableLikeBtn(true);
    }

    public void setClickableLikeBtn(boolean clickable) {
        mHomeLike.setClickable(clickable);
        mGuestLike.setClickable(clickable);
        if (clickable) {
            mHomeLike.setImageResource(R.mipmap.like_red);
            mGuestLike.setImageResource(R.mipmap.like_blue);
        } else {
            mHomeLike.setImageResource(R.mipmap.like_anim_left);
            mGuestLike.setImageResource(R.mipmap.like_anim_right);
        }
    }
}
