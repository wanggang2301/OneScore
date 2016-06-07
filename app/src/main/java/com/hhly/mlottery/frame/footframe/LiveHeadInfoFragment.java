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
import com.hhly.mlottery.bean.footballDetails.MathchStatisInfo;
import com.hhly.mlottery.util.StadiumUtils;

/**
 * @author wang gang
 * @date 2016/6/2 18:34
 * @des ${TODO}
 */
public class LiveHeadInfoFragment extends Fragment {

    private static final String LIVEHEADINFO_PARAM = "LIVEHEADINFO_PARAM";
    private View mView;

    private TextView mKeepTime;

    private TextView tv_home_corner, tv_home_rc, tv_home_yc, tv_home_danger, tv_home_shoot_correct, tv_home_shoot_miss;
    private TextView tv_guest_corner, tv_guest_rc, tv_guest_yc, tv_guest_danger, tv_guest_shoot_correct, tv_guest_shoot_miss;
    private TextView tv_frequency;
    private ProgressBar pb_danger, pb_shoot_correct, pb_shoot_miss;

    private TextView score;

    private TextView homename;

    private TextView guestname;

    private Context mContext;

    private MatchDetail mMatchDetail;

    public static LiveHeadInfoFragment newInstance(MatchDetail matchDetail) {
        LiveHeadInfoFragment fragment = new LiveHeadInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(LIVEHEADINFO_PARAM, matchDetail);
        fragment.setArguments(args);
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
        initData();
        return mView;
    }

    private void initView() {
        mKeepTime = (TextView) mView.findViewById(R.id.keepTime);
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
        pb_danger = (ProgressBar) mView.findViewById(R.id.pb_danger_attack);
        pb_shoot_correct = (ProgressBar) mView.findViewById(R.id.pb_shoot_correct);
        pb_shoot_miss = (ProgressBar) mView.findViewById(R.id.pb_shoot_miss);
        tv_frequency = (TextView) mView.findViewById(R.id.tv_frequency);
        StadiumUtils.keepTimeAnimation(tv_frequency);

    }


    private void initData(){
        homename.setText(mMatchDetail.getHomeTeamInfo().getName());
        guestname.setText(mMatchDetail.getGuestTeamInfo().getName());
    }


    public void setKeepTime(String keeptime) {
        mKeepTime.setText(keeptime);
    }

    public void setfrequencyText(String s) {
        tv_frequency.setText(s);
    }

    //完场
    public void initMatchOverData(MatchDetail mMatchDetail) {
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
        pb_danger.setProgress((int) StadiumUtils.computeProgressbarPercent(mMatchDetail.getHomeTeamInfo().getDanger(), mMatchDetail.getGuestTeamInfo().getDanger()));
        pb_shoot_correct.setProgress((int) StadiumUtils.computeProgressbarPercent(mMatchDetail.getHomeTeamInfo().getShot(), mMatchDetail.getGuestTeamInfo().getShot()));
        pb_shoot_miss.setProgress((int) StadiumUtils.computeProgressbarPercent(mMatchDetail.getHomeTeamInfo().getAside(), mMatchDetail.getGuestTeamInfo().getAside()));
        mKeepTime.setText(mContext.getResources().getString(R.string.finish_txt));
        tv_frequency.setText("");
        score.setText(mMatchDetail.getHomeTeamInfo().getScore() + ":" + mMatchDetail.getGuestTeamInfo().getScore());
        score.setTextColor(mContext.getResources().getColor(R.color.score));
    }

    //比赛中
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
        tv_guest_danger.setText(String.valueOf(mathchStatisInfo.getGuest_danger()));
        tv_guest_shoot_correct.setText(String.valueOf(mathchStatisInfo.getGuest_shoot_correct()));
        tv_guest_shoot_miss.setText(String.valueOf(mathchStatisInfo.getGuest_shoot_miss()));
        pb_danger.setProgress((int) StadiumUtils.computeProgressbarPercent(String.valueOf(mathchStatisInfo.getHome_danger()), String.valueOf(mathchStatisInfo.getGuest_danger())));
        pb_shoot_correct.setProgress((int) StadiumUtils.computeProgressbarPercent(String.valueOf(mathchStatisInfo.getHome_shoot_correct()), String.valueOf(mathchStatisInfo.getGuest_shoot_correct())));
        pb_shoot_miss.setProgress((int) StadiumUtils.computeProgressbarPercent(String.valueOf(mathchStatisInfo.getHome_shoot_miss()), String.valueOf(mathchStatisInfo.getGuest_shoot_miss())));
    }
}
