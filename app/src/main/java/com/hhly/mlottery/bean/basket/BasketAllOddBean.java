package com.hhly.mlottery.bean.basket;

/**
 * Created by A on 2016/2/18.
 */
public class BasketAllOddBean {

//    private String bet365;//bet365;//   bet365

    private BasketOddBean bet365;
    private BasketOddBean crown;//      皇冠
    private BasketOddBean easybets;//  易胜博
    private BasketOddBean vcbet;//      韦德
    private BasketOddBean macauslot;// 澳门
    private BasketOddBean euro; //欧赔默认

    public BasketOddBean getEuro() {
        return euro;
    }

    public void setEuro(BasketOddBean euro) {
        this.euro = euro;
    }

    public BasketOddBean getBet365() {
        return bet365;
    }

    public void setBet365(BasketOddBean bet365) {
        this.bet365 = bet365;
    }

    public BasketOddBean getCrown() {
        return crown;
    }

    public void setCrown(BasketOddBean crown) {
        this.crown = crown;
    }

    public BasketOddBean getEasybets() {
        return easybets;
    }

    public void setEasybets(BasketOddBean easybets) {
        this.easybets = easybets;
    }

    public BasketOddBean getVcbet() {
        return vcbet;
    }

    public void setVcbet(BasketOddBean vcbet) {
        this.vcbet = vcbet;
    }

    public BasketOddBean getMacauslot() {
        return macauslot;
    }

    public void setMacauslot(BasketOddBean macauslot) {
        this.macauslot = macauslot;
    }
}
