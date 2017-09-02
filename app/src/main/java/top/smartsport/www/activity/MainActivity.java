package top.smartsport.www.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.H;
import top.smartsport.www.O;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.AuthInfo;
import top.smartsport.www.bean.BSSZInfo;
import top.smartsport.www.bean.BSZTInfo;
import top.smartsport.www.bean.ComCity;
import top.smartsport.www.bean.HotCity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.Province;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.SSJBInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.fragment.BSFragment;
import top.smartsport.www.fragment.QXFragment;
import top.smartsport.www.fragment.WDFragment;
import top.smartsport.www.fragment.ZXFragment;
import top.smartsport.www.utils.MD5Util;
import top.smartsport.www.utils.SPUtils;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/6/30.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private String mCurrentFragmentTag = ZXFragment.class.getSimpleName();
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ZXFragment zxFragment;
    private QXFragment qxFragment;
    private BSFragment bsFragment;
    private WDFragment wdFragment;
    @ViewInject(R.id.main_tabs_home)
    private RadioButton main_tabs_home;
    @ViewInject(R.id.main_bottom_tabs)
    private RadioGroup main_bottom_tabs;

    private String imei; //获取机器唯一标识
    private String mdImei; //唯一标识加密
    private RegInfo regInfo;
    private AuthInfo authInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    private String isLogin;

    public static final String TAG = MainActivity.class.getName();

    @Override
    public View getTopBar() {
        return null;
    }

    @Override
    public void featureNoTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    @Override
    protected void initView() {
//        isLogin = (String) getObj("Login");
//        if(null==isLogin){
//            getIMEI();
//        }
        new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {

                if (SPUtils.getBoolean(getBaseContext(), "welcomeGuide", "isFirst")) {
                    getIMEI();
                } else {

                }
                return false;
            }
        }).sendEmptyMessageDelayed(0,0);

        showLeft(false);//隐藏返回键

        fragmentManager = getSupportFragmentManager();

        zxFragment = ZXFragment.newInstance();
        qxFragment = QXFragment.newInstance();
        bsFragment = BSFragment.newInstance();
        wdFragment = WDFragment.newInstance();
        addFragment(zxFragment, ZXFragment.TAG);
        addFragment(qxFragment, QXFragment.TAG);
        addFragment(bsFragment, BSFragment.TAG);
        addFragment(wdFragment, WDFragment.TAG);
        main_bottom_tabs.setOnCheckedChangeListener(MainActivity.this);
        // 默认选中第一个标签页
        ((RadioButton) findViewById(R.id.main_tabs_home)).setChecked(true);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_tabs_home://资讯

                changeTabContent(zxFragment, ZXFragment.TAG);
                break;
            case R.id.main_tabs_qx://青训
                changeTabContent(qxFragment, QXFragment.TAG);
                break;
            case R.id.main_tabs_ss://赛事
                changeTabContent(bsFragment, BSFragment.TAG);
                break;
            case R.id.main_tabs_me://我的
                String phone = (String) SPUtils.get(getApplicationContext(),"USER","");
                if(phone==null||phone.equals("")){
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }else {
                    changeTabContent(wdFragment, WDFragment.TAG);
                }break;

        }
    }



    /**
     * 向FragmentManager中添加Fragment
     *
     * @param fragment 要添加的Fragment
     * @param tag      给Fragment绑定的Tag
     */
    private void addFragment(Fragment fragment, String tag) {
        if (fragment.isAdded()) {
            return;
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_content, fragment, tag);
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }

    /**
     * 切换Tab
     *
     * @param fragment Fragment的实例
     * @param tag      要绑定的Tag
     */
    private void changeTabContent(Fragment fragment, String tag) {

        fragmentTransaction = fragmentManager.beginTransaction();
        if (getCurrentFragment() != null) {
            getCurrentFragment().onPause();
            fragmentTransaction.hide(getCurrentFragment());
        }
        fragment.onResume();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
        mCurrentFragmentTag = tag;
    }

    /**
     * 获取当前正在显示的Fragment
     *
     * @return Fragment 当前正在显示的Fragment
     */
    private Fragment getCurrentFragment() {
        return fragmentManager.findFragmentByTag(mCurrentFragmentTag);
    }


    private void getIMEI(){
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
                getCity();
                getChoice();
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
     * 获取二级城市
     * */
    private void getCity(){
        String url = regInfo.getSource_url();
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","getCounties");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {

            }

            @Override
            public void onSuccess(NetEntity entity) {
                List<Province> areas = entity.toList(Province.class);
                O.setAreas(areas);
            }
        });
    }

    /**
     * 获取筛选条件
     * */
    private List<SSJBInfo> ssjbInfoList = new ArrayList<>();
    private List<BSZTInfo> bsztInfoList = new ArrayList<>();
    private List<BSSZInfo> bsszInfoList = new ArrayList<>();
    private void getChoice(){
        String url = regInfo.getSource_url();
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","getMatchFilter");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {

            }

            @Override
            public void onSuccess(NetEntity entity) {
                JsonElement jsonElement = entity.getData();
                try {
                    JSONObject json = new JSONObject(jsonElement.toString());
                    JSONArray levelList = json.optJSONArray("level");
                    JSONArray statusList = json.optJSONArray("status");
                    JSONArray typeList = json.optJSONArray("type");
                    for(int i =0;i<levelList.length();i++){
                        JSONObject obj = (JSONObject) levelList.get(i);
                        String id = obj.optString("id");
                        String name = obj.optString("name");
                        SSJBInfo info = new SSJBInfo();
                        info.setId(id);
                        info.setName(name);
                        ssjbInfoList.add(info);

                    }
                    for(int i=0;i<statusList.length();i++){
                        JSONObject obj = (JSONObject) statusList.get(i);
                        String id = obj.optString("id");
                        String name = obj.optString("name");
                        BSZTInfo info = new BSZTInfo();
                        info.setId(id);
                        info.setName(name);
                        bsztInfoList.add(info);

                    }

                    for(int i=0;i<typeList.length();i++){
                        JSONObject obj = (JSONObject) typeList.get(i);
                        String id = obj.optString("id");
                        String name = obj.optString("name");
                        BSSZInfo info = new BSSZInfo();
                        info.setId(id);
                        info.setName(name);
                        bsszInfoList.add(info);

                    }
                    O.setSSJB(ssjbInfoList);
                    O.setBSZT(bsztInfoList);
                    O.setBSSZ(bsszInfoList);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
