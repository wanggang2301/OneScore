package com.hhly.mlottery.bean.numbersBean;

/**
 * @ClassName: NumberNextTime 
 * @Description: 下一期的开奖期数和开奖时间
 * @author tangrr
 * @date 2015-11-10 下午9:21:51
 */
public class NumberNextTime {
	
	private NextLotteryResult nextLotteryResult;
	private String serverTime;// 服务器时间戳
	
	public String getServerTime() {
		return serverTime;
	}
	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}
	public NextLotteryResult getNextLotteryResult() {
		return nextLotteryResult;
	}
	public void setNextLotteryResult(NextLotteryResult nextLotteryResult) {
		this.nextLotteryResult = nextLotteryResult;
	}
	
}
