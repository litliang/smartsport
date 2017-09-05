package top.smartsport.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;

import intf.FunCallback;
import org.xutils.view.annotation.ContentView;

import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.utils.Dev;
import top.smartsport.www.utils.SPUtils;

/**
 * Created by Aaron on 2017/7/11.
 * 欢迎页
 */
@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                String phone = (String) SPUtils.get(getBaseContext(),"USER","");

                if (SPUtils.getBoolean(getBaseContext(), "welcomeGuide", "isFirst")) {
                    if(!phone.equals("")){
                        goActivity(MainActivity.class);
                        finish();
                        return false;
                    }else {
                        goActivity(LoginActivity.class);
                    }
                } else {
                    goActivity(GuiderActivity.class);
                    SPUtils.putBoolean(getBaseContext(), "welcomeGuide", "isFirst", true);
                }
                finish();
                return false;
            }

            private void goActivity(Class loginActivityClass) {
                startActivity(new Intent(getBaseContext(),loginActivityClass));
            }
        }).sendEmptyMessageDelayed(0, 2000);


    }



}
