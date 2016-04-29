package com.hhly.mlottery.util;

public class HomeTeamWinUtils {

	public static int setAsiaLetWinBackground(double homeScore, double handicapValue, double guestScore) {
		/**
		 * 设置亚盘胜出
		 */
		if (homeScore - guestScore - handicapValue > 0) {
			return 1;
		} else if (homeScore - guestScore - handicapValue < 0) {
			return 2;
		}
		return 0;

	}

	public static int setAsiaSizeWinBackground(double homeScore, double handicapValue, double guestScore) {
		/**
		 * 设置大小球胜出
		 */
		if (homeScore + guestScore - handicapValue > 0) {
			return 1;
		} else if (homeScore + guestScore - handicapValue < 0) {
			return 2;
		}
		return 0;

	}

	public static int setEurotWinBackground(double homeScore, double guestScore) {
		/**
		 * 设置欧盘胜出
		 */
		if (homeScore - guestScore > 0) {
			return 1;
		} else if (homeScore + -guestScore < 0) {
			return 2;
		}
		return 0;

	}

}
