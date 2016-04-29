package com.hhly.mlottery.util;

import com.hhly.mlottery.R;

import android.view.View;

/**
 * 
 * @ClassName: 字符串处理,以及更具数字选择对应的颜色
 * @Description: TODO
 * @author jimbo
 * @date 2015-10-20 上午8:43:11
 */
public class StringAndColorHelper {
	public static final String LHC = "lhc";
	public static final String PK10 = "pk10";

	/**
	 * 
	 * @param data 把字符串中
	 *            "#"替换成",",然后以","进行分割字符串
	 * @return返回一个数组
	 */
	public static String[] StringToArray(String data) {
		if (data.indexOf("#") != -1) {
			String newString = data.replace('#', ',');
			String[] stringArray = newString.split(",");
			return stringArray;
		} else {
			return data.split(",");
		}

	}

	/**
	 * 香港彩票对应的颜色值
	 * 
	 * @param view
	 * @param number
	 * @return
	 */
	public static void setLHCTextViewBackgroundColor(View view, int number) {
		if (number > 0) {
			switch (number) {
			case AppConstants.ONE:
			case AppConstants.TWO:
			case AppConstants.SEVEN:
			case AppConstants.EIGHT:
			case AppConstants.TWELVE:
			case AppConstants.THIRTEEN:
			case AppConstants.EIGHTEEN:
			case AppConstants.NINETEEN:
			case AppConstants.TWENTY_THREE:
			case AppConstants.TWENTY_FOUR:
			case AppConstants.TWENTY_NINE:
			case AppConstants.THIRTY:
			case AppConstants.THIRTY_FOUR:
			case AppConstants.THIRTY_FIVE:
			case AppConstants.FORTY:
			case AppConstants.FORTY_FIVE:
				view.setBackgroundResource(R.mipmap.number_bg_red);
				break;
			case AppConstants.THREE:
			case AppConstants.FOUR:
			case AppConstants.NINE:
			case AppConstants.TEN:
			case AppConstants.FOURTEEN:
			case AppConstants.FIFTEEN:
			case AppConstants.TWENTY:
			case AppConstants.TWENTY_FINE:
			case AppConstants.TWENTY_SIX:
			case AppConstants.THIRTY_ONE:
			case AppConstants.THIRTY_SIX:
			case AppConstants.THIRTY_SEVEN:
			case AppConstants.FORTY_ONE:
			case AppConstants.FORTY_TWO:
			case AppConstants.FORTY_SEVEN:
			case AppConstants.FORTY_EIGHT:
				view.setBackgroundResource(R.mipmap.number_bg_blue);
				break;
			case AppConstants.FIVE:
			case AppConstants.SIX:
			case AppConstants.ELEVEN:
			case AppConstants.SIRTEEN:
			case AppConstants.SEVENTEEN:
			case AppConstants.TWENTY_ONE:
			case AppConstants.TWENTY_TWO:
			case AppConstants.TWENTY_SEVEN:
			case AppConstants.TWENTY_ENGHT:
			case AppConstants.THIRTY_TWO:
			case AppConstants.THIRTY_THREE:
			case AppConstants.THIRTY_EIGHT:
			case AppConstants.THIRTY_NINE:
			case AppConstants.FORTY_THREE:
			case AppConstants.FORTY_FOUR:
			case AppConstants.FORTY_NINE:
				view.setBackgroundResource(R.mipmap.number_bg_green);
				break;
			default:
				break;
			}
		}
		
	}

	public static int setPK10TextViewBackgroundColor(View view, int number) {
		if (number>0) {
			switch (number) {
			case AppConstants.ONE:
				return view.getResources().getColor(
						R.color.homwe_pk10_brightyellow);
			case AppConstants.TWO:
				return view.getResources().getColor(R.color.homwe_pk10_blue);
			case AppConstants.THREE:
				return view.getResources().getColor(R.color.homwe_pk10_bgrey);
			case AppConstants.FOUR:
				return view.getResources().getColor(R.color.homwe_pk10_yellow);
			case AppConstants.FIVE:
				return view.getResources().getColor(R.color.homwe_pk10_sblue);
			case AppConstants.SIX:
				return view.getResources().getColor(R.color.homwe_pk10_violet);
			case AppConstants.SEVEN:
				return view.getResources().getColor(R.color.homwe_pk10_grey);
			case AppConstants.EIGHT:
				return view.getResources().getColor(R.color.homwe_pk10_red);
			case AppConstants.NINE:
				return view.getResources().getColor(R.color.homwe_pk10_oldred);
			case AppConstants.TEN:
				return view.getResources().getColor(R.color.homwe_pk10_green);
			default:
				break;
			}
		}
		return 0;

	}
}
