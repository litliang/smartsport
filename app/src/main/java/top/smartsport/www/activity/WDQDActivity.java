package top.smartsport.www.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xutils.view.annotation.ContentView;

import java.util.List;
import java.util.Map;

import app.base.JsonUtil;
import app.base.MapAdapter;
import app.base.MapContent;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;

/**
 * Created by Aaron on 2017/7/28.
 * 我的球队
 */
@ContentView(R.layout.activity_wdqd)
public class WDQDActivity extends BaseActivity {


    @Override
    protected void initView() {
        back();

        MapAdapter.AdaptInfo adaptinfo = new MapAdapter.AdaptInfo();
        adaptinfo.addListviewItemLayoutId(R.layout.wdqd_item);
        adaptinfo.addViewIds(new Integer[]{R.id.name});
        adaptinfo.addObjectFields(new String[]{"name"});
        mapadapter = new MapAdapter(this, adaptinfo);
        ((ListView) findViewById(R.id.wd_qd_listView)).setAdapter(mapadapter);
        ((ListView) findViewById(R.id.wd_qd_listView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map map = (Map) adapterView.getItemAtPosition(i);
                startActivityForResult(new Intent(getApplication(), AddMemberActivity.class).putExtra("id",map.get("id").toString()).putExtra("name",map.get("name").toString()),0);
            }
        });
        findViewById(R.id.addteam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), AddMemberActivity.class));
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void refresh() {
        callHttp(MapBuilder.build().add("action", "getMyTeamList").get(), new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {

                String data = ((NetEntity) result).getData().toString();
                mapadapter.setItemDataSrc(new MapContent(JsonUtil.extractJsonRightValue(data)));
                mapadapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object result, List object) {

            }

            @Override
            public void onCallback(Object result, List object) {
            }
        });
    }

    MapAdapter mapadapter;

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}
