package top.smartsport.www.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.KCLYInfo;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by gst-pc on 2017/9/22.
 */
public class KCLYAdapter extends EntityListAdapter<KCLYInfo,KCLYViewHolder> {
    private int clickTemp = -1;
    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }
    public KCLYAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_bssz;
    }

    @Override
    protected KCLYViewHolder getViewHolder(View root) {
        return new KCLYViewHolder(root);
    }

    @Override
    protected void initViewHolder(KCLYViewHolder bsszViewHolder, int position) {
        bsszViewHolder.init(getItem(position),position,clickTemp);
    }
}
class KCLYViewHolder extends ViewHolder {
    @ViewInject(R.id.tv_bssz)
    private TextView tv_bssz;

    public KCLYViewHolder(View root) {
        super(root);
    }

    public void init(KCLYInfo info,int position,int clickTemp){
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