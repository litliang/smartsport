package top.smartsport.www.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.listener.OnClickThrottleListener;


public class PackageAdapter extends BaseAdapter {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PackageAdapter.ViewHolder holder;
        if (convertView == null){
            holder = new PackageAdapter.ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_item, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.title_tv);
            holder.content = (TextView) convertView.findViewById(R.id.content_tv);
            holder.sell_price = (TextView) convertView.findViewById(R.id.sell_price_tv);
            holder.iv_check = (CheckBox) convertView.findViewById(R.id.iv_check);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        }else {
            holder = (PackageAdapter.ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView content;
        TextView sell_price;
        CheckBox iv_check;
    }


}
