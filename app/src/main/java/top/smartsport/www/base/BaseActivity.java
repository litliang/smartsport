package top.smartsport.www.base;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.zhy.autolayout.AutoLayoutActivity;

import intf.FunCallback;
import intf.QueryBuilder;

import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.smartsport.www.R;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.dialog.CustomProgressDialog;
import top.smartsport.www.utils.ActivityStack;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/6/30.
 */

public abstract class BaseActivity extends AutoLayoutActivity {
    public CustomProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        featureNoTitle();

        super.onCreate(savedInstanceState);

        x.view().inject(this);
        ActivityStack.getInstance().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制是竖屏
        initActionBar();
        initProgressDialog();
        initHttpParams();

        actionbar = findViewById(R.id.action_bar);
        if(actionbar!=null&&((TextView)actionbar.findViewById(R.id.tvTitle))!=null){
            ((TextView)actionbar.findViewById(R.id.tvTitle)).setText(getTitle());
            back();
        }
        initView();
    }

    public View actionbar;


    public View getTopBar(){
        return actionbar;
    };

    public void setTitle(String title) {
        ((TextView) getTopBar().findViewById(R.id.tvTitle)).setText(title);
    }

    public void back() {
        if(getTopBar()==null){
            return;
        }
        getTopBar().findViewById(R.id.ivLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void share() {
        if(getTopBar()==null){
            return;
        }
        getTopBar().findViewById(R.id.ivRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        getTopBar().findViewById(R.id.ivRight).setBackground(getResources().getDrawable(R.mipmap.share, null));
    }

    public void fav() {
        if(getTopBar()==null){
            return;
        }
        getTopBar().findViewById(R.id.ivRight_text).setOnClickListener(new View.OnClickListener() {
            boolean isinit = false;

            @Override
            public void onClick(View view) {
                if (!isinit) {
                    view.setBackground(getResources().getDrawable(R.mipmap.fav_done, null));
                } else {
                    view.setBackground(getResources().getDrawable(R.mipmap.fav_undo, null));
                }
                isinit = !isinit;
            }
        });
        getTopBar().findViewById(R.id.ivRight_text).setBackground(getResources().getDrawable(R.mipmap.fav_undo, null));
    }

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    private void initHttpParams() {
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

    }

    public static void callHttp(final Map map, final FunCallback funcall) {
        RegInfo regInfo = RegInfo.newInstance();
        TokenInfo tokenInfo = TokenInfo.newInstance();

        String client_id = regInfo.getApp_key();
        String state = regInfo.getSeed_secret();
        String url = regInfo.getSource_url();
        String access_token = tokenInfo.getAccess_token();
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            for (Object o : map.keySet()) {
                json.put(o.toString(), map.get(o.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                funcall.<String>onFailureConnected(message);
            }

            @Override
            public void onSuccess(NetEntity entity) {
                funcall.<NetEntity>onCallbackConnected(entity);
            }
        });
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void featureNoTitle() {
    }

    /**
     * 初始化参数
     */
    protected abstract void initView();

    private void initProgressDialog() {
        pd = new CustomProgressDialog(this);
        pd.setCancelable(false);
    }

    private void initActionBar() {
        if (getActionBar() == null) {
            return;
        }

//        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bg));
        getActionBar().setCustomView(R.layout.layout_actionbar);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ivLeft:
                        doLeft();
                        break;
                    case R.id.ll_right:
                        doRight();
                        break;
                }
            }
        };
        getActionBar().getCustomView().findViewById(R.id.ivLeft).setOnClickListener(listener);
        // getActionBar().getCustomView().findViewById(R.id.ivRight).setOnClickListener(listener);
        getActionBar().getCustomView().findViewById(R.id.ll_right).setOnClickListener(listener);
        ((TextView) getActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(getTitle().toString());

        getActionBar().hide();
    }

    @Override
    public void setTitle(CharSequence title) {
        ((TextView) getActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(title);
    }

    protected void showLeft(boolean flag) {
        if (getActionBar() == null) {
            return;
        }
        getActionBar().getCustomView().findViewById(R.id.ivLeft).setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    protected void showRight(boolean flag) {
        if (getActionBar() == null) {
            return;
        }
        getActionBar().getCustomView().findViewById(R.id.ll_right).setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    protected void showRightText(boolean flag) {
        if (getActionBar() == null) {
            return;
        }
        getActionBar().getCustomView().findViewById(R.id.ivRight_text).setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    protected void showRightImage(boolean flag) {
        if (getActionBar() == null) {
            return;
        }
        getActionBar().getCustomView().findViewById(R.id.ivRight).setVisibility(flag ? View.VISIBLE : View.GONE);
    }


    protected void doLeft() {
        finish();
    }

    protected void doRight() {

    }

    protected void setRight(int drawRes) {
        if (getActionBar() == null) {
            return;
        }
        ((ImageView) getActionBar().getCustomView().findViewById(R.id.ivRight)).setImageResource(drawRes);
        getActionBar().getCustomView().findViewById(R.id.ivRight).setVisibility(View.VISIBLE);
    }

    protected void setRightText(String drawRes) {
        if (getActionBar() == null) {
            return;
        }
        ((TextView) getActionBar().getCustomView().findViewById(R.id.ivRight_text)).setText(drawRes);
        getActionBar().getCustomView().findViewById(R.id.ivRight_text).setVisibility(View.VISIBLE);
    }

    public void goActivity(Class<?> clazz) {
        goActivity(clazz, null);
    }

    public void goActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public <T extends Object> T getData(Bundle data, String key, T def) {
        if (data != null) {
            T t = (T) data.get(key);
            if (t != null) {
                return t;
            }
        }
        return def;
    }

    public <T extends Object> T getData(String key, T def) {
        return getData(getIntent().getExtras(), key, def);
    }

    protected Serializable getObj(String key) {
        Serializable obj = null;
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(key)) {
            try {
                obj = (Serializable) getIntent().getExtras().get(key);
            } catch (Exception e) {
            }
        }
        return obj;
    }

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int PERMISSON_REQUESTCODE = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedCheck) {
            checkPermissions(needPermissions);
        }
    }

    /**
     * @param needRequestPermissonList
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.notifyTitle);
        builder.setMessage(R.string.notifyMsg);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
