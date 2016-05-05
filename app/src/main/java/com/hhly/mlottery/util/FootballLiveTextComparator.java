package com.hhly.mlottery.util;

import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;

import java.util.Comparator;

/**
 * Created by asus1 on 2016/1/14.
 */
public class FootballLiveTextComparator implements Comparator<MatchTextLiveBean> {
    @Override
    public int compare(MatchTextLiveBean o1, MatchTextLiveBean o2) {

        int compare = 0;
        compare =  Integer.parseInt(o2.getMsgId())-Integer.parseInt(o1.getMsgId());//先按照时间排序
        return compare;
    }
}


