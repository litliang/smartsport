package top.smartsport.www.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;

import java.util.List;

import app.base.JsonUtil;
import app.base.MapAdapter;
import app.base.MapConf;
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
    public  static final int ADD_MEMBER = 1;

    private MapAdapter mapadapter;

    boolean add;
    private String teamid;
    boolean saved;

    String id;
    private ListView mListView;
    private TeamMemberAdapter mAdapter;
    private List list;
    private String data;

    @Override
    protected void initView() {


        if (getIntent().hasExtra("name")) {
            String name = getIntent().getStringExtra("name");
            ((TextView) findViewById(R.id.et_team_name)).setText(name);
        }
        String team_id = getIntent().getStringExtra("id");
        id = team_id == null ? "" : team_id;

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                saved = true;
                MapBuilder m = MapBuilder.build().add("action", "editMyTeam");
                if (!id.equals("")) {
                    m.add("team_id", id);
                }
                m.add("type", "0");
                m.add("team_name", ((TextView) findViewById(R.id.et_team_name)).getText().toString());
//                m.add("team_name", ((TextView) findViewById(R.id.et_main_coach_name)).getText().toString());
//                m.add("team_name", ((TextView) findViewById(R.id.et_coach1_name)).getText().toString());
//                m.add("team_name", ((TextView) findViewById(R.id.et_coach2_name)).getText().toString());
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
                        showToast(((NetEntity)result).getMessage().toString());
                    }
                });
            }
        });
        if (getIntent().hasExtra("id")) {
            ((TextView) findViewById(R.id.ivRight_text)).setText("删除");
            (findViewById(R.id.ivRight)).setVisibility(View.GONE);
            findViewById(R.id.ivRight_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callHttp(MapBuilder.build().add("action", "delMyTeam").add("team_id", id).get(), new FunCallback() {

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
                }
            });

            getMyTeam();


        } else {
            MapBuilder m = MapBuilder.build().add("action", "editMyTeam");


            callHttp(m.get(), new FunCallback() {
                @Override
                public void onSuccess(Object result, List object) {
                    id = (String) JsonUtil.findJsonLink("team_id",((NetEntity)result).getData().toString());



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
        mListView = (ListView) findViewById(R.id.lv_team);
         mAdapter = new TeamMemberAdapter();
        mListView.setAdapter(mAdapter);
        TextView tvAddMember = (TextView) findViewById(R.id.tv_add_member);
        tvAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (mAdapter.getCount()<12) {
                        startActivityForResult(getIntent().setClass(AddMemberActivity.this, AddMemberDetailActivity.class).putExtra("team_id", id), ADD_MEMBER);
                    }else {
                        Toast.makeText(AddMemberActivity.this,"最多添加11名队员",Toast.LENGTH_SHORT).show();
                    }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivityForResult(getIntent().setClass(AddMemberActivity.this, AddMemberDetailActivity.class).putExtra("member",data).putExtra("position",i).putExtra("team_id",id), ADD_MEMBER);
            }
        });

        findViewById(R.id.ivLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delteam(new FunCallback() {
                    @Override
                    public void onSuccess(Object result, List object) {
                        finish();
                    }

                    @Override
                    public void onFailure(Object result, List object) {


                    }

                    @Override
                    public void onCallback(Object result, List object) {
                        if (object instanceof Throwable) {

                        }
                        finish();
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ADD_MEMBER){
            getMyTeam();
        }
    }

    private void getMyTeam() {
        callHttp(MapBuilder.build().add("action", "getMyTeamDetail").add("team_id", id).get(), new FunCallback() {

            @Override
            public void onSuccess(Object result, List object) {

               data = ((NetEntity) result).getData().toString();
                list = (List) intf.JsonUtil.extractJsonRightValue(JsonUtil.findJsonLink("player",data));
                MapConf.with(AddMemberActivity.this)
                        .pair("team_name->et_team_name")
                        .pair("coach[0]-name->et_main_coach_name")
                        .pair("assists[0]-name->et_coach1_name")
                        .pair("assists[1]-name->et_coach2_name")
                        .source(data, AddMemberActivity.this).toView();
                mAdapter.setData(list);
                MapConf mc = MapConf.with(AddMemberActivity.this)
                        .pair("name:name-%s->view_member","", "addMember()")
                        .pair("position:position-%s->view_member","","addMember()")
                        .pair("number:number-%s->view_member","","addMember()")
                        .source(R.layout.member_list);
                MapConf.with(AddMemberActivity.this).conf(mc).source(list, mListView).toView();
            }

            @Override
            public void onFailure(Object result, List object) {

            }

            @Override
            public void onCallback(Object result, List object) {
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            delteam(new FunCallback() {
                @Override
                public void onSuccess(Object result, List object) {
                }

                @Override
                public void onFailure(Object result, List object) {


                }

                @Override
                public void onCallback(Object result, List object) {
                    finish();
                }
            });
        }
        return false;
    }


    private void delteam(FunCallback func) {
        if (!saved) {
            MapBuilder m = MapBuilder.build().add("action", "delMyTeam");
//            if (getIntent().hasExtra("id")) {
//                m.add("team_id", getIntent().getStringExtra("id"));
//            }
            m.add("team_id", teamid);
            callHttp(m.get(), func);
        } else {
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    public void addMemeber(){

        MapBuilder builder = MapBuilder.build().add("action", "editMyTeam").add("team_id", id).add("name","").add("type","").add("number","").add("position","");

        callHttp(builder.get(), new FunCallback() {

            @Override
            public void onSuccess(Object result, List object) {

                String data = ((NetEntity) result).getData().toString();
                MapConf.with(AddMemberActivity.this)
                        .pair("team_name->et_team_name")
                        .pair("coach[0]-name->et_main_coach_name")
                        .pair("assists[0]-name->et_coach1_name")
                        .pair("assists[1]-name->et_coach2_name")
                        .source(data, AddMemberActivity.this).toView();

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