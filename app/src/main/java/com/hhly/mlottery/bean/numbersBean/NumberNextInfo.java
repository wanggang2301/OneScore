package com.hhly.mlottery.bean.numbersBean;

import java.io.Serializable;

/**
 * 彩票下一期开奖时间及期号
 * @ClassName: NumberNextInfo 
 * @Description:
 * @author Tenney
 * @date 2015-10-24 下午8:01:47
 */
public class NumberNextInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String drawtime;// 开奖时间
	private String issue;// 开奖期号
	private String serverTime;// 服务器时间
	
	public String getDrawtime() {
		return drawtime;
	}
	public void setDrawtime(String drawtime) {
		this.drawtime = drawtime;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getServerTime() {
		return serverTime;
	}
	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}


}
