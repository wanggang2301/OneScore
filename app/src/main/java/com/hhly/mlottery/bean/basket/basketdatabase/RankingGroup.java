package com.hhly.mlottery.bean.basket.basketdatabase;

import java.util.List;

/**
 * 描    述：排行小组(或赛区)
 * 作    者：longs@13322.com
 * 时    间：2016/8/10
 */
public class RankingGroup {

    private String groupName;
    private List<RankingTeam> datas;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<RankingTeam> getDatas() {
        return datas;
    }

    public void setDatas(List<RankingTeam> datas) {
        this.datas = datas;
    }
}
