package com.hhly.mlottery.bean.basket.basketdetails;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by andy on 2016/4/12 15:40.
 * <p/>
 * 描述：篮球详情界面指数数据实体类
 */
public class BasketDetailOddsBean {



    private String thirdId;

    private List<CompanyOddsEntity> companyOdds;//

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public List<CompanyOddsEntity> getCompanyOdds() {
        return companyOdds;
    }

    public void setCompanyOdds(List<CompanyOddsEntity> companyOdds) {
        this.companyOdds = companyOdds;
    }

    public static class CompanyOddsEntity implements Parcelable {
        private String oddsId;     //赔率id
        private String company;  //公司名

//        public CompanyOddsEntity(String company) {
//            this.company = company;
//        }
        public CompanyOddsEntity(){

        }
        private List<OddsDataEntity> oddsData;

        protected CompanyOddsEntity(Parcel in) {
            oddsId = in.readString();
            company = in.readString();
        }

        public static final Creator<CompanyOddsEntity> CREATOR = new Creator<CompanyOddsEntity>() {
            @Override
            public CompanyOddsEntity createFromParcel(Parcel in) {
                return new CompanyOddsEntity(in);
            }

            @Override
            public CompanyOddsEntity[] newArray(int size) {
                return new CompanyOddsEntity[size];
            }
        };

        public String getOddsId() {
            return oddsId;
        }

        public void setOddsId(String oddsId) {
            this.oddsId = oddsId;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public List<OddsDataEntity> getOddsData() {
            return oddsData;
        }

        public void setOddsData(List<OddsDataEntity> oddsData) {
            this.oddsData = oddsData;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(oddsId);
            dest.writeString(company);
        }
    }
}
