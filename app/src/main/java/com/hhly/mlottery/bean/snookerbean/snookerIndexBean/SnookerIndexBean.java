package com.hhly.mlottery.bean.snookerbean.snookerIndexBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：斯诺克指数详细数据
 * 作    者：mady@13322.com
 * 时    间：2017/3/20
 */
public class SnookerIndexBean {


    /**
     * currDate : 2017-02-27
     * company : [{"comId":"3","comName":"SB"},{"comId":"31","comName":"SBO"},{"comId":"38","comName":"IBC"},{"comId":"44","comName":"ISN"},{"comId":"45","comName":"VinBet"}]
     * code : 200
            * allInfo : [{"leagueName":"Championship League Group 7","leagueId":"320977","leagueColor":null,"hot":false,"matchInfo":{"matchId":"848814045","matchHomeName":"约翰·希金斯","matchGuestName":"迈克尔·霍尔特","matchResult":null,"openTime":" 20:00","matchState":null},"comList":[{"comId":"31","comName":"SBO","currLevel":{"left":"1.23","middle":"1.5","right":"0.65"},"preLevel":{"left":"1.23","middle":"1.5","right":"0.65"}}]},{"leagueName":"Championship League Group 7","leagueId":"320977","leagueColor":null,"hot":false,"matchInfo":{"matchId":"848814046","matchHomeName":"迈克尔·霍尔特","matchGuestName":"格雷姆·多特","matchResult":null,"openTime":" 23:00","matchState":null},"comList":[{"comId":"31","comName":"SBO","currLevel":{"left":"1.63","middle":"1.5","right":"0.45"},"preLevel":{"left":"1.63","middle":"1.5","right":"0.45"}}]}]
            */

    private String currDate;
    private int code;
    /**
     * comId : 3
     * comName : SB
     */

    private List<CompanyEntity> company;
    /**
     * leagueName : Championship League Group 7
     * leagueId : 320977
     * leagueColor : null
     * hot : false
     * matchInfo : {"matchId":"848814045","matchHomeName":"约翰·希金斯","matchGuestName":"迈克尔·霍尔特","matchResult":null,"openTime":" 20:00","matchState":null}
     * comList : [{"comId":"31","comName":"SBO","currLevel":{"left":"1.23","middle":"1.5","right":"0.65"},"preLevel":{"left":"1.23","middle":"1.5","right":"0.65"}}]
     */

    private List<AllInfoEntity> allInfo;

    public String getCurrDate() {
        return currDate;
    }

    public void setCurrDate(String currDate) {
        this.currDate = currDate;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<CompanyEntity> getCompany() {
        return company;
    }

    public void setCompany(List<CompanyEntity> company) {
        this.company = company;
    }

    public List<AllInfoEntity> getAllInfo() {
        return allInfo;
    }

    public void setAllInfo(List<AllInfoEntity> allInfo) {
        this.allInfo = allInfo;
    }

    public static class CompanyEntity implements Parcelable {
        private String comId;
        private String comName;
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
        public CompanyEntity(){

        }

        protected CompanyEntity(Parcel in) {
            comId = in.readString();
            comName = in.readString();
            this.isChecked = in.readByte() != 0;
        }

        public static final Creator<CompanyEntity> CREATOR = new Creator<CompanyEntity>() {
            @Override
            public CompanyEntity createFromParcel(Parcel in) {
                return new CompanyEntity(in);
            }

            @Override
            public CompanyEntity[] newArray(int size) {
                return new CompanyEntity[size];
            }
        };

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
            dest.writeString(comId);
            dest.writeString(comName);
            dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        }
    }

    public static class AllInfoEntity implements Cloneable {
        private String leagueName;
        private String leagueId;
        private String leagueColor;
        private boolean hot;
        /**
         * matchId : 848814045
         * matchHomeName : 约翰·希金斯
         * matchGuestName : 迈克尔·霍尔特
         * matchResult : null
         * openTime :  20:00
         * matchState : null
         */

        public MatchInfoEntity matchInfo;
        /**
         * comId : 31
         * comName : SBO
         * currLevel : {"left":"1.23","middle":"1.5","right":"0.65"}
         * preLevel : {"left":"1.23","middle":"1.5","right":"0.65"}
         */

        public List<ComListEntity> comList;

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public String getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(String leagueId) {
            this.leagueId = leagueId;
        }

        public String getLeagueColor() {
            return leagueColor;
        }

        public void setLeagueColor(String leagueColor) {
            this.leagueColor = leagueColor;
        }

        public boolean isHot() {
            return hot;
        }

        public void setHot(boolean hot) {
            this.hot = hot;
        }

        public MatchInfoEntity getMatchInfo() {
            return matchInfo;
        }

        public void setMatchInfo(MatchInfoEntity matchInfo) {
            this.matchInfo = matchInfo;
        }

        public List<ComListEntity> getComList() {
            return comList;
        }

        public void setComList(List<ComListEntity> comList) {
            this.comList = comList;
        }

        @Override
        public AllInfoEntity clone() {
            try {
                AllInfoEntity allInfoBean = (AllInfoEntity) super.clone();
                List<ComListEntity> list = new ArrayList<>();
                List<ComListEntity> comList = allInfoBean.getComList();
                for (ComListEntity comListBean : comList) {
                    list.add(comListBean.clone());
                }
                allInfoBean.setComList(list);
                return allInfoBean;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 比赛信息
         */
        public static class MatchInfoEntity {
            private String matchId;
            private String matchHomeName;
            private String matchGuestName;
            private String matchResult;
            private String openTime;
            private String matchState;

            public String getMatchId() {
                return matchId;
            }

            public void setMatchId(String matchId) {
                this.matchId = matchId;
            }

            public String getMatchHomeName() {
                return matchHomeName;
            }

            public void setMatchHomeName(String matchHomeName) {
                this.matchHomeName = matchHomeName;
            }

            public String getMatchGuestName() {
                return matchGuestName;
            }

            public void setMatchGuestName(String matchGuestName) {
                this.matchGuestName = matchGuestName;
            }

            public String getMatchResult() {
                return matchResult;
            }

            public void setMatchResult(String matchResult) {
                this.matchResult = matchResult;
            }

            public String getOpenTime() {
                return openTime;
            }

            public void setOpenTime(String openTime) {
                this.openTime = openTime;
            }

            public String getMatchState() {
                return matchState;
            }

            public void setMatchState(String matchState) {
                this.matchState = matchState;
            }
        }

        public static class ComListEntity implements Cloneable{
            private String comId;
            private String comName;

            @Override
            public ComListEntity clone() {
                try {
                    return (ComListEntity) super.clone();
                } catch (CloneNotSupportedException e) {
                    return null;
                }
            }

            /**
             * 属于并且可显示
             *
             * @param company 公司
             * @return 属于可显示的公司
             */
            public boolean belongToShow(CompanyEntity company) {
                return this.comId.equals(company.comId) && company.isChecked;
            }

            public boolean belongToShow(List<CompanyEntity> companies) {
                // 遍历
                for (CompanyEntity company : companies) {
                    // 属于其中一家公司即可
                    if (this.belongToShow(company)) return true;
                }
                // 都不属于则不属于
                return false;
            }
            /**
             * left : 1.23
             * middle : 1.5
             * right : 0.65
             */


            private LevelEntity currLevel;
            /**
             * left : 1.23
             * middle : 1.5
             * right : 0.65
             */

            private LevelEntity preLevel;

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

            public LevelEntity getCurrLevel() {
                return currLevel;
            }

            public void setCurrLevel(LevelEntity currLevel) {
                this.currLevel = currLevel;
            }

            public LevelEntity getPreLevel() {
                return preLevel;
            }

            public void setPreLevel(LevelEntity preLevel) {
                this.preLevel = preLevel;
            }

            public static class LevelEntity {
                private String left;
                private String middle;
                private String right;
                private int leftStatus;
                private int rightStatus;
                private int middleStatus;

                public int getLeftStatus() {
                    return leftStatus;
                }

                public void setLeftStatus(int leftStatus) {
                    this.leftStatus = leftStatus;
                }

                public int getRightStatus() {
                    return rightStatus;
                }

                public void setRightStatus(int rightStatus) {
                    this.rightStatus = rightStatus;
                }

                public int getMiddleStatus() {
                    return middleStatus;
                }

                public void setMiddleStatus(int middleStatus) {
                    this.middleStatus = middleStatus;
                }

                public String getLeft() {
                    return left;
                }

                public void setLeft(String left) {
                    this.left = left;
                }

                public String getMiddle() {
                    return middle;
                }

                public void setMiddle(String middle) {
                    this.middle = middle;
                }

                public String getRight() {
                    return right;
                }

                public void setRight(String right) {
                    this.right = right;
                }
            }

        }
    }
}
