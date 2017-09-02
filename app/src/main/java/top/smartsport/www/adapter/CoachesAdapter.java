package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.Coaches;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/8/10.
 */

public class CoachesAdapter extends EntityListAdapter<Coaches,CoachesHolder> {
    public CoachesAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_coaches;
    }

    @Override
    protected CoachesHolder getViewHolder(View root) {
        return new CoachesHolder(root);
    }

    @Override
    protected void initViewHolder(CoachesHolder coachesHolder, int position) {
        coachesHolder.init(getItem(position));

    }
}
class CoachesHolder extends ViewHolder{
    @ViewInject(R.id.coaches_img)
    private ImageView coaches_img;
    @ViewInject(R.id.coaches_name)
    private TextView coaches_name;
    @ViewInject(R.id.coaches_dis)
    private TextView coaches_dis;

    public CoachesHolder(View root) {
        super(root);
    }

    public void init(Coaches info){
        ImageLoader.getInstance().displayImage(info.getHeader_url(), coaches_img, ImageUtil.getOptions());
        coaches_name.setText(info.getName());
        coaches_dis.setText(info.getTeam_name());

    }
}
