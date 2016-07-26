package com.hhly.mlottery.util;

import java.util.Locale;

/**
 * 字符串格式化工具类
 * <p/>
 * Created by loshine on 2016/7/19.
 */
public class StringFormatUtils {

    /**
     * 小数转百分比
     *
     * @param f
     * @return
     */
    public static String toPercentString(Float f) {
        return String.format(Locale.getDefault(), "%.0f%%", f * 100);
    }

    /**
     * 转String
     *
     * @param d
     * @return
     */
    public static String toString(double d) {
        String string = String.format(Locale.getDefault(), "%.3f", d);
        return string.substring(0, string.length() - 1);
    }

    public static double asDouble(String string) {
        if (string == null || string.isEmpty()) return 0;
        return Double.parseDouble(string);
    }
}
