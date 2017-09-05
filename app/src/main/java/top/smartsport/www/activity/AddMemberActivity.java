package top.smartsport.www.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.autolayout.AutoLayoutActivity;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.base.DialogUtil;
import app.base.JsonUtil;
import app.base.MapAdapter;
import app.base.MapContent;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.adapter.TeamMemberAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;

/**
 * Created by Administrator on 2017/8/17.
 */
@ContentView(R.layout.activity_add_member)
public class AddMemberActivity extends BaseActivity {


    private MapAdapter mapadapter;

    boolean add;
    private String teamid;
boolean saved;
    @Override
    protected void initView() {

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saved = true;
                MapBuilder m = MapBuilder.build().add("action", "editMyTeam");
//            if (getIntent().hasExtra("id")) {
                m.add("team_id", teamid);
//            }
                m.add("type", "0");
                m.add("team_name", ((TextView) findViewById(R.id.et_team_name)).getText().toString());
                callHttp(m.get(), new FunCallback() {
                    @Override
                    public void onSuccess(Object result, List object) {
                        finish();
                    }

                    @Override
                    public void onFailure(Object result, List object) {

                    }

                    @Override
                    public void onCallback(Object result, List object) {
                        showToast(((NetEntity) result).getMessage());
                    }
                });
            }
        });
        if (getIntent().hasExtra("name")) {
            String name = getIntent().getStringExtra("name");
            ((TextView) findViewById(R.id.et_team_name)).setText(name);
        }

        if (getIntent().hasExtra("id")) {
            String id = getIntent().getStringExtra("id");

            callHttp(MapBuilder.build().add("action", "getMyTeamDetail").add("team_id", id).get(), new FunCallback() {

                @Override
                public void onSuccess(Object result, List object) {

                }

                @Override
                public void onFailure(Object result, List object) {

                }

                @Override
                public void onCallback(Object result, List object) {
                    String assists = (String) JsonUtil.findJsonLink("assists", (((NetEntity) result).getData().toString()));
                    mapadapter.setItemDataSrc(new MapContent(JsonUtil.extractJsonRightValue(assists)));
                }
            });


        } else {
            MapBuilder m = MapBuilder.build().add("action", "editMyTeam");
//            if (getIntent().hasExtra("id")) {
//                m.add("team_id", getIntent().getStringExtra("id"));
//            }
            m.add("team_name", ((TextView) findViewById(R.id.et_team_name)).getText().toString());
            callHttp(m.get(), new FunCallback() {
                @Override
                public void onSuccess(Object result, List object) {

                }

                @Override
                public void onFailure(Object result, List object) {

                }

                @Override
                public void onCallback(Object result, List object) {

                }
            });

        }

        back();
        ListView mListView = (ListView) findViewById(R.id.lv_team);
        final TeamMemberAdapter adapter = new TeamMemberAdapter();
        mListView.setAdapter(adapter);
        TextView tvAddMember = (TextView) findViewById(R.id.tv_add_member);
        tvAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addMember();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(getIntent().setClass(AddMemberActivity.this, AddMemberDetailActivity.class));
            }
        });


    }


    @Override
    public void finish() {
        if(!saved){
            MapBuilder m = MapBuilder.build().add("action", "delMyTeam");
//            if (getIntent().hasExtra("id")) {
//                m.add("team_id", getIntent().getStringExtra("id"));
//            }
            m.add("team_id", teamid);
            callHttp(m.get(), new FunCallback() {
                @Override
                public void onSuccess(Object result, List object) {
                    finish();
                }

                @Override
                public void onFailure(Object result, List object) {

                }

                @Override
                public void onCallback(Object result, List object) {

                }
            });
        }else{
            super.finish();
        }
    }
}