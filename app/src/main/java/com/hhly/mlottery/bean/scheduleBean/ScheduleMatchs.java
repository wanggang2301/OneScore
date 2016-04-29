package com.hhly.mlottery.bean.scheduleBean;

import java.util.List;
import java.util.Map;

import com.hhly.mlottery.bean.LeagueCup;


/***
 * 
 * @ClassName: ScheduleMatchs 
 * @Description: URL:：http://m.13322.com/mlottery/core/matchResults.findCeaselessMatchs.do
 * @author wangg
 * @date 2015-10-15 上午10:18:37
 */
public class ScheduleMatchs {

	private List<LeagueCup>  ceaselessFilter;

	private ScheduleCurrent current;
	
		
	public List<LeagueCup> getCeaselessFilter() {
		return ceaselessFilter;
	}

	public void setCeaselessFilter(List<LeagueCup> ceaselessFilter) {
		this.ceaselessFilter = ceaselessFilter;
	}

	public ScheduleCurrent getCurrent() {
		return current;
	}

	public void setCurrent(ScheduleCurrent current) {
		this.current = current;
	}

	


		

	

		
}
