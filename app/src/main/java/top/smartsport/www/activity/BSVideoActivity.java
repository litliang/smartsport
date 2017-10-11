package top.smartsport.www.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.dl7.player.media.IjkPlayerView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import app.base.MapConf;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseComptActivity;
import top.smartsport.www.bean.BSzbInfo;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;

/**
 * Created by Aaron on 2017/8/2.
 * 视频详情
 */
@ContentView(R.layout.activity_spdetail)
public class BSVideoActivity extends BaseComptActivity {
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
    private String videoid;

    @Override
    protected void initView() {
        videoid = getIntent().getStringExtra("videoid");
        back();
        share();
//        ((TextView) action_bar.findViewById(R.id.tvTitle)).setText("比赛视频");
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();

        Log.i("_________", "3");

        initViews();
    }

    @ViewInject(R.id.action_bar)
    private View actionbar;

    @Override
    public View getTopBar() {
        return actionbar;
    }

    private void initViews() {

        mPlayerView = (IjkPlayerView) findViewById(R.id.player_view);

        getOtherVideo();
    }

    private void setVideoDetail(Object url, String name) {
        url = url == null ? "" : url;
        if (url != null) {
            mPlayerView.init()                // 初始化，必须先调用
//                .setTitle("这是个标题")    // 设置标题，全屏时显示
                    .setSkipTip(1000 * 60 * 1)    // 设置跳转提示
//                .enableOrientation()    // 使能重力翻转
                    .setVideoPath(url.toString())    // 设置视频Url，单个视频源可用这个
//                .setVideoSource(null, url, url, url, null)    // 设置视频Url，多个视频源用这个
                    .setMediaQuality(IjkPlayerView.MEDIA_QUALITY_HIGH)    // 指定初始视频源
                    .enableDanmaku()      // 使能弹幕功能
//                .setDanmakuSource(getResources().openRawResource(R.raw.comments))	// 添加弹幕资源，必须在enableDanmaku()后调用
                    .start();    // 启动播放
        }

        timeText = (TextView) findViewById(R.id.time_text);
        beginTime = System.currentTimeMillis();
        findViewById(R.id.zb_detail_bq_start).performClick();
        ((TextView) findViewById(R.id.bszb_detail_bszb_title)).setText(name);
    }

    //{"detail":{"id":"2","name":"小组赛-第二场","description":"小组赛-第二场","fileurl":"http://soccer.baibaobike.com/apps/admin/_static/file/upload/2017/08/01/video1.mp4","ctime":"2017-08-24"},"other":null}

    /**
     * 获取相关视频数据
     */

    private void getOtherVideo() {
        if(videoid!=null){

            BaseActivity.callHttp(MapBuilder.build().add("action", "getVideoDetail").add("video_id", videoid).get(), new FunCallback() {
                @Override
                public void onSuccess(Object result, List object) {
                    MapConf.with(getBaseContext()).pair("detail-name->bszb_detail_bszb_title").pair("detail-description->bszb_detail__bszb_dis").pair("detail-ctime->bszb_detail__bszb_date").source(((NetEntity)result).getData().toString(),getWindow().getDecorView()).toView();
                    MapConf listconf = MapConf.with(getBaseContext()).pair("name->news_name").pair("fileurl->news_img").source(R.layout.adapter_bssp);
                    MapConf.with(getBaseContext()).pair("other->bs_detail_video", listconf)
                            .source(((NetEntity) result).getData().toString(), getWindow().getDecorView()).toView();
                    ((AdapterView)findViewById(R.id.bs_detail_video)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(new Intent(getBaseContext(),BSVideoActivity.class).putExtra("videoid",((Map)parent.getItemAtPosition(position)).get("id").toString()));
                        }
                    });
                    String name = intf.JsonUtil.findJsonLink("detail-fileurl",((NetEntity)result).getData().toString()).toString();
                    setSharetitle(name);
                    setSharetxt(name);
                    setShareurl(intf.JsonUtil.findJsonLink("detail-name",((NetEntity)result).getData().toString()).toString());
                    setVideoDetail(intf.JsonUtil.findJsonLink("detail-fileurl",((NetEntity)result).getData().toString()).toString(), intf.JsonUtil.findJsonLink("detail-name",((NetEntity)result).getData().toString()).toString());
                }

                @Override
                public void onFailure(Object result, List object) {

                }

                @Override
                public void onCallback(Object result, List object) {

                }
            });
        }else {
            findViewById(R.id.sp_detail_ll_video).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayerView == null) return;
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayerView == null) return;
        try {
            mPlayerView.onPause();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerView == null) return;
        try {
//            mPlayerView.onDestroy();
        } catch (Exception e) {
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPlayerView.configurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPlayerView != null) {
            if (mPlayerView.handleVolumeKey(keyCode)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
