package top.smartsport.www.leUtil;

import android.content.Context;
import android.widget.RelativeLayout;

import top.smartsport.www.handler.GetDeviceInfo;

/**
 * Created by gaolinhua on 2016/5/26.
 */
public class VideoLayoutParams {
    public static RelativeLayout.LayoutParams computeContainerSize(Context context, int mWidth, int mHeight) {
        int width = GetDeviceInfo.getScreenWidth(context);
        int height = width * mHeight / mWidth;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        params.width = width;
        params.height = height;
//        params.height = 1920;
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        return params;
    }
}
