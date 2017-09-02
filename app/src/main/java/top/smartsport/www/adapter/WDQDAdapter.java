package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.WDQDInfo;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/7/30.
 */

public class WDQDAdapter extends EntityListAdapter<WDQDInfo,WDQDViewHolder> {


    public WDQDAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_wdqd;
    }

    @Override
    protected WDQDViewHolder getViewHolder(View root) {
        return new WDQDViewHolder(root);
    }

    @Override
    protected void initViewHolder(WDQDViewHolder wdqdViewHolder, int position) {
        wdqdViewHolder.init(getItem(position));
    }
}
class WDQDViewHolder extends ViewHolder{
    @ViewInject(R.id.adapter_wdqd_people)
    private TextView adapter_wdqd_people;
    @ViewInject(R.id.adapter_wdqd_check)
    private ImageView adapter_wdqd_check;

    public WDQDViewHolder(View root) {
        super(root);
    }

    public void init(WDQDInfo info){
        adapter_wdqd_people.setText(info.getName());
    }
}
