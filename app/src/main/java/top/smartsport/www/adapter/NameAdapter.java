package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import org.xutils.view.annotation.ViewInject;

import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.PlaceItemInfo;
import top.smartsport.www.utils.ViewHolder;

/**
 * @author：suntongfu
 * @time: 2016/8/8 16:18
 * @describe:
 */
public class NameAdapter extends EntityListAdapter<PlaceItemInfo,CityViewHolder> {
    private List<PlaceItemInfo> mDatas ;
    private Context context;

    public NameAdapter(Context context) {
        super(context);
    }

    public NameAdapter(Context context, List<PlaceItemInfo> list) {
        super(context, list);
        this.context=context;
        this.mDatas=list;
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.layout_placeitem;
    }

    @Override
    protected CityViewHolder getViewHolder(View root) {
        return new CityViewHolder(root);
    }

    @Override
    protected void initViewHolder(CityViewHolder viewHolder, int position) {
        if(position==0){
            viewHolder.initCity(context,getItem(position),null,position);
        }else{
        viewHolder.initCity(context,getItem(position),getItem(position-1),position);
        }
    }
}

class CityViewHolder extends ViewHolder {
    @ViewInject(R.id.tv_pinyin)
    private TextView tvPinyin;
    @ViewInject(R.id.tv_name)
    private TextView tvName;

    private String mFirstLetter;
    private String mPreFirstLetter;
    public CityViewHolder(View root) {
        super(root);
    }

    public void initCity(Context context, PlaceItemInfo item,PlaceItemInfo preItem,int position) {
        mFirstLetter = String.valueOf(item.mPinyin.charAt(0));
        // 对于0号索引, 特殊处理
        if (position == 0) {
            mPreFirstLetter = "-"; // 只要和 A 不一样就可以
        } else {
            mPreFirstLetter = String.valueOf(preItem.mPinyin.charAt(0));
        }
        // 根据当前首字母和之前条目的首字母作对比, 决定是否显示字母条
        if (mFirstLetter.equals(mPreFirstLetter)) {
            tvPinyin.setVisibility(View.GONE);
        } else {
            tvPinyin.setVisibility(View.VISIBLE);
        }
        tvName.setText(item.mName);
        tvPinyin.setText(String.valueOf(item.mPinyin.charAt(0)));
    }
}