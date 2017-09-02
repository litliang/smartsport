package top.smartsport.www.bean;

import android.content.Context;
import android.content.SharedPreferences;
import top.smartsport.www.base.BaseApplication;
import top.smartsport.www.utils.JsonUtil;

/**
 * Created by Aaron on 2017/7/4.
 */

public class AuthInfo {
    private String app_key;
    private String authorize_code;

    public String getApp_key() {
        return app_key;
    }

    public String getAuthorize_code() {
        return authorize_code;
    }

    private static AuthInfo authInfo;
    public static AuthInfo newInstance(){
        if(authInfo==null){
            authInfo = new AuthInfo();
        }
        return authInfo;
    }

    private static final String DEFAULT = "DEFAULT";
    public static void setAuthInfo(AuthInfo authInfo) {
        AuthInfo.authInfo = authInfo;
        AuthInfo.save(authInfo, AuthInfo.class);
    }

    public static <T> void save(T t, Class<T> clazz) {
        save(t, DEFAULT, clazz);
    }

    public static <T> void save(T t, String key, Class<T> clazz) {
        SharedPreferences preferences = BaseApplication.getApplication().getSharedPreferences(clazz.getName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (t == null) {
            editor.remove(key);
        } else {
            editor.putString(key, JsonUtil.entityToJson(t));
        }
        editor.commit();
    }
}

