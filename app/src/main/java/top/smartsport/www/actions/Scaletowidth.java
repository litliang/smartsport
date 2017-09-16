package top.smartsport.www.actions;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.base.openaction.MapConfTask;
import cn.android.service.ImageLoader;
import top.smartsport.www.utils.ImageUtil;

/**
 * Created by admin on 2017/9/3.
 */

public class Scaletowidth extends MapConfTask {

    @Override
    public void run(View view, Object item, String name, Object value, Object casevalue, View convertView, Object... objects) {
       com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(value.toString(), (ImageView) view, ImageUtil.getOptions(), ImageUtil.getImageLoadingListener(true));

    }
}
