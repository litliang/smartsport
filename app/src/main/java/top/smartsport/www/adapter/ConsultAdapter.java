package top.smartsport.www.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.bean.ZXInfoNews;
import top.smartsport.www.utils.ImageUtil;

/**
 * Created by zl on 2017/8/24.
 */

public class ConsultAdapter extends BaseAdapter {
    private List<ZXInfoNews> list;
    public void setData(List<ZXInfoNews> newsList){
        list = new ArrayList<>();
        list.addAll(newsList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list !=null?list.size():0;
    }

    @Override
    public ZXInfoNews getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        ZXInfoNews news = list.get(i);
        if (view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.consult_list_item, viewGroup, false);
            holder.pic = (ImageView) view.findViewById(R.id.iv_pic);
            holder.title = (TextView) view.findViewById(R.id.tv_title);
            holder.time = (TextView) view.findViewById(R.id.tv_date);
            holder.action = (TextView) view.findViewById(R.id.tv_action);
            holder.describe = (TextView) view.findViewById(R.id.tv_describe);
            holder.read = (TextView) view.findViewById(R.id.tv_read_count);
            view.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(view);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        ImageLoader.getInstance().displayImage(news.getCover_url(), holder.pic, ImageUtil.getOptions(), ImageUtil.getImageLoadingListener());
        holder.title.setText(news.getTitle());
        holder.describe.setText(news.getDescription());
        holder.time.setText(news.getCtime());
        holder.action.setText(news.getCate_name());
        holder.read.setText("阅读 " + news.getHits());
        return view;
    }

    class ViewHolder {
        TextView title;
        TextView time;
        TextView action;
        ImageView pic;
        TextView describe;
        TextView read;
    }
}
