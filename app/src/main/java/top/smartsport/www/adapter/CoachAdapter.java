package top.smartsport.www.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import top.smartsport.www.R;

/**
 * Created by Administrator on 2017/8/24.
 */

public class CoachAdapter extends RecyclerView.Adapter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coach_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
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
