package com.hhly.mlottery.bean.footballsecond;

import java.util.List;

/**
 * 攻防实体类
 * Created by hhly107 on 2015/12/31.
 */
public class AttackTrendBean {


    /**
     * guestDanger : [0,4,7]
     * result : 200
     * homeDanger : [1,2,5]
     */

    private String result;// 处理结果
    private List<Integer> guestDanger;// 主队危险进攻
    private List<Integer> homeDanger;// 客队危险进攻

    public void setResult(String result) {
        this.result = result;
    }

    public void setGuestDanger(List<Integer> guestDanger) {
        this.guestDanger = guestDanger;
    }

    public void setHomeDanger(List<Integer> homeDanger) {
        this.homeDanger = homeDanger;
    }

    public String getResult() {
        return result;
    }

    public List<Integer> getGuestDanger() {
        return guestDanger;
    }

    public List<Integer> getHomeDanger() {
        return homeDanger;
    }
}
