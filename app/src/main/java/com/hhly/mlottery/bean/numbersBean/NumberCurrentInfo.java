package com.hhly.mlottery.bean.numbersBean;

import java.io.Serializable;
import java.util.List;

/**
 * 当前开奖号码
 * 
 * @ClassName: NumberCurrentInfo
 * @Description:
 * @author Tenney
 * @date 2015-10-23 下午12:23:53
 */
public class NumberCurrentInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String issue;// 开奖期号
	private String name;// 彩票名称
	private String nextIssue;// 下一期号
	private String nextTime;// 下一期号时间
	private String numbers;// 开奖号码
	private String time;// 时间
	private String zodiac;// 开奖生肖
	private String jackpot;// 奖金池
	private String sales;// 全国销量
	private String firstCount;// 一等奖中奖注数
	private String firstBonus;// 一等奖单注奖金
	private String firstAddCount;// 一等奖追加中奖注数
	private String firstAddBonus;//一等奖追加单注奖金
	private String secondCount;//
	private String secondBonus;//
	private String secondAddCount;//
	private String secondAddBonus;//
	private String thirdCount;//
	private String thirdBonus;//
	private String thirdAddCount;//
	private String thirdAddBonus;//
	private String fourthCount;//
	private String fourthBonus;//
	private String fourthAddCount;//
	private String fourthAddBonus;//
	private String fifthCount;//
	private String fifthBonus;//
	private String fifthAddCount;//
	private String fifthAddBonus;//
	private String sixthCount;//
	private String sixthBonus;//
	private String sixthAddCount;//
	private String sixthAddBonus;//
	private String dirCount;// 直选中奖注数
	private String dirBonus;// 直选单注奖金
	private String groupCount;// 组选中奖注数
	private String groupBonus;// 组选单注奖金
	private String prizeTime;// 兑奖日期
	private String saleTime;// 开售日期
	private String stopTime;// 停售日期
	private String lotteryTime;// 开奖日期
	private List<FootIssueResultData> footballLotteryIssueResultData;//
	private FootballSecLottery footballSecLottery;
	private FootballFirlottery footballFirlottery;
	private boolean isOpen = false;// 是否发生改变了

	public String getSixthAddCount() {
		return sixthAddCount;
	}

	public void setSixthAddCount(String sixthAddCount) {
		this.sixthAddCount = sixthAddCount;
	}

	public String getSixthAddBonus() {
		return sixthAddBonus;
	}

	public void setSixthAddBonus(String sixthAddBonus) {
		this.sixthAddBonus = sixthAddBonus;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getFirstCount() {
		return firstCount;
	}

	public void setFirstCount(String firstCount) {
		this.firstCount = firstCount;
	}

	public String getFirstBonus() {
		return firstBonus;
	}

	public void setFirstBonus(String firstBonus) {
		this.firstBonus = firstBonus;
	}

	public String getFirstAddCount() {
		return firstAddCount;
	}

	public void setFirstAddCount(String firstAddCount) {
		this.firstAddCount = firstAddCount;
	}

	public String getFirstAddBonus() {
		return firstAddBonus;
	}

	public void setFirstAddBonus(String firstAddBonus) {
		this.firstAddBonus = firstAddBonus;
	}

	public String getSecondCount() {
		return secondCount;
	}

	public void setSecondCount(String secondCount) {
		this.secondCount = secondCount;
	}

	public String getSecondBonus() {
		return secondBonus;
	}

	public void setSecondBonus(String secondBonus) {
		this.secondBonus = secondBonus;
	}

	public String getSecondAddCount() {
		return secondAddCount;
	}

	public void setSecondAddCount(String secondAddCount) {
		this.secondAddCount = secondAddCount;
	}

	public String getSecondAddBonus() {
		return secondAddBonus;
	}

	public void setSecondAddBonus(String secondAddBonus) {
		this.secondAddBonus = secondAddBonus;
	}

	public String getThirdCount() {
		return thirdCount;
	}

	public void setThirdCount(String thirdCount) {
		this.thirdCount = thirdCount;
	}

	public String getThirdBonus() {
		return thirdBonus;
	}

	public void setThirdBonus(String thirdBonus) {
		this.thirdBonus = thirdBonus;
	}

	public String getThirdAddCount() {
		return thirdAddCount;
	}

	public void setThirdAddCount(String thirdAddCount) {
		this.thirdAddCount = thirdAddCount;
	}

	public String getThirdAddBonus() {
		return thirdAddBonus;
	}

	public void setThirdAddBonus(String thirdAddBonus) {
		this.thirdAddBonus = thirdAddBonus;
	}

	public String getFourthCount() {
		return fourthCount;
	}

	public void setFourthCount(String fourthCount) {
		this.fourthCount = fourthCount;
	}

	public String getFourthBonus() {
		return fourthBonus;
	}

	public void setFourthBonus(String fourthBonus) {
		this.fourthBonus = fourthBonus;
	}

	public String getFourthAddCount() {
		return fourthAddCount;
	}

	public void setFourthAddCount(String fourthAddCount) {
		this.fourthAddCount = fourthAddCount;
	}

	public String getFourthAddBonus() {
		return fourthAddBonus;
	}

	public void setFourthAddBonus(String fourthAddBonus) {
		this.fourthAddBonus = fourthAddBonus;
	}

	public String getFifthCount() {
		return fifthCount;
	}

	public void setFifthCount(String fifthCount) {
		this.fifthCount = fifthCount;
	}

	public String getFifthBonus() {
		return fifthBonus;
	}

	public void setFifthBonus(String fifthBonus) {
		this.fifthBonus = fifthBonus;
	}

	public String getFifthAddCount() {
		return fifthAddCount;
	}

	public void setFifthAddCount(String fifthAddCount) {
		this.fifthAddCount = fifthAddCount;
	}

	public String getFifthAddBonus() {
		return fifthAddBonus;
	}

	public void setFifthAddBonus(String fifthAddBonus) {
		this.fifthAddBonus = fifthAddBonus;
	}

	public String getSixthCount() {
		return sixthCount;
	}

	public void setSixthCount(String sixthCount) {
		this.sixthCount = sixthCount;
	}

	public String getSixthBonus() {
		return sixthBonus;
	}

	public void setSixthBonus(String sixthBonus) {
		this.sixthBonus = sixthBonus;
	}

	public String getDirCount() {
		return dirCount;
	}

	public void setDirCount(String dirCount) {
		this.dirCount = dirCount;
	}

	public String getDirBonus() {
		return dirBonus;
	}

	public void setDirBonus(String dirBonus) {
		this.dirBonus = dirBonus;
	}

	public String getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(String groupCount) {
		this.groupCount = groupCount;
	}

	public String getGroupBonus() {
		return groupBonus;
	}

	public void setGroupBonus(String groupBonus) {
		this.groupBonus = groupBonus;
	}

	public String getPrizeTime() {
		return prizeTime;
	}

	public void setPrizeTime(String prizeTime) {
		this.prizeTime = prizeTime;
	}

	public String getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(String saleTime) {
		this.saleTime = saleTime;
	}

	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public String getLotteryTime() {
		return lotteryTime;
	}

	public void setLotteryTime(String lotteryTime) {
		this.lotteryTime = lotteryTime;
	}

	public List<FootIssueResultData> getFootballLotteryIssueResultData() {
		return footballLotteryIssueResultData;
	}

	public void setFootballLotteryIssueResultData(List<FootIssueResultData> footballLotteryIssueResultData) {
		this.footballLotteryIssueResultData = footballLotteryIssueResultData;
	}

	public FootballSecLottery getFootballSecLottery() {
		return footballSecLottery;
	}

	public void setFootballSecLottery(FootballSecLottery footballSecLottery) {
		this.footballSecLottery = footballSecLottery;
	}

	public FootballFirlottery getFootballFirlottery() {
		return footballFirlottery;
	}

	public void setFootballFirlottery(FootballFirlottery footballFirlottery) {
		this.footballFirlottery = footballFirlottery;
	}

	public String getJackpot() {
		return jackpot;
	}

	public void setJackpot(String jackpot) {
		this.jackpot = jackpot;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNextIssue() {
		return nextIssue;
	}

	public void setNextIssue(String nextIssue) {
		this.nextIssue = nextIssue;
	}

	public String getNextTime() {
		return nextTime;
	}

	public void setNextTime(String nextTime) {
		this.nextTime = nextTime;
	}

	public String getNumbers() {
		return numbers;
	}

	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getZodiac() {
		return zodiac;
	}

	public void setZodiac(String zodiac) {
		this.zodiac = zodiac;
	}

	@Override
	public String toString() {
		return "彩种：" + getName() + "-号码 ：" + getNumbers();
	}
}
