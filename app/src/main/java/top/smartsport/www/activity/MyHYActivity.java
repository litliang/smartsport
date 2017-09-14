package top.smartsport.www.activity;

import android.content.Intent;
import android.view.View;

import org.xutils.view.annotation.ContentView;

import java.io.Serializable;
import java.util.Map;

import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;

/**
 * Created by Aaron on 2017/7/18.
 * 我的会员
 */
@ContentView(R.layout.activity_my_vip)
public class MyHYActivity extends BaseActivity{

    @Override
    protected void initView() {
        back();
        findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map= MapBuilder.build().add("total","199").add("type","3").add("product_id","1").get();

                startActivity(new Intent(getBaseContext(),ActivityOrderConfirm.class).putExtra("data", (Serializable) map));
            }
        });
    }
}
