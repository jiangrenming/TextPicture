package main.demo.mobads.baidu.com.androidutils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 字符串工具类
 *
 * @author chenkh
 *         2015-03-12
 */
public class StringUtils {

    /**
     * 判断ip合法性
     *
     * @param ip 字符串
     */
    @SuppressLint("NewApi")
    public static boolean isMacthIp(String ip) {
        if (ip != null && !ip.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$"; // 判断ip地址是否与正则表达式匹配
            if (ip.matches(regex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 需要判断的字符串
     * @return true 为空; false 不为空
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为空(包括对"null")
     *
     * @param str 需要判断的字符串
     * @return true 为空; false 不为空
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return true;
        }
        return false;
    }


    /**
     * 判断字符串是否纯数字
     *
     * @param str 需要判断的字符串
     * @return true 是纯数字; false 不是纯数字
     */
    public static boolean isDigital(String str) {
        if (!isEmpty(str))
            return str.matches("[0-9]+");
        return false;
    }

    /**
     * 计算含有中文的字符串长度
     *
     * @param value 字符串（支持含中文字符串）
     * @return 中文字符串长度
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 字符串数组转换List [String]
     *
     * @param items
     * @return List [String]
     */
    public static List<String> stringsToList(String[] items) {
        List<String> lists = new ArrayList<String>();
        for (int i = 0; i < items.length; i++) {
            lists.add(items[i]);
        }
        return lists;
    }

    /**
     * 字符串填充，将sour使用fillStr前补或后补满len长度
     *
     * @param sour    待填充字符串，支持含有中文
     * @param fillStr 填充数据
     * @param len     填充完整字符串长度
     * @param isLeft  是否左补填充数据，否则右补填充数据
     * @return 填充后的数据
     */
    public static String fill(String sour, String fillStr, int len, boolean isLeft) {
        if (sour == null) {
            sour = "";
        }
        int fillLen = len - length(sour);
        String fill = "";
        for (int i = 0; i < fillLen; i++) {
            fill = fill + fillStr;
        }
        if (isLeft) {
            return fill + sour;
        } else {
            return sour + fill;
        }
    }

    /**
     * 字符串填充
     *
     * @param strData 待填充字符串，不支持含有中文
     * @param nLen    填充长度
     * @param subStr  填充的数据
     * @param nOption 0:左侧填充; 1:右侧填充; 2:两边填充
     * @return 填充后字符串
     */
    public static String paddingString(String strData, int nLen, String subStr, int nOption) {
        int i, addCharLen;
        String strHead = "";
        String strEnd = "";
        i = strData.length();
        if (i >= nLen) {
            return strData;
        }
        switch (nOption) {
            case 0:
                addCharLen = (nLen - i) / subStr.length();
                for (i = 0; i < addCharLen; i++) {
                    strHead += subStr;
                }
                return strHead + strData;
            case 1:
                addCharLen = (nLen - i) / subStr.length();
                for (i = 0; i < addCharLen; i++) {
                    strEnd += subStr;
                }
                return strData + strEnd;
            case 2:
                addCharLen = (nLen - i) / (subStr.length() * 2);
                for (i = 0; i < addCharLen; i++) {
                    strHead += subStr;
                    strEnd += subStr;
                }
                return strHead + strData + strEnd;
            default:
                return strData;
        }
    }

    /**
     * 整形转换成BCD型的字符串
     * 9转换成后将变成09,00 09
     * 19转换后将变成19, 00 19
     *
     * @param value    整型
     * @param bytesNum BCD字节个数
     * @return
     */
    public static String intToBcd(int value, int bytesNum) {
        switch (bytesNum) {
            case 1:
                if (value >= 0 && value <= 99) {
                    return paddingString(String.valueOf(value), 2, "0", 0);
                }
                break;
            case 2:
                if (value >= 0 && value <= 999) {
                    return paddingString(String.valueOf(value), 4, "0", 0);
                }
                break;

            case 3:
                if (value >= 0 && value <= 999) {
                    return paddingString(String.valueOf(value), 3, "0", 0);
                }
                break;
        }
        return "";
    }

    /**
     * Hex数据转换成字符串
     *
     * @param value Hex数据
     * @return 转换后字符串
     * @throws UnsupportedEncodingException
     */
    public static String hexToStr(String value) throws UnsupportedEncodingException {
        return new String(BytesUtils.hexToBytes(value), "GBK");
    }

    /**
     * 字符串转换成Hex
     *
     * @param value 字符串
     * @return 转换后Hex
     */
    public static String strToHex(String value) {
        return BytesUtils.bytesToHex(BytesUtils.getBytes(value));
    }

    /**
     * 往value中填充一个字符0 ,当数据长度正好为2的整数倍时，不填充
     *
     * @param value  需要填充的字符串
     * @param option 0:往后填充 ;1:往前填充
     * @return 填充后字符串
     */
    public static String paddingZeroToHexStr(String value, int option) {
        if (value.length() % 2 == 0) {
            return value;
        }

        if (option == 0) {
            return "0" + value;
        } else if (option == 1) {
            return value + "0";
        } else {
            return value;
        }
    }

    /**
     * 判断是否是Hex格式数据
     *
     * @param value 数据
     * @return ture 是Hex格式数据;
     * false 不是Hex格式数据
     */
    public static boolean checkHexStr(String value) {
        int i;
        int len;
        if (value == null)
            return false;
        len = value.length();
        if (len == 0)
            return false;
        for (i = 0; i < len; i++) {
            if (!((value.charAt(i) >= '0' && value.charAt(i) <= '9') ||
                    (value.charAt(i) >= 'a' && value.charAt(i) <= 'f') ||
                    (value.charAt(i) >= 'A' && value.charAt(i) <= 'F'))) {
                return false;
            }
        }
        return true;
    }



    /**
     * Binary数据转换成Hex
     *
     * @param value Binary数据
     * @return Hex数据
     */
    public static String binaryToHex(String value) {
        int i, j, len;
        String result = "";
        char[] hexVocable = {'0', '1', '2', '3',
                '4', '5', '6', '7',
                '8', '9', 'A', 'B',
                'C', 'D', 'E', 'F'};
        String[] binString = {"0000", "0001", "0010", "0011",
                "0100", "0101", "0110", "0111",
                "1000", "1001", "1010", "1011",
                "1100", "1101", "1110", "1111"};
        //System.out.println("value: " + value);

        len = value.length();
        for (i = 0; i < len; i += 4) {
            for (j = 0; j < 16; j++) {
                if (binString[j].equals(value.substring(i, i + 4))) {
                    result += hexVocable[j];
                    break;
                }
            }
        }
        //System.out.println("result: " + result);
        return result;
    }

    /**
     * Hex数据转换成Binary
     *
     * @param value Hex数据
     * @return Binary数据
     */
    public static String hexToBinary(String value) {
        int i, j, len;
        String result = "";
        char[] hexVocable = {'0', '1', '2', '3',
                '4', '5', '6', '7',
                '8', '9', 'A', 'B',
                'C', 'D', 'E', 'F'};
        String[] binString = {"0000", "0001", "0010", "0011",
                "0100", "0101", "0110", "0111",
                "1000", "1001", "1010", "1011",
                "1100", "1101", "1110", "1111"};

        len = value.length();
        for (i = 0; i < len; i++) {
            for (j = 0; j < 16; j++) {
                if (value.charAt(i) == hexVocable[j]) {
                    result += binString[j];
                    break;
                }
            }
        }
        //System.out.println("result: " + result);
        return result;
    }

    /**
     * 获取二进制字符串
     * 0x00 0x01 0x00 0x01 0x01转换成"01011"
     *
     * @param value 字符串
     * @return 二进制字符串
     */
    public static String getBinaryString(byte[] value) {
        int len;
        String result = "";

        len = value.length;

        for (int i = 0; i < len; i++) {
            result += String.valueOf(value[i]);
        }

        return result;
    }

    /**
     * 获取两个数值字符串中较小的数值字符串。
     *
     * @param str1
     * @param str2
     * @return
     */
    public static String getMinNumStr(String str1, String str2) {
        if (!isDigital(str1) || !isDigital(str2)) {
            return null;
        }
        double num1 = Double.valueOf(str1);
        double num2 = Double.valueOf(str2);
        return num1 <= num2 ? String.valueOf(num1) : String.valueOf(num2);
    }

    /**
     * 判断是否是double数据类型的字符串。
     *
     * @param strNum
     * @return
     */
    public static boolean isDoubleNum(String strNum) {
        if (TextUtils.isEmpty(strNum)) {
            return false;
        }
        try {
            double doubleNum = Double.valueOf(strNum);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 右填充空格，获取16位倍数长度的字符串。
     *
     * @param data
     * @return
     */
    public static String addSpace2Blocks16(String data) {
        String space16 = null;
        if (!TextUtils.isEmpty(data)) {
            int remainder = data.length() % 16;
            if (remainder != 0) {
                space16 = String.format("%-" + (data.length() + 16 - remainder) + "s", data);
            } else {
                space16 = data;
            }
        }
        return space16;
    }

    /**
     * 判断字符串是否全是'0'
     *
     * @return
     */
    public static boolean isAllZero(String data) {
        boolean isAllZero = true;
        if (!TextUtils.isEmpty(data)) {
            for (char c : data.toCharArray()) {
                if (c != '0') {
                    isAllZero = false;
                    break;
                }
            }
        }
        return isAllZero;
    }

    /**
     * 把Bundle转换成字符串。
     *
     * @param bundle
     * @return
     */
    public static String bundleToString(Bundle bundle) {
        String s = "Bundle[{";
        if (bundle == null) {
            s += "null";
        } else {
            for (String key : bundle.keySet()) {
                s += key + "=" + bundle.get(key) + ", ";
            }
            s = s.trim();
        }
        if (s.endsWith(",")) {
            s = s.substring(0, s.length() - 1);
        }
        s += "}]";
        return s;
    }
}
