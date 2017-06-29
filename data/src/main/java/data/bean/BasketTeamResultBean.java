package data.bean;

import java.util.List;

/**
 * 描    述：篮球球队赛程赛果实体类
 * 作    者：mady@13322.com
 * 时    间：2017/6/22
 */
public class BasketTeamResultBean {

    /**
     * result : 200
     * teamMatchData : [{"id":"3511357","matchTime":"2016-10-29 09:00:00","homeTeamId":"20","guestTeamId":"1","homeTeamName":"犹他爵士","guestTeamName":"洛杉矶湖人","homeScore":"96","guestScore":"89","matchStatus":"-1"},{"id":"3527477","matchTime":"2016-10-31 07:00:00","homeTeamId":"28","guestTeamId":"1","homeTeamName":"俄克拉荷马城雷霆","guestTeamName":"洛杉矶湖人","homeScore":"113","guestScore":"96","matchStatus":"-1"},{"id":"3542357","matchTime":"2016-11-02 07:00:00","homeTeamId":"10","guestTeamId":"1","homeTeamName":"印第安纳步行者","guestTeamName":"洛杉矶湖人","homeScore":"115","guestScore":"108","matchStatus":"-1"},{"id":"3546687","matchTime":"2016-11-03 07:30:00","homeTeamId":"13","guestTeamId":"1","homeTeamName":"亚特兰大老鹰","guestTeamName":"洛杉矶湖人","homeScore":"116","guestScore":"123","matchStatus":"-1"},{"id":"3578097","matchTime":"2016-11-11 11:30:00","homeTeamId":"24","guestTeamId":"1","homeTeamName":"萨克拉门托国王","guestTeamName":"洛杉矶湖人","homeScore":"91","guestScore":"101","matchStatus":"-1"},{"id":"3635587","matchTime":"2016-11-13 08:00:00","homeTeamId":"11","guestTeamId":"1","homeTeamName":"新奥尔良鹈鹕","guestTeamName":"洛杉矶湖人","homeScore":"99","guestScore":"126","matchStatus":"-1"},{"id":"3655107","matchTime":"2016-11-14 08:00:00","homeTeamId":"19","guestTeamId":"1","homeTeamName":"明尼苏达森林狼","guestTeamName":"洛杉矶湖人","homeScore":"125","guestScore":"99","matchStatus":"-1"},{"id":"3772317","matchTime":"2016-11-24 11:30:00","homeTeamId":"27","guestTeamId":"1","homeTeamName":"金州勇士","guestTeamName":"洛杉矶湖人","homeScore":"149","guestScore":"106","matchStatus":"-1"},{"id":"3842657","matchTime":"2016-11-30 09:00:00","homeTeamId":"11","guestTeamId":"1","homeTeamName":"新奥尔良鹈鹕","guestTeamName":"洛杉矶湖人","homeScore":"105","guestScore":"88","matchStatus":"-1"},{"id":"3846317","matchTime":"2016-12-01 09:00:00","homeTeamId":"14","guestTeamId":"1","homeTeamName":"芝加哥公牛","guestTeamName":"洛杉矶湖人","homeScore":"90","guestScore":"96","matchStatus":"-1"}]
     */

    private int result;
    private List<TeamMatchDataEntity> teamMatchData;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<TeamMatchDataEntity> getTeamMatchData() {
        return teamMatchData;
    }

    public void setTeamMatchData(List<TeamMatchDataEntity> teamMatchData) {
        this.teamMatchData = teamMatchData;
    }

    public static class TeamMatchDataEntity {
        /**
         * id : 3511357
         * matchTime : 2016-10-29 09:00:00
         * homeTeamId : 20
         * guestTeamId : 1
         * homeTeamName : 犹他爵士
         * guestTeamName : 洛杉矶湖人
         * homeScore : 96
         * guestScore : 89
         * matchStatus : -1
         */

        private String id;
        private String matchTime;
        private String homeTeamId;
        private String guestTeamId;
        private String homeTeamName;
        private String guestTeamName;
        private String homeScore;
        private String guestScore;
        private String matchStatus;
        private String leagueName;
        private String homeLogoUrl;
        private String guestLogoUrl;
        private String leagueId;

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

        public String getHomeLogoUrl() {
            return homeLogoUrl;
        }

        public void setHomeLogoUrl(String homeLogoUrl) {
            this.homeLogoUrl = homeLogoUrl;
        }

        public String getGuestLogoUrl() {
            return guestLogoUrl;
        }

        public void setGuestLogoUrl(String guestLogoUrl) {
            this.guestLogoUrl = guestLogoUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMatchTime() {
            return matchTime;
        }

        public void setMatchTime(String matchTime) {
            this.matchTime = matchTime;
        }

        public String getHomeTeamId() {
            return homeTeamId;
        }

        public void setHomeTeamId(String homeTeamId) {
            this.homeTeamId = homeTeamId;
        }

        public String getGuestTeamId() {
            return guestTeamId;
        }

        public void setGuestTeamId(String guestTeamId) {
            this.guestTeamId = guestTeamId;
        }

        public String getHomeTeamName() {
            return homeTeamName;
        }

        public void setHomeTeamName(String homeTeamName) {
            this.homeTeamName = homeTeamName;
        }

        public String getGuestTeamName() {
            return guestTeamName;
        }

        public void setGuestTeamName(String guestTeamName) {
            this.guestTeamName = guestTeamName;
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

        public String getMatchStatus() {
            return matchStatus;
        }

        public void setMatchStatus(String matchStatus) {
            this.matchStatus = matchStatus;
        }
    }
}
