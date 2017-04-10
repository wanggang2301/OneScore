package com.hhly.mlottery.bean.numbersBean;

import java.io.Serializable;

/**
 * 往期开奖号码
 * @ClassName: NumberHistoryInfo 
 * @Description:
 * @author Tenney
 * @date 2015-10-23 下午12:23:41
 */
public class NumberHistoryInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String issue;// 开奖期号
	private String numbers;// 开奖号码
	private String time;// 时间

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

	@Override
	public String toString() {
		return "开奖期号：" + getIssue() + "-开奖号码 ：" + getNumbers() + "--时间："
				+ getTime() ;
	}
}
