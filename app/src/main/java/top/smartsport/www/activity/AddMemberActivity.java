package top.smartsport.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.base.JsonUtil;
import app.base.MapAdapter;
import app.base.MapConf;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.actions.Showinputbox;
import top.smartsport.www.adapter.TeamMemberAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.Coach;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.utils.AppUtil;
import top.smartsport.www.utils.FileHelper;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Administrator on 2017/8/17.
 */
@ContentView(R.layout.activity_add_member)
public class AddMemberActivity extends BaseActivity {
    public static final int ADD_MEMBER = 1;

    @ViewInject(R.id.et_team_name)
    private TextView et_team_name;
    @ViewInject(R.id.et_main_coach_name)
    private TextView et_main_coach_name;
    @ViewInject(R.id.et_coach1_name)
    private TextView et_coach1_name;
    @ViewInject(R.id.et_coach2_name)
    private TextView et_coach2_name;
    @ViewInject(R.id.account_header)
    ImageView mIcon;
    @ViewInject(R.id.account_set_rl)
    RelativeLayout mSetIconRl;

    private MapAdapter mapadapter;

    boolean add;
    private String teamid;
    boolean saved;

    String id;
    private ListView mListView;
    private TeamMemberAdapter mAdapter;
    private List list;
    private String data;
    private List<Coach> listCoach;
    private List<Coach> listAssists;
    private List<Coach> listPlayer;

    private Bitmap iconBitMap;
    private final int CODE_CHOOSE_ICON = 3;
    private final int CODE_CHOOSE_ICON_CAMERA = 4;
    private final int CODE_CHOOSE_ICON_PICTURE = 5;
    private final int CODE_CHOOSE_ICON_ZOOM = 6;
    private final String KEY_CHOOSE_ICON_TYPE = "choose_type";
    private final String KEY_CHOOSE_TYPE_CAMERA = "choose_type_camera";
    private final String KEY_CHOOSE_TYPE_PICTURE = "choose_type_picture";
    private final String ICON_NAME = "ICON.jpg";

    private RegInfo regInfo;
    private TokenInfo tokenInfo;
    private String client_id;
    private String state;
    private String url;
    private String access_token;

    @Override
    protected void initView() {
        back();
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();
        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        if (getIntent().hasExtra("name")) {
            String name = getIntent().getStringExtra("name");
            ((TextView) findViewById(R.id.et_team_name)).setText(name);
        }
        String team_id = getIntent().getStringExtra("id");
        id = team_id == null ? "" : team_id;
        mSetIconRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMemberActivity.this, ActivityChooseIcon.class);
                startActivityForResult(intent, CODE_CHOOSE_ICON);
            }
        });
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                saved = true;
                editMember(new FunCallback() {
                    @Override
                    public void onSuccess(Object result, List object) {
                        setResult(RESULT_OK);
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
                    id = (String) JsonUtil.findJsonLink("team_id", ((NetEntity) result).getData().toString());


                }

                @Override
                public void onFailure(Object result, List object) {

                }

                @Override
                public void onCallback(Object result, List object) {

                }
            });

        }

        mListView = (ListView) findViewById(R.id.lv_team);
        mAdapter = new TeamMemberAdapter();
        mListView.setAdapter(mAdapter);
        TextView tvAddMember = (TextView) findViewById(R.id.tv_add_member);
        tvAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdapter.getCount() < 12) {
                    startActivityForResult(getIntent().setClass(AddMemberActivity.this, AddMemberDetailActivity.class).putExtra("team_id", id), ADD_MEMBER);
                } else {
                    Toast.makeText(AddMemberActivity.this, "最多添加11名队员", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivityForResult(getIntent().setClass(AddMemberActivity.this, AddMemberDetailActivity.class).putExtra("member", data).putExtra("position", i).putExtra("team_id", id), ADD_MEMBER);
            }
        });

        findViewById(R.id.ivLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!id.equals("")) {
                    finish();
                } else {
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

            }
        });
        setClick(R.id.et_team_name, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Showinputbox().showDialog((Activity) view.getContext(), getTextString(R.id.et_team_name), "设置您的球队名字", new FunCallback() {
                    @Override
                    public void onSuccess(Object result, List object) {

                    }

                    @Override
                    public void onFailure(Object result, List object) {

                    }

                    @Override
                    public void onCallback(Object result, List object) {
                        getTextView(R.id.et_team_name).setText(result.toString());
                        MapBuilder builder = MapBuilder.build().add("action", "editMyTeam").add("team_id", id).add("team_name", getTextString(R.id.et_team_name));


                        callHttp(builder.get(), new FunCallback() {
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
                });
            }
        });
        setClick(R.id.majorcoach, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Showinputbox().showDialog((Activity) view.getContext(), getTextString(R.id.et_main_coach_name), "设置您的主教练", new FunCallback() {
                    @Override
                    public void onSuccess(Object result, List object) {
                    }

                    @Override
                    public void onFailure(Object result, List object) {
                    }

                    @Override
                    public void onCallback(Object result, List object) {
                        getTextView(R.id.et_main_coach_name).setText(result.toString());
                        MapBuilder builder = MapBuilder.build().add("action", "editMyTeam").add("team_id", id).add("type", "2").add("name", getTextString(R.id.et_main_coach_name));
                        if (getIntent().hasExtra("id") && listCoach != null && listCoach.size() > 0) {
                            builder.add("member_id", listCoach.get(0).getId());
                        }
                        callHttp(builder.get(), new FunCallback() {
                            @Override
                            public void onSuccess(Object result, List object) {
                                editcoach(listCoach != null ? listCoach.get(0) : null);
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
            }
        });
        setClick(R.id.assit1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Showinputbox().showDialog((Activity) view.getContext(), getTextString(R.id.et_coach1_name), "设置您的第1位助理教练", new FunCallback() {
                    @Override
                    public void onSuccess(Object result, List object) {

                    }

                    @Override
                    public void onFailure(Object result, List object) {

                    }

                    @Override
                    public void onCallback(Object result, List object) {
                        getTextView(R.id.et_coach1_name).setText(result.toString());
                            MapBuilder builder = MapBuilder.build().add("action", "editMyTeam").add("team_id", id).add("type", "1").add("name", getTextString(R.id.et_coach1_name));
                            if (getIntent().hasExtra("id") && listAssists != null && listAssists.size() > 0) {
                                builder.add("member_id", listAssists.get(0).getId());
                            }
                            callHttp(builder.get(), new FunCallback() {

                                @Override
                                public void onSuccess(Object result, List object) {
                                    editcoach(listAssists != null ? listAssists.get(0) : null);
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
            }
        });
        setClick(R.id.assit2, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Showinputbox().showDialog((Activity) view.getContext(), getTextString(R.id.et_coach2_name), "设置您的第2位助理教练", new FunCallback() {
                    @Override
                    public void onSuccess(Object result, List object) {

                    }

                    @Override
                    public void onFailure(Object result, List object) {

                    }

                    @Override
                    public void onCallback(Object result, List object) {
                        getTextView(R.id.et_coach2_name).setText(result.toString());

                            MapBuilder builder = MapBuilder.build().add("action", "editMyTeam").add("team_id", id).add("type", "1").add("name", getTextString(R.id.et_coach2_name));
                            if (getIntent().hasExtra("id") && listAssists != null && listAssists.size() > 1) {
                                builder.add("member_id", listAssists.get(1).getId());
                            }
                            callHttp(builder.get(), new FunCallback() {
                                @Override
                                public void onSuccess(Object result, List object) {
                                    editcoach(listAssists != null ? listAssists.get(1) : null);
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
            }
        });

    }

    private void editMember(FunCallback func) {

        MapBuilder m = MapBuilder.build().add("action", "editMyTeam");
        m.add("team_id", id);
        m.add("team_name", getTextString(R.id.et_team_name));
        if(imageid!=null){
            m.add("logo",imageid);
        }
        callHttp(m.get(), func);
    }

    private void editcoach(Coach coach) {
        MapBuilder builder = MapBuilder.build()
                .add("action", "editMyTeam")
                .add("team_id", id)
                .add("team_name", getTextString(R.id.et_team_name));
//                .add("member_id", getField("coach[0]-id"))
//                .add("name", getField("coach[0]-name"))
//                .add("type", getField("coach[0]-type"))
//                .add("number", getField("coach[0]-number"))
//                .add("position", getField("coach[0]-position"));
        if(getIntent().hasExtra("id") && coach != null) {
            builder.add("member_id", coach.getId())
                    .add("name", coach.getName())
                    .add("type", coach.getType())
                    .add("number", coach.getNumber())
                    .add("position", coach.getPosition());
        }

        callHttp(builder.get(), new FunCallback() {
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

    private void getMyTeam() {
        callHttp(MapBuilder.build().add("action", "getMyTeamDetail").add("team_id", id).get(), new FunCallback() {

            @Override
            public void onSuccess(Object result, List object) {

                data = ((NetEntity) result).getData().toString();
                setInitData(data);
                list = (List) intf.JsonUtil.extractJsonRightValue(JsonUtil.findJsonLink("player", data));
                try {
                    JSONObject jsonData = new JSONObject(data);
                    JSONArray coach = jsonData.optJSONArray("coach");
                    Gson gson = new Gson();
                    if(coach != null && coach.length() > 0) {
                        listCoach = new ArrayList<Coach>();
                        JSONObject coachItem = coach.optJSONObject(0);
                        et_main_coach_name.setText(coachItem.optString("name"));
                        if (coachItem != null) {
                            Coach coachValue = gson.fromJson(coachItem.toString(), Coach.class);
                            listCoach.add(coachValue);
                        }
                    }

                    JSONArray assists = jsonData.optJSONArray("assists");
                    if(assists != null && assists.length() > 0) {
                        listAssists = new ArrayList<Coach>();
                        for(int i=0; i<assists.length(); i++) {
                            JSONObject assistsItem = assists.optJSONObject(i);
                            if(i == 0) {
                                et_coach1_name.setText(assistsItem.optString("name"));
                            } else if(i == 1) {
                                et_coach2_name.setText(assistsItem.optString("name"));
                            }
                            Coach coachValue = gson.fromJson(assistsItem.toString(), Coach.class);
                            listAssists.add(coachValue);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MapConf.with(AddMemberActivity.this)
                        .pair("team_name->et_team_name")
                        .pair("logo_url->account_header")
                        .pair("coach[0]-name->et_main_coach_name")
                        .pair("assists[0]-name->et_coach1_name")
                        .pair("assists[1]-name->et_coach2_name")
                        .source(data, AddMemberActivity.this).toView();
                mAdapter.setData(list);
                MapConf mc = MapConf.with(AddMemberActivity.this)
                        .pair("name:name-%s->view_member", "", "addMember()")
                        .pair("position:position-%s->view_member", "", "addMember()")
                        .pair("number:number-%s->view_member", "", "addMember()")
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
            if (id.equals(""))
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
            else return super.onKeyDown(keyCode,  event);
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

    public void addMemeber(String tid, String tname, String mid, String mname, String mtype, String mnuumber, String mposition) {

        MapBuilder builder = MapBuilder.build().add("action", "editMyTeam").add("team_id", id).add("team_name", "").add("member_id", "").add("name", "").add("type", "").add("number", "").add("position", "");

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

    private void chooseCamera(){
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (AppUtil.hasSdcard()) {
            in.putExtra("return-data", false);
            in.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            in.putExtra("noFaceDetection", true);
            in.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment.getExternalStorageDirectory(), ICON_NAME)));
        }else{
            Toast.makeText(AddMemberActivity.this, "SD卡不存在，请插入SD卡",
                    Toast.LENGTH_LONG).show();
            return;
        }
        startActivityForResult(in, CODE_CHOOSE_ICON_CAMERA);
    }

    private void choosePicture(){
        Intent in = new Intent(Intent.ACTION_PICK);
        in.setType("image/*");
        startActivityForResult(in, CODE_CHOOSE_ICON_PICTURE);
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪 crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_CHOOSE_ICON_ZOOM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String temp;
        if (resultCode == RESULT_OK && requestCode == ADD_MEMBER) {
            getMyTeam();
        } else if (resultCode == CODE_CHOOSE_ICON){
            temp = data.getStringExtra(KEY_CHOOSE_ICON_TYPE);
            if (!temp.isEmpty()){
                if (temp.equals(KEY_CHOOSE_TYPE_CAMERA)){//选择照相机
                    chooseCamera();
                }else if (temp.equals(KEY_CHOOSE_TYPE_PICTURE)){//选择图册
                    choosePicture();
                }
            }
        }else if (requestCode == CODE_CHOOSE_ICON_CAMERA){//相机返回
            startPhotoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), ICON_NAME)));
        }else if ((data != null) &&(requestCode == CODE_CHOOSE_ICON_PICTURE)){//图册返回
            startPhotoZoom(data.getData());
        }else if ((data != null) && (requestCode == CODE_CHOOSE_ICON_ZOOM)){//裁剪完后
            Bundle extras = data.getExtras();
            if (extras != null) {
                iconBitMap = AppUtil.toRoundBitmap((Bitmap)extras.getParcelable("data"));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                iconBitMap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                mIcon.setImageBitmap(iconBitMap);
                postIcon(saveIcon(baos), new FunCallback() {
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
        }
    }

    private String saveIcon(ByteArrayOutputStream baos) {
        //头像存本地
        File baseFile = FileHelper.getBaseFile(FileHelper.PATH_PHOTOGRAPH);
        if (baseFile == null) {
            Toast.makeText(this, "SD卡不存在，请插入SD卡",
                    Toast.LENGTH_LONG).show();
            return "";
        }
        FileHelper.saveBitmap(iconBitMap, ICON_NAME, baseFile);
        String imagePath = Environment
                .getExternalStorageDirectory()
                + File.separator
                + FileHelper.PATH_PHOTOGRAPH + ICON_NAME;
        return imagePath;

    }

    protected void onDestroy() {
        super.onDestroy();
        if (iconBitMap != null && (!iconBitMap.isRecycled())){
            iconBitMap.recycle();
        }
    }
    String imageid;
    private void postIcon(final String fileName,FunCallback funCallback) {
        RegInfo regInfo = RegInfo.newInstance();
        TokenInfo tokenInfo = TokenInfo.newInstance();

        String client_id = regInfo.getApp_key();
        String state = regInfo.getSeed_secret();
        String url = regInfo.getSource_url();
        String access_token = tokenInfo.getAccess_token();
        File file = new File(fileName);
        Map<String,Object> map = new HashMap<>();
        map.put("client_id",client_id);
        map.put("state",state);
        map.put("access_token",access_token);
        map.put("action","uploadImg");
        map.put("image", file);

        X.Post(url, map, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                Toast.makeText(AddMemberActivity.this, getResources().getString(R.string.icon_post_fail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(NetEntity entity) {
                String entity_data = entity.getStatus();
                if (entity_data.equals("true")){
                    Toast.makeText(AddMemberActivity.this, getResources().getString(R.string.icon_post_success), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddMemberActivity.this, getResources().getString(R.string.icon_post_fail), Toast.LENGTH_SHORT).show();
                }
                imageid = entity.getImg_id();
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });
    }

}