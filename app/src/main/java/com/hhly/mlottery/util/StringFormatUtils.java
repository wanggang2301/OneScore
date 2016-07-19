package com.hhly.mlottery.util;

import java.util.Locale;

/**
 * 字符串格式化工具类
 * <p>
 * Created by loshine on 2016/7/19.
 */
public class StringFormatUtils {

    /**
     * 小数转百分比
     *
     * @param d
     * @return
     */
    public static String toPercentString(double d) {
        return String.format(Locale.getDefault(), "%.0f%%", d * 100);
    }

    /**
     * 转String
     *
     * @param d
     * @return
     */
    public static String toString(double d) {
        return String.format(Locale.getDefault(), "%.2f", d);
    }

    public static double asDouble(String string) {
        if (string == null || string.isEmpty()) return 0;
        return Double.parseDouble(string);
    }
}
