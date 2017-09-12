package top.smartsport.www.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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


    private MapAdapter mapadapter;

    boolean add;
    private String teamid;
    boolean saved;

    String id;
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

            callHttp(MapBuilder.build().add("action", "getMyTeamDetail").add("team_id", id).get(), new FunCallback() {

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