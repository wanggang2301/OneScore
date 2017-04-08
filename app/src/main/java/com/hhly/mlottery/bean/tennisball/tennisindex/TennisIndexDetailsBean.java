package com.hhly.mlottery.bean.tennisball.tennisindex;

import java.util.List;

/**
 * 描    述：网球指数详情bean
 * 作    者：mady@13322.com
 * 时    间：2017/4/8
 */
public class TennisIndexDetailsBean {


    private int result;
    private DataEntity data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private List<ComListEntity> comList;
        private List<OddsDataEntity> oddsData;

        public List<ComListEntity> getComList() {
            return comList;
        }

        public void setComList(List<ComListEntity> comList) {
            this.comList = comList;
        }

        public List<OddsDataEntity> getOddsData() {
            return oddsData;
        }

        public void setOddsData(List<OddsDataEntity> oddsData) {
            this.oddsData = oddsData;
        }

        public static class ComListEntity {
            /**
             * comId : 1
             * comName : VinBet
             */

            private String comId;
            private String comName;

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
        }

        public static class OddsDataEntity {
            /**
             * left : 1.04
             * middle : 2.5
             * right : 0.8
             * oddDate : 2017-03-31
             * oddTime : 07:16
             * leftStatus : 0
             * rightStatus : 0
             * middleStatus : 0
             */

            private String left;
            private String middle;
            private String right;
            private String oddDate;
            private String oddTime;
            private int leftStatus;
            private int rightStatus;
            private int middleStatus;

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

            public String getOddDate() {
                return oddDate;
            }

            public void setOddDate(String oddDate) {
                this.oddDate = oddDate;
            }

            public String getOddTime() {
                return oddTime;
            }

            public void setOddTime(String oddTime) {
                this.oddTime = oddTime;
            }

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
        }
    }
}
