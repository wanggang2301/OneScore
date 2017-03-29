package com.hhly.mlottery.bean.tennisball.datails.analysis;

import java.util.List;

/**
 * desc:网球内页分析bean
 * Created by 107_tangrr on 2017/3/27 0027.
 */

public class TennisAnalysisBean {

    private String result;
    private DataBean data;

    public static class DataBean {
        private RecentMatchBean recentMatch;
        private MatchRecordBean matchRecord;
        private MatchInfoBean matchInfo;
        private List<RankScoreBean> rankScore;
        private List<DataCompareBean> dataCompare;

        public RecentMatchBean getRecentMatch() {
            return recentMatch;
        }

        public void setRecentMatch(RecentMatchBean recentMatch) {
            this.recentMatch = recentMatch;
        }

        public MatchRecordBean getMatchRecord() {
            return matchRecord;
        }

        public void setMatchRecord(MatchRecordBean matchRecord) {
            this.matchRecord = matchRecord;
        }

        public MatchInfoBean getMatchInfo() {
            return matchInfo;
        }

        public void setMatchInfo(MatchInfoBean matchInfo) {
            this.matchInfo = matchInfo;
        }

        public List<RankScoreBean> getRankScore() {
            return rankScore;
        }

        public void setRankScore(List<RankScoreBean> rankScore) {
            this.rankScore = rankScore;
        }

        public List<DataCompareBean> getDataCompare() {
            return dataCompare;
        }

        public void setDataCompare(List<DataCompareBean> dataCompare) {
            this.dataCompare = dataCompare;
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
}
