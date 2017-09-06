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
import top.smartsport.www.bean.CoachInfoCourse;
import top.smartsport.www.utils.ImageUtil;

/**
 * Created by zl on 2017/8/24.
 */

public class TrainningAdapter extends BaseAdapter {
    private List<CoachInfoCourse> list;
    public void setData(List<CoachInfoCourse> courses){
        list = new ArrayList<>();
        list.addAll(courses);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list !=null?list.size():0;
    }

    @Override
    public CoachInfoCourse getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CoachInfoCourse course = list.get(i);
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trainning_list_item, viewGroup, false);
            holder.pic = (ImageView) view.findViewById(R.id.iv_pic);
            holder.title = (TextView) view.findViewById(R.id.tv_title);
            holder.time = (TextView) view.findViewById(R.id.tv_date);
            holder.address = (TextView) view.findViewById(R.id.tv_address);
            holder.tag = (TextView) view.findViewById(R.id.tv_tag);
            holder.price = (TextView) view.findViewById(R.id.tv_price);
            view.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(view);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(course.getTitle());
        holder.time.setText(course.getStart_time());
        holder.address.setText(course.getAddress());
        holder.tag.setText("U"+course.getLevel());
        holder.price.setText("¥"+course.getSell_price());
        ImageLoader.getInstance().displayImage(course.getCover_url(), holder.pic, ImageUtil.getOptions());
        return view;
    }

    class ViewHolder {
        TextView title;
        TextView time;
        TextView address;
        TextView tag;
        TextView price;
        ImageView pic;
    }
}
