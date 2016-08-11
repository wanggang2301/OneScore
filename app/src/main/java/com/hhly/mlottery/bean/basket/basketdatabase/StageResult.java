package com.hhly.mlottery.bean.basket.basketdatabase;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 描    述：传递数据所需的赛程信息类
 * 作    者：longs@13322.com
 * 时    间：2016/8/11
 */
public class StageResult implements Parcelable {

    private int firstStageId;
    private int secondStageId;
    private List<MatchStage> searchCondition;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.firstStageId);
        dest.writeInt(this.secondStageId);
        dest.writeTypedList(this.searchCondition);
    }

    public StageResult() {
    }

    protected StageResult(Parcel in) {
        this.firstStageId = in.readInt();
        this.secondStageId = in.readInt();
        this.searchCondition = in.createTypedArrayList(MatchStage.CREATOR);
    }

    public static final Parcelable.Creator<StageResult> CREATOR = new Parcelable.Creator<StageResult>() {
        @Override
        public StageResult createFromParcel(Parcel source) {
            return new StageResult(source);
        }

        @Override
        public StageResult[] newArray(int size) {
            return new StageResult[size];
        }
    };
}
