package com.hhly.mlottery.adapter.cpiadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.CpiDetailsActivity;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.frame.CPIFragment;
import com.hhly.mlottery.widget.CpiListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tjl on 2016年4月14日 12:27:10
 * 新版指数（亚盘，大小，欧赔）CPIRecyclerViewAdapter
 */
public class CPIRecyclerViewAdapter extends RecyclerView.Adapter<CPIRecyclerViewAdapter.CPIViewHolder> {

    private List<NewOddsInfo.AllInfoBean> mAllInfoBean;
    private Context context;
    //cardview里面listview的适配器
    private CardViewListAdapter cardViewListAdapter;

    //标记是哪个类型“亚盘，大小，欧赔
    private String stType;


    public CPIRecyclerViewAdapter(List<NewOddsInfo.AllInfoBean> mAllInfoBean, Context context, String stType) {
        this.mAllInfoBean = mAllInfoBean;
        this.context = context;
        this.stType = stType;
    }


    //自定义ViewHolder类
    static class CPIViewHolder extends RecyclerView.ViewHolder {

        CardView cpi_item_cardview;//cardview
        CpiListView item_cpi_odds_listview;//cardview里面list
        TextView cpi_item_leagueName_txt;//联赛名称
        TextView cpi_item_time_txt;//比赛时间
        TextView cpi_scoreAndName_txt;//比分

        TextView cpi_item_home_txt;//主队
        TextView cpi_item_odds_txt;//盘口
        TextView cpi_item_guest_txt;//客队
        LinearLayout cpi_item_layout;//比赛进行时候即时时间的layout
        TextView cpi_item_keepTime_txt;//即时时间textview
        TextView cpi_item_seconds_txt;//即时时间分钟的 “ ' ”
        TextView tv_tag;


        public CPIViewHolder(final View itemView) {
            super(itemView);
            cpi_item_cardview = (CardView) itemView.findViewById(R.id.cpi_item_cardview);
            item_cpi_odds_listview = (CpiListView) itemView.findViewById(R.id.item_cpi_odds_list);
            cpi_item_leagueName_txt = (TextView) itemView.findViewById(R.id.cpi_item_leagueName_txt);
            cpi_item_time_txt = (TextView) itemView.findViewById(R.id.cpi_item_time_txt);
            cpi_scoreAndName_txt = (TextView) itemView.findViewById(R.id.cpi_scoreAndName_txt);

            cpi_item_home_txt = (TextView) itemView.findViewById(R.id.cpi_item_home_txt);
            cpi_item_odds_txt = (TextView) itemView.findViewById(R.id.cpi_item_odds_txt);
            cpi_item_guest_txt = (TextView) itemView.findViewById(R.id.cpi_item_guest_txt);

            cpi_item_layout = (LinearLayout) itemView.findViewById(R.id.cpi_item_layout);
            cpi_item_keepTime_txt = (TextView) itemView.findViewById(R.id.cpi_item_keepTime_txt);
            cpi_item_seconds_txt = (TextView) itemView.findViewById(R.id.cpi_item_seconds_txt);

            tv_tag = (TextView) itemView.findViewById(R.id.tv_tag);
        }
    }

    @Override
    public CPIViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_cpi_odds, viewGroup, false);
        return new CPIViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CPIViewHolder cpiViewHolder, final int position) {
        startInfinateAnimation(cpiViewHolder.cpi_item_seconds_txt);
        if (CPIFragment.TYPE_PLATE.equals(stType)) {
            cpiViewHolder.cpi_item_home_txt.setText(R.string.odd_home_txt);
            cpiViewHolder.cpi_item_odds_txt.setText(R.string.odd_dish_txt);
            cpiViewHolder.cpi_item_guest_txt.setText(R.string.odd_guest_txt);
        } else if (CPIFragment.TYPE_BIG.equals(stType)) {
            cpiViewHolder.cpi_item_home_txt.setText(R.string.odd_home_big_txt);
            cpiViewHolder.cpi_item_odds_txt.setText(R.string.odd_dish_txt);
            cpiViewHolder.cpi_item_guest_txt.setText(R.string.odd_guest_big_txt);
        } else if (CPIFragment.TYPE_OP.equals(stType)) {
            cpiViewHolder.cpi_item_home_txt.setText(R.string.odd_home_op_txt);
            cpiViewHolder.cpi_item_odds_txt.setText(R.string.odd_dish_op_txt);
            cpiViewHolder.cpi_item_guest_txt.setText(R.string.odd_guest_op_txt);
        }

        final NewOddsInfo.AllInfoBean allInfoBean = mAllInfoBean.get(position);
        final NewOddsInfo.AllInfoBean.MatchInfoBean matchInfo = allInfoBean.getMatchInfo();
        //获得联赛名称
        cpiViewHolder.cpi_item_leagueName_txt.setText(allInfoBean.getLeagueName());
        cpiViewHolder.cpi_item_leagueName_txt.setTextColor(Color.parseColor(allInfoBean.getLeagueColor()));
        //比赛时间
        cpiViewHolder.cpi_item_time_txt.setText(matchInfo.getOpenTime());
        //如果推送即时比赛开赛了
        if (matchInfo.isShowTitle()) {
            cpiViewHolder.cpi_item_layout.setVisibility(View.VISIBLE);
            cpiViewHolder.cpi_item_keepTime_txt.setText(matchInfo.getKeepTime());

            String maybeTime = matchInfo.getKeepTime();
            if (maybeTime.indexOf("+") > 0) {
                maybeTime = maybeTime.substring(0, maybeTime.length() - 1);
            }

            try {
                int i = Integer.parseInt(maybeTime);
                cpiViewHolder.cpi_item_keepTime_txt.setTextColor(
                        ContextCompat.getColor(context, R.color.colorPrimary));
                cpiViewHolder.cpi_item_seconds_txt.setVisibility(View.VISIBLE);

                cpiViewHolder.cpi_item_keepTime_txt.setTextColor(
                        ContextCompat.getColor(context, R.color.colorPrimary));
            } catch (Exception e) {
                e.printStackTrace();
                cpiViewHolder.cpi_item_keepTime_txt.setTextColor(
                        ContextCompat.getColor(context, R.color.analyze_left));
                cpiViewHolder.cpi_item_seconds_txt.setVisibility(View.GONE);
            }
            cpiViewHolder.cpi_item_time_txt.setVisibility(View.GONE);
            cpiViewHolder.tv_tag.setVisibility(View.GONE);
        } else {
            cpiViewHolder.tv_tag.setVisibility(View.VISIBLE);
            cpiViewHolder.cpi_item_layout.setVisibility(View.GONE);
        }

        //亚盘
        switch (stType) {
            case CPIFragment.TYPE_PLATE:
                for (int i = 0; i < allInfoBean.getComList().size(); i++) {
                    //亚盘如果降
                    if (allInfoBean.getComList().get(i).getCurrLevel().getMiddleUp() == -1) {
                        allInfoBean.getComList().get(i).getCurrLevel().setCurrTextBgColor("green");
                    }
                    //亚盘如果升
                    else if (allInfoBean.getComList().get(i).getCurrLevel().getMiddleUp() == 1) {
                        allInfoBean.getComList().get(i).getCurrLevel().setCurrTextBgColor("red");
                    } else {
                        allInfoBean.getComList().get(i).getCurrLevel().setCurrTextBgColor("black");
                    }
                }
                cardViewListAdapter = new CardViewListAdapter(context, allInfoBean.getComList(), stType);
                break;
            case CPIFragment.TYPE_BIG:
                for (int i = 0; i < allInfoBean.getComList().size(); i++) {
                    //大小球如果降
                    if (allInfoBean.getComList().get(i).getCurrLevel().getMiddleUp() == -1) {
                        allInfoBean.getComList().get(i).getCurrLevel().setCurrTextBgColor("green");
                    }
                    //大小球如果升
                    else if (allInfoBean.getComList().get(i).getCurrLevel().getMiddleUp() == 1) {
                        allInfoBean.getComList().get(i).getCurrLevel().setCurrTextBgColor("red");
                    } else {
                        allInfoBean.getComList().get(i).getCurrLevel().setCurrTextBgColor("black");
                    }
                }
                cardViewListAdapter = new CardViewListAdapter(context, allInfoBean.getComList(), stType);
                break;
            default:
                cardViewListAdapter = new CardViewListAdapter(context, allInfoBean.getComList(), stType);
                break;
        }

        //比赛的主客队名称和比分
        if ("0".equals(matchInfo.getMatchState())) {
            //未开赛
            cpiViewHolder.cpi_scoreAndName_txt.setText(Html.fromHtml(matchInfo.getMatchHomeName() +
                    "&nbsp;" + "VS" + "&nbsp;" + matchInfo.getMatchGuestName()));
        } else { //开赛
            cpiViewHolder.cpi_scoreAndName_txt.setText(Html.fromHtml(matchInfo.getMatchHomeName() +
                    "<font color=#ff0000>&nbsp;" + matchInfo.getMatchResult() + "</font>&nbsp;" + matchInfo.getMatchGuestName()));
        }
        if ("0".equals(matchInfo.getMatchState())) {
            cpiViewHolder.tv_tag.setTextColor(
                    ContextCompat.getColor(context, R.color.textcolor_football_footer_normal));
            cpiViewHolder.tv_tag.setText(R.string.not_start_short_txt);
        } else if ("-1".equals(matchInfo.getMatchState())) {
            cpiViewHolder.tv_tag.setTextColor(ContextCompat.getColor(context, R.color.analyze_left));
            cpiViewHolder.tv_tag.setText(R.string.fragme_home_wanchang_text);
        } else if ("-10".equals(matchInfo.getMatchState())) {
            cpiViewHolder.tv_tag.setTextColor(ContextCompat.getColor(context, R.color.analyze_left));
            cpiViewHolder.tv_tag.setText(R.string.fragme_home_quxiao_text);
        } else if ("-11".equals(matchInfo.getMatchState())) {
            cpiViewHolder.tv_tag.setTextColor(ContextCompat.getColor(context, R.color.analyze_left));
            cpiViewHolder.tv_tag.setText(R.string.fragme_home_daiding_text);
        } else if ("-12".equals(matchInfo.getMatchState())) {
            cpiViewHolder.tv_tag.setTextColor(ContextCompat.getColor(context, R.color.analyze_left));
            cpiViewHolder.tv_tag.setText(R.string.fragme_home_yaozhan_text);
        } else if ("-13".equals(matchInfo.getMatchState())) {
            cpiViewHolder.tv_tag.setTextColor(ContextCompat.getColor(context, R.color.analyze_left));
            cpiViewHolder.tv_tag.setText(R.string.fragme_home_zhongduan_text);
        } else if ("-14".equals(matchInfo.getMatchState())) {
            cpiViewHolder.tv_tag.setTextColor(ContextCompat.getColor(context, R.color.analyze_left));
            cpiViewHolder.tv_tag.setText(R.string.fragme_home_tuichi_text);
        }
        try {
            int stateInt = Integer.parseInt(matchInfo.getMatchState());
            if (stateInt > 0) {
                cpiViewHolder.cpi_scoreAndName_txt.setText(Html.fromHtml(matchInfo.getMatchHomeName() + "<font color=#0085e1>&nbsp;"
                        + matchInfo.getMatchResult() + "</font>&nbsp;" + matchInfo.getMatchGuestName()));
                cpiViewHolder.tv_tag.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                cpiViewHolder.cpi_item_time_txt.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            } else {
                cpiViewHolder.cpi_item_time_txt.setTextColor(ContextCompat.getColor(context, android.R.attr.textColorSecondary));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cpiViewHolder.item_cpi_odds_listview.setDivider(ContextCompat.getDrawable(context, R.color.homwe_grey));
        cpiViewHolder.item_cpi_odds_listview.setDividerHeight(1);
        cpiViewHolder.item_cpi_odds_listview.setAdapter(cardViewListAdapter);
        new CpiListView(context).setListViewHeightBasedOnChildren(cpiViewHolder.item_cpi_odds_listview);

        cpiViewHolder.item_cpi_odds_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positionNunber, long id) {

                List<Map<String, String>> obList = new ArrayList<>();// 建立一个数组存储listview上显示的数据
                for (int m = 0; m < allInfoBean.getComList().size(); m++) {
                    Map<String, String> obMap = new HashMap<>();
                    obMap.put("id", allInfoBean.getComList().get(m).getComId());
                    obMap.put("name", allInfoBean.getComList().get(m).getComName());
                    obMap.put("thirdid", matchInfo.getMatchId());
                    obList.add(obMap);
                }
                //点击指数页面，传值给详情界面
                Intent intent = new Intent(context, CpiDetailsActivity.class);
                intent.putExtra("obListEntity", (Serializable) obList);
                intent.putExtra("comId", allInfoBean.getComList().get(positionNunber).getComId());
                intent.putExtra("positionNunber", positionNunber + "");
                intent.putExtra("stType", stType);
                //如果未开赛
                if ("0".equals(matchInfo.getMatchState())) {
                    intent.putExtra("PKScore", matchInfo.getMatchHomeName()
                            + "\t" + "VS" + "\t" + matchInfo.getMatchGuestName());
                } else {
                    //否则开赛了
                    intent.putExtra("PKScore", matchInfo.getMatchHomeName()
                            + "\t" + matchInfo.getMatchResult() + "\t" + matchInfo.getMatchGuestName());
                }
                context.startActivity(intent);
            }
        });
        //为 cardView设置点击事件
        cpiViewHolder.cpi_item_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FootballMatchDetailActivity.class);
                intent.putExtra("thirdId", matchInfo.getMatchId());
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mAllInfoBean.size();
    }

    public void setAllInfoBean(List<NewOddsInfo.AllInfoBean> allInfoBean) {
        this.mAllInfoBean = allInfoBean;
    }

    /**
     * 清除数据
     */
    public void clearData() {
        // clear the data
        mAllInfoBean.clear();
        cardViewListAdapter.cardViewclearData();
        notifyDataSetChanged();
    }

    private void startInfinateAnimation(final View view) {
        final AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 1);
        alphaAnimation1.setDuration(500);
        final AlphaAnimation alphaAnimation0 = new AlphaAnimation(0, 0);
        alphaAnimation0.setDuration(500);
        alphaAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(alphaAnimation0);
            }
        });

        alphaAnimation0.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(alphaAnimation1);

            }
        });
        view.startAnimation(alphaAnimation1);
    }

}
