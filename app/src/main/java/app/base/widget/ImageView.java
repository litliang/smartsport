package app.base.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by admin on 2017/9/8.
 */

public class ImageView extends android.support.v7.widget.AppCompatImageView{
    public ImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
