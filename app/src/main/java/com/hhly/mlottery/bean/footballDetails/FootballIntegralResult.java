package com.hhly.mlottery.bean.footballDetails;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * description:
 * author: yixq
 * Created by A on 2016/9/9.
 */

public class FootballIntegralResult {

    public static final int SINGLE_LEAGUE = 0;
    public static final int MULTI_PART_LEAGUE = 1;
    public static final int CUP = 2;

    @IntDef({SINGLE_LEAGUE, MULTI_PART_LEAGUE, CUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }
    private int code; // 200,
    private int rankingType;
    private String[] searchCondition;
    private FootballIntegralClassify rankingObj;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    @Type
    public int getRankingType() {
        return rankingType;
    }

    public void setRankingType(@Type int rankingType) {
        this.rankingType = rankingType;
    }

    public String[] getSearchCondition() {
        return searchCondition;
    }

    public void setSearchCondition(String[] searchCondition) {
        this.searchCondition = searchCondition;
    }

    public FootballIntegralClassify getRankingObj() {
        return rankingObj;
    }

    public void setRankingObj(FootballIntegralClassify rankingObj) {
        this.rankingObj = rankingObj;
    }
}
