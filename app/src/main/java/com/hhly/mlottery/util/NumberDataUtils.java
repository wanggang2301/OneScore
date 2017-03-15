package com.hhly.mlottery.util;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.NumbersActivity;
import com.hhly.mlottery.bean.numbersBean.NumberCurrentInfo;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

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
    private LinearLayout ll_number_info_content;// 头部详情控件
    private RelativeLayout rl_lottery_switch;// 体育彩种头部左右滑动和期号
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
    private FrameLayout fl_number_pl3;
    private FrameLayout fl_number_pl5;
    private FrameLayout fl_number_sfc;
    private FrameLayout fl_number_lcbqc;
    private FrameLayout fl_number_scjq;
    private Context context;
    private View mView;
    private NumberCurrentInfo mNumberInfo;
    private String mServerTime;

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
                    if ("24".equals(mNumberInfo.getName()) && i == 6) {
                        numbers.add("88");
                    }
                    if ("29".equals(mNumberInfo.getName()) && i == 5) {
                        numbers.add("88");
                    }
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
                                     boolean isSSQOpenNumberStart, boolean isQLCOpenNumberStart, boolean isDLTOpenNumberStart, boolean isNextNumber, int isGravity, String index) {

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

                    GifImageView iv = new GifImageView(context);
                    iv.setLayoutParams(params);
                    iv.setImageResource(R.mipmap.number_anim_klsf_blue);

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

                        GifImageView iv = new GifImageView(context);
                        iv.setImageResource(AppConstants.numberHKOpenGIF[i]);

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

                    GifImageView iv = new GifImageView(context);
                    iv.setLayoutParams(params);
                    iv.setImageResource(AppConstants.numberQXCOpenGIF[i]);

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

                if (i == 6) {
                    params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER;
                    ImageView iv = new ImageView(context);
                    iv.setLayoutParams(params);
                    iv.setImageResource(R.mipmap.number_tiema_icon);
                    iv.setPadding(0, DisplayUtil.dip2px(context, 8), 0, 0);
                    ll.addView(iv);
                } else {

                    if (isSSQOpenNumberStart) {
                        GifImageView iv = new GifImageView(context);
                        iv.setLayoutParams(params);
                        if (i <= 5) {

                            iv.setImageResource(R.mipmap.number_anim_red_ssq);
                        } else {

                            iv.setImageResource(R.mipmap.number_anim_blue_ssq);
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
                }
            } else if ("29".equals(mNumberInfo.getName())) {// 大乐透

                if (i == 5) {
                    params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER;
                    ImageView iv = new ImageView(context);
                    iv.setLayoutParams(params);
                    iv.setImageResource(R.mipmap.number_tiema_icon);
                    iv.setPadding(0, DisplayUtil.dip2px(context, 8), 0, 0);
                    ll.addView(iv);
                } else {

                    if (isDLTOpenNumberStart) {
                        GifImageView iv = new GifImageView(context);
                        iv.setLayoutParams(params);
                        if (i <= 4) {

                            iv.setImageResource(R.mipmap.number_anim_red_dlt);
                        } else {

                            iv.setImageResource(R.mipmap.number_anim_blue_dlt);
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
                }
            } else if ("28".equals(mNumberInfo.getName())) {// 七乐彩
                if (isQLCOpenNumberStart) {
                    GifImageView iv = new GifImageView(context);
                    iv.setLayoutParams(params);
                    if (i <= 6) {

                        iv.setImageResource(R.mipmap.number_anim_red_qlc);
                    } else {

                        iv.setImageResource(R.mipmap.number_anim_blue_qlc);
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
            } else if ("30".equals(mNumberInfo.getName()) || "31".equals(mNumberInfo.getName()) || "32".equals(mNumberInfo.getName())) {// 胜负彩、六场半全场、4场进球

                TextView tv_number = new TextView(context);
                params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(context, 17), DisplayUtil.dip2px(context, 24));
                tv_number.setLayoutParams(params);
                tv_number.setGravity(Gravity.CENTER);
                tv_number.setText(numbers.get(i).equals("null") ? "..." : numbers.get(i));
                tv_number.setTextColor(context.getResources().getColor(R.color.numberinfo_text_color));
                tv_number.setBackgroundResource(R.mipmap.number_bg_jc);
                ll.addView(tv_number);

                params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                if (i == 0) {
                    params.setMargins(DisplayUtil.dip2px(context, 10), 0, 0, 0);
                } else {
                    params.setMargins(DisplayUtil.dip2px(context, 3), 0, 0, 0);
                }
                ll.setLayoutParams(params);
            } else if ("8".equals(mNumberInfo.getName()) || "11".equals(mNumberInfo.getName())) {
                // 广东快乐十分,湖南快乐十分
                if (isGravity == 1 && isQXCOpenNumberStart) {
                    // 正在开奖中时显示的内容
                    GifImageView iv = new GifImageView(context);
                    iv.setLayoutParams(params);
                    iv.setImageResource(R.mipmap.number_anim_klsf_blue);// 蓝球
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
                    GifImageView iv = new GifImageView(context);
                    iv.setLayoutParams(params);
                    iv.setImageResource(AppConstants.numberQXCOpenGIF[0]);
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
     * @param serverTime               服务器时间戳
     */
    public void numberHistoryShow(Context context, View mView, NumberCurrentInfo mNumberInfo, int isGravity, boolean isOpenNumberStartHistory, boolean isNextNumber, String index, String serverTime) {
        tv_number_title = (TextView) mView.findViewById(R.id.tv_Currentnumber_title);
        tv_Currentnumber_time = (TextView) mView.findViewById(R.id.tv_Currentnumber_time);// 时间
        ll_Currentnumber_numbers = (LinearLayout) mView.findViewById(R.id.ll_Currentnumber_numbers);// 显示开奖号码容器
        number_new_icon = (ImageView) mView.findViewById(R.id.number_new_icon);// new字样图片
        ll_number_info_countDown = (LinearLayout) mView.findViewById(R.id.ll_number_info_countDown);// 倒计时
        ll_number_info_content = (LinearLayout) mView.findViewById(R.id.ll_number_info_content);// 头部详情容器
        rl_lottery_switch = (RelativeLayout) mView.findViewById(R.id.rl_lottery_switch);// 体育彩种头部左右滑动和期号
        TextView tv_lottery_issue = (TextView) mView.findViewById(R.id.tv_lottery_issue);// 体育彩种头部左右滑动和期号
        ImageView iv_lottery_left = (ImageView) mView.findViewById(R.id.iv_lottery_left);// 体育彩种头部左右滑动和期号
        ImageView iv_lottery_right = (ImageView) mView.findViewById(R.id.iv_lottery_right);// 体育彩种头部左右滑动和期号

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
        fl_number_pl3 = (FrameLayout) mView.findViewById(R.id.fl_number_pl3);
        fl_number_pl5 = (FrameLayout) mView.findViewById(R.id.fl_number_pl5);
        fl_number_sfc = (FrameLayout) mView.findViewById(R.id.fl_number_sfc);
        fl_number_lcbqc = (FrameLayout) mView.findViewById(R.id.fl_number_lcbqc);
        fl_number_scjq = (FrameLayout) mView.findViewById(R.id.fl_number_scjq);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.number_current_swiperefreshlayout);// 下拉刷新控件

        this.context = context;
        this.mView = mView;
        this.mNumberInfo = mNumberInfo;
        this.mServerTime = serverTime;

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
        if ("30".equals(mNumberInfo.getName()) || "31".equals(mNumberInfo.getName()) || "32".equals(mNumberInfo.getName())) {
            // 胜负彩、六场半全场、4场进球  不显示头部信息
            ll_number_info_content.setVisibility(View.GONE);
            rl_lottery_switch.setVisibility(View.VISIBLE);
            tv_lottery_issue.setText(context.getResources().getString(R.string.number_code_di) + (mNumberInfo.getIssue() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getIssue()) + context.getResources().getString(R.string.number_code_qi));
            if (isGravity == 2) {// 历史详情显示左右按钮
                iv_lottery_left.setVisibility(View.VISIBLE);
                iv_lottery_right.setVisibility(View.VISIBLE);
            } else {
                iv_lottery_left.setVisibility(View.GONE);
                iv_lottery_right.setVisibility(View.GONE);
            }
        } else {
            ll_number_info_content.setVisibility(View.VISIBLE);
            rl_lottery_switch.setVisibility(View.GONE);

            disposeSubNumbers(mNumberInfo, numbers, zodiacs);// 拆分开奖号码
            isOpenNumberStartShow(context, mNumberInfo, isOpenNumberStartHistory, isOpenNumberStartHistory, isGravity);// 按开奖状态显示对应的标题
            numberAddInfo(context, mNumberInfo, ll_Currentnumber_numbers, numbers, zodiacs, isOpenNumberStartHistory, isOpenNumberStartHistory, isOpenNumberStartHistory, isOpenNumberStartHistory, isOpenNumberStartHistory, isNextNumber, isGravity, index);// 动态添加数据
        }
        numberAddInfo(isOpenNumberStartHistory);// 详情数据显示
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
                        tv_Currentnumber_time.setText(DateUtil.convertDateToNation(Dates[0]) + " " + weekDate);// 显示日期和周
                        tv_number_title.setText(context.getResources().getString(R.string.number_code_di) + mNumberInfo.getNextIssue() + context.getResources().getString(R.string.number_code_qi));
                    } else {
                        String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mNumberInfo.getTime()));// 根据日期获取星期
                        String[] Dates = mNumberInfo.getTime().split(" ");
                        tv_Currentnumber_time.setText(DateUtil.convertDateToNation(Dates[0]) + " " + weekDate);// 显示日期和周
                        tv_number_title.setText(context.getResources().getString(R.string.number_code_di) + mNumberInfo.getIssue() + context.getResources().getString(R.string.number_code_qi));
                    }

                } else {
                    number_new_icon.setVisibility(View.VISIBLE);

                    String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mNumberInfo.getTime()));// 根据日期获取星期
                    String[] Dates = mNumberInfo.getTime().split(" ");
                    tv_Currentnumber_time.setText(DateUtil.convertDateToNation(Dates[0]) + " " + weekDate);// 显示日期和周
                    tv_number_title.setText(context.getResources().getString(R.string.number_code_di) + mNumberInfo.getIssue() + context.getResources().getString(R.string.number_code_qi));
                }
            } else if ("6".equals(mNumberInfo.getName()) || "24".equals(mNumberInfo.getName()) || "28".equals(mNumberInfo.getName()) ||
                    "29".equals(mNumberInfo.getName()) || "25".equals(mNumberInfo.getName()) || "26".equals(mNumberInfo.getName())) {
                if (isQXCOpenNumberStart) {
                    number_new_icon.setVisibility(View.GONE);

                    String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mNumberInfo.getNextTime()));// 根据日期获取星期
                    String[] Dates = mNumberInfo.getNextTime().split(" ");
                    tv_Currentnumber_time.setText(DateUtil.convertDateToNation(Dates[0]) + " " + weekDate);// 显示日期和周
                    tv_number_title.setText(context.getResources().getString(R.string.number_code_di) + mNumberInfo.getNextIssue() + context.getResources().getString(R.string.number_code_qi));
                } else {
                    number_new_icon.setVisibility(View.VISIBLE);

                    String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mNumberInfo.getTime()));// 根据日期获取星期
                    String[] Dates = mNumberInfo.getTime().split(" ");
                    tv_Currentnumber_time.setText(DateUtil.convertDateToNation(Dates[0]) + " " + weekDate);// 显示日期和周
                    tv_number_title.setText(context.getResources().getString(R.string.number_code_di) + mNumberInfo.getIssue() + context.getResources().getString(R.string.number_code_qi));
                }
            } else if ("30".equals(mNumberInfo.getName()) || "31".equals(mNumberInfo.getName()) || "32".equals(mNumberInfo.getName())) {
                // 不做设置处理
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

            if ("1".equals(mNumberInfo.getName()) || "6".equals(mNumberInfo.getName()) || "24".equals(mNumberInfo.getName()) || "28".equals(mNumberInfo.getName()) ||
                    "29".equals(mNumberInfo.getName()) || "25".equals(mNumberInfo.getName()) || "26".equals(mNumberInfo.getName())) {
                tv_Currentnumber_time.setText(DateUtil.convertDateToNation(Dates[0]) + " " + weekDate);

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
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.GONE);

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
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.GONE);

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
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.GONE);

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
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.GONE);

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
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.GONE);

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
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.GONE);

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
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.GONE);

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
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.GONE);

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
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.GONE);

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
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.GONE);

                if (!isOpenNumberStartHistory) {
                    processingMethodDLT(numbersInfo);
                } else {
                    processingMethodDLT(null);
                }
                break;
            case 25:// 排列3
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
                fl_number_f3d.setVisibility(View.GONE);
                fl_number_pl3.setVisibility(View.VISIBLE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.GONE);

                if (!isOpenNumberStartHistory) {
                    processingMethodPL3(numbersInfo);
                } else {
                    processingMethodPL3(null);
                }
                break;
            case 26:// 排列5
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
                fl_number_f3d.setVisibility(View.GONE);
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.VISIBLE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.GONE);

                if (!isOpenNumberStartHistory) {
                    processingMethodPL5(numbersInfo);
                } else {
                    processingMethodPL5(null);
                }
                break;
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
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.GONE);

                if (!isOpenNumberStartHistory) {
                    processingMethodF3D(numbersInfo);
                } else {
                    processingMethodF3D(null);
                }
                break;
            case 30:// 胜负彩
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
                fl_number_f3d.setVisibility(View.GONE);
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.VISIBLE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.GONE);

                processingMethodSFC();
                break;
            case 31:// 六场半全场
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
                fl_number_f3d.setVisibility(View.GONE);
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.VISIBLE);
                fl_number_scjq.setVisibility(View.GONE);

                processingMethodLCBQC();
                break;
            case 32:// 4场进球
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
                fl_number_f3d.setVisibility(View.GONE);
                fl_number_pl3.setVisibility(View.GONE);
                fl_number_pl5.setVisibility(View.GONE);
                fl_number_sfc.setVisibility(View.GONE);
                fl_number_lcbqc.setVisibility(View.GONE);
                fl_number_scjq.setVisibility(View.VISIBLE);

                processingMethodSCJQ(numbersInfo);
                break;
        }
    }

    /**
     * 排列3
     *
     * @param numbers
     */
    private void processingMethodPL3(List<String> numbers) {
        TextView lottery_pl3_bonus1 = (TextView) mView.findViewById(R.id.lottery_pl3_bonus1);
        TextView lottery_pl3_bonus2 = (TextView) mView.findViewById(R.id.lottery_pl3_bonus2);
        TextView lottery_pl3_bonus3 = (TextView) mView.findViewById(R.id.lottery_pl3_bonus3);
        TextView lottery_pl3_count1 = (TextView) mView.findViewById(R.id.lottery_pl3_count1);
        TextView lottery_pl3_count2 = (TextView) mView.findViewById(R.id.lottery_pl3_count2);
        TextView lottery_pl3_count3 = (TextView) mView.findViewById(R.id.lottery_pl3_count3);
        TextView lottery_pl3_sales_volume = (TextView) mView.findViewById(R.id.lottery_pl3_sales_volume);

        if (numbers == null) {
            lottery_pl3_bonus1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_pl3_bonus2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_pl3_bonus3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_pl3_count1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_pl3_count2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_pl3_count3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_pl3_sales_volume.setText(context.getResources().getString(R.string.number_info_default));
        } else {
            lottery_pl3_bonus1.setText(mNumberInfo.getDirBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getDirBonus())));
            lottery_pl3_count1.setText(mNumberInfo.getDirCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getDirCount())));
            lottery_pl3_sales_volume.setText(mNumberInfo.getSales() == null ? context.getResources().getString(R.string.number_info_default) : NumberFormat.getCurrencyInstance().format(Long.parseLong(mNumberInfo.getSales())));
            boolean isEquals = false;
            if (numbers != null) {
                String num = numbers.get(0);
                for (int i = 1; i < numbers.size(); i++) {
                    if (num.equals(numbers.get(i))) {
                        isEquals = true;
                    }
                }
            }
            if (isEquals) {
                lottery_pl3_bonus2.setText(mNumberInfo.getGroupBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getGroupBonus())));
                lottery_pl3_count2.setText(mNumberInfo.getGroupCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getGroupCount())));
                lottery_pl3_bonus3.setText(context.getResources().getString(R.string.number_info_default));
                lottery_pl3_count3.setText(context.getResources().getString(R.string.number_info_default));
            } else {
                lottery_pl3_bonus2.setText(context.getResources().getString(R.string.number_info_default));
                lottery_pl3_count2.setText(context.getResources().getString(R.string.number_info_default));
                lottery_pl3_bonus3.setText(mNumberInfo.getGroupBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getGroupBonus())));
                lottery_pl3_count3.setText(mNumberInfo.getGroupCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getGroupCount())));
            }
        }
    }

    /**
     * 排列5
     *
     * @param numbers
     */
    private void processingMethodPL5(List<String> numbers) {
        TextView lottery_pl5_bonus = (TextView) mView.findViewById(R.id.lottery_pl5_bonus);
        TextView lottery_pl5_count = (TextView) mView.findViewById(R.id.lottery_pl5_count);
        TextView lottery_pl5_sales_volume = (TextView) mView.findViewById(R.id.lottery_pl5_sales_volume);
        TextView lottery_pl5_bonus_sum = (TextView) mView.findViewById(R.id.lottery_pl5_bonus_sum);

        if (numbers == null) {
            lottery_pl5_bonus.setText(context.getResources().getString(R.string.number_info_default));
            lottery_pl5_count.setText(context.getResources().getString(R.string.number_info_default));
            lottery_pl5_sales_volume.setText(context.getResources().getString(R.string.number_info_default));
            lottery_pl5_bonus_sum.setText(context.getResources().getString(R.string.number_info_default));
        } else {
            lottery_pl5_bonus.setText(mNumberInfo.getDirBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getDirBonus())));
            lottery_pl5_count.setText(mNumberInfo.getDirCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getDirCount())));
            lottery_pl5_sales_volume.setText(mNumberInfo.getSales() == null ? context.getResources().getString(R.string.number_info_default) : NumberFormat.getCurrencyInstance().format(Long.parseLong(mNumberInfo.getSales())));
            lottery_pl5_bonus_sum.setText(mNumberInfo.getJackpot() == null ? context.getResources().getString(R.string.number_info_default) : NumberFormat.getCurrencyInstance().format(Long.parseLong(mNumberInfo.getJackpot())));
        }
    }

    /**
     * 胜负彩
     */
    private void processingMethodSFC() {
//        TextView lottery_sfc_issue = (TextView) mView.findViewById(R.id.lottery_sfc_issue);
        TextView lottery_sfc_home_name1 = (TextView) mView.findViewById(R.id.lottery_sfc_home_name1);
        TextView lottery_sfc_home_name2 = (TextView) mView.findViewById(R.id.lottery_sfc_home_name2);
        TextView lottery_sfc_home_name3 = (TextView) mView.findViewById(R.id.lottery_sfc_home_name3);
        TextView lottery_sfc_home_name4 = (TextView) mView.findViewById(R.id.lottery_sfc_home_name4);
        TextView lottery_sfc_home_name5 = (TextView) mView.findViewById(R.id.lottery_sfc_home_name5);
        TextView lottery_sfc_home_name6 = (TextView) mView.findViewById(R.id.lottery_sfc_home_name6);
        TextView lottery_sfc_home_name7 = (TextView) mView.findViewById(R.id.lottery_sfc_home_name7);
        TextView lottery_sfc_home_name8 = (TextView) mView.findViewById(R.id.lottery_sfc_home_name8);
        TextView lottery_sfc_home_name9 = (TextView) mView.findViewById(R.id.lottery_sfc_home_name9);
        TextView lottery_sfc_home_name10 = (TextView) mView.findViewById(R.id.lottery_sfc_home_name10);
        TextView lottery_sfc_home_name11 = (TextView) mView.findViewById(R.id.lottery_sfc_home_name11);
        TextView lottery_sfc_home_name12 = (TextView) mView.findViewById(R.id.lottery_sfc_home_name12);
        TextView lottery_sfc_home_name13 = (TextView) mView.findViewById(R.id.lottery_sfc_home_name13);
        TextView lottery_sfc_home_name14 = (TextView) mView.findViewById(R.id.lottery_sfc_home_name14);
        TextView lottery_sfc_vs1 = (TextView) mView.findViewById(R.id.lottery_sfc_vs1);
        TextView lottery_sfc_vs2 = (TextView) mView.findViewById(R.id.lottery_sfc_vs2);
        TextView lottery_sfc_vs3 = (TextView) mView.findViewById(R.id.lottery_sfc_vs3);
        TextView lottery_sfc_vs4 = (TextView) mView.findViewById(R.id.lottery_sfc_vs4);
        TextView lottery_sfc_vs5 = (TextView) mView.findViewById(R.id.lottery_sfc_vs5);
        TextView lottery_sfc_vs6 = (TextView) mView.findViewById(R.id.lottery_sfc_vs6);
        TextView lottery_sfc_vs7 = (TextView) mView.findViewById(R.id.lottery_sfc_vs7);
        TextView lottery_sfc_vs8 = (TextView) mView.findViewById(R.id.lottery_sfc_vs8);
        TextView lottery_sfc_vs9 = (TextView) mView.findViewById(R.id.lottery_sfc_vs9);
        TextView lottery_sfc_vs10 = (TextView) mView.findViewById(R.id.lottery_sfc_vs10);
        TextView lottery_sfc_vs11 = (TextView) mView.findViewById(R.id.lottery_sfc_vs11);
        TextView lottery_sfc_vs12 = (TextView) mView.findViewById(R.id.lottery_sfc_vs12);
        TextView lottery_sfc_vs13 = (TextView) mView.findViewById(R.id.lottery_sfc_vs13);
        TextView lottery_sfc_vs14 = (TextView) mView.findViewById(R.id.lottery_sfc_vs14);
        TextView lottery_sfc_guest_name1 = (TextView) mView.findViewById(R.id.lottery_sfc_guest_name1);
        TextView lottery_sfc_guest_name2 = (TextView) mView.findViewById(R.id.lottery_sfc_guest_name2);
        TextView lottery_sfc_guest_name3 = (TextView) mView.findViewById(R.id.lottery_sfc_guest_name3);
        TextView lottery_sfc_guest_name4 = (TextView) mView.findViewById(R.id.lottery_sfc_guest_name4);
        TextView lottery_sfc_guest_name5 = (TextView) mView.findViewById(R.id.lottery_sfc_guest_name5);
        TextView lottery_sfc_guest_name6 = (TextView) mView.findViewById(R.id.lottery_sfc_guest_name6);
        TextView lottery_sfc_guest_name7 = (TextView) mView.findViewById(R.id.lottery_sfc_guest_name7);
        TextView lottery_sfc_guest_name8 = (TextView) mView.findViewById(R.id.lottery_sfc_guest_name8);
        TextView lottery_sfc_guest_name9 = (TextView) mView.findViewById(R.id.lottery_sfc_guest_name9);
        TextView lottery_sfc_guest_name10 = (TextView) mView.findViewById(R.id.lottery_sfc_guest_name10);
        TextView lottery_sfc_guest_name11 = (TextView) mView.findViewById(R.id.lottery_sfc_guest_name11);
        TextView lottery_sfc_guest_name12 = (TextView) mView.findViewById(R.id.lottery_sfc_guest_name12);
        TextView lottery_sfc_guest_name13 = (TextView) mView.findViewById(R.id.lottery_sfc_guest_name13);
        TextView lottery_sfc_guest_name14 = (TextView) mView.findViewById(R.id.lottery_sfc_guest_name14);
        TextView lottery_sfc_time1 = (TextView) mView.findViewById(R.id.lottery_sfc_time1);
        TextView lottery_sfc_time2 = (TextView) mView.findViewById(R.id.lottery_sfc_time2);
        TextView lottery_sfc_time3 = (TextView) mView.findViewById(R.id.lottery_sfc_time3);
        TextView lottery_sfc_time4 = (TextView) mView.findViewById(R.id.lottery_sfc_time4);
        TextView lottery_sfc_time5 = (TextView) mView.findViewById(R.id.lottery_sfc_time5);
        TextView lottery_sfc_time6 = (TextView) mView.findViewById(R.id.lottery_sfc_time6);
        TextView lottery_sfc_time7 = (TextView) mView.findViewById(R.id.lottery_sfc_time7);
        TextView lottery_sfc_time8 = (TextView) mView.findViewById(R.id.lottery_sfc_time8);
        TextView lottery_sfc_time9 = (TextView) mView.findViewById(R.id.lottery_sfc_time9);
        TextView lottery_sfc_time10 = (TextView) mView.findViewById(R.id.lottery_sfc_time10);
        TextView lottery_sfc_time11 = (TextView) mView.findViewById(R.id.lottery_sfc_time11);
        TextView lottery_sfc_time12 = (TextView) mView.findViewById(R.id.lottery_sfc_time12);
        TextView lottery_sfc_time13 = (TextView) mView.findViewById(R.id.lottery_sfc_time13);
        TextView lottery_sfc_time14 = (TextView) mView.findViewById(R.id.lottery_sfc_time14);
        TextView lottery_sfc_result1 = (TextView) mView.findViewById(R.id.lottery_sfc_result1);
        TextView lottery_sfc_result2 = (TextView) mView.findViewById(R.id.lottery_sfc_result2);
        TextView lottery_sfc_result3 = (TextView) mView.findViewById(R.id.lottery_sfc_result3);
        TextView lottery_sfc_result4 = (TextView) mView.findViewById(R.id.lottery_sfc_result4);
        TextView lottery_sfc_result5 = (TextView) mView.findViewById(R.id.lottery_sfc_result5);
        TextView lottery_sfc_result6 = (TextView) mView.findViewById(R.id.lottery_sfc_result6);
        TextView lottery_sfc_result7 = (TextView) mView.findViewById(R.id.lottery_sfc_result7);
        TextView lottery_sfc_result8 = (TextView) mView.findViewById(R.id.lottery_sfc_result8);
        TextView lottery_sfc_result9 = (TextView) mView.findViewById(R.id.lottery_sfc_result9);
        TextView lottery_sfc_result10 = (TextView) mView.findViewById(R.id.lottery_sfc_result10);
        TextView lottery_sfc_result11 = (TextView) mView.findViewById(R.id.lottery_sfc_result11);
        TextView lottery_sfc_result12 = (TextView) mView.findViewById(R.id.lottery_sfc_result12);
        TextView lottery_sfc_result13 = (TextView) mView.findViewById(R.id.lottery_sfc_result13);
        TextView lottery_sfc_result14 = (TextView) mView.findViewById(R.id.lottery_sfc_result14);
        TextView lottery_sfssc_sales_volume = (TextView) mView.findViewById(R.id.lottery_sfssc_sales_volume);
        TextView lottery_sfc_bonus_sum = (TextView) mView.findViewById(R.id.lottery_sfc_bonus_sum);
        TextView lottery_sfssc_count1 = (TextView) mView.findViewById(R.id.lottery_sfssc_count1);
        TextView lottery_sfssc_count2 = (TextView) mView.findViewById(R.id.lottery_sfssc_count2);
        TextView lottery_sfssc_bouns1 = (TextView) mView.findViewById(R.id.lottery_sfssc_bouns1);
        TextView lottery_sfssc_bouns2 = (TextView) mView.findViewById(R.id.lottery_sfssc_bouns2);
        TextView lottery_rxjc_sales_volume = (TextView) mView.findViewById(R.id.lottery_rxjc_sales_volume);
        TextView lottery_rxjc_count = (TextView) mView.findViewById(R.id.lottery_rxjc_count);
        TextView lottery_rxjc_bouns = (TextView) mView.findViewById(R.id.lottery_rxjc_bouns);

        List<TextView> sfc_home_name_list = new ArrayList<>();
        sfc_home_name_list.add(lottery_sfc_home_name1);
        sfc_home_name_list.add(lottery_sfc_home_name2);
        sfc_home_name_list.add(lottery_sfc_home_name3);
        sfc_home_name_list.add(lottery_sfc_home_name4);
        sfc_home_name_list.add(lottery_sfc_home_name5);
        sfc_home_name_list.add(lottery_sfc_home_name6);
        sfc_home_name_list.add(lottery_sfc_home_name7);
        sfc_home_name_list.add(lottery_sfc_home_name8);
        sfc_home_name_list.add(lottery_sfc_home_name9);
        sfc_home_name_list.add(lottery_sfc_home_name10);
        sfc_home_name_list.add(lottery_sfc_home_name11);
        sfc_home_name_list.add(lottery_sfc_home_name12);
        sfc_home_name_list.add(lottery_sfc_home_name13);
        sfc_home_name_list.add(lottery_sfc_home_name14);
        List<TextView> sfc_vs_list = new ArrayList<>();
        sfc_vs_list.add(lottery_sfc_vs1);
        sfc_vs_list.add(lottery_sfc_vs2);
        sfc_vs_list.add(lottery_sfc_vs3);
        sfc_vs_list.add(lottery_sfc_vs4);
        sfc_vs_list.add(lottery_sfc_vs5);
        sfc_vs_list.add(lottery_sfc_vs6);
        sfc_vs_list.add(lottery_sfc_vs7);
        sfc_vs_list.add(lottery_sfc_vs8);
        sfc_vs_list.add(lottery_sfc_vs9);
        sfc_vs_list.add(lottery_sfc_vs10);
        sfc_vs_list.add(lottery_sfc_vs11);
        sfc_vs_list.add(lottery_sfc_vs12);
        sfc_vs_list.add(lottery_sfc_vs13);
        sfc_vs_list.add(lottery_sfc_vs14);
        List<TextView> sfc_guest_name_list = new ArrayList<>();
        sfc_guest_name_list.add(lottery_sfc_guest_name1);
        sfc_guest_name_list.add(lottery_sfc_guest_name2);
        sfc_guest_name_list.add(lottery_sfc_guest_name3);
        sfc_guest_name_list.add(lottery_sfc_guest_name4);
        sfc_guest_name_list.add(lottery_sfc_guest_name5);
        sfc_guest_name_list.add(lottery_sfc_guest_name6);
        sfc_guest_name_list.add(lottery_sfc_guest_name7);
        sfc_guest_name_list.add(lottery_sfc_guest_name8);
        sfc_guest_name_list.add(lottery_sfc_guest_name9);
        sfc_guest_name_list.add(lottery_sfc_guest_name10);
        sfc_guest_name_list.add(lottery_sfc_guest_name11);
        sfc_guest_name_list.add(lottery_sfc_guest_name12);
        sfc_guest_name_list.add(lottery_sfc_guest_name13);
        sfc_guest_name_list.add(lottery_sfc_guest_name14);
        List<TextView> sfc_time_list = new ArrayList<>();
        sfc_time_list.add(lottery_sfc_time1);
        sfc_time_list.add(lottery_sfc_time2);
        sfc_time_list.add(lottery_sfc_time3);
        sfc_time_list.add(lottery_sfc_time4);
        sfc_time_list.add(lottery_sfc_time5);
        sfc_time_list.add(lottery_sfc_time6);
        sfc_time_list.add(lottery_sfc_time7);
        sfc_time_list.add(lottery_sfc_time8);
        sfc_time_list.add(lottery_sfc_time9);
        sfc_time_list.add(lottery_sfc_time10);
        sfc_time_list.add(lottery_sfc_time11);
        sfc_time_list.add(lottery_sfc_time12);
        sfc_time_list.add(lottery_sfc_time13);
        sfc_time_list.add(lottery_sfc_time14);
        List<TextView> sfc_result_list = new ArrayList<>();
        sfc_result_list.add(lottery_sfc_result1);
        sfc_result_list.add(lottery_sfc_result2);
        sfc_result_list.add(lottery_sfc_result3);
        sfc_result_list.add(lottery_sfc_result4);
        sfc_result_list.add(lottery_sfc_result5);
        sfc_result_list.add(lottery_sfc_result6);
        sfc_result_list.add(lottery_sfc_result7);
        sfc_result_list.add(lottery_sfc_result8);
        sfc_result_list.add(lottery_sfc_result9);
        sfc_result_list.add(lottery_sfc_result10);
        sfc_result_list.add(lottery_sfc_result11);
        sfc_result_list.add(lottery_sfc_result12);
        sfc_result_list.add(lottery_sfc_result13);
        sfc_result_list.add(lottery_sfc_result14);

//        lottery_sfc_issue.setText(context.getResources().getString(R.string.number_code_di) + (mNumberInfo.getIssue() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getIssue()) + context.getResources().getString(R.string.number_code_qi));

        for (int i = 0, len = mNumberInfo.getFootballLotteryIssueResultData() == null ? 0 : mNumberInfo.getFootballLotteryIssueResultData().size(); i < len; i++) {
            // 判断是否开赛时间
//            try {
//                if (mNumberInfo.getFootballLotteryIssueResultData().get(i).getKickOffTime() == null) {
//                    sfc_vs_list.get(i).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
//                    sfc_vs_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//                    sfc_result_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//                } else if (Long.parseLong(mServerTime) > DateUtil.getCurrentTime(mNumberInfo.getFootballLotteryIssueResultData().get(i).getKickOffTime())) {
//                    sfc_vs_list.get(i).setTextColor(context.getResources().getColor(R.color.number_red));
//                    sfc_vs_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore());
//                    sfc_result_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullDrawcode() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullDrawcode());
//                } else {
//                    sfc_vs_list.get(i).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
//                    sfc_vs_list.get(i).setText("VS");
//                    sfc_result_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//                sfc_vs_list.get(i).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
//                sfc_vs_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//            }

            sfc_vs_list.get(i).setTextColor(mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore() == null ? context.getResources().getColor(R.color.content_txt_dark_grad) : context.getResources().getColor(R.color.number_red));
            sfc_vs_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore() == null ? "VS" : mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore());
            sfc_result_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullDrawcode() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullDrawcode());

            sfc_home_name_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getHomeName() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getHomeName());
            sfc_guest_name_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getGuestName() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getGuestName());
            sfc_time_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getKickOffTime() == null ? context.getResources().getString(R.string.number_info_default) : DateUtil.getLotteryInfoDate(mNumberInfo.getFootballLotteryIssueResultData().get(i).getKickOffTime(), "yyyy-MM-dd"));
        }
        lottery_sfssc_sales_volume.setText(mNumberInfo.getFootballFirlottery().getSales() == null ? context.getResources().getString(R.string.number_info_default) : NumberFormat.getCurrencyInstance().format(Long.parseLong(mNumberInfo.getFootballFirlottery().getSales())));
        lottery_sfc_bonus_sum.setText(mNumberInfo.getFootballFirlottery().getJackpot() == null ? context.getResources().getString(R.string.number_info_default) : NumberFormat.getCurrencyInstance().format(Long.parseLong(mNumberInfo.getFootballFirlottery().getJackpot())));
        lottery_sfssc_count1.setText(mNumberInfo.getFootballFirlottery().getFirCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFootballFirlottery().getFirCount())));
        lottery_sfssc_bouns1.setText(mNumberInfo.getFootballFirlottery().getFirSinBon() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFootballFirlottery().getFirSinBon())));
        lottery_sfssc_count2.setText(mNumberInfo.getFootballFirlottery().getSecCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFootballFirlottery().getSecCount())));
        lottery_sfssc_bouns2.setText(mNumberInfo.getFootballFirlottery().getSecSinBon() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFootballFirlottery().getSecSinBon())));
        lottery_rxjc_sales_volume.setText(mNumberInfo.getFootballSecLottery().getRsales() == null ? context.getResources().getString(R.string.number_info_default) : NumberFormat.getCurrencyInstance().format(Long.parseLong(mNumberInfo.getFootballSecLottery().getRsales())));
        lottery_rxjc_count.setText(mNumberInfo.getFootballSecLottery().getRfirCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFootballSecLottery().getRfirCount())));
        lottery_rxjc_bouns.setText(mNumberInfo.getFootballSecLottery().getRfirSinBon() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFootballSecLottery().getRfirSinBon())));
    }

    /**
     * 六场半全场
     */
    private void processingMethodLCBQC() {
//        TextView lottery_lcbqc_issue = (TextView) mView.findViewById(R.id.lottery_lcbqc_issue);
        TextView lottery_lcbqc_home_name1 = (TextView) mView.findViewById(R.id.lottery_lcbqc_home_name1);
        TextView lottery_lcbqc_home_name2 = (TextView) mView.findViewById(R.id.lottery_lcbqc_home_name2);
        TextView lottery_lcbqc_home_name3 = (TextView) mView.findViewById(R.id.lottery_lcbqc_home_name3);
        TextView lottery_lcbqc_home_name4 = (TextView) mView.findViewById(R.id.lottery_lcbqc_home_name4);
        TextView lottery_lcbqc_home_name5 = (TextView) mView.findViewById(R.id.lottery_lcbqc_home_name5);
        TextView lottery_lcbqc_home_name6 = (TextView) mView.findViewById(R.id.lottery_lcbqc_home_name6);
        TextView lottery_lcbqc_half_vs1 = (TextView) mView.findViewById(R.id.lottery_lcbqc_half_vs1);
        TextView lottery_lcbqc_half_vs2 = (TextView) mView.findViewById(R.id.lottery_lcbqc_half_vs2);
        TextView lottery_lcbqc_half_vs3 = (TextView) mView.findViewById(R.id.lottery_lcbqc_half_vs3);
        TextView lottery_lcbqc_half_vs4 = (TextView) mView.findViewById(R.id.lottery_lcbqc_half_vs4);
        TextView lottery_lcbqc_half_vs5 = (TextView) mView.findViewById(R.id.lottery_lcbqc_half_vs5);
        TextView lottery_lcbqc_half_vs6 = (TextView) mView.findViewById(R.id.lottery_lcbqc_half_vs6);
        TextView lottery_lcbqc_full_vs1 = (TextView) mView.findViewById(R.id.lottery_lcbqc_full_vs1);
        TextView lottery_lcbqc_full_vs2 = (TextView) mView.findViewById(R.id.lottery_lcbqc_full_vs2);
        TextView lottery_lcbqc_full_vs3 = (TextView) mView.findViewById(R.id.lottery_lcbqc_full_vs3);
        TextView lottery_lcbqc_full_vs4 = (TextView) mView.findViewById(R.id.lottery_lcbqc_full_vs4);
        TextView lottery_lcbqc_full_vs5 = (TextView) mView.findViewById(R.id.lottery_lcbqc_full_vs5);
        TextView lottery_lcbqc_full_vs6 = (TextView) mView.findViewById(R.id.lottery_lcbqc_full_vs6);
        TextView lottery_lcbqc_guest_name1 = (TextView) mView.findViewById(R.id.lottery_lcbqc_guest_name1);
        TextView lottery_lcbqc_guest_name2 = (TextView) mView.findViewById(R.id.lottery_lcbqc_guest_name2);
        TextView lottery_lcbqc_guest_name3 = (TextView) mView.findViewById(R.id.lottery_lcbqc_guest_name3);
        TextView lottery_lcbqc_guest_name4 = (TextView) mView.findViewById(R.id.lottery_lcbqc_guest_name4);
        TextView lottery_lcbqc_guest_name5 = (TextView) mView.findViewById(R.id.lottery_lcbqc_guest_name5);
        TextView lottery_lcbqc_guest_name6 = (TextView) mView.findViewById(R.id.lottery_lcbqc_guest_name6);
        TextView lottery_lcbqc_time1 = (TextView) mView.findViewById(R.id.lottery_lcbqc_time1);
        TextView lottery_lcbqc_time2 = (TextView) mView.findViewById(R.id.lottery_lcbqc_time2);
        TextView lottery_lcbqc_time3 = (TextView) mView.findViewById(R.id.lottery_lcbqc_time3);
        TextView lottery_lcbqc_time4 = (TextView) mView.findViewById(R.id.lottery_lcbqc_time4);
        TextView lottery_lcbqc_time5 = (TextView) mView.findViewById(R.id.lottery_lcbqc_time5);
        TextView lottery_lcbqc_time6 = (TextView) mView.findViewById(R.id.lottery_lcbqc_time6);
        TextView lottery_lcbqc_half1 = (TextView) mView.findViewById(R.id.lottery_lcbqc_half1);
        TextView lottery_lcbqc_half2 = (TextView) mView.findViewById(R.id.lottery_lcbqc_half2);
        TextView lottery_lcbqc_half3 = (TextView) mView.findViewById(R.id.lottery_lcbqc_half3);
        TextView lottery_lcbqc_half4 = (TextView) mView.findViewById(R.id.lottery_lcbqc_half4);
        TextView lottery_lcbqc_half5 = (TextView) mView.findViewById(R.id.lottery_lcbqc_half5);
        TextView lottery_lcbqc_half6 = (TextView) mView.findViewById(R.id.lottery_lcbqc_half6);
        TextView lottery_lcbqc_full1 = (TextView) mView.findViewById(R.id.lottery_lcbqc_full1);
        TextView lottery_lcbqc_full2 = (TextView) mView.findViewById(R.id.lottery_lcbqc_full2);
        TextView lottery_lcbqc_full3 = (TextView) mView.findViewById(R.id.lottery_lcbqc_full3);
        TextView lottery_lcbqc_full4 = (TextView) mView.findViewById(R.id.lottery_lcbqc_full4);
        TextView lottery_lcbqc_full5 = (TextView) mView.findViewById(R.id.lottery_lcbqc_full5);
        TextView lottery_lcbqc_full6 = (TextView) mView.findViewById(R.id.lottery_lcbqc_full6);
        TextView lottery_lcbqc_sales_volume = (TextView) mView.findViewById(R.id.lottery_lcbqc_sales_volume);
        TextView lottery_lcbqc_bonus_sum = (TextView) mView.findViewById(R.id.lottery_lcbqc_bonus_sum);
        TextView lottery_lcbqc_count = (TextView) mView.findViewById(R.id.lottery_lcbqc_count);
        TextView lottery_lcbqc_bonus = (TextView) mView.findViewById(R.id.lottery_lcbqc_bonus);

        List<TextView> lcbqc_home_list = new ArrayList<>();
        lcbqc_home_list.add(lottery_lcbqc_home_name1);
        lcbqc_home_list.add(lottery_lcbqc_home_name2);
        lcbqc_home_list.add(lottery_lcbqc_home_name3);
        lcbqc_home_list.add(lottery_lcbqc_home_name4);
        lcbqc_home_list.add(lottery_lcbqc_home_name5);
        lcbqc_home_list.add(lottery_lcbqc_home_name6);
        List<TextView> lcbqc_half_vs_list = new ArrayList<>();
        lcbqc_half_vs_list.add(lottery_lcbqc_half_vs1);
        lcbqc_half_vs_list.add(lottery_lcbqc_half_vs2);
        lcbqc_half_vs_list.add(lottery_lcbqc_half_vs3);
        lcbqc_half_vs_list.add(lottery_lcbqc_half_vs4);
        lcbqc_half_vs_list.add(lottery_lcbqc_half_vs5);
        lcbqc_half_vs_list.add(lottery_lcbqc_half_vs6);
        List<TextView> lcbqc_full_vs_list = new ArrayList<>();
        lcbqc_full_vs_list.add(lottery_lcbqc_full_vs1);
        lcbqc_full_vs_list.add(lottery_lcbqc_full_vs2);
        lcbqc_full_vs_list.add(lottery_lcbqc_full_vs3);
        lcbqc_full_vs_list.add(lottery_lcbqc_full_vs4);
        lcbqc_full_vs_list.add(lottery_lcbqc_full_vs5);
        lcbqc_full_vs_list.add(lottery_lcbqc_full_vs6);
        List<TextView> lcbqc_guest_list = new ArrayList<>();
        lcbqc_guest_list.add(lottery_lcbqc_guest_name1);
        lcbqc_guest_list.add(lottery_lcbqc_guest_name2);
        lcbqc_guest_list.add(lottery_lcbqc_guest_name3);
        lcbqc_guest_list.add(lottery_lcbqc_guest_name4);
        lcbqc_guest_list.add(lottery_lcbqc_guest_name5);
        lcbqc_guest_list.add(lottery_lcbqc_guest_name6);
        List<TextView> lcbqc_time_list = new ArrayList<>();
        lcbqc_time_list.add(lottery_lcbqc_time1);
        lcbqc_time_list.add(lottery_lcbqc_time2);
        lcbqc_time_list.add(lottery_lcbqc_time3);
        lcbqc_time_list.add(lottery_lcbqc_time4);
        lcbqc_time_list.add(lottery_lcbqc_time5);
        lcbqc_time_list.add(lottery_lcbqc_time6);
        List<TextView> lcbqc_half_list = new ArrayList<>();
        lcbqc_half_list.add(lottery_lcbqc_half1);
        lcbqc_half_list.add(lottery_lcbqc_half2);
        lcbqc_half_list.add(lottery_lcbqc_half3);
        lcbqc_half_list.add(lottery_lcbqc_half4);
        lcbqc_half_list.add(lottery_lcbqc_half5);
        lcbqc_half_list.add(lottery_lcbqc_half6);
        List<TextView> lcbqc_full_list = new ArrayList<>();
        lcbqc_full_list.add(lottery_lcbqc_full1);
        lcbqc_full_list.add(lottery_lcbqc_full2);
        lcbqc_full_list.add(lottery_lcbqc_full3);
        lcbqc_full_list.add(lottery_lcbqc_full4);
        lcbqc_full_list.add(lottery_lcbqc_full5);
        lcbqc_full_list.add(lottery_lcbqc_full6);

//        lottery_lcbqc_issue.setText(context.getResources().getString(R.string.number_code_di) + (mNumberInfo.getIssue() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getIssue()) + context.getResources().getString(R.string.number_code_qi));

        for (int i = 0, len = mNumberInfo.getFootballLotteryIssueResultData() == null ? 0 : mNumberInfo.getFootballLotteryIssueResultData().size(); i < len; i++) {
            // 判断是否开赛时间
//            try {
//                if (mNumberInfo.getFootballLotteryIssueResultData().get(i).getKickOffTime() == null) {
//                    lcbqc_full_vs_list.get(i).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
//                    lcbqc_full_vs_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//                    lcbqc_half_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//                    lcbqc_full_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//                } else if (Long.parseLong(mServerTime) > DateUtil.getCurrentTime(mNumberInfo.getFootballLotteryIssueResultData().get(i).getKickOffTime())) {
//                    lcbqc_half_vs_list.get(i).setTextColor(context.getResources().getColor(R.color.number_red));
//                    lcbqc_half_vs_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getHalfScore() == null ? "(--)" : "(" + mNumberInfo.getFootballLotteryIssueResultData().get(i).getHalfScore() + ")");
//                    lcbqc_full_vs_list.get(i).setTextColor(context.getResources().getColor(R.color.number_red));
//                    lcbqc_full_vs_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore());
//                    lcbqc_half_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getHalfDrawcode() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getHalfDrawcode());
//                    lcbqc_full_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullDrawcode() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullDrawcode());
//                } else {
//                    lcbqc_half_vs_list.get(i).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
//                    lcbqc_full_vs_list.get(i).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
//                    lcbqc_half_vs_list.get(i).setText("");
//                    lcbqc_full_vs_list.get(i).setText("VS");
//                    lcbqc_half_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//                    lcbqc_full_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//                lcbqc_full_vs_list.get(i).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
//                lcbqc_full_vs_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//            }

            lcbqc_half_vs_list.get(i).setTextColor(mNumberInfo.getFootballLotteryIssueResultData().get(i).getHalfScore() == null ? context.getResources().getColor(R.color.content_txt_dark_grad) : context.getResources().getColor(R.color.number_red));
            lcbqc_half_vs_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getHalfScore() == null ? "" : "(" + mNumberInfo.getFootballLotteryIssueResultData().get(i).getHalfScore() + ")");
            lcbqc_full_vs_list.get(i).setTextColor(mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore() == null ? context.getResources().getColor(R.color.content_txt_dark_grad) : context.getResources().getColor(R.color.number_red));
            lcbqc_full_vs_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore() == null ? "VS" : mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore());
            lcbqc_half_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getHalfDrawcode() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getHalfDrawcode());
            lcbqc_full_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullDrawcode() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullDrawcode());

            lcbqc_home_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getHomeName() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getHomeName());
            lcbqc_guest_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getGuestName() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getGuestName());
            lcbqc_time_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getKickOffTime() == null ? context.getResources().getString(R.string.number_info_default) : DateUtil.getLotteryInfoDate(mNumberInfo.getFootballLotteryIssueResultData().get(i).getKickOffTime(), "yyyy-MM-dd"));

        }
        lottery_lcbqc_sales_volume.setText(mNumberInfo.getFootballFirlottery().getSales() == null ? context.getResources().getString(R.string.number_info_default) : NumberFormat.getCurrencyInstance().format(Long.parseLong(mNumberInfo.getFootballFirlottery().getSales())));
        lottery_lcbqc_bonus_sum.setText(mNumberInfo.getFootballFirlottery().getJackpot() == null ? context.getResources().getString(R.string.number_info_default) : NumberFormat.getCurrencyInstance().format(Long.parseLong(mNumberInfo.getFootballFirlottery().getJackpot())));
        lottery_lcbqc_count.setText(mNumberInfo.getFootballFirlottery().getFirCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFootballFirlottery().getFirCount())));
        lottery_lcbqc_bonus.setText(mNumberInfo.getFootballFirlottery().getFirSinBon() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFootballFirlottery().getFirSinBon())));
    }

    /**
     * 4场进球
     */
    private void processingMethodSCJQ(List<String> numbers) {
//        TextView lottery_scjq_issue = (TextView) mView.findViewById(R.id.lottery_scjq_issue);
        TextView lottery_scjq_home_name1 = (TextView) mView.findViewById(R.id.lottery_scjq_home_name1);
        TextView lottery_scjq_home_name2 = (TextView) mView.findViewById(R.id.lottery_scjq_home_name2);
        TextView lottery_scjq_home_name3 = (TextView) mView.findViewById(R.id.lottery_scjq_home_name3);
        TextView lottery_scjq_home_name4 = (TextView) mView.findViewById(R.id.lottery_scjq_home_name4);
        TextView lottery_scjq_vs1 = (TextView) mView.findViewById(R.id.lottery_scjq_vs1);
        TextView lottery_scjq_vs2 = (TextView) mView.findViewById(R.id.lottery_scjq_vs2);
        TextView lottery_scjq_vs3 = (TextView) mView.findViewById(R.id.lottery_scjq_vs3);
        TextView lottery_scjq_vs4 = (TextView) mView.findViewById(R.id.lottery_scjq_vs4);
        TextView lottery_scjq_half_result1 = (TextView) mView.findViewById(R.id.lottery_scjq_half_result1);
        TextView lottery_scjq_half_result2 = (TextView) mView.findViewById(R.id.lottery_scjq_half_result2);
        TextView lottery_scjq_half_result3 = (TextView) mView.findViewById(R.id.lottery_scjq_half_result3);
        TextView lottery_scjq_half_result4 = (TextView) mView.findViewById(R.id.lottery_scjq_half_result4);
        TextView lottery_scjq_full_result1 = (TextView) mView.findViewById(R.id.lottery_scjq_full_result1);
        TextView lottery_scjq_full_result2 = (TextView) mView.findViewById(R.id.lottery_scjq_full_result2);
        TextView lottery_scjq_full_result3 = (TextView) mView.findViewById(R.id.lottery_scjq_full_result3);
        TextView lottery_scjq_full_result4 = (TextView) mView.findViewById(R.id.lottery_scjq_full_result4);
        TextView lottery_scjq_guest_name1 = (TextView) mView.findViewById(R.id.lottery_scjq_guest_name1);
        TextView lottery_scjq_guest_name2 = (TextView) mView.findViewById(R.id.lottery_scjq_guest_name2);
        TextView lottery_scjq_guest_name3 = (TextView) mView.findViewById(R.id.lottery_scjq_guest_name3);
        TextView lottery_scjq_guest_name4 = (TextView) mView.findViewById(R.id.lottery_scjq_guest_name4);
        TextView lottery_scjq_time1 = (TextView) mView.findViewById(R.id.lottery_scjq_time1);
        TextView lottery_scjq_time2 = (TextView) mView.findViewById(R.id.lottery_scjq_time2);
        TextView lottery_scjq_time3 = (TextView) mView.findViewById(R.id.lottery_scjq_time3);
        TextView lottery_scjq_time4 = (TextView) mView.findViewById(R.id.lottery_scjq_time4);
        TextView lottery_scjq_sales_volume = (TextView) mView.findViewById(R.id.lottery_scjq_sales_volume);
        TextView lottery_scjq_bonus_sum = (TextView) mView.findViewById(R.id.lottery_scjq_bonus_sum);
        TextView lottery_scjq_count = (TextView) mView.findViewById(R.id.lottery_scjq_count);
        TextView lottery_scjq_bonus = (TextView) mView.findViewById(R.id.lottery_scjq_bonus);

        List<TextView> scjq_home_list = new ArrayList<>();
        scjq_home_list.add(lottery_scjq_home_name1);
        scjq_home_list.add(lottery_scjq_home_name2);
        scjq_home_list.add(lottery_scjq_home_name3);
        scjq_home_list.add(lottery_scjq_home_name4);
        List<TextView> scjq_vs_list = new ArrayList<>();
        scjq_vs_list.add(lottery_scjq_vs1);
        scjq_vs_list.add(lottery_scjq_vs2);
        scjq_vs_list.add(lottery_scjq_vs3);
        scjq_vs_list.add(lottery_scjq_vs4);
        List<TextView> scjq_guest_list = new ArrayList<>();
        scjq_guest_list.add(lottery_scjq_guest_name1);
        scjq_guest_list.add(lottery_scjq_guest_name2);
        scjq_guest_list.add(lottery_scjq_guest_name3);
        scjq_guest_list.add(lottery_scjq_guest_name4);
        List<TextView> scjq_time_list = new ArrayList<>();
        scjq_time_list.add(lottery_scjq_time1);
        scjq_time_list.add(lottery_scjq_time2);
        scjq_time_list.add(lottery_scjq_time3);
        scjq_time_list.add(lottery_scjq_time4);
        List<TextView> scjq_half_result_list = new ArrayList<>();
        scjq_half_result_list.add(lottery_scjq_half_result1);
        scjq_half_result_list.add(lottery_scjq_half_result2);
        scjq_half_result_list.add(lottery_scjq_half_result3);
        scjq_half_result_list.add(lottery_scjq_half_result4);
        List<TextView> scjq_full_result_list = new ArrayList<>();
        scjq_full_result_list.add(lottery_scjq_full_result1);
        scjq_full_result_list.add(lottery_scjq_full_result2);
        scjq_full_result_list.add(lottery_scjq_full_result3);
        scjq_full_result_list.add(lottery_scjq_full_result4);

//        lottery_scjq_issue.setText(context.getResources().getString(R.string.number_code_di) + (mNumberInfo.getIssue() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getIssue()) + context.getResources().getString(R.string.number_code_qi));
        // 设置对阵和开赛时间
        int index = 0;
        for (int i = 0, len = mNumberInfo.getFootballLotteryIssueResultData() == null ? 0 : mNumberInfo.getFootballLotteryIssueResultData().size(); i < len; i++) {
            // 判断是否开赛时间
//            try {
//                if (mNumberInfo.getFootballLotteryIssueResultData().get(i).getKickOffTime() == null) {
//                    scjq_vs_list.get(i).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
//                    scjq_vs_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//                    scjq_half_result_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//                    scjq_full_result_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//                } else if (Long.parseLong(mServerTime) > DateUtil.getCurrentTime(mNumberInfo.getFootballLotteryIssueResultData().get(i).getKickOffTime())) {
//                    scjq_vs_list.get(i).setTextColor(context.getResources().getColor(R.color.number_red));
//                    scjq_vs_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore());
//
//                    if (i == 0) {
//                        index = i;
//                    }else{
//                        index++;
//                    }
//                    scjq_half_result_list.get(i).setText(numbers.get(index) == null ? "" : numbers.get(index));
//                    index++;
//                    scjq_full_result_list.get(i).setText(numbers.get(index) == null ? "" : numbers.get(index));
//                } else {
//                    scjq_vs_list.get(i).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
//                    scjq_vs_list.get(i).setText("VS");
//                    scjq_half_result_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//                    scjq_full_result_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//                scjq_vs_list.get(i).setTextColor(context.getResources().getColor(R.color.content_txt_dark_grad));
//                scjq_vs_list.get(i).setText(context.getResources().getString(R.string.number_info_default));
//            }

            scjq_vs_list.get(i).setTextColor(mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore() == null ? context.getResources().getColor(R.color.content_txt_dark_grad) : context.getResources().getColor(R.color.number_red));
            scjq_vs_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore() == null ? "VS" : mNumberInfo.getFootballLotteryIssueResultData().get(i).getFullScore());

            if (i == 0) {
                index = i;
            } else {
                index++;
            }
            scjq_half_result_list.get(i).setText(numbers.get(index) == null ? context.getResources().getString(R.string.number_info_default) : numbers.get(index));
            index++;
            scjq_full_result_list.get(i).setText(numbers.get(index) == null ? context.getResources().getString(R.string.number_info_default) : numbers.get(index));

            scjq_home_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getHomeName() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getHomeName());
            scjq_guest_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getGuestName() == null ? context.getResources().getString(R.string.number_info_default) : mNumberInfo.getFootballLotteryIssueResultData().get(i).getGuestName());
            scjq_time_list.get(i).setText(mNumberInfo.getFootballLotteryIssueResultData().get(i).getKickOffTime() == null ? context.getResources().getString(R.string.number_info_default) : DateUtil.getLotteryInfoDate(mNumberInfo.getFootballLotteryIssueResultData().get(i).getKickOffTime(), "yyyy-MM-dd"));
        }
        lottery_scjq_sales_volume.setText(mNumberInfo.getFootballFirlottery().getSales() == null ? context.getResources().getString(R.string.number_info_default) : NumberFormat.getCurrencyInstance().format(Long.parseLong(mNumberInfo.getFootballFirlottery().getSales())));
        lottery_scjq_bonus_sum.setText(mNumberInfo.getFootballFirlottery().getJackpot() == null ? context.getResources().getString(R.string.number_info_default) : NumberFormat.getCurrencyInstance().format(Long.parseLong(mNumberInfo.getFootballFirlottery().getJackpot())));
        lottery_scjq_count.setText(mNumberInfo.getFootballFirlottery().getFirCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFootballFirlottery().getFirCount())));
        lottery_scjq_bonus.setText(mNumberInfo.getFootballFirlottery().getFirSinBon() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFootballFirlottery().getFirSinBon())));
    }

    /**
     * 福彩3D
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

                if (num == 0) {
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
            number_f3d_dx.setText(sum > 14 ? context.getResources().getString(R.string.number_bjsc_da) : context.getResources().getString(R.string.number_bjsc_xiao));
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
        TextView lottery_dlt_count1 = (TextView) mView.findViewById(R.id.lottery_dlt_count1);
        TextView lottery_dlt_count2 = (TextView) mView.findViewById(R.id.lottery_dlt_count2);
        TextView lottery_dlt_count3 = (TextView) mView.findViewById(R.id.lottery_dlt_count3);
        TextView lottery_dlt_count4 = (TextView) mView.findViewById(R.id.lottery_dlt_count4);
        TextView lottery_dlt_count5 = (TextView) mView.findViewById(R.id.lottery_dlt_count5);
        TextView lottery_dlt_count6 = (TextView) mView.findViewById(R.id.lottery_dlt_count6);
        TextView lottery_dlt_add_count1 = (TextView) mView.findViewById(R.id.lottery_dlt_add_count1);
        TextView lottery_dlt_add_count2 = (TextView) mView.findViewById(R.id.lottery_dlt_add_count2);
        TextView lottery_dlt_add_count3 = (TextView) mView.findViewById(R.id.lottery_dlt_add_count3);
        TextView lottery_dlt_add_count4 = (TextView) mView.findViewById(R.id.lottery_dlt_add_count4);
        TextView lottery_dlt_add_count5 = (TextView) mView.findViewById(R.id.lottery_dlt_add_count5);
        TextView lottery_dlt_bonus1 = (TextView) mView.findViewById(R.id.lottery_dlt_bonus1);
        TextView lottery_dlt_bonus2 = (TextView) mView.findViewById(R.id.lottery_dlt_bonus2);
        TextView lottery_dlt_bonus3 = (TextView) mView.findViewById(R.id.lottery_dlt_bonus3);
        TextView lottery_dlt_bonus4 = (TextView) mView.findViewById(R.id.lottery_dlt_bonus4);
        TextView lottery_dlt_bonus5 = (TextView) mView.findViewById(R.id.lottery_dlt_bonus5);
        TextView lottery_dlt_bonus6 = (TextView) mView.findViewById(R.id.lottery_dlt_bonus6);
        TextView lottery_dlt_add_bonus1 = (TextView) mView.findViewById(R.id.lottery_dlt_add_bonus1);
        TextView lottery_dlt_add_bonus2 = (TextView) mView.findViewById(R.id.lottery_dlt_add_bonus2);
        TextView lottery_dlt_add_bonus3 = (TextView) mView.findViewById(R.id.lottery_dlt_add_bonus3);
        TextView lottery_dlt_add_bonus4 = (TextView) mView.findViewById(R.id.lottery_dlt_add_bonus4);
        TextView lottery_dlt_add_bonus5 = (TextView) mView.findViewById(R.id.lottery_dlt_add_bonus5);
        TextView lottery_dlt_sales_volume = (TextView) mView.findViewById(R.id.lottery_dlt_sales_volume);
        TextView lottery_dlt_bonus_sum = (TextView) mView.findViewById(R.id.lottery_dlt_bonus_sum);
        TextView lottery_dlt_front_hz = (TextView) mView.findViewById(R.id.lottery_dlt_front_hz);
        TextView lottery_dlt_front_dx1 = (TextView) mView.findViewById(R.id.lottery_dlt_front_dx1);
        TextView lottery_dlt_front_dx2 = (TextView) mView.findViewById(R.id.lottery_dlt_front_dx2);
        TextView lottery_dlt_front_dx3 = (TextView) mView.findViewById(R.id.lottery_dlt_front_dx3);
        TextView lottery_dlt_front_dx4 = (TextView) mView.findViewById(R.id.lottery_dlt_front_dx4);
        TextView lottery_dlt_front_dx5 = (TextView) mView.findViewById(R.id.lottery_dlt_front_dx5);
        TextView lottery_dlt_front_jo1 = (TextView) mView.findViewById(R.id.lottery_dlt_front_jo1);
        TextView lottery_dlt_front_jo2 = (TextView) mView.findViewById(R.id.lottery_dlt_front_jo2);
        TextView lottery_dlt_front_jo3 = (TextView) mView.findViewById(R.id.lottery_dlt_front_jo3);
        TextView lottery_dlt_front_jo4 = (TextView) mView.findViewById(R.id.lottery_dlt_front_jo4);
        TextView lottery_dlt_front_jo5 = (TextView) mView.findViewById(R.id.lottery_dlt_front_jo5);
        TextView lottery_dlt_front_wx1 = (TextView) mView.findViewById(R.id.lottery_dlt_front_wx1);
        TextView lottery_dlt_front_wx2 = (TextView) mView.findViewById(R.id.lottery_dlt_front_wx2);
        TextView lottery_dlt_front_wx3 = (TextView) mView.findViewById(R.id.lottery_dlt_front_wx3);
        TextView lottery_dlt_front_wx4 = (TextView) mView.findViewById(R.id.lottery_dlt_front_wx4);
        TextView lottery_dlt_front_wx5 = (TextView) mView.findViewById(R.id.lottery_dlt_front_wx5);
        TextView lottery_dlt_front_fw1 = (TextView) mView.findViewById(R.id.lottery_dlt_front_fw1);
        TextView lottery_dlt_front_fw2 = (TextView) mView.findViewById(R.id.lottery_dlt_front_fw2);
        TextView lottery_dlt_front_fw3 = (TextView) mView.findViewById(R.id.lottery_dlt_front_fw3);
        TextView lottery_dlt_front_fw4 = (TextView) mView.findViewById(R.id.lottery_dlt_front_fw4);
        TextView lottery_dlt_front_fw5 = (TextView) mView.findViewById(R.id.lottery_dlt_front_fw5);
        TextView lottery_dlt_front_qj1 = (TextView) mView.findViewById(R.id.lottery_dlt_front_qj1);
        TextView lottery_dlt_front_qj2 = (TextView) mView.findViewById(R.id.lottery_dlt_front_qj2);
        TextView lottery_dlt_front_qj3 = (TextView) mView.findViewById(R.id.lottery_dlt_front_qj3);
        TextView lottery_dlt_front_qj4 = (TextView) mView.findViewById(R.id.lottery_dlt_front_qj4);
        TextView lottery_dlt_front_qj5 = (TextView) mView.findViewById(R.id.lottery_dlt_front_qj5);
        TextView lottery_dlt_later_hz1 = (TextView) mView.findViewById(R.id.lottery_dlt_later_hz1);
//        TextView lottery_dlt_later_hz2 = (TextView) mView.findViewById(R.id.lottery_dlt_later_hz2);
        TextView lottery_dlt_later_jo1 = (TextView) mView.findViewById(R.id.lottery_dlt_later_jo1);
        TextView lottery_dlt_later_jo2 = (TextView) mView.findViewById(R.id.lottery_dlt_later_jo2);
        TextView lottery_dlt_later_wx1 = (TextView) mView.findViewById(R.id.lottery_dlt_later_wx1);
        TextView lottery_dlt_later_wx2 = (TextView) mView.findViewById(R.id.lottery_dlt_later_wx2);
        TextView lottery_dlt_later_fw1 = (TextView) mView.findViewById(R.id.lottery_dlt_later_fw1);
        TextView lottery_dlt_later_fw2 = (TextView) mView.findViewById(R.id.lottery_dlt_later_fw2);

        List<TextView> dlt_front_dx_list = new ArrayList<>();
        dlt_front_dx_list.add(lottery_dlt_front_dx1);
        dlt_front_dx_list.add(lottery_dlt_front_dx2);
        dlt_front_dx_list.add(lottery_dlt_front_dx3);
        dlt_front_dx_list.add(lottery_dlt_front_dx4);
        dlt_front_dx_list.add(lottery_dlt_front_dx5);
        List<TextView> dlt_front_jo_list = new ArrayList<>();
        dlt_front_jo_list.add(lottery_dlt_front_jo1);
        dlt_front_jo_list.add(lottery_dlt_front_jo2);
        dlt_front_jo_list.add(lottery_dlt_front_jo3);
        dlt_front_jo_list.add(lottery_dlt_front_jo4);
        dlt_front_jo_list.add(lottery_dlt_front_jo5);
        List<TextView> dlt_front_wx_list = new ArrayList<>();
        dlt_front_wx_list.add(lottery_dlt_front_wx1);
        dlt_front_wx_list.add(lottery_dlt_front_wx2);
        dlt_front_wx_list.add(lottery_dlt_front_wx3);
        dlt_front_wx_list.add(lottery_dlt_front_wx4);
        dlt_front_wx_list.add(lottery_dlt_front_wx5);
        List<TextView> dlt_front_fw_list = new ArrayList<>();
        dlt_front_fw_list.add(lottery_dlt_front_fw1);
        dlt_front_fw_list.add(lottery_dlt_front_fw2);
        dlt_front_fw_list.add(lottery_dlt_front_fw3);
        dlt_front_fw_list.add(lottery_dlt_front_fw4);
        dlt_front_fw_list.add(lottery_dlt_front_fw5);
        List<TextView> dlt_front_qj_list = new ArrayList<>();
        dlt_front_qj_list.add(lottery_dlt_front_qj1);
        dlt_front_qj_list.add(lottery_dlt_front_qj2);
        dlt_front_qj_list.add(lottery_dlt_front_qj3);
        dlt_front_qj_list.add(lottery_dlt_front_qj4);
        dlt_front_qj_list.add(lottery_dlt_front_qj5);

        if (null == numbers) {
            lottery_dlt_count1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_count2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_count3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_count4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_count5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_count6.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_add_count1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_add_count2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_add_count3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_add_count4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_add_count5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_bonus1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_bonus2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_bonus3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_bonus4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_bonus5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_bonus6.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_add_bonus1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_add_bonus2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_add_bonus3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_add_bonus4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_add_bonus5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_sales_volume.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_bonus_sum.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_hz.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_dx1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_dx2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_dx3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_dx4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_dx5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_jo1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_jo2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_jo3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_jo4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_jo5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_wx1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_wx2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_wx3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_wx4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_wx5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_fw1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_fw2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_fw3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_fw4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_fw5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_qj1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_qj2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_qj3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_qj4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_front_qj5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_later_hz1.setText(context.getResources().getString(R.string.number_info_default));
//            lottery_dlt_later_hz2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_later_jo1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_later_jo2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_later_wx1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_later_wx2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_later_fw1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_dlt_later_fw2.setText(context.getResources().getString(R.string.number_info_default));
        } else {

            lottery_dlt_count1.setText(mNumberInfo.getFirstCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFirstCount())));
            lottery_dlt_bonus1.setText(mNumberInfo.getFirstBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFirstBonus())));
            lottery_dlt_add_count1.setText(mNumberInfo.getFirstAddCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFirstAddCount())));
            lottery_dlt_add_bonus1.setText(mNumberInfo.getFirstAddBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFirstAddBonus())));
            lottery_dlt_count2.setText(mNumberInfo.getSecondCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getSecondCount())));
            lottery_dlt_bonus2.setText(mNumberInfo.getSecondBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getSecondBonus())));
            lottery_dlt_add_count2.setText(mNumberInfo.getSecondAddCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getSecondAddCount())));
            lottery_dlt_add_bonus2.setText(mNumberInfo.getSecondAddBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getSecondAddBonus())));
            lottery_dlt_count3.setText(mNumberInfo.getThirdCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getThirdCount())));
            lottery_dlt_bonus3.setText(mNumberInfo.getThirdBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getThirdBonus())));
            lottery_dlt_add_count3.setText(mNumberInfo.getThirdAddCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getThirdAddCount())));
            lottery_dlt_add_bonus3.setText(mNumberInfo.getThirdAddBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getThirdAddBonus())));
            lottery_dlt_count4.setText(mNumberInfo.getFourthCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFourthCount())));
            lottery_dlt_bonus4.setText(mNumberInfo.getFourthBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFourthBonus())));
            lottery_dlt_add_count4.setText(mNumberInfo.getFourthAddCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFourthAddCount())));
            lottery_dlt_add_bonus4.setText(mNumberInfo.getFourthAddBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFourthAddBonus())));
            lottery_dlt_count5.setText(mNumberInfo.getFifthCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFifthCount())));
            lottery_dlt_bonus5.setText(mNumberInfo.getFifthBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFifthBonus())));
            lottery_dlt_add_count5.setText(mNumberInfo.getFifthAddCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFifthAddCount())));
            lottery_dlt_add_bonus5.setText(mNumberInfo.getFifthAddBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFifthAddBonus())));
            lottery_dlt_count6.setText(mNumberInfo.getSixthCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getSixthCount())));
            lottery_dlt_bonus6.setText(mNumberInfo.getSixthBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getSixthBonus())));
            lottery_dlt_sales_volume.setText(mNumberInfo.getSales() == null ? context.getResources().getString(R.string.number_info_default) : NumberFormat.getCurrencyInstance().format(Long.parseLong(mNumberInfo.getSales())));
            lottery_dlt_bonus_sum.setText(mNumberInfo.getJackpot() == null ? context.getResources().getString(R.string.number_info_default) : NumberFormat.getCurrencyInstance().format(Long.parseLong(mNumberInfo.getJackpot())));

            int sum = 0;
            int sum_b = 0;
            for (int i = 0, len = numbers.size(); i < len; i++) {
                if (i == 5) {
                    continue;
                }
                int num = Integer.parseInt(numbers.get(i));
                String lastNum;
                if (numbers.get(i).length() > 1) {
                    lastNum = numbers.get(i).substring(numbers.get(i).length() - 1, numbers.get(i).length());
                } else {
                    lastNum = numbers.get(i);
                }
                if (i < 5 && num > 0) {
                    sum += num;// 和
                    // 大小
                    if (num > 17) {
                        dlt_front_dx_list.get(i).setText(context.getResources().getString(R.string.number_bjsc_da));
                    } else {
                        dlt_front_dx_list.get(i).setText(context.getResources().getString(R.string.number_bjsc_xiao));
                    }
                    // 奇偶
                    if (num % 2 == 0) {
                        dlt_front_jo_list.get(i).setText(context.getResources().getString(R.string.number_info_o));
                    } else {
                        dlt_front_jo_list.get(i).setText(context.getResources().getString(R.string.number_info_j));
                    }
                    // 区间
                    if (num <= 12) {
                        dlt_front_qj_list.get(i).setText(context.getResources().getString(R.string.number_info_front));
                    } else if (num <= 24) {
                        dlt_front_qj_list.get(i).setText(context.getResources().getString(R.string.number_info_center));
                    } else {
                        dlt_front_qj_list.get(i).setText(context.getResources().getString(R.string.number_info_after));
                    }
                    // 五行和方位
                    setTextValue(lastNum, dlt_front_wx_list.get(i), dlt_front_fw_list.get(i));
                } else if (i == 6 && num > 0) {
                    sum_b += num;

                    // 大小
//                    if (num < 7) {
//                        lottery_dlt_later_hz1.setText(context.getResources().getString(R.string.number_bjsc_xiao));
//                    } else {
//                        lottery_dlt_later_hz1.setText(context.getResources().getString(R.string.number_bjsc_da));
//                    }
                    // 奇偶
                    if (num % 2 == 0) {
                        lottery_dlt_later_jo1.setText(context.getResources().getString(R.string.number_info_o));
                    } else {
                        lottery_dlt_later_jo1.setText(context.getResources().getString(R.string.number_info_j));
                    }
                    // 五行和方位
                    setTextValue(lastNum, lottery_dlt_later_wx1, lottery_dlt_later_fw1);
                } else if (i == 7 && num > 0) {
                    sum_b += num;
                    // 大小
//                    if (num < 7) {
//                        lottery_dlt_later_hz2.setText(context.getResources().getString(R.string.number_bjsc_xiao));
//                    } else {
//                        lottery_dlt_later_hz2.setText(context.getResources().getString(R.string.number_bjsc_da));
//                    }
                    // 奇偶
                    if (num % 2 == 0) {
                        lottery_dlt_later_jo2.setText(context.getResources().getString(R.string.number_info_o));
                    } else {
                        lottery_dlt_later_jo2.setText(context.getResources().getString(R.string.number_info_j));
                    }
                    // 五行和方位
                    setTextValue(lastNum, lottery_dlt_later_wx2, lottery_dlt_later_fw2);
                }
            }
            lottery_dlt_front_hz.setText(String.valueOf(sum));
            lottery_dlt_later_hz1.setText(String.valueOf(sum_b));
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
        TextView lottery_title1 = (TextView) mView.findViewById(R.id.lottery_title1);
        TextView lottery_title6 = (TextView) mView.findViewById(R.id.lottery_title6);
        TextView lottery_ssq_count1 = (TextView) mView.findViewById(R.id.lottery_ssq_count1);
        TextView lottery_ssq_count1_1 = (TextView) mView.findViewById(R.id.lottery_ssq_count1_1);
        TextView lottery_ssq_count2 = (TextView) mView.findViewById(R.id.lottery_ssq_count2);
        TextView lottery_ssq_count3 = (TextView) mView.findViewById(R.id.lottery_ssq_count3);
        TextView lottery_ssq_count4 = (TextView) mView.findViewById(R.id.lottery_ssq_count4);
        TextView lottery_ssq_count5 = (TextView) mView.findViewById(R.id.lottery_ssq_count5);
        TextView lottery_ssq_count6 = (TextView) mView.findViewById(R.id.lottery_ssq_count6);
        TextView lottery_ssq_count6_1 = (TextView) mView.findViewById(R.id.lottery_ssq_count6_1);
        TextView lottery_ssq_bonus1 = (TextView) mView.findViewById(R.id.lottery_ssq_bonus1);
        TextView lottery_ssq_bonus1_1 = (TextView) mView.findViewById(R.id.lottery_ssq_bonus1_1);
        TextView lottery_ssq_bonus2 = (TextView) mView.findViewById(R.id.lottery_ssq_bonus2);
        TextView lottery_ssq_bonus3 = (TextView) mView.findViewById(R.id.lottery_ssq_bonus3);
        TextView lottery_ssq_bonus4 = (TextView) mView.findViewById(R.id.lottery_ssq_bonus4);
        TextView lottery_ssq_bonus5 = (TextView) mView.findViewById(R.id.lottery_ssq_bonus5);
        TextView lottery_ssq_bonus6 = (TextView) mView.findViewById(R.id.lottery_ssq_bonus6);
        TextView lottery_ssq_bonus6_1 = (TextView) mView.findViewById(R.id.lottery_ssq_bonus6_1);
        TextView lottery_ssq_sales_volume = (TextView) mView.findViewById(R.id.lottery_ssq_sales_volume);
        TextView lottery_ssq_bonus_sum = (TextView) mView.findViewById(R.id.lottery_ssq_bonus_sum);
        TextView lottery_ssq_hz = (TextView) mView.findViewById(R.id.lottery_ssq_hz);
        TextView lottery_ssq_dx1 = (TextView) mView.findViewById(R.id.lottery_ssq_dx1);
        TextView lottery_ssq_dx2 = (TextView) mView.findViewById(R.id.lottery_ssq_dx2);
        TextView lottery_ssq_dx3 = (TextView) mView.findViewById(R.id.lottery_ssq_dx3);
        TextView lottery_ssq_dx4 = (TextView) mView.findViewById(R.id.lottery_ssq_dx4);
        TextView lottery_ssq_dx5 = (TextView) mView.findViewById(R.id.lottery_ssq_dx5);
        TextView lottery_ssq_dx6 = (TextView) mView.findViewById(R.id.lottery_ssq_dx6);
        TextView lottery_ssq_jo1 = (TextView) mView.findViewById(R.id.lottery_ssq_jo1);
        TextView lottery_ssq_jo2 = (TextView) mView.findViewById(R.id.lottery_ssq_jo2);
        TextView lottery_ssq_jo3 = (TextView) mView.findViewById(R.id.lottery_ssq_jo3);
        TextView lottery_ssq_jo4 = (TextView) mView.findViewById(R.id.lottery_ssq_jo4);
        TextView lottery_ssq_jo5 = (TextView) mView.findViewById(R.id.lottery_ssq_jo5);
        TextView lottery_ssq_jo6 = (TextView) mView.findViewById(R.id.lottery_ssq_jo6);
        TextView lottery_ssq_qj1 = (TextView) mView.findViewById(R.id.lottery_ssq_qj1);
        TextView lottery_ssq_qj2 = (TextView) mView.findViewById(R.id.lottery_ssq_qj2);
        TextView lottery_ssq_qj3 = (TextView) mView.findViewById(R.id.lottery_ssq_qj3);
        TextView lottery_ssq_qj4 = (TextView) mView.findViewById(R.id.lottery_ssq_qj4);
        TextView lottery_ssq_qj5 = (TextView) mView.findViewById(R.id.lottery_ssq_qj5);
        TextView lottery_ssq_qj6 = (TextView) mView.findViewById(R.id.lottery_ssq_qj6);
        TextView lottery_ssq_wx1 = (TextView) mView.findViewById(R.id.lottery_ssq_wx1);
        TextView lottery_ssq_wx2 = (TextView) mView.findViewById(R.id.lottery_ssq_wx2);
        TextView lottery_ssq_wx3 = (TextView) mView.findViewById(R.id.lottery_ssq_wx3);
        TextView lottery_ssq_wx4 = (TextView) mView.findViewById(R.id.lottery_ssq_wx4);
        TextView lottery_ssq_wx5 = (TextView) mView.findViewById(R.id.lottery_ssq_wx5);
        TextView lottery_ssq_wx6 = (TextView) mView.findViewById(R.id.lottery_ssq_wx6);
        TextView lottery_ssq_fw1 = (TextView) mView.findViewById(R.id.lottery_ssq_fw1);
        TextView lottery_ssq_fw2 = (TextView) mView.findViewById(R.id.lottery_ssq_fw2);
        TextView lottery_ssq_fw3 = (TextView) mView.findViewById(R.id.lottery_ssq_fw3);
        TextView lottery_ssq_fw4 = (TextView) mView.findViewById(R.id.lottery_ssq_fw4);
        TextView lottery_ssq_fw5 = (TextView) mView.findViewById(R.id.lottery_ssq_fw5);
        TextView lottery_ssq_fw6 = (TextView) mView.findViewById(R.id.lottery_ssq_fw6);
        TextView lottery_ssq_b_dx = (TextView) mView.findViewById(R.id.lottery_ssq_b_dx);
        TextView lottery_ssq_b_jo = (TextView) mView.findViewById(R.id.lottery_ssq_b_jo);
        TextView lottery_ssq_b_wx = (TextView) mView.findViewById(R.id.lottery_ssq_b_wx);
        TextView lottery_ssq_b_fw = (TextView) mView.findViewById(R.id.lottery_ssq_b_fw);
        List<TextView> ssq_dx_list = new ArrayList<>();
        ssq_dx_list.add(lottery_ssq_dx1);
        ssq_dx_list.add(lottery_ssq_dx2);
        ssq_dx_list.add(lottery_ssq_dx3);
        ssq_dx_list.add(lottery_ssq_dx4);
        ssq_dx_list.add(lottery_ssq_dx5);
        ssq_dx_list.add(lottery_ssq_dx6);
        List<TextView> ssq_jo_list = new ArrayList<>();
        ssq_jo_list.add(lottery_ssq_jo1);
        ssq_jo_list.add(lottery_ssq_jo2);
        ssq_jo_list.add(lottery_ssq_jo3);
        ssq_jo_list.add(lottery_ssq_jo4);
        ssq_jo_list.add(lottery_ssq_jo5);
        ssq_jo_list.add(lottery_ssq_jo6);
        List<TextView> ssq_qj_list = new ArrayList<>();
        ssq_qj_list.add(lottery_ssq_qj1);
        ssq_qj_list.add(lottery_ssq_qj2);
        ssq_qj_list.add(lottery_ssq_qj3);
        ssq_qj_list.add(lottery_ssq_qj4);
        ssq_qj_list.add(lottery_ssq_qj5);
        ssq_qj_list.add(lottery_ssq_qj6);
        List<TextView> ssq_wx_list = new ArrayList<>();
        ssq_wx_list.add(lottery_ssq_wx1);
        ssq_wx_list.add(lottery_ssq_wx2);
        ssq_wx_list.add(lottery_ssq_wx3);
        ssq_wx_list.add(lottery_ssq_wx4);
        ssq_wx_list.add(lottery_ssq_wx5);
        ssq_wx_list.add(lottery_ssq_wx6);
        List<TextView> ssq_fw_list = new ArrayList<>();
        ssq_fw_list.add(lottery_ssq_fw1);
        ssq_fw_list.add(lottery_ssq_fw2);
        ssq_fw_list.add(lottery_ssq_fw3);
        ssq_fw_list.add(lottery_ssq_fw4);
        ssq_fw_list.add(lottery_ssq_fw5);
        ssq_fw_list.add(lottery_ssq_fw6);

        if (null == numbers) {
            lottery_ssq_count1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_count1_1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_count2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_count3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_count4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_count5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_count6.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_count6_1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_bonus1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_bonus1_1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_bonus2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_bonus3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_bonus4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_bonus5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_bonus6.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_bonus6_1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_sales_volume.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_bonus_sum.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_hz.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_dx1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_dx2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_dx3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_dx4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_dx5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_dx6.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_jo1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_jo2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_jo3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_jo4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_jo5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_jo6.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_qj1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_qj2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_qj3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_qj4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_qj5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_qj6.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_wx1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_wx2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_wx3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_wx4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_wx5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_wx6.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_fw1.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_fw2.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_fw3.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_fw4.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_fw5.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_fw6.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_b_dx.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_b_jo.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_b_wx.setText(context.getResources().getString(R.string.number_info_default));
            lottery_ssq_b_fw.setText(context.getResources().getString(R.string.number_info_default));
        } else {
            // 没数据时　隐藏复式开奖条目
            if (mNumberInfo.getFirstAddCount() == null && mNumberInfo.getFirstAddBonus() == null) {
                lottery_title1.setVisibility(View.GONE);
                lottery_ssq_count1_1.setVisibility(View.GONE);
                lottery_ssq_bonus1_1.setVisibility(View.GONE);
            } else {
                lottery_title1.setVisibility(View.VISIBLE);
                lottery_ssq_count1_1.setVisibility(View.VISIBLE);
                lottery_ssq_bonus1_1.setVisibility(View.VISIBLE);
            }
            if (mNumberInfo.getSixthAddCount() == null && mNumberInfo.getSixthAddBonus() == null) {
                lottery_title6.setVisibility(View.GONE);
                lottery_ssq_count6_1.setVisibility(View.GONE);
                lottery_ssq_bonus6_1.setVisibility(View.GONE);
            } else {
                lottery_title6.setVisibility(View.VISIBLE);
                lottery_ssq_count6_1.setVisibility(View.VISIBLE);
                lottery_ssq_bonus6_1.setVisibility(View.VISIBLE);
            }

            lottery_ssq_count1.setText(mNumberInfo.getFirstCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFirstCount())));
            lottery_ssq_bonus1.setText(mNumberInfo.getFirstBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFirstBonus())));
            lottery_ssq_count1_1.setText(mNumberInfo.getFirstAddCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFirstAddCount())));
            lottery_ssq_bonus1_1.setText(mNumberInfo.getFirstAddBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFirstAddBonus())));
            lottery_ssq_count2.setText(mNumberInfo.getSecondCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getSecondCount())));
            lottery_ssq_bonus2.setText(mNumberInfo.getSecondBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getSecondBonus())));
            lottery_ssq_count3.setText(mNumberInfo.getThirdCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getThirdCount())));
            lottery_ssq_bonus3.setText(mNumberInfo.getThirdBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getThirdBonus())));
            lottery_ssq_count4.setText(mNumberInfo.getFourthCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFourthCount())));
            lottery_ssq_bonus4.setText(mNumberInfo.getFourthBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFourthBonus())));
            lottery_ssq_count5.setText(mNumberInfo.getFifthCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFifthCount())));
            lottery_ssq_bonus5.setText(mNumberInfo.getFifthBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getFifthBonus())));
            lottery_ssq_count6.setText(mNumberInfo.getSixthCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getSixthCount())));
            lottery_ssq_bonus6.setText(mNumberInfo.getSixthBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getSixthBonus())));
            lottery_ssq_count6_1.setText(mNumberInfo.getSixthAddCount() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getSixthAddCount())));
            lottery_ssq_bonus6_1.setText(mNumberInfo.getSixthAddBonus() == null ? context.getResources().getString(R.string.number_info_default) : NumberSubUtils.qianweifenge(Long.parseLong(mNumberInfo.getSixthAddBonus())));
            lottery_ssq_sales_volume.setText(mNumberInfo.getSales() == null ? context.getResources().getString(R.string.number_info_default) : NumberFormat.getCurrencyInstance().format(Long.parseLong(mNumberInfo.getSales())));
            lottery_ssq_bonus_sum.setText(mNumberInfo.getJackpot() == null ? context.getResources().getString(R.string.number_info_default) : NumberFormat.getCurrencyInstance().format(Long.parseLong(mNumberInfo.getJackpot())));

            int sum = 0;
            for (int i = 0, len = numbers.size(); i < len; i++) {
                if (i == 6) {
                    continue;
                }
                int num = Integer.parseInt(numbers.get(i));
                String lastNum;
                if (numbers.get(i).length() > 1) {
                    lastNum = numbers.get(i).substring(numbers.get(i).length() - 1, numbers.get(i).length());
                } else {
                    lastNum = numbers.get(i);
                }
                if (i < 6 && num > 0) {
                    sum += num;// 和
                    // 大小
                    if (num > 16) {
                        ssq_dx_list.get(i).setText(context.getResources().getString(R.string.number_bjsc_da));
                    } else {
                        ssq_dx_list.get(i).setText(context.getResources().getString(R.string.number_bjsc_xiao));
                    }
                    // 奇偶
                    if (num % 2 == 0) {
                        ssq_jo_list.get(i).setText(context.getResources().getString(R.string.number_info_o));
                    } else {
                        ssq_jo_list.get(i).setText(context.getResources().getString(R.string.number_info_j));
                    }
                    // 区位
                    if (num <= 11) {
                        ssq_qj_list.get(i).setText(context.getResources().getString(R.string.number_info_front));
                    } else if (num <= 22) {
                        ssq_qj_list.get(i).setText(context.getResources().getString(R.string.number_info_center));
                    } else {
                        ssq_qj_list.get(i).setText(context.getResources().getString(R.string.number_info_after));
                    }
                    // 五行和方位
                    setTextValue(lastNum, ssq_wx_list.get(i), ssq_fw_list.get(i));
                } else if (i == 7 && num > 0) {
                    // 大小
                    if (num > 8) {
                        lottery_ssq_b_dx.setText(context.getResources().getString(R.string.number_bjsc_da));
                    } else {
                        lottery_ssq_b_dx.setText(context.getResources().getString(R.string.number_bjsc_xiao));
                    }
                    // 奇偶
                    if (num % 2 == 0) {
                        lottery_ssq_b_jo.setText(context.getResources().getString(R.string.number_info_o));
                    } else {
                        lottery_ssq_b_jo.setText(context.getResources().getString(R.string.number_info_j));
                    }
                    // 五行和方位
                    setTextValue(lastNum, lottery_ssq_b_wx, lottery_ssq_b_fw);
                }
            }
            lottery_ssq_hz.setText(String.valueOf(sum));
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
     * 设置五行和方位方法
     */
    private void setTextValue(String lastNum, TextView tv_wx, TextView tv_fw) {
        switch (lastNum) {
            case "1":// 水
            case "6":// 北
                tv_wx.setText(context.getResources().getString(R.string.number_info_wx_s));
                tv_fw.setText(context.getResources().getString(R.string.number_info_fw_N));
                break;
            case "2":// 火
            case "7":// 南
                tv_wx.setText(context.getResources().getString(R.string.number_info_wx_h));
                tv_fw.setText(context.getResources().getString(R.string.number_info_fw_S));
                break;
            case "3":// 木
            case "8":// 东
                tv_wx.setText(context.getResources().getString(R.string.number_info_wx_m));
                tv_fw.setText(context.getResources().getString(R.string.number_info_fw_E));
                break;
            case "4":// 金
            case "9":// 西
                tv_wx.setText(context.getResources().getString(R.string.number_info_wx_j));
                tv_fw.setText(context.getResources().getString(R.string.number_info_fw_W));
                break;
            case "0":// 土
            case "5":// 中
                tv_wx.setText(context.getResources().getString(R.string.number_info_wx_t));
                tv_fw.setText(context.getResources().getString(R.string.number_info_fw_Z));
                break;
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
            case "30":// 胜负彩
                tv.setText(mContext.getResources().getString(R.string.number_cz_sfc));
                break;
            case "31":// 六场半全场
                tv.setText(mContext.getResources().getString(R.string.number_cz_lcbqc));
                break;
            case "32":// 4场进球
                tv.setText(mContext.getResources().getString(R.string.number_cz_scjq));
                break;
            default:
                tv.setText("-");
                break;
        }
    }

    /**
     * 添加彩种开奖描述信息
     */
    public static void numberAddDesc(Context mContext, TextView tv, String numberName) {
        switch (numberName) {
            case "1":// 六合彩
                tv.setVisibility(View.GONE);
                break;
            case "6":// 七星彩
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_qxc));
                break;
            case "15":// 北京赛车
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_bj_sc));
                break;
            case "8":// 广东快乐10分
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_gd_klsf));
                break;
            case "11":// 湖南快乐10分
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_hn_klsf));
                break;
            case "19":// 幸运农场
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_xylc));
                break;
            case "2":// 重庆时时彩
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_cq_ssc));
                break;
            case "4":// 新疆时时彩
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_xj_ssc));
                break;
            case "5":// 云南时时彩
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_yn_ssc));
                break;
            case "23":// 天津时时彩
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_tj_ssc));
                break;
            case "3":// 江西时时彩
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_jx_ssc));
                break;
            case "7":// 广东11选5
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_gd_syxw));
                break;
            case "20":// 江苏11选5
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_js_syxw));
                break;
            case "22":// 山东11选5
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_sd_syxw));
                break;
            case "10":// 安徽快三
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_ah_ks));
                break;
            case "16":// 江苏快三
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_js_ks));
                break;
            case "18":// 广西快三
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_gx_ks));
                break;
            case "24":// 双色球
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_ssq));
                break;
            case "25":// 排列3
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_pl3));
                break;
            case "26":// 排列5
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_pl5));
                break;
            case "27":// 福彩3D
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_f3d));
                break;
            case "28":// 七乐彩
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_qlc));
                break;
            case "29":// 大乐透
                tv.setText(mContext.getResources().getString(R.string.number_code_desc_dlt));
                break;
            default:
                tv.setText(" ");
                break;
        }
    }
}
