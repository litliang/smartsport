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

public class TokenInfo implements Serializable {
    private String access_token;
    private String expires_in;
    private String token_type;
    private String scope;
    private String refresh_token;

    public String getAccess_token() {
        return access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getScope() {
        return scope;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    private static TokenInfo tokenInfo;
    public static TokenInfo newInstance(){
        if(tokenInfo==null){
            tokenInfo = (TokenInfo) SerialUtil.getObject("tokeninfo");
            if (tokenInfo == null) {
                tokenInfo = new TokenInfo();
            }
        }
        return tokenInfo;
    }

    private static final String DEFAULT = "DEFAULT";
    public static void setTokenInfo(TokenInfo tokenInfo) {
        TokenInfo.tokenInfo = tokenInfo;
        TokenInfo.save(tokenInfo, TokenInfo.class);
        SerialUtil.saveObject("tokeninfo",tokenInfo);
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
