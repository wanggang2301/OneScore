package com.hhly.mlottery.util;

import android.text.TextUtils;

import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;

import java.util.Comparator;

/**
 * Created by asus1 on 2016/1/14.
 */
public class FootballLiveTextComparator implements Comparator<MatchTextLiveBean> {
    @Override
    public int compare(MatchTextLiveBean o1, MatchTextLiveBean o2) {
        int compare = 0;
        if (!TextUtils.isEmpty(o1.getMsgId()) && !TextUtils.isEmpty(o2.getMsgId())) {
            compare = Integer.parseInt(o2.getMsgId()) - Integer.parseInt(o1.getMsgId());//先按msgid排序
        }
        return compare;

    }
}


