package com.hhly.mlottery.bean.websocket;

/**
 * Created by A on 2016/1/8.
 */
public class WebBasketOdds {

    private int type;	//101
//    private List<Map<String,String>> data;
    private WebBasketAllOdds data;

//    private WebBasketData data; // 	Object
    private String  thirdId; // 	224562

    public WebBasketAllOdds getData() {
        return data;
    }

    public void setData(WebBasketAllOdds data) {
        this.data = data;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
