package com.hhly.mlottery.bean.numbersBean;

import java.io.Serializable;

/**
 * 往期六合彩开奖号码
 * @ClassName: NumberHistoryInfo 
 * @Description:
 * @author Tenney
 * @date 2015-10-23 下午12:23:41
 */
public class NumberHistoryHKInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String issue;// 开奖期号
	private String numbers;// 开奖号码
	private String time;// 时间
	private String zodiac;// 生肖

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
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
		return "期号：" + getIssue()+ "-开奖生肖 ：" + getZodiac() + "--时间：" + getTime();
	}
}
