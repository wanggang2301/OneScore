package com.hhly.mlottery.bean.resultbean;

import com.hhly.mlottery.bean.Match;

import java.util.List;


/**
 * 
 * @ClassName: Resulttoday 
 * @Description: current 字段对应数据  （当天）
 * @author yixq
 * @date 2015-10-16 下午1:40:50
 */

public class ResultDateMatchs {

	private String date;
	
	private List<Match> match;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Match> getMatch() {
		return match;
	}

	public void setMatch(List<Match> match) {
		this.match = match;
	}
	
}
