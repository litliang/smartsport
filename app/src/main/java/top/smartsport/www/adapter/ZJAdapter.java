package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.ZJInfo;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/8/1.
 * 助教
 */

public class ZJAdapter extends EntityListAdapter<ZJInfo,ZJViewHolder> {
    public ZJAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_qd_home;
    }

    @Override
    protected ZJViewHolder getViewHolder(View root) {
        return new ZJViewHolder(root);
    }

    @Override
    protected void initViewHolder(ZJViewHolder zjViewHolder, int position) {
        zjViewHolder.init(getItem(position));
    }
}
class ZJViewHolder extends ViewHolder{

    @ViewInject(R.id.adapter_home_header)
    private ImageView adapter_home_header;
    @ViewInject(R.id.adapter_home_name)
    private TextView adapter_home_name;
    @ViewInject(R.id.adapter_home_content)
    private TextView adapter_home_content;

    public ZJViewHolder(View root) {
        super(root);
    }

    public void init(ZJInfo info){
//        ImageLoader.getInstance().displayImage(info.getPosition(), adapter_home_header, ImageUtil.getOptions());
        adapter_home_name.setText(info.getName());
        adapter_home_content.setText(info.getPosition());
    }

}
