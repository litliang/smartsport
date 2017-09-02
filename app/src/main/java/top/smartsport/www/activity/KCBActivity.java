package top.smartsport.www.activity;

import android.view.View;

import intf.FunCallback;
import intf.MapBuilder;
import intf.QXFunCallback;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

import java.util.List;

/**
 * Created by Aaron on 2017/7/25.
 * 课程表
 */
@ContentView(R.layout.activity_kcb)
public class KCBActivity extends BaseActivity {


    @Override
    protected void initView() {

        callHttp(MapBuilder.build().add("match_id", "1").add("action","getSchedule").get(),  new QXFunCallback() {
            @Override
            public void onSuccess(NetEntity result, List<Object> object) {

            }

            @Override
            public void onFailure(String result, List<Object> object) {

            }

            @Override
            public void onCallback(NetEntity result, List<Object> object) {

            }
        });
    }

    /**
     * 获取接口
     */
    private void getData() {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", getClient_id());
            json.put("state", getState());
            json.put("access_token", getAccess_token());
            json.put("action", "getSchedule");
            json.put("match_id", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        X.Post(getUrl(), json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {

            }

            @Override
            public void onSuccess(NetEntity entity) {

            }
        });
    }


}
