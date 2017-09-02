package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.News;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/8/9.
 */

public class NewsAdapter extends EntityListAdapter<News,NewsHolder> {
    public NewsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_news;
    }

    @Override
    protected NewsHolder getViewHolder(View root) {
        return new NewsHolder(root);
    }

    @Override
    protected void initViewHolder(NewsHolder newsHolder, int position) {
        newsHolder.init(getItem(position));

    }
}
class NewsHolder extends ViewHolder{
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

    public NewsHolder(View root) {
        super(root);
    }

    public void init(News info){
        ImageLoader.getInstance().displayImage(info.getCover_url(), news_img, ImageUtil.getOptions());
        news_name.setText(info.getTitle());
        news_dis.setText(info.getDescription());
        news_date.setText(info.getCtime());
        news_hint.setText("阅读 "+info.getHits());

    }
}

