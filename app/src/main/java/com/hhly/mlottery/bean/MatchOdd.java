package com.hhly.mlottery.bean;

/**
 * 
 * @ClassName: MatchOdd 
 * @Description: 对应matchOdds字段
 * @author chenml
 * @date 2015-10-13 下午6:52:07
 */
public class MatchOdd {
	private String relateThirdId;
	private String handicap;
	private String handicapValue;
	private boolean seal;
	private String leftOddsState;
	private String mediumValueState;
	private String rightOddsState;
	private String leftOdds;
	private String mediumOdds;
	private String rightOdds;
	public String getRelateThirdId() {
		return relateThirdId;
	}
	public void setRelateThirdId(String relateThirdId) {
		this.relateThirdId = relateThirdId;
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
	public boolean isSeal() {
		return seal;
	}
	public void setSeal(boolean seal) {
		this.seal = seal;
	}
	public String getLeftOddsState() {
		return leftOddsState;
	}
	public void setLeftOddsState(String leftOddsState) {
		this.leftOddsState = leftOddsState;
	}
	public String getMediumValueState() {
		return mediumValueState;
	}
	public void setMediumValueState(String mediumValueState) {
		this.mediumValueState = mediumValueState;
	}
	public String getRightOddsState() {
		return rightOddsState;
	}
	public void setRightOddsState(String rightOddsState) {
		this.rightOddsState = rightOddsState;
	}
	public String getLeftOdds() {
		return leftOdds;
	}
	public void setLeftOdds(String leftOdds) {
		this.leftOdds = leftOdds;
	}
	public String getMediumOdds() {
		return mediumOdds;
	}
	public void setMediumOdds(String mediumOdds) {
		this.mediumOdds = mediumOdds;
	}
	public String getRightOdds() {
		return rightOdds;
	}
	public void setRightOdds(String rightOdds) {
		this.rightOdds = rightOdds;
	}
	@Override
	public String toString() {
		return "MatchOdd [relateThirdId=" + relateThirdId + ", handicap=" + handicap + ", handicapValue=" + handicapValue + ", seal=" + seal + ", leftOddsState=" + leftOddsState
				+ ", mediumValueState=" + mediumValueState + ", rightOddsState=" + rightOddsState + ", leftOdds=" + leftOdds + ", mediumOdds=" + mediumOdds + ", rightOdds=" + rightOdds + "]";
	}

	public MatchOdd(){}
	public MatchOdd(String handicap, String handicapValue, String rightOddsState, String leftOdds) {
		this.setHandicap(handicap);
		this.setHandicapValue(handicapValue);
		this.setRightOdds(rightOddsState);
		this.setLeftOdds(leftOdds);
	}

	public void setTypeOddds(String handicapValue, String rightOddsState, String leftOdds) {
		this.setHandicapValue(handicapValue);
		this.setRightOdds(rightOddsState);
		this.setLeftOdds(leftOdds);
	}

	public void setEuroTypeOdds(String mediumOdds, String rightOddsState, String leftOdds) {
		this.setMediumOdds(mediumOdds);
		this.setRightOdds(rightOddsState);
		this.setLeftOdds(leftOdds);
	}

}
