package com.hhly.mlottery.util;

import com.hhly.mlottery.bean.footballDetails.MatchTimeLiveBean;

import java.util.Comparator;

/**
 * 描述:  ${TODO}
 * 作者:  wangg@13322.com
 * 时间:  2016/8/11 18:41
 */
public class FootballEventLiveComparator implements Comparator<MatchTimeLiveBean> {

    @Override
    public int compare(MatchTimeLiveBean o1, MatchTimeLiveBean o2) {
        int compare = 0;
        compare = Integer.parseInt(o1.getTime()) - Integer.parseInt(o2.getTime());//先按照时间排序
        return compare;
    }
}
