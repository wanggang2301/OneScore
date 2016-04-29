package com.hhly.mlottery.bean.resultbean;

import com.hhly.mlottery.bean.Match;

import java.util.List;

/**
 * 
 * @ClassName: ResultMatchDto
 * @Description: 赛果--数据集
 * @author yixq
 * @date 2015-10-21 下午2:06:53
 */

public class ResultMatchDto {

	private int type;
	private String date;

	private Match matchs;// current 的matchs 数据
//	private Previousmatchs match;// previous 的matchs 数据

//	public Previousmatchs getMatch() {
//		return match;
//	}
//
//	public void setMatch(Previousmatchs match) {
//		this.match = match;
//	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Match getMatchs() {
		return matchs;
	}

	public void setMatchs(Match matchs) {
		this.matchs = matchs;
	}

}
