package top.smartsport.www.activity;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/12.
 * 忘记密码
 */
@ContentView(R.layout.activity_forgetpsw)
public class ForgetPSWActivity extends BaseActivity {
    @ViewInject(R.id.forget_edit_phone)
    private EditText forget_edit_phone;
    @ViewInject(R.id.forget_text_send_code)
    private TextView forget_text_send_code;
    @ViewInject(R.id.forget_next_edit_psw)
    private EditText forget_next_edit_psw;
    @ViewInject(R.id.forget_next_edit_psw_again)
    private EditText forget_next_edit_psw_again;
    @ViewInject(R.id.forget_next_edit_code)
    private EditText forget_next_edit_code;

    private String phone;
    private String psd;
    private String psd_again;
    private String code;

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;
    private TimeCount time;//获取短信倒计时间


    @Override
    protected void initView() {
        forget_text_send_code = (TextView) findViewById(R.id.forget_text_send_code);
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        time= new TimeCount(60000,1000);
    }

    @Event(value = {R.id.forget_text_send_code,R.id.forget_next_btn_reg})
    private void getEvent(View v){
        switch (v.getId()){
            case R.id.forget_text_send_code:
                getSms();
                break;
            case R.id.forget_next_btn_reg:
                forget();
                break;
        }
    }

    /**
     * 确认提交按钮
     * */
    private void forget(){
        phone = forget_edit_phone.getText().toString().trim();
        code = forget_next_edit_code.getText().toString().trim();
        psd = forget_next_edit_psw.getText().toString().trim();
        psd_again= forget_next_edit_psw_again.getText().toString().trim();
        if(phone.isEmpty()){
            time.onFinish();
            showToast("手机号码不能为空");
            return;
        }
        if(code.isEmpty()){
            time.onFinish();
            showToast("验证码不能为空");
            return;
        }
        if(psd.isEmpty()){
            time.onFinish();
            showToast("密码不能为空");
            return;
        }
        if(psd.length()<6){
            time.onFinish();
            showToast("密码长度不能小于6位");
            return;
        }
        if(psd_again.isEmpty()){
            time.onFinish();
            showToast("重复密码不能为空");
        }
        if(!psd.equals(psd_again)){
            time.onFinish();
            showToast("两次密码输入不一致");
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","forget");
            json.put("mobile",phone);
            json.put("vericode",code);
            json.put("password",psd);
            json.put("repassword",psd_again);
            json.put("use_type","forget");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
                time.onFinish();
                Log.i("________()____",message);
            }

            @Override
            public void onSuccess(NetEntity entity) {
                time.onFinish();
                showToast(entity.getMessage());
                goActivity(LoginActivity.class);
                finish();

                Log.i("________",entity.getMessage());
            }
        });

    }

    /**
     * 自定义一个倒计时内部类
     */
    public class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            forget_text_send_code.setText(millisUntilFinished / 1000 + "秒");
            forget_text_send_code.setEnabled(false);
        }

        @Override
        public void onFinish() {
            forget_text_send_code.setEnabled(true);
            forget_text_send_code.setText("验证");
        }
    }


    /**
     * 获取验证码接口
     * */
    private void getSms(){
        phone = forget_edit_phone.getText().toString().trim();
        if(phone.isEmpty()){
            showToast("请输入手机号");
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","sendSmsCode");
            json.put("mobile",phone);
            json.put("use_type","forget");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);

                Log.i("________()____",message);
            }

            @Override
            public void onSuccess(NetEntity entity) {
                showToast(entity.getErrmsg());
                time.start();

                Log.i("________",entity.getMessage());
            }
        });

    }
}
