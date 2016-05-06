package com.hhly.mlottery.bean.footballDetails;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus1 on 2015/12/30.
 * 直播时间轴数据
 */
public class MatchTimeLiveBean implements Parcelable {

    private  String time;

    private  String code;

    private  String isHome;
    /**从文字直播中存进来的Enum 用来取消对应事件*/
    private String msgId;
    /**赛事状态*/
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public MatchTimeLiveBean(String time, String code, String isHome,String msgId,String state) {
        this.time = time;
        this.code = code;
        this.isHome = isHome;
        this.msgId=msgId;
        this.state=state;
    }




    public String getIsHome() {
        return isHome;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String isHome() {
        return isHome;
    }

    public void setIsHome(String isHome) {
        this.isHome = isHome;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }


    public static final Creator<MatchTimeLiveBean> CREATOR = new Creator<MatchTimeLiveBean>() {
        @Override
        public MatchTimeLiveBean createFromParcel(Parcel in) {
            return new MatchTimeLiveBean(in);
        }

        @Override
        public MatchTimeLiveBean[] newArray(int size) {
            return new MatchTimeLiveBean[size];
        }
    };
    public MatchTimeLiveBean(){

    }
    public MatchTimeLiveBean(Parcel in){
        time = in.readString();
        code = in.readString();
        isHome = in.readString();
        msgId=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(code);
        dest.writeString(isHome);
        dest.writeString(msgId);
    }


}
