package com.hhly.mlottery.bean.oddsbean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 103TJL on 2016/3/8.
 *指数，博彩数据
 */
public class OddsDataInfo{

    /**
     * id : 10
     * abb : HB
     * name : 浩博
     * details : [{"time":null,"score":null,"homeOdd":2.01,"hand":2.5,"guestOdd":1.5},{"time":null,"score":null,"homeOdd":3.01,"hand":4.5,"guestOdd":1.5}]
     */

    private List<ListOddEntity> listOdd;

    public void setListOdd(List<ListOddEntity> listOdd) {
        this.listOdd = listOdd;
    }

    public List<ListOddEntity> getListOdd() {
        return listOdd;
    }



    public static class ListOddEntity implements Parcelable {
        private String id;
        private String abb;
        private String name;
        private boolean checked;
        /**
         * time : null
         * score : null
         * homeOdd : 2.01
         * hand : 2.5
         * guestOdd : 1.5
         */

        private List<DetailsEntity> details;

        public void setId(String id) {
            this.id = id;
        }

        public void setAbb(String abb) {
            this.abb = abb;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDetails(List<DetailsEntity> details) {
            this.details = details;
        }

        public String getId() {
            return id;
        }

        public String getAbb() {
            return abb;
        }

        public String getName() {
            return name;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public List<DetailsEntity> getDetails() {
            return details;
        }

        public static class DetailsEntity implements Parcelable {
            private String time;
            private String score;
            private double homeOdd;
            private double hand;
            private double guestOdd;

            public void setTime(String time) {
                this.time = time;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public void setHomeOdd(double homeOdd) {
                this.homeOdd = homeOdd;
            }

            public void setHand(double hand) {
                this.hand = hand;
            }

            public void setGuestOdd(double guestOdd) {
                this.guestOdd = guestOdd;
            }

            public String getTime() {
                return time;
            }

            public String getScore() {
                return score;
            }

            public double getHomeOdd() {
                return homeOdd;
            }

            public double getHand() {
                return hand;
            }

            public double getGuestOdd() {
                return guestOdd;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.time);
                dest.writeString(this.score);
                dest.writeDouble(this.homeOdd);
                dest.writeDouble(this.hand);
                dest.writeDouble(this.guestOdd);
            }

            public DetailsEntity() {
            }

            protected DetailsEntity(Parcel in) {
                this.time = in.readString();
                this.score = in.readString();
                this.homeOdd = in.readDouble();
                this.hand = in.readDouble();
                this.guestOdd = in.readDouble();
            }

            public static final Creator<DetailsEntity> CREATOR = new Creator<DetailsEntity>() {
                @Override
                public DetailsEntity createFromParcel(Parcel source) {
                    return new DetailsEntity(source);
                }

                @Override
                public DetailsEntity[] newArray(int size) {
                    return new DetailsEntity[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.abb);
            dest.writeString(this.name);
            dest.writeList(this.details);
        }

        public ListOddEntity() {
        }

        protected ListOddEntity(Parcel in) {
            this.id = in.readString();
            this.abb = in.readString();
            this.name = in.readString();
            this.details = new ArrayList<DetailsEntity>();
            in.readList(this.details, DetailsEntity.class.getClassLoader());
        }

        public static final Parcelable.Creator<ListOddEntity> CREATOR = new Parcelable.Creator<ListOddEntity>() {
            @Override
            public ListOddEntity createFromParcel(Parcel source) {
                return new ListOddEntity(source);
            }

            @Override
            public ListOddEntity[] newArray(int size) {
                return new ListOddEntity[size];
            }
        };
    }
}
