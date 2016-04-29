package com.hhly.mlottery.bean.footballsecond;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus1 on 2015/12/30.
 * 球员信息
 */
public class PlayerInfo implements Parcelable {

    private String name;

    public PlayerInfo(){

    }

    public PlayerInfo(String name){
        this.name = name;
    }

    protected PlayerInfo(Parcel in) {
        name = in.readString();
    }

    public static final Creator<PlayerInfo> CREATOR = new Creator<PlayerInfo>() {
        @Override
        public PlayerInfo createFromParcel(Parcel in) {
            return new PlayerInfo(in);
        }

        @Override
        public PlayerInfo[] newArray(int size) {
            return new PlayerInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
