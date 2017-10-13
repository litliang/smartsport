package top.smartsport.www.utils;

import android.content.Context;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //输入手机号码检查是否有误
    public static boolean checkMobile(Context context, String mobile) {
        if(mobile.equals(null)){
            Toast.makeText(context, "手机号码不能为空！", Toast.LENGTH_LONG).show();
            return false;
                /*^匹配开始地方$匹配结束地方，[3|4|5|7|8]选择其中一个{4,8},\d从[0-9]选择
                {4,8}匹配次数4~8    ，java中/表示转义，所以在正则表达式中//匹配/,/匹配""*/
            //验证手机号码格式是否正确
        }else if(!mobile.matches("^1[3|4|5|7|8][0-9]\\d{4,8}$")){
            Toast.makeText(context, "手机号输入有误，请重新输入", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }

    // 保留两位小数
    public static String strToDouble(String value) {
        DecimalFormat df = new DecimalFormat("0.00");
        double d = Double.valueOf(value);
        return df.format(d);
    }

    /**
     * 判断字符串是否是数字(0.0)
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        char[] p = str.toCharArray();
        for (int i = 0; i < p.length; i++) {
            if (!isNum("" + p[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNum(String str) {
        Pattern pattern = Pattern.compile("[0-9.]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    // 根据秒显示时分秒
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}
