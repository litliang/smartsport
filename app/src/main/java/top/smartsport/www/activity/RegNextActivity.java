package top.smartsport.www.activity;

import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.H;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/12.
 * 注册
 */
@ContentView(R.layout.activity_regnext)
public class RegNextActivity extends BaseActivity{
    public static String TAG = "RegNextActivity";
    @ViewInject(R.id.reg_next_edit_psw)
    private EditText reg_next_edit_psw;//密码
    @ViewInject(R.id.reg_next_edit_psw_again)
    private EditText reg_next_edit_psw_again;//重复密码
    @ViewInject(R.id.reg_next_edit_user)
    private EditText reg_next_edit_user;//用户名
    private String username;
    private String psd;
    private String psd_again;

    private String phone;
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

        phone = getData(RegNextActivity.TAG,"");
    }

    @Event(value = {R.id.reg_next_btn_reg})
    private void getEvent(View view){
        switch (view.getId()){
            case R.id.reg_next_btn_reg:
                reg();
                break;
        }
    }

    /**
     * 注册
     * */
    private void reg(){
        username = reg_next_edit_user.getText().toString().trim();
        psd = reg_next_edit_psw.getText().toString().trim();
        psd_again =reg_next_edit_psw_again.getText().toString().trim();

        if(phone.isEmpty()){
            return;
        }
        if(username.isEmpty()){
            showToast("用户名不能为空");
            return;
        }
        if(psd.isEmpty()){
            showToast("密码不能为空");
            return;
        }
        if(psd.length()<6){
            showToast("密码长度不能小于6位");
            return;
        }
        if(psd_again.isEmpty()){
            showToast("重复密码不能为空");
        }
        if(!psd.equals(psd_again)){
            showToast("两次密码输入不一致");
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","register");
            json.put("mobile",phone);
            json.put("username",username);
            json.put("password",psd);
            json.put("repassword",psd_again);
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
                goActivity(WSZLActivity.class);
            }
        });

    }
}
