package top.smartsport.www.activity;

import android.view.View;
import android.widget.ListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import app.base.MapConf;
import intf.FunCallback;
import intf.JsonUtil;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;

/**
 * Created by Aaron on 2017/7/18.
 * 消息
 */
@ContentView(R.layout.activity_message)
public class MessageActivity extends BaseActivity{

    private RegInfo regInfo;
    private TokenInfo tokenInfo;
    private String client_id;
    private String state;
    private String access_token;
    @ViewInject(R.id.lv)
    ListView listView;

    @Override
    protected void initView() {
        back();
        findViewById(R.id.empty).setVisibility(View.GONE);

        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        access_token = tokenInfo.getAccess_token();
        getData();
    }

    private void getData() {
        final MapBuilder json = MapBuilder.build();
        json.add("client_id", client_id);
        json.add("state", state);
        json.add("access_token", access_token);
        json.add("action", "getMyMessage");

        BaseActivity.callHttp(json.get(), getView(R.id.content), new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {
                String data = ((NetEntity)result).getData().toString();
                List list = (List) app.base.JsonUtil.extractJsonRightValue(JsonUtil.findJsonLink("message",data).toString());
                MapConf mc = MapConf.with(getBaseContext())
                        .pair("message-header_url->iv_head_icon")
                        .pair("message-title->title")
                        .pair("message-content->content")
                        .pair("message-ctime->time")
                        .source(R.layout.message_item);
                MapConf.with(getBaseContext()).conf(mc).source(list, listView).toView();
            }

            @Override
            public void onFailure(Object result, List object) {
            }

            @Override
            public void onCallback(Object result, List object) {
            }
        });
    }

}
