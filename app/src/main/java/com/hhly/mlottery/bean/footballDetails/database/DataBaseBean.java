package com.hhly.mlottery.bean.footballDetails.database;

/**
 * 描述:  ${TODO}
 * 作者:  wangg@13322.com
 * 时间:  2016/9/2 17:15
 */
public class DataBaseBean {

    private String kind;
    private String leagueId;
    private String lgName;
    private String pic;
    public DataBaseBean() {

    }

    public DataBaseBean(String kind, String leagueId, String pic, String lgName) {
        this.kind = kind;
        this.leagueId = leagueId;
        this.pic = pic;
        this.lgName = lgName;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getLgName() {
        return lgName;
    }

    public void setLgName(String lgName) {
        this.lgName = lgName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
