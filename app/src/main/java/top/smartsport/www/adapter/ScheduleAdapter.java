package top.smartsport.www.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.bean.Schedule;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.StringUtil;

/**
 * Created by gst-pc on 2017/10/6.
 */

public class ScheduleAdapter extends BaseAdapter {

    private Context mContext;
    private List<Schedule> mListSchedule;
    private LayoutInflater mInflater;

    public ScheduleAdapter(Context context, List<Schedule> listSchedule) {
        mContext = context;
        mListSchedule = listSchedule;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mListSchedule.size();
    }

    @Override
    public Object getItem(int position) {
        return mListSchedule.get(position);
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
            convertView = mInflater.inflate(R.layout.schedule_item, null);
            holder.ivHomePic = (ImageView) convertView.findViewById(R.id.iv_home_pic);
            holder.ivAwayPic = (ImageView) convertView.findViewById(R.id.iv_away_pic);
            holder.tvHomeName = (TextView) convertView.findViewById(R.id.tv_home_name);
            holder.tvAwayName = (TextView) convertView.findViewById(R.id.tv_away_name);
            holder.tvCateName = (TextView) convertView.findViewById(R.id.tv_cate_name);
            holder.tvGroupName = (TextView) convertView.findViewById(R.id.tv_group_name);
            holder.tvStartTime = (TextView) convertView.findViewById(R.id.tv_start_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Schedule item = mListSchedule.get(position);
        ImageLoader.getInstance().displayImage(item.getHome_logo(), holder.ivHomePic, ImageUtil.getOptions());
        ImageLoader.getInstance().displayImage(item.getAway_logo(), holder.ivAwayPic, ImageUtil.getOptions());
        holder.tvHomeName.setText(!StringUtil.isEmpty(item.getHome_team()) ? item.getHome_team() : "");
        holder.tvAwayName.setText(!StringUtil.isEmpty(item.getAway_team()) ? item.getAway_team() : "");
        holder.tvCateName.setText(!StringUtil.isEmpty(item.getCate_name()) ? item.getCate_name() : "");
        holder.tvGroupName.setText(!StringUtil.isEmpty(item.getGroup_name()) ? item.getGroup_name() : "");
        holder.tvStartTime.setText(!StringUtil.isEmpty(item.getStart_time()) ? item.getStart_time() : "");
        return convertView;
    }

    class ViewHolder {
        ImageView ivHomePic, ivAwayPic;
        TextView tvHomeName, tvAwayName, tvCateName, tvGroupName, tvStartTime;
    }

}