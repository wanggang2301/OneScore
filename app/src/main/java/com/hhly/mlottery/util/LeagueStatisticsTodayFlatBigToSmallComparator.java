package com.hhly.mlottery.util;

import com.hhly.mlottery.bean.LeagueStatisticsTodayChildBean;

import java.util.Comparator;

/**
 * 描述:  ${}
 * 作者:  wangg@13322.com
 * 时间:  2016/9/7 10:46
 */
public class LeagueStatisticsTodayFlatBigToSmallComparator implements Comparator<LeagueStatisticsTodayChildBean> {

    @Override
    public int compare(LeagueStatisticsTodayChildBean o1, LeagueStatisticsTodayChildBean o2) {

        int compare = 0;
        compare = Integer.parseInt(o2.getDrawPercent()) - Integer.parseInt(o1.getDrawPercent());//先按msgid排序
        if (compare == 0) {
            compare = Integer.parseInt(o1.getNum()) - Integer.parseInt(o2.getNum());//大到小
            return compare;
        }
        return compare;
    }
}
