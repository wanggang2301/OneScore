package com.hhly.mlottery.bean.footballDetails;

import java.util.List;

/**
 * @author wang gang
 * @date 2016/6/12 15:30
 * @des ${}
 */
public class BottomOddsDetails {

    private String result;

    private List<BottomOddsDetailsItem> matchoddlist;

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
