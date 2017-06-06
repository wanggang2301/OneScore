package data.bean;

import java.util.List;

/**
 * @author wang gang
 * @date 2016/6/12 15:30
 * @des ${}
 */
public class BottomOddsDetails {

    private String result;

    private List<BottomOddsDetailsItem> matchoddlist;

    private FirstOdd first;

    public FirstOdd getFirst() {
        return first;
    }

    public void setFirst(FirstOdd first) {
        this.first = first;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<BottomOddsDetailsItem> getMatchoddlist() {
        return matchoddlist;
    }

    public void setMatchoddlist(List<BottomOddsDetailsItem> matchoddlist) {
        this.matchoddlist = matchoddlist;
    }


}
