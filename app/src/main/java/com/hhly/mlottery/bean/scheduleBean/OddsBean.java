package com.hhly.mlottery.bean.scheduleBean;


/**
 * 亚赔
 * @ClassName: MatchOdd
 * @Description:
 * @author Tenney
 * @date 2015-10-20 下午5:36:54
 */
public class OddsBean {

	
	private String  handicap;
	private String  handicapValue;
	private String  leftOdds;
	private String  mediumOdds;
	private String  rightOdds;


	public String getMediumOdds() {
		return mediumOdds;
	}

	public void setMediumOdds(String mediumOdds) {
		this.mediumOdds = mediumOdds;
	}

	public String getHandicap() {
		return handicap;
	}
	public void setHandicap(String handicap) {
		this.handicap = handicap;
	}
	public String getHandicapValue() {
		return handicapValue;
	}
	public void setHandicapValue(String handicapValue) {
		this.handicapValue = handicapValue;
	}
	public String getLeftOdds() {
		return leftOdds;
	}
	public void setLeftOdds(String leftOdds) {
		this.leftOdds = leftOdds;
	}
	public String getRightOdds() {
		return rightOdds;
	}
	public void setRightOdds(String rightOdds) {
		this.rightOdds = rightOdds;
	}
	
	
}
