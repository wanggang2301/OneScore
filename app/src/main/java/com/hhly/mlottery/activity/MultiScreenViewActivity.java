package com.hhly.mlottery.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewHolder;
import com.hhly.mlottery.adapter.multiscreen.MultiScreenViewAdapter;
import com.hhly.mlottery.bean.footballDetails.MatchDetail;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.bean.multiscreenview.MultiScreenBasketMatchScoreBean;
import com.hhly.mlottery.bean.multiscreenview.MultiScreenBasketballBean;
import com.hhly.mlottery.bean.multiscreenview.MultiScreenFootBallBean;
import com.hhly.mlottery.bean.multiscreenview.MultiScreenViewBean;
import com.hhly.mlottery.bean.multiscreenview.MultiWebSocketBasketBall;
import com.hhly.mlottery.bean.multiscreenview.WebSocketMultiScreenViewBean;
import com.hhly.mlottery.bean.multiscreenview.WebSocketMultiScreenViewTextBean;
import com.hhly.mlottery.bean.websocket.WebSocketStadiumLiveTextEvent;
import com.hhly.mlottery.callback.MultiScreenViewCallBack;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MultiScreenViewActivity extends BaseWebSocketMultiScreenViewActivity {


    //setTopic("USER.topic.basketball.score." + mThirdId + ".zh");

//        setTopic("USER.topic.liveEvent." + mThirdId + "." + appendLanguage());

    private static final String baseUrl = "http://pic.13322.com/bg/";

    private static final int VIEW_TYPE_FOOTBALL = 1;
    private static final int VIEW_TYPE_BASKETBALL = 2;

    private final static String LIVEBEFORE = "0";//直播前

    private final static String ONLIVE = "1";//直播中
    private final static String LIVEENDED = "-1";//直播结束

    @BindView(R.id.public_img_back)
    ImageView publicImgBack;
    @BindView(R.id.ll_add)
    LinearLayout ll_add;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    private MultiScreenViewAdapter multiScreenViewAdapter;

    private List<MultiScreenViewBean> list;

    private MultiScreenFootBallBean footBallBean;

    private MultiScreenViewCallBack multiScreenViewCallBack;


    private String mFootThirdId = "";
    private String mBasketThirdId = "4273637";
    //private String mBasketThirdId1 = "4273697";
    private String mBasketThirdId2 = "4228107";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setWebSocketUri("ws://m.13322.com/ws");
        // setTopic("USER.topic.liveEvent." + mFootThirdId + "." + appendLanguage());  //足球
        setTopic(new WebSocketMultiScreenViewBean(VIEW_TYPE_BASKETBALL, mBasketThirdId, "USER.topic.basketball.score." + mBasketThirdId + ".zh"));  //篮球
       // setTopic(new WebSocketMultiScreenViewBean(VIEW_TYPE_BASKETBALL, mBasketThirdId1, "USER.topic.basketball.score." + mBasketThirdId1 + ".zh"));  //篮球
        // setTopic(new WebSocketMultiScreenViewBean(VIEW_TYPE_BASKETBALL, mBasketThirdId2, "USER.topic.basketball.score." + mBasketThirdId2 + ".zh"));  //篮球

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_screen_view);
        ButterKnife.bind(this);
        initView();
        connectWebSocket();

    }

    @Override
    protected void onTextResult(WebSocketMultiScreenViewTextBean w) {
        String type = "";
        try {
            JSONObject jsonObject = new JSONObject(w.getText());
            type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!"".equals(type)) {
            Message msg = Message.obtain();
            msg.obj = w;

            if (w.getType() == VIEW_TYPE_BASKETBALL) {
                if (Integer.parseInt(type) == 100) { //篮球只需要typo==100的数据
                    msg.obj = w;
                    mSocketHandler.sendMessage(msg);
                }

            } else if (w.getType() == VIEW_TYPE_FOOTBALL) {
                if (Integer.parseInt(type) == 6) { //足球事件直播type=6
                    msg.obj = w;
                    mSocketHandler.sendMessage(msg);
                }
            }
        }
    }


    Handler mSocketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WebSocketMultiScreenViewTextBean w = (WebSocketMultiScreenViewTextBean) msg.obj;

            if (w.getType() == VIEW_TYPE_BASKETBALL) {   //篮球

                L.d("multiscreen", "____篮球___" + w.getMatchId() + "___" + w.getText());

                String ws_json = w.getText();

                MultiWebSocketBasketBall multiWebSocketBasketBall = null;
                try {
                    multiWebSocketBasketBall = JSON.parseObject(ws_json, MultiWebSocketBasketBall.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    // Log.e(TAG, "ws_json = " + ws_json);
                    multiWebSocketBasketBall = JSON.parseObject(ws_json, MultiWebSocketBasketBall.class);
                }
                //  L.e();


                updatePushBasketBallScore(w.getMatchId(), multiWebSocketBasketBall.getData());


            } else if (w.getType() == VIEW_TYPE_FOOTBALL) { //足球
                L.d("multiscreen", "____足球___" + w.getMatchId() + "___" + w.getText());

                String ws_json = w.getText();
                WebSocketStadiumLiveTextEvent webSocketStadiumLiveTextEvent = null;
                try {
                    webSocketStadiumLiveTextEvent = JSON.parseObject(ws_json, WebSocketStadiumLiveTextEvent.class);
                } catch (Exception e) {
                    ws_json = ws_json.substring(0, ws_json.length() - 1);
                    webSocketStadiumLiveTextEvent = JSON.parseObject(ws_json, WebSocketStadiumLiveTextEvent.class);
                }

                Map<String, String> data = webSocketStadiumLiveTextEvent.getData();
                //更新足球比分
                updatePushFootBallScore(w.getMatchId(), data.get("code"), data.get("homeScore"), data.get("guestScore"));
            }
        }
    };


    private void updatePushBasketBallScore(String matchId, MultiScreenBasketMatchScoreBean matchScoreBean) {
        for (MultiScreenViewBean m : list) {
            if (m.getMatchId().equals(matchId)) {
                if (m.getData() instanceof MultiScreenBasketballBean) {

                    L.d("multiscreen123", "____篮球___" + matchScoreBean.getHomeScore());


                    ((MultiScreenBasketballBean) m.getData()).getMatch().setMatchScore(matchScoreBean);
                    L.d("multiscreen123", "____篮球___" + ((MultiScreenBasketballBean) m.getData()).getMatch().getMatchScore().getHomeScore());


                    multiScreenViewAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    private void updatePushFootBallScore(String matchId, String code, String homeGoal, String guestGoal) {
        switch (code) {
            case "12":
            case "13":  //下半场开始
                break;
            case "3": //结束下半场
                //socket关闭
                // 获取上半场的走势图数据
                // setScoreClolor(getApplicationContext().getResources().getColor(R.color.score));

                for (MultiScreenViewBean m : list) {
                    if (m.getMatchId().equals(matchId)) {
                        if (m.getData() instanceof MultiScreenFootBallBean) {
                            ((MultiScreenFootBallBean) m.getData()).setLiveStatus("-1");
                        }
                    }
                }
                break;

            case "1029": //主队进球
                if ("".equals(homeGoal) || "".equals(guestGoal)) {
                    updateAdapterFootballData(matchId, "", "", true, true, false);
                } else {
                    updateAdapterFootballData(matchId, homeGoal, guestGoal, false, true, false);
                }

                break;
            case "1030"://取消主队进球
                if (Integer.parseInt(homeGoal) > 0) {
                    if ("".equals(homeGoal) || "".equals(guestGoal)) {
                        updateAdapterFootballData(matchId, "", "", true, true, true);
                    } else {
                        updateAdapterFootballData(matchId, homeGoal, guestGoal, false, true, true);
                    }
                }

                break;
            case "2053"://客队进球

                if ("".equals(homeGoal) || "".equals(guestGoal)) {
                    updateAdapterFootballData(matchId, "", "", true, false, false);
                } else {
                    updateAdapterFootballData(matchId, homeGoal, guestGoal, false, false, false);
                }

                break;

            case "2054"://取消客队进球
                if (Integer.parseInt(guestGoal) > 0) {
                    if ("".equals(homeGoal) || "".equals(guestGoal)) {
                        updateAdapterFootballData(matchId, "", "", true, false, true);
                    } else {
                        updateAdapterFootballData(matchId, homeGoal, guestGoal, false, false, true);
                    }
                }

                break;
            default:
                break;
        }


    }


    private void updateAdapterFootballData(String matchId, String homeScore, String guestScore, boolean isScoreNull, boolean isHome, boolean isCancelScore) {
        L.d("zxcvbnm", list.size() + "");

        for (MultiScreenViewBean m : list) {
            if (m.getMatchId().equals(matchId)) {
                if (m.getData() instanceof MultiScreenFootBallBean) {
                    //足球
                    if (isScoreNull) {
                        if (isHome) {
                            if (isCancelScore) {  //取消=true
                                ((MultiScreenFootBallBean) m.getData()).setHomeScore(((MultiScreenFootBallBean) m.getData()).getHomeScore() - 1);
                            } else {
                                ((MultiScreenFootBallBean) m.getData()).setHomeScore(((MultiScreenFootBallBean) m.getData()).getHomeScore() + 1);
                            }
                        } else {
                            if (isCancelScore) {
                                ((MultiScreenFootBallBean) m.getData()).setGuestScore(((MultiScreenFootBallBean) m.getData()).getGuestScore() - 1);
                            } else {
                                ((MultiScreenFootBallBean) m.getData()).setGuestScore(((MultiScreenFootBallBean) m.getData()).getGuestScore() + 1);
                            }
                        }
                    } else {
                        ((MultiScreenFootBallBean) m.getData()).setHomeScore(Integer.parseInt(homeScore));
                        ((MultiScreenFootBallBean) m.getData()).setGuestScore(Integer.parseInt(guestScore));
                    }
                    //
                    multiScreenViewAdapter.notifyDataSetChanged();
                }
            }
        }
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


    private void initView() {


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        list = new ArrayList<>();
        multiScreenViewAdapter = new MultiScreenViewAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(multiScreenViewAdapter);


        multiScreenViewCallBack = new MultiScreenViewCallBack() {
            @Override
            public void delete(int position) {
                promptNetInfo(position);
            }
        };

        multiScreenViewAdapter.setMultiScreenViewCallBack(multiScreenViewCallBack);

        requestFootballData("405181");
        requestFootballData("405176");

        requestBasketballData("4273637");
      //  requestBasketballData("4273697");


        multiScreenViewAdapter.setOnItemClickListener(new BaseRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {

                if (VIEW_TYPE_FOOTBALL == list.get(position).getType()) {
                    Intent intent = new Intent(MultiScreenViewActivity.this, FootballMatchDetailActivity.class);
                    intent.putExtra("thirdId", list.get(position).getMatchId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MultiScreenViewActivity.this, BasketDetailsActivityTest.class);
                    intent.putExtra("thirdId", list.get(position).getMatchId());
                    intent.putExtra("MatchStatus", ((MultiScreenBasketballBean) list.get(position).getData()).getMatch().getMatchStatus());
                    intent.putExtra("leagueId", ((MultiScreenBasketballBean) list.get(position).getData()).getMatch().getLeagueId());
                    intent.putExtra("matchType", ((MultiScreenBasketballBean) list.get(position).getData()).getMatch().getMatchType());
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick({R.id.public_img_back, R.id.ll_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_img_back:
                finish();
                break;
            case R.id.ll_add:

                Toast.makeText(getApplicationContext(), "敬请期待", Toast.LENGTH_SHORT).show();

                break;
        }
    }


    private void requestFootballData(final String id) {
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", id);

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_INFO_FIRST, params, new VolleyContentFast.ResponseSuccessListener<MatchDetail>() {
            @Override
            public void onResponse(MatchDetail matchDetail) {

                if (!"200".equals(matchDetail.getResult())) {
                    // mHandler.sendEmptyMessage(ERROR);
                    return;
                }

                initFootballData(matchDetail, id);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, MatchDetail.class);
    }


    public void requestBasketballData(final String id) {
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", id);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_BASKET_DETAILS, params, new VolleyContentFast.ResponseSuccessListener<MultiScreenBasketballBean>() {
            @Override
            public void onResponse(MultiScreenBasketballBean basketDetailsBean) {
                if (basketDetailsBean.getMatch() != null) {

                    list.add(new MultiScreenViewBean(VIEW_TYPE_BASKETBALL, id, basketDetailsBean));

                    updateAdapter();
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, MultiScreenBasketballBean.class);
    }


    private void initFootballData(MatchDetail matchDetail, String id) {

        footBallBean = new MultiScreenFootBallBean();

        int random = new Random().nextInt(20);
        String url = baseUrl + random + ".png";

        footBallBean.setLiveStatus(matchDetail.getLiveStatus());

        footBallBean.setBg(url);

        footBallBean.setHome_name(matchDetail.getHomeTeamInfo().getName());

        footBallBean.setGuest_name(matchDetail.getGuestTeamInfo().getName());

        footBallBean.setHome_icon(matchDetail.getHomeTeamInfo().getUrl());
        footBallBean.setGuest_icon(matchDetail.getGuestTeamInfo().getUrl());

        footBallBean.setmMatchType1(matchDetail.getMatchType1());

        footBallBean.setmMatchType2(matchDetail.getMatchType2());

        footBallBean.setDate(matchDetail.getMatchInfo().getStartTime());
        if (LIVEBEFORE.equals(matchDetail.getLiveStatus())) { //赛前
        } else if (LIVEENDED.equals(matchDetail.getLiveStatus())) {
            footBallBean.setHomeScore(Integer.parseInt(matchDetail.getHomeTeamInfo().getScore()));
            footBallBean.setGuestScore(Integer.parseInt(matchDetail.getGuestTeamInfo().getScore()));
        } else if (ONLIVE.equals(matchDetail.getLiveStatus())) { //未完场头部  统计比分
            computeMatchLiveScore(matchDetail.getMatchInfo().getMatchLive(), footBallBean);
        }

        list.add(new MultiScreenViewBean(VIEW_TYPE_FOOTBALL, id, footBallBean));

        updateAdapter();
    }


    private void computeMatchLiveScore(List<MatchTextLiveBean> matchLive, MultiScreenFootBallBean multiScreenFootBallBean) {

        int homeScore = 0;
        int guestScore = 0;


        for (MatchTextLiveBean m : matchLive) {
            switch (m.getCode().trim()) {
                case "1029": //主队进球
                    homeScore = homeScore + 1;
                    break;
                case "1030"://取消主队进球
                    homeScore = homeScore - 1;
                    break;
                case "2053": //可队进球
                    guestScore = guestScore + 1;
                    break;
                case "2054"://取消可队进球
                    guestScore = guestScore - 1;
                    break;
                default:
                    break;
            }
        }

        multiScreenFootBallBean.setHomeScore(homeScore);
        multiScreenFootBallBean.setGuestScore(guestScore);
    }

    private void updateAdapter() {
        multiScreenViewAdapter.setList(list);
        multiScreenViewAdapter.notifyDataSetChanged();
    }


    /**
     * 当前连接的网络提示
     */
    private void promptNetInfo(final int position) {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MultiScreenViewActivity.this, R.style.AppThemeDialog);
            builder.setCancelable(false);// 设置对话框以外不可点击
            builder.setTitle(getApplicationContext().getResources().getString(R.string.to_update_kindly_reminder));// 提示标题
            builder.setMessage("您确认移除这场比赛吗?");// 提示内容
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    deleteAdapterItem(position);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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

    private void deleteAdapterItem(int position) {
        list.remove(position);
        multiScreenViewAdapter.notifyDataSetChanged();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

        closeWebSocket();
    }
}
