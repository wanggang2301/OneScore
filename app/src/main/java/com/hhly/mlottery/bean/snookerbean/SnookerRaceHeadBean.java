package com.hhly.mlottery.bean.snookerbean;

import java.util.List;

/**
 * Created by yuely198 on 2017/2/20.
 * 斯诺克赛事内页头部数据列表
 */

public class SnookerRaceHeadBean {

    /**
     * result : 200
     * data : {"leagueId":"319655","leagueName":"澳大利亚公开赛","leagueLogo":null,"currentSeason":"2015","seasonList":["2015","2014","2013","2012","2011"],"leagueBetweenDate":"06.29-07.05","leagueProfile":"2015-16斯诺克赛季首站大型排名赛澳大利亚公开赛，6月29日至7月5日在本迭戈开战，卫冕冠军特鲁姆普、世锦赛冠亚军宾汉姆和墨菲、世界排名第一的塞尔比以及本土名将罗伯逊，将争夺新赛季首个大赛荣誉，丁俊晖和奥沙利文缺席。"}
     */

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
        /**
         * leagueId : 319655
         * leagueName : 澳大利亚公开赛
         * leagueLogo : null
         * currentSeason : 2015
         * seasonList : ["2015","2014","2013","2012","2011"]
         * leagueBetweenDate : 06.29-07.05
         * leagueProfile : 2015-16斯诺克赛季首站大型排名赛澳大利亚公开赛，6月29日至7月5日在本迭戈开战，卫冕冠军特鲁姆普、世锦赛冠亚军宾汉姆和墨菲、世界排名第一的塞尔比以及本土名将罗伯逊，将争夺新赛季首个大赛荣誉，丁俊晖和奥沙利文缺席。
         */

        private String leagueId;
        private String leagueName;
        private Object leagueLogo;
        private String currentSeason;
        private String leagueBetweenDate;
        private String leagueProfile;
        private List<String> seasonList;

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

        public Object getLeagueLogo() {
            return leagueLogo;
        }

        public void setLeagueLogo(Object leagueLogo) {
            this.leagueLogo = leagueLogo;
        }

        public String getCurrentSeason() {
            return currentSeason;
        }

        public void setCurrentSeason(String currentSeason) {
            this.currentSeason = currentSeason;
        }

        public String getLeagueBetweenDate() {
            return leagueBetweenDate;
        }

        public void setLeagueBetweenDate(String leagueBetweenDate) {
            this.leagueBetweenDate = leagueBetweenDate;
        }

        public String getLeagueProfile() {
            return leagueProfile;
        }

        public void setLeagueProfile(String leagueProfile) {
            this.leagueProfile = leagueProfile;
        }

        public List<String> getSeasonList() {
            return seasonList;
        }

        public void setSeasonList(List<String> seasonList) {
            this.seasonList = seasonList;
        }
    }
}
