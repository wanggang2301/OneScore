package com.hhly.mlottery.util;

import android.text.TextUtils;

/**
 * @author chenml
 * @ClassName: HandicapUtils
 * @Description: 盘口计算
 * @date 2015-10-21 下午4:57:40
 */
public class HandicapUtils {

    //    private static float[] numCNs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//    private static float[] numTWs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static char[] numCNs = {'零', '一', '两', '三', '四', '五', '六', '七', '八', '九', '十'};
    private static char[] numTWs = {'零', '一', '兩', '三', '四', '五', '六', '七', '八', '九', '十'};

    /**
     * 亚盘盘口转换
     *
     * @param handicap
     * @return
     */
    public static String changeHandicap(String handicap) {

        if (TextUtils.isEmpty(handicap) || handicap.equals("-")) {
            return "封";
        }
        StringBuffer result = new StringBuffer();
        float handicapF = 0.0f;
        try {
            handicapF = Float.parseFloat(handicap);
        } catch (NumberFormatException e) {
            return "e";
        }

        if (handicapF < 0) {
            result.append("*");
        }

        handicapF = Math.abs(handicapF);
        float fractionalPart = handicapF - (int) handicapF;

        if (fractionalPart == 0.25f || fractionalPart == 0.75f) {
            float left = handicapF - 0.25f;
            float right = handicapF + 0.25f;

            numToBall(left, result);
            result.append("/");
            numToBall(right, result);

        } else if (fractionalPart == 0.5f) {
            if ((int) handicapF == 0) {
                result.append("半球");  //半球 0.5
            } else if ((int) handicapF == 1) {
                result.append("球半");  //球半 1.5
            } else {

                result.append(numToCN((int) handicapF) + "球半"); //球半 0.5
            }
        } else if (fractionalPart == 0.0f) {
            if ((int) handicapF == 0) {
                result.append("平");  //平手 0
            } else {
                result.append(numToCN((int) handicapF) + "球");  //球
            }
        }

        return result.toString();
    }

    private static char numToCN(int num) {
        L.d("pankou", num + "");

//        if (num <= 10) {
            String st = PreferenceUtil.getString("language", "rCN");
            if ("rTW".equals(st)) {
                return numTWs[num];
            } else {
                return numCNs[num];
            }
//        }
//
//
//        return num;

    }

    private static StringBuffer numToBall(float handicapF, StringBuffer result) {
        float fractionalPart = handicapF - (int) handicapF;
        if (fractionalPart == 0.5f) {
            if ((int) handicapF == 0) {
                result.append("半");
            } else if ((int) handicapF == 1) {
                result.append("球半");
            } else {
                result.append(numToCN((int) handicapF) + "球半");  //球半

            }
        } else if (fractionalPart == 0.0f) {
            if ((int) handicapF == 0) {
                result.append("平");  //平
            } else {
                result.append(numToCN((int) handicapF));
            }
        }
        return result;
    }

    /**
     * 大小球
     *
     * @param handicap 盘口
     * @return
     */
    public static String changeHandicapByBigLittleBall(String handicap) {
        if (handicap == null || handicap.equals("-"))
            return "封";
        float handicapF;
        try {
            handicapF = Float.parseFloat(handicap);
        } catch (NumberFormatException e) {
            return "e";
        }

        float fractionalPart = handicapF - (int) handicapF;

        if (fractionalPart == 0.25f || fractionalPart == 0.75f) {
            float left = handicapF - 0.25f;
            float right = handicapF + 0.25f;
            String leftResult = left + "";
            String rightResult = right + "";

            if (left - (int) left == 0.0f) {
                leftResult = (int) left + "";
            }

            if (right - (int) right == 0.0f) {
                rightResult = (int) right + "";
            }

            return leftResult + "/" + rightResult;
        } else if (handicapF == (int) handicapF) {
            return ((int) handicapF) + "";
        } else {
            return handicap;
        }
    }

    public static String[] interChangeHandicap(String handicap) {
        String[] handicaps = new String[2];

        float handicapF = 0.0f;
        try {
            handicapF = Float.parseFloat(handicap);
        } catch (NumberFormatException e) {
        }

        if (handicapF > 0) {
            handicapF = Math.abs(handicapF);
            handicaps[0] = "-" + handicapF;
            handicaps[1] = "+" + handicapF;
        } else {
            handicapF = Math.abs(handicapF);
            handicaps[1] = "-" + handicapF;
            handicaps[0] = "+" + handicapF;
        }

        return handicaps;
    }

    public static void main(String[] args) {
        float hand = 0.0f;

        for (int n = 0; n < 40; n++) {

            String re = changeHandicap(hand + "");
//			System.out.println(re);
            hand += 0.25f;
        }
    }

}
