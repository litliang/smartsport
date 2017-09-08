package top.smartsport.www.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.bean.Coaches;
import top.smartsport.www.listener.OnRecyclerViewItemListener;
import top.smartsport.www.utils.ImageUtil;

/**
 * Created by Administrator on 2017/8/24.
 */

public class CoachAdapter extends RecyclerView.Adapter<CoachAdapter.ViewHolder>{
    private List<Coaches> list;
    private OnRecyclerViewItemListener mOnRecyclerViewItemListener;
    public void setOnRecyclerViewItemListener(OnRecyclerViewItemListener listener) {
        mOnRecyclerViewItemListener = listener;
    }
    public void setData(List<Coaches> coaches){
        list = new ArrayList<>();
        list.addAll(coaches);
        notifyDataSetChanged();
    }
    @Override
    public CoachAdapter.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coach_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(CoachAdapter.ViewHolder holder, final int position) {
        Coaches coach = list.get(position);
        holder.name.setText(coach.getName());
        holder.team.setText(coach.getTeam_name());
        ImageLoader.getInstance().displayImage(coach.getHeader_url(), holder.pic, ImageUtil.getOptions(), ImageUtil.getImageLoadingListener());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRecyclerViewItemListener.onItemClickListener(view,position);
            }
        });
    }

    public Coaches getItem(int position){
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list !=null?list.size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView team;
        ImageView pic;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            pic = (ImageView) itemView.findViewById(R.id.iv_pic);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            team = (TextView) itemView.findViewById(R.id.tv_team);
        }
    }
}
