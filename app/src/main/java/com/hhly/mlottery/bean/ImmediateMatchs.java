package com.hhly.mlottery.bean;

import java.util.List;

/**
 *
 * @ClassName: ImmediateMatchs
 * @Description: 对应url：http://m.13322.com/mlottery/core/matchResults.findImmediateMatchs.do 
 * @author chenml
 * @date 2015-10-13 下午6:50:43
 */
public class ImmediateMatchs {
	private List<Match> immediateMatch;
	private List<LeagueCup> all;

	private String teamLogoSuff;

	private String teamLogoPre;

	public String getFilerDate() {
		return filerDate;
	}

	public void setFilerDate(String filerDate) {
		this.filerDate = filerDate;
	}

	private String filerDate;



	public List<Match> getImmediateMatch() {
		return immediateMatch;
	}
	public void setImmediateMatch(List<Match> immediateMatch) {
		this.immediateMatch = immediateMatch;
	}
	public List<LeagueCup> getAll() {
		return all;
	}
	public void setAll(List<LeagueCup> all) {
		this.all = all;
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



//	private ArrayList<Match> immediateMatch;
//	private ArrayList<LeagueCup> all;
//	public ArrayList<Match> getImmediateMatch() {
//		return immediateMatch;
//	}
//	public void setImmediateMatch(ArrayList<Match> immediateMatch) {
//		this.immediateMatch = immediateMatch;
//	}
//	public ArrayList<LeagueCup> getAll() {
//		return all;
//	}
//	public void setAll(ArrayList<LeagueCup> all) {
//		this.all = all;
//	}





}
