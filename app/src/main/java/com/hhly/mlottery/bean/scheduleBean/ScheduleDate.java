package com.hhly.mlottery.bean.scheduleBean;

public class ScheduleDate {
	
	public ScheduleDate(String date, String week) {
		super();
		this.date = date;
		this.week = week;
	}
	private String date;
	private String week;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}

}
