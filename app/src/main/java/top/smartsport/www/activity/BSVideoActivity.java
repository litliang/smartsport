package top.smartsport.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dl7.player.media.IjkPlayerView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.LinkedHashMap;

import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.ShareActivity;
import top.smartsport.www.bean.BSzbInfo;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;

/**
 * Created by Aaron on 2017/8/2.
 * 视频详情
 */

public class BSVideoActivity extends AppCompatActivity {
    public static String TAG = BSVideoActivity.class.getName();

    private BSzbInfo bSzbInfo;

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    public final static String DATA = "data";

    private IjkPlayerView mPlayerView;

    LinkedHashMap<String, String> rateMap = new LinkedHashMap<>();

    private String mPlayUrl;
    private Bundle mBundle;
    private int mPlayMode;
    private boolean mHasSkin;
    private boolean mPano;

    private TextView timeText;
    private long beginTime;

    public void back() {
        if (getTopBar() == null) {
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
        if (getTopBar() == null) {
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
        if (getTopBar() == null) {
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

    public View actionbar;


    public View getTopBar() {
        return actionbar;
    }

    ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spdetail);
        actionbar = findViewById(R.id.action_bar);
        if (actionbar != null && ((TextView) actionbar.findViewById(R.id.tvTitle)) != null) {
            ((TextView) actionbar.findViewById(R.id.tvTitle)).setText(getTitle());
            back();
        }
        back();
        fav();

//        ((TextView) action_bar.findViewById(R.id.tvTitle)).setText("比赛视频");
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();

        Log.i("_________", "3");
//        bSzbInfo = (BSzbInfo) getObj(BSVideoActivity.TAG);
//        if(null==bSzbInfo){
//            finish();
//        }

//        bszb_detail_bszb_title.setText(bSzbInfo.getActivityName());
//        bszb_detail__bszb_date.setText(bSzbInfo.getStartTime());
//        bszb_detail__bszb_address.setText(bSzbInfo.getDescription());

        initViews();


//        getData();
    }


    private void initViews() {
        mPlayerView = (IjkPlayerView) findViewById(R.id.player_view);
        String url = getIntent().getStringExtra("fileurl").toString();
        mPlayerView.init()                // 初始化，必须先调用
//                .setTitle("这是个标题")    // 设置标题，全屏时显示
                .setSkipTip(1000 * 60 * 1)    // 设置跳转提示
//                .enableOrientation()    // 使能重力翻转
                .setVideoPath(url)    // 设置视频Url，单个视频源可用这个
//                .setVideoSource(null, url, url, url, null)    // 设置视频Url，多个视频源用这个
                .setMediaQuality(IjkPlayerView.MEDIA_QUALITY_HIGH)    // 指定初始视频源
                .enableDanmaku()      // 使能弹幕功能
//                .setDanmakuSource(getResources().openRawResource(R.raw.comments))	// 添加弹幕资源，必须在enableDanmaku()后调用
                .start();    // 启动播放


        timeText = (TextView) findViewById(R.id.time_text);
        beginTime = System.currentTimeMillis();
        findViewById(R.id.zb_detail_bq_start).performClick();
        String name = getIntent().getStringExtra("name");
        ((TextView)findViewById(R.id.bszb_detail_bszb_title)).setText(name);
        ((TextView)findViewById(R.id.bszb_detail__bszb_dis)).setText(name);

        ShareParams shareParams = new ShareParams();
        shareParams.setTitle(name);
        shareParams.setText(name);
        shareParams.setShareType(Platform.SHARE_VIDEO);
        shareParams.setUrl(url);
        share(shareParams, BaseActivity.Sharetype.URl);
    }

    public void share(ShareParams shareParams, final BaseActivity.Sharetype type) {
        if (getTopBar() == null) {
            return;
        }
        ShareActivity.shareparams = shareParams;
        getTopBar().findViewById(R.id.ivRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ShareActivity.class));
            }
        });
        getTopBar().findViewById(R.id.ivRight).setBackground(getResources().getDrawable(R.mipmap.share, null));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPlayerView.configurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPlayerView.handleVolumeKey(keyCode)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
