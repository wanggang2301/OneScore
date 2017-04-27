package com.hhly.mlottery.bean.footballteaminfo;

import java.util.List;

/**
 * desc:足球球队历史比赛
 * Created by 107_tangrr on 2017/4/20 0020.
 */

public class FootTeamHistoryMatchBean {

    private int code;
    private List<MatchListBean> matchList;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<MatchListBean> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<MatchListBean> matchList) {
        this.matchList = matchList;
    }

    public static class MatchListBean {

        private String date;// 比赛时间
        private String league;// 赛事类型
        private String homeTeam;// 主队名
        private String guestTeam;// 客队名
        private String matchResult;// 比分
        private int homeId;
        private int guestId;
        private int matchId;
        private String homeIcon;
        private String guestIcon;
        private int isHome; // 1、主;0、客
        private int color; // 3、胜;2、平;1、负
        private int state;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getLeague() {
            return league;
        }

        public void setLeague(String league) {
            this.league = league;
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

        public String getMatchResult() {
            return matchResult;
        }

        public void setMatchResult(String matchResult) {
            this.matchResult = matchResult;
        }

        public int getHomeId() {
            return homeId;
        }

        public void setHomeId(int homeId) {
            this.homeId = homeId;
        }

        public int getGuestId() {
            return guestId;
        }

        public void setGuestId(int guestId) {
            this.guestId = guestId;
        }

        public int getMatchId() {
            return matchId;
        }

        public void setMatchId(int matchId) {
            this.matchId = matchId;
        }

        public String getHomeIcon() {
            return homeIcon;
        }

        public void setHomeIcon(String homeIcon) {
            this.homeIcon = homeIcon;
        }

        public String getGuestIcon() {
            return guestIcon;
        }

        public void setGuestIcon(String guestIcon) {
            this.guestIcon = guestIcon;
        }

        public int getIsHome() {
            return isHome;
        }

        public void setIsHome(int isHome) {
            this.isHome = isHome;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
