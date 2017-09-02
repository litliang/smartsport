package top.smartsport.www.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhy.autolayout.utils.AutoUtils;
import top.smartsport.www.R;
import top.smartsport.www.widget.TeamMemberView;

/**
 * Created by Administrator on 2017/8/17.
 */

public class TeamMemberAdapter extends BaseAdapter {
    private int count;


    public void addMember(){
        count++;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return count;

    }

    @Override
    public Object getItem(int postion) {
        return postion;
    }

    @Override
    public long getItemId(int postion) {
        return postion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_list,parent,false);
            viewHolder.viewMember = (TeamMemberView) convertView.findViewById(R.id.view_member);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.viewMember.setLaber("球员"+(position+1));
        return convertView;
    }

    class ViewHolder {
        TeamMemberView viewMember;
    }
}
