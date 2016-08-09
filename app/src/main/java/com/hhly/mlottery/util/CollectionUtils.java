package com.hhly.mlottery.util;

import java.util.Collection;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */

public class CollectionUtils {

    public static boolean notNull(Collection c) {
        return c != null;
    }

    public static boolean notEmpty(Collection c) {
        return notNull(c) && c.size() > 0;
    }
}
