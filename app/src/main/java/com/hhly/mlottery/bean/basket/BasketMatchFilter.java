package com.hhly.mlottery.bean.basket;

import java.io.Serializable;
import java.util.List;

/**
 * Created by A on 2016/1/11.
 */
public class BasketMatchFilter implements Serializable {



    private int count; //	3
    private boolean hot; //	false
    private String leagueId; //	134
    private String leagueName; //	波罗的海
    private List<String> thirdIds ;//= new ArrayList<String>(); //	Array

    private boolean isChecked;

    public String getLeagueLogoUrl() {
        return leagueLogoUrl;
    }

    public void setLeagueLogoUrl(String leagueLogoUrl) {
        this.leagueLogoUrl = leagueLogoUrl;
    }

    private String leagueLogoUrl; //	http://pic.13322.com/basketball/league/120_120/1.png

    public BasketMatchFilter(){
    }

//    public BasketMatchFilter(int count,boolean hot,String leagueId,String leagueName,List<String> thirdIds)
//    {
//        this.count = count;
//        this.hot = hot;
//        this.leagueId = leagueId;
//        this.leagueName = leagueName;
//        this.thirdIds = thirdIds;
//    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public List<String> getThirdIds() {
        return thirdIds;
    }

    public void setThirdIds(List<String> thirdIds) {
        this.thirdIds = thirdIds;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    /* @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(count);
        dest.writeByte((byte) (hot ? 1 : 0));
        dest.writeList(thirdIds);
        dest.writeString(leagueId);
        dest.writeString(leagueName);
    }
*/
    /*public static final Parcelable.Creator<BasketMatchFilter> CREATOR = new Creator<BasketMatchFilter>() {

        @Override
        public BasketMatchFilter[] newArray(int size) {
            return new BasketMatchFilter[size];
        }

        @Override
        public BasketMatchFilter createFromParcel(Parcel source) {
            BasketMatchFilter basketFilter = new BasketMatchFilter(source);
            return basketFilter;
        }
    };

    public BasketMatchFilter(Parcel in) {
        thirdIds = in.readArrayList(ArrayList.class.getClassLoader());
        leagueName = in.readString();
        count = in.readInt();
        leagueId = in.readString();
        hot = in.readByte() == 1 ? true : false;
    }*/
}
