package com.hhly.mlottery.frame.footballframe.eventbus;

/**
 * desc:
 * Created by 107_tangrr on 2016/12/8 0008.
 */

public class ChartBallContentEntitiy {
    private String nickName;
    private String userId;
    private String msg;
    private  String headIcon;
public  ChartBallContentEntitiy(String nickName,String userId,String headIcon,String msg){
    this.nickName=nickName;
    this.userId=userId;
    this.headIcon=headIcon;

}
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }
}
