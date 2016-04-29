package com.hhly.mlottery.util;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.HomeData;

/**
 * 
 * @ClassName: HomeNumberDataUtils
 * @Description: 字符串处理, 注意88 是自己定义的一个数字
 * @author jimbo
 * @date 2015-10-25 下午4:42:50
 */
public class HomeNumberDataUtils {

	public static int[] pk10NumberCarNos = { R.mipmap.home_pk10_normal1, R.mipmap.home_pk10_normal2, R.mipmap.home_pk10_normal3, R.mipmap.home_pk10_normal4, R.mipmap.home_pk10_normal5,
			R.mipmap.home_pk10_normal6, R.mipmap.home_pk10_normal7, R.mipmap.home_pk10_normal8, R.mipmap.home_pk10_normal9, R.mipmap.home_pk10_normal10 };

	public static boolean isDynamicNumber = false;// 号码是否正在一个一个号码的的显示

	public static void disposeSubNumbers(HomeData mNumberInfo, List<String> numbers, List<String> zodiacs) {

		// 拆分开奖号码
		// if (!TextUtils.isEmpty(mNumberInfo.getNumbers())) {

		if (TextUtils.isEmpty(mNumberInfo.getNumbers())) {// 如果号码为空
			if ("1".equals(mNumberInfo.getName())) {
				for (int i = 0; i < 8; i++) {
					numbers.add("88");// 表示号码正在开奖中的状态
					// zodiacs.add("");
				}
			} else if ("15".equals(mNumberInfo.getName())) {
				for (int i = 0; i < 10; i++) {
					numbers.add("88");// 表示号码正在开奖中的状态
					// zodiacs.add("");
				}
			}

		} else {

			if ("1".equals(mNumberInfo.getName())) {

				/** ------模拟数据------ */
				// mNumberInfo.setNumbers("38,12,08");//38,12,08,38,12,08#50
				// mNumberInfo.setZodiac("马,猴,鸡");//马,猴,鸡,马,猴,鸡#猫
				/** ------模拟数据------ */
				if (!TextUtils.isEmpty(mNumberInfo.getNumbers())) {
					String[] nums1 = mNumberInfo.getNumbers().split(",");

					int len1 = 0;
					if (mNumberInfo.getNumbers().contains("#")) {
						len1 = nums1.length - 1;
					} else {
						len1 = nums1.length;
					}
					// 将号码添加到集合中
					for (int i = 0; i < len1; i++) {
						numbers.add(nums1[i]);
					}

					if (mNumberInfo.getNumbers().contains("#")) {
						String[] nums2 = nums1[len1].split("#");

						if (nums2.length > 1) {
							String[] sxh = { nums2[0], "88", nums2[1] };// 添加"+"号标记

							for (String string : sxh) {
								numbers.add(string);
							}

						} else {
							for (String string : nums2) {
								numbers.add(string);
							}
						}
					}
				}

				// 判断号码是否有8位，7位加一个标记号
				if (numbers.size() < 8) {
					for (int i = 0, len = (8 - numbers.size()); i < len; i++) {
						numbers.add("88");// 表示号码正在开奖中的状态
					}
				}
				if (!TextUtils.isEmpty(mNumberInfo.getZodiac())) {
					// 将生肖添加到集合中
					String[] nums3 = mNumberInfo.getZodiac().split(",");

					int len2 = 0;
					if (mNumberInfo.getNumbers().contains("#")) {
						len2 = nums3.length - 1;
					} else {
						len2 = nums3.length;
					}
					for (int i = 0; i < len2; i++) {
						zodiacs.add(nums3[i]);
					}

					if (mNumberInfo.getZodiac().contains("#")) {
						String[] nums4 = nums3[len2].split("#");
						if (nums4.length > 1) {
							String[] sx = { nums4[0], "加", nums4[1] };// 添加"+"号标记

							for (String string : sx) {
								zodiacs.add(string);
							}
						} else {
							for (String string : nums4) {
								zodiacs.add(string);
							}
						}
					}
				}

			} else {
				// 46,15,38,39,7,11#25
				String[] nums = mNumberInfo.getNumbers().split(",");

				// 将号码添加到集合中
				for (int i = 0; i < nums.length; i++) {
					numbers.add(nums[i]);
				}
			}
		}

	}

	/**
	 * 显示开奖号码详情
	 * 
	 * @param isHKOpenNumberStart
	 * 
	 * @param context上下文
	 * @param mNumberInfo开奖对象
	 * @param view容器
	 * @param numbers开奖号码集合
	 * @param zodiacs开奖生肖集合
	 */
	public static void numberAddInfo(Context context, HomeData mNumberInfo, View view, List<String> numbers, List<String> zodiacs) {

		if (view != null) {
			((ViewGroup) view).removeAllViews();
		}

		android.widget.LinearLayout.LayoutParams params;
		params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		view.setLayoutParams(params);

		for (int i = 0, len = numbers.size(); i < len; i++) {

			LinearLayout ll = new LinearLayout(context);

			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);// ,
			params.setMargins(DisplayUtil.dip2px(context, 8), 0, 0, 0);

			if ("1".equals(mNumberInfo.getName())) {
				view.setPadding(0, DisplayUtil.dip2px(context, 10), 0, 0);

				ll.setPadding(0, 0, 0, DisplayUtil.dip2px(context, 10));// 设置详情展示的号码离底部的距离
			} else {
				view.setPadding(0, DisplayUtil.dip2px(context, 10), 0, DisplayUtil.dip2px(context, 10));
			}

			ll.setLayoutParams(params);
			ll.setOrientation(1);// 设置垂直布局

			int dip = DisplayUtil.dip2px(context, 26);
			params = new LinearLayout.LayoutParams(dip, dip);

			if ("15".equals(mNumberInfo.getName())) {

				// 北京赛车
				params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(context, 20), DisplayUtil.dip2px(context, 24));
				String string = numbers.get(i);

				if ("88".equals(string)) {// 设置没有网络或加载失败的
					// int num = Integer.parseInt(numbers.get(i));
					ImageView iv_car = new ImageView(context);
					iv_car.setLayoutParams(params);
					iv_car.setBackgroundResource(pk10NumberCarNos[i]);
					params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

					if (i == 0) {

						params.setMargins(DisplayUtil.dip2px(context, 12), 0, 0, 0);
					} else {
						params.setMargins(DisplayUtil.dip2px(context, 5), 0, 0, 0);

					}
					ll.setLayoutParams(params);
					ll.addView(iv_car);

				} else {// 加载成功
					int num = Integer.parseInt(numbers.get(i));
					ImageView iv_car = new ImageView(context);
					iv_car.setLayoutParams(params);
					iv_car.setBackgroundResource(AppConstants.numberCarNos[num - 1]);

					params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

					if (i == 0) {

						params.setMargins(DisplayUtil.dip2px(context, 12), 0, 0, 0);
					} else {
						params.setMargins(DisplayUtil.dip2px(context, 5), 0, 0, 0);

					}
					ll.setLayoutParams(params);
					ll.addView(iv_car);
				}

			} else if ("1".equals(mNumberInfo.getName())) {

				// 香港彩

				String string = numbers.get(i);// 给个位数号码前面加上0
				if (string.length() == 1) {
					string = "0" + string;
				}

				if (i == 6) {// 添加"+"号标记
					params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.gravity = Gravity.CENTER;

					ImageView iv = new ImageView(context);
					iv.setLayoutParams(params);
					iv.setImageResource(R.mipmap.number_tiema_icon);
					iv.setPadding(0, DisplayUtil.dip2px(context, 8), 0, 0);

					TextView tv = new TextView(context);

					tv.setText("");
					tv.setGravity(Gravity.CENTER);

					// 去掉+号的左边距，否则小屏手机显示不全
					params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.setMargins(DisplayUtil.dip2px(context, 3), 0, DisplayUtil.dip2px(context, 3), 0);
					ll.setLayoutParams(params);

					ll.addView(iv);
					ll.addView(tv);
				} else {

					if ("88".equals(string)) {// 正在开奖中//isNextNumber==True

						// 显示动态的图片
						TextView tv_number = new TextView(context);
						/*
						 * tv_number.setLayoutParams(params);
						 * tv_number.setGravity(Gravity.CENTER);
						 * tv_number.setBackgroundResource
						 * (R.mipmap.home_lhc_normal);
						 */

						// tv_number.setText("");// 正在开奖中时显示的内容
						params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(context, 26), DisplayUtil.dip2px(context, 26));
						params.setMargins(DisplayUtil.dip2px(context, 4), 0, DisplayUtil.dip2px(context, 4), 0);
						tv_number.setLayoutParams(params);

						tv_number.setGravity(Gravity.CENTER);
						tv_number.setBackgroundResource(R.mipmap.home_lhc_normal);
						// 去掉GIF图的外边距
						params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						params.setMargins(0, 0, 0, 0);
						ll.setLayoutParams(params);

						// if (!isGravity) {// 查看往期详情的时候不设置左边距
						if (i == 0) {
							ll.setPadding(DisplayUtil.dip2px(context, 6), 0, 0, 0);
						} else {
							ll.setPadding(0, 0, 0, 0);
						}
						// }

						// TextView tv = new TextView(context);
						// tv.setText("-");// 正在开奖中时显示的内容
						// tv.setGravity(Gravity.CENTER);
						ll.addView(tv_number);
						// ll.addView(tv);

					} else {
						// 添加号码
						TextView tv_number = new TextView(context);

						/*
						 * if (i == 0) {// 设置第一个的距离
						 * params.setMargins(DisplayUtil.dip2px(context, 2), 0,
						 * 0, 0); tv_number.setLayoutParams(params);
						 * 
						 * } tv_number.setLayoutParams(params);
						 * 
						 * tv_number.setGravity(Gravity.CENTER);
						 * tv_number.setTextColor
						 * (context.getResources().getColor
						 * (R.color.numberinfo_text_color));
						 */
						tv_number.setGravity(Gravity.CENTER);
						tv_number.setTextColor(context.getResources().getColor(R.color.numberinfo_text_color));

						// 设置背景圆球的指定大小
						// tv_number.setLayoutParams(params);
						params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(context, 26), DisplayUtil.dip2px(context, 26));
						params.setMargins(DisplayUtil.dip2px(context, 4), 0, DisplayUtil.dip2px(context, 4), 0);
						tv_number.setLayoutParams(params);

						/*
						 * if (isHKOpenNumberStart) { //
						 * tv_number.setText("");// 正在开奖中时显示的号码和背景
						 * tv_number.setBackgroundResource
						 * (R.mipmap.number_bg_hk_not); } else {
						 */
						tv_number.setText(string);

						switch (Integer.parseInt(numbers.get(i))) {
						case 1:
						case 2:
						case 7:
						case 8:
						case 12:
						case 13:
						case 18:
						case 19:
						case 23:
						case 24:
						case 29:
						case 30:
						case 34:
						case 35:
						case 40:
						case 45:
						case 46:
							tv_number.setBackgroundResource(R.mipmap.number_bg_red);
							break;
						case 3:
						case 4:
						case 9:
						case 10:
						case 14:
						case 15:
						case 20:
						case 25:
						case 26:
						case 31:
						case 36:
						case 37:
						case 41:
						case 42:
						case 47:
						case 48:
							tv_number.setBackgroundResource(R.mipmap.number_bg_blue);
							break;
						case 5:
						case 6:
						case 11:
						case 16:
						case 17:
						case 21:
						case 22:
						case 27:
						case 28:
						case 32:
						case 33:
						case 38:
						case 39:
						case 43:
						case 44:
						case 49:
							tv_number.setBackgroundResource(R.mipmap.number_bg_green);
							break;
						}
						// }
						String zod = "";
						if (0 != zodiacs.size()) {
							if (zodiacs.size() > i) {
								char[] array = zodiacs.get(i).toCharArray();
								switch (array[0]) {
								// 牛、马、羊、鸡、狗、猪
								// 鼠、虎、兔、龙、蛇、猴
								case '牛':
									zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_nu);
									break;
								case '马':
									zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_ma);
									break;
								case '羊':
									zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_ya);
									break;
								case '鸡':
									zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_ji);
									break;
								case '狗':
									zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_gou);
									break;
								case '猪':
									zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_zhu);
									break;
								case '鼠':
									zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_su);
									break;
								case '虎':
									zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_hu);
									break;
								case '兔':
									zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_tu);
									break;
								case '龙':
									zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_long);
									break;
								case '蛇':
									zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_se);
									break;
								case '猴':
									zod = MyApp.getContext().getResources().getString(R.string.number_bjsc_hou);
									break;
								}
							}
						}
						// 添加生肖

						TextView tv_zodiac = new TextView(context);
						/*
						 * if (i == 0) {
						 * tv_zodiac.setPadding(DisplayUtil.dip2px(context, 2),
						 * 0, 0, 0); }
						 * 
						 * tv_zodiac.setText(zod);
						 * tv_zodiac.setGravity(Gravity.CENTER);
						 */
						tv_zodiac.setText(zod);

						// 自动匹配中英文的字体大小
						String zodIndex = "牛马羊鸡狗猪鼠虎兔龙蛇猴龍馬雞豬猴";
						if (zodIndex.indexOf(zod) != -1) {
							tv_zodiac.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);// 中文
						} else {
							tv_zodiac.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);// 英文
						}

						tv_zodiac.setGravity(Gravity.CENTER);
						// 添加颜色
						tv_zodiac.setTextColor(context.getResources().getColor(R.color.msg));

						// 去掉左右外边距
						params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						params.setMargins(0, 0, 0, 0);
						ll.setLayoutParams(params);

						// if (!isGravity) {// 查看往期详情的时候不设置左边距
						if (i == 0) {
							ll.setPadding(DisplayUtil.dip2px(context, 6), 0, 0, 0);
						} else {
							ll.setPadding(0, 0, 0, 0);
						}
						// }

						ll.addView(tv_number);
						ll.addView(tv_zodiac);
					}
				}
			}

			((ViewGroup) view).addView(ll);
		}

	}

	// 设置生肖

}
