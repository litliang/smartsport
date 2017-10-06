package top.smartsport.www.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import intf.FunCallback;
import intf.JsonUtil;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.adapter.PackageAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.PackageEntity;

@ContentView(R.layout.activity_buy_custom_vedio)
public class ActivityBuyCustomVedio extends BaseActivity{
    @ViewInject(R.id.list_view)
    private ListView mListView;
    private PackageAdapter mAdapter;
    private List mList = new ArrayList();

    @Override
    protected void initView() {
        initUI();
    }

    private void initUI() {
        mAdapter = new PackageAdapter();
        mListView.setAdapter(mAdapter);
        requestServer();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map packageEntity = (Map) mList.get(position);
                if (packageEntity != null) {
                    setResult(SSBMActivity.CHANGE_CUSTOM_VEDIO, new Intent().putExtra("sell_price",packageEntity.get("sell_price").toString()).putExtra("id",packageEntity.get("id").toString()).putExtra("title",packageEntity.get("title").toString()));
                    finish();
                }
            }
        });

    }

    private void requestServer(){
        BaseActivity.callHttp(MapBuilder.build().add("action", "getPackage").get(), this, new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {
                String data = ((NetEntity)result).getData().toString();
                List<PackageEntity> list = (List<PackageEntity>) app.base.JsonUtil.extractJsonRightValue(JsonUtil.findJsonLink("package",data).toString());
                mList.clear();
                mList.addAll(list);
                mList.addAll((List<PackageEntity>)getNoBuyInfo());
                mListView.setAdapter(mAdapter);
                mAdapter.setSelIndex(0);
                mAdapter.setData(mList);
//                MapConf mc = MapConf.with(ActivityBuyCustomVedio.this)
//                        .pair("title->title_tv")
//                        .pair("content->content_tv")
//                        .pair("sell_price:¥%s->sell_price_tv")
//                        .source(R.layout.package_item);
//                MapConf.with(ActivityBuyCustomVedio.this).conf(mc).source(mList,mListView).toView();
            }
            @Override
            public void onFailure(Object result, List object) {
            }
            @Override
            public void onCallback(Object result, List object) {
            }
        });
    }

    // 不购买定制视频
    private Object getNoBuyInfo() {
        Object obj = new Object();
        List<Object> arrayvalues = new ArrayList<Object>();
        Object obj1 = new Object();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id", "");
        map1.put("title", "不购买定制视频");
        map1.put("cover", "");
        map1.put("content", "不购买比赛定制视频");
        map1.put("sell_price", "0.00");
        map1.put("cover_url", "");
        obj1 = map1;
        arrayvalues.add(obj1);
        obj = arrayvalues;
        return obj;
    }

}
