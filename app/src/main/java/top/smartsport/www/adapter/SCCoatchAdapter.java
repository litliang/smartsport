package top.smartsport.www.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.StringUtil;
import top.smartsport.www.widget.DefineRoundImageView;

/**
 * Created by ZL on 2017/9/9.
 */

public class SCCoatchAdapter extends BaseAdapter {
    private List<Object> list;
    private boolean mIsShowCollect;

    public SCCoatchAdapter(boolean isShowCollect) {
        mIsShowCollect = isShowCollect;
    }

    public void setData(List<Object> l){
            list = new ArrayList<>();
            list.addAll(l);
//        LogUtil.d("------setData----value--------->" + value);
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
        Object obj = list.get(position);
        Map<String, Object> map = (Map<String, Object>) obj;
        String headerUrl = (String) map.get("header_url");
        String name = (String) map.get("name");
        holder.name.setText(name);
        String teamName = (String) map.get("team_name");
        holder.team.setText(teamName);
        if(mIsShowCollect) {
            holder.collect.setVisibility(View.VISIBLE);
            String collectStatus = (String) map.get("collect_status");
            if(!StringUtil.isEmpty(collectStatus) && collectStatus.equals("0")) {
                holder.collect.setImageResource(R.mipmap.icon_collect);
            } else {
                holder.collect.setImageResource(R.mipmap.collect_checked);
            }
            ImageLoader.getInstance().displayImage(headerUrl, holder.pic, ImageUtil.getOptions());
            final String id = (String) map.get("id");
            holder.collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object obj = list.get(position);
                    Map<String, Object> map = (Map<String, Object>) obj;
                    String collectStatus = (String) map.get("collect_status");
                    boolean unFav = true;
                    if(!StringUtil.isEmpty(collectStatus) && collectStatus.equals("1")) {
                        unFav = false;
                    }
                    favImpl(v, unFav, "5", id, position);
                }
            });
        } else {
            holder.collect.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView team;
        DefineRoundImageView pic;
        ImageView collect;
    }

    public void favImpl(final View view, final boolean unfav, String type, String id, final int pos) {
        if (unfav) {
            BaseActivity.callHttp(MapBuilder.build().add("action", "collect").add("type", type + "").add("source_id", id).get(), new FunCallback() {
                @Override
                public void onSuccess(Object result, List object) {
                    ((ImageView) view).setImageResource(R.mipmap.collect_checked);
                    Toast.makeText(view.getContext(), "已收藏", Toast.LENGTH_SHORT).show();
                    refreshData(pos, "1");
                }

                @Override
                public void onFailure(Object result, List object) {
LogUtil.d("-----------result----------->" + result);
                }

                @Override
                public void onCallback(Object result, List object) {
                    LogUtil.d("-----------result----------->" + result);
                }
            });
        } else {
            BaseActivity.callHttp(MapBuilder.build().add("action", "cancelCollect").add("type", type + "").add("id", id).get(), new FunCallback() {
                @Override
                public void onSuccess(Object result, List object) {
                    ((ImageView) view).setImageResource(R.mipmap.icon_collect);
                    Toast.makeText(view.getContext(), "已为您取消", Toast.LENGTH_SHORT).show();
                    refreshData(pos, "0");
                }

                @Override
                public void onFailure(Object result, List object) {
                    LogUtil.d("-----------result----------->" + result);
                }

                @Override
                public void onCallback(Object result, List object) {
                    LogUtil.d("-----------result----------->" + result);
                }
            });

        }
    }

    private void refreshData(int pos, String value) {
        Object obj = list.get(pos);
        Map<String, Object> map = (Map<String, Object>) obj;
        map.put("collect_status", value);
    }

}
