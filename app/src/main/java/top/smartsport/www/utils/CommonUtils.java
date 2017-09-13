package top.smartsport.www.utils;

/**
 * deprecation:通用工具类
 * author:AnYB
 * time:2017/9/24
 */
public class CommonUtils {


    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
