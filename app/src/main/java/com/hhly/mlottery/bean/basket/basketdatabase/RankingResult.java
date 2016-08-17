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
    public static final int CUP_MULTI_STAGE = 4;

    @IntDef({SINGLE_LEAGUE, MULTI_PART_LEAGUE, CUP, CUP_MULTI_STAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    private int rankingType;
    private Integer firstStageIndex;
    private Integer secondStageIndex;
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

    public Integer getFirstStageIndex() {
        return firstStageIndex;
    }

    public void setFirstStageIndex(Integer firstStageIndex) {
        this.firstStageIndex = firstStageIndex;
    }

    public Integer getSecondStageIndex() {
        return secondStageIndex;
    }

    public void setSecondStageIndex(Integer secondStageIndex) {
        this.secondStageIndex = secondStageIndex;
    }

    public List<MatchStage> getSearchCondition() {
        return searchCondition;
    }

    public void setSearchCondition(List<MatchStage> searchCondition) {
        this.searchCondition = searchCondition;
    }

    /**
     * 获取 StageResult
     *
     * @return
     */
    public StageResult getStageResult() {
        StageResult stageResult = new StageResult();
        stageResult.setFirstStageId(this.firstStageIndex);
        stageResult.setSecondStageId(this.secondStageIndex);
        stageResult.setSearchCondition(this.searchCondition);
        return stageResult;
    }
}
