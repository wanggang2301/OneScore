package com.hhly.mlottery.adapter.football;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewAdapter;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewHolder;
import com.hhly.mlottery.bean.footballDetails.MatchTimeLiveBean;

import java.util.List;

/**
 * 描述:  足球内页事件
 * 作者:  wangg@13322.com
 * 时间:  2016/8/9 15:40
 */
public class EventAdapter extends BaseRecyclerViewAdapter {

    private static final int VIEW_TYPE_DEFAULT = 1;
    private static final int VIEW_TYPE_HALF_FINISH = 2;

    private static final String HOME = "1"; //主队
    private static final String GUEST = "0"; //客队


    //主队事件
    private static final String SCORE = "1029";//主队进球
    private static final String RED_CARD = "1032";
    private static final String YELLOW_CARD = "1034";
    private static final String SUBSTITUTION = "1055";
    private static final String CORNER = "1025";
    private static final String YTORED = "1045";//两黄变一红
    //客队事件
    private static final String SCORE1 = "2053";//客队进球
    private static final String RED_CARD1 = "2056";
    private static final String YELLOW_CARD1 = "2058";
    private static final String SUBSTITUTION1 = "2079";
    private static final String CORNER1 = "2049";
    private static final String YTORED1 = "2069";//两黄变一红


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
                BindRecycleViewData(viewHolder, matchTimeLiveBeans.size() - position - 1);

                break;
            case VIEW_TYPE_HALF_FINISH:
                TextView tv_score = viewHolder.findViewById(R.id.tv_score);
                TextView tv_status = viewHolder.findViewById(R.id.tv_status);

                break;
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

        if (HOME.equals(m.getIsHome())) {
            tv_time.setText(m.getTime() + "'");
            msg_left.setVisibility(View.INVISIBLE);
            iv_left.setVisibility(View.INVISIBLE);

            if (SCORE.equals(m.getCode())) {

                msg_right.setText("第" + m.getEventnum() + "个进球");
                iv_right.setImageResource(R.mipmap.event_goal);
            } else if (YELLOW_CARD.equals(m.getCode())) {

                msg_right.setText("第" + m.getEventnum() + "个黄牌");
                iv_right.setImageResource(R.mipmap.event_yc);

            } else if (RED_CARD.equals(m.getCode())) {

                msg_right.setText("第" + m.getEventnum() + "个红牌");
                iv_right.setImageResource(R.mipmap.event_rc);
            } else if (SUBSTITUTION.equals(m.getCode())) {
                msg_right.setText("换人");
                iv_right.setImageResource(R.mipmap.event_player);
            } else if (CORNER.equals(m.getCode())) {

                msg_right.setText("第" + m.getEventnum() + "个角球");
                iv_right.setImageResource(R.mipmap.event_corner);
            } else if (YTORED.equals(m.getCode())) {
                msg_right.setText("两黄变一红");
                iv_right.setImageResource(R.mipmap.event_ytor);
            }
        } else {
            tv_time.setText(m.getTime() + "'");
            msg_right.setVisibility(View.INVISIBLE);
            iv_right.setVisibility(View.INVISIBLE);
            if (SCORE1.equals(m.getCode())) {

                msg_left.setText("第" + m.getEventnum() + "个进球");
                iv_left.setImageResource(R.mipmap.event_goal);
            } else if (YELLOW_CARD1.equals(m.getCode())) {

                msg_left.setText("第" + m.getEventnum() + "个黄牌");
                iv_left.setImageResource(R.mipmap.event_yc);

            } else if (RED_CARD1.equals(m.getCode())) {

                msg_left.setText("第" + m.getEventnum() + "个红牌");
                iv_left.setImageResource(R.mipmap.event_rc);
            } else if (SUBSTITUTION1.equals(m.getCode())) {
                msg_left.setText("换人");
                iv_left.setImageResource(R.mipmap.event_player);
            } else if (CORNER1.equals(m.getCode())) {

                msg_left.setText("第" + m.getEventnum() + "个角球");
                iv_left.setImageResource(R.mipmap.event_corner);
            } else if (YTORED1.equals(m.getCode())) {
                msg_left.setText("两黄变一红");
                iv_left.setImageResource(R.mipmap.event_ytor);
            }
        }
    }


    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.item_event_normal, R.layout.item_event_half_finish};
    }

    @Override
    public int getItemCount() {
        return matchTimeLiveBeans.size();
    }

    @Override
    public int getRecycleViewItemType(int position) {
        if ("2".equals(matchTimeLiveBeans.get(matchTimeLiveBeans.size() - position - 1).getState())) {
            return VIEW_TYPE_HALF_FINISH;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

}
