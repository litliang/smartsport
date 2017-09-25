package top.smartsport.www.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.KCJBInfo;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by gst-pc on 2017/9/22.
 */

public class KCJBAdapter extends EntityListAdapter<KCJBInfo,KCJBViewHolder> {

    private int clickTemp = -1;
    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }

    public KCJBAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_ssjb;
    }

    @Override
    protected KCJBViewHolder getViewHolder(View root) {
        return new KCJBViewHolder(root);
    }

    @Override
    protected void initViewHolder(KCJBViewHolder kcjbViewHolder, int position) {

        kcjbViewHolder.init(getItem(position),position,clickTemp);
    }
}

class KCJBViewHolder extends ViewHolder {
    @ViewInject(R.id.tv_ssjb)
    private TextView tv_ssjb;

    public KCJBViewHolder(View root) {
        super(root);
    }

    public void init(KCJBInfo info,int position,int clickTemp){
        if(clickTemp==position){
            tv_ssjb.setBackgroundResource(R.drawable.shape_bg_hotcity_green);
            tv_ssjb.setTextColor(Color.parseColor("#3CB963"));
        }else {
            tv_ssjb.setBackgroundResource(R.drawable.shape_bg_hotcity);
            tv_ssjb.setTextColor(Color.parseColor("#8C8C8C"));
        }
//        if(tv_ssjb.getText().toString().startsWith("全部")){
//            tv_ssjb.setText("全部");
//        }else {
            tv_ssjb.setText(info.getName());
//        }
    }
}