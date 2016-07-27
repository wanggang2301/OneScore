package com.hhly.mlottery.util;

import java.util.Locale;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/7/19.
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
