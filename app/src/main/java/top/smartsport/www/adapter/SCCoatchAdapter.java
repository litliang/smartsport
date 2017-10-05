package top.smartsport.www.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.widget.DefineRoundImageView;

/**
 * Created by ZL on 2017/9/9.
 */

public class SCCoatchAdapter extends BaseAdapter {
    private List<Object> list;

    public void setData(List<Object> l){
            list = new ArrayList<>();
            list.addAll(l);
            notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list != null?list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SCCoatchAdapter.ViewHolder holder;
        if (convertView == null){
            holder = new SCCoatchAdapter.ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coach_list, parent, false);
            holder.pic = (DefineRoundImageView) convertView.findViewById(R.id.iv_head_icon);
            holder.name = (TextView) convertView.findViewById(R.id.tv_coach_name);
            holder.team = (TextView) convertView.findViewById(R.id.tv_team);
            holder.collect = (ImageView) convertView.findViewById(R.id.iv_collect);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        }else {
            holder = (SCCoatchAdapter.ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView team;
        DefineRoundImageView pic;
        ImageView collect;
    }
}
