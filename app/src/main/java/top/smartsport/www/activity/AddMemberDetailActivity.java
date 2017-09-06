package top.smartsport.www.activity;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
@ContentView(R.layout.activity_add_member_detail)
public class AddMemberDetailActivity extends BaseActivity {


    @Override
    protected void initView() {

        back();
//        findViewById(R.id.weizhi_click).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                List list = Arrays.asList(new String[]{"前锋", "后卫", "门将", "后腰", "边锋", "边卫"});
//                String title = "位置";
//
//                showDialog(AddMemberDetailActivity.this, title, list, new FunCallback() {
//                    @Override
//                    public void onSuccess(Object result, List object) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Object result, List object) {
//
//                    }
//
//                    @Override
//                    public void onCallback(Object result, List object) {
//                        ((TextView) view.findViewById(R.id.weizhi)).setText(result.toString());
//                    }
//                });
//            }
//        });
//        findViewById(R.id.haoma_click).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//
//                List list = new ArrayList();
//
//                for (int i = 1; i < 19; i++) {
//                    list.add(i);
//                }
//                String title = "号码";
//
//                showDialog(AddMemberDetailActivity.this, title, list, new FunCallback() {
//                    @Override
//                    public void onSuccess(Object result, List object) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Object result, List object) {
//
//                    }
//
//                    @Override
//                    public void onCallback(Object result, List object) {
//                        ((TextView) view.findViewById(R.id.haoma)).setText(result.toString());
//                    }
//                });
//
//            }
//        });
//        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MapBuilder.build().add("type", "0").add("name", ((TextView) findViewById(R.id.et_team_name)).getText().toString()).add("position", ((TextView) findViewById(R.id.weizhi)).getText().toString()).add("number", ((TextView) findViewById(R.id.haoma)).getText().toString()).get();
////                MapBuilder m = MapBuilder.build().add("action", "editMyTeam");
////                if (getIntent().hasExtra("id")) {
////                    m.add("team_id", getIntent().getStringExtra("id"));
////                }
////                m.add("type", "0");
////                m.add("name", ((TextView) findViewById(R.id.et_team_name)).getText().toString());
////                m.add("position", ((TextView) findViewById(R.id.weizhi)).getText().toString());
////                m.add("number", ((TextView) findViewById(R.id.haoma)).getText().toString());
////
////                callHttp(m.get(), new FunCallback() {
////                    @Override
////                    public void onSuccess(Object result, List object) {
////
////                    }
////
////                    @Override
////                    public void onFailure(Object result, List object) {
////
////                    }
////
////                    @Override
////                    public void onCallback(Object result, List object) {
////                        if (((NetEntity) result).getErrno().equals("0")) {
////                            finish();
////                        } else {
////                            showToast(((NetEntity) result).getErrmsg());
////                        }
////                    }
////                });
//            }
//        });
    }


}