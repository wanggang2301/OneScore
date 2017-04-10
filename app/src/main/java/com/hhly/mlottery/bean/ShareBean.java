package com.hhly.mlottery.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author wang gang
 * @date 2016/6/15 15:30
 * @des ${}
 */
public class ShareBean implements Parcelable {
    private String title;

    private String summary;

    private String target_url;

    private String image_url;

    private String copy;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTarget_url() {
        return target_url;
    }

    public void setTarget_url(String target_url) {
        this.target_url = target_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCopy() {
        return copy;
    }

    public void setCopy(String copy) {
        this.copy = copy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.summary);
        dest.writeString(this.target_url);
        dest.writeString(this.image_url);
        dest.writeString(this.copy);
    }

    public ShareBean() {
    }

    protected ShareBean(Parcel in) {
        this.title = in.readString();
        this.summary = in.readString();
        this.target_url = in.readString();
        this.image_url = in.readString();
        this.copy = in.readString();
    }

    public static final Parcelable.Creator<ShareBean> CREATOR = new Parcelable.Creator<ShareBean>() {
        @Override
        public ShareBean createFromParcel(Parcel source) {
            return new ShareBean(source);
        }

        @Override
        public ShareBean[] newArray(int size) {
            return new ShareBean[size];
        }
    };
}
