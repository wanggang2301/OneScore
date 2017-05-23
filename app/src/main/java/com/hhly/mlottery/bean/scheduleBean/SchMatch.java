package com.hhly.mlottery.bean.scheduleBean;

public class SchMatch {
	private String thirdId;
	private String racename;
	private String raceId;
	private String raceColor;
	private String date;
	private String time;
	private String hometeam;
	private String guestteam;
	private String homeId;


	private String guestId;

	private String statusOrigin;

	private ScheduleMatchOdd matchOdds;
	private String txt;
	private String winner;

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getHomeId() {
		return homeId;
	}

	public void setHomeId(String homeId) {
		this.homeId = homeId;
	}

	public String getGuestId() {
		return guestId;
	}

	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}



	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}

	public String getRacename() {
		return racename;
	}

	public void setRacename(String racename) {
		this.racename = racename;
	}

	public String getRaceId() {
		return raceId;
	}

	public void setRaceId(String raceId) {
		this.raceId = raceId;
	}

	public String getRaceColor() {
		return raceColor;
	}

	public void setRaceColor(String raceColor) {
		this.raceColor = raceColor;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getHometeam() {
		return hometeam;
	}

	public void setHometeam(String hometeam) {
		this.hometeam = hometeam;
	}

	public String getGuestteam() {
		return guestteam;
	}

	public void setGuestteam(String guestteam) {
		this.guestteam = guestteam;
	}

	public String getStatusOrigin() {
		return statusOrigin;
	}

	public void setStatusOrigin(String statusOrigin) {
		this.statusOrigin = statusOrigin;
	}

	public ScheduleMatchOdd getMatchOdds() {
		return matchOdds;
	}

	public void setMatchOdds(ScheduleMatchOdd matchOdds) {
		this.matchOdds = matchOdds;
	}



}
