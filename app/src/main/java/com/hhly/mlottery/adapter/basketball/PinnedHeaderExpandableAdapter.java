package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.BasketAllOddBean;
import com.hhly.mlottery.bean.basket.BasketMatchBean;
import com.hhly.mlottery.bean.basket.BasketOddBean;
import com.hhly.mlottery.frame.basketballframe.ImmedBasketballFragment;
import com.hhly.mlottery.frame.basketballframe.MyRotateAnimation;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.view.PinnedHeaderExpandableListView;
import com.hhly.mlottery.view.PinnedHeaderExpandableListView.HeaderAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;
import java.util.Map;

/**
 * 篮球列表的Adapter
 * Created by yixq on 2015/12/30.
 */
public class PinnedHeaderExpandableAdapter extends BaseExpandableListAdapter implements HeaderAdapter {

    private List<String> groupDataList;//父类view 数据
    private List<List<BasketMatchBean>> childrenDataList;//子view数据

    private Context mContext;
//	private int memClass = ((ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();//为系统可用内存
//	private int cacheSize = 1024 * 1024 * memClass / 4;  //硬引用缓存容量，为系统可用内存的1/4

//	private LruCache<String, Bitmap> mLruCache = new LruCache<>(cacheSize);

    private Map<String, BasketAllOddBean> MatchOdds;
//	private BasketOddsBean MatchOdds;

    private ImmedBasketballFragment.BasketFocusClickListener mFocus; //关注监听回掉

    public void setmFocus(ImmedBasketballFragment.BasketFocusClickListener mFocus) {
        this.mFocus = mFocus;
    }
//    private Bitmap mDefualtImg;

    private PinnedHeaderExpandableListView listView;
    private LayoutInflater inflater;
    private List<Integer> isToday;//---fragment判断

//    private ImageLoader mImageLoader; //
//    private ImageLoader.ImageCache mImageCache;

    private DisplayImageOptions options; //
    private com.nostra13.universalimageloader.core.ImageLoader universalImageLoader;

    public PinnedHeaderExpandableAdapter(List<List<BasketMatchBean>> childrenDataList, List<String> groupDataList
            , Context mContext, PinnedHeaderExpandableListView listView) {
        this.groupDataList = groupDataList;
        this.childrenDataList = childrenDataList;
        this.mContext = mContext;
        this.listView = listView;
//        this.isToday = isToday;
        inflater = LayoutInflater.from(this.mContext);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageOnLoading(R.mipmap.basket_default)
                .showImageForEmptyUri(R.mipmap.basket_default)    //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.mipmap.basket_default)// 加载失败显示的图片
                .resetViewBeforeLoading(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext).build();
        universalImageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance(); //初始化
        universalImageLoader.init(config);
    }

    public void updateDatas(List<List<BasketMatchBean>> childrenDataList, List<String> groupDataList) {
        this.groupDataList = groupDataList;
        this.childrenDataList = childrenDataList;
    }

    /**
     * 获取指定组中的指定子元素数据。    返回值      返回指定子元素数据。
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childrenDataList.get(groupPosition).get(childPosition);
    }

    /**
     * 获取指定组中的指定子元素ID，这个ID在组里一定是唯一的。联合ID（getCombinedChildId(long, long)）在所有条目（所有组和所有元素）中也是唯一的。
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    /**
     * 设置 子内容 item数据
     *
     * @param groupPosition 组位置（该组内部含有子元素）
     * @param childPosition 子元素位置（决定返回哪个视图）
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象。注意：在使用前你应该检查一下这个视图对象是否非空并且这个对象的类型是否合适。
     *                      由此引伸出，如果该对象不能被转换并显示正确的数据，这个方法就会调用getChildView(int, int, boolean, View, ViewGroup)来创建一个视图(View)对象。
     * @param parent        指定位置上的子元素返回的视图对象
     * @return
     */


    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Holder holder;
        if (null == convertView) {
            convertView = createChildrenView();
            holder = new Holder();

            holder.home_icon = (ImageView) convertView.findViewById(R.id.home_icon);
//			holder.home_icon.setTag(childrenDataList.get(groupPosition).get(childPosition).getHomeLogoUrl());//url 标记

            holder.guest_icon = (ImageView) convertView.findViewById(R.id.guest_icon);
//			holder.guest_icon.setTag(childrenDataList.get(groupPosition).get(childPosition).getGuestLogoUrl());//url 标记

            holder.mIv_guangzhu = (ImageView) convertView.findViewById(R.id.Iv_guangzhu);


            holder.matches_name = (TextView) convertView.findViewById(R.id.backetball_matches_name);
            holder.game_time = (TextView) convertView.findViewById(R.id.backetball_game_time);
            holder.st_time = (TextView) convertView.findViewById(R.id.backetball_st_time);
            holder.ongoing_time = (TextView) convertView.findViewById(R.id.backetball_ongoing_time);
            holder.score_total = (TextView) convertView.findViewById(R.id.backetball_score_total);
            holder.score_differ = (TextView) convertView.findViewById(R.id.backetball_score_differ);
            holder.home_name = (TextView) convertView.findViewById(R.id.home_name);
            holder.guest_name = (TextView) convertView.findViewById(R.id.guest_name);

            holder.home_Ranking = (TextView) convertView.findViewById(R.id.basket_home_Ranking);
            holder.guest_Ranking = (TextView) convertView.findViewById(R.id.basket_guest_Ranking);

            holder.basket_guest_all_score = (TextView) convertView.findViewById(R.id.basket_guest_all_score);
            holder.basket_score = (TextView) convertView.findViewById(R.id.basket_score);
            holder.basket_home_all_score = (TextView) convertView.findViewById(R.id.basket_home_all_score);

            holder.basket_half_score = (TextView) convertView.findViewById(R.id.basket_half_score);

            /**
             * 适配4.1系统
             */
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion == 16) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,20,0,20);
                holder.basket_half_score.setLayoutParams(params);
            }
            holder.basket_leftOdds = (TextView) convertView.findViewById(R.id.basket_leftOdds);
            holder.basket_rightOdds = (TextView) convertView.findViewById(R.id.basket_rightOdds);
            holder.basket_handicap = (TextView) convertView.findViewById(R.id.basket_handicap);

            holder.backetball_differ = (TextView) convertView.findViewById(R.id.backetball_differ);
            holder.backetball_total = (TextView) convertView.findViewById(R.id.backetball_total);

            holder.tv_a = (TextView) convertView.findViewById(R.id.tv_a);
            holder.tv_b = (TextView) convertView.findViewById(R.id.tv_b);

            holder.backetball_apos = (TextView) convertView.findViewById(R.id.backetball_apos);


            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final BasketMatchBean childredata = childrenDataList.get(groupPosition).get(childPosition);

        holder.basket_guest_all_score.setTag(childredata.getThirdId());
        holder.basket_home_all_score.setTag(childredata.getThirdId());


//		holder.home_icon.setTag(childrenDataList.get(groupPosition).get(childPosition).getHomeLogoUrl());//url 标记
//		holder.guest_icon.setTag(childrenDataList.get(groupPosition).get(childPosition).getGuestLogoUrl());//url 标记

        //主队url
        final String homelogourl = childredata.getHomeLogoUrl();  //"http://pic.13322.com/basketball/team/135_135/29.png"
        //客队url
        final String guestlogourl = childredata.getGuestLogoUrl();

        /**
         * 设置tag 、默认图片
         */
        holder.home_icon.setTag(homelogourl);
        holder.guest_icon.setTag(guestlogourl);
        //ImagaeLoader -- 加载图片


        universalImageLoader.displayImage(homelogourl, holder.home_icon,options);
        universalImageLoader.displayImage(guestlogourl, holder.guest_icon,options);

        //赔率设置
        MatchOdds = childredata.getMatchOdds();
        if (MatchOdds != null) {

            boolean asize = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_rbSizeBall, false); //大小球
            boolean eur = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_RBOCOMPENSATE, false);//欧赔
            boolean alet = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_RBSECOND, true); //亚盘
            boolean noshow = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_RBNOTSHOW, false);//不显示

            /**
             * 大小球
             */
            if (asize) {

                if (MatchOdds.get("asiaSize") != null) {
                    BasketOddBean mAsize = MatchOdds.get("asiaSize").getCrown();

                    if (mAsize == null) {
                        holder.basket_leftOdds.setText("");
                        holder.basket_rightOdds.setText("");
                        holder.basket_handicap.setText("");
                        holder.tv_a.setText("");
                        holder.tv_b.setText("");
                    } else if (mAsize != null) {

                        if (mAsize.getLeftOdds() != null && mAsize.getRightOdds() != null && mAsize.getHandicapValue() != null) {
                            if (mAsize.getLeftOdds().equals("-") || mAsize.getRightOdds().equals("-") || mAsize.getHandicapValue().equals("-")) {
                                holder.basket_leftOdds.setText("");
                                holder.basket_rightOdds.setText("");
                                holder.basket_handicap.setText(R.string.basket_handicap_feng);
                                holder.tv_a.setText("");
                                holder.tv_b.setText("");
                            } else {
                                holder.basket_leftOdds.setText(mAsize.getLeftOdds());
                                holder.basket_rightOdds.setText(mAsize.getRightOdds());
                                holder.basket_handicap.setText(mContext.getString(R.string.basket_odds_asize) + mAsize.getHandicapValue());
                                holder.tv_a.setText("|");
                                holder.tv_b.setText("|");
                            }
                        }else{
                            holder.basket_leftOdds.setText("");
                            holder.basket_rightOdds.setText("");
                            holder.basket_handicap.setText("");
                            holder.tv_a.setText("");
                            holder.tv_b.setText("");
                        }
                    }
                }else{
                    holder.basket_leftOdds.setText("");
                    holder.basket_rightOdds.setText("");
                    holder.basket_handicap.setText("");
                    holder.tv_a.setText("");
                    holder.tv_b.setText("");
                }
            }
            /**
             * 欧赔
             */
            if (eur) {
//				BasketOddBean mEur = MatchOdds.get(1);
//				BasketOddsBean mOdds = new BasketOddsBean();

                if (MatchOdds.get("euro") != null) {
                    BasketOddBean mEur = MatchOdds.get("euro").getEuro();
//				BasketOddBean mEur = MatchOdds.getEuro();

                    if (mEur == null) {
                        holder.basket_leftOdds.setText("");
                        holder.basket_rightOdds.setText("");
                        holder.basket_handicap.setText("");
                        holder.tv_a.setText("");
                        holder.tv_b.setText("");
                    } else if (mEur != null) {

                        if (mEur.getLeftOdds() != null && mEur.getRightOdds() != null) {
                            if (mEur.getLeftOdds().equals("-") || mEur.getRightOdds().equals("-")) {
                                holder.basket_leftOdds.setText("");
                                holder.basket_rightOdds.setText("");
                                holder.basket_handicap.setText(R.string.basket_handicap_feng);
                                holder.tv_a.setText("");
                                holder.tv_b.setText("");
                            } else {
                                holder.basket_leftOdds.setText(mEur.getLeftOdds());
                                holder.basket_rightOdds.setText(mEur.getRightOdds());
                                holder.basket_handicap.setText(mContext.getString(R.string.basket_odds_eur));
                                holder.tv_a.setText("|");
                                holder.tv_b.setText("|");
                            }
                        }else{
                            holder.basket_leftOdds.setText("");
                            holder.basket_rightOdds.setText("");
                            holder.basket_handicap.setText("");
                            holder.tv_a.setText("");
                            holder.tv_b.setText("");
                        }

                    }
                }else{
                    holder.basket_leftOdds.setText("");
                    holder.basket_rightOdds.setText("");
                    holder.basket_handicap.setText("");
                    holder.tv_a.setText("");
                    holder.tv_b.setText("");
                }
            }
            /**
             * 亚盘
             */
            if (alet) {
//				BasketOddBean mAlet = MatchOdds.get(0);
//				BasketOddsBean mOdds = new BasketOddsBean();
                if (MatchOdds.get("asiaLet") != null) {
                    BasketOddBean mAlet = MatchOdds.get("asiaLet").getCrown();

//				BasketOddBean mAlet = MatchOdds.getAsiaLet();
                    if (mAlet == null) {
                        holder.basket_leftOdds.setText("");
                        holder.basket_rightOdds.setText("");
                        holder.basket_handicap.setText("");
                        holder.tv_a.setText("");
                        holder.tv_b.setText("");
                    } else if (mAlet != null) {

                        if (mAlet.getLeftOdds() != null && mAlet.getRightOdds() != null && mAlet.getHandicapValue() != null) {
                            if (mAlet.getLeftOdds().equals("-") || mAlet.getRightOdds().equals("-") || mAlet.getHandicapValue().equals("-")) {
                                holder.basket_leftOdds.setText("");
                                holder.basket_rightOdds.setText("");
                                holder.basket_handicap.setText(R.string.basket_handicap_feng);
                                holder.tv_a.setText("");
                                holder.tv_b.setText("");
                            } else {
                                holder.basket_leftOdds.setText(mAlet.getLeftOdds());
                                holder.basket_rightOdds.setText(mAlet.getRightOdds());

                                //int i = Integer.parseInt(mAlet.getHandicapValue());
                                Double aletData = Double.parseDouble(mAlet.getHandicapValue()); // 转换为 int 型 不行(null)？？
                                if (aletData > 0) {
                                    holder.basket_handicap.setText(mContext.getString(R.string.basket_odds_alet) + "-" + aletData + "");
                                } else {
                                    holder.basket_handicap.setText(mContext.getString(R.string.basket_odds_alet) + "+" + Math.abs(aletData) + "");//绝对值
                                }
                                holder.tv_a.setText("|");
                                holder.tv_b.setText("|");
                            }
                        }else{
                            holder.basket_leftOdds.setText("");
                            holder.basket_rightOdds.setText("");
                            holder.basket_handicap.setText("");
                            holder.tv_a.setText("");
                            holder.tv_b.setText("");
                        }
                    }
                }else{
                    holder.basket_leftOdds.setText("");
                    holder.basket_rightOdds.setText("");
                    holder.basket_handicap.setText("");
                    holder.tv_a.setText("");
                    holder.tv_b.setText("");
                }
            }
            /**
             * 不显示
             */
            if (noshow) {
                holder.basket_leftOdds.setText("");
                holder.basket_rightOdds.setText("");
                holder.basket_handicap.setText("");
                holder.tv_a.setText("");
                holder.tv_b.setText("");
            }
        } else if (MatchOdds == null) {
            holder.basket_leftOdds.setText("");
            holder.basket_rightOdds.setText("");
            holder.basket_handicap.setText("");
            holder.tv_a.setText("");
            holder.tv_b.setText("");
        }


        //比分数据设置
        if (childredata.getLeagueName() != null) {
            holder.matches_name.setText(childredata.getLeagueName());
            holder.matches_name.setTextColor(Color.parseColor(childredata.getLeagueColor()));
        }

        holder.game_time.setText(childredata.getTime());
        /**
         * 去除两端的空格，trim()；  防止-->  "XX   ..."情况
         */
        if (childredata.getHomeTeam() != null) {
            holder.home_name.setText(childredata.getHomeTeam().trim());
        }else{
            holder.home_name.setText("--");
        }
        if (childredata.getGuestTeam() != null) {
            holder.guest_name.setText(childredata.getGuestTeam().trim());
        }else{
            holder.guest_name.setText("--");
        }


//        holder.home_name.setText("波罗的海波罗的海波罗的");
//        holder.guest_name.setText("波罗的海波罗的海波罗的");

//        holder.home_name.setSingleLine();
//		holder.ongoing_time.setText(childrenDataList.get(groupPosition).get(childPosition).getMatchScore().getRemainTime());
//		holder.st_time.setText(childrenDataList.get(groupPosition).get(childPosition).getStatus());

        /**
         * 超过后天的比赛 不显示排名(含后天)
         */
//        if (isToday.get(groupPosition) > 1) {
//            holder.guest_Ranking.setText("");
//            holder.home_Ranking.setText("");
//        }else{
//            if (childredata.getGuestRanking().length() == 0) {
//                holder.guest_Ranking.setText("");
//            } else {
//                holder.guest_Ranking.setText("[" + childredata.getGuestRanking() + "]");
//            }
//            if (childredata.getHomeRanking().length() == 0) {
//                holder.home_Ranking.setText("");
//            } else {
//                holder.home_Ranking.setText("[" + childredata.getHomeRanking() + "]");
//            }
//        }

        if (childredata.getGuestRanking().length() == 0) {
            holder.guest_Ranking.setText("");
        } else {
            holder.guest_Ranking.setText("[" + childredata.getGuestRanking() + "]");
        }
        if (childredata.getHomeRanking().length() == 0) {
            holder.home_Ranking.setText("");
        } else {
            holder.home_Ranking.setText("[" + childredata.getHomeRanking() + "]");
        }

        Integer score = Integer.parseInt(childredata.getMatchStatus());

//        //启动 比分动画
//        if (childredata.getMatchScore() != null && score > 0 && score < 8) {
//            if (childredata.isHomeAnim()) {
//                scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
//                childredata.setIsHomeAnim(false);
//                holder.basket_home_all_score.setVisibility(View.VISIBLE);
//            }
//            if (childredata.isGuestAnim()) {
//                scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
//                childredata.setIsGuestAnim(false);
//                holder.basket_guest_all_score.setVisibility(View.VISIBLE);
//            }
//        }

        if (childredata.getMatchStatus() != null) {

            int scorehome;
            int scoreguest;
            int scorehomehalf;
            int scoreguesthalf;

            if (childredata.getMatchScore() != null) {
                if (childredata.getMatchScore().getRemainTime() == null) {
//                    holder.ongoing_time.setText("进行中");
                    holder.ongoing_time.setText("");
                } else {
                    holder.ongoing_time.setText(childredata.getMatchScore().getRemainTime());
                }
            } else {
                holder.ongoing_time.setText("");
            }

            //0:未开赛,1:一节,2:二节,5:1'OT，以此类推 -1:完场,-2:待定,-3:中断,-4:取消,-5:推迟,50中场
            if (score == 0 || score == -2 || score == -3 || score == -4 || score == -5) {

                scorehome = 0;
                scoreguest = 0;
                scorehomehalf = 0;
                scoreguesthalf = 0;
            } else {

                if (childredata.getMatchScore() == null) {
                    scorehome = 0;
                    scoreguest = 0;
                    scorehomehalf = 0;
                    scoreguesthalf = 0;
                } else {

                    scorehome = childredata.getMatchScore().getHomeScore();//主队分数
                    scoreguest = childredata.getMatchScore().getGuestScore();//客队分数

                    if (childredata.getSection() == 2) { // 只有两节比赛的情况
                        scorehomehalf = childredata.getMatchScore().getHome1();//主队半场得分
                        scoreguesthalf = childredata.getMatchScore().getGuest1();//客队半场得分
                    } else {
                        scorehomehalf = childredata.getMatchScore().getHome1() + childredata.getMatchScore().getHome2();//主队半场得分
                        scoreguesthalf = childredata.getMatchScore().getGuest1() + childredata.getMatchScore().getGuest2();//客队半场得分
                    }
                }
            }

//            //启动 比分动画
//            if (childredata.getMatchScore() != null && score > 0 && score < 8) {
//                if (childredata.isHomeAnim()) {
//                    scoreAnimation(holder.basket_home_all_score, scorehome + ""); //启动动画 客队
//                    childredata.setIsHomeAnim(false);
//                    holder.basket_home_all_score.setVisibility(View.VISIBLE);
//                }
//                if (childredata.isGuestAnim()) {
//                    scoreAnimation(holder.basket_guest_all_score, scoreguest + ""); //启动动画 主队
//                    childredata.setIsGuestAnim(false);
//                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
//                }
//            }


            switch (score) {
                case 0: //未开赛

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.score_total.setVisibility(View.INVISIBLE);
                    holder.score_differ.setVisibility(View.INVISIBLE);
                    holder.basket_score.setText(R.string.basket_VS);
//                    holder.basket_score.setBackgroundColor(Color.WHITE);// -------设置背景色 (防止背景复用比分)-------
                    holder.basket_guest_all_score.setText("");
                    holder.basket_home_all_score.setText("");
                    holder.basket_guest_all_score.setVisibility(View.GONE);
                    holder.basket_home_all_score.setVisibility(View.GONE);
//					holder.basket_score.setTextColor(Color.BLACK);
//                    holder.basket_score.setTextColor(Color.parseColor("#666666"));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_vs));
                    holder.basket_half_score.setVisibility(View.INVISIBLE);
                    holder.ongoing_time.setVisibility(View.GONE);
//					holder.backetball_apos.setVisibility(View.GONE);
                    holder.st_time.setText("");
                    settingdata(holder, 0);

                    break;
                case 1: //一节

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));//#0085E1
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText(scorehome + scoreguest + "");
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

//                        holder.basket_guest_all_score.setText(scoreguest + "");
//                        holder.basket_home_all_score.setText(scorehome + "");
////                        startAnima(childredata, score, holder);

                        if (childredata.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            childredata.setIsHomeAnim(false);
                        }
//                        else{
////                            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//                        }
                        if (childredata.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            childredata.setIsGuestAnim(false);
                        }
//                        else{
////                            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest1() + ":" + childredata.getMatchScore().getHome1() + ")");
                        if (childrenDataList.get(groupPosition).get(childPosition).getSection() == 2) {
                            holder.st_time.setText(R.string.basket_1st_half);
                        } else {
                            holder.st_time.setText(R.string.basket_1st);
                        }
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    }else{
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
//                        holder.basket_half_score.setVisibility(View.GONE);
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }
//					holder.backetball_apos.setVisibility(View.VISIBLE);
                    settingdata(holder, 1);
                    break;
                case 2: //二节

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText(scorehome + scoreguest + "");
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

//                        holder.basket_guest_all_score.setText(scoreguest + "");
//                        holder.basket_home_all_score.setText(scorehome + "");
////                        startAnima(childredata, score, holder);

                        if (childredata.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            childredata.setIsHomeAnim(false);
                        }
//                        else{
////                            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//                        }
                        if (childredata.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            childredata.setIsGuestAnim(false);
                        }
//                        else{
////                            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        if (childredata.getSection() == 2) {
                            holder.st_time.setText(R.string.basket_1st_half);
                        } else {
                            holder.st_time.setText(R.string.basket_2nd);
                        }
//                    holder.st_time.setText("2nd");
                        holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest2() + ":" + childredata.getMatchScore().getHome2() + ")");

                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);

                    }else{
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
//                        holder.basket_half_score.setVisibility(View.GONE);
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

//					holder.backetball_apos.setVisibility(View.VISIBLE);
                    settingdata(holder, 2);
                    break;
                case 3: //三节

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

//                        holder.basket_guest_all_score.setText(scoreguest + "");
//                        holder.basket_home_all_score.setText(scorehome + "");
////                        startAnima(childredata, score, holder);

                        if (childredata.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            childredata.setIsHomeAnim(false);
                        }
//                        else{
////                            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//                        }
                        if (childredata.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            childredata.setIsGuestAnim(false);
                        }
//                        else{
////                            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        if (childredata.getSection() == 2) {
                            holder.st_time.setText(R.string.basket_2nd_half);
                            holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest3() + ":" + childredata.getMatchScore().getHome3() + ")");
                        } else {
                            holder.st_time.setText(R.string.basket_3rd);
                            holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest3() + ":" + childredata.getMatchScore().getHome3() + ")");
                        }

//                    holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest3() + ":" + childredata.getMatchScore().getHome3() + ")");
//                    holder.st_time.setText("3rd");
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    }else{
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
//                        holder.basket_half_score.setVisibility(View.GONE);
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

//					holder.backetball_apos.setVisibility(View.VISIBLE);
                    settingdata(holder, 3);
                    break;
                case 4: //四节

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

//                        holder.basket_guest_all_score.setText(scoreguest + "");
//                        holder.basket_home_all_score.setText(scorehome + "");
////                        startAnima(childredata, score, holder);

                        if (childredata.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            childredata.setIsHomeAnim(false);
                        }
//                        else{
////                            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//                        }
                        if (childredata.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            childredata.setIsGuestAnim(false);
                        }
//                        else{
////                            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest4() + ":" + childredata.getMatchScore().getHome4() + ")");

                        if (childredata.getSection() == 2) {
                            holder.st_time.setText(R.string.basket_2nd_half);
                            holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest3() + ":" + childredata.getMatchScore().getHome3() + ")");
                        } else {
                            holder.st_time.setText(R.string.basket_4th);
                            holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest4() + ":" + childredata.getMatchScore().getHome4() + ")");
                        }
//                    holder.st_time.setText("4th");
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    }else{
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
//                        holder.basket_half_score.setVisibility(View.GONE);
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

//					holder.backetball_apos.setVisibility(View.VISIBLE);
                    settingdata(holder, 4);
                    break;
                case 5: //加时1

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

//                        holder.basket_guest_all_score.setText(scoreguest + "");
//                        holder.basket_home_all_score.setText(scorehome + "");
////                        startAnima(childredata, score, holder);

                        if (childredata.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            childredata.setIsHomeAnim(false);
                        }
//                        else{
////                            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//                        }
                        if (childredata.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            childredata.setIsGuestAnim(false);
                        }
//                        else{
////                            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuestOt1() + ":" + childredata.getMatchScore().getHomeOt1() + ")");
                        holder.st_time.setText(R.string.basket_OT1);
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    }else{
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
//                        holder.basket_half_score.setVisibility(View.GONE);
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

//					holder.backetball_apos.setVisibility(View.VISIBLE);
                    settingdata(holder, 5);
                    break;
                case 6: //加时2

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

//                        holder.basket_guest_all_score.setText(scoreguest + "");
//                        holder.basket_home_all_score.setText(scorehome + "");
////                        startAnima(childredata, score, holder);

                        if (childredata.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            childredata.setIsHomeAnim(false);
                        }
//                        else{
////                            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//                        }
                        if (childredata.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            childredata.setIsGuestAnim(false);
                        }
//                        else{
////                            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuestOt2() + ":" + childredata.getMatchScore().getHomeOt2() + ")");
                        holder.st_time.setText(R.string.basket_OT2);
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    }else{
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
//                        holder.basket_half_score.setVisibility(View.GONE);
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

//					holder.backetball_apos.setVisibility(View.VISIBLE);
                    settingdata(holder, 6);
                    break;
                case 7: //加时3

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

//                        holder.basket_guest_all_score.setText(scoreguest + "");
//                        holder.basket_home_all_score.setText(scorehome + "");
////                        startAnima(childredata, score, holder);

                        if (childredata.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            childredata.setIsHomeAnim(false);
                        }
//                        else{
////                            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//                        }
                        if (childredata.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            childredata.setIsGuestAnim(false);
                        }
//                        else{
////                            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuestOt3() + ":" + childredata.getMatchScore().getHomeOt3() + ")");
                        holder.st_time.setText(R.string.basket_OT3);
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    }else{
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
//                        holder.basket_half_score.setVisibility(View.GONE);
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

//					holder.backetball_apos.setVisibility(View.VISIBLE);
                    settingdata(holder, 7);
                    break;
                case 50: //中场

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.INVISIBLE);
//					holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest2() + ":" + childredata.getMatchScore().getHome2() + ")");
                        holder.st_time.setText(R.string.basket_half_time);

                    }else{
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");
                        holder.basket_half_score.setVisibility(View.GONE);
                    }

                    holder.ongoing_time.setVisibility(View.GONE);
//					holder.backetball_apos.setVisibility(View.GONE);
                    settingdata(holder, 50);
                    break;
                case -1: //完场

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

                    //当借口无数据时 显示“--”
                    if (childredata.getMatchScore() != null) {
                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
//                        holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");
                    }

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.RED);
                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_over_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_over_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_over_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    holder.basket_half_score.setVisibility(View.INVISIBLE);
//					holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest4() + ":" + childredata.getMatchScore().getHome4() + ")");
//					holder.st_time.setVisibility(View.GONE);
                    holder.st_time.setText("");
                    holder.ongoing_time.setVisibility(View.GONE);
//					holder.backetball_apos.setVisibility(View.GONE);
                    settingdata(holder, -1);
                    break;
                case -2: //待定

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.score_total.setVisibility(View.INVISIBLE);
                    holder.score_differ.setVisibility(View.INVISIBLE);

//					holder.basket_all_score.setText("VS");
//					holder.basket_all_score.setTextColor(Color.BLACK);
                    holder.basket_score.setText(R.string.basket_VS);
//                    holder.basket_score.setBackgroundColor(Color.WHITE);// -------设置背景色 (防止背景复用比分)-------
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_vs));
                    holder.basket_guest_all_score.setText("");
                    holder.basket_home_all_score.setText("");
                    holder.basket_guest_all_score.setVisibility(View.GONE);
                    holder.basket_home_all_score.setVisibility(View.GONE);
//					holder.basket_guest_all_score.setText(scoreguest);
//					holder.basket_home_all_score.setText(scorehome);
//					holder.basket_guest_all_score.setTextColor(Color.RED);
//					holder.basket_home_all_score.setTextColor(Color.RED);

                    holder.basket_half_score.setVisibility(View.INVISIBLE);
                    holder.st_time.setText(R.string.basket_undetermined);
                    holder.ongoing_time.setVisibility(View.GONE);
//					holder.backetball_apos.setVisibility(View.GONE);
                    settingdata(holder, -2);
                    break;
                case -3: //中断

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.ongoing_time.setVisibility(View.GONE);
//					holder.backetball_apos.setVisibility(View.GONE);
                    holder.basket_half_score.setVisibility(View.INVISIBLE);
                    holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                    holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
                    //TODO--------********************************
                    holder.st_time.setText(R.string.basket_interrupt);
                    settingdata(holder, -3);
                    break;
                case -4: //取消

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.score_total.setVisibility(View.INVISIBLE);
                    holder.score_differ.setVisibility(View.INVISIBLE);

//					holder.basket_all_score.setText("VS");
//					holder.basket_all_score.setTextColor(Color.BLACK);
                    holder.basket_score.setText(R.string.basket_VS);
//                    holder.basket_score.setBackgroundColor(Color.WHITE);// -------设置背景色 (防止背景复用比分)-------
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_vs));
                    holder.basket_guest_all_score.setText("");
                    holder.basket_home_all_score.setText("");
                    holder.basket_guest_all_score.setVisibility(View.GONE);
                    holder.basket_home_all_score.setVisibility(View.GONE);

                    holder.basket_half_score.setVisibility(View.INVISIBLE);
                    holder.st_time.setText(R.string.basket_cancel);
                    holder.ongoing_time.setVisibility(View.GONE);
//					holder.backetball_apos.setVisibility(View.GONE);
                    settingdata(holder, -4);
                    break;
                case -5: //推迟

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.score_total.setVisibility(View.INVISIBLE);
                    holder.score_differ.setVisibility(View.INVISIBLE);

//					holder.basket_all_score.setText("VS");
//					holder.basket_all_score.setTextColor(Color.BLACK);
                    holder.basket_score.setText(R.string.basket_VS);
//                    holder.basket_score.setBackgroundColor(Color.WHITE);// -------设置背景色 (防止背景复用比分)-------
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_vs));
                    holder.basket_guest_all_score.setText("");
                    holder.basket_home_all_score.setText("");
                    holder.basket_guest_all_score.setVisibility(View.GONE);
                    holder.basket_home_all_score.setVisibility(View.GONE);

                    holder.basket_half_score.setVisibility(View.INVISIBLE);
                    holder.st_time.setText(R.string.basket_postpone);
                    holder.ongoing_time.setVisibility(View.GONE);
//					holder.backetball_apos.setVisibility(View.GONE);
                    settingdata(holder, -5);
                    break;
                default:
                    break;
            }
        }

        if (score > 0 && score < 8 && childredata.getMatchScore() != null && childredata.getMatchScore().getRemainTime() != null) {// 显示秒的闪烁
            holder.backetball_apos.setText("\'");
            holder.backetball_apos.setVisibility(View.VISIBLE);

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
                    holder.backetball_apos.startAnimation(anim2);
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
                    holder.backetball_apos.startAnimation(anim1);
                }
            });
            holder.backetball_apos.startAnimation(anim1);
            // }
        } else {
            holder.backetball_apos.setText("");
            holder.backetball_apos.setVisibility(View.VISIBLE);
//			holder.backetball_apos.cancelAnimation(R.id.backetball_apos);
        }

//        //启动 比分动画
//        if (childredata.getMatchScore() != null && score > 0 && score < 8) {
//            if (childredata.isHomeAnim()) {
//                scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
//                childredata.setIsHomeAnim(false);
//                holder.basket_home_all_score.setVisibility(View.VISIBLE);
//            }
//            if (childredata.isGuestAnim()) {
//                scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
//                childredata.setIsGuestAnim(false);
//                holder.basket_guest_all_score.setVisibility(View.VISIBLE);
//            }
//        }

        /**
         * 关注 监听
         */
        String focusIds = PreferenceUtil.getString("basket_focus_ids", "");
        String[] Ids = focusIds.split("[,]");
        holder.mIv_guangzhu.setBackgroundResource(R.mipmap.iconfont_guanzhu);
        holder.mIv_guangzhu.setTag(false);
        for (String id : Ids) {
            if (id.equals(childredata.getThirdId())) {
                holder.mIv_guangzhu.setBackgroundResource(R.mipmap.iconfont_guanzhu_hover);
                holder.mIv_guangzhu.setTag(true);
                break;
            }
        }
        holder.mIv_guangzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFocus != null) {
                    mFocus.FocusOnClick(v, childredata);
                }
            }
        });

        return convertView;
    }

    //设置启动动画
//    public void startAnima(BasketMatchBean childredata , int score , Holder holder){
//        //启动 比分动画
//        if (childredata.getMatchScore() != null && score > 0 && score < 8) {
//            if (childredata.isHomeAnim()) {
//                scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
//                childredata.setIsHomeAnim(false);
//            }else{
//                holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//            }
//            if (childredata.isGuestAnim()) {
//                scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
//                childredata.setIsGuestAnim(false);
//            }else{
//                holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//            }
//        }
//        else if (childredata.getMatchScore() != null){
//            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//        }
//    }

    /**
     * 是否允许显示最新的数字。
     */
    private boolean enableRefresh;
    //比分动画
    public void scoreAnimation(final TextView scoreView, final String score) {
        //翻转动画 （RotateAnimation-->自定义X轴翻转）
        enableRefresh = true;
        MyRotateAnimation rotateAnim = null;
        float cX = scoreView.getWidth() / 2.0f;
        float cY = scoreView.getHeight() / 2.0f;

        rotateAnim = new MyRotateAnimation(cX, cY, MyRotateAnimation.ROTATE_DECREASE);
            rotateAnim.setInterpolatedTimeListener(new MyRotateAnimation.InterpolatedTimeListener() {
                @Override
                public void interpolatedTime(float interpolatedTime) {
                    // 监听到翻转进度过半时，更新显示内容。
                    if (enableRefresh && interpolatedTime > 0.5f) {
                        scoreView.setText(score);
                        enableRefresh = false;
                    }
                }
            });
            rotateAnim.setFillAfter(true);
            scoreView.startAnimation(rotateAnim);

//        //创建动画 （位移动画--translate）
//        final Animation out = AnimationUtils.loadAnimation(mContext, R.anim.basket_out_to_up);//飞出
//        final Animation in = AnimationUtils.loadAnimation(mContext, R.anim.basket_in_from_down);//飞入
//        //启动飞出动画
//        scoreView.startAnimation(out);
//        out.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//            //飞出动画结束后启动飞入动画
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                scoreView.setText(score);
//                scoreView.startAnimation(in);
//            }
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
    }

    //设置界面score
    public void settingdata(Holder holder, int score){

        boolean fullscore = PreferenceUtil.getBoolean(MyConstants.HALF_FULL_SCORE, true); //半全场比分
        boolean differscore = PreferenceUtil.getBoolean(MyConstants.SCORE_DIFFERENCE, true);//总分差
        boolean singscore = PreferenceUtil.getBoolean(MyConstants.SINGLE_SCORE, true); //单节比分
        boolean ranking = PreferenceUtil.getBoolean(MyConstants.HOST_RANKING, true);//排名

        //Integer score = Integer.parseInt(data.getMatchStatus());

        //0:未开赛,1:一节,2:二节,5:1'OT，以此类推 -1:完场,-2:待定,-3:中断,-4:取消,-5:推迟,50中场
        if (fullscore) {

            if (score == 0 || score == -2 || score == -3 || score == -4 || score == -5) {
//                holder.basket_guest_all_score.setVisibility(View.GONE);
//                holder.basket_score.setVisibility(View.VISIBLE);
//                holder.basket_home_all_score.setVisibility(View.GONE);
                holder.backetball_total.setVisibility(View.GONE);
                holder.score_total.setVisibility(View.GONE);

            } else {
//                holder.basket_guest_all_score.setVisibility(View.VISIBLE);
//                holder.basket_score.setVisibility(View.VISIBLE);
//                holder.basket_home_all_score.setVisibility(View.VISIBLE);
                holder.backetball_total.setVisibility(View.VISIBLE);
                holder.score_total.setVisibility(View.VISIBLE);
            }
        } else {
//            holder.basket_guest_all_score.setVisibility(View.INVISIBLE);
//            holder.basket_score.setVisibility(View.INVISIBLE);
//            holder.basket_home_all_score.setVisibility(View.INVISIBLE);
            holder.backetball_total.setVisibility(View.GONE);
            holder.score_total.setVisibility(View.GONE);
        }
        if (differscore) {

            if (score == 0 || score == -2 || score == -3 || score == -4 || score == -5) {
                holder.backetball_differ.setVisibility(View.INVISIBLE);
                holder.score_differ.setVisibility(View.INVISIBLE);
            } else {
                holder.backetball_differ.setVisibility(View.VISIBLE);
                holder.score_differ.setVisibility(View.VISIBLE);
            }
        } else {
            holder.backetball_differ.setVisibility(View.INVISIBLE);
            holder.score_differ.setVisibility(View.INVISIBLE);
        }
        if (singscore) {
            if (score == 0 || score == -2 || score == -3 || score == -4 || score == -5 || score == -1 || score == 50) {
                holder.basket_half_score.setVisibility(View.INVISIBLE);
            } else {
                holder.basket_half_score.setVisibility(View.VISIBLE);
            }
        } else {
            holder.basket_half_score.setVisibility(View.INVISIBLE);
        }
        if (ranking) {
            holder.guest_Ranking.setVisibility(View.VISIBLE);
            holder.home_Ranking.setVisibility(View.VISIBLE);
        } else {
            holder.guest_Ranking.setVisibility(View.INVISIBLE);
            holder.home_Ranking.setVisibility(View.INVISIBLE);
        }
    }

//    /**
//     * Volley_ImageLoader 加载图片
//     */
//    private void initImageLoader(final String url, final ImageView imageView) {
//        final int maxWidth = -1;
//        final int maxHeight = -1;
//        mImageLoader.get(url, new ImageLoader.ImageListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
////                System.out.println("error:" + error.getMessage());
//                //mImageCache.putBitmap();
//                if (imageView.getTag().equals(url)) {
////                    imageView.setMinimumHeight(40);
////                    imageView.setMinimumWidth(40);
////                    imageView.setBackgroundResource(R.mipmap.basket_default);
//                    imageView.setImageResource(R.mipmap.basket_default);
//                    mImageCache.putBitmap(url, mDefualtImg);
//                }
//            }
//
//            @Override
//            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//                L.d("____initImageLoader_____");
//                Bitmap bitmap = response.getBitmap();
////                mImageCache.putBitmap(url , bitmap);
//                if (imageView.getTag().equals(url)) {
////                    imageView.setBackgroundColor(Color.WHITE);
////                    imageView.setMinimumHeight(40);
////                    imageView.setMinimumWidth(40);
//                    imageView.setImageBitmap(bitmap);
//                }
//            }
//        }, maxWidth, maxHeight);
//    }

    public class Holder {

        ImageView home_icon; //主队图标
        ImageView guest_icon; //客队图标

        ImageView mIv_guangzhu;//关注 星星

        TextView matches_name; //联赛名称
        TextView game_time; //比赛时间
        TextView st_time; //第几节
        TextView ongoing_time; // 单节时间

        TextView score_total;// 半全场比分
        TextView score_differ;//比分差

        TextView home_name;// 主队名称
        TextView guest_name;//客队名称

        TextView home_Ranking; //主队赛区
        TextView guest_Ranking; //客队赛区

        TextView basket_guest_all_score;
        TextView basket_score;
        TextView basket_home_all_score;

        TextView basket_half_score;// 主客队半场比分

        TextView basket_leftOdds;// 赔率 左
        TextView basket_rightOdds;// 赔率 右
        TextView basket_handicap;// 赔率盘口

        TextView backetball_total;//总
        TextView backetball_differ;//差

        TextView tv_a; // 赔率分割线
        TextView tv_b;

        TextView backetball_apos; // 秒针

    }

    /**
     * 获取指定组中的子元素个数
     *
     * @param groupPosition 组位置（决定返回哪个组的子元素个数）
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition<0){
            return 0;
        }

        return childrenDataList.get(groupPosition).size();
    }

    /**
     * 获取指定组中的数据
     *
     * @param groupPosition 组位置
     * @return 返回组中的数据，也就是该组中的子元素数据
     */
    @Override
    public Object getGroup(int groupPosition) {
        return groupDataList.get(groupPosition);
    }

    /**
     * 获取组的个数
     *
     * @return 组的个数
     */
    @Override
    public int getGroupCount() {
        return groupDataList.size();
    }

    /**
     * 获取指定组的ID，这个组ID必须是唯一的。联合ID(参见getCombinedGroupId(long))在所有条目(所有组和所有元素)中也是唯一的
     *
     * @param groupPosition
     * @return
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    /**
     * 重写  getGroupView 方法    实现箭头在右边显示
     *
     * @param groupPosition 组位置（决定返回哪个视图）
     * @param isExpanded    该组是展开状态还是伸缩状态
     * @param convertView   重用已有的视图对象。
     * @param parent        返回的视图对象始终依附于的视图组。
     * @return 返回指定组的视图对象
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String tittledata = groupDataList.get(groupPosition); //"2016-1-13,周三,0"
        String[] weekdatas = tittledata.split(",");

        RelativeLayout parentLayout = (RelativeLayout) View.inflate(mContext, R.layout.basketball_riqi_head, null);
        TextView parentTextView = (TextView) parentLayout.findViewById(R.id.basketball_riqi);
        TextView parentTextView1 = (TextView) parentLayout.findViewById(R.id.basketball_xingqi);
        TextView parentTextView2 = (TextView) parentLayout.findViewById(R.id.basketball_current_day);
        //设置 外层数据
//		parentTextView.setText("******* "+groupDataList.get(groupPosition));
        parentTextView.setText(weekdatas[0]);
        parentTextView1.setText(weekdatas[1]);

        if (weekdatas[2].equals("0")) {
            parentTextView2.setText(R.string.basket_today);
            parentTextView2.setVisibility(View.VISIBLE);
        }else if (weekdatas[2].equals("-1")){
            parentTextView2.setText(R.string.basket_yesterday);
            parentTextView2.setVisibility(View.VISIBLE);
        }else if (weekdatas[2].equals("1")) {
            parentTextView2.setText(R.string.basket_monday);
            parentTextView2.setVisibility(View.VISIBLE);
        }else{
            parentTextView2.setVisibility(View.GONE);
        }
//        switch (isToday.get(groupPosition)){
//            case -1:
//                parentTextView2.setText(R.string.basket_yesterday);
//                parentTextView2.setVisibility(View.VISIBLE);
//                break;
//            case 0:
//                parentTextView2.setText(R.string.basket_today);
//                parentTextView2.setVisibility(View.VISIBLE);
//                break;
//            case 1:
//                parentTextView2.setText(R.string.basket_monday);
//                parentTextView2.setVisibility(View.VISIBLE);
//                break;
//            default:
////                parentTextView2.setText("");
//                parentTextView2.setVisibility(View.GONE);
//                break;
//        }
        ImageView parentImageViw = (ImageView) parentLayout.findViewById(R.id.basketball_details_open);
        //判断isExpanded就可以控制是按下还是关闭，同时更换图片
        if (isExpanded) {
            parentImageViw.setBackgroundResource(R.mipmap.iconfont_xiala_2);
        } else {
            parentImageViw.setBackgroundResource(R.mipmap.iconfont_xiala_1);
        }
        return parentLayout;
    }

    /**
     * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
     *
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 是否选中指定位置上的子元素。
     *
     * @param groupPosition 组位置（该组内部含有这个子元素）
     * @param childPosition 子元素位置
     * @return 是否选中子元素
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private View createChildrenView() {
        return inflater.inflate(R.layout.basketball_details_item, null);
    }

    private View createGroupView() {
        return inflater.inflate(R.layout.basketball_riqi_head, null);
    }

    @Override
    public int getHeaderState(int groupPosition, int childPosition) {
        final int childCount = getChildrenCount(groupPosition);
        if (childPosition == childCount - 1) {
            return PINNED_HEADER_PUSHED_UP;
        } else if (childPosition == -1
                && !listView.isGroupExpanded(groupPosition)) {
            return PINNED_HEADER_GONE;
        } else {
            return PINNED_HEADER_VISIBLE;
        }
    }

    /**
     * 设置置顶的  item数据
     */
    @Override
    public void configureHeader(View header, int groupPosition,int childPosition, int alpha) {

        TextView riqi_textview = (TextView) header.findViewById(R.id.basketball_riqi);
        TextView xingqi_textview = (TextView) header.findViewById(R.id.basketball_xingqi);
        TextView current_day_textview = (TextView) header.findViewById(R.id.basketball_current_day);

        String data = groupDataList.get(groupPosition); //"2016-1-13,周三,0"
        String[] weekdata = data.split(",");

        riqi_textview.setText(weekdata[0]);
        xingqi_textview.setText(weekdata[1]);

        if (weekdata[2].equals("0")) {
            current_day_textview.setText(R.string.basket_today);
            current_day_textview.setVisibility(View.VISIBLE);
        }else if (weekdata[2].equals("-1")){
            current_day_textview.setText(R.string.basket_yesterday);
            current_day_textview.setVisibility(View.VISIBLE);
        }else if (weekdata[2].equals("1")) {
            current_day_textview.setText(R.string.basket_monday);
            current_day_textview.setVisibility(View.VISIBLE);
        }else{
            current_day_textview.setVisibility(View.GONE);
        }


//        switch (isToday.get(groupPosition)){
//            case -1:
//                current_day_textview.setText(R.string.basket_yesterday);
//                current_day_textview.setVisibility(View.VISIBLE);
//                break;
//            case 0:
//                current_day_textview.setText(R.string.basket_today);
//                current_day_textview.setVisibility(View.VISIBLE);
//                break;
//            case 1:
//                current_day_textview.setText(R.string.basket_monday);
//                current_day_textview.setVisibility(View.VISIBLE);
//                break;
//            default:
////                current_day_textview.setText("");
//                current_day_textview.setVisibility(View.GONE);//直接设置 "" ,会出现复用问题，滑动到底部回滑时 “昨日、今日”等字不显示
//                break;
//        }
    }

    private SparseIntArray groupStatusMap = new SparseIntArray();

    /**
     * 设置组按下的状态
     * @param groupPosition
     * @param status    0 收起  1 展开
     */
    @Override
    public void setGroupClickStatus(int groupPosition, int status) {
        groupStatusMap.put(groupPosition, status);
    }

    @Override
    public int getGroupClickStatus(int groupPosition) {
        if (groupStatusMap.keyAt(groupPosition) >= 0) {
            return groupStatusMap.get(groupPosition);
        } else {
            return 0;
        }
    }

}
