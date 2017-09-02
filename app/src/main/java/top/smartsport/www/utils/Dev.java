package top.smartsport.www.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.JsonElement;
import intf.FunCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import top.smartsport.www.H;
import top.smartsport.www.O;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.*;
import top.smartsport.www.utils.MD5Util;
import top.smartsport.www.utils.SPUtils;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 2017/7/11.
 * 登录
 */
public class Dev {
    private String imei; //获取机器唯一标识
    private String mdImei; //唯一标识加密
    private RegInfo regInfo;
    private AuthInfo authInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;
    private String phone;
    private String psd;
    private Activity aty;
    private FunCallback cb;

    public void reg(Activity aty, FunCallback cb) {
        this.cb = cb;
        this.aty = aty;
        TelephonyManager tm = (TelephonyManager) aty.getSystemService(Context.TELEPHONY_SERVICE);
        //获取手机imei
        imei = tm.getDeviceId();
        getRegInfo(imei);

    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    RegInfo.setRegInfo(regInfo);
                    Log.i("___regInfo","regInfo");
                    break;
                case 2:
                    AuthInfo.setAuthInfo(authInfo);
                    Log.i("___authInfo","authInfo");
                    break;
                case 3:
                    TokenInfo.setTokenInfo(tokenInfo);
                    Log.i("____tokenInfo","tokenInfo");
                    break;
            }
        }
    };

    /**
     * 获取register接口
     */

    public void getRegInfo(String imei) {
        mdImei = MD5Util.encrypt(imei);
        JSONObject json = new JSONObject();
        try {
            json.put("imei",imei+"");
            json.put("code",mdImei);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(H.HOST + H.authed, json, new MyCallBack<String>() {


            @Override
            protected void onFailure(String message) {
                Log.i("_______++", message + "");
                Toast.makeText(aty,message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(NetEntity entity) {
                Log.i("________", entity.getStatus() + "__________" + entity.getErrno() + "");
                regInfo = entity.toObj(RegInfo.class);
//                RegInfo.setRegInfo(regInfo);//保存对象
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);

                getAuthInfo();

            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });
    }


    /**
     * 获取授权接口
     */
    public void getAuthInfo() {
        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getAuthorize_url();

        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state", state);
            json.put("response_type", "code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                Log.i("_______++", message + "");
                Toast.makeText(aty,message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(NetEntity entity) {
                Log.i("________++___",entity.getStatus());
                authInfo = entity.toObj(AuthInfo.class);
//                AuthInfo.setAuthInfo(authInfo);
                Message msg = new Message();
                msg.what = 2;
                mHandler.sendMessage(msg);
                getTokenInfo();
            }
        });

    }



    //获取Access_token

    public void getTokenInfo(){
        url = regInfo.getToken_url();
        client_id = regInfo.getApp_key();
        String client_secret = regInfo.getApp_secret();
        String grant_type = "authorization_code";
        String code = authInfo.getAuthorize_code();
        String state = regInfo.getSeed_secret();
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("grant_type",grant_type);
            json.put("client_secret",client_secret);
            json.put("code",code);
            json.put("state",state);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                Log.i("________**",message+"");
                Toast.makeText(aty,message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(NetEntity entity) {
                Log.i("______!!",entity.getStatus());
                tokenInfo = entity.toObj(TokenInfo.class);
//                TokenInfo.setTokenInfo(tokenInfo);
                access_token = tokenInfo.getAccess_token();
                Message msg = new Message();
                msg.what = 3;
                mHandler.sendMessage(msg);
                loadCityList();
                cb.onCallback(null,null);
            }
        });
    }


/**
 * 获取城市和热门城市
 * */
    private List<HotCity> hotCityList = new ArrayList<>();
    private List<ComCity> comCityList = new ArrayList<>();
    private void loadCityList() {
        String url = regInfo.getSource_url();
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","getCitys");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        X.Post(url, json, new MyCallBack<String>() {


            @Override
            protected void onFailure(String message) {
                Toast.makeText(aty,message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(NetEntity entity) {
                JsonElement jsonElement = entity.getData();
                try {
                    JSONObject json =  new JSONObject(jsonElement.toString());
                    JSONArray hotCity = json.optJSONArray("hotCity");
                    JSONArray cityList = json.optJSONArray("cityList");
                    for(int i =0;i<hotCity.length();i++){
                        JSONObject obj = (JSONObject) hotCity.get(i);
                        String title = obj.optString("title");
                        String area_id = obj.optString("area_id");
                        String pid = obj.optString("pid");
                        HotCity c = new HotCity();
                        c.setArea_id(area_id);
                        c.setTitle(title);
                        c.setPid(pid);
                        hotCityList.add(c);
                    }

                    for(int i =0;i<cityList.length();i++){
                        JSONObject obj = (JSONObject) cityList.get(i);
                        String title = obj.optString("title");
                        String area_id = obj.optString("area_id");
                        String pid = obj.optString("pid");
                        ComCity c = new ComCity();
                        c.setArea_id(area_id);
                        c.setTitle(title);
                        c.setPid(pid);
                        comCityList.add(c);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                O.setComAreas(comCityList);
                O.setHotAreas(hotCityList);
            }
        });
    }


}
