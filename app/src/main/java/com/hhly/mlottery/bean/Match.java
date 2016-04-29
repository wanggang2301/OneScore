package com.hhly.mlottery.bean;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * 
 * @ClassName: Match 
 * @Description: 对应immediateMatch字段
 * @author chenml
 * @date 2015-10-13 下午6:51:47
 */
public class Match implements Parcelable,Cloneable{
	private String thirdId;
	private String racename;
	private String raceId;
	private String raceColor;
	private String date;
	private String time;
	private String hometeam;
	private String guestteam;
	private String homerank;
	private String guestrank;
	
	private String statusOrigin;
	
	private String status;
	private String keepTime;
	private String halfScore;
	private String fullScore;
	private String home_rc;
	private String home_yc;
	private String guest_rc;
	private String guest_yc;
	
	private String homeScore;
	private String guestScore;
	
	private String homeHalfScore;
	private String guestHalfScore;

	private Map<String,MatchOdd> matchOdds;
	
	private int leftOddTextColorId;
	private int rightOddTextColorId;
	private int midOddTextColorId;
	
	private int homeTeamTextColorId;
	private int guestTeamTextColorId;
	
	private int itemBackGroundColorId;//用于进球背景变化


	public static final Creator<Match> CREATOR = new Creator<Match>() {
		@Override
		public Match createFromParcel(Parcel in) {
			return new Match(in);
		}

		@Override
		public Match[] newArray(int size) {
			return new Match[size];
		}
	};

	public int getHomeTeamTextColorId() {
		return homeTeamTextColorId;
	}
	public void setHomeTeamTextColorId(int homeTeamTextColorId) {
		this.homeTeamTextColorId = homeTeamTextColorId;
	}
	public int getGuestTeamTextColorId() {
		return guestTeamTextColorId;
	}
	
	public void setGuestTeamTextColorId(int guestTeamTextColorId) {
		this.guestTeamTextColorId = guestTeamTextColorId;
	}
	public int getItemBackGroundColorId() {
		return itemBackGroundColorId;
	}
	public void setItemBackGroundColorId(int itemBackGroundColorId) {
		this.itemBackGroundColorId = itemBackGroundColorId;
	}
	
	

	public int getLeftOddTextColorId() {
		return leftOddTextColorId;
	}
	public void setLeftOddTextColorId(int leftOddTextColorId) {
		this.leftOddTextColorId = leftOddTextColorId;
	}
	public int getRightOddTextColorId() {
		return rightOddTextColorId;
	}
	public void setRightOddTextColorId(int rightOddTextColorId) {
		this.rightOddTextColorId = rightOddTextColorId;
	}
	public int getMidOddTextColorId() {
		return midOddTextColorId;
	}
	public void setMidOddTextColorId(int midOddTextColorId) {
		this.midOddTextColorId = midOddTextColorId;
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
	public String getHomerank() {
		return homerank;
	}
	public void setHomerank(String homerank) {
		this.homerank = homerank;
	}
	public String getGuestrank() {
		return guestrank;
	}
	public void setGuestrank(String guestrank) {
		this.guestrank = guestrank;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getKeepTime() {
		return keepTime;
	}
	public void setKeepTime(String keepTime) {
		this.keepTime = keepTime;
	}
	public String getHalfScore() {
		return halfScore;
	}
	public void setHalfScore(String halfScore) {
		this.halfScore = halfScore;
	}
	public String getFullScore() {
		return fullScore;
	}
	public void setFullScore(String fullScore) {
		this.fullScore = fullScore;
	}
	public String getHome_rc() {
		return home_rc;
	}
	public void setHome_rc(String home_rc) {
		this.home_rc = home_rc;
	}
	public String getHome_yc() {
		return home_yc;
	}
	public void setHome_yc(String home_yc) {
		this.home_yc = home_yc;
	}
	public String getGuest_rc() {
		return guest_rc;
	}
	public void setGuest_rc(String guest_rc) {
		this.guest_rc = guest_rc;
	}
	public String getGuest_yc() {
		return guest_yc;
	}
	public void setGuest_yc(String guest_yc) {
		this.guest_yc = guest_yc;
	}
	public Map<String, MatchOdd> getMatchOdds() {
		return matchOdds;
	}
	public void setMatchOdds(Map<String, MatchOdd> matchOdds) {
		this.matchOdds = matchOdds;
	}
	
	
	
	public String getHomeScore() {
		return homeScore;
	}
	public void setHomeScore(String homeScore) {
		this.homeScore = homeScore;
	}
	public String getGuestScore() {
		return guestScore;
	}
	public void setGuestScore(String guestScore) {
		this.guestScore = guestScore;
	}
	public String getStatusOrigin() {
		return statusOrigin;
	}
	public void setStatusOrigin(String statusOrigin) {
		this.statusOrigin = statusOrigin;
	}
	
	
	
	public String getHomeHalfScore() {
		return homeHalfScore;
	}
	public void setHomeHalfScore(String homeHalfScore) {
		this.homeHalfScore = homeHalfScore;
	}
	public String getGuestHalfScore() {
		return guestHalfScore;
	}
	public void setGuestHalfScore(String guestHalfScore) {
		this.guestHalfScore = guestHalfScore;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(thirdId);
		dest.writeString(racename);
		dest.writeString(raceId);
		dest.writeString(raceColor);
		dest.writeString(date);
		dest.writeString(time);
		dest.writeString(hometeam);
		dest.writeString(guestteam);
		dest.writeString(homerank);
		dest.writeString(guestrank);
		dest.writeString(status);
		dest.writeString(statusOrigin);
		dest.writeString(keepTime);
		dest.writeString(halfScore);
		dest.writeString(fullScore);
		dest.writeString(home_rc);
		dest.writeString(home_yc);
		dest.writeString(guest_rc);
		dest.writeString(guest_yc);
		dest.writeMap(matchOdds);
		
	}
	

	
	public Match(){
		
	}
	
	public Match(Parcel in){
		thirdId = in.readString();
		racename = in.readString();
		raceId = in.readString();
		raceColor = in.readString();
		date = in.readString();
		time = in.readString();
		hometeam = in.readString();
		guestteam = in.readString();
		homerank = in.readString();
		guestrank = in.readString();
		status = in.readString();
		keepTime = in.readString();
		halfScore = in.readString();
		fullScore = in.readString();
		home_rc = in.readString();
		home_yc = in.readString();
		guest_rc = in.readString();
		guest_yc = in.readString();
		statusOrigin = in.readString();
		matchOdds = in.readHashMap(HashMap.class.getClassLoader());
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	

}
