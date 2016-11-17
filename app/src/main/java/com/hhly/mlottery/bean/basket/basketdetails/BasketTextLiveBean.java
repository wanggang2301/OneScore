package com.hhly.mlottery.bean.basket.basketdetails;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author: Wangg
 * @Name：BasketTextLiveBean
 * @Description:
 * @Created on:2016/11/15  17:52.
 */

public class BasketTextLiveBean implements Parcelable {
    private int result;

    private List<BasketEachTextLiveBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<BasketEachTextLiveBean> getData() {
        return data;
    }

    public void setData(List<BasketEachTextLiveBean> data) {
        this.data = data;
    }



    public BasketTextLiveBean() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.result);
        dest.writeTypedList(this.data);
    }

    protected BasketTextLiveBean(Parcel in) {
        this.result = in.readInt();
        this.data = in.createTypedArrayList(BasketEachTextLiveBean.CREATOR);
    }

    public static final Parcelable.Creator<BasketTextLiveBean> CREATOR = new Parcelable.Creator<BasketTextLiveBean>() {
        @Override
        public BasketTextLiveBean createFromParcel(Parcel source) {
            return new BasketTextLiveBean(source);
        }

        @Override
        public BasketTextLiveBean[] newArray(int size) {
            return new BasketTextLiveBean[size];
        }
    };
}
