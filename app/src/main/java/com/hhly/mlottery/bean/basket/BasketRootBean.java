package com.hhly.mlottery.bean.basket;

import java.util.List;

/**
 * Created by A on 2016/1/4.
 */
public class BasketRootBean {

        private List<BasketMatchBean> match ;

        private String date;

        private int diffDays;//标识具体是那天


        public void setMatch(List<BasketMatchBean> match){
            this.match = match;
        }
        public List<BasketMatchBean> getMatch(){
            return this.match;
        }
        public void setDate(String date){
            this.date = date;
        }
        public String getDate(){
            return this.date;
        }

    public int getDiffDays() {
        return diffDays;
    }

    public void setDiffDays(int diffDays) {
        this.diffDays = diffDays;
    }
}
