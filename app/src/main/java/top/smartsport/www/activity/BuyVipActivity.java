package top.smartsport.www.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;

import java.io.Serializable;
import java.util.Map;

import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
@ContentView(R.layout.activity_buy_vip)
public class BuyVipActivity extends BaseActivity {

    @Override
    protected void initView() {
        setTitle("购买会员");
        ((TextView)findViewById(R.id.sign_up_my_tv)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((TextView)findViewById(R.id.sign_up_my_tv)).setText("￥"+0.01);
                return false;
            }
        });
        ((TextView)findViewById(R.id.sign_up_my_tv)).setText("￥"+getIntent().getStringExtra("total"));
        findViewById(R.id.sign_up_pay_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price = ((TextView)findViewById(R.id.sign_up_my_tv)).getText().toString().replace("￥","");
                Map map= MapBuilder.build().add("total",price+"").add("type","3").add("product_id","1").get();

                startActivity(new Intent(getBaseContext(),ActivityOrderConfirm.class).putExtra("data", (Serializable) map));
            }
        });
    }
}
