package com.hhly.mlottery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.util.StadiumUtils;

import java.util.List;

/**
 * @author wang gang
 * @date 2016/6/28 15:29
 * @des ${}
 */
public class FootBallLiveTextAdapter extends BaseQuickAdapter<MatchTextLiveBean> {

    private Context context;
    private List<MatchTextLiveBean> list;
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


    public FootBallLiveTextAdapter(Context context, List<MatchTextLiveBean> data) {
        super(R.layout.item_live_text, data);
        this.context = context;
        this.list = data;
    }


    @Override
    public int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return super.getViewHolderPosition(viewHolder);
    }

    @Override
    protected void convert(BaseViewHolder holder, MatchTextLiveBean matchTextLiveBean) {

        holder.setText(R.id.time, StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) + "'");
        holder.getView(R.id.xian).setVisibility(View.VISIBLE);


        boolean nostart = true;

        if (list.size() > 1 && getViewHolderPosition(holder) < list.size() - 1) {
            if (StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) - StadiumUtils.convertStringToInt(list.get(getViewHolderPosition(holder) + 1).getTime()) == 0) {
                if (HALFTIME.equals(matchTextLiveBean.getState())) {//中场
                    if (HALFTIME.equals(list.get(getViewHolderPosition(holder) + 1).getState())) {
                        holder.getView(R.id.time).setVisibility(View.INVISIBLE);
                    } else {
                        holder.setText(R.id.time, context.getResources().getString(R.string.paused_txt));
                        holder.getView(R.id.time).setVisibility(View.VISIBLE);
                    }

                } else {
                    holder.getView(R.id.time).setVisibility(View.INVISIBLE);
                }
            } else {
                holder.getView(R.id.time).setVisibility(View.VISIBLE);

                //上半场
                if (FIRSTHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 45) {

                    holder.setText(R.id.time, "45+'");

                } else if (HALFTIME.equals(matchTextLiveBean.getState())) {  //中场休息
                    if (HALFTIME.equals(list.get(getViewHolderPosition(holder) + 1).getState())) {
                        holder.getView(R.id.time).setVisibility(View.INVISIBLE);
                    } else {

                        holder.setText(R.id.time, context.getResources().getString(R.string.paused_txt));
                        holder.getView(R.id.time).setVisibility(View.VISIBLE);

                    }

                } else if (StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 90) {
                    holder.setText(R.id.time, "90+" + (StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) - 90) + "'".trim());

                } else if (MATCHFINISH.equals(matchTextLiveBean.getState())) {
                    // hod.time.setText(context.getResources().getString(R.string.finished_txt));
                    //  hod.time.setVisibility(View.INVISIBLE);

                }
            }
        } else {

            holder.getView(R.id.time).setVisibility(View.VISIBLE);

            if (FIRSTHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 45) {
                holder.setText(R.id.time, "45+'");

            } else if (HALFTIME.equals(matchTextLiveBean.getState())) {
                holder.setText(R.id.time, context.getResources().getString(R.string.paused_txt));

            } else if (StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 90) {
                holder.setText(R.id.time, "90+" + (StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) - 90) + "'".trim());

            } else if (0 == StadiumUtils.convertStringToInt(matchTextLiveBean.getTime())) {
                holder.setText(R.id.time, context.getResources().getString(R.string.not_start_txt));


                nostart = false;
                holder.getView(R.id.xian).setVisibility(View.INVISIBLE);

            }
        }

        int msgPlace = Integer.parseInt(matchTextLiveBean.getMsgPlace());
        //中间
        if (msgPlace == 0 || (msgPlace == 1 && "20".equals(matchTextLiveBean.getCode()))) {  //接口已经没有按以前约定的规则走了，没办法只能这样写了
            if ("99999999".equals(matchTextLiveBean.getTime())) {  //比赛结束
                holder.setText(R.id.zuo_msg_text, matchTextLiveBean.getMsgText());
                holder.setText(R.id.msg_text, "");
                holder.setText(R.id.time, context.getResources().getString(R.string.finished_txt));
                holder.getView(R.id.xian).setVisibility(View.INVISIBLE);


                holder.getView(R.id.zuo_msg_text).setPadding(0, 0, 0, -2);
                holder.getView(R.id.msg_text).setPadding(0, 0, 0, -2);
                holder.getView(R.id.time).setPadding(0, 0, 0, -2);

            } else {
                holder.setText(R.id.msg_text, "");
                holder.getView(R.id.xian).setVisibility(View.VISIBLE);
                holder.getView(R.id.zuo_msg_text).setPadding(0, 7, 0, 7);
                holder.getView(R.id.msg_text).setPadding(0, 7, 0, 7);
            }
            holder.setText(R.id.zuo_msg_text, matchTextLiveBean.getMsgText());

            holder.setBackgroundRes(R.id.time, R.drawable.live_text_finish);

        } else if (msgPlace == 1) {  //主队
            if (nostart) {
                holder.getView(R.id.zuo_msg_text).setPadding(0, 7, 0, 7);
                holder.getView(R.id.msg_text).setPadding(0, 7, 0, 7);
            } else {
                holder.getView(R.id.zuo_msg_text).setPadding(0, -2, 0, 0);
                holder.getView(R.id.msg_text).setPadding(0, -2, 0, 0);
                //  hod.time.setPadding(0, -2, 0, 0);
            }

            holder.setText(R.id.zuo_msg_text, matchTextLiveBean.getMsgText().trim());
            holder.setText(R.id.msg_text, "");
            if (StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 90) {
                if (SECONDHALF.equals(matchTextLiveBean.getState())) {
                    holder.setBackgroundRes(R.id.time, R.drawable.live_text_ot);
                } else if (MATCHFINISH.equals(matchTextLiveBean.getState())) {
                    holder.setBackgroundRes(R.id.time, R.drawable.live_text_ot);

                } else {
                    holder.setBackgroundRes(R.id.time, R.drawable.live_text_time);
                    holder.setText(R.id.time, StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) + "'");
                }
            } else {
                holder.setBackgroundRes(R.id.time, R.drawable.live_text_time);

            }

        } else if (msgPlace == 2) {  //客队
            holder.getView(R.id.zuo_msg_text).setPadding(0, 7, 0, 7);
            holder.getView(R.id.msg_text).setPadding(0, 7, 0, 7);
            //  hod.time.setPadding(0, 7, 0, 7);

            holder.setText(R.id.zuo_msg_text, "");
            holder.setText(R.id.msg_text, matchTextLiveBean.getMsgText().trim());


            if (StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 90) {
                if (SECONDHALF.equals(matchTextLiveBean.getState())) {
                    holder.setBackgroundRes(R.id.time, R.drawable.live_text_ot);
                } else {
                    holder.setBackgroundRes(R.id.time, R.drawable.live_text_time);
                    holder.setText(R.id.time, StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) + "'");

                }


            } else {
                holder.setBackgroundRes(R.id.time, R.drawable.live_text_time);
            }
        }


        //1 重要信息   24px   #666666
        //2 一般信息   24px   #999999
        //3 辅助信息   20px   #999999
        //4 警示信息   24px   #ff0000

        //部分时间消息红色显示
        if ("1".equals(matchTextLiveBean.getFontStyle())) {

            holder.setTextColor(R.id.zuo_msg_text, context.getResources().getColor(R.color.live_text1));
            holder.setTextColor(R.id.msg_text, context.getResources().getColor(R.color.live_text1));

            ((TextView) holder.getView(R.id.zuo_msg_text)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            ((TextView) holder.getView(R.id.msg_text)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);


        } else if ("2".equals(matchTextLiveBean.getFontStyle())) {

            holder.setTextColor(R.id.zuo_msg_text, context.getResources().getColor(R.color.live_text2));
            holder.setTextColor(R.id.msg_text, context.getResources().getColor(R.color.live_text2));

            ((TextView) holder.getView(R.id.zuo_msg_text)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            ((TextView) holder.getView(R.id.msg_text)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);


        } else if ("3".equals(matchTextLiveBean.getFontStyle())) {
            holder.setTextColor(R.id.zuo_msg_text, context.getResources().getColor(R.color.live_text3));
            holder.setTextColor(R.id.msg_text, context.getResources().getColor(R.color.live_text3));

            ((TextView) holder.getView(R.id.zuo_msg_text)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
            ((TextView) holder.getView(R.id.msg_text)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);


        } else if ("4".equals(matchTextLiveBean.getFontStyle())) {

            holder.setTextColor(R.id.zuo_msg_text, context.getResources().getColor(R.color.live_text4));
            holder.setTextColor(R.id.msg_text, context.getResources().getColor(R.color.live_text4));

            ((TextView) holder.getView(R.id.zuo_msg_text)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            ((TextView) holder.getView(R.id.msg_text)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);

        }


    }
}
