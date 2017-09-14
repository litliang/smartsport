package top.smartsport.www.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import app.base.JsonUtil;
import app.base.MapConf;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.utils.SPUtils;

/**
 * Created by Aaron on 2017/7/18.
 * 我的会员
 */
@ContentView(R.layout.activity_my_vip)
public class MyHYActivity extends BaseActivity{

    @Override
    protected void initView() {
        back();
        BaseActivity.callHttp(MapBuilder.build().add("action", "getUserInfo").get(), new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {

            }

            @Override
            public void onFailure(Object result, List object) {

            }

            @Override
            public void onCallback(Object result, List object) {
                String data = ((NetEntity)result).getData().toString();

                SPUtils.put(getBaseContext(), "getUserInfo", data);
                if(JsonUtil.findJsonLink("is_vip",data).toString().equals("0")){
                    findViewById(R.id.btn_buy).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.btn_buy).setVisibility(View.GONE);
                }
            }
        });
        findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map= MapBuilder.build().add("total","199").add("type","3").add("product_id","1").get();

                startActivity(new Intent(getBaseContext(),ActivityOrderConfirm.class).putExtra("data", (Serializable) map));
            }
        });
    }
}
