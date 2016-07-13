package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballAnalyzeDetailsActivity;
import com.hhly.mlottery.activity.FootballInformationActivity;
import com.hhly.mlottery.bean.footballDetails.NewAnalyzeBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  足球分析界面
 *  Created by madongyun 155
 *  date :2016-06-13
 */
public class AnalyzeFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private DisplayImageOptions mOptions;
    private ImageLoader mImageLoader;

    private View mView;
    private Context mContext;// 上下文对象


    /**
     * 历史交锋
     */
    private ProgressBar mProgressBar;
    private TextView mProgressHomeWin;
    private TextView mProgressGuestWin;
    /**
     * 近期战绩
     */
    private ImageView mHomeRecent1;
    private ImageView mHomeRecent2;
    private ImageView mHomeRecent3;
    private ImageView mHomeRecent4;
    private ImageView mHomeRecent5;
    private ImageView mHomeRecent6;

    private ImageView mGuestRecent1;
    private ImageView mGuestRecent2;
    private ImageView mGuestRecent3;
    private ImageView mGuestRecent4;
    private ImageView mGuestRecent5;
    private ImageView mGuestRecent6;
    //未来比赛
    private LinearLayout mLinearFutureMatch;
    private TextView mFutureNodata;
    /**主队未来比赛距离天数*/
    private TextView mHomeFutureDate;
    private TextView mHomeFutureName;
    private ImageView mHomeFutureLogo;

    private TextView mGuestFutureDate;
    private TextView mGuestFutureName;
    private ImageView mGuestFutureLogo;
    /**更多战绩*/
    private TextView mTextMoreGame;

    //积分排名
    private LinearLayout mLinearRanking;
    private TextView mRankingNodata;
    private TextView mHomeRank;
    private TextView mHomeHasGame;
    private TextView mHomeWinOrLose;
    private TextView mHomeGoalOrLose;
    private TextView mHomeGoalDifference;
    private TextView mHomeIntegral;//积分

    private TextView mGuestRank;
    private TextView mGuestHasGame;
    private TextView mGuestWinOrLose;
    private TextView mGuestGoalOrLose;
    private TextView mGuestGoalDifference;
    private TextView mGuestIntegral;//积分
    //完整积分榜
    private TextView mIntegralTable;

    //攻防对比
    private LinearLayout mLinearAttack;
    private TextView mAttackNodata;
    private TextView mHomeGoal;
    private TextView mHomeLose;
    private TextView mGuestGoal;
    private TextView mGuestLose;
    private TextView mSizeOfBet;
    //球员信息
    private TextView mHomeTeamName;
    private TextView mGuestTeamName;
    private LinearLayout ll_rosters_homeTeam;// 主队名单容器
    private LinearLayout ll_rosters_visitingTeam;// 客队名单容器
    private FrameLayout fl_firsPlayers_not;// 暂无首发容器
    private LinearLayout fl_firsPlayers_content;// 首发内容容器

    // TODO: Rename and change types of parameters
    private String mThirdId="1111";

    private NewAnalyzeBean mAnalyzeBean;
    public AnalyzeFragment() {
        // Required empty public constructor
    }


    public static AnalyzeFragment newInstance(String param1, String param2,String param3) {
        AnalyzeFragment fragment = new AnalyzeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThirdId = getArguments().getString(ARG_PARAM1);
        }
        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                        //  .showImageOnLoading(R.mipmap.basket_default)//加上这句的话会导致刷新时闪烁
                .showImageForEmptyUri(R.mipmap.football_analyze_default)
                .showImageOnFail(R.mipmap.football_analyze_default)// 加载失败显示的图片
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
        mImageLoader = ImageLoader.getInstance(); //初始化
        mImageLoader.init(config);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_analyze_fragment, container, false);
        mAnalyzeBean=new NewAnalyzeBean();
        initView();
        initData();
        setListener();

        return mView;
}

    private void initView() {
        mProgressBar = (ProgressBar) mView.findViewById(R.id.football_analyze__progressbar);
        mProgressHomeWin= (TextView) mView.findViewById(R.id.football_progressbar_home);
        mProgressGuestWin= (TextView) mView.findViewById(R.id.football_progressbar_guest);
        //近期比赛
        mHomeRecent1= (ImageView) mView.findViewById(R.id.football_img_recent_home1);
        mHomeRecent2= (ImageView) mView.findViewById(R.id.football_img_recent_home2);
        mHomeRecent3= (ImageView) mView.findViewById(R.id.football_img_recent_home3);
        mHomeRecent4= (ImageView) mView.findViewById(R.id.football_img_recent_home4);
        mHomeRecent5= (ImageView) mView.findViewById(R.id.football_img_recent_home5);
        mHomeRecent6= (ImageView) mView.findViewById(R.id.football_img_recent_home6);

        mGuestRecent1= (ImageView) mView.findViewById(R.id.football_img_recent_guest1);
        mGuestRecent2= (ImageView) mView.findViewById(R.id.football_img_recent_guest2);
        mGuestRecent3= (ImageView) mView.findViewById(R.id.football_img_recent_guest3);
        mGuestRecent4= (ImageView) mView.findViewById(R.id.football_img_recent_guest4);
        mGuestRecent5= (ImageView) mView.findViewById(R.id.football_img_recent_guest5);
        mGuestRecent6= (ImageView) mView.findViewById(R.id.football_img_recent_guest6);
        //未来比赛
        mLinearFutureMatch= (LinearLayout) mView.findViewById(R.id.ll_football_analyze_future);
        mFutureNodata= (TextView) mView.findViewById(R.id.football_analyze_nodata1);
        mHomeFutureDate= (TextView) mView.findViewById(R.id.football_home_future_date);
        mHomeFutureName= (TextView) mView.findViewById(R.id.football_home_future_name);
        mHomeFutureLogo= (ImageView) mView.findViewById(R.id.football_home_future_logo);
        mGuestFutureDate= (TextView) mView.findViewById(R.id.football_guest_future_date);
        mGuestFutureName= (TextView) mView.findViewById(R.id.football_guest_future_name);
        mGuestFutureLogo= (ImageView) mView.findViewById(R.id.football_guest_future_logo);
        //更多战绩
        mTextMoreGame= (TextView) mView.findViewById(R.id.football_analyze_more_record);
        //积分榜
        mLinearRanking= (LinearLayout) mView.findViewById(R.id.ll_football_analyze_ranking);
        mRankingNodata= (TextView) mView.findViewById(R.id.football_analyze_nodata2);
        mHomeRank= (TextView) mView.findViewById(R.id.football_analyze__home_rank);
        mHomeHasGame= (TextView) mView.findViewById(R.id.football_analyze_home_hadgame);
        mHomeWinOrLose= (TextView) mView.findViewById(R.id.football_analyze_home_win_or_lose);
        mHomeGoalOrLose= (TextView) mView.findViewById(R.id.football_analyze_home_goal_or_lose);
        mHomeGoalDifference= (TextView) mView.findViewById(R.id.football_analyze_home_goal_difference);
        mHomeIntegral= (TextView) mView.findViewById(R.id.football_analyze_home_integral);

        mGuestRank= (TextView) mView.findViewById(R.id.football_analyze__guest_rank);
        mGuestHasGame= (TextView) mView.findViewById(R.id.football_analyze_guest_hadgame);
        mGuestWinOrLose= (TextView) mView.findViewById(R.id.football_analyze_guest_win_or_lose);
        mGuestGoalOrLose= (TextView) mView.findViewById(R.id.football_analyze_guest_goal_or_lose);
        mGuestGoalDifference= (TextView) mView.findViewById(R.id.football_analyze_guest_goal_difference);
        mGuestIntegral= (TextView) mView.findViewById(R.id.football_analyze_guest_integral);
        //完整积分榜
        mIntegralTable= (TextView) mView.findViewById(R.id.football_analyze_integral_table);
        //攻防对比
        mLinearAttack= (LinearLayout) mView.findViewById(R.id.ll_football_analyze_attack);
        mAttackNodata= (TextView) mView.findViewById(R.id.football_analyze_nodata3);
        mHomeGoal= (TextView) mView.findViewById(R.id.home_goal);
        mHomeLose= (TextView) mView.findViewById(R.id.home_lose);
        mGuestGoal= (TextView) mView.findViewById(R.id.guest_goal);
        mGuestLose= (TextView) mView.findViewById(R.id.guest_lose);
        mSizeOfBet= (TextView) mView.findViewById(R.id.football_analyze_size_of_bet);
        //球员信息
        mHomeTeamName= (TextView) mView.findViewById(R.id.lineup_home_team);
        mGuestTeamName= (TextView) mView.findViewById(R.id.lineup_guest_team);
//        mListView= (NestedListView) mView.findViewById(R.id.listview_player_message);
        ll_rosters_homeTeam = (LinearLayout) mView.findViewById(R.id.ll_rosters_homeTeam);
        ll_rosters_visitingTeam = (LinearLayout) mView.findViewById(R.id.ll_rosters_visitingTeam);
        fl_firsPlayers_not = (FrameLayout) mView.findViewById(R.id.fl_firsPlayers_not);
        fl_firsPlayers_content = (LinearLayout) mView.findViewById(R.id.fl_firsPlayers_content);
    }

    public void initData() {
        Map<String ,String > params=new HashMap<>();
      //  params.put("thirdId","337367");
        params.put("thirdId",mThirdId);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_NEW_ANALYZE,params,new VolleyContentFast.ResponseSuccessListener<NewAnalyzeBean>() {
            @Override
            public void onResponse(NewAnalyzeBean analyzeBean) {
                if (analyzeBean.getResult().equals("200")) {
                    mAnalyzeBean=analyzeBean;
                    loadData(mAnalyzeBean);
                }
                else{
                    mLinearRanking.setVisibility(View.GONE);
                    mRankingNodata.setVisibility(View.VISIBLE);
                    mLinearAttack.setVisibility(View.GONE);
                    mAttackNodata.setVisibility(View.VISIBLE);
                    fl_firsPlayers_not.setVisibility(View.VISIBLE);
                    fl_firsPlayers_content.setVisibility(View.GONE);
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mLinearRanking.setVisibility(View.GONE);
                mRankingNodata.setVisibility(View.VISIBLE);
                mLinearAttack.setVisibility(View.GONE);
                mAttackNodata.setVisibility(View.VISIBLE);
                fl_firsPlayers_not.setVisibility(View.VISIBLE);
                fl_firsPlayers_content.setVisibility(View.GONE);
            }
        }, NewAnalyzeBean.class);

    }
    /**
     * 设置监听事件
     */
    private void setListener() {
        mTextMoreGame.setOnClickListener(this);
        mIntegralTable.setOnClickListener(this);
    }


    /**
     * 加载数据
     */
    private void loadData(NewAnalyzeBean analyzeBean){
        int progress;
        if(analyzeBean.getBothRecord()!=null&&analyzeBean.getBothRecord().getHome()!=null&&getActivity()!=null){

            int homeWin=analyzeBean.getBothRecord().getHome().getHistoryWin();
            int guestWin=analyzeBean.getBothRecord().getGuest().getHistoryWin();
            if (homeWin == 0 && guestWin == 0) {
                progress = 50;
            } else if (homeWin == 0) {
                progress = 0;
            } else if (guestWin == 0) {
                progress = 100;
            } else {
                progress = homeWin * 100 / (guestWin+homeWin);
            }
            mProgressBar.setProgress(progress);
            mProgressHomeWin.setText(homeWin + getActivity().getResources().getString(R.string.analyze_win));
            mProgressGuestWin.setText(guestWin+ getActivity().getResources().getString(R.string.analyze_win));
        }

        //近期战绩
        if(analyzeBean.getBothRecord()!=null&&analyzeBean.getBothRecord().getHome()!=null){
            List<Integer> homeRecent=analyzeBean.getBothRecord().getHome().getRecentRecord();
            if(homeRecent.size()!=0){
                setRecent(mHomeRecent1,homeRecent.get(0));
                setRecent(mHomeRecent2,homeRecent.get(1));
                setRecent(mHomeRecent3,homeRecent.get(2));
                setRecent(mHomeRecent4,homeRecent.get(3));
                setRecent(mHomeRecent5,homeRecent.get(4));
                setRecent(mHomeRecent6,homeRecent.get(5));
            }
        }
        if(analyzeBean.getBothRecord()!=null&&analyzeBean.getBothRecord().getGuest()!=null){
            List<Integer> guestRecent=analyzeBean.getBothRecord().getGuest().getRecentRecord();

            if(guestRecent.size()!=0){
                setRecent(mGuestRecent1,guestRecent.get(0));
                setRecent(mGuestRecent2,guestRecent.get(1));
                setRecent(mGuestRecent3,guestRecent.get(2));
                setRecent(mGuestRecent4,guestRecent.get(3));
                setRecent(mGuestRecent5,guestRecent.get(4));
                setRecent(mGuestRecent6,guestRecent.get(5));
            }
        }

        //未来三场
        if(analyzeBean.getBothRecord()!=null&&analyzeBean.getBothRecord().getHome()!=null&&analyzeBean.getBothRecord().getHome().getFutureMatch()!=null&&getActivity()!=null){
            mHomeFutureDate.setText(analyzeBean.getBothRecord().getHome().getFutureMatch().getDiffDays()+getActivity().getResources().getString(R.string.number_hk_dd));
            mHomeFutureName.setText(analyzeBean.getBothRecord().getHome().getFutureMatch().getTeam());
            mImageLoader.displayImage(analyzeBean.getBothRecord().getHome().getFutureMatch().getLogoUrl(), mHomeFutureLogo, mOptions);
        }
        if(analyzeBean.getBothRecord()!=null&&analyzeBean.getBothRecord().getGuest()!=null&&analyzeBean.getBothRecord().getGuest().getFutureMatch()!=null&&getActivity()!=null){
            mGuestFutureDate.setText(analyzeBean.getBothRecord().getGuest().getFutureMatch().getDiffDays() + getActivity().getResources().getString(R.string.number_hk_dd));
            mGuestFutureName.setText(analyzeBean.getBothRecord().getGuest().getFutureMatch().getTeam());
            mImageLoader.displayImage(analyzeBean.getBothRecord().getGuest().getFutureMatch().getLogoUrl(),mGuestFutureLogo,mOptions);
        }

        //积分排名
        if(analyzeBean.getScoreRank().getHome()==null&&analyzeBean.getScoreRank().getGuest()==null){
            mLinearRanking.setVisibility(View.GONE);
            mRankingNodata.setVisibility(View.VISIBLE);
        }
        else{
            mLinearRanking.setVisibility(View.VISIBLE);
            mRankingNodata.setVisibility(View.GONE);
            NewAnalyzeBean.ScoreRankEntity entity=analyzeBean.getScoreRank();
            if(entity.getHome()!=null){
                mHomeRank.setText(entity.getHome().getRank()+entity.getHome().getTeam());
                mHomeHasGame.setText(entity.getHome().getVsCount()+"");
                mHomeWinOrLose.setText(entity.getHome().getWin()+"/"+entity.getHome().getDraw()+"/"+entity.getHome().getLose());
                mHomeGoalOrLose.setText(entity.getHome().getGoal()+"/"+entity.getHome().getMiss());
                mHomeGoalDifference.setText(entity.getHome().getGoalDiff()+"");
                mHomeIntegral.setText(entity.getHome().getIntegral() + "");

            }
            if(entity.getGuest()!=null){
                mGuestRank.setText(entity.getGuest().getRank()+entity.getGuest().getTeam());
                mGuestHasGame.setText(entity.getGuest().getVsCount()+"");
                mGuestWinOrLose.setText(entity.getGuest().getWin()+"/"+entity.getGuest().getDraw()+"/"+entity.getGuest().getLose());
                mGuestGoalOrLose.setText(entity.getGuest().getGoal()+"/"+entity.getGuest().getMiss());
                mGuestGoalDifference.setText(entity.getGuest().getGoalDiff()+"");
                mGuestIntegral.setText(entity.getGuest().getIntegral()+"");


            }

        }
//        //球员信息
//        mHomeTeamName.setText(mHomeName);
//        mGuestTeamName.setText(mGuestName);

        //是否显示积分榜
        if(analyzeBean.getFullScoreRank()==1){ //有完整积分榜
            mIntegralTable.setVisibility(View.VISIBLE);
        }else if(analyzeBean.getFullScoreRank()==0){
            mIntegralTable.setVisibility(View.GONE);
        }

        NewAnalyzeBean.AttackDefenseEntity defense=analyzeBean.getAttackDefense();
        //攻防对比
        if(defense.getGuestFieldGoal()==null&&defense.getGuestFieldLose()==null&&defense.getHomeFieldGoal()==null&&defense.getHomeFieldLose()==null){
            mLinearAttack.setVisibility(View.GONE);
            mAttackNodata.setVisibility(View.VISIBLE);
        }
        else{
            mLinearAttack.setVisibility(View.VISIBLE);
            mAttackNodata.setVisibility(View.GONE);
            mHomeGoal.setText(analyzeBean.getAttackDefense().getHomeFieldGoal());
            mHomeLose.setText(analyzeBean.getAttackDefense().getHomeFieldLose());
            mGuestGoal.setText(analyzeBean.getAttackDefense().getGuestFieldGoal());
            mGuestLose.setText(analyzeBean.getAttackDefense().getGuestFieldLose());
            mSizeOfBet.setText(analyzeBean.getAttackDefense().getSizeHandicap()==null?"--":analyzeBean.getAttackDefense().getSizeHandicap());
        }

        List<NewAnalyzeBean.LineUpEntity.PlayerInfo> homeLineUpList=analyzeBean.getLineUp().getHomeLineUp();//主队队员
        List<NewAnalyzeBean.LineUpEntity.PlayerInfo> guestLineUpList=analyzeBean.getLineUp().getGuestLineUp();//客队队员
//        List<NewAnalyzeBean.LineUpEntity.PlayerInfo> homeLineUpList=new ArrayList<>();//主队队员
//        List<NewAnalyzeBean.LineUpEntity.PlayerInfo> guestLineUpList=new ArrayList<>();//客队队员
//        for(int i=0;i<11;i++){
//            homeLineUpList.add(new NewAnalyzeBean.LineUpEntity.PlayerInfo("梅西"+i));
//        }
//        for(int i=0;i<11;i++){
//            guestLineUpList.add(new NewAnalyzeBean.LineUpEntity.PlayerInfo("C罗"+i));
//        }

        if(getActivity()!=null){
            mContext=getActivity();
            if (homeLineUpList != null && guestLineUpList != null) {
                if (homeLineUpList.size() > 0) {
                    // 显示首发内容
                    fl_firsPlayers_not.setVisibility(View.GONE);
                    fl_firsPlayers_content.setVisibility(View.VISIBLE);

                    int dip5 = DisplayUtil.dip2px(mContext, 5);
                    int dip10 = DisplayUtil.dip2px(mContext, 10);

                    if(ll_rosters_homeTeam.getChildCount()!=0){  //刷新时清空上次add的textview
                        ll_rosters_homeTeam.removeAllViews();
                    }
                    if(ll_rosters_visitingTeam.getChildCount()!=0){
                        ll_rosters_visitingTeam.removeAllViews();
                    }
                    // 添加主队名单
                    for (int i = 0, len = homeLineUpList.size(); i < len; i++) {
                        TextView tv_homeTeams = new TextView(mContext);
                        tv_homeTeams.setText(homeLineUpList.get(i).getName());
                        if (i == 0) {
                            tv_homeTeams.setPadding(dip5, 0, 0, dip10);
                        } else {
                            tv_homeTeams.setPadding(dip5, 0, 0, dip10);
                        }
                        tv_homeTeams.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
                        ll_rosters_homeTeam.addView(tv_homeTeams);
                    }

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.RIGHT;// 设置靠右对齐
                    // 添加客队名单
                    for (int i = 0, len = guestLineUpList.size(); i < len; i++) {
                        TextView tv_visitingTeams = new TextView(mContext);
                        tv_visitingTeams.setText(guestLineUpList.get(i).getName());
                        tv_visitingTeams.setLayoutParams(params);
                        if (i == 0) {
                            tv_visitingTeams.setPadding(0, 0, dip5, dip10);
                        } else {
                            tv_visitingTeams.setPadding(0, 0, dip5, dip10);
                        }
                        tv_visitingTeams.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
                        ll_rosters_visitingTeam.addView(tv_visitingTeams);
                    }
                    return;
                }
            }
            // 显示暂无首发提示
            fl_firsPlayers_not.setVisibility(View.VISIBLE);
            fl_firsPlayers_content.setVisibility(View.GONE);
        }

    }

    /**
     * 设置队员信息的主客队队名
     * @param home
     * @param guest
     */
    public void setTeamName(String home,String guest){
        mHomeTeamName.setText(home);
        mGuestTeamName.setText(guest);
    }
    /**
     * 设置近期战绩图片  胜平负
     * @param mImage
     * @param recent
     */
    private void setRecent(ImageView mImage, int recent) {
        if(recent==0){ //平
            mImage.setBackgroundResource(R.mipmap.basket_draw);
        }
        if (recent == 2) { //输
            mImage.setBackgroundResource(R.mipmap.basket_lose);
        }
        if (recent == 1) { //赢
            mImage.setBackgroundResource(R.mipmap.basket_win);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.football_analyze_more_record:
                Intent intent=new Intent(getActivity(),FootballAnalyzeDetailsActivity.class);
                intent.putExtra(FootballAnalyzeDetailsActivity.FOOTBALL_ANALYZE_THIRD_ID,mThirdId);
                startActivity(intent);
                break;
            case R.id.football_analyze_integral_table:
                Intent intent1=new Intent(getActivity(), FootballInformationActivity.class);
                intent1.putExtra("lid",mAnalyzeBean.getLeagueId()+"");
                intent1.putExtra("leagueType",mAnalyzeBean.getLeagueType()+"");
                startActivity(intent1);
                break;
        }
    }
}
