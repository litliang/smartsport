package top.smartsport.www.activity;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import app.base.MapAdapter;
import app.base.MapConf;
import app.base.MapContent;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;

@ContentView(R.layout.all_jiaolian)
public class AllJiaolianActivity extends BaseActivity {
    @ViewInject(R.id.pullrefreshlistview)
    PullToRefreshListView mlistview;
    MapAdapter mapadapter;

    @Override
    protected void initView() {
        getData();
    }

    private void getData(){
        BaseActivity.callHttp(MapBuilder.build().add("action", "getRecommendCoaches").get(), new FunCallback<NetEntity, String, NetEntity>() {
            @Override
            public void onSuccess(NetEntity result, List<Object> object) {
            }
            @Override
            public void onFailure(String result, List<Object> object) {
            }
            @Override
            public void onCallback(NetEntity result, List<Object> object) {
                String jsonResult = result.getData().toString();
                try{
                    JSONObject jsonObject = new JSONObject(jsonResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("coaches");
                    String data = jsonArray.toString();

                    MapConf mc = MapConf.with(AllJiaolianActivity.this)
                            .pair("header->head_iv")
                            .source(R.layout.item_all_jiaolian);

                    MapConf.with(AllJiaolianActivity.this).conf(mc).source(data, mlistview.getRefreshableView()).toView();

                }catch (Exception e){
                }

            }
        });
    }

}
