package com.hhly.mlottery.bean.scheduleBean;

import com.hhly.mlottery.bean.LeagueCup;

import java.util.List;


/***
 *
 * @ClassName: ScheduleMatchs
 * @Description: URL:：http://m.13322.com/mlottery/core/matchResults.findCeaselessMatchs.do
 * @author wangg
 * @date 2015-10-15 上午10:18:37
 */
public class ScheduleMatchs {

	private List<LeagueCup>  ceaselessFilter;

	private ScheduleCurrent current;

	private String teamLogoPre;

	private String teamLogoSuff;

	private String filerDate;

	public String getFilerDate() {
		return filerDate;
	}

	public void setFilerDate(String filerDate) {
		this.filerDate = filerDate;
	}

	public String getTeamLogoSuff() {
		return teamLogoSuff;
	}

	public void setTeamLogoSuff(String teamLogoSuff) {
		this.teamLogoSuff = teamLogoSuff;
	}

	public String getTeamLogoPre() {
		return teamLogoPre;
	}

	public void setTeamLogoPre(String teamLogoPre) {
		this.teamLogoPre = teamLogoPre;
	}

	public List<LeagueCup> getCeaselessFilter() {
		return ceaselessFilter;
	}

	public void setCeaselessFilter(List<LeagueCup> ceaselessFilter) {
		this.ceaselessFilter = ceaselessFilter;
	}

	public ScheduleCurrent getCurrent() {
		return current;
	}

	public void setCurrent(ScheduleCurrent current) {
		this.current = current;
	}



}
