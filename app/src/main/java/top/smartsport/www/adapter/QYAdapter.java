package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.QYInfo;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/8/1.
 * 球员
 */

public class QYAdapter extends EntityListAdapter<QYInfo,QYViewHolder> {
    public QYAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_qd_home;
    }

    @Override
    protected QYViewHolder getViewHolder(View root) {
        return new QYViewHolder(root);
    }

    @Override
    protected void initViewHolder(QYViewHolder qyViewHolder, int position) {
        qyViewHolder.init(getItem(position));
    }
}

class QYViewHolder  extends ViewHolder{
    @ViewInject(R.id.adapter_home_header)
    private ImageView adapter_home_header;
    @ViewInject(R.id.adapter_home_name)
    private TextView adapter_home_name;
    @ViewInject(R.id.adapter_home_content)
    private TextView adapter_home_content;

    public QYViewHolder(View root) {
        super(root);
    }

    public void init(QYInfo info){
        //        ImageLoader.getInstance().displayImage(info.getPosition(), adapter_home_header, ImageUtil.getOptions());
        adapter_home_name.setText(info.getName());
        adapter_home_content.setText(info.getPosition());
    }
}
