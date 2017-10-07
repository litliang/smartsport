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

import app.base.MapConf;
import app.base.action.ViewInflater;
import top.smartsport.www.R;
import top.smartsport.www.bean.ZXInfoComment;
import top.smartsport.www.utils.ImageUtil;

/**
 * Created by zl on 2017/8/24.
 */

public class CommentAdapter extends BaseAdapter {
    private List<ZXInfoComment> list;
    public void setData(List<ZXInfoComment> commentList){
        list = new ArrayList<>();
        list.addAll(commentList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list !=null?list.size():0;
    }

    @Override
    public ZXInfoComment getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        ZXInfoComment comment = list.get(i);
        if (view == null){
            holder = new ViewHolder();
            view = new ViewInflater(viewGroup.getContext()).inflate(R.layout.item_comment, null);
            holder.pic = (ImageView) view.findViewById(R.id.iv_head_icon);
            holder.time = (TextView) view.findViewById(R.id.tv_comment_time);
            holder.describe = (TextView) view.findViewById(R.id.tv_comment);
            holder.name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(view);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        MapConf.with(view.getContext()).setView(comment.getHeader_url(),holder.pic);
//        ImageLoader.getInstance().displayImage(comment.getHeader_url(), holder.pic, ImageUtil.getOptions(), ImageUtil.getImageLoadingListener());
        holder.name.setText(comment.getUsername());
        holder.describe.setText(comment.getContent());
        holder.time.setText(comment.getComment_time());
        return view;
    }

    class ViewHolder {
        TextView name;
        TextView time;
        ImageView pic;
        TextView describe;
    }
}
