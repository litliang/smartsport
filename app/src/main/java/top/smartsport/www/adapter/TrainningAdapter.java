package top.smartsport.www.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import top.smartsport.www.R;

/**
 * Created by zl on 2017/8/24.
 */

public class TrainningAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trainning_list_item, viewGroup, false);
            holder.pic = (ImageView) view.findViewById(R.id.iv_pic);
            holder.title = (TextView) view.findViewById(R.id.tv_title);
            holder.time = (TextView) view.findViewById(R.id.tv_time);
            holder.address = (TextView) view.findViewById(R.id.tv_address);
            holder.tag = (TextView) view.findViewById(R.id.tv_tag);
            holder.price = (TextView) view.findViewById(R.id.tv_price);
            view.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(view);
        }else {
            holder = (ViewHolder) view.getTag();
        }
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
