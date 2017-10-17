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
import java.util.Random;

import app.auto.DrawerLeftActivity;
import app.base.framework.CrashHandler;
import top.smartsport.www.H;
import top.smartsport.www.O;
import top.smartsport.www.R;
import top.smartsport.www.actions.DataInfo;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.AuthInfo;
import top.smartsport.www.bean.ComCity;
import top.smartsport.www.bean.HotCity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.Province;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.fragment.BSV4Fragment;
import top.smartsport.www.fragment.QXV4Fragment;
import top.smartsport.www.fragment.WDV4Fragment;
import top.smartsport.www.fragment.ZXV4Fragment;
import top.smartsport.www.utils.MD5Util;
import top.smartsport.www.utils.SPUtils;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/6/30.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private String mCurrentFragmentTag = ZXV4Fragment.class.getSimpleName();
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ZXV4Fragment zxFragment;
    private QXV4Fragment qxFragment;
    private BSV4Fragment bsFragment;
    private WDV4Fragment wdFragment;
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
//                    getIMEI();
                    loadCityList();
                    getCity();
                    DataInfo.getChoice(regInfo, client_id, state, access_token);
                    DataInfo.getCourseChoice(regInfo, client_id, state, access_token);
                    DataInfo.getTeachingPlanChoice(regInfo, client_id, state, access_token);
                } else {

                }
                return false;
            }
        }).sendEmptyMessageDelayed(0, 0);

        showLeft(false);//隐藏返回键

        fragmentManager = getSupportFragmentManager();

        zxFragment = ZXV4Fragment.newInstance();
        qxFragment = QXV4Fragment.newInstance();
        bsFragment = BSV4Fragment.newInstance();
        wdFragment = WDV4Fragment.newInstance();
        addFragment(zxFragment, ZXV4Fragment.TAG);
        addFragment(qxFragment, QXV4Fragment.TAG);
        addFragment(bsFragment, BSV4Fragment.TAG);
        addFragment(wdFragment, WDV4Fragment.TAG);
        main_bottom_tabs.setOnCheckedChangeListener(MainActivity.this);
        // 默认选中第一个标签页
        ((RadioButton) findViewById(R.id.main_tabs_home)).setChecked(true);
//        Epr.parseParam("Print(Clickactivityid($ss:123%s456))",(RadioButton) findViewById(R.id.main_tabs_home)).innerrun();
//        goActivity(ActivityTrainingDetails.class);
        CrashHandler.errorDialog();
        ((RadioButton) findViewById(R.id.main_tabs_me)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), DrawerLeftActivity.class));
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            wdFragment.initUserInfo();
        }
        currfragment.refresh();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_tabs_home://资讯

                changeTabContent(zxFragment, ZXV4Fragment.TAG);
                break;
            case R.id.main_tabs_qx://青训
                changeTabContent(qxFragment, QXV4Fragment.TAG);
                break;
            case R.id.main_tabs_ss://赛事
                changeTabContent(bsFragment, BSV4Fragment.TAG);
                break;
            case R.id.main_tabs_me://我的
                String phone = (String) SPUtils.get(getApplicationContext(), "USER", "");
                if (phone == null || phone.equals("")) {
                    startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 1);

                } else {
                    changeTabContent(wdFragment, WDV4Fragment.TAG);
                }
                break;

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

    BaseV4Fragment currfragment;

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
        currfragment = (BaseV4Fragment) fragment;
    }

    /**
     * 获取当前正在显示的Fragment
     *
     * @return Fragment 当前正在显示的Fragment
     */
    private Fragment getCurrentFragment() {
        return fragmentManager.findFragmentByTag(mCurrentFragmentTag);
    }


    private void getIMEI() {
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
                    Log.i("___regInfo", "regInfo");
                    break;
                case 2:
                    AuthInfo.setAuthInfo(authInfo);
                    Log.i("___authInfo", "authInfo");
                    break;
                case 3:
                    TokenInfo.setTokenInfo(tokenInfo);
                    Log.i("____tokenInfo", "tokenInfo");
                    break;
            }
        }
    };

    /**
     * 获取register接口
     */

    private void getRegInfo(String imei) {
        mdImei = MD5Util.encrypt(new Random().nextLong()+"");
        JSONObject json = new JSONObject();
        try {
            json.put("imei", imei + "");
            json.put("code", mdImei);
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
            json.put("client_id", client_id);
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
                Log.i("________++___", entity.getStatus());
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

    private void getTokenInfo() {
        url = regInfo.getToken_url();
        client_id = regInfo.getApp_key();
        String client_secret = regInfo.getApp_secret();
        String grant_type = "authorization_code";
        String code = authInfo.getAuthorize_code();
        String state = regInfo.getSeed_secret();
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("grant_type", grant_type);
            json.put("client_secret", client_secret);
            json.put("code", code);
            json.put("state", state);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                Log.i("________**", message + "");
                showToast(message);
            }

            @Override
            public void onSuccess(NetEntity entity) {
                Log.i("______!!", entity.getStatus());
                tokenInfo = entity.toObj(TokenInfo.class);
//                TokenInfo.setTokenInfo(tokenInfo);
                access_token = tokenInfo.getAccess_token();
                Message msg = new Message();
                msg.what = 3;
                mHandler.sendMessage(msg);

            }
        });
    }

    /**
     * 获取城市和热门城市
     */
    private List<HotCity> hotCityList = new ArrayList<>();
    private List<ComCity> comCityList = new ArrayList<>();

    private void loadCityList() {
        regInfo = RegInfo.newInstance();
        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getAuthorize_url();
        String url = regInfo.getSource_url();
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getCitys");
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
                    JSONObject json = new JSONObject(jsonElement.toString());
                    JSONArray hotCity = json.optJSONArray("hotCity");
                    JSONArray cityList = json.optJSONArray("cityList");
                    for (int i = 0; i < hotCity.length(); i++) {
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

                    for (int i = 0; i < cityList.length(); i++) {
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
     */
    private void getCity() {
        String url = regInfo.getSource_url();
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getCounties");
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

}
