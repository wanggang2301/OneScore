package com.hhly.mlottery.bean.footballDetails.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 描述:  ${}
 * 作者:  wangg@13322.com
 * 时间:  2016/9/2 17:14
 */
public class NationBean implements Parcelable {

    private String id;
    private String name;
    private String pic;
    private boolean isShow = false;

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    private List<DataBaseBean> leagueMenues;

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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<DataBaseBean> getLeagueMenues() {
        return leagueMenues;
    }

    public void setLeagueMenues(List<DataBaseBean> leagueMenues) {
        this.leagueMenues = leagueMenues;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.pic);
        dest.writeByte(this.isShow ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.leagueMenues);
    }

    public NationBean() {
    }

    protected NationBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.pic = in.readString();
        this.isShow = in.readByte() != 0;
        this.leagueMenues = in.createTypedArrayList(DataBaseBean.CREATOR);
    }

    public static final Parcelable.Creator<NationBean> CREATOR = new Parcelable.Creator<NationBean>() {
        @Override
        public NationBean createFromParcel(Parcel source) {
            return new NationBean(source);
        }

        @Override
        public NationBean[] newArray(int size) {
            return new NationBean[size];
        }
    };
}
