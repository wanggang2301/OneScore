package com.hhly.mlottery.bean.basket.index;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author: Wangg
 * @Name：BasketIndex
 * @Description:篮球指数
 * @Created on:2017/3/20  15:08.
 */

public class BasketIndexBean {


    private int result;
    private DataBean data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String currDate;
        private String filerDate;

        public String getFilerDate() {
            return filerDate;
        }

        public void setFilerDate(String filerDate) {
            this.filerDate = filerDate;
        }

        private List<String> focusLeagueIds;
        private List<FileterTagsBean> fileterTags;
        private List<CompanyBean> company;
        private List<AllInfoBean> allInfo;

        public String getCurrDate() {
            return currDate;
        }

        public void setCurrDate(String currDate) {
            this.currDate = currDate;
        }

        public List<String> getFocusLeagueIds() {
            return focusLeagueIds;
        }

        public void setFocusLeagueIds(List<String> focusLeagueIds) {
            this.focusLeagueIds = focusLeagueIds;
        }

        public List<FileterTagsBean> getFileterTags() {
            return fileterTags;
        }

        public void setFileterTags(List<FileterTagsBean> fileterTags) {
            this.fileterTags = fileterTags;
        }

        public List<CompanyBean> getCompany() {
            return company;
        }

        public void setCompany(List<CompanyBean> company) {
            this.company = company;
        }

        public List<AllInfoBean> getAllInfo() {
            return allInfo;
        }

        public void setAllInfo(List<AllInfoBean> allInfo) {
            this.allInfo = allInfo;
        }

        public static class FileterTagsBean implements Parcelable {

            private String leagueId;
            private boolean hot;
            private String leagueName;
            private String leagueLogoUrl;
            private int count;
            private List<String> thirdIds;

            public String getLeagueId() {
                return leagueId;
            }

            public void setLeagueId(String leagueId) {
                this.leagueId = leagueId;
            }

            public boolean isHot() {
                return hot;
            }

            public void setHot(boolean hot) {
                this.hot = hot;
            }

            public String getLeagueName() {
                return leagueName;
            }

            public void setLeagueName(String leagueName) {
                this.leagueName = leagueName;
            }

            public String getLeagueLogoUrl() {
                return leagueLogoUrl;
            }

            public void setLeagueLogoUrl(String leagueLogoUrl) {
                this.leagueLogoUrl = leagueLogoUrl;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public List<String> getThirdIds() {
                return thirdIds;
            }

            public void setThirdIds(List<String> thirdIds) {
                this.thirdIds = thirdIds;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.leagueId);
                dest.writeByte(this.hot ? (byte) 1 : (byte) 0);
                dest.writeString(this.leagueName);
                dest.writeString(this.leagueLogoUrl);
                dest.writeInt(this.count);
                dest.writeStringList(this.thirdIds);
            }

            public FileterTagsBean() {
            }

            protected FileterTagsBean(Parcel in) {
                this.leagueId = in.readString();
                this.hot = in.readByte() != 0;
                this.leagueName = in.readString();
                this.leagueLogoUrl = in.readString();
                this.count = in.readInt();
                this.thirdIds = in.createStringArrayList();
            }

            public static final Parcelable.Creator<FileterTagsBean> CREATOR = new Parcelable.Creator<FileterTagsBean>() {
                @Override
                public FileterTagsBean createFromParcel(Parcel source) {
                    return new FileterTagsBean(source);
                }

                @Override
                public FileterTagsBean[] newArray(int size) {
                    return new FileterTagsBean[size];
                }
            };
        }

        public static class CompanyBean implements Parcelable {


            private String comId;
            private String comName;
            private boolean isChecked;

            public CompanyBean(String comId, String comName, boolean isChecked) {
                this.comId = comId;
                this.comName = comName;
                this.isChecked = isChecked;
            }

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            public String getComId() {
                return comId;
            }

            public void setComId(String comId) {
                this.comId = comId;
            }

            public String getComName() {
                return comName;
            }

            public void setComName(String comName) {
                this.comName = comName;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.comId);
                dest.writeString(this.comName);
                dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
            }

            public CompanyBean() {
            }

            protected CompanyBean(Parcel in) {
                this.comId = in.readString();
                this.comName = in.readString();
                this.isChecked = in.readByte() != 0;
            }

            public static final Parcelable.Creator<CompanyBean> CREATOR = new Parcelable.Creator<CompanyBean>() {
                @Override
                public CompanyBean createFromParcel(Parcel source) {
                    return new CompanyBean(source);
                }

                @Override
                public CompanyBean[] newArray(int size) {
                    return new CompanyBean[size];
                }
            };
        }

        public static class AllInfoBean {


            private String thirdId;
            private String homeTeam;
            private String guestTeam;
            private String leagueId;
            private String leagueName;
            private String leagueColor;
            private String date;
            private String time;
            private int matchStatus;
            private String remainTime;
            private boolean hot;
            private String matchResult;
            private String matchType;
            private int section;

            public int getSection() {
                return section;
            }

            public void setSection(int section) {
                this.section = section;
            }

            private List<MatchOddsBean> matchOdds;

            public String getRemainTime() {
                return remainTime;
            }

            public void setRemainTime(String remainTime) {
                this.remainTime = remainTime;
            }


            public String getThirdId() {
                return thirdId;
            }

            public void setThirdId(String thirdId) {
                this.thirdId = thirdId;
            }

            public String getHomeTeam() {
                return homeTeam;
            }

            public void setHomeTeam(String homeTeam) {
                this.homeTeam = homeTeam;
            }

            public String getGuestTeam() {
                return guestTeam;
            }

            public void setGuestTeam(String guestTeam) {
                this.guestTeam = guestTeam;
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

            public String getLeagueColor() {
                return leagueColor;
            }

            public void setLeagueColor(String leagueColor) {
                this.leagueColor = leagueColor;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getMatchStatus() {
                return matchStatus;
            }

            public void setMatchStatus(int matchStatus) {
                this.matchStatus = matchStatus;
            }


            public boolean isHot() {
                return hot;
            }

            public void setHot(boolean hot) {
                this.hot = hot;
            }

            public String getMatchResult() {
                return matchResult;
            }

            public void setMatchResult(String matchResult) {
                this.matchResult = matchResult;
            }

            public String getMatchType() {
                return matchType;
            }

            public void setMatchType(String matchType) {
                this.matchType = matchType;
            }

            public List<MatchOddsBean> getMatchOdds() {
                return matchOdds;
            }

            public void setMatchOdds(List<MatchOddsBean> matchOdds) {
                this.matchOdds = matchOdds;
            }

            public static class MatchOddsBean {


                private String oddsId;
                private String comName;
                private String comId;
                private List<OddsDataBean> oddsData;

                public String getOddsId() {
                    return oddsId;
                }

                public void setOddsId(String oddsId) {
                    this.oddsId = oddsId;
                }

                public String getComName() {
                    return comName;
                }

                public void setComName(String comName) {
                    this.comName = comName;
                }

                public String getComId() {
                    return comId;
                }

                public void setComId(String comId) {
                    this.comId = comId;
                }

                public List<OddsDataBean> getOddsData() {
                    return oddsData;
                }

                public void setOddsData(List<OddsDataBean> oddsData) {
                    this.oddsData = oddsData;
                }

                public static class OddsDataBean {


                    private Object updateTime;
                    private String leftOdds;
                    private int leftOddsTrend;
                    private String rightOdds;
                    private int rightOddsTrend;
                    private String handicapValue;
                    private int handicapValueTrend;

                    public Object getUpdateTime() {
                        return updateTime;
                    }

                    public void setUpdateTime(Object updateTime) {
                        this.updateTime = updateTime;
                    }

                    public String getLeftOdds() {
                        return leftOdds;
                    }

                    public void setLeftOdds(String leftOdds) {
                        this.leftOdds = leftOdds;
                    }

                    public int getLeftOddsTrend() {
                        return leftOddsTrend;
                    }

                    public void setLeftOddsTrend(int leftOddsTrend) {
                        this.leftOddsTrend = leftOddsTrend;
                    }

                    public String getRightOdds() {
                        return rightOdds;
                    }

                    public void setRightOdds(String rightOdds) {
                        this.rightOdds = rightOdds;
                    }

                    public int getRightOddsTrend() {
                        return rightOddsTrend;
                    }

                    public void setRightOddsTrend(int rightOddsTrend) {
                        this.rightOddsTrend = rightOddsTrend;
                    }

                    public String getHandicapValue() {
                        return handicapValue;
                    }

                    public void setHandicapValue(String handicapValue) {
                        this.handicapValue = handicapValue;
                    }

                    public int getHandicapValueTrend() {
                        return handicapValueTrend;
                    }

                    public void setHandicapValueTrend(int handicapValueTrend) {
                        this.handicapValueTrend = handicapValueTrend;
                    }
                }
            }
        }
    }
}
