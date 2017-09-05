package top.smartsport.www.bean;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

import top.smartsport.www.base.BaseApplication;
import top.smartsport.www.utils.JsonUtil;
import top.smartsport.www.utils.SerialUtil;

/**
 * Created by Aaron on 2017/7/4.
 */

public class RegInfo implements Serializable {
    static final long serialVersionUID = 1L;
    private String app_key;
    private String app_secret;
    private String authorize_url;
    private String refresh_url;
    private String seed_secret;
    private String source_url;
    private String token_url;

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public void setApp_secret(String app_secret) {
        this.app_secret = app_secret;
    }

    public void setAuthorize_url(String authorize_url) {
        this.authorize_url = authorize_url;
    }

    public void setRefresh_url(String refresh_url) {
        this.refresh_url = refresh_url;
    }

    public void setSeed_secret(String seed_secret) {
        this.seed_secret = seed_secret;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public void setToken_url(String token_url) {
        this.token_url = token_url;
    }

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

    public static RegInfo newInstance() {
        if (regInfo == null) {
            regInfo = (RegInfo) get("reginfo", RegInfo.class);
            if (regInfo == null) {
                regInfo = new RegInfo();
            }
        }
        return regInfo;
    }

    private static final String DEFAULT = "DEFAULT";

    public static void setRegInfo(RegInfo regInfo) {
        RegInfo.regInfo = regInfo;

        RegInfo.save(regInfo, "reginfo", RegInfo.class);
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

    public static <T> T get(String key, Class<T> clazz) {
        SharedPreferences preferences = BaseApplication.getApplication().getSharedPreferences(clazz.getName(), Context.MODE_PRIVATE);
        String json = preferences.getString(key, "");
        return JsonUtil.jsonToEntity(json, clazz);
    }
}
