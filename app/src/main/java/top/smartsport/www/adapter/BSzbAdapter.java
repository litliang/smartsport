package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.BSzbInfo;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/7/24.
 * 比赛---直播
 */
public class BSzbAdapter extends EntityListAdapter<BSzbInfo,BSzbViewHolder>
{
    public BSzbAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_bszb;
    }

    @Override
    protected BSzbViewHolder getViewHolder(View root) {
        return new BSzbViewHolder(root);
    }

    @Override
    protected void initViewHolder(BSzbViewHolder bSzbViewHolder, int position) {
        bSzbViewHolder.init(getItem(position));
    }
}

class BSzbViewHolder extends ViewHolder{
    @ViewInject(R.id.adapter_bszb_img)
    private ImageView adapter_bszb_img;
    @ViewInject(R.id.adapter_bszb_state)
    private TextView adapter_bszb_state;
    @ViewInject(R.id.adapter_bszb_title)
    private TextView adapter_bszb_title;
    @ViewInject(R.id.adapter_bszb_date)
    private TextView adapter_bszb_date;
    @ViewInject(R.id.adapter_bszb_address)
    private TextView adapter_bszb_address;

    public BSzbViewHolder(View root) {
        super(root);
    }

    public void init(BSzbInfo info){
        ImageLoader.getInstance().displayImage(info.getCoverImgUrl(), adapter_bszb_img, ImageUtil.getOptions());
        adapter_bszb_state.setText(info.getActivityStatus());
        adapter_bszb_title.setText(info.getActivityName());
        adapter_bszb_date.setText(info.getStartTime());
        adapter_bszb_address.setText(info.getDescription());

    }
}
