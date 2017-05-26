package com.hhly.mlottery.adapter.football;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewAdapter;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewHolder;
import com.hhly.mlottery.bean.footballDetails.MatchTimeLiveBean;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.StadiumUtils;

import java.util.List;

/**
 * 描述:  足球内页事件直播Adpter
 * 作者:  wangg@13322.com
 * 时间:  2016/8/9 15:40
 */
public class EventAdapter extends BaseRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_DEFAULT = 1;
    private static final int VIEW_TYPE_HALF_FINISH = 2;
    private static final int VIEW_TYPE_HALF_PENALTY = 3;
    private static final int VIEW_TYPE_HALF_EXTRA_TIME = 4;

    private static final String HOME = "1"; //主队
    private static final String GUEST = "0"; //客队


    //主队事件
    private static final String SCORE = "1029";//主队进球
    private static final String RED_CARD = "1032";
    private static final String YELLOW_CARD = "1034";
    private static final String SUBSTITUTION = "1055";
    private static final String CORNER = "1025";
    private static final String YTORED = "1045";//两黄变一红
    private static final String DIANQIU = "1031";//点球

    //客队事件
    private static final String SCORE1 = "2053";//客队进球
    private static final String RED_CARD1 = "2056";
    private static final String YELLOW_CARD1 = "2058";
    private static final String SUBSTITUTION1 = "2079";
    private static final String CORNER1 = "2049";
    private static final String YTORED1 = "2069";//两黄变一红
    private static final String DIANQIU1 = "2055";//点球


    /**
     * 上半场
     */
    private static final String FIRSTHALF = "1";
    /**
     * 中场
     */
    private static final String HALFTIME = "2";

    /**
     * 下班场
     */
    private static final String SECONDTIME = "3";


    private static final String DIANQIUTIME = "5";  //code=18

    private Context mContext;

    private List<MatchTimeLiveBean> matchTimeLiveBeans;


    public EventAdapter(Context mContext, List<MatchTimeLiveBean> eventMatchLive) {
        this.mContext = mContext;
        this.matchTimeLiveBeans = eventMatchLive;
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        int itemViewType = getRecycleViewItemType(position);

        switch (itemViewType) {
            case VIEW_TYPE_DEFAULT:
                BindRecycleViewData(viewHolder, getItemCount() - position - 1);
                break;
            case VIEW_TYPE_HALF_FINISH:
                BindRecycleViewMatchStatus(viewHolder, getItemCount() - position - 1);
                break;
       /*     case VIEW_TYPE_HALF_PENALTY:
                break;
            case VIEW_TYPE_HALF_EXTRA_TIME:
                break;*/
        }
    }



    private void BindRecycleViewMatchStatus(BaseRecyclerViewHolder viewHolder, int position) {
        TextView tv_score = viewHolder.findViewById(R.id.tv_score);
        TextView tv_status = viewHolder.findViewById(R.id.tv_status);
        MatchTimeLiveBean m = matchTimeLiveBeans.get(position);


        if ("2".equals(matchTimeLiveBeans.get(position).getState()) && "1".equals(matchTimeLiveBeans.get(position).getCode())) {
            L.d("ccvvbn", "_______H" + matchTimeLiveBeans.get(position).getState() + "====" + matchTimeLiveBeans.get(position).getCode());

            tv_score.setText(m.getIsHome());
            tv_status.setText("H");
        } else if ("-1".equals(matchTimeLiveBeans.get(position).getState()) && "3".equals(matchTimeLiveBeans.get(position).getCode())) {

            L.d("ccvvbn", "_______" + matchTimeLiveBeans.get(position).getState() + "====" + matchTimeLiveBeans.get(position).getCode());
            tv_score.setText(m.getIsHome());
            tv_status.setText("F");
        }
    }

    private void BindRecycleViewData(BaseRecyclerViewHolder viewHolder, int position) {
        TextView tv_time = viewHolder.findViewById(R.id.tv_time);
        TextView msg_left = viewHolder.findViewById(R.id.msg_left);
        TextView msg_right = viewHolder.findViewById(R.id.msg_right);
        ImageView iv_left = viewHolder.findViewById(R.id.iv_left);
        ImageView iv_right = viewHolder.findViewById(R.id.iv_right);
        TextView line = viewHolder.findViewById(R.id.line);
        if (position == 0) {
            line.setVisibility(View.GONE);
        } else {
            line.setVisibility(View.VISIBLE);
        }

        // MatchTimeLiveBean m = matchTimeLiveBeans.get(position);
        //  tv_time.setText(m.getTime() + "'");

        MatchTimeLiveBean m = matchTimeLiveBeans.get(position);
        msg_left.setVisibility(View.VISIBLE);
        iv_left.setVisibility(View.VISIBLE);
        msg_right.setVisibility(View.VISIBLE);
        iv_right.setVisibility(View.VISIBLE);

        if (FIRSTHALF.equals(m.getState()) && StadiumUtils.convertStringToInt(m.getTime()) > 45) {
            tv_time.setText("45+'");
            tv_time.setBackgroundResource(R.drawable.live_text_time);

        } else if (SECONDTIME.equals(m.getState()) && StadiumUtils.convertStringToInt(m.getTime()) > 90) {
            tv_time.setText("90+" + (StadiumUtils.convertStringToInt(m.getTime()) - 90) + "'");
            tv_time.setBackgroundResource(R.drawable.live_text_ot);
        } else {
            tv_time.setText(StadiumUtils.convertStringToInt(m.getTime()) + "'");
            tv_time.setBackgroundResource(R.drawable.live_text_time);

        }

        if (m.getPlayInfo() == null) {
            m.setPlayInfo("");
        }

        if (HOME.equals(m.getIsHome())) {

            msg_right.setVisibility(View.INVISIBLE);
            iv_right.setVisibility(View.INVISIBLE);
            if (SCORE.equals(m.getCode())) {

                msg_left.setText(m.getPlayInfo() + mContext.getResources().getString(R.string.foot_event_di) + m.getEventnum() + mContext.getResources().getString(R.string.foot_event_ge));
                iv_left.setImageResource(R.mipmap.event_goal);
            } else if (YELLOW_CARD.equals(m.getCode())) {
                msg_left.setText(m.getPlayInfo() + mContext.getResources().getString(R.string.foot_event_di) + m.getEventnum() + mContext.getResources().getString(R.string.foot_event_zhang));
                iv_left.setImageResource(R.mipmap.event_yc);
            } else if (RED_CARD.equals(m.getCode())) {
                msg_left.setText(m.getPlayInfo() + mContext.getResources().getString(R.string.foot_event_di) + m.getEventnum() + mContext.getResources().getString(R.string.foot_event_zhang));
                iv_left.setImageResource(R.mipmap.event_rc);
            } else if (SUBSTITUTION.equals(m.getCode())) {
                // No:人名|No:人名(前上后下)
                if (m.getPlayInfo() != null && !"".equals(m.getPlayInfo())) {
                    if (m.getPlayInfo().contains("|")) {

                        msg_left.setText(m.getPlayInfo().split("|")[0] + mContext.getResources().getString(R.string.foot_event_in) + m.getPlayInfo().split("|")[1] + mContext.getResources().getString(R.string.foot_event_out));
                    } else {

                        if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) { //国内
                            msg_left.setText(m.getPlayInfo().split(":")[0] + mContext.getResources().getString(R.string.foot_event_num_in) + m.getPlayInfo().split(":")[1] + mContext.getResources().getString(R.string.foot_event_num_out));
                        } else {
                            msg_left.setText(mContext.getResources().getString(R.string.foot_event_num) + m.getPlayInfo().split(":")[0] + mContext.getResources().getString(R.string.foot_event_num_in) + mContext.getResources().getString(R.string.foot_event_num) + m.getPlayInfo().split(":")[1] + mContext.getResources().getString(R.string.foot_event_num_out));
                        }
                    }
                } else {
                    msg_left.setText(mContext.getResources().getString(R.string.foot_event_player));
                }

                iv_left.setImageResource(R.mipmap.event_player);
            } else if (CORNER.equals(m.getCode())) {
                msg_left.setText(m.getPlayInfo() + mContext.getResources().getString(R.string.foot_event_di) + m.getEventnum() + mContext.getResources().getString(R.string.foot_event_ge));
                iv_left.setImageResource(R.mipmap.event_corner);
            } else if (YTORED.equals(m.getCode())) {
                msg_left.setText(m.getPlayInfo() + mContext.getResources().getString(R.string.foot_event_ychanger));
                iv_left.setImageResource(R.mipmap.event_ytor);
            } else if (DIANQIU.equals(m.getCode())) {

                msg_left.setText(m.getPlayInfo() + mContext.getResources().getString(R.string.foot_event_di) + m.getEventnum() + mContext.getResources().getString(R.string.foot_event_ge));
                iv_left.setImageResource(R.mipmap.event_penalty);
            }


        } else {

            msg_left.setVisibility(View.INVISIBLE);
            iv_left.setVisibility(View.INVISIBLE);

            if (SCORE1.equals(m.getCode())) {

                msg_right.setText(mContext.getResources().getString(R.string.foot_event_di) + m.getEventnum() + mContext.getResources().getString(R.string.foot_event_ge));
                iv_right.setImageResource(R.mipmap.event_goal);
            } else if (YELLOW_CARD1.equals(m.getCode())) {

                msg_right.setText(mContext.getResources().getString(R.string.foot_event_di) + m.getEventnum() + mContext.getResources().getString(R.string.foot_event_zhang) + m.getPlayInfo());
                iv_right.setImageResource(R.mipmap.event_yc);

            } else if (RED_CARD1.equals(m.getCode())) {

                msg_right.setText(mContext.getResources().getString(R.string.foot_event_di) + m.getEventnum() + mContext.getResources().getString(R.string.foot_event_zhang) + m.getPlayInfo());
                iv_right.setImageResource(R.mipmap.event_rc);
            } else if (SUBSTITUTION1.equals(m.getCode())) {

                if (m.getPlayInfo() != null && !"".equals(m.getPlayInfo())) {
                    if (m.getPlayInfo().contains("|")) {
                        msg_right.setText(m.getPlayInfo().split("|")[0] + mContext.getResources().getString(R.string.foot_event_in) + m.getPlayInfo().split("|")[1] + mContext.getResources().getString(R.string.foot_event_out));
                    } else {
                        if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) { //国内
                            msg_right.setText(m.getPlayInfo().split(":")[0] + mContext.getResources().getString(R.string.foot_event_num_in) + m.getPlayInfo().split(":")[1] + mContext.getResources().getString(R.string.foot_event_num_out));
                        } else {
                            msg_right.setText(mContext.getResources().getString(R.string.foot_event_num) + m.getPlayInfo().split(":")[0] + mContext.getResources().getString(R.string.foot_event_num_in) + mContext.getResources().getString(R.string.foot_event_num) + m.getPlayInfo().split(":")[1] + mContext.getResources().getString(R.string.foot_event_num_out));
                        }
                    }
                } else {
                    msg_right.setText(mContext.getResources().getString(R.string.foot_event_player));
                }
                iv_right.setImageResource(R.mipmap.event_player);
            } else if (CORNER1.equals(m.getCode())) {

                msg_right.setText(mContext.getResources().getString(R.string.foot_event_di) + m.getEventnum() + mContext.getResources().getString(R.string.foot_event_ge) + m.getPlayInfo());
                iv_right.setImageResource(R.mipmap.event_corner);
            } else if (YTORED1.equals(m.getCode())) {
                msg_right.setText(mContext.getResources().getString(R.string.foot_event_ychanger) + m.getPlayInfo());
                iv_right.setImageResource(R.mipmap.event_ytor);
            } else if (DIANQIU1.equals(m.getCode())) {
                msg_right.setText(mContext.getResources().getString(R.string.foot_event_di) + m.getEventnum() + mContext.getResources().getString(R.string.foot_event_ge) + m.getPlayInfo());
                iv_right.setImageResource(R.mipmap.event_penalty);
            }
        }
    }


    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.item_event_normal, R.layout.item_event_half_finish, R.layout.item_event_dianqiu, R.layout.item_event_jiashi};
    }

    @Override
    public int getItemCount() {
        return matchTimeLiveBeans.size();
    }

    @Override
    public int getRecycleViewItemType(int position) {
        if ("2".equals(matchTimeLiveBeans.get(getItemCount() - position - 1).getState()) && "1".equals(matchTimeLiveBeans.get(getItemCount() - position - 1).getCode())) {
            return VIEW_TYPE_HALF_FINISH;
        } else if ("-1".equals(matchTimeLiveBeans.get(getItemCount() - position - 1).getState()) && "3".equals(matchTimeLiveBeans.get(getItemCount() - position - 1).getCode())) {
            return VIEW_TYPE_HALF_FINISH;
        } else if ("18".equals(matchTimeLiveBeans.get(getItemCount() - position - 1).getCode()) || "19".equals(matchTimeLiveBeans.get(getItemCount() - position - 1).getCode())) {
            return VIEW_TYPE_HALF_PENALTY;
        } else if ("14".equals(matchTimeLiveBeans.get(getItemCount() - position - 1).getCode()) || "15".equals(matchTimeLiveBeans.get(getItemCount() - position - 1))) {
            return VIEW_TYPE_HALF_EXTRA_TIME;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

}
