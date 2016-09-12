package com.hhly.mlottery.bean.footballDetails;

import java.util.List;

/**
 * description:
 * author: yixq
 * Created by A on 2016/9/9.
 */
public class FootballRankingList {

    private String group;
    private List<FootballRankingData> list;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<FootballRankingData> getList() {
        return list;
    }

    public void setList(List<FootballRankingData> list) {
        this.list = list;
    }
}
