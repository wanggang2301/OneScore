package com.hhly.mlottery.bean.footballDetails;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by asus1 on 2015/12/28.
 */
public class TeamInfo implements Parcelable {


    private String id;

    private String name;

    private String url;

    private String gas;

    private String rc;

    private String yc;

    private String corner;

    private String score;

    private String halfScore;

    private String danger;

    private String shot;

    private String aside;

    private String trapping;

    private String columnals;

    private String foul;

    private String freeHit;

    private String lineOut;

    private String attackCount;

    public String getAttackCount() {
        return attackCount;
    }

    public void setAttackCount(String attackCount) {
        this.attackCount = attackCount;
    }

    private List<PlayerInfo> lineup;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getYc() {
        return yc;
    }

    public void setYc(String yc) {
        this.yc = yc;
    }

    public String getCorner() {
        return corner;
    }

    public void setCorner(String corner) {
        this.corner = corner;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getHalfScore() {
        return halfScore;
    }

    public void setHalfScore(String halfScore) {
        this.halfScore = halfScore;
    }

    public String getDanger() {
        return danger;
    }

    public void setDanger(String danger) {
        this.danger = danger;
    }

    public String getShot() {
        return shot;
    }

    public void setShot(String shot) {
        this.shot = shot;
    }

    public String getAside() {
        return aside;
    }

    public void setAside(String aside) {
        this.aside = aside;
    }

    public String getTrapping() {
        return trapping;
    }

    public void setTrapping(String trapping) {
        this.trapping = trapping;
    }

    public String getColumnals() {
        return columnals;
    }

    public void setColumnals(String columnals) {
        this.columnals = columnals;
    }

    public String getFoul() {
        return foul;
    }

    public void setFoul(String foul) {
        this.foul = foul;
    }

    public String getFreeHit() {
        return freeHit;
    }

    public void setFreeHit(String freeHit) {
        this.freeHit = freeHit;
    }

    public String getLineOut() {
        return lineOut;
    }

    public void setLineOut(String lineOut) {
        this.lineOut = lineOut;
    }

    public List<PlayerInfo> getLineup() {
        return lineup;
    }

    public void setLineup(List<PlayerInfo> lineup) {
        this.lineup = lineup;
    }


    public static final Creator<TeamInfo> CREATOR = new Creator<TeamInfo>() {
        @Override
        public TeamInfo createFromParcel(Parcel in) {
            return new TeamInfo(in);
        }

        @Override
        public TeamInfo[] newArray(int size) {
            return new TeamInfo[size];
        }
    };

    public TeamInfo() {

    }

    public TeamInfo(Parcel in) {
        id = in.readString();
        name = in.readString();
        url = in.readString();
        gas = in.readString();
        rc = in.readString();
        yc = in.readString();
        corner = in.readString();
        score = in.readString();
        halfScore = in.readString();
        danger = in.readString();
        shot = in.readString();
        aside = in.readString();
        trapping = in.readString();
        columnals = in.readString();
        foul = in.readString();
        freeHit = in.readString();
        lineOut = in.readString();
        attackCount = in.readString();
        PlayerInfo[] lineupArray = (PlayerInfo[]) in.readParcelableArray(TeamInfo.class.getClassLoader());
        if (lineupArray == null) {
            lineup = null;
        } else {
            lineup = Arrays.asList(lineupArray);
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(gas);
        dest.writeString(rc);
        dest.writeString(yc);
        dest.writeString(corner);
        dest.writeString(score);
        dest.writeString(halfScore);
        dest.writeString(danger);
        dest.writeString(shot);
        dest.writeString(aside);
        dest.writeString(trapping);
        dest.writeString(columnals);
        dest.writeString(foul);
        dest.writeString(freeHit);
        dest.writeString(lineOut);
        dest.writeString(attackCount);
        if (lineup == null) {
            dest.writeParcelableArray(null, flags);
        } else {
            dest.writeParcelableArray(lineup.toArray(new PlayerInfo[lineup.size()]), flags);
        }

    }
}
