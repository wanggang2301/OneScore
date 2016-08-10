package com.hhly.mlottery.bean.basket.basketdatabase;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * 描    述：排行接口结果
 * 作    者：longs@13322.com
 * 时    间：2016/8/10
 */
public class RankingResult {

    public static final int SINGLE_LEAGUE = 1;
    public static final int MULTI_PART_LEAGUE = 2;
    public static final int CUP = 3;

    @IntDef({SINGLE_LEAGUE, MULTI_PART_LEAGUE, CUP})
    @Retention(RetentionPolicy.SOURCE)
    @interface Type {
    }

    private int rankingType;
    private int firstStageId;
    private int secondStageId;
    private List<MatchStage> searchCondition;
    private List<RankingGroup> rankingObj;

    public List<RankingGroup> getRankingObj() {
        return rankingObj;
    }

    public void setRankingObj(List<RankingGroup> rankingObj) {
        this.rankingObj = rankingObj;
    }

    @Type
    public int getRankingType() {
        return rankingType;
    }

    public void setRankingType(@Type int rankingType) {
        this.rankingType = rankingType;
    }

    public int getFirstStageId() {
        return firstStageId;
    }

    public void setFirstStageId(int firstStageId) {
        this.firstStageId = firstStageId;
    }

    public int getSecondStageId() {
        return secondStageId;
    }

    public void setSecondStageId(int secondStageId) {
        this.secondStageId = secondStageId;
    }

    public List<MatchStage> getSearchCondition() {
        return searchCondition;
    }

    public void setSearchCondition(List<MatchStage> searchCondition) {
        this.searchCondition = searchCondition;
    }
}
