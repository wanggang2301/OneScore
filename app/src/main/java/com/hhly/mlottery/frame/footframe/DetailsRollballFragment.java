package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.BottomOdds;
import com.hhly.mlottery.bean.footballDetails.MatchDetail;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.bean.footballDetails.PlayerInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.StadiumUtils;
import com.hhly.mlottery.util.StringUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.DetailsRollOdd;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wang gang
 * @date 2016/6/3 14:17
 * @des ${TODO}
 */
public class DetailsRollballFragment extends Fragment {

    private final int ERROR = -1;//访问失败
    private final int SUCCESS = 0;// 访问成功
    private final int STARTLOADING = 1;// 正在加载中

    private View mView;

    private Context mContext;

    private MatchDetail mMatchDetail;

    private List<MatchTextLiveBean> matchLive;
    private List<Integer> allMatchLiveMsgId;
    private int mViewType = 0;

    public static final int ASIA = 0;
    public static final int BIG_SMALL_BALL = 1;

    public static final int EU = 2;

    private static final String DETAILSROLLBALL_PARAM = "DETAILSROLLBALL_PARAM";
    private static final String DETAILSROLLBALL_TYPE = "DETAILSROLLBALL_TYPE";
    public static final int DETAILSROLLBALL_TYPE_PRE = 0x01;//赛前
    public static final int DETAILSROLLBALL_TYPE_ING = 0x10;//赛中赛后

    //直播状态 liveStatus
    private static final String BEFOURLIVE = "0";//直播前
    private static final String ONLIVE = "1";//直播中
    private static final String LIVEENDED = "-1";//直播结束


    //赛事进行状态。matchStatus
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


    //赛前view
    private TextView pre_dataTime_txt;//开赛时间
    private TextView pre_dataDate_txt;//开赛日期
    private TextView pre_weather_data_txt;//天气
    private TextView pre_site_data_txt;//场地
    private TextView pre_first_home_data_txt;//主队首发阵容
    private TextView pre_first_guest_data_txt;//客队首发阵容
    private TextView pre_first_home_datas_txt;//zhu队首发阵容
    private TextView pre_first_guest_datas_txt;//客队首发阵容
    private TextView pre_first_not_tv;
    private ImageView pre_weather_img;//天气图

    private TextView live_time; //实时时间

    private TextView live_text;  //实时直播

    private TextView reLoading; //赔率请求失败重新加载

    private DetailsRollOdd odd_alet; //亚盘

    private DetailsRollOdd odd_asize; //大小

    private DetailsRollOdd odd_eur; //欧赔

    private ImageView live_infos;


    private FrameLayout fl_odds_loading;

    private FrameLayout fl_odds_net_error;

    private LinearLayout ll_odds;

    private BottomOddsDetailsFragment mBottomOddsDetailsFragment;

    private LiveTextFragmentTest liveTextFragmentTest;

    private FinishMatchLiveTextFragment finishMatchLiveTextFragment;//完场

    public static DetailsRollballFragment newInstance(int fragmentType, MatchDetail matchDetail) {
        DetailsRollballFragment fragment = new DetailsRollballFragment();
        Bundle args = new Bundle();
        args.putParcelable(DETAILSROLLBALL_PARAM, matchDetail);
        args.putInt(DETAILSROLLBALL_TYPE, fragmentType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_details_rollball, container, false);
        initView();
        if (getArguments() != null) {
            mMatchDetail = getArguments().getParcelable(DETAILSROLLBALL_PARAM);
            mViewType = getArguments().getInt(DETAILSROLLBALL_TYPE);
            if (mViewType == DETAILSROLLBALL_TYPE_PRE) {//赛前
                mView.findViewById(R.id.prestadium_layout).setVisibility(View.VISIBLE);
                mView.findViewById(R.id.stadium_layout).setVisibility(View.GONE);
                initPreData();
            } else {
                mView.findViewById(R.id.prestadium_layout).setVisibility(View.GONE);
                mView.findViewById(R.id.stadium_layout).setVisibility(View.VISIBLE);
                initLiveText();
                initOdds();
            }
        }

        return mView;
    }


    /**
     * 比赛赛中、赛后初始化数据
     */
    private void initLiveText() {
        matchLive = new ArrayList<MatchTextLiveBean>();
        // xMatchLive = new ArrayList<MatchTimeLiveBean>();

        matchLive = mMatchDetail.getMatchInfo().getMatchLive();
        if (matchLive == null) {
            return;
        }

        allMatchLiveMsgId = new ArrayList<>();
        for (MatchTextLiveBean ml : matchLive) {

            if (ml.getMsgId() != null && !"".equals(ml.getMsgId())) {
                allMatchLiveMsgId.add(Integer.parseInt(ml.getMsgId()));
            }
        }


        //完场处理
        if (LIVEENDED.equals(mMatchDetail.getLiveStatus())) {
            live_time.setText(mContext.getResources().getString(R.string.finish_txt));
            live_text.setText(matchLive.get(0).getMsgText());

        } else {  //比赛中处理
            String state = matchLive.get(0).getState();//获取最后一个的比赛状态
            String mKeepTime = matchLive.get(0).getTime();//获取时间
            String text = matchLive.get(0).getMsgText();
            if (NOTOPEN.equals(state)) {
                live_time.setText(mContext.getResources().getString(R.string.not_start_txt));
            } else if (FIRSTHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) <= 45) {
                live_time.setText(StadiumUtils.convertStringToInt(mKeepTime) + "");
            } else if (FIRSTHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) > 45) {
                live_time.setText("45+");
            } else if (HALFTIME.equals(state)) {
                live_time.setText(mContext.getResources().getString(R.string.pause_txt));
            } else if (SECONDHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) <= 90) {
                live_time.setText(StadiumUtils.convertStringToInt(mKeepTime) + "");
            } else if (SECONDHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) > 90) {
                live_time.setText("90+");
            } else {
                live_time.setText(StadiumUtils.convertStringToInt(mKeepTime) + "");
            }
            live_text.setText(text);
        }


        //文字直播
        live_infos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LIVEENDED.equals(mMatchDetail.getLiveStatus())) {
                    finishMatchLiveTextFragment = new FinishMatchLiveTextFragment().newInstance((ArrayList<MatchTextLiveBean>) matchLive, mMatchDetail.getLiveStatus());
                    finishMatchLiveTextFragment.show(getChildFragmentManager(), "finishLive");
                } else {
                    liveTextFragmentTest = new LiveTextFragmentTest().newInstance((ArrayList<MatchTextLiveBean>) matchLive, mMatchDetail.getLiveStatus());
                    liveTextFragmentTest.show(getChildFragmentManager(), "bottomLive");
                }
            }
        });

        reLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initOdds();
            }
        });


    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case STARTLOADING:// 正在加载中
                    fl_odds_loading.setVisibility(View.VISIBLE);
                    fl_odds_net_error.setVisibility(View.GONE);
                    ll_odds.setVisibility(View.GONE);
                    break;
                case SUCCESS:// 加载成功
                    fl_odds_loading.setVisibility(View.GONE);
                    fl_odds_net_error.setVisibility(View.GONE);
                    ll_odds.setVisibility(View.VISIBLE);

                    break;
                case ERROR:// 加载失败
                    fl_odds_loading.setVisibility(View.GONE);
                    fl_odds_net_error.setVisibility(View.VISIBLE);
                    ll_odds.setVisibility(View.GONE);
                    // isHttpData = false;
                    break;
            }
        }
    };

    private void initOdds() {
        if (getActivity() == null) {
            return;
        }

        L.d("ddd", "加载数据");
        mHandler.sendEmptyMessage(STARTLOADING);// 正在加载数据中
        // Map<String, String> params = new HashMap<>();
        //params.put("thirdId", mThirdId);
        String url = "http://192.168.31.70:8080/mlottery/core/footballBallList.ballListOverview.do?thirdId=336621&lang=zh";

        VolleyContentFast.requestJsonByGet(url, null,
                new VolleyContentFast.ResponseSuccessListener<BottomOdds>() {
                    @Override
                    public void onResponse(BottomOdds bottomOdds) {
                        if (!bottomOdds.getResult().equals("200")) {
                            return;
                        }

                        initItemOdd(bottomOdds);
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        mHandler.sendEmptyMessage(ERROR);
                    }
                }, BottomOdds.class
        );
    }

    private void initItemOdd(final BottomOdds bottomOdds) {
        odd_alet.setTitle("亚盘");
        odd_asize.setTitle("大小球");
        odd_eur.setTitle("欧赔");
        odd_alet.setTableLayoutData(bottomOdds.getAsianlistOdd());
        odd_asize.setTableLayoutData(bottomOdds.getOverunderlistOdd());
        odd_eur.setTableLayoutData(bottomOdds.getEuropelistOdd());

        //亚盘
        odd_alet.findViewById(R.id.odd_infos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomOddsDetailsFragment = new BottomOddsDetailsFragment().newInstance(bottomOdds.getAsianlistOdd().get(0), 0);
                mBottomOddsDetailsFragment.show(getChildFragmentManager(), "bottomOdds");
            }
        });


        odd_asize.findViewById(R.id.odd_infos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomOddsDetailsFragment = new BottomOddsDetailsFragment().newInstance(bottomOdds.getOverunderlistOdd().get(0), 1);
                mBottomOddsDetailsFragment.show(getChildFragmentManager(), "bottomOdds");
            }
        });

        odd_eur.findViewById(R.id.odd_infos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomOddsDetailsFragment = new BottomOddsDetailsFragment().newInstance(bottomOdds.getEuropelistOdd().get(0), 2);
                mBottomOddsDetailsFragment.show(getChildFragmentManager(), "bottomOdds");
            }
        });

        mHandler.sendEmptyMessage(SUCCESS);

    }


    private void initView() {
        pre_dataTime_txt = (TextView) mView.findViewById(R.id.pre_dataTime_txt);//开始时间
        pre_dataDate_txt = (TextView) mView.findViewById(R.id.pre_dataDate_txt);//开始时间
        pre_weather_data_txt = (TextView) mView.findViewById(R.id.pre_weather_data_txt);//天气
        pre_site_data_txt = (TextView) mView.findViewById(R.id.pre_site_data_txt);//场地
        pre_first_not_tv = (TextView) mView.findViewById(R.id.pre_first_not_tv);
        pre_weather_img = (ImageView) mView.findViewById(R.id.pre_weather_img);//天气图标

        pre_first_home_data_txt = (TextView) mView.findViewById(R.id.pre_first_home_data_txt);//主队首发阵容
        pre_first_home_datas_txt = (TextView) mView.findViewById(R.id.pre_first_home_datas_txt);//主队首发人员

        pre_first_guest_data_txt = (TextView) mView.findViewById(R.id.pre_first_guest_data_txt);//克队首发阵容
        pre_first_guest_datas_txt = (TextView) mView.findViewById(R.id.pre_first_guest_datas_txt);//客队首发人员
        live_time = (TextView) mView.findViewById(R.id.live_time);
        live_text = (TextView) mView.findViewById(R.id.live_text);

        odd_alet = (DetailsRollOdd) mView.findViewById(R.id.odd_alet);
        odd_asize = (DetailsRollOdd) mView.findViewById(R.id.odd_asize);
        odd_eur = (DetailsRollOdd) mView.findViewById(R.id.odd_eur);
        live_infos = (ImageView) mView.findViewById(R.id.live_infos);

        fl_odds_loading = (FrameLayout) mView.findViewById(R.id.fl_odds_loading);
        fl_odds_net_error = (FrameLayout) mView.findViewById(R.id.fl_odds_networkError);

        ll_odds = (LinearLayout) mView.findViewById(R.id.ll_0dds);

        reLoading = (TextView) mView.findViewById(R.id.reLoading);
    }

    public void initPreData() {
        String startTime = mMatchDetail.getMatchInfo().getStartTime();
        if (pre_dataTime_txt == null) {
            return;
        }
        if (!StringUtils.isEmpty(startTime) && startTime.length() == 16) {
            pre_dataTime_txt.setText(startTime.substring(11, 16));
            pre_dataDate_txt.setText(startTime.substring(0, 10));
        } else {
            pre_dataTime_txt.setText("");//开赛时间
            pre_dataDate_txt.setText("");
        }


        pre_site_data_txt.setText(StringUtils.isBlank(mMatchDetail.getMatchInfo().getCity()) ? mMatchDetail.getMatchInfo().getVenue() : mMatchDetail.getMatchInfo().getCity() + " " + mMatchDetail.getMatchInfo().getVenue());//场地
        pre_weather_data_txt.setText(mMatchDetail.getMatchInfo().getWeather());//天气
        switch (mMatchDetail.getMatchInfo().getWeather()) {
            case "0":
            case "5":
            case "8":
            case "14":
            case "15":
            case "16":
            case "17":
            case "18":
                pre_weather_data_txt.setText(R.string.pre_stadium_weather_sunny);
                pre_weather_img.setImageResource(R.mipmap.pre_weather_sunny);
                break;
            case "1":
            case "12":
            case "13":
                pre_weather_data_txt.setText(R.string.pre_stadium_weather_wind);
                pre_weather_img.setImageResource(R.mipmap.pre_weather_wind);
                break;
            case "2":
            case "6":
            case "19":
                pre_weather_data_txt.setText(R.string.pre_stadium_weather_heavy_rain);
                pre_weather_img.setImageResource(R.mipmap.pre_weather_heavy_rain);
                break;
            case "3":
            case "7":
                pre_weather_data_txt.setText(R.string.pre_stadium_weather_light_rain);
                pre_weather_img.setImageResource(R.mipmap.pre_weather_light_rain);
                break;
            case "4":
            case "9":
            case "10":
            case "11":
                pre_weather_data_txt.setText(R.string.pre_stadium_weather_snow);
                pre_weather_img.setImageResource(R.mipmap.pre_weather_snow);
                break;
            default:
                pre_weather_data_txt.setText("--");
                break;

        }

        if ((mMatchDetail.getHomeTeamInfo().getLineup() == null && mMatchDetail.getGuestTeamInfo().getLineup() == null) || (mMatchDetail.getHomeTeamInfo().getLineup().size() == 0 && mMatchDetail.getGuestTeamInfo().getLineup().size() == 0)) {
            mView.findViewById(R.id.pre_first_layout).setVisibility(View.GONE);
            pre_first_not_tv.setText(R.string.firsPlayers_not);
            pre_first_not_tv.setVisibility(View.VISIBLE);
        } else {
            pre_first_home_data_txt.setText(mMatchDetail.getHomeTeamInfo().getName());//zhu队首发
            pre_first_home_datas_txt.setText(appendPlayerNames(mMatchDetail.getHomeTeamInfo().getLineup()));//zhu队首发人员
            pre_first_guest_data_txt.setText(mMatchDetail.getGuestTeamInfo().getName());//客队首发
            pre_first_guest_datas_txt.setText(appendPlayerNames(mMatchDetail.getGuestTeamInfo().getLineup()));//客队首发人员
        }
    }

    private String appendPlayerNames(List<PlayerInfo> playerInfos) {
        StringBuffer s = new StringBuffer();
        if (playerInfos != null) {
            for (int i = 0; i < playerInfos.size(); i++) {
                s.append((i + 1) + ".");
                s.append(playerInfos.get(i).getName() + "\n");
            }
        }
        return s.toString();
    }


    public void setLiveTime(String time) {
        live_time.setText(time);

    }

    public void setLiveText(String msg) {
        live_text.setText(msg);
    }

    public void setLiveTextDetails(List<MatchTextLiveBean> matchLive) {

        if (liveTextFragmentTest != null) {
            liveTextFragmentTest.updataLiveTextAdapter(matchLive);
        }

    }

}
