package com.hhly.mlottery.bean.scheduleBean;

import java.util.List;

public class ScheduleCurrent {

	
	private String date;
	
	private List<SchMatch> match;


	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<SchMatch> getMatch() {
		return match;
	}

	public void setMatch(List<SchMatch> match) {
		this.match = match;
	}

	
}
