package com.hhly.mlottery.bean.basket.basketdatabase;

import java.util.List;

/**
 * 描    述：赛程接口结果
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */
public class ScheduleResult {

    /**
     * firstStageIndex : 1
     * secondStageIndex : 0
     * matchType : 1
     */

    private int firstStageIndex;
    private int secondStageIndex;
    private int matchType;
    private List<MatchStage> searchCondition;
    private List<MatchDay> matchData;

    public List<MatchStage> getSearchCondition() {
        return searchCondition;
    }

    public void setSearchCondition(List<MatchStage> searchCondition) {
        this.searchCondition = searchCondition;
    }

    public List<MatchDay> getMatchData() {
        return matchData;
    }

    public void setMatchData(List<MatchDay> matchData) {
        this.matchData = matchData;
    }

    public int getFirstStageIndex() {
        return firstStageIndex;
    }

    public void setFirstStageIndex(int firstStageIndex) {
        this.firstStageIndex = firstStageIndex;
    }

    public int getSecondStageIndex() {
        return secondStageIndex;
    }

    public void setSecondStageIndex(int secondStageIndex) {
        this.secondStageIndex = secondStageIndex;
    }

    public int getMatchType() {
        return matchType;
    }

    public void setMatchType(int matchType) {
        this.matchType = matchType;
    }
}
