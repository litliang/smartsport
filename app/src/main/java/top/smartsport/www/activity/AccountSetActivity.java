package top.smartsport.www.activity;

import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import app.base.MapConf;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.utils.ActivityStack;
import top.smartsport.www.utils.SPUtils;

/**
 * Created by Aaron on 2017/7/18.
 * 账户设置
 */
@ContentView(R.layout.activity_accountset)
public class AccountSetActivity extends BaseActivity {
    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    @Override
    protected void initView() {
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        String data = (String) SPUtils.get(getBaseContext()
                , "getUserInfo", "");
        MapConf.build().addPair("header_url", R.id.account_header).addPair("username", R.id.username).addPair("truename", R.id.truename).addPair("age", R.id.account_age).addPair("sex", R.id.account_sex).addPair("height", R.id.account_height).addPair("weight", R.id.account_weight).addPair("leg", R.id.leg).addPair("address", R.id.account_jz)
//                .addPair("soccer_age",R.id.soccer_age")"" +
                .addTackle(new MapConf.Tackle() {
                    @Override
                    public void tackleBefore(Object item, Object value, String name, View convertView, View theView) {

                    }

                    @Override
                    public void tackleAfter(Object item, Object value, String name, View convertView, View theView) {
                        if (name.equals("height")) {
                            value += " cm";
                        } else if (name.equals("weight")) {
                            value += " kg";
                        } else if (name.equals("sex")) {
                            if (value.equals("0")) {
                                value = "女";
                            } else if (value.equals("1")) {
                                value = "男";
                            }
                        }
                        if(theView instanceof  TextView) {
                            ((TextView) theView).setText(value.toString());
                        }
                    }
                }).with(getBaseContext()).source(app.base.JsonUtil.extractJsonRightValue(data), getWindow().getDecorView()).match();
    }

    @Event(value = {R.id.account_btn_login_out})
    private void getEvent(View v) {
        switch (v.getId()) {
            case R.id.account_btn_login_out:
                loginOut();
                break;
        }
    }

    /**
     * 用户退出
     */
    private void loginOut() {
        SPUtils.put(getBaseContext(), "USER", "");
        goActivity(LoginActivity.class);
        finish();
        ActivityStack.getInstance().findActivityByClass(MainActivity.class).finish();
//        JSONObject json = new JSONObject();
//        try {
//            json.put("client_id", client_id);
//            json.put("state", state);
//            json.put("access_token", access_token);
//            json.put("action", "logout");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        X.Post(url, json, new MyCallBack<String>() {
//            @Override
//            protected void onFailure(String message) {
//                showToast(message);
//            }
//
//            @Override
//            public void onSuccess(NetEntity entity) {
//
//            }
//        });
    }
}
