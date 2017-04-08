package com.hhly.mlottery.bean.footballDetails.database;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 描述:  ${}
 * 作者:  wangg@13322.com
 * 时间:  2016/9/2 17:15
 */
public class DataBaseBean implements Parcelable {

    private String kind;       //联赛类型
    private String leagueId;    // 联赛id
    private String lgName;     //联赛名
    private String pic;      //联赛图标
    private String lgId;     //背景图片

    public DataBaseBean() {

    }

    public DataBaseBean(String kind, String leagueId, String pic, String lgName) {
        this.kind = kind;
        this.leagueId = leagueId;
        this.pic = pic;
        this.lgName = lgName;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getLgName() {
        return lgName;
    }

    public void setLgName(String lgName) {
        this.lgName = lgName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLgId() {
        return lgId;
    }

    public void setLgId(String lgId) {
        this.lgId = lgId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.kind);
        dest.writeString(this.leagueId);
        dest.writeString(this.lgName);
        dest.writeString(this.pic);
        dest.writeString(this.lgId);
    }

    protected DataBaseBean(Parcel in) {
        this.kind = in.readString();
        this.leagueId = in.readString();
        this.lgName = in.readString();
        this.pic = in.readString();
        this.lgId = in.readString();
    }

    public static final Parcelable.Creator<DataBaseBean> CREATOR = new Parcelable.Creator<DataBaseBean>() {
        @Override
        public DataBaseBean createFromParcel(Parcel source) {
            return new DataBaseBean(source);
        }

        @Override
        public DataBaseBean[] newArray(int size) {
            return new DataBaseBean[size];
        }
    };
}
