package com.hhly.mlottery.bean.numbersBean;

import java.util.List;

/**
 * 彩票开奖信息类
 * @ClassName: NumbersOpenBean 
 * @Description:
 * @author Tenney
 * @date 2015-10-20 下午12:04:11
 */
public class NumbersOpenBean {
	private List<NumberCurrentInfo> numLotteryResults;
//	private List<NumberHistoryInfo> historyLotteryResults;
	//private List<NumberHistoryHKInfo> historyLotteryResults;
	private String serverTime;

	public List<NumberCurrentInfo> getNumLotteryResults() {
		return numLotteryResults;
	}

	public void setNumLotteryResults(List<NumberCurrentInfo> numLotteryResults) {
		this.numLotteryResults = numLotteryResults;
	}


//	public List<NumberHistoryInfo> getHistoryLotteryResults() {
//		return historyLotteryResults;
//	}
//
//	public void setHistoryLotteryResults(
//			List<NumberHistoryInfo> historyLotteryResults) {
//		this.historyLotteryResults = historyLotteryResults;
//	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}
}
