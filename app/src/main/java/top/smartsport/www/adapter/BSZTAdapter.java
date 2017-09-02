package top.smartsport.www.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.BSZTInfo;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/7/26.
 */

public class BSZTAdapter extends EntityListAdapter<BSZTInfo,BSZTViewHolder> {

    private int clickTemp = -1;
    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }
    public BSZTAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_bszt;
    }

    @Override
    protected BSZTViewHolder getViewHolder(View root) {
        return new BSZTViewHolder(root);
    }

    @Override
    protected void initViewHolder(BSZTViewHolder bsztViewHolder, int position) {
        bsztViewHolder.init(getItem(position),position,clickTemp);
    }
}

class BSZTViewHolder extends ViewHolder{
    @ViewInject(R.id.tv_bszt)
    private TextView tv_bszt;

    public BSZTViewHolder(View root) {
        super(root);
    }
    public void init(BSZTInfo info,int position,int clickTemp){
        if(clickTemp==position){
            tv_bszt.setBackgroundResource(R.drawable.shape_bg_hotcity_green);
            tv_bszt.setTextColor(Color.parseColor("#3CB963"));
        }else {
            tv_bszt.setBackgroundResource(R.drawable.shape_bg_hotcity);
            tv_bszt.setTextColor(Color.parseColor("#8C8C8C"));
        }
        if(tv_bszt.getText().toString().startsWith("全部")){
            tv_bszt.setText("全部");
        }else {
            tv_bszt.setText(info.getName());
        }
    }
}
