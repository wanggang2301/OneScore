package com.hhly.mlottery.util;

import com.hhly.mlottery.bean.footballDetails.MatchTimeLiveBean;

import java.util.Comparator;

/**
 * Created by andy on 2016/1/7.
 * 足球时间的排序
 * 不管时间。让进球时间放在集合最后
 */
public class FootballEventComparator implements Comparator<MatchTimeLiveBean> {

    private static final String SCORE="1029";//主队进球
    private static final String SCORE1="2053";//客队进球

    @Override
    public int compare(MatchTimeLiveBean o1, MatchTimeLiveBean o2) {
        int compare;
        compare= Integer.parseInt(o1.getTime())- Integer.parseInt(o2.getTime());//先按照时间排序
//        if(compare==0){
            if(o1.getCode().equals(SCORE)||o1.getCode().equals(SCORE1)){
                compare=1;//如果第一个事件是进球。则让其到后面
            }
           else if(o2.getCode().equals(SCORE)||o2.getCode().equals(SCORE1)){
                compare=-1;//如果第二个事件是进球，则正常放
            }
        if((o1.getCode().equals(SCORE)||o1.getCode().equals(SCORE1))&&(o2.getCode().equals(SCORE)||o2.getCode().equals(SCORE1))){
            compare= Integer.parseInt(o1.getTime())- Integer.parseInt(o2.getTime());
        }
//            else{
//                compare=1;//没有进球时间。则正常顺序
//            }
//        }

        return compare;
    }
}
