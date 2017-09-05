package top.smartsport.www.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.lecloud.skin.ui.utils.ScreenUtils;
import com.lecloud.skin.videoview.live.UIActionLiveVideoView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.BSzbInfo;
import top.smartsport.www.bean.Data;
import top.smartsport.www.bean.Members;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.Schedule;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.leUtil.VideoLayoutParams;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/8/2.
 * 直播详情
 */
@ContentView(R.layout.activity_zbdetail)
public class ZBDetailActivity extends BaseActivity {
    public static String TAG = ZBDetailActivity.class.getName();

    private BSzbInfo bSzbInfo;

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    @ViewInject(R.id.zb_detail_bq_zhibo)
    private TextView zb_detail_bq_zhibo;
    @ViewInject(R.id.bszb_detail_frame)
    private FrameLayout bszb_detail_frame;
    @ViewInject(R.id.zb_detail_bq_start)
    private TextView zb_detail_bq_start;
    @ViewInject(R.id.bszb_detail_bszb_img)//图片
    private ImageView bszb_detail_bszb_img;
    @ViewInject(R.id.bszb_detail_bszb_title)
    private TextView bszb_detail_bszb_title;//标题
    @ViewInject(R.id.bszb_detail__bszb_date)
    private TextView bszb_detail__bszb_date;//时间
    @ViewInject(R.id.bszb_detail__bszb_address)
    private TextView bszb_detail__bszb_address;//地址
    @ViewInject(R.id.bszb_detail__bszb_dis)
    private TextView bszb_detail__bszb_dis;//描述

    @ViewInject(R.id.bszb_detail_a_img)
    private ImageView bszb_detail_a_img;
    @ViewInject(R.id.bszb_detail_a_name)
    private TextView bszb_detail_a_name;
    @ViewInject(R.id.bszb_detail_b_img)
    private ImageView bszb_detail_b_img;
    @ViewInject(R.id.bszb_detail_b_name)
    private TextView bszb_detail_b_name;

    @ViewInject(R.id.bszb_detail_num)
    private TextView bszb_detail_num;
    @ViewInject(R.id.bszb_detail_time)
    private TextView bszb_detail_time;

    @ViewInject(R.id.ll_title)
    private RelativeLayout ll_title;

    public final static String DATA = "data";

    private IMediaDataVideoView videoView;

    LinkedHashMap<String, String> rateMap  = new LinkedHashMap<>();
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


    @Override
    public void featureNoTitle() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    @Override
    protected void initView() {
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();

        Log.i("_________","3");
        bSzbInfo = (BSzbInfo) getObj(ZBDetailActivity.TAG);
        if(null==bSzbInfo){
            finish();
        }

        ImageLoader.getInstance().displayImage(bSzbInfo.getCoverImgUrl(), bszb_detail_bszb_img, ImageUtil.getOptions());
        bszb_detail_bszb_title.setText(bSzbInfo.getActivityName());
        bszb_detail__bszb_date.setText(bSzbInfo.getStartTime());
        bszb_detail__bszb_address.setText(bSzbInfo.getDescription());

        initViews();
        ShareParams shareParams = new ShareParams();
        shareParams.setText(bSzbInfo.getActivityName());
//        shareParams.setText(bSzbInfo.getActivityName());
        shareParams.setShareType(Platform.SHARE_TEXT);
//        shareParams.setImageUrl(bSzbInfo.getCoverImgUrl());
        share(shareParams, Sharetype.TEXT);

        getData();
    }


    private void initViews() {

        videoView = new UIActionLiveVideoView(this);

        videoView.setVideoViewListener(mVideoViewListener);

        RelativeLayout videoContainer = (RelativeLayout) findViewById(R.id.videoContainers);
        videoContainer.addView((View) videoView, VideoLayoutParams.computeContainerSize(this, 16, 9));

        Bundle bundle = new Bundle();
        bundle.putString(PlayerParams.KEY_PLAY_ACTIONID, bSzbInfo.getActivityId());
        videoView.setDataSource(bundle);

        timeText = (TextView)findViewById(R.id.time_text);
        beginTime = System.currentTimeMillis();
        findViewById(R.id.zb_detail_bq_start).performClick();

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

    //重写旋转时方法，不销毁activity
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (videoView != null) {
            videoView.onConfigurationChanged(newConfig);
        }
        if (ScreenUtils.getOrientation(ZBDetailActivity.this) == Configuration.ORIENTATION_LANDSCAPE) {

            Message msg = new Message();
            msg.what=102;
            handler.sendMessage(msg);
            Log.i("_________","0");
        }else {
            Message msg = new Message();
            msg.what=103;
            handler.sendMessage(msg);
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
                    float num= (float)startPlayTime/1000;
                    if(timeText != null){
                        timeText.setText("起播耗时："+ num + "秒");
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

    @Event(value = {R.id.zb_detail_bq_start,R.id.back})
    private void getEvent(View v){
        switch (v.getId()){
            case R.id.zb_detail_bq_start:
                Message msg = new Message();
                msg.what = 101;
                handler.sendMessage(msg);
                break;
            case R.id.back:
                finish();
                break;

        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 101:
                    bszb_detail_bszb_img.setVisibility(View.GONE);
                    zb_detail_bq_start.setVisibility(View.GONE);
                    zb_detail_bq_zhibo.setVisibility(View.GONE);
                    videoView.onStart();//Aaron添加，点击图片start按钮直接播放
                    Log.i("__________onclick","______");
                    break;
                case 102:
                    ll_title.setVisibility(View.GONE);
                    Log.i("__________________","no______");
                    break;
                case 103:
                    ll_title.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    /**
     * 直播接口列表
     * */


    private void getData(){

        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","getLetvLiveList");
            json.put("activity_id",bSzbInfo.getActivityId());
//            json.put("page",page);

//‘activity_id’ : ’’,     //直播活动ID（选填）
//‘activity_name’ : ’value’,   //直播活动名称（选填）
//‘activity_status’ : ’value’,   //直播活动状态（选填）0:未开始1:已开始 3:已结束
//‘offset’ : ’value’,   //从第几条数据开始查询，默认0（选填）
//‘fetch_size’ : ’value’   //一次返回多少条数据，默认为10，最多不能超过100条（选填）
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
            }

            @Override
            public void onSuccess(NetEntity entity) {

                Data data = entity.toObj(Data.class);
                Schedule info = data.toSchedule(Schedule.class);
                ImageLoader.getInstance().displayImage(info.getHome_logo(), bszb_detail_a_img, ImageUtil.getOptions());
                ImageLoader.getInstance().displayImage(info.getAway_logo(), bszb_detail_b_img, ImageUtil.getOptions());
                bszb_detail_a_name.setText(info.getHome_team());
                bszb_detail_b_name.setText(info.getAway_team());
                bszb_detail_num.setText(info.getRound());
                bszb_detail_time.setText(info.getStart_time());

            }
        });

    }
}
