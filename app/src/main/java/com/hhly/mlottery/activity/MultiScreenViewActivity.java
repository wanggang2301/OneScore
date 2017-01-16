package com.hhly.mlottery.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
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
import com.hhly.mlottery.bean.multiplebean.MultipleByValueBean;
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
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

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
import de.greenrobot.event.EventBus;


/***
 * 多屏动画Activity
 */

public class MultiScreenViewActivity extends BaseWebSocketMultiScreenViewActivity implements ExactSwipeRefreshLayout.OnRefreshListener {

    private static final String baseUrl = "http://pic.13322.com/bg/";

    private static final int VIEW_TYPE_FOOTBALL = 1;
    private static final int VIEW_TYPE_BASKETBALL = 2;

    private static final int REQUEST_ERROR = -1;
    private static final int REQUEST_SUCESS = 1;
    private static final int REQUEST_LOAD = 0;

    private final static String LIVEBEFORE = "0";//直播前
    private final static String ONLIVE = "1";//直播中
    private final static String LIVEENDED = "-1";//直播结束

    @BindView(R.id.public_img_back)
    ImageView publicImgBack;
    @BindView(R.id.ll_add)
    LinearLayout ll_add;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    ExactSwipeRefreshLayout refresh;

    private MultiScreenViewAdapter multiScreenViewAdapter;

    private List<MultiScreenViewBean> list;

    private MultiScreenFootBallBean footBallBean;

    private MultiScreenViewCallBack multiScreenViewCallBack;

    private List<MultipleByValueBean> matchIdList;

    private static final String FOOTBALL_TOPIC = "USER.topic.liveEvent.";   //足球推送主题
    private static final String BASKETBALL_TOPIC = "USER.topic.basketball.score."; //篮球推送主题


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getExtras() != null) {
            matchIdList = (ArrayList<MultipleByValueBean>) getIntent().getSerializableExtra("byValue");
            setWebSocketUri(BaseURLs.WS_SERVICE);
            // setWebSocketUri("ws://m.13322.com/ws");

            for (MultipleByValueBean m : matchIdList) {
                if (m.getType() == VIEW_TYPE_FOOTBALL) {
                    setTopic(new WebSocketMultiScreenViewBean(VIEW_TYPE_FOOTBALL, m.getThirdId(), FOOTBALL_TOPIC + m.getThirdId() + "." + appendLanguage()));  //篮球
                } else if (m.getType() == VIEW_TYPE_BASKETBALL) {
                    setTopic(new WebSocketMultiScreenViewBean(VIEW_TYPE_BASKETBALL, m.getThirdId(), BASKETBALL_TOPIC + m.getThirdId() + ".zh"));  //篮球
                }
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_screen_view);
        ButterKnife.bind(this);
        initView();
        loadData();
    }


    /***
     * 接受推送消息
     *
     * @param w
     */
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
                    multiWebSocketBasketBall = JSON.parseObject(ws_json, MultiWebSocketBasketBall.class);
                }
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


    /***
     * 篮球接受推送
     *
     * @param matchId
     * @param matchScoreBean
     */
    private void updatePushBasketBallScore(String matchId, MultiScreenBasketMatchScoreBean matchScoreBean) {
        for (MultiScreenViewBean m : list) {
            if (m.getMatchId().equals(matchId)) {
                if (m.getData() instanceof MultiScreenBasketballBean) {
                    ((MultiScreenBasketballBean) m.getData()).getMatch().setMatchStatus(matchScoreBean.getMatchStatus());
                    ((MultiScreenBasketballBean) m.getData()).getMatch().setMatchScore(matchScoreBean);
                    multiScreenViewAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    /***
     * 足球接受推送
     *
     * @param matchId
     * @param code
     * @param homeGoal
     * @param guestGoal
     */
    private void updatePushFootBallScore(String matchId, String code, String homeGoal, String guestGoal) {
        switch (code) {
            case "3": //结束下半场
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


    /***
     * 更新足球推送数据
     *
     * @param matchId
     * @param homeScore
     * @param guestScore
     * @param isScoreNull
     * @param isHome
     * @param isCancelScore
     */
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

        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(this);
        refresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getApplicationContext(), 100));

        refresh.setEnabled(false); //禁止下拉刷新

        mHandler.sendEmptyMessage(REQUEST_LOAD);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        list = new ArrayList<>();
        multiScreenViewAdapter = new MultiScreenViewAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(multiScreenViewAdapter);

        ll_add.setVisibility(matchIdList.size() >= 3 ? View.GONE : View.VISIBLE);
    }


    /***
     * 首次进入请求数据
     */
    private void loadData() {
        //requestFootballData("405181");

        for (MultipleByValueBean m : matchIdList) {
            if (m.getType() == VIEW_TYPE_FOOTBALL) {
                requestFootballData(m.getThirdId());
            } else if (m.getType() == VIEW_TYPE_BASKETBALL) {
                requestBasketballData(m.getThirdId());
            }
        }


        multiScreenViewCallBack = new MultiScreenViewCallBack() {
            @Override
            public void delete(int position) {
                if (list.size() <= 1) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.multi_zhishao_text), Toast.LENGTH_SHORT).show();
                } else {
                    promptNetInfo(position);
                }
            }
        };

        multiScreenViewAdapter.setMultiScreenViewCallBack(multiScreenViewCallBack);

        multiScreenViewAdapter.setOnItemClickListener(new BaseRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {

                if (VIEW_TYPE_FOOTBALL == list.get(position).getType()) {
                    Intent intent = new Intent(MultiScreenViewActivity.this, FootballMatchDetailActivity.class);
                    intent.putExtra("thirdId", list.get(position).getMatchId());
                    intent.putExtra("isAddMultiViewHide", true);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MultiScreenViewActivity.this, BasketDetailsActivityTest.class);
                    intent.putExtra("thirdId", list.get(position).getMatchId());
                    intent.putExtra("MatchStatus", ((MultiScreenBasketballBean) list.get(position).getData()).getMatch().getMatchStatus());
                    intent.putExtra("leagueId", ((MultiScreenBasketballBean) list.get(position).getData()).getMatch().getLeagueId());
                    intent.putExtra("matchType", ((MultiScreenBasketballBean) list.get(position).getData()).getMatch().getMatchType());
                    intent.putExtra("isAddMultiViewHide", true);

                    startActivity(intent);
                }
            }
        });

        //开启推送
        connectWebSocket();
    }

    @OnClick({R.id.public_img_back, R.id.ll_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_img_back:
                setResultData();
                finish();

                break;
            case R.id.ll_add:
                //Toast.makeText(getApplicationContext(), "敬请期待", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MultiScreenViewActivity.this, MultiScreenViewingListActivity.class));
                setResultData();
                finish();
                break;
        }
    }

    private void setResultData() {
        EventBus.getDefault().post(matchIdList);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResultData();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_LOAD:
                    refresh.setRefreshing(true);
                    break;
                case REQUEST_ERROR:
                    break;
                case REQUEST_SUCESS:
                    refresh.setRefreshing(false);
                    break;
            }
        }
    };

    /***
     * 请求足球数据
     *
     * @param id
     */
    private void requestFootballData(final String id) {
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", id);

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_INFO_FIRST, params, new VolleyContentFast.ResponseSuccessListener<MatchDetail>() {
            @Override
            public void onResponse(MatchDetail matchDetail) {
                if (matchDetail != null) {
                    if (!"200".equals(matchDetail.getResult())) {
                        // mHandler.sendEmptyMessage(ERROR);
                        return;
                    }
                    initFootballData(matchDetail, id);
                    mHandler.sendEmptyMessage(REQUEST_SUCESS);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, MatchDetail.class);
    }


    /***
     * 请求篮球数据
     *
     * @param id
     */
    public void requestBasketballData(final String id) {
        Map<String, String> params = new HashMap<>();
        params.put("thirdId", id);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_BASKET_DETAILS, params, new VolleyContentFast.ResponseSuccessListener<MultiScreenBasketballBean>() {
            @Override
            public void onResponse(MultiScreenBasketballBean basketDetailsBean) {
                if (basketDetailsBean != null) {
                    if (basketDetailsBean.getMatch() != null) {
                        list.add(new MultiScreenViewBean(VIEW_TYPE_BASKETBALL, id, basketDetailsBean));
                        updateAdapter();
                        mHandler.sendEmptyMessage(REQUEST_SUCESS);
                    }
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
            AlertDialog.Builder builder = new AlertDialog.Builder(MultiScreenViewActivity.this, R.style.AppThemeDialog);
            builder.setCancelable(false);// 设置对话框以外不可点击
            builder.setTitle(getApplicationContext().getResources().getString(R.string.to_update_kindly_reminder));// 提示标题
            builder.setMessage(getApplicationContext().getResources().getString(R.string.multi_dialog_text));// 提示内容
            builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.about_confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    deleteAdapterItem(position);
                }
            });
            builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.about_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    /***
     * 删除操作
     *
     * @param position
     */
    private void deleteAdapterItem(int position) {


        for (MultipleByValueBean m : matchIdList) {
            if (m.getThirdId().equals(list.get(position).getMatchId())) {
                matchIdList.remove(position);
            }
        }

        list.remove(position);
        multiScreenViewAdapter.notifyDataSetChanged();

        ll_add.setVisibility(list.size() >= 3 ? View.GONE : View.VISIBLE);
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

    @Override
    public void onRefresh() {
        L.d("dddfff", "刷新");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        }, 3000);
    }
}
