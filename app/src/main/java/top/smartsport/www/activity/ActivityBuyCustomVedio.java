package top.smartsport.www.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.base.MapConf;
import intf.FunCallback;
import intf.JsonUtil;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.adapter.PackageAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.PackageEntity;
import top.smartsport.www.bean.WDQDInfo;

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
                //                View selectedView = parent.getSelectedView();
//                if (selectedView != null){
//                    CheckBox checkBox = (CheckBox) selectedView.findViewById(R.id.iv_check);
//                    if (checkBox != null){
//                        checkBox.setChecked(true);
                Map packageEntity = (Map) mList.get(position);
                if (packageEntity != null) {
                    setResult(SSBMActivity.CHANGE_CUSTOM_VEDIO, new Intent().putExtra("sell_price",packageEntity.get("sell_price").toString()).putExtra("package_id",packageEntity.get("id").toString()));
                    finish();
//                        }
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
                mListView.setAdapter(mAdapter);
                mAdapter.setData(mList);
                MapConf mc = MapConf.with(ActivityBuyCustomVedio.this)
                        .pair("title->title_tv")
                        .pair("content->content_tv")
                        .pair("sell_price->sell_price_tv")
                        .source(R.layout.package_item);
                MapConf.with(ActivityBuyCustomVedio.this).conf(mc).source(mList,mListView).toView();
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
