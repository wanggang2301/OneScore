package com.hhly.mlottery.util;

import android.text.TextUtils;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 功能描述：完成与日期相关的各种操作
 * <p/>
 * <p/>
 * 包括将日期格式化、从字符串中解析出对应的日期、对日期的加减操作等
 *
 * @author maluming 2011-4-14
 * @see
 * @since 1.0
 */
public class DateUtil {
    /**
     * 功能描述：按照给出格式解析出日期
     *
     * @param dateStr String 字符型日期
     * @param format  String 格式
     * @return Date 日期
     */
    public static Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat df_parseDate = new SimpleDateFormat(format);
            String dt = dateStr;
            date = df_parseDate.parse(dt);
        } catch (Exception e) {
        }
        return date;
    }


    /**
     * 字符串日期时间yyyy-MM-dd hh:mm 转Date
     *
     * @param dateStr
     * @return
     */
    public static Date yyyyMMddhhmmToDate(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd hh:mm");
    }


    /**
     * 功能描述：格式化日期
     *
     * @param dateStr String 字符型日期：YYYY-MM-DD 格式
     * @return Date
     */
    public static Date parseDate(String dateStr) {

        return parseDate(dateStr, "yyyy-MM-dd");
    }

    /**
     * 功能描述：格式化输出日期
     *
     * @param date   Date 日期
     * @param format String 格式
     * @return 字符型日期
     */
    public static String format(Date date, String format) {
        String result = "";
        try {
            if (date != null) {
                DateFormat df_format = new SimpleDateFormat(format);
                result = df_format.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 国际化日期格式
     *
     * @param date
     * @return
     */
    public static String convertDateToNation(String date) {
        if (TextUtils.isEmpty(date)) return "";

        try {
            if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) { //国内
                return DateUtil.format(DateUtil.parseDate(date, "yyyy-MM-dd"), "yyyy-MM-dd");
            } else {
                return DateUtil.format(DateUtil.parseDate(date, "yyyy-MM-dd"), "dd-MM-yyyy");
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertDateToNationYY(String date) {
        if (TextUtils.isEmpty(date)) return "";
        try {
            if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) { //国内
                return DateUtil.format(DateUtil.parseDate(date, "yy-MM-dd"), "yy-MM-dd");
            } else {
                return DateUtil.format(DateUtil.parseDate(date, "yy-MM-dd"), "dd-MM-yy");
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertDateToNationMD(String date) {
        if (TextUtils.isEmpty(date)) return "";
        try {
            if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) { //国内
                return DateUtil.format(DateUtil.parseDate(date, "MM-dd"), "MM-dd");
            } else {
                return DateUtil.format(DateUtil.parseDate(date, "MM-dd"), "dd-MM");
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertDateToNationYD(String date) {
        if (TextUtils.isEmpty(date)) return "";
        try {
            if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) { //国内
                return DateUtil.format(DateUtil.parseDate(date, "yyyy-MM"), "yyyy-MM");
            } else {
                return DateUtil.format(DateUtil.parseDate(date, "yyyy-MM"), "MM-yyyy");
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertDateToNationHM(String date) {
        try {
            if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) { //国内
                return DateUtil.format(DateUtil.parseDate(date, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
            } else {
                return DateUtil.format(DateUtil.parseDate(date, "yyyy-MM-dd HH:mm"), "dd-MM-yyyy HH:mm");
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertDateToNationMDHM(String date) {
        try {
            if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) { //国内
                return DateUtil.format(DateUtil.parseDate(date, "MM-dd HH:mm"), "MM-dd HH:mm");
            } else {
                return DateUtil.format(DateUtil.parseDate(date, "MM-dd HH:mm"), "dd-MM HH:mm");
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertDateToNationHMS(String date) {
        try {
            if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) { //国内
                return DateUtil.format(DateUtil.parseDate(date, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
            } else {
                return DateUtil.format(DateUtil.parseDate(date, "yyyy-MM-dd HH:mm:ss"), "dd-MM-yyyy HH:mm:ss");
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 判断字符串是否为日期格式 yyyy-MM-dd
     *
     * @param s
     * @return
     */
    public static boolean isValidDateYMD(String s) {
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }

    }

    /**
     * 判断字符串是否为日期格式 MM-dd
     *
     * @param s
     * @return
     */
    public static boolean isValidDateYM(String s) {
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }

    }

    /**
     * 把毫秒转化成日期
     *
     * @param millSec(毫秒数)
     * @return
     */

    public static String transferLongToDate(Long millSec) {
        String dateFormat = "MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        Date date = new Date(millSec);

        return sdf.format(date);

    }

    /**
     * 功能描述：返回字符型日期
     *
     * @param date 日期
     * @return 返回字符型日期 yyyy/MM/dd 格式
     */
    public static String getDate(Date date) {
        return format(date, "yyyy/MM/dd");
    }

    /**
     * 功能描述：返回字符型时间
     *
     * @param date Date 日期
     * @return 返回字符型时间 HH:mm:ss 格式
     */
    public static String getTime(Date date) {
        return format(date, "HH:mm:ss");
    }

    /**
     * 功能描述：返回字符型日期时间
     *
     * @param date Date 日期
     * @return 返回字符型日期时间 yyyy/MM/dd HH:mm:ss 格式
     */
    public static String getDateTime(Date date) {
        return format(date, "yyyy/MM/dd HH:mm:ss");
    }

    public static String getMillisDateTime(Date date) {
        return format(date, "yyyy/MM/dd HH:mm:ss.SSS");
    }

    /**
     * 功能描述：取得指定月份的第一天
     *
     * @param strdate String 字符型日期
     * @return String yyyy-MM-dd 格式
     */
    public static String getMonthBegin(String strdate) {
        Date date = parseDate(strdate);
        return format(date, "yyyy-MM") + "-01";
    }

    /**
     * 转换为指日格式日期字符串
     *
     * @param strdate
     * @return
     */
    public static String getLotteryInfoDate(String strdate, String style) {
        if (strdate == null) {
            return null;
        }
        DateFormat d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat d2 = new SimpleDateFormat(style);
        try {
            strdate = d2.format(d1.parse(strdate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strdate;
    }

    /**
     * 功能描述：常用的格式化日期
     *
     * @param date Date 日期
     * @return String 日期字符串 yyyy-MM-dd格式
     */
    public static String formatDate(Date date) {
        return formatDateByFormat(date, "yyyy-MM-dd");
    }

    /**
     * 功能描述：以指定的格式来格式化日期
     *
     * @param date   Date 日期
     * @param format String 格式
     * @return String 日期字符串
     */
    public static String formatDateByFormat(Date date, String format) {
        String result = "";
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception ex) {
            }
        }
        return result;
    }

    /**
     * 计算2个日期之间的相隔天数
     *
     * @param d1 日期1
     * @param d2 日期2
     * @return 日期1和日期2相隔天数
     */
    public int getDaysBetween(Calendar d1, Calendar d2) {
        if (d1.after(d2)) {
            // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 计算2个日期之间的工作天数（去除周六周日）
     *
     * @param d1 日期1
     * @param d2 日期2
     * @return 日期1和日期2之间的工作天数
     */
    public int getWorkingDay(Calendar d1, Calendar d2) {
        int result = -1;
        if (d1.after(d2)) {
            // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }

        // int betweendays = getDaysBetween(d1, d2);

        // int charge_date = 0;

        // 开始日期的日期偏移量
        int charge_start_date = 0;
        // 结束日期的日期偏移量
        int charge_end_date = 0;

        int stmp;
        int etmp;
        stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
        etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);

        // 日期不在同一个日期内
        if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
            charge_start_date = stmp - 1;
        }
        if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
            charge_end_date = etmp - 1;
        }
        // }
        result = (getDaysBetween(this.getNextMonday(d1), this.getNextMonday(d2)) / 7) * 5 + charge_start_date - charge_end_date;
        // System.out.println("charge_start_date>" + charge_start_date);
        // System.out.println("charge_end_date>" + charge_end_date);
        // System.out.println("between day is-->" + betweendays);
        return result;
    }

    /**
     * 获取当前星期
     *
     * @param date      当前日期
     * @param character zh : 标识中文 ， en : 标识英文（默认）
     * @return 当前日期
     */
    public String getChineseWeek(Calendar date, String character) {
        String dayNames[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        if ("zh".equals(character)) {
            dayNames[0] = "星期日";
            dayNames[1] = "星期一";
            dayNames[2] = "星期二";
            dayNames[3] = "星期三";
            dayNames[4] = "星期四";
            dayNames[5] = "星期五";
            dayNames[6] = "星期六";
        }
        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);

        // System.out.println(dayNames[dayOfWeek - 1]);
        return dayNames[dayOfWeek - 1];

    }

    /**
     * 获得日期的下一个星期一的日期
     *
     * @param date
     * @return
     */
    public Calendar getNextMonday(Calendar date) {
        Calendar result = null;
        result = date;
        do {
            result = (Calendar) result.clone();
            result.add(Calendar.DATE, 1);
        } while (result.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY);
        return result;
    }

    /**
     * 计算两个日期之间的非工作日天数
     *
     * @param d1 日期1
     * @param d2 日期2
     * @return 日期1与日期2之间的非工作天数
     */
    public int getHolidays(Calendar d1, Calendar d2) {
        return this.getDaysBetween(d1, d2) - this.getWorkingDay(d1, d2);

    }

    public static void main(String[] args) {
        DateUtil dt = new DateUtil();
        // System.out.println(d.toString());
        // System.out.println(formatDate(d).toString());
        // System.out.println(getMonthBegin(formatDate(d).toString()));
        // System.out.println(getMonthBegin("2008/07/19"));
        // System.out.println(getMonthEnd("2008/07/19"));

//		System.out.println(DateUtil.getDateTime(null));
    }

    /**
     * 获得周几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {MyApp.getContext().getResources().getString(R.string.number_xq7), MyApp.getContext().getResources().getString(R.string.number_xq1),
                MyApp.getContext().getResources().getString(R.string.number_xq2), MyApp.getContext().getResources().getString(R.string.number_xq3),
                MyApp.getContext().getResources().getString(R.string.number_xq4), MyApp.getContext().getResources().getString(R.string.number_xq5),
                MyApp.getContext().getResources().getString(R.string.number_xq6)};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getWeekOfXinQi(Date dt) {
        String[] weekDays = {MyApp.getContext().getResources().getString(R.string.number_xinqi7), MyApp.getContext().getResources().getString(R.string.number_xinqi1),
                MyApp.getContext().getResources().getString(R.string.number_xinqi2), MyApp.getContext().getResources().getString(R.string.number_xinqi3),
                MyApp.getContext().getResources().getString(R.string.number_xinqi4), MyApp.getContext().getResources().getString(R.string.number_xinqi5),
                MyApp.getContext().getResources().getString(R.string.number_xinqi6)};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 彩票专用，获取周几
     */
    public static String getLotteryWeekOfDate(Date dt) {
        if (dt == null) {
            return null;
        }
        String[] weekDays = {MyApp.getContext().getResources().getString(R.string.number_7), MyApp.getContext().getResources().getString(R.string.number_1),
                MyApp.getContext().getResources().getString(R.string.number_2), MyApp.getContext().getResources().getString(R.string.number_3),
                MyApp.getContext().getResources().getString(R.string.number_4), MyApp.getContext().getResources().getString(R.string.number_5),
                MyApp.getContext().getResources().getString(R.string.number_6)};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getDate(int i, String dString) {
        int y, m, d;
        String year = "", month = "", day = "";


        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.parseDate(dString));

        cal.add(Calendar.DAY_OF_YEAR, i);
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

    public static Long getCurrentTime(String nextTime) throws ParseException {
        TimeZone time = TimeZone.getTimeZone("GMT+8");// 默认国内版
        switch (MyApp.isPackageName) {
            case AppConstants.PACKGER_NAME_ZH:// 国内版
                time = TimeZone.getTimeZone("GMT+8");
                break;
            case AppConstants.PACKGER_NAME_TH:// 泰国版
            case AppConstants.PACKGER_NAME_VN_HN:// 越南北版
            case AppConstants.PACKGER_NAME_VN:// 越南南版
                time = TimeZone.getTimeZone("GMT+7");
                break;
            case AppConstants.PACKGER_NAME_UK:// 英文版
                time = TimeZone.getTimeZone("GMT+0");
                break;
        }
        TimeZone.setDefault(time);// 设置时区
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = sdf.parse(nextTime);
        sdf.setTimeZone(TimeZone.getDefault());
        return data.getTime();
    }

    /**
     * 将字符串转为XX月XX日
     */
    public static String getAssignDate(String date) {
        String[] split = date.split("-");
        return split[1] + "-" + split[2];
    }

    /**
     * 根据传过来的日期判断是今天还是明天
     *
     * @param date
     * @return
     */

    public static String getTodayorTomorrow(String date) {

        String current = formatDate(new Date());

        long today = Long.valueOf(current.replaceAll("[-\\s:]", ""));

        long day = Long.valueOf(date.replaceAll("[-\\s:]", ""));

        if ((today - day) == 1) {
            return MyApp.getContext().getResources().getString(R.string.return_yesterday);
        } else if ((today - day) == -1) {
            return MyApp.getContext().getResources().getString(R.string.return_tomorrow);
        } else if ((today - day) == 0) {
            return MyApp.getContext().getResources().getString(R.string.return_today);
        }
        return "";
    }

    /**
     * 获取当前时间年月日
     */
    public static String getMomentDate() {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }
}
