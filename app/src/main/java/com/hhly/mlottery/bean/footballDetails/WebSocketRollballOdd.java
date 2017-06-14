package com.hhly.mlottery.bean.footballDetails;

/**
 * @author wang gang
 * @date 2016/6/16 12:03
 * @des ${}
 */
public class WebSocketRollballOdd {

    private String thirdId;
    private String oddType;
    private String left;
    private String middle;
    private String right;
    private String matchInTime;
    private String score;

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getOddType() {
        return oddType;
    }

    public void setOddType(String oddType) {
        this.oddType = oddType;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getMatchInTime() {
        return matchInTime;
    }

    public void setMatchInTime(String matchInTime) {
        this.matchInTime = matchInTime;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "WebSocketRollballOdd{" +
                "thirdId='" + thirdId + '\'' +
                ", oddType='" + oddType + '\'' +
                ", left='" + left + '\'' +
                ", middle='" + middle + '\'' +
                ", right='" + right + '\'' +
                ", matchInTime='" + matchInTime + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
