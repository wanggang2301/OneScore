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
