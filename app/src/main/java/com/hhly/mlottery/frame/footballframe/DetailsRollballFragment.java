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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.bean.footballDetails.BottomOdds;
import com.hhly.mlottery.bean.footballDetails.BottomOddsItem;
import com.hhly.mlottery.bean.footballDetails.WebSocketRollballOdd;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.DetailsRollOdd;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wang gang
 * @date 2016/6/3 14:17
 * @des 足球内页聊球下部内容
 */
public class DetailsRollballFragment extends BaseWebSocketFragment implements View.OnClickListener{

    private final int ERROR = -1;//访问失败
    private final int SUCCESS = 0;// 访问成功
    private final int STARTLOADING = 1;// 正在加载中

    private View mView;

    private Context mContext;

    private static final String DETAILSROLLBALL_THIRD_ID = "THIRD_ID";

    //亚盘
    private static final int ALET = 1;
    private static final int EUR = 2;
    private static final int ASIZE = 3;  //大小球

    private TextView reLoading; //赔率请求失败重新加载

    private DetailsRollOdd odd_alet; //亚盘

    private DetailsRollOdd odd_asize; //大小

    private DetailsRollOdd odd_eur; //欧赔


    private FrameLayout fl_odds_loading;

    private FrameLayout fl_odds_net_error;

    private LinearLayout ll_odds;

    private BottomOddsDetailsFragment mBottomOddsDetailsFragment;

    //保存上次推送赔率
    private BottomOddsItem aletBottomOddsItem;
    private BottomOddsItem asizeBottomOddsItem;
    private BottomOddsItem eurBottomOddsItem;

    private String mThirdId;

    public static DetailsRollballFragment newInstance(String thirdId) {
        DetailsRollballFragment fragment = new DetailsRollballFragment();
        Bundle args = new Bundle();
        args.putString(DETAILSROLLBALL_THIRD_ID, thirdId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(getArguments() != null){
            mThirdId = getArguments().getString(DETAILSROLLBALL_THIRD_ID);
        }
        setWebSocketUri(BaseURLs.WS_SERVICE);
        setTopic(BaseUserTopics.oddsFootballMatch + "." + mThirdId);
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_details_rollball, container, false);
        initView();
        initOdds();
        return mView;
    }

    // 设置页面显示状态
    private void setStatus(int status) {
        fl_odds_loading.setVisibility(status == STARTLOADING ? View.VISIBLE : View.GONE);
        fl_odds_net_error.setVisibility(status == ERROR ? View.VISIBLE : View.GONE);
        ll_odds.setVisibility(status == SUCCESS ? View.VISIBLE : View.GONE);
    }

    private void initOdds() {
        L.d("ddd", "加载数据");
        setStatus(STARTLOADING);// 正在加载数据中

        Map<String, String> params = new HashMap<>();
        params.put("thirdId", mThirdId);

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_BALLLISTOVERVIEW_INFO, params,
                new VolleyContentFast.ResponseSuccessListener<BottomOdds>() {
                    @Override
                    public void onResponse(BottomOdds bottomOdds) {
                        if (!bottomOdds.getResult().equals("200")) {
                            setStatus(ERROR);
                            return;
                        }

                        setStatus(SUCCESS);

                        initItemOdd(bottomOdds);

                        connectWebSocket();

                    }
                }

                , new VolleyContentFast.ResponseErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        setStatus(ERROR);
                    }
                }

                , BottomOdds.class
        );
    }

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
                mBottomOddsDetailsFragment = BottomOddsDetailsFragment.newInstance(bottomOdds.getAsianlistOdd().get(0), ALET);
                mBottomOddsDetailsFragment.show(getChildFragmentManager(), "bottomOdds");
            }
        });


        //大小球
        odd_asize.findViewById(R.id.tl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isRepeatShowDialog();

                mBottomOddsDetailsFragment = BottomOddsDetailsFragment.newInstance(bottomOdds.getOverunderlistOdd().get(0), ASIZE);
                mBottomOddsDetailsFragment.show(getChildFragmentManager(), "bottomOdds");
            }
        });


        //欧赔
        odd_eur.findViewById(R.id.tl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRepeatShowDialog();

                mBottomOddsDetailsFragment = BottomOddsDetailsFragment.newInstance(bottomOdds.getEuropelistOdd().get(0), EUR);
                mBottomOddsDetailsFragment.show(getChildFragmentManager(), "bottomOdds");
            }
        });
    }


    private void isRepeatShowDialog() {
      /*  if (finishMatchLiveTextFragment != null) {
            finishMatchLiveTextFragment.dismiss();
        }

        if (liveTextFragmentTest != null) {
            liveTextFragmentTest.dismiss();
        }*/

        if (mBottomOddsDetailsFragment != null) {
            mBottomOddsDetailsFragment.dismiss();
        }
    }

    private void initView() {
        odd_alet = (DetailsRollOdd) mView.findViewById(R.id.odd_alet);
        odd_asize = (DetailsRollOdd) mView.findViewById(R.id.odd_asize);
        odd_eur = (DetailsRollOdd) mView.findViewById(R.id.odd_eur);
        // live_infos = (RelativeLayout) mView.findViewById(R.id.rl_live);

        fl_odds_loading = (FrameLayout) mView.findViewById(R.id.fl_odd_loading);
        fl_odds_net_error = (FrameLayout) mView.findViewById(R.id.fl_odds_networkError);

        ll_odds = (LinearLayout) mView.findViewById(R.id.ll_0dds);

        reLoading = (TextView) mView.findViewById(R.id.reLoading);
        reLoading.setOnClickListener(this);
    }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeWebSocket();
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

    private boolean isNULLOrEmpty(String s) {
        return s == null || "".equals(s) || "-".equals(s);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reLoading:
                initOdds();
                break;
        }
    }
}
