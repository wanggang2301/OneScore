package com.hhly.mlottery.bean.foreigninfomation;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wangg
 * @Name：ForeignInfomationBean
 * @Description:
 * @Created on:2016/9/20  16:58.
 */

public class ForeignInfomationBean implements Parcelable {

    private String result;
    private long datetime;


    private List<OverseasInformationListBean> overseasInformationList;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public List<OverseasInformationListBean> getOverseasInformationList() {
        return overseasInformationList;
    }

    public void setOverseasInformationList(List<OverseasInformationListBean> overseasInformationList) {
        this.overseasInformationList = overseasInformationList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.result);
        dest.writeLong(this.datetime);
        dest.writeList(this.overseasInformationList);
    }

    public ForeignInfomationBean() {
    }

    protected ForeignInfomationBean(Parcel in) {
        this.result = in.readString();
        this.datetime = in.readLong();
        this.overseasInformationList = new ArrayList<OverseasInformationListBean>();
        in.readList(this.overseasInformationList, OverseasInformationListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ForeignInfomationBean> CREATOR = new Parcelable.Creator<ForeignInfomationBean>() {
        @Override
        public ForeignInfomationBean createFromParcel(Parcel source) {
            return new ForeignInfomationBean(source);
        }

        @Override
        public ForeignInfomationBean[] newArray(int size) {
            return new ForeignInfomationBean[size];
        }
    };
}
