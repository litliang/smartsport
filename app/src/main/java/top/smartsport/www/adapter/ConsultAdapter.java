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

public class ConsultAdapter extends BaseAdapter {

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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.consult_list_item, viewGroup, false);
            holder.pic = (ImageView) view.findViewById(R.id.iv_pic);
            holder.title = (TextView) view.findViewById(R.id.tv_title);
            holder.time = (TextView) view.findViewById(R.id.tv_time);
            holder.action = (TextView) view.findViewById(R.id.tv_action);
            holder.describe = (TextView) view.findViewById(R.id.tv_describe);
            holder.read = (TextView) view.findViewById(R.id.tv_read_count);
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
        TextView action;
        ImageView pic;
        TextView describe;
        TextView read;
    }
}
