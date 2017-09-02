package com.lecloud.skin.videoview.live;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lecloud.sdk.api.md.entity.action.LiveInfo;
import com.lecloud.sdk.api.md.entity.live.Stream;
import com.lecloud.sdk.api.status.ActionStatus;
import com.lecloud.sdk.api.status.ActionStatusListener;
import com.lecloud.sdk.api.timeshift.ItimeShiftListener;
import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.constant.PlayerParams;
import com.lecloud.sdk.constant.StatusCode;
import com.lecloud.sdk.http.logutils.LeLog;
import com.lecloud.sdk.pano.base.BasePanoSurfaceView;
import com.lecloud.sdk.player.IAdPlayer;
import com.lecloud.sdk.player.IMediaDataActionPlayer;
import com.lecloud.sdk.player.IMediaDataPlayer;
import com.lecloud.sdk.player.IPlayer;
import com.lecloud.sdk.player.base.BaseMediaDataPlayer;
import com.lecloud.sdk.surfaceview.ISurfaceView;
import com.lecloud.sdk.utils.NetworkUtils;
import com.lecloud.sdk.videoview.live.ActionLiveVideoView;
import com.lecloud.skin.R;
import com.lecloud.skin.ui.ILetvLiveUICon;
import com.lecloud.skin.ui.ILetvSwitchMachineListener;
import com.lecloud.skin.ui.ILetvUICon;
import com.lecloud.skin.ui.LetvLiveUIListener;
import com.lecloud.skin.ui.impl.LetvLiveUICon;
import com.lecloud.skin.ui.utils.ScreenUtils;
import com.lecloud.skin.ui.view.V4MultLiveRightView;
import com.lecloud.skin.ui.view.VideoNoticeView;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class UIActionLiveVideoView extends ActionLiveVideoView {
    public static final String TAG = "UIActionLiveVideoView";

    protected ILetvLiveUICon letvLiveUICon;
    protected List<String> vtypList = new ArrayList<String>();
    protected V4MultLiveRightView mV4MultLiveRightView;
    protected TextView timeTextView;
    protected long serverTime;

    private boolean isSeeking = false;
    private ISurfaceView surfaceView;
    private int mPortraitWidth;
    private int mPortraitHeight;
    private int mLandscapeWidth;
    private int mLandscapeHeight;

    public UIActionLiveVideoView(Context context) {
        super(context);
        initUICon(context);
        initTimeShiftListener();
        initStatusListener();
    }

    private void initTimeShiftListener() {
        setTimeShiftListener(new ItimeShiftListener() {

            @Override
            public void onChange(long serverTime, long currentTime, long begin) {
                Log.e(TAG, "onChange: serverTime=" + serverTime + "currentTime=" + currentTime + "begin=" + begin);
                UIActionLiveVideoView.this.serverTime = serverTime;
                if (letvLiveUICon != null && !isSeeking) {
                    letvLiveUICon.setTimeShiftChange(serverTime, currentTime, begin);
                }
            }
        });
    }

    @Override
    public void setSurface(Surface surface) {
        if (!NetworkUtils.hasConnect(context)) {
            return;
        }
        player.setDisplay(surface);
    }

    @Override
    public void onSingleTapUp(MotionEvent e) {
        letvLiveUICon.performClick();
    }

    @Override
    public void onNotSupport(int mode) {
        Toast.makeText(context, "not support current mode " + mode, Toast.LENGTH_LONG).show();
    }

    private void initStatusListener() {
        if (player == null) {
            return;
        }
        ((IMediaDataActionPlayer) player).setActionStatusListener(new ActionStatusListener() {
            @Override
            public void onChange(ActionStatus actionStatus) {
                Log.d("TAG", "actionStatus " + actionStatus);
                if (ActionStatus.STATUS_END == actionStatus.getStatus() || ActionStatus.STATUS_INTERRUPTED == actionStatus.getStatus()) {
                    player.stop();
                    ((IMediaDataActionPlayer) player).resetLiveId();
                }
                processActionStatus(actionStatus.getStatus());
            }
        });
    }

    @Override
    protected void processActionStatus(int state) {
        super.processActionStatus(state);
        letvLiveUICon.hideLoading();
        letvLiveUICon.hideWaterMark();
        letvLiveUICon.processActionStatus(state);
        if (state == ActionStatus.STATUS_END) {
            removeRight();
        }
    }

    private void removeRight() {
        if (mV4MultLiveRightView != null) {
            removeView(mV4MultLiveRightView);
            mV4MultLiveRightView = null;
        }
    }

    @Override
    protected void processLiveStatus(int state) {
        super.processLiveStatus(state);
        letvLiveUICon.hideLoading();
        letvLiveUICon.hideWaterMark();
        letvLiveUICon.processLiveStatus(state);
        if (state == LiveInfo.STATUS_END) {
            removeRight();
        }
    }

    private void initUICon(final Context context) {
        letvLiveUICon = new LetvLiveUICon(context);
        letvLiveUICon.setLiveUIOnClickListener();
        letvLiveUICon.setPlayState(false);
        letvLiveUICon.showLoadingAnimation();
//        letvLiveUICon.setGravitySensor(false);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(letvLiveUICon.getView(), params);
        letvLiveUICon.setMachineListener(new ILetvSwitchMachineListener() {
            @Override
            public void showSwitchMachineBtn(boolean isLockFlag) {
                if (mV4MultLiveRightView == null) {
                    return;
                }

                if (isLockFlag) {
                    mV4MultLiveRightView.hideLiveMachineLayout();
                } else {
                    mV4MultLiveRightView.showLiveMachineLayout();
                }
            }
        });
        letvLiveUICon.setRePlayListener(new VideoNoticeView.IReplayListener() {

            @Override
            public Bundle getReportParams() {
                return ((BaseMediaDataPlayer) player).getReportParams();
            }

            @Override
            public void onRePlay() {
                if (!mVideoAutoPlay) {
                    mVideoAutoPlay = true;
                    setVideoAutoPlay(mVideoAutoPlay);
                }
                if (letvLiveUICon != null) {
                    letvLiveUICon.setTimeShiftChange(0, 0, 0);
                    if (letvLiveUICon.isLockScreen() && (letvLiveUICon.isFullScreen() || ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_LANDSCAPE)) {
                        letvLiveUICon.canGesture(true);
                    }
                }
                player.retry();
            }
        });
        letvLiveUICon.setLetvLiveUIListener(new LetvLiveUIListener() {

            @Override
            public void setRequestedOrientation(int requestedOrientation) {
                if (context instanceof Activity) {
                    if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                        showMultLiveBtn(false);
                    }
                    ((Activity) context).setRequestedOrientation(requestedOrientation);
                }
            }

            @Override
            public void resetPlay() {
                if (letvLiveUICon != null && !letvLiveUICon.isLockScreen() && (letvLiveUICon.isFullScreen() || ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_LANDSCAPE)) {
                    letvLiveUICon.canGesture(true);
                }
                player.retry();
            }

            @Override
            public void onSetDefination(int positon) {
                letvLiveUICon.showLoadingProgress();
                ((IMediaDataPlayer) player).setDataSourceByRate(vtypList.get(positon));
            }

            @Override
            public void onSeekTo(float sec) {
                isSeeking = false;
                LeLog.iPrint(TAG, "------sec:" + (long) sec);
                if (player != null) {
                    letvLiveUICon.showLoadingProgress();
                    seekTimeShift((long) sec);
                }
            }

            @Override
            public void onClickPlay() {
                letvLiveUICon.setPlayState(!player.isPlaying());
                if (!mVideoAutoPlay) {
                    if (player instanceof IMediaDataActionPlayer) {
                        ((IMediaDataActionPlayer) player).prepareVideoPlay();
                    }
                    mVideoAutoPlay = true;
                    setVideoAutoPlay(mVideoAutoPlay);
                }
                if (player.isPlaying()) {
                    mVideoPlaying = false;
                    dealPlayCompletion();
                } else {
                    player.retry();
                    letvLiveUICon.showController(true);
                    if (!letvLiveUICon.isFullScreen() || ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_LANDSCAPE) {
                        letvLiveUICon.canGesture(true);
                        showMultLiveBtn(true);
                    }
                }
            }

            @Override
            public void onUIEvent(int event, Bundle bundle) {
                // TODO Auto-generated method stub

            }

            @Override
            public int onSwitchPanoControllMode(int controllMode) {
                if (mPanoEvent != null) {
                    return mPanoEvent.switchControllMode(controllMode);
                }
                return BasePanoSurfaceView.CONTROLL_MODE_TOUCH;
            }

            @Override
            public int onSwitchPanoDisplayMode(int displayMode) {
                if (mPanoEvent != null) {
                    return mPanoEvent.switchDisplayMode(displayMode);
                }
                return BasePanoSurfaceView.DISPLAY_MODE_NORMAL;
            }

            @Override
            public void onProgressChanged(int progress) {
                // TODO Auto-generated method stub
                ((LetvLiveUICon) letvLiveUICon).syncSeekProgress(progress);
            }

            @Override
            public void onStartSeek() {
                // TODO Auto-generated method stub
                isSeeking = true;
            }

            @Override
            public void onEndSeek() {
                isSeeking = false;
            }

        });
        letvLiveUICon.setVrDisplayMode(false);
    }

    private void dealPlayCompletion() {
        letvLiveUICon.setPlayState(false);
        releaseAudioFocus();
        player.stop();
        player.reset();
        player.release();
        letvLiveUICon.showController(false);
        letvLiveUICon.canGesture(false);
        showMultLiveBtn(false);
        enablePanoGesture(false);
    }

    private void showMultLiveBtn(boolean isShow) {
        if (mV4MultLiveRightView == null || mV4MultLiveRightView.mMultLiveBtn == null) {
            return;
        }

        if (isShow) {
            if (((Activity) context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mV4MultLiveRightView.setVisibility(View.VISIBLE);
            }
        } else {
            if (mV4MultLiveRightView.getVisibility() == View.VISIBLE) {
                if (mV4MultLiveRightView.isShowSubLiveView) {
                    mV4MultLiveRightView.toggleSubMultLiveView();
                }
                mV4MultLiveRightView.stopAndRelease();
                mV4MultLiveRightView.setVisibility(GONE);
            } else {
            }
        }
    }

    protected void enablePanoGesture(boolean enable) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_LANDSCAPE) {
            mPortraitWidth = h;
            mPortraitHeight = h * 9 / 16;
            mLandscapeWidth = w;
            mLandscapeHeight = h;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (letvLiveUICon != null && letvLiveUICon.isLockScreen()) {
            if (ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_PORTRAIT) {
                getLayoutParams().width = mPortraitWidth;
                getLayoutParams().height = mPortraitHeight;
            } else {
                getLayoutParams().width = mLandscapeWidth;
                getLayoutParams().height = mLandscapeHeight;
            }
            return;
        }
        if (ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_PORTRAIT) {
            letvLiveUICon.setRequestedOrientation(ILetvUICon.SCREEN_PORTRAIT, UIActionLiveVideoView.this);
            if (mV4MultLiveRightView != null) {
                if (mV4MultLiveRightView.isShowSubLiveView) {
                    mV4MultLiveRightView.toggleSubMultLiveView();
                }
                mV4MultLiveRightView.stopAndRelease();
                mV4MultLiveRightView.setVisibility(GONE);
            }
        } else {
            letvLiveUICon.setRequestedOrientation(ILetvUICon.SCREEN_LANDSCAPE, UIActionLiveVideoView.this);
            if (mV4MultLiveRightView != null) {
                if (player != null && player.isPlaying()) {
                    mV4MultLiveRightView.setVisibility(VISIBLE);
                    letvLiveUICon.canGesture(true);
                }
            }
        }
        if (hasPanoView()) {
            letvLiveUICon.setVrDisplayMode(false);
            letvLiveUICon.isOpenGyroMode(false);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void prepareVideoSurface() {
        if (hasPanoView()) {
            surfaceView = mPanoEvent.getPanoVideoView();
            mPanoEvent.setPlayerPropertyListener();
            setVideoView(surfaceView);
            mPanoEvent.init();
            letvLiveUICon.canGesture(false);
            letvLiveUICon.isPano(true);
            ((LetvLiveUICon) letvLiveUICon).setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ((BasePanoSurfaceView) surfaceView).onPanoTouch(v, event);
                    return true;
                }
            });
        } else {
            super.prepareVideoSurface();
        }
    }

    @Override
    protected void onInterceptActionMediaDataSuccess(int event, Bundle bundle) {
        super.onInterceptActionMediaDataSuccess(event, bundle);
        if (actionLiveAuthInfo == null || mActionData == null || lv_info == null
                || action_play_info == null || action_live_config == null || liveInfo == null) {
            return;
        }
        if (!TextUtils.isEmpty(lv_info.getActivityName())) {
            letvLiveUICon.setTitle(lv_info.getActivityName());
        }
        if (((IMediaDataActionPlayer) player).actionAvailable() &&
                ((IMediaDataActionPlayer) player).machineInUse()) {
            letvLiveUICon.showLoadingPic(actionLiveAuthInfo.getCoverConfig());
        }
        letvLiveUICon.addWaterMark(actionLiveAuthInfo.getCoverConfig());
        //添加机位
        if (mV4MultLiveRightView == null) {
            List<LiveInfo> lives = action_play_info.getLiveInfos();
            if (lives.size() > 1) {
                initSubVideoView(lives);
            }
        }
        List<String> ratetypes = new ArrayList<String>();
        if (mV4MultLiveRightView != null) {
            mV4MultLiveRightView.setCurrentMultLive(liveInfo.getLiveId());
        }
        List<Stream> mStreams = liveInfo.getStreams();
        vtypList.clear();
        for (Stream stream : mStreams) {
            ratetypes.add(stream.getRateName());
            vtypList.add(stream.getRateType());
        }
        String definition = liveInfo.getVtypes().get(onInterceptSelectDefiniton(liveInfo.getVtypes(), liveInfo.getDefaultVtype()));
        letvLiveUICon.setRateTypeItems(ratetypes, definition);
    }

    @Override
    protected void onInterceptLiveMediaDataSuccess(int event, Bundle bundle) {
        super.onInterceptLiveMediaDataSuccess(event, bundle);
        if (liveAuthInfo != null) {
            List<String> ratetypes = new ArrayList<String>();
            List<Stream> mStreams = liveAuthInfo.getStreams();
            vtypList.clear();
            for (Stream stream : mStreams) {
                ratetypes.add(stream.getRateName());
                vtypList.add(stream.getRateType());
            }
            String definition = liveAuthInfo.getVtypes().get(onInterceptSelectDefiniton(liveAuthInfo.getVtypes(), liveAuthInfo.getDefaultVtype()));
            letvLiveUICon.setRateTypeItems(ratetypes, definition);
        }
    }

    @Override
    protected void onInterceptMediaDataError(int event, Bundle bundle) {
        letvLiveUICon.hideLoading();
        letvLiveUICon.hideWaterMark();
        letvLiveUICon.processMediaState(event, bundle);
        super.onInterceptMediaDataError(event, bundle);
    }

    private void initSubVideoView(List<LiveInfo> lives) {

        // LayoutInflater mLayoutInflater = (LayoutInflater)
        // context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT | RelativeLayout.CENTER_VERTICAL);
        if (mV4MultLiveRightView == null) {
            mV4MultLiveRightView = new V4MultLiveRightView(context);
            addView(mV4MultLiveRightView, lp);
        }

        if (ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_LANDSCAPE && mVideoAutoPlay) {
            if (player != null && player.isPlaying()) {
                mV4MultLiveRightView.setVisibility(VISIBLE);
            } else {
                mV4MultLiveRightView.setVisibility(GONE);
            }
        } else {
            mV4MultLiveRightView.setVisibility(GONE);
        }
        mV4MultLiveRightView.setActionInfoDone();
        mV4MultLiveRightView.setStreams(lives);
        mV4MultLiveRightView.setSwitchMultLiveCallbackk(new V4MultLiveRightView.SwitchMultLiveCallback() {

            @Override
            public void switchMultLive(String liveId) {
                // initSubVideoView(liveInfo);
                if (player != null) {
                    setDataSourceByLiveId(liveId);
                }
            }
        });
    }

    @Override
    protected void notifyPlayerEvent(int event, Bundle bundle) {
        super.notifyPlayerEvent(event, bundle);
        letvLiveUICon.processPlayerState(event, bundle);
        switch (event) {
            case PlayerEvent.PLAY_PREPARED:
                letvLiveUICon.setPlayState(true);
                if (NetworkUtils.hasConnect(context) && !letvLiveUICon.isShowLoading()) {
                    letvLiveUICon.showLoadingProgress();
                }
                break;
            case PlayerEvent.PLAY_INFO:
                int code = bundle.getInt(PlayerParams.KEY_RESULT_STATUS_CODE);
                if (code == StatusCode.PLAY_INFO_VIDEO_RENDERING_START) {

                }

                switch (code) {
                    case StatusCode.PLAY_INFO_BUFFERING_START:// 500004
                        if (NetworkUtils.hasConnect(context) && !letvLiveUICon.isShowLoading()) {
                            letvLiveUICon.showLoadingProgress();
                        }
                        break;
                    case StatusCode.PLAY_INFO_BUFFERING_END:// 500005
                        letvLiveUICon.hideLoading();
                        break;
                    case StatusCode.PLAY_INFO_VIDEO_RENDERING_START:// 500006
                        if (ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_LANDSCAPE && !letvLiveUICon.isLockScreen()) {
                            if (mV4MultLiveRightView != null) {
                                mV4MultLiveRightView.setVisibility(VISIBLE);
                            }
                        }
                        mVideoPlaying = true;
                        letvLiveUICon.showWaterMark();
                        letvLiveUICon.hideLoading();
                        break;
                    default:
                        break;
                }
                break;
            case PlayerEvent.PLAY_ERROR_CDE:
            case PlayerEvent.PLAY_ERROR:// 205
                removeView(timeTextView);
                letvLiveUICon.getView().setVisibility(VISIBLE);
                letvLiveUICon.hideLoading();
                letvLiveUICon.hideWaterMark();
                dealPlayCompletion();
                break;
            case PlayerEvent.PLAY_SEEK_COMPLETE: {//209
                isSeeking = false;
                break;
            }
            case PlayerEvent.PLAY_COMPLETION:
                letvLiveUICon.setPlayState(false);
                mVideoPlaying = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @TargetApi(11)
    @Override
    protected void onInterceptAdEvent(int code, Bundle bundle) {
        switch (code) {
            case PlayerEvent.AD_START:
                letvLiveUICon.getView().setVisibility(GONE);
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                if (timeTextView == null) {
                    timeTextView = new TextView(context);
                    timeTextView.setBackgroundColor(Color.BLACK);
                    timeTextView.setAlpha(0.7f);
                    timeTextView.setTextColor(Color.WHITE);
                    timeTextView.setPadding(20, 20, 20, 20);
                }
                if (!timeTextView.isShown()) {
                    removeView(timeTextView);//添加前先移除
                    addView(timeTextView, lp);
                    bringChildToFront(timeTextView);
                }
                letvLiveUICon.processPlayerState(code, bundle);
                letvLiveUICon.hideLoading();
                break;
            case PlayerEvent.AD_BUFFERING_START:
                if (!NetworkUtils.hasConnect(context)) {
                    letvLiveUICon.processPlayerState(code, bundle);
                    letvLiveUICon.getView().setVisibility(VISIBLE);
                }
                break;
            case PlayerEvent.AD_BUFFERING_END:
                letvLiveUICon.getView().setVisibility(GONE);
                letvLiveUICon.processPlayerState(code, bundle);
                break;
            case PlayerEvent.AD_ERROR:
                if (!NetworkUtils.hasConnect(context)) {
                    letvLiveUICon.processPlayerState(code, bundle);
                }
            case IAdPlayer.AD_PLAY_ERROR:
            case PlayerEvent.AD_COMPLETE:
                removeView(timeTextView);
                letvLiveUICon.getView().setVisibility(VISIBLE);
//                by heyuekuai 2016/09/19 fix bug ad play complete not show loading pic
//                letvLiveUICon.hideLoading();
//                letvLiveUICon.hideWaterMark();
                break;
            case PlayerEvent.AD_PROGRESS:
                timeTextView.setText(getContext().getResources().getString(R.string.ad) + bundle.getInt(IAdPlayer.AD_TIME) + "s");
                break;

            default:
                break;
        }
        super.onInterceptAdEvent(code, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean isComplete() {
        // TODO
        return player != null && player.getStatus() == IPlayer.PLAYER_STATUS_EOS;
    }

    @Override
    public void setDataSource(Bundle bundle) {
        super.setDataSource(bundle);
        if (isFirstPlay) {
            int orientation = letvLiveUICon.getRequestedOrientation();
            if (orientation == 0) {
                letvLiveUICon.setRequestedOrientation(ILetvUICon.SCREEN_LANDSCAPE, this);
            } else {
                letvLiveUICon.setRequestedOrientation(ILetvUICon.SCREEN_PORTRAIT, this);
            }
            isFirstPlay = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!letvLiveUICon.isLockScreen() && (letvLiveUICon.isFullScreen() || ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_LANDSCAPE)) {
            letvLiveUICon.canGesture(true);
        }
        if (letvLiveUICon.isLockScreen() && mV4MultLiveRightView != null) {
            mV4MultLiveRightView.hideLiveMachineLayout();
        } else {
            showMultLiveBtn(true);
        }
        if (mVideoPlaying) {
            letvLiveUICon.showController(true);
        }
    }

    protected void setVideoView(ISurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        super.setVideoView(surfaceView);
    }

    public ISurfaceView getSurfaceView() {
        return surfaceView;
    }
}
