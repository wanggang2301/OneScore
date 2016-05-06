package com.hhly.mlottery.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.util.StadiumUtils;

import java.util.List;

/**
 * Created by asus1 on 1215/12/25.
 */
public class LiveTextAdapter extends BaseAdapter {

    private Context context;
    private List<MatchTextLiveBean> list;
    /**上半场*/
    private static final String FIRSTHALF="1";
    /**中场*/
    private static final String HALFTIME="2";
    /**下半场*/
    private static final String SECONDHALF="3";

    /**完场**/
    private static  final  String MATCHFINISH="-1";


    public LiveTextAdapter(Context context, List<MatchTextLiveBean> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    @Override
    public MatchTextLiveBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        HodView hod;
        if (convertView == null) {
            hod = new HodView();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_live_text, null);
            hod.time = (TextView) convertView.findViewById(R.id.time);
            hod.msg = (TextView) convertView.findViewById(R.id.msg_text);
            hod.zuo_msg = (TextView) convertView.findViewById(R.id.zuo_msg_text);
            hod.xian = (TextView) convertView.findViewById(R.id.xian);
            convertView.setTag(hod);
        } else {
            hod = (HodView) convertView.getTag();
        }

        MatchTextLiveBean matchTextLiveBean = list.get(position);


            hod.time.setText(StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) + "'");
            hod.xian.setVisibility(View.VISIBLE);

            boolean nostart = true;

            if (list.size() > 1 && position < list.size() - 1) {
                if (StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) - StadiumUtils.convertStringToInt(list.get(position + 1).getTime()) == 0) {
                    if (HALFTIME.equals(matchTextLiveBean.getState())) {//中场
                        if (HALFTIME.equals(list.get(position + 1).getState())) {
                            hod.time.setVisibility(View.INVISIBLE);
                        } else {
                            hod.time.setText(context.getResources().getString(R.string.paused_txt));
                            hod.time.setVisibility(View.VISIBLE);
                        }

                    } else {
                        hod.time.setVisibility(View.INVISIBLE);
                    }
                } else {

                    hod.time.setVisibility(View.VISIBLE);
                    //上半场
                    if (FIRSTHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 45) {
                        hod.time.setText("45+'");
                    } else if (HALFTIME.equals(matchTextLiveBean.getState())) {  //中场休息
                        if (HALFTIME.equals(list.get(position + 1).getState())) {
                            hod.time.setVisibility(View.INVISIBLE);
                        } else {
                            hod.time.setText(context.getResources().getString(R.string.paused_txt));
                            hod.time.setVisibility(View.VISIBLE);
                        }

                    } else if (StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 90) {
                        hod.time.setText("90+" + (StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) - 90) + "'".trim());
                    } else if (MATCHFINISH.equals(matchTextLiveBean.getState())) {
                       // hod.time.setText(context.getResources().getString(R.string.finished_txt));
                       //  hod.time.setVisibility(View.INVISIBLE);

                    }
                }
            } else {
                hod.time.setVisibility(View.VISIBLE);
                if (FIRSTHALF.equals(matchTextLiveBean.getState()) && StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 45) {
                    hod.time.setText("45+'");
                } else if (HALFTIME.equals(matchTextLiveBean.getState())) {
                    hod.time.setText(context.getResources().getString(R.string.paused_txt));

                } else if (StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 90) {
                    hod.time.setText("90+" + (StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) - 90) + "'".trim());

                } else if (0 == StadiumUtils.convertStringToInt(matchTextLiveBean.getTime())) {
                    hod.time.setText(context.getResources().getString(R.string.not_start_txt));
                    nostart = false;
                    hod.xian.setVisibility(View.INVISIBLE);
                }
            }

            int msgPlace = Integer.parseInt(matchTextLiveBean.getMsgPlace());
            //中间
            if (msgPlace == 0) {
                if ("99999999".equals(matchTextLiveBean.getTime())) {  //比赛结束
                    hod.zuo_msg.setText(matchTextLiveBean.getMsgText());
                    hod.msg.setText("");
                    hod.time.setText(context.getResources().getString(R.string.finished_txt));
                    hod.xian.setVisibility(View.INVISIBLE);
                    hod.zuo_msg.setPadding(0, 0, 0, -2);
                    hod.msg.setPadding(0, 0, 0, -2);
                    hod.time.setPadding(0, 0, 0, -2);

                } else {
                    hod.msg.setText("");
                    hod.xian.setVisibility(View.VISIBLE);
                    hod.zuo_msg.setPadding(0, 7, 0, 7);
                    hod.msg.setPadding(0, 7, 0, 7);

                }
                hod.zuo_msg.setText(matchTextLiveBean.getMsgText());
                hod.time.setBackgroundResource(R.drawable.live_text_finish);
            } else if (msgPlace == 1) {  //主队
                if (nostart) {
                    hod.zuo_msg.setPadding(0, 7, 0, 7);
                    hod.msg.setPadding(0, 7, 0, 7);
                } else {
                    hod.zuo_msg.setPadding(0, -2, 0, 0);
                    hod.msg.setPadding(0, -2, 0, 0);
                    //  hod.time.setPadding(0, -2, 0, 0);
                }
                hod.zuo_msg.setText(matchTextLiveBean.getMsgText().trim());
                hod.msg.setText("");

                if (StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 90) {
                    hod.time.setBackgroundResource(R.drawable.live_text_ot);

                } else {
                    hod.time.setBackgroundResource(R.drawable.live_text_time);
                }

            } else if (msgPlace == 2) {  //客队
                hod.zuo_msg.setPadding(0, 7, 0, 7);
                hod.msg.setPadding(0, 7, 0, 7);
                //  hod.time.setPadding(0, 7, 0, 7);
                hod.zuo_msg.setText("");
                hod.msg.setText(matchTextLiveBean.getMsgText().trim());
                if (StadiumUtils.convertStringToInt(matchTextLiveBean.getTime()) > 90) {
                    hod.time.setBackgroundResource(R.drawable.live_text_ot);
                } else {
                    hod.time.setBackgroundResource(R.drawable.live_text_time);
                }
            }


            //1 重要信息   24px   #666666
            //2 一般信息   24px   #999999
            //3 辅助信息   20px   #999999
            //4 警示信息   24px   #ff0000

            //部分时间消息红色显示
            if ("1".equals(matchTextLiveBean.getFontStyle())) {
                hod.zuo_msg.setTextColor(context.getResources().getColor(R.color.live_text1));
                hod.msg.setTextColor(context.getResources().getColor(R.color.live_text1));
                hod.zuo_msg.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                hod.msg.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);

            } else if ("2".equals(matchTextLiveBean.getFontStyle())) {
                hod.zuo_msg.setTextColor(context.getResources().getColor(R.color.live_text2));
                hod.msg.setTextColor(context.getResources().getColor(R.color.live_text2));
                hod.zuo_msg.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                hod.msg.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            } else if ("3".equals(matchTextLiveBean.getFontStyle())) {
                hod.zuo_msg.setTextColor(context.getResources().getColor(R.color.live_text3));
                hod.msg.setTextColor(context.getResources().getColor(R.color.live_text3));
                hod.zuo_msg.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                hod.msg.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);

            } else if ("4".equals(matchTextLiveBean.getFontStyle())) {
                hod.zuo_msg.setTextColor(context.getResources().getColor(R.color.live_text4));
                hod.msg.setTextColor(context.getResources().getColor(R.color.live_text4));
                hod.zuo_msg.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                hod.msg.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            }
        return convertView;
    }


    private class HodView {
        TextView time, msg, zuo_msg;
        TextView xian;
    }
}
