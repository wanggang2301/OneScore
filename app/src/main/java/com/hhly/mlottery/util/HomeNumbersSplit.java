package com.hhly.mlottery.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页号码拆分工具类
 * Created by hhly107 on 2016/4/7.
 */
public class HomeNumbersSplit {
    /**
     * 拆分号码
     */
    public static List<String> getSplitData(String object) {
        List<String> resultList = new ArrayList<>();
        if (TextUtils.isEmpty(object) || object.contains("?")) {// 如果号码为空
            return null;
        }
        if (object.contains("#")) {
            String[] nums = object.split(",");
            int len2 = nums.length - 1;
            for (int i = 0; i < len2; i++) {
                resultList.add(nums[i]);
            }
            String[] nums1 = nums[5].split("#");
            for (int i = 0; i < nums1.length; i++) {
                resultList.add(nums1[i]);
            }
        } else {
            String[] nums = object.split(",");
            for (int i = 0; i < nums.length; i++) {
                resultList.add(nums[i]);
            }
        }
        return resultList;
    }
}
