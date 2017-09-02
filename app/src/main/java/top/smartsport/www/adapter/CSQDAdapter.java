package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.CSQDInfo;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/7/25.
 */

public class CSQDAdapter extends EntityListAdapter<CSQDInfo,CSQDViewHolder> {
    public CSQDAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_csqd;
    }

    @Override
    protected CSQDViewHolder getViewHolder(View root) {
        return new CSQDViewHolder(root);
    }

    @Override
    protected void initViewHolder(CSQDViewHolder csqdViewHolder, int position) {
        csqdViewHolder.init(getItem(position));
    }
}
class CSQDViewHolder extends ViewHolder{
    @ViewInject(R.id.adapter_csqd_name)
    private TextView adapter_csqd_name;
    @ViewInject(R.id.adapter_csqd_dc)
    private TextView adapter_csqd_dc;
    @ViewInject(R.id.adapter_csqd_pm)
    private TextView adapter_csqd_pm;
    @ViewInject(R.id.adapter_csqd_score)
    private TextView adapter_csqd_score;
    @ViewInject(R.id.ivAvatar)
    private ImageView ivAvatar;

    public CSQDViewHolder(View root) {
        super(root);
    }

    public void init(CSQDInfo info){
        ImageLoader.getInstance().displayImage(info.getLogo(), ivAvatar, ImageUtil.getOptions_avater());
        adapter_csqd_name.setText(info.getTeam_name());
        adapter_csqd_dc.setText(info.getDescription()+"");
        adapter_csqd_pm.setText("第"+info.getTeam_id()+"名");
        adapter_csqd_score.setText(info.getIntegral()+"积分");
    }
}
