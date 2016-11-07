package com.hhly.mlottery.util;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Layout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.NumbersActivity;
import com.hhly.mlottery.bean.numbersBean.NumberCurrentInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hhly.mlottery.R.id.number_ks_ds;
import static com.hhly.mlottery.R.id.number_ks_dx;

/**
 * @author Tenney
 * @des 对号码还有生肖进行拆分动作
 * @ClassName: NumberSubUtils
 * @date 2015-10-22 上午10:22:29
 */
public class NumberDataUtils {

    public static boolean isDynamicNumber = true;// 号码是否正在一个一个号码的的显示

    private TextView tv_number_title;
    private TextView tv_Currentnumber_time;
    private LinearLayout ll_Currentnumber_numbers;
    private LinearLayout ll_gravity_center;
    private LinearLayout ll_number_info_countDown;// 倒计时控件
    private ImageView number_new_icon;// new 字样图片
    // private TextView number_info_isOpenNumber;// 正在开奖...中字样
    private FrameLayout fl_number_hk;
    private FrameLayout fl_number_qxc;
    private FrameLayout fl_number_ssc;
    private FrameLayout fl_number_klsf;
    private FrameLayout fl_number_syxw;
    private FrameLayout fl_number_ks;
    private FrameLayout fl_number_bjsc;
    private FrameLayout fl_number_ssq;
    private FrameLayout fl_number_dlt;
    private FrameLayout fl_number_qlc;
    private FrameLayout fl_number_f3d;
    private View layout_lottery_bonus;
    private TextView tv_lottery_bonus;// 奖金池
    private Context context;
    private View mView;
    private NumberCurrentInfo mNumberInfo;

    private List<String> numbers = new ArrayList<String>();// 存放开奖号码
    private List<String> zodiacs = new ArrayList<String>();// 存放六合彩生肖

    private SwipeRefreshLayout mSwipeRefreshLayout;// 下拉刷新

    private int[] zhis = {1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31};// 1~33所有质数

    /**
     * 拆分开奖号码
     *
     * @param mNumberInfo 彩票对象
     * @param numbers     号码集合
     * @param zodiacs     生肖集合
     */
    public static void disposeSubNumbers(NumberCurrentInfo mNumberInfo, List<String> numbers, List<String> zodiacs) {

        // 拆分开奖号码
        if (TextUtils.isEmpty(mNumberInfo.getNumbers())) {// 如果号码为空
            if ("1".equals(mNumberInfo.getName())) {
                for (int i = 0; i < 8; i++) {
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
                        String[] sxh = {nums2[0], "88", nums2[1]};// 添加"+"号标记

                        for (String string : sxh) {
                            numbers.add(string);
                        }

                    } else {
                        for (String string : nums2) {
                            numbers.add(string);
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
                            String[] sx = {nums4[0], "加", nums4[1]};// 添加"+"号标记

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
                String num;
                if (mNumberInfo.getNumbers().contains("#")) {
                    num = mNumberInfo.getNumbers().replace('#', ',');
                } else {
                    num = mNumberInfo.getNumbers();
                }
                // 46,15,38,39,7,11#25
                String[] nums = num.split(",");

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
     * @param context              上下文
     * @param mNumberInfo          开奖对象
     * @param view                 容器
     * @param numbers              开奖号码集合
     * @param zodiacs              开奖生肖集合
     * @param isHKOpenNumberStart  香港彩是否正在开奖中
     * @param isQXCOpenNumberStart 七星彩是否正在开奖中
     * @param isNextNumber         是否正在开下一期
     * @param isGravity            0：开奖列表 1：当前详情 2：往期详情
     * @param index                快乐十分的红球显示下标
     */
    public static void numberAddInfo(Context context, NumberCurrentInfo mNumberInfo, View view, List<String> numbers, List<String> zodiacs, boolean isHKOpenNumberStart, boolean isQXCOpenNumberStart,
                                     boolean isNextNumber, int isGravity, String index) {

        if (view != null) {
            ((ViewGroup) view).removeAllViews();
        }

        android.widget.LinearLayout.LayoutParams params;
        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        view.setLayoutParams(params);

        for (int i = 0, len = numbers.size(); i < len; i++) {

            LinearLayout ll = new LinearLayout(context);

            params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);// ,
            params.setMargins(DisplayUtil.dip2px(context, 10), 0, 0, 0);

            view.setPadding(0, DisplayUtil.dip2px(context, 10), 0, DisplayUtil.dip2px(context, 10));

            ll.setLayoutParams(params);
            ll.setOrientation(LinearLayout.VERTICAL);// 设置垂直布局

            int dip = DisplayUtil.dip2px(context, 26);
            params = new LinearLayout.LayoutParams(dip, dip);

            if ("15".equals(mNumberInfo.getName())) {
                // 北京赛车
                int num = Integer.parseInt(numbers.get(i));
                ImageView iv_car = new ImageView(context);
                params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(context, 20), DisplayUtil.dip2px(context, 24));

                iv_car.setLayoutParams(params);
                iv_car.setBackgroundResource(AppConstants.numberCarNos[num - 1]);

                params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                if (i == 0) {
                    params.setMargins(DisplayUtil.dip2px(context, 10), 0, 0, 0);
                } else {
                    params.setMargins(DisplayUtil.dip2px(context, 5), 0, 0, 0);
                }
                ll.setLayoutParams(params);
                ll.addView(iv_car);
            } else if ("19".equals(mNumberInfo.getName())) {
                // 重庆幸运农场
                if (isGravity == 1 && isQXCOpenNumberStart) {
                    // 正在开奖中时显示的内容
                    ImageView iv = new ImageView(context);
                    iv.setLayoutParams(params);

//                    iv.setImageResource(R.mipmap.number_anim_klsf_blue);// 蓝球
                    ImageLoader.loadFitCenter(context, R.mipmap.number_anim_klsf_blue, R.mipmap.number_kj_icon_def).into(iv);

                    ll.addView(iv);
                } else {
                    int num = Integer.parseInt(numbers.get(i));
                    ImageView iv_lc = new ImageView(context);
                    iv_lc.setLayoutParams(params);
                    iv_lc.setBackgroundResource(AppConstants.numberXYLCs[num - 1]);
                    ll.addView(iv_lc);
                }

                params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                if (i == 0) {
                    params.setMargins(DisplayUtil.dip2px(context, 10), 0, 0, 0);
                } else {
                    params.setMargins(DisplayUtil.dip2px(context, 8), 0, 0, 0);
                }
                ll.setLayoutParams(params);
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
                    params.setMargins(0, 0, 0, 0);
                    ll.setLayoutParams(params);

                    ll.addView(iv);
                    ll.addView(tv);
                } else {

                    if (i == 7 && !"88".equals(string)) {

                        try {
                            // 获取每个彩种下一期开奖时间
                            long num = DateUtil.getCurrentTime(mNumberInfo.getNextTime()) - Long.parseLong(NumbersActivity.serverTime);

                            if (num > 0 && isHKOpenNumberStart) {

                                isDynamicNumber = false;

                            }
                        } catch (Exception e) {
                            L.d("获取下一期开奖时间失败：" + e.getMessage());
                        }
                    }

                    if (isNextNumber || (isHKOpenNumberStart && "88".equals(string))) {// 正在开奖中

                        // System.out.println("添加动画");

                        isDynamicNumber = true;
                        // 显示动态的gif图
                        ImageView iv = new ImageView(context);
//                        iv.setImageResource(AppConstants.numberHKOpenGIF[i]);
                        ImageLoader.loadFitCenter(context, AppConstants.numberHKOpenGIF[i], R.mipmap.number_kj_icon_def).into(iv);

                        // 设置GIF图的大小和左右的外边距
                        params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(context, 26), DisplayUtil.dip2px(context, 26));
                        params.setMargins(DisplayUtil.dip2px(context, 6), 0, DisplayUtil.dip2px(context, 6), 0);
                        iv.setLayoutParams(params);

                        TextView tv = new TextView(context);
                        tv.setText("");// 正在开奖中时显示的内容
                        tv.setGravity(Gravity.CENTER);

                        // 去掉GIF图的外边距
                        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, 0);
                        ll.setLayoutParams(params);

                        if (i == 0) {
                            ll.setPadding(DisplayUtil.dip2px(context, 4), 0, 0, 0);
                        } else {
                            ll.setPadding(0, 0, 0, 0);
                        }

                        ll.addView(iv);
                        ll.addView(tv);

                    } else {

                        // 添加号码
                        TextView tv_number = new TextView(context);

                        tv_number.setGravity(Gravity.CENTER);
                        tv_number.setTextColor(context.getResources().getColor(R.color.numberinfo_text_color));

                        // 设置背景圆球的指定大小
                        // tv_number.setLayoutParams(params);
                        params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(context, 26), DisplayUtil.dip2px(context, 26));
                        params.setMargins(DisplayUtil.dip2px(context, 6), 0, DisplayUtil.dip2px(context, 6), 0);
                        tv_number.setLayoutParams(params);

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

                        String zod = "?";
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

                        tv_zodiac.setText(zod);

                        // 自动匹配中英文的字体大小
                        if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) {
                            tv_zodiac.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);// 中文
                        } else {
                            tv_zodiac.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);// 英文
                        }

                        tv_zodiac.setGravity(Gravity.CENTER);
                        // 添加颜色
                        tv_zodiac.setTextColor(context.getResources().getColor(R.color.msg));

                        // 去掉左右外边距
                        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, 0);
                        ll.setLayoutParams(params);

                        if (i == 0) {
                            ll.setPadding(DisplayUtil.dip2px(context, 4), 0, 0, 0);
                        } else {
                            ll.setPadding(0, 0, 0, 0);
                        }
                        ll.addView(tv_number);
                        ll.addView(tv_zodiac);
                    }
                }
            } else if ("6".equals(mNumberInfo.getName())) {
                // 七星彩
                if (isQXCOpenNumberStart) {
                    // 正在开奖中时显示的内容
                    ImageView iv = new ImageView(context);
                    iv.setLayoutParams(params);
                    ImageLoader.loadFitCenter(context, AppConstants.numberQXCOpenGIF[i], R.mipmap.number_kj_icon_def).into(iv);
                    ll.addView(iv);
                } else {
                    TextView tv_number = new TextView(context);
                    tv_number.setLayoutParams(params);
                    tv_number.setGravity(Gravity.CENTER);
                    tv_number.setTextColor(context.getResources().getColor(R.color.numberinfo_text_color));
                    tv_number.setText(numbers.get(i));
                    if (i >= 4) {
                        tv_number.setBackgroundResource(R.mipmap.number_bg_blue);
                    } else {
                        tv_number.setBackgroundResource(R.mipmap.number_bg_red);
                    }
                    ll.addView(tv_number);
                }
            } else if ("24".equals(mNumberInfo.getName())) {// 双色球
                if (false) {
                    ImageView iv = new ImageView(context);// 正在开奖中时显示的内容
                    iv.setLayoutParams(params);
                    if (i <= 5) {
                        ImageLoader.loadFitCenter(context, R.mipmap.number_anim_red_ssq, R.mipmap.number_kj_icon_def).into(iv);
                    } else {
                        ImageLoader.loadFitCenter(context, R.mipmap.number_anim_blue_ssq, R.mipmap.number_kj_icon_def).into(iv);
                    }
                    ll.addView(iv);
                } else {
                    TextView tv_number = new TextView(context);
                    tv_number.setLayoutParams(params);
                    tv_number.setGravity(Gravity.CENTER);
                    tv_number.setTextColor(context.getResources().getColor(R.color.numberinfo_text_color));
                    tv_number.setText(numbers.get(i));
                    if (i > 5) {
                        tv_number.setBackgroundResource(R.mipmap.number_bg_blue);
                    } else {
                        tv_number.setBackgroundResource(R.mipmap.number_bg_red);
                    }
                    ll.addView(tv_number);
                }
            } else if ("29".equals(mNumberInfo.getName())) {// 大乐透
                if (false) {
                    ImageView iv = new ImageView(context);// 正在开奖中时显示的内容
                    iv.setLayoutParams(params);
                    if (i <= 4) {
                        ImageLoader.loadFitCenter(context, R.mipmap.number_anim_red_dlt, R.mipmap.number_kj_icon_def).into(iv);
                    } else {
                        ImageLoader.loadFitCenter(context, R.mipmap.number_anim_blue_dlt, R.mipmap.number_kj_icon_def).into(iv);
                    }
                    ll.addView(iv);
                } else {
                    TextView tv_number = new TextView(context);
                    tv_number.setLayoutParams(params);
                    tv_number.setGravity(Gravity.CENTER);
                    tv_number.setTextColor(context.getResources().getColor(R.color.numberinfo_text_color));
                    tv_number.setText(numbers.get(i));
                    if (i > 4) {
                        tv_number.setBackgroundResource(R.mipmap.number_bg_blue);
                    } else {
                        tv_number.setBackgroundResource(R.mipmap.number_bg_red);
                    }
                    ll.addView(tv_number);
                }
            } else if ("28".equals(mNumberInfo.getName())) {// 七乐彩
                if (false) {
                    ImageView iv = new ImageView(context);// 正在开奖中时显示的内容
                    iv.setLayoutParams(params);
                    if (i <= 6) {
                        ImageLoader.loadFitCenter(context, R.mipmap.number_anim_red_qlc, R.mipmap.number_kj_icon_def).into(iv);
                    } else {
                        ImageLoader.loadFitCenter(context, R.mipmap.number_anim_blue_qlc, R.mipmap.number_kj_icon_def).into(iv);
                    }
                    ll.addView(iv);
                } else {
                    TextView tv_number = new TextView(context);
                    tv_number.setLayoutParams(params);
                    tv_number.setGravity(Gravity.CENTER);
                    tv_number.setTextColor(context.getResources().getColor(R.color.numberinfo_text_color));
                    tv_number.setText(numbers.get(i));
                    if (i > 6) {
                        tv_number.setBackgroundResource(R.mipmap.number_bg_blue);
                    } else {
                        tv_number.setBackgroundResource(R.mipmap.number_bg_red);
                    }
                    ll.addView(tv_number);
                }
                params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                if (i == 0) {
                    params.setMargins(DisplayUtil.dip2px(context, 10), 0, 0, 0);
                } else {
                    params.setMargins(DisplayUtil.dip2px(context, 8), 0, 0, 0);
                }
                ll.setLayoutParams(params);
            } else if ("8".equals(mNumberInfo.getName()) || "11".equals(mNumberInfo.getName())) {
                // 广东快乐十分,湖南快乐十分
                if (isGravity == 1 && isQXCOpenNumberStart) {
                    // 正在开奖中时显示的内容
                    ImageView iv = new ImageView(context);
                    iv.setLayoutParams(params);
//                    iv.setImageResource(R.mipmap.number_anim_klsf_blue);// 蓝球
                    ImageLoader.loadFitCenter(context, R.mipmap.number_anim_klsf_blue, R.mipmap.number_kj_icon_def).into(iv);
                    ll.addView(iv);
                } else {
                    TextView tv_number = new TextView(context);

                    tv_number.setLayoutParams(params);
                    tv_number.setGravity(Gravity.CENTER);
                    tv_number.setText(numbers.get(i));
                    tv_number.setTextColor(context.getResources().getColor(R.color.numberinfo_text_color));
                    if (Integer.parseInt(numbers.get(i)) == 19 || Integer.parseInt(numbers.get(i)) == 20) {

                        tv_number.setBackgroundResource(R.mipmap.number_bg_red);
                    } else {

                        tv_number.setBackgroundResource(R.mipmap.number_bg_blue);
                    }
                    ll.addView(tv_number);
                }

                params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                if (i == 0) {
                    params.setMargins(DisplayUtil.dip2px(context, 10), 0, 0, 0);
                } else {
                    params.setMargins(DisplayUtil.dip2px(context, 8), 0, 0, 0);
                }

                ll.setLayoutParams(params);

            } else if ("16".equals(mNumberInfo.getName()) || "10".equals(mNumberInfo.getName()) || "18".equals(mNumberInfo.getName())) {
                // 快三
                try {
                    int num = Integer.parseInt(numbers.get(i));

                    ImageView iv_car = new ImageView(context);

                    iv_car.setLayoutParams(params);
                    iv_car.setBackgroundResource(AppConstants.numberKSNos[num - 1]);

                    ll.addView(iv_car);
                } catch (Exception e) {
                    L.d("快三号码异常：" + e.getMessage());
                }

            } else {
                // 其它号码
                if (isGravity == 1 && isQXCOpenNumberStart) {
                    // 正在开奖中时显示的内容
                    ImageView iv = new ImageView(context);
                    iv.setLayoutParams(params);
//                    iv.setImageResource(AppConstants.numberQXCOpenGIF[0]);
                    ImageLoader.loadFitCenter(context, AppConstants.numberQXCOpenGIF[0], R.mipmap.number_kj_icon_def).into(iv);
                    ll.addView(iv);
                } else {
                    // 添加号码布局
                    TextView tv_number = new TextView(context);

                    tv_number.setLayoutParams(params);
                    tv_number.setGravity(Gravity.CENTER);
                    tv_number.setText(numbers.get(i));
                    tv_number.setTextColor(context.getResources().getColor(R.color.numberinfo_text_color));
                    tv_number.setBackgroundResource(R.mipmap.number_bg_red);

                    ll.addView(tv_number);
                }
            }

            ((ViewGroup) view).addView(ll);

        }
    }

    /**
     * 开奖详情页面
     *
     * @param context
     * @param mView
     * @param mNumberInfo
     * @param isGravity
     * @param isOpenNumberStartHistory
     */
    public void numberHistoryShow(Context context, View mView, NumberCurrentInfo mNumberInfo, int isGravity, boolean isOpenNumberStartHistory, boolean isNextNumber, String index) {
        tv_number_title = (TextView) mView.findViewById(R.id.tv_Currentnumber_title);
        tv_Currentnumber_time = (TextView) mView.findViewById(R.id.tv_Currentnumber_time);// 时间
        ll_Currentnumber_numbers = (LinearLayout) mView.findViewById(R.id.ll_Currentnumber_numbers);// 显示开奖号码容器
        number_new_icon = (ImageView) mView.findViewById(R.id.number_new_icon);// new字样图片
        ll_number_info_countDown = (LinearLayout) mView.findViewById(R.id.ll_number_info_countDown);// 倒计时

        ll_gravity_center = (LinearLayout) mView.findViewById(R.id.ll_gravity_center);
        fl_number_hk = (FrameLayout) mView.findViewById(R.id.fl_number_hk);
        fl_number_qxc = (FrameLayout) mView.findViewById(R.id.fl_number_qxc);
        fl_number_ssc = (FrameLayout) mView.findViewById(R.id.fl_number_ssc);
        fl_number_klsf = (FrameLayout) mView.findViewById(R.id.fl_number_klsf);
        fl_number_syxw = (FrameLayout) mView.findViewById(R.id.fl_number_syxw);
        fl_number_ks = (FrameLayout) mView.findViewById(R.id.fl_number_ks);
        fl_number_bjsc = (FrameLayout) mView.findViewById(R.id.fl_number_bjsc);
        fl_number_ssq = (FrameLayout) mView.findViewById(R.id.fl_number_ssq);
        fl_number_dlt = (FrameLayout) mView.findViewById(R.id.fl_number_dlt);
        fl_number_qlc = (FrameLayout) mView.findViewById(R.id.fl_number_qlc);
        fl_number_f3d = (FrameLayout) mView.findViewById(R.id.fl_number_f3d);
        tv_lottery_bonus = (TextView) mView.findViewById(R.id.tv_lottery_bonus);
        layout_lottery_bonus = mView.findViewById(R.id.layout_lottery_bonus);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.number_current_swiperefreshlayout);// 下拉刷新控件

        this.context = context;
        this.mView = mView;
        this.mNumberInfo = mNumberInfo;

        if (isGravity == 2) {
            mSwipeRefreshLayout.setEnabled(false);// 禁下拉刷新
            ll_gravity_center.setGravity(Gravity.CENTER);// 号码居中显示
            ll_number_info_countDown.setVisibility(View.GONE);// 隐藏倒计时
        } else if (isGravity == 1) {
            ll_gravity_center.setGravity(Gravity.NO_GRAVITY);// 号码不居中显示
            ll_number_info_countDown.setVisibility(View.VISIBLE);// 显示倒计时
        }

        numbers.clear();
        zodiacs.clear();

        disposeSubNumbers(mNumberInfo, numbers, zodiacs);// 拆分开奖号码
        isOpenNumberStartShow(context, mNumberInfo, isOpenNumberStartHistory, isOpenNumberStartHistory, isGravity);// 按开奖状态显示对应的标题
        numberAddInfo(context, mNumberInfo, ll_Currentnumber_numbers, numbers, zodiacs, isOpenNumberStartHistory, isOpenNumberStartHistory, isNextNumber, isGravity, index);// 动态添加数据
        numberAddInfo(isOpenNumberStartHistory);// 详情数据显示
        tv_lottery_bonus.setText(context.getResources().getString(R.string.number_bonus_type) + mNumberInfo.getJackpot());// 设置奖金滚存
    }

    /**
     * 开奖状态显示
     *
     * @param context
     * @param mNumberInfo
     * @param isHKOpenNumberStart
     * @param isQXCOpenNumberStart
     * @param state                详情显示的对应的页面 ,0表示最新当前开奖显示，1表示往期数据的显示
     */
    private void isOpenNumberStartShow(Context context, NumberCurrentInfo mNumberInfo, boolean isHKOpenNumberStart, boolean isQXCOpenNumberStart, int state) {
        if (state == 1) {
            if ("1".equals(mNumberInfo.getName())) {// 香港彩正在开奖状态
                if (isHKOpenNumberStart) {
                    number_new_icon.setVisibility(View.GONE);

                    if (!"88".equals(numbers.get(7))) { // 如果下一期还未开出来，则显示当前和下一期期号
                        String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mNumberInfo.getNextTime()));// 根据日期获取星期
                        String[] Dates = mNumberInfo.getNextTime().split(" ");
                        tv_Currentnumber_time.setText(Dates[0] + " " + weekDate);// 显示日期和周
                        tv_number_title.setText(context.getResources().getString(R.string.number_code_di) + mNumberInfo.getNextIssue() + context.getResources().getString(R.string.number_code_qi));
                    } else {
                        String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mNumberInfo.getTime()));// 根据日期获取星期
                        String[] Dates = mNumberInfo.getTime().split(" ");
                        tv_Currentnumber_time.setText(Dates[0] + " " + weekDate);// 显示日期和周
                        tv_number_title.setText(context.getResources().getString(R.string.number_code_di) + mNumberInfo.getIssue() + context.getResources().getString(R.string.number_code_qi));
                    }

                } else {
                    number_new_icon.setVisibility(View.VISIBLE);

                    String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mNumberInfo.getTime()));// 根据日期获取星期
                    String[] Dates = mNumberInfo.getTime().split(" ");
                    tv_Currentnumber_time.setText(Dates[0] + " " + weekDate);// 显示日期和周
                    tv_number_title.setText(context.getResources().getString(R.string.number_code_di) + mNumberInfo.getIssue() + context.getResources().getString(R.string.number_code_qi));
                }
            } else if ("6".equals(mNumberInfo.getName()) || "24".equals(mNumberInfo.getName()) || "28".equals(mNumberInfo.getName()) || "29".equals(mNumberInfo.getName())) {// 七星彩、双色球、七乐彩、大乐透正在开奖状态
                if (isQXCOpenNumberStart) {
                    number_new_icon.setVisibility(View.GONE);

                    String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mNumberInfo.getNextTime()));// 根据日期获取星期
                    String[] Dates = mNumberInfo.getNextTime().split(" ");
                    tv_Currentnumber_time.setText(Dates[0] + " " + weekDate);// 显示日期和周
                    tv_number_title.setText(context.getResources().getString(R.string.number_code_di) + mNumberInfo.getNextIssue() + context.getResources().getString(R.string.number_code_qi));
                } else {
                    number_new_icon.setVisibility(View.VISIBLE);

                    String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mNumberInfo.getTime()));// 根据日期获取星期
                    String[] Dates = mNumberInfo.getTime().split(" ");
                    tv_Currentnumber_time.setText(Dates[0] + " " + weekDate);// 显示日期和周
                    tv_number_title.setText(context.getResources().getString(R.string.number_code_di) + mNumberInfo.getIssue() + context.getResources().getString(R.string.number_code_qi));
                }
            } else {
                if (isQXCOpenNumberStart) {// 高频彩正在开奖状态
                    number_new_icon.setVisibility(View.GONE);

                    String[] Dates = mNumberInfo.getNextTime().split(" ");
                    String mTime = Dates[1].substring(0, Dates[1].lastIndexOf(":"));
                    tv_Currentnumber_time.setText(mTime);
                    tv_number_title.setText(context.getResources().getString(R.string.number_code_di) + mNumberInfo.getNextIssue() + context.getResources().getString(R.string.number_code_qi));
                } else {
                    number_new_icon.setVisibility(View.VISIBLE);

                    String[] Dates = mNumberInfo.getTime().split(" ");
                    String mTime = Dates[1].substring(0, Dates[1].lastIndexOf(":"));
                    tv_Currentnumber_time.setText(mTime);
                    tv_number_title.setText(context.getResources().getString(R.string.number_code_di) + mNumberInfo.getIssue() + context.getResources().getString(R.string.number_code_qi));
                }
            }
        } else if (state == 2) {
            String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mNumberInfo.getTime()));// 根据日期获取星期
            String[] Dates = mNumberInfo.getTime().split(" ");

            tv_number_title.setText(context.getResources().getString(R.string.number_code_di) + mNumberInfo.getIssue() + context.getResources().getString(R.string.number_code_qi));

            if ("1".equals(mNumberInfo.getName()) || "6".equals(mNumberInfo.getName())) {
                tv_Currentnumber_time.setText(Dates[0] + " " + weekDate);

            } else {
                String mTime = Dates[1].substring(0, Dates[1].lastIndexOf(":"));
                tv_Currentnumber_time.setText(mTime);
            }
        }
    }

    /**
     * 详情数据显示
     */
    private void numberAddInfo(boolean isOpenNumberStartHistory) {

        List<String> numbersInfo = new ArrayList<String>();// 存放开奖号码
        List<String> zodiacsInfo = new ArrayList<String>();// 存放六合彩生肖
        NumberDataUtils.disposeSubNumbers(mNumberInfo, numbersInfo, zodiacsInfo);// 拆分开奖号码

        int num = Integer.parseInt(mNumberInfo.getName());

        switch (num) {
            case 1:// 香港彩
                fl_number_hk.setVisibility(View.VISIBLE);
                fl_number_qxc.setVisibility(View.GONE);
                fl_number_ssc.setVisibility(View.GONE);
                fl_number_klsf.setVisibility(View.GONE);
                fl_number_syxw.setVisibility(View.GONE);
                fl_number_ks.setVisibility(View.GONE);
                fl_number_bjsc.setVisibility(View.GONE);
                fl_number_ssq.setVisibility(View.GONE);
                fl_number_dlt.setVisibility(View.GONE);
                fl_number_qlc.setVisibility(View.GONE);
                fl_number_f3d.setVisibility(View.GONE);
                layout_lottery_bonus.setVisibility(View.GONE);

                if (!isOpenNumberStartHistory) {
                    processingMethodHK(numbersInfo, zodiacsInfo);
                } else {
                    processingMethodHK(null, null);
                }
                break;
            case 6:// 七星彩
                fl_number_hk.setVisibility(View.GONE);
                fl_number_qxc.setVisibility(View.VISIBLE);
                fl_number_ssc.setVisibility(View.GONE);
                fl_number_klsf.setVisibility(View.GONE);
                fl_number_syxw.setVisibility(View.GONE);
                fl_number_ks.setVisibility(View.GONE);
                fl_number_bjsc.setVisibility(View.GONE);
                fl_number_ssq.setVisibility(View.GONE);
                fl_number_dlt.setVisibility(View.GONE);
                fl_number_qlc.setVisibility(View.GONE);
                fl_number_f3d.setVisibility(View.GONE);
                layout_lottery_bonus.setVisibility(View.VISIBLE);

                if (!isOpenNumberStartHistory) {
                    processingMethodQXC(numbersInfo);
                } else {
                    processingMethodQXC(null);
                }
                break;
            case 15:// 北京赛车
                fl_number_hk.setVisibility(View.GONE);
                fl_number_qxc.setVisibility(View.GONE);
                fl_number_ssc.setVisibility(View.GONE);
                fl_number_klsf.setVisibility(View.GONE);
                fl_number_syxw.setVisibility(View.GONE);
                fl_number_ks.setVisibility(View.GONE);
                fl_number_bjsc.setVisibility(View.VISIBLE);
                fl_number_ssq.setVisibility(View.GONE);
                fl_number_dlt.setVisibility(View.GONE);
                fl_number_qlc.setVisibility(View.GONE);
                fl_number_f3d.setVisibility(View.GONE);
                layout_lottery_bonus.setVisibility(View.GONE);

                if (!isOpenNumberStartHistory) {
                    processingMethodBJSC(numbersInfo);
                } else {
                    processingMethodBJSC(null);
                }
                break;
            case 8:// 广东快乐10分
            case 11:// 湖南快乐10分
            case 19:// 幸运农场
                fl_number_hk.setVisibility(View.GONE);
                fl_number_qxc.setVisibility(View.GONE);
                fl_number_ssc.setVisibility(View.GONE);
                fl_number_klsf.setVisibility(View.VISIBLE);
                fl_number_syxw.setVisibility(View.GONE);
                fl_number_ks.setVisibility(View.GONE);
                fl_number_bjsc.setVisibility(View.GONE);
                fl_number_ssq.setVisibility(View.GONE);
                fl_number_dlt.setVisibility(View.GONE);
                fl_number_qlc.setVisibility(View.GONE);
                fl_number_f3d.setVisibility(View.GONE);
                layout_lottery_bonus.setVisibility(View.GONE);

                if (!isOpenNumberStartHistory) {
                    processingMethodKLSF(numbersInfo);
                } else {
                    processingMethodKLSF(null);
                }
                break;
            case 2:// 重庆时时彩
            case 4:// 新疆时时彩
            case 5:// 云南时时彩
            case 23:// 天津时时彩
            case 3:// 江西时时彩
                fl_number_hk.setVisibility(View.GONE);
                fl_number_qxc.setVisibility(View.GONE);
                fl_number_ssc.setVisibility(View.VISIBLE);
                fl_number_klsf.setVisibility(View.GONE);
                fl_number_syxw.setVisibility(View.GONE);
                fl_number_ks.setVisibility(View.GONE);
                fl_number_bjsc.setVisibility(View.GONE);
                fl_number_ssq.setVisibility(View.GONE);
                fl_number_dlt.setVisibility(View.GONE);
                fl_number_qlc.setVisibility(View.GONE);
                fl_number_f3d.setVisibility(View.GONE);
                layout_lottery_bonus.setVisibility(View.GONE);

                if (!isOpenNumberStartHistory) {
                    processingMethodSSC(numbersInfo);
                } else {
                    processingMethodSSC(null);
                }
                break;
            case 7:// 广东11选5
            case 20:// 江苏11选5
            case 22:// 山东11选5
            case 14:// 辽宁11选5
                fl_number_hk.setVisibility(View.GONE);
                fl_number_qxc.setVisibility(View.GONE);
                fl_number_ssc.setVisibility(View.GONE);
                fl_number_klsf.setVisibility(View.GONE);
                fl_number_syxw.setVisibility(View.VISIBLE);
                fl_number_ks.setVisibility(View.GONE);
                fl_number_bjsc.setVisibility(View.GONE);
                fl_number_ssq.setVisibility(View.GONE);
                fl_number_dlt.setVisibility(View.GONE);
                fl_number_qlc.setVisibility(View.GONE);
                fl_number_f3d.setVisibility(View.GONE);
                layout_lottery_bonus.setVisibility(View.GONE);

                if (!isOpenNumberStartHistory) {
                    processingMethodSYXW(numbersInfo);
                } else {
                    processingMethodSYXW(null);
                }
                break;
            case 10:// 安徽快三
            case 16:// 江苏快三
            case 18:// 广西快三
                fl_number_hk.setVisibility(View.GONE);
                fl_number_qxc.setVisibility(View.GONE);
                fl_number_ssc.setVisibility(View.GONE);
                fl_number_klsf.setVisibility(View.GONE);
                fl_number_syxw.setVisibility(View.GONE);
                fl_number_ks.setVisibility(View.VISIBLE);
                fl_number_bjsc.setVisibility(View.GONE);
                fl_number_ssq.setVisibility(View.GONE);
                fl_number_dlt.setVisibility(View.GONE);
                fl_number_qlc.setVisibility(View.GONE);
                fl_number_f3d.setVisibility(View.GONE);
                layout_lottery_bonus.setVisibility(View.GONE);

                if (!isOpenNumberStartHistory) {
                    processingMethodKS(numbersInfo);
                } else {
                    processingMethodKS(null);
                }
                break;
            case 24:// 双色球
                fl_number_hk.setVisibility(View.GONE);
                fl_number_qxc.setVisibility(View.GONE);
                fl_number_ssc.setVisibility(View.GONE);
                fl_number_klsf.setVisibility(View.GONE);
                fl_number_syxw.setVisibility(View.GONE);
                fl_number_ks.setVisibility(View.GONE);
                fl_number_bjsc.setVisibility(View.GONE);
                fl_number_ssq.setVisibility(View.VISIBLE);
                fl_number_dlt.setVisibility(View.GONE);
                fl_number_qlc.setVisibility(View.GONE);
                fl_number_f3d.setVisibility(View.GONE);
                layout_lottery_bonus.setVisibility(View.VISIBLE);

                if (!isOpenNumberStartHistory) {
                    processingMethodSSQ(numbersInfo);
                } else {
                    processingMethodSSQ(null);
                }
                break;
            case 28:// 七乐彩
                fl_number_hk.setVisibility(View.GONE);
                fl_number_qxc.setVisibility(View.GONE);
                fl_number_ssc.setVisibility(View.GONE);
                fl_number_klsf.setVisibility(View.GONE);
                fl_number_syxw.setVisibility(View.GONE);
                fl_number_ks.setVisibility(View.GONE);
                fl_number_bjsc.setVisibility(View.GONE);
                fl_number_ssq.setVisibility(View.GONE);
                fl_number_dlt.setVisibility(View.GONE);
                fl_number_qlc.setVisibility(View.VISIBLE);
                fl_number_f3d.setVisibility(View.GONE);
                layout_lottery_bonus.setVisibility(View.VISIBLE);

                if (!isOpenNumberStartHistory) {
                    processingMethodQLC(numbersInfo);
                } else {
                    processingMethodQLC(null);
                }
                break;
            case 29:// 大乐透
                fl_number_hk.setVisibility(View.GONE);
                fl_number_qxc.setVisibility(View.GONE);
                fl_number_ssc.setVisibility(View.GONE);
                fl_number_klsf.setVisibility(View.GONE);
                fl_number_syxw.setVisibility(View.GONE);
                fl_number_ks.setVisibility(View.GONE);
                fl_number_bjsc.setVisibility(View.GONE);
                fl_number_ssq.setVisibility(View.GONE);
                fl_number_dlt.setVisibility(View.VISIBLE);
                fl_number_qlc.setVisibility(View.GONE);
                fl_number_f3d.setVisibility(View.GONE);
                layout_lottery_bonus.setVisibility(View.VISIBLE);

                if (!isOpenNumberStartHistory) {
                    processingMethodDLT(numbersInfo);
                } else {
                    processingMethodDLT(null);
                }
                break;
            case 25:// 排列3
            case 26:// 排列5
            case 27:// 福彩3D
                fl_number_hk.setVisibility(View.GONE);
                fl_number_qxc.setVisibility(View.GONE);
                fl_number_ssc.setVisibility(View.GONE);
                fl_number_klsf.setVisibility(View.GONE);
                fl_number_syxw.setVisibility(View.GONE);
                fl_number_ks.setVisibility(View.GONE);
                fl_number_bjsc.setVisibility(View.GONE);
                fl_number_ssq.setVisibility(View.GONE);
                fl_number_dlt.setVisibility(View.GONE);
                fl_number_qlc.setVisibility(View.GONE);
                fl_number_f3d.setVisibility(View.VISIBLE);
                layout_lottery_bonus.setVisibility(View.GONE);

                if (!isOpenNumberStartHistory) {
                    processingMethodF3D(numbersInfo);
                } else {
                    processingMethodF3D(null);
                }
                break;
        }
    }

    /**
     * 福彩3D、排列3、排列5
     *
     * @param numbers
     */
    private void processingMethodF3D(List<String> numbers) {
        TextView number_f3d_fz = (TextView) mView.findViewById(R.id.number_f3d_fz);
        TextView number_f3d_dx = (TextView) mView.findViewById(R.id.number_f3d_dx);
        TextView number_f3d_ds = (TextView) mView.findViewById(R.id.number_f3d_ds);
        TextView number_f3d_job = (TextView) mView.findViewById(R.id.number_f3d_job);
        TextView number_f3d_dxb = (TextView) mView.findViewById(R.id.number_f3d_dxb);
        TextView number_f3d_zhb = (TextView) mView.findViewById(R.id.number_f3d_zhb);

        if (null == numbers) {
            number_f3d_fz.setText(context.getResources().getString(R.string.number_info_default));
            number_f3d_dx.setText(context.getResources().getString(R.string.number_info_default));
            number_f3d_ds.setText(context.getResources().getString(R.string.number_info_default));
            number_f3d_job.setText(context.getResources().getString(R.string.number_info_default));
            number_f3d_dxb.setText(context.getResources().getString(R.string.number_info_default));
            number_f3d_zhb.setText(context.getResources().getString(R.string.number_info_default));
        } else {
            int sum = 0; // 和
            int odd = 0; // 奇
            int even = 0;// 偶
            int max = 0;// 大
            int min = 0;// 小
            int zhi = 0;// 质
            int he = 0;// 合
            for (int i = 0; i < numbers.size(); i++) {
                int num = Integer.parseInt(numbers.get(i));
                sum += num;

                if(num == 0){
                    even++;
                } else if (num % 2 == 0) {
                    even++;
                } else {
                    odd++;
                }
                if (num > 4) {
                    max++;
                } else {
                    min++;
                }
                if (Arrays.binarySearch(zhis, num) < 0) {
                    he++;
                } else {
                    zhi++;
                }
            }
            number_f3d_fz.setText(String.valueOf(sum));
            number_f3d_dx.setText(sum > 14 ? context.getResources().getString(R.string.number_bjsc_da):context.getResources().getString(R.string.number_bjsc_xiao));
            if (sum == 0) {
                number_f3d_ds.setText(context.getResources().getString(R.string.number_bjsc_suang));
            } else if (sum % 2 == 0) {
                number_f3d_ds.setText(context.getResources().getString(R.string.number_bjsc_suang));
            } else {
                number_f3d_ds.setText(context.getResources().getString(R.string.number_bjsc_dan));
            }
            number_f3d_job.setText(odd + ":" + even);
            number_f3d_dxb.setText(max + ":" + min);
            number_f3d_zhb.setText(zhi + ":" + he);
        }
    }

    /**
     * 大乐透
     *
     * @param numbers
     */
    private void processingMethodDLT(List<String> numbers) {
        TextView number_dlt_fz = (TextView) mView.findViewById(R.id.number_dlt_fz);
        TextView number_dlt_ds = (TextView) mView.findViewById(R.id.number_dlt_ds);
        TextView number_dlt_kd_r = (TextView) mView.findViewById(R.id.number_dlt_kd_r);
        TextView number_dlt_job_r = (TextView) mView.findViewById(R.id.number_dlt_job_r);
        TextView number_dlt_dxb_r = (TextView) mView.findViewById(R.id.number_dlt_dxb_r);
        TextView number_dlt_zhb_r = (TextView) mView.findViewById(R.id.number_dlt_zhb_r);
        TextView number_dlt_qjb_r = (TextView) mView.findViewById(R.id.number_dlt_qjb_r);
        TextView number_dlt_kd_b = (TextView) mView.findViewById(R.id.number_dlt_kd_b);
        TextView number_dlt_job_b = (TextView) mView.findViewById(R.id.number_dlt_job_b);
        TextView number_dlt_dxb_b = (TextView) mView.findViewById(R.id.number_dlt_dxb_b);
        TextView number_dlt_zhb_b = (TextView) mView.findViewById(R.id.number_dlt_zhb_b);
        TextView number_dlt_qjb_b = (TextView) mView.findViewById(R.id.number_dlt_qjb_b);

        if (null == numbers) {
            number_dlt_fz.setText(context.getResources().getString(R.string.number_info_default));
            number_dlt_ds.setText(context.getResources().getString(R.string.number_info_default));
            number_dlt_kd_r.setText(context.getResources().getString(R.string.number_info_default));
            number_dlt_job_r.setText(context.getResources().getString(R.string.number_info_default));
            number_dlt_dxb_r.setText(context.getResources().getString(R.string.number_info_default));
            number_dlt_zhb_r.setText(context.getResources().getString(R.string.number_info_default));
            number_dlt_qjb_r.setText(context.getResources().getString(R.string.number_info_default));
            number_dlt_kd_b.setText(context.getResources().getString(R.string.number_info_default));
            number_dlt_job_b.setText(context.getResources().getString(R.string.number_info_default));
            number_dlt_dxb_b.setText(context.getResources().getString(R.string.number_info_default));
            number_dlt_zhb_b.setText(context.getResources().getString(R.string.number_info_default));
            number_dlt_qjb_b.setText(context.getResources().getString(R.string.number_info_default));
        } else {
            int sum = 0; // 和
            int odd_r = 0; // 奇
            int odd_b = 0; // 奇
            int even_r = 0;// 偶
            int even_b = 0;// 偶
            int max_r = 0;// 大
            int max_b = 0;// 大
            int min_r = 0;// 小
            int min_b = 0;// 小
            int zhi_r = 0;// 质
            int zhi_b = 0;// 质
            int he_r = 0;// 合
            int he_b = 0;// 合
            int one_r = 0;// 1区
            int one_b = 0;// 1区
            int two_r = 0;// 2区
            int two_b = 0;// 2区
            int three_r = 0;// 3区
            int three_b = 0;// 3区
            for (int i = 0; i < numbers.size(); i++) {
                int num = Integer.parseInt(numbers.get(i));
                sum += num;
                if (i < numbers.size() - 2 && num > 0) {
                    if (num % 2 == 0) {
                        even_r++;
                    } else {
                        odd_r++;
                    }
                    if (num > 17) {
                        max_r++;
                    } else {
                        min_r++;
                    }
                    if (Arrays.binarySearch(zhis, num) < 0) {
                        he_r++;
                    } else {
                        zhi_r++;
                    }
                    if (num <= 12) {
                        one_r++;
                    } else if (num <= 24) {
                        two_r++;
                    } else {
                        three_r++;
                    }
                } else {
                    if (num % 2 == 0) {
                        even_b++;
                    } else {
                        odd_b++;
                    }
                    if (num > 6) {
                        max_b++;
                    } else {
                        min_b++;
                    }
                    if (Arrays.binarySearch(zhis, num) < 0) {
                        he_b++;
                    } else {
                        zhi_b++;
                    }
                    if (num <= 4) {
                        one_b++;
                    } else if (num <= 8) {
                        two_b++;
                    } else {
                        three_b++;
                    }
                }
            }
            number_dlt_fz.setText(String.valueOf(sum));
            if (sum % 2 == 0) {
                number_dlt_ds.setText(context.getResources().getString(R.string.number_bjsc_suang));
            } else {
                number_dlt_ds.setText(context.getResources().getString(R.string.number_bjsc_dan));
            }
            int kd_r = Integer.parseInt(numbers.get(numbers.size() - 3)) - Integer.parseInt(numbers.get(0));
            int kd_b = Integer.parseInt(numbers.get(numbers.size() - 1)) - Integer.parseInt(numbers.get(numbers.size() - 2));
            number_dlt_kd_r.setText(String.valueOf(kd_r));
            number_dlt_kd_b.setText(String.valueOf(kd_b));
            number_dlt_job_r.setText(odd_r + ":" + even_r);
            number_dlt_job_b.setText(odd_b + ":" + even_b);
            number_dlt_dxb_r.setText(max_r + ":" + min_r);
            number_dlt_dxb_b.setText(max_b + ":" + min_b);
            number_dlt_zhb_r.setText(zhi_r + ":" + he_r);
            number_dlt_zhb_b.setText(zhi_b + ":" + he_b);
            number_dlt_qjb_r.setText(one_r + ":" + two_r + ":" + three_r);
            number_dlt_qjb_b.setText(one_b + ":" + two_b + ":" + three_b);
        }
    }

    /**
     * 七乐彩
     *
     * @param numbers
     */
    private void processingMethodQLC(List<String> numbers) {
        TextView number_qlc_fz = (TextView) mView.findViewById(R.id.number_qlc_fz);
        TextView number_qlc_ds = (TextView) mView.findViewById(R.id.number_qlc_ds);
        TextView number_qlc_kd_r = (TextView) mView.findViewById(R.id.number_qlc_kd_r);
        TextView number_qlc_job_r = (TextView) mView.findViewById(R.id.number_qlc_job_r);
        TextView number_qlc_dxb_r = (TextView) mView.findViewById(R.id.number_qlc_dxb_r);
        TextView number_qlc_zhb_r = (TextView) mView.findViewById(R.id.number_qlc_zhb_r);
        TextView number_qlc_qjb_r = (TextView) mView.findViewById(R.id.number_qlc_qjb_r);

        if (null == numbers) {
            number_qlc_fz.setText(context.getResources().getString(R.string.number_info_default));
            number_qlc_ds.setText(context.getResources().getString(R.string.number_info_default));
            number_qlc_kd_r.setText(context.getResources().getString(R.string.number_info_default));
            number_qlc_job_r.setText(context.getResources().getString(R.string.number_info_default));
            number_qlc_dxb_r.setText(context.getResources().getString(R.string.number_info_default));
            number_qlc_zhb_r.setText(context.getResources().getString(R.string.number_info_default));
            number_qlc_qjb_r.setText(context.getResources().getString(R.string.number_info_default));
        } else {
            int sum = 0; // 和
            int odd = 0; // 奇
            int even = 0;// 偶
            int max = 0;// 大
            int min = 0;// 小
            int zhi = 0;// 质
            int he = 0;// 合
            int one = 0;// 1区
            int two = 0;// 2区
            int three = 0;// 3区
            for (int i = 0; i < numbers.size(); i++) {
                int num = Integer.parseInt(numbers.get(i));
                sum += num;
                if (i < numbers.size() - 1 && num > 0) {
                    if (num % 2 == 0) {
                        even++;
                    } else {
                        odd++;
                    }
                    if (num > 15) {
                        max++;
                    } else {
                        min++;
                    }
                    if (Arrays.binarySearch(zhis, num) < 0) {
                        he++;
                    } else {
                        zhi++;
                    }
                    if (num <= 10) {
                        one++;
                    } else if (num <= 20) {
                        two++;
                    } else {
                        three++;
                    }
                }
            }
            number_qlc_fz.setText(String.valueOf(sum));
            if (sum % 2 == 0) {
                number_qlc_ds.setText(context.getResources().getString(R.string.number_bjsc_suang));
            } else {
                number_qlc_ds.setText(context.getResources().getString(R.string.number_bjsc_dan));
            }
            int kd = Integer.parseInt(numbers.get(numbers.size() - 2)) - Integer.parseInt(numbers.get(0));
            number_qlc_kd_r.setText(String.valueOf(kd));
            number_qlc_job_r.setText(odd + ":" + even);
            number_qlc_dxb_r.setText(max + ":" + min);
            number_qlc_zhb_r.setText(zhi + ":" + he);
            number_qlc_qjb_r.setText(one + ":" + two + ":" + three);
        }
    }

    /**
     * 双色球
     *
     * @param numbers
     */
    private void processingMethodSSQ(List<String> numbers) {
        TextView number_ssq_fz = (TextView) mView.findViewById(R.id.number_ssq_fz);
        TextView number_ssq_ds = (TextView) mView.findViewById(R.id.number_ssq_ds);
        TextView number_ssq_kd = (TextView) mView.findViewById(R.id.number_ssq_kd);
        TextView number_ssq_job = (TextView) mView.findViewById(R.id.number_ssq_job);
        TextView number_ssq_dxb = (TextView) mView.findViewById(R.id.number_ssq_dxb);
        TextView number_ssq_zhb = (TextView) mView.findViewById(R.id.number_ssq_zhb);
        TextView number_ssq_qjb = (TextView) mView.findViewById(R.id.number_ssq_qjb);

        if (null == numbers) {
            number_ssq_fz.setText(context.getResources().getString(R.string.number_info_default));
            number_ssq_ds.setText(context.getResources().getString(R.string.number_info_default));
            number_ssq_kd.setText(context.getResources().getString(R.string.number_info_default));
            number_ssq_job.setText(context.getResources().getString(R.string.number_info_default));
            number_ssq_dxb.setText(context.getResources().getString(R.string.number_info_default));
            number_ssq_zhb.setText(context.getResources().getString(R.string.number_info_default));
            number_ssq_qjb.setText(context.getResources().getString(R.string.number_info_default));
        } else {
            int sum = 0; // 和
            int odd = 0; // 奇
            int even = 0;// 偶
            int max = 0;// 大
            int min = 0;// 小
            int zhi = 0;// 质
            int he = 0;// 合
            int one = 0;// 1区
            int two = 0;// 2区
            int three = 0;// 3区
            for (int i = 0; i < numbers.size(); i++) {
                int num = Integer.parseInt(numbers.get(i));
                sum += num;
                if (i < numbers.size() - 1 && num > 0) {
                    if (num % 2 == 0) {
                        even++;
                    } else {
                        odd++;
                    }
                    if (num > 16) {
                        max++;
                    } else {
                        min++;
                    }
                    if (Arrays.binarySearch(zhis, num) < 0) {
                        he++;
                    } else {
                        zhi++;
                    }
                    if (num <= 11) {
                        one++;
                    } else if (num <= 22) {
                        two++;
                    } else {
                        three++;
                    }
                }
            }
            number_ssq_fz.setText(String.valueOf(sum));
            if (sum % 2 == 0) {
                number_ssq_ds.setText(context.getResources().getString(R.string.number_bjsc_suang));
            } else {
                number_ssq_ds.setText(context.getResources().getString(R.string.number_bjsc_dan));
            }
            int kd = Integer.parseInt(numbers.get(numbers.size() - 2)) - Integer.parseInt(numbers.get(0));
            number_ssq_kd.setText(String.valueOf(kd));
            number_ssq_job.setText(odd + ":" + even);
            number_ssq_dxb.setText(max + ":" + min);
            number_ssq_zhb.setText(zhi + ":" + he);
            number_ssq_qjb.setText(one + ":" + two + ":" + three);
        }
    }

    /**
     * 快三
     *
     * @param numbers
     */
    private void processingMethodKS(List<String> numbers) {
        TextView number_ks_ds = (TextView) mView.findViewById(R.id.number_ks_ds);
        TextView number_ks_dx = (TextView) mView.findViewById(R.id.number_ks_dx);
        TextView number_ks_fz = (TextView) mView.findViewById(R.id.number_ks_zfz);

        if (null == numbers) {
            number_ks_ds.setText(context.getResources().getString(R.string.number_info_default));
            number_ks_dx.setText(context.getResources().getString(R.string.number_info_default));
            number_ks_fz.setText(context.getResources().getString(R.string.number_info_default));
        } else {
            int sum = 0;
            for (int i = 0; i < numbers.size(); i++) {
                sum += Integer.parseInt(numbers.get(i));
            }
            number_ks_fz.setText(sum + "");
            if (sum >= 11) {
                number_ks_dx.setText(context.getResources().getString(R.string.number_bjsc_da));
            } else {
                number_ks_dx.setText(context.getResources().getString(R.string.number_bjsc_xiao));
            }
            if (sum % 2 == 0) {
                number_ks_ds.setText(context.getResources().getString(R.string.number_bjsc_suang));
            } else {
                number_ks_ds.setText(context.getResources().getString(R.string.number_bjsc_dan));
            }
        }
    }

    /**
     * 十一选五
     *
     * @param numbers
     */
    private void processingMethodSYXW(List<String> numbers) {
        TextView number_syxw_ds = (TextView) mView.findViewById(R.id.number_syxw_ds);
        TextView number_syxw_dx = (TextView) mView.findViewById(R.id.number_syxw_dx);
        TextView number_syxw_fz = (TextView) mView.findViewById(R.id.number_syxw_fz);
        TextView number_syxw_lh = (TextView) mView.findViewById(R.id.number_syxw_lh);
        TextView number_syxw_ws = (TextView) mView.findViewById(R.id.number_syxw_ws);

        if (null == numbers) {
            number_syxw_ds.setText(context.getResources().getString(R.string.number_info_default));
            number_syxw_dx.setText(context.getResources().getString(R.string.number_info_default));
            number_syxw_fz.setText(context.getResources().getString(R.string.number_info_default));
            number_syxw_lh.setText(context.getResources().getString(R.string.number_info_default));
            number_syxw_ws.setText(context.getResources().getString(R.string.number_info_default));
        } else {

            int sum = 0;
            for (int i = 0; i < numbers.size(); i++) {
                sum += Integer.parseInt(numbers.get(i));
            }
            number_syxw_fz.setText(sum + "");
            if (sum == 30) {
                number_syxw_dx.setText(context.getResources().getString(R.string.number_bjsc_he));
            } else if (sum >= 30) {
                number_syxw_dx.setText(context.getResources().getString(R.string.number_bjsc_da));
            } else {
                number_syxw_dx.setText(context.getResources().getString(R.string.number_bjsc_xiao));
            }
            if (sum % 2 == 0) {
                number_syxw_ds.setText(context.getResources().getString(R.string.number_bjsc_suang));
            } else {
                number_syxw_ds.setText(context.getResources().getString(R.string.number_bjsc_dan));
            }
            int numWS = Integer.parseInt((sum + "").substring((sum + "").length() - 1));// 获取个位数
            if (numWS >= 5) {
                number_syxw_ws.setText(context.getResources().getString(R.string.number_bjsc_da));
            } else {
                number_syxw_ws.setText(context.getResources().getString(R.string.number_bjsc_xiao));
            }
            if (numbers.size() != 0 && numbers.size() > 4) {
                if (Integer.parseInt(numbers.get(0)) > Integer.parseInt(numbers.get(4))) {
                    number_syxw_lh.setText(context.getResources().getString(R.string.number_bjsc_long));
                } else {
                    number_syxw_lh.setText(context.getResources().getString(R.string.number_bjsc_hu));
                }
            }
        }
    }

    /**
     * 时时彩
     *
     * @param numbers
     */
    private void processingMethodSSC(List<String> numbers) {
        TextView number_ssc_fz = (TextView) mView.findViewById(R.id.number_ssc_fz);
        TextView number_ssc_dx = (TextView) mView.findViewById(R.id.number_ssc_dx);
        TextView number_ssc_ds = (TextView) mView.findViewById(R.id.number_ssc_ds);
        TextView number_ssc_lh = (TextView) mView.findViewById(R.id.number_ssc_lh);

        if (null == numbers) {
            number_ssc_fz.setText(context.getResources().getString(R.string.number_info_default));
            number_ssc_dx.setText(context.getResources().getString(R.string.number_info_default));
            number_ssc_ds.setText(context.getResources().getString(R.string.number_info_default));
            number_ssc_lh.setText(context.getResources().getString(R.string.number_info_default));
        } else {
            int sum = 0;
            for (int i = 0; i < numbers.size(); i++) {
                sum += Integer.parseInt(numbers.get(i));
            }
            number_ssc_fz.setText(sum + "");
            if (sum >= 23) {
                number_ssc_dx.setText(context.getResources().getString(R.string.number_bjsc_da));
            } else {
                number_ssc_dx.setText(context.getResources().getString(R.string.number_bjsc_xiao));
            }
            if (sum % 2 == 0) {
                number_ssc_ds.setText(context.getResources().getString(R.string.number_bjsc_suang));
            } else {
                number_ssc_ds.setText(context.getResources().getString(R.string.number_bjsc_dan));
            }
            if (numbers.size() != 0 && numbers.size() > 4) {
                if (Integer.parseInt(numbers.get(0)) == Integer.parseInt(numbers.get(4))) {
                    number_ssc_lh.setText(context.getResources().getString(R.string.number_bjsc_he));
                } else if (Integer.parseInt(numbers.get(0)) > Integer.parseInt(numbers.get(4))) {
                    number_ssc_lh.setText(context.getResources().getString(R.string.number_bjsc_long));
                } else {
                    number_ssc_lh.setText(context.getResources().getString(R.string.number_bjsc_hu));
                }
            }
        }
    }

    /**
     * 快乐十分
     *
     * @param numbers
     */
    private void processingMethodKLSF(List<String> numbers) {
        TextView number_klsf_ds = (TextView) mView.findViewById(R.id.number_klsf_ds);
        TextView number_klsf_dx = (TextView) mView.findViewById(R.id.number_klsf_dx);
        TextView number_klsf_fz = (TextView) mView.findViewById(R.id.number_klsf_fz);
        TextView number_klsf_lh1 = (TextView) mView.findViewById(R.id.number_klsf_lh1);
        TextView number_klsf_lh2 = (TextView) mView.findViewById(R.id.number_klsf_lh2);
        TextView number_klsf_lh3 = (TextView) mView.findViewById(R.id.number_klsf_lh3);
        TextView number_klsf_lh4 = (TextView) mView.findViewById(R.id.number_klsf_lh4);
        TextView number_klsf_ws = (TextView) mView.findViewById(R.id.number_klsf_ws);

        if (null == numbers) {
            number_klsf_ds.setText(context.getResources().getString(R.string.number_info_default));
            number_klsf_dx.setText(context.getResources().getString(R.string.number_info_default));
            number_klsf_fz.setText(context.getResources().getString(R.string.number_info_default));
            number_klsf_lh1.setText(context.getResources().getString(R.string.number_info_default));
            number_klsf_lh2.setText(context.getResources().getString(R.string.number_info_default));
            number_klsf_lh3.setText(context.getResources().getString(R.string.number_info_default));
            number_klsf_lh4.setText(context.getResources().getString(R.string.number_info_default));
            number_klsf_ws.setText(context.getResources().getString(R.string.number_info_default));
        } else {

            int sum = 0;
            for (int i = 0; i < numbers.size(); i++) {
                sum += Integer.parseInt(numbers.get(i));
            }

            number_klsf_fz.setText(sum + "");
            if (sum == 84) {
                number_klsf_dx.setText(context.getResources().getString(R.string.number_bjsc_he));
            } else if (sum >= 85) {
                number_klsf_dx.setText(context.getResources().getString(R.string.number_bjsc_da));
            } else {
                number_klsf_dx.setText(context.getResources().getString(R.string.number_bjsc_xiao));
            }
            if (sum % 2 == 0) {
                number_klsf_ds.setText(context.getResources().getString(R.string.number_bjsc_suang));
            } else {
                number_klsf_ds.setText(context.getResources().getString(R.string.number_bjsc_dan));
            }
            int numWS = Integer.parseInt((sum + "").substring((sum + "").length() - 1));// 获取个位数
            if (numWS >= 5) {
                number_klsf_ws.setText(context.getResources().getString(R.string.number_bjsc_da));
            } else {
                number_klsf_ws.setText(context.getResources().getString(R.string.number_bjsc_xiao));
            }
            if (numbers.size() != 0 && numbers.size() > 7) {
                if (Integer.parseInt(numbers.get(0)) > Integer.parseInt(numbers.get(7))) {
                    number_klsf_lh1.setText(context.getResources().getString(R.string.number_bjsc_long));
                } else {
                    number_klsf_lh1.setText(context.getResources().getString(R.string.number_bjsc_hu));
                }
            }
            if (numbers.size() != 0 && numbers.size() > 6) {
                if (Integer.parseInt(numbers.get(1)) > Integer.parseInt(numbers.get(6))) {
                    number_klsf_lh2.setText(context.getResources().getString(R.string.number_bjsc_long));
                } else {
                    number_klsf_lh2.setText(context.getResources().getString(R.string.number_bjsc_hu));
                }
            }
            if (numbers.size() != 0 && numbers.size() > 5) {
                if (Integer.parseInt(numbers.get(2)) > Integer.parseInt(numbers.get(5))) {
                    number_klsf_lh3.setText(context.getResources().getString(R.string.number_bjsc_long));
                } else {
                    number_klsf_lh3.setText(context.getResources().getString(R.string.number_bjsc_hu));
                }
            }
            if (numbers.size() != 0 && numbers.size() > 4) {
                if (Integer.parseInt(numbers.get(3)) > Integer.parseInt(numbers.get(4))) {
                    number_klsf_lh4.setText(context.getResources().getString(R.string.number_bjsc_long));
                } else {
                    number_klsf_lh4.setText(context.getResources().getString(R.string.number_bjsc_hu));
                }
            }
        }
    }

    /**
     * 北京赛车
     *
     * @param numbers
     */
    private void processingMethodBJSC(List<String> numbers) {
        TextView number_bjsc_ds = (TextView) mView.findViewById(R.id.number_bjsc_ds);
        TextView number_bjsc_dx = (TextView) mView.findViewById(R.id.number_bjsc_dx);
        TextView number_bjsc_fz = (TextView) mView.findViewById(R.id.number_bjsc_fz);
        TextView number_bjsc_lh1 = (TextView) mView.findViewById(R.id.number_bjsc_lh1);
        TextView number_bjsc_lh2 = (TextView) mView.findViewById(R.id.number_bjsc_lh2);
        TextView number_bjsc_lh3 = (TextView) mView.findViewById(R.id.number_bjsc_lh3);
        TextView number_bjsc_lh4 = (TextView) mView.findViewById(R.id.number_bjsc_lh4);
        TextView number_bjsc_lh5 = (TextView) mView.findViewById(R.id.number_bjsc_lh5);

        if (null == numbers) {
            number_bjsc_ds.setText(context.getResources().getString(R.string.number_info_default));
            number_bjsc_dx.setText(context.getResources().getString(R.string.number_info_default));
            number_bjsc_fz.setText(context.getResources().getString(R.string.number_info_default));
            number_bjsc_lh1.setText(context.getResources().getString(R.string.number_info_default));
            number_bjsc_lh2.setText(context.getResources().getString(R.string.number_info_default));
            number_bjsc_lh3.setText(context.getResources().getString(R.string.number_info_default));
            number_bjsc_lh4.setText(context.getResources().getString(R.string.number_info_default));
            number_bjsc_lh5.setText(context.getResources().getString(R.string.number_info_default));
        } else {
            if (numbers.size() != 0 && numbers.size() > 1) {
                int sum = Integer.parseInt(numbers.get(0)) + Integer.parseInt(numbers.get(1));
                number_bjsc_fz.setText(sum + "");
                if (sum % 2 == 0) {
                    number_bjsc_ds.setText(context.getResources().getString(R.string.number_bjsc_suang));
                } else {
                    number_bjsc_ds.setText(context.getResources().getString(R.string.number_bjsc_dan));
                }
                if (sum >= 11) {
                    number_bjsc_dx.setText(context.getResources().getString(R.string.number_bjsc_da));
                } else {
                    number_bjsc_dx.setText(context.getResources().getString(R.string.number_bjsc_xiao));
                }
                if (numbers.size() > 9) {
                    if (Integer.parseInt(numbers.get(0)) > Integer.parseInt(numbers.get(9))) {
                        number_bjsc_lh1.setText(context.getResources().getString(R.string.number_bjsc_long));
                    } else {
                        number_bjsc_lh1.setText(context.getResources().getString(R.string.number_bjsc_hu));
                    }
                }
                if (numbers.size() > 8) {
                    if (Integer.parseInt(numbers.get(1)) > Integer.parseInt(numbers.get(8))) {
                        number_bjsc_lh2.setText(context.getResources().getString(R.string.number_bjsc_long));
                    } else {
                        number_bjsc_lh2.setText(context.getResources().getString(R.string.number_bjsc_hu));
                    }
                }
                if (numbers.size() > 7) {
                    if (Integer.parseInt(numbers.get(2)) > Integer.parseInt(numbers.get(7))) {
                        number_bjsc_lh3.setText(context.getResources().getString(R.string.number_bjsc_long));
                    } else {
                        number_bjsc_lh3.setText(context.getResources().getString(R.string.number_bjsc_hu));
                    }
                }
                if (numbers.size() > 6) {
                    if (Integer.parseInt(numbers.get(3)) > Integer.parseInt(numbers.get(6))) {
                        number_bjsc_lh4.setText(context.getResources().getString(R.string.number_bjsc_long));
                    } else {
                        number_bjsc_lh4.setText(context.getResources().getString(R.string.number_bjsc_hu));
                    }
                }
                if (numbers.size() > 5) {
                    if (Integer.parseInt(numbers.get(4)) > Integer.parseInt(numbers.get(5))) {
                        number_bjsc_lh5.setText(context.getResources().getString(R.string.number_bjsc_long));
                    } else {
                        number_bjsc_lh5.setText(context.getResources().getString(R.string.number_bjsc_hu));
                    }
                }
            }
        }
    }

    /**
     * 七星彩
     *
     * @param numbers
     */
    private void processingMethodQXC(List<String> numbers) {
        TextView number_qxc_dx_1 = (TextView) mView.findViewById(R.id.number_qxc_dx_1);
        TextView number_qxc_dx_2 = (TextView) mView.findViewById(R.id.number_qxc_dx_2);
        TextView number_qxc_dx_3 = (TextView) mView.findViewById(R.id.number_qxc_dx_3);
        TextView number_qxc_dx_4 = (TextView) mView.findViewById(R.id.number_qxc_dx_4);
        TextView number_qxc_dx_5 = (TextView) mView.findViewById(R.id.number_qxc_dx_5);
        TextView number_qxc_dx_6 = (TextView) mView.findViewById(R.id.number_qxc_dx_6);
        TextView number_qxc_dx_7 = (TextView) mView.findViewById(R.id.number_qxc_dx_7);
        TextView number_qxc_ds_1 = (TextView) mView.findViewById(R.id.number_qxc_ds_1);
        TextView number_qxc_ds_2 = (TextView) mView.findViewById(R.id.number_qxc_ds_2);
        TextView number_qxc_ds_3 = (TextView) mView.findViewById(R.id.number_qxc_ds_3);
        TextView number_qxc_ds_4 = (TextView) mView.findViewById(R.id.number_qxc_ds_4);
        TextView number_qxc_ds_5 = (TextView) mView.findViewById(R.id.number_qxc_ds_5);
        TextView number_qxc_ds_6 = (TextView) mView.findViewById(R.id.number_qxc_ds_6);
        TextView number_qxc_ds_7 = (TextView) mView.findViewById(R.id.number_qxc_ds_7);

        if (null == numbers) {
            number_qxc_dx_1.setText(context.getResources().getString(R.string.number_info_default));
            number_qxc_ds_1.setText(context.getResources().getString(R.string.number_info_default));
            number_qxc_dx_2.setText(context.getResources().getString(R.string.number_info_default));
            number_qxc_ds_2.setText(context.getResources().getString(R.string.number_info_default));
            number_qxc_dx_3.setText(context.getResources().getString(R.string.number_info_default));
            number_qxc_ds_3.setText(context.getResources().getString(R.string.number_info_default));
            number_qxc_dx_4.setText(context.getResources().getString(R.string.number_info_default));
            number_qxc_ds_4.setText(context.getResources().getString(R.string.number_info_default));
            number_qxc_dx_5.setText(context.getResources().getString(R.string.number_info_default));
            number_qxc_ds_5.setText(context.getResources().getString(R.string.number_info_default));
            number_qxc_dx_6.setText(context.getResources().getString(R.string.number_info_default));
            number_qxc_ds_6.setText(context.getResources().getString(R.string.number_info_default));
            number_qxc_dx_7.setText(context.getResources().getString(R.string.number_info_default));
            number_qxc_ds_7.setText(context.getResources().getString(R.string.number_info_default));
        } else {

            for (int i = 0; i < numbers.size(); i++) {

                int num = Integer.parseInt(numbers.get(i));

                switch (i) {
                    case 0:

                        if (num >= 5) {
                            number_qxc_dx_1.setText(context.getResources().getString(R.string.number_bjsc_da));
                        } else {
                            number_qxc_dx_1.setText(context.getResources().getString(R.string.number_bjsc_xiao));
                        }

                        if (num % 2 == 0) {
                            number_qxc_ds_1.setText(context.getResources().getString(R.string.number_bjsc_suang));
                        } else {
                            number_qxc_ds_1.setText(context.getResources().getString(R.string.number_bjsc_dan));
                        }

                        break;
                    case 1:

                        if (num >= 5) {
                            number_qxc_dx_2.setText(context.getResources().getString(R.string.number_bjsc_da));
                        } else {
                            number_qxc_dx_2.setText(context.getResources().getString(R.string.number_bjsc_xiao));
                        }

                        if (num % 2 == 0) {
                            number_qxc_ds_2.setText(context.getResources().getString(R.string.number_bjsc_suang));
                        } else {
                            number_qxc_ds_2.setText(context.getResources().getString(R.string.number_bjsc_dan));
                        }
                        break;
                    case 2:
                        if (num >= 5) {
                            number_qxc_dx_3.setText(context.getResources().getString(R.string.number_bjsc_da));
                        } else {
                            number_qxc_dx_3.setText(context.getResources().getString(R.string.number_bjsc_xiao));
                        }

                        if (num % 2 == 0) {
                            number_qxc_ds_3.setText(context.getResources().getString(R.string.number_bjsc_suang));
                        } else {
                            number_qxc_ds_3.setText(context.getResources().getString(R.string.number_bjsc_dan));
                        }
                        break;
                    case 3:
                        if (num >= 5) {
                            number_qxc_dx_4.setText(context.getResources().getString(R.string.number_bjsc_da));
                        } else {
                            number_qxc_dx_4.setText(context.getResources().getString(R.string.number_bjsc_xiao));
                        }

                        if (num % 2 == 0) {
                            number_qxc_ds_4.setText(context.getResources().getString(R.string.number_bjsc_suang));
                        } else {
                            number_qxc_ds_4.setText(context.getResources().getString(R.string.number_bjsc_dan));
                        }
                        break;
                    case 4:
                        if (num >= 5) {
                            number_qxc_dx_5.setText(context.getResources().getString(R.string.number_bjsc_da));
                        } else {
                            number_qxc_dx_5.setText(context.getResources().getString(R.string.number_bjsc_xiao));
                        }

                        if (num % 2 == 0) {
                            number_qxc_ds_5.setText(context.getResources().getString(R.string.number_bjsc_suang));
                        } else {
                            number_qxc_ds_5.setText(context.getResources().getString(R.string.number_bjsc_dan));
                        }
                        break;
                    case 5:
                        if (num >= 5) {
                            number_qxc_dx_6.setText(context.getResources().getString(R.string.number_bjsc_da));
                        } else {
                            number_qxc_dx_6.setText(context.getResources().getString(R.string.number_bjsc_xiao));
                        }

                        if (num % 2 == 0) {
                            number_qxc_ds_6.setText(context.getResources().getString(R.string.number_bjsc_suang));
                        } else {
                            number_qxc_ds_6.setText(context.getResources().getString(R.string.number_bjsc_dan));
                        }
                        break;
                    case 6:
                        if (num >= 5) {
                            number_qxc_dx_7.setText(context.getResources().getString(R.string.number_bjsc_da));
                        } else {
                            number_qxc_dx_7.setText(context.getResources().getString(R.string.number_bjsc_xiao));
                        }

                        if (num % 2 == 0) {
                            number_qxc_ds_7.setText(context.getResources().getString(R.string.number_bjsc_suang));
                        } else {
                            number_qxc_ds_7.setText(context.getResources().getString(R.string.number_bjsc_dan));
                        }
                        break;
                }
            }
        }
    }

    /**
     * 香港彩
     *
     * @param zodiacs
     * @param numbers
     */
    private void processingMethodHK(List<String> numbers, List<String> zodiacs) {
        TextView number_hk__zfds = (TextView) mView.findViewById(R.id.number_hk__zfds);// 总分单双
        TextView number_hk__zfdx = (TextView) mView.findViewById(R.id.number_hk__zfdx);// 总分大小
        TextView number_hk__fz = (TextView) mView.findViewById(R.id.number_hk__fz);// 总分分值
        TextView number_hk_ds = (TextView) mView.findViewById(R.id.number_hk_ds);// 单双
        TextView number_hk_dx = (TextView) mView.findViewById(R.id.number_hk_dx);// 大小
        TextView number_hk_jy = (TextView) mView.findViewById(R.id.number_hk_jy);// 家野
        TextView number_hk_sx = (TextView) mView.findViewById(R.id.number_hk_sx);// 生肖
        TextView number_hk_tm = (TextView) mView.findViewById(R.id.number_hk_tm);// 特码
        TextView number_hk_ys = (TextView) mView.findViewById(R.id.number_hk_ys);// 颜色

        if (null == numbers) {
            number_hk_tm.setText(context.getResources().getString(R.string.number_info_default));
            number_hk_sx.setText(context.getResources().getString(R.string.number_info_default));
            number_hk_jy.setText(context.getResources().getString(R.string.number_info_default));
            number_hk_ds.setText(context.getResources().getString(R.string.number_info_default));
            number_hk_dx.setText(context.getResources().getString(R.string.number_info_default));
            number_hk__fz.setText(context.getResources().getString(R.string.number_info_default));
            number_hk__zfdx.setText(context.getResources().getString(R.string.number_info_default));
            number_hk__zfds.setText(context.getResources().getString(R.string.number_info_default));
            number_hk_ys.setText(context.getResources().getString(R.string.number_info_default));
            number_hk_ys.setTextColor(context.getResources().getColor(R.color.msg));
            number_hk_tm.setTextColor(context.getResources().getColor(R.color.msg));
        } else {

            int number = Integer.parseInt(numbers.get(7));// 特码
            String sx = "";
            if (zodiacs.size() != 0) {
                sx = zodiacs.get(zodiacs.size() - 1);// 生肖
                char[] array = sx.toCharArray();
                switch (array[0]) {
                    // 牛、马、羊、鸡、狗、猪
                    // 鼠、虎、兔、龙、蛇、猴
                    case '牛':
                        sx = MyApp.getContext().getResources().getString(R.string.number_bjsc_nu);
                        break;
                    case '马':
                        sx = MyApp.getContext().getResources().getString(R.string.number_bjsc_ma);
                        break;
                    case '羊':
                        sx = MyApp.getContext().getResources().getString(R.string.number_bjsc_ya);
                        break;
                    case '鸡':
                        sx = MyApp.getContext().getResources().getString(R.string.number_bjsc_ji);
                        break;
                    case '狗':
                        sx = MyApp.getContext().getResources().getString(R.string.number_bjsc_gou);
                        break;
                    case '猪':
                        sx = MyApp.getContext().getResources().getString(R.string.number_bjsc_zhu);
                        break;
                    case '鼠':
                        sx = MyApp.getContext().getResources().getString(R.string.number_bjsc_su);
                        break;
                    case '虎':
                        sx = MyApp.getContext().getResources().getString(R.string.number_bjsc_hu);
                        break;
                    case '兔':
                        sx = MyApp.getContext().getResources().getString(R.string.number_bjsc_tu);
                        break;
                    case '龙':
                        sx = MyApp.getContext().getResources().getString(R.string.number_bjsc_long);
                        break;
                    case '蛇':
                        sx = MyApp.getContext().getResources().getString(R.string.number_bjsc_se);
                        break;
                    case '猴':
                        sx = MyApp.getContext().getResources().getString(R.string.number_bjsc_hou);
                        break;
                }
            }
            String tm = "";
            if (number < 10) {
                tm = "0" + number;
            } else {
                tm = String.valueOf(number);
            }

            number_hk_tm.setText(tm);
            number_hk_sx.setText(sx);

            String jsx = "牛马羊鸡狗猪";
            if (jsx.indexOf(sx) != -1) {
                number_hk_jy.setText(context.getResources().getString(R.string.number_bjsc_jia));
            } else {
                number_hk_jy.setText(context.getResources().getString(R.string.number_bjsc_ye));
            }

            if (number == 49) {
                number_hk_ds.setText(context.getResources().getString(R.string.number_bjsc_he));
            } else if (number % 2 == 0) {
                number_hk_ds.setText(context.getResources().getString(R.string.number_bjsc_suang));
            } else {
                number_hk_ds.setText(context.getResources().getString(R.string.number_bjsc_dan));
            }

            if (number == 49) {
                number_hk_dx.setText(context.getResources().getString(R.string.number_bjsc_he));
            } else if (number >= 25) {
                number_hk_dx.setText(context.getResources().getString(R.string.number_bjsc_da));
            } else {
                number_hk_dx.setText(context.getResources().getString(R.string.number_bjsc_xiao));
            }

            int sum = 0;
            /*
             * for (String num : numbers) { sum += Integer.parseInt(num); }
			 */

            for (int i = 0; i < numbers.size(); i++) {
                if (i != 6) {
                    sum += Integer.parseInt(numbers.get(i));
                }
            }

            number_hk__fz.setText(sum + "");

            if (sum >= 175) {
                number_hk__zfdx.setText(context.getResources().getString(R.string.number_bjsc_da));
            } else {
                number_hk__zfdx.setText(context.getResources().getString(R.string.number_bjsc_xiao));
            }

            if (sum % 2 == 0) {
                number_hk__zfds.setText(context.getResources().getString(R.string.number_bjsc_suang));
            } else {
                number_hk__zfds.setText(context.getResources().getString(R.string.number_bjsc_dan));
            }

            switch (number) {
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
                    number_hk_ys.setText(context.getResources().getString(R.string.number_bjsc_red));
                    number_hk_ys.setTextColor(context.getResources().getColor(R.color.number_red));
                    number_hk_tm.setTextColor(context.getResources().getColor(R.color.number_red));
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
                    number_hk_ys.setText(context.getResources().getString(R.string.number_bjsc_blue));
                    number_hk_ys.setTextColor(context.getResources().getColor(R.color.number_blue));
                    number_hk_tm.setTextColor(context.getResources().getColor(R.color.number_blue));
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
                    number_hk_ys.setText(context.getResources().getString(R.string.number_bjsc_green));
                    number_hk_ys.setTextColor(context.getResources().getColor(R.color.number_green));
                    number_hk_tm.setTextColor(context.getResources().getColor(R.color.number_green));
                    break;
            }
        }
    }

    /**
     * 设置彩种名称
     */
    public static void setTextTitle(Context mContext, TextView tv, String name) {
        switch (name) {
            case "1":// 香港彩
                tv.setText(mContext.getResources().getString(R.string.number_cz_hk));
                break;
            case "6":// 七星彩
                tv.setText(mContext.getResources().getString(R.string.number_cz_qxc));
                break;
            case "15":// 北京赛车
                tv.setText(mContext.getResources().getString(R.string.number_cz_bj_sc));
                break;
            case "8":// 广东快乐十分
                tv.setText(mContext.getResources().getString(R.string.number_cz_gd_klsf));
                break;
            case "11":// 湖南快乐十分
                tv.setText(mContext.getResources().getString(R.string.number_cz_hn_klsf));
                break;
            case "19":// 幸运农场
                tv.setText(mContext.getResources().getString(R.string.number_cz_xylc));
                break;
            case "2":// 重庆时时彩
                tv.setText(mContext.getResources().getString(R.string.number_cz_cq_ssc));
                break;
            case "3":// 江西时时彩
                tv.setText(mContext.getResources().getString(R.string.number_cz_jx_ssc));
                break;
            case "4":// 新疆时时彩
                tv.setText(mContext.getResources().getString(R.string.number_cz_xj_ssc));
                break;
            case "5":// 云南时时彩
                tv.setText(mContext.getResources().getString(R.string.number_cz_yn_ssc));
                break;
            case "23":// 天津时时彩
                tv.setText(mContext.getResources().getString(R.string.number_cz_tj_ssc));
                break;
            case "7":// 广东十一选五
                tv.setText(mContext.getResources().getString(R.string.number_cz_gd_syxw));
                break;
            case "9":// 湖北十一选五
                tv.setText(mContext.getResources().getString(R.string.number_cz_hb_syxw));
                break;
            case "20":// 江苏十一选五
                tv.setText(mContext.getResources().getString(R.string.number_cz_js_syxw));
                break;
            case "21":// 江西十一选五
                tv.setText(mContext.getResources().getString(R.string.number_cz_jx_syxw));
                break;
            case "22":// 山东十一选五
                tv.setText(mContext.getResources().getString(R.string.number_cz_sd_syxw));
                break;
            case "10":// 安徽快3
                tv.setText(mContext.getResources().getString(R.string.number_cz_ah_ks));
                break;
            case "16":// 江苏快3
                tv.setText(mContext.getResources().getString(R.string.number_cz_js_ks));
                break;
            case "18":// 广西快3
                tv.setText(mContext.getResources().getString(R.string.number_cz_gx_ks));
                break;
            case "24":// 双色球
                tv.setText(mContext.getResources().getString(R.string.number_cz_ssq));
                break;
            case "25":// 排列3
                tv.setText(mContext.getResources().getString(R.string.number_cz_pl3));
                break;
            case "26":// 排列5
                tv.setText(mContext.getResources().getString(R.string.number_cz_pl5));
                break;
            case "27":// 福彩3D
                tv.setText(mContext.getResources().getString(R.string.number_cz_f3d));
                break;
            case "28":// 七乐彩
                tv.setText(mContext.getResources().getString(R.string.number_cz_qlc));
                break;
            case "29":// 大乐透
                tv.setText(mContext.getResources().getString(R.string.number_cz_dlt));
                break;
            default:
                tv.setText("-");
                break;
        }
    }
}
