package top.smartsport.www.fragment.information;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import top.smartsport.www.bean.Carousel;
import top.smartsport.www.listener.OnClickThrottleListener;
import top.smartsport.www.widget.banner.AutoFlingPagerAdapter;

/**
 * deprecation:资讯Banner适配器
 * author:AnYB
 * time:2017/9/24
 */
public class ZXBannerAdapter extends AutoFlingPagerAdapter<Carousel> {

    @Override
    public View instantiateView(Context context) {
        ImageView convertView = new ImageView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.
                MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        convertView.setAdjustViewBounds(true);
        convertView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        convertView.setLayoutParams(layoutParams);
        return convertView;
    }

    @Override
    public void bindView(final Carousel carousel, View view, int position) {
        ImageView imageView = (ImageView) view;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.
                MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(layoutParams);
        ImageLoader.getInstance().displayImage(carousel.getImgurl(), imageView);
        imageView.setOnClickListener(new OnClickThrottleListener() {
            @Override
            protected void onThrottleClick(View v) {
                //TODO Banner点击事件
            }
        });
    }

    @Override
    public String getTitle(int position) {
        return "";
    }

}
