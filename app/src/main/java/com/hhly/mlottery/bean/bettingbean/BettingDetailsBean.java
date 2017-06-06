package com.hhly.mlottery.bean.bettingbean;

/**
 * Created by：XQyi on 2017/6/6 12:20
 * Use: 推介详情的实体
 */
public class BettingDetailsBean {
//    code	200
//    detail	Object

    private Integer code;
    private DetailsBeanData detail;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public DetailsBeanData getDetail() {
        return detail;
    }

    public void setDetail(DetailsBeanData detail) {
        this.detail = detail;
    }

    public class DetailsBeanData{
//        choose	1
//        choose1
//                context
//        handicap	-1
//        leagueId	2

//        leftOdds	4.41
//        matchId	848853606
//        midOdds	3.6
//        oddsCompany
//        oddsId	19731

//        price	1
//        rightOdds
//        status	2
//        title	yyy
//        type	0
//        userId	HHLY00000136

        private String choose;
        private String choose1;
        private String context;
        private String handicap;
        private String leagueId;

        private String leftOdds;
        private String matchId;
        private String midOdds;
        private String oddsCompany;
        private String oddsId;

        private String price;
        private String rightOdds;
        private String status;
        private String title;
        private String type;
        private String userId;

        public String getChoose() {
            return choose;
        }

        public void setChoose(String choose) {
            this.choose = choose;
        }

        public String getChoose1() {
            return choose1;
        }

        public void setChoose1(String choose1) {
            this.choose1 = choose1;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getHandicap() {
            return handicap;
        }

        public void setHandicap(String handicap) {
            this.handicap = handicap;
        }

        public String getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(String leagueId) {
            this.leagueId = leagueId;
        }

        public String getLeftOdds() {
            return leftOdds;
        }

        public void setLeftOdds(String leftOdds) {
            this.leftOdds = leftOdds;
        }

        public String getMatchId() {
            return matchId;
        }

        public void setMatchId(String matchId) {
            this.matchId = matchId;
        }

        public String getMidOdds() {
            return midOdds;
        }

        public void setMidOdds(String midOdds) {
            this.midOdds = midOdds;
        }

        public String getOddsCompany() {
            return oddsCompany;
        }

        public void setOddsCompany(String oddsCompany) {
            this.oddsCompany = oddsCompany;
        }

        public String getOddsId() {
            return oddsId;
        }

        public void setOddsId(String oddsId) {
            this.oddsId = oddsId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getRightOdds() {
            return rightOdds;
        }

        public void setRightOdds(String rightOdds) {
            this.rightOdds = rightOdds;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

}
