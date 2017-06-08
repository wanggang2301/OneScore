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
    private MatchInfoBeanData matchInfo;

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

    public MatchInfoBeanData getMatchInfo() {
        return matchInfo;
    }

    public void setMatchInfo(MatchInfoBeanData matchInfo) {
        this.matchInfo = matchInfo;
    }

    public class DetailsBeanData{

//        id: "671",
//        title: "拉斐拉竞技&nbsp;VS&nbsp;阿马格罗",
//        context: "",
//        choose: null,
//        choose1: null,
//        price: 1,
//        userid: "HHLY00000136",
//        matchid: "848855144",
//        leagueid: "1183",
//        type: 3,
//        status: 0,
//        oddsId: 610541,
//        handicap: "平/半",
//        leftOdds: 1.11,
//        midOdds: null,
//        rightOdds: null,
//        oddsCompany: null,
//        serNum: ""

        private String id;
        private String userid;
        private String matchid;
        private String leagueid;

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
        private String serNum;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getMatchid() {
            return matchid;
        }

        public void setMatchid(String matchid) {
            this.matchid = matchid;
        }

        public String getLeagueid() {
            return leagueid;
        }

        public void setLeagueid(String leagueid) {
            this.leagueid = leagueid;
        }

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

        public String getSerNum() {
            return serNum;
        }

        public void setSerNum(String serNum) {
            this.serNum = serNum;
        }
    }

    public class MatchInfoBeanData{

//        matchId: "848853836",
//        screenings: null,
//        leagueName: "巴西甲",
//        homeName: "弗鲁米嫩塞",
//        guestName: "巴拉纳竞技",
//        matchDateTime: "2017-06-07 07:00:00",
//        homeImg: "20130913233035.png",
//        guestImg: "20130913214349.png",
//        status: "16",
//        homeScore: "1",
//        guestScore: "1",
//        nickname: "小456",
//        photoUrl: "http://tp.1332255.com/img/2017-05-18/c139f58c-3b8c-4dbc-85fd-33cf7206ccd4.png",
//        lookStatus: 4,
//        leagueId: "4"

        private String matchId;
        private String screenings;
        private String leagueName;
        private String homeName;
        private String guestName;
        private String matchDateTime;
        private String homeImg;
        private String guestImg;
        private String status;
        private String homeScore;
        private String guestScore;
        private String nickname;
        private String photoUrl;
        private String lookStatus;
        private String leagueId;

        public String getMatchId() {
            return matchId;
        }

        public void setMatchId(String matchId) {
            this.matchId = matchId;
        }

        public String getScreenings() {
            return screenings;
        }

        public void setScreenings(String screenings) {
            this.screenings = screenings;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public String getHomeName() {
            return homeName;
        }

        public void setHomeName(String homeName) {
            this.homeName = homeName;
        }

        public String getGuestName() {
            return guestName;
        }

        public void setGuestName(String guestName) {
            this.guestName = guestName;
        }

        public String getMatchDateTime() {
            return matchDateTime;
        }

        public void setMatchDateTime(String matchDateTime) {
            this.matchDateTime = matchDateTime;
        }

        public String getHomeImg() {
            return homeImg;
        }

        public void setHomeImg(String homeImg) {
            this.homeImg = homeImg;
        }

        public String getGuestImg() {
            return guestImg;
        }

        public void setGuestImg(String guestImg) {
            this.guestImg = guestImg;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getHomeScore() {
            return homeScore;
        }

        public void setHomeScore(String homeScore) {
            this.homeScore = homeScore;
        }

        public String getGuestScore() {
            return guestScore;
        }

        public void setGuestScore(String guestScore) {
            this.guestScore = guestScore;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getLookStatus() {
            return lookStatus;
        }

        public void setLookStatus(String lookStatus) {
            this.lookStatus = lookStatus;
        }

        public String getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(String leagueId) {
            this.leagueId = leagueId;
        }
    }

}
