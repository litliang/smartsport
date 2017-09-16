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
        String isvip = (String) SPUtils.get(getBaseContext(), "is_vip","");

        if(!isvip.equals("1")){
            findViewById(R.id.btn_buy).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.btn_buy).setVisibility(View.INVISIBLE);
            findViewById(R.id.btn_buy).setEnabled(false);
        }
        findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getBaseContext(),BuyVipActivity.class).putExtra("total","199"),0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshPaystatus();
    }

    private void refreshPaystatus() {
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
                SPUtils.put(getBaseContext(), "is_vip", JsonUtil.findJsonLink("is_vip",data).toString());
                if(JsonUtil.findJsonLink("is_vip",data).toString().equals("0")){
                    findViewById(R.id.btn_buy).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.btn_buy).setVisibility(View.INVISIBLE);
                    findViewById(R.id.btn_buy).setEnabled(false);
                }
            }
        });

    }
}
