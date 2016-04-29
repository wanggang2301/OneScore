package com.hhly.mlottery.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.ScheduleDateAdapter;
import com.hhly.mlottery.bean.scheduleBean.ScheduleDate;

public class ResultDateUtil {
	
	public static String currentData() {

		int y, m, d;
		Calendar cal = Calendar.getInstance();
		y = cal.get(Calendar.YEAR);
		m = cal.get(Calendar.MONTH) + 1;
		d = cal.get(Calendar.DATE);
		// System.out.println("现在时刻是"+y+"年"+m+"月"+d+"日");

		return y + "-" + m + "-" + d;
	}
	
	/** 获得日期 */
	public static String getDate(int i, String dString) {
		int y, m, d;
		String year = "", month = "", day = "";

		Calendar cal = Calendar.getInstance();

		cal.setTime(DateUtil.parseDate(dString));

		cal.add(Calendar.DAY_OF_YEAR, -i); // -i 前一天日期
		y = cal.get(Calendar.YEAR);
		m = cal.get(Calendar.MONTH) + 1;
		d = cal.get(Calendar.DATE);

		year = String.valueOf(y);
		month = String.valueOf(m);
		day = String.valueOf(d);

		if (month.length() == 1) {
			month = "0" + month;
		}
		if (day.length() == 1) {
			day = "0" + day;
		}
		return year + "-" + month + "-" + day;
	}
	
	/** 获得星期 */
	public static String getWeekOfDate(Date dt) {
//		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		
		String[] weekDays = { MyApp.getContext().getResources().getString(R.string.number_xinqi7), MyApp.getContext().getResources().getString(R.string.number_xinqi1),
				MyApp.getContext().getResources().getString(R.string.number_xinqi2), MyApp.getContext().getResources().getString(R.string.number_xinqi3),
				MyApp.getContext().getResources().getString(R.string.number_xinqi4), MyApp.getContext().getResources().getString(R.string.number_xinqi5),
				MyApp.getContext().getResources().getString(R.string.number_xinqi6) };
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	
	/**
	 * 初始化7天的日期
	 * 
	 * @param s
	 */
	public static ScheduleDateAdapter initListDateAndWeek(Context context,List<ScheduleDate> datelist, String s) {
		datelist = new ArrayList<ScheduleDate>();
		for (int i = 0; i < 7; i++) {
			datelist.add(new ScheduleDate(getDate(i, s), DateUtil.getWeekOfXinQi(DateUtil.parseDate(getDate(i, s)))));
		}
		return new ScheduleDateAdapter(datelist, context);
	}
}
