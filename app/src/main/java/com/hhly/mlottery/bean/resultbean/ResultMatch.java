package com.hhly.mlottery.bean.resultbean;

import com.hhly.mlottery.bean.LeagueCup;

import java.util.List;

/**
 *
 * @ClassName: ResultDetailsBean
 * @Description: 赛果 - 对应URL
 *               http://183.61.172.82:8096/mlottery/core/appMatchResults
 *               .findResultOfBallMatch.do
 * @author yixq
 * @date 2015-10-15 上午10:44:32
 */

public class ResultMatch {

	private ResultDateMatchs current;// 当天数据
	private ResultDateMatchs previous;// 前一天数据

	private String filerDate;

	public String getFilerDate() {
		return filerDate;
	}

	public void setFilerDate(String filerDate) {
		this.filerDate = filerDate;
	}

	private List<LeagueCup> finishFilter;

	private String teamLogoPre;

	private String teamLogoSuff;

	public String getTeamLogoPre() {
		return teamLogoPre;
	}

	public void setTeamLogoPre(String teamLogoPre) {
		this.teamLogoPre = teamLogoPre;
	}

	public String getTeamLogoSuff() {
		return teamLogoSuff;
	}

	public void setTeamLogoSuff(String teamLogoSuff) {
		this.teamLogoSuff = teamLogoSuff;
	}




	public ResultDateMatchs getCurrent() {
		return current;
	}

	public void setCurrent(ResultDateMatchs current) {
		this.current = current;
	}

	public ResultDateMatchs getPrevious() {
		return previous;
	}

	public void setPrevious(ResultDateMatchs previous) {
		this.previous = previous;
	}


	public List<LeagueCup> getFinishFilter() {
		return finishFilter;
	}

	public void setFinishFilter(List<LeagueCup> finishFilter) {
		this.finishFilter = finishFilter;
	}

}
