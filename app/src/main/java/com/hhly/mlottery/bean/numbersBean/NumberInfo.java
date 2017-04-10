package com.hhly.mlottery.bean.numbersBean;

import java.io.Serializable;

public class NumberInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String issue;//	开奖期号
	private String name	;// 彩票名称
	private String nextIssue	;// 下一期号
	private String nextTime	;// 下一期号时间
	private String numbers	;// 开奖号码
	private String time	;// 时间
	private String zodiac ;// 开奖生肖
	
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNextIssue() {
		return nextIssue;
	}
	public void setNextIssue(String nextIssue) {
		this.nextIssue = nextIssue;
	}
	public String getNextTime() {
		return nextTime;
	}
	public void setNextTime(String nextTime) {
		this.nextTime = nextTime;
	}
	public String getNumbers() {
		return numbers;
	}
	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getZodiac() {
		return zodiac;
	}
	public void setZodiac(String zodiac) {
		this.zodiac = zodiac;
	}
	
	@Override
	public String toString() {
		return "彩种：" + getName()
				+ "-号码 ：" + getNumbers();
	}
}
