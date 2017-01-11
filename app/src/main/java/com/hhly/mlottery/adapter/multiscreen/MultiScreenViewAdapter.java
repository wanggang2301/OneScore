package com.hhly.mlottery.adapter.multiscreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewAdapter;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewHolder;
import com.hhly.mlottery.bean.multiscreenview.MultiScreenBasketMatchBean;
import com.hhly.mlottery.bean.multiscreenview.MultiScreenBasketMatchScoreBean;
import com.hhly.mlottery.bean.multiscreenview.MultiScreenBasketballBean;
import com.hhly.mlottery.bean.multiscreenview.MultiScreenFootBallBean;
import com.hhly.mlottery.bean.multiscreenview.MultiScreenViewBean;
import com.hhly.mlottery.callback.MultiScreenViewCallBack;
import com.hhly.mlottery.frame.basketballframe.MyRotateAnimation;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.StringUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.List;

/**
 * @author: Wangg
 * @Name：MultiScreenViewAdapter
 * @Description:
 * @Created on:2017/1/5  10:46.
 */

public class MultiScreenViewAdapter extends BaseRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_FOOTBALL = 1;
    private static final int VIEW_TYPE_BASKETBALL = 2;

    private final static String LIVEBEFORE = "0";//直播前
    private final static String ONLIVE = "1";//直播中
    private final static String LIVEENDED = "-1";//直播结束
    private List<MultiScreenViewBean> list;

    private Context mContext;

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

    public MultiScreenViewCallBack multiScreenViewCallBack;


    public void setMultiScreenViewCallBack(MultiScreenViewCallBack multiScreenViewCallBack) {
        this.multiScreenViewCallBack = multiScreenViewCallBack;
    }

    public MultiScreenViewAdapter(Context mContext, List<MultiScreenViewBean> list) {
        this.list = list;
        this.mContext = mContext;
    }


    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.item_multi_screen_view_football, R.layout.item_multi_screen_view_basketball};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {

        int itemViewType = getRecycleViewItemType(position);
        switch (itemViewType) {
            case 1:
                bindFootballData(viewHolder, position);
                break;
            case 2:
                bindBasketballData(viewHolder, position);
                break;
        }
    }


    private void bindFootballData(BaseRecyclerViewHolder viewHolder, final int position) {

        MultiScreenFootBallBean m = (MultiScreenFootBallBean) list.get(position).getData();

        ImageView iv_bg = viewHolder.findViewById(R.id.iv_bg);
        ImageView delete = viewHolder.findViewById(R.id.btn_delete);
        TextView tv_homename = viewHolder.findViewById(R.id.tv_home_name);
        TextView tv_guestname = viewHolder.findViewById(R.id.tv_guest_name);

        ImageView iv_home_icon = viewHolder.findViewById(R.id.iv_home_icon);
        ImageView iv_guest_icon = viewHolder.findViewById(R.id.iv_guest_icon);

        TextView score = viewHolder.findViewById(R.id.score);

        TextView date = viewHolder.findViewById(R.id.date);

        LinearLayout mMatchTypeLayout = viewHolder.findViewById(R.id.matchType);

        TextView mMatchType1 = viewHolder.findViewById(R.id.football_match_detail_matchtype1);

        TextView mMatchType2 = viewHolder.findViewById(R.id.football_match_detail_matchtype2);

        ImageLoader.load(mContext, m.getBg(), R.color.black_title).into(iv_bg);

        loadImage(m.getHome_icon(), iv_home_icon);
        loadImage(m.getGuest_icon(), iv_guest_icon);
        tv_homename.setText(m.getHome_name());
        tv_guestname.setText(m.getGuest_name());

        if (LIVEBEFORE.equals(m.getLiveStatus())) { //赛前
            score.setText("VS");
            score.setTextColor(mContext.getResources().getColor(R.color.white));
        } else if (LIVEENDED.equals(m.getLiveStatus())) {
            score.setText(m.getHomeScore() + " : " + m.getGuestScore());
            score.setTextColor(mContext.getResources().getColor(R.color.score));
        } else if (ONLIVE.equals(m.getLiveStatus())) { //未完场头部
            score.setText(m.getHomeScore() + " : " + m.getGuestScore());
            score.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        //赛事类型

        if (m.getmMatchType1() == null && m.getmMatchType2() == null) {
            mMatchTypeLayout.setVisibility(View.INVISIBLE);
        } else {

            if (StringUtils.isEmpty(m.getmMatchType1())) {
                mMatchType1.setVisibility(View.INVISIBLE);
            }
            if (StringUtils.isEmpty(m.getmMatchType2())) {
                mMatchType2.setVisibility(View.INVISIBLE);
            }
            mMatchType1.setText(StringUtils.nullStrToEmpty(m.getmMatchType1()));
            mMatchType2.setText(StringUtils.nullStrToEmpty(m.getmMatchType2()));
            mMatchTypeLayout.setVisibility(View.VISIBLE);
        }

        if (m.getDate() != null) {
            String startTime = m.getDate();
            if (date == null) {
                return;
            }
            if (!StringUtils.isEmpty(startTime) && startTime.length() == 16) {
                date.setText(startTime);
            } else {
                date.setText("");//开赛时间
            }
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.d("zxcvbnm", "足球" + position);
                if (multiScreenViewCallBack != null) {
                    multiScreenViewCallBack.delete(position);
                }
            }
        });
    }

    private int mGuestNum = 0;
    private int mHomeNum = 0;

    private void bindBasketballData(BaseRecyclerViewHolder viewHolder, final int position) {
        MultiScreenBasketballBean b = (MultiScreenBasketballBean) list.get(position).getData();
        ImageView mHeadImage = viewHolder.findViewById(R.id.image_background);
        ImageView delete = viewHolder.findViewById(R.id.btn_delete);

        ImageView mHomeIcon = viewHolder.findViewById(R.id.basket_details_home_icon);
        ImageView mGuestIcon = viewHolder.findViewById(R.id.basket_details_guest_icon);
        TextView mHomeTeam = viewHolder.findViewById(R.id.basket_details_home_name);
        TextView mGuestTeam = viewHolder.findViewById(R.id.basket_details_guest_name);
        TextView mHomeRanking = viewHolder.findViewById(R.id.basket_details_home_Ranking);
        TextView mGuestRanking = viewHolder.findViewById(R.id.basket_details_guest_Ranking);
        TextView mLeagueName = viewHolder.findViewById(R.id.basket_details_matches_name);
        TextView mVS = viewHolder.findViewById(R.id.basket_score_maohao);
        TextView mHomeScore = viewHolder.findViewById(R.id.basket_details_home_all_score);

        TextView mGuestScore = viewHolder.findViewById(R.id.basket_details_guest_all_score);
        TextView mMatchState = viewHolder.findViewById(R.id.basket_details_state);
        TextView mRemainTime = viewHolder.findViewById(R.id.basket_details_remain_time);
        TextView mApos = viewHolder.findViewById(R.id.backetball_details_apos);

        mApos.setVisibility(View.GONE);

        TextView mGuest1 = viewHolder.findViewById(R.id.basket_details_guest_first);
        TextView mGuest2 = viewHolder.findViewById(R.id.basket_details_guest_second);
        TextView mGuest3 = viewHolder.findViewById(R.id.basket_details_guest_third);
        TextView mGuest4 = viewHolder.findViewById(R.id.basket_details_guest_fourth);
        TextView mHome1 = viewHolder.findViewById(R.id.basket_details_home_first);
        TextView mHome2 = viewHolder.findViewById(R.id.basket_details_home_second);
        TextView mHome3 = viewHolder.findViewById(R.id.basket_details_home_third);
        TextView mHome4 = viewHolder.findViewById(R.id.basket_details_home_fourth);

        LinearLayout mLayoutOt1 = viewHolder.findViewById(R.id.basket_details_llot1);
        LinearLayout mLayoutOt2 = viewHolder.findViewById(R.id.basket_details_llot2);
        LinearLayout mLayoutOt3 = viewHolder.findViewById(R.id.basket_details_llot3);
        TextView mGuestOt1 = viewHolder.findViewById(R.id.basket_details_guest_ot1);
        TextView mGuestOt2 = viewHolder.findViewById(R.id.basket_details_guest_ot2);
        TextView mGuestOt3 = viewHolder.findViewById(R.id.basket_details_guest_ot3);
        TextView mHomeOt1 = viewHolder.findViewById(R.id.basket_details_home_ot1);
        TextView mHomeOt2 = viewHolder.findViewById(R.id.basket_details_home_ot2);
        TextView mHomeOt3 = viewHolder.findViewById(R.id.basket_details_home_ot3);
        TextView mSmallGuestScore = viewHolder.findViewById(R.id.basket_details_guest_small_total);
        TextView mSmallHomeScore = viewHolder.findViewById(R.id.basket_details_home_small_total);

        /**
         * 启动秒闪烁
         */
        setApos(mApos);

        MultiScreenBasketMatchBean mMatch = b.getMatch();
        MultiScreenBasketMatchScoreBean score = b.getMatch().getMatchScore();//比分

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
            ImageLoader.load(mContext, mMatch.getGuestLogoUrl(), R.mipmap.basket_default).into(mGuestIcon);
            ImageLoader.load(mContext, b.getBgUrl(), R.color.black).into(mHeadImage);
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
                        mMatchState.setText(b.getMatch().getDate() + "  " + b.getMatch().getTime() + "   " + MyApp.getContext().getResources().getString(R.string.basket_begin_game));
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

                    break;


                case END://完场

                    mGuestScore.setText(score.getGuestScore() + "");
                    mGuestScore.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                    mHomeScore.setText(score.getHomeScore() + "");
                    mHomeScore.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                    mMatchState.setText(R.string.finished_txt);
                    // mMatchState.setText("哈哈哈");
                    mGuest1.setText(score.getGuest1() + "");
                    mGuest1.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                    mGuest2.setText(score.getGuest2() + "");
                    mGuest2.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                    mGuest3.setText(score.getGuest3() + "");
                    mGuest3.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                    mGuest4.setText(score.getGuest4() + "");
                    mGuest4.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                    mHome1.setText(score.getHome1() + "");
                    mHome1.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                    mHome2.setText(score.getHome2() + "");
                    mHome2.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                    mHome3.setText(score.getHome3() + "");
                    mHome3.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                    mHome4.setText(score.getHome4() + "");
                    mHome4.setTextColor(mContext.getResources().getColor(R.color.score_color_white));

                    mSmallGuestScore.setText(score.getGuestScore() + "");
                    mSmallGuestScore.setTextColor(mContext.getResources().getColor(R.color.score_color_white));

                    mSmallHomeScore.setText(score.getHomeScore() + "");
                    mSmallHomeScore.setTextColor(mContext.getResources().getColor(R.color.score_color_white));

                    mVS.setText(":");
                    if (score.getAddTime() == 3) {//三个加时
                        mLayoutOt3.setVisibility(View.VISIBLE);
                        mLayoutOt2.setVisibility(View.VISIBLE);
                        mLayoutOt1.setVisibility(View.VISIBLE);
                        mGuestOt1.setText(score.getGuestOt1() + "");
                        mGuestOt1.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                        mHomeOt1.setText(score.getHomeOt1() + "");
                        mHomeOt1.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                        mGuestOt2.setText(score.getGuestOt2() + "");
                        mGuestOt2.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                        mHomeOt2.setText(score.getHomeOt2() + "");
                        mHomeOt2.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                        mGuestOt3.setText(score.getGuestOt3() + "");
                        mGuestOt3.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                        mHomeOt3.setText(score.getHomeOt3() + "");
                        mHomeOt3.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                    } else if (score.getAddTime() == 2) {
                        mLayoutOt2.setVisibility(View.VISIBLE);
                        mLayoutOt1.setVisibility(View.VISIBLE);
                        mGuestOt1.setText(score.getGuestOt1() + "");
                        mGuestOt1.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                        mHomeOt1.setText(score.getHomeOt1() + "");
                        mHomeOt1.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                        mGuestOt2.setText(score.getGuestOt2() + "");
                        mGuestOt2.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                        mHomeOt2.setText(score.getHomeOt2() + "");
                        mHomeOt2.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                    } else if (score.getAddTime() == 1) {
                        mLayoutOt1.setVisibility(View.VISIBLE);
                        mGuestOt1.setText(score.getGuestOt1() + "");
                        mGuestOt1.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
                        mHomeOt1.setText(score.getHomeOt1() + "");
                        mHomeOt1.setTextColor(mContext.getResources().getColor(R.color.score_color_white));
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


                    if (mGuestNum != score.getGuestScore()) {
                        scoreAnimation(mGuestScore);
                        mGuestNum = score.getGuestScore();
                    }
                    if (mHomeNum != score.getHomeScore()) {
                        scoreAnimation(mHomeScore);
                        mHomeNum = score.getHomeScore();
                    }
                    setScore(score.getGuestScore(), mGuestScore, score.getHomeScore(), mHomeScore);// 动画有毒，最后在设一下比分

                    setScore(score.getGuestScore(), mSmallGuestScore, score.getHomeScore(), mSmallHomeScore);


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

                default:
                    break;
            }
        }


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.d("zxcvbnm", "篮球" + position);

                //notifyItemRemoved(position);
                if (multiScreenViewCallBack != null) {
                    multiScreenViewCallBack.delete(position);
                }
            }
        });
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


    @Override
    public int getRecycleViewItemType(int position) {
        if (list.get(position).getType() == 1) {  //足球
            return VIEW_TYPE_FOOTBALL;
        } else {
            return VIEW_TYPE_BASKETBALL;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    /**
     * 秒闪烁
     */
    private void setApos(final TextView mApos) {
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


    /**
     * load internet image
     *
     * @param imageUrl
     * @param imageView
     */
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


}
