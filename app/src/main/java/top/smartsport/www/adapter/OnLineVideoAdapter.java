package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.OnLineVideoInfo;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by bajieaichirou on 17/9/9.
 */
public class OnLineVideoAdapter extends EntityListAdapter<OnLineVideoInfo,OnLineViedoViewHolder> {

    public OnLineVideoAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_online_video_item;
    }

    @Override
    protected OnLineViedoViewHolder getViewHolder(View root) {
        return new OnLineViedoViewHolder(root);
    }

    @Override
    protected void initViewHolder(OnLineViedoViewHolder viewHolder, int position) {
        viewHolder.init(getItem(position));
    }
}
class OnLineViedoViewHolder extends ViewHolder {
    @ViewInject(R.id.online_item_iv)
    ImageView mIv;
    @ViewInject(R.id.online_item_name_tv)
    TextView mNameTv;
    @ViewInject(R.id.online_item_grade_tv)
    TextView mGradeTv;
    @ViewInject(R.id.online_item_country_tv)
    TextView mCountryTv;
    @ViewInject(R.id.online_item_sport_tv)
    TextView mSportTv;
    @ViewInject(R.id.online_watched_count_tv)
    TextView mCountTv;

    public OnLineViedoViewHolder(View root) {
        super(root);
    }

    public void init(OnLineVideoInfo info){
        mNameTv.setText(info.getViedoName());
    }
}
