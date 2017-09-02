package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.PicInfo;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/7/27.
 */

public class PICAdapter extends EntityListAdapter<PicInfo,PICViewHolder> {


    public PICAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_pic;
    }

    @Override
    protected PICViewHolder getViewHolder(View root) {
        return new PICViewHolder(root);
    }

    @Override
    protected void initViewHolder(PICViewHolder picViewHolder, int position) {
        picViewHolder.init(getItem(position));
    }
}

class PICViewHolder extends ViewHolder{
    @ViewInject(R.id.image)
    private ImageView image;

    public PICViewHolder(View root) {
        super(root);
    }

    public void init(PicInfo info){
        ImageLoader.getInstance().displayImage(info.getFileurl(), image, ImageUtil.getOptions());
    }

}
