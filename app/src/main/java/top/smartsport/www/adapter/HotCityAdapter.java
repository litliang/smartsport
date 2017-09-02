package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.HotCity;
import top.smartsport.www.utils.ViewHolder;

/**
 * @author：suntongfu
 * @time: 2016/7/29 10:43
 * @describe:
 */
public class HotCityAdapter extends EntityListAdapter<HotCity, HotCityHolder> {


    public HotCityAdapter(Context context) {
        super(context);
    }

    public HotCityAdapter(Context context, List<HotCity> list) {
        super(context, list);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.layout_hotcity;
    }

    @Override
    protected HotCityHolder getViewHolder(View root) {
        return new HotCityHolder(root);
    }

    @Override
    protected void initViewHolder(HotCityHolder hotCityHolder, int position) {
        hotCityHolder.init(getItem(position), mContext);
    }
}

class HotCityHolder extends ViewHolder {


    @ViewInject(R.id.tv_hotcity)
    private TextView tv_hotcity;

    public HotCityHolder(View root) {
        super(root);
    }


    public void init(final HotCity hotCity, final Context context) {
        tv_hotcity.setText(hotCity.getTitle());
    }
}
