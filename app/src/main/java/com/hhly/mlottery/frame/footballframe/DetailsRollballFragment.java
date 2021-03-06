package com.hhly.mlottery.frame.footballframe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.bean.footballDetails.BottomOdds;
import com.hhly.mlottery.bean.footballDetails.BottomOddsItem;
import com.hhly.mlottery.bean.footballDetails.MatchDetail;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.bean.footballDetails.PlayerInfo;
import com.hhly.mlottery.bean.footballDetails.WebSocketRollballOdd;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.StadiumUtils;
import com.hhly.mlottery.util.StringUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.DetailsRollOdd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * @author wang gang
 * @date 2016/6/3 14:17
 * @des 足球内页聊球下部内容
 */
public class DetailsRollballFragment extends BaseWebSocketFragment {


    private final int ERROR = -1;//访问失败
    private final int SUCCESS = 0;// 访问成功
    private final int STARTLOADING = 1;// 正在加载中

    private View mView;

    private Context mContext;

    private MatchDetail mMatchDetail;

    private List<MatchTextLiveBean> matchLive;
    private List<Integer> allMatchLiveMsgId;
    private int mViewType = 0;


    private static final String DETAILSROLLBALL_PARAM = "DETAILSROLLBALL_PARAM";
    private static final String DETAILSROLLBALL_TYPE = "DETAILSROLLBALL_TYPE";
    private static final String DETAILSROLLBALL_THIRD_ID = "THIRD_ID";
    public static final int DETAILSROLLBALL_TYPE_PRE = 0x01;//赛前
    public static final int DETAILSROLLBALL_TYPE_ING = 0x10;//赛中
    public static final int DETAILSROLLBALL_TYPE_ED = 0x20;//赛后

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

    //亚盘
    private static final int ALET = 1;
    private static final int EUR = 2;
    private static final int ASIZE = 3;  //大小球

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

    private RelativeLayout live_infos;


    private FrameLayout fl_odds_loading;

    private FrameLayout fl_odds_net_error;

    private LinearLayout ll_odds;

    private BottomOddsDetailsFragment mBottomOddsDetailsFragment;

    private LiveTextFragmentTest liveTextFragmentTest;

    private FinishMatchLiveTextFragment finishMatchLiveTextFragment;//完场


//    private HappySocketClient hSocketClient;
//    private URI hSocketUri = null;


    //保存上次推送赔率
    private BottomOddsItem aletBottomOddsItem;
    private BottomOddsItem asizeBottomOddsItem;
    private BottomOddsItem eurBottomOddsItem;


    private boolean isSocketStart = true;


    public static DetailsRollballFragment newInstance(String thirdId) {
        DetailsRollballFragment fragment = new DetailsRollballFragment();
        Bundle args = new Bundle();
        args.putString(DETAILSROLLBALL_THIRD_ID, thirdId);
        fragment.setArguments(args);
        return fragment;
    }


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
        String thirdId = getArguments().getString(DETAILSROLLBALL_THIRD_ID);
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic("USER.topic.scollodds." + thirdId);
        super.onCreate(savedInstanceState);

        mContext = getActivity();

//        try {
        // hSocketUri = new URI(BaseURLs.WS_SERVICE);
//            hSocketUri = new URI("ws://192.168.10.242:61634/ws");
//            System.out.println(">>>>>" + hSocketUri);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_details_rollball, container, false);
        initView();
        return mView;
    }


    public void activateMatch(MatchDetail matchDetail, int type) {
        this.mMatchDetail = matchDetail;
        this.mViewType = type;
        mView.findViewById(R.id.prestadium_layout).setVisibility(View.GONE);
        mView.findViewById(R.id.stadium_layout).setVisibility(View.VISIBLE);
        if (mViewType == DETAILSROLLBALL_TYPE_ING) {
            initOdds();
        }
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


                if (mBottomOddsDetailsFragment != null) {
                    mBottomOddsDetailsFragment.dismiss();
                }
                if (LIVEENDED.equals(mMatchDetail.getLiveStatus())) {
                    if (finishMatchLiveTextFragment != null) {
                        finishMatchLiveTextFragment.dismiss();
                    }

                    finishMatchLiveTextFragment = new FinishMatchLiveTextFragment().newInstance((ArrayList<MatchTextLiveBean>) matchLive, mMatchDetail.getLiveStatus());
                    finishMatchLiveTextFragment.show(getChildFragmentManager(), "finishLive");


                } else {

                    if (liveTextFragmentTest != null) {
                        liveTextFragmentTest.dismiss();
                    }

                    liveTextFragmentTest = new LiveTextFragmentTest().newInstance((ArrayList<MatchTextLiveBean>) matchLive, mMatchDetail.getLiveStatus());
                    liveTextFragmentTest.show(getChildFragmentManager(), "bottomLive");
                }
            }
        });

        reLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.d(TAG, "reLoading");
                initOdds();
            }
        });
    }


    //下拉刷新
    public void refreshMatch(MatchDetail matchDetail, int type) {
        this.mMatchDetail = matchDetail;
        this.mViewType = type;
        if (mViewType == DETAILSROLLBALL_TYPE_PRE) {
            initPreData();
        } else {
            initLiveText();

            L.d(TAG, "refreshMatch");
            initOdds();
        }
    }


    public void setMatchData(int type, MatchDetail mMatchDetail) {
        this.mViewType = type;
        this.mMatchDetail = mMatchDetail;
        L.d(TAG, "mViewType=" + mViewType + "-------DETAILSROLLBALL_TYPE_PRE=" + DETAILSROLLBALL_TYPE_PRE);

        if (mViewType == DETAILSROLLBALL_TYPE_PRE) {//赛前

            L.d(TAG, "赛前");
            mView.findViewById(R.id.prestadium_layout).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.stadium_layout).setVisibility(View.GONE);
            initPreData();
        } else {  //赛后活赛中


            mView.findViewById(R.id.prestadium_layout).setVisibility(View.GONE);
            mView.findViewById(R.id.stadium_layout).setVisibility(View.VISIBLE);
            initLiveText();

            L.d(TAG, "赛后");

            initOdds();
        }
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

        Map<String, String> params = new HashMap<>();
        params.put("thirdId", ((FootballMatchDetailActivity) getActivity()).mThirdId);
        //String url = "http://192.168.10.242:8181/mlottery/core/footballBallList.ballListOverview.do";

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_BALLLISTOVERVIEW_INFO, params,
                new VolleyContentFast.ResponseSuccessListener<BottomOdds>() {
                    @Override
                    public void onResponse(BottomOdds bottomOdds) {
                        if (!bottomOdds.getResult().equals("200")) {
                            return;
                        }

                        initItemOdd(bottomOdds);

                        //赛中的时候开启socket推送
                        if (mViewType == DETAILSROLLBALL_TYPE_ING) {
                            if (isSocketStart) {
//                                startWebsocket();
//                                computeWebSocket();
                                connectWebSocket();
                                isSocketStart = false;

                            }
                        }
                        mHandler.sendEmptyMessage(SUCCESS);

                    }
                }

                , new VolleyContentFast.ResponseErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        mHandler.sendEmptyMessage(ERROR);
                    }
                }

                , BottomOdds.class
        );
    }

    private boolean isOpenOddsDetails = true;


    private void initItemOdd(final BottomOdds bottomOdds) {
        if (mContext == null) {
            return;
        }
        odd_alet.setTitle(mContext.getResources().getString(R.string.set_asialet_txt));
        odd_asize.setTitle(mContext.getResources().getString(R.string.set_asiasize_txt));
        odd_eur.setTitle(mContext.getResources().getString(R.string.set_euro_txt));


        //  "-"表示没有数据   null 表封盘

        odd_alet.setTableLayoutData(bottomOdds.getAsianlistOdd(), ALET);

        aletBottomOddsItem = bottomOdds.getAsianlistOdd().get(1);  //获取即时赔率


        odd_asize.setTableLayoutData(bottomOdds.getOverunderlistOdd(), ASIZE);
        asizeBottomOddsItem = bottomOdds.getOverunderlistOdd().get(1);


        odd_eur.setTableLayoutData(bottomOdds.getEuropelistOdd(), EUR);
        eurBottomOddsItem = bottomOdds.getEuropelistOdd().get(1);


        //亚盘1
        odd_alet.findViewById(R.id.tl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isRepeatShowDialog();
                mBottomOddsDetailsFragment = new BottomOddsDetailsFragment().newInstance(bottomOdds.getAsianlistOdd().get(0), ALET);
                mBottomOddsDetailsFragment.show(getChildFragmentManager(), "bottomOdds");
            }
        });


        //大小球
        odd_asize.findViewById(R.id.tl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isRepeatShowDialog();

                mBottomOddsDetailsFragment = new BottomOddsDetailsFragment().newInstance(bottomOdds.getOverunderlistOdd().get(0), ASIZE);
                mBottomOddsDetailsFragment.show(getChildFragmentManager(), "bottomOdds");
            }
        });


        //欧赔
        odd_eur.findViewById(R.id.tl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRepeatShowDialog();

                mBottomOddsDetailsFragment = new BottomOddsDetailsFragment().newInstance(bottomOdds.getEuropelistOdd().get(0), EUR);
                mBottomOddsDetailsFragment.show(getChildFragmentManager(), "bottomOdds");
            }
        });

        mHandler.sendEmptyMessage(SUCCESS);


    }


    private void isRepeatShowDialog() {
        if (finishMatchLiveTextFragment != null) {
            finishMatchLiveTextFragment.dismiss();
        }

        if (liveTextFragmentTest != null) {
            liveTextFragmentTest.dismiss();
        }

        if (mBottomOddsDetailsFragment != null) {
            mBottomOddsDetailsFragment.dismiss();
        }
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
        live_infos = (RelativeLayout) mView.findViewById(R.id.rl_live);

        fl_odds_loading = (FrameLayout) mView.findViewById(R.id.fl_odd_loading);
        fl_odds_net_error = (FrameLayout) mView.findViewById(R.id.fl_odds_networkError);

        ll_odds = (LinearLayout) mView.findViewById(R.id.ll_0dds);

        reLoading = (TextView) mView.findViewById(R.id.reLoading);
    }

    private void initPreData() {
        String startTime = mMatchDetail.getMatchInfo().getStartTime();
        if (pre_dataTime_txt == null) {
            return;
        }
        if (!StringUtils.isEmpty(startTime) && startTime.length() == 16) {
            pre_dataTime_txt.setText(startTime.substring(11, 16));
            pre_dataDate_txt.setText(DateUtil.convertDateToNation(startTime.substring(0, 10)));
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

        L.d(TAG, "推送文字直播LiveText");
        live_text.setText(msg);
    }

    //推送刷新
    public void setLiveTextDetails(List<MatchTextLiveBean> matchLive) {

        if (liveTextFragmentTest != null) {
            liveTextFragmentTest.updataLiveTextAdapter(matchLive);
        }
    }


//    /***
//     * 开始推送socket
//     */
//    private synchronized void startWebsocket() {
//
//        L.d(TAG, "滚球赔率");
//
//        if (hSocketClient != null) {
//            if (!hSocketClient.isClosed()) {
//                hSocketClient.close();
//                hSocketClient = null;
//            }
//
//            L.d(TAG, "hSocketClient=" + hSocketClient);
//
//            hSocketClient = new HappySocketClient(hSocketUri, new Draft_17());
//            hSocketClient.setSocketResponseMessageListener(this);
//            hSocketClient.setSocketResponseCloseListener(this);
//            hSocketClient.setSocketResponseErrorListener(this);
//            try {
//                hSocketClient.connect();
//            } catch (Exception e) {
//                hSocketClient.close();
//                hSocketClient = null;
//            }
//        } else {
//            hSocketClient = new HappySocketClient(hSocketUri, new Draft_17());
//            hSocketClient.setSocketResponseMessageListener(this);
//            hSocketClient.setSocketResponseCloseListener(this);
//            hSocketClient.setSocketResponseErrorListener(this);
//            try {
//                hSocketClient.connect();
//            } catch (Exception e) {
//                hSocketClient.close();
//                hSocketClient = null;
//            }
//        }
//    }

    //事件推送

//    @Override
//    public void onMessage(String message) {
//        // L.d(TAG, "---onMessage---推送比赛thirdId==" + ((FootballMatchDetailActivity) getActivity()).mThirdId);
//
//        pushStartTime = System.currentTimeMillis(); // 记录起始时间
//        L.d(TAG, "心跳时间" + pushStartTime);
//        if (message.startsWith("CONNECTED")) {
//            String id = "android" + DeviceInfo.getDeviceId(mContext);
//            id = MD5Util.getMD5(id);
//            if (mContext == null) {
//                return;
//            }
//            hSocketClient.send("SUBSCRIBE\nid:" + id + "\ndestination:/topic/USER.topic.scollodds." + "337438" + "\n\n");
//            return;
//        } else if (message.startsWith("MESSAGE")) {
//
//            String[] msgs = message.split("\n");
//            String ws_json = msgs[msgs.length - 1];
//
//            try {
//                JSONObject jsonObject = new JSONObject(ws_json);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            Message msg = Message.obtain();
//            msg.obj = ws_json;
//            mSocketHandler.sendMessage(msg);
//
//        }
//        hSocketClient.send("\n");
//    }


    /**
     * 直播时间推送，更新比赛即时时间和时间轴、文字直播
     */
    Handler mSocketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //时间推送

            String ws_json = (String) msg.obj;
            WebSocketRollballOdd webSocketRollballOdd = null;
            try {
                webSocketRollballOdd = JSON.parseObject(ws_json, WebSocketRollballOdd.class);
            } catch (Exception e) {
                ws_json = ws_json.substring(0, ws_json.length() - 1);
                webSocketRollballOdd = JSON.parseObject(ws_json, WebSocketRollballOdd.class);
            }

            L.d(TAG, "=======赔率推送=====" + ws_json);
            L.d(TAG, "=======赔率推送=====" + webSocketRollballOdd.getLeft() + "==" + webSocketRollballOdd.getOddType());

            //更新赔率

            updateOdds(webSocketRollballOdd);

        }
    };


    private void updateOdds(WebSocketRollballOdd webSocketRollballOdd) {

        //红升绿降  1 升  -1降  0不变
        //亚盘
        if (ALET == Integer.parseInt(webSocketRollballOdd.getOddType())) {
            odd_alet.updateOdds(setLiveOdds(aletBottomOddsItem, webSocketRollballOdd));
        } else if (ASIZE == Integer.parseInt(webSocketRollballOdd.getOddType())) {
            //大小球
            odd_asize.updateOdds(setLiveOdds(asizeBottomOddsItem, webSocketRollballOdd));
        } else if (EUR == Integer.parseInt(webSocketRollballOdd.getOddType())) {
            //欧赔
            odd_eur.updateOdds(setLiveOdds(eurBottomOddsItem, webSocketRollballOdd));
        }
    }


    /**
     * 对获取推送对象进行红升绿降处理
     *
     * @param b
     * @param webodds
     * @return
     */
    private synchronized BottomOddsItem setLiveOdds(BottomOddsItem b, WebSocketRollballOdd webodds) {


        if (isNULLOrEmpty(webodds.getLeft()) || isNULLOrEmpty(webodds.getMiddle()) || isNULLOrEmpty(webodds.getRight()) || isNULLOrEmpty(b.getLeft()) || isNULLOrEmpty(b.getMiddle()) || isNULLOrEmpty(b.getRight())) {

            b.setLeft(webodds.getLeft());
            b.setLeftUp("0");
            b.setMiddle(webodds.getMiddle());
            b.setMiddleUp("0");
            b.setRight(webodds.getRight());
            b.setRightUp("0");
        } else {

            if (Float.parseFloat(b.getLeft()) > Float.parseFloat(webodds.getLeft())) {


                b.setLeftUp("-1");  //降

            } else if (Float.parseFloat(b.getLeft()) < Float.parseFloat(webodds.getLeft())) {
                b.setLeftUp("1");
            } else {
                b.setLeftUp("0");
            }

            b.setLeft(webodds.getLeft());

            if (Float.parseFloat(b.getMiddle()) > Float.parseFloat(webodds.getMiddle())) {
                b.setMiddleUp("-1");  //降
            } else if (Float.parseFloat(b.getMiddle()) < Float.parseFloat(webodds.getMiddle())) {
                b.setMiddleUp("1");
            } else {
                b.setMiddleUp("0");
            }

            b.setMiddle(webodds.getMiddle());

            if (Float.parseFloat(b.getRight()) > Float.parseFloat(webodds.getRight())) {
                b.setRightUp("-1");  //降
            } else if (Float.parseFloat(b.getRight()) < Float.parseFloat(webodds.getRight())) {
                b.setRightUp("1");
            } else {
                b.setRightUp("0");
            }

            b.setRight(webodds.getRight());

        }


        return b;
    }


    //心跳时间
    private long pushStartTime;

    public Timer detailsTimer = new Timer();

    /***
     * 计算推送Socket断开重新连接
     */
    private boolean isStartTimer = false;

//    private void computeWebSocket() {
//        if (!isStartTimer) {
//            TimerTask tt = new TimerTask() {
//                @Override
//                public void run() {
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//                    L.d(TAG, df.format(new Date()) + "---监听socket连接状态:Open=" + hSocketClient.isOpen() + ",Connecting=" + hSocketClient.isConnecting() + ",Close=" + hSocketClient.isClosed() + ",Closing=" + hSocketClient.isClosing());
//                    long pushEndTime = System.currentTimeMillis();
//                    if ((pushEndTime - pushStartTime) >= 30000) {
//                        L.d(TAG, "重新启动socketvvvv");
//                        startWebsocket();
//                    }
//                }
//            };
//
//            if (detailsTimer != null) {
//                detailsTimer.schedule(tt, 15000, 15000);
//                isStartTimer = true;
//            }
//        }
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();

//        if (detailsTimer != null) {
//            L.d("timer", "detailsRoll定时器");
//            detailsTimer.cancel();
//            detailsTimer.purge();
//            detailsTimer = null;
//        }
//
//        if (hSocketClient != null) {
//            hSocketClient.close();
//            hSocketClient = null;
//        }
    }

    @Override
    protected void onTextResult(String text) {
        Message msg = Message.obtain();
        msg.obj = text;
        mSocketHandler.sendMessage(msg);
    }

    @Override
    protected void onConnectFail() {

    }

    @Override
    protected void onDisconnected() {

    }

    @Override
    protected void onConnected() {

    }

//    @Override
//    public void onClose(String message) {
//
//    }
//
//    @Override
//    public void onError(Exception exception) {
//
//    }

    private boolean isNULLOrEmpty(String s) {
        return s == null || "".equals(s) || "-".equals(s);
    }

}
