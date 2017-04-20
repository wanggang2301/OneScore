package com.hhly.mlottery.bean.numbersBean;

import java.util.List;

/**
 * 彩票开奖信息类
 * 
 * @ClassName: NumbersOpenBean
 * @Description:
 * @author Tenney
 * @date 2015-10-20 下午12:04:11
 */
public class NumbersHistoryBean {

	public List<NumberCurrentInfo> historyLotteryResults;

	public List<NumberCurrentInfo> getHistoryLotteryResults() {
		return historyLotteryResults;
	}

	public void setHistoryLotteryResults(List<NumberCurrentInfo> historyLotteryResults) {
		this.historyLotteryResults = historyLotteryResults;
	}

	//	public List<NumberHistoryHKInfo> historyLotteryResults;
//
//	public List<NumberHistoryHKInfo> getHistoryLotteryResults() {
//		return historyLotteryResults;
//	}
//
//	public void setHistoryLotteryResults(
//			List<NumberHistoryHKInfo> historyLotteryResults) {
//		this.historyLotteryResults = historyLotteryResults;
//	}

}
