package top.smartsport.www.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.smartsport.www.H;
import top.smartsport.www.O;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.AuthInfo;
import top.smartsport.www.bean.ComCity;
import top.smartsport.www.bean.Data;
import top.smartsport.www.bean.HotCity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.utils.MD5Util;
import top.smartsport.www.utils.SPUtils;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/11.
 * 登录
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    private String imei; //获取机器唯一标识
    private String mdImei; //唯一标识加密
    private RegInfo regInfo;
    private AuthInfo authInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;
    @ViewInject(R.id.login_edit_phone)
    private EditText login_edit_phone;
    private String phone;
    @ViewInject(R.id.login_edit_psd)
    private EditText login_edit_psd;
    private String psd;


    @Override
    public void featureNoTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    @Override
    protected void initView() {

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
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

    private void getRegInfo(String imei) {
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
                showToast(message);
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
    private void getAuthInfo() {
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
                showToast(message);
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

    private void getTokenInfo(){
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
                showToast(message);
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
            }
        });
    }

    @Event(value = {R.id.login_btn_login,R.id.login_text_all_login,R.id.login_text_reg,R.id.login_text_forget})
    private void getEvent(View view){
        switch (view.getId()){
            case R.id.login_btn_login://手机密码登录
                login();
                break;
            case R.id.login_text_all_login://游客登录
                goActivity(MainActivity.class);
                finish();
//                goActivity(ChoiceCityActivity.class);
//                goActivity(WSZLActivity.class);
                break;
            case R.id.login_text_reg://注册界面
                goActivity(RegActivity.class);
                break;
            case R.id.login_text_forget://忘记密码
                goActivity(ForgetPSWActivity.class);
                break;
            default:
                break;
        }
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
                showToast(message);
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

    /**
     * 登录
     * */
    private void login(){
        phone = login_edit_phone.getText().toString().trim();
        psd = login_edit_psd.getText().toString().trim();
        String url = regInfo.getSource_url();

        if(phone.isEmpty()||psd.isEmpty()){
            showToast("手机号或密码不能为空");
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","login");
            json.put("mobile",phone);
            json.put("password",psd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
            }

            @Override
            public void onSuccess(NetEntity entity) {
                showToast(entity.getMessage());
                SPUtils.put(LoginActivity.this,"USER",phone);

                Bundle bundle = new Bundle();
                bundle.putString("Login","isLogin");
                goActivity(MainActivity.class,bundle);
                finish();
            }
        });
    }

}
