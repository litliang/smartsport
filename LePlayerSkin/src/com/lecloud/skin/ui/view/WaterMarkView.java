package com.lecloud.skin.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.lecloud.sdk.api.md.entity.action.WaterConfig;
import com.lecloud.skin.ui.utils.PxUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class WaterMarkView extends RelativeLayout {
    public static final int ALTERNATE_DISPLAY_MSG = 1;
    private List<WaterConfig> mWaterMarks;
    private Context mContext;
    private WaterMarkImageView mCurWaterMark;
    private String mCurPicUrl = "";
    private WaterConfig mCurWaterConfig;
    private static final int ALTERNATE_DISPLAY_TIME = 60 * 1000;

    private HashMap<String, WaterMarkImageView> map = new HashMap<>();

    public WaterMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WaterMarkView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    public void setWaterMarks(List<WaterConfig> marks) {
        if (mWaterMarks != null) {
            removeAllViews();
            mWaterMarks = null;
        }
        mWaterMarks = marks;
        for (WaterConfig waterConfig : marks) {
            WaterMarkImageView waterMark = new WaterMarkImageView(mContext, waterConfig.getPicUrl());
            map.put(waterConfig.getPicUrl(), waterMark);
        }
        mCurWaterConfig = marks.get(0);
        mCurPicUrl = mCurWaterConfig.getPicUrl();
        mCurWaterMark = map.get(mCurPicUrl);
        addWaterConfigView(mCurWaterConfig.getPos());
    }


    public void addWaterConfigView(String position) {
        if (mCurWaterMark == null || mCurWaterConfig == null) {
            return;
        }
        int pos = 0;
        pos = Integer.parseInt(mCurWaterConfig.getPos());
        LayoutParams params = getWatermarkLocation(pos);
        addView(mCurWaterMark, params);
    }


    private void removeWaterConfigView() {
        removeView(mCurWaterMark);
    }

    private RelativeLayout.LayoutParams getWatermarkLocation(int location) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(PxUtils.dip2px(mContext, 40), PxUtils.dip2px(mContext, 26));
        switch (location) {
            case 1: // 左上角
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case 2: // 右上角
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

                break;
            case 3: // 左下角
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case 4: // 右下角
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
        }
        int margin = PxUtils.dip2px(mContext, 12);
        params.leftMargin = margin;
        params.rightMargin = margin;
        params.topMargin = margin;
        params.bottomMargin = margin;
        return params;

    }

    public void show() {
//        Log.d("test", "show: ===");
        if (map.size() > 1) {
            initHandler();
        }
        setVisibility(View.VISIBLE);
        sendMessage();
    }

    public void hide() {
//        Log.d("test", "hide: ===");
        destroyHandler();
        setVisibility(View.INVISIBLE);
    }

    MyHandler handler = null;

    private void initHandler() {
        handler = new MyHandler(WaterMarkView.this);
    }

    public void sendMessage() {
        if (handler != null) {
            handler.removeMessages(ALTERNATE_DISPLAY_MSG);
            handler.sendEmptyMessageDelayed(ALTERNATE_DISPLAY_MSG, ALTERNATE_DISPLAY_TIME);
        }
    }

    private void destroyHandler() {
        if (handler != null) {
            handler.removeMessages(ALTERNATE_DISPLAY_MSG);
            handler = null;
        }
    }

    public String showNextWaterConfig() {
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String url = (String) iterator.next();
            if (!mCurPicUrl.equals(url)) {
                mCurPicUrl = url;
//                Log.d("test", "showNextWaterConfig:=url= " + mCurPicUrl);
                mCurWaterMark = map.get(mCurPicUrl);
                for (WaterConfig config1 : mWaterMarks) {
                    if (config1.getPicUrl().equals(mCurPicUrl)) {
                        mCurWaterConfig = config1;
                        return mCurWaterConfig.getPos();
                    }
                }
            }
        }
        return mCurWaterConfig.getPos();
    }

    static class MyHandler extends Handler {
        WeakReference<WaterMarkView> mWaterConfig;

        MyHandler(WaterMarkView view) {
            mWaterConfig = new WeakReference<WaterMarkView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            WaterMarkView view = mWaterConfig.get();
            switch (msg.what) {
                case ALTERNATE_DISPLAY_MSG:
                    view.removeWaterConfigView();
                    view.addWaterConfigView(view.showNextWaterConfig());
                    view.sendMessage();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
