package data.bean;

/**
 * 描    述：篮球球队球员数据实体类
 * 作    者：mady@13322.com
 * 时    间：2017/6/24
 */
public class BasketTeamPlayerBean {

    /**
     * playerId : 2973
     * playerName : 蒂莫菲-莫兹戈夫
     * positionType : 1
     * positionName : 中锋
     * playerImg : http://data.1332255.com/basket/images/database/player/2973.png
     * palyerShirtNum : 20
     */

    private String playerId;
    private String playerName;
    private String positionType;
    private String positionName;
    private String playerImg;
    private String palyerShirtNum;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPlayerImg() {
        return playerImg;
    }

    public void setPlayerImg(String playerImg) {
        this.playerImg = playerImg;
    }

    public String getPalyerShirtNum() {
        return palyerShirtNum;
    }

    public void setPalyerShirtNum(String palyerShirtNum) {
        this.palyerShirtNum = palyerShirtNum;
    }
}
