package com.hhly.mlottery.bean.multiplebean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yixq on 2017/1/9.
 * mail：yixq@13322.com
 * describe: 多屏动画列表传值类
 */

public class MultipleByValueBean implements Parcelable {

    //区分足篮球 [0:篮球 ， 1:足球]
    private int type;

    public String thirdId;//比赛id

    public MultipleByValueBean(int type, String thirdId) {
        this.type = type;
        this.thirdId = thirdId;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.thirdId);
    }

    public MultipleByValueBean() {
    }

    protected MultipleByValueBean(Parcel in) {
        this.type = in.readInt();
        this.thirdId = in.readString();
    }

    public static final Parcelable.Creator<MultipleByValueBean> CREATOR = new Parcelable.Creator<MultipleByValueBean>() {
        @Override
        public MultipleByValueBean createFromParcel(Parcel source) {
            return new MultipleByValueBean(source);
        }

        @Override
        public MultipleByValueBean[] newArray(int size) {
            return new MultipleByValueBean[size];
        }
    };
}
