package com.hhly.mlottery.bean.footballDetails;

import java.util.List;

/**首发阵容
 * Created by asus1 on 2015/12/30.
 */
public class FirstPlayersBean {



    private  String result;

    private List<PlayerInfo> homeLineUp;

    private List<PlayerInfo> guestLineUp;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<PlayerInfo> getHomeLineUp() {
        return homeLineUp;
    }

    public void setHomeLineUp(List<PlayerInfo> homeLineUp) {
        this.homeLineUp = homeLineUp;
    }

    public List<PlayerInfo> getGuestLineUp() {
        return guestLineUp;
    }

    public void setGuestLineUp(List<PlayerInfo> guestLineUp) {
        this.guestLineUp = guestLineUp;
    }
}
