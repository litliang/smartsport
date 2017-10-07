package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.QYInfo;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.StringUtil;
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
    @ViewInject(R.id.qd_player_other_info)
    private LinearLayout qd_player_other_info;
    @ViewInject(R.id.tv_num)
    private TextView tv_num;
    @ViewInject(R.id.tv_position)
    private TextView tv_position;

    public QYViewHolder(View root) {
        super(root);
    }

    public void init(QYInfo info){
        ImageLoader.getInstance().displayImage(info.getHeader_url(), adapter_home_header, ImageUtil.getOptions(), ImageUtil.getImageLoadingListener());
        String name = info.getName();
        if(!StringUtil.isEmpty(name)) {
            adapter_home_name.setText(name);
        } else {
            adapter_home_name.setText("");
        }
        String title = info.getTitle();
        if(!StringUtil.isEmpty(title)) {
            adapter_home_content.setText(title);
        } else {
            adapter_home_content.setText("");
        }

//        adapter_home_name.setText(info.getName());
//        adapter_home_content.setText(info.getTitle());
        qd_player_other_info.setVisibility(View.VISIBLE);
        String number = info.getNumber();
        if(!StringUtil.isEmpty(number)) {
            tv_num.setText(number + "号");
        } else {
            tv_num.setText("0" + "号");
        }
//        tv_num.setText(info.getNumber() + "号");

        String position = info.getPosition();
        if(!StringUtil.isEmpty(position)) {
            tv_position.setText(position);
        } else {
            tv_position.setText("");
        }
//        tv_position.setText(info.getPosition());
    }
}
