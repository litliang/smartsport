package top.smartsport.www.utils;

/**
 *  字符串 常用工具
 */
public class StringUtil {

    /**
     * 判断字符串是否为空
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        boolean isEmpty = true;
        if (value != null && !value.trim().equals("") && !value.trim().equals("null")) {
            isEmpty = false;
        }
        return isEmpty;
    }

}
