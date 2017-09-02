package top.smartsport.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.constant.PlayerParams;
import com.lecloud.sdk.constant.StatusCode;
import com.lecloud.sdk.videoview.IMediaDataVideoView;
import com.lecloud.sdk.videoview.VideoViewListener;
import com.lecloud.sdk.videoview.base.BaseMediaDataVideoView;
import com.lecloud.sdk.videoview.live.ActionLiveVideoView;
import com.lecloud.skin.videoview.live.UIActionLiveVideoView;

import org.xutils.view.annotation.ContentView;

import java.util.LinkedHashMap;
import java.util.Map;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.leUtil.VideoLayoutParams;

/**
 * Created by Aaron on 2017/7/31.
 */
public class LeTvPlayActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //设置窗口透明，可避免播放器SurfaceView初始化时的黑屏现象
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        // 视频播放界面，保持屏幕常亮利于视频观看体验
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letv_play);
//        initData();
//        initViews();

        // videoContainer作为视频播放器的容器使用
        RelativeLayout videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);
        //无皮肤播放器请初始化ActionLiveVideoView
        // videoView = new ActionLiveVideoView(this);
        //有皮肤播放器请初始化UIActionLiveVideoView
        videoView = new UIActionLiveVideoView(this);
        //将播放器添加到容器中
        // 在这儿，我们使用的是16:9的比例适配播放器界面
        //详细请参考Demo工程PlayActivity布局添加播放器时的param配置
        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        videoContainer.addView((View) videoView, params);
    }

    public final static String DATA = "data";

    private IMediaDataVideoView videoView;

    LinkedHashMap<String, String> rateMap = new LinkedHashMap<String, String>();
    VideoViewListener mVideoViewListener = new VideoViewListener() {


        @Override
        public void onStateResult(int event, Bundle bundle) {
            handleVideoInfoEvent(event, bundle);// 处理视频信息事件
            handlePlayerEvent(event, bundle);// 处理播放器事件
            handleLiveEvent(event, bundle);// 处理直播类事件,如果是点播，则这些事件不会回调

        }

        @Override
        public String onGetVideoRateList(LinkedHashMap<String, String> map) {
            rateMap = map;
            for (Map.Entry<String, String> rates : map.entrySet()) {
                if (rates.getValue().equals("高清")) {
                    return rates.getKey();
                }
            }
            return "";
        }
    };
    private String mPlayUrl;
    private Bundle mBundle;
    private int mPlayMode;
    private boolean mHasSkin;
    private boolean mPano;

    private TextView timeText;
    private long beginTime;


    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mBundle = intent.getBundleExtra(DATA);
            if (mBundle == null) {
                Toast.makeText(this, "no data", Toast.LENGTH_LONG).show();
                return;
            } else {
                mPlayUrl = mBundle.getString("path");
                mPlayMode = mBundle.getInt(PlayerParams.KEY_PLAY_MODE, -1);
            }
        }
    }

    private void initViews() {
        switch (mPlayMode) {
            case PlayerParams.VALUE_PLAYER_ACTION_LIVE: {
                videoView = mHasSkin ? new UIActionLiveVideoView(this) : new ActionLiveVideoView(this);
                setActionLiveParameter(mBundle.getBoolean(PlayerParams.KEY_PLAY_USEHLS));
                break;
            }
            default:
                videoView = new BaseMediaDataVideoView(this);
                break;
        }
        if (videoView instanceof UIActionLiveVideoView) {
            ((UIActionLiveVideoView) videoView).setVideoAutoPlay(true);
        }
        videoView.setVideoViewListener(mVideoViewListener);

        final RelativeLayout videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);
        videoContainer.addView((View) videoView, VideoLayoutParams.computeContainerSize(this, 16, 9));
        if (!TextUtils.isEmpty(mPlayUrl)) {
            videoView.setPanorama(mPano);
            videoView.setDataSource(mPlayUrl);
        } else {
            videoView.setDataSource(mBundle);
        }

        timeText = (TextView) findViewById(R.id.time_text);
        beginTime = System.currentTimeMillis();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.onPause();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.onDestroy();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (videoView != null) {
            videoView.onConfigurationChanged(newConfig);
        }
    }

    /**
     * 处理播放器本身事件，具体事件可以参见IPlayer类
     */
    private void handlePlayerEvent(int state, Bundle bundle) {
        switch (state) {
            case PlayerEvent.ACTION_LIVE_PLAY_PROTOCOL:
                setActionLiveParameter(bundle.getBoolean(PlayerParams.KEY_PLAY_USEHLS));
                break;
            case PlayerEvent.PLAY_VIDEOSIZE_CHANGED:
                /**
                 * 获取到视频的宽高的时候，此时可以通过视频的宽高计算出比例，进而设置视频view的显示大小。
                 * 如果不按照视频的比例进行显示的话，(以surfaceView为例子)内容会填充整个surfaceView。
                 * 意味着你的surfaceView显示的内容有可能是拉伸的
                 */
                break;

            case PlayerEvent.PLAY_PREPARED:
                // 播放器准备完成，此刻调用start()就可以进行播放了
                if (videoView != null) {
                    videoView.onStart();
                }
                break;
            case PlayerEvent.PLAY_INFO:
                int code = bundle.getInt(PlayerParams.KEY_RESULT_STATUS_CODE);
                if (code == StatusCode.PLAY_INFO_VIDEO_RENDERING_START) {
                    long startPlayTime = (System.currentTimeMillis() - beginTime);
                    float num = (float) startPlayTime / 1000;
                    if (timeText != null) {
                        timeText.setText("起播耗时：" + num + "秒");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 处理直播类事件
     */
    private void handleLiveEvent(int state, Bundle bundle) {
    }

    /**
     * 处理视频信息类事件
     */
    private void handleVideoInfoEvent(int state, Bundle bundle) {
    }

    private void setActionLiveParameter(boolean hls) {
        if (hls) {
            videoView.setCacheWatermark(1000, 100);
            videoView.setMaxDelayTime(50000);
            videoView.setCachePreSize(1000);
            videoView.setCacheMaxSize(40000);
        } else {
            //rtmp
            videoView.setCacheWatermark(500, 100);
            videoView.setMaxDelayTime(1000);
            videoView.setCachePreSize(200);
            videoView.setCacheMaxSize(10000);
        }
    }
}
