package data.utils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/6/6  15:20.
 */

public class Sign {

    private static String signKey = "ybfmobile";

    public static String getSign(String signUrl, Map<String, String> paramentMap) {

        String mapParamentData = getStringKeyValue(paixuKey(getMapKey(paramentMap)), paramentMap);
        String singKeyMD5 = encodeByMD5(signKey).toLowerCase();
        String allParament = signUrl + mapParamentData + singKeyMD5;
        String paramentMD5 = encodeByMD5(allParament) + ranDomNum();//小写

        return paramentMD5.toLowerCase();
    }

    private static List<String> getMapKey(Map<String, String> paraMap) {

        List<String> mapKeyList = new ArrayList<>();

        Set<String> mapKey = paraMap.keySet();
        for (String key : mapKey) {
            mapKeyList.add(key.toString());
        }
        return mapKeyList;
    }

    private static List<String> paixuKey(List<String> keyList) {
        List<String> mapSortList = new ArrayList<>();
        Collections.sort(keyList, String.CASE_INSENSITIVE_ORDER);
        for (String key : keyList) {
            mapSortList.add(key);
        }
        return mapSortList;
    }

    private static String getStringKeyValue(List<String> sortList, Map<String, String> paraMap) {

        StringBuffer sb = new StringBuffer();
        for (String keyList : sortList) {
            sb.append(keyList + paraMap.get(keyList));
        }

        return sb.toString();
    }

    //获得随机两位 hexDigits[] 中
    private static String ranDomNum() {
        Random rand = new Random();
        int i = rand.nextInt(15);
        int j = rand.nextInt(15);
        return hexDigits[i] + hexDigits[j];
    }

    /* 十六进制下数字到字符的映射数组 */
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 对字符串进行MD5编码
     *
     * @param originString
     * @return
     */
    private static String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] results = md.digest(originString.getBytes());
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 十六进制字串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成16进制形式的字符串
     *
     * @param b
     * @return
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
