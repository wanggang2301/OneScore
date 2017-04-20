package com.hhly.mlottery.util;

import java.text.DecimalFormat;
import java.util.List;

import android.text.TextUtils;

import com.hhly.mlottery.bean.numbersBean.NumberInfo;

/**
 * @des 对号码还有生肖进行拆分动作
 * @ClassName: NumberSubUtils 
 * @Description:
 * @author Tenney
 * @date 2015-10-22 上午10:22:29
 */
public class NumberSubUtils {

	public static void disposeSubNumbers(NumberInfo mNumberInfo,
			List<String> numbers, List<String> zodiacs) {

		// System.out.println("彩种："+mNumberInfo.getName()+"-开奖号码 ："+mNumberInfo.getNumbers()+"-开奖时间 ："
		// + mNumberInfo.getTime());

		// 拆分开奖号码
		if (!TextUtils.isEmpty(mNumberInfo.getNumbers())) {

			// 46,15,38,39,7,11#25
			String[] nums1 = mNumberInfo.getNumbers().split(",");

			if ("1".equals(mNumberInfo.getName())) {

				// 将号码添加到集合中
				for (int i = 0; i < nums1.length - 1; i++) {
					numbers.add(nums1[i]);
				}

				String[] nums2 = nums1[5].split("#");
				for (String string : nums2) {
					numbers.add(string);
				}
			} else {
				// 将号码添加到集合中
				for (int i = 0; i < nums1.length; i++) {
					numbers.add(nums1[i]);
				}
			}
		}

		// 六合彩拆分生肖
		if (!TextUtils.isEmpty(mNumberInfo.getZodiac())) {
			// 狗,蛇,马,蛇,牛,鸡#羊
			String[] nums1 = mNumberInfo.getZodiac().split(",");
			for (int i = 0; i < nums1.length - 1; i++) {
				zodiacs.add(nums1[i]);
			}
			String[] nums2 = nums1[5].split("#");
			for (String string : nums2) {
				zodiacs.add(string);
			}
		}

	}

	/**
	 * 添加千位符
	 * @param num
	 * @return
     */
	public static String qianweifenge(Long num) {
		DecimalFormat df = new DecimalFormat("#,##0");
		String ss = df.format(num);
		return ss;
	}
}
