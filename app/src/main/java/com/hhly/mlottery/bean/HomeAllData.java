package com.hhly.mlottery.bean;

import java.util.List;


public class HomeAllData {
	private String total;
	private String serverTime;
	private List<Match> ball;
	private HomePK10Data pk10;
	private HomeLHCData lhc;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<Match> getBall() {
		return ball;
	}

	public void setBall(List<Match> ball) {
		this.ball = ball;
	}

	public HomePK10Data getPk10() {
		return pk10;
	}

	public void setPk10(HomePK10Data pk10) {
		this.pk10 = pk10;
	}

	public HomeLHCData getLhc() {
		return lhc;
	}

	public void setLhc(HomeLHCData lhc) {
		this.lhc = lhc;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	
	
}
