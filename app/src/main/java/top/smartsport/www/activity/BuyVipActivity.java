package top.smartsport.www.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
@ContentView(R.layout.activity_buy_vip)
public class BuyVipActivity extends BaseActivity {

    @Override
    protected void initView() {
        ((TextView)findViewById(R.id.sign_up_my_tv)).setText("￥"+getIntent().getStringExtra("total"));
        findViewById(R.id.sign_up_pay_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
