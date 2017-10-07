package top.smartsport.www.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.smartsport.www.R;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.widget.utils.RoundImageView;

/**
 * Created by gst-pc on 2017/10/7.
 */
public class MessageAdapter extends BaseAdapter {
    private List list;

    public void setData(List l){
        list = new ArrayList();
        list.addAll(l);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int postion) {
        return list.get(postion);
    }

    @Override
    public long getItemId(int postion) {
        return postion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageAdapter.ViewHolder viewHolder ;
        if (convertView == null){
            viewHolder = new MessageAdapter.ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
            viewHolder.ivHeader = (RoundImageView) convertView.findViewById(R.id.iv_head_icon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.content);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
        } else {
            viewHolder = (MessageAdapter.ViewHolder) convertView.getTag();
        }
        Object obj = list.get(position);
        Map<String, Object> map = (Map<String, Object>) obj;
        String headerUrl = (String) map.get("header_url");
        String title = (String) map.get("title");
        viewHolder.tvTitle.setText(title);
        String content = (String) map.get("content");
        viewHolder.tvContent.setText(content);
        viewHolder.tvTime.setText((String) map.get("ctime"));
        ImageLoader.getInstance().displayImage(headerUrl, viewHolder.ivHeader, ImageUtil.getOptions());
        return convertView;
    }

    class ViewHolder {
        RoundImageView ivHeader;
        TextView tvTitle, tvContent, tvTime;
    }
}
