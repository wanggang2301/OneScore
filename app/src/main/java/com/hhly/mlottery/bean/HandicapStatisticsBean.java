package com.hhly.mlottery.bean;

/**
 * Created by yuely198 on 2017/6/21.
 */

public class HandicapStatisticsBean {


    /**
     * result : 200
     * trendPlate : {"totalPlate":{"upCount":"19","upWin":"8","upDraw":"1","upLose":"10","downCount":"71","downWin":"33","downDraw":"2","downLose":"36"},"homePlate":{"upCount":"17","upWin":"7","upDraw":"1","upLose":"9","downCount":"32","downWin":"16","downDraw":"2","downLose":"14"},"guestPlate":{"upCount":"2","upWin":"1","upDraw":"0","upLose":"1","downCount":"39","downWin":"17","downDraw":"0","downLose":"22"}}
     * sizePlate : {"totalSizePlate":{"count":"90","high":"49","draw":"1","low":"40","highPer":"54.44%","drawPer":"1.11%","lowPer":"44.44%"},"homeSizePlate":{"count":"49","high":"27","draw":"1","low":"21","highPer":"55.10%","drawPer":"2.04%","lowPer":"42.86%"},"guestSizePlate":{"count":"41","high":"22","draw":"0","low":"19","highPer":"53.66%","drawPer":"0.00%","lowPer":"46.34%"}}
     * letPlate : {"totalLetPlate":{"count":"90","over":"19","under":"71","win":"41","draw":"3","lose":"46","net":"-5","winPer":"45.56%","drawPer":"3.33%","losePer":"51.11%"},"homeLetPlate":{"count":"49","over":"17","under":"32","win":"23","draw":"3","lose":"23","net":"0","winPer":"46.94%","drawPer":"6.12%","losePer":"46.94%"},"guestLetPlate":{"count":"41","over":"2","under":"39","win":"18","draw":"0","lose":"23","net":"-5","winPer":"43.90%","drawPer":"0.00%","losePer":"56.10%"}}
     */

    private int result;
    private TrendPlateBean trendPlate;
    private SizePlateBean sizePlate;
    private LetPlateBean letPlate;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public TrendPlateBean getTrendPlate() {
        return trendPlate;
    }

    public void setTrendPlate(TrendPlateBean trendPlate) {
        this.trendPlate = trendPlate;
    }

    public SizePlateBean getSizePlate() {
        return sizePlate;
    }

    public void setSizePlate(SizePlateBean sizePlate) {
        this.sizePlate = sizePlate;
    }

    public LetPlateBean getLetPlate() {
        return letPlate;
    }

    public void setLetPlate(LetPlateBean letPlate) {
        this.letPlate = letPlate;
    }

    public static class TrendPlateBean {
        /**
         * totalPlate : {"upCount":"19","upWin":"8","upDraw":"1","upLose":"10","downCount":"71","downWin":"33","downDraw":"2","downLose":"36"}
         * homePlate : {"upCount":"17","upWin":"7","upDraw":"1","upLose":"9","downCount":"32","downWin":"16","downDraw":"2","downLose":"14"}
         * guestPlate : {"upCount":"2","upWin":"1","upDraw":"0","upLose":"1","downCount":"39","downWin":"17","downDraw":"0","downLose":"22"}
         */

        private TotalPlateBean totalPlate;
        private HomePlateBean homePlate;
        private GuestPlateBean guestPlate;

        public TotalPlateBean getTotalPlate() {
            return totalPlate;
        }

        public void setTotalPlate(TotalPlateBean totalPlate) {
            this.totalPlate = totalPlate;
        }

        public HomePlateBean getHomePlate() {
            return homePlate;
        }

        public void setHomePlate(HomePlateBean homePlate) {
            this.homePlate = homePlate;
        }

        public GuestPlateBean getGuestPlate() {
            return guestPlate;
        }

        public void setGuestPlate(GuestPlateBean guestPlate) {
            this.guestPlate = guestPlate;
        }

        public static class TotalPlateBean {
            /**
             * upCount : 19
             * upWin : 8
             * upDraw : 1
             * upLose : 10
             * downCount : 71
             * downWin : 33
             * downDraw : 2
             * downLose : 36
             */

            private String upCount;
            private String upWin;
            private String upDraw;
            private String upLose;
            private String downCount;
            private String downWin;
            private String downDraw;
            private String downLose;

            public String getUpCount() {
                return upCount;
            }

            public void setUpCount(String upCount) {
                this.upCount = upCount;
            }

            public String getUpWin() {
                return upWin;
            }

            public void setUpWin(String upWin) {
                this.upWin = upWin;
            }

            public String getUpDraw() {
                return upDraw;
            }

            public void setUpDraw(String upDraw) {
                this.upDraw = upDraw;
            }

            public String getUpLose() {
                return upLose;
            }

            public void setUpLose(String upLose) {
                this.upLose = upLose;
            }

            public String getDownCount() {
                return downCount;
            }

            public void setDownCount(String downCount) {
                this.downCount = downCount;
            }

            public String getDownWin() {
                return downWin;
            }

            public void setDownWin(String downWin) {
                this.downWin = downWin;
            }

            public String getDownDraw() {
                return downDraw;
            }

            public void setDownDraw(String downDraw) {
                this.downDraw = downDraw;
            }

            public String getDownLose() {
                return downLose;
            }

            public void setDownLose(String downLose) {
                this.downLose = downLose;
            }
        }

        public static class HomePlateBean {
            /**
             * upCount : 17
             * upWin : 7
             * upDraw : 1
             * upLose : 9
             * downCount : 32
             * downWin : 16
             * downDraw : 2
             * downLose : 14
             */

            private String upCount;
            private String upWin;
            private String upDraw;
            private String upLose;
            private String downCount;
            private String downWin;
            private String downDraw;
            private String downLose;

            public String getUpCount() {
                return upCount;
            }

            public void setUpCount(String upCount) {
                this.upCount = upCount;
            }

            public String getUpWin() {
                return upWin;
            }

            public void setUpWin(String upWin) {
                this.upWin = upWin;
            }

            public String getUpDraw() {
                return upDraw;
            }

            public void setUpDraw(String upDraw) {
                this.upDraw = upDraw;
            }

            public String getUpLose() {
                return upLose;
            }

            public void setUpLose(String upLose) {
                this.upLose = upLose;
            }

            public String getDownCount() {
                return downCount;
            }

            public void setDownCount(String downCount) {
                this.downCount = downCount;
            }

            public String getDownWin() {
                return downWin;
            }

            public void setDownWin(String downWin) {
                this.downWin = downWin;
            }

            public String getDownDraw() {
                return downDraw;
            }

            public void setDownDraw(String downDraw) {
                this.downDraw = downDraw;
            }

            public String getDownLose() {
                return downLose;
            }

            public void setDownLose(String downLose) {
                this.downLose = downLose;
            }
        }

        public static class GuestPlateBean {
            /**
             * upCount : 2
             * upWin : 1
             * upDraw : 0
             * upLose : 1
             * downCount : 39
             * downWin : 17
             * downDraw : 0
             * downLose : 22
             */

            private String upCount;
            private String upWin;
            private String upDraw;
            private String upLose;
            private String downCount;
            private String downWin;
            private String downDraw;
            private String downLose;

            public String getUpCount() {
                return upCount;
            }

            public void setUpCount(String upCount) {
                this.upCount = upCount;
            }

            public String getUpWin() {
                return upWin;
            }

            public void setUpWin(String upWin) {
                this.upWin = upWin;
            }

            public String getUpDraw() {
                return upDraw;
            }

            public void setUpDraw(String upDraw) {
                this.upDraw = upDraw;
            }

            public String getUpLose() {
                return upLose;
            }

            public void setUpLose(String upLose) {
                this.upLose = upLose;
            }

            public String getDownCount() {
                return downCount;
            }

            public void setDownCount(String downCount) {
                this.downCount = downCount;
            }

            public String getDownWin() {
                return downWin;
            }

            public void setDownWin(String downWin) {
                this.downWin = downWin;
            }

            public String getDownDraw() {
                return downDraw;
            }

            public void setDownDraw(String downDraw) {
                this.downDraw = downDraw;
            }

            public String getDownLose() {
                return downLose;
            }

            public void setDownLose(String downLose) {
                this.downLose = downLose;
            }
        }
    }

    public static class SizePlateBean {
        /**
         * totalSizePlate : {"count":"90","high":"49","draw":"1","low":"40","highPer":"54.44%","drawPer":"1.11%","lowPer":"44.44%"}
         * homeSizePlate : {"count":"49","high":"27","draw":"1","low":"21","highPer":"55.10%","drawPer":"2.04%","lowPer":"42.86%"}
         * guestSizePlate : {"count":"41","high":"22","draw":"0","low":"19","highPer":"53.66%","drawPer":"0.00%","lowPer":"46.34%"}
         */

        private TotalSizePlateBean totalSizePlate;
        private HomeSizePlateBean homeSizePlate;
        private GuestSizePlateBean guestSizePlate;

        public TotalSizePlateBean getTotalSizePlate() {
            return totalSizePlate;
        }

        public void setTotalSizePlate(TotalSizePlateBean totalSizePlate) {
            this.totalSizePlate = totalSizePlate;
        }

        public HomeSizePlateBean getHomeSizePlate() {
            return homeSizePlate;
        }

        public void setHomeSizePlate(HomeSizePlateBean homeSizePlate) {
            this.homeSizePlate = homeSizePlate;
        }

        public GuestSizePlateBean getGuestSizePlate() {
            return guestSizePlate;
        }

        public void setGuestSizePlate(GuestSizePlateBean guestSizePlate) {
            this.guestSizePlate = guestSizePlate;
        }

        public static class TotalSizePlateBean {
            /**
             * count : 90
             * high : 49
             * draw : 1
             * low : 40
             * highPer : 54.44%
             * drawPer : 1.11%
             * lowPer : 44.44%
             */

            private String count;
            private String high;
            private String draw;
            private String low;
            private String highPer;
            private String drawPer;
            private String lowPer;

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getDraw() {
                return draw;
            }

            public void setDraw(String draw) {
                this.draw = draw;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getHighPer() {
                return highPer;
            }

            public void setHighPer(String highPer) {
                this.highPer = highPer;
            }

            public String getDrawPer() {
                return drawPer;
            }

            public void setDrawPer(String drawPer) {
                this.drawPer = drawPer;
            }

            public String getLowPer() {
                return lowPer;
            }

            public void setLowPer(String lowPer) {
                this.lowPer = lowPer;
            }
        }

        public static class HomeSizePlateBean {
            /**
             * count : 49
             * high : 27
             * draw : 1
             * low : 21
             * highPer : 55.10%
             * drawPer : 2.04%
             * lowPer : 42.86%
             */

            private String count;
            private String high;
            private String draw;
            private String low;
            private String highPer;
            private String drawPer;
            private String lowPer;

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getDraw() {
                return draw;
            }

            public void setDraw(String draw) {
                this.draw = draw;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getHighPer() {
                return highPer;
            }

            public void setHighPer(String highPer) {
                this.highPer = highPer;
            }

            public String getDrawPer() {
                return drawPer;
            }

            public void setDrawPer(String drawPer) {
                this.drawPer = drawPer;
            }

            public String getLowPer() {
                return lowPer;
            }

            public void setLowPer(String lowPer) {
                this.lowPer = lowPer;
            }
        }

        public static class GuestSizePlateBean {
            /**
             * count : 41
             * high : 22
             * draw : 0
             * low : 19
             * highPer : 53.66%
             * drawPer : 0.00%
             * lowPer : 46.34%
             */

            private String count;
            private String high;
            private String draw;
            private String low;
            private String highPer;
            private String drawPer;
            private String lowPer;

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getDraw() {
                return draw;
            }

            public void setDraw(String draw) {
                this.draw = draw;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getHighPer() {
                return highPer;
            }

            public void setHighPer(String highPer) {
                this.highPer = highPer;
            }

            public String getDrawPer() {
                return drawPer;
            }

            public void setDrawPer(String drawPer) {
                this.drawPer = drawPer;
            }

            public String getLowPer() {
                return lowPer;
            }

            public void setLowPer(String lowPer) {
                this.lowPer = lowPer;
            }
        }
    }

    public static class LetPlateBean {
        /**
         * totalLetPlate : {"count":"90","over":"19","under":"71","win":"41","draw":"3","lose":"46","net":"-5","winPer":"45.56%","drawPer":"3.33%","losePer":"51.11%"}
         * homeLetPlate : {"count":"49","over":"17","under":"32","win":"23","draw":"3","lose":"23","net":"0","winPer":"46.94%","drawPer":"6.12%","losePer":"46.94%"}
         * guestLetPlate : {"count":"41","over":"2","under":"39","win":"18","draw":"0","lose":"23","net":"-5","winPer":"43.90%","drawPer":"0.00%","losePer":"56.10%"}
         */

        private TotalLetPlateBean totalLetPlate;
        private HomeLetPlateBean homeLetPlate;
        private GuestLetPlateBean guestLetPlate;

        public TotalLetPlateBean getTotalLetPlate() {
            return totalLetPlate;
        }

        public void setTotalLetPlate(TotalLetPlateBean totalLetPlate) {
            this.totalLetPlate = totalLetPlate;
        }

        public HomeLetPlateBean getHomeLetPlate() {
            return homeLetPlate;
        }

        public void setHomeLetPlate(HomeLetPlateBean homeLetPlate) {
            this.homeLetPlate = homeLetPlate;
        }

        public GuestLetPlateBean getGuestLetPlate() {
            return guestLetPlate;
        }

        public void setGuestLetPlate(GuestLetPlateBean guestLetPlate) {
            this.guestLetPlate = guestLetPlate;
        }

        public static class TotalLetPlateBean {
            /**
             * count : 90
             * over : 19
             * under : 71
             * win : 41
             * draw : 3
             * lose : 46
             * net : -5
             * winPer : 45.56%
             * drawPer : 3.33%
             * losePer : 51.11%
             */

            private String count;
            private String over;
            private String under;
            private String win;
            private String draw;
            private String lose;
            private String net;
            private String winPer;
            private String drawPer;
            private String losePer;

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getOver() {
                return over;
            }

            public void setOver(String over) {
                this.over = over;
            }

            public String getUnder() {
                return under;
            }

            public void setUnder(String under) {
                this.under = under;
            }

            public String getWin() {
                return win;
            }

            public void setWin(String win) {
                this.win = win;
            }

            public String getDraw() {
                return draw;
            }

            public void setDraw(String draw) {
                this.draw = draw;
            }

            public String getLose() {
                return lose;
            }

            public void setLose(String lose) {
                this.lose = lose;
            }

            public String getNet() {
                return net;
            }

            public void setNet(String net) {
                this.net = net;
            }

            public String getWinPer() {
                return winPer;
            }

            public void setWinPer(String winPer) {
                this.winPer = winPer;
            }

            public String getDrawPer() {
                return drawPer;
            }

            public void setDrawPer(String drawPer) {
                this.drawPer = drawPer;
            }

            public String getLosePer() {
                return losePer;
            }

            public void setLosePer(String losePer) {
                this.losePer = losePer;
            }
        }

        public static class HomeLetPlateBean {
            /**
             * count : 49
             * over : 17
             * under : 32
             * win : 23
             * draw : 3
             * lose : 23
             * net : 0
             * winPer : 46.94%
             * drawPer : 6.12%
             * losePer : 46.94%
             */

            private String count;
            private String over;
            private String under;
            private String win;
            private String draw;
            private String lose;
            private String net;
            private String winPer;
            private String drawPer;
            private String losePer;

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getOver() {
                return over;
            }

            public void setOver(String over) {
                this.over = over;
            }

            public String getUnder() {
                return under;
            }

            public void setUnder(String under) {
                this.under = under;
            }

            public String getWin() {
                return win;
            }

            public void setWin(String win) {
                this.win = win;
            }

            public String getDraw() {
                return draw;
            }

            public void setDraw(String draw) {
                this.draw = draw;
            }

            public String getLose() {
                return lose;
            }

            public void setLose(String lose) {
                this.lose = lose;
            }

            public String getNet() {
                return net;
            }

            public void setNet(String net) {
                this.net = net;
            }

            public String getWinPer() {
                return winPer;
            }

            public void setWinPer(String winPer) {
                this.winPer = winPer;
            }

            public String getDrawPer() {
                return drawPer;
            }

            public void setDrawPer(String drawPer) {
                this.drawPer = drawPer;
            }

            public String getLosePer() {
                return losePer;
            }

            public void setLosePer(String losePer) {
                this.losePer = losePer;
            }
        }

        public static class GuestLetPlateBean {
            /**
             * count : 41
             * over : 2
             * under : 39
             * win : 18
             * draw : 0
             * lose : 23
             * net : -5
             * winPer : 43.90%
             * drawPer : 0.00%
             * losePer : 56.10%
             */

            private String count;
            private String over;
            private String under;
            private String win;
            private String draw;
            private String lose;
            private String net;
            private String winPer;
            private String drawPer;
            private String losePer;

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getOver() {
                return over;
            }

            public void setOver(String over) {
                this.over = over;
            }

            public String getUnder() {
                return under;
            }

            public void setUnder(String under) {
                this.under = under;
            }

            public String getWin() {
                return win;
            }

            public void setWin(String win) {
                this.win = win;
            }

            public String getDraw() {
                return draw;
            }

            public void setDraw(String draw) {
                this.draw = draw;
            }

            public String getLose() {
                return lose;
            }

            public void setLose(String lose) {
                this.lose = lose;
            }

            public String getNet() {
                return net;
            }

            public void setNet(String net) {
                this.net = net;
            }

            public String getWinPer() {
                return winPer;
            }

            public void setWinPer(String winPer) {
                this.winPer = winPer;
            }

            public String getDrawPer() {
                return drawPer;
            }

            public void setDrawPer(String drawPer) {
                this.drawPer = drawPer;
            }

            public String getLosePer() {
                return losePer;
            }

            public void setLosePer(String losePer) {
                this.losePer = losePer;
            }
        }
    }
}
