package com.lecloud.skin.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lecloud.skin.ui.LetvUIListener;
import com.lecloud.skin.ui.orientation.OrientationSensorUtils;
import com.lecloud.skin.ui.utils.ReUtils;
import com.lecloud.skin.ui.utils.ScreenUtils;
import com.lecloud.skin.ui.utils.StatusUtils;

/**
 * 顶部浮层
 * @author pys
 */
public class V4TopTitleView extends LinearLayout {
	private Context context;
	protected LetvUIListener mLetvUIListener;
    private TextView textView;
    private ImageView netStateView;
    private ImageView batteryView;
    private TextView timeView;

    private String title;
    private OrientationSensorUtils mOrientationSensorUtils;

    public V4TopTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

	public V4TopTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4TopTitleView(Context context) {
        super(context);
    }


    /**
     * 设置状态栏
     */
    public void setState() {
            /**
             * 设置标题
             */
            if (title != null) {
                textView.setText(title);
            }

            /**
             * 获取网络状态
             */
            netStateView.setImageLevel(StatusUtils.getWiFistate(context));
            /**
             * 电池
             */
            batteryView.setImageLevel(StatusUtils.getBatteryStatus(context));
            /**
             * 时间
             */
            timeView.setText(StatusUtils.getCurrentTime(context));
    }
    
    @Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		context = getContext();
		initView(context);
	}

    
    protected void initView(final Context context) {
        /**
         * 返回键
         */
        findViewById(ReUtils.getId(context, "full_back")).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//////////////如果需要返回键功能是大小屏切换，放开下面注释代码把 finish方法注释就可以了
            if (mLetvUIListener != null) {
                if (ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_LANDSCAPE) {
                    // mLetvUIListener.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mOrientationSensorUtils.setOrientation(OrientationSensorUtils.ORIENTATION_1);
                }else {
//                    mOrientationSensorUtils.setOrientation(OrientationSensorUtils.ORIENTATION_0);
                    ((Activity)getContext()).finish();//Aaron添加的
                }
                if (mOrientationSensorUtils != null) {
                    mOrientationSensorUtils.getmOrientationSensorListener().lockOnce(((Activity) getContext()).getRequestedOrientation());
                }
            }

//            ((Activity)getContext()).finish();
            }
        });
        
        textView = (TextView) findViewById(ReUtils.getId(context, "full_title"));
        netStateView = (ImageView) findViewById(ReUtils.getId(context, "full_net"));
        batteryView = (ImageView) findViewById(ReUtils.getId(context, "full_battery"));
        timeView = (TextView) findViewById(ReUtils.getId(context, "full_time"));
        setState();
    }

    
    public void setLetvUIListener(LetvUIListener mLetvUIListener) {
     this.mLetvUIListener = mLetvUIListener;
    }
    public void setTitle(String title){
    	if(!TextUtils.isEmpty(title)){
    		textView.setText(title);
    	}
    }
    
    /**
     * whether show topview's right views, including screen lock, time etc.
     * @param isShow
     */
    public void showTopRightView(boolean isShow){
    	int visibility;
    	if(isShow){
    		visibility = View.VISIBLE;
    	}else{
    		visibility = View.GONE;
    	}
		if(netStateView != null){
			netStateView.setVisibility(visibility);
		}
		if(batteryView != null){
			batteryView.setVisibility(visibility);
		}
		if(timeView != null){
			timeView.setVisibility(visibility);
		}
    }

    public void setOrientationSensorUtils(OrientationSensorUtils mOrientationSensorUtils) {
        this.mOrientationSensorUtils = mOrientationSensorUtils;
    }


}
