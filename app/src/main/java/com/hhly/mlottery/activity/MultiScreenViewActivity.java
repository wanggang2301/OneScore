package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.multiscreen.MultiScreenViewAdapter;
import com.hhly.mlottery.bean.basket.BasketballDetailsBean;
import com.hhly.mlottery.bean.footballDetails.MatchDetail;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.bean.multiscreenview.MultiScreenFootBallBean;
import com.hhly.mlottery.bean.multiscreenview.MultiScreenViewBean;
import com.hhly.mlottery.callback.MultiScreenViewCallBack;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hhly.mlottery.activity.BasketDetailsActivityTest.mThirdId;

public class MultiScreenViewActivity extends Activity {

    private static final String baseUrl = "http://pic.13322.com/bg/";

    private static final int VIEW_TYPE_FOOTBALL = 1;
    private static final int VIEW_TYPE_BASKETBALL = 2;

    private final static String LIVEBEFORE = "0";//直播前

    private final static String ONLIVE = "1";//直播中
    private final static String LIVEENDED = "-1";//直播结束

    @BindView(R.id.public_img_back)
    ImageView publicImgBack;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    private MultiScreenViewAdapter multiScreenViewAdapter;

    private List<MultiScreenViewBean> list;

    private MultiScreenFootBallBean footBallBean;

    private MultiScreenViewCallBack multiScreenViewCallBack;


    private MathchStatisInfo mathchStatisInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_screen_view);
        ButterKnife.bind(this);
        initView();
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

        requestBasketballData("4227937");
        requestBasketballData("4227947");
    }

    @OnClick({R.id.public_img_back, R.id.btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_img_back:
                finish();
                break;
            case R.id.btn_add:
                //((MultiScreenFootBallBean) list.get(1).getData()).setScore("5:6");
                // multiScreenViewAdapter.notifyItemChanged(1);

                L.d("zxcvbnm", list.size() + "");


                for (MultiScreenViewBean m : list) {

                    if (m.getMatchId().equals("405181")) {
                        if (m.getData() instanceof MultiScreenFootBallBean) {
                            //足球

                            ((MultiScreenFootBallBean) m.getData()).setScore("" + new Random().nextInt(10) + ":" + new Random().nextInt(10));
                            multiScreenViewAdapter.notifyDataSetChanged();


                        } else {
                            //篮球
                        }

                    }

                }

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
        L.d("456789", mThirdId);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_BASKET_DETAILS, params, new VolleyContentFast.ResponseSuccessListener<BasketballDetailsBean>() {
            @Override
            public void onResponse(BasketballDetailsBean basketDetailsBean) {
                if (basketDetailsBean.getMatch() != null) {

                    list.add(new MultiScreenViewBean(VIEW_TYPE_BASKETBALL, id, basketDetailsBean));

                    updateAdapter();
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, BasketballDetailsBean.class);
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
            footBallBean.setScore("VS");
        } else if (LIVEENDED.equals(matchDetail.getLiveStatus())) {
            footBallBean.setScore(matchDetail.getHomeTeamInfo().getScore() + ":" + matchDetail.getGuestTeamInfo().getScore());
        } else if (ONLIVE.equals(matchDetail.getLiveStatus())) { //未完场头部  统计比分
            computeMatchLiveScore(matchDetail.getMatchInfo().getMatchLive());
            footBallBean.setScore(mathchStatisInfo.getHome_score() + ":" + mathchStatisInfo.getGuest_score() + "");
        }

        list.add(new MultiScreenViewBean(VIEW_TYPE_FOOTBALL, id, footBallBean));

        updateAdapter();
    }

    private void computeMatchLiveScore(List<MatchTextLiveBean> matchLive) {
        mathchStatisInfo = new MathchStatisInfo();

        for (MatchTextLiveBean m : matchLive) {
            switch (m.getCode().trim()) {
                case "1029": //主队进球
                    mathchStatisInfo.setHome_score(mathchStatisInfo.getHome_score() + 1);
                    break;
                case "1030"://取消主队进球
                    mathchStatisInfo.setHome_score(mathchStatisInfo.getHome_score() - 1);
                    break;
                case "2053": //可队进球
                    mathchStatisInfo.setGuest_score(mathchStatisInfo.getGuest_score() + 1);
                    break;
                case "2054"://取消可队进球
                    mathchStatisInfo.setGuest_score(mathchStatisInfo.getGuest_score() - 1);
                    break;
                default:
                    break;
            }
        }
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
                    updateAdapter(position);
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


    private void updateAdapter(int position) {
        list.remove(position);
        multiScreenViewAdapter.notifyDataSetChanged();
    }


    public class MathchStatisInfo {
        private int home_score = 0;
        private int guest_score = 0;

        public int getHome_score() {
            return home_score;
        }

        public void setHome_score(int home_score) {
            this.home_score = home_score;
        }

        public int getGuest_score() {
            return guest_score;
        }

        public void setGuest_score(int guest_score) {
            this.guest_score = guest_score;
        }
    }
}
