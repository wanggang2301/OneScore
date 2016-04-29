package com.hhly.mlottery.bean.footballsecond;

import java.util.List;

/**
 * @ClassName: OneScore
 * @author:Administrator luyao
 * @Description:  联赛列表
 * @data: 2016/3/30 10:37
 */
public class InforListData {


    /**
     * code : 200
     * leagueList : [{"lid":36,"name":"英格兰超级联赛","type":0},{"lid":31,"name":"西班牙甲组联赛","type":0},{"lid":34,"name":"意大利甲组联赛","type":0},{"lid":8,"name":"德国甲组联赛","type":0},{"lid":11,"name":"法国甲组联赛","type":0},{"lid":60,"name":"中国超级联赛","type":0},{"lid":67,"name":"欧洲国家杯","type":2},{"lid":224,"name":"美洲国家杯","type":2},{"lid":103,"name":"欧洲联赛冠军杯赛","type":2},{"lid":113,"name":"欧罗巴联赛杯","type":2},{"lid":192,"name":"亚洲联赛冠军杯","type":2},{"lid":61,"name":"中国甲级联赛","type":0},{"lid":780,"name":"日本足球联赛","type":1},{"lid":284,"name":"日本职业联赛J2","type":1}]
     */

    private int code;
    /**
     * lid : 36
     * name : 英格兰超级联赛
     * type : 0
     */

    private List<LeagueListBean> leagueList;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<LeagueListBean> getLeagueList() {
        return leagueList;
    }

    public void setLeagueList(List<LeagueListBean> leagueList) {
        this.leagueList = leagueList;
    }

    public static class LeagueListBean {
        private int lid;
        private String name;
        private int type;

        public int getLid() {
            return lid;
        }

        public void setLid(int lid) {
            this.lid = lid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
