package top.smartsport.www.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.QXJBInfo;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by gst-pc on 2017/9/23.
 */
public class QXJBAdapter extends EntityListAdapter<QXJBInfo,QXJBViewHolder> {
    private int clickTemp = -1;
    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }
    public QXJBAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_bssz;
    }

    @Override
    protected QXJBViewHolder getViewHolder(View root) {
        return new QXJBViewHolder(root);
    }

    @Override
    protected void initViewHolder(QXJBViewHolder qxjbViewHolder, int position) {
        qxjbViewHolder.init(getItem(position),position,clickTemp);
    }
}
class QXJBViewHolder extends ViewHolder {
    @ViewInject(R.id.tv_bssz)
    private TextView tv_bssz;

    public QXJBViewHolder(View root) {
        super(root);
    }

    public void init(QXJBInfo info,int position,int clickTemp){
        if(clickTemp==position){
            tv_bssz.setBackgroundResource(R.drawable.shape_bg_hotcity_green);
            tv_bssz.setTextColor(Color.parseColor("#3CB963"));
        }else {
            tv_bssz.setBackgroundResource(R.drawable.shape_bg_hotcity);
            tv_bssz.setTextColor(Color.parseColor("#8C8C8C"));
        }
//        if(tv_bssz.getText().toString().startsWith("全部")){
//            tv_bssz.setText("全部");
//        }else {
            tv_bssz.setText(info.getName());
//        }
    }
}