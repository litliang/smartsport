package top.smartsport.www.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.bean.ShareItem;

/**
 * 分享
 */
public class ShareAdapter extends BaseAdapter {

    private Context mContext;
    private List<ShareItem> mListShare;
    private LayoutInflater mInflater;

    public ShareAdapter(Context context, List<ShareItem> listShare) {
        mContext = context;
        mListShare = listShare;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mListShare.size();
    }

    @Override
    public Object getItem(int position) {
        return mListShare.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(null == convertView) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.share_item, null);
            holder.ivSharePic = (ImageView) convertView.findViewById(R.id.iv_share_pic);
            holder.tvShareName = (TextView) convertView.findViewById(R.id.tv_share_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShareItem item = mListShare.get(position);
        holder.tvShareName.setText(item.getName());
        holder.ivSharePic.setImageResource(item.getPicId());
        return convertView;
    }

    class ViewHolder {
        ImageView ivSharePic;
        TextView tvShareName;
    }

}
