package com.hhly.mlottery.bean;

/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description:
 * @data: 2016/5/5 15:58
 */
public class WelcomeUrl {

    /**
     * result : 200
     * url : http://192.168.10.242:9090/oms/upload/coopenpicture/8aa6bb9115de482e88ef52e306fe3f76.png
     */

    private int result;
    private String url;
    private int duration;
    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
