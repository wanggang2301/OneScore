package com.hhly.mlottery.bean.websocket;

import java.util.Map;

/**
 * Created by A on 2016/2/18.
 */
public class WebBasketOdds5 {

    private Map<String , String> bet365; //     bet365
    private Map<String , String> crown;//      皇冠
    private Map<String , String> easybets;//  易胜博
    private Map<String , String> vcbet;//      韦德
    private Map<String , String> macauslot;// 澳门

    private Map<String , String> euro; // 欧赔取值

    public Map<String, String> getEuro() {
        return euro;
    }

    public void setEuro(Map<String, String> euro) {
        this.euro = euro;
    }

    public Map<String, String> getBet365() {
        return bet365;
    }

    public void setBet365(Map<String, String> bet365) {
        this.bet365 = bet365;
    }

    public Map<String, String> getCrown() {
        return crown;
    }

    public void setCrown(Map<String, String> crown) {
        this.crown = crown;
    }

    public Map<String, String> getEasybets() {
        return easybets;
    }

    public void setEasybets(Map<String, String> easybets) {
        this.easybets = easybets;
    }

    public Map<String, String> getVcbet() {
        return vcbet;
    }

    public void setVcbet(Map<String, String> vcbet) {
        this.vcbet = vcbet;
    }

    public Map<String, String> getMacauslot() {
        return macauslot;
    }

    public void setMacauslot(Map<String, String> macauslot) {
        this.macauslot = macauslot;
    }
}
