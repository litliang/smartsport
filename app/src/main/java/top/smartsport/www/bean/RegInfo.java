package top.smartsport.www.bean;

import android.content.Context;
import android.content.SharedPreferences;

import top.smartsport.www.base.BaseApplication;
import top.smartsport.www.utils.JsonUtil;

/**
 * Created by Aaron on 2017/7/4.
 */

public class RegInfo {
    private String app_key;
    private String app_secret;
    private String authorize_url;
    private String refresh_url;
    private String seed_secret;
    private String source_url;
    private String token_url;


    public String getApp_key() {
        return app_key;
    }

    public String getApp_secret() {
        return app_secret;
    }

    public String getAuthorize_url() {
        return authorize_url;
    }

    public String getRefresh_url() {
        return refresh_url;
    }

    public String getSeed_secret() {
        return seed_secret;
    }

    public String getSource_url() {
        return source_url;
    }

    public String getToken_url() {
        return token_url;
    }


    private static RegInfo regInfo;
    public static RegInfo newInstance(){
        if(regInfo==null){
            regInfo = new RegInfo();
        }
        return regInfo;
    }

    private static final String DEFAULT = "DEFAULT";
    public static void setRegInfo(RegInfo regInfo) {
        RegInfo.regInfo = regInfo;
        RegInfo.save(regInfo, RegInfo.class);
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
