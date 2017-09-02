package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.HDZXInfo;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/8/13.
 */

public class HDZXAdapter extends EntityListAdapter<HDZXInfo,HDZXHoder> {
    public HDZXAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_news;
    }

    @Override
    protected HDZXHoder getViewHolder(View root) {
        return new HDZXHoder(root);
    }

    @Override
    protected void initViewHolder(HDZXHoder hdzxHoder, int position) {
        hdzxHoder.init(getItem(position));
    }
}
class HDZXHoder extends ViewHolder{
    @ViewInject(R.id.news_img)
    private ImageView news_img;
    @ViewInject(R.id.news_name)
    private TextView news_name;
    @ViewInject(R.id.news_date)
    private TextView news_date;
    @ViewInject(R.id.news_dis)
    private TextView news_dis;
    @ViewInject(R.id.news_hint)
    private TextView news_hint;
    public HDZXHoder(View root) {
        super(root);
    }

    public void init(HDZXInfo info){
        ImageLoader.getInstance().displayImage(info.getCover_url(), news_img, ImageUtil.getOptions());
        news_name.setText(info.getTitle());
        news_dis.setText(info.getDescription());
        news_date.setText(info.getCtime());
        news_hint.setText("阅读 "+info.getHits());
    }

}

