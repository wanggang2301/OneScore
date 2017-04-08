package com.hhly.mlottery.bean.footballDetails.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:  ${}
 * 作者:  wangg@13322.com
 * 时间:  2016/9/2 17:13
 */
public class LeagueDataBase implements Parcelable {
    private String code;
    private List<DataBaseBean> internation;
    private List<NationBean> nation;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataBaseBean> getInternation() {
        return internation;
    }

    public void setInternation(List<DataBaseBean> internation) {
        this.internation = internation;
    }

    public List<NationBean> getNation() {
        return nation;
    }

    public void setNation(List<NationBean> nation) {
        this.nation = nation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeTypedList(this.internation);
        dest.writeList(this.nation);
    }

    public LeagueDataBase() {
    }

    protected LeagueDataBase(Parcel in) {
        this.code = in.readString();
        this.internation = in.createTypedArrayList(DataBaseBean.CREATOR);
        this.nation = new ArrayList<NationBean>();
        in.readList(this.nation, NationBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<LeagueDataBase> CREATOR = new Parcelable.Creator<LeagueDataBase>() {
        @Override
        public LeagueDataBase createFromParcel(Parcel source) {
            return new LeagueDataBase(source);
        }

        @Override
        public LeagueDataBase[] newArray(int size) {
            return new LeagueDataBase[size];
        }
    };
}
