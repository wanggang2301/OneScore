package com.hhly.mlottery.bean.basket.basketdatabase;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */

public class MatchStage implements Parcelable {

    /**
     * stageId : 154
     * stageName : 常规赛
     * stages : [{"secondStageId":"265","secondStageName":"2016-04"},{"secondStageId":"289","secondStageName":"2016-03"}]
     */

    private String stageId;
    private String stageName;
    private boolean hasSecondStage;
    private boolean selected;
    private List<MatchStage> stages;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public boolean isHasSecondStage() {
        return hasSecondStage;
    }

    public void setHasSecondStage(boolean hasSecondStage) {
        this.hasSecondStage = hasSecondStage;
    }

    public List<MatchStage> getStages() {
        return stages;
    }

    public void setStages(List<MatchStage> stages) {
        this.stages = stages;
    }

    public MatchStage() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.stageId);
        dest.writeString(this.stageName);
        dest.writeByte(this.hasSecondStage ? (byte) 1 : (byte) 0);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.stages);
    }

    protected MatchStage(Parcel in) {
        this.stageId = in.readString();
        this.stageName = in.readString();
        this.hasSecondStage = in.readByte() != 0;
        this.selected = in.readByte() != 0;
        this.stages = in.createTypedArrayList(MatchStage.CREATOR);
    }

    public static final Creator<MatchStage> CREATOR = new Creator<MatchStage>() {
        @Override
        public MatchStage createFromParcel(Parcel source) {
            return new MatchStage(source);
        }

        @Override
        public MatchStage[] newArray(int size) {
            return new MatchStage[size];
        }
    };
}
