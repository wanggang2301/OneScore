package com.hhly.mlottery.util;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.config.BaseURLs;

import java.util.Locale;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/8/16
 */
public class LocaleFactory {

    public static Locale get() {
        switch (MyApp.isLanguage) {
            case "rCN":
                return Locale.CHINA;
            case "rTW":
                return Locale.TAIWAN;
            case "rKO":
                return Locale.KOREA;
            case "rEN":
            case "rID":
                // 印尼
            case "rTH":
                // 泰国
            case "rVI":
                // 越南
            default:
                return Locale.ENGLISH;
        }
    }
}
