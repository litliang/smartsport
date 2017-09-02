package top.smartsport.www.activity;

import android.view.View;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.adapter.WDQDAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.bean.WDQDInfo;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/28.
 * 修改球队
 */
@ContentView(R.layout.activity_changeqd)
public class ChangeQDActivity extends BaseActivity {
    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    @ViewInject(R.id.wd_qd_listView)
    private ListView wd_qd_listView;

    private WDQDAdapter wdqdAdapter;


    @Override
    protected void initView() {
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        wdqdAdapter = new WDQDAdapter(this);
        wd_qd_listView.setAdapter(wdqdAdapter);

        getData();
    }

    /**
     * 获取我的球队列表
     * */
    private List<WDQDInfo> wdqdInfoList;
    private void getData(){
        wdqdInfoList = new ArrayList<>();
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","getMyTeamList");
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
                wdqdInfoList = entity.toList(WDQDInfo.class);
                wdqdAdapter.addAll(wdqdInfoList);
            }
        });
    }
}
