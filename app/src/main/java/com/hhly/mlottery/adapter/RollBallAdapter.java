package com.hhly.mlottery.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewAdapter;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewHolder;
import com.hhly.mlottery.bean.Match;
import com.hhly.mlottery.bean.MatchOdd;
import com.hhly.mlottery.bean.websocket.WebSocketMatchChange;
import com.hhly.mlottery.bean.websocket.WebSocketMatchEvent;
import com.hhly.mlottery.bean.websocket.WebSocketMatchOdd;
import com.hhly.mlottery.bean.websocket.WebSocketMatchStatus;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.RxBus;

import java.util.List;
import java.util.Map;

public class RollBallAdapter extends BaseRecyclerViewAdapter {

    public static final int VIEW_TYPE_DEFAULT = 1;

    private Context context;

    public RollBallAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.item_football_roll};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        int itemViewType = getRecycleViewItemType(position);
        switch (itemViewType) {
            case VIEW_TYPE_DEFAULT:
                this.bindDefaultView(viewHolder, position);
                break;
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @SuppressLint("SetTextI18n")
    private void bindDefaultView(final BaseRecyclerViewHolder viewHolder, final int position) {
        Match data = getItemByPosition(position);
        if (data == null) return;

        final CardView container = viewHolder.findViewById(R.id.container);
        RelativeLayout rlHalfContainer = viewHolder.findViewById(R.id.rlHalfContainer);
        TextView tvTournament = viewHolder.findViewById(R.id.tvTournament);
        TextView tvScoreUp = viewHolder.findViewById(R.id.tvScoreUp);
        TextView tvTime = viewHolder.findViewById(R.id.tvTime);
        TextView tvSpedingTime = viewHolder.findViewById(R.id.tvSpedingTime);
        TextView tvScoreDown = viewHolder.findViewById(R.id.tvScoreDown);
        LinearLayout itemPositionControl = viewHolder.findViewById(R.id.ll1);
        final ImageView ivItemPositionControl = viewHolder.findViewById(R.id.ivItemPositionControl);
        TextView tvTeamUp = viewHolder.findViewById(R.id.tvTeamUp);
        TextView tvTeamUPScore1 = viewHolder.findViewById(R.id.tvTeamUPScore1);
        TextView tvTeamUpScore2 = viewHolder.findViewById(R.id.tvTeamUpScore2);
        TextView tvMiddleScore1 = viewHolder.findViewById(R.id.tvMiddleScore1);
        TextView tvMiddleScore2 = viewHolder.findViewById(R.id.tvMiddleScore2);
        TextView tvTeamDown = viewHolder.findViewById(R.id.tvTeamDown);
        TextView tvTeamDownScore1 = viewHolder.findViewById(R.id.tvTeamDownScore1);
        TextView tvTeamDownScore2 = viewHolder.findViewById(R.id.tvTeamDownScore2);
        TextView tvTeamUp_YA = viewHolder.findViewById(R.id.tvTeamUp_YA);
        TextView tvTeamMiddle_YA = viewHolder.findViewById(R.id.tvTeamMiddle_YA);
        TextView tvTeamDown_YA = viewHolder.findViewById(R.id.tvTeamDown_YA);
        TextView tvTeamUp_DA = viewHolder.findViewById(R.id.tvTeamUp_DA);
        TextView tvTeamMiddle_DA = viewHolder.findViewById(R.id.tvTeamMiddle_DA);
        TextView tvTeamDown_DA = viewHolder.findViewById(R.id.tvTeamDown_DA);
        TextView tvTeamUp_EU = viewHolder.findViewById(R.id.tvTeamUp_EU);
        TextView tvTeamMiddle_EU = viewHolder.findViewById(R.id.tvTeamMiddle_EU);
        TextView tvTeamDown_EU = viewHolder.findViewById(R.id.tvTeamDown_EU);

        this.setVisiableStateOfThisViews(container, rlHalfContainer, tvScoreUp, tvScoreDown);
        tvSpedingTime.setTextColor(context.getResources().getColor(R.color.text_about_color));

        // TODO:后台数据应该将默认值为null情况，改成0更合理；
        if (TextUtils.isEmpty(data.getKeepTime())) {
            data.setKeepTime("0");
        }

        /** 比赛状态 */
        // TODO: -11 待定 -13 中断没有写
        switch (data.getStatusOrigin()) {
            case "0": // 未开赛
                rlHalfContainer.setVisibility(View.INVISIBLE);
                tvScoreUp.setVisibility(View.INVISIBLE);
                tvScoreDown.setVisibility(View.INVISIBLE);
                tvSpedingTime.setText("VS");
                tvSpedingTime.setTextColor(context.getResources().getColor(R.color.res_pl_color));
                break;
            case "1": // 上半场进行时间
                if(Integer.parseInt(data.getKeepTime()) > 45) tvSpedingTime.setText("45+");
                else tvSpedingTime.setText(data.getKeepTime() + "'");
                break;
            case "3": // 下半场进行时间
                if(Integer.parseInt(data.getKeepTime()) > 90) tvSpedingTime.setText("90+");
                else tvSpedingTime.setText(data.getKeepTime() + "'");
                break;
            case "2": // 中场
                tvSpedingTime.setText("中场");
                break;
            case "4": // 加时
                tvSpedingTime.setText("加时");
                break;
            case "5": // 点球
                tvSpedingTime.setText("点球");
                break;
        }

        /** 赔率 */
        Map<String, MatchOdd> matchOddMap = data.getMatchOdds();
        MatchOdd asiaSize = null, euro = null, asiaLet = null;
        if (matchOddMap != null && matchOddMap.size() > 0) {
            for (Map.Entry<String, MatchOdd> matchOdd : matchOddMap.entrySet()) {
                switch (matchOdd.getKey()) {
                    case "asiaSize":// 大小盘数据
                        asiaSize = matchOddMap.get("asiaSize");
                        break;
                    case "euro":// 欧赔数据
                        euro = matchOddMap.get("euro");
                        break;
                    case "asiaLet":// 亚赔数据
                        asiaLet = matchOddMap.get("asiaLet");
                        break;
                }
            }
        }

        // 杯赛名称
        tvTournament.setText(data.getRacename());
        // 主队分数
        tvScoreUp.setText(data.getHomeScore());
        // 开赛时间
        tvTime.setText(data.getTime());

        // 客队分数
        tvScoreDown.setText(data.getGuestScore());
        // 主队名称
        tvTeamUp.setText(data.getHometeam());
        // 主队黄牌数
        tvTeamUPScore1.setText(data.getHome_yc());
        // 主队红牌数
        tvTeamUpScore2.setText(data.getHome_rc());
        // 半场主队得分数
        tvMiddleScore1.setText(data.getHomeHalfScore());
        // 半场客服的分数
        tvMiddleScore2.setText(data.getGuestHalfScore());
        // 客队名称
        tvTeamDown.setText(data.getGuestteam());
        // 客队黄牌数
        tvTeamDownScore1.setText(data.getGuest_yc());
        // 客队红牌数
        tvTeamDownScore2.setText(data.getGuest_rc());
        // 亚盘赔率
        tvTeamUp_YA.setText(asiaLet != null ? asiaLet.getHandicapValue() : "-");
        tvTeamMiddle_YA.setText(asiaLet != null ? asiaLet.getLeftOdds() : "-");
        tvTeamDown_YA.setText(asiaLet != null ? asiaLet.getRightOdds() : "-");
        // 大小盘赔率
        tvTeamUp_DA.setText(asiaSize != null ? asiaSize.getHandicapValue() : "-");
        tvTeamMiddle_DA.setText(asiaSize != null ? asiaSize.getLeftOdds() : "-");
        tvTeamDown_DA.setText(asiaSize != null ? asiaSize.getRightOdds() : "-");
        // 欧盘赔率
        tvTeamUp_EU.setText(euro != null ? euro.getMediumOdds() : "-");
        tvTeamMiddle_EU.setText(euro != null ? euro.getLeftOdds() : "-");
        tvTeamDown_EU.setText(euro != null ? euro.getRightOdds() : "-");

        // 控制器
        itemPositionControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivItemPositionControl.setBackgroundResource(R.mipmap.roll_default);
                RollBallAdapter.this.transformMapper(position);
                RollBallAdapter.this.showShadow(container);
            }
        });
    }

    private void setVisiableStateOfThisViews(View ...views) {
        for(View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void showShadow(final View view) {
        view.animate()
                .setDuration(400)
                .setInterpolator(new OvershootInterpolator(4.f))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ObjectAnimator.ofFloat(view, View.ALPHA, 0, 0.8f).setDuration(400).start();
                    }
                })
                .start();
    }

    private void transformMapper(int position) {
        List list = getList();
        Match data = (Match) list.get(position);
        list.remove(data);
        list.add(0, data);
        notifyDataSetChanged();
    }

    public void updateItemFromWebSocket(Object o) {
        List<Match> matchList = getList();
        for (int i = 0; i < matchList.size(); i++) {
            Match match = matchList.get(i);
            if (o instanceof WebSocketMatchStatus) {
                this.updateTypeStatus(match, i, (WebSocketMatchStatus) o);
            } else if (o instanceof WebSocketMatchOdd) {
                this.updateTypeOdds(match, i, (WebSocketMatchOdd) o);
            } else if (o instanceof WebSocketMatchEvent) {
                this.updateTypeEvent(match, i, (WebSocketMatchEvent) o);
            } else if (o instanceof WebSocketMatchChange) {
                this.updateTypeMatchChange(match, i, (WebSocketMatchChange) o);
            }
        }
    }

    private void updateTypeStatus(Match match, int position, WebSocketMatchStatus webSocketMatchStatus) {
        final Match[] target = new Match[1];

        Map<String, String> data = webSocketMatchStatus.getData();
        if (match.getThirdId().equals(webSocketMatchStatus.getThirdId())) {
            target[0] = match;
            match.setStatusOrigin(data.get("statusOrigin"));
            if ("0".equals(match.getStatusOrigin()) && "1".equals(match.getStatusOrigin())) {// 未开场变成开场
                match.setHomeScore("0");
                match.setGuestScore("0");
            }
            if (data.get("keepTime") != null) {
                match.setKeepTime(data.get("keepTime"));
            }
            if (data.get("home_yc") != null) {
                match.setHome_yc(data.get("home_yc"));
            }
            if (data.get("guest_yc") != null) {
                match.setGuest_yc(data.get("guest_yc"));
            }
            if (data.get("homeHalfScore") != null) {
                match.setHomeHalfScore(data.get("homeHalfScore"));
            }
            if (data.get("guestHalfScore") != null) {
                match.setGuestHalfScore(data.get("guestHalfScore"));
            }
            getList().set(position, match);
            notifyItemChanged(position);
        }

        // 界面1分钟后销毁
        if (target[0] != null) {
            /**
             * 0：未开，页面显示VS
             1：上半场，页面显示KeepTime（注：KeepTime>45，显示45+）
             3：下半场，显示KeepTime（注： KeepTime>90，显示90+）
             2：中场
             4：加时
             5：点球
             -1：完场，在KeepTime位置显示半场比分(1分钟后清除)
             -10：取消，1分钟后清除
             -11：待定
             -12：腰斩，1分钟后清除
             -13：中断
             -14：推迟，1分钟后清除
             */
            if ("-1".equals(target[0].getStatusOrigin()) || "-10".equals(target[0].getStatusOrigin()) || "-12".equals(target[0].getStatusOrigin()) || "-14".equals(target[0].getStatusOrigin())) {
                RxBus.getDefault().post(target[0]);
            }
        }
    }

    private void updateTypeOdds(Match match, int position, WebSocketMatchOdd webSocketMatchOdd) {
        if (match.getThirdId().equals(webSocketMatchOdd.getThirdId())) {
            List<Map<String, String>> matchOddDataLists = webSocketMatchOdd.getData();
            for (Map<String, String> oddDataMaps : matchOddDataLists) {
                // 亚赔
                if (oddDataMaps.get("handicap").equals("asiaLet")) {
                    MatchOdd asiaLetOdd = this.getMatchOdd(match, "asiaLet");
                    this.updateMatchOdd(match, asiaLetOdd, oddDataMaps, "asiaLet");
                    // 欧赔
                } else if (oddDataMaps.get("handicap").equals("euro")) {
                    MatchOdd euroOdd = this.getMatchOdd(match, "euro");
                    this.updateMatchOdd(match, euroOdd, oddDataMaps, "euro");
                    // 大小
                } else if (oddDataMaps.get("handicap").equals("asiaSize")) {
                    MatchOdd asiaSizeOdd = this.getMatchOdd(match, "asiaSize");
                    this.updateMatchOdd(match, asiaSizeOdd, oddDataMaps, "asiaSize");
                }
            }
            getList().set(position, match);
            notifyItemChanged(position);
        }
    }

    private void updateTypeEvent(final Match match, final int position, WebSocketMatchEvent webSocketMatchEvent) {
        if (match.getThirdId().equals(webSocketMatchEvent.getThirdId())) {
            match.setItemBackGroundColorId(R.color.item_football_event_yellow);
            String eventType = webSocketMatchEvent.getData().get("eventType");
            if ("1".equals(eventType) || "2".equals(eventType) || "5".equals(eventType) || "6".equals(eventType)) {// 主队有效进球传
                // 客队有效进球5or客队进球取消6
                if (webSocketMatchEvent.getData().get("homeScore") != null) {
                    match.setHomeScore(webSocketMatchEvent.getData().get("homeScore"));
                }
                if (webSocketMatchEvent.getData().get("guestScore") != null) {
                    match.setGuestScore(webSocketMatchEvent.getData().get("guestScore"));
                }
            } else if ("3".equals(eventType) || "4".equals(eventType) || "7".equals(eventType) || "8".equals(
                    eventType)) {// 主队红牌3or主队红牌取消4
                if (webSocketMatchEvent.getData().get("home_rc") != null) {
                    match.setHome_rc(webSocketMatchEvent.getData().get("home_rc"));
                }
                if (webSocketMatchEvent.getData().get("guest_rc") != null) {
                    match.setGuest_rc(webSocketMatchEvent.getData().get("guest_rc"));
                }
                if (webSocketMatchEvent.getData().get("home_yc") != null) {
                    match.setHome_yc(webSocketMatchEvent.getData().get("home_yc"));
                }
                if (webSocketMatchEvent.getData().get("guest_yc") != null) {
                    match.setGuest_yc(webSocketMatchEvent.getData().get("guest_yc"));
                }
            }

            if ("1".equals(eventType) || "2".equals(eventType) || "3".equals(eventType) || "4".equals(eventType)) {// 主场红名
                match.setHomeTeamTextColorId(R.color.red);
            } else if ("5".equals(eventType) || "6".equals(eventType) || "7".equals(eventType) || "8".equals(eventType)) {// 客场红名
                match.setGuestTeamTextColorId(R.color.red);
            }

            getList().set(position, match);
            notifyItemChanged(position);// 先换颜色

            new Handler().postDelayed(new Runnable() {// 五秒后把颜色修改回来
                @Override
                public void run() {
                    match.setItemBackGroundColorId(R.color.white);
                    match.setHomeTeamTextColorId(R.color.msg);
                    match.setGuestTeamTextColorId(R.color.msg);
                    getList().set(position, match);
                    notifyItemChanged(position);
                }
            }, 5 * 1000);

        }
    }

    private void updateTypeMatchChange(Match match, int position, WebSocketMatchChange webSocketMatchChange) {
        if (match.getThirdId().equals(webSocketMatchChange.getThirdId())) {
            String language = PreferenceUtil.getString("language", "rCN");
            if ((webSocketMatchChange.getData().get("region").equals("zh") && language.equals(
                    "rCN")) || (webSocketMatchChange.getData().get("region").equals("zh-TW") && language.equals(
                    "rTW")) || (webSocketMatchChange.getData().get("region").equals("en") && language.equals(
                    "rEN")) || (webSocketMatchChange.getData().get("region").equals("ko") && language.equals(
                    "rKO")) || (webSocketMatchChange.getData().get("region").equals("id") && language.equals(
                    "rID")) || (webSocketMatchChange.getData().get("region").equals("th") && language.equals(
                    "rTH")) || (webSocketMatchChange.getData().get("region").equals("vi") && language.equals("rVI"))) {

                if (webSocketMatchChange.getData().get("hometeam") != null) {
                    match.setHometeam(webSocketMatchChange.getData().get("hometeam"));
                }

                if (webSocketMatchChange.getData().get("guestteam") != null) {
                    match.setGuestteam(webSocketMatchChange.getData().get("guestteam"));
                }

                if (webSocketMatchChange.getData().get("racename") != null) {
                    match.setRacename(webSocketMatchChange.getData().get("racename"));
                }
                getList().set(position, match);
                notifyItemChanged(position);
            }
        }
    }

    private MatchOdd getMatchOdd(Match match, String handicap) {
        return match.getMatchOdds().get(handicap);
    }

    private void updateMatchOdd(Match match, MatchOdd matchOdd, Map<String, String> oddDataMaps, String key) {
        if (matchOdd == null) {
            matchOdd = new MatchOdd(key, oddDataMaps.get("mediumOdds"), oddDataMaps.get("rightOdds"), oddDataMaps.get("leftOdds"));
            match.getMatchOdds().put(key, matchOdd);
        } else {
            if (!key.equals("euro")) {
                matchOdd.setTypeOddds(oddDataMaps.get("mediumOdds"), oddDataMaps.get("rightOdds"), oddDataMaps.get("leftOdds"));
            } else {
                matchOdd.setEuroTypeOdds(oddDataMaps.get("mediumOdds"), oddDataMaps.get("rightOdds"), oddDataMaps.get("leftOdds"));
            }
        }
    }
}
