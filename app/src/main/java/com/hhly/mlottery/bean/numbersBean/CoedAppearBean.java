package com.hhly.mlottery.bean.numbersBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * desc:
 * Created by 107_tangrr on 2017/1/12 0012.
 */

public class CoedAppearBean implements Parcelable{

    private int coedAppear;
    private String key;

    public CoedAppearBean(Parcel in) {
        coedAppear = in.readInt();
        key = in.readString();
    }

    public static final Creator<CoedAppearBean> CREATOR = new Creator<CoedAppearBean>() {
        @Override
        public CoedAppearBean createFromParcel(Parcel in) {
            return new CoedAppearBean(in);
        }

        @Override
        public CoedAppearBean[] newArray(int size) {
            return new CoedAppearBean[size];
        }
    };

    public int getCoedAppear() {
        return coedAppear;
    }

    public void setCoedAppear(int coedAppear) {
        this.coedAppear = coedAppear;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(coedAppear);
        parcel.writeString(key);
    }
}
