package com.hhly.mlottery.bean.footballteaminfo;

import java.util.List;

/**
 * desc:足球球队盘口
 * Created by 107_tangrr on 2017/4/20 0020.
 */

public class FootTeamMatchOddBean {

    private int code;
    private List<CurrOUBean> currOU;
    private List<CurrHDPBean> currHDP;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<CurrOUBean> getCurrOU() {
        return currOU;
    }

    public void setCurrOU(List<CurrOUBean> currOU) {
        this.currOU = currOU;
    }

    public List<CurrHDPBean> getCurrHDP() {
        return currHDP;
    }

    public void setCurrHDP(List<CurrHDPBean> currHDP) {
        this.currHDP = currHDP;
    }

    public static class CurrOUBean {

        private String lgName;
        private DataBeanOU data;

        public String getLgName() {
            return lgName;
        }

        public void setLgName(String lgName) {
            this.lgName = lgName;
        }

        public DataBeanOU getData() {
            return data;
        }

        public void setData(DataBeanOU data) {
            this.data = data;
        }

        public static class DataBeanOU {

            private String matchCount;// 比赛场次
            private String over;// 大球
            private String voi;// 走盘
            private String under;// 小球
            private String overRate;// 大球率
            private String voiRate;// 走盘率
            private String underRate;// 小球率
            private String under25;// 低于2.5
            private String over25;// 高于2.5

            public String getMatchCount() {
                return matchCount;
            }

            public void setMatchCount(String matchCount) {
                this.matchCount = matchCount;
            }

            public String getOver() {
                return over;
            }

            public void setOver(String over) {
                this.over = over;
            }

            public String getVoi() {
                return voi;
            }

            public void setVoi(String voi) {
                this.voi = voi;
            }

            public String getUnder() {
                return under;
            }

            public void setUnder(String under) {
                this.under = under;
            }

            public String getOverRate() {
                return overRate;
            }

            public void setOverRate(String overRate) {
                this.overRate = overRate;
            }

            public String getVoiRate() {
                return voiRate;
            }

            public void setVoiRate(String voiRate) {
                this.voiRate = voiRate;
            }

            public String getUnderRate() {
                return underRate;
            }

            public void setUnderRate(String underRate) {
                this.underRate = underRate;
            }

            public String getUnder25() {
                return under25;
            }

            public void setUnder25(String under25) {
                this.under25 = under25;
            }

            public String getOver25() {
                return over25;
            }

            public void setOver25(String over25) {
                this.over25 = over25;
            }
        }
    }

    public static class CurrHDPBean {

        private String lgName;
        private DataBeanHDP data;

        public String getLgName() {
            return lgName;
        }

        public void setLgName(String lgName) {
            this.lgName = lgName;
        }

        public DataBeanHDP getData() {
            return data;
        }

        public void setData(DataBeanHDP data) {
            this.data = data;
        }

        public static class DataBeanHDP {

            private String matchCount;// 比赛场次
            private String ms;// 上盘
            private String ns;// 平盘
            private String ts;// 下盘
            private String hdpw;// 赢盘
            private String hdph;// 赢半
            private String voi;// 走盘
            private String hdplh;// 输半
            private String hdpl;// 输盘
            private String winrate;// 胜率
            private String loserate;// 负率

            public String getMatchCount() {
                return matchCount;
            }

            public void setMatchCount(String matchCount) {
                this.matchCount = matchCount;
            }

            public String getMs() {
                return ms;
            }

            public void setMs(String ms) {
                this.ms = ms;
            }

            public String getNs() {
                return ns;
            }

            public void setNs(String ns) {
                this.ns = ns;
            }

            public String getTs() {
                return ts;
            }

            public void setTs(String ts) {
                this.ts = ts;
            }

            public String getHdpw() {
                return hdpw;
            }

            public void setHdpw(String hdpw) {
                this.hdpw = hdpw;
            }

            public String getHdph() {
                return hdph;
            }

            public void setHdph(String hdph) {
                this.hdph = hdph;
            }

            public String getVoi() {
                return voi;
            }

            public void setVoi(String voi) {
                this.voi = voi;
            }

            public String getHdplh() {
                return hdplh;
            }

            public void setHdplh(String hdplh) {
                this.hdplh = hdplh;
            }

            public String getHdpl() {
                return hdpl;
            }

            public void setHdpl(String hdpl) {
                this.hdpl = hdpl;
            }

            public String getWinrate() {
                return winrate;
            }

            public void setWinrate(String winrate) {
                this.winrate = winrate;
            }

            public String getLoserate() {
                return loserate;
            }

            public void setLoserate(String loserate) {
                this.loserate = loserate;
            }
        }
    }
}
