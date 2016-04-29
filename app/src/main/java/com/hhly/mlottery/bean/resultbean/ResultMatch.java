package com.hhly.mlottery.bean.resultbean;

import com.hhly.mlottery.bean.LeagueCup;

import java.util.List;

/**
 * 
 * @ClassName: ResultDetailsBean
 * @Description: 赛果 - 对应URL
 *               http://183.61.172.82:8096/mlottery/core/appMatchResults
 *               .findResultOfBallMatch.do
 * @author yixq
 * @date 2015-10-15 上午10:44:32
 */

public class ResultMatch {

	 private ResultDateMatchs current;// 当天数据
	 private ResultDateMatchs previous;// 前一天数据


	private List<LeagueCup> finishFilter;

	
	 public ResultDateMatchs getCurrent() {
	 return current;
	 }
	
	 public void setCurrent(ResultDateMatchs current) {
	 this.current = current;
	 }
	
	 public ResultDateMatchs getPrevious() {
	 return previous;
	 }
	
	 public void setPrevious(ResultDateMatchs previous) {
	 this.previous = previous;
	 }


	 public List<LeagueCup> getFinishFilter() {
			return finishFilter;
		}

		public void setFinishFilter(List<LeagueCup> finishFilter) {
			this.finishFilter = finishFilter;
		}

}
