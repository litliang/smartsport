package top.smartsport.www.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.BSSZInfo;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/7/26.
 */

public class BSSZAdapter extends EntityListAdapter<BSSZInfo,BSSZViewHolder> {
    private int clickTemp = -1;
    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }
    public BSSZAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_bssz;
    }

    @Override
    protected BSSZViewHolder getViewHolder(View root) {
        return new BSSZViewHolder(root);
    }

    @Override
    protected void initViewHolder(BSSZViewHolder bsszViewHolder, int position) {
        bsszViewHolder.init(getItem(position),position,clickTemp);
    }
}
class BSSZViewHolder extends ViewHolder{
    @ViewInject(R.id.tv_bssz)
    private TextView tv_bssz;

    public BSSZViewHolder(View root) {
        super(root);
    }

    public void init(BSSZInfo info,int position,int clickTemp){
        if(clickTemp==position){
            tv_bssz.setBackgroundResource(R.drawable.shape_bg_hotcity_green);
            tv_bssz.setTextColor(Color.parseColor("#3CB963"));
        }else {
            tv_bssz.setBackgroundResource(R.drawable.shape_bg_hotcity);
            tv_bssz.setTextColor(Color.parseColor("#8C8C8C"));
        }
        if(tv_bssz.getText().toString().startsWith("全部")){
            tv_bssz.setText("全部");
        }else {
            tv_bssz.setText(info.getName());
        }
    }
}
