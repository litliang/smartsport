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
import top.smartsport.www.bean.TrainingClassBean;


/**
 * Created by bajieaichirou on 2017/8/21 0021.
 */
public class AdapterTrainingDetails extends BaseAdapter {
    private Context context;
    private List<TrainingClassBean> classList;

    public AdapterTrainingDetails(Context context, List<TrainingClassBean> list) {
        this.context = context;
        this.classList = list;
    }

    @Override
    public int getCount() {
        return classList == null ? 0 : classList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_class_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTitleTv.setText(classList.get(position).getClassTitle());
        holder.mPriceTv.setText(classList.get(position).getClassPrice());
        return convertView;
    }

    class ViewHolder {
        private TextView mTitleTv, mPriceTv;
        private ImageView mImg;

        public ViewHolder(View itemView) {
            mTitleTv = (TextView) itemView.findViewById(R.id.class_title_tv);
            mPriceTv = (TextView) itemView.findViewById(R.id.class_price_tv);
            mImg = (ImageView) itemView.findViewById(R.id.class_iv);
        }
    }
}