package com.hhly.mlottery.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class HomeLHCData implements Parcelable {


	private String name;
	private String issue;
	private String time;
	private String numbers;
	private String zodiac;
	private String nextTime;
	private String nextIssue;

	public String getNextTime() {
		return nextTime;
	}

	public void setNextTime(String nextTime) {
		this.nextTime = nextTime;
	}

	public String getNextIssue() {
		return nextIssue;
	}

	public void setNextIssue(String nextIssue) {
		this.nextIssue = nextIssue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNumbers() {
		return numbers;
	}

	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}

	public String getZodiac() {
		return zodiac;
	}

	public void setZodiac(String zodiac) {
		this.zodiac = zodiac;
	}

	@Override
	public String toString() {
		return "LHC [name=" + name + ", issue=" + issue + ", time=" + time
				+ ", numbers=" + numbers + ", zodiac=" + zodiac + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(name);
		dest.writeString(issue);
		dest.writeString(time);
		dest.writeString(numbers);
		dest.writeString(zodiac);
		dest.writeString(nextTime);
		dest.writeString(nextIssue);

	}

	public HomeLHCData() {

	}

	public HomeLHCData(Parcel in) {
		name = in.readString();
		issue = in.readString();
		time = in.readString();
		numbers = in.readString();
		zodiac=in.readString();
		nextTime=in.readString();
		nextIssue=in.readString();
	}

}
