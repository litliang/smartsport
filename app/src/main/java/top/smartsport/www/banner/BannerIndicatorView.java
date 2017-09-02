package top.smartsport.www.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import top.smartsport.www.R;


/**
 * BannerIndicatorView是广告栏下方的小白点，用于指示当前的广告栏
 */
public class BannerIndicatorView extends LinearLayout {

    private static final String TAG = "BannerIndicatorView";

    private int lastPosition;

    public BannerIndicatorView(Context context) {
        super(context, null);
    }

    public BannerIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param count
     */
    public void init(int count) {
        lastPosition = 0;
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (int i = 0; i < count; i++) {

            View layout = inflater.inflate(R.layout.item_banner_indicator_dot, null);
            addView(layout);
        }
    }

    public void setFocus(int position) {
        getChildAt(lastPosition).findViewById(R.id.dot_view).setBackgroundResource(
                R.drawable.banner_indicator_dot_normal);
        getChildAt(position).findViewById(R.id.dot_view)
                .setBackgroundResource(R.drawable.banner_indicator_dot_focused);
        lastPosition = position;
    }
}
