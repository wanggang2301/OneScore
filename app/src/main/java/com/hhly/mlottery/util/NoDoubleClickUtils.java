package com.hhly.mlottery.util;

/**
 * @author wang gang
 * @date 2016/5/16 14:47
 * @des ${}
 */
public class NoDoubleClickUtils {
    private static long lastClickTime;
    private final static int SPACE_TIME = 500;

    public static void initLastClickTime() {
        lastClickTime = 0;
    }

    public synchronized static boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        boolean isClick2;
        isClick2 = currentTime - lastClickTime <= SPACE_TIME;
        lastClickTime = currentTime;
        return isClick2;
    }
}