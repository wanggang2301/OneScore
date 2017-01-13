package com.hhly.mlottery.bean.numbersBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * desc:
 * Created by 107_tangrr on 2017/1/12 0012.
 */

public class LotteryInfoDateBean implements Parcelable {

    private int coedAppear;
    private int numberAppear;
    private int coedNotAppear;
    private String key;

    public LotteryInfoDateBean(Parcel in) {
        coedAppear = in.readInt();
        numberAppear = in.readInt();
        coedNotAppear = in.readInt();
        key = in.readString();
    }

    public LotteryInfoDateBean(){}

    public static final Creator<LotteryInfoDateBean> CREATOR = new Creator<LotteryInfoDateBean>() {
        @Override
        public LotteryInfoDateBean createFromParcel(Parcel in) {
            return new LotteryInfoDateBean(in);
        }

        @Override
        public LotteryInfoDateBean[] newArray(int size) {
            return new LotteryInfoDateBean[size];
        }
    };

    public int getCoedAppear() {
        return coedAppear;
    }

    public void setCoedAppear(int coedAppear) {
        this.coedAppear = coedAppear;
    }

    public int getNumberAppear() {
        return numberAppear;
    }

    public void setNumberAppear(int numberAppear) {
        this.numberAppear = numberAppear;
    }

    public int getCoedNotAppear() {
        return coedNotAppear;
    }

    public void setCoedNotAppear(int coedNotAppear) {
        this.coedNotAppear = coedNotAppear;
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
        parcel.writeInt(numberAppear);
        parcel.writeInt(coedNotAppear);
        parcel.writeString(key);
    }
}
