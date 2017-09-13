package top.smartsport.www.listener;

import android.view.View;

import top.smartsport.www.utils.CommonUtils;

/**
 * deprecation:防止连续点击监听
 * author:AnYB
 * time:2017/9/24
 */
public abstract class OnClickThrottleListener implements View.OnClickListener {

    protected abstract void onThrottleClick(View v);

    @Override
    public void onClick(View v) {
        if (CommonUtils.isFastDoubleClick()) return;

        onThrottleClick(v);
    }

}
