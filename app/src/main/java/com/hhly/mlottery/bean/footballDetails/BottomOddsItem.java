package com.hhly.mlottery.bean.footballDetails;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author wang gang
 * @date 2016/6/8 11:24
 * @des ${TODO}
 */
public class BottomOddsItem implements Parcelable {

    private String time;
    private String score;

    private String homeOdd;

    private String hand;

    private String guestOdd;

    public void setTime(String time) {
        this.time = time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getHomeOdd() {
        return homeOdd;
    }

    public void setHomeOdd(String homeOdd) {
        this.homeOdd = homeOdd;
    }

    public String getHand() {
        return hand;
    }

    public void setHand(String hand) {
        this.hand = hand;
    }

    public String getGuestOdd() {
        return guestOdd;
    }

    public void setGuestOdd(String guestOdd) {
        this.guestOdd = guestOdd;
    }



    public String getTime() {
        return time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.time);
        dest.writeString(this.score);
        dest.writeString(this.homeOdd);
        dest.writeString(this.hand);
        dest.writeString(this.guestOdd);
    }

    public BottomOddsItem() {
    }

    protected BottomOddsItem(Parcel in) {
        this.time = in.readString();
        this.score = in.readString();
        this.homeOdd = in.readString();
        this.hand = in.readString();
        this.guestOdd = in.readString();
    }

    public static final Parcelable.Creator<BottomOddsItem> CREATOR = new Parcelable.Creator<BottomOddsItem>() {
        @Override
        public BottomOddsItem createFromParcel(Parcel source) {
            return new BottomOddsItem(source);
        }

        @Override
        public BottomOddsItem[] newArray(int size) {
            return new BottomOddsItem[size];
        }
    };
}
