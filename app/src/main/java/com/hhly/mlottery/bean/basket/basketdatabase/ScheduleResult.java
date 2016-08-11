package com.hhly.mlottery.bean.basket.basketdatabase;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 描    述：赛程接口结果
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */
public class ScheduleResult implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.firstStageIndex);
        dest.writeInt(this.secondStageIndex);
        dest.writeInt(this.matchType);
        dest.writeTypedList(this.searchCondition);
        dest.writeTypedList(this.matchData);
    }

    public ScheduleResult() {
    }

    protected ScheduleResult(Parcel in) {
        this.firstStageIndex = in.readInt();
        this.secondStageIndex = in.readInt();
        this.matchType = in.readInt();
        this.searchCondition = in.createTypedArrayList(MatchStage.CREATOR);
        this.matchData = in.createTypedArrayList(MatchDay.CREATOR);
    }

    public static final Parcelable.Creator<ScheduleResult> CREATOR = new Parcelable.Creator<ScheduleResult>() {
        @Override
        public ScheduleResult createFromParcel(Parcel source) {
            return new ScheduleResult(source);
        }

        @Override
        public ScheduleResult[] newArray(int size) {
            return new ScheduleResult[size];
        }
    };

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
