package top.smartsport.www.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import app.base.MapConf;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.utils.ActivityStack;
import top.smartsport.www.utils.AppUtil;
import top.smartsport.www.utils.FileHelper;
import top.smartsport.www.utils.SPUtils;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/18.
 * 账户设置
 */
@ContentView(R.layout.activity_accountset)
public class AccountSetActivity extends BaseActivity {

    @ViewInject(R.id.account_header)
    ImageView mIcon;

    @ViewInject(R.id.account_set_rl)
    RelativeLayout mSetIconRl;

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;
    private Bitmap iconBitMap;

    private final int CODE_CHOOSE_ICON = 3;
    private final int CODE_CHOOSE_ICON_CAMERA = 4;
    private final int CODE_CHOOSE_ICON_PICTURE = 5;
    private final int CODE_CHOOSE_ICON_ZOOM = 6;
    private final String KEY_CHOOSE_ICON_TYPE = "choose_type";
    private final String KEY_CHOOSE_TYPE_CAMERA = "choose_type_camera";
    private final String KEY_CHOOSE_TYPE_PICTURE = "choose_type_picture";
    private final String ICON_NAME = "ICON.jpg";

    @Override
    protected void initView() {
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        String data = (String) SPUtils.get(getBaseContext()
                , "getUserInfo", "");
        MapConf.build().addPair("header_url", R.id.account_header).addPair("username", R.id.username).addPair("truename", R.id.truename).addPair("age", R.id.account_age).addPair("sex", R.id.account_sex).addPair("height", R.id.account_height).addPair("weight", R.id.account_weight).addPair("leg", R.id.leg).addPair("address", R.id.account_jz)
//                .addPair("soccer_age",R.id.soccer_age")"" +
                .addTackle(new MapConf.Tackle() {
                    @Override
                    public void tackleBefore(Object item, Object value, String name, View convertView, View theView) {

                    }

                    @Override
                    public void tackleAfter(Object item, Object value, String name, View convertView, View theView) {
                        if (name.equals("height")) {
                            value += " cm";
                        } else if (name.equals("weight")) {
                            value += " kg";
                        } else if (name.equals("sex")) {
                            if (value.equals("0")) {
                                value = "女";
                            } else if (value.equals("1")) {
                                value = "男";
                            }
                        }
                        if(theView instanceof  TextView) {
                            ((TextView) theView).setText(value.toString());
                        }
                    }
                }).with(getBaseContext()).source(app.base.JsonUtil.extractJsonRightValue(data), getWindow().getDecorView()).toView();

        mSetIconRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSetActivity.this, ActivityChooseIcon.class);
                startActivityForResult(intent, CODE_CHOOSE_ICON);
            }
        });
    }

    @Event(value = {R.id.account_btn_login_out})
    private void getEvent(View v) {
        switch (v.getId()) {
            case R.id.account_btn_login_out:
                loginOut();
                break;
        }
    }

    /**
     * 用户退出
     */
    private void loginOut() {
        SPUtils.put(getBaseContext(), "USER", "");
        goActivity(LoginActivity.class);
        finish();
        ActivityStack.getInstance().findActivityByClass(MainActivity.class).finish();
//        JSONObject json = new JSONObject();
//        try {
//            json.put("client_id", client_id);
//            json.put("state", state);
//            json.put("access_token", access_token);
//            json.put("action", "logout");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        X.Post(url, json, new MyCallBack<String>() {
//            @Override
//            protected void onFailure(String message) {
//                showToast(message);
//            }
//
//            @Override
//            public void onSuccess(NetEntity entity) {
//
//            }
//        });
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
            Toast.makeText(AccountSetActivity.this, "SD卡不存在，请插入SD卡",
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
        if (resultCode == CODE_CHOOSE_ICON){
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
                saveIcon(baos);
            }
        }
    }

    private void saveIcon(ByteArrayOutputStream baos) {
        //头像存本地
        File baseFile = FileHelper.getBaseFile(FileHelper.PATH_PHOTOGRAPH);
        if (baseFile == null) {
            Toast.makeText(AccountSetActivity.this, "SD卡不存在，请插入SD卡",
                    Toast.LENGTH_LONG).show();
            return;
        }
        FileHelper.saveBitmap(iconBitMap, ICON_NAME, baseFile);
        String imagePath = Environment
                .getExternalStorageDirectory()
                + File.separator
                + FileHelper.PATH_PHOTOGRAPH + ICON_NAME;
        postIcon(imagePath);
    }

    private void postIcon(final String fileName) {
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
                Toast.makeText(AccountSetActivity.this, getResources().getString(R.string.icon_post_fail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(NetEntity entity) {
                String entity_data = entity.getStatus();
                if (entity_data.equals("true")){
                    Toast.makeText(AccountSetActivity.this, getResources().getString(R.string.icon_post_success), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AccountSetActivity.this, getResources().getString(R.string.icon_post_fail), Toast.LENGTH_SHORT).show();
                }
                MapConf.with(AccountSetActivity.this).pair("header", entity.getImg_id()).toMap();
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });
    }

    private void postData(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iconBitMap != null && (!iconBitMap.isRecycled())){
            iconBitMap.recycle();
        }
    }
}
