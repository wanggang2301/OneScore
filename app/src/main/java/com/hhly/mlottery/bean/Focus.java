package com.hhly.mlottery.bean;

import java.util.List;

public class Focus {
	private List<Match> focus;

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

	public List<Match> getFocus() {
		return focus;
	}

	public void setFocus(List<Match> focus) {
		this.focus = focus;
	}
	
	
	
}
