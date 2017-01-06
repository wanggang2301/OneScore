package com.hhly.mlottery.frame.basketballframe;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.PlayHighLightActivity;
import com.hhly.mlottery.bean.basket.BasketballDetailsBean;
import com.hhly.mlottery.bean.websocket.DataEntity;
import com.hhly.mlottery.bean.websocket.WebSocketBasketBallDetails;
import com.hhly.mlottery.frame.chartBallFragment.ChartBallFragment;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.NetworkUtils;
import com.hhly.mlottery.util.PreferenceUtil;

/**
 * Created by Administrator on 2016/10/12.
 */

public class BasketDetailsHeadFragment extends Fragment {

    private View mView;
    private ImageView mHeadImage;
    private ImageView mHomeIcon;
    private ImageView mGuestIcon;
    private TextView mHomeTeam;
    private TextView mGuestTeam;
    private TextView mHomeRanking;
    private TextView mGuestRanking;
    private TextView mLeagueName;
    private TextView mVS;
    private TextView mHomeScore;
    private TextView mGuestScore;
    private TextView mMatchState;
    private TextView mRemainTime;
    private TextView mApos;
    private TextView mGuest1;
    private TextView mGuest2;
    private TextView mGuest3;
    private TextView mGuest4;
    private TextView mHome1;
    private TextView mHome2;
    private TextView mHome3;
    private TextView mHome4;
    private LinearLayout mLayoutOt1;
    private LinearLayout mLayoutOt2;
    private LinearLayout mLayoutOt3;
    private TextView mGuestOt1;
    private TextView mGuestOt2;
    private TextView mGuestOt3;
    private TextView mHomeOt1;
    private TextView mHomeOt2;
    private TextView mHomeOt3;
    private TextView mSmallGuestScore;
    private TextView mSmallHomeScore;
    private Context mContext;
    private Activity mActivity;

    private LinearLayout btn_showGif;
    private final static String MATCH_TYPE = "2"; //篮球

    private View red_point;

    private final static String BASKETBALL_GIF = "basketball_gif";

    public static BasketDetailsHeadFragment newInstance() {

        Bundle args = new Bundle();

        BasketDetailsHeadFragment fragment = new BasketDetailsHeadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mContext = mActivity;

        mView = inflater.inflate(R.layout.layout_basket_header, container, false);


        initView();

        initGifData();
        return mView;
    }

    private void initView() {

        mHeadImage = (ImageView) mView.findViewById(R.id.image_background);

        mHomeIcon = (ImageView) mView.findViewById(R.id.basket_details_home_icon);
        mGuestIcon = (ImageView) mView.findViewById(R.id.basket_details_guest_icon);

        mHomeTeam = (TextView) mView.findViewById(R.id.basket_details_home_name);
        mGuestTeam = (TextView) mView.findViewById(R.id.basket_details_guest_name);

        mHomeRanking = (TextView) mView.findViewById(R.id.basket_details_home_Ranking);
        mGuestRanking = (TextView) mView.findViewById(R.id.basket_details_guest_Ranking);

        mLeagueName = (TextView) mView.findViewById(R.id.basket_details_matches_name);
        mVS = (TextView) mView.findViewById(R.id.basket_score_maohao);

        mHomeScore = (TextView) mView.findViewById(R.id.basket_details_home_all_score);
        mGuestScore = (TextView) mView.findViewById(R.id.basket_details_guest_all_score);

        mMatchState = (TextView) mView.findViewById(R.id.basket_details_state);
        mRemainTime = (TextView) mView.findViewById(R.id.basket_details_remain_time);

        mApos = (TextView) mView.findViewById(R.id.backetball_details_apos);
        mApos.setVisibility(View.GONE);

        mGuest1 = (TextView) mView.findViewById(R.id.basket_details_guest_first);
        mGuest2 = (TextView) mView.findViewById(R.id.basket_details_guest_second);
        mGuest3 = (TextView) mView.findViewById(R.id.basket_details_guest_third);
        mGuest4 = (TextView) mView.findViewById(R.id.basket_details_guest_fourth);

        mHome1 = (TextView) mView.findViewById(R.id.basket_details_home_first);
        mHome2 = (TextView) mView.findViewById(R.id.basket_details_home_second);
        mHome3 = (TextView) mView.findViewById(R.id.basket_details_home_third);
        mHome4 = (TextView) mView.findViewById(R.id.basket_details_home_fourth);

        mLayoutOt1 = (LinearLayout) mView.findViewById(R.id.basket_details_llot1);
        mLayoutOt2 = (LinearLayout) mView.findViewById(R.id.basket_details_llot2);
        mLayoutOt3 = (LinearLayout) mView.findViewById(R.id.basket_details_llot3);

        mGuestOt1 = (TextView) mView.findViewById(R.id.basket_details_guest_ot1);
        mGuestOt2 = (TextView) mView.findViewById(R.id.basket_details_guest_ot2);
        mGuestOt3 = (TextView) mView.findViewById(R.id.basket_details_guest_ot3);

        mHomeOt1 = (TextView) mView.findViewById(R.id.basket_details_home_ot1);
        mHomeOt2 = (TextView) mView.findViewById(R.id.basket_details_home_ot2);
        mHomeOt3 = (TextView) mView.findViewById(R.id.basket_details_home_ot3);

        mSmallGuestScore = (TextView) mView.findViewById(R.id.basket_details_guest_small_total);
        mSmallHomeScore = (TextView) mView.findViewById(R.id.basket_details_home_small_total);

        btn_showGif = (LinearLayout) mView.findViewById(R.id.btn_showGif);

        red_point = (View) mView.findViewById(R.id.red_point);
    }


    private void initGifData() {
        //getCollectionCount();

        btn_showGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isConnected(getActivity())) {
                    int type = com.hhly.mlottery.util.NetworkUtils.getCurNetworkType(getActivity());
                    if (type == 1) {

                        hideGifRedPoint();

                        L.d("zxcvbn", "WIFI");
                        Intent intent = new Intent(getActivity(), PlayHighLightActivity.class);
                        intent.putExtra("thirdId", BasketDetailsActivityTest.mThirdId);
                        intent.putExtra("match_type", MATCH_TYPE);

                        startActivity(intent);
                        //wifi
                    } else if (type == 2 || type == 3 || type == 4) {//2G  3G  4G
                        L.d("zxcvbn", "移动网络-" + type + "G");
                        promptNetInfo();
                    }
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.about_net_failed), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    /**
     * 影藏红点
     */

    private void hideGifRedPoint() {
        PreferenceUtil.commitBoolean(BASKETBALL_GIF, false);
        red_point.setVisibility(View.GONE);
    }

    public void setBtn_showGifVisible(int visible) {
        btn_showGif.setVisibility(visible);
    }

    public void setRedPointVisible(int visible) {
        red_point.setVisibility(visible);
    }

    /**
     * 当前连接的网络提示
     */
    private void promptNetInfo() {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AppThemeDialog);
            builder.setCancelable(false);// 设置对话框以外不可点击
            builder.setTitle(getActivity().getResources().getString(R.string.to_update_kindly_reminder));// 提示标题
            builder.setMessage(getActivity().getResources().getString(R.string.video_high_light_reminder_comment));// 提示内容
            builder.setPositiveButton(getActivity().getResources().getString(R.string.video_high_light_continue_open), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    hideGifRedPoint();
                    dialog.dismiss();
                    Intent intent = new Intent(getActivity(), PlayHighLightActivity.class);
                    intent.putExtra("thirdId", BasketDetailsActivityTest.mThirdId);
                    intent.putExtra("match_type", MATCH_TYPE);

                    startActivity(intent);
                }
            });
            builder.setNegativeButton(getActivity().getResources().getString(R.string.basket_analyze_dialog_cancle), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            android.support.v7.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }



    /**
     * 请求数据之后展示
     */

//    private DisplayImageOptions mOptions;
//    private DisplayImageOptions mOptionsHead;
//    private ImageLoader mImageLoader;

    private final static int PRE_MATCH = 0;//赛前
    private final static int FIRST_QUARTER = 1;
    private final static int SECOND_QUARTER = 2;
    private final static int THIRD_QUARTER = 3;
    private final static int FOURTH_QUARTER = 4;
    private final static int OT1 = 5;

    private final static int OT2 = 6;
    private final static int OT3 = 7;
    private final static int END = -1;
    private final static int DETERMINED = -2;//待定
    private final static int GAME_CUT = -3;
    private final static int GAME_CANCLE = -4;
    private final static int GAME_DELAY = -5;
    private final static int HALF_GAME = 50;

    private int mGuestNum = 0;
    private int mHomeNum = 0;
    BasketballDetailsBean.MatchEntity mMatch;

    public void initData(BasketballDetailsBean bean, ChartBallFragment mChartBallFragment) {

        /**
         * 启动秒闪烁
         */
        setApos();


        BasketballDetailsBean.MatchEntity.MatchScoreEntity score = bean.getMatch().getMatchScore();//比分
        mMatch = bean.getMatch();


        if (score != null) {
            mGuestNum = score.getGuestScore();
            mHomeNum = score.getHomeScore();
        }

        //联赛名
        mLeagueName.setText(mMatch.getLeagueName());
        mLeagueName.setTextColor(Color.parseColor(mMatch.getLeagueColor()));

        mHomeTeam.setText(mMatch.getHomeTeam());
        mGuestTeam.setText(mMatch.getGuestTeam());
        if (mMatch.getHomeRanking().equals("")) {
            mHomeRanking.setText("");
        } else {
            mHomeRanking.setText("[ " + mMatch.getHomeRanking() + " ]");
        }
        if (mMatch.getGuestRanking().equals("")) {
            mGuestRanking.setText("");
        } else {
            mGuestRanking.setText("[ " + mMatch.getGuestRanking() + " ]");
        }


        //图标
        if (mContext != null) {
            ImageLoader.load(mContext, mMatch.getHomeLogoUrl(), R.mipmap.basket_default).into(mHomeIcon);
//    mImageLoader.displayImage(mMatch.getHomeLogoUrl(), mHomeIcon, mOptions);

            ImageLoader.load(mContext, mMatch.getGuestLogoUrl(), R.mipmap.basket_default).into(mGuestIcon);
//        mImageLoader.displayImage(mMatch.getGuestLogoUrl(), mGuestIcon, mOptions);

            ImageLoader.load(mContext, bean.getBgUrl(), R.color.black).into(mHeadImage);
//        mImageLoader.displayImage(bean.getBgUrl(), mHeadImage, mOptionsHead);
        }


        if (mMatch.getSection() == 2) { //只有上下半场
            mGuest2.setVisibility(View.INVISIBLE);
            mGuest4.setVisibility(View.INVISIBLE);
            mHome2.setVisibility(View.INVISIBLE);
            mHome4.setVisibility(View.INVISIBLE);
        }

        if (mContext != null) {

            switch (mMatch.getMatchStatus()) {
                case PRE_MATCH: ///赛前
                case DETERMINED://待定
                case GAME_CANCLE: //比赛取消
                case GAME_CUT: //比赛中断
                case GAME_DELAY: //比赛推迟
                    //赛前显示 客队 VS  主队
                    mGuestScore.setText("");
                    mHomeScore.setText("");

                    mVS.setText("VS");
                    if (mMatch.getMatchStatus() == PRE_MATCH) {
                        mMatchState.setText(bean.getMatch().getDate() + "  " + bean.getMatch().getTime() + "   " + MyApp.getContext().getResources().getString(R.string.basket_begin_game));
                    } else if (mMatch.getMatchStatus() == DETERMINED) {
                        mMatchState.setText(R.string.basket_undetermined);
                    } else if (mMatch.getMatchStatus() == GAME_CANCLE) {
                        mMatchState.setText(R.string.basket_cancel);
                    } else if (mMatch.getMatchStatus() == GAME_CUT) {
                        mMatchState.setText(R.string.basket_interrupt);
                    } else {
                        mMatchState.setText(R.string.basket_postpone);
                    }
                    mApos.setVisibility(View.GONE);
                    mRemainTime.setText("");
                    if (mMatch.getMatchStatus() == PRE_MATCH) {
                        mChartBallFragment.setClickableLikeBtn(true);
                    }
                    break;
                case END://完场
                    mChartBallFragment.setClickableLikeBtn(false);

                    mGuestScore.setText(score.getGuestScore() + "");
                    mHomeScore.setText(score.getHomeScore() + "");
                    mMatchState.setText(R.string.finished_txt);
                    mGuest1.setText(score.getGuest1() + "");
                    mGuest2.setText(score.getGuest2() + "");
                    mGuest3.setText(score.getGuest3() + "");
                    mGuest4.setText(score.getGuest4() + "");
                    mHome1.setText(score.getHome1() + "");
                    mHome2.setText(score.getHome2() + "");
                    mHome3.setText(score.getHome3() + "");
                    mHome4.setText(score.getHome4() + "");


                    mSmallGuestScore.setText(score.getGuestScore() + "");
                    mSmallHomeScore.setText(score.getHomeScore() + "");
                    mVS.setText(":");
                    if (score.getAddTime() == 3) {//三个加时
                        mLayoutOt3.setVisibility(View.VISIBLE);
                        mLayoutOt2.setVisibility(View.VISIBLE);
                        mLayoutOt1.setVisibility(View.VISIBLE);
                        mGuestOt1.setText(score.getGuestOt1() + "");
                        mHomeOt1.setText(score.getHomeOt1() + "");
                        mGuestOt2.setText(score.getGuestOt2() + "");
                        mHomeOt2.setText(score.getHomeOt2() + "");
                        mGuestOt3.setText(score.getGuestOt3() + "");
                        mHomeOt3.setText(score.getHomeOt3() + "");
                    } else if (score.getAddTime() == 2) {
                        mLayoutOt2.setVisibility(View.VISIBLE);
                        mLayoutOt1.setVisibility(View.VISIBLE);
                        mGuestOt1.setText(score.getGuestOt1() + "");
                        mHomeOt1.setText(score.getHomeOt1() + "");
                        mGuestOt2.setText(score.getGuestOt2() + "");
                        mHomeOt2.setText(score.getHomeOt2() + "");
                    } else if (score.getAddTime() == 1) {
                        mLayoutOt1.setVisibility(View.VISIBLE);
                        mGuestOt1.setText(score.getGuestOt1() + "");
                        mHomeOt1.setText(score.getHomeOt1() + "");
                    }
                    mApos.setVisibility(View.GONE);
                    mRemainTime.setText("");
                    break;
                case OT3:
                    mLayoutOt3.setVisibility(View.VISIBLE);
                    setScore(score.getGuestOt3(), mGuestOt3, score.getHomeOt3(), mHomeOt3);
                case OT2:
                    mLayoutOt2.setVisibility(View.VISIBLE);
                    setScore(score.getGuestOt2(), mGuestOt2, score.getHomeOt2(), mHomeOt2);

                case OT1:
                    mLayoutOt1.setVisibility(View.VISIBLE);
                    setScore(score.getGuestOt1(), mGuestOt1, score.getHomeOt1(), mHomeOt1);

                case FOURTH_QUARTER:
                    setScore(score.getGuest4(), mGuest4, score.getHome4(), mHome4);

                case THIRD_QUARTER:
                    setScore(score.getGuest3(), mGuest3, score.getHome3(), mHome3);

                case HALF_GAME: //中场
                case SECOND_QUARTER:
                    setScore(score.getGuest2(), mGuest2, score.getHome2(), mHome2);
                case FIRST_QUARTER:
                    setScore(score.getGuest1(), mGuest1, score.getHome1(), mHome1);
                    //不管是第几节都设置总比分,设置剩余时间
                    setScore(score.getGuestScore(), mGuestScore, score.getHomeScore(), mHomeScore);
                    setScore(score.getGuestScore(), mSmallGuestScore, score.getHomeScore(), mSmallHomeScore);
                    mVS.setText(":");

                    mChartBallFragment.setClickableLikeBtn(true); //聊球可点赞

                    //设置比赛时间及状态
                    if (mMatch.getMatchStatus() == FIRST_QUARTER) {
                        if (mMatch.getSection() == 2) {
                            mMatchState.setText("1st half  ");
                        } else {
                            mMatchState.setText("1st  ");
                        }
                        mApos.setVisibility(View.VISIBLE);
                    } else if (mMatch.getMatchStatus() == SECOND_QUARTER) {
                        if (mMatch.getSection() == 2) {
                            mMatchState.setText("1st half  ");
                        } else {
                            mMatchState.setText("2nd  ");
                        }
                        mApos.setVisibility(View.VISIBLE);
                    } else if (mMatch.getMatchStatus() == HALF_GAME) {
                        mMatchState.setText("half time  ");
                        mApos.setVisibility(View.GONE);
                    } else if (mMatch.getMatchStatus() == THIRD_QUARTER) {
                        if (mMatch.getSection() == 2) {
                            mMatchState.setText("2nd half");
                        } else {
                            mMatchState.setText("3rd  ");
                        }
                        mApos.setVisibility(View.VISIBLE);
                    } else if (mMatch.getMatchStatus() == FOURTH_QUARTER) {
                        if (mMatch.getSection() == 2) {
                            mMatchState.setText("2nd half  ");
                        } else {
                            mMatchState.setText("4th  ");
                        }
                        mApos.setVisibility(View.VISIBLE);
                    } else if (mMatch.getMatchStatus() == OT1) {
                        mMatchState.setText("OT1  ");
                        mApos.setVisibility(View.VISIBLE);
                    } else if (mMatch.getMatchStatus() == OT2) {
                        mMatchState.setText("OT2  ");
                        mApos.setVisibility(View.VISIBLE);
                    } else {
                        mMatchState.setText("OT3  ");
                        mApos.setVisibility(View.VISIBLE);
                    }

                    mRemainTime.setText(score.getRemainTime());//剩余时间
                    if (mMatch.getMatchStatus() == HALF_GAME) {
                        mRemainTime.setText("");//中场时无剩余时间。。后台可能中场也给时间。没办法
                    }
                    if (score.getRemainTime() == null || score.getRemainTime().equals("")) {//没有剩余时间的时候
                        mApos.setVisibility(View.GONE);
                    }
                    break;
            }
        }

    }

    /**
     * 设置比分
     *
     * @param guestScore 客队得分
     * @param guest      客队显示比分的textview
     * @param homeScore  主队比分
     * @param home       主队显示比分的textview
     */
    private void setScore(int guestScore, TextView guest, int homeScore, TextView home) {

        guest.setText(guestScore + "");
        home.setText(homeScore + "");
        if (guestScore > homeScore) {//得分少的用灰色
            guest.setTextColor(MyApp.getContext().getResources().getColor(R.color.basket_score_white));
            home.setTextColor(MyApp.getContext().getResources().getColor(R.color.basket_score_gray));
        } else if (guestScore < homeScore) {
            guest.setTextColor(MyApp.getContext().getResources().getColor(R.color.basket_score_gray));
            home.setTextColor(MyApp.getContext().getResources().getColor(R.color.basket_score_white));
        } else {
            guest.setTextColor(MyApp.getContext().getResources().getColor(R.color.basket_score_white));
            home.setTextColor(MyApp.getContext().getResources().getColor(R.color.basket_score_white));
        }


    }

    /**
     * 秒闪烁
     */
    private void setApos() {
        mApos.setText("\'");

        final AlphaAnimation anim1 = new AlphaAnimation(1, 1);
        anim1.setDuration(500);
        final AlphaAnimation anim2 = new AlphaAnimation(0, 0);
        anim2.setDuration(500);
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mApos.startAnimation(anim2);
            }
        });
        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mApos.startAnimation(anim1);
            }
        });
        mApos.startAnimation(anim1);
    }

    public void updateData(WebSocketBasketBallDetails basketBallDetails, ChartBallFragment mChartBallFragment, TextView mTitleGuest, TextView mTitleHome, TextView mTitleVS) {
        DataEntity score = basketBallDetails.getData();

        switch (basketBallDetails.getData().getMatchStatus()) {
            case DETERMINED://待定
            case GAME_CANCLE: //比赛取消
            case GAME_CUT: //比赛中断
            case GAME_DELAY: //比赛推迟
                if (mMatch.getMatchStatus() == DETERMINED) {
                    mMatchState.setText(R.string.basket_undetermined);
                } else if (mMatch.getMatchStatus() == GAME_CANCLE) {
                    mMatchState.setText(R.string.basket_cancel);
                } else if (mMatch.getMatchStatus() == GAME_CUT) {
                    mMatchState.setText(R.string.basket_interrupt);
                } else {
                    mMatchState.setText(R.string.basket_postpone);
                }
                mApos.setVisibility(View.GONE);
                mRemainTime.setText("");
                mChartBallFragment.setClickableLikeBtn(false);
                break;

            case END://完场
                mChartBallFragment.setClickableLikeBtn(false);
                mApos.setVisibility(View.GONE);
                mGuestScore.setText(score.getGuestScore() + "");
                mGuestScore.setTextColor(getResources().getColor(R.color.score_color_white));
                mHomeScore.setText(score.getHomeScore() + "");
                mHomeScore.setTextColor(getResources().getColor(R.color.score_color_white));
                mMatchState.setText(R.string.finished_txt);
                mGuest1.setText(score.getGuest1() + "");
                mGuest1.setTextColor(getResources().getColor(R.color.score_color_white));
                mGuest2.setText(score.getGuest2() + "");
                mGuest2.setTextColor(getResources().getColor(R.color.score_color_white));
                mGuest3.setText(score.getGuest3() + "");
                mGuest3.setTextColor(getResources().getColor(R.color.score_color_white));
                mGuest4.setText(score.getGuest4() + "");
                mGuest4.setTextColor(getResources().getColor(R.color.score_color_white));
                mHome1.setText(score.getHome1() + "");
                mHome1.setTextColor(getResources().getColor(R.color.score_color_white));
                mHome2.setText(score.getHome2() + "");
                mHome2.setTextColor(getResources().getColor(R.color.score_color_white));
                mHome3.setText(score.getHome3() + "");
                mHome3.setTextColor(getResources().getColor(R.color.score_color_white));
                mHome4.setText(score.getHome4() + "");
                mHome4.setTextColor(getResources().getColor(R.color.score_color_white));

                mTitleHome.setText(score.getHomeScore() + "");
                mTitleGuest.setText(score.getGuestScore() + "");
                mSmallGuestScore.setText(score.getGuestScore() + "");
                mSmallGuestScore.setTextColor(getResources().getColor(R.color.score_color_white));
                mSmallHomeScore.setText(score.getHomeScore() + "");
                mSmallHomeScore.setTextColor(getResources().getColor(R.color.score_color_white));
                mVS.setText(":");
                mTitleVS.setText(":");
                if (score.getAddTime() == 3) {//三个加时
                    mLayoutOt3.setVisibility(View.VISIBLE);
                    mLayoutOt2.setVisibility(View.VISIBLE);
                    mLayoutOt1.setVisibility(View.VISIBLE);
                    mGuestOt1.setText(score.getGuestOt1() + "");
                    mGuestOt1.setTextColor(getResources().getColor(R.color.score_color_white));
                    mHomeOt1.setText(score.getHomeOt1() + "");
                    mHomeOt1.setTextColor(getResources().getColor(R.color.score_color_white));
                    mGuestOt2.setText(score.getGuestOt2() + "");
                    mGuestOt2.setTextColor(getResources().getColor(R.color.score_color_white));
                    mHomeOt2.setText(score.getHomeOt2() + "");
                    mHomeOt2.setTextColor(getResources().getColor(R.color.score_color_white));
                    mGuestOt3.setText(score.getGuestOt3() + "");
                    mGuestOt3.setTextColor(getResources().getColor(R.color.score_color_white));
                    mHomeOt3.setText(score.getHomeOt3() + "");
                    mHomeOt3.setTextColor(getResources().getColor(R.color.score_color_white));
                } else if (score.getAddTime() == 2) {
                    mLayoutOt2.setVisibility(View.VISIBLE);
                    mLayoutOt1.setVisibility(View.VISIBLE);
                    mGuestOt1.setText(score.getGuestOt1() + "");
                    mGuestOt1.setTextColor(getResources().getColor(R.color.score_color_white));
                    mHomeOt1.setText(score.getHomeOt1() + "");
                    mHomeOt1.setTextColor(getResources().getColor(R.color.score_color_white));
                    mGuestOt2.setText(score.getGuestOt2() + "");
                    mGuestOt2.setTextColor(getResources().getColor(R.color.score_color_white));
                    mHomeOt2.setText(score.getHomeOt2() + "");
                    mHomeOt2.setTextColor(getResources().getColor(R.color.score_color_white));
                } else if (score.getAddTime() == 1) {
                    mLayoutOt1.setVisibility(View.VISIBLE);
                    mGuestOt1.setText(score.getGuestOt1() + "");
                    mGuestOt1.setTextColor(getResources().getColor(R.color.score_color_white));
                    mHomeOt1.setText(score.getHomeOt1() + "");
                    mHomeOt1.setTextColor(getResources().getColor(R.color.score_color_white));
                }
                mRemainTime.setText("");//完场无剩余时间
                break;
            case OT3:
                mLayoutOt3.setVisibility(View.VISIBLE);
                setScore(score.getGuestOt3(), mGuestOt3, score.getHomeOt3(), mHomeOt3);
            case OT2:
                mLayoutOt2.setVisibility(View.VISIBLE);
                setScore(score.getGuestOt2(), mGuestOt2, score.getHomeOt2(), mHomeOt2);
            case OT1:
                mLayoutOt1.setVisibility(View.VISIBLE);
                setScore(score.getGuestOt1(), mGuestOt1, score.getHomeOt1(), mHomeOt1);
            case FOURTH_QUARTER:
                setScore(score.getGuest4(), mGuest4, score.getHome4(), mHome4);
            case THIRD_QUARTER:
                setScore(score.getGuest3(), mGuest3, score.getHome3(), mHome3);

            case HALF_GAME: //中场
            case SECOND_QUARTER:
                setScore(score.getGuest2(), mGuest2, score.getHome2(), mHome2);

            case FIRST_QUARTER:
                setScore(score.getGuest1(), mGuest1, score.getHome1(), mHome1);
                //不管是第几节都设置总比分.推送過來的話比分有变化要翻转

                mVS.setText(":");
                mTitleVS.setText(":");

                if (mGuestNum != score.getGuestScore()) {
                    scoreAnimation(mGuestScore);
                    mGuestNum = score.getGuestScore();
                }
                if (mHomeNum != score.getHomeScore()) {
                    scoreAnimation(mHomeScore);
                    mHomeNum = score.getHomeScore();
                }
                mChartBallFragment.setClickableLikeBtn(true);//聊球可点赞
                setScore(score.getGuestScore(), mGuestScore, score.getHomeScore(), mHomeScore);// 动画有毒，最后在设一下比分

//                L.d("score.getHomeScore()>>>>...>>>" + score.getHomeScore());
                setScore(score.getGuestScore(), mSmallGuestScore, score.getHomeScore(), mSmallHomeScore);

                mTitleHome.setText(score.getHomeScore() + "");
                mTitleGuest.setText(score.getGuestScore() + "");


                //设置比赛时间及状态
                if (score.getMatchStatus() == FIRST_QUARTER) {
                    if (mMatch.getSection() == 2) {
                        mMatchState.setText("1st half ");
                    } else {
                        mMatchState.setText("1st  ");
                    }
                    mApos.setVisibility(View.VISIBLE);
                } else if (score.getMatchStatus() == SECOND_QUARTER) {
                    if (mMatch.getSection() == 2) {
                        mMatchState.setText("1st half ");
                    } else {
                        mMatchState.setText("2nd  ");
                    }
                    mApos.setVisibility(View.VISIBLE);
                } else if (score.getMatchStatus() == HALF_GAME) {
                    mMatchState.setText("half time  ");
                    mApos.setVisibility(View.GONE);
                } else if (score.getMatchStatus() == THIRD_QUARTER) {
                    if (mMatch.getSection() == 2) {
                        mMatchState.setText("2nd half ");
                    } else {
                        mMatchState.setText("3rd  ");
                    }
                    mApos.setVisibility(View.VISIBLE);
                } else if (score.getMatchStatus() == FOURTH_QUARTER) {
                    if (mMatch.getSection() == 2) {
                        mMatchState.setText("2nd half  ");
                    } else {
                        mMatchState.setText("4th  ");
                    }
                    mApos.setVisibility(View.VISIBLE);
                } else if (score.getMatchStatus() == OT1) {
                    mMatchState.setText("OT1  ");
                    mApos.setVisibility(View.VISIBLE);
                } else if (score.getMatchStatus() == OT2) {
                    mMatchState.setText("OT2  ");
                    mApos.setVisibility(View.VISIBLE);
                } else {
                    mMatchState.setText("OT3  ");
                    mApos.setVisibility(View.VISIBLE);
                }

                //设置剩余时间
                mRemainTime.setText(score.getRemainTime() == null ? "" : score.getRemainTime());//为空的话就设置为空字符

                if (score.getMatchStatus() == HALF_GAME) {
                    mRemainTime.setText("");//中场时无剩余时间。。后台可能中场也给时间。没办法
                }

                if (score.getRemainTime() == null || score.getRemainTime().equals("")) {
                    mApos.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * 设置比分变化时的的翻转动画
     */
    public void scoreAnimation(final TextView changeText) {
        float cX = changeText.getWidth() / 2.0f;
        float cY = changeText.getHeight() / 2.0f;

        MyRotateAnimation rotateAnim = new MyRotateAnimation(cX, cY, MyRotateAnimation.ROTATE_DECREASE);

        rotateAnim.setFillAfter(true);

        changeText.startAnimation(rotateAnim);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        L.d("wanggg", "getApplicationContext=" + mActivity);
        L.d("wanggg", "MyApp=" + MyApp.getContext());


    }
}
