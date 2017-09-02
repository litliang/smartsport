package com.lecloud.skin.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.lecloud.sdk.pano.base.BasePanoSurfaceView;
import com.lecloud.skin.ui.utils.ReUtils;

public class V4ChangeDisplayModeBtn extends V4ChangeModeBtn {
    //全景视频
    public static final int DISPLAY_MODE_NORMAL = BasePanoSurfaceView.DISPLAY_MODE_NORMAL;
    //vr视频
    public static final int DISPLAY_MODE_GLASS = BasePanoSurfaceView.DISPLAY_MODE_GLASS;

    //    public static final int
    public int displayMode = DISPLAY_MODE_NORMAL;

    public V4ChangeDisplayModeBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4ChangeDisplayModeBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4ChangeDisplayModeBtn(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (displayMode == DISPLAY_MODE_NORMAL) {
                    displayMode = DISPLAY_MODE_GLASS;
                } else {
                    displayMode = DISPLAY_MODE_NORMAL;
                }
                if (mLetvUIListener != null) {
                    mLetvUIListener.onSwitchPanoDisplayMode(displayMode);
                }
                reset();
            }
        });
        reset();
    }

    @Override
    protected void reset() {
        String btnResId = displayMode == DISPLAY_MODE_GLASS ? getTouchStyle() : getMoveStyle();
        setImageResource(ReUtils.getDrawableId(getContext(), btnResId));
    }

    @Override
    protected String getMoveStyle() {
        return "letv_skin_v4_btn_vr_normal";
    }

    @Override
    protected String getTouchStyle() {
        return "letv_skin_v4_btn_vr_pressed";
    }

    @Override
    protected void setVrDisplayMode(boolean glassPlayMode) {
        if (glassPlayMode) {
            displayMode = DISPLAY_MODE_GLASS;
        } else {
            displayMode = DISPLAY_MODE_NORMAL;
        }
        if (mLetvUIListener != null) {
            mLetvUIListener.onSwitchPanoDisplayMode(displayMode);
        }
        reset();
    }

}
