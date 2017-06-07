package data.bean;

/**
 * @author wang gang
 * @date 2016/6/12 15:37
 * @des ${}
 */
public class BottomOddsDetailsItem {
    private String time;

    private String score;

    private BottomOddsItem odd;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public BottomOddsItem getOdd() {
        return odd;
    }

    public void setOdd(BottomOddsItem odd) {
        this.odd = odd;
    }
}
