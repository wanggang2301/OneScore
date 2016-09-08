package com.hhly.mlottery.bean.basket.basketdatabase;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */

public class IntegralResult {

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
    private IntegralClassify rankingObj;

    public IntegralClassify getRankingObj() {
        return rankingObj;
    }

    public void setRankingObj(IntegralClassify rankingObj) {
        this.rankingObj = rankingObj;
    }

    @RankingResult.Type
    public int getRankingType() {
        return rankingType;
    }

    public void setRankingType(@RankingResult.Type int rankingType) {
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
