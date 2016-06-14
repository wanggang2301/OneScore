package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.LiveTextAdapter;
import com.hhly.mlottery.bean.footballDetails.MatchDetail;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.bean.footballDetails.MatchTimeLiveBean;
import com.hhly.mlottery.bean.footballDetails.MathchStatisInfo;
import com.hhly.mlottery.bean.footballDetails.PlayerInfo;
import com.hhly.mlottery.bean.footballDetails.PreLiveText;
import com.hhly.mlottery.bean.websocket.WebSocketStadiumKeepTime;
import com.hhly.mlottery.bean.websocket.WebSocketStadiumLiveTextEvent;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.FootballEventComparator;
import com.hhly.mlottery.util.FootballLiveTextComparator;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.StadiumUtils;
import com.hhly.mlottery.util.StringUtils;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.websocket.HappySocketClient;
import com.hhly.mlottery.widget.FootballEventView;
import com.hhly.mlottery.widget.TimeView;
import com.umeng.analytics.MobclickAgent;

import org.java_websocket.drafts.Draft_17;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 赛场Fragment
 */
public class StadiumFragment extends Fragment implements View.OnClickListener, HappySocketClient.SocketResponseErrorListener, HappySocketClient.SocketResponseCloseListener, HappySocketClient.SocketResponseMessageListener {
    private static final String TAG = "StadiumFragment";

    private static final Logger logger = LoggerFactory.getLogger(StadiumFragment.class);

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

    private static final int GET_DATA_SUCCESS = 10;//请求成功
    private static final int GET_DATA_ERROR = 100;//请求失败

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String STADIUM_PARAM = "STADIUM_PARAM";
    private static final String STADIUM_TYPE = "STADIUM_TYPE";
    public static final int STADIUM_TYPE_PRE = 0x01;//赛前
    public static final int STADIUM_TYPE_ING = 0x10;//赛中赛后

    private int mViewType = 0;


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
    private static final String MATCHFINISH = "-1";

    private static final String GAMECANCEL = "-10";//取消比赛
    private static final String LIVECANCEL = "-100";//取消直播
    private static final String GAMEDETERMINED = "-11";//比赛待定
    private static final String CAMECUT = "-12";//比赛腰斩
    private static final String INTERRUPTGAME = "-13";//比赛中断
    /**
     * 完场
     */
    private static final String FINAL = "-1";


    //直播状态 liveStatus
    private static final String BEFOURLIVE = "0";//直播前
    private static final String ONLIVE = "1";//直播中
    private static final String LIVEENDED = "-1";//直播结束

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context mContext;

    private View view;

    private TextView tv_home_corner, tv_home_rc, tv_home_yc, tv_home_score, tv_home_danger, tv_home_shoot_correct, tv_home_shoot_miss;
    private TextView tv_guest_corner, tv_guest_rc, tv_guest_yc, tv_guest_score, tv_guest_danger, tv_guest_shoot_correct, tv_guest_shoot_miss;
    private TextView keepTime, halfScore, tv_frequency;

    private RadioGroup radioGroup;


    private LinearLayout ll_eventLayout, tv_out, tv_in;

    private LiveTextFragment liveTextFragment;
    private FirstPlayersFragment firstPlayersFragment;
    private StatisticsFragment statisticsFragment;//统计
    private TrendFragment trendFragment;
    private Fragment fragment;

    private FragmentManager fragmentManager;

    private FrameLayout frame_content, frame_content_players, frame_content_corner, frame_content_attack;


    // Fragment fragment;
    private String lastFragmentTag = "lastFragmentTag";


    private ProgressBar pb_danger, pb_shoot_correct, pb_shoot_miss;


    private MatchDetail mMatchDetail;

    private TimeView timeView;


    private FootballEventView timeLayoutTop;
    private FootballEventView timeLaoutBottom;

    private List<MatchTextLiveBean> matchLive;

    private List<Integer> allMatchLiveMsgId;


    private List<MatchTimeLiveBean> xMatchLive;

    public static MathchStatisInfo mathchStatisInfo;

    private LiveTextAdapter timeAdapter;

    private HappySocketClient hSocketClient;
    private URI hSocketUri = null;
    //测试推送按钮

    /**
     * 比赛时间
     */
    private String mKeepTime;

    /**
     * 文字直播最新一条推送时间
     */
    private int liveTextTime;

    private int matchKeepTime;

    //保存上次的msgId
    // private static String msgId;

    public static ArrayList<Integer> homeCorners = new ArrayList<>();
    public static ArrayList<Integer> guestCorners = new ArrayList<>();
    public static ArrayList<Integer> homeDangers = new ArrayList<>();
    public static ArrayList<Integer> guestDangers = new ArrayList<>();
    private int mStartTime;// 获取推送的开赛时间


    public StadiumFragment() {
        // Required empty public constructor
    }


    public static StadiumFragment newInstance(int fragmentType, MatchDetail matchDetail) {
        StadiumFragment fragment = new StadiumFragment();
        Bundle args = new Bundle();
        args.putParcelable(STADIUM_PARAM, matchDetail);
        args.putInt(STADIUM_TYPE, fragmentType);
        fragment.setArguments(args);
        return fragment;
    }


    public static StadiumFragment newInstance(String param1, String param2) {
        StadiumFragment fragment = new StadiumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stadium, container, false);
        mContext = getContext();
        PropertyConfigurator.getConfigurator(mContext).configure();

        initView();

        if (getArguments() != null) {
            mMatchDetail = getArguments().getParcelable(STADIUM_PARAM);
            mViewType = getArguments().getInt(STADIUM_TYPE);
            if (mViewType == STADIUM_TYPE_PRE) {//赛前
                view.findViewById(R.id.prestadium_layout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.stadium_layout).setVisibility(View.GONE);
                initPreData();
            } else {
                view.findViewById(R.id.prestadium_layout).setVisibility(View.GONE);
                view.findViewById(R.id.stadium_layout).setVisibility(View.VISIBLE);
                initData(true);

            }
        }

        try {
            hSocketUri = new URI(BaseURLs.WS_SERVICE);
            System.out.println(">>>>>" + hSocketUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        computeWebSocketConnTimer.cancel();

        if (hSocketClient != null) {
            hSocketClient.close();
        }
    }


    @Override
    public void onClose(String message) {
    }

    @Override
    public void onError(Exception exception) {

    }


    //心跳时间
    private long pushStartTime;

    private Timer computeWebSocketConnTimer = new Timer();

    private void computeWebSocket() {
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                L.i("1029", df.format(new Date()) + "---监听socket连接状态:Open=" + hSocketClient.isOpen() + ",Connecting=" + hSocketClient.isConnecting() + ",Close=" + hSocketClient.isClosed() + ",Closing=" + hSocketClient.isClosing());
                long pushEndTime = System.currentTimeMillis();
                if ((pushEndTime - pushStartTime) >= 30000) {
                    L.i("1029", "重新启动socket");
                    startWebsocket();
                }
            }
        };
        computeWebSocketConnTimer.schedule(tt, 15000, 15000);
    }


    @Override
    public void onMessage(String message) {
        L.i("1029", "---onMessage---推送比赛thirdId==" + ((FootballMatchDetailActivity) getActivity()).mThirdId);
        pushStartTime = System.currentTimeMillis(); // 记录起始时间
        L.i("1029", "心跳时间" + pushStartTime);
        if (message.startsWith("CONNECTED")) {
            String id = "android" + DeviceInfo.getDeviceId(getActivity());
            id = MD5Util.getMD5(id);
            if (getActivity() == null) {
                return;
            }
            hSocketClient.send("SUBSCRIBE\nid:" + id + "\ndestination:/topic/USER.topic.liveEvent." + ((FootballMatchDetailActivity) getActivity()).mThirdId + "." + appendLanguage() + "\n\n");
            L.i("1029", "CONNECTED");
            return;
        } else if (message.startsWith("MESSAGE")) {

            String[] msgs = message.split("\n");
            String ws_json = msgs[msgs.length - 1];

            String type = "";
            try {
                JSONObject jsonObject = new JSONObject(ws_json);
                type = jsonObject.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (!"".equals(type)) {
                Message msg = Message.obtain();
                msg.obj = ws_json;
                msg.arg1 = Integer.parseInt(type);

                mSocketHandler.sendMessage(msg);
            }
        }
        hSocketClient.send("\n");

    }


    /**
     * 直播时间推送，更新比赛即时时间和时间轴、文字直播
     */
    Handler mSocketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //时间推送
            if (msg.arg1 == 7) {
                String ws_json = (String) msg.obj;
                WebSocketStadiumKeepTime webSocketStadiumKeepTime = null;
                try {
                    webSocketStadiumKeepTime = JSON.parseObject(ws_json, WebSocketStadiumKeepTime.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    webSocketStadiumKeepTime = JSON.parseObject(ws_json, WebSocketStadiumKeepTime.class);
                }
                L.i("1029", "=======直播時間=====" + StadiumUtils.convertStringToInt(webSocketStadiumKeepTime.getData().get("time")));

                updatePushKeepTime(webSocketStadiumKeepTime);

            } else if (msg.arg1 == 6) { //直播事件

                String ws_json = (String) msg.obj;
                WebSocketStadiumLiveTextEvent webSocketStadiumLiveTextEvent = null;
                try {
                    webSocketStadiumLiveTextEvent = JSON.parseObject(ws_json, WebSocketStadiumLiveTextEvent.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    webSocketStadiumLiveTextEvent = JSON.parseObject(ws_json, WebSocketStadiumLiveTextEvent.class);

                }

                //  L.i("1029",ws_json);
                L.i("1029", "===直播事件===" + "msgId=" + webSocketStadiumLiveTextEvent.getData().get("msgId") + ",,,時間" + StadiumUtils.convertStringToInt(webSocketStadiumLiveTextEvent.getData().get("time")) + ",,,msgText=" + webSocketStadiumLiveTextEvent.getData().get("msgText"));

                if (AppConstants.isTestEnv) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    logger.debug("\n" + df.format(new Date()) + "===直播推送消息======\n" + ws_json + "\n");
                }
                Map<String, String> data = webSocketStadiumLiveTextEvent.getData();

                MatchTextLiveBean currMatchTextLiveBean = new MatchTextLiveBean(data.get("code"), data.get("msgId"), data.get("msgPlace"), data.get("showId"), data.get("fontStyle"), data.get("time"), data.get("msgText"), data.get("cancelEnNum"), data.get("enNum"), data.get("state"), data.get("homeScore"), data.get("guestScore"));

                if (currMatchTextLiveBean != null) {
                    isLostMsgId(currMatchTextLiveBean.getMsgId());
                }
                allMatchLiveMsgId.add(0, Integer.parseInt(currMatchTextLiveBean.getMsgId()));

                if (currMatchTextLiveBean != null) {
                    updatePushData(currMatchTextLiveBean);
                }
            }
        }
    };


    private void isLostMsgId(String currMsgId) {
        if (allMatchLiveMsgId == null) {
            return;
        }
        synchronized (allMatchLiveMsgId) {
            if (allMatchLiveMsgId != null && allMatchLiveMsgId.size() > 0) {
                Collections.sort(allMatchLiveMsgId, Collections.reverseOrder());

                if ((Integer.parseInt(currMsgId) - allMatchLiveMsgId.get(0) >= 2)) {
                    L.i("1028", "------" + currMsgId + "---" + allMatchLiveMsgId.get(0));
                    isRequestMsgIdRepeat(String.valueOf(allMatchLiveMsgId.get(0)), currMsgId);
                } else {
                    for (int i = 0; i < allMatchLiveMsgId.size() - 1; i++) {
                        boolean flag = false;
                        if ((allMatchLiveMsgId.get(i) - allMatchLiveMsgId.get(i + 1)) >= 2) {
                            L.i("1028", allMatchLiveMsgId.get(i) + "---" + allMatchLiveMsgId.get(i + 1));
                            isRequestMsgIdRepeat(String.valueOf(allMatchLiveMsgId.get(i + 1)), String.valueOf(allMatchLiveMsgId.get(i)));
                            flag = true;
                        }
                        if (flag) {
                            break;
                        }
                    }
                }
            }
        }
    }


    //请求是否存在重复


    private void isRequestMsgIdRepeat(String preMsgId, String msgId) {

        Map<String, String> msgIdParams = new HashMap<>();
        if (getActivity() == null) {
            return;
        }
        msgIdParams.put("thirdId", ((FootballMatchDetailActivity) getActivity()).mThirdId);
        msgIdParams.put("startTextId", String.valueOf(Integer.parseInt(preMsgId) + 1));
        msgIdParams.put("endTextId", String.valueOf(Integer.parseInt(msgId) - 1));


        String url = BaseURLs.URL_FOOTBALL_IS_LOST_MSG_INFO;

        VolleyContentFast.requestJsonByPost(url, msgIdParams, new VolleyContentFast.ResponseSuccessListener<PreLiveText>() {
            @Override
            public void onResponse(PreLiveText jsonObject) {
                // 访问成功
                List<MatchTextLiveBean> liveTextList;
                L.i("1028", "请求漏消息");
                if (jsonObject != null && "200".equals(jsonObject.getResult())) {
                    liveTextList = new ArrayList<MatchTextLiveBean>();
                    liveTextList = jsonObject.getLive();// 获取当前页的下一页数据
                    L.i("1028", "漏消息个数：" + liveTextList.size());

                    if (liveTextList.size() > 0) {
                        for (MatchTextLiveBean m : liveTextList) {
                            L.i("1028", "漏消息msgId=" + m.getMsgId());
                            boolean isRepeat = true;
                            for (Integer matchTextLiveBean : allMatchLiveMsgId) {
                                if (m.getMsgId().equals(matchTextLiveBean)) {
                                    isRepeat = false;
                                }
                            }
                            if (isRepeat) {
                                allMatchLiveMsgId.add(0, Integer.parseInt(m.getMsgId()));
                                updatePushData(m);

                            }
                        }
                    }
                } else {
                    L.i("1028", "请求漏消息失败");

                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, PreLiveText.class);
    }


    /**
     * 根据选择语言，改变推送接口语言环境
     *
     * @return
     */
    private String appendLanguage() {
        String lang = "zh";//默认中文
        if (MyApp.isLanguage.equals("rCN")) {
            // 如果是中文简体的语言环境
            lang = BaseURLs.LANGUAGE_SWITCHING_CN;
        } else if (MyApp.isLanguage.equals("rTW")) {
            // 如果是中文繁体的语言环境
            lang = BaseURLs.LANGUAGE_SWITCHING_TW;
        } else if (MyApp.isLanguage.equals("rEN")) {
            // 如果是英文环境
            lang = BaseURLs.LANGUAGE_SWITCHING_EN;
        } else if (MyApp.isLanguage.equals("rKO")) {
            // 如果是韩语环境
            lang = BaseURLs.LANGUAGE_SWITCHING_KO;
        } else if (MyApp.isLanguage.equals("rID")) {
            // 如果是印尼语
            lang = BaseURLs.LANGUAGE_SWITCHING_ID;
        } else if (MyApp.isLanguage.equals("rTH")) {
            // 如果是泰语
            lang = BaseURLs.LANGUAGE_SWITCHING_TH;
        } else if (MyApp.isLanguage.equals("rVI")) {
            // 如果是越南语
            lang = BaseURLs.LANGUAGE_SWITCHING_VI;
        }

        return lang.trim();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void initView() {
        final ScrollView prestadium_layout = (ScrollView) view.findViewById(R.id.prestadium_layout);
        prestadium_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        if (prestadium_layout.getScrollY() != 0) {// 处于顶部
                            if (getActivity() != null) {
                                ((FootballMatchDetailActivity) getActivity()).mRefreshLayout.setEnabled(false);
                            }
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (getActivity() != null) {
                            ((FootballMatchDetailActivity) getActivity()).mRefreshLayout.setEnabled(true);
                        }
                        break;
                }
                return false;
            }
        });

        pre_dataTime_txt = (TextView) view.findViewById(R.id.pre_dataTime_txt);//开始时间
        pre_dataDate_txt = (TextView) view.findViewById(R.id.pre_dataDate_txt);//开始时间
        pre_weather_data_txt = (TextView) view.findViewById(R.id.pre_weather_data_txt);//天气
        pre_site_data_txt = (TextView) view.findViewById(R.id.pre_site_data_txt);//场地
        pre_first_not_tv = (TextView) view.findViewById(R.id.pre_first_not_tv);
        pre_weather_img = (ImageView) view.findViewById(R.id.pre_weather_img);//天气图标

        pre_first_home_data_txt = (TextView) view.findViewById(R.id.pre_first_home_data_txt);//主队首发阵容
        pre_first_home_datas_txt = (TextView) view.findViewById(R.id.pre_first_home_datas_txt);//主队首发人员

        pre_first_guest_data_txt = (TextView) view.findViewById(R.id.pre_first_guest_data_txt);//克队首发阵容
        pre_first_guest_datas_txt = (TextView) view.findViewById(R.id.pre_first_guest_datas_txt);//客队首发人员


        tv_home_corner = (TextView) view.findViewById(R.id.tv_home_corner);
        tv_home_rc = (TextView) view.findViewById(R.id.tv_home_rc);
        tv_home_yc = (TextView) view.findViewById(R.id.tv_home_yc);
        tv_home_score = (TextView) view.findViewById(R.id.home_score);
        tv_home_danger = (TextView) view.findViewById(R.id.home_danger_attack);
        tv_home_shoot_correct = (TextView) view.findViewById(R.id.home_shoot_correct);
        tv_home_shoot_miss = (TextView) view.findViewById(R.id.home_shoot_miss);

        tv_guest_corner = (TextView) view.findViewById(R.id.tv_guest_corner);
        tv_guest_rc = (TextView) view.findViewById(R.id.tv_guest_rc);
        tv_guest_yc = (TextView) view.findViewById(R.id.tv_guest_yc);
        tv_guest_score = (TextView) view.findViewById(R.id.guest_score);
        tv_guest_danger = (TextView) view.findViewById(R.id.guest_danger_attack);
        tv_guest_shoot_correct = (TextView) view.findViewById(R.id.guest_shoot_correct);
        tv_guest_shoot_miss = (TextView) view.findViewById(R.id.guest_shoot_miss);


        keepTime = (TextView) view.findViewById(R.id.keepTime);
        halfScore = (TextView) view.findViewById(R.id.halfScore);
        tv_frequency = (TextView) view.findViewById(R.id.tv_frequency);

        //x时间轴
        timeView = (TimeView) view.findViewById(R.id.time_football);
        timeLayoutTop = (FootballEventView) view.findViewById(R.id.time_layout_top);
        timeLaoutBottom = (FootballEventView) view.findViewById(R.id.time_layout_bottom);

        tv_in = (LinearLayout) view.findViewById(R.id.tv_in);
        tv_out = (LinearLayout) view.findViewById(R.id.tv_out);
        ll_eventLayout = (LinearLayout) view.findViewById(R.id.ll_envet);
        tv_in.setOnClickListener(this);
        tv_out.setOnClickListener(this);

        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        pb_danger = (ProgressBar) view.findViewById(R.id.pb_danger_attack);
        pb_shoot_correct = (ProgressBar) view.findViewById(R.id.pb_shoot_correct);
        pb_shoot_miss = (ProgressBar) view.findViewById(R.id.pb_shoot_miss);

        frame_content = (FrameLayout) view.findViewById(R.id.frame_content);
        frame_content_players = (FrameLayout) view.findViewById(R.id.frame_content_players);
        frame_content_corner = (FrameLayout) view.findViewById(R.id.frame_content_corner);
        frame_content_attack = (FrameLayout) view.findViewById(R.id.frame_content_attack);

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
            view.findViewById(R.id.pre_first_layout).setVisibility(View.GONE);
            pre_first_not_tv.setText(R.string.firsPlayers_not);
            pre_first_not_tv.setVisibility(View.VISIBLE);
        } else {
            pre_first_home_data_txt.setText(mMatchDetail.getHomeTeamInfo().getName());//zhu队首发
            pre_first_home_datas_txt.setText(appendPlayerNames(mMatchDetail.getHomeTeamInfo().getLineup()));//zhu队首发人员
            pre_first_guest_data_txt.setText(mMatchDetail.getGuestTeamInfo().getName());//客队首发
            pre_first_guest_datas_txt.setText(appendPlayerNames(mMatchDetail.getGuestTeamInfo().getLineup()));//客队首发人员
        }
    }

    //外部调用更新实体对象
    public void setMatchDetail(MatchDetail matchDetail) {
        this.mMatchDetail = matchDetail;
    }

    //重赛前跳到赛中，比赛开始
    public void activateMatch() {
        view.findViewById(R.id.prestadium_layout).setVisibility(View.GONE);
        view.findViewById(R.id.stadium_layout).setVisibility(View.VISIBLE);

        initData(true);
        if ("0".equals(mMatchDetail.getLiveStatus()) || "1".equals(mMatchDetail.getLiveStatus())) {
            startWebsocket();
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

    /***
     * 完场初始化请求数据
     */
    private void initMatchOverData() {
        tv_home_corner.setText(mMatchDetail.getHomeTeamInfo().getCorner());
        tv_home_rc.setText(mMatchDetail.getHomeTeamInfo().getRc());
        tv_home_yc.setText(mMatchDetail.getHomeTeamInfo().getYc());
        tv_home_score.setText(mMatchDetail.getHomeTeamInfo().getScore());
        tv_home_danger.setText(mMatchDetail.getHomeTeamInfo().getDanger());
        tv_home_shoot_correct.setText(String.valueOf(Integer.parseInt(mMatchDetail.getHomeTeamInfo().getShot())));
        tv_home_shoot_miss.setText(mMatchDetail.getHomeTeamInfo().getAside());

        tv_guest_corner.setText(mMatchDetail.getGuestTeamInfo().getCorner());
        tv_guest_rc.setText(mMatchDetail.getGuestTeamInfo().getRc());
        tv_guest_yc.setText(mMatchDetail.getGuestTeamInfo().getYc());
        tv_guest_score.setText(mMatchDetail.getGuestTeamInfo().getScore());
        tv_guest_danger.setText(mMatchDetail.getGuestTeamInfo().getDanger());
        tv_guest_shoot_correct.setText(String.valueOf(Integer.parseInt(mMatchDetail.getGuestTeamInfo().getShot())));
        tv_guest_shoot_miss.setText(mMatchDetail.getGuestTeamInfo().getAside());
        pb_danger.setProgress((int) StadiumUtils.computeProgressbarPercent(mMatchDetail.getHomeTeamInfo().getDanger(), mMatchDetail.getGuestTeamInfo().getDanger()));
        pb_shoot_correct.setProgress((int) StadiumUtils.computeProgressbarPercent(mMatchDetail.getHomeTeamInfo().getShot(), mMatchDetail.getGuestTeamInfo().getShot()));
        pb_shoot_miss.setProgress((int) StadiumUtils.computeProgressbarPercent(mMatchDetail.getHomeTeamInfo().getAside(), mMatchDetail.getGuestTeamInfo().getAside()));
        halfScore.setText("(" + mMatchDetail.getHomeTeamInfo().getHalfScore() + ":" + mMatchDetail.getGuestTeamInfo().getHalfScore() + ")");
    }


    /**
     * 赛中初始化数据
     */
    private void initMatchNowData() {
        initMatchSatisInfo();
        tv_home_corner.setText(String.valueOf(mathchStatisInfo.getHome_corner()));
        tv_home_rc.setText(String.valueOf(mathchStatisInfo.getHome_rc()));
        tv_home_yc.setText(String.valueOf(mathchStatisInfo.getHome_yc()));
        tv_home_score.setText(String.valueOf(mathchStatisInfo.getHome_score()));
        tv_home_danger.setText(String.valueOf(mathchStatisInfo.getHome_danger()));
        tv_home_shoot_correct.setText(String.valueOf(mathchStatisInfo.getHome_shoot_correct()));
        tv_home_shoot_miss.setText(String.valueOf(mathchStatisInfo.getHome_shoot_miss()));

        tv_guest_corner.setText(String.valueOf(mathchStatisInfo.getGuest_corner()));
        tv_guest_rc.setText(String.valueOf(mathchStatisInfo.getGuest_rc()));
        tv_guest_yc.setText(String.valueOf(mathchStatisInfo.getGuest_yc()));
        tv_guest_score.setText(String.valueOf(mathchStatisInfo.getGuest_score()));
        tv_guest_danger.setText(String.valueOf(mathchStatisInfo.getGuest_danger()));
        tv_guest_shoot_correct.setText(String.valueOf(mathchStatisInfo.getGuest_shoot_correct()));
        tv_guest_shoot_miss.setText(String.valueOf(mathchStatisInfo.getGuest_shoot_miss()));

        pb_danger.setProgress((int) StadiumUtils.computeProgressbarPercent(String.valueOf(mathchStatisInfo.getHome_danger()), String.valueOf(mathchStatisInfo.getGuest_danger())));
        pb_shoot_correct.setProgress((int) StadiumUtils.computeProgressbarPercent(String.valueOf(mathchStatisInfo.getHome_shoot_correct()), String.valueOf(mathchStatisInfo.getGuest_shoot_correct())));
        pb_shoot_miss.setProgress((int) StadiumUtils.computeProgressbarPercent(String.valueOf(mathchStatisInfo.getHome_shoot_miss()), String.valueOf(mathchStatisInfo.getGuest_shoot_miss())));
        halfScore.setText("(" + mathchStatisInfo.getHome_half_score() + ":" + mathchStatisInfo.getGuest_half_score() + ")");

        homeCorners.clear();
        homeDangers.clear();
        guestCorners.clear();
        guestDangers.clear();
        homeCorners = initMatchTrend("1025", "1050", StadiumUtils.convertStringToInt(matchLive.get(0).getTime()));//通过最后一个事件来获取时间
        homeDangers = initMatchTrend("1026", null, StadiumUtils.convertStringToInt(matchLive.get(0).getTime()));
        guestCorners = initMatchTrend("2049", "2074", StadiumUtils.convertStringToInt(matchLive.get(0).getTime()));
        guestDangers = initMatchTrend("2050", null, StadiumUtils.convertStringToInt(matchLive.get(0).getTime()));
        homeCorners.add(0, 0);
        homeDangers.add(0, 0);
        guestCorners.add(0, 0);
        guestDangers.add(0, 0);
    }


    /**
     * 统计赛中红黄牌等信息
     */
    private void initMatchSatisInfo() {
        mathchStatisInfo = new MathchStatisInfo();
        for (MatchTextLiveBean m : matchLive) {
            switch (m.getCode().trim()) {
                case "1029": //主队进球
                    mathchStatisInfo.setHome_score(mathchStatisInfo.getHome_score() + 1);
                    mathchStatisInfo.setHome_shoot_correct(mathchStatisInfo.getHome_shoot_correct() + 1);
                    break;
                case "1030"://取消主队进球
                    mathchStatisInfo.setHome_score(mathchStatisInfo.getHome_score() - 1);
                    mathchStatisInfo.setHome_shoot_correct(mathchStatisInfo.getHome_shoot_correct() - 1);
                    break;
                case "1025"://主队角球
                    mathchStatisInfo.setHome_corner(mathchStatisInfo.getHome_corner() + 1);
                    break;
                case "1050"://取消主队角球
                    mathchStatisInfo.setHome_corner(mathchStatisInfo.getHome_corner() - 1);
                    break;
                case "1032":  //主队红牌
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() + 1);
                    break;
                case "1047"://取消主队红牌
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() - 1);
                    break;
                case "1034": //主队黄牌
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() + 1);
                    break;
                case "1048": //取消主队红牌
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() - 1);
                    break;
                case "1045"://主队两黄变一红
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() + 1);
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() + 1);
                    break;
                case "1046"://取消两黄变一红
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() - 1);
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() - 1);
                    break;
                case "1026"://主队危险进攻
                    mathchStatisInfo.setHome_danger(mathchStatisInfo.getHome_danger() + 1);
                    break;
                case "1040"://主队射偏
                    mathchStatisInfo.setHome_shoot_miss(mathchStatisInfo.getHome_shoot_miss() + 1);
                    break;
                case "1041"://主队中门框事件
                    mathchStatisInfo.setHome_shoot_miss(mathchStatisInfo.getHome_shoot_miss() + 1);
                    break;
                case "1039"://主队射正
                    mathchStatisInfo.setHome_shoot_correct(mathchStatisInfo.getHome_shoot_correct() + 1);
                    break;
                case "1043"://主队越位
                    mathchStatisInfo.setHome_away(mathchStatisInfo.getHome_away() + 1);
                    break;
                case "1027"://主队任意球
                    mathchStatisInfo.setHome_free_kick(mathchStatisInfo.getHome_free_kick() + 1);
                    break;
                case "1042": //主队犯规
                    mathchStatisInfo.setHome_foul(mathchStatisInfo.getHome_foul() + 1);
                    break;
                case "1054": //主队界外球
                    mathchStatisInfo.setHome_lineOut(mathchStatisInfo.getHome_lineOut() + 1);
                    break;
                case "2053": //可队进球
                    mathchStatisInfo.setGuest_score(mathchStatisInfo.getGuest_score() + 1);
                    mathchStatisInfo.setGuest_shoot_correct(mathchStatisInfo.getGuest_shoot_correct() + 1);
                    break;
                case "2054"://取消可队进球
                    mathchStatisInfo.setGuest_score(mathchStatisInfo.getGuest_score() - 1);
                    mathchStatisInfo.setGuest_shoot_correct(mathchStatisInfo.getGuest_shoot_correct() - 1);
                    break;
                case "2049"://客队角球
                    mathchStatisInfo.setGuest_corner(mathchStatisInfo.getGuest_corner() + 1);
                    break;
                case "2074"://取消客队角球
                    mathchStatisInfo.setGuest_corner(mathchStatisInfo.getGuest_corner() - 1);
                    break;
                case "2056":  //ke队红牌
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() + 1);
                    break;
                case "2071"://取消ke队红牌
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() - 1);
                    break;
                case "2058": //客队黄牌
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() + 1);
                    break;
                case "2072": //qu xiao ke dui huang pai
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() - 1);
                    break;
                case "2069"://2黄1红
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() + 1);
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() + 1);
                    break;
                case "2070"://取消2黄1红
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() - 1);
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() - 1);
                    break;
                case "2050"://危险
                    mathchStatisInfo.setGuest_danger(mathchStatisInfo.getGuest_danger() + 1);
                    break;
                case "2064"://射偏
                    mathchStatisInfo.setGuest_shoot_miss(mathchStatisInfo.getGuest_shoot_miss() + 1);
                    break;
                case "2065"://射偏
                    mathchStatisInfo.setGuest_shoot_miss(mathchStatisInfo.getGuest_shoot_miss() + 1);
                    break;
                case "2063"://射正
                    mathchStatisInfo.setGuest_shoot_correct(mathchStatisInfo.getGuest_shoot_correct() + 1);
                    break;
                case "2067":
                    mathchStatisInfo.setGuest_away(mathchStatisInfo.getGuest_away() + 1);
                    break;
                case "2051":
                    mathchStatisInfo.setGuest_free_kick(mathchStatisInfo.getGuest_free_kick() + 1);
                    break;
                case "1"://半场结束获取半场比分
                    mathchStatisInfo.setHome_half_score(Integer.parseInt(m.getHomeScore()));
                    mathchStatisInfo.setGuest_half_score(Integer.parseInt(m.getGuestScore()));
                    break;
                case "2066": //客队犯规
                    mathchStatisInfo.setGuest_foul(mathchStatisInfo.getGuest_foul() + 1);
                    break;
                case "2078": //客队界外球
                    mathchStatisInfo.setGuest_lineOut(mathchStatisInfo.getGuest_lineOut() + 1);
                    break;
                default:
                    break;
            }
        }

        mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());
        mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());
        mathchStatisInfo.setHome_rescue(mathchStatisInfo.getGuest_shoot_correct() - mathchStatisInfo.getGuest_score());
        mathchStatisInfo.setGuest_rescue(mathchStatisInfo.getHome_shoot_correct() - mathchStatisInfo.getHome_score());
    }

    /**
     * 按时间段统计走势图的数据
     *
     * @param code  进球的code
     * @param code2 取消的code
     * @param time  开赛的当前时间
     * @return 统计好的数据集合
     */
    private ArrayList<Integer> initMatchTrend(String code, String code2, int time) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        int time15 = 0, time30 = 0, time45 = 0, time60 = 0, time75 = 0, time90 = 0;

        for (MatchTextLiveBean matchTextLiveBean : matchLive) {
            Integer dd = StadiumUtils.convertStringToInt(matchTextLiveBean.getTime());
            if (code.equals(matchTextLiveBean.getCode())) {  //主队角球
                if (FIRSTHALF.equals(matchTextLiveBean.getState()) || HALFTIME.equals(matchTextLiveBean.getState())) {// 上半场和中场
                    if (dd >= 0 && dd <= 15 && time >= 15) {
                        time15++;
                    }
                    if (dd > 15 && dd <= 30 && time >= 30) {
                        time30++;
                    }
                    if (dd > 30 && time >= 45) {
                        time45++;
                    }
                } else if (SECONDHALF.equals(matchTextLiveBean.getState())) {// 下半场
                    if (dd >= 0 && dd <= 60 && time >= 60) {
                        time60++;
                    }
                    if (dd > 60 && dd <= 75 && time >= 75) {
                        time75++;
                    }
                    if (dd > 75 && time >= 90) {
                        time90++;
                    }
                }
            }
        }

        if (code2 != null) {
            for (MatchTextLiveBean matchTextLiveBean : matchLive) {
                if (code2.equals(matchTextLiveBean.getCode())) {  //主队角球
                    if (FIRSTHALF.equals(matchTextLiveBean.getState()) || HALFTIME.equals(matchTextLiveBean.getState())) {// 上半场和中场
                        if (time >= 0 && time <= 15) {
                            time15--;
                        }
                        if (time > 15 && time <= 30) {
                            time30--;
                        }
                        if (time > 30) {
                            time45--;
                        }
                    } else if (SECONDHALF.equals(matchTextLiveBean.getState())) {// 下半场
                        if (time >= 0 && time <= 60) {
                            time60--;
                        }
                        if (time > 60 && time <= 75) {
                            time75--;
                        }
                        if (time > 75) {
                            time90--;
                        }
                    }
                }
            }
        }


        if (time >= 15) {
            list.add(time15);
        }
        if (time >= 30) {
            list.add(time30 + time15);
        }
        if (time >= 45) {
            list.add(time45 + time30 + time15);
        }
        if (time >= 60) {
            list.add(time60 + time45 + time30 + time15);
        }
        if (time >= 75) {
            list.add(time75 + time60 + time45 + time30 + time15);
        }
        if (time >= 90) {
            list.add(time90 + time75 + time60 + time45 + time30 + time15);
        }

        return list;

    }


    private boolean isMatchStart = false;
    public static boolean isStart;// 判断是否完场

    /**
     * 比赛赛中、赛后初始化数据
     */
    private void initData(boolean flag) {
        matchLive = new ArrayList<MatchTextLiveBean>();
        xMatchLive = new ArrayList<MatchTimeLiveBean>();

        matchLive = mMatchDetail.getMatchInfo().getMatchLive();


        if (matchLive == null) {
            return;
        }


        allMatchLiveMsgId = new ArrayList<>();
        for (MatchTextLiveBean ml : matchLive) {
            allMatchLiveMsgId.add(Integer.parseInt(ml.getMsgId()));
        }

        if (LIVEENDED.equals(mMatchDetail.getLiveStatus())) {

            isStart = true;// 获取完场状态
            keepTime.setText(mContext.getResources().getString(R.string.finish_txt));
            tv_frequency.setText("");
            tv_home_score.setTextColor(getResources().getColor(R.color.score));
            tv_guest_score.setTextColor(getResources().getColor(R.color.score));

            xMatchLive = mMatchDetail.getMatchInfo().getMatchTimeLive();//完场直接有数据
            secondToMinute(xMatchLive);
            mKeepTime = "5400000";//90分钟的毫秒数
            initMatchOverData();

        } else {
            isStart = false;// 获取未完场状态
            initMatchNowData();

            String state = matchLive.get(0).getState();//获取最后一个的比赛状态
            mKeepTime = matchLive.get(0).getTime();//获取时间

            if (NOTOPEN.equals(state)) {
                //未开state=0
                keepTime.setText(mContext.getResources().getString(R.string.not_start_txt));
                tv_frequency.setText("");
                isMatchStart = false;

            } else if (FIRSTHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) <= 45) {
                //上半场state=1
                tv_frequency.setText("'");

                keepTime.setText(StadiumUtils.convertStringToInt(mKeepTime) + "");
                isMatchStart = true;

            } else if (FIRSTHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) > 45) {
                keepTime.setText("45+");
                tv_frequency.setText("'");
                isMatchStart = false;

            } else if (HALFTIME.equals(state)) {
                //中场state=2
                keepTime.setText(mContext.getResources().getString(R.string.pause_txt));
                tv_frequency.setText("");
                isMatchStart = false;
                mKeepTime = 45 * 60 * 1000 + "";

            } else if (SECONDHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) <= 90) {
                //下半场state=3
                tv_frequency.setText("'");
                keepTime.setText(StadiumUtils.convertStringToInt(mKeepTime) + "");
                isMatchStart = true;

            } else if (SECONDHALF.equals(state) && StadiumUtils.convertStringToInt(mKeepTime) > 90) {
                keepTime.setText("90+");
                tv_frequency.setText("'");
                isMatchStart = false;

            } else {
                keepTime.setText(StadiumUtils.convertStringToInt(mKeepTime) + "");
            }
            getTimeLive();//赛中则从文字直播中得到.此处进行是否上半场结束的判断来进行设置


            if (Integer.parseInt(mKeepTime) >= (45 * 60 * 1000)) {
                if (state.equals(FIRSTHALF) || state.equals(HALFTIME)) {//上半场补时中场时间轴不变
                    mKeepTime = 45 * 60 * 1000 + "";//时间继续赋值为45分钟
                }
            }
        }
        // showFootballEvent();//显示事件
        showFootballEventByState();
        showTimeView();//第一次进来画一下时间轴

        StadiumUtils.keepTimeAnimation(tv_frequency);
        if (StadiumUtils.isHalfScoreVisible(matchLive.get(0).getState())) {
            halfScore.setVisibility(View.VISIBLE);
        }

        //文字直播

        fragmentManager = getChildFragmentManager();
        if (fragmentManager == null) {
            return;
        }
        if (liveTextFragment == null || !liveTextFragment.isAdded()) {
            liveTextFragment = new LiveTextFragment(getActivity(), matchLive, mMatchDetail.getLiveStatus());
            fragmentManager.beginTransaction().add(R.id.frame_content, liveTextFragment).commitAllowingStateLoss();
        } else {
            //下拉刷新
            liveTextFragment.updateAdapter(matchLive, mMatchDetail.getLiveStatus());
        }


        if (flag) {

            //阵容
            firstPlayersFragment = new FirstPlayersFragment();

            if (!firstPlayersFragment.isAdded()) {
                fragmentManager.beginTransaction().add(R.id.frame_content_players, firstPlayersFragment).commitAllowingStateLoss();
            }

            //统计数据
            statisticsFragment = new StatisticsFragment();

            if (!statisticsFragment.isAdded()) {
                fragmentManager.beginTransaction().add(R.id.frame_content_corner, statisticsFragment).commitAllowingStateLoss();
            }

            //攻防走势
            trendFragment = new TrendFragment();
            if (!trendFragment.isAdded()) {
                fragmentManager.beginTransaction().add(R.id.frame_content_attack, trendFragment).commitAllowingStateLoss();
            }

            fragmentManager.beginTransaction().show(liveTextFragment).commitAllowingStateLoss();
            fragmentManager.beginTransaction().hide(firstPlayersFragment).commitAllowingStateLoss();
            fragmentManager.beginTransaction().hide(trendFragment).commitAllowingStateLoss();
            fragmentManager.beginTransaction().hide(statisticsFragment).commitAllowingStateLoss();

        }
        //场上阵容


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();

                switch (radioButtonId) {

                    case R.id.rb_live_text:
                        MobclickAgent.onEvent(mContext, "Football_MatchData_LiveTextBtn");
                        Log.d("1029", "rb_live_text");
                        frame_content.setVisibility(View.VISIBLE);
                        frame_content_corner.setVisibility(View.GONE);
                        frame_content_players.setVisibility(View.GONE);
                        frame_content_attack.setVisibility(View.GONE);
                        fragmentManager.beginTransaction().hide(firstPlayersFragment).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().hide(statisticsFragment).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().hide(trendFragment).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().show(liveTextFragment).commitAllowingStateLoss();
                        break;

                    case R.id.rb_corner_trend:
                        MobclickAgent.onEvent(mContext, "Football_MatchData_StatisticsBtn");
                        frame_content.setVisibility(View.GONE);
                        frame_content_players.setVisibility(View.GONE);
                        frame_content_corner.setVisibility(View.VISIBLE);
                        frame_content_attack.setVisibility(View.GONE);
                        fragmentManager.beginTransaction().hide(liveTextFragment).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().hide(firstPlayersFragment).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().hide(trendFragment).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().show(statisticsFragment).commitAllowingStateLoss();

                        break;


                    case R.id.rb_first_players:
                        MobclickAgent.onEvent(mContext, "Football_MatchData_FirstPlayersBtn");
                        frame_content.setVisibility(View.GONE);
                        frame_content_players.setVisibility(View.VISIBLE);
                        frame_content_corner.setVisibility(View.GONE);
                        frame_content_attack.setVisibility(View.GONE);
                        fragmentManager.beginTransaction().hide(liveTextFragment).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().hide(statisticsFragment).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().hide(trendFragment).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().show(firstPlayersFragment).commitAllowingStateLoss();

                        break;

                    case R.id.rb_attack_trend:
                        MobclickAgent.onEvent(mContext, "Football_MatchData_TrendBtn");
                        frame_content.setVisibility(View.GONE);
                        frame_content_players.setVisibility(View.GONE);
                        frame_content_corner.setVisibility(View.GONE);
                        frame_content_attack.setVisibility(View.VISIBLE);
                        fragmentManager.beginTransaction().hide(liveTextFragment).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().hide(firstPlayersFragment).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().show(trendFragment).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().hide(statisticsFragment).commitAllowingStateLoss();

                        break;

                    default:
                        break;

                }
            }
        });


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
     * 赛中、统计、攻防趋势、数据统计下拉刷新
     *
     * @param mDetail
     */


    public void refreshStadiumData(MatchDetail mDetail) {
        L.i("1029", "下拉刷新！");
        //刷新重新请求
        if (mDetail == null) {
            return;
        }

        mMatchDetail = mDetail;
        initData(false);
        if (statisticsFragment.isVisible()) {
            statisticsFragment.initData();
        }
        if (trendFragment.isVisible()) {
            trendFragment.initData();
        }
        if (firstPlayersFragment.isVisible()) {
            firstPlayersFragment.refershData();
        }

       /* //区分下拉刷新和Socket close
        if (hSocketClient != null) {
            if (hSocketClient.isClosed()) {
                startWebsocket();
            }
        }*/

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

    @Override
    public void onResume() {
        super.onResume();
        if (BEFOURLIVE.equals(mMatchDetail.getLiveStatus()) || ONLIVE.equals(mMatchDetail.getLiveStatus())) {
            startWebsocket();
            computeWebSocket();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        L.i("1029", "onDestroyView");
        computeWebSocketConnTimer.cancel();

        if (hSocketClient != null) {
            hSocketClient.close();
        }
    }

    private synchronized void startWebsocket() {
        if (hSocketClient != null) {
            if (!hSocketClient.isClosed()) {
                hSocketClient.close();
            }

            hSocketClient = new HappySocketClient(hSocketUri, new Draft_17());
            hSocketClient.setSocketResponseMessageListener(this);
            hSocketClient.setSocketResponseCloseListener(this);
            hSocketClient.setSocketResponseErrorListener(this);
            try {
                hSocketClient.connect();
            } catch (IllegalThreadStateException e) {
                hSocketClient.close();
            }
        } else {
            hSocketClient = new HappySocketClient(hSocketUri, new Draft_17());
            hSocketClient.setSocketResponseMessageListener(this);
            hSocketClient.setSocketResponseCloseListener(this);
            hSocketClient.setSocketResponseErrorListener(this);
            try {
                hSocketClient.connect();
            } catch (IllegalThreadStateException e) {
                hSocketClient.close();
            }
        }
    }


    /**
     * 比赛时间的推送
     *
     * @param webSocketStadiumKeepTime
     */
    private void updatePushKeepTime(WebSocketStadiumKeepTime webSocketStadiumKeepTime) {
        Map<String, String> data = webSocketStadiumKeepTime.getData();
        mKeepTime = data.get("time");
        matchKeepTime = StadiumUtils.convertStringToInt(data.get("time"));
        String state = data.get("state");//比赛的状态
        if (Integer.parseInt(mKeepTime) > (45 * 60 * 1000)) {
            if (state.equals(FIRSTHALF) || state.equals(HALFTIME)) {//上半场补时中场时间轴不变
                mKeepTime = 45 * 60 * 1000 + "";//时间继续赋值为45分钟
            }
        }

        timeView.updateTime(Integer.parseInt(mKeepTime));

        if (isMatchStart) {

            if (liveTextTime >= matchKeepTime) {
                keepTime.setText(liveTextTime + "");
            } else {
                if (FIRSTHALF.equals(state) && matchKeepTime <= 45) {
                    keepTime.setText(matchKeepTime + "");
                } else if (FIRSTHALF.equals(state) && matchKeepTime > 45) {
                    keepTime.setText("45+");
                } else if (SECONDHALF.equals(state) && matchKeepTime <= 90) {
                    keepTime.setText(matchKeepTime + "");
                } else if (SECONDHALF.equals(state) && matchKeepTime > 90) {
                    keepTime.setText("90+");
                }
            }
            mStartTime = StadiumUtils.convertStringToInt(data.get("time"));
            switch (mStartTime) {
                case 15:
                case 30:
                case 45:
                case 60:
                case 75:
                case 90:
                    homeCorners.clear();
                    guestCorners.clear();
                    homeDangers.clear();
                    guestDangers.clear();
                    homeCorners = initMatchTrend("1025", "1050", mStartTime);
                    guestCorners = initMatchTrend("2049", "2074", mStartTime);
                    homeDangers = initMatchTrend("1026", null, mStartTime);
                    guestDangers = initMatchTrend("2050", null, mStartTime);
                    homeCorners.add(0, 0);
                    guestCorners.add(0, 0);
                    homeDangers.add(0, 0);
                    guestDangers.add(0, 0);
                    trendFragment.initData();// 刷新攻防走势图
                    statisticsFragment.initData();// 刷新角球走势图
                    break;
            }
        }
    }


    /**
     * 比赛数据的推送
     *
     * @param matchTextLiveBean
     */
    private synchronized void updatePushData(MatchTextLiveBean matchTextLiveBean) {

        Iterator<MatchTimeLiveBean> iterator = null;
        if (xMatchLive != null && xMatchLive.size() > 0) {
            iterator = xMatchLive.iterator();//时间轴直播数据的iterator
        }
        liveTextTime = StadiumUtils.convertStringToInt(matchTextLiveBean.getTime());
        if (NOTOPEN.equals(matchTextLiveBean.getState())) { //未开
            keepTime.setText(mContext.getResources().getString(R.string.not_start_txt));
            isMatchStart = true;
        } else if (FIRSTHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) <= 45) {

            if (liveTextTime >= matchKeepTime) {
                keepTime.setText(liveTextTime + "");
            } else {
                if (matchKeepTime > 45) {
                    keepTime.setText("45+");
                } else {
                    keepTime.setText(matchKeepTime + "");
                }
            }

            tv_frequency.setText("'");  //上半场
            isMatchStart = true;
        } else if (FIRSTHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 45) {
            keepTime.setText("45+");  //上半场45+
            tv_frequency.setText("'");
            isMatchStart = false;
        } else if (HALFTIME.equals(matchTextLiveBean.getState())) {  //中场
            keepTime.setText(mContext.getResources().getString(R.string.pause_txt));
            tv_frequency.setText("");
            isMatchStart = false;
            halfScore.setVisibility(View.VISIBLE);
            halfScore.setText("(" + tv_home_score.getText() + ":" + tv_guest_score.getText() + ")");
        } else if (SECONDHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) <= 90) {
            if (liveTextTime >= matchKeepTime) {
                keepTime.setText(liveTextTime + "");
            } else {
                if (matchKeepTime > 90) {
                    keepTime.setText("90+");
                } else {
                    keepTime.setText(matchKeepTime + "");
                }
            }
            tv_frequency.setText("'"); //下半场
            isMatchStart = true;
        } else if (SECONDHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 90) {
            keepTime.setText("90+");
            tv_frequency.setText("'");
            isMatchStart = false;
        }
//        else if ("16".equals(matchTextLiveBean.getState()) || "32".equals(matchTextLiveBean.getState())) {
//        }


        switch (matchTextLiveBean.getCode()) {
            case "0":
            case "10":
            case "11":  //开始上半场，开球：
                break;
            case "1"://上半场结束
                halfScore.setVisibility(View.VISIBLE);
                halfScore.setText("(" + tv_home_score.getText() + ":" + tv_guest_score.getText() + ")");
                keepTime.setText("中场");
                tv_frequency.setText("");
                isMatchStart = false;
                // 获取上半场的走势图数据
                homeCorners.clear();
                guestCorners.clear();
                homeDangers.clear();
                guestDangers.clear();
                homeCorners = initMatchTrend("1025", "1050", mStartTime);
                guestCorners = initMatchTrend("2049", "2074", mStartTime);
                homeDangers = initMatchTrend("1026", null, mStartTime);
                guestDangers = initMatchTrend("2050", null, mStartTime);
                homeCorners.add(0, 0);
                guestCorners.add(0, 0);
                homeDangers.add(0, 0);
                guestDangers.add(0, 0);
                trendFragment.initData();// 刷新攻防走势图
                statisticsFragment.initData();// 刷新统计
                break;
            case "12":
            case "13":  //下半场开始
                tv_frequency.setText("'");
                break;
            case "3": //结束下半场
                //socket关闭
                keepTime.setText(mContext.getResources().getString(R.string.finish_txt));
                tv_home_score.setTextColor(getResources().getColor(R.color.score));
                tv_guest_score.setTextColor(getResources().getColor(R.color.score));

                tv_frequency.setText("");
                // 赛场后更新走势图数据
                isStart = true;
                trendFragment.initData();// 刷新攻防走势图

                statisticsFragment.initData();// 刷新统计

                //点赞不可点
                ((FootballMatchDetailActivity) getActivity()).finishMatch();

                if (hSocketClient != null) {
                    if (!hSocketClient.isClosed()) {
                        hSocketClient.close();
                    }
                }

                break;
            case "1029": //主队进球

                if ("".equals(matchTextLiveBean.getHomeScore()) || "".equals(matchTextLiveBean.getGuestScore())) {
                    mathchStatisInfo.setHome_score(mathchStatisInfo.getHome_score() + 1);
                    tv_home_score.setText(mathchStatisInfo.getHome_score() + "");
                } else {

                    mathchStatisInfo.setHome_score(Integer.parseInt(matchTextLiveBean.getHomeScore()));
                    mathchStatisInfo.setGuest_score(Integer.parseInt(matchTextLiveBean.getGuestScore()));
                    tv_home_score.setText(mathchStatisInfo.getHome_score() + "");
                    tv_guest_score.setText(mathchStatisInfo.getGuest_score() + "");
                }

                mathchStatisInfo.setHome_shoot_correct(mathchStatisInfo.getHome_shoot_correct() + 1);

                tv_home_shoot_correct.setText(String.valueOf(mathchStatisInfo.getHome_shoot_correct()));
                pb_shoot_correct.setProgress((int) StadiumUtils.computeProgressbarPercent(mathchStatisInfo.getHome_shoot_correct() + "", mathchStatisInfo.getGuest_shoot_correct() + ""));

                mathchStatisInfo.setGuest_rescue(mathchStatisInfo.getHome_shoot_correct() - mathchStatisInfo.getHome_score());


                //x时间轴
                addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                // showFootballEvent();//显示事件
                showFootballEventByState();
                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }
                break;
            case "1030"://取消主队进球

                if (mathchStatisInfo.getHome_score() > 0) {
                    if ("".equals(matchTextLiveBean.getHomeScore()) || "".equals(matchTextLiveBean.getGuestScore())) {
                        mathchStatisInfo.setHome_score(mathchStatisInfo.getHome_score() - 1);
                        tv_home_score.setText(mathchStatisInfo.getHome_score() + "");
                    } else {

                        mathchStatisInfo.setHome_score(Integer.parseInt(matchTextLiveBean.getHomeScore()));
                        mathchStatisInfo.setGuest_score(Integer.parseInt(matchTextLiveBean.getGuestScore()));
                        tv_home_score.setText(mathchStatisInfo.getHome_score() + "");
                        tv_guest_score.setText(mathchStatisInfo.getGuest_score() + "");
                    }
                }
                if (mathchStatisInfo.getHome_shoot_correct() > 0) {
                    mathchStatisInfo.setHome_shoot_correct(mathchStatisInfo.getHome_shoot_correct() - 1);
                    tv_home_shoot_correct.setText(String.valueOf(mathchStatisInfo.getHome_shoot_correct()));
                    pb_shoot_correct.setProgress((int) StadiumUtils.computeProgressbarPercent(mathchStatisInfo.getHome_shoot_correct() + "", mathchStatisInfo.getGuest_shoot_correct() + ""));
                }
                mathchStatisInfo.setGuest_rescue(mathchStatisInfo.getHome_shoot_correct() - mathchStatisInfo.getHome_score());


                cancelFootBallEvent(iterator, matchTextLiveBean);
                // showFootballEvent();//显示事件
                showFootballEventByState();


                break;
            case "2053"://客队进球


                if ("".equals(matchTextLiveBean.getHomeScore()) || "".equals(matchTextLiveBean.getGuestScore())) {
                    mathchStatisInfo.setGuest_score(mathchStatisInfo.getGuest_score() + 1);
                    tv_guest_score.setText(mathchStatisInfo.getGuest_score() + "");
                } else {
                    mathchStatisInfo.setHome_score(Integer.parseInt(matchTextLiveBean.getHomeScore()));
                    mathchStatisInfo.setGuest_score(Integer.parseInt(matchTextLiveBean.getGuestScore()));
                    tv_home_score.setText(mathchStatisInfo.getHome_score() + "");
                    tv_guest_score.setText(mathchStatisInfo.getGuest_score() + "");
                }


                mathchStatisInfo.setGuest_shoot_correct(mathchStatisInfo.getGuest_shoot_correct() + 1);

                tv_guest_shoot_correct.setText(String.valueOf(mathchStatisInfo.getGuest_shoot_correct()));
                pb_shoot_correct.setProgress((int) StadiumUtils.computeProgressbarPercent(mathchStatisInfo.getHome_shoot_correct() + "", mathchStatisInfo.getGuest_shoot_correct() + ""));

                mathchStatisInfo.setHome_rescue(mathchStatisInfo.getGuest_shoot_correct() - mathchStatisInfo.getGuest_score());


                addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                // showFootballEvent();//显示事件
                showFootballEventByState();


                break;

            case "2054"://取消客队进球
                if (mathchStatisInfo.getGuest_score() > 0) {
                    if ("".equals(matchTextLiveBean.getHomeScore()) || "".equals(matchTextLiveBean.getGuestScore())) {
                        mathchStatisInfo.setGuest_score(mathchStatisInfo.getGuest_score() - 1);
                        tv_guest_score.setText(mathchStatisInfo.getGuest_score() + "");
                    } else {
                        mathchStatisInfo.setHome_score(Integer.parseInt(matchTextLiveBean.getHomeScore()));
                        mathchStatisInfo.setGuest_score(Integer.parseInt(matchTextLiveBean.getGuestScore()));
                        tv_home_score.setText(mathchStatisInfo.getHome_score() + "");
                        tv_guest_score.setText(mathchStatisInfo.getGuest_score() + "");
                    }
                }
                if (mathchStatisInfo.getGuest_shoot_correct() > 0) {
                    mathchStatisInfo.setGuest_shoot_correct(mathchStatisInfo.getGuest_shoot_correct() - 1);
                    tv_guest_shoot_correct.setText(String.valueOf(mathchStatisInfo.getGuest_shoot_correct()));
                    pb_shoot_correct.setProgress((int) StadiumUtils.computeProgressbarPercent(mathchStatisInfo.getHome_shoot_correct() + "", mathchStatisInfo.getGuest_shoot_correct() + ""));
                }

                mathchStatisInfo.setHome_rescue(mathchStatisInfo.getGuest_shoot_correct() - mathchStatisInfo.getGuest_score());

                cancelFootBallEvent(iterator, matchTextLiveBean);
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;

            case "1025": //主队角球
                mathchStatisInfo.setHome_corner(mathchStatisInfo.getHome_corner() + 1);
                tv_home_corner.setText(String.valueOf(mathchStatisInfo.getHome_corner()));
                addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                // showFootballEvent();//显示事件
                showFootballEventByState();

                break;

            case "1050": //取消主队角球

                if (mathchStatisInfo.getHome_corner() > 0) {
                    mathchStatisInfo.setHome_corner(mathchStatisInfo.getHome_corner() - 1);
                    tv_home_corner.setText(String.valueOf(mathchStatisInfo.getHome_corner()));
                }
                cancelFootBallEvent(iterator, matchTextLiveBean);
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;

            case "2049": //客队角球
                mathchStatisInfo.setGuest_corner(mathchStatisInfo.getGuest_corner() + 1);
                tv_guest_corner.setText(String.valueOf(mathchStatisInfo.getGuest_corner()));
                addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                // showFootballEvent();//显示事件
                showFootballEventByState();

                break;

            case "2074": //取消客队角球
                if (mathchStatisInfo.getGuest_corner() > 0) {
                    mathchStatisInfo.setGuest_corner(mathchStatisInfo.getGuest_corner() - 1);
                    tv_guest_corner.setText(String.valueOf(mathchStatisInfo.getGuest_corner()));
                }
                // showFootballEvent();//显示事件
                cancelFootBallEvent(iterator, matchTextLiveBean);
                showFootballEventByState();

                break;

            case "1034":   //主队黄牌
                mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() + 1);
                tv_home_yc.setText(String.valueOf(mathchStatisInfo.getHome_yc()));
                addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;

            case "1048": //取消 主队黄牌
                if (mathchStatisInfo.getHome_yc() > 0) {
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() - 1);
                    tv_home_yc.setText(String.valueOf(mathchStatisInfo.getHome_yc()));
                }
                cancelFootBallEvent(iterator, matchTextLiveBean);
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;

            case "1045": //主队两黄变一红

                mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() + 1);
                mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() + 1);
                tv_home_yc.setText(String.valueOf(mathchStatisInfo.getHome_yc()));
                tv_home_rc.setText(String.valueOf(mathchStatisInfo.getHome_rc()));

                addFootballEvent(matchTextLiveBean);
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;

            case "1046": //取消主队黄牌过度到红牌
                if (mathchStatisInfo.getHome_yc() > 0 && mathchStatisInfo.getHome_rc() > 0) {
                    mathchStatisInfo.setHome_yc(mathchStatisInfo.getHome_yc() - 1);
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() - 1);
                    tv_home_yc.setText(String.valueOf(mathchStatisInfo.getHome_yc()));
                    tv_home_rc.setText(String.valueOf(mathchStatisInfo.getHome_rc()));
                }
                cancelFootBallEvent(iterator, matchTextLiveBean);
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;


            case "2058":  //客队黄牌
                mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() + 1);
                tv_guest_yc.setText(String.valueOf(mathchStatisInfo.getGuest_yc()));


                addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;

            case "2072":  //取消客队黄牌
                if (mathchStatisInfo.getGuest_yc() > 0) {
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() - 1);
                    tv_guest_yc.setText(String.valueOf(mathchStatisInfo.getGuest_yc()));
                }
                cancelFootBallEvent(iterator, matchTextLiveBean);
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;


            case "2069": //客队两黄变一红
                mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() + 1);
                mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() + 1);
                tv_guest_yc.setText(String.valueOf(mathchStatisInfo.getGuest_yc()));
                tv_guest_rc.setText(String.valueOf(mathchStatisInfo.getGuest_rc()));

                addFootballEvent(matchTextLiveBean);
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;

            case "2070": //取消客队两黄变一红

                if (mathchStatisInfo.getGuest_yc() > 0 && mathchStatisInfo.getGuest_rc() > 0) {
                    mathchStatisInfo.setGuest_yc(mathchStatisInfo.getGuest_yc() - 1);
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() - 1);
                    tv_guest_yc.setText(String.valueOf(mathchStatisInfo.getGuest_yc()));
                    tv_guest_rc.setText(String.valueOf(mathchStatisInfo.getGuest_rc()));
                }
                cancelFootBallEvent(iterator, matchTextLiveBean);
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;

            case "1032":  //主队红牌
                mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() + 1);
                tv_home_rc.setText(String.valueOf(mathchStatisInfo.getHome_rc()));
                addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;
            case "1047": //取消主队红牌
                if (mathchStatisInfo.getHome_rc() > 0) {
                    mathchStatisInfo.setHome_rc(mathchStatisInfo.getHome_rc() - 1);
                    tv_home_rc.setText(String.valueOf(mathchStatisInfo.getHome_rc()));
                }
                cancelFootBallEvent(iterator, matchTextLiveBean);
                // showFootballEvent();//显示事件
                showFootballEventByState();

                break;

            case "2056":  //客队红牌
                mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() + 1);
                tv_guest_rc.setText(String.valueOf(mathchStatisInfo.getGuest_rc()));
                addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;

            case "2071":  //取消客队红牌
                if (mathchStatisInfo.getGuest_rc() > 0) {
                    mathchStatisInfo.setGuest_rc(mathchStatisInfo.getGuest_rc() - 1);
                    tv_guest_rc.setText(String.valueOf(mathchStatisInfo.getGuest_rc()));
                }
                cancelFootBallEvent(iterator, matchTextLiveBean);
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;

            case "1026":    //主队危险进攻
                mathchStatisInfo.setHome_danger(mathchStatisInfo.getHome_danger() + 1);
                tv_home_danger.setText(String.valueOf(mathchStatisInfo.getHome_danger()));
                pb_danger.setProgress((int) StadiumUtils.computeProgressbarPercent(mathchStatisInfo.getHome_danger() + "", mathchStatisInfo.getGuest_danger() + ""));

                break;

            case "2050":  //客队危进攻
                mathchStatisInfo.setGuest_danger(mathchStatisInfo.getGuest_danger() + 1);
                tv_guest_danger.setText(String.valueOf(mathchStatisInfo.getGuest_danger()));
                pb_danger.setProgress((int) StadiumUtils.computeProgressbarPercent(mathchStatisInfo.getHome_danger() + "", mathchStatisInfo.getGuest_danger() + ""));

                break;

            case "1039":    //主队射正球门

                mathchStatisInfo.setHome_shoot_correct(mathchStatisInfo.getHome_shoot_correct() + 1);
                tv_home_shoot_correct.setText(String.valueOf(mathchStatisInfo.getHome_shoot_correct()));
                pb_shoot_correct.setProgress((int) StadiumUtils.computeProgressbarPercent(mathchStatisInfo.getHome_shoot_correct() + "", mathchStatisInfo.getGuest_shoot_correct() + ""));

                mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());

                //客队扑救=主队射正-主队比分
                mathchStatisInfo.setGuest_rescue(mathchStatisInfo.getHome_shoot_correct() - mathchStatisInfo.getHome_score());
                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }
                break;

            case "2063":  //客队射正球门
                mathchStatisInfo.setGuest_shoot_correct(mathchStatisInfo.getGuest_shoot_correct() + 1);
                tv_guest_shoot_correct.setText(String.valueOf(mathchStatisInfo.getGuest_shoot_correct()));
                pb_shoot_correct.setProgress((int) StadiumUtils.computeProgressbarPercent(mathchStatisInfo.getHome_shoot_correct() + "", mathchStatisInfo.getGuest_shoot_correct() + ""));

                mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());

                //主队扑救=客队射正-客队比分
                mathchStatisInfo.setHome_rescue(mathchStatisInfo.getGuest_shoot_correct() - mathchStatisInfo.getGuest_score());
                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }
                break;

            case "1040":    //主队射偏球门
                mathchStatisInfo.setHome_shoot_miss(mathchStatisInfo.getHome_shoot_miss() + 1);

                tv_home_shoot_miss.setText(String.valueOf(mathchStatisInfo.getHome_shoot_miss()));
                pb_shoot_miss.setProgress((int) StadiumUtils.computeProgressbarPercent(mathchStatisInfo.getHome_shoot_miss() + "", mathchStatisInfo.getGuest_shoot_miss() + ""));

                mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());

                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据
                break;
            case "1041":    //主队射偏球门
                mathchStatisInfo.setHome_shoot_miss(mathchStatisInfo.getHome_shoot_miss() + 1);

                tv_home_shoot_miss.setText(String.valueOf(mathchStatisInfo.getHome_shoot_miss()));
                pb_shoot_miss.setProgress((int) StadiumUtils.computeProgressbarPercent(mathchStatisInfo.getHome_shoot_miss() + "", mathchStatisInfo.getGuest_shoot_miss() + ""));

                mathchStatisInfo.setHome_shoot_door(mathchStatisInfo.getHome_shoot_correct() + mathchStatisInfo.getHome_shoot_miss());

                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据
                break;


            case "2064":  //客队射偏球门
                mathchStatisInfo.setGuest_shoot_miss(mathchStatisInfo.getGuest_shoot_miss() + 1);
                tv_guest_shoot_miss.setText(String.valueOf(mathchStatisInfo.getGuest_shoot_miss()));
                pb_shoot_miss.setProgress((int) StadiumUtils.computeProgressbarPercent(mathchStatisInfo.getHome_shoot_miss() + "", mathchStatisInfo.getGuest_shoot_miss() + ""));

                mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());

                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据
                break;
            case "2065":  //客队门框
                mathchStatisInfo.setGuest_shoot_miss(mathchStatisInfo.getGuest_shoot_miss() + 1);
                tv_guest_shoot_miss.setText(String.valueOf(mathchStatisInfo.getGuest_shoot_miss()));
                pb_shoot_miss.setProgress((int) StadiumUtils.computeProgressbarPercent(mathchStatisInfo.getHome_shoot_miss() + "", mathchStatisInfo.getGuest_shoot_miss() + ""));

                mathchStatisInfo.setGuest_shoot_door(mathchStatisInfo.getGuest_shoot_correct() + mathchStatisInfo.getGuest_shoot_miss());

                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据
                break;


            case "1055"://主队换人
                addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;

            case "2079": //客队换人
                addFootballEvent(matchTextLiveBean);
                //事件放入并展示
                // showFootballEvent();//显示事件
                showFootballEventByState();
                break;


            case "1043"://主队越位
                mathchStatisInfo.setHome_away(mathchStatisInfo.getHome_away() + 1);
                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据
                break;

            case "1027": //主队任意球
                mathchStatisInfo.setHome_free_kick(mathchStatisInfo.getHome_free_kick() + 1);
                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据
                break;

            case "2067":  //客队越位
                mathchStatisInfo.setGuest_away(mathchStatisInfo.getGuest_away() + 1);
                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据
                break;
            case "2051"://客队任意球
                mathchStatisInfo.setGuest_free_kick(mathchStatisInfo.getGuest_free_kick() + 1);
                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据
                break;

            case "1042"://主队犯规
                mathchStatisInfo.setHome_foul(mathchStatisInfo.getHome_foul() + 1);
                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据
                break;
            case "1054"://主队界外球
                mathchStatisInfo.setHome_lineOut(mathchStatisInfo.getHome_lineOut() + 1);
                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据
                break;
            case "2066"://客队犯规
                mathchStatisInfo.setGuest_foul(mathchStatisInfo.getGuest_foul() + 1);
                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据
                break;
            case "2078"://客队界外球
                mathchStatisInfo.setGuest_lineOut(mathchStatisInfo.getGuest_lineOut() + 1);
                if (statisticsFragment.isVisible()) {
                    statisticsFragment.initData();
                }//如果有变动就刷新统计数据
                break;
            case "256": //取消（不显示，指定消息取消）
            case "257"://清除（多个指定消息不显示）
                if (matchTextLiveBean.getCancelEnNum() != null && !"".equals(matchTextLiveBean.getCancelEnNum())) {
                    List<String> list = Arrays.asList(matchTextLiveBean.getCancelEnNum().split(":"));
                    if (list.size() > 0) {
                        for (MatchTextLiveBean m : matchLive) {
                            if (matchTextLiveBean.getEnNum().equals(m.getEnNum())) {
                                m.setShowId("1");
                            }
                            for (String ennum : list) {
                                if (ennum.equals(m.getEnNum())) {
                                    m.setShowId("1");
                                }
                            }
                        }
                    }
                }

                break;
            default:
                break;
        }


        if ("0".equals(matchTextLiveBean.getShowId()) && !"1".equals(matchTextLiveBean.getShowId())) {
            matchLive.add(0, matchTextLiveBean);
            Collections.sort(matchLive, new FootballLiveTextComparator()); //排序
            liveTextFragment.getTimeAdapter().notifyDataSetChanged();
        }


        //自己推送一条结束消息     -1 完场
        if (MATCHFINISH.equals(matchTextLiveBean.getState())) {
            matchLive.add(0, new MatchTextLiveBean("", "", "0", "0", "4", "99999999", mContext.getResources().getString(R.string.matchFinished_txt), "", "", "0", "", ""));
            liveTextFragment.getTimeAdapter().notifyDataSetChanged();
        }


    }

    /**
     * 赛中时获取时间轴数据
     */
    private void getTimeLive() {
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
     * 把事件从文字直播集合中取出加到时间轴集合中
     *
     * @param matchTextLiveBean
     */
    private void addFootballEvent(MatchTextLiveBean matchTextLiveBean) {
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
     * @param iterator
     */
    private void cancelFootBallEvent(Iterator<MatchTimeLiveBean> iterator, MatchTextLiveBean matchTextLiveBean) {
        while (iterator.hasNext()) {
            MatchTimeLiveBean bean = iterator.next();
            if (bean.getMsgId().equals(matchTextLiveBean.getCancelEnNum())) {//取消进球等事件的判断
                iterator.remove();//用xMatchLive.remove会有异常
            }
        }
    }

    /**
     * 展示足球事件,通过上半场结束事件判断
     */
    private void showFootballEvent() {
        Collections.sort(xMatchLive, new FootballEventComparator());
        //F表示上半场的数据。FtimeEventMap1 上半场主队
        Map<String, List<MatchTimeLiveBean>> FtimeEventMap1 = new LinkedHashMap<>();//LinkedHashMap保证map有序
        Map<String, List<MatchTimeLiveBean>> FtimeEventMap2 = new LinkedHashMap<>();
        Map<String, List<MatchTimeLiveBean>> StimeEventMap1 = new LinkedHashMap<>();//LinkedHashMap保证map有序
        Map<String, List<MatchTimeLiveBean>> StimeEventMap2 = new LinkedHashMap<>();//下半场数据

        boolean firstOver = false;//上半场结束。默认没结束

        for (MatchTimeLiveBean data : xMatchLive) {
            List<MatchTimeLiveBean> Ftemp1 = FtimeEventMap1.get(data.getTime());
            List<MatchTimeLiveBean> Ftemp2 = FtimeEventMap2.get(data.getTime());
            List<MatchTimeLiveBean> Stemp1 = StimeEventMap1.get(data.getTime());
            List<MatchTimeLiveBean> Stemp2 = StimeEventMap2.get(data.getTime());
            if (Ftemp1 == null) {
                Ftemp1 = new ArrayList<>();
            }
            if (Ftemp2 == null) {
                Ftemp2 = new ArrayList<>();
            }

            if (data.getCode().equals(FIRSTOVER)) {//上半场结束
                firstOver = true;
            }
            if (!firstOver) {   //上半场
                if (data.getIsHome().equals("1")) {//主队
                    Ftemp1.add(data);
                    FtimeEventMap1.put(data.getTime(), Ftemp1);
                } else {//客队
                    Ftemp2.add(data);
                    FtimeEventMap2.put(data.getTime(), Ftemp2);
                }


            } else {//下半场
                if (Stemp1 == null) {
                    Stemp1 = new ArrayList<>();
                }
                if (Stemp2 == null) {
                    Stemp2 = new ArrayList<>();
                }
                if (data.getIsHome().equals("1")) {//主队
                    Stemp1.add(data);
                    StimeEventMap1.put(data.getTime(), Stemp1);
                } else {//客队
                    Stemp2.add(data);
                    StimeEventMap2.put(data.getTime(), Stemp2);
                }


            }

        }

        timeLayoutTop.setEventImage(FtimeEventMap1, StimeEventMap1);
        timeLaoutBottom.setEventImage(FtimeEventMap2, StimeEventMap2);

    }

    /**
     * 展示足球事件。根据比赛状态
     */
    private void showFootballEventByState() {
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
        timeLaoutBottom.setEventImageByState(timeEventMap2);


    }

    /**
     * 展示时间轴时间
     */
    private void showTimeView() {
        if (mKeepTime == null) {
            mKeepTime = "0";
        }
        timeView.updateTime(Integer.parseInt(mKeepTime));
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        fragmentManager = getFragmentManager();
        switch (v.getId()) {
            case R.id.tv_in:
                ll_eventLayout.setVisibility(View.GONE);
                tv_out.setVisibility(View.VISIBLE);

                break;
            case R.id.tv_out:
                ll_eventLayout.setVisibility(View.VISIBLE);
                tv_out.setVisibility(View.GONE);

                break;

            default:
                break;
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }
}
