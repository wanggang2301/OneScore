package com.hhly.mlottery.bean.tennisball;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:网球比分实体类
 * Created by 107_tangrr on 2017/2/21 0021.
 */

public class TennisBallBean {


    /**
     * result : 200
     * data : {"dates":["2017-02-20","2017-02-19","2017-02-15"],"matchData":[{"matchId":"786811","leagueId":"323417","leagueName":"卡塔尔公开赛 ","roundName":"第一轮","time":"08:00:00","date":"2017-02-20","homePlayerId":"319473","homePlayerName":"西格蒙德","homePlayerId2":"","homePlayerName2":"","awayPlayerId":"319494","awayPlayerName":"普格","awayPlayerId2":"","awayPlayerName2":"","matchStatus":-1,"server":0,"set":0,"matchScore":{"homeSetScore1":0,"homeSetScore2":0,"homeSetScore3":0,"homeSetScore4":0,"homeSetScore5":0,"awaySetScore1":0,"awaySetScore2":0,"awaySetScore3":0,"awaySetScore4":0,"awaySetScore5":0,"homeDeciderScore1":0,"homeDeciderScore2":0,"homeDeciderScore3":0,"homeDeciderScore4":0,"homeDeciderScore5":0,"awayDeciderScore1":0,"awayDeciderScore2":0,"awayDeciderScore3":0,"awayDeciderScore4":0,"awayDeciderScore5":0,"homeTotalScore":0,"awayTotalScore":0,"homeCurrentScore":null,"awayCurrentScore":null}},{"matchId":"786563","leagueId":"323417","leagueName":"卡塔1111尔公开赛 ","roundName":"第一轮","time":"16:00:00","date":"2017-02-20","homePlayerId":"319426","homePlayerName":"张啊啊啊帅","homePlayerId2":"","homePlayerName2":"","awayPlayerId":"319578","awayPlayerName":"巴波丝","awayPlayerId2":"","awayPlayerName2":"","matchStatus":0,"server":0,"set":0,"matchScore":{"homeSetScore1":0,"homeSetScore2":10,"homeSetScore3":0,"homeSetScore4":0,"homeSetScore5":0,"awaySetScore1":0,"awaySetScore2":0,"awaySetScore3":10,"awaySetScore4":0,"awaySetScore5":0,"homeDeciderScore1":0,"homeDeciderScore2":0,"homeDeciderScore3":0,"homeDeciderScore4":0,"homeDeciderScore5":0,"awayDeciderScore1":0,"awayDeciderScore2":0,"awayDeciderScore3":0,"awayDeciderScore4":0,"awayDeciderScore5":0,"homeTotalScore":30,"awayTotalScore":0,"homeCurrentScore":null,"awayCurrentScore":null}}]}
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
        private ArrayList<String> dates;
        private List<MatchDataBean> matchData;

        public ArrayList<String> getDates() {
            return dates;
        }

        public void setDates(ArrayList<String> dates) {
            this.dates = dates;
        }

        public List<MatchDataBean> getMatchData() {
            return matchData;
        }

        public void setMatchData(List<MatchDataBean> matchData) {
            this.matchData = matchData;
        }
    }
}
