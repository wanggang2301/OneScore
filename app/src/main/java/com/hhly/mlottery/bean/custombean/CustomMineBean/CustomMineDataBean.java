package com.hhly.mlottery.bean.custombean.CustomMineBean;

import java.util.List;

/**
 * Created by yixq on 2016/12/15.
 * mail：yixq@13322.com
 * describe:
 */

public class CustomMineDataBean {

//    data	Object
//    result	200

    private OuterData data;
    private int result;

    public OuterData getData() {
        return data;
    }

    public void setData(OuterData data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public class OuterData{

//        leagueItem	Array

        private List<CustomMineFirstDataBean> leagueItem;

        public List<CustomMineFirstDataBean> getLeagueItem() {
            return leagueItem;
        }

        public void setLeagueItem(List<CustomMineFirstDataBean> leagueItem) {
            this.leagueItem = leagueItem;
        }
    }
}
