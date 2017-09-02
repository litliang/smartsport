package top.smartsport.www.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.SSJBInfo;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/7/26.
 */

public class SSJBAdapter extends EntityListAdapter<SSJBInfo,SSJBViewHolder>{

    private int clickTemp = -1;
    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }

    public SSJBAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_ssjb;
    }

    @Override
    protected SSJBViewHolder getViewHolder(View root) {
        return new SSJBViewHolder(root);
    }

    @Override
    protected void initViewHolder(SSJBViewHolder ssjbViewHolder, int position) {

        ssjbViewHolder.init(getItem(position),position,clickTemp);
    }
}

class SSJBViewHolder extends ViewHolder{
    @ViewInject(R.id.tv_ssjb)
    private TextView tv_ssjb;

    public SSJBViewHolder(View root) {
        super(root);
    }

    public void init(SSJBInfo info,int position,int clickTemp){
        if(clickTemp==position){
            tv_ssjb.setBackgroundResource(R.drawable.shape_bg_hotcity_green);
            tv_ssjb.setTextColor(Color.parseColor("#3CB963"));
        }else {
            tv_ssjb.setBackgroundResource(R.drawable.shape_bg_hotcity);
            tv_ssjb.setTextColor(Color.parseColor("#8C8C8C"));
        }
        if(tv_ssjb.getText().toString().startsWith("全部")){
            tv_ssjb.setText("全部");
        }else {
            tv_ssjb.setText(info.getName());
        }
    }
}
