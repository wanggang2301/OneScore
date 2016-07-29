package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.MatchDetail;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.bean.footballDetails.MatchTimeLiveBean;
import com.hhly.mlottery.bean.footballDetails.MathchStatisInfo;
import com.hhly.mlottery.util.FootballEventComparator;
import com.hhly.mlottery.util.StadiumUtils;
import com.hhly.mlottery.widget.FootballEventView;
import com.hhly.mlottery.widget.TimeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang gang
 * @date 2016/6/2 18:34
 * @des 足球内页改版头部viewpager第二页
 */
public class LiveHeadInfoFragment extends Fragment {

    private static final String LIVEHEADINFO_PARAM = "LIVEHEADINFO_PARAM";
    private View mView;

    private TextView mTvTime;

    private TextView tv_home_corner, tv_home_rc, tv_home_yc, tv_home_danger, tv_home_shoot_correct, tv_home_shoot_miss, tv_home_attack;
    private TextView tv_guest_corner, tv_guest_rc, tv_guest_yc, tv_guest_danger, tv_guest_shoot_correct, tv_guest_shoot_miss, tv_guest_attack;
    private TextView tv_frequency;
    private ProgressBar pb_danger, pb_shoot_correct, pb_shoot_miss, pb_attack;

    private TextView score;

    private TextView homename;

    private TextView guestname;

    private Context mContext;
    /**
     * 赛事所有信息
     */
    private MatchDetail mMatchDetail;

    /**
     * 所有直播的list
     */
    private List<MatchTextLiveBean> matchLive;

    /**
     * 时间轴的集合
     */
    private List<MatchTimeLiveBean> xMatchLive = new ArrayList<>();

    //时间轴事件
    private static final String SCORE = "1029";//主队进球
    private static final String RED_CARD = "1032";
    private static final String POINT = "point";//点球进球数据没有
    private static final String YELLOW_CARD = "1034";
    private static final String SUBSTITUTION = "1055";
    private static final String CORNER = "1025";
    private static final String YTORED = "1045";//两黄变一红
    //客队事件
    private static final String SCORE1 = "2053";//客队进球
    private static final String RED_CARD1 = "2056";
    private static final String POINT1 = "point";//点球进球数据没有
    private static final String YELLOW_CARD1 = "2058";
    private static final String SUBSTITUTION1 = "2079";
    private static final String CORNER1 = "2049";
    private static final String YTORED1 = "2069";//两黄变一红
    private static final String FIRSTOVER = "1";

    /**
     * 未开
     */
    private static final String NOTOPEN = "0";
    /**
     * 上半场
     */
    private static final String FIRSTHALF = "1";
    /**
     * 中场
     */
    private static final String HALFTIME = "2";
    /**
     * 下半场
     */
    private static final String SECONDHALF = "3";
    /**
     * 完场
     **/
    private static final String MATCHFINISH = "-1";

    /**
     * 比赛时间
     */
    //  private String mKeepTime;

    //时间轴的view
    private TimeView timeView;
    private FootballEventView timeLayoutTop;
    private FootballEventView timeLayoutBottom;


    public static LiveHeadInfoFragment newInstance() {
        LiveHeadInfoFragment fragment = new LiveHeadInfoFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_live_headinfo, container, false);
        if (getArguments() != null) {
            mMatchDetail = getArguments().getParcelable(LIVEHEADINFO_PARAM);
        }
        this.mContext = getActivity();
        initView();
        return mView;
    }

    private void initView() {
        mTvTime = (TextView) mView.findViewById(R.id.keepTime);
        homename = (TextView) mView.findViewById(R.id.home_name);
        guestname = (TextView) mView.findViewById(R.id.guest_name);
        tv_home_corner = (TextView) mView.findViewById(R.id.tv_home_corner);
        tv_home_rc = (TextView) mView.findViewById(R.id.tv_home_rc);
        tv_home_yc = (TextView) mView.findViewById(R.id.tv_home_yc);
        score = (TextView) mView.findViewById(R.id.score);
        tv_home_danger = (TextView) mView.findViewById(R.id.home_danger_attack);
        tv_home_shoot_correct = (TextView) mView.findViewById(R.id.home_shoot_correct);
        tv_home_shoot_miss = (TextView) mView.findViewById(R.id.home_shoot_miss);
        tv_guest_corner = (TextView) mView.findViewById(R.id.tv_guest_corner);
        tv_guest_rc = (TextView) mView.findViewById(R.id.tv_guest_rc);
        tv_guest_yc = (TextView) mView.findViewById(R.id.tv_guest_yc);
        tv_guest_danger = (TextView) mView.findViewById(R.id.guest_danger_attack);
        tv_guest_shoot_correct = (TextView) mView.findViewById(R.id.guest_shoot_correct);
        tv_guest_shoot_miss = (TextView) mView.findViewById(R.id.guest_shoot_miss);
        tv_home_attack = (TextView) mView.findViewById(R.id.home_attack);
        tv_guest_attack = (TextView) mView.findViewById(R.id.guest_attack);

        pb_attack = (ProgressBar) mView.findViewById(R.id.pb_attack);

        pb_danger = (ProgressBar) mView.findViewById(R.id.pb_danger_attack);
        pb_shoot_correct = (ProgressBar) mView.findViewById(R.id.pb_shoot_correct);
        pb_shoot_miss = (ProgressBar) mView.findViewById(R.id.pb_shoot_miss);
        tv_frequency = (TextView) mView.findViewById(R.id.tv_frequency);
        StadiumUtils.keepTimeAnimation(tv_frequency);

        //x时间轴
        timeView = (TimeView) mView.findViewById(R.id.time_football);
        timeLayoutTop = (FootballEventView) mView.findViewById(R.id.time_layout_top);
        timeLayoutBottom = (FootballEventView) mView.findViewById(R.id.time_layout_bottom);

    }


    public void initData(MatchDetail mMatchDetail) {
        homename.setText(mMatchDetail.getHomeTeamInfo().getName());
        guestname.setText(mMatchDetail.getGuestTeamInfo().getName());
    }

    /**
     * 设置比赛时间
     *
     * @param keeptime
     */
    public void setKeepTime(String keeptime) {
        mTvTime.setText(keeptime);
    }

    public void setfrequencyText(String s) {
        tv_frequency.setText(s);
    }

    /**
     * 完场进行的处理
     *
     * @param mMatchDetail 赛事总信息
     */
    public void initMatchOverData(MatchDetail mMatchDetail) {
        //时间轴的处理
        //完场直接有数据

        xMatchLive = mMatchDetail.getMatchInfo().getMatchTimeLive();//完场直接有数据
        secondToMinute(xMatchLive);
        String time = "5400000";//90分钟的毫秒数
        showFootballEventByState();
        showTimeView(time);//画一下时间轴

        homename.setText(mMatchDetail.getHomeTeamInfo().getName());
        guestname.setText(mMatchDetail.getGuestTeamInfo().getName());
        tv_home_corner.setText(mMatchDetail.getHomeTeamInfo().getCorner());
        tv_home_rc.setText(mMatchDetail.getHomeTeamInfo().getRc());
        tv_home_yc.setText(mMatchDetail.getHomeTeamInfo().getYc());
        tv_home_danger.setText(mMatchDetail.getHomeTeamInfo().getDanger());
        tv_home_shoot_correct.setText(String.valueOf(Integer.parseInt(mMatchDetail.getHomeTeamInfo().getShot())));
        tv_home_shoot_miss.setText(mMatchDetail.getHomeTeamInfo().getAside());
        tv_guest_corner.setText(mMatchDetail.getGuestTeamInfo().getCorner());
        tv_guest_rc.setText(mMatchDetail.getGuestTeamInfo().getRc());
        tv_guest_yc.setText(mMatchDetail.getGuestTeamInfo().getYc());
        tv_guest_danger.setText(mMatchDetail.getGuestTeamInfo().getDanger());
        tv_guest_shoot_correct.setText(String.valueOf(Integer.parseInt(mMatchDetail.getGuestTeamInfo().getShot())));
        tv_guest_shoot_miss.setText(mMatchDetail.getGuestTeamInfo().getAside());
        tv_home_attack.setText(mMatchDetail.getHomeTeamInfo().getAttackCount());
        tv_guest_attack.setText(mMatchDetail.getGuestTeamInfo().getAttackCount());

        pb_attack.setProgress((int) StadiumUtils.computeProgressbarPercent(mMatchDetail.getHomeTeamInfo().getAttackCount(), mMatchDetail.getGuestTeamInfo().getAttackCount()));

        pb_danger.setProgress((int) StadiumUtils.computeProgressbarPercent(mMatchDetail.getHomeTeamInfo().getDanger(), mMatchDetail.getGuestTeamInfo().getDanger()));
        pb_shoot_correct.setProgress((int) StadiumUtils.computeProgressbarPercent(mMatchDetail.getHomeTeamInfo().getShot(), mMatchDetail.getGuestTeamInfo().getShot()));
        pb_shoot_miss.setProgress((int) StadiumUtils.computeProgressbarPercent(mMatchDetail.getHomeTeamInfo().getAside(), mMatchDetail.getGuestTeamInfo().getAside()));

        mTvTime.setText(mContext.getResources().getString(R.string.finish_txt));
        tv_frequency.setText("");
        score.setText(mMatchDetail.getHomeTeamInfo().getScore() + ":" + mMatchDetail.getGuestTeamInfo().getScore());
        score.setTextColor(mContext.getResources().getColor(R.color.score));

    }

    /**
     * 赛中的时间轴数据，从文字直播数据中筛选
     *
     * @param mMatchDetail
     */
    public void initFootBallEventData(MatchDetail mMatchDetail) {
        matchLive = mMatchDetail.getMatchInfo().getMatchLive();
        getTimeLive();
        showFootballEventByState();
    }

    /**
     * 比赛中的统计信息
     *
     * @param mathchStatisInfo
     */
    public void initMatchNowData(MathchStatisInfo mathchStatisInfo) {

        tv_home_corner.setText(String.valueOf(mathchStatisInfo.getHome_corner()));
        tv_home_rc.setText(String.valueOf(mathchStatisInfo.getHome_rc()));
        tv_home_yc.setText(String.valueOf(mathchStatisInfo.getHome_yc()));
        tv_home_danger.setText(String.valueOf(mathchStatisInfo.getHome_danger()));
        tv_home_shoot_correct.setText(String.valueOf(mathchStatisInfo.getHome_shoot_correct()));
        tv_home_shoot_miss.setText(String.valueOf(mathchStatisInfo.getHome_shoot_miss()));
        tv_guest_corner.setText(String.valueOf(mathchStatisInfo.getGuest_corner()));
        tv_guest_rc.setText(String.valueOf(mathchStatisInfo.getGuest_rc()));
        tv_guest_yc.setText(String.valueOf(mathchStatisInfo.getGuest_yc()));
        score.setText(String.valueOf(mathchStatisInfo.getHome_score()) + ":" + String.valueOf(mathchStatisInfo.getGuest_score()));
        tv_frequency.setText("'");

        tv_guest_danger.setText(String.valueOf(mathchStatisInfo.getGuest_danger()));
        tv_guest_shoot_correct.setText(String.valueOf(mathchStatisInfo.getGuest_shoot_correct()));
        tv_guest_shoot_miss.setText(String.valueOf(mathchStatisInfo.getGuest_shoot_miss()));

        tv_home_attack.setText(String.valueOf(mathchStatisInfo.getHome_attack()));
        tv_guest_attack.setText(String.valueOf(mathchStatisInfo.getGuest_attack()));

        pb_attack.setProgress((int) StadiumUtils.computeProgressbarPercent(String.valueOf(mathchStatisInfo.getHome_attack()), String.valueOf(mathchStatisInfo.getGuest_attack())));

        pb_danger.setProgress((int) StadiumUtils.computeProgressbarPercent(String.valueOf(mathchStatisInfo.getHome_danger()), String.valueOf(mathchStatisInfo.getGuest_danger())));
        pb_shoot_correct.setProgress((int) StadiumUtils.computeProgressbarPercent(String.valueOf(mathchStatisInfo.getHome_shoot_correct()), String.valueOf(mathchStatisInfo.getGuest_shoot_correct())));
        pb_shoot_miss.setProgress((int) StadiumUtils.computeProgressbarPercent(String.valueOf(mathchStatisInfo.getHome_shoot_miss()), String.valueOf(mathchStatisInfo.getGuest_shoot_miss())));
    }

    /**
     * 完场将时间改为分钟
     *
     * @param xMatchLive
     */
    private void secondToMinute(List<MatchTimeLiveBean> xMatchLive) {
        for (MatchTimeLiveBean timeLiveBean : xMatchLive) {
            int time = StadiumUtils.convertStringToInt(timeLiveBean.getTime());
            if (time > 45 && timeLiveBean.getState().equals(FIRSTHALF)) {//上半场
                time = 45;
            }
            if (time > 90) {
                time = 90;
            }
            if (timeLiveBean.getState().equals(HALFTIME)) {
                time = 46;
            }
            timeLiveBean.setTime(time + "");
        }
    }

    /**
     * 展示足球事件。根据比赛状态
     */
    public void showFootballEventByState() {
        Collections.sort(xMatchLive, new FootballEventComparator());
        Map<String, List<MatchTimeLiveBean>> timeEventMap1 = new LinkedHashMap<String, List<MatchTimeLiveBean>>();//LinkedHashMap保证map有序
        Map<String, List<MatchTimeLiveBean>> timeEventMap2 = new LinkedHashMap<String, List<MatchTimeLiveBean>>();

        for (MatchTimeLiveBean data : xMatchLive) {
            List<MatchTimeLiveBean> temp1 = timeEventMap1.get(data.getTime());
            List<MatchTimeLiveBean> temp2 = timeEventMap2.get(data.getTime());
            if (temp1 == null) {
                temp1 = new ArrayList<>();
            }
            if (temp2 == null) {
                temp2 = new ArrayList<>();
            }
            if (data.getIsHome().equals("1")) {//主队
                temp1.add(data);
                timeEventMap1.put(data.getTime(), temp1);
            }
            if (data.getIsHome().equals("0")) {//客队
                temp2.add(data);
                timeEventMap2.put(data.getTime(), temp2);
            }

        }

        timeLayoutTop.setEventImageByState(timeEventMap1);
        timeLayoutBottom.setEventImageByState(timeEventMap2);


    }

    /**
     * 展示时间轴时间
     */
    public void showTimeView(String mKeepTime) {
        if (mKeepTime == null) {
            mKeepTime = "0";
        }
        timeView.updateTime(Integer.parseInt(mKeepTime));
    }

    /**
     * 赛中时获取时间轴数据
     */
    private void getTimeLive() {
        xMatchLive=new ArrayList<>();
        if (matchLive == null) {
            return;
        }
        Iterator<MatchTextLiveBean> iterator1 = matchLive.iterator();
        while (iterator1.hasNext()) {
            MatchTextLiveBean bean1 = iterator1.next();
            if (bean1.getCode().equals(SCORE) || bean1.getCode().equals(SCORE1) ||
                    bean1.getCode().equals(RED_CARD) || bean1.getCode().equals(RED_CARD1) ||
                    bean1.getCode().equals(YTORED) || bean1.getCode().equals(YTORED1) ||
                    bean1.getCode().equals(YELLOW_CARD) || bean1.getCode().equals(YELLOW_CARD1) ||
                    bean1.getCode().equals(CORNER) || bean1.getCode().equals(CORNER1) ||
                    bean1.getCode().equals(SUBSTITUTION) || bean1.getCode().equals(SUBSTITUTION1)
                //  bean1.getCode().equals(FIRSTOVER)//把上半场结束的状态也加进来
                    ) {
                String place = bean1.getMsgPlace();
                if (place.equals("2")) {//客队
                    place = "0";//isHome=0客队
                }
                MatchTimeLiveBean timeLiveBean = new MatchTimeLiveBean(secondToMInute(bean1) + "", bean1.getCode(),//这里时间直接转换为分钟，就会一样了
                        place, bean1.getEnNum(), bean1.getState());
                xMatchLive.add(timeLiveBean);

            }

        }

        for (MatchTextLiveBean bean : matchLive) {
            Iterator<MatchTimeLiveBean> iterator2 = xMatchLive.iterator();
            while (iterator2.hasNext()) {
                MatchTimeLiveBean bean2 = iterator2.next();
                if (bean2.getMsgId().equals(bean.getCancelEnNum())) {
                    iterator2.remove();//去掉已经取消的
                }
            }
        }


    }

    /**
     * 赛中将时间改为分钟
     *
     * @param bean
     * @return
     */
    private int secondToMInute(MatchTextLiveBean bean) {
        int time = StadiumUtils.convertStringToInt(bean.getTime());
        if (time > 45 && bean.getState().equals(FIRSTHALF)) {//上半场
            time = 45;
        }
        if (time > 90) {
            time = 90;
        }
        if (bean.getState().equals(HALFTIME)) {
            time = 46;
        }
        return time;

    }

    /**
     * 把事件从文字直播集合中取出加到时间轴集合中
     *
     * @param matchTextLiveBean
     */
    public void addFootballEvent(MatchTextLiveBean matchTextLiveBean) {
        String place = matchTextLiveBean.getMsgPlace();
        if (matchTextLiveBean.getMsgPlace().equals("2")) {//客队
            place = "0";
        }

        xMatchLive.add(new MatchTimeLiveBean(secondToMInute(matchTextLiveBean) + "",
                matchTextLiveBean.getCode(), place, matchTextLiveBean.getEnNum(), matchTextLiveBean.getState()));
    }

    /**
     * 取消对应的足球事件
     *
     *
     */
    public void cancelFootBallEvent( MatchTextLiveBean matchTextLiveBean) {
        Iterator<MatchTimeLiveBean> iterator =xMatchLive.iterator();
        while (iterator.hasNext()) {
            MatchTimeLiveBean bean = iterator.next();
            if (bean.getMsgId().equals(matchTextLiveBean.getCancelEnNum())) {//取消进球等事件的判断
                iterator.remove();//用xMatchLive.remove会有异常
            }
        }
    }
}
