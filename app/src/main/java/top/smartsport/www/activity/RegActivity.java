package top.smartsport.www.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
 * 获取手机验证
 */
@ContentView(R.layout.activity_reg)
public class RegActivity extends BaseActivity {
    @ViewInject(R.id.reg_edit_code)
    private EditText reg_edit_code;
    @ViewInject(R.id.reg_edit_phone)
    private EditText reg_edit_phone;
    @ViewInject(R.id.reg_btn_next)
    private Button reg_btn_next;
    @ViewInject(R.id.reg_text_send_code)
    private TextView reg_text_send_code;

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    private String phone;
    private String code;

    private TimeCount time;//获取短信倒计时间


    @Override
    protected void initView() {
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        time= new TimeCount(60000,1000);
    }


    @Event(value = {R.id.reg_btn_next,R.id.reg_text_send_code})
    private void getEvent(View view){
        switch (view.getId()){
            case R.id.reg_btn_next://下一步
                onNext();
                break;
            case R.id.reg_text_send_code://验证

                getSms();
                break;
        }
    }

    /**
     * 获取验证码接口
     * */
    private void getSms(){
        phone = reg_edit_phone.getText().toString().trim();
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
            json.put("use_type","register");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {

                Log.i("________()____",message);
                showToast(message);
            }

            @Override
            public void onSuccess(NetEntity entity) {
                time.start();
                showToast(entity.getErrmsg());

                Log.i("________",entity.getMessage());
            }
        });

    }

    /**
     * 点击下一步按钮
     * */
    private void onNext(){
        phone = reg_edit_phone.getText().toString().trim();
        code = reg_edit_code.getText().toString().trim();
        if(phone.isEmpty()||code.isEmpty()){
            showToast("手机号或验证码不能为空");
            time.onFinish();
            return;
        }


        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","verifySmsCode");
            json.put("mobile",phone);
            json.put("vericode",code);
            json.put("use_type","register");
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
                Bundle bundle = new Bundle();
                bundle.putString(RegNextActivity.TAG,phone);

                goActivity(RegNextActivity.class,bundle);
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
            reg_text_send_code.setText(millisUntilFinished / 1000 + "秒");
            reg_text_send_code.setEnabled(false);
        }

        @Override
        public void onFinish() {
            reg_text_send_code.setEnabled(true);
            reg_text_send_code.setText("验证");
        }
    }

}
